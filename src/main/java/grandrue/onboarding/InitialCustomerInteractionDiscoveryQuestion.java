package grandrue.onboarding;

import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingQuestionIdentity;
import grandrue.onboarding.OnboardingPromptPriority;
import grandrue.onboarding.OnboardingAnswerForm;
import grandrue.onboarding.OnboardingPromptClass;
/**
 * Stable definition metadata authority for the initial customer-interaction discovery question.
 *
 * <p>The question identity is independent of merchant-facing wording. The material definition
 * metadata binds the accepted v1.1 question shape without owning option semantics, discovery
 * mappings, configuration activation or runtime authority.</p>
 */
public final class InitialCustomerInteractionDiscoveryQuestion {

    private static final OnboardingQuestionIdentity IDENTITY =
            new OnboardingQuestionIdentity("onboarding.discovery", "customer-interactions");
    private static final OnboardingQuestionDefinitionVersion DEFINITION_VERSION =
            new OnboardingQuestionDefinitionVersion("v2");

    private InitialCustomerInteractionDiscoveryQuestion() {
    }

    public static OnboardingQuestionIdentity identity() {
        return IDENTITY;
    }

    public static OnboardingQuestionDefinitionVersion definitionVersion() {
        return DEFINITION_VERSION;
    }

    public static OnboardingPromptClass promptClass() {
        return OnboardingPromptClass.DISCOVERY_QUESTION;
    }

    public static OnboardingAnswerForm answerForm() {
        return OnboardingAnswerForm.MULTI_SELECT;
    }

    public static OnboardingPromptPriority priority() {
        return OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY;
    }
}
