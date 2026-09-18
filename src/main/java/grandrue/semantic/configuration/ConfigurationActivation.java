package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Authoritative committed configuration-activation fact.
 */
public record ConfigurationActivation(
        String activationRequestIdentifier,
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String releaseIdentifier,
        Instant activatedAt,
        Optional<String> replacedConfigurationRevisionIdentifier,
        Optional<ConfigurationActivationAdmissionEvidence> admissionEvidence
) {

    public ConfigurationActivation(
            String activationRequestIdentifier,
            String merchantIdentifier,
            String configurationRevisionIdentifier,
            String releaseIdentifier,
            Instant activatedAt,
            Optional<String> replacedConfigurationRevisionIdentifier
    ) {
        this(
                activationRequestIdentifier,
                merchantIdentifier,
                configurationRevisionIdentifier,
                releaseIdentifier,
                activatedAt,
                replacedConfigurationRevisionIdentifier,
                Optional.empty()
        );
    }

    public ConfigurationActivation {
        requireIdentifier(
                activationRequestIdentifier,
                "Activation request identifier"
        );
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                configurationRevisionIdentifier,
                "Configuration revision identifier"
        );
        requireIdentifier(releaseIdentifier, "Release identifier");
        activatedAt = Objects.requireNonNull(activatedAt, "activatedAt");
        replacedConfigurationRevisionIdentifier = Objects.requireNonNull(
                replacedConfigurationRevisionIdentifier,
                "replacedConfigurationRevisionIdentifier"
        );
        admissionEvidence = Objects.requireNonNull(
                admissionEvidence,
                "admissionEvidence"
        );
        replacedConfigurationRevisionIdentifier.ifPresent(replaced ->
                requireIdentifier(
                        replaced,
                        "Replaced configuration revision identifier"
                )
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
