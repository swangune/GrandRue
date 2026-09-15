package mainstreet.onboarding;

import java.util.List;
import java.util.Objects;

/**
 * Exact effective Onboarding Case evidence reconstructed at one logical
 * revision.
 */
public record OnboardingCaseEvidenceSnapshot(
        OnboardingCase caseAtRevision,
        List<OnboardingAnswerEvidenceRevision> effectiveAnswers
) {

    public OnboardingCaseEvidenceSnapshot {
        Objects.requireNonNull(caseAtRevision, "caseAtRevision");
        effectiveAnswers = List.copyOf(
                Objects.requireNonNull(effectiveAnswers, "effectiveAnswers")
        );
        effectiveAnswers.forEach(answer -> {
            if (!caseAtRevision.identity().equals(
                    answer.onboardingCaseIdentity()
            )) {
                throw new IllegalArgumentException(
                        "Snapshot answer belongs to a different Onboarding Case"
                );
            }
        });
    }
}
