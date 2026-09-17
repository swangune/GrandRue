package grandrue.background;

/** Owning-contract instruction for how due work is treated after its target time. */
public enum OverdueHandling {
    EXECUTE_WHEN_OVERDUE,
    RE_EVALUATE_CURRENT_STATE,
    EXPIRE_WITHOUT_EXECUTION,
    ESCALATE
}
