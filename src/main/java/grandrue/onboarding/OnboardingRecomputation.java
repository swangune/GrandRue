package grandrue.onboarding;

import mainstreet.onboarding.OnboardingPromptDefinition;
import grandrue.onboarding.OnboardingAnswerEvidenceRevision;
import grandrue.onboarding.OnboardingCase;
import grandrue.onboarding.OnboardingSemanticSeed;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Deterministic, revision-affined onboarding candidate and prompt frontier.
 *
 * <p>The candidate remains non-executable onboarding state.</p>
 */
public record OnboardingRecomputation(
        OnboardingCase caseAtRevision,
        Set<OnboardingSemanticSeed> candidateSemanticSeeds,
        List<OnboardingPromptDefinition> unresolvedFrontier,
        List<OnboardingAnswerEvidenceRevision> candidateEvidence,
        List<OnboardingAnswerEvidenceRevision> excludedEvidence
) {

    public OnboardingRecomputation {
        Objects.requireNonNull(caseAtRevision, "caseAtRevision");
        candidateSemanticSeeds = Set.copyOf(
                Objects.requireNonNull(
                        candidateSemanticSeeds,
                        "candidateSemanticSeeds"
                )
        );
        unresolvedFrontier = List.copyOf(
                Objects.requireNonNull(
                        unresolvedFrontier,
                        "unresolvedFrontier"
                )
        );
        candidateEvidence = List.copyOf(
                Objects.requireNonNull(
                        candidateEvidence,
                        "candidateEvidence"
                )
        );
        excludedEvidence = List.copyOf(
                Objects.requireNonNull(
                        excludedEvidence,
                        "excludedEvidence"
                )
        );
    }

    public Optional<OnboardingPromptDefinition> nextPrompt() {
        return unresolvedFrontier.stream().findFirst();
    }
}
