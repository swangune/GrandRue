package mainstreet.semantic.executable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable runtime-ready representation of one compiled merchant configuration. */
public record ExecutableMerchantModel(
        String merchantIdentifier,
        String modelIdentifier,
        long version,
        String semanticRegistryVersion,
        Set<String> capabilityIdentifiers,
        List<ExecutableOperationalObjectDefinition> operationalObjects,
        List<ExecutableSchemaDefinition> schemas,
        List<ExecutableRelationshipDefinition> relationships,
        List<ExecutableOperationDefinition> operations,
        List<ExecutablePolicyValue> policies,
        List<ExecutableCapabilityActivation> capabilityActivations
) {

    public ExecutableMerchantModel {
        Objects.requireNonNull(merchantIdentifier);
        Objects.requireNonNull(modelIdentifier);
        Objects.requireNonNull(semanticRegistryVersion);
        capabilityIdentifiers = Set.copyOf(
                Objects.requireNonNull(capabilityIdentifiers)
        );
        operationalObjects = List.copyOf(
                Objects.requireNonNull(operationalObjects)
        );
        schemas = List.copyOf(Objects.requireNonNull(schemas));
        relationships = List.copyOf(Objects.requireNonNull(relationships));
        operations = List.copyOf(Objects.requireNonNull(operations));
        policies = List.copyOf(Objects.requireNonNull(policies));
        capabilityActivations = List.copyOf(
                Objects.requireNonNull(capabilityActivations)
        );
        requireOperationalObjectOwnersActive(
                capabilityIdentifiers,
                operationalObjects
        );
        requireUniqueOperationalObjectIdentities(operationalObjects);
        requireUniqueSchemaIdentities(schemas);
        requireObjectSchemasPresent(operationalObjects, schemas);
        requireUniqueRelationshipIdentifiers(relationships, operationalObjects);
        requireUniqueOperationIdentifiers(operations);
        requireUniquePolicyIdentifiers(policies);
        requireActivationCoverage(capabilityIdentifiers, capabilityActivations);
    }

    public ExecutableMerchantModel(
            String merchantIdentifier,
            String modelIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers,
            List<ExecutableOperationalObjectDefinition> operationalObjects,
            List<ExecutableSchemaDefinition> schemas,
            List<ExecutableRelationshipDefinition> relationships,
            List<ExecutableOperationDefinition> operations,
            List<ExecutablePolicyValue> policies
    ) {
        this(
                merchantIdentifier,
                modelIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                operationalObjects,
                schemas,
                relationships,
                operations,
                policies,
                merchantSelectedActivations(capabilityIdentifiers)
        );
    }

    public ExecutableMerchantModel(
            String merchantIdentifier,
            String modelIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers,
            List<ExecutableOperationalObjectDefinition> operationalObjects,
            List<ExecutableOperationDefinition> operations,
            List<ExecutablePolicyValue> policies,
            List<ExecutableCapabilityActivation> capabilityActivations
    ) {
        this(
                merchantIdentifier,
                modelIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                operationalObjects,
                List.of(),
                List.of(),
                operations,
                policies,
                capabilityActivations
        );
    }

    public ExecutableMerchantModel(
            String merchantIdentifier,
            String modelIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers,
            List<ExecutableOperationalObjectDefinition> operationalObjects,
            List<ExecutableOperationDefinition> operations,
            List<ExecutablePolicyValue> policies
    ) {
        this(
                merchantIdentifier,
                modelIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                operationalObjects,
                List.of(),
                List.of(),
                operations,
                policies,
                merchantSelectedActivations(capabilityIdentifiers)
        );
    }

    public ExecutableMerchantModel(
            String merchantIdentifier,
            String modelIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers,
            List<ExecutableOperationalObjectDefinition> operationalObjects,
            List<ExecutableOperationDefinition> operations
    ) {
        this(
                merchantIdentifier,
                modelIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                operationalObjects,
                List.of(),
                List.of(),
                operations,
                List.of(),
                merchantSelectedActivations(capabilityIdentifiers)
        );
    }

    public Optional<ExecutableOperationalObjectDefinition> operationalObject(
            String ownerCapabilityIdentifier,
            String objectIdentifier
    ) {
        return operationalObject(new ExecutableOperationalObjectTypeIdentity(
                ownerCapabilityIdentifier,
                objectIdentifier
        ));
    }

    public Optional<ExecutableOperationalObjectDefinition> operationalObject(
            ExecutableOperationalObjectTypeIdentity identity
    ) {
        Objects.requireNonNull(identity);
        return operationalObjects.stream()
                .filter(object -> object.identity().equals(identity))
                .findFirst();
    }

    public Optional<ExecutableSchemaDefinition> schema(
            String ownerCapabilityIdentifier,
            String schemaIdentifier,
            long schemaVersion
    ) {
        return schemas.stream()
                .filter(schema -> schema.ownerCapabilityIdentifier().equals(
                        ownerCapabilityIdentifier
                ))
                .filter(schema -> schema.identifier().equals(schemaIdentifier))
                .filter(schema -> schema.version() == schemaVersion)
                .findFirst();
    }

    public Optional<ExecutableRelationshipDefinition> relationship(
            String identifier
    ) {
        return relationships.stream()
                .filter(relationship -> relationship.identifier().equals(identifier))
                .findFirst();
    }

    public Optional<ExecutableOperationDefinition> operation(String identifier) {
        return operations.stream()
                .filter(operation -> operation.identifier().equals(identifier))
                .findFirst();
    }

    public Optional<ExecutablePolicyValue> policy(
            String ownerCapabilityIdentifier,
            String policyIdentifier
    ) {
        return policies.stream()
                .filter(policy -> policy.ownerCapabilityIdentifier().equals(
                        ownerCapabilityIdentifier
                ))
                .filter(policy -> policy.policyIdentifier().equals(policyIdentifier))
                .findFirst();
    }

    public Optional<ExecutableCapabilityActivation> activation(
            String capabilityIdentifier
    ) {
        return capabilityActivations.stream()
                .filter(activation -> activation.capabilityIdentifier().equals(
                        capabilityIdentifier
                ))
                .findFirst();
    }

    private static void requireOperationalObjectOwnersActive(
            Set<String> capabilityIdentifiers,
            List<ExecutableOperationalObjectDefinition> objects
    ) {
        for (ExecutableOperationalObjectDefinition object : objects) {
            if (!capabilityIdentifiers.contains(object.ownerCapabilityIdentifier())) {
                throw new IllegalArgumentException(
                        "Operational Object owner is not an active capability: "
                                + object.ownerCapabilityIdentifier()
                );
            }
        }
    }

    private static void requireUniqueOperationalObjectIdentities(
            List<ExecutableOperationalObjectDefinition> objects
    ) {
        Set<ExecutableOperationalObjectTypeIdentity> identities = new HashSet<>();
        for (ExecutableOperationalObjectDefinition object : objects) {
            if (!identities.add(object.identity())) {
                throw new IllegalArgumentException(
                        "Duplicate executable Operational Object: "
                                + object.ownerCapabilityIdentifier()
                                + "/"
                                + object.identifier()
                );
            }
        }
    }

    private static void requireUniqueSchemaIdentities(
            List<ExecutableSchemaDefinition> schemas
    ) {
        Set<SchemaIdentity> identities = new HashSet<>();
        for (ExecutableSchemaDefinition schema : schemas) {
            SchemaIdentity identity = new SchemaIdentity(
                    schema.ownerCapabilityIdentifier(),
                    schema.identifier(),
                    schema.version()
            );
            if (!identities.add(identity)) {
                throw new IllegalArgumentException(
                        "Duplicate executable schema: "
                                + schema.ownerCapabilityIdentifier()
                                + "/"
                                + schema.identifier()
                                + "@"
                                + schema.version()
                );
            }
        }
    }

    private static void requireObjectSchemasPresent(
            List<ExecutableOperationalObjectDefinition> objects,
            List<ExecutableSchemaDefinition> schemas
    ) {
        Set<ExecutableSchemaReference> available = schemas.stream()
                .map(ExecutableSchemaDefinition::reference)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        for (ExecutableOperationalObjectDefinition object : objects) {
            object.schemaReference().ifPresent(reference -> {
                if (!available.contains(reference)) {
                    throw new IllegalArgumentException(
                            "Operational Object schema is absent from executable model: "
                                    + reference.ownerCapabilityIdentifier()
                                    + "/"
                                    + reference.schemaIdentifier()
                                    + "@"
                                    + reference.schemaVersion()
                    );
                }
            });
        }
    }

    private static void requireUniqueRelationshipIdentifiers(
            List<ExecutableRelationshipDefinition> relationships,
            List<ExecutableOperationalObjectDefinition> objects
    ) {
        Set<ExecutableOperationalObjectTypeIdentity> objectIdentities = objects.stream()
                .map(ExecutableOperationalObjectDefinition::identity)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        Set<String> identifiers = new HashSet<>();
        for (ExecutableRelationshipDefinition relationship : relationships) {
            if (!identifiers.add(relationship.identifier())) {
                throw new IllegalArgumentException(
                        "Duplicate executable relationship: "
                                + relationship.identifier()
                );
            }
            if (!objectIdentities.contains(relationship.sourceObjectType())) {
                throw new IllegalArgumentException(
                        "Executable relationship source is absent from the model: "
                                + relationship.sourceObjectType()
                );
            }
            if (!objectIdentities.contains(relationship.targetObjectType())) {
                throw new IllegalArgumentException(
                        "Executable relationship target is absent from the model: "
                                + relationship.targetObjectType()
                );
            }
        }
    }

    private static void requireUniqueOperationIdentifiers(
            List<ExecutableOperationDefinition> operations
    ) {
        Set<String> identifiers = new HashSet<>();
        for (ExecutableOperationDefinition operation : operations) {
            if (!identifiers.add(operation.identifier())) {
                throw new IllegalArgumentException(
                        "Duplicate executable operation: " + operation.identifier()
                );
            }
        }
    }

    private static void requireUniquePolicyIdentifiers(
            List<ExecutablePolicyValue> policies
    ) {
        Set<PolicyIdentity> identities = new HashSet<>();
        for (ExecutablePolicyValue policy : policies) {
            PolicyIdentity identity = new PolicyIdentity(
                    policy.ownerCapabilityIdentifier(),
                    policy.policyIdentifier()
            );
            if (!identities.add(identity)) {
                throw new IllegalArgumentException(
                        "Duplicate executable policy: "
                                + policy.ownerCapabilityIdentifier()
                                + "/"
                                + policy.policyIdentifier()
                );
            }
        }
    }

    private static void requireActivationCoverage(
            Set<String> capabilityIdentifiers,
            List<ExecutableCapabilityActivation> activations
    ) {
        Set<String> activationIdentifiers = new HashSet<>();
        for (ExecutableCapabilityActivation activation : activations) {
            if (!activationIdentifiers.add(activation.capabilityIdentifier())) {
                throw new IllegalArgumentException(
                        "Duplicate executable capability activation: "
                                + activation.capabilityIdentifier()
                );
            }
        }
        if (!activationIdentifiers.equals(capabilityIdentifiers)) {
            throw new IllegalArgumentException(
                    "Capability activations must exactly cover active capabilities"
            );
        }
    }

    private static List<ExecutableCapabilityActivation>
            merchantSelectedActivations(Set<String> capabilityIdentifiers) {
        return Objects.requireNonNull(capabilityIdentifiers).stream()
                .sorted()
                .map(identifier -> new ExecutableCapabilityActivation(
                        identifier,
                        Set.of(CapabilityActivationReason.merchantSelected())
                ))
                .toList();
    }

    private record SchemaIdentity(
            String ownerCapabilityIdentifier,
            String schemaIdentifier,
            long schemaVersion
    ) {
    }

    private record PolicyIdentity(
            String ownerCapabilityIdentifier,
            String policyIdentifier
    ) {
    }
}
