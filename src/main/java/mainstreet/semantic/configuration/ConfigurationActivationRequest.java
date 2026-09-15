package mainstreet.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * One logical request to activate an already-published configuration release.
 * Expected current revision is absent only for first activation.
 */
public record ConfigurationActivationRequest(
        String activationRequestIdentifier,
        String releaseIdentifier,
        Optional<String> expectedCurrentConfigurationIdentifier,
        String initiatingPrincipalIdentifier
) {

    public ConfigurationActivationRequest {
        requireIdentifier(
                activationRequestIdentifier,
                "Activation request identifier"
        );
        requireIdentifier(releaseIdentifier, "Release identifier");
        expectedCurrentConfigurationIdentifier = Objects.requireNonNull(
                expectedCurrentConfigurationIdentifier,
                "expectedCurrentConfigurationIdentifier"
        );
        expectedCurrentConfigurationIdentifier.ifPresent(expected ->
                requireIdentifier(
                        expected,
                        "Expected current configuration identifier"
                )
        );
        requireIdentifier(
                initiatingPrincipalIdentifier,
                "Initiating principal identifier"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
