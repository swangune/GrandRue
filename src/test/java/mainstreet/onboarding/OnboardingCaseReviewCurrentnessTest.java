package mainstreet.onboarding;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MS-PROT-052 v1.2 stale-review protection by exact case/revision affinity.
 */
class OnboardingCaseReviewCurrentnessTest {

    private final OnboardingCaseReviewCurrentness currentness = new OnboardingCaseReviewCurrentness();

    @Test
    void review_is_current_only_for_the_same_case_and_exact_current_revision() {
        OnboardingCase onboardingCase = new OnboardingCase(
                new OnboardingCaseIdentity("case-1"),
                new MerchantScope("merchant-1"),
                OnboardingCaseLifecycle.IN_PROGRESS,
                new OnboardingCaseRevision("revision-14")
        );
        OnboardingCaseReview review = new OnboardingCaseReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-14")
        );

        assertTrue(currentness.isCurrent(review, onboardingCase));
    }

    @Test
    void review_is_stale_when_the_case_has_advanced() {
        OnboardingCase onboardingCase = new OnboardingCase(
                new OnboardingCaseIdentity("case-1"),
                new MerchantScope("merchant-1"),
                OnboardingCaseLifecycle.IN_PROGRESS,
                new OnboardingCaseRevision("revision-15")
        );
        OnboardingCaseReview review = new OnboardingCaseReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-14")
        );

        assertFalse(currentness.isCurrent(review, onboardingCase));
    }

    @Test
    void review_for_another_case_is_not_current_even_when_revision_tokens_match() {
        OnboardingCase onboardingCase = new OnboardingCase(
                new OnboardingCaseIdentity("case-2"),
                new MerchantScope("merchant-1"),
                OnboardingCaseLifecycle.IN_PROGRESS,
                new OnboardingCaseRevision("revision-14")
        );
        OnboardingCaseReview review = new OnboardingCaseReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-14")
        );

        assertFalse(currentness.isCurrent(review, onboardingCase));
    }

    @Test
    void currentness_requires_review_and_live_case() {
        OnboardingCase onboardingCase = new OnboardingCase(
                new OnboardingCaseIdentity("case-1"),
                new MerchantScope("merchant-1"),
                OnboardingCaseLifecycle.IN_PROGRESS,
                new OnboardingCaseRevision("revision-14")
        );
        OnboardingCaseReview review = new OnboardingCaseReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-14")
        );

        assertThrows(NullPointerException.class, () -> currentness.isCurrent(null, onboardingCase));
        assertThrows(NullPointerException.class, () -> currentness.isCurrent(review, null));
    }
}
