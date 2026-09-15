package mainstreet.semantic.configuration;

import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.fulfilment.FulfilmentRoleDefinition;
import mainstreet.fulfilment.FulfilmentRoleIdentity;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.SurfaceAudience;
import mainstreet.surface.SurfaceContributionDefinition;
import mainstreet.surface.SurfaceContributionIdentity;
import mainstreet.surface.SurfaceContributionKind;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import mainstreet.surface.SurfaceEligibilityContract;
import mainstreet.surface.SurfaceInteractionAvailabilityContract;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurfaceFulfilmentRoleDependencyPackageResolutionTest {

    private static final FulfilmentRoleIdentity EXTERNAL_CALENDAR =
            new FulfilmentRoleIdentity("calendar", "external-calendar");

    @Test
    void rejects_surface_dependency_on_unregistered_fulfilment_role() {
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                contributions(EXTERNAL_CALENDAR),
                new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(),
                        Set.of()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        configuration(),
                        "mainstreet-compiler-1",
                        Instant.parse("2026-08-25T20:00:00Z")
                )
        );
    }

    @Test
    void accepts_registered_role_without_making_it_configuration_applicable() {
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                contributions(EXTERNAL_CALENDAR),
                new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(role(EXTERNAL_CALENDAR)),
                        Set.of()
                )
        );

        ResolvedConfigurationPackage resolved = resolver.resolve(
                configuration(),
                "mainstreet-compiler-1",
                Instant.parse("2026-08-25T20:00:00Z")
        );

        assertEquals(1, resolved.staticSurfaceContributionCatalogue().contributions().size());
        assertEquals(0, resolved.fulfilmentPlan().bindings().size());
    }

    @Test
    void rejects_fulfilment_registry_from_a_different_semantic_release_when_used_for_surface_dependency_validation() {
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                contributions(EXTERNAL_CALENDAR),
                new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-2.0",
                        Set.of(role(EXTERNAL_CALENDAR)),
                        Set.of()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        configuration(),
                        "mainstreet-compiler-1",
                        Instant.parse("2026-08-25T20:00:00Z")
                )
        );
    }

    private static SurfaceContributionRegistrySnapshot contributions(
            FulfilmentRoleIdentity roleIdentity
    ) {
        return new SurfaceContributionRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new SurfaceContributionDefinition(
                        new SurfaceContributionIdentity(
                                "booking",
                                "push-external-calendar"
                        ),
                        SurfaceAudience.MERCHANT,
                        SurfaceContributionKind.ACTION,
                        Optional.of("merchant/calendar"),
                        Set.of(),
                        Set.of("booking.create"),
                        SurfaceEligibilityContract.activeOnly(),
                        SurfaceInteractionAvailabilityContract.requiresFulfilmentRole(
                                roleIdentity
                        )
                ))
        );
    }

    private static FulfilmentRoleDefinition role(FulfilmentRoleIdentity identity) {
        return new FulfilmentRoleDefinition(
                identity,
                Set.of("push-calendar-entry"),
                "calendar-authority-v1",
                "calendar-evidence-v1",
                "calendar-failure-v1"
        );
    }

    private static MerchantConfiguration configuration() {
        return new MerchantConfiguration(
                "merchant-a",
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("booking")
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
                                Set.of("draft"),
                                "draft"
                        )),
                        List.of(OwnedOperationDefinition.creation(
                                "booking.create",
                                "booking",
                                "draft",
                                "BookingCreated",
                                "booking.create"
                        ))
                ))
        ));
        return registry;
    }
}
