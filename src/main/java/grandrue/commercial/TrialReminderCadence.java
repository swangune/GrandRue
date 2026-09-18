package grandrue.commercial;

import mainstreet.commercial.TrialReminderMilestone;

import java.util.List;
import java.util.Optional;

/**
 * Versioned commercial policy for routine reminders during the initial
 * full-experience trial.
 *
 * <p>The cadence deliberately contains only the accepted milestone days. It
 * does not schedule infrastructure work, choose delivery channels or create
 * semantic/runtime authority.</p>
 */
public final class TrialReminderCadence {

    private static final List<TrialReminderMilestone> MILESTONES = List.of(
            new TrialReminderMilestone(15, 15),
            new TrialReminderMilestone(21, 9),
            new TrialReminderMilestone(25, 5),
            new TrialReminderMilestone(28, 2),
            new TrialReminderMilestone(29, 1),
            new TrialReminderMilestone(30, 0)
    );

    public List<TrialReminderMilestone> milestones() {
        return MILESTONES;
    }

    public Optional<TrialReminderMilestone> milestoneForTrialDay(
            int trialDay
    ) {
        return MILESTONES.stream()
                .filter(milestone -> milestone.trialDay() == trialDay)
                .findFirst();
    }
}
