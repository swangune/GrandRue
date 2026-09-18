package grandrue.surface;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable Projection Contract registry for one exact Semantic Registry
 * Release. Lookup deliberately has no latest-release fallback.
 */
public final class ProjectionContractRegistrySnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<ProjectionContractIdentity, ProjectionContractDefinition>
            contracts;

    public ProjectionContractRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Set<ProjectionContractDefinition> contracts
    ) {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
        this.contracts = index(Objects.requireNonNull(contracts, "contracts"));
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public Set<ProjectionContractDefinition> contracts() {
        return Set.copyOf(contracts.values());
    }

    /** Resolves only the exact contract in the explicitly requested release. */
    public Optional<ProjectionContractDefinition> contract(
            String requestedSemanticRegistryReleaseIdentifier,
            ProjectionContractIdentity identity
    ) {
        requireIdentifier(
                requestedSemanticRegistryReleaseIdentifier,
                "Requested semantic registry release identifier"
        );
        Objects.requireNonNull(identity, "identity");
        if (!semanticRegistryReleaseIdentifier.equals(
                requestedSemanticRegistryReleaseIdentifier
        )) {
            return Optional.empty();
        }
        return Optional.ofNullable(contracts.get(identity));
    }

    private static Map<ProjectionContractIdentity, ProjectionContractDefinition>
            index(Set<ProjectionContractDefinition> definitions) {
        Map<ProjectionContractIdentity, ProjectionContractDefinition> indexed =
                new HashMap<>();
        for (ProjectionContractDefinition definition : definitions) {
            Objects.requireNonNull(definition, "projection contract");
            if (indexed.putIfAbsent(
                    definition.identity(),
                    definition
            ) != null) {
                throw new IllegalArgumentException(
                        "Duplicate Projection Contract identity: "
                                + definition.identity()
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
