package grandrue.surface;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * One statically applicable declarative contribution. It contains composition
 * inputs and references only; no route, frontend component or execution
 * authority is embedded here. Provider-readiness and customer-relationship
 * dependencies remain static references; current decisions are never stored
 * here.
 */
public record StaticSurfaceContribution(
        SurfaceContributionIdentity identity,
        SurfaceAudience audience,
        SurfaceContributionKind kind,
        Optional<String> compositionTargetReference,
        Set<String> projectionRequirements,
        Set<String> supportedOperationReferences,
        Set<StaticSurfaceEligibilityEvidence> staticEligibilityEvidence,
        SurfaceEligibilityContract eligibilityContract,
        SurfaceInteractionAvailabilityContract interactionAvailabilityContract,
        Optional<CustomerSurfaceEligibilityRequirementIdentity>
                customerEligibilityRequirement
) {
    public StaticSurfaceContribution {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(audience, "audience");
        Objects.requireNonNull(kind, "kind");
        compositionTargetReference = Objects.requireNonNull(
                compositionTargetReference,
                "compositionTargetReference"
        );
        compositionTargetReference.ifPresent(value ->
                requireIdentifier(value, "Composition target reference"));
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
        staticEligibilityEvidence = Set.copyOf(
                Objects.requireNonNull(
                        staticEligibilityEvidence,
                        "staticEligibilityEvidence"
                )
        );
        Objects.requireNonNull(eligibilityContract, "eligibilityContract");
        Objects.requireNonNull(
                interactionAvailabilityContract,
                "interactionAvailabilityContract"
        );
        customerEligibilityRequirement = Objects.requireNonNull(
                customerEligibilityRequirement,
                "customerEligibilityRequirement"
        );
        if (customerEligibilityRequirement.isPresent()
                && audience != SurfaceAudience.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Customer surface eligibility requirement is CUSTOMER-only"
            );
        }
        if (interactionAvailabilityContract.providerReadinessDependent()
                && (audience != SurfaceAudience.MERCHANT
                || kind != SurfaceContributionKind.ACTION)) {
            throw new IllegalArgumentException(
                    "Fulfilment-role interaction availability is limited to MERCHANT ACTION contributions"
            );
        }
        projectionRequirements.forEach(value ->
                requireIdentifier(value, "Projection requirement"));
        supportedOperationReferences.forEach(value ->
                requireIdentifier(value, "Supported operation reference"));
    }

    public StaticSurfaceContribution(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences,
            Set<StaticSurfaceEligibilityEvidence> staticEligibilityEvidence,
            SurfaceEligibilityContract eligibilityContract,
            SurfaceInteractionAvailabilityContract interactionAvailabilityContract
    ) {
        this(
                identity,
                audience,
                kind,
                compositionTargetReference,
                projectionRequirements,
                supportedOperationReferences,
                staticEligibilityEvidence,
                eligibilityContract,
                interactionAvailabilityContract,
                Optional.empty()
        );
    }

    public StaticSurfaceContribution(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences,
            Set<StaticSurfaceEligibilityEvidence> staticEligibilityEvidence,
            SurfaceEligibilityContract eligibilityContract
    ) {
        this(
                identity,
                audience,
                kind,
                compositionTargetReference,
                projectionRequirements,
                supportedOperationReferences,
                staticEligibilityEvidence,
                eligibilityContract,
                SurfaceInteractionAvailabilityContract.independent(),
                Optional.empty()
        );
    }

    public StaticSurfaceContribution(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences,
            Set<StaticSurfaceEligibilityEvidence> staticEligibilityEvidence
    ) {
        this(
                identity,
                audience,
                kind,
                compositionTargetReference,
                projectionRequirements,
                supportedOperationReferences,
                staticEligibilityEvidence,
                SurfaceEligibilityContract.activeOnly(),
                SurfaceInteractionAvailabilityContract.independent(),
                Optional.empty()
        );
    }

    public StaticSurfaceContribution(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences
    ) {
        this(
                identity,
                audience,
                kind,
                compositionTargetReference,
                projectionRequirements,
                supportedOperationReferences,
                Set.of(),
                SurfaceEligibilityContract.activeOnly(),
                SurfaceInteractionAvailabilityContract.independent(),
                Optional.empty()
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
