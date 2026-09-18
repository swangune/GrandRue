package grandrue.semantic.configuration;

import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.onboarding.OnboardingCaseRevision;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable authoritative Merchant Configuration Revision content and source
 * provenance. Existence does not imply validation, approval or activation.
 */
public record MerchantConfigurationRevision(
        MerchantConfiguration configuration,
        InitialConfigurationIntentIdentity
                sourceInitialConfigurationIntentIdentity,
        OnboardingCaseIdentity sourceOnboardingCaseIdentity,
        OnboardingCaseRevision sourceOnboardingCaseRevision,
        OnboardingCaseRevision onboardingCompletionRevision,
        String materialisedBy,
        Optional<String> originIdentifier,
        Instant materialisedAt
) {

    public MerchantConfigurationRevision {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(
                sourceInitialConfigurationIntentIdentity,
                "sourceInitialConfigurationIntentIdentity"
        );
        Objects.requireNonNull(
                sourceOnboardingCaseIdentity,
                "sourceOnboardingCaseIdentity"
        );
        Objects.requireNonNull(
                sourceOnboardingCaseRevision,
                "sourceOnboardingCaseRevision"
        );
        Objects.requireNonNull(
                onboardingCompletionRevision,
                "onboardingCompletionRevision"
        );
        if (configuration.baseConfigurationIdentifier().isPresent()) {
            throw new IllegalArgumentException(
                    "Initial Configuration Revision must not have a base revision"
            );
        }
        if (configuration.version() != 1) {
            throw new IllegalArgumentException(
                    "Initial Configuration Revision version must be one"
            );
        }
        if (materialisedBy == null || materialisedBy.isBlank()) {
            throw new IllegalArgumentException(
                    "Materialising principal must not be blank"
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
        Objects.requireNonNull(materialisedAt, "materialisedAt");
    }
}
