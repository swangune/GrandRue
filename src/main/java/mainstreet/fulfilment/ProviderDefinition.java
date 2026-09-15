package mainstreet.fulfilment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Platform-owned registration of one external provider's supported fulfilment
 * responsibilities for a specific provider contract version.
 */
public record ProviderDefinition(
        String providerIdentifier,
        String providerContractVersion,
        Set<ProviderFulfilmentSupport> supportedRoles
) {
    public ProviderDefinition {
        requireIdentifier(providerIdentifier, "Provider identifier");
        requireIdentifier(providerContractVersion, "Provider contract version");
        supportedRoles = Set.copyOf(
                Objects.requireNonNull(supportedRoles, "supportedRoles")
        );
        requireUniqueRoleSupport(supportedRoles);
    }

    private static void requireUniqueRoleSupport(
            Set<ProviderFulfilmentSupport> supportedRoles
    ) {
        Set<FulfilmentRoleIdentity> identities = new HashSet<>();
        for (ProviderFulfilmentSupport support : supportedRoles) {
            Objects.requireNonNull(support, "provider fulfilment support");
            if (!identities.add(support.roleIdentity())) {
                throw new IllegalArgumentException(
                        "Provider definition contains duplicate role support"
                );
            }
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
