package grandrue.onboarding;

import java.util.Objects;

/**
 * Exact final-review affinity to one Onboarding Case revision.
 *
 * <p>This record identifies what onboarding evidence state was reviewed. It does not itself
 * authorise submission or establish merchant approval.</p>
 */
public record OnboardingCaseReview(
        OnboardingCaseIdentity onboardingCaseIdentity,
        OnboardingCaseRevision reviewedRevision
) {

    public OnboardingCaseReview {
        Objects.requireNonNull(onboardingCaseIdentity, "onboardingCaseIdentity");
        Objects.requireNonNull(reviewedRevision, "reviewedRevision");
    }
}
