package mainstreet.publication;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OpportunityPublicationStateTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-acme");

    @Test
    void lifecycle_vocabulary_is_exactly_draft_published_withdrawn() {
        assertArrayEquals(
                new PublicationLifecycle[]{
                        PublicationLifecycle.DRAFT,
                        PublicationLifecycle.PUBLISHED,
                        PublicationLifecycle.WITHDRAWN
                },
                PublicationLifecycle.values()
        );
    }

    @Test
    void publish_binds_the_exact_current_revision_and_rejects_stale_expected_revision() {
        OpportunityPublicationState draft = OpportunityPublicationState.draft(
                MERCHANT,
                "opportunity-1",
                "revision-1"
        );

        OpportunityPublicationState published = draft.publish("revision-1");

        assertEquals(PublicationLifecycle.PUBLISHED, published.lifecycle());
        assertEquals("revision-1", published.currentRevisionIdentity());
        assertEquals(Optional.of("revision-1"), published.publishedRevisionIdentity());

        OpportunityPublicationState revised = published.revise(
                "revision-1",
                "revision-2"
        );

        assertThrows(
                PublicationRevisionConflictException.class,
                () -> revised.publish("revision-1")
        );
    }

    @Test
    void revising_a_published_opportunity_does_not_withdraw_or_silently_publish_the_new_revision() {
        OpportunityPublicationState published = OpportunityPublicationState.draft(
                        MERCHANT,
                        "opportunity-1",
                        "revision-1"
                )
                .publish("revision-1");

        OpportunityPublicationState revised = published.revise(
                "revision-1",
                "revision-2"
        );

        assertEquals(PublicationLifecycle.PUBLISHED, revised.lifecycle());
        assertEquals("revision-2", revised.currentRevisionIdentity());
        assertEquals(Optional.of("revision-1"), revised.publishedRevisionIdentity());

        OpportunityPublicationState republishedCurrent = revised.publish("revision-2");

        assertEquals(Optional.of("revision-2"), republishedCurrent.publishedRevisionIdentity());
    }

    @Test
    void withdrawal_preserves_last_published_revision_and_only_explicit_republish_restores_published_state() {
        OpportunityPublicationState published = OpportunityPublicationState.draft(
                        MERCHANT,
                        "opportunity-1",
                        "revision-1"
                )
                .publish("revision-1");

        OpportunityPublicationState withdrawn = published.withdraw("revision-1");

        assertEquals(PublicationLifecycle.WITHDRAWN, withdrawn.lifecycle());
        assertEquals(Optional.of("revision-1"), withdrawn.publishedRevisionIdentity());

        assertThrows(IllegalStateException.class, () -> withdrawn.publish("revision-1"));

        OpportunityPublicationState republished = withdrawn.republish("revision-1");

        assertEquals(PublicationLifecycle.PUBLISHED, republished.lifecycle());
        assertEquals(Optional.of("revision-1"), republished.publishedRevisionIdentity());
    }

    @Test
    void withdrawal_of_a_revised_published_opportunity_requires_current_revision_and_preserves_published_evidence() {
        OpportunityPublicationState revised = OpportunityPublicationState.draft(
                        MERCHANT,
                        "opportunity-1",
                        "revision-1"
                )
                .publish("revision-1")
                .revise("revision-1", "revision-2");

        assertThrows(
                PublicationRevisionConflictException.class,
                () -> revised.withdraw("revision-1")
        );

        OpportunityPublicationState withdrawn = revised.withdraw("revision-2");

        assertEquals(PublicationLifecycle.WITHDRAWN, withdrawn.lifecycle());
        assertEquals("revision-2", withdrawn.currentRevisionIdentity());
        assertEquals(Optional.of("revision-1"), withdrawn.publishedRevisionIdentity());
    }

    @Test
    void state_is_merchant_scoped_and_revisions_require_exact_expected_currentness() {
        OpportunityPublicationState state = OpportunityPublicationState.draft(
                MERCHANT,
                "opportunity-1",
                "revision-1"
        );

        assertEquals(MERCHANT, state.merchantScope());
        assertEquals("opportunity-1", state.opportunityIdentity());

        assertThrows(
                PublicationRevisionConflictException.class,
                () -> state.revise("revision-stale", "revision-2")
        );
    }
}
