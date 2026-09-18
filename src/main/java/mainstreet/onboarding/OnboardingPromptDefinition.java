package mainstreet.onboarding;

import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingAnswerConstraint;
import grandrue.onboarding.OnboardingPromptPriority;
import grandrue.onboarding.OnboardingAnswerForm;
import grandrue.onboarding.OnboardingPromptClass;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Current registered definition for one scoped onboarding prompt.
 *
 * <p>Accepted answer options are retained per material definition version so
 * historical evidence is reused only by an explicit registered contract.</p>
 */
public record OnboardingPromptDefinition(
        OnboardingPromptKey key,
        OnboardingQuestionDefinitionVersion currentDefinitionVersion,
        OnboardingPromptClass promptClass,
        OnboardingAnswerForm answerForm,
        OnboardingPromptPriority priority,
        Map<OnboardingQuestionDefinitionVersion, Set<String>>
                acceptedOptionIdentifiersByVersion,
        OnboardingPromptApplicability applicability,
        OnboardingAnswerConstraint answerConstraint
) {

    public OnboardingPromptDefinition(
            OnboardingPromptKey key,
            OnboardingQuestionDefinitionVersion currentDefinitionVersion,
            OnboardingPromptClass promptClass,
            OnboardingAnswerForm answerForm,
            OnboardingPromptPriority priority,
            Map<OnboardingQuestionDefinitionVersion, Set<String>>
                    acceptedOptionIdentifiersByVersion,
            OnboardingPromptApplicability applicability
    ) {
        this(
                key,
                currentDefinitionVersion,
                promptClass,
                answerForm,
                priority,
                acceptedOptionIdentifiersByVersion,
                applicability,
                OnboardingAnswerConstraint.unrestricted()
        );
    }

    public OnboardingPromptDefinition {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(
                currentDefinitionVersion,
                "currentDefinitionVersion"
        );
        Objects.requireNonNull(promptClass, "promptClass");
        Objects.requireNonNull(answerForm, "answerForm");
        Objects.requireNonNull(priority, "priority");
        Objects.requireNonNull(
                acceptedOptionIdentifiersByVersion,
                "acceptedOptionIdentifiersByVersion"
        );
        LinkedHashMap<OnboardingQuestionDefinitionVersion, Set<String>> copy =
                new LinkedHashMap<>();
        acceptedOptionIdentifiersByVersion.forEach((version, options) -> {
            Objects.requireNonNull(version, "answer definition version");
            Set<String> copiedOptions = Set.copyOf(
                    Objects.requireNonNull(options, "answer option identifiers")
            );
            copiedOptions.forEach(option -> {
                if (option == null || option.isBlank()) {
                    throw new IllegalArgumentException(
                            "Answer option identifier must not be blank"
                    );
                }
            });
            copy.put(version, copiedOptions);
        });
        if (!copy.containsKey(currentDefinitionVersion)) {
            throw new IllegalArgumentException(
                    "Current question definition version must be registered"
            );
        }
        acceptedOptionIdentifiersByVersion = Map.copyOf(copy);
        Objects.requireNonNull(applicability, "applicability");
        Objects.requireNonNull(answerConstraint, "answerConstraint");
    }

    public boolean accepts(OnboardingAnswerEvidence answer) {
        Objects.requireNonNull(answer, "answer");
        if (!key.questionIdentity().equals(answer.questionIdentity())
                || !key.contextScopeReference().equals(
                        answer.contextScopeReference()
                )
                || answerForm != answer.answerForm()) {
            return false;
        }
        Set<String> accepted = acceptedOptionIdentifiersByVersion.get(
                answer.questionDefinitionVersion()
        );
        if (accepted == null) {
            return false;
        }
        if (answer.structuredValueReference().isPresent()) {
            return accepted.isEmpty()
                    && answerConstraint.isSatisfiedBy(Set.of());
        }
        Set<String> supplied = answer.answerOptionIdentifiers();
        if (supplied.isEmpty() || !accepted.containsAll(supplied)) {
            return false;
        }
        if ((answerForm == OnboardingAnswerForm.SINGLE_SELECT
                || answerForm == OnboardingAnswerForm.BOOLEAN)
                && supplied.size() != 1) {
            return false;
        }
        return answerConstraint.isSatisfiedBy(supplied);
    }

    public Set<String> currentOptionIdentifiers() {
        return acceptedOptionIdentifiersByVersion.get(
                currentDefinitionVersion
        );
    }
}
