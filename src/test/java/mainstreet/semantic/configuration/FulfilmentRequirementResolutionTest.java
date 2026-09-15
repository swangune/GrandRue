package mainstreet.semantic.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.fulfilment.AlwaysFulfilmentRequirementApplicability;
import mainstreet.fulfilment.EnumDecisionValueFulfilmentRequirementApplicability;
import mainstreet.fulfilment.FulfillerKind;
import mainstreet.fulfilment.FulfilmentBindingSelection;
import mainstreet.fulfilment.FulfilmentBindingSetRevision;
import mainstreet.fulfilment.FulfilmentBindingSetRevisionReference;
import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.fulfilment.FulfilmentRequirementDefinition;
import mainstreet.fulfilment.FulfilmentRequirementIdentity;
import mainstreet.fulfilment.FulfilmentRoleDefinition;
import mainstreet.fulfilment.FulfilmentRoleIdentity;
import mainstreet.fulfilment.FulfilmentRoleOwnerKind;
import mainstreet.fulfilment.ProviderDefinition;
import mainstreet.fulfilment.ProviderFulfilmentSupport;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedPolicyDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FulfilmentRequirementResolutionTest {

    private static final Instant GENERATED_AT =
            Instant.parse("2026-08-25T17:30:00Z");
    private static final FulfilmentRoleIdentity MESSAGE_DELIVERY =
            new FulfilmentRoleIdentity("notification", "message-delivery");
    private static final FulfilmentBindingSetRevisionReference F1 =
            new FulfilmentBindingSetRevisionReference("notification-routing", 1);

    @Test
    void platform_service_role_is_applicable_only_when_an_active_capability_requirement_produces_obligations() {
        MerchantConfiguration configuration = configuration(Set.of())
                .withFulfilmentBindingSetRevisionReference(F1);

        FulfilmentBindingSetRevision bindingSet = bindingSet(Set.of(
                providerBinding("provider-confirmation")
        ));

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver(requirements(), providers()).resolve(
                        configuration,
                        bindingSet,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void provider_is_valid_against_the_exact_configuration_specific_obligation_subset() {
        MerchantConfiguration configuration = configuration(Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F1);

        ResolvedConfigurationPackage resolved = resolver(
                requirements(),
                providers()
        ).resolve(
                configuration,
                bindingSet(Set.of(providerBinding("provider-confirmation"))),
                "mainstreet-compiler-1",
                GENERATED_AT
        );

        var binding = resolved.fulfilmentPlan().bindings().getFirst();
        assertEquals(MESSAGE_DELIVERY, binding.roleIdentity());
        assertEquals(Set.of("send-confirmation"), binding.requiredObligations());
    }

    @Test
    void matching_configuration_decision_adds_its_obligation_and_rejects_a_provider_that_cannot_cover_it() {
        MerchantConfiguration configuration = configuration(
                Set.of("booking"),
                Set.of(new PolicySelection(
                        "booking",
                        "reminder.mode",
                        "day-before"
                ))
        ).withFulfilmentBindingSetRevisionReference(F1);

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver(requirements(), providers()).resolve(
                        configuration,
                        bindingSet(Set.of(providerBinding("provider-confirmation"))),
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void full_provider_receives_the_union_of_all_applicable_obligations() {
        MerchantConfiguration configuration = configuration(
                Set.of("booking"),
                Set.of(new PolicySelection(
                        "booking",
                        "reminder.mode",
                        "day-before"
                ))
        ).withFulfilmentBindingSetRevisionReference(F1);

        ResolvedConfigurationPackage resolved = resolver(
                requirements(),
                providers()
        ).resolve(
                configuration,
                bindingSet(Set.of(providerBinding("provider-full"))),
                "mainstreet-compiler-1",
                GENERATED_AT
        );

        assertEquals(
                Set.of("send-confirmation", "send-reminder"),
                resolved.fulfilmentPlan().bindings().getFirst().requiredObligations()
        );
    }

    @Test
    void applicable_role_context_without_a_binding_fails_closed() {
        MerchantConfiguration configuration = configuration(Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F1);

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver(requirements(), providers()).resolve(
                        configuration,
                        bindingSet(Set.of()),
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void binding_without_any_applicable_requirement_is_rejected_as_stale_routing() {
        MerchantConfiguration configuration = configuration(Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F1);

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver(Set.of(), providers()).resolve(
                        configuration,
                        bindingSet(Set.of(providerBinding("provider-full"))),
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void registry_rejects_a_requirement_that_invents_an_obligation_absent_from_the_role() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fulfilmentRegistry(
                        Set.of(new FulfilmentRequirementDefinition(
                                new FulfilmentRequirementIdentity(
                                        "booking",
                                        "invented-obligation"
                                ),
                                MESSAGE_DELIVERY,
                                Optional.empty(),
                                Set.of("provider-specific-magic"),
                                new AlwaysFulfilmentRequirementApplicability()
                        )),
                        providers()
                )
        );
    }

    @Test
    void capability_requirement_cannot_target_another_capability_owned_private_role() {
        FulfilmentRoleIdentity privateSchedulingRole =
                new FulfilmentRoleIdentity("scheduling", "private-storage");
        FulfilmentRoleDefinition privateRole = new FulfilmentRoleDefinition(
                privateSchedulingRole,
                FulfilmentRoleOwnerKind.CAPABILITY,
                Set.of("store-schedule"),
                "private-authority-v1",
                "private-evidence-v1",
                "private-failure-v1"
        );
        FulfilmentRequirementDefinition crossCapability =
                new FulfilmentRequirementDefinition(
                        new FulfilmentRequirementIdentity(
                                "booking",
                                "use-scheduling-private-storage"
                        ),
                        privateSchedulingRole,
                        Optional.empty(),
                        Set.of("store-schedule"),
                        new AlwaysFulfilmentRequirementApplicability()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(privateRole),
                        Set.of(),
                        Set.of(crossCapability)
                )
        );
    }

    private static Set<FulfilmentRequirementDefinition> requirements() {
        return Set.of(
                new FulfilmentRequirementDefinition(
                        new FulfilmentRequirementIdentity(
                                "booking",
                                "confirmation-delivery"
                        ),
                        MESSAGE_DELIVERY,
                        Optional.empty(),
                        Set.of("send-confirmation"),
                        new AlwaysFulfilmentRequirementApplicability()
                ),
                new FulfilmentRequirementDefinition(
                        new FulfilmentRequirementIdentity(
                                "booking",
                                "reminder-delivery"
                        ),
                        MESSAGE_DELIVERY,
                        Optional.empty(),
                        Set.of("send-reminder"),
                        new EnumDecisionValueFulfilmentRequirementApplicability(
                                new CapabilityConfigurationDecisionIdentity(
                                        "booking",
                                        "reminder.mode"
                                ),
                                Set.of("day-before")
                        )
                )
        );
    }

    private static Set<ProviderDefinition> providers() {
        return Set.of(
                new ProviderDefinition(
                        "provider-confirmation",
                        "provider-confirmation-contract-v1",
                        Set.of(new ProviderFulfilmentSupport(
                                MESSAGE_DELIVERY,
                                Set.of("send-confirmation")
                        ))
                ),
                new ProviderDefinition(
                        "provider-full",
                        "provider-full-contract-v1",
                        Set.of(new ProviderFulfilmentSupport(
                                MESSAGE_DELIVERY,
                                Set.of("send-confirmation", "send-reminder")
                        ))
                )
        );
    }

    private static ConfigurationPackageResolver resolver(
            Set<FulfilmentRequirementDefinition> requirements,
            Set<ProviderDefinition> providers
    ) {
        return new ConfigurationPackageResolver(
                new ConfigurationCompiler(semanticRegistry()),
                fulfilmentRegistry(requirements, providers)
        );
    }

    private static FulfilmentContractRegistrySnapshot fulfilmentRegistry(
            Set<FulfilmentRequirementDefinition> requirements,
            Set<ProviderDefinition> providers
    ) {
        return new FulfilmentContractRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new FulfilmentRoleDefinition(
                        MESSAGE_DELIVERY,
                        FulfilmentRoleOwnerKind.PLATFORM_SERVICE,
                        Set.of("send-confirmation", "send-reminder"),
                        "notification-authority-v1",
                        "notification-evidence-v1",
                        "notification-failure-v1"
                )),
                providers,
                requirements
        );
    }

    private static FulfilmentBindingSetRevision bindingSet(
            Set<FulfilmentBindingSelection> selections
    ) {
        return new FulfilmentBindingSetRevision(
                F1.bindingSetIdentifier(),
                F1.revision(),
                new MerchantScope("merchant-a"),
                "semantic-registry-1.0",
                selections
        );
    }

    private static FulfilmentBindingSelection providerBinding(String provider) {
        return new FulfilmentBindingSelection(
                MESSAGE_DELIVERY,
                Optional.empty(),
                FulfillerKind.EXTERNAL_PROVIDER,
                provider,
                Optional.of("connection-" + provider)
        );
    }

    private static MerchantConfiguration configuration(Set<String> capabilities) {
        return configuration(capabilities, Set.of());
    }

    private static MerchantConfiguration configuration(
            Set<String> capabilities,
            Set<PolicySelection> policySelections
    ) {
        return new MerchantConfiguration(
                "merchant-a",
                "configuration-1",
                1,
                "semantic-registry-1.0",
                capabilities,
                policySelections
        );
    }

    private static InMemorySemanticRegistry semanticRegistry() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(),
                        List.of(),
                        List.of(new OwnedPolicyDefinition(
                                "reminder.mode",
                                "disabled",
                                Set.of("disabled", "day-before")
                        ))
                ))
        ));
        return registry;
    }
}
