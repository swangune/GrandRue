package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-039 / MS-PROT-052 authoritative onboarding answer-form taxonomy.
 */
class OnboardingAnswerFormTest {

    @Test
    void exposes_exactly_the_seven_accepted_answer_forms() {
        assertEquals(
                Set.of(
                        OnboardingAnswerForm.SINGLE_SELECT,
                        OnboardingAnswerForm.MULTI_SELECT,
                        OnboardingAnswerForm.BOOLEAN,
                        OnboardingAnswerForm.QUANTITY,
                        OnboardingAnswerForm.DURATION,
                        OnboardingAnswerForm.TEXT,
                        OnboardingAnswerForm.STRUCTURED_VALUE
                ),
                Set.of(OnboardingAnswerForm.values())
        );
    }
}
