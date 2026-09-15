package mainstreet.semantic.configuration;

import mainstreet.semantic.execution.ExecutableSupportRequirement;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Deterministic resolver backed by exact release-affined Semantic Registry
 * operation-to-execution-contract mappings.
 */
public final class RegisteredConfigurationNewActivityRequirementResolver
        implements ConfigurationNewActivityRequirementResolver {

    private final Map<MappingIdentity, ExecutableSupportRequirement> mappings;

    public RegisteredConfigurationNewActivityRequirementResolver(
            Collection<ConfigurationOperationExecutionRequirement> mappings
    ) {
        Objects.requireNonNull(mappings, "mappings");
        Map<MappingIdentity, ExecutableSupportRequirement> indexed =
                new HashMap<>();
        for (ConfigurationOperationExecutionRequirement mapping : mappings) {
            Objects.requireNonNull(mapping, "mappings contains null");
            MappingIdentity identity = new MappingIdentity(
                    mapping.semanticRegistryReleaseIdentifier(),
                    mapping.operationIdentifier()
            );
            if (indexed.putIfAbsent(identity, mapping.requirement()) != null) {
                throw new IllegalArgumentException(
                        "Duplicate execution requirement mapping: " + identity
                );
            }
        }
        this.mappings = Map.copyOf(indexed);
    }

    @Override
    public Set<ExecutableSupportRequirement> resolve(
            ResolvedConfigurationPackage resolvedPackage
    ) {
        Objects.requireNonNull(resolvedPackage, "resolvedPackage");
        String release = resolvedPackage.semanticRegistryReleaseIdentifier();
        LinkedHashSet<ExecutableSupportRequirement> requirements =
                new LinkedHashSet<>();
        resolvedPackage.executableSemanticModel().operations().forEach(
                operation -> {
                    MappingIdentity identity = new MappingIdentity(
                            release,
                            operation.identifier()
                    );
                    ExecutableSupportRequirement requirement = mappings.get(
                            identity
                    );
                    if (requirement == null) {
                        throw new IllegalStateException(
                                "No release-affined execution requirement mapping for "
                                        + identity
                        );
                    }
                    if (!requirements.add(requirement)) {
                        throw new IllegalStateException(
                                "Multiple operations resolve to one execution requirement: "
                                        + requirement.affectedExecutionContract()
                        );
                    }
                }
        );
        ConfigurationNewActivityRequirementSetIdentity.derive(requirements);
        return Set.copyOf(requirements);
    }

    private record MappingIdentity(
            String semanticRegistryReleaseIdentifier,
            String operationIdentifier
    ) {
    }
}
