package grandrue.resilience;

/**
 * Retry safety established by the owning operation/provider/process contract.
 * Transport libraries and exceptions do not manufacture SAFE.
 */
public enum RetrySafety {
    SAFE,
    NOT_SAFE,
    UNKNOWN
}
