package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 deterministic onboarding prompt eligibility outcome.
 */
class OnboardingPromptEligibilityTest {

    @Test
    void exposes_exactly_the_two_accepted_eligibility_outcomes() {
        assertEquals(
                Set.of(
                        OnboardingPromptEligibility.ELIGIBLE,
                        OnboardingPromptEligibility.INELIGIBLE
                ),
                Set.of(OnboardingPromptEligibility.values())
        );
    }
}
