package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.ActorAuthorisationAuthority;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContextualSurfaceResolverTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final ExecutionPrincipal PRINCIPAL = new ExecutionPrincipal("staff-a");

    @Test
    void filters_active_merchant_contributions_by_current_actor_authority() {
        ExecutableMerchantModel model = model(Set.of("booking"));
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        StaticSurfaceContributionCatalogue staticCatalogue = registry.resolveFor(model);
        ActorAuthorisationAuthority authority = (scope, principal, privilege) ->
                privilege.identifier().equals("booking.view");

        ContextualSurfaceComposition resolved = new ContextualSurfaceResolver(
                authority,
                (scope, capabilityIdentifier) -> false
        ).resolve(
                new MerchantSurfaceResolutionContext(MERCHANT, PRINCIPAL),
                model,
                staticCatalogue,
                registry
        );

        assertEquals(1, resolved.groups().size());
        assertEquals(
                List.of(new SurfaceContributionIdentity("booking", "manage-bookings")),
                resolved.groups().getFirst().contributions().stream()
                        .map(ContextualSurfaceContribution::identity)
                        .toList()
        );
        assertEquals(
                Set.of(new ActorAuthoritySurfaceEligibilityEvidence("booking.view")),
                resolved.groups().getFirst().contributions().getFirst()
                        .contextualEligibilityEvidence()
        );
    }

    @Test
    void residual_obligation_keeps_only_explicit_residual_management_surface_after_deactivation() {
        ExecutableMerchantModel model = model(Set.of());
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        StaticSurfaceContributionCatalogue staticCatalogue = registry.resolveFor(model);
        ActorAuthorisationAuthority authority = (scope, principal, privilege) -> true;

        ContextualSurfaceComposition resolved = new ContextualSurfaceResolver(
                authority,
                (scope, capabilityIdentifier) -> capabilityIdentifier.equals("booking")
        ).resolve(
                new MerchantSurfaceResolutionContext(MERCHANT, PRINCIPAL),
                model,
                staticCatalogue,
                registry
        );

        assertEquals(1, resolved.groups().size());
        ContextualSurfaceContribution contribution =
                resolved.groups().getFirst().contributions().getFirst();
        assertEquals(
                new SurfaceContributionIdentity("booking", "manage-bookings"),
                contribution.identity()
        );
        assertEquals(
                Set.of(
                        new ResidualObligationSurfaceEligibilityEvidence("booking"),
                        new ActorAuthoritySurfaceEligibilityEvidence("booking.view")
                ),
                contribution.contextualEligibilityEvidence()
        );
    }

    @Test
    void deactivated_capability_without_residual_obligation_has_no_merchant_surface() {
        ExecutableMerchantModel model = model(Set.of());
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");

        ContextualSurfaceComposition resolved = new ContextualSurfaceResolver(
                (scope, principal, privilege) -> true,
                (scope, capabilityIdentifier) -> false
        ).resolve(
                new MerchantSurfaceResolutionContext(MERCHANT, PRINCIPAL),
                model,
                registry.resolveFor(model),
                registry
        );

        assertEquals(List.of(), resolved.groups());
    }

    @Test
    void rejects_registry_release_or_merchant_scope_mismatch() {
        ExecutableMerchantModel model = model(Set.of("booking"));
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ContextualSurfaceResolver resolver = new ContextualSurfaceResolver(
                (scope, principal, privilege) -> true,
                (scope, capabilityIdentifier) -> false
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        new MerchantSurfaceResolutionContext(
                                new MerchantScope("merchant-b"),
                                PRINCIPAL
                        ),
                        model,
                        registry.resolveFor(model),
                        registry
                )
        );

        SurfaceContributionRegistrySnapshot wrongRelease =
                registry("semantic-registry-2.0");
        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        new MerchantSurfaceResolutionContext(MERCHANT, PRINCIPAL),
                        model,
                        registry.resolveFor(model),
                        wrongRelease
                )
        );
    }

    private static SurfaceContributionRegistrySnapshot registry(String release) {
        return new SurfaceContributionRegistrySnapshot(
                release,
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
                                Set.of(),
                                SurfaceEligibilityContract.activeOrResidual(
                                        "booking.view"
                                )
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "create-booking"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.ACTION,
                                Optional.of("merchant/bookings"),
                                Set.of(),
                                Set.of(),
                                SurfaceEligibilityContract.activeOnly(
                                        "booking.create"
                                )
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "public-book"
                                ),
                                SurfaceAudience.PUBLIC,
                                SurfaceContributionKind.PUBLIC_INTERACTION,
                                Optional.of("public/primary-actions"),
                                Set.of(),
                                Set.of()
                        )
                )
        );
    }

    private static ExecutableMerchantModel model(Set<String> activeCapabilities) {
        return new ExecutableMerchantModel(
                "merchant-a",
                "model-1",
                1,
                "semantic-registry-1.0",
                activeCapabilities,
                List.of(),
                List.of(),
                List.of()
        );
    }
}
