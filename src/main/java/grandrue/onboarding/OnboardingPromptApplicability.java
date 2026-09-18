package grandrue.onboarding;

import grandrue.onboarding.OnboardingSemanticSeed;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Registered deterministic prompt-applicability clauses.
 *
 * <p>Each inner set is an all-of clause. A prompt is eligible when any clause
 * is satisfied. An empty clause represents an independently eligible root
 * prompt.</p>
 */
public record OnboardingPromptApplicability(
        List<Set<OnboardingSemanticSeed>> satisfiedByAnyClause
) {

    public OnboardingPromptApplicability {
        Objects.requireNonNull(
                satisfiedByAnyClause,
                "satisfiedByAnyClause"
        );
        if (satisfiedByAnyClause.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one applicability clause is required"
            );
        }
        satisfiedByAnyClause = satisfiedByAnyClause.stream()
                .map(clause -> Set.copyOf(
                        Objects.requireNonNull(clause, "applicability clause")
                ))
                .toList();
    }

    public static OnboardingPromptApplicability always() {
        return new OnboardingPromptApplicability(List.of(Set.of()));
    }

    public static OnboardingPromptApplicability requiringAll(
            Set<OnboardingSemanticSeed> requiredSeeds
    ) {
        return new OnboardingPromptApplicability(List.of(requiredSeeds));
    }

    public static OnboardingPromptApplicability satisfiedByAnyOf(
            List<Set<OnboardingSemanticSeed>> alternativeClauses
    ) {
        return new OnboardingPromptApplicability(alternativeClauses);
    }

    public boolean isSatisfiedBy(Set<OnboardingSemanticSeed> candidateSeeds) {
        Objects.requireNonNull(candidateSeeds, "candidateSeeds");
        return satisfiedByAnyClause.stream()
                .anyMatch(candidateSeeds::containsAll);
    }

    public Set<OnboardingSemanticSeed> referencedSemanticSeeds() {
        LinkedHashSet<OnboardingSemanticSeed> seeds = new LinkedHashSet<>();
        satisfiedByAnyClause.forEach(seeds::addAll);
        return Set.copyOf(seeds);
    }
}
