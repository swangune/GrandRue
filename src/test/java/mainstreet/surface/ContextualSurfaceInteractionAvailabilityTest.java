package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.fulfilment.FulfilmentRoleIdentity;
import mainstreet.runtime.ActorAuthorisationAuthority;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextualSurfaceInteractionAvailabilityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final ExecutionPrincipal PRINCIPAL = new ExecutionPrincipal("staff-a");
    private static final FulfilmentRoleIdentity EXTERNAL_CALENDAR =
            new FulfilmentRoleIdentity("scheduling", "external-calendar");

    @Test
    void provider_unavailability_keeps_workspace_and_marks_only_dependent_action_unavailable() {
        SurfaceContributionRegistrySnapshot registry = registry();
        ExecutableMerchantModel model = model();
        StaticSurfaceContributionCatalogue staticCatalogue = registry.resolveFor(model);

        ContextualSurfaceComposition resolved = resolver(
                allAuthorised(),
                (scope, role) -> Optional.of(SurfaceInteractionAvailability.UNAVAILABLE)
        ).resolve(
                new MerchantSurfaceResolutionContext(MERCHANT, PRINCIPAL),
                model,
                staticCatalogue,
                registry
        );

        ContextualSurfaceContribution workspace = contribution(resolved, "calendar-workspace");
        ContextualSurfaceContribution externalAction = contribution(resolved, "push-external-calendar");
        ContextualSurfaceContribution internalAction = contribution(resolved, "create-internal-appointment");

        assertEquals(Optional.empty(), workspace.interactionAvailability());
        assertEquals(
                Optional.of(SurfaceInteractionAvailability.UNAVAILABLE),
                externalAction.interactionAvailability()
        );
        assertEquals(
                Optional.of(SurfaceInteractionAvailability.AVAILABLE),
                internalAction.interactionAvailability()
        );
        assertTrue(resolved.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .noneMatch(item -> item.kind() == SurfaceContributionKind.ATTENTION));
        assertEquals(
                Optional.of(EXTERNAL_CALENDAR),
                externalAction.interactionAvailabilityContract()
                        .requiredFulfilmentRole()
        );
    }

    @Test
    void degraded_provider_marks_only_explicitly_dependent_action_degraded() {
        ContextualSurfaceComposition resolved = resolveWithReadiness(
                Optional.of(SurfaceInteractionAvailability.DEGRADED),
                allAuthorised()
        );

        assertEquals(
                Optional.of(SurfaceInteractionAvailability.DEGRADED),
                contribution(resolved, "push-external-calendar").interactionAvailability()
        );
        assertEquals(
                Optional.of(SurfaceInteractionAvailability.AVAILABLE),
                contribution(resolved, "create-internal-appointment").interactionAvailability()
        );
        assertTrue(hasContribution(resolved, "calendar-workspace"));
    }

    @Test
    void ready_provider_marks_dependent_action_available() {
        ContextualSurfaceComposition resolved = resolveWithReadiness(
                Optional.of(SurfaceInteractionAvailability.AVAILABLE),
                allAuthorised()
        );

        assertEquals(
                Optional.of(SurfaceInteractionAvailability.AVAILABLE),
                contribution(resolved, "push-external-calendar").interactionAvailability()
        );
    }

    @Test
    void missing_readiness_evidence_fails_closed_to_unavailable() {
        ContextualSurfaceComposition resolved = resolveWithReadiness(
                Optional.empty(),
                allAuthorised()
        );

        assertEquals(
                Optional.of(SurfaceInteractionAvailability.UNAVAILABLE),
                contribution(resolved, "push-external-calendar").interactionAvailability()
        );
        assertTrue(hasContribution(resolved, "calendar-workspace"));
    }

    @Test
    void provider_readiness_does_not_grant_actor_authority() {
        ActorAuthorisationAuthority authority = (scope, principal, privilege) ->
                !privilege.identifier().equals("booking.external-calendar.push");

        ContextualSurfaceComposition resolved = resolveWithReadiness(
                Optional.of(SurfaceInteractionAvailability.AVAILABLE),
                authority
        );

        assertFalse(hasContribution(resolved, "push-external-calendar"));
        assertTrue(hasContribution(resolved, "calendar-workspace"));
        assertTrue(hasContribution(resolved, "create-internal-appointment"));
    }

    @Test
    void non_action_contribution_cannot_declare_fulfilment_role_interaction_dependency() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SurfaceContributionDefinition(
                        new SurfaceContributionIdentity("booking", "invalid-workspace"),
                        SurfaceAudience.MERCHANT,
                        SurfaceContributionKind.WORKSPACE,
                        Optional.of("merchant/calendar"),
                        Set.of(),
                        Set.of(),
                        SurfaceEligibilityContract.activeOnly("booking.view"),
                        SurfaceInteractionAvailabilityContract.requiresFulfilmentRole(
                                EXTERNAL_CALENDAR
                        )
                )
        );
    }

    private static ContextualSurfaceComposition resolveWithReadiness(
            Optional<SurfaceInteractionAvailability> readiness,
            ActorAuthorisationAuthority authority
    ) {
        SurfaceContributionRegistrySnapshot registry = registry();
        ExecutableMerchantModel model = model();
        return resolver(authority, (scope, role) -> readiness).resolve(
                new MerchantSurfaceResolutionContext(MERCHANT, PRINCIPAL),
                model,
                registry.resolveFor(model),
                registry
        );
    }

    private static ContextualSurfaceResolver resolver(
            ActorAuthorisationAuthority authority,
            FulfilmentRoleInteractionAvailabilityAuthority availabilityAuthority
    ) {
        return new ContextualSurfaceResolver(
                authority,
                (scope, capabilityIdentifier) -> false,
                availabilityAuthority
        );
    }

    private static ActorAuthorisationAuthority allAuthorised() {
        return (scope, principal, privilege) -> true;
    }

    private static ContextualSurfaceContribution contribution(
            ContextualSurfaceComposition composition,
            String contributionIdentifier
    ) {
        return composition.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .filter(item -> item.identity().contributionIdentifier()
                        .equals(contributionIdentifier))
                .findFirst()
                .orElseThrow();
    }

    private static boolean hasContribution(
            ContextualSurfaceComposition composition,
            String contributionIdentifier
    ) {
        return composition.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .anyMatch(item -> item.identity().contributionIdentifier()
                        .equals(contributionIdentifier));
    }

    private static SurfaceContributionRegistrySnapshot registry() {
        return new SurfaceContributionRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "calendar-workspace"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("merchant/calendar"),
                                Set.of("calendar-summary"),
                                Set.of(),
                                SurfaceEligibilityContract.activeOnly("booking.view")
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "push-external-calendar"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.ACTION,
                                Optional.of("merchant/calendar"),
                                Set.of(),
                                Set.of(),
                                SurfaceEligibilityContract.activeOnly(
                                        "booking.external-calendar.push"
                                ),
                                SurfaceInteractionAvailabilityContract
                                        .requiresFulfilmentRole(EXTERNAL_CALENDAR)
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "create-internal-appointment"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.ACTION,
                                Optional.of("merchant/calendar"),
                                Set.of(),
                                Set.of(),
                                SurfaceEligibilityContract.activeOnly(
                                        "booking.create"
                                )
                        )
                )
        );
    }

    private static ExecutableMerchantModel model() {
        return new ExecutableMerchantModel(
                "merchant-a",
                "model-1",
                1,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(),
                List.of(),
                List.of()
        );
    }
}
