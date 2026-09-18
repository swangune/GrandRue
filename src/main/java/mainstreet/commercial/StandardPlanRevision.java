package mainstreet.commercial;

import grandrue.commercial.StandardPlanLevel;

import java.util.Objects;
import java.util.Set;

/**
 * One historically interpretable standard-plan commercial revision with an
 * explicit entitlement snapshot.
 *
 * <p>The entitlement set is resolved explicitly at publication time. This type
 * does not inherit live entitlements from another plan and does not define
 * merchant semantics.</p>
 */
public record StandardPlanRevision(
        StandardPlanLevel level,
        String revisionIdentifier,
        Set<CommercialEntitlementIdentity> entitlements
) {

    public StandardPlanRevision {
        Objects.requireNonNull(level, "level");
        if (revisionIdentifier == null || revisionIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Plan revision identifier must not be blank"
            );
        }
        entitlements = Set.copyOf(
                Objects.requireNonNull(entitlements, "entitlements")
        );
    }
}
