package mainstreet.commercial;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScheduledStandardPlanChangeTest {

    private static final StandardPlanRevision BUSINESS = new StandardPlanRevision(
            StandardPlanLevel.BUSINESS,
            "business-v1",
            Set.of(new CommercialEntitlementIdentity("booking.new-activity"))
    );
    private static final StandardPlanRevision GROWTH = new StandardPlanRevision(
            StandardPlanLevel.GROWTH,
            "growth-v1",
            Set.of(
                    new CommercialEntitlementIdentity("booking.new-activity"),
                    new CommercialEntitlementIdentity("analytics.advanced")
            )
    );
    private static final Instant REQUESTED_AT =
            Instant.parse("2026-08-23T05:30:00Z");
    private static final Instant CURRENT_PERIOD_END =
            Instant.parse("2026-09-23T05:30:00Z");

    @Test
    void scheduled_downgrade_preserves_current_plan_until_period_end() {
        ScheduledStandardPlanChange change = new ScheduledStandardPlanChange(
                REQUESTED_AT,
                GROWTH,
                BUSINESS,
                CURRENT_PERIOD_END
        );

        assertEquals(GROWTH, change.effectivePlanRevisionAt(REQUESTED_AT));
        assertEquals(
                GROWTH,
                change.effectivePlanRevisionAt(CURRENT_PERIOD_END.minusNanos(1))
        );
        assertEquals(BUSINESS, change.effectivePlanRevisionAt(CURRENT_PERIOD_END));
        assertEquals(
                BUSINESS,
                change.effectivePlanRevisionAt(CURRENT_PERIOD_END.plusSeconds(1))
        );
    }

    @Test
    void period_end_schedule_rejects_an_upgrade_that_should_be_immediate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ScheduledStandardPlanChange(
                        REQUESTED_AT,
                        BUSINESS,
                        GROWTH,
                        CURRENT_PERIOD_END
                )
        );
    }

    @Test
    void period_end_schedule_rejects_same_level_change() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ScheduledStandardPlanChange(
                        REQUESTED_AT,
                        BUSINESS,
                        BUSINESS,
                        CURRENT_PERIOD_END
                )
        );
    }

    @Test
    void scheduled_boundary_cannot_precede_the_request() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ScheduledStandardPlanChange(
                        REQUESTED_AT,
                        GROWTH,
                        BUSINESS,
                        REQUESTED_AT.minusSeconds(1)
                )
        );
    }
}
