package mainstreet.commercial;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * One merchant-scoped commercial permission source for one entitlement.
 *
 * <p>A grant contributes commercial permission only while its effective
 * window contains the evaluation instant. It does not create semantic
 * applicability or capability configuration.</p>
 *
 * <p>Every grant retains the authoritative commercial-source provenance
 * required to reconstruct why the permission existed for its interval.</p>
 */
public record CommercialEntitlementGrant(
        MerchantScope merchantScope,
        CommercialEntitlementIdentity entitlementIdentity,
        CommercialEntitlementGrantProvenance provenance,
        Instant effectiveFrom,
        Optional<Instant> effectiveUntilExclusive
) {

    public CommercialEntitlementGrant {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Objects.requireNonNull(provenance, "provenance");
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        effectiveUntilExclusive = Objects.requireNonNull(
                effectiveUntilExclusive,
                "effectiveUntilExclusive"
        );
        effectiveUntilExclusive.ifPresent(end -> {
            if (!end.isAfter(effectiveFrom)) {
                throw new IllegalArgumentException(
                        "Entitlement grant end must be after its start"
                );
            }
        });
    }

    public boolean isEffectiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        if (instant.isBefore(effectiveFrom)) {
            return false;
        }
        return effectiveUntilExclusive
                .map(instant::isBefore)
                .orElse(true);
    }
}
