package mainstreet.semantic.configuration;

import java.time.Instant;

/**
 * Trusted merchant-approval provenance for one exact immutable configuration
 * revision.
 */
public record ConfigurationRevisionApproval(
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String approvingPrincipalIdentifier,
        Instant approvedAt
) {

    public ConfigurationRevisionApproval {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                configurationRevisionIdentifier,
                "Configuration revision identifier"
        );
        requireIdentifier(
                approvingPrincipalIdentifier,
                "Approving principal identifier"
        );
        if (approvedAt == null) {
            throw new NullPointerException("approvedAt");
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
