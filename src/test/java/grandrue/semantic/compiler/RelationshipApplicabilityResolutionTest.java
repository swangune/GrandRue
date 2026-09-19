package grandrue.semantic.compiler;

import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.executable.CapabilityActivationReason;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.executable.ExecutableRelationshipDefinition;
import grandrue.semantic.executable.ExecutableRelationshipEstablishmentEffect;
import grandrue.semantic.registry.CapabilityRelationshipType;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedObjectCreationEffect;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.OwnedOperationalObjectTypeReference;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.OwnedRelationshipDefinition;
import grandrue.semantic.registry.OwnedRelationshipEstablishmentEffect;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.RegisteredCapabilityRelationship;
import grandrue.semantic.registry.RelationshipCardinality;
import grandrue.semantic.registry.RelationshipScopeConstraint;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelationshipApplicabilityResolutionTest {

    @Test
    void inactive_target_does_not_activate_or_materialise_relationship_semantics() {
        var model = compile(
                Set.of("enquiry"),
                Set.of(new RegisteredCapabilityRelationship(
                        "enquiry",
                        CapabilityRelationshipType.SUPPORTS,
                        "listing"
                ))
        );

        assertEquals(Set.of("enquiry"), model.capabilityIdentifiers());
        assertTrue(model.activation("listing").isEmpty());
        assertTrue(model.relationship("enquiry.subject-listing").isEmpty());

        var effects = model.operation("enquiry.create").orElseThrow().effects();
        assertEquals(1, effects.size());
        assertInstanceOf(ExecutableObjectCreationEffect.class, effects.getFirst());
    }

    @Test
    void explicitly_active_target_materialises_relationship_and_effect() {
        var model = compile(
                Set.of("enquiry", "listing"),
                Set.of()
        );

        assertEquals(Set.of("enquiry", "listing"), model.capabilityIdentifiers());
        assertTrue(model.relationship("enquiry.subject-listing").isPresent());

        var effects = model.operation("enquiry.create").orElseThrow().effects();
        assertEquals(2, effects.size());
        assertInstanceOf(ExecutableObjectCreationEffect.class, effects.get(0));
        assertInstanceOf(
                ExecutableRelationshipEstablishmentEffect.class,
                effects.get(1)
        );
    }

    @Test
    void requires_activates_target_before_relationship_applicability_is_resolved() {
        var model = compile(
                Set.of("enquiry"),
                Set.of(new RegisteredCapabilityRelationship(
                        "enquiry",
                        CapabilityRelationshipType.REQUIRES,
                        "listing"
                ))
        );

        assertEquals(Set.of("enquiry", "listing"), model.capabilityIdentifiers());
        assertEquals(
                Set.of(CapabilityActivationReason.requiredBy("enquiry")),
                model.activation("listing").orElseThrow().reasons()
        );
        assertTrue(model.relationship("enquiry.subject-listing").isPresent());
        assertTrue(model.operation("enquiry.create").orElseThrow().effects().stream()
                .anyMatch(ExecutableRelationshipEstablishmentEffect.class::isInstance));
    }

    @Test
    void executable_model_rejects_a_relationship_whose_target_type_is_absent() {
        ExecutableOperationalObjectTypeIdentity enquiryType =
                new ExecutableOperationalObjectTypeIdentity(
                        "enquiry",
                        "enquiry"
                );
        ExecutableOperationalObjectTypeIdentity listingType =
                new ExecutableOperationalObjectTypeIdentity(
                        "listing",
                        "listing"
                );
        ExecutableRelationshipDefinition relationship =
                new ExecutableRelationshipDefinition(
                        "enquiry.subject-listing",
                        "SUBJECT",
                        enquiryType,
                        listingType,
                        RelationshipCardinality.OPTIONAL_ONE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                );
        ExecutableOperationDefinition operation =
                new ExecutableOperationDefinition(
                        "enquiry.create",
                        List.of(new ExecutableObjectCreationEffect(
                                enquiryType,
                                Optional.empty()
                        )),
                        Set.of("enquiry.created"),
                        "enquiry.create"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExecutableMerchantModel(
                        "merchant-001",
                        "configuration-001",
                        1,
                        "semantic-registry-1.0",
                        Set.of("enquiry"),
                        List.of(new ExecutableOperationalObjectDefinition(
                                "enquiry",
                                "enquiry",
                                Set.of(),
                                Optional.empty(),
                                Optional.empty()
                        )),
                        List.of(),
                        List.of(relationship),
                        List.of(operation),
                        List.of()
                )
        );
    }

    private static ExecutableMerchantModel compile(
            Set<String> selectedCapabilities,
            Set<RegisteredCapabilityRelationship> capabilityRelationships
    ) {
        RegisteredCapability listing = new RegisteredCapability(
                "listing",
                List.of(statelessObject("listing")),
                List.of()
        );
        RegisteredCapability enquiry = new RegisteredCapability(
                "enquiry",
                List.of(statelessObject("enquiry")),
                List.of(new OwnedOperationDefinition(
                        "enquiry.create",
                        List.of(
                                new OwnedObjectCreationEffect(
                                        "enquiry",
                                        Optional.empty()
                                ),
                                new OwnedRelationshipEstablishmentEffect(
                                        "enquiry.subject-listing"
                                )
                        ),
                        Set.of("enquiry.created"),
                        "enquiry.create"
                )),
                List.of(),
                List.of(new OwnedRelationshipDefinition(
                        "enquiry.subject-listing",
                        "SUBJECT",
                        "enquiry",
                        new OwnedOperationalObjectTypeReference(
                                "listing",
                                "listing"
                        ),
                        RelationshipCardinality.OPTIONAL_ONE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                ))
        );

        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(enquiry, listing),
                capabilityRelationships
        ));

        return new ConfigurationCompiler(registry).compile(
                new MerchantConfiguration(
                        "merchant-001",
                        "configuration-001",
                        1,
                        "semantic-registry-1.0",
                        selectedCapabilities
                )
        );
    }

    private static OwnedOperationalObjectDefinition statelessObject(
            String identifier
    ) {
        return new OwnedOperationalObjectDefinition(
                identifier,
                Set.of(),
                Optional.empty(),
                Optional.empty()
        );
    }
}
