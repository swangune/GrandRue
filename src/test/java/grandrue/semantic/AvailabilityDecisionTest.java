package grandrue.semantic;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvailabilityDecisionTest {

    @Test
    void availability_is_a_contextual_decision_for_a_request() {

        AvailabilityDecision decision =
                new AvailabilityDecision(
                        "request-001",
                        "oil-change-service",
                        true,
                        Instant.parse("2026-08-20T10:15:30Z")
                );

        assertTrue(decision.available());
    }

    @Test
    void same_subject_can_have_different_availability_by_request_context() {

        AvailabilityDecision tuesday =
                new AvailabilityDecision(
                        "request-tuesday-1400",
                        "oil-change-service",
                        true,
                        Instant.parse("2026-08-20T10:15:30Z")
                );

        AvailabilityDecision wednesday =
                new AvailabilityDecision(
                        "request-wednesday-1400",
                        "oil-change-service",
                        false,
                        Instant.parse("2026-08-20T10:15:31Z")
                );

        assertNotEquals(
                tuesday.requestIdentifier(),
                wednesday.requestIdentifier()
        );
        assertTrue(tuesday.available());
        assertFalse(wednesday.available());
    }
}
