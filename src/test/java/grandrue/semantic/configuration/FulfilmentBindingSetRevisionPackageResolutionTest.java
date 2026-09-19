package grandrue.semantic.configuration;

import grandrue.application.MerchantScope;
import grandrue.fulfilment.AlwaysFulfilmentRequirementApplicability;
import grandrue.fulfilment.FulfillerKind;
import grandrue.fulfilment.FulfilmentBindingSelection;
import grandrue.fulfilment.FulfilmentBindingSetRevision;
import grandrue.fulfilment.FulfilmentBindingSetRevisionReference;
import grandrue.fulfilment.FulfilmentContractRegistrySnapshot;
import grandrue.fulfilment.FulfilmentRequirementDefinition;
import grandrue.fulfilment.FulfilmentRequirementIdentity;
import grandrue.fulfilment.FulfilmentRoleDefinition;
import grandrue.fulfilment.FulfilmentRoleIdentity;
import grandrue.fulfilment.ProviderDefinition;
import grandrue.fulfilment.ProviderFulfilmentSupport;
import grandrue.fulfilment.ResolvedFulfilmentBinding;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FulfilmentBindingSetRevisionPackageResolutionTest {

    private static final Instant GENERATED_AT =
            Instant.parse("2026-08-25T16:15:00Z");
    private static final FulfilmentRoleIdentity BOOKING_STORAGE =
            new FulfilmentRoleIdentity("booking", "reservation-storage");
    private static final FulfilmentRoleIdentity SCHEDULING_STORAGE =
            new FulfilmentRoleIdentity("scheduling", "schedule-storage");
    private static final FulfilmentBindingSetRevisionReference F8 =
            new FulfilmentBindingSetRevisionReference("merchant-routing", 8);

    @Test
    void exact_pinned_binding_set_materialises_a_non_empty_fulfilment_plan() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision bindingSet = bindingSet(
                "merchant-a",
                "semantic-registry-1.0",
                F8,
                new FulfilmentBindingSelection(
                        BOOKING_STORAGE,
                        Optional.empty(),
                        FulfillerKind.INTERNAL,
                        "mainstreet-booking-storage",
                        Optional.empty()
                )
        );

        ResolvedConfigurationPackage resolved = resolver().resolve(
                configuration,
                bindingSet,
                "mainstreet-compiler-1",
                GENERATED_AT
        );

        assertEquals(
                F8,
                resolved.fulfilmentPlan().sourceBindingSetRevision().orElseThrow()
        );
        assertEquals(1, resolved.fulfilmentPlan().bindings().size());
        ResolvedFulfilmentBinding binding =
                resolved.fulfilmentPlan().bindings().getFirst();
        assertEquals(new MerchantScope("merchant-a"), binding.merchantScope());
        assertEquals(BOOKING_STORAGE, binding.roleIdentity());
        assertEquals(
                Set.of("persist-reservation", "release-reservation"),
                binding.requiredObligations()
        );
        assertEquals(FulfillerKind.INTERNAL, binding.fulfillerKind());
        assertEquals("mainstreet-booking-storage", binding.fulfillerIdentity());
        assertEquals(
                "fulfilment-binding-set:merchant-routing@8",
                binding.bindingProvenance()
        );
    }

    @Test
    void exact_reference_must_match_the_supplied_binding_set_revision() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision wrongRevision = bindingSet(
                "merchant-a",
                "semantic-registry-1.0",
                new FulfilmentBindingSetRevisionReference("merchant-routing", 9),
                internalBookingStorage()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver().resolve(
                        configuration,
                        wrongRevision,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void binding_set_cannot_cross_merchant_scope() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision otherMerchant = bindingSet(
                "merchant-b",
                "semantic-registry-1.0",
                F8,
                internalBookingStorage()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver().resolve(
                        configuration,
                        otherMerchant,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void binding_set_must_share_the_configuration_semantic_release() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision wrongRelease = bindingSet(
                "merchant-a",
                "semantic-registry-2.0",
                F8,
                internalBookingStorage()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver().resolve(
                        configuration,
                        wrongRelease,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void binding_cannot_activate_a_role_owned_by_an_inactive_capability() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision bindingSet = bindingSet(
                "merchant-a",
                "semantic-registry-1.0",
                F8,
                new FulfilmentBindingSelection(
                        SCHEDULING_STORAGE,
                        Optional.empty(),
                        FulfillerKind.INTERNAL,
                        "mainstreet-schedule-storage",
                        Optional.empty()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver().resolve(
                        configuration,
                        bindingSet,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void external_binding_must_satisfy_the_registered_role_contract() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision bindingSet = bindingSet(
                "merchant-a",
                "semantic-registry-1.0",
                F8,
                new FulfilmentBindingSelection(
                        BOOKING_STORAGE,
                        Optional.empty(),
                        FulfillerKind.EXTERNAL_PROVIDER,
                        "provider-x",
                        Optional.of("provider-connection-17")
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolverWithPartialProvider().resolve(
                        configuration,
                        bindingSet,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void external_binding_with_full_static_contract_support_is_materialised() {
        MerchantConfiguration configuration = configuration("merchant-a", Set.of("booking"))
                .withFulfilmentBindingSetRevisionReference(F8);
        FulfilmentBindingSetRevision bindingSet = bindingSet(
                "merchant-a",
                "semantic-registry-1.0",
                F8,
                new FulfilmentBindingSelection(
                        BOOKING_STORAGE,
                        Optional.empty(),
                        FulfillerKind.EXTERNAL_PROVIDER,
                        "provider-full",
                        Optional.of("provider-connection-18")
                )
        );

        ResolvedConfigurationPackage resolved = resolver().resolve(
                configuration,
                bindingSet,
                "mainstreet-compiler-1",
                GENERATED_AT
        );

        ResolvedFulfilmentBinding binding =
                resolved.fulfilmentPlan().bindings().getFirst();
        assertEquals(FulfillerKind.EXTERNAL_PROVIDER, binding.fulfillerKind());
        assertEquals("provider-full", binding.fulfillerIdentity());
        assertEquals(
                Optional.of("provider-connection-18"),
                binding.providerConnectionIdentity()
        );
    }

    private static FulfilmentBindingSelection internalBookingStorage() {
        return new FulfilmentBindingSelection(
                BOOKING_STORAGE,
                Optional.empty(),
                FulfillerKind.INTERNAL,
                "mainstreet-booking-storage",
                Optional.empty()
        );
    }

    private static FulfilmentBindingSetRevision bindingSet(
            String merchantIdentifier,
            String semanticRelease,
            FulfilmentBindingSetRevisionReference reference,
            FulfilmentBindingSelection selection
    ) {
        return new FulfilmentBindingSetRevision(
                reference.bindingSetIdentifier(),
                reference.revision(),
                new MerchantScope(merchantIdentifier),
                semanticRelease,
                Set.of(selection)
        );
    }

    private static MerchantConfiguration configuration(
            String merchantIdentifier,
            Set<String> capabilities
    ) {
        return new MerchantConfiguration(
                merchantIdentifier,
                "configuration-1",
                1,
                "semantic-registry-1.0",
                capabilities
        );
    }

    private static ConfigurationPackageResolver resolver() {
        return new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                fulfilmentRegistry(Set.of(
                        new ProviderDefinition(
                                "provider-full",
                                "provider-full-contract-v1",
                                Set.of(new ProviderFulfilmentSupport(
                                        BOOKING_STORAGE,
                                        Set.of("persist-reservation", "release-reservation")
                                ))
                        )
                ))
        );
    }

    private static ConfigurationPackageResolver resolverWithPartialProvider() {
        return new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                fulfilmentRegistry(Set.of(
                        new ProviderDefinition(
                                "provider-x",
                                "provider-x-contract-v1",
                                Set.of(new ProviderFulfilmentSupport(
                                        BOOKING_STORAGE,
                                        Set.of("persist-reservation")
                                ))
                        )
                ))
        );
    }

    private static FulfilmentContractRegistrySnapshot fulfilmentRegistry(
            Set<ProviderDefinition> providers
    ) {
        return new FulfilmentContractRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(
                        new FulfilmentRoleDefinition(
                                BOOKING_STORAGE,
                                Set.of("persist-reservation", "release-reservation"),
                                "booking-storage-authority-v1",
                                "booking-storage-evidence-v1",
                                "booking-storage-failure-v1"
                        ),
                        new FulfilmentRoleDefinition(
                                SCHEDULING_STORAGE,
                                Set.of("store-schedule"),
                                "schedule-storage-authority-v1",
                                "schedule-storage-evidence-v1",
                                "schedule-storage-failure-v1"
                        )
                ),
                providers,
                Set.of(
                        new FulfilmentRequirementDefinition(
                                new FulfilmentRequirementIdentity(
                                        "booking",
                                        "reservation-storage"
                                ),
                                BOOKING_STORAGE,
                                Optional.empty(),
                                Set.of("persist-reservation", "release-reservation"),
                                new AlwaysFulfilmentRequirementApplicability()
                        ),
                        new FulfilmentRequirementDefinition(
                                new FulfilmentRequirementIdentity(
                                        "scheduling",
                                        "schedule-storage"
                                ),
                                SCHEDULING_STORAGE,
                                Optional.empty(),
                                Set.of("store-schedule"),
                                new AlwaysFulfilmentRequirementApplicability()
                        )
                )
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
