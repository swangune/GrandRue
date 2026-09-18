package grandrue.onboarding;

import grandrue.onboarding.OnboardingAnswerEvidenceRevision;
import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingAnswerOutcomeRule;
import grandrue.onboarding.OnboardingBlockingAnswerOutcome;
import grandrue.onboarding.OnboardingPromptCompletionRequirement;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Registered completeness policy for the currently supported onboarding
 * catalogue.
 *
 * <p>Every applicable prompt must be explicitly classified REQUIRED or
 * OPTIONAL. This prevents missing catalogue policy from silently becoming
 * submission-ready.</p>
 */
public final class OnboardingCompletionPolicy {

    private final Map<OnboardingPromptKey,
            OnboardingPromptCompletionRequirement> requirements;
    private final Map<OutcomeKey, OnboardingBlockingAnswerOutcome>
            blockingOutcomes;

    public OnboardingCompletionPolicy(
            Map<OnboardingPromptKey,
                    OnboardingPromptCompletionRequirement> requirements,
            List<OnboardingAnswerOutcomeRule> answerOutcomeRules
    ) {
        this.requirements = Map.copyOf(
                Objects.requireNonNull(requirements, "requirements")
        );
        this.requirements.forEach((key, requirement) -> {
            Objects.requireNonNull(key, "prompt key");
            Objects.requireNonNull(requirement, "completion requirement");
        });
        Objects.requireNonNull(answerOutcomeRules, "answerOutcomeRules");
        LinkedHashMap<OutcomeKey, OnboardingBlockingAnswerOutcome> outcomes =
                new LinkedHashMap<>();
        for (OnboardingAnswerOutcomeRule rule : answerOutcomeRules) {
            Objects.requireNonNull(rule, "answer outcome rule");
            OutcomeKey key = OutcomeKey.from(rule);
            if (outcomes.putIfAbsent(key, rule.outcome()) != null) {
                throw new IllegalArgumentException(
                        "Duplicate onboarding answer outcome rule: " + key
                );
            }
        }
        blockingOutcomes = Map.copyOf(outcomes);
    }

    public static OnboardingCompletionPolicy empty() {
        return new OnboardingCompletionPolicy(Map.of(), List.of());
    }

    public Optional<OnboardingPromptCompletionRequirement> requirementFor(
            OnboardingPromptKey key
    ) {
        return Optional.ofNullable(requirements.get(key));
    }

    public Set<OnboardingBlockingAnswerOutcome> blockingOutcomesFor(
            OnboardingAnswerEvidenceRevision evidence
    ) {
        Objects.requireNonNull(evidence, "evidence");
        OnboardingAnswerEvidence answer = evidence.answer();
        OnboardingPromptKey key = new OnboardingPromptKey(
                answer.questionIdentity(),
                answer.contextScopeReference()
        );
        LinkedHashSet<OnboardingBlockingAnswerOutcome> outcomes =
                new LinkedHashSet<>();
        answer.answerOptionIdentifiers().forEach(option ->
                Optional.ofNullable(blockingOutcomes.get(new OutcomeKey(
                        key,
                        answer.questionDefinitionVersion(),
                        option
                ))).ifPresent(outcomes::add)
        );
        return Set.copyOf(outcomes);
    }

    private record OutcomeKey(
            OnboardingPromptKey promptKey,
            OnboardingQuestionDefinitionVersion questionVersion,
            String optionIdentifier
    ) {
        private static OutcomeKey from(OnboardingAnswerOutcomeRule rule) {
            return new OutcomeKey(
                    rule.promptKey(),
                    rule.questionDefinitionVersion(),
                    rule.optionIdentifier()
            );
        }
    }
}
