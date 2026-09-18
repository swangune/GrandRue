package grandrue.onboarding;

import java.util.Objects;
import java.util.Optional;

/**
 * Exact question and semantic context resolved by one prompt instance.
 */
public record OnboardingPromptKey(
        OnboardingQuestionIdentity questionIdentity,
        Optional<String> contextScopeReference
) {

    public OnboardingPromptKey {
        Objects.requireNonNull(questionIdentity, "questionIdentity");
        contextScopeReference = Objects.requireNonNull(
                contextScopeReference,
                "contextScopeReference"
        );
        contextScopeReference.ifPresent(value -> {
            if (value.isBlank()) {
                throw new IllegalArgumentException(
                        "Context scope reference must not be blank"
                );
            }
        });
    }
}
