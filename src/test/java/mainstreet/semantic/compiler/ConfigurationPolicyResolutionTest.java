package mainstreet.semantic.compiler;

import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.PolicySelection;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.PolicyResolutionStatus;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedPolicyDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationPolicyResolutionTest {

    @Test
    void active_policy_without_a_selection_uses_its_registered_default() {
        ExecutableMerchantModel model = compile(
                Set.of("booking"),
                Set.of()
        );

        var policy = model.policy(
                "booking",
                "confirmation.mode"
        ).orElseThrow();

        assertEquals(PolicyResolutionStatus.DEFAULTED, policy.status());
        assertEquals("automatic", policy.value().orElseThrow());
    }

    @Test
    void active_policy_preserves_an_explicit_allowed_selection() {
        ExecutableMerchantModel model = compile(
                Set.of("booking"),
                Set.of(new PolicySelection(
                        "booking",
                        "confirmation.mode",
                        "merchant-confirmation"
                ))
        );

        var policy = model.policy(
                "booking",
                "confirmation.mode"
        ).orElseThrow();

        assertEquals(
                PolicyResolutionStatus.EXPLICITLY_SELECTED,
                policy.status()
        );
        assertEquals(
                "merchant-confirmation",
                policy.value().orElseThrow()
        );
    }

    @Test
    void policy_owned_by_an_inactive_capability_is_not_applicable() {
        ExecutableMerchantModel model = compile(
                Set.of("booking"),
                Set.of()
        );

        var policy = model.policy(
                "payment",
                "payment.timing"
        ).orElseThrow();

        assertEquals(
                PolicyResolutionStatus.NOT_APPLICABLE,
                policy.status()
        );
        assertTrue(policy.value().isEmpty());
    }

    @Test
    void policy_selection_cannot_activate_its_owning_capability() {
        assertThrows(
                IllegalArgumentException.class,
                () -> compile(
                        Set.of("booking"),
                        Set.of(new PolicySelection(
                                "payment",
                                "payment.timing",
                                "on-confirmation"
                        ))
                )
        );
    }

    @Test
    void rejects_an_explicit_value_outside_the_registered_set() {
        assertThrows(
                IllegalArgumentException.class,
                () -> compile(
                        Set.of("booking"),
                        Set.of(new PolicySelection(
                                "booking",
                                "confirmation.mode",
                                "customer-decides"
                        ))
                )
        );
    }

    @Test
    void rejects_a_policy_not_registered_by_the_selected_owner() {
        assertThrows(
                IllegalArgumentException.class,
                () -> compile(
                        Set.of("booking"),
                        Set.of(new PolicySelection(
                                "booking",
                                "booking.unregistered",
                                "value"
                        ))
                )
        );
    }

    @Test
    void one_configuration_cannot_select_two_values_for_one_policy() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantConfiguration(
                        "merchant-001",
                        "configuration-001",
                        1,
                        "semantic-registry-1.0",
                        Set.of("booking"),
                        Set.of(
                                new PolicySelection(
                                        "booking",
                                        "confirmation.mode",
                                        "automatic"
                                ),
                                new PolicySelection(
                                        "booking",
                                        "confirmation.mode",
                                        "merchant-confirmation"
                                )
                        )
                )
        );
    }

    private static ExecutableMerchantModel compile(
            Set<String> capabilityIdentifiers,
            Set<PolicySelection> policySelections
    ) {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(
                        capability(
                                "booking",
                                new OwnedPolicyDefinition(
                                        "confirmation.mode",
                                        "automatic",
                                        Set.of(
                                                "automatic",
                                                "merchant-confirmation"
                                        )
                                )
                        ),
                        capability(
                                "payment",
                                new OwnedPolicyDefinition(
                                        "payment.timing",
                                        "on-confirmation",
                                        Set.of(
                                                "on-confirmation",
                                                "before-confirmation"
                                        )
                                )
                        )
                )
        ));
        MerchantConfiguration configuration = new MerchantConfiguration(
                "merchant-001",
                "configuration-001",
                1,
                "semantic-registry-1.0",
                capabilityIdentifiers,
                policySelections
        );
        return new ConfigurationCompiler(registry).compile(configuration);
    }

    private static RegisteredCapability capability(
            String identifier,
            OwnedPolicyDefinition policy
    ) {
        return new RegisteredCapability(
                identifier,
                List.of(),
                List.of(),
                List.of(policy)
        );
    }
}
