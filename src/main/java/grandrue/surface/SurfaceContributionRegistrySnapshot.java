package grandrue.surface;

import grandrue.semantic.executable.ExecutableMerchantModel;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable registry release of capability-owned surface contribution
 * definitions and their release-affined CUSTOMER eligibility requirement
 * definitions. Its release identifier must match the semantic release used by
 * the executable merchant model.
 */
public record SurfaceContributionRegistrySnapshot(
        String semanticRegistryReleaseIdentifier,
        Set<SurfaceContributionDefinition> definitions,
        Set<CustomerSurfaceEligibilityRequirementDefinition>
                customerEligibilityRequirementDefinitions
) {
    public SurfaceContributionRegistrySnapshot {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        definitions = Set.copyOf(Objects.requireNonNull(definitions, "definitions"));
        customerEligibilityRequirementDefinitions = Set.copyOf(
                Objects.requireNonNull(
                        customerEligibilityRequirementDefinitions,
                        "customerEligibilityRequirementDefinitions"
                )
        );
        requireUniqueIdentities(definitions);
        requireRegisteredCustomerEligibilityRequirements(
                definitions,
                customerEligibilityRequirementDefinitions
        );
    }

    public SurfaceContributionRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Set<SurfaceContributionDefinition> definitions
    ) {
        this(
                semanticRegistryReleaseIdentifier,
                definitions,
                Set.of()
        );
    }

    /**
     * Materialises only configuration-level applicable contributions. Actor
     * authority and other live eligibility/readiness inputs remain contextual.
     */
    public StaticSurfaceContributionCatalogue resolveFor(
            ExecutableMerchantModel model
    ) {
        Objects.requireNonNull(model, "model");
        if (!semanticRegistryReleaseIdentifier.equals(
                model.semanticRegistryVersion()
        )) {
            throw new IllegalArgumentException(
                    "Surface contribution registry release does not match executable model"
            );
        }

        List<StaticSurfaceContribution> applicable = definitions.stream()
                .filter(definition -> model.capabilityIdentifiers().contains(
                        definition.identity().ownerCapabilityIdentifier()
                ))
                .sorted(Comparator
                        .comparing((SurfaceContributionDefinition definition) ->
                                definition.identity().ownerCapabilityIdentifier())
                        .thenComparing(definition ->
                                definition.identity().contributionIdentifier()))
                .map(definition -> materialise(definition, model))
                .toList();

        return new StaticSurfaceContributionCatalogue(applicable);
    }

    private static StaticSurfaceContribution materialise(
            SurfaceContributionDefinition definition,
            ExecutableMerchantModel model
    ) {
        for (String operationReference : definition.supportedOperationReferences()) {
            if (model.operation(operationReference).isEmpty()) {
                throw new IllegalArgumentException(
                        "Surface contribution references an operation absent from "
                                + "the resolved model: "
                                + operationReference
                );
            }
        }

        String owner = definition.identity().ownerCapabilityIdentifier();
        return new StaticSurfaceContribution(
                definition.identity(),
                definition.audience(),
                definition.kind(),
                definition.compositionTargetReference(),
                definition.projectionRequirements(),
                definition.supportedOperationReferences(),
                Set.of(new ActiveCapabilitySurfaceEligibilityEvidence(owner)),
                definition.eligibilityContract(),
                definition.interactionAvailabilityContract(),
                definition.customerEligibilityRequirement()
        );
    }

    private static void requireUniqueIdentities(
            Set<SurfaceContributionDefinition> definitions
    ) {
        Set<SurfaceContributionIdentity> identities = new HashSet<>();
        for (SurfaceContributionDefinition definition : definitions) {
            if (!identities.add(definition.identity())) {
                throw new IllegalArgumentException(
                        "Duplicate surface contribution identity: "
                                + definition.identity()
                );
            }
        }
    }

    private static void requireRegisteredCustomerEligibilityRequirements(
            Set<SurfaceContributionDefinition> definitions,
            Set<CustomerSurfaceEligibilityRequirementDefinition>
                    requirementDefinitions
    ) {
        Set<CustomerSurfaceEligibilityRequirementIdentity> registered =
                requirementDefinitions.stream()
                        .map(CustomerSurfaceEligibilityRequirementDefinition::identity)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet());

        for (SurfaceContributionDefinition definition : definitions) {
            definition.customerEligibilityRequirement().ifPresent(requirement -> {
                if (!registered.contains(requirement)) {
                    throw new IllegalArgumentException(
                            "Surface contribution references an unregistered customer "
                                    + "eligibility requirement: "
                                    + requirement
                    );
                }
            });
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
