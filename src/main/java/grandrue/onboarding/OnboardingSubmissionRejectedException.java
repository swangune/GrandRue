package grandrue.onboarding;

import java.util.Objects;
import java.util.Set;

/** Fail-closed rejection of an onboarding submission before commit. */
public final class OnboardingSubmissionRejectedException
        extends IllegalStateException {

    private final Set<OnboardingSubmissionBlocker> blockers;

    public OnboardingSubmissionRejectedException(
            Set<OnboardingSubmissionBlocker> blockers
    ) {
        super("Onboarding submission is not ready: " + blockers);
        this.blockers = Set.copyOf(
                Objects.requireNonNull(blockers, "blockers")
        );
        if (this.blockers.isEmpty()) {
            throw new IllegalArgumentException(
                    "Submission rejection requires at least one blocker"
            );
        }
    }

    public Set<OnboardingSubmissionBlocker> blockers() {
        return blockers;
    }
}
