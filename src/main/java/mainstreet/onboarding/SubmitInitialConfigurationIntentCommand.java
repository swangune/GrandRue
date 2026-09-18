package mainstreet.onboarding;

import grandrue.onboarding.OnboardingCaseRevision;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Retry-identifiable request to submit one exact reviewed onboarding state. */
public record SubmitInitialConfigurationIntentCommand(
        String submissionRequestIdentifier,
        InitialConfigurationIntentIdentity intentIdentity,
        OnboardingFinalReview reviewedIntent,
        OnboardingCaseRevision submittedCaseRevision,
        String principalReference,
        Optional<String> originIdentifier,
        Instant submittedAt
) {

    public SubmitInitialConfigurationIntentCommand {
        if (submissionRequestIdentifier == null
                || submissionRequestIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Submission request identifier must not be blank"
            );
        }
        Objects.requireNonNull(intentIdentity, "intentIdentity");
        Objects.requireNonNull(reviewedIntent, "reviewedIntent");
        Objects.requireNonNull(
                submittedCaseRevision,
                "submittedCaseRevision"
        );
        if (submittedCaseRevision.equals(
                reviewedIntent.affinity().reviewedRevision()
        )) {
            throw new IllegalArgumentException(
                    "Submitted case revision must advance the reviewed revision"
            );
        }
        if (principalReference == null || principalReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Principal reference must not be blank"
            );
        }
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(origin -> {
            if (origin.isBlank()) {
                throw new IllegalArgumentException(
                        "Origin identifier must not be blank"
                );
            }
        });
        Objects.requireNonNull(submittedAt, "submittedAt");
    }
}
