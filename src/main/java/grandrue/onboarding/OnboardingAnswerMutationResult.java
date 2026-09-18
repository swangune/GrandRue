package grandrue.onboarding;

import java.util.Objects;

/**
 * Exact committed result of one answer mutation, suitable for retry recovery.
 */
public record OnboardingAnswerMutationResult(
        OnboardingCase caseAtCommit,
        OnboardingAnswerEvidenceRevision answerEvidence
) {

    public OnboardingAnswerMutationResult {
        Objects.requireNonNull(caseAtCommit, "caseAtCommit");
        Objects.requireNonNull(answerEvidence, "answerEvidence");
        if (!caseAtCommit.identity().equals(
                answerEvidence.onboardingCaseIdentity()
        )) {
            throw new IllegalArgumentException(
                    "Answer evidence must belong to the committed case"
            );
        }
        if (!caseAtCommit.currentRevision().equals(
                answerEvidence.recordedAtRevision()
        )) {
            throw new IllegalArgumentException(
                    "Answer evidence must belong to the committed case revision"
            );
        }
    }
}
