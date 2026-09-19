package grandrue.commercial;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrialReminderCadenceTest {

    @Test
    void exposes_only_the_six_accepted_trial_expiry_milestones() {
        TrialReminderCadence cadence = new TrialReminderCadence();

        assertEquals(
                List.of(
                        new TrialReminderMilestone(15, 15),
                        new TrialReminderMilestone(21, 9),
                        new TrialReminderMilestone(25, 5),
                        new TrialReminderMilestone(28, 2),
                        new TrialReminderMilestone(29, 1),
                        new TrialReminderMilestone(30, 0)
                ),
                cadence.milestones()
        );
    }

    @Test
    void days_one_through_fourteen_have_no_scheduled_expiry_reminder() {
        TrialReminderCadence cadence = new TrialReminderCadence();

        for (int trialDay = 1; trialDay <= 14; trialDay++) {
            assertTrue(cadence.milestoneForTrialDay(trialDay).isEmpty());
        }
    }

    @Test
    void non_milestone_days_in_the_second_half_do_not_reintroduce_daily_reminders() {
        TrialReminderCadence cadence = new TrialReminderCadence();

        for (int trialDay : List.of(16, 17, 18, 19, 20, 22, 23, 24, 26, 27)) {
            assertTrue(cadence.milestoneForTrialDay(trialDay).isEmpty());
        }
    }

    @Test
    void accepted_milestone_can_be_resolved_by_trial_day() {
        TrialReminderCadence cadence = new TrialReminderCadence();

        assertEquals(
                new TrialReminderMilestone(29, 1),
                cadence.milestoneForTrialDay(29).orElseThrow()
        );
        assertEquals(
                new TrialReminderMilestone(30, 0),
                cadence.milestoneForTrialDay(30).orElseThrow()
        );
    }
}
