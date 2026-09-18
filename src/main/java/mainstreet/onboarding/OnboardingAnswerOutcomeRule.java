package mainstreet.onboarding;

import grandrue.onboarding.OnboardingBlockingAnswerOutcome;
import java.util.Objects;

/**
 * Exact registered submission consequence of one question-version option.
 */
public record OnboardingAnswerOutcomeRule(
        OnboardingPromptKey promptKey,
        OnboardingQuestionDefinitionVersion questionDefinitionVersion,
        String optionIdentifier,
        OnboardingBlockingAnswerOutcome outcome
) {

    public OnboardingAnswerOutcomeRule {
        Objects.requireNonNull(promptKey, "promptKey");
        Objects.requireNonNull(
                questionDefinitionVersion,
                "questionDefinitionVersion"
        );
        if (optionIdentifier == null || optionIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Option identifier must not be blank"
            );
        }
        Objects.requireNonNull(outcome, "outcome");
    }
}
