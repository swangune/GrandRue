package grandrue.onboarding;

import mainstreet.onboarding.OnboardingAnswerMutationResult;
import mainstreet.onboarding.OnboardingRecomputation;
import java.util.Objects;

/**
 * Exact answer-mutation outcome paired with deterministic recomputation of the
 * same committed case revision.
 */
public record OnboardingAnswerRecomputationResult(
        OnboardingAnswerMutationResult answerMutation,
        OnboardingRecomputation recomputation
) {

    public OnboardingAnswerRecomputationResult {
        Objects.requireNonNull(answerMutation, "answerMutation");
        Objects.requireNonNull(recomputation, "recomputation");
        if (!answerMutation.caseAtCommit().equals(
                recomputation.caseAtRevision()
        )) {
            throw new IllegalArgumentException(
                    "Answer mutation and recomputation must share an exact "
                            + "Onboarding Case revision"
            );
        }
    }
}
