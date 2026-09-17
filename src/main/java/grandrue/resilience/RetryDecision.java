package grandrue.resilience;

/** Technical retry disposition; never a business lifecycle result. */
public enum RetryDecision {
    RETRY_ALLOWED,
    RETRY_NOT_ALLOWED,
    RECONCILIATION_REQUIRED,
    WAIT_FOR_PENDING_EXECUTION
}
