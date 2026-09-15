package mainstreet.onboarding;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 adaptive sequencing priority taxonomy.
 */
class OnboardingPromptPriorityTest {

    @Test
    void preserves_the_five_accepted_priority_classes_in_governing_order() {
        assertEquals(
                List.of(
                        OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                        OnboardingPromptPriority.MANDATORY_CONFIGURATION_DECISION,
                        OnboardingPromptPriority.REQUIRED_STRUCTURED_DATA,
                        OnboardingPromptPriority.OPTIONAL_INTEGRATION,
                        OnboardingPromptPriority.OPTIONAL_PROFILE_ENRICHMENT
                ),
                List.of(OnboardingPromptPriority.values())
        );
    }
}
