package mainstreet.commercial;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Commercial grant source backed by the authoritative Standing Free baseline.
 */
public final class StandingFreeBaselineGrantAuthority
        implements CommercialEntitlementGrantAuthority {

    private final StandingFreeBaselineStore store;

    public StandingFreeBaselineGrantAuthority(StandingFreeBaselineStore store) {
        this.store = Objects.requireNonNull(store, "store");
    }

    @Override
    public List<CommercialEntitlementGrant> effectiveGrants(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity,
            Instant instant) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Objects.requireNonNull(instant, "instant");

        return store.baselineFor(merchantScope)
                .filter(baseline -> !instant.isBefore(baseline.effectiveFrom()))
                .filter(baseline -> baseline.grants(entitlementIdentity))
                .map(baseline -> List.of(baseline.grantFor(entitlementIdentity)))
                .orElseGet(List::of);
    }
}
