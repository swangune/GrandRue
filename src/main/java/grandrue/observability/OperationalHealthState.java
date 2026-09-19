package grandrue.observability;

/** Operational condition only; never business or capability state. */
public enum OperationalHealthState {
    READY,
    DEGRADED,
    UNAVAILABLE,
    UNKNOWN
}
