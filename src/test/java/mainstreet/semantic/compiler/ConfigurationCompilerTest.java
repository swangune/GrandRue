package mainstreet.semantic.compiler;

import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableConditionalRequirementDefinition;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import mainstreet.semantic.executable.ExecutableRequirementDefinition;
import mainstreet.semantic.executable.ExecutableStateTransitionEffect;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedConditionalRequirementDefinition;
import mainstreet.semantic.registry.OwnedObjectCreationEffect;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedRequirementDefinition;
import mainstreet.semantic.registry.OwnedStateTransitionEffect;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationCompilerTest {

    @Test
    void resolves_platform_owned_creation_semantics_into_an_executable_model() {
        ConfigurationCompiler compiler = new ConfigurationCompiler(registry());

        ExecutableMerchantModel model = compiler.compile(configuration(
                Set.of("booking")
        ));

        assertEquals("merchant-001", model.merchantIdentifier());
        assertEquals("configuration-001", model.modelIdentifier());
        assertEquals(1, model.version());
        assertEquals("semantic-registry-1.0", model.semanticRegistryVersion());
        assertEquals(Set.of("booking"), model.capabilityIdentifiers());
        assertEquals(
                "requested",
                model.operationalObject("booking", "booking")
                        .orElseThrow()
                        .initialStateIdentifier()
                        .orElseThrow()
        );

        ExecutableOperationDefinition create =
                model.operation("booking.create").orElseThrow();
        assertEquals(1, create.effects().size());
        ExecutableObjectCreationEffect creation = assertInstanceOf(
                ExecutableObjectCreationEffect.class,
                create.effects().getFirst()
        );
        assertEquals(
                new ExecutableOperationalObjectTypeIdentity(
                        "booking",
                        "booking"
                ),
                creation.targetObjectType()
        );
        assertEquals(
                "requested",
                creation.initialStateIdentifier().orElseThrow()
        );
        assertEquals(Set.of("BookingCreated"), create.eventIdentifiers());
        assertEquals("booking.create", create.requiredPrivilegeIdentifier());
    }

    @Test
    void merchant_configuration_has_no_surface_for_defining_semantics() {
        Set<String> componentNames = Set.of(
                "merchantIdentifier",
                "configurationIdentifier",
                "version",
                "semanticRegistryVersion",
                "capabilityIdentifiers",
                "policySelections",
                "baseConfigurationIdentifier",
                "fulfilmentBindingSetRevisionReference"
        );

        assertEquals(
                componentNames,
                java.util.Arrays.stream(
                                MerchantConfiguration.class.getRecordComponents()
                        )
                        .map(java.lang.reflect.RecordComponent::getName)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    @Test
    void resolves_every_bounded_effect_owned_by_a_registered_operation() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "checkout",
                        List.of(
                                object("order", "pending", "confirmed"),
                                object("inventory", "available", "allocated"),
                                object("payment", "pending", "authorised")
                        ),
                        List.of(new OwnedOperationDefinition(
                                "checkout.confirm",
                                List.of(
                                        new OwnedStateTransitionEffect(
                                                "order",
                                                "pending",
                                                "confirmed"
                                        ),
                                        new OwnedStateTransitionEffect(
                                                "inventory",
                                                "available",
                                                "allocated"
                                        ),
                                        new OwnedStateTransitionEffect(
                                                "payment",
                                                "pending",
                                                "authorised"
                                        )
                                ),
                                Set.of("CheckoutConfirmed"),
                                "checkout.confirm"
                        ))
                ))
        ));

        ExecutableOperationDefinition operation =
                new ConfigurationCompiler(registry)
                        .compile(configuration(Set.of("checkout")))
                        .operation("checkout.confirm")
                        .orElseThrow();

        assertEquals(3, operation.effects().size());
        assertTrue(operation.effects().stream().allMatch(
                ExecutableStateTransitionEffect.class::isInstance
        ));
        assertEquals(Set.of("CheckoutConfirmed"), operation.eventIdentifiers());
    }

    @Test
    void preserves_unconditional_requirements_owned_by_the_operation() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(object("booking", "requested", "confirmed")),
                        List.of(new OwnedOperationDefinition(
                                "booking.create",
                                List.of(new OwnedObjectCreationEffect(
                                        "booking",
                                        "requested"
                                )),
                                List.of(new OwnedRequirementDefinition(
                                        "customer.contact"
                                )),
                                Set.of("BookingCreated"),
                                "booking.create"
                        ))
                ))
        ));

        ExecutableOperationDefinition operation =
                new ConfigurationCompiler(registry)
                        .compile(configuration(Set.of("booking")))
                        .operation("booking.create")
                        .orElseThrow();

        assertEquals(
                List.of(new ExecutableRequirementDefinition(
                        "customer.contact"
                )),
                operation.unconditionalRequirements()
        );
    }

    @Test
    void preserves_platform_owned_conditional_requirement_semantics() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "fulfilment",
                        List.of(object("order", "draft", "confirmed")),
                        List.of(new OwnedOperationDefinition(
                                "order.confirm",
                                List.of(new OwnedStateTransitionEffect(
                                        "order",
                                        "draft",
                                        "confirmed"
                                )),
                                List.of(new OwnedRequirementDefinition(
                                        "customer.contact"
                                )),
                                List.of(new OwnedConditionalRequirementDefinition(
                                        "delivery.address",
                                        "fulfilment.delivery"
                                )),
                                Set.of("OrderConfirmed"),
                                "order.confirm"
                        ))
                ))
        ));

        ExecutableOperationDefinition operation =
                new ConfigurationCompiler(registry)
                        .compile(configuration(Set.of("fulfilment")))
                        .operation("order.confirm")
                        .orElseThrow();

        assertEquals(
                List.of(new ExecutableConditionalRequirementDefinition(
                        "delivery.address",
                        "fulfilment.delivery"
                )),
                operation.conditionalRequirements()
        );
    }

    @Test
    void rejects_duplicate_requirements_on_one_owned_operation() {
        OwnedRequirementDefinition requirement =
                new OwnedRequirementDefinition("customer.contact");

        assertThrows(
                IllegalArgumentException.class,
                () -> new OwnedOperationDefinition(
                        "booking.create",
                        List.of(new OwnedObjectCreationEffect(
                                "booking",
                                "requested"
                        )),
                        List.of(requirement, requirement),
                        Set.of("BookingCreated"),
                        "booking.create"
                )
        );
    }

    @Test
    void rejects_one_requirement_identity_with_two_applicability_contracts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OwnedOperationDefinition(
                        "booking.create",
                        List.of(new OwnedObjectCreationEffect(
                                "booking",
                                "requested"
                        )),
                        List.of(new OwnedRequirementDefinition(
                                "customer.contact"
                        )),
                        List.of(new OwnedConditionalRequirementDefinition(
                                "customer.contact",
                                "customer.contact.required"
                        )),
                        Set.of("BookingCreated"),
                        "booking.create"
                )
        );
    }

    @Test
    void rejects_a_capability_absent_from_the_referenced_registry() {
        ConfigurationCompiler compiler = new ConfigurationCompiler(registry());

        assertThrows(
                IllegalArgumentException.class,
                () -> compiler.compile(configuration(
                        Set.of("booking", "payment")
                ))
        );
    }

    private static MerchantConfiguration configuration(Set<String> capabilities) {
        return new MerchantConfiguration(
                "merchant-001",
                "configuration-001",
                1,
                "semantic-registry-1.0",
                capabilities
        );
    }

    private static OwnedOperationalObjectDefinition object(
            String identifier,
            String initialState,
            String resultingState
    ) {
        return new OwnedOperationalObjectDefinition(
                identifier,
                Set.of(initialState, resultingState),
                initialState
        );
    }

    private static InMemorySemanticRegistry registry() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(new OwnedOperationalObjectDefinition(
                                "booking",
                                Set.of("requested", "confirmed"),
                                "requested"
                        )),
                        List.of(OwnedOperationDefinition.creation(
                                "booking.create",
                                "booking",
                                "requested",
                                "BookingCreated",
                                "booking.create"
                        ))
                ))
        ));
        return registry;
    }
}
