package mainstreet.commercial;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Resolves one immutable standard-plan revision into merchant-scoped commercial
 * entitlement grants for a supplied effective window and authoritative
 * commercial source.
 *
 * <p>The plan revision's explicit entitlement snapshot is the only source of
 * granted entitlement identities. The supplied provenance identifies why this
 * merchant receives those grants; a plan revision alone is not merchant-
 * specific commercial authority.</p>
 */
public final class StandardPlanEntitlementGrantSource {

    public List<CommercialEntitlementGrant> grantsFor(
            MerchantScope merchantScope,
            StandardPlanRevision planRevision,
            CommercialEntitlementGrantProvenance provenance,
            Instant effectiveFrom,
            Optional<Instant> effectiveUntilExclusive
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(planRevision, "planRevision");
        Objects.requireNonNull(provenance, "provenance");
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        Optional<Instant> end = Objects.requireNonNull(
                effectiveUntilExclusive,
                "effectiveUntilExclusive"
        );

        return planRevision.entitlements().stream()
                .sorted(Comparator.comparing(
                        CommercialEntitlementIdentity::identifier
                ))
                .map(identity -> new CommercialEntitlementGrant(
                        merchantScope,
                        identity,
                        provenance,
                        effectiveFrom,
                        end
                ))
                .toList();
    }
}
