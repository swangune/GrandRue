package mainstreet.semantic.compiler;

import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;
import mainstreet.semantic.registry.OwnedRelationshipDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.RelationshipCardinality;
import mainstreet.semantic.registry.RelationshipScopeConstraint;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationalObjectTypeIdentityConformanceTest {

    @Test
    void active_capabilities_may_own_the_same_local_object_identifier() {
        RegisteredCapability support = new RegisteredCapability(
                "support",
                List.of(statelessObject("case")),
                List.of()
        );
        RegisteredCapability legal = new RegisteredCapability(
                "legal",
                List.of(statelessObject("case")),
                List.of()
        );
        InMemorySemanticRegistry registry = registry(support, legal);

        ExecutableMerchantModel model = new ConfigurationCompiler(registry)
                .compile(configuration(Set.of("support", "legal")));

        var supportCase = model.operationalObject("support", "case")
                .orElseThrow();
        var legalCase = model.operationalObject("legal", "case")
                .orElseThrow();

        assertEquals(
                new ExecutableOperationalObjectTypeIdentity("support", "case"),
                supportCase.identity()
        );
        assertEquals(
                new ExecutableOperationalObjectTypeIdentity("legal", "case"),
                legalCase.identity()
        );
        assertNotEquals(supportCase.identity(), legalCase.identity());
    }

    @Test
    void cross_capability_relationship_endpoints_are_owner_qualified() {
        RegisteredCapability customer = new RegisteredCapability(
                "customer",
                List.of(statelessObject("customer-context")),
                List.of()
        );
        RegisteredCapability booking = new RegisteredCapability(
                "booking",
                List.of(statelessObject("booking")),
                List.of(),
                List.of(),
                List.of(new OwnedRelationshipDefinition(
                        "booking.customer-context",
                        "CUSTOMER",
                        "booking",
                        new OwnedOperationalObjectTypeReference(
                                "customer",
                                "customer-context"
                        ),
                        RelationshipCardinality.REQUIRED_ONE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                ))
        );
        InMemorySemanticRegistry registry = registry(customer, booking);

        ExecutableMerchantModel model = new ConfigurationCompiler(registry)
                .compile(configuration(Set.of("booking", "customer")));

        var relationship = model.relationship("booking.customer-context")
                .orElseThrow();
        assertEquals(
                new ExecutableOperationalObjectTypeIdentity("booking", "booking"),
                relationship.sourceObjectType()
        );
        assertEquals(
                new ExecutableOperationalObjectTypeIdentity(
                        "customer",
                        "customer-context"
                ),
                relationship.targetObjectType()
        );
    }

    @Test
    void merchant_configuration_still_contains_no_object_namespace_surface() {
        Set<String> componentNames = java.util.Arrays.stream(
                        MerchantConfiguration.class.getRecordComponents()
                )
                .map(java.lang.reflect.RecordComponent::getName)
                .collect(java.util.stream.Collectors.toSet());

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
                componentNames
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

    private static InMemorySemanticRegistry registry(
            RegisteredCapability... capabilities
    ) {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(capabilities)
        ));
        return registry;
    }

    private static MerchantConfiguration configuration(
            Set<String> capabilityIdentifiers
    ) {
        return new MerchantConfiguration(
                "merchant-001",
                "configuration-001",
                1,
                "semantic-registry-1.0",
                capabilityIdentifiers
        );
    }
}
