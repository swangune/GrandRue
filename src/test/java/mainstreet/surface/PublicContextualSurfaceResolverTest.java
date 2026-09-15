package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PublicContextualSurfaceResolverTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final PublicSurfaceResolutionContext CONTEXT =
            new PublicSurfaceResolutionContext(MERCHANT, Optional.of("storefront"));

    @Test
    void keeps_only_public_candidates_with_serviceable_projections_and_exposure() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("publication", "booking"));

        PublicContextualSurfaceResolver resolver = new PublicContextualSurfaceResolver(
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, contribution, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );

        ContextualSurfaceComposition result = resolver.resolve(
                CONTEXT,
                model,
                registry.resolveFor(model),
                registry
        );

        assertEquals(
                Set.of(
                        new SurfaceContributionIdentity("publication", "public-content"),
                        new SurfaceContributionIdentity("booking", "public-book")
                ),
                contributionIdentities(result)
        );
    }

    @Test
    void projection_not_serviceable_or_unestablished_removes_only_affected_candidate() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("publication", "booking"));

        PublicContextualSurfaceResolver unavailable = new PublicContextualSurfaceResolver(
                (scope, projection, requestContext) ->
                        projection.equals("publication-public-summary")
                                ? Optional.of(false)
                                : Optional.of(true),
                (scope, contribution, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );
        PublicContextualSurfaceResolver unknown = new PublicContextualSurfaceResolver(
                (scope, projection, requestContext) ->
                        projection.equals("publication-public-summary")
                                ? Optional.empty()
                                : Optional.of(true),
                (scope, contribution, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );

        for (PublicContextualSurfaceResolver resolver : List.of(unavailable, unknown)) {
            ContextualSurfaceComposition result = resolver.resolve(
                    CONTEXT,
                    model,
                    registry.resolveFor(model),
                    registry
            );
            assertEquals(
                    Set.of(new SurfaceContributionIdentity("booking", "public-book")),
                    contributionIdentities(result)
            );
        }
    }

    @Test
    void exposure_withhold_or_unestablished_fails_closed_without_creating_membership() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("publication", "booking"));
        List<SurfaceContributionIdentity> observed = new ArrayList<>();

        PublicContextualSurfaceResolver resolver = new PublicContextualSurfaceResolver(
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, contribution, requestContext) -> {
                    observed.add(contribution);
                    if (contribution.equals(new SurfaceContributionIdentity(
                            "publication",
                            "public-content"
                    ))) {
                        return Optional.of(ExposureDecision.WITHHOLD);
                    }
                    return Optional.empty();
                }
        );

        ContextualSurfaceComposition result = resolver.resolve(
                CONTEXT,
                model,
                registry.resolveFor(model),
                registry
        );

        assertEquals(Set.of(), contributionIdentities(result));
        assertEquals(
                Set.of(
                        new SurfaceContributionIdentity("publication", "public-content"),
                        new SurfaceContributionIdentity("booking", "public-book")
                ),
                Set.copyOf(observed)
        );
    }

    @Test
    void non_public_contributions_are_not_submitted_to_public_exposure_authority() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("publication", "booking"));
        List<SurfaceContributionIdentity> observed = new ArrayList<>();

        PublicContextualSurfaceResolver resolver = new PublicContextualSurfaceResolver(
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, contribution, requestContext) -> {
                    observed.add(contribution);
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
                Set.of(
                        new SurfaceContributionIdentity("publication", "public-content"),
                        new SurfaceContributionIdentity("booking", "public-book")
                ),
                contributionIdentities(result)
        );
        assertEquals(contributionIdentities(result), Set.copyOf(observed));
    }

    @Test
    void rejects_cross_merchant_or_release_mismatch() {
        SurfaceContributionRegistrySnapshot registry = registry("semantic-registry-1.0");
        ExecutableMerchantModel model = model(Set.of("publication"));
        PublicContextualSurfaceResolver resolver = new PublicContextualSurfaceResolver(
                (scope, projection, requestContext) -> Optional.of(true),
                (scope, contribution, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        new PublicSurfaceResolutionContext(
                                new MerchantScope("merchant-b"),
                                Optional.of("storefront")
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
                                        "publication",
                                        "public-content"
                                ),
                                SurfaceAudience.PUBLIC,
                                SurfaceContributionKind.PUBLIC_INTERACTION,
                                Optional.of("public/content"),
                                Set.of("publication-public-summary"),
                                Set.of()
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
                        ),
                        new SurfaceContributionDefinition(
                                new SurfaceContributionIdentity(
                                        "booking",
                                        "merchant-bookings"
                                ),
                                SurfaceAudience.MERCHANT,
                                SurfaceContributionKind.WORKSPACE,
                                Optional.of("merchant/bookings"),
                                Set.of("booking-summary"),
                                Set.of()
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
