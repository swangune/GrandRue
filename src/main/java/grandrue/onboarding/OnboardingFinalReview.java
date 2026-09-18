package grandrue.onboarding;

import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingSemanticSeed;
import grandrue.onboarding.OnboardingCaseReview;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Exact business-intent review projection for one Onboarding Case revision.
 *
 * <p>The review is not merchant approval and does not authorise submission.</p>
 */
public record OnboardingFinalReview(
        OnboardingCaseReview affinity,
        Set<OnboardingSemanticSeed> candidateRegisteredIntent,
        List<String> provenanceReferences,
        List<OnboardingPromptKey> unresolvedPromptKeys
) {

    public OnboardingFinalReview {
        Objects.requireNonNull(affinity, "affinity");
        candidateRegisteredIntent = Set.copyOf(
                Objects.requireNonNull(
                        candidateRegisteredIntent,
                        "candidateRegisteredIntent"
                )
        );
        provenanceReferences = List.copyOf(
                Objects.requireNonNull(
                        provenanceReferences,
                        "provenanceReferences"
                )
        );
        provenanceReferences.forEach(reference -> {
            if (reference == null || reference.isBlank()) {
                throw new IllegalArgumentException(
                        "Provenance reference must not be blank"
                );
            }
        });
        if (new HashSet<>(provenanceReferences).size()
                != provenanceReferences.size()) {
            throw new IllegalArgumentException(
                    "Provenance references must be unique"
            );
        }
        unresolvedPromptKeys = List.copyOf(
                Objects.requireNonNull(
                        unresolvedPromptKeys,
                        "unresolvedPromptKeys"
                )
        );
        if (new HashSet<>(unresolvedPromptKeys).size()
                != unresolvedPromptKeys.size()) {
            throw new IllegalArgumentException(
                    "Unresolved prompt keys must be unique"
            );
        }
    }
}
