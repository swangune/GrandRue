package grandrue.commercial;

import grandrue.application.MerchantScope;
import grandrue.commercial.CommercialEntitlementGrant;
import grandrue.commercial.CommercialEntitlementGrantAuthority;
import mainstreet.commercial.CommercialEntitlementIdentity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Commercial-only composition of independently authoritative grant sources.
 *
 * <p>This class concatenates provenance-bearing Commercial grants. It does not
 * decide semantic applicability, Actor Authorisation, trust, operational
 * eligibility or provider readiness.</p>
 */
public final class CompositeCommercialEntitlementGrantAuthority
        implements CommercialEntitlementGrantAuthority {

    private final List<CommercialEntitlementGrantAuthority> sources;

    public CompositeCommercialEntitlementGrantAuthority(
            List<CommercialEntitlementGrantAuthority> sources
    ) {
        this.sources = List.copyOf(Objects.requireNonNull(sources, "sources"));
        if (this.sources.isEmpty()) {
            throw new IllegalArgumentException(
                    "Commercial grant-source composition requires at least one source"
            );
        }
    }

    @Override
    public List<CommercialEntitlementGrant> effectiveGrants(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity,
            Instant instant
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Objects.requireNonNull(instant, "instant");

        List<CommercialEntitlementGrant> grants = new ArrayList<>();
        for (CommercialEntitlementGrantAuthority source : sources) {
            List<CommercialEntitlementGrant> resolved = Objects.requireNonNull(
                    source.effectiveGrants(
                            merchantScope,
                            entitlementIdentity,
                            instant
                    ),
                    "Commercial grant source returned null"
            );
            for (CommercialEntitlementGrant grant : resolved) {
                requireAffinity(grant, merchantScope, entitlementIdentity, instant);
                grants.add(grant);
            }
        }
        grants.sort(Comparator
                .comparing((CommercialEntitlementGrant grant) ->
                        grant.provenance().sourceClassIdentifier())
                .thenComparing(grant -> grant.provenance().sourceIdentifier()));
        return List.copyOf(grants);
    }

    private static void requireAffinity(
            CommercialEntitlementGrant grant,
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity,
            Instant instant
    ) {
        Objects.requireNonNull(grant, "commercial grant");
        if (!grant.merchantScope().equals(merchantScope)) {
            throw new IllegalStateException(
                    "Commercial grant source returned another Merchant Scope"
            );
        }
        if (!grant.entitlementIdentity().equals(entitlementIdentity)) {
            throw new IllegalStateException(
                    "Commercial grant source returned another entitlement identity"
            );
        }
        if (!grant.isEffectiveAt(instant)) {
            throw new IllegalStateException(
                    "Commercial grant source returned a grant outside its effective interval"
            );
        }
    }
}
