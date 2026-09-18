package grandrue.surface;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Platform-registered declarative surface contribution owned by one
 * capability. It references already-supported semantics and composition
 * inputs; it does not create operations, routes, components or authority.
 */
public record SurfaceContributionDefinition(
        SurfaceContributionIdentity identity,
        SurfaceAudience audience,
        SurfaceContributionKind kind,
        Optional<String> compositionTargetReference,
        Set<String> projectionRequirements,
        Set<String> supportedOperationReferences,
        SurfaceEligibilityContract eligibilityContract,
        SurfaceInteractionAvailabilityContract interactionAvailabilityContract,
        Optional<CustomerSurfaceEligibilityRequirementIdentity>
                customerEligibilityRequirement
) {
    public SurfaceContributionDefinition {
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
        if (eligibilityContract.allowsResidualManagement()
                && audience != SurfaceAudience.MERCHANT) {
            throw new IllegalArgumentException(
                    "Residual-management surface eligibility is merchant-only"
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

    public SurfaceContributionDefinition(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences,
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
                eligibilityContract,
                interactionAvailabilityContract,
                Optional.empty()
        );
    }

    public SurfaceContributionDefinition(
            SurfaceContributionIdentity identity,
            SurfaceAudience audience,
            SurfaceContributionKind kind,
            Optional<String> compositionTargetReference,
            Set<String> projectionRequirements,
            Set<String> supportedOperationReferences,
            SurfaceEligibilityContract eligibilityContract
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
                Optional.empty()
        );
    }

    public SurfaceContributionDefinition(
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
