package mainstreet.semantic.configuration;

import grandrue.fulfilment.FulfilmentBindingSetRevisionReference;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable declarative input to the configuration compiler. It selects
 * registered capabilities and bounded values but contains no executable
 * semantics.
 *
 * <p>The optional base configuration identifier is authoritative revision
 * lineage used by activation concurrency. It is absent only for a first
 * revision candidate and is not inferred from sequence/version numbers.</p>
 *
 * <p>The optional fulfilment binding-set reference pins an exact immutable
 * subordinate routing revision under MS-PROT-048 v1.2. The referenced
 * binding-set revision has no independent activation authority.</p>
 */
public record MerchantConfiguration(
        String merchantIdentifier,
        String configurationIdentifier,
        long version,
        String semanticRegistryVersion,
        Set<String> capabilityIdentifiers,
        Set<PolicySelection> policySelections,
        Optional<String> baseConfigurationIdentifier,
        Optional<FulfilmentBindingSetRevisionReference>
                fulfilmentBindingSetRevisionReference
) {

    public MerchantConfiguration {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                configurationIdentifier,
                "Configuration identifier"
        );
        if (version < 1) {
            throw new IllegalArgumentException(
                    "Configuration version must be positive"
            );
        }
        requireIdentifier(
                semanticRegistryVersion,
                "Semantic registry version"
        );
        capabilityIdentifiers = Set.copyOf(
                Objects.requireNonNull(capabilityIdentifiers)
        );
        for (String capabilityIdentifier : capabilityIdentifiers) {
            requireIdentifier(
                    capabilityIdentifier,
                    "Capability identifier"
            );
        }
        policySelections = Set.copyOf(
                Objects.requireNonNull(policySelections)
        );
        requireUniquePolicySelections(policySelections);
        baseConfigurationIdentifier = Objects.requireNonNull(
                baseConfigurationIdentifier,
                "baseConfigurationIdentifier"
        );
        baseConfigurationIdentifier.ifPresent(base -> {
            requireIdentifier(base, "Base configuration identifier");
            if (base.equals(configurationIdentifier)) {
                throw new IllegalArgumentException(
                        "Configuration revision cannot be its own base"
                );
            }
        });
        fulfilmentBindingSetRevisionReference = Objects.requireNonNull(
                fulfilmentBindingSetRevisionReference,
                "fulfilmentBindingSetRevisionReference"
        );
    }

    public MerchantConfiguration(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers,
            Set<PolicySelection> policySelections,
            Optional<String> baseConfigurationIdentifier
    ) {
        this(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                policySelections,
                baseConfigurationIdentifier,
                Optional.empty()
        );
    }

    public MerchantConfiguration(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers,
            Set<PolicySelection> policySelections
    ) {
        this(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                policySelections,
                Optional.empty(),
                Optional.empty()
        );
    }

    public MerchantConfiguration(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers
    ) {
        this(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                Set.of(),
                Optional.empty(),
                Optional.empty()
        );
    }

    public MerchantConfiguration withBaseConfigurationIdentifier(
            String baseConfigurationIdentifier
    ) {
        return new MerchantConfiguration(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                policySelections,
                Optional.of(baseConfigurationIdentifier),
                fulfilmentBindingSetRevisionReference
        );
    }

    public MerchantConfiguration withFulfilmentBindingSetRevisionReference(
            FulfilmentBindingSetRevisionReference reference
    ) {
        return new MerchantConfiguration(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                policySelections,
                baseConfigurationIdentifier,
                Optional.of(Objects.requireNonNull(reference, "reference"))
        );
    }

    private static void requireUniquePolicySelections(
            Set<PolicySelection> selections
    ) {
        Set<PolicyIdentity> identities = new HashSet<>();
        for (PolicySelection selection : selections) {
            PolicyIdentity identity = new PolicyIdentity(
                    selection.ownerCapabilityIdentifier(),
                    selection.policyIdentifier()
            );
            if (!identities.add(identity)) {
                throw new IllegalArgumentException(
                        "Merchant configuration selects one policy more than once"
                );
            }
        }
    }

    private record PolicyIdentity(
            String ownerCapabilityIdentifier,
            String policyIdentifier
    ) {
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
