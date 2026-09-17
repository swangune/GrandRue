package grandrue.resilience;

import java.util.Objects;

/**
 * Minimal invariant-bearing retry gate for MS-PROT-069/070. It does not choose
 * timing/backoff, circuit state or provider fallback.
 */
public final class ResilienceRetryGate {

    public RetryDecision decide(
            ExecutionCertainty executionCertainty,
            RetrySafety retrySafety
    ) {
        Objects.requireNonNull(executionCertainty, "executionCertainty");
        Objects.requireNonNull(retrySafety, "retrySafety");

        return switch (executionCertainty) {
            case KNOWN_EXECUTED -> RetryDecision.RETRY_NOT_ALLOWED;
            case EXECUTION_PENDING -> RetryDecision.WAIT_FOR_PENDING_EXECUTION;
            case KNOWN_NOT_EXECUTED -> retrySafety == RetrySafety.SAFE
                    ? RetryDecision.RETRY_ALLOWED
                    : RetryDecision.RETRY_NOT_ALLOWED;
            case EXECUTION_UNCERTAIN -> retrySafety == RetrySafety.SAFE
                    ? RetryDecision.RETRY_ALLOWED
                    : RetryDecision.RECONCILIATION_REQUIRED;
        };
    }
}
