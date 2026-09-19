package grandrue.semantic.configuration;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import grandrue.surface.ActiveCapabilitySurfaceEligibilityEvidence;
import grandrue.surface.SurfaceAudience;
import grandrue.surface.SurfaceContributionDefinition;
import grandrue.surface.SurfaceContributionIdentity;
import grandrue.surface.SurfaceContributionKind;
import grandrue.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurfaceContributionPackageResolutionTest {

    @Test
    void resolves_only_contributions_owned_by_active_capabilities() {
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                contributions("semantic-registry-1.0")
        );

        ResolvedConfigurationPackage resolved = resolver.resolve(
                configuration(Set.of("booking")),
                "grandrue-compiler-1",
                Instant.parse("2026-08-25T14:30:00Z")
        );

        assertEquals(
                List.of(new SurfaceContributionIdentity(
                        "booking",
                        "manage-bookings"
                )),
                resolved.staticSurfaceContributionCatalogue()
                        .contributions()
                        .stream()
                        .map(contribution -> contribution.identity())
                        .toList()
        );

        var contribution = resolved.staticSurfaceContributionCatalogue()
                .contributions()
                .getFirst();
        assertEquals(SurfaceAudience.MERCHANT, contribution.audience());
        assertEquals(SurfaceContributionKind.WORKSPACE, contribution.kind());
        assertEquals(
                Optional.of("merchant/bookings"),
                contribution.compositionTargetReference()
        );
        assertEquals(Set.of("booking-summary"), contribution.projectionRequirements());
        assertEquals(Set.of("booking.create"), contribution.supportedOperationReferences());
        assertEquals(
                Set.of(new ActiveCapabilitySurfaceEligibilityEvidence("booking")),
                contribution.staticEligibilityEvidence()
        );
    }

    @Test
    void rejects_surface_contract_from_a_different_registry_release() {
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                contributions("semantic-registry-2.0")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        configuration(Set.of("booking")),
                        "grandrue-compiler-1",
                        Instant.parse("2026-08-25T14:30:00Z")
                )
        );
    }

    @Test
    void rejects_supported_operation_reference_not_present_in_resolved_model() {
        SurfaceContributionDefinition invalid = new SurfaceContributionDefinition(
                new SurfaceContributionIdentity("booking", "broken-action"),
                SurfaceAudience.MERCHANT,
                SurfaceContributionKind.ACTION,
                Optional.empty(),
                Set.of(),
                Set.of("booking.missing")
        );
        SurfaceContributionRegistrySnapshot snapshot =
                new SurfaceContributionRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(invalid)
                );
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry()),
                snapshot
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        configuration(Set.of("booking")),
                        "grandrue-compiler-1",
                        Instant.parse("2026-08-25T14:30:00Z")
                )
        );
    }

    private static MerchantConfiguration configuration(Set<String> capabilities) {
        return new MerchantConfiguration(
                "merchant-a",
                "configuration-1",
                1,
                "semantic-registry-1.0",
                capabilities
        );
    }

    private static SurfaceContributionRegistrySnapshot contributions(
            String releaseIdentifier
    ) {
        return new SurfaceContributionRegistrySnapshot(
                releaseIdentifier,
                Set.of(
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "manage-bookings"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("merchant/bookings"),
                                Set.of("booking-summary"),
                                Set.of("booking.create")
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "publication",
                                        "manage-publications"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("merchant/publications"),
                                Set.of("publication-summary"),
                                Set.of("publication.create")
                        )
                )
        );
    }

    private static InMemorySemanticRegistry registry() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(
                        capability("booking", "booking.create", "BookingCreated"),
                        capability(
                                "publication",
                                "publication.create",
                                "PublicationCreated"
                        )
                )
        ));
        return registry;
    }

    private static RegisteredCapability capability(
            String identifier,
            String operationIdentifier,
            String eventIdentifier
    ) {
        String objectIdentifier = identifier;
        return new RegisteredCapability(
                identifier,
                List.of(new OwnedOperationalObjectDefinition(
                        objectIdentifier,
                        Set.of("draft"),
                        "draft"
                )),
                List.of(OwnedOperationDefinition.creation(
                        operationIdentifier,
                        objectIdentifier,
                        "draft",
                        eventIdentifier,
                        operationIdentifier
                ))
        );
    }
}
