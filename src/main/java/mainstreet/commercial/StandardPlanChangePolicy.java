package mainstreet.commercial;

import grandrue.commercial.StandardPlanChangeTiming;

import java.util.Objects;

/**
 * Applies the accepted timing policy for movement within the standard
 * FREE/BUSINESS/GROWTH hierarchy.
 *
 * <p>Upward movement is commercially effective immediately once validly
 * accepted. Downward movement becomes effective at the current paid-period
 * boundary. Same-level changes are not plan-level changes and may have separate
 * billing-cadence policy elsewhere.</p>
 */
public final class StandardPlanChangePolicy {

    public StandardPlanChangeTiming timingFor(
            StandardPlanLevel current,
            StandardPlanLevel target
    ) {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(target, "target");

        if (current == target) {
            return StandardPlanChangeTiming.NO_PLAN_LEVEL_CHANGE;
        }

        if (isUpgrade(current, target)) {
            return StandardPlanChangeTiming.IMMEDIATE;
        }

        return StandardPlanChangeTiming.CURRENT_PAID_PERIOD_END;
    }

    private boolean isUpgrade(
            StandardPlanLevel current,
            StandardPlanLevel target
    ) {
        return switch (current) {
            case FREE -> target == StandardPlanLevel.BUSINESS
                    || target == StandardPlanLevel.GROWTH;
            case BUSINESS -> target == StandardPlanLevel.GROWTH;
            case GROWTH -> false;
        };
    }
}
