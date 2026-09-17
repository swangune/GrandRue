package grandrue.scheduling;

/** Lifecycle states of an offered, non-committing appointment time. */
public enum TimeProposalStatus {
    PROPOSED,
    ACCEPTED,
    DECLINED,
    WITHDRAWN,
    EXPIRED
}
