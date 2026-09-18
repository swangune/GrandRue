package mainstreet.onboarding;

import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Expected-revision and retry-identifiable answer/correction mutation.
 */
public record RecordOnboardingAnswerCommand(
        OnboardingCaseIdentity onboardingCaseIdentity,
        OnboardingCaseRevision expectedRevision,
        OnboardingCaseRevision replacementRevision,
        String mutationRequestIdentifier,
        String answerEvidenceIdentifier,
        OnboardingAnswerEvidence answer,
        String principalReference,
        Optional<String> originIdentifier,
        Instant committedAt
) {

    public RecordOnboardingAnswerCommand {
        Objects.requireNonNull(
                onboardingCaseIdentity,
                "onboardingCaseIdentity"
        );
        Objects.requireNonNull(expectedRevision, "expectedRevision");
        Objects.requireNonNull(replacementRevision, "replacementRevision");
        if (expectedRevision.equals(replacementRevision)) {
            throw new IllegalArgumentException(
                    "Replacement case revision must differ from expected revision"
            );
        }
        require(mutationRequestIdentifier, "mutationRequestIdentifier");
        require(answerEvidenceIdentifier, "answerEvidenceIdentifier");
        Objects.requireNonNull(answer, "answer");
        require(principalReference, "principalReference");
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(value ->
                require(value, "originIdentifier")
        );
        Objects.requireNonNull(committedAt, "committedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
