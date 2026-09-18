package grandrue.onboarding;

import grandrue.onboarding.OnboardingSubmissionBlocker;
import java.util.Objects;
import java.util.Set;

/**
 * Deterministic result of final-review submission-readiness evaluation.
 */
public record OnboardingSubmissionReadiness(
        Set<OnboardingSubmissionBlocker> blockers
) {

    public OnboardingSubmissionReadiness {
        blockers = Set.copyOf(
                Objects.requireNonNull(blockers, "blockers")
        );
    }

    public boolean isReady() {
        return blockers.isEmpty();
    }
}
