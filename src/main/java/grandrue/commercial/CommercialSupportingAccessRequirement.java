package grandrue.commercial;

import mainstreet.commercial.CommercialAccessTarget;

import java.util.Objects;
import java.util.Set;

/**
 * Retained supporting-contract classification. An explicitly empty purpose set
 * represents a claimed no-independent-entitlement classification, never missing data.
 * The publisher must resolve and validate the supplied authority before admission.
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §§4–6 — Exact binding requirements; Binding identity and satisfaction; Manifest completeness.
 */
public record CommercialSupportingAccessRequirement(
        CommercialAccessTarget target,
        Set<String> requiredPurposes,
        String classificationAuthority
) {
    public CommercialSupportingAccessRequirement {
        Objects.requireNonNull(target, "target");
        requiredPurposes = Set.copyOf(Objects.requireNonNull(requiredPurposes, "requiredPurposes"));
        requiredPurposes.forEach(purpose -> CommercialAccessTarget.requireExactReference(purpose, "requiredPurpose"));
        CommercialAccessTarget.requireText(classificationAuthority, "classificationAuthority");
    }
}
