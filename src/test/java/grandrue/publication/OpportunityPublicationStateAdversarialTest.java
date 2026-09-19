package grandrue.publication;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpportunityPublicationStateAdversarialTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-acme");

    @Test
    void lifecycle_evidence_cannot_be_structurally_laundered() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpportunityPublicationState(
                        MERCHANT,
                        "opportunity-1",
                        "revision-1",
                        PublicationLifecycle.DRAFT,
                        Optional.of("revision-1")
                )
        );

        for (PublicationLifecycle lifecycle : Set.of(
                PublicationLifecycle.PUBLISHED,
                PublicationLifecycle.WITHDRAWN
        )) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new OpportunityPublicationState(
                            MERCHANT,
                            "opportunity-1",
                            "revision-1",
                            lifecycle,
                            Optional.empty()
                    )
            );
        }
    }

    @Test
    void identities_must_be_nonblank_and_material_revision_must_advance_identity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> OpportunityPublicationState.draft(MERCHANT, " ", "revision-1")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> OpportunityPublicationState.draft(MERCHANT, "opportunity-1", " ")
        );

        OpportunityPublicationState state = OpportunityPublicationState.draft(
                MERCHANT,
                "opportunity-1",
                "revision-1"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> state.revise("revision-1", "revision-1")
        );
    }

    @Test
    void stale_expected_currentness_fails_for_revise_publish_withdraw_and_republish() {
        OpportunityPublicationState draft = OpportunityPublicationState.draft(
                MERCHANT,
                "opportunity-1",
                "revision-1"
        );
        OpportunityPublicationState revised = draft.revise("revision-1", "revision-2");

        assertThrows(
                PublicationRevisionConflictException.class,
                () -> revised.revise("revision-1", "revision-3")
        );
        assertThrows(
                PublicationRevisionConflictException.class,
                () -> revised.publish("revision-1")
        );

        OpportunityPublicationState published = revised.publish("revision-2");
        OpportunityPublicationState revisedAfterPublish = published.revise("revision-2", "revision-3");
        assertThrows(
                PublicationRevisionConflictException.class,
                () -> revisedAfterPublish.withdraw("revision-2")
        );

        OpportunityPublicationState withdrawn = revisedAfterPublish.withdraw("revision-3");
        assertEquals("revision-3", withdrawn.currentRevisionIdentity());
        assertEquals(Optional.of("revision-2"), withdrawn.publishedRevisionIdentity());

        assertThrows(
                PublicationRevisionConflictException.class,
                () -> withdrawn.republish("revision-2")
        );
    }

    @Test
    void lifecycle_transition_entry_points_are_fail_closed() {
        OpportunityPublicationState draft = OpportunityPublicationState.draft(
                MERCHANT,
                "opportunity-1",
                "revision-1"
        );
        assertThrows(IllegalStateException.class, () -> draft.withdraw("revision-1"));
        assertThrows(IllegalStateException.class, () -> draft.republish("revision-1"));

        OpportunityPublicationState published = draft.publish("revision-1");
        assertThrows(
                IllegalStateException.class,
                () -> published.republish("revision-1")
        );

        OpportunityPublicationState withdrawn = published.withdraw("revision-1");
        assertThrows(IllegalStateException.class, () -> withdrawn.withdraw("revision-1"));
        assertThrows(
                IllegalStateException.class,
                () -> withdrawn.publish("revision-1")
        );
    }

    @Test
    void lifecycle_state_is_an_immutable_domain_record_without_downstream_authority_dependencies() {
        assertTrue(OpportunityPublicationState.class.isRecord());

        Set<Class<?>> forbiddenExactTypes = Set.of(Map.class);
        Set<String> forbiddenTypeNameFragments = Set.of(
                "com.fasterxml.jackson.databind.jsonnode",
                "repository",
                "provider",
                "exposure",
                "participation",
                "enquiry",
                "authorization",
                "availability"
        );

        for (Field field : OpportunityPublicationState.class.getDeclaredFields()) {
            assertFalse(forbiddenExactTypes.contains(field.getType()), field::toString);
            assertFalse(
                    containsAny(field.getType().getName(), forbiddenTypeNameFragments),
                    field::toString
            );
        }

        for (Method method : OpportunityPublicationState.class.getDeclaredMethods()) {
            assertFalse(forbiddenExactTypes.contains(method.getReturnType()), method::toString);
            assertFalse(
                    containsAny(method.getReturnType().getName(), forbiddenTypeNameFragments),
                    method::toString
            );
            Arrays.stream(method.getParameterTypes()).forEach(type -> {
                assertFalse(forbiddenExactTypes.contains(type), method::toString);
                assertFalse(containsAny(type.getName(), forbiddenTypeNameFragments), method::toString);
            });
        }

        OpportunityPublicationState state = OpportunityPublicationState.draft(
                MERCHANT,
                "opportunity-1",
                "revision-1"
        );
        assertEquals(Optional.empty(), state.publishedRevisionIdentity());
    }

    private static boolean containsAny(String value, Set<String> fragments) {
        String lower = value.toLowerCase();
        return fragments.stream().anyMatch(lower::contains);
    }
}
