package mainstreet.onboarding;

import grandrue.onboarding.OnboardingAnswerEvidenceRevision;
import grandrue.onboarding.OnboardingCase;
import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingSemanticSeed;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Deterministically reconstructs candidate semantic seeds and the eligible
 * unresolved prompt frontier from effective answer evidence.
 */
public final class DeterministicOnboardingRecomputation {

    private final OnboardingPromptRegistry registry;

    public DeterministicOnboardingRecomputation(
            OnboardingPromptRegistry registry
    ) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    public OnboardingRecomputation recompute(
            OnboardingCase caseAtRevision,
            List<OnboardingAnswerEvidenceRevision> effectiveEvidence
    ) {
        Objects.requireNonNull(caseAtRevision, "caseAtRevision");
        Objects.requireNonNull(effectiveEvidence, "effectiveEvidence");

        LinkedHashMap<OnboardingPromptKey, OnboardingAnswerEvidenceRevision>
                evidenceByPrompt = new LinkedHashMap<>();
        for (OnboardingAnswerEvidenceRevision evidence : effectiveEvidence) {
            Objects.requireNonNull(evidence, "answer evidence");
            if (!caseAtRevision.identity().equals(
                    evidence.onboardingCaseIdentity()
            )) {
                throw new IllegalArgumentException(
                        "Answer evidence belongs to a different Onboarding Case"
                );
            }
            OnboardingPromptKey key = new OnboardingPromptKey(
                    evidence.answer().questionIdentity(),
                    evidence.answer().contextScopeReference()
            );
            if (evidenceByPrompt.putIfAbsent(key, evidence) != null) {
                throw new IllegalArgumentException(
                        "Effective evidence contains duplicate prompt keys"
                );
            }
        }

        List<OnboardingPromptDefinition> stableDefinitions =
                registry.promptDefinitions().stream()
                        .sorted(stableDefinitionOrder())
                        .toList();
        LinkedHashSet<OnboardingSemanticSeed> candidateSeeds =
                new LinkedHashSet<>();
        LinkedHashMap<OnboardingPromptDefinition,
                OnboardingAnswerEvidenceRevision> candidateEvidence =
                new LinkedHashMap<>();

        boolean changed;
        do {
            changed = false;
            for (OnboardingPromptDefinition definition : stableDefinitions) {
                if (candidateEvidence.containsKey(definition)
                        || !definition.applicability()
                                .isSatisfiedBy(candidateSeeds)) {
                    continue;
                }
                OnboardingAnswerEvidenceRevision evidence =
                        evidenceByPrompt.get(definition.key());
                if (evidence == null
                        || !definition.accepts(evidence.answer())) {
                    continue;
                }
                candidateEvidence.put(definition, evidence);
                candidateSeeds.addAll(
                        registry.candidateSeeds(evidence.answer())
                );
                changed = true;
            }
        } while (changed);

        List<OnboardingPromptDefinition> frontier =
                stableDefinitions.stream()
                        .filter(definition -> definition.applicability()
                                .isSatisfiedBy(candidateSeeds))
                        .filter(definition ->
                                !candidateEvidence.containsKey(definition))
                        .sorted(frontierOrder(candidateSeeds))
                        .toList();

        Set<OnboardingAnswerEvidenceRevision> included = new HashSet<>(
                candidateEvidence.values()
        );
        List<OnboardingAnswerEvidenceRevision> excluded =
                effectiveEvidence.stream()
                        .filter(evidence -> !included.contains(evidence))
                        .toList();

        return new OnboardingRecomputation(
                caseAtRevision,
                candidateSeeds,
                frontier,
                new ArrayList<>(candidateEvidence.values()),
                excluded
        );
    }

    private Comparator<OnboardingPromptDefinition> frontierOrder(
            Set<OnboardingSemanticSeed> candidateSeeds
    ) {
        return Comparator
                .comparingInt((OnboardingPromptDefinition definition) ->
                        definition.priority().ordinal())
                .thenComparing(
                        Comparator.comparingInt(
                                (OnboardingPromptDefinition definition) ->
                                        registry.informationGain(
                                                definition,
                                                candidateSeeds
                                        )
                        ).reversed()
                )
                .thenComparing(stableDefinitionOrder());
    }

    private static Comparator<OnboardingPromptDefinition>
            stableDefinitionOrder() {
        return Comparator
                .comparing((OnboardingPromptDefinition definition) ->
                        definition.key().questionIdentity().namespace())
                .thenComparing(definition ->
                        definition.key().questionIdentity().identifier())
                .thenComparing(definition ->
                        definition.key().contextScopeReference().orElse(""));
    }
}
