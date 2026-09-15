package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerContextualSurfaceResolverTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final CustomerSurfaceResolutionContext CONTEXT =
            new CustomerSurfaceResolutionContext(
                    new TrustedExecutionContext(
                            MERCHANT,
                            new ExecutionPrincipal("customer-a"),
                            Optional.empty()
                    ),
                    Optional.of("customer/request")
            );

    @Test
    void includes_only_customer_candidates_with_satisfied_registered_relationship_requirement() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("booking", "appointment"));
        List<CustomerSurfaceEligibilityRequirementIdentity> observedRequirements =
                new ArrayList<>();

        CustomerContextualSurfaceResolver resolver = new CustomerContextualSurfaceResolver(
                (scope, trustedContext, requirement, requestContext) -> {
                    observedRequirements.add(requirement);
                    return Optional.of(requirement.equals(bookingRequirement()));
                },
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, trustedContext, contribution, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );

        ContextualSurfaceComposition result = resolver.resolve(
                CONTEXT,
                model,
                registry.resolveFor(model),
                registry
        );

        assertEquals(
                Set.of(new SurfaceContributionIdentity("booking", "customer-booking")),
                contributionIdentities(result)
        );
        assertEquals(
                Set.of(new CustomerRelationshipSurfaceEligibilityEvidence(
                        bookingRequirement()
                )),
                onlyContribution(result).contextualEligibilityEvidence()
        );
        assertEquals(
                Set.of(bookingRequirement(), appointmentRequirement()),
                Set.copyOf(observedRequirements)
        );
    }

    @Test
    void customer_contribution_without_registered_relationship_requirement_fails_closed() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("booking", "publication"));
        List<SurfaceContributionIdentity> exposureChecks = new ArrayList<>();

        CustomerContextualSurfaceResolver resolver = new CustomerContextualSurfaceResolver(
                (scope, trustedContext, requirement, requestContext) -> Optional.of(true),
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, trustedContext, contribution, requestContext) -> {
                    exposureChecks.add(contribution);
                    return Optional.of(ExposureDecision.EXPOSE);
                }
        );

        ContextualSurfaceComposition result = resolver.resolve(
                CONTEXT,
                model,
                registry.resolveFor(model),
                registry
        );

        assertEquals(
                Set.of(new SurfaceContributionIdentity("booking", "customer-booking")),
                contributionIdentities(result)
        );
        assertEquals(
                Set.of(new SurfaceContributionIdentity("booking", "customer-booking")),
                Set.copyOf(exposureChecks)
        );
    }

    @Test
    void authenticated_principal_does_not_bypass_missing_or_unestablished_relationship_evidence() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("booking"));

        for (Optional<Boolean> decision
                : List.of(Optional.<Boolean>empty(), Optional.of(false))) {
            CustomerContextualSurfaceResolver resolver =
                    new CustomerContextualSurfaceResolver(
                            (scope, trustedContext, requirement, requestContext) -> decision,
                            (scope, projection, requestContext) -> Optional.of(true),
                            (scope, trustedContext, contribution, requestContext) ->
                                    Optional.of(ExposureDecision.EXPOSE)
                    );

            assertEquals(
                    Set.of(),
                    contributionIdentities(resolver.resolve(
                            CONTEXT,
                            model,
                            registry.resolveFor(model),
                            registry
                    ))
            );
        }
    }

    @Test
    void projection_serviceability_and_exposure_restrict_but_do_not_create_customer_membership() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("booking", "appointment"));

        CustomerContextualSurfaceResolver resolver = new CustomerContextualSurfaceResolver(
                (scope, trustedContext, requirement, requestContext) -> Optional.of(true),
                (scope, projection, requestContext) ->
                        projection.equals("booking-customer-summary")
                                ? Optional.of(false)
                                : Optional.of(true),
                (scope, trustedContext, contribution, requestContext) ->
                        contribution.equals(new SurfaceContributionIdentity(
                                "appointment",
                                "customer-appointment"
                        ))
                                ? Optional.of(ExposureDecision.WITHHOLD)
                                : Optional.of(ExposureDecision.EXPOSE)
        );

        assertEquals(
                Set.of(),
                contributionIdentities(resolver.resolve(
                        CONTEXT,
                        model,
                        registry.resolveFor(model),
                        registry
                ))
        );
    }

    @Test
    void ignores_non_customer_candidates_and_rejects_cross_merchant_or_release_mismatch() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("booking", "publication"));
        List<SurfaceContributionIdentity> exposureChecks = new ArrayList<>();

        CustomerContextualSurfaceResolver resolver = new CustomerContextualSurfaceResolver(
                (scope, trustedContext, requirement, requestContext) -> Optional.of(true),
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, trustedContext, contribution, requestContext) -> {
                    exposureChecks.add(contribution);
                    return Optional.of(ExposureDecision.EXPOSE);
                }
        );

        ContextualSurfaceComposition result = resolver.resolve(
                CONTEXT,
                model,
                registry.resolveFor(model),
                registry
        );
        assertEquals(
                Set.of(new SurfaceContributionIdentity("booking", "customer-booking")),
                contributionIdentities(result)
        );
        assertEquals(contributionIdentities(result), Set.copyOf(exposureChecks));

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        new CustomerSurfaceResolutionContext(
                                new TrustedExecutionContext(
                                        new MerchantScope("merchant-b"),
                                        new ExecutionPrincipal("customer-a"),
                                        Optional.empty()
                                ),
                                Optional.empty()
                        ),
                        model,
                        registry.resolveFor(model),
                        registry
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        CONTEXT,
                        model,
                        registry.resolveFor(model),
                        registry("semantic-registry-2.0")
                )
        );
    }

    private static CustomerSurfaceEligibilityRequirementIdentity bookingRequirement() {
        return new CustomerSurfaceEligibilityRequirementIdentity(
                "booking",
                "related-customer-booking"
        );
    }

    private static CustomerSurfaceEligibilityRequirementIdentity appointmentRequirement() {
        return new CustomerSurfaceEligibilityRequirementIdentity(
                "appointment",
                "related-customer-appointment"
        );
    }

    private static ContextualSurfaceContribution onlyContribution(
            ContextualSurfaceComposition composition
    ) {
        return composition.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .findFirst()
                .orElseThrow();
    }

    private static Set<SurfaceContributionIdentity> contributionIdentities(
            ContextualSurfaceComposition composition
    ) {
        return composition.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .map(ContextualSurfaceContribution::identity)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    private static SurfaceContributionRegistrySnapshot registry(String release) {
        return new SurfaceContributionRegistrySnapshot(
                release,
                Set.of(
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "customer-booking"
                                ),
                                SurfaceAudience.CUSTOMER,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("customer/bookings"),
                                Set.of("booking-customer-summary"),
                                Set.of(),
                                SurfaceEligibilityContract.activeOnly(),
                                SurfaceInteractionAvailabilityContract.independent(),
                                Optional.of(bookingRequirement())
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "appointment",
                                        "customer-appointment"
                                ),
                                SurfaceAudience.CUSTOMER,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("customer/appointments"),
                                Set.of("appointment-customer-summary"),
                                Set.of(),
                                SurfaceEligibilityContract.activeOnly(),
                                SurfaceInteractionAvailabilityContract.independent(),
                                Optional.of(appointmentRequirement())
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "publication",
                                        "customer-unqualified"
                                ),
                                SurfaceAudience.CUSTOMER,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("customer/content"),
                                Set.of(),
                                Set.of()
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "publication",
                                        "public-content"
                                ),
                                SurfaceAudience.PUBLIC,
                                SurfaceContributionKind.PUBLIC_INTERACTION,
                                Optional.of("public/content"),
                                Set.of(),
                                Set.of()
                        )
                ),
                Set.of(
                        new CustomerSurfaceEligibilityRequirementDefinition(
                                bookingRequirement()
                        ),
                        new CustomerSurfaceEligibilityRequirementDefinition(
                                appointmentRequirement()
                        )
                )
        );
    }

    private static ExecutableMerchantModel model(Set<String> capabilities) {
        return new ExecutableMerchantModel(
                "merchant-a",
                "model-1",
                1,
                "semantic-registry-1.0",
                capabilities,
                List.of(),
                List.of(),
                List.of()
        );
    }
}
