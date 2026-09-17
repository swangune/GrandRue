package grandrue.fulfilment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable platform-owned fulfilment contract registry for one semantic
 * registry release.
 *
 * <p>This snapshot defines roles, capability-owned fulfilment requirements and
 * provider technical support only. It does not select a provider for a
 * merchant, establish provider connection state, or activate capability
 * semantics.</p>
 */
public final class FulfilmentContractRegistrySnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<FulfilmentRoleIdentity, FulfilmentRoleDefinition> roles;
    private final Map<String, ProviderDefinition> providers;
    private final Map<FulfilmentRequirementIdentity, FulfilmentRequirementDefinition>
            requirements;

    public FulfilmentContractRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Set<FulfilmentRoleDefinition> roles,
            Set<ProviderDefinition> providers
    ) {
        this(
                semanticRegistryReleaseIdentifier,
                roles,
                providers,
                Set.of()
        );
    }

    public FulfilmentContractRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Set<FulfilmentRoleDefinition> roles,
            Set<ProviderDefinition> providers,
            Set<FulfilmentRequirementDefinition> requirements
    ) {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        this.semanticRegistryReleaseIdentifier = semanticRegistryReleaseIdentifier;
        this.roles = indexRoles(Objects.requireNonNull(roles, "roles"));
        this.providers = indexProviders(
                Objects.requireNonNull(providers, "providers")
        );
        this.requirements = indexRequirements(
                Objects.requireNonNull(requirements, "requirements")
        );
        validateProviderClaims(this.roles, this.providers);
        validateRequirements(this.roles, this.requirements);
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public Optional<FulfilmentRoleDefinition> role(FulfilmentRoleIdentity identity) {
        return Optional.ofNullable(roles.get(Objects.requireNonNull(identity, "identity")));
    }

    public Optional<ProviderDefinition> provider(String providerIdentifier) {
        requireIdentifier(providerIdentifier, "Provider identifier");
        return Optional.ofNullable(providers.get(providerIdentifier));
    }

    public Set<FulfilmentRequirementDefinition> requirements() {
        return Set.copyOf(requirements.values());
    }

    /**
     * Determines whether a registered provider claims every currently required
     * obligation for the registered role. Live provider readiness/health is
     * intentionally outside this static compatibility question.
     */
    public boolean providerSupports(
            String providerIdentifier,
            FulfilmentRoleIdentity roleIdentity,
            Set<String> requiredObligations
    ) {
        requireIdentifier(providerIdentifier, "Provider identifier");
        Objects.requireNonNull(roleIdentity, "roleIdentity");
        Set<String> obligations = Set.copyOf(
                Objects.requireNonNull(requiredObligations, "requiredObligations")
        );
        obligations.forEach(value ->
                requireIdentifier(value, "Required fulfilment obligation"));

        FulfilmentRoleDefinition role = roles.get(roleIdentity);
        if (role == null) {
            throw new IllegalArgumentException("Unknown fulfilment role");
        }
        if (!role.obligations().containsAll(obligations)) {
            throw new IllegalArgumentException(
                    "Required obligation is not declared by the fulfilment role"
            );
        }

        ProviderDefinition provider = providers.get(providerIdentifier);
        if (provider == null) {
            return false;
        }
        return provider.supportedRoles().stream()
                .filter(support -> support.roleIdentity().equals(roleIdentity))
                .findFirst()
                .map(support -> support.supportedObligations().containsAll(
                        obligations
                ))
                .orElse(false);
    }

    private static Map<FulfilmentRoleIdentity, FulfilmentRoleDefinition> indexRoles(
            Set<FulfilmentRoleDefinition> definitions
    ) {
        Map<FulfilmentRoleIdentity, FulfilmentRoleDefinition> indexed =
                new HashMap<>();
        for (FulfilmentRoleDefinition definition : definitions) {
            Objects.requireNonNull(definition, "fulfilment role definition");
            if (indexed.putIfAbsent(definition.identity(), definition) != null) {
                throw new IllegalArgumentException(
                        "Duplicate fulfilment role identity"
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static Map<String, ProviderDefinition> indexProviders(
            Set<ProviderDefinition> definitions
    ) {
        Map<String, ProviderDefinition> indexed = new HashMap<>();
        for (ProviderDefinition definition : definitions) {
            Objects.requireNonNull(definition, "provider definition");
            if (indexed.putIfAbsent(
                    definition.providerIdentifier(),
                    definition
            ) != null) {
                throw new IllegalArgumentException("Duplicate provider identifier");
            }
        }
        return Map.copyOf(indexed);
    }

    private static Map<FulfilmentRequirementIdentity, FulfilmentRequirementDefinition>
            indexRequirements(Set<FulfilmentRequirementDefinition> definitions) {
        Map<FulfilmentRequirementIdentity, FulfilmentRequirementDefinition> indexed =
                new HashMap<>();
        for (FulfilmentRequirementDefinition definition : definitions) {
            Objects.requireNonNull(definition, "fulfilment requirement definition");
            if (indexed.putIfAbsent(definition.identity(), definition) != null) {
                throw new IllegalArgumentException(
                        "Duplicate fulfilment requirement identity"
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static void validateProviderClaims(
            Map<FulfilmentRoleIdentity, FulfilmentRoleDefinition> roles,
            Map<String, ProviderDefinition> providers
    ) {
        for (ProviderDefinition provider : providers.values()) {
            for (ProviderFulfilmentSupport support : provider.supportedRoles()) {
                FulfilmentRoleDefinition role = roles.get(support.roleIdentity());
                if (role == null) {
                    throw new IllegalArgumentException(
                            "Provider references an unregistered fulfilment role"
                    );
                }
                if (!role.obligations().containsAll(
                        support.supportedObligations()
                )) {
                    throw new IllegalArgumentException(
                            "Provider claims an obligation not declared by its role"
                    );
                }
            }
        }
    }

    private static void validateRequirements(
            Map<FulfilmentRoleIdentity, FulfilmentRoleDefinition> roles,
            Map<FulfilmentRequirementIdentity, FulfilmentRequirementDefinition>
                    requirements
    ) {
        for (FulfilmentRequirementDefinition requirement : requirements.values()) {
            FulfilmentRoleDefinition role = roles.get(requirement.roleIdentity());
            if (role == null) {
                throw new IllegalArgumentException(
                        "Fulfilment requirement references an unregistered role"
                );
            }
            if (!role.obligations().containsAll(
                    requirement.obligationIdentifiers()
            )) {
                throw new IllegalArgumentException(
                        "Fulfilment requirement references an obligation not declared by its role"
                );
            }
            if (role.ownerKind() == FulfilmentRoleOwnerKind.CAPABILITY
                    && !role.identity().ownerContextIdentifier().equals(
                            requirement.identity().ownerCapabilityIdentifier()
                    )) {
                throw new IllegalArgumentException(
                        "A capability fulfilment requirement cannot target another capability's private role"
                );
            }
            if (requirement.applicability()
                    instanceof EnumDecisionValueFulfilmentRequirementApplicability conditional
                    && !conditional.decisionIdentity().ownerCapabilityIdentifier().equals(
                            requirement.identity().ownerCapabilityIdentifier()
                    )) {
                throw new IllegalArgumentException(
                        "Fulfilment requirement applicability cannot depend on another capability's configuration decision"
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
