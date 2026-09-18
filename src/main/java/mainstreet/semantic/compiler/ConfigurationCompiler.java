package mainstreet.semantic.compiler;

import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.PolicySelection;
import grandrue.semantic.executable.CapabilityActivationReason;
import grandrue.semantic.executable.ExecutableAllocationClaimEffect;
import grandrue.semantic.executable.ExecutableAllocationReleaseEffect;
import grandrue.semantic.executable.ExecutableCapabilityActivation;
import grandrue.semantic.executable.ExecutableCapabilityDataConceptReference;
import grandrue.semantic.executable.ExecutableConditionalRequirementDefinition;
import grandrue.semantic.executable.ExecutableDataMutationEffect;
import grandrue.semantic.executable.ExecutableFieldDefinition;
import grandrue.semantic.executable.ExecutableFieldReference;
import grandrue.semantic.executable.ExecutableFieldSemanticBasis;
import grandrue.semantic.executable.ExecutableFieldSpecificSemantics;
import grandrue.semantic.executable.ExecutableGlobalDataConceptReference;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.executable.ExecutableOperationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutablePolicyValue;
import grandrue.semantic.executable.ExecutableRelationshipDefinition;
import grandrue.semantic.executable.ExecutableRelationshipEstablishmentEffect;
import grandrue.semantic.executable.ExecutableRelationshipRemovalEffect;
import grandrue.semantic.executable.ExecutableRequirementDefinition;
import grandrue.semantic.executable.ExecutableSchemaDefinition;
import grandrue.semantic.executable.ExecutableSchemaReference;
import grandrue.semantic.executable.ExecutableStateTransitionEffect;
import grandrue.semantic.registry.CapabilityDataConceptReference;
import grandrue.semantic.registry.CapabilityRelationshipType;
import grandrue.semantic.registry.FieldSpecificSemantics;
import grandrue.semantic.registry.GlobalDataConceptReference;
import grandrue.semantic.registry.OwnedAllocationClaimEffect;
import grandrue.semantic.registry.OwnedAllocationReleaseEffect;
import grandrue.semantic.registry.OwnedConditionalRequirementDefinition;
import grandrue.semantic.registry.OwnedDataMutationEffect;
import grandrue.semantic.registry.OwnedFieldDefinition;
import grandrue.semantic.registry.OwnedObjectCreationEffect;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.OwnedOperationEffect;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.OwnedPolicyDefinition;
import grandrue.semantic.registry.OwnedRelationshipDefinition;
import grandrue.semantic.registry.OwnedRelationshipEstablishmentEffect;
import grandrue.semantic.registry.OwnedRelationshipRemovalEffect;
import grandrue.semantic.registry.OwnedSchemaDefinition;
import grandrue.semantic.registry.OwnedSchemaReference;
import grandrue.semantic.registry.OwnedStateTransitionEffect;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.RegisteredCapabilityRelationship;
import grandrue.semantic.registry.SemanticRegistry;
import grandrue.semantic.registry.SemanticRegistrySnapshot;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolves and validates platform-owned registered semantics into an immutable
 * executable merchant model. It is authoritative over validity, never over
 * semantic meaning.
 */
public final class ConfigurationCompiler {

    private final SemanticRegistry registry;

