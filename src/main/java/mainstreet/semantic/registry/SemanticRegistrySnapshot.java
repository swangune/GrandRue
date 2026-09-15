package mainstreet.semantic.registry;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Immutable release of the platform semantic registry. Merchant models retain
 * this version so their historical meaning can be resolved later.
 */
public record SemanticRegistrySnapshot(
        String version,
        Set<RegisteredDefinition> definitions,
        Set<RegisteredCapabilityRelationship> capabilityRelationships
) {

    public SemanticRegistrySnapshot {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic registry version must not be blank"
            );
        }
        definitions = Set.copyOf(
                Objects.requireNonNull(definitions)
        );
        capabilityRelationships = Set.copyOf(
                Objects.requireNonNull(capabilityRelationships)
        );
        Set<SemanticIdentity> identities = new HashSet<>();
        for (RegisteredDefinition definition : definitions) {
            if (!identities.add(definition.identity())) {
                throw new IllegalArgumentException(
                        "Duplicate registered semantic identity: "
                                + definition.identity()
                );
            }
        }
        validateGlobalDataConceptReferences(definitions);
        validateOperationalObjectRelationshipTargets(definitions);
        validateRelationships(definitions, capabilityRelationships);
    }

    public SemanticRegistrySnapshot(
            String version,
            Set<RegisteredDefinition> definitions
    ) {
        this(version, definitions, Set.of());
    }

    /**
     * Reports whether this release contains a definition with the given
     * stable semantic identity.
     */
    public boolean contains(
            SemanticDefinitionKind kind,
            String identifier
    ) {
        Objects.requireNonNull(kind);
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic definition identifier must not be blank"
            );
        }

        return definitions.stream().anyMatch(definition ->
                definition.identity().kind() == kind
                        && definition.identity().identifier().equals(identifier)
        );
    }

    /** Resolves one globally registered DataConcept. */
    public Optional<RegisteredGlobalDataConcept> globalDataConcept(
            String identifier
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Global DataConcept identifier must not be blank"
            );
        }
        return definitions.stream()
                .filter(RegisteredGlobalDataConcept.class::isInstance)
                .map(RegisteredGlobalDataConcept.class::cast)
                .filter(concept -> concept.identifier().equals(identifier))
                .findFirst();
    }

    /**
     * Resolves a registered capability and its platform-owned semantics.
     */
    public Optional<RegisteredCapability> capability(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Capability identifier must not be blank"
            );
        }

        return definitions.stream()
                .filter(RegisteredCapability.class::isInstance)
                .map(RegisteredCapability.class::cast)
                .filter(capability -> capability.identifier().equals(identifier))
                .findFirst();
    }

    /**
     * Returns all registered capabilities in deterministic identity order.
     */
    public List<RegisteredCapability> capabilities() {
        return definitions.stream()
                .filter(RegisteredCapability.class::isInstance)
                .map(RegisteredCapability.class::cast)
                .sorted(Comparator.comparing(
                        RegisteredCapability::identifier
                ))
                .toList();
    }

    private static void validateGlobalDataConceptReferences(
            Set<RegisteredDefinition> definitions
    ) {
        Set<String> globalConceptIdentifiers = definitions.stream()
                .filter(RegisteredGlobalDataConcept.class::isInstance)
                .map(RegisteredGlobalDataConcept.class::cast)
                .map(RegisteredGlobalDataConcept::identifier)
                .collect(Collectors.toUnmodifiableSet());

        definitions.stream()
                .filter(RegisteredCapability.class::isInstance)
                .map(RegisteredCapability.class::cast)
                .flatMap(capability -> capability.schemas().stream())
                .flatMap(schema -> schema.fields().stream())
                .map(OwnedFieldDefinition::semanticBasis)
                .filter(GlobalDataConceptReference.class::isInstance)
                .map(GlobalDataConceptReference.class::cast)
                .forEach(reference -> {
                    if (!globalConceptIdentifiers.contains(
                            reference.identifier()
                    )) {
                        throw new IllegalArgumentException(
                                "Schema field references an unregistered global "
                                        + "DataConcept: "
                                        + reference.identifier()
                        );
                    }
                });
    }

    private static void validateOperationalObjectRelationshipTargets(
            Set<RegisteredDefinition> definitions
    ) {
        Map<String, RegisteredCapability> capabilities = definitions.stream()
                .filter(RegisteredCapability.class::isInstance)
                .map(RegisteredCapability.class::cast)
                .collect(Collectors.toUnmodifiableMap(
                        RegisteredCapability::identifier,
                        capability -> capability
                ));

        for (RegisteredCapability sourceCapability : capabilities.values()) {
            for (OwnedRelationshipDefinition relationship
                    : sourceCapability.relationships()) {
                OwnedOperationalObjectTypeReference target =
                        relationship.targetObjectType();
                RegisteredCapability targetCapability = capabilities.get(
                        target.ownerCapabilityIdentifier()
                );
                if (targetCapability == null) {
                    throw new IllegalArgumentException(
                            "Relationship target capability is not registered: "
                                    + target.ownerCapabilityIdentifier()
                    );
                }
                boolean targetExists = targetCapability.operationalObjects()
                        .stream()
                        .anyMatch(object -> object.identifier().equals(
                                target.objectIdentifier()
                        ));
                if (!targetExists) {
                    throw new IllegalArgumentException(
                            "Relationship target Operational Object is not registered: "
                                    + target.ownerCapabilityIdentifier()
                                    + "/"
                                    + target.objectIdentifier()
                    );
                }
            }
        }
    }

    private static void validateRelationships(
            Set<RegisteredDefinition> definitions,
            Set<RegisteredCapabilityRelationship> relationships
    ) {
        Set<String> capabilityIdentifiers = definitions.stream()
                .filter(RegisteredCapability.class::isInstance)
                .map(RegisteredCapability.class::cast)
                .map(RegisteredCapability::identifier)
                .collect(Collectors.toUnmodifiableSet());

        for (RegisteredCapabilityRelationship relationship : relationships) {
            requireRegisteredEndpoint(
                    capabilityIdentifiers,
                    relationship.sourceCapabilityIdentifier()
            );
            requireRegisteredEndpoint(
                    capabilityIdentifiers,
                    relationship.targetCapabilityIdentifier()
            );
        }
        rejectRequiredCycles(capabilityIdentifiers, relationships);
    }

    private static void requireRegisteredEndpoint(
            Set<String> capabilityIdentifiers,
            String identifier
    ) {
        if (!capabilityIdentifiers.contains(identifier)) {
            throw new IllegalArgumentException(
                    "Capability relationship endpoint is not registered: "
                            + identifier
            );
        }
    }

    private static void rejectRequiredCycles(
            Set<String> capabilityIdentifiers,
            Set<RegisteredCapabilityRelationship> relationships
    ) {
        Map<String, Set<String>> requiredTargets = new HashMap<>();
        for (String identifier : capabilityIdentifiers) {
            requiredTargets.put(identifier, new HashSet<>());
        }
        relationships.stream()
                .filter(relationship -> relationship.type()
                        == CapabilityRelationshipType.REQUIRES)
                .forEach(relationship -> requiredTargets.get(
                        relationship.sourceCapabilityIdentifier()
                ).add(relationship.targetCapabilityIdentifier()));

        Map<String, VisitState> states = new HashMap<>();
        for (String identifier : capabilityIdentifiers) {
            detectRequiredCycle(identifier, requiredTargets, states);
        }
    }

    private static void detectRequiredCycle(
            String identifier,
            Map<String, Set<String>> requiredTargets,
            Map<String, VisitState> states
    ) {
        VisitState state = states.get(identifier);
        if (state == VisitState.VISITING) {
            throw new IllegalArgumentException(
                    "REQUIRES relationships must not contain a cycle at: "
                            + identifier
            );
        }
        if (state == VisitState.VISITED) {
            return;
        }

        states.put(identifier, VisitState.VISITING);
        for (String target : requiredTargets.get(identifier)) {
            detectRequiredCycle(target, requiredTargets, states);
        }
        states.put(identifier, VisitState.VISITED);
    }

    private enum VisitState {
        VISITING,
        VISITED
    }
}
