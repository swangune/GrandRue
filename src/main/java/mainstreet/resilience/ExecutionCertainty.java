package mainstreet.resilience;

/** Whether one logical command's authoritative business effect is known. */
public enum ExecutionCertainty {
    EXECUTION_PENDING,
    KNOWN_EXECUTED,
    KNOWN_NOT_EXECUTED,
    EXECUTION_UNCERTAIN
}
