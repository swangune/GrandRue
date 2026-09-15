package mainstreet.surface;

import mainstreet.semantic.executable.ExecutableMerchantModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves PUBLIC Surface candidates under MS-PROT-027 v1.2/v1.3 and
 * MS-PROT-049.
 *
 * <p>Static Surface Contribution resolution owns candidate membership.
 * Projection Serviceability and Exposure can only restrict those candidates;
 * neither can create a contribution or grant execution authority.</p>
 */
public final class PublicContextualSurfaceResolver {

    private static final Comparator<ContextualSurfaceContribution> CONTRIBUTION_ORDER =
            Comparator.comparing((ContextualSurfaceContribution contribution) ->
                            contribution.identity().ownerCapabilityIdentifier())
                    .thenComparing(contribution ->
                            contribution.identity().contributionIdentifier());

    private final ProjectionServiceabilityAuthority projectionServiceabilityAuthority;
    private final PublicSurfaceExposureAuthority exposureAuthority;

    public PublicContextualSurfaceResolver(
            ProjectionServiceabilityAuthority projectionServiceabilityAuthority,
            PublicSurfaceExposureAuthority exposureAuthority
    ) {
        this.projectionServiceabilityAuthority = Objects.requireNonNull(
                projectionServiceabilityAuthority,
                "projectionServiceabilityAuthority"
        );
        this.exposureAuthority = Objects.requireNonNull(
                exposureAuthority,
                "exposureAuthority"
        );
    }

    public ContextualSurfaceComposition resolve(
            PublicSurfaceResolutionContext context,
            ExecutableMerchantModel model,
            StaticSurfaceContributionCatalogue staticCatalogue,
            SurfaceContributionRegistrySnapshot registry
    ) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(staticCatalogue, "staticCatalogue");
        Objects.requireNonNull(registry, "registry");

        if (!context.merchantScope().merchantIdentifier().equals(
                model.merchantIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Public surface resolution context belongs to another merchant"
            );
        }
        if (!registry.semanticRegistryReleaseIdentifier().equals(
                model.semanticRegistryVersion()
        )) {
            throw new IllegalArgumentException(
                    "Surface registry release does not match executable model"
            );
        }

        Map<SurfaceContributionIdentity, SurfaceContributionDefinition> definitions =
                registry.definitions().stream().collect(
                        java.util.stream.Collectors.toUnmodifiableMap(
                                SurfaceContributionDefinition::identity,
                                definition -> definition
                        )
                );

        List<ContextualSurfaceContribution> included = new ArrayList<>();
        for (StaticSurfaceContribution contribution : staticCatalogue.contributions()) {
            if (contribution.audience() != SurfaceAudience.PUBLIC) {
                continue;
            }

            SurfaceContributionDefinition definition = definitions.get(
                    contribution.identity()
            );
            if (definition == null) {
                throw new IllegalArgumentException(
                        "Static surface contribution is absent from the pinned registry: "
                                + contribution.identity()
                );
            }
            requireStaticAffinity(contribution, definition);
            if (!model.capabilityIdentifiers().contains(
                    contribution.identity().ownerCapabilityIdentifier()
            )) {
                throw new IllegalArgumentException(
                        "Static surface contribution owner is not active: "
                                + contribution.identity().ownerCapabilityIdentifier()
                );
            }
            if (!projectionsServiceable(context, contribution)) {
                continue;
            }
            if (!exposed(context, contribution.identity())) {
                continue;
            }

            included.add(ContextualSurfaceContribution.fromStatic(
                    contribution,
                    Set.of(),
                    Optional.empty()
            ));
        }

        included.sort(CONTRIBUTION_ORDER);
        return compose(included);
    }

    private boolean projectionsServiceable(
            PublicSurfaceResolutionContext context,
            StaticSurfaceContribution contribution
    ) {
        for (String requirement : contribution.projectionRequirements()) {
            Optional<Boolean> decision = projectionServiceabilityAuthority
                    .currentServiceability(
                            context.merchantScope(),
                            requirement,
                            context.requestContext()
                    );
            if (decision == null || decision.isEmpty() || !decision.orElseThrow()) {
                return false;
            }
        }
        return true;
    }

    private boolean exposed(
            PublicSurfaceResolutionContext context,
            SurfaceContributionIdentity contributionIdentity
    ) {
        Optional<ExposureDecision> decision = exposureAuthority.currentDecision(
                context.merchantScope(),
                contributionIdentity,
                context.requestContext()
        );
        return decision != null
                && decision.orElse(ExposureDecision.WITHHOLD)
                == ExposureDecision.EXPOSE;
    }

    private static void requireStaticAffinity(
            StaticSurfaceContribution contribution,
            SurfaceContributionDefinition definition
    ) {
        if (contribution.audience() != definition.audience()
                || contribution.kind() != definition.kind()
                || !contribution.compositionTargetReference().equals(
                        definition.compositionTargetReference()
                )
                || !contribution.projectionRequirements().equals(
                        definition.projectionRequirements()
                )
                || !contribution.supportedOperationReferences().equals(
                        definition.supportedOperationReferences()
                )
                || !contribution.eligibilityContract().equals(
                        definition.eligibilityContract()
                )
                || !contribution.interactionAvailabilityContract().equals(
                        definition.interactionAvailabilityContract()
                )) {
            throw new IllegalArgumentException(
                    "Static surface contribution does not match pinned registry definition: "
                            + contribution.identity()
            );
        }
    }

    private static ContextualSurfaceComposition compose(
            List<ContextualSurfaceContribution> contributions
    ) {
        Map<String, List<ContextualSurfaceContribution>> targeted =
                new LinkedHashMap<>();
        List<ContextualSurfaceCompositionGroup> groups = new ArrayList<>();

        for (ContextualSurfaceContribution contribution : contributions) {
            Optional<String> target = contribution.compositionTargetReference();
            if (target.isPresent()) {
                targeted.computeIfAbsent(
                        target.orElseThrow(),
                        ignored -> new ArrayList<>()
                ).add(contribution);
            } else {
                groups.add(new ContextualSurfaceCompositionGroup(
                        SurfaceAudience.PUBLIC,
                        Optional.empty(),
                        List.of(contribution)
                ));
            }
        }

        for (Map.Entry<String, List<ContextualSurfaceContribution>> entry
                : targeted.entrySet()) {
            groups.add(new ContextualSurfaceCompositionGroup(
                    SurfaceAudience.PUBLIC,
                    Optional.of(entry.getKey()),
                    entry.getValue()
            ));
        }

        groups.sort(Comparator
                .comparing((ContextualSurfaceCompositionGroup group) ->
                        group.compositionTargetReference().isEmpty())
                .thenComparing(group ->
                        group.compositionTargetReference().orElse(""))
                .thenComparing(group ->
                        group.contributions().getFirst().identity()
                                .ownerCapabilityIdentifier())
                .thenComparing(group ->
                        group.contributions().getFirst().identity()
                                .contributionIdentifier()));

        return new ContextualSurfaceComposition(groups);
    }
}
