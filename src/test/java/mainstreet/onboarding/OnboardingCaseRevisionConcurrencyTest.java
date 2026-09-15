package mainstreet.onboarding;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MS-PROT-052 v1.2 expected-current revision protection for material onboarding mutations.
 */
class OnboardingCaseRevisionConcurrencyTest {

    private final OnboardingCaseRevisionConcurrency concurrency = new OnboardingCaseRevisionConcurrency();

    @Test
    void exact_expected_revision_is_accepted() {
        OnboardingCase onboardingCase = caseAt("revision-12");

        assertDoesNotThrow(() -> concurrency.requireCurrent(
                new OnboardingCaseRevision("revision-12"),
                onboardingCase
        ));
    }

    @Test
    void stale_expected_revision_is_rejected_as_case_revision_conflict() {
        OnboardingCase onboardingCase = caseAt("revision-13");

        assertThrows(
                OnboardingCaseRevisionConflictException.class,
                () -> concurrency.requireCurrent(
                        new OnboardingCaseRevision("revision-12"),
                        onboardingCase
                )
        );
    }

    @Test
    void concurrency_check_requires_expected_revision_and_live_case() {
        OnboardingCase onboardingCase = caseAt("revision-13");
        OnboardingCaseRevision expectedRevision = new OnboardingCaseRevision("revision-13");

        assertThrows(NullPointerException.class, () -> concurrency.requireCurrent(null, onboardingCase));
        assertThrows(NullPointerException.class, () -> concurrency.requireCurrent(expectedRevision, null));
    }

    private static OnboardingCase caseAt(String revision) {
        return new OnboardingCase(
                new OnboardingCaseIdentity("case-1"),
                new MerchantScope("merchant-1"),
                OnboardingCaseLifecycle.IN_PROGRESS,
                new OnboardingCaseRevision(revision)
        );
    }
}
