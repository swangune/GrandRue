package mainstreet.onboarding;

import java.util.Objects;

/**
 * Determines whether a final review still describes the live Onboarding Case evidence state.
 *
 * <p>This is only an exact case/revision affinity check. It does not establish submission
 * completeness, merchant approval, lifecycle eligibility or current Controller authority.</p>
 */
public final class OnboardingCaseReviewCurrentness {

    public boolean isCurrent(OnboardingCaseReview review, OnboardingCase onboardingCase) {
        Objects.requireNonNull(review, "review");
        Objects.requireNonNull(onboardingCase, "onboardingCase");

        return review.onboardingCaseIdentity().equals(onboardingCase.identity())
                && review.reviewedRevision().equals(onboardingCase.currentRevision());
    }
}
