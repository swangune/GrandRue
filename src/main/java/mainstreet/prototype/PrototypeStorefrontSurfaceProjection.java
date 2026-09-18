package mainstreet.prototype;

import grandrue.application.MerchantScope;
import mainstreet.surface.ContextualSurfaceContribution;
import mainstreet.surface.ExposureDecision;
import mainstreet.surface.ProjectionServiceabilityAuthority;
import mainstreet.surface.PublicContextualSurfaceResolver;
import mainstreet.surface.PublicInteractionBindingExposureAuthority;
import mainstreet.surface.PublicSurfaceExposureAuthority;
import mainstreet.surface.PublicSurfaceResolutionContext;
import mainstreet.surface.SurfaceAudience;
import mainstreet.surface.SurfaceContributionDefinition;
import mainstreet.surface.SurfaceContributionIdentity;
import mainstreet.surface.SurfaceContributionKind;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Prototype adapter from active executable configuration to public storefront
 * composition inputs. Static Surface Contribution membership is narrowed by
 * current PUBLIC Projection Serviceability and Exposure before audience-safe
 * public subject bindings are projected. Merchant category is never consulted.
 */
public final class PrototypeStorefrontSurfaceProjection {

    private static final String STOREFRONT_REQUEST_CONTEXT = "prototype-storefront";

    private final PrototypeMerchantRuntime merchantRuntime;
    private final SurfaceContributionRegistrySnapshot registry;
    private final PublicContextualSurfaceResolver contextualResolver;
    private final PrototypePublicInteractionBindingResolver bindingResolver;
    private final PublicInteractionBindingExposureAuthority bindingExposureAuthority;

    private PrototypeStorefrontSurfaceProjection(
            PrototypeMerchantRuntime merchantRuntime,
            SurfaceContributionRegistrySnapshot registry,
            PublicContextualSurfaceResolver contextualResolver,
            PrototypePublicInteractionBindingResolver bindingResolver,
            PublicInteractionBindingExposureAuthority bindingExposureAuthority
    ) {
        this.merchantRuntime = Objects.requireNonNull(
                merchantRuntime,
                "merchantRuntime"
        );
        this.registry = Objects.requireNonNull(registry, "registry");
        this.contextualResolver = Objects.requireNonNull(
                contextualResolver,
                "contextualResolver"
        );
        this.bindingResolver = Objects.requireNonNull(
                bindingResolver,
                "bindingResolver"
        );
        this.bindingExposureAuthority = Objects.requireNonNull(
                bindingExposureAuthority,
                "bindingExposureAuthority"
        );
    }

    public static PrototypeStorefrontSurfaceProjection standard(
            PrototypeMerchantRuntime merchantRuntime
    ) {
        return withContextAuthorities(
                merchantRuntime,
                // No current prototype contribution declares a Projection
                // Contract dependency. Any future requirement therefore fails
                // closed until a real owner/serviceability adapter is wired.
                (scope, projectionRequirement, requestContext) -> Optional.empty(),
                // Explicit prototype contribution Exposure fixture for
                // already-candidate PUBLIC contributions.
                (scope, contribution, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE),
                // Explicit prototype subject Exposure fixture. Binding
                // participation is still resolved independently before this
                // filter is asked.
                (scope, contribution, subjectReference, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );
    }

    static PrototypeStorefrontSurfaceProjection withContextAuthorities(
            PrototypeMerchantRuntime merchantRuntime,
            ProjectionServiceabilityAuthority projectionServiceabilityAuthority,
            PublicSurfaceExposureAuthority exposureAuthority
    ) {
        return withContextAuthorities(
                merchantRuntime,
                projectionServiceabilityAuthority,
                exposureAuthority,
                (scope, contribution, subjectReference, requestContext) ->
                        Optional.of(ExposureDecision.EXPOSE)
        );
    }

    static PrototypeStorefrontSurfaceProjection withContextAuthorities(
            PrototypeMerchantRuntime merchantRuntime,
            ProjectionServiceabilityAuthority projectionServiceabilityAuthority,
            PublicSurfaceExposureAuthority exposureAuthority,
            PublicInteractionBindingExposureAuthority bindingExposureAuthority
    ) {
        SurfaceContributionRegistrySnapshot registry = standardRegistry();
        return new PrototypeStorefrontSurfaceProjection(
                merchantRuntime,
                registry,
                new PublicContextualSurfaceResolver(
                        projectionServiceabilityAuthority,
                        exposureAuthority
                ),
                PrototypePublicInteractionBindingResolver.standard(),
                bindingExposureAuthority
        );
    }

    public PrototypeStorefrontSurfaceView surface(String merchantIdentifier) {
        var active = merchantRuntime.activation().current(merchantIdentifier)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown prototype merchant: " + merchantIdentifier
                ));
        var release = active.release();
        var model = release.executableModel();
        var catalogue = registry.resolveFor(model);
        PublicSurfaceResolutionContext context = new PublicSurfaceResolutionContext(
                new MerchantScope(merchantIdentifier),
                Optional.of(STOREFRONT_REQUEST_CONTEXT)
        );
        var composition = contextualResolver.resolve(
                context,
                model,
                catalogue,
                registry
        );

