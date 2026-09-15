package mainstreet.onboarding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 / v1.1 material definition metadata for the initial customer-interaction discovery question.
 */
class InitialCustomerInteractionDiscoveryQuestionDefinitionTest {

    @Test
    void binds_current_material_definition_metadata_without_creating_semantic_authority() {
        assertEquals(
                new OnboardingQuestionDefinitionVersion("v2"),
                InitialCustomerInteractionDiscoveryQuestion.definitionVersion()
        );
        assertEquals(
                OnboardingPromptClass.DISCOVERY_QUESTION,
                InitialCustomerInteractionDiscoveryQuestion.promptClass()
        );
        assertEquals(
                OnboardingAnswerForm.MULTI_SELECT,
                InitialCustomerInteractionDiscoveryQuestion.answerForm()
        );
        assertEquals(
                OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                InitialCustomerInteractionDiscoveryQuestion.priority()
        );
    }
}
