package mainstreet.commercial;

import grandrue.commercial.StandardPlanChangePolicy;
import grandrue.commercial.StandardPlanChangeTiming;

import java.time.Instant;
import java.util.Objects;

/**
 * Future commercial intent for a standard-plan downgrade.
 *
 * <p>The current plan revision remains effective before the supplied paid-period
 * boundary. The target revision becomes effective at that boundary. This value
 * does not mutate semantic configuration, perform billing or calculate the paid
 * period itself.</p>
 */
public record ScheduledStandardPlanChange(
        Instant requestedAt,
        StandardPlanRevision currentPlanRevision,
        StandardPlanRevision targetPlanRevision,
        Instant effectiveAt
) {

    public ScheduledStandardPlanChange {
        Objects.requireNonNull(requestedAt, "requestedAt");
        Objects.requireNonNull(currentPlanRevision, "currentPlanRevision");
        Objects.requireNonNull(targetPlanRevision, "targetPlanRevision");
        Objects.requireNonNull(effectiveAt, "effectiveAt");

        if (effectiveAt.isBefore(requestedAt)) {
            throw new IllegalArgumentException(
                    "Scheduled plan-change boundary must not precede request"
            );
        }

        StandardPlanChangeTiming timing = new StandardPlanChangePolicy()
                .timingFor(
                        currentPlanRevision.level(),
                        targetPlanRevision.level()
                );
        if (timing != StandardPlanChangeTiming.CURRENT_PAID_PERIOD_END) {
            throw new IllegalArgumentException(
                    "Only period-end standard-plan changes may be scheduled"
            );
        }
    }

    public StandardPlanRevision effectivePlanRevisionAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return instant.isBefore(effectiveAt)
                ? currentPlanRevision
                : targetPlanRevision;
    }
}
