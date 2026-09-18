package grandrue.commercial;

import grandrue.commercial.CommercialAccessDecision;
import grandrue.application.MerchantScope;
import grandrue.commercial.CommercialEntitlementAuthority;
import grandrue.commercial.CommercialEntitlementGrant;
import grandrue.commercial.CommercialEntitlementGrantAuthority;
import grandrue.commercial.CommercialEntitlementGrantProvenance;
import mainstreet.commercial.CommercialEntitlementIdentity;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Resolves current commercial entitlement from independent merchant-scoped
 * provenance-bearing grant sources without consulting Merchant Configuration
 * or branching on a plan name.
 */
public final class EffectiveCommercialEntitlementAuthority
        implements CommercialEntitlementAuthority {

    private final CommercialEntitlementGrantAuthority grantAuthority;
    private final Clock clock;

    /**
     * Compatibility constructor for already-materialised grant sets.
     * New durable runtime composition should provide a grant authority instead.
     */
    public EffectiveCommercialEntitlementAuthority(
            List<CommercialEntitlementGrant> grants,
            Clock clock
    ) {
        List<CommercialEntitlementGrant> copied = List.copyOf(
                Objects.requireNonNull(grants, "grants")
        );
        this.grantAuthority = (merchantScope, entitlementIdentity, instant) ->
                copied.stream()
                        .filter(grant -> grant.merchantScope().equals(merchantScope))
                        .filter(grant -> grant.entitlementIdentity().equals(entitlementIdentity))
                        .filter(grant -> grant.isEffectiveAt(instant))
                        .toList();
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public EffectiveCommercialEntitlementAuthority(
            CommercialEntitlementGrantAuthority grantAuthority,
            Clock clock
    ) {
        this.grantAuthority = Objects.requireNonNull(
                grantAuthority,
                "grantAuthority"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public boolean isEntitled(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity
    ) {
        return decisionFor(merchantScope, entitlementIdentity).permitted();
    }

    public CommercialAccessDecision decisionFor(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Instant now = clock.instant();
        List<CommercialEntitlementGrant> grants = Objects.requireNonNull(
                grantAuthority.effectiveGrants(
                        merchantScope,
                        entitlementIdentity,
                        now
                ),
                "Commercial grant authority returned null"
        );
        for (CommercialEntitlementGrant grant : grants) {
            if (!grant.merchantScope().equals(merchantScope)
                    || !grant.entitlementIdentity().equals(entitlementIdentity)
                    || !grant.isEffectiveAt(now)) {
                throw new IllegalStateException(
                        "Commercial grant authority returned a non-affine or ineffective grant"
                );
            }
        }
        List<CommercialEntitlementGrantProvenance> effectiveSources = grants.stream()
                .map(CommercialEntitlementGrant::provenance)
                .distinct()
                .toList();
        return new CommercialAccessDecision(
                entitlementIdentity,
                !effectiveSources.isEmpty(),
                effectiveSources,
                now
        );
    }
}
