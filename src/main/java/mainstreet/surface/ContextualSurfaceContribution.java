package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Presentation-neutral contribution that survived current merchant-context
 * eligibility filtering. It retains evidence and current interaction
 * availability, but grants no operation authority.
 */
public record ContextualSurfaceContribution(
        SurfaceContributionIdentity identity,
        SurfaceAudience audience,
        SurfaceContributionKind kind,
        Optional<String> compositionTargetReference,
        Set<String> projectionRequirements,
        Set<String> supportedOperationReferences,
        SurfaceEligibilityContract eligibilityContract,
        SurfaceInteractionAvailabilityContract interactionAvailabilityContract,
        Set<StaticSurfaceEligibilityEvidence> staticEligibilityEvidence,
        Set<ContextualSurfaceEligibilityEvidence> contextualEligibilityEvidence,
        Optional<SurfaceInteractionAvailability> interactionAvailability
) {
    public ContextualSurfaceContribution {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(audience, "audience");
        Objects.requireNonNull(kind, "kind");
        compositionTargetReference = Objects.requireNonNull(
                compositionTargetReference,
                "compositionTargetReference"
        );
        projectionRequirements = Set.copyOf(
                Objects.requireNonNull(
                        projectionRequirements,
                        "projectionRequirements"
                )
        );
        supportedOperationReferences = Set.copyOf(
                Objects.requireNonNull(
                        supportedOperationReferences,
                        "supportedOperationReferences"
                )
        );
        Objects.requireNonNull(eligibilityContract, "eligibilityContract");
        Objects.requireNonNull(
                interactionAvailabilityContract,
                "interactionAvailabilityContract"
        );
        staticEligibilityEvidence = Set.copyOf(
                Objects.requireNonNull(
                        staticEligibilityEvidence,
                        "staticEligibilityEvidence"
                )
        );
        contextualEligibilityEvidence = Set.copyOf(
                Objects.requireNonNull(
                        contextualEligibilityEvidence,
                        "contextualEligibilityEvidence"
                )
        );
        interactionAvailability = Objects.requireNonNull(
                interactionAvailability,
                "interactionAvailability"
        );
        if (kind == SurfaceContributionKind.ACTION
                && interactionAvailability.isEmpty()) {
            throw new IllegalArgumentException(
                    "Contextually included ACTION requires interaction availability"
            );
        }
        if (kind != SurfaceContributionKind.ACTION
                && interactionAvailability.isPresent()) {
            throw new IllegalArgumentException(
                    "Only ACTION contributions have interaction availability"
            );
        }
    }

    public ContextualSurfaceContribution(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences,
            SurfaceEligibilityContract eligibilityContract,
            Set<StaticSurfaceEligibilityEvidence> staticEligibilityEvidence,
            Set<ContextualSurfaceEligibilityEvidence> contextualEligibilityEvidence
    ) {
        this(
                identity,
                audience,
                kind,
                compositionTargetReference,
                projectionRequirements,
                supportedOperationReferences,
                eligibilityContract,
                SurfaceInteractionAvailabilityContract.independent(),
                staticEligibilityEvidence,
                contextualEligibilityEvidence,
                kind == SurfaceContributionKind.ACTION
                        ? Optional.of(SurfaceInteractionAvailability.AVAILABLE)
                        : Optional.empty()
        );
    }

    static ContextualSurfaceContribution fromStatic(
            StaticSurfaceContribution contribution,
            Set<ContextualSurfaceEligibilityEvidence> contextualEvidence,
            Optional<SurfaceInteractionAvailability> interactionAvailability
    ) {
        Objects.requireNonNull(contribution, "contribution");
        return new ContextualSurfaceContribution(
                contribution.identity(),
                contribution.audience(),
                contribution.kind(),
                contribution.compositionTargetReference(),
                contribution.projectionRequirements(),
                contribution.supportedOperationReferences(),
                contribution.eligibilityContract(),
                contribution.interactionAvailabilityContract(),
                contribution.staticEligibilityEvidence(),
                contextualEvidence,
                interactionAvailability
        );
    }

    static ContextualSurfaceContribution fromResidual(
            SurfaceContributionDefinition definition,
            Set<ContextualSurfaceEligibilityEvidence> contextualEvidence,
            Optional<SurfaceInteractionAvailability> interactionAvailability
    ) {
        Objects.requireNonNull(definition, "definition");
        return new ContextualSurfaceContribution(
                definition.identity(),
                definition.audience(),
                definition.kind(),
                definition.compositionTargetReference(),
                definition.projectionRequirements(),
                definition.supportedOperationReferences(),
                definition.eligibilityContract(),
                definition.interactionAvailabilityContract(),
                Set.of(),
                contextualEvidence,
                interactionAvailability
        );
    }
}
