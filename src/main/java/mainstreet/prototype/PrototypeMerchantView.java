package mainstreet.prototype;

import java.util.Objects;
import java.util.Set;

/**
 * Derived prototype navigation view over one active merchant configuration.
 * It is not merchant configuration or business-state authority.
 */
public record PrototypeMerchantView(
        String merchantIdentifier,
        String configurationIdentifier,
        String releaseIdentifier,
        Set<String> capabilityIdentifiers,
        Set<String> operationIdentifiers
) {

    public PrototypeMerchantView {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(configurationIdentifier, "Configuration identifier");
        requireIdentifier(releaseIdentifier, "Release identifier");
        capabilityIdentifiers = Set.copyOf(
                Objects.requireNonNull(capabilityIdentifiers, "capabilityIdentifiers")
        );
        operationIdentifiers = Set.copyOf(
                Objects.requireNonNull(operationIdentifiers, "operationIdentifiers")
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
