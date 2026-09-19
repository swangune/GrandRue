package grandrue.publication;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorityBackedOpportunityPublicExposureReadPortTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String OPPORTUNITY = "opportunity-1";

    @Test
    void resolves_window_from_exact_published_revision_not_newer_unpublished_current_revision() {
        StubAuthority authority = new StubAuthority();
        OpportunityPublicationMaterialRevision published = material(
                "revision-1",
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-09-01T09:00:00Z")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-09-30T09:00:00Z")
                ))
        );
        OpportunityPublicationMaterialRevision current = material(
                "revision-2",
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2027-01-01T00:00:00Z")
                )),
                Optional.empty()
        );
        authority.revisions.put(published.revisionIdentity(), published);
        authority.revisions.put(current.revisionIdentity(), current);
        authority.state = OpportunityPublicationState.draft(
                        MERCHANT,
                        OPPORTUNITY,
                        "revision-1"
                )
                .publish("revision-1")
                .revise("revision-1", "revision-2");

        OpportunityPublicExposureEvidence evidence =
                new AuthorityBackedOpportunityPublicExposureReadPort(authority)
                        .currentPublishedExposure(MERCHANT, OPPORTUNITY)
                        .orElseThrow();

        assertEquals("revision-1", evidence.publishedRevisionIdentity());
        assertEquals(published.publishFrom(), evidence.publishFrom());
        assertEquals(published.publishUntil(), evidence.publishUntil());
    }

    @Test
    void draft_and_withdrawn_opportunities_fail_closed_as_public_exposure_evidence() {
        StubAuthority authority = new StubAuthority();
        authority.revisions.put(
                "revision-1",
                material("revision-1", Optional.empty(), Optional.empty())
        );
        AuthorityBackedOpportunityPublicExposureReadPort readPort =
                new AuthorityBackedOpportunityPublicExposureReadPort(authority);

        authority.state = OpportunityPublicationState.draft(
                MERCHANT,
                OPPORTUNITY,
                "revision-1"
        );
        assertTrue(readPort.currentPublishedExposure(MERCHANT, OPPORTUNITY).isEmpty());

        authority.state = authority.state
                .publish("revision-1")
                .withdraw("revision-1");
        assertTrue(readPort.currentPublishedExposure(MERCHANT, OPPORTUNITY).isEmpty());
    }

    @Test
    void concurrent_change_of_published_binding_between_reads_fails_closed() {
        StubAuthority authority = new StubAuthority();
        authority.revisions.put(
                "revision-1",
                material("revision-1", Optional.empty(), Optional.empty())
        );

        OpportunityPublicationState initiallyPublished = OpportunityPublicationState.draft(
                        MERCHANT,
                        OPPORTUNITY,
                        "revision-1"
                )
                .publish("revision-1");
        OpportunityPublicationState concurrentlyRepublished = initiallyPublished
                .revise("revision-1", "revision-2")
                .publish("revision-2");

        authority.state = initiallyPublished;
        authority.stateAfterFirstRead = Optional.of(concurrentlyRepublished);

        assertTrue(
                new AuthorityBackedOpportunityPublicExposureReadPort(authority)
                        .currentPublishedExposure(MERCHANT, OPPORTUNITY)
                        .isEmpty()
        );
    }

    @Test
    void mismatched_revision_affinity_from_authority_is_rejected() {
        StubAuthority authority = new StubAuthority();
        authority.state = OpportunityPublicationState.draft(
                        MERCHANT,
                        OPPORTUNITY,
                        "revision-1"
                )
                .publish("revision-1");
        authority.revisions.put(
                "revision-1",
                material("revision-2", Optional.empty(), Optional.empty())
        );
        authority.bypassRevisionAffinityFilter = true;

        assertThrows(
                IllegalStateException.class,
                () -> new AuthorityBackedOpportunityPublicExposureReadPort(authority)
                        .currentPublishedExposure(MERCHANT, OPPORTUNITY)
        );
    }

    private static OpportunityPublicationMaterialRevision material(
            String revisionIdentity,
            Optional<OpportunityTemporalBoundary> publishFrom,
            Optional<OpportunityTemporalBoundary> publishUntil
    ) {
        return new OpportunityPublicationMaterialRevision(
                MERCHANT,
                OPPORTUNITY,
                revisionIdentity,
                "Opportunity title",
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                publishFrom,
                publishUntil
        );
    }

    private static final class StubAuthority
            implements OpportunityPublicationStateAuthority {

        private OpportunityPublicationState state;
        private Optional<OpportunityPublicationState> stateAfterFirstRead;
        private int currentReadCount;
        private boolean bypassRevisionAffinityFilter;
        private final Map<String, OpportunityPublicationMaterialRevision> revisions =
                new HashMap<>();

        @Override
        public OpportunityPublicationState establish(
                OpportunityPublicationState initialState,
                OpportunityPublicationMaterialRevision initialRevision
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public OpportunityPublicationState compareAndSet(
                OpportunityPublicationState expectedState,
                OpportunityPublicationState nextState,
                Optional<OpportunityPublicationMaterialRevision> newRevision
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<OpportunityPublicationState> current(
                MerchantScope merchantScope,
                String opportunityIdentity
        ) {
            Optional<OpportunityPublicationState> response;
            if (currentReadCount++ > 0 && stateAfterFirstRead != null) {
                response = stateAfterFirstRead;
            } else {
                response = Optional.ofNullable(state);
            }
            return response.filter(candidate -> candidate.merchantScope().equals(merchantScope)
                    && candidate.opportunityIdentity().equals(opportunityIdentity));
        }

        @Override
        public Optional<OpportunityPublicationMaterialRevision> revision(
                MerchantScope merchantScope,
                String opportunityIdentity,
                String revisionIdentity
        ) {
            OpportunityPublicationMaterialRevision revision =
                    revisions.get(revisionIdentity);
            if (revision == null) {
                return Optional.empty();
            }
            if (bypassRevisionAffinityFilter) {
                return Optional.of(revision);
            }
            if (!revision.merchantScope().equals(merchantScope)
                    || !revision.opportunityIdentity().equals(opportunityIdentity)) {
                return Optional.empty();
            }
            return Optional.of(revision);
        }

        @Override
        public List<OpportunityPublicationHistoryEntry> publicationHistory(
                MerchantScope merchantScope,
                String opportunityIdentity
        ) {
            return List.of();
        }
    }
}
