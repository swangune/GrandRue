package grandrue.commercial;

import mainstreet.commercial.StandardPlanLevel;
import mainstreet.commercial.StandardPlanRevision;

import java.util.Objects;
import java.util.Set;

/**
 * One publication generation of the standard FREE/BUSINESS/GROWTH catalogue.
 *
 * <p>Monotonicity is validated when the catalogue revision is constructed.
 * Each plan nevertheless retains its own explicit entitlement snapshot, so
 * historical interpretation never depends on mutable live inheritance.</p>
 *
 * <p>Plan identity cardinality: MS-PROT-056 v1.9 — Commercial Catalogue Binding,
 * Publication & Historical Resolution Amendment, §6 — Manifest completeness.</p>
 */
public record StandardPlanCatalogueRevision(
        String catalogueRevisionIdentifier,
        StandardPlanRevision freePlan,
        StandardPlanRevision businessPlan,
        StandardPlanRevision growthPlan
) {

    public StandardPlanCatalogueRevision {
        if (catalogueRevisionIdentifier == null
                || catalogueRevisionIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Catalogue revision identifier must not be blank"
            );
        }
        Objects.requireNonNull(freePlan, "freePlan");
        Objects.requireNonNull(businessPlan, "businessPlan");
        Objects.requireNonNull(growthPlan, "growthPlan");
        requireLevel(freePlan, StandardPlanLevel.FREE, "freePlan");
        requireLevel(businessPlan, StandardPlanLevel.BUSINESS, "businessPlan");
        requireLevel(growthPlan, StandardPlanLevel.GROWTH, "growthPlan");
        if (freePlan.revisionIdentifier().equals(businessPlan.revisionIdentifier())
                || freePlan.revisionIdentifier().equals(growthPlan.revisionIdentifier())
                || businessPlan.revisionIdentifier().equals(growthPlan.revisionIdentifier())) {
            throw new IllegalArgumentException("Catalogue requires three distinct plan revision identities");
        }
        requireSuperset(
                businessPlan.entitlements(),
                freePlan.entitlements(),
                "BUSINESS must include all FREE entitlements"
        );
        requireSuperset(
                growthPlan.entitlements(),
                businessPlan.entitlements(),
                "GROWTH must include all BUSINESS entitlements"
        );
    }

    private static void requireLevel(
            StandardPlanRevision revision,
            StandardPlanLevel expected,
            String slot
    ) {
        if (revision.level() != expected) {
            throw new IllegalArgumentException(
                    slot + " requires " + expected + " revision"
            );
        }
    }

    private static void requireSuperset(
            Set<CommercialEntitlementIdentity> higher,
            Set<CommercialEntitlementIdentity> lower,
            String message
    ) {
        if (!higher.containsAll(lower)) {
            throw new IllegalArgumentException(message);
        }
    }
}
