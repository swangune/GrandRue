package grandrue.commercial;

import java.util.Objects;
import java.util.Set;

/**
 * Immutable binding evidence, not target registration or runtime permission.
 * Authority references must still be resolved and their meaning checked at publication.
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §§4–5 — Exact binding requirements; Binding identity and satisfaction.
 * MS-PROT-056 v1.10 §§11–15 adds owner-qualified conditional supporting requirements.
 */
public record CommercialAccessBinding(
        CommercialEntitlementIdentity entitlementIdentity,
        CommercialEntitlementTargetKind targetKind,
        CommercialAccessTarget target,
        String protectedPurpose,
        String governingAuthority,
        Set<CommercialSupportingAccessRequirement> supportingAccessRequirements,
        Set<CommercialConditionalSupportingAccessRequirement> conditionalSupportingAccessRequirements,
        String newUseAndResidualBoundaryAuthority
) {
    public CommercialAccessBinding {
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Objects.requireNonNull(targetKind, "targetKind");
        Objects.requireNonNull(target, "target");
        CommercialAccessTarget.requireExactReference(protectedPurpose, "protectedPurpose");
        CommercialAccessTarget.requireText(governingAuthority, "governingAuthority");
        supportingAccessRequirements = Set.copyOf(
                Objects.requireNonNull(supportingAccessRequirements, "supportingAccessRequirements"));
        conditionalSupportingAccessRequirements = Set.copyOf(
                Objects.requireNonNull(
                        conditionalSupportingAccessRequirements,
                        "conditionalSupportingAccessRequirements"));
        CommercialAccessTarget.requireText(
                newUseAndResidualBoundaryAuthority,
                "newUseAndResidualBoundaryAuthority");
    }

    public CommercialAccessBinding(
            CommercialEntitlementIdentity entitlementIdentity,
            CommercialEntitlementTargetKind targetKind,
            CommercialAccessTarget target,
            String protectedPurpose,
            String governingAuthority,
            Set<CommercialSupportingAccessRequirement> supportingAccessRequirements,
            String newUseAndResidualBoundaryAuthority
    ) {
        this(
                entitlementIdentity,
                targetKind,
                target,
                protectedPurpose,
                governingAuthority,
                supportingAccessRequirements,
                Set.of(),
                newUseAndResidualBoundaryAuthority
        );
    }
}
