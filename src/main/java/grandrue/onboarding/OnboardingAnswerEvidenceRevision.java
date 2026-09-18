package grandrue.onboarding;

import grandrue.onboarding.OnboardingAnswerEvidence;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable answer evidence recorded by one exact Onboarding Case revision.
 */
public record OnboardingAnswerEvidenceRevision(
        String answerEvidenceIdentifier,
        OnboardingCaseIdentity onboardingCaseIdentity,
        OnboardingCaseRevision recordedAtRevision,
        Optional<String> supersedesAnswerEvidenceIdentifier,
        OnboardingAnswerEvidence answer
) {

    public OnboardingAnswerEvidenceRevision {
        require(answerEvidenceIdentifier, "answerEvidenceIdentifier");
        Objects.requireNonNull(
                onboardingCaseIdentity,
                "onboardingCaseIdentity"
        );
        Objects.requireNonNull(recordedAtRevision, "recordedAtRevision");
        supersedesAnswerEvidenceIdentifier = Objects.requireNonNull(
                supersedesAnswerEvidenceIdentifier,
                "supersedesAnswerEvidenceIdentifier"
        );
        supersedesAnswerEvidenceIdentifier.ifPresent(value ->
                require(value, "supersedesAnswerEvidenceIdentifier")
        );
        Objects.requireNonNull(answer, "answer");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
