package grandrue.commercial;

/**
 * Commercial timing classification for movement within the accepted standard
 * plan hierarchy.
 *
 * <p>This classification says when the target standard-plan entitlement
 * position may become effective. It does not perform billing, proration,
 * subscription persistence or semantic capability activation.</p>
 */
public enum StandardPlanChangeTiming {
    IMMEDIATE,
    CURRENT_PAID_PERIOD_END,
    NO_PLAN_LEVEL_CHANGE
}
