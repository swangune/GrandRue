package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 authoritative onboarding prompt-class taxonomy.
 */
class OnboardingPromptClassTest {

    @Test
    void exposes_exactly_the_four_accepted_authority_classes() {
        assertEquals(
                Set.of(
                        OnboardingPromptClass.DISCOVERY_QUESTION,
                        OnboardingPromptClass.CONFIGURATION_DECISION_QUESTION,
                        OnboardingPromptClass.DATA_CAPTURE_PROMPT,
                        OnboardingPromptClass.REVIEW_CONFIRMATION
                ),
                Set.of(OnboardingPromptClass.values())
        );
    }
}
