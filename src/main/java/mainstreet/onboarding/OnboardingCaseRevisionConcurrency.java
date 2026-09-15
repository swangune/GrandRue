package mainstreet.onboarding;

import java.util.Objects;

/**
 * Expected-current revision guard for material Onboarding Case mutations.
 *
 * <p>This guard protects against lost merchant intent from concurrent stale writes. It does not
 * advance revisions or perform a mutation itself.</p>
 */
public final class OnboardingCaseRevisionConcurrency {

    public void requireCurrent(
            OnboardingCaseRevision expectedRevision,
            OnboardingCase onboardingCase
    ) {
        Objects.requireNonNull(expectedRevision, "expectedRevision");
        Objects.requireNonNull(onboardingCase, "onboardingCase");

        if (!expectedRevision.equals(onboardingCase.currentRevision())) {
            throw new OnboardingCaseRevisionConflictException(
                    onboardingCase.identity(),
                    expectedRevision,
                    onboardingCase.currentRevision()
            );
        }
    }
}
