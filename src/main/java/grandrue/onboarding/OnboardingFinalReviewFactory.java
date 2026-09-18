package grandrue.onboarding;

import mainstreet.onboarding.OnboardingFinalReview;
import mainstreet.onboarding.OnboardingRecomputation;
import mainstreet.onboarding.OnboardingCase;
import grandrue.onboarding.OnboardingCaseReview;
import mainstreet.onboarding.OnboardingAnswerEvidenceRevision;
import mainstreet.onboarding.OnboardingPromptDefinition;
import java.util.Objects;

/**
 * Creates an exact final-review projection from deterministic recomputation.
 */
public final class OnboardingFinalReviewFactory {

    public OnboardingFinalReview create(
            OnboardingRecomputation recomputation
    ) {
        Objects.requireNonNull(recomputation, "recomputation");
        OnboardingCase source = recomputation.caseAtRevision();
        return new OnboardingFinalReview(
                new OnboardingCaseReview(
                        source.identity(),
                        source.currentRevision()
                ),
                recomputation.candidateSemanticSeeds(),
                recomputation.candidateEvidence().stream()
                        .map(OnboardingAnswerEvidenceRevision
                                ::answerEvidenceIdentifier)
                        .toList(),
                recomputation.unresolvedFrontier().stream()
                        .map(OnboardingPromptDefinition::key)
                        .toList()
        );
    }
}
