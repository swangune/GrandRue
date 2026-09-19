package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MS-PROT-052 v1.2 final review affinity to one exact Onboarding Case revision.
 */
class OnboardingCaseReviewAffinityTest {

    @Test
    void final_review_binds_one_case_identity_to_one_exact_reviewed_revision() {
        OnboardingCaseIdentity caseIdentity = new OnboardingCaseIdentity("case-1");
        OnboardingCaseRevision reviewedRevision = new OnboardingCaseRevision("revision-14");

        OnboardingCaseReview review = new OnboardingCaseReview(caseIdentity, reviewedRevision);

        assertEquals(caseIdentity, review.onboardingCaseIdentity());
        assertEquals(reviewedRevision, review.reviewedRevision());
    }

    @Test
    void review_affinity_changes_when_the_reviewed_revision_changes() {
        OnboardingCaseIdentity caseIdentity = new OnboardingCaseIdentity("case-1");

        OnboardingCaseReview revision14 = new OnboardingCaseReview(
                caseIdentity,
                new OnboardingCaseRevision("revision-14")
        );
        OnboardingCaseReview revision15 = new OnboardingCaseReview(
                caseIdentity,
                new OnboardingCaseRevision("revision-15")
        );

        assertNotEquals(revision14, revision15);
    }

    @Test
    void review_requires_case_identity_and_reviewed_revision() {
        OnboardingCaseIdentity caseIdentity = new OnboardingCaseIdentity("case-1");
        OnboardingCaseRevision revision = new OnboardingCaseRevision("revision-14");

        assertThrows(NullPointerException.class, () -> new OnboardingCaseReview(null, revision));
        assertThrows(NullPointerException.class, () -> new OnboardingCaseReview(caseIdentity, null));
    }
}
