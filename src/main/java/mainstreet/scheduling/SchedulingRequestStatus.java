package mainstreet.scheduling;

/** Lifecycle states of non-committing scheduling intent. */
public enum SchedulingRequestStatus {
    REQUESTED,
    RESOLVED,
    WITHDRAWN,
    EXPIRED
}
