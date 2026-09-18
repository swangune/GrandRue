package mainstreet.onboarding;

import grandrue.onboarding.OnboardingCaseRevision;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Configuration-bootstrap acknowledgement that advances the source case only
 * after responsibility for its immutable intent has been accepted.
 */
public record AcknowledgeInitialConfigurationIntentCommand(
        InitialConfigurationIntentIdentity intentIdentity,
        OnboardingCaseRevision completionCaseRevision,
        String acknowledgementRequestIdentifier,
        String principalReference,
        Optional<String> originIdentifier,
        Instant acknowledgedAt
) {

    public AcknowledgeInitialConfigurationIntentCommand {
        Objects.requireNonNull(intentIdentity, "intentIdentity");
        Objects.requireNonNull(
                completionCaseRevision,
                "completionCaseRevision"
        );
        requireNonBlank(
                acknowledgementRequestIdentifier,
                "Acknowledgement request identifier"
        );
        requireNonBlank(principalReference, "Principal reference");
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(origin ->
                requireNonBlank(origin, "Origin identifier")
        );
        Objects.requireNonNull(acknowledgedAt, "acknowledgedAt");
    }

    private static void requireNonBlank(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
