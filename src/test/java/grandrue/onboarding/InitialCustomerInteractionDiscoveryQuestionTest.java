package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 / v1.1 stable identity for the initial customer-interaction discovery question.
 */
class InitialCustomerInteractionDiscoveryQuestionTest {

    @Test
    void exposes_the_stable_question_identity_independently_of_presentation_wording() {
        OnboardingQuestionIdentity identity = InitialCustomerInteractionDiscoveryQuestion.identity();

        assertEquals("onboarding.discovery", identity.namespace());
        assertEquals("customer-interactions", identity.identifier());
    }
}