    public ConfigurationCompiler(SemanticRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    public ExecutableMerchantModel compile(MerchantConfiguration configuration) {
        Objects.requireNonNull(configuration);
        SemanticRegistrySnapshot snapshot = registry.version(
                        configuration.semanticRegistryVersion()
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown semantic registry version: "
                                + configuration.semanticRegistryVersion()
                ));

        CapabilityResolution capabilityResolution =
                resolveCapabilities(configuration, snapshot);
        List<RegisteredCapability> capabilities =
                capabilityResolution.capabilities();
        Set<String> activeCapabilityIdentifiers =
                capabilityResolution.identifiers();

        List<ExecutableSchemaDefinition> schemas = capabilities.stream()
                .flatMap(capability -> capability.schemas().stream()
                        .map(schema -> materialiseSchema(
                                capability.identifier(),
                                schema
                        )))
                .toList();
        List<ExecutableOperationalObjectDefinition> operationalObjects =
                capabilities.stream()
                        .flatMap(capability -> capability.operationalObjects()
                                .stream()
                                .map(object -> materialiseOperationalObject(
                                        capability.identifier(),
                                        object
                                )))
                        .toList();
        List<ExecutableRelationshipDefinition> relationships = capabilities.stream()
                .flatMap(capability -> capability.relationships().stream()
                        .filter(relationship -> relationshipTargetIsActive(
                                relationship,
                                activeCapabilityIdentifiers
                        ))
                        .map(relationship -> materialiseRelationship(
                                capability.identifier(),
                                relationship
                        )))
                .toList();
        Set<String> applicableRelationshipIdentifiers = relationships.stream()
                .map(ExecutableRelationshipDefinition::identifier)
                .collect(Collectors.toUnmodifiableSet());
        List<ExecutableOperationDefinition> operations = capabilities.stream()
                .flatMap(capability -> capability.operations().stream()
                        .map(operation -> materialiseOperation(
                                capability,
                                operation,
                                applicableRelationshipIdentifiers
                        )))
                .toList();
        List<ExecutablePolicyValue> policies = resolvePolicies(
                configuration,
                snapshot,
                activeCapabilityIdentifiers
        );

        return new ExecutableMerchantModel(
                configuration.merchantIdentifier(),
                configuration.configurationIdentifier(),
                configuration.version(),
                configuration.semanticRegistryVersion(),
                activeCapabilityIdentifiers,
                operationalObjects,
                schemas,
                relationships,
                operations,
                policies,
                capabilityResolution.activations()
        );
    }

    private static List<ExecutablePolicyValue> resolvePolicies(
            MerchantConfiguration configuration,
            SemanticRegistrySnapshot snapshot,
            Set<String> activeCapabilityIdentifiers
    ) {
        Map<PolicyIdentity, PolicySelection> selections =
                configuration.policySelections().stream().collect(
                        Collectors.toUnmodifiableMap(
                                PolicyIdentity::of,
                                Function.identity()
                        )
                );
        validatePolicySelections(
                snapshot,
                activeCapabilityIdentifiers,
                selections
        );

        return snapshot.capabilities().stream()
                .flatMap(capability -> capability.policies().stream()
                        .sorted(Comparator.comparing(
                                OwnedPolicyDefinition::identifier
                        ))
                        .map(policy -> resolvePolicy(
                                activeCapabilityIdentifiers,
                                capability,
                                policy,
                                selections.get(new PolicyIdentity(
                                        capability.identifier(),
                                        policy.identifier()
                                ))
                        )))
                .toList();
    }

