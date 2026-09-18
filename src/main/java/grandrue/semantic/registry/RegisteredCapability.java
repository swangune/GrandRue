package grandrue.semantic.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Platform-owned capability semantics published in a registry release.
 * Operational Objects, schemas, operations, policies and typed relationships
 * remain capability-owned definitions.
 */
public record RegisteredCapability(
        String identifier,
        List<OwnedOperationalObjectDefinition> operationalObjects,
        List<OwnedOperationDefinition> operations,
        List<OwnedPolicyDefinition> policies,
        List<OwnedRelationshipDefinition> relationships,
        List<OwnedSchemaDefinition> schemas,
        List<OwnedDataConceptDefinition> dataConcepts
) implements RegisteredDefinition {

    public RegisteredCapability {
        requireIdentifier(identifier, "Capability identifier");
        operationalObjects = List.copyOf(
                Objects.requireNonNull(operationalObjects)
        );
        operations = List.copyOf(Objects.requireNonNull(operations));
        policies = List.copyOf(Objects.requireNonNull(policies));
        relationships = List.copyOf(Objects.requireNonNull(relationships));
        schemas = List.copyOf(Objects.requireNonNull(schemas));
        dataConcepts = List.copyOf(Objects.requireNonNull(dataConcepts));

        Map<String, OwnedOperationalObjectDefinition> objectsByIdentifier =
                indexOperationalObjects(operationalObjects);
        Map<OwnedSchemaReference, OwnedSchemaDefinition> schemasByIdentity =
                indexSchemas(schemas);
        Set<String> localDataConceptIdentifiers = indexDataConcepts(dataConcepts);
        validateSchemaSemantics(schemas, localDataConceptIdentifiers);
        validateObjectSchemas(operationalObjects, schemasByIdentity);
        Map<String, OwnedRelationshipDefinition> relationshipsByIdentifier =
                indexRelationships(relationships, objectsByIdentifier);
        requireUniqueOperationIdentifiers(operations);
        requireUniquePolicyIdentifiers(policies);
        for (OwnedOperationDefinition operation : operations) {
            for (OwnedOperationEffect effect : operation.effects()) {
                validateEffect(
                        effect,
                        objectsByIdentifier,
                        schemasByIdentity,
                        relationshipsByIdentifier
                );
            }
        }
    }

    public RegisteredCapability(
            String identifier,
            List<OwnedOperationalObjectDefinition> operationalObjects,
            List<OwnedOperationDefinition> operations,
            List<OwnedPolicyDefinition> policies,
            List<OwnedRelationshipDefinition> relationships
    ) {
        this(
                identifier,
                operationalObjects,
                operations,
                policies,
                relationships,
                List.of(),
                List.of()
        );
    }

    public RegisteredCapability(
            String identifier,
            List<OwnedOperationalObjectDefinition> operationalObjects,
            List<OwnedOperationDefinition> operations,
            List<OwnedPolicyDefinition> policies
    ) {
        this(
                identifier,
                operationalObjects,
                operations,
                policies,
                List.of(),
                List.of(),
                List.of()
        );
    }

    public RegisteredCapability(
            String identifier,
            List<OwnedOperationalObjectDefinition> operationalObjects,
            List<OwnedOperationDefinition> operations
    ) {
        this(
                identifier,
                operationalObjects,
                operations,
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }

    @Override
    public SemanticIdentity identity() {
        return new SemanticIdentity(
                identifier,
                SemanticDefinitionKind.CAPABILITY
        );
    }

    public OwnedSchemaDefinition requireSchema(OwnedSchemaReference reference) {
        Objects.requireNonNull(reference);
        return schemas.stream()
                .filter(schema -> schema.identifier().equals(
                        reference.schemaIdentifier()
                ))
                .filter(schema -> schema.version() == reference.schemaVersion())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Capability does not own schema version: "
                                + reference.schemaIdentifier()
                                + "@"
                                + reference.schemaVersion()
                ));
    }

    private static Map<String, OwnedOperationalObjectDefinition>
            indexOperationalObjects(
                    List<OwnedOperationalObjectDefinition> objects
            ) {
        try {
            return objects.stream().collect(Collectors.toUnmodifiableMap(
                    OwnedOperationalObjectDefinition::identifier,
                    Function.identity()
            ));
        } catch (IllegalStateException duplicate) {
            throw new IllegalArgumentException(
                    "Capability contains a duplicate operational object identifier",
                    duplicate
            );
        }
    }

    private static Map<OwnedSchemaReference, OwnedSchemaDefinition> indexSchemas(
            List<OwnedSchemaDefinition> schemas
    ) {
        Map<OwnedSchemaReference, OwnedSchemaDefinition> indexed = new HashMap<>();
        for (OwnedSchemaDefinition schema : schemas) {
            OwnedSchemaReference reference = schema.reference();
            if (indexed.putIfAbsent(reference, schema) != null) {
                throw new IllegalArgumentException(
                        "Capability contains a duplicate schema version: "
                                + reference.schemaIdentifier()
                                + "@"
                                + reference.schemaVersion()
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static Set<String> indexDataConcepts(
            List<OwnedDataConceptDefinition> dataConcepts
    ) {
        Set<String> identifiers = new HashSet<>();
        for (OwnedDataConceptDefinition dataConcept : dataConcepts) {
            if (!identifiers.add(dataConcept.identifier())) {
                throw new IllegalArgumentException(
                        "Capability contains a duplicate DataConcept: "
                                + dataConcept.identifier()
                );
            }
        }
        return Set.copyOf(identifiers);
    }

    private static void validateSchemaSemantics(
            List<OwnedSchemaDefinition> schemas,
            Set<String> localDataConceptIdentifiers
    ) {
        for (OwnedSchemaDefinition schema : schemas) {
            for (OwnedFieldDefinition field : schema.fields()) {
                switch (field.semanticBasis()) {
                    case CapabilityDataConceptReference reference -> {
                        if (!localDataConceptIdentifiers.contains(
                                reference.identifier()
                        )) {
                            throw new IllegalArgumentException(
                                    "Schema field references a capability DataConcept "
                                            + "not owned by the capability: "
                                            + reference.identifier()
                            );
                        }
                    }
                    case GlobalDataConceptReference ignored -> {
                        // Registry-level validation owns global references.
                    }
                    case FieldSpecificSemantics ignored -> {
                        // The field itself owns the semantic meaning.
                    }
                }
            }
        }
    }

    private static void validateObjectSchemas(
            List<OwnedOperationalObjectDefinition> objects,
            Map<OwnedSchemaReference, OwnedSchemaDefinition> schemas
    ) {
        for (OwnedOperationalObjectDefinition object : objects) {
            object.schemaReference().ifPresent(reference -> {
                if (!schemas.containsKey(reference)) {
                    throw new IllegalArgumentException(
                            "Operational Object references a schema version not owned "
                                    + "by its capability: "
                                    + reference.schemaIdentifier()
                                    + "@"
                                    + reference.schemaVersion()
                    );
                }
            });
        }
    }

    private static Map<String, OwnedRelationshipDefinition> indexRelationships(
            List<OwnedRelationshipDefinition> relationships,
            Map<String, OwnedOperationalObjectDefinition> objects
    ) {
        Map<String, OwnedRelationshipDefinition> indexed = new HashMap<>();
        for (OwnedRelationshipDefinition relationship : relationships) {
            if (!objects.containsKey(relationship.sourceObjectIdentifier())) {
                throw new IllegalArgumentException(
                        "Relationship source is not an Operational Object owned by "
                                + "its capability: "
                                + relationship.sourceObjectIdentifier()
                );
            }
            if (indexed.putIfAbsent(
                    relationship.identifier(),
                    relationship
            ) != null) {
                throw new IllegalArgumentException(
                        "Capability contains a duplicate relationship: "
                                + relationship.identifier()
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static void requireUniqueOperationIdentifiers(
            List<OwnedOperationDefinition> operations
    ) {
        Set<String> identifiers = new HashSet<>();
        for (OwnedOperationDefinition operation : operations) {
            if (!identifiers.add(operation.identifier())) {
                throw new IllegalArgumentException(
                        "Capability contains a duplicate operation: "
                                + operation.identifier()
                );
            }
        }
    }

    private static void requireUniquePolicyIdentifiers(
            List<OwnedPolicyDefinition> policies
    ) {
        Set<String> identifiers = new HashSet<>();
        for (OwnedPolicyDefinition policy : policies) {
            if (!identifiers.add(policy.identifier())) {
                throw new IllegalArgumentException(
                        "Capability contains a duplicate policy: "
                                + policy.identifier()
                );
            }
        }
    }

    private static void validateEffect(
            OwnedOperationEffect effect,
            Map<String, OwnedOperationalObjectDefinition> objects,
            Map<OwnedSchemaReference, OwnedSchemaDefinition> schemas,
            Map<String, OwnedRelationshipDefinition> relationships
    ) {
        switch (effect) {
            case OwnedObjectCreationEffect creation ->
                    validateCreation(creation, requireObject(
                            objects,
                            creation.targetObjectIdentifier()
                    ));
            case OwnedStateTransitionEffect transition -> {
                OwnedOperationalObjectDefinition object = requireObject(
                        objects,
                        transition.targetObjectIdentifier()
                );
                if (!object.hasLifecycle()) {
                    throw new IllegalArgumentException(
                            "State transition requires a lifecycle-bearing "
                                    + "Operational Object: "
                                    + object.identifier()
                    );
                }
                requireOwnedState(
                        object,
                        transition.sourceStateIdentifier(),
                        "source"
                );
                requireOwnedState(
                        object,
                        transition.resultingStateIdentifier(),
                        "resulting"
                );
            }
            case OwnedDataMutationEffect mutation ->
                    validateDataMutation(mutation, objects, schemas);
            case OwnedRelationshipEstablishmentEffect establishment ->
                    requireRelationship(
                            relationships,
                            establishment.relationshipIdentifier()
                    );
            case OwnedRelationshipRemovalEffect removal ->
                    requireRelationship(
                            relationships,
                            removal.relationshipIdentifier()
                    );
            case OwnedAllocationClaimEffect ignored -> {
                // Runtime revalidates resource-role existence and capacity.
            }
            case OwnedAllocationReleaseEffect ignored -> {
                // Runtime revalidates the authoritative claim being released.
            }
        }
    }

    private static void validateCreation(
            OwnedObjectCreationEffect creation,
            OwnedOperationalObjectDefinition object
    ) {
        if (!creation.initialStateIdentifier().equals(
                object.initialStateIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Creation effect must produce the Operational Object's "
                            + "registered initial lifecycle state"
            );
        }
    }

    private static void validateDataMutation(
            OwnedDataMutationEffect mutation,
            Map<String, OwnedOperationalObjectDefinition> objects,
            Map<OwnedSchemaReference, OwnedSchemaDefinition> schemas
    ) {
        OwnedOperationalObjectDefinition object = requireObject(
                objects,
                mutation.targetObjectIdentifier()
        );
        OwnedSchemaReference reference = object.schemaReference()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Data mutation requires a schema-governed Operational Object: "
                                + object.identifier()
                ));
        OwnedSchemaDefinition schema = schemas.get(reference);
        if (schema == null) {
            throw new IllegalArgumentException(
                    "Data mutation target schema is not registered: "
                            + reference.schemaIdentifier()
                            + "@"
                            + reference.schemaVersion()
            );
        }
        if (schema.field(mutation.targetFieldIdentifier()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Data mutation must target a registered field: "
                            + mutation.targetFieldIdentifier()
            );
        }
    }

    private static OwnedOperationalObjectDefinition requireObject(
            Map<String, OwnedOperationalObjectDefinition> objects,
            String identifier
    ) {
        OwnedOperationalObjectDefinition object = objects.get(identifier);
        if (object == null) {
            throw new IllegalArgumentException(
                    "Operation effect targets an Operational Object not owned "
                            + "by its capability: "
                            + identifier
            );
        }
        return object;
    }

    private static void requireRelationship(
            Map<String, OwnedRelationshipDefinition> relationships,
            String identifier
    ) {
        if (!relationships.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "Operation effect references an unregistered relationship: "
                            + identifier
            );
        }
    }

    private static void requireOwnedState(
            OwnedOperationalObjectDefinition object,
            String stateIdentifier,
            String role
    ) {
        if (!object.stateIdentifiers().contains(stateIdentifier)) {
            throw new IllegalArgumentException(
                    "Operation "
                            + role
                            + " state is not owned by its target Operational Object: "
                            + stateIdentifier
            );
        }
    }

    private static void requireIdentifier(String identifier, String label) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
