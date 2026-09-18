package grandrue.commercial;

/**
 * One accepted routine trial-expiry reminder milestone.
 *
 * <p>The milestone is commercial communication policy only. It does not alter
 * trial entitlement, merchant configuration or capability semantics.</p>
 */
public record TrialReminderMilestone(
        int trialDay,
        int daysRemaining
) {

    public TrialReminderMilestone {
        if (trialDay < 1 || trialDay > 30) {
            throw new IllegalArgumentException(
                    "Trial reminder day must be within the 30-day trial"
            );
        }
        if (daysRemaining < 0 || daysRemaining > 29) {
            throw new IllegalArgumentException(
                    "Trial days remaining must be within the trial"
            );
        }
    }
}
