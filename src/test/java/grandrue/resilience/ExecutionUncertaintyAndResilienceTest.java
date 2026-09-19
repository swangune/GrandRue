package grandrue.resilience;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutionUncertaintyAndResilienceTest {

    private final ResilienceRetryGate retryGate = new ResilienceRetryGate();

    @Test
    void known_execution_and_lost_acknowledgement_remain_distinct() {
        LogicalCommandCertainty certainty = new LogicalCommandCertainty(
                "command-1",
                AcceptanceCertainty.DURABLY_ACCEPTED,
                ExecutionCertainty.KNOWN_EXECUTED,
                AcknowledgementCertainty.LOST
        );

        assertEquals(ExecutionCertainty.KNOWN_EXECUTED, certainty.execution());
        assertEquals(AcknowledgementCertainty.LOST, certainty.acknowledgement());
        assertEquals(
                RetryDecision.RETRY_NOT_ALLOWED,
                retryGate.decide(certainty.execution(), RetrySafety.SAFE)
        );
    }

    @Test
    void uncertain_side_effect_requires_reconciliation_when_retry_safety_is_unknown() {
        assertEquals(
                RetryDecision.RECONCILIATION_REQUIRED,
                retryGate.decide(
                        ExecutionCertainty.EXECUTION_UNCERTAIN,
                        RetrySafety.UNKNOWN
                )
        );
    }

    @Test
    void independently_safe_idempotent_retry_may_proceed_despite_transport_uncertainty() {
        assertEquals(
                RetryDecision.RETRY_ALLOWED,
                retryGate.decide(
                        ExecutionCertainty.EXECUTION_UNCERTAIN,
                        RetrySafety.SAFE
                )
        );
    }

    @Test
    void pending_durable_execution_is_not_blindly_retried() {
        assertEquals(
                RetryDecision.WAIT_FOR_PENDING_EXECUTION,
                retryGate.decide(
                        ExecutionCertainty.EXECUTION_PENDING,
                        RetrySafety.SAFE
                )
        );
    }

    @Test
    void known_non_execution_still_requires_explicit_retry_safety() {
        assertEquals(
                RetryDecision.RETRY_NOT_ALLOWED,
                retryGate.decide(
                        ExecutionCertainty.KNOWN_NOT_EXECUTED,
                        RetrySafety.UNKNOWN
                )
        );
        assertEquals(
                RetryDecision.RETRY_ALLOWED,
                retryGate.decide(
                        ExecutionCertainty.KNOWN_NOT_EXECUTED,
                        RetrySafety.SAFE
                )
        );
    }

    @Test
    void impossible_known_executed_without_acceptance_is_rejected() {
        assertThrows(IllegalArgumentException.class, () -> new LogicalCommandCertainty(
                "command-1",
                AcceptanceCertainty.KNOWN_NOT_ACCEPTED,
                ExecutionCertainty.KNOWN_EXECUTED,
                AcknowledgementCertainty.LOST
        ));
    }

    @Test
    void uncertain_execution_cannot_be_acknowledged_as_resolved() {
        assertThrows(IllegalArgumentException.class, () -> new LogicalCommandCertainty(
                "command-1",
                AcceptanceCertainty.UNCERTAIN,
                ExecutionCertainty.EXECUTION_UNCERTAIN,
                AcknowledgementCertainty.ACKNOWLEDGED
        ));
    }
}
