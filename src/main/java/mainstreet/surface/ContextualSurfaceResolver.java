package mainstreet.surface;

import mainstreet.fulfilment.FulfilmentRoleIdentity;
import mainstreet.runtime.ActorAuthorisationAuthority;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.executable.ExecutableMerchantModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves bounded merchant-context surface eligibility governed by
 * MS-PROT-049. Capability presence/residual obligations and actor authority
 * decide contribution inclusion. For included ACTION contributions, v1.1 may
 * additionally project current interaction availability from an explicitly
 * declared fulfilment-role dependency.
 *
 * <p>Surface filtering and interaction availability never grant execution
 * authority. Commands remain independently guarded by the runtime execution
 * boundary.</p>
 */
public final class ContextualSurfaceResolver {

    private static final Comparator<ContextualSurfaceContribution> CONTRIBUTION_ORDER =
            Comparator.comparingInt(
                            (ContextualSurfaceContribution contribution) ->
                                    contribution.audience().ordinal()
                    )
                    .thenComparing(contribution ->
                            contribution.identity().ownerCapabilityIdentifier())
                    .thenComparing(contribution ->
                            contribution.identity().contributionIdentifier());

    private static final Comparator<SurfaceContributionDefinition> DEFINITION_ORDER =
            Comparator.comparing((SurfaceContributionDefinition definition) ->
                            definition.identity().ownerCapabilityIdentifier())
                    .thenComparing(definition ->
                            definition.identity().contributionIdentifier());

    private final ActorAuthorisationAuthority actorAuthorisationAuthority;
    private final ResidualSurfaceObligationAuthority residualObligationAuthority;
    private final FulfilmentRoleInteractionAvailabilityAuthority
            interactionAvailabilityAuthority;

    public ContextualSurfaceResolver(
            ActorAuthorisationAuthority actorAuthorisationAuthority,
            ResidualSurfaceObligationAuthority residualObligationAuthority,
            FulfilmentRoleInteractionAvailabilityAuthority
                    interactionAvailabilityAuthority
    ) {
        this.actorAuthorisationAuthority = Objects.requireNonNull(
                actorAuthorisationAuthority,
                "actorAuthorisationAuthority"
        );
        this.residualObligationAuthority = Objects.requireNonNull(
                residualObligationAuthority,
                "residualObligationAuthority"
        );
        this.interactionAvailabilityAuthority = Objects.requireNonNull(
                interactionAvailabilityAuthority,
                "interactionAvailabilityAuthority"
        );
    }

    /**
     * Compatibility constructor for contexts that have no provider-readiness
     * adapter yet. Any future explicitly dependent ACTION fails closed to
     * UNAVAILABLE rather than being assumed ready.
     */
    public ContextualSurfaceResolver(
            ActorAuthorisationAuthority actorAuthorisationAuthority,
            ResidualSurfaceObligationAuthority residualObligationAuthority
    ) {
        this(
                actorAuthorisationAuthority,
                residualObligationAuthority,
                (scope, roleIdentity) -> Optional.empty()
        );
    }

