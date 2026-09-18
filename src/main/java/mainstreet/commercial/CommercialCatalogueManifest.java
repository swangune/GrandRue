package mainstreet.commercial;

import grandrue.commercial.StandardPlanCatalogueRevision;

import grandrue.commercial.CommercialAccessBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable proposed manifest with structural completeness checks. Each referenced
 * binding is retained in full, including reused immutable definitions.
 *
 * <p>Construction does not authenticate approval, resolve external authority,
 * establish allocation conformance, publish a generation or grant runtime access.
 * The publisher must validate the evidence and exact-content approval affinity
 * at its trusted boundary; a nonblank evidence reference is not proof.</p>
 *
 * <p>MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §5 — Binding identity and satisfaction; §6 — Manifest completeness;
 * §8 — Publication operation; §13 — Retention, recovery and runtime boundaries.</p>
 */
public record CommercialCatalogueManifest(
        StandardPlanCatalogueRevision revision,
        Set<CommercialAccessBinding> bindings,
        Set<String> allocationConformanceEvidence,
        String approvalProvenanceReference
) {
    public CommercialCatalogueManifest {
        Objects.requireNonNull(revision, "revision");
        bindings = Set.copyOf(Objects.requireNonNull(bindings, "bindings"));
        allocationConformanceEvidence = Set.copyOf(
                Objects.requireNonNull(allocationConformanceEvidence, "allocationConformanceEvidence"));
        if (allocationConformanceEvidence.isEmpty()) {
            throw new IllegalArgumentException("Manifest requires allocation conformance evidence");
        }
        allocationConformanceEvidence.forEach(reference ->
                CommercialAccessTarget.requireText(reference, "allocationConformanceEvidence"));
        CommercialAccessTarget.requireText(approvalProvenanceReference, "approvalProvenanceReference");

        var byIdentity = indexIdentities(bindings);
        var byPurpose = indexPurposes(bindings);
        if (!byIdentity.keySet().containsAll(revision.growthPlan().entitlements())) {
            throw new IllegalArgumentException("Every grant requires its exact retained binding");
        }
        requireSupportingAccess(revision, bindings, byPurpose);
    }

    /**
     * Check against one retained generation, not merely the current predecessor.
     * A publisher must apply this to all retained generations before appending.
     * This is content validation, not publication request idempotency or authorisation.
     */
    public void requireCompatibleWith(CommercialCatalogueManifest retained) {
        Objects.requireNonNull(retained, "retained");
        if (revision.catalogueRevisionIdentifier().equals(retained.revision.catalogueRevisionIdentifier())
                && !equals(retained)) {
            throw new IllegalArgumentException("Catalogue identity already has different manifest content");
        }
        var oldIdentities = indexIdentities(retained.bindings);
        var oldPurposes = indexPurposes(retained.bindings);
        for (var binding : bindings) {
            var oldBinding = oldIdentities.get(binding.entitlementIdentity());
            if (oldBinding != null && !oldBinding.equals(binding)) {
                throw new IllegalArgumentException("Entitlement identity already has different binding content");
            }
            var canonical = oldPurposes.get(purposeOf(binding));
            if (canonical != null && !canonical.entitlementIdentity().equals(binding.entitlementIdentity())) {
                throw new IllegalArgumentException("Exact target and purpose already have a canonical entitlement");
            }
        }
        for (var plan : plans(revision)) {
            for (var oldPlan : plans(retained.revision)) {
                if (plan.revisionIdentifier().equals(oldPlan.revisionIdentifier()) && !plan.equals(oldPlan)) {
                    throw new IllegalArgumentException("Plan identity already has different content");
                }
            }
        }
    }

    private static void requireSupportingAccess(StandardPlanCatalogueRevision revision,
                                               Set<CommercialAccessBinding> bindings,
                                               Map<Purpose, CommercialAccessBinding> byPurpose) {
        for (var binding : bindings) {
            for (var support : binding.supportingAccessRequirements()) {
                if (support.requiredPurposes().isEmpty() && byPurpose.keySet().stream()
                        .anyMatch(purpose -> purpose.target().equals(support.target()))) {
                    throw new IllegalArgumentException("Supporting classification conflicts with a protected target");
                }
                for (var purpose : support.requiredPurposes()) {
                    var required = byPurpose.get(new Purpose(support.target(), purpose));
                    if (required == null) {
                        throw new IllegalArgumentException("Supporting purpose has no exact retained binding");
                    }
                    for (var plan : plans(revision)) {
                        if (plan.entitlements().contains(binding.entitlementIdentity())
                                && !plan.entitlements().contains(required.entitlementIdentity())) {
                            throw new IllegalArgumentException("Granted service lacks required support in the same plan");
                        }
                    }
                }
            }
        }
    }

    private static Map<CommercialEntitlementIdentity, CommercialAccessBinding> indexIdentities(
            Set<CommercialAccessBinding> bindings) {
        Map<CommercialEntitlementIdentity, CommercialAccessBinding> result = new HashMap<>();
        for (var binding : bindings) {
            if (result.putIfAbsent(binding.entitlementIdentity(), binding) != null) {
                throw new IllegalArgumentException("Entitlement identity has multiple bindings");
            }
        }
        return result;
    }

    private static Map<Purpose, CommercialAccessBinding> indexPurposes(Set<CommercialAccessBinding> bindings) {
        Map<Purpose, CommercialAccessBinding> result = new HashMap<>();
        for (var binding : bindings) {
            if (result.putIfAbsent(purposeOf(binding), binding) != null) {
                throw new IllegalArgumentException("Exact target and purpose have multiple entitlement identities");
            }
        }
        return result;
    }

    private static Purpose purposeOf(CommercialAccessBinding binding) {
        return new Purpose(binding.target(), binding.protectedPurpose());
    }

    private static List<StandardPlanRevision> plans(StandardPlanCatalogueRevision revision) {
        return List.of(revision.freePlan(), revision.businessPlan(), revision.growthPlan());
    }

    private record Purpose(CommercialAccessTarget target, String purpose) { }
}
