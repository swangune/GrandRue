package grandrue.background;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkAttemptTest {

    private static final Instant ATTEMPTED_AT =
            Instant.parse("2026-09-14T02:00:00Z");

    @Test
    void started_attempt_can_exist_before_any_outcome_is_known() {
        WorkAttempt attempt = new WorkAttempt(
                "attempt-1",
                "work-1",
                ATTEMPTED_AT,
                "scheduled-principal",
                Optional.empty(),
                Optional.empty()
        );

        assertEquals("attempt-1", attempt.attemptIdentity());
        assertEquals("work-1", attempt.workIdentity());
        assertEquals(ATTEMPTED_AT, attempt.attemptedAt());
        assertEquals("scheduled-principal", attempt.principalReference());
        assertTrue(attempt.resultClassification().isEmpty());
        assertTrue(attempt.evidenceReference().isEmpty());
    }

    @Test
    void classified_attempt_preserves_known_outcome_and_evidence() {
        WorkAttempt attempt = new WorkAttempt(
                "attempt-1",
                "work-1",
                ATTEMPTED_AT,
                "scheduled-principal",
                Optional.of(BackgroundWorkResultClassification.SUCCESS),
                Optional.of("owner-outcome-evidence-1")
        );

        assertEquals(
                Optional.of(BackgroundWorkResultClassification.SUCCESS),
                attempt.resultClassification()
        );
        assertEquals(
                Optional.of("owner-outcome-evidence-1"),
                attempt.evidenceReference()
        );
    }
}