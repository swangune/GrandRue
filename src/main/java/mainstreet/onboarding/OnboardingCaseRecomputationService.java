package mainstreet.onboarding;

import grandrue.onboarding.OnboardingAnswerMutationResult;
import grandrue.onboarding.OnboardingCaseEvidenceSnapshot;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.onboarding.OnboardingAnswerRecomputationResult;
import java.util.Objects;

/**
 * Application boundary that recomputes onboarding state from the exact durable
 * evidence revision committed by an answer mutation.
 */
public final class OnboardingCaseRecomputationService {

    private final OnboardingCaseEvidenceStore evidenceStore;
    private final DeterministicOnboardingRecomputation recomputation;

    public OnboardingCaseRecomputationService(
            OnboardingCaseEvidenceStore evidenceStore,
            DeterministicOnboardingRecomputation recomputation
    ) {
        this.evidenceStore = Objects.requireNonNull(
                evidenceStore,
                "evidenceStore"
        );
        this.recomputation = Objects.requireNonNull(
                recomputation,
                "recomputation"
        );
    }

    public OnboardingAnswerRecomputationResult recordAnswerAndRecompute(
            RecordOnboardingAnswerCommand command
    ) {
        Objects.requireNonNull(command, "command");
        OnboardingAnswerMutationResult mutation =
                evidenceStore.recordAnswer(command);
        OnboardingRecomputation result = recomputeAtRevision(
                mutation.caseAtCommit().identity(),
                mutation.caseAtCommit().currentRevision()
        );
        return new OnboardingAnswerRecomputationResult(mutation, result);
    }

    public OnboardingRecomputation recomputeAtRevision(
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision revision
    ) {
        OnboardingCaseEvidenceSnapshot snapshot =
                evidenceStore.evidenceSnapshotAtRevision(
                        Objects.requireNonNull(caseIdentity, "caseIdentity"),
                        Objects.requireNonNull(revision, "revision")
                );
        return recomputation.recompute(
                snapshot.caseAtRevision(),
                snapshot.effectiveAnswers()
        );
    }
}
