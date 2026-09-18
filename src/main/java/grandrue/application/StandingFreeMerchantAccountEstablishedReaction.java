package grandrue.application;

import grandrue.application.StandingFreeEventReactionContract;
import grandrue.application.StandingFreeFromMerchantAccountEstablishedHandler;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrenceAuthority;
import grandrue.runtime.ScheduledEventReactionExecutionAuthority;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.event.EventReactionAcknowledgement;
import mainstreet.semantic.event.EventReactionContractDefinition;
import mainstreet.semantic.event.EventReactionContractRegistrySnapshot;
import mainstreet.semantic.event.EventReactionIdentity;
import mainstreet.semantic.event.MerchantEventReactionReceipt;
import mainstreet.semantic.event.MerchantEventReactionStore;

import java.time.Clock;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Trusted post-commit composition for Commercial's mandatory Standing Free
 * reaction to Merchant Account establishment.
 *
 * <p>Each delivery independently resolves Merchant Account's exact durable
 * occurrence, the current registered reaction contract and Commercial's own
 * scheduled principal before recording responsibility or invoking the
 * Commercial owner operation. The receipt records progression only; it never
 * becomes permission for a retry.</p>
 *
 * <p>Authority: MS-PROT-026 v1.1, §21 — Authoritative Downstream Mutation,
 * §23 — Reaction Deduplication, §24 — Downstream Idempotency Remains Separate,
 * §32 — Historical Affinity, §47 — Event Receipt Does Not Carry Actor
 * Authority and §48 — Merchant Scope; MS-PROT-056 v1.6, §3 — Standing Free
 * baseline fact and §5 — Idempotency and duplicate delivery.</p>
 */
public final class StandingFreeMerchantAccountEstablishedReaction {
    private final MerchantAccountEstablishedOccurrenceAuthority sourceAuthority;
    private final Supplier<EventReactionContractRegistrySnapshot> currentContracts;
    private final ScheduledEventReactionExecutionAuthority executionAuthority;
    private final MerchantEventReactionStore reactionStore;
    private final StandingFreeFromMerchantAccountEstablishedHandler ownerOperation;
    private final Clock clock;

    public StandingFreeMerchantAccountEstablishedReaction(
            MerchantAccountEstablishedOccurrenceAuthority sourceAuthority,
            Supplier<EventReactionContractRegistrySnapshot> currentContracts,
            ScheduledEventReactionExecutionAuthority executionAuthority,
            MerchantEventReactionStore reactionStore,
            StandingFreeFromMerchantAccountEstablishedHandler ownerOperation,
            Clock clock) {
        this.sourceAuthority = Objects.requireNonNull(sourceAuthority, "sourceAuthority");
        this.currentContracts = Objects.requireNonNull(currentContracts, "currentContracts");
        this.executionAuthority = Objects.requireNonNull(executionAuthority, "executionAuthority");
        this.reactionStore = Objects.requireNonNull(reactionStore, "reactionStore");
        this.ownerOperation = Objects.requireNonNull(ownerOperation, "ownerOperation");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public StandingFreeBaseline react(MerchantAccountEstablishedOccurrence suppliedOccurrence) {
        Objects.requireNonNull(suppliedOccurrence, "suppliedOccurrence");
        MerchantAccountEstablishedOccurrence authoritativeOccurrence = sourceAuthority
                .authoritativeOccurrence(suppliedOccurrence)
                .filter(suppliedOccurrence::equals)
                .orElseThrow(() -> new IllegalStateException(
                        "MerchantAccountEstablished occurrence is not authoritative"));

        EventReactionContractDefinition currentContract = Objects.requireNonNull(
                        currentContracts.get(), "current Event Reaction Contract registry")
                .contract(StandingFreeEventReactionContract.AFFINITY,
                        authoritativeOccurrence.contractAffinity())
                .filter(StandingFreeEventReactionContract.DEFINITION::equals)
                .orElseThrow(() -> new IllegalStateException(
                        "Standing Free Event Reaction Contract is not currently registered"));

        TrustedExecutionContext executionContext = Objects.requireNonNull(
                executionAuthority.establish(currentContract,
                        authoritativeOccurrence.fact().merchantScope()),
                "scheduled Event Reaction execution context");
        if (!executionContext.merchantScope().equals(
                authoritativeOccurrence.fact().merchantScope())) {
            throw new IllegalStateException(
                    "Scheduled Event Reaction authority returned a different Merchant Scope");
        }

        EventReactionIdentity reactionIdentity = new EventReactionIdentity(
                authoritativeOccurrence.eventIdentity(), currentContract.identity());
        MerchantEventReactionReceipt candidateReceipt = new MerchantEventReactionReceipt(
                reactionIdentity,
                StandingFreeEventReactionContract.AFFINITY,
                authoritativeOccurrence.contractAffinity(),
                executionContext.merchantScope(),
                StandingFreeEventReactionContract
                        .downstreamLogicalIntentReference(authoritativeOccurrence),
                clock.instant());
        MerchantEventReactionReceipt acceptedReceipt = Objects.requireNonNull(
                reactionStore.accept(candidateReceipt), "accepted reaction receipt");
        if (!acceptedReceipt.sameResponsibility(candidateReceipt)) {
            throw new IllegalStateException(
                    "Accepted reaction receipt belongs to another responsibility");
        }

        StandingFreeBaseline outcome = Objects.requireNonNull(
                ownerOperation.handle(authoritativeOccurrence.fact()),
                "Standing Free owner operation outcome");
        EventReactionAcknowledgement acknowledgement = Objects.requireNonNull(
                reactionStore.acknowledge(new EventReactionAcknowledgement(
                        reactionIdentity, outcome.baselineIdentity(), clock.instant())),
                "reaction acknowledgement");
        if (!acknowledgement.identity().equals(reactionIdentity)
                || !acknowledgement.outcomeReference().equals(outcome.baselineIdentity())) {
            throw new IllegalStateException(
                    "Reaction acknowledgement belongs to another outcome");
        }
        return outcome;
    }
}
