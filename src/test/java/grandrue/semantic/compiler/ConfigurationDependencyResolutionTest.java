package grandrue.semantic.compiler;

import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.PolicySelection;
import grandrue.semantic.executable.CapabilityActivationReason;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.PolicyResolutionStatus;
import grandrue.semantic.registry.CapabilityRelationshipType;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedPolicyDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.RegisteredCapabilityRelationship;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationDependencyResolutionTest {

    @Test
    void transitively_activates_registered_required_capabilities() {
        ExecutableMerchantModel model = compile(
                Set.of("booking"),
                Set.of(
                        relationship(
                                "booking",
                                CapabilityRelationshipType.REQUIRES,
                                "scheduling"
                        ),
                        relationship(
                                "scheduling",
                                CapabilityRelationshipType.REQUIRES,
                                "time-model"
                        )
                ),
                capability("booking"),
                capability("scheduling"),
                capability("time-model")
        );

        assertEquals(
                Set.of("booking", "scheduling", "time-model"),
                model.capabilityIdentifiers()
        );
        assertEquals(
                Set.of(CapabilityActivationReason.merchantSelected()),
                model.activation("booking").orElseThrow().reasons()
        );
        assertEquals(
                Set.of(CapabilityActivationReason.requiredBy("booking")),
                model.activation("scheduling").orElseThrow().reasons()
        );
        assertEquals(
                Set.of(CapabilityActivationReason.requiredBy("scheduling")),
                model.activation("time-model").orElseThrow().reasons()
        );
    }

    @Test
    void preserves_every_reason_for_a_shared_required_capability() {
        ExecutableMerchantModel model = compile(
                Set.of("booking", "consultation"),
                Set.of(
                        relationship(
                                "booking",
                                CapabilityRelationshipType.REQUIRES,
                                "scheduling"
                        ),
                        relationship(
                                "consultation",
                                CapabilityRelationshipType.REQUIRES,
                                "scheduling"
                        )
                ),
                capability("booking"),
                capability("consultation"),
                capability("scheduling")
        );

        assertEquals(
                Set.of(
                        CapabilityActivationReason.requiredBy("booking"),
                        CapabilityActivationReason.requiredBy("consultation")
                ),
                model.activation("scheduling").orElseThrow().reasons()
        );
    }

    @Test
    void preserves_selection_and_requirement_as_distinct_reasons() {
        ExecutableMerchantModel model = compile(
                Set.of("booking", "scheduling"),
                Set.of(relationship(
                        "booking",
                        CapabilityRelationshipType.REQUIRES,
                        "scheduling"
                )),
                capability("booking"),
                capability("scheduling")
        );

        assertEquals(
                Set.of(
                        CapabilityActivationReason.merchantSelected(),
                        CapabilityActivationReason.requiredBy("booking")
                ),
                model.activation("scheduling").orElseThrow().reasons()
        );
    }

    @Test
    void policy_of_a_derived_capability_becomes_applicable() {
        RegisteredCapability scheduling = new RegisteredCapability(
                "scheduling",
                List.of(),
                List.of(),
                List.of(new OwnedPolicyDefinition(
                        "scheduling.mode",
                        "appointments",
                        Set.of("appointments", "windows")
                ))
        );
        ExecutableMerchantModel model = compile(
                Set.of("booking"),
                Set.of(relationship(
                        "booking",
                        CapabilityRelationshipType.REQUIRES,
                        "scheduling"
                )),
                capability("booking"),
                scheduling
        );

        var policy = model.policy(
                "scheduling",
                "scheduling.mode"
        ).orElseThrow();

        assertEquals(PolicyResolutionStatus.DEFAULTED, policy.status());
        assertEquals("appointments", policy.value().orElseThrow());
    }

    @Test
    void accepts_an_allowed_policy_selection_for_a_derived_capability() {
        RegisteredCapability scheduling = new RegisteredCapability(
                "scheduling",
                List.of(),
                List.of(),
                List.of(new OwnedPolicyDefinition(
                        "scheduling.mode",
                        "appointments",
                        Set.of("appointments", "windows")
                ))
        );
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(snapshot(
                Set.of(relationship(
                        "booking",
                        CapabilityRelationshipType.REQUIRES,
                        "scheduling"
                )),
                capability("booking"),
                scheduling
        ));

        ExecutableMerchantModel model = new ConfigurationCompiler(registry)
                .compile(new MerchantConfiguration(
                        "merchant-001",
                        "configuration-001",
                        1,
                        "semantic-registry-1.0",
                        Set.of("booking"),
                        Set.of(new PolicySelection(
                                "scheduling",
                                "scheduling.mode",
                                "windows"
                        ))
                ));

        var policy = model.policy(
                "scheduling",
                "scheduling.mode"
        ).orElseThrow();
        assertEquals(
                PolicyResolutionStatus.EXPLICITLY_SELECTED,
                policy.status()
        );
        assertEquals("windows", policy.value().orElseThrow());
    }

    @Test
    void rejects_a_cycle_in_registered_required_relationships() {
        assertThrows(
                IllegalArgumentException.class,
                () -> snapshot(
                        Set.of(
                                relationship(
                                        "booking",
                                        CapabilityRelationshipType.REQUIRES,
                                        "scheduling"
                                ),
                                relationship(
                                        "scheduling",
                                        CapabilityRelationshipType.REQUIRES,
                                        "booking"
                                )
                        ),
                        capability("booking"),
                        capability("scheduling")
                )
        );
    }

    @Test
    void rejects_a_relationship_with_an_unregistered_endpoint() {
        assertThrows(
                IllegalArgumentException.class,
                () -> snapshot(
                        Set.of(relationship(
                                "booking",
                                CapabilityRelationshipType.REQUIRES,
                                "missing"
                        )),
                        capability("booking")
                )
        );
    }

    @Test
    void rejects_a_conflict_introduced_by_dependency_closure() {
        assertThrows(
                IllegalArgumentException.class,
                () -> compile(
                        Set.of("booking", "walk-in-only"),
                        Set.of(
                                relationship(
                                        "booking",
                                        CapabilityRelationshipType.REQUIRES,
                                        "scheduling"
                                ),
                                relationship(
                                        "scheduling",
                                        CapabilityRelationshipType.CONFLICTS_WITH,
                                        "walk-in-only"
                                )
                        ),
                        capability("booking"),
                        capability("scheduling"),
                        capability("walk-in-only")
                )
        );
    }

    @Test
    void non_activating_relationships_do_not_expand_the_model() {
        ExecutableMerchantModel model = compile(
                Set.of("booking"),
                Set.of(
                        relationship(
                                "booking",
                                CapabilityRelationshipType.SUPPORTS,
                                "notification"
                        ),
                        relationship(
                                "booking",
                                CapabilityRelationshipType.DEPENDS_ON,
                                "reporting"
                        )
                ),
                capability("booking"),
                capability("notification"),
                capability("reporting")
        );

        assertEquals(Set.of("booking"), model.capabilityIdentifiers());
        assertTrue(model.activation("notification").isEmpty());
        assertTrue(model.activation("reporting").isEmpty());
    }

    private static ExecutableMerchantModel compile(
            Set<String> selectedCapabilities,
            Set<RegisteredCapabilityRelationship> relationships,
            RegisteredCapability... capabilities
    ) {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(snapshot(relationships, capabilities));
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

    private static SemanticRegistrySnapshot snapshot(
            Set<RegisteredCapabilityRelationship> relationships,
            RegisteredCapability... capabilities
    ) {
        return new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(capabilities),
                relationships
        );
    }

    private static RegisteredCapability capability(String identifier) {
        return new RegisteredCapability(
                identifier,
                List.of(),
                List.of()
        );
    }

    private static RegisteredCapabilityRelationship relationship(
            String source,
            CapabilityRelationshipType type,
            String target
    ) {
        return new RegisteredCapabilityRelationship(source, type, target);
    }
}
