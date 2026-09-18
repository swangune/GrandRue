package grandrue.semantic.configuration;

import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseRevision;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Retry-safe request to consume one immutable initial configuration intent. */
public record MaterialiseInitialConfigurationRevisionCommand(
        InitialConfigurationIntentIdentity initialConfigurationIntentIdentity,
        String requestedConfigurationRevisionIdentifier,
        OnboardingCaseRevision onboardingCompletionRevision,
        String materialisedBy,
        Optional<String> originIdentifier,
        Instant materialisedAt
) {

    public MaterialiseInitialConfigurationRevisionCommand {
        Objects.requireNonNull(
                initialConfigurationIntentIdentity,
                "initialConfigurationIntentIdentity"
        );
        requireNonBlank(
                requestedConfigurationRevisionIdentifier,
                "Requested Configuration Revision identifier"
        );
        Objects.requireNonNull(
                onboardingCompletionRevision,
                "onboardingCompletionRevision"
        );
        requireNonBlank(materialisedBy, "Materialising principal");
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(origin ->
                requireNonBlank(origin, "Origin identifier")
        );
        Objects.requireNonNull(materialisedAt, "materialisedAt");
    }

    private static void requireNonBlank(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
