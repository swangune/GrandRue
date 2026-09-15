package mainstreet.application;

import mainstreet.merchantaccount.MerchantAccountEstablished;
import mainstreet.merchantaccount.MerchantAccountEstablishedEventContract;
import mainstreet.merchantaccount.MerchantAccountEstablishedOccurrence;
import mainstreet.merchantaccount.MerchantAccountEstablishedPublicationSource;
import mainstreet.semantic.event.EventReactionAcknowledgement;
import mainstreet.semantic.event.EventReactionContractAffinity;
import mainstreet.semantic.event.EventReactionContractDefinition;
import mainstreet.semantic.event.EventReactionContractIdentity;
import mainstreet.semantic.event.EventReactionContractRegistrySnapshot;
import mainstreet.semantic.event.EventReactionIdentity;
import mainstreet.semantic.event.EventReactionTargetReference;
import mainstreet.semantic.event.MerchantEventReactionReceipt;
import mainstreet.semantic.event.MerchantEventReactionStore;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantAccountEstablishedPublicationWorkerTest {
    private static final Instant OCCURRED_AT = Instant.parse("2026-09-15T08:00:00Z");
    private static final Instant PUBLISHED_AT = Instant.parse("2026-09-15T08:01:00Z");
    private static final MerchantAccountEstablishedOccurrence OCCURRENCE =
            new MerchantAccountEstablishedOccurrence(
                    "merchant-account-established-event/publication-1",
                    MerchantAccountEstablishedEventContract.AFFINITY,
                    new MerchantAccountEstablished(
                            "establishment-1",
                            new MerchantScope("merchant-1"),
                            "request-1",
                            OCCURRED_AT));

    @Test
    void registered_occurrence_is_discovered_as_c4d1_responsibility_before_technical_publication() {
        var source = sourceFor(Optional.of(OCCURRENCE));
        var reactions = new RecordingReactionStore(false);

        int progressed = worker(source, StandingFreeEventReactionContract::registry, reactions)
                .runOnce(10);

        assertEquals(1, progressed);
        assertEquals(PUBLISHED_AT, source.published.get("publication-1"));
        assertEquals(1, reactions.accepted.size());
        MerchantEventReactionReceipt receipt = reactions.accepted.getFirst();
        assertEquals(new EventReactionIdentity(
                OCCURRENCE.eventIdentity(), StandingFreeEventReactionContract.IDENTITY),
                receipt.identity());
        assertEquals(StandingFreeEventReactionContract.AFFINITY, receipt.contractAffinity());
        assertEquals(OCCURRENCE.contractAffinity(), receipt.sourceEventContract());
        assertEquals(OCCURRENCE.fact().merchantScope(), receipt.merchantScope());
        assertEquals(
                StandingFreeEventReactionContract.downstreamLogicalIntentReference(OCCURRENCE),
                receipt.downstreamLogicalIntentReference());
        assertEquals(PUBLISHED_AT, receipt.acceptedAt());
    }

    @Test
    void unknown_historical_affinity_stays_pending_and_does_not_create_reaction() {
        var source = sourceFor(Optional.empty());
        var reactions = new RecordingReactionStore(false);

        int progressed = worker(source, StandingFreeEventReactionContract::registry, reactions)
                .runOnce(10);

        assertEquals(0, progressed);
        assertTrue(source.published.isEmpty());
        assertTrue(reactions.accepted.isEmpty());
    }

    @Test
    void missing_current_reaction_registration_fails_closed_before_publication() {
        var source = sourceFor(Optional.of(OCCURRENCE));
        var reactions = new RecordingReactionStore(false);
        var emptyRegistry = new EventReactionContractRegistrySnapshot(
                StandingFreeEventReactionContract.AFFINITY.semanticRegistryReleaseIdentifier(),
                List.of());

        assertThrows(IllegalStateException.class,
                () -> worker(source, () -> emptyRegistry, reactions).runOnce(10));
        assertTrue(source.published.isEmpty());
        assertTrue(reactions.accepted.isEmpty());
    }

    @Test
    void expanded_current_registration_fails_closed_until_a_bounded_intent_mapping_exists() {
        var source = sourceFor(Optional.of(OCCURRENCE));
        var reactions = new RecordingReactionStore(false);
        EventReactionContractDefinition extra = new EventReactionContractDefinition(
                new EventReactionContractIdentity("projection", "merchant-account-established-refresh"),
                MerchantAccountEstablishedEventContract.AFFINITY,
                "purpose",
                "scope",
                "mode",
                new EventReactionTargetReference("projection", "refresh"),
                "principal",
                "duplicate",
                "ordering",
                "supersession",
                Set.of("data"),
                "retry",
                "intent");
        var expandedRegistry = new EventReactionContractRegistrySnapshot(
                StandingFreeEventReactionContract.AFFINITY.semanticRegistryReleaseIdentifier(),
                List.of(StandingFreeEventReactionContract.DEFINITION, extra));

        assertThrows(IllegalStateException.class,
                () -> worker(source, () -> expandedRegistry, reactions).runOnce(10));
        assertTrue(source.published.isEmpty());
        assertTrue(reactions.accepted.isEmpty());
    }

    @Test
    void reaction_receipt_failure_leaves_publication_pending() {
        var source = sourceFor(Optional.of(OCCURRENCE));
        var reactions = new RecordingReactionStore(true);

        assertThrows(IllegalStateException.class,
                () -> worker(source, StandingFreeEventReactionContract::registry, reactions)
                        .runOnce(10));

        assertTrue(source.published.isEmpty());
    }

    @Test
    void concurrent_publication_completion_does_not_manufacture_consumer_acknowledgement() {
        var source = sourceFor(Optional.of(OCCURRENCE));
        source.recordPublishedResult = false;
        var reactions = new RecordingReactionStore(false);

        int progressed = worker(source, StandingFreeEventReactionContract::registry, reactions)
                .runOnce(10);

        assertEquals(0, progressed);
        assertEquals(1, reactions.accepted.size());
        assertTrue(source.published.isEmpty());
    }

    private static MerchantAccountEstablishedPublicationWorker worker(
            MerchantAccountEstablishedPublicationSource source,
            java.util.function.Supplier<EventReactionContractRegistrySnapshot> contracts,
            MerchantEventReactionStore reactions) {
        return new MerchantAccountEstablishedPublicationWorker(
                source,
                contracts,
                reactions,
                Clock.fixed(PUBLISHED_AT, ZoneOffset.UTC));
    }

    private static FakePublicationSource sourceFor(
            Optional<MerchantAccountEstablishedOccurrence> occurrence) {
        return new FakePublicationSource(List.of(
                new MerchantAccountEstablishedPublicationSource.PendingPublication(
                        "publication-1", occurrence)));
    }

    private static final class FakePublicationSource
            implements MerchantAccountEstablishedPublicationSource {
        private final List<PendingPublication> pending;
        private final Map<String, Instant> published = new HashMap<>();
        private boolean recordPublishedResult = true;

        private FakePublicationSource(List<PendingPublication> pending) {
            this.pending = List.copyOf(pending);
        }

        @Override
        public List<PendingPublication> pendingPublications(int limit) {
            return pending.stream().limit(limit).toList();
        }

        @Override
        public boolean recordPublished(String publicationIntentIdentifier, Instant publishedAt) {
            if (!recordPublishedResult) {
                return false;
            }
            published.put(publicationIntentIdentifier, publishedAt);
            return true;
        }
    }

    private static final class RecordingReactionStore implements MerchantEventReactionStore {
        private final boolean failOnAccept;
        private final List<MerchantEventReactionReceipt> accepted = new ArrayList<>();

        private RecordingReactionStore(boolean failOnAccept) {
            this.failOnAccept = failOnAccept;
        }

        @Override
        public MerchantEventReactionReceipt accept(MerchantEventReactionReceipt candidate) {
            if (failOnAccept) {
                throw new IllegalStateException("reaction store unavailable");
            }
            accepted.add(candidate);
            return candidate;
        }

        @Override
        public Optional<MerchantEventReactionReceipt> receipt(EventReactionIdentity identity) {
            return accepted.stream().filter(value -> value.identity().equals(identity)).findFirst();
        }

        @Override
        public List<MerchantEventReactionReceipt> pending(
                EventReactionContractAffinity affinity, int limit) {
            return accepted.stream()
                    .filter(value -> value.contractAffinity().equals(affinity))
                    .limit(limit)
                    .toList();
        }

        @Override
        public EventReactionAcknowledgement acknowledge(EventReactionAcknowledgement candidate) {
            throw new AssertionError("Publication must not acknowledge a consumer reaction");
        }

        @Override
        public Optional<EventReactionAcknowledgement> acknowledgement(
                EventReactionIdentity identity) {
            return Optional.empty();
        }
    }
}
