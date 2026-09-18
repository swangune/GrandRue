package grandrue.onboarding;

import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingSemanticSeed;
import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingQuestionIdentity;
import grandrue.onboarding.OnboardingDiscoveryMapping;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable onboarding prompt and discovery-mapping registry.
 *
 * <p>This registry presents registered choices. It is deliberately distinct
 * from the Semantic Registry and cannot activate runtime semantics.</p>
 */
public final class OnboardingPromptRegistry {

    private final List<OnboardingPromptDefinition> promptDefinitions;
    private final List<OnboardingDiscoveryMapping> discoveryMappings;
    private final Map<OnboardingPromptKey, OnboardingPromptDefinition>
            definitionsByKey;
    private final Map<MappingKey, OnboardingDiscoveryMapping> mappingsByKey;

    public OnboardingPromptRegistry(
            List<OnboardingPromptDefinition> promptDefinitions,
            List<OnboardingDiscoveryMapping> discoveryMappings
    ) {
        this.promptDefinitions = List.copyOf(
                Objects.requireNonNull(
                        promptDefinitions,
                        "promptDefinitions"
                )
        );
        this.discoveryMappings = List.copyOf(
                Objects.requireNonNull(
                        discoveryMappings,
                        "discoveryMappings"
                )
        );
        LinkedHashMap<OnboardingPromptKey, OnboardingPromptDefinition>
                definitions = new LinkedHashMap<>();
        for (OnboardingPromptDefinition definition : this.promptDefinitions) {
            Objects.requireNonNull(definition, "prompt definition");
            if (definitions.putIfAbsent(
                    definition.key(),
                    definition
            ) != null) {
                throw new IllegalArgumentException(
                        "Duplicate onboarding prompt key: " + definition.key()
                );
            }
        }
        definitionsByKey = Map.copyOf(definitions);

        LinkedHashMap<MappingKey, OnboardingDiscoveryMapping> mappings =
                new LinkedHashMap<>();
        for (OnboardingDiscoveryMapping mapping : this.discoveryMappings) {
            Objects.requireNonNull(mapping, "discovery mapping");
            boolean registeredOption = this.promptDefinitions.stream()
                    .filter(definition -> definition.key()
                            .questionIdentity()
                            .equals(mapping.sourceQuestionIdentity()))
                    .map(OnboardingPromptDefinition
                            ::acceptedOptionIdentifiersByVersion)
                    .map(options -> options.get(
                            mapping.sourceQuestionVersion()
                    ))
                    .filter(Objects::nonNull)
                    .anyMatch(options -> options.contains(
                            mapping.sourceOptionIdentifier()
                    ));
            if (!registeredOption) {
                throw new IllegalArgumentException(
                        "Discovery mapping references an unregistered "
                                + "question version or option"
                );
            }
            MappingKey key = MappingKey.from(mapping);
            if (mappings.putIfAbsent(key, mapping) != null) {
                throw new IllegalArgumentException(
                        "Duplicate onboarding discovery mapping: " + key
                );
            }
        }
        mappingsByKey = Map.copyOf(mappings);
    }

    public List<OnboardingPromptDefinition> promptDefinitions() {
        return promptDefinitions;
    }

    public List<OnboardingDiscoveryMapping> discoveryMappings() {
        return discoveryMappings;
    }

    public Optional<OnboardingPromptDefinition> definitionFor(
            OnboardingPromptKey key
    ) {
        return Optional.ofNullable(definitionsByKey.get(key));
    }

    public Set<OnboardingSemanticSeed> candidateSeeds(
            OnboardingAnswerEvidence answer
    ) {
        LinkedHashSet<OnboardingSemanticSeed> seeds = new LinkedHashSet<>();
        answer.answerOptionIdentifiers().forEach(option ->
                Optional.ofNullable(mappingsByKey.get(new MappingKey(
                        answer.questionIdentity(),
                        answer.questionDefinitionVersion(),
                        option
                ))).ifPresent(mapping ->
                        seeds.addAll(mapping.proposedSemanticSeeds())
                )
        );
        return Set.copyOf(seeds);
    }

    public int informationGain(
            OnboardingPromptDefinition definition,
            Set<OnboardingSemanticSeed> currentSeeds
    ) {
        Set<OnboardingSemanticSeed> potential =
                potentialSeeds(definition);
        if (potential.isEmpty()) {
            return 0;
        }
        LinkedHashSet<OnboardingSemanticSeed> prospective =
                new LinkedHashSet<>(currentSeeds);
        prospective.addAll(potential);
        return Math.toIntExact(promptDefinitions.stream()
                .filter(candidate ->
                        !candidate.applicability().isSatisfiedBy(currentSeeds))
                .filter(candidate ->
                        candidate.applicability().isSatisfiedBy(prospective))
                .count());
    }

    private Set<OnboardingSemanticSeed> potentialSeeds(
            OnboardingPromptDefinition definition
    ) {
        LinkedHashSet<OnboardingSemanticSeed> seeds = new LinkedHashSet<>();
        definition.currentOptionIdentifiers().forEach(option ->
                Optional.ofNullable(mappingsByKey.get(new MappingKey(
                        definition.key().questionIdentity(),
                        definition.currentDefinitionVersion(),
                        option
                ))).ifPresent(mapping ->
                        seeds.addAll(mapping.proposedSemanticSeeds())
                )
        );
        return Set.copyOf(seeds);
    }

    private record MappingKey(
            OnboardingQuestionIdentity questionIdentity,
            OnboardingQuestionDefinitionVersion questionVersion,
            String optionIdentifier
    ) {
        private static MappingKey from(OnboardingDiscoveryMapping mapping) {
            return new MappingKey(
                    mapping.sourceQuestionIdentity(),
                    mapping.sourceQuestionVersion(),
                    mapping.sourceOptionIdentifier()
            );
        }
    }
}
