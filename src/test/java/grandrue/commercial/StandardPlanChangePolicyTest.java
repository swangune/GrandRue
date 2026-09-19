package grandrue.commercial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardPlanChangePolicyTest {

    private final StandardPlanChangePolicy policy = new StandardPlanChangePolicy();

    @Test
    void upward_standard_plan_changes_are_effective_immediately() {
        assertEquals(
                StandardPlanChangeTiming.IMMEDIATE,
                policy.timingFor(StandardPlanLevel.FREE, StandardPlanLevel.BUSINESS)
        );
        assertEquals(
                StandardPlanChangeTiming.IMMEDIATE,
                policy.timingFor(StandardPlanLevel.FREE, StandardPlanLevel.GROWTH)
        );
        assertEquals(
                StandardPlanChangeTiming.IMMEDIATE,
                policy.timingFor(StandardPlanLevel.BUSINESS, StandardPlanLevel.GROWTH)
        );
    }

    @Test
    void downward_standard_plan_changes_take_effect_at_current_paid_period_end() {
        assertEquals(
                StandardPlanChangeTiming.CURRENT_PAID_PERIOD_END,
                policy.timingFor(StandardPlanLevel.GROWTH, StandardPlanLevel.BUSINESS)
        );
        assertEquals(
                StandardPlanChangeTiming.CURRENT_PAID_PERIOD_END,
                policy.timingFor(StandardPlanLevel.GROWTH, StandardPlanLevel.FREE)
        );
        assertEquals(
                StandardPlanChangeTiming.CURRENT_PAID_PERIOD_END,
                policy.timingFor(StandardPlanLevel.BUSINESS, StandardPlanLevel.FREE)
        );
    }

    @Test
    void same_standard_plan_level_is_not_a_plan_level_change() {
        for (StandardPlanLevel level : StandardPlanLevel.values()) {
            assertEquals(
                    StandardPlanChangeTiming.NO_PLAN_LEVEL_CHANGE,
                    policy.timingFor(level, level)
            );
        }
    }
}
