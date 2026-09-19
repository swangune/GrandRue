package grandrue.protection;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceProtectionModelTest {

    private static final ProtectionTarget TARGET =
            new ProtectionTarget("notification-dispatch");
    private static final ProtectionSubject SUBJECT =
            new ProtectionSubject("MERCHANT", "merchant-1");

    @Test
    void protection_target_is_explicit_stable_identity() {
        assertEquals("notification-dispatch", TARGET.identifier());
        assertThrows(IllegalArgumentException.class, () -> new ProtectionTarget(" "));
    }

    @Test
    void protection_subject_keeps_scope_and_identity_distinct() {
        assertEquals("MERCHANT", SUBJECT.scopeIdentifier());
        assertEquals("merchant-1", SUBJECT.subjectIdentifier());
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProtectionSubject(" ", "merchant-1")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProtectionSubject("MERCHANT", " ")
        );
    }

    @Test
    void bounded_window_policy_defines_measurement_budget_time_and_failure_behaviour() {
        ProtectionPolicy policy = new ProtectionPolicy(
                "notification-dispatch-per-merchant",
                3,
                TARGET,
                "MERCHANT",
                "LOGICAL_APPLICATION_REQUEST",
                20,
                Duration.ofMinutes(1),
                ProtectionAdmissionDecision.REJECT,
                ProtectionStateFailureBehaviour.REJECT
        );

        assertEquals(3, policy.version());
        assertEquals(20, policy.capacityUnits());
        assertEquals(Duration.ofMinutes(1), policy.window());
        assertFalse(policy.permitsDeferredExhaustion());
    }

    @Test
    void defer_is_policy_behaviour_but_requires_owner_support_at_decision_time() {
        ProtectionPolicy policy = new ProtectionPolicy(
                "media-rendition-per-merchant",
                1,
                new ProtectionTarget("media-rendition"),
                "MERCHANT",
                "LOGICAL_APPLICATION_REQUEST",
                2,
                Duration.ofMinutes(1),
                ProtectionAdmissionDecision.DEFER,
                ProtectionStateFailureBehaviour.REJECT
        );

        assertTrue(policy.permitsDeferredExhaustion());
    }

    @Test
    void policy_rejects_admit_as_exhaustion_behaviour() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProtectionPolicy(
                        "invalid-policy",
                        1,
                        TARGET,
                        "MERCHANT",
                        "LOGICAL_APPLICATION_REQUEST",
                        1,
                        Duration.ofMinutes(1),
                        ProtectionAdmissionDecision.ADMIT,
                        ProtectionStateFailureBehaviour.REJECT
                )
        );
    }

    @Test
    void temporary_restriction_is_bounded_and_target_scoped() {
        Instant start = Instant.parse("2026-08-24T05:30:00Z");
        TemporaryProtectiveRestriction restriction =
                new TemporaryProtectiveRestriction(
                        "restriction-1",
                        SUBJECT,
                        Set.of(TARGET),
                        start,
                        start.plus(Duration.ofMinutes(10)),
                        "resource-exhaustion-pattern"
                );

        assertTrue(restriction.appliesTo(TARGET, start));
        assertTrue(restriction.appliesTo(TARGET, start.plusSeconds(599)));
        assertFalse(restriction.appliesTo(TARGET, start.plusSeconds(600)));
        assertFalse(
                restriction.appliesTo(
                        new ProtectionTarget("media-rendition"),
                        start.plusSeconds(1)
                )
        );
    }

    @Test
    void temporary_restriction_cannot_be_indefinite_or_empty() {
        Instant start = Instant.parse("2026-08-24T05:30:00Z");
        assertThrows(
                IllegalArgumentException.class,
                () -> new TemporaryProtectiveRestriction(
                        "restriction-1",
                        SUBJECT,
                        Set.of(),
                        start,
                        start.plusSeconds(1),
                        "reason"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new TemporaryProtectiveRestriction(
                        "restriction-1",
                        SUBJECT,
                        Set.of(TARGET),
                        start,
                        start,
                        "reason"
                )
        );
    }
}