    public ContextualSurfaceComposition resolve(
            MerchantSurfaceResolutionContext context,
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
                    "Surface resolution context belongs to another merchant"
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

        List<ContextualSurfaceContribution> eligible = new ArrayList<>();

        for (StaticSurfaceContribution contribution : staticCatalogue.contributions()) {
            if (contribution.audience() != SurfaceAudience.MERCHANT) {
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

            Set<ContextualSurfaceEligibilityEvidence> evidence = new HashSet<>();
            if (actorEligible(context, contribution.eligibilityContract(), evidence)) {
                eligible.add(ContextualSurfaceContribution.fromStatic(
                        contribution,
                        evidence,
                        interactionAvailability(
                                context,
                                contribution.kind(),
                                contribution.interactionAvailabilityContract()
                        )
                ));
            }
        }

        Map<String, Boolean> residualByCapability = new HashMap<>();
        List<SurfaceContributionDefinition> orderedDefinitions = new ArrayList<>(
                registry.definitions()
        );
        orderedDefinitions.sort(DEFINITION_ORDER);

        for (SurfaceContributionDefinition definition : orderedDefinitions) {
            String owner = definition.identity().ownerCapabilityIdentifier();
            if (definition.audience() != SurfaceAudience.MERCHANT
                    || model.capabilityIdentifiers().contains(owner)
                    || !definition.eligibilityContract().allowsResidualManagement()) {
                continue;
            }

            boolean hasResidual = residualByCapability.computeIfAbsent(
                    owner,
                    capabilityIdentifier -> residualObligationAuthority
                            .hasOutstandingObligations(
                                    context.merchantScope(),
                                    capabilityIdentifier
                            )
            );
            if (!hasResidual) {
                continue;
            }

            Set<ContextualSurfaceEligibilityEvidence> evidence = new HashSet<>();
            evidence.add(new ResidualObligationSurfaceEligibilityEvidence(owner));
            if (actorEligible(context, definition.eligibilityContract(), evidence)) {
                eligible.add(ContextualSurfaceContribution.fromResidual(
                        definition,
                        evidence,
                        interactionAvailability(
                                context,
                                definition.kind(),
                                definition.interactionAvailabilityContract()
                        )
                ));
            }
        }

        eligible.sort(CONTRIBUTION_ORDER);
        return compose(eligible);
    }

    private boolean actorEligible(
            MerchantSurfaceResolutionContext context,
            SurfaceEligibilityContract eligibilityContract,
            Set<ContextualSurfaceEligibilityEvidence> evidence
    ) {
        Optional<String> requiredPrivilege =
                eligibilityContract.requiredPrivilegeIdentifier();
        if (requiredPrivilege.isEmpty()) {
            return true;
        }

        String privilegeIdentifier = requiredPrivilege.orElseThrow();
        boolean authorised = actorAuthorisationAuthority.isAuthorised(
                context.merchantScope(),
                context.principal(),
                new Privilege(privilegeIdentifier)
        );
        if (authorised) {
            evidence.add(new ActorAuthoritySurfaceEligibilityEvidence(
                    privilegeIdentifier
            ));
        }
        return authorised;
    }

    private Optional<SurfaceInteractionAvailability> interactionAvailability(
            MerchantSurfaceResolutionContext context,
            SurfaceContributionKind kind,
            SurfaceInteractionAvailabilityContract availabilityContract
    ) {
        if (kind != SurfaceContributionKind.ACTION) {
            return Optional.empty();
        }

        Optional<FulfilmentRoleIdentity> requiredRole =
                availabilityContract.requiredFulfilmentRole();
        if (requiredRole.isEmpty()) {
            return Optional.of(SurfaceInteractionAvailability.AVAILABLE);
        }

        Optional<SurfaceInteractionAvailability> current =
                interactionAvailabilityAuthority.currentAvailability(
                        context.merchantScope(),
                        requiredRole.orElseThrow()
                );
        return Optional.of(
                current == null
                        ? SurfaceInteractionAvailability.UNAVAILABLE
                        : current.orElse(SurfaceInteractionAvailability.UNAVAILABLE)
        );
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
        Map<TargetKey, List<ContextualSurfaceContribution>> targeted =
                new LinkedHashMap<>();
        List<ContextualSurfaceCompositionGroup> groups = new ArrayList<>();

        for (ContextualSurfaceContribution contribution : contributions) {
            Optional<String> target = contribution.compositionTargetReference();
            if (target.isPresent()) {
                targeted.computeIfAbsent(
                        new TargetKey(contribution.audience(), target.orElseThrow()),
                        ignored -> new ArrayList<>()
                ).add(contribution);
            } else {
                groups.add(new ContextualSurfaceCompositionGroup(
                        contribution.audience(),
                        Optional.empty(),
                        List.of(contribution)
                ));
            }
        }

        for (Map.Entry<TargetKey, List<ContextualSurfaceContribution>> entry
                : targeted.entrySet()) {
            groups.add(new ContextualSurfaceCompositionGroup(
                    entry.getKey().audience(),
                    Optional.of(entry.getKey().targetReference()),
                    entry.getValue()
            ));
        }

        groups.sort(GROUP_ORDER);
        return new ContextualSurfaceComposition(groups);
    }

    private static final Comparator<ContextualSurfaceCompositionGroup> GROUP_ORDER =
            Comparator.comparingInt(
                            (ContextualSurfaceCompositionGroup group) ->
                                    group.audience().ordinal()
                    )
                    .thenComparing(group ->
                            group.compositionTargetReference().isEmpty())
                    .thenComparing(group ->
                            group.compositionTargetReference().orElse(""))
                    .thenComparing(group ->
                            group.contributions().getFirst().identity()
                                    .ownerCapabilityIdentifier())
                    .thenComparing(group ->
                            group.contributions().getFirst().identity()
                                    .contributionIdentifier());

    private record TargetKey(
            SurfaceAudience audience,
            String targetReference
    ) {
        private TargetKey {
            Objects.requireNonNull(audience, "audience");
            if (targetReference == null || targetReference.isBlank()) {
                throw new IllegalArgumentException(
                        "Composition target reference must not be blank"
                );
            }
        }
    }
}
