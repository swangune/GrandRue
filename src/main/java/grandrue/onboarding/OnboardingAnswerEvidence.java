package grandrue.onboarding;

import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingQuestionIdentity;
import grandrue.onboarding.OnboardingAnswerOrigin;
import grandrue.onboarding.OnboardingAnswerForm;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Exact, non-executable onboarding answer evidence and provenance.
 */
public record OnboardingAnswerEvidence(
        OnboardingQuestionIdentity questionIdentity,
        OnboardingQuestionDefinitionVersion questionDefinitionVersion,
        OnboardingAnswerForm answerForm,
        Set<String> answerOptionIdentifiers,
        Optional<String> structuredValueReference,
        Optional<String> contextScopeReference,
        OnboardingAnswerOrigin answerOrigin,
        String answeredBy,
        Instant answeredAt,
        Optional<String> semanticRegistryRelease
) {

    public OnboardingAnswerEvidence {
        Objects.requireNonNull(questionIdentity, "questionIdentity");
        Objects.requireNonNull(
                questionDefinitionVersion,
                "questionDefinitionVersion"
        );
        Objects.requireNonNull(answerForm, "answerForm");
        answerOptionIdentifiers = Set.copyOf(
                Objects.requireNonNull(
                        answerOptionIdentifiers,
                        "answerOptionIdentifiers"
                )
        );
        for (String option : answerOptionIdentifiers) {
            require(option, "answerOptionIdentifier");
        }
        structuredValueReference = Objects.requireNonNull(
                structuredValueReference,
                "structuredValueReference"
        );
        structuredValueReference.ifPresent(value ->
                require(value, "structuredValueReference")
        );
        boolean hasOptions = !answerOptionIdentifiers.isEmpty();
        boolean hasStructuredValue = structuredValueReference.isPresent();
        if (hasOptions == hasStructuredValue) {
            throw new IllegalArgumentException(
                    "Answer evidence requires exactly one option-set or structured-value reference"
            );
        }
        contextScopeReference = Objects.requireNonNull(
                contextScopeReference,
                "contextScopeReference"
        );
        contextScopeReference.ifPresent(value ->
                require(value, "contextScopeReference")
        );
        Objects.requireNonNull(answerOrigin, "answerOrigin");
        require(answeredBy, "answeredBy");
        Objects.requireNonNull(answeredAt, "answeredAt");
        semanticRegistryRelease = Objects.requireNonNull(
                semanticRegistryRelease,
                "semanticRegistryRelease"
        );
        semanticRegistryRelease.ifPresent(value ->
                require(value, "semanticRegistryRelease")
        );
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
