package mainstreet.application;

import mainstreet.merchantaccount.MerchantAccountEstablishedOccurrence;
import mainstreet.merchantaccount.MerchantAccountEstablishedPublicationSource;
import mainstreet.semantic.event.EventReactionContractDefinition;
import mainstreet.semantic.event.EventReactionContractRegistrySnapshot;
import mainstreet.semantic.event.EventReactionIdentity;
import mainstreet.semantic.event.MerchantEventReactionReceipt;
import mainstreet.semantic.event.MerchantEventReactionStore;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bounded post-commit publication progression for MerchantAccountEstablished.
 *
 * <p>The worker reconstructs only owner-registered historical occurrences,
 * discovers the currently supported registered reaction responsibility, durably
 * accepts that responsibility, and only then records technical publication.
 * It never executes or acknowledges the consumer reaction.</p>
 *
 * <p>Authority: MS-PROT-026 v1.1,
 * designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md,
 * §10 — Publication Failure After Commit; §11 — Event Publication Is Not Reaction Completion;
 * §12 — Publication Responsibility; §14 — Registration Is Required;
 * §16 — Event Reaction Identity; §23 — Reaction Deduplication;
 * §32 — Historical Affinity.</p>
 */
public final class MerchantAccountEstablishedPublicationWorker {
    private final MerchantAccountEstablishedPublicationSource publicationSource;
    private final Supplier<EventReactionContractRegistrySnapshot> currentReactionContracts;
    private final MerchantEventReactionStore reactionStore;
    private final Clock clock;

    public MerchantAccountEstablishedPublicationWorker(
            MerchantAccountEstablishedPublicationSource publicationSource,
            Supplier<EventReactionContractRegistrySnapshot> currentReactionContracts,
            MerchantEventReactionStore reactionStore,
            Clock clock) {
        this.publicationSource = Objects.requireNonNull(
                publicationSource, "publicationSource");
        this.currentReactionContracts = Objects.requireNonNull(
                currentReactionContracts, "currentReactionContracts");
        this.reactionStore = Objects.requireNonNull(reactionStore, "reactionStore");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public int runOnce(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be positive");
        }

        Instant now = clock.instant();
        int published = 0;
        for (MerchantAccountEstablishedPublicationSource.PendingPublication pending
                : publicationSource.pendingPublications(limit)) {
            var occurrence = pending.registeredOccurrence();
            if (occurrence.isEmpty()) {
                continue;
            }

            establishReactionResponsibility(occurrence.orElseThrow(), now);
            if (publicationSource.recordPublished(
                    pending.publicationIntentIdentifier(), now)) {
                published++;
            }
        }
        return published;
    }

    private void establishReactionResponsibility(
            MerchantAccountEstablishedOccurrence occurrence,
            Instant acceptedAt) {
        EventReactionContractRegistrySnapshot registry = Objects.requireNonNull(
                currentReactionContracts.get(), "current Event Reaction Contract registry");

        Set<EventReactionContractDefinition> sourceContracts = registry.contracts().stream()
                .filter(contract -> contract.sourceEventContract()
                        .equals(occurrence.contractAffinity()))
                .collect(Collectors.toUnmodifiableSet());
        if (!sourceContracts.equals(Set.of(StandingFreeEventReactionContract.DEFINITION))) {
            throw new IllegalStateException(
                    "MerchantAccountEstablished reaction discovery is not fully supported by this worker");
        }

        EventReactionContractDefinition currentContract = registry
                .contract(
                        StandingFreeEventReactionContract.AFFINITY,
                        occurrence.contractAffinity())
                .filter(StandingFreeEventReactionContract.DEFINITION::equals)
                .orElseThrow(() -> new IllegalStateException(
                        "Standing Free Event Reaction Contract is not currently registered"));

        EventReactionIdentity reactionIdentity = new EventReactionIdentity(
                occurrence.eventIdentity(), currentContract.identity());
        MerchantEventReactionReceipt candidate = new MerchantEventReactionReceipt(
                reactionIdentity,
                StandingFreeEventReactionContract.AFFINITY,
                occurrence.contractAffinity(),
                occurrence.fact().merchantScope(),
                StandingFreeEventReactionContract
                        .downstreamLogicalIntentReference(occurrence),
                acceptedAt);
        MerchantEventReactionReceipt accepted = Objects.requireNonNull(
                reactionStore.accept(candidate), "accepted reaction receipt");
        if (!accepted.sameResponsibility(candidate)) {
            throw new IllegalStateException(
                    "Accepted reaction receipt belongs to another responsibility");
        }
    }
}
