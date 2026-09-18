package grandrue.surface;

import grandrue.semantic.executable.ExecutableMerchantModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves CUSTOMER Surface candidates under MS-PROT-049 v1.3.
 *
 * <p>Registered Surface definitions own candidate membership. A relationship-
 * bound CUSTOMER contribution is included only when its owner-qualified
 * customer eligibility requirement is currently satisfied from trusted
 * merchant-scoped context. Projection Serviceability and Exposure then restrict
 * the eligible candidates independently. None of these read-side decisions
 * grants execution authority.</p>
 */
public final class CustomerContextualSurfaceResolver {

    private static final Comparator<ContextualSurfaceContribution> CONTRIBUTION_ORDER =
            Comparator.comparing((ContextualSurfaceContribution contribution) ->
                            contribution.identity().ownerCapabilityIdentifier())
                    .thenComparing(contribution ->
                            contribution.identity().contributionIdentifier());

    private final CustomerSurfaceEligibilityAuthority eligibilityAuthority;
    private final ProjectionServiceabilityAuthority projectionServiceabilityAuthority;
    private final CustomerSurfaceExposureAuthority exposureAuthority;

    public CustomerContextualSurfaceResolver(
            CustomerSurfaceEligibilityAuthority eligibilityAuthority,
            ProjectionServiceabilityAuthority projectionServiceabilityAuthority,
            CustomerSurfaceExposureAuthority exposureAuthority
    ) {
        this.eligibilityAuthority = Objects.requireNonNull(
                eligibilityAuthority,
                "eligibilityAuthority"
        );
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
            CustomerSurfaceResolutionContext context,
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
                    "Customer surface resolution context belongs to another merchant"
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
            if (contribution.audience() != SurfaceAudience.CUSTOMER) {
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

            Optional<CustomerSurfaceEligibilityRequirementIdentity> satisfiedRequirement =
                    satisfiedRequirement(context, contribution);
            if (satisfiedRequirement.isEmpty()) {
                continue;
            }
            if (!projectionsServiceable(context, contribution)) {
                continue;
            }
            if (!exposed(context, contribution.identity())) {
                continue;
            }

            included.add(ContextualSurfaceContribution.fromStatic(
                    contribution,
                    Set.of(new CustomerRelationshipSurfaceEligibilityEvidence(
                            satisfiedRequirement.orElseThrow()
                    )),
                    contribution.kind() == SurfaceContributionKind.ACTION
                            ? Optional.of(SurfaceInteractionAvailability.AVAILABLE)
                            : Optional.empty()
            ));
        }

        included.sort(CONTRIBUTION_ORDER);
        return compose(included);
    }

    private Optional<CustomerSurfaceEligibilityRequirementIdentity> satisfiedRequirement(
            CustomerSurfaceResolutionContext context,
            StaticSurfaceContribution contribution
    ) {
        Optional<CustomerSurfaceEligibilityRequirementIdentity> requirement =
                contribution.customerEligibilityRequirement();
        if (requirement.isEmpty()) {
            return Optional.empty();
        }

        Optional<Boolean> decision = eligibilityAuthority.currentEligibility(
                context.merchantScope(),
                context.trustedExecutionContext(),
                requirement.orElseThrow(),
                context.requestContext()
        );
        if (decision == null || !decision.orElse(false)) {
            return Optional.empty();
        }
        return requirement;
    }

    private boolean projectionsServiceable(
            CustomerSurfaceResolutionContext context,
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
            CustomerSurfaceResolutionContext context,
            SurfaceContributionIdentity contributionIdentity
    ) {
        Optional<ExposureDecision> decision = exposureAuthority.currentDecision(
                context.merchantScope(),
                context.trustedExecutionContext(),
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
                )
                || !contribution.customerEligibilityRequirement().equals(
                        definition.customerEligibilityRequirement()
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
                        SurfaceAudience.CUSTOMER,
                        Optional.empty(),
                        List.of(contribution)
                ));
            }
        }

        for (Map.Entry<String, List<ContextualSurfaceContribution>> entry
                : targeted.entrySet()) {
            groups.add(new ContextualSurfaceCompositionGroup(
                    SurfaceAudience.CUSTOMER,
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
