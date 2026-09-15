package mainstreet.commercial;

import java.util.Objects;
import java.util.Set;

/**
 * Immutable binding evidence, not target registration or runtime permission.
 * Authority references must still be resolved and their meaning checked at publication.
 * MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md,
 * §§4–5 — Exact binding requirements; Binding identity and satisfaction.
 */
public record CommercialAccessBinding(
        CommercialEntitlementIdentity entitlementIdentity,
        CommercialEntitlementTargetKind targetKind,
        CommercialAccessTarget target,
        String protectedPurpose,
        String governingAuthority,
        Set<CommercialSupportingAccessRequirement> supportingAccessRequirements,
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
        CommercialAccessTarget.requireText(newUseAndResidualBoundaryAuthority, "newUseAndResidualBoundaryAuthority");
    }
}
