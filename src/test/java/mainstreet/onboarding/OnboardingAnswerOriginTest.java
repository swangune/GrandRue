package mainstreet.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 authoritative onboarding answer-origin provenance taxonomy.
 */
class OnboardingAnswerOriginTest {

    @Test
    void exposes_exactly_the_six_accepted_answer_origins() {
        assertEquals(
                Set.of(
                        OnboardingAnswerOrigin.MERCHANT_SELECTED,
                        OnboardingAnswerOrigin.MERCHANT_APPROVED_IN_REVIEW,
                        OnboardingAnswerOrigin.INFERRED_PROPOSAL,
                        OnboardingAnswerOrigin.DERIVED,
                        OnboardingAnswerOrigin.DEFAULTED,
                        OnboardingAnswerOrigin.IMPORTED_EVIDENCE
                ),
                Set.of(OnboardingAnswerOrigin.values())
        );
    }
}