        List<PrototypeStorefrontSurfaceView.Group> groups =
                composition.groups().stream()
                        .filter(group -> group.audience() == SurfaceAudience.PUBLIC)
                        .map(group -> new PrototypeStorefrontSurfaceView.Group(
                                group.compositionTargetReference()
                                        .orElseThrow(() -> new IllegalStateException(
                                                "Prototype storefront contribution requires a registered composition target"
                                        )),
                                group.contributions().stream()
                                        .map(contribution -> view(
                                                context,
                                                contribution
                                        ))
                                        .toList()
                        ))
                        .toList();

        return new PrototypeStorefrontSurfaceView(
                merchantIdentifier,
                release.configurationIdentifier(),
                release.releaseIdentifier(),
                groups
        );
    }

    private static SurfaceContributionRegistrySnapshot standardRegistry() {
        return new SurfaceContributionRegistrySnapshot(
                PrototypeMerchantRuntime.SEMANTIC_REGISTRY,
                Set.of(
                        publicContribution(
                                "publication",
                                "browse-published-content",
                                "public/content",
                                Set.of()
                        ),
                        publicContribution(
                                "appointment",
                                "arrange-appointment",
                                "public/primary-actions",
                                Set.of("appointment.confirm")
                        ),
                        publicContribution(
                                "booking",
                                "reserve-subject",
                                "public/primary-actions",
                                Set.of("booking.confirm")
                        ),
                        publicContribution(
                                "ordering",
                                "place-order",
                                "public/primary-actions",
                                Set.of("ordering.commit")
                        )
                )
        );
    }

    private static SurfaceContributionDefinition publicContribution(
            String ownerCapabilityIdentifier,
            String contributionIdentifier,
            String compositionTargetReference,
            Set<String> supportedOperationReferences
    ) {
        return new SurfaceContributionDefinition(
                new SurfaceContributionIdentity(
                        ownerCapabilityIdentifier,
                        contributionIdentifier
                ),
                SurfaceAudience.PUBLIC,
                SurfaceContributionKind.PUBLIC_INTERACTION,
                Optional.of(compositionTargetReference),
                Set.of(),
                supportedOperationReferences
        );
    }

    private PrototypeStorefrontSurfaceView.Contribution view(
            PublicSurfaceResolutionContext context,
            ContextualSurfaceContribution contribution
    ) {
        List<PrototypePublicInteractionBinding> bindings = bindingResolver.resolve(
                        context.merchantScope().merchantIdentifier(),
                        contribution.identity().ownerCapabilityIdentifier(),
                        contribution.identity().contributionIdentifier()
                ).stream()
                .filter(binding -> bindingExposed(
                        context,
                        contribution.identity(),
                        binding
                ))
                .toList();

        return new PrototypeStorefrontSurfaceView.Contribution(
                contribution.identity().ownerCapabilityIdentifier(),
                contribution.identity().contributionIdentifier(),
                contribution.kind().name(),
                contribution.supportedOperationReferences(),
                bindings
        );
    }

    private boolean bindingExposed(
            PublicSurfaceResolutionContext context,
            SurfaceContributionIdentity contributionIdentity,
            PrototypePublicInteractionBinding binding
    ) {
        Optional<ExposureDecision> decision = bindingExposureAuthority.currentDecision(
                context.merchantScope(),
                contributionIdentity,
                binding.subjectReference(),
                context.requestContext()
        );
        return decision != null
                && decision.orElse(ExposureDecision.WITHHOLD)
                == ExposureDecision.EXPOSE;
    }
}