    private static void validatePolicySelections(
            SemanticRegistrySnapshot snapshot,
            Set<String> activeCapabilityIdentifiers,
            Map<PolicyIdentity, PolicySelection> selections
    ) {
        for (PolicySelection selection : selections.values()) {
            RegisteredCapability owner = snapshot.capability(
                            selection.ownerCapabilityIdentifier()
                    )
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Policy owner capability is not registered: "
                                    + selection.ownerCapabilityIdentifier()
                    ));
            if (!activeCapabilityIdentifiers.contains(owner.identifier())) {
                throw new IllegalArgumentException(
                        "Policy selection cannot activate its owner: "
                                + owner.identifier()
                );
            }
            OwnedPolicyDefinition policy = owner.policies().stream()
                    .filter(candidate -> candidate.identifier().equals(
                            selection.policyIdentifier()
                    ))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Policy is not registered for capability: "
                                    + selection.policyIdentifier()
                    ));
            if (!policy.allows(selection.selectedValue())) {
                throw new IllegalArgumentException(
                        "Policy value is not allowed: "
                                + selection.selectedValue()
                );
            }
        }
    }

    private static ExecutablePolicyValue resolvePolicy(
            Set<String> activeCapabilityIdentifiers,
            RegisteredCapability owner,
            OwnedPolicyDefinition policy,
            PolicySelection selection
    ) {
        if (!activeCapabilityIdentifiers.contains(owner.identifier())) {
            return ExecutablePolicyValue.notApplicable(
                    owner.identifier(),
                    policy.identifier()
            );
        }
        if (selection == null) {
            return ExecutablePolicyValue.defaulted(
                    owner.identifier(),
                    policy.identifier(),
                    policy.defaultValue()
            );
        }
        return ExecutablePolicyValue.explicitlySelected(
                owner.identifier(),
                policy.identifier(),
                selection.selectedValue()
        );
    }

    private static CapabilityResolution resolveCapabilities(
            MerchantConfiguration configuration,
            SemanticRegistrySnapshot snapshot
    ) {
        Map<String, RegisteredCapability> registered =
                snapshot.capabilities().stream().collect(
                        Collectors.toUnmodifiableMap(
                                RegisteredCapability::identifier,
                                Function.identity()
                        )
                );
        Map<String, Set<CapabilityActivationReason>> reasons =
                new LinkedHashMap<>();
        Queue<String> unresolved = new ArrayDeque<>();

        configuration.capabilityIdentifiers().stream()
                .sorted()
                .forEach(identifier -> {
                    requireRegisteredCapability(
                            identifier,
                            snapshot,
                            registered
                    );
                    reasons.computeIfAbsent(identifier, ignored -> {
                        unresolved.add(identifier);
                        return new LinkedHashSet<>();
                    }).add(CapabilityActivationReason.merchantSelected());
                });

        Map<String, List<RegisteredCapabilityRelationship>> requirements =
                snapshot.capabilityRelationships().stream()
                        .filter(relationship -> relationship.type()
                                == CapabilityRelationshipType.REQUIRES)
                        .sorted(Comparator
                                .comparing(RegisteredCapabilityRelationship::
                                        sourceCapabilityIdentifier)
                                .thenComparing(RegisteredCapabilityRelationship::
                                        targetCapabilityIdentifier))
                        .collect(Collectors.groupingBy(
                                RegisteredCapabilityRelationship::
                                        sourceCapabilityIdentifier,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        while (!unresolved.isEmpty()) {
            String source = unresolved.remove();
            for (RegisteredCapabilityRelationship requirement
                    : requirements.getOrDefault(source, List.of())) {
                String target = requirement.targetCapabilityIdentifier();
                Set<CapabilityActivationReason> targetReasons =
                        reasons.get(target);
                if (targetReasons == null) {
                    targetReasons = new LinkedHashSet<>();
                    reasons.put(target, targetReasons);
                    unresolved.add(target);
                }
                targetReasons.add(CapabilityActivationReason.requiredBy(source));
            }
        }

        Set<String> activeIdentifiers = Set.copyOf(reasons.keySet());
        rejectActiveConflicts(snapshot, activeIdentifiers);
        List<RegisteredCapability> activeCapabilities = activeIdentifiers.stream()
                .sorted()
                .map(registered::get)
                .toList();
        List<ExecutableCapabilityActivation> activations = reasons.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new ExecutableCapabilityActivation(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
        return new CapabilityResolution(
                activeCapabilities,
                activeIdentifiers,
                activations
        );
    }

    private static void requireRegisteredCapability(
            String identifier,
            SemanticRegistrySnapshot snapshot,
            Map<String, RegisteredCapability> registered
    ) {
        if (!registered.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "Capability is not registered in "
                            + snapshot.version()
                            + ": "
                            + identifier
            );
        }
    }

    private static void rejectActiveConflicts(
            SemanticRegistrySnapshot snapshot,
            Set<String> activeIdentifiers
    ) {
        snapshot.capabilityRelationships().stream()
                .filter(relationship -> relationship.type()
                        == CapabilityRelationshipType.CONFLICTS_WITH)
                .filter(relationship -> activeIdentifiers.contains(
                        relationship.sourceCapabilityIdentifier()
                ))
                .filter(relationship -> activeIdentifiers.contains(
                        relationship.targetCapabilityIdentifier()
                ))
                .findFirst()
                .ifPresent(conflict -> {
                    throw new IllegalArgumentException(
                            "Active capabilities conflict: "
                                    + conflict.sourceCapabilityIdentifier()
                                    + " and "
                                    + conflict.targetCapabilityIdentifier()
                    );
                });
    }

    private static ExecutableSchemaDefinition materialiseSchema(
            String ownerCapabilityIdentifier,
            OwnedSchemaDefinition schema
    ) {
        return new ExecutableSchemaDefinition(
                ownerCapabilityIdentifier,
                schema.identifier(),
                schema.version(),
                schema.fields().stream()
                        .map(ConfigurationCompiler::materialiseField)
                        .toList()
        );
    }

    private static ExecutableFieldDefinition materialiseField(
            OwnedFieldDefinition field
    ) {
        return new ExecutableFieldDefinition(
                field.identifier(),
                materialiseFieldSemanticBasis(field)
        );
    }

    private static ExecutableFieldSemanticBasis materialiseFieldSemanticBasis(
            OwnedFieldDefinition field
    ) {
        return switch (field.semanticBasis()) {
            case GlobalDataConceptReference reference ->
                    new ExecutableGlobalDataConceptReference(
                            reference.identifier()
                    );
            case CapabilityDataConceptReference reference ->
                    new ExecutableCapabilityDataConceptReference(
                            reference.identifier()
                    );
            case FieldSpecificSemantics ignored ->
                    new ExecutableFieldSpecificSemantics();
        };
    }

    private static ExecutableOperationalObjectDefinition
            materialiseOperationalObject(
                    String ownerCapabilityIdentifier,
                    OwnedOperationalObjectDefinition object
            ) {
        return new ExecutableOperationalObjectDefinition(
                ownerCapabilityIdentifier,
                object.identifier(),
                object.stateIdentifiers(),
                object.initialStateIdentifier(),
                object.schemaReference().map(reference ->
                        new ExecutableSchemaReference(
                                ownerCapabilityIdentifier,
                                reference.schemaIdentifier(),
                                reference.schemaVersion()
                        ))
        );
    }

    private static boolean relationshipTargetIsActive(
            OwnedRelationshipDefinition relationship,
            Set<String> activeCapabilityIdentifiers
    ) {
        return activeCapabilityIdentifiers.contains(
                relationship.targetObjectType().ownerCapabilityIdentifier()
        );
    }

    private static ExecutableRelationshipDefinition materialiseRelationship(
            String ownerCapabilityIdentifier,
            OwnedRelationshipDefinition relationship
    ) {
        return new ExecutableRelationshipDefinition(
                relationship.identifier(),
                relationship.role(),
                new ExecutableOperationalObjectTypeIdentity(
                        ownerCapabilityIdentifier,
                        relationship.sourceObjectIdentifier()
                ),
                new ExecutableOperationalObjectTypeIdentity(
                        relationship.targetObjectType().ownerCapabilityIdentifier(),
                        relationship.targetObjectType().objectIdentifier()
                ),
                relationship.cardinality(),
                relationship.scopeConstraint()
        );
    }

    private static ExecutableOperationDefinition materialiseOperation(
            RegisteredCapability owner,
            OwnedOperationDefinition operation,
            Set<String> applicableRelationshipIdentifiers
    ) {
        List<ExecutableOperationEffect> effects = operation.effects().stream()
                .filter(effect -> effectIsApplicable(
                        effect,
                        applicableRelationshipIdentifiers
                ))
                .map(effect -> materialiseEffect(owner, effect))
                .toList();
        if (effects.isEmpty()) {
            throw new IllegalArgumentException(
                    "Resolved operation has no applicable effects: "
                            + operation.identifier()
            );
        }

        return new ExecutableOperationDefinition(
                operation.identifier(),
                effects,
                operation.unconditionalRequirements().stream()
                        .map(requirement ->
                                new ExecutableRequirementDefinition(
                                        requirement.identifier()
                                ))
                        .toList(),
                operation.conditionalRequirements().stream()
                        .map(ConfigurationCompiler::
                                materialiseConditionalRequirement)
                        .toList(),
                operation.eventIdentifiers(),
                operation.requiredPrivilegeIdentifier()
        );
    }

    private static boolean effectIsApplicable(
            OwnedOperationEffect effect,
            Set<String> applicableRelationshipIdentifiers
    ) {
        return switch (effect) {
            case OwnedRelationshipEstablishmentEffect establishment ->
                    applicableRelationshipIdentifiers.contains(
                            establishment.relationshipIdentifier()
                    );
            case OwnedRelationshipRemovalEffect removal ->
                    applicableRelationshipIdentifiers.contains(
                            removal.relationshipIdentifier()
                    );
            default -> true;
        };
    }

    private static ExecutableConditionalRequirementDefinition
            materialiseConditionalRequirement(
                    OwnedConditionalRequirementDefinition requirement
            ) {
        return new ExecutableConditionalRequirementDefinition(
                requirement.identifier(),
                requirement.applicabilityConditionIdentifier()
        );
    }

    private static ExecutableOperationEffect materialiseEffect(
            RegisteredCapability owner,
            OwnedOperationEffect effect
    ) {
        return switch (effect) {
            case OwnedObjectCreationEffect creation ->
                    new ExecutableObjectCreationEffect(
                            objectType(owner, creation.targetObjectIdentifier()),
                            creation.initialStateIdentifier()
                    );
            case OwnedStateTransitionEffect transition ->
                    new ExecutableStateTransitionEffect(
                            objectType(owner, transition.targetObjectIdentifier()),
                            transition.sourceStateIdentifier(),
                            transition.resultingStateIdentifier()
                    );
            case OwnedDataMutationEffect mutation ->
                    materialiseDataMutation(owner, mutation);
            case OwnedRelationshipEstablishmentEffect establishment ->
                    new ExecutableRelationshipEstablishmentEffect(
                            establishment.relationshipIdentifier()
                    );
            case OwnedRelationshipRemovalEffect removal ->
                    new ExecutableRelationshipRemovalEffect(
                            removal.relationshipIdentifier()
                    );
            case OwnedAllocationClaimEffect claim ->
                    new ExecutableAllocationClaimEffect(
                            claim.resourceRoleIdentifier()
                    );
            case OwnedAllocationReleaseEffect release ->
                    new ExecutableAllocationReleaseEffect(
                            release.resourceRoleIdentifier()
                    );
        };
    }

    private static ExecutableDataMutationEffect materialiseDataMutation(
            RegisteredCapability owner,
            OwnedDataMutationEffect mutation
    ) {
        OwnedOperationalObjectDefinition object = owner.operationalObjects()
                .stream()
                .filter(candidate -> candidate.identifier().equals(
                        mutation.targetObjectIdentifier()
                ))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Validated mutation target object is absent"
                ));
        OwnedSchemaReference schemaReference = object.schemaReference()
                .orElseThrow(() -> new IllegalStateException(
                        "Validated mutation target schema is absent"
                ));
        OwnedSchemaDefinition schema = owner.requireSchema(schemaReference);
        schema.field(mutation.targetFieldIdentifier())
                .orElseThrow(() -> new IllegalStateException(
                        "Validated mutation target field is absent"
                ));

        return new ExecutableDataMutationEffect(
                objectType(owner, mutation.targetObjectIdentifier()),
                new ExecutableFieldReference(
                        owner.identifier(),
                        schemaReference.schemaIdentifier(),
                        schemaReference.schemaVersion(),
                        mutation.targetFieldIdentifier()
                )
        );
    }

    private static ExecutableOperationalObjectTypeIdentity objectType(
            RegisteredCapability owner,
            String objectIdentifier
    ) {
        return new ExecutableOperationalObjectTypeIdentity(
                owner.identifier(),
                objectIdentifier
        );
    }

    private record PolicyIdentity(
            String ownerCapabilityIdentifier,
            String policyIdentifier
    ) {
        private static PolicyIdentity of(PolicySelection selection) {
            return new PolicyIdentity(
                    selection.ownerCapabilityIdentifier(),
                    selection.policyIdentifier()
            );
        }
    }

    private record CapabilityResolution(
            List<RegisteredCapability> capabilities,
            Set<String> identifiers,
            List<ExecutableCapabilityActivation> activations
    ) {
    }
}
