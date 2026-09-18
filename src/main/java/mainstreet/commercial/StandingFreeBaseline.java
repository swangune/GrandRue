package mainstreet.commercial;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Commercial-owned permanent Standing Free source for one Merchant Account.
 *
 * <p>The baseline is historically bound to the FREE plan revision effective at
 * Merchant Account establishment and retains that revision's explicit
 * entitlement snapshot. It is not a paid-style Merchant Commercial Agreement.</p>
 */
public record StandingFreeBaseline(
        String baselineIdentity,
        MerchantScope merchantScope,
        String originatingMerchantAccountEstablishmentIdentity,
        Instant effectiveFrom,
        String freePlanRevisionIdentity,
        Set<CommercialEntitlementIdentity> entitlementSnapshot
) {
    public static final String GRANT_SOURCE_CLASS = "standing-free-baseline";

    public StandingFreeBaseline {
        requireIdentifier(baselineIdentity, "baselineIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                originatingMerchantAccountEstablishmentIdentity,
                "originatingMerchantAccountEstablishmentIdentity"
        );
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        requireIdentifier(freePlanRevisionIdentity, "freePlanRevisionIdentity");
        entitlementSnapshot = Set.copyOf(
                Objects.requireNonNull(entitlementSnapshot, "entitlementSnapshot")
        );
    }

    public boolean grants(CommercialEntitlementIdentity entitlementIdentity) {
        return entitlementSnapshot.contains(
                Objects.requireNonNull(entitlementIdentity, "entitlementIdentity")
        );
    }

    public CommercialEntitlementGrant grantFor(
            CommercialEntitlementIdentity entitlementIdentity) {
        if (!grants(entitlementIdentity)) {
            throw new IllegalArgumentException(
                    "Standing Free baseline does not grant requested entitlement"
            );
        }
        return new CommercialEntitlementGrant(
                merchantScope,
                entitlementIdentity,
                new CommercialEntitlementGrantProvenance(
                        GRANT_SOURCE_CLASS,
                        baselineIdentity
                ),
                effectiveFrom,
                java.util.Optional.empty()
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
