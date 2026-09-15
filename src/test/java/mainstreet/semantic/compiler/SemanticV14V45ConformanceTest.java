package mainstreet.semantic.compiler;

import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableAllocationClaimEffect;
import mainstreet.semantic.executable.ExecutableAllocationReleaseEffect;
import mainstreet.semantic.executable.ExecutableCapabilityDataConceptReference;
import mainstreet.semantic.executable.ExecutableDataMutationEffect;
import mainstreet.semantic.executable.ExecutableFieldSpecificSemantics;
import mainstreet.semantic.executable.ExecutableGlobalDataConceptReference;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableRelationshipEstablishmentEffect;
import mainstreet.semantic.executable.ExecutableRelationshipRemovalEffect;
import mainstreet.semantic.executable.ExecutableStateTransitionEffect;
import mainstreet.semantic.registry.CapabilityDataConceptReference;
import mainstreet.semantic.registry.FieldSpecificSemantics;
import mainstreet.semantic.registry.GlobalDataConceptReference;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedAllocationClaimEffect;
import mainstreet.semantic.registry.OwnedAllocationReleaseEffect;
import mainstreet.semantic.registry.OwnedDataConceptDefinition;
import mainstreet.semantic.registry.OwnedDataMutationEffect;
import mainstreet.semantic.registry.OwnedFieldDefinition;
import mainstreet.semantic.registry.OwnedObjectCreationEffect;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedRelationshipDefinition;
import mainstreet.semantic.registry.OwnedRelationshipEstablishmentEffect;
import mainstreet.semantic.registry.OwnedRelationshipRemovalEffect;
import mainstreet.semantic.registry.OwnedSchemaDefinition;
import mainstreet.semantic.registry.OwnedSchemaReference;
import mainstreet.semantic.registry.OwnedStateTransitionEffect;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.RegisteredGlobalDataConcept;
import mainstreet.semantic.registry.RelationshipCardinality;
import mainstreet.semantic.registry.RelationshipScopeConstraint;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SemanticV14V45ConformanceTest {

    @Test
    void operational_object_may_exist_without_lifecycle_or_schema() {
        OwnedOperationalObjectDefinition customerContext =
                new OwnedOperationalObjectDefinition(
                        "customer-context",
                        Set.of(),
                        Optional.empty(),
                        Optional.empty()
                );

        assertFalse(customerContext.hasLifecycle());
        assertTrue(customerContext.stateIdentifiers().isEmpty());
        assertTrue(customerContext.initialStateIdentifier().isEmpty());
        assertTrue(customerContext.schemaReference().isEmpty());
    }

    @Test
    void snapshot_rejects_an_unregistered_global_data_concept_reference() {
        OwnedSchemaDefinition schema = new OwnedSchemaDefinition(
                "listing",
                1,
                List.of(new OwnedFieldDefinition(
                        "asking-price",
                        new GlobalDataConceptReference("money")
                ))
        );
        RegisteredCapability listing = new RegisteredCapability(
                "listing",
                List.of(new OwnedOperationalObjectDefinition(
                        "listing",
                        Set.of(),
                        Optional.empty(),
                        Optional.of(new OwnedSchemaReference("listing", 1))
                )),
                List.of(),
                List.of(),
                List.of(),
                List.of(schema),
                List.of()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(listing)
                )
        );
    }

    @Test
    void capability_rejects_an_unregistered_capability_data_concept_reference() {
        OwnedSchemaDefinition schema = new OwnedSchemaDefinition(
                "opportunity",
                1,
                List.of(new OwnedFieldDefinition(
                        "funding-type",
                        new CapabilityDataConceptReference("funding-type")
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new RegisteredCapability(
                        "publication",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(schema),
                        List.of()
                )
        );
    }

    @Test
    void compiler_materialises_schema_semantics_and_complete_effect_algebra() {
        OwnedSchemaDefinition caseSchema = new OwnedSchemaDefinition(
                "case-record",
                1,
                List.of(
                        new OwnedFieldDefinition(
                                "summary",
                                new GlobalDataConceptReference("text")
                        ),
                        new OwnedFieldDefinition(
                                "priority",
                                new CapabilityDataConceptReference("case-priority")
                        ),
                        new OwnedFieldDefinition(
                                "internal-note",
                                new FieldSpecificSemantics()
                        )
                )
        );
        OwnedOperationalObjectDefinition caseRecord =
                new OwnedOperationalObjectDefinition(
                        "case-record",
                        Set.of("draft", "open"),
                        Optional.of("draft"),
                        Optional.of(new OwnedSchemaReference("case-record", 1))
                );
        OwnedOperationalObjectDefinition party =
                new OwnedOperationalObjectDefinition(
                        "party",
                        Set.of(),
                        Optional.empty(),
                        Optional.empty()
                );
        OwnedRelationshipDefinition relationship =
                new OwnedRelationshipDefinition(
                        "case.party",
                        "PARTY",
                        "case-record",
                        new OwnedOperationalObjectTypeReference(
                                "case-management",
                                "party"
                        ),
                        RelationshipCardinality.ZERO_OR_MORE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                );
        OwnedOperationDefinition operation =
                new OwnedOperationDefinition(
                        "case.exercise-effects",
                        List.of(
                                new OwnedObjectCreationEffect(
                                        "case-record",
                                        Optional.of("draft")
                                ),
                                new OwnedStateTransitionEffect(
                                        "case-record",
                                        "draft",
                                        "open"
                                ),
                                new OwnedDataMutationEffect(
                                        "case-record",
                                        "summary"
                                ),
                                new OwnedRelationshipEstablishmentEffect(
                                        "case.party"
                                ),
                                new OwnedRelationshipRemovalEffect(
                                        "case.party"
                                ),
                                new OwnedAllocationClaimEffect(
                                        "case.capacity"
                                ),
                                new OwnedAllocationReleaseEffect(
                                        "case.capacity"
                                )
                        ),
                        Set.of("case.effects-exercised"),
                        "case.exercise-effects"
                );
        RegisteredCapability capability = new RegisteredCapability(
                "case-management",
                List.of(caseRecord, party),
                List.of(operation),
                List.of(),
                List.of(relationship),
                List.of(caseSchema),
                List.of(new OwnedDataConceptDefinition("case-priority"))
        );
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(
                        new RegisteredGlobalDataConcept("text"),
                        capability
                )
        ));

        ExecutableMerchantModel model = new ConfigurationCompiler(registry)
                .compile(new MerchantConfiguration(
                        "merchant-001",
                        "configuration-001",
                        1,
                        "semantic-registry-1.0",
                        Set.of("case-management")
                ));

        assertTrue(model.operationalObject(
                "case-management",
                "case-record"
        ).isPresent());
        assertTrue(model.operationalObject(
                "case-management",
                "party"
        ).isPresent());
        assertTrue(model.relationship("case.party").isPresent());
        var schema = model.schema("case-management", "case-record", 1)
                .orElseThrow();
        assertInstanceOf(
                ExecutableGlobalDataConceptReference.class,
                schema.field("summary").orElseThrow().semanticBasis()
        );
        assertInstanceOf(
                ExecutableCapabilityDataConceptReference.class,
                schema.field("priority").orElseThrow().semanticBasis()
        );
        assertInstanceOf(
                ExecutableFieldSpecificSemantics.class,
                schema.field("internal-note").orElseThrow().semanticBasis()
        );
        assertEquals(
                Set.of(
                        "merchantIdentifier",
                        "modelIdentifier",
                        "version",
                        "semanticRegistryVersion",
                        "capabilityIdentifiers",
                        "operationalObjects",
                        "schemas",
                        "relationships",
                        "operations",
                        "policies",
                        "capabilityActivations"
                ),
                Arrays.stream(ExecutableMerchantModel.class.getRecordComponents())
                        .map(java.lang.reflect.RecordComponent::getName)
                        .collect(java.util.stream.Collectors.toSet())
        );

        var effects = model.operation("case.exercise-effects")
                .orElseThrow()
                .effects();
        assertEquals(7, effects.size());
        assertInstanceOf(ExecutableObjectCreationEffect.class, effects.get(0));
        assertInstanceOf(ExecutableStateTransitionEffect.class, effects.get(1));
        ExecutableDataMutationEffect mutation = assertInstanceOf(
                ExecutableDataMutationEffect.class,
                effects.get(2)
        );
        assertEquals(
                new ExecutableOperationalObjectTypeIdentity(
                        "case-management",
                        "case-record"
                ),
                mutation.targetObjectType()
        );
        assertEquals("case-management", mutation.targetField().ownerCapabilityIdentifier());
        assertEquals("case-record", mutation.targetField().schemaIdentifier());
        assertEquals(1, mutation.targetField().schemaVersion());
        assertEquals("summary", mutation.targetField().fieldIdentifier());
        assertInstanceOf(
                ExecutableRelationshipEstablishmentEffect.class,
                effects.get(3)
        );
        assertInstanceOf(
                ExecutableRelationshipRemovalEffect.class,
                effects.get(4)
        );
        assertInstanceOf(ExecutableAllocationClaimEffect.class, effects.get(5));
        assertInstanceOf(ExecutableAllocationReleaseEffect.class, effects.get(6));
    }

    @Test
    void one_capability_cannot_directly_mutate_another_capability_object() {
        OwnedOperationDefinition invalid = new OwnedOperationDefinition(
                "booking.change-customer-email",
                List.of(new OwnedDataMutationEffect(
                        "customer-context",
                        "email"
                )),
                Set.of("CustomerEmailChanged"),
                "booking.change-customer-email"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new RegisteredCapability(
                        "booking",
                        List.of(),
                        List.of(invalid)
                )
        );
    }

    @Test
    void merchant_configuration_still_cannot_define_templates_schemas_or_fields() {
        assertEquals(
                Set.of(
                        "merchantIdentifier",
                        "configurationIdentifier",
                        "version",
                        "semanticRegistryVersion",
                        "capabilityIdentifiers",
                        "policySelections",
                        "baseConfigurationIdentifier",
                        "fulfilmentBindingSetRevisionReference"
                ),
                Arrays.stream(MerchantConfiguration.class.getRecordComponents())
                        .map(java.lang.reflect.RecordComponent::getName)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }
}
