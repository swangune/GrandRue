package grandrue.commercial;

import grandrue.commercial.CommercialEntitlementGrantProvenance;
import mainstreet.commercial.StandardPlanRevision;

import grandrue.commercial.BillingCadence;
import grandrue.commercial.CommercialAcceptanceProvenance;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Commercial authority's durable merchant-specific binding to one exact plan
 * revision for one effective interval and billing cadence.
 *
 * <p>This record does not represent payment evidence, semantic configuration
 * or effective entitlement by itself. Governed by MS-PROT-056 v1.5.</p>
 */
public record MerchantCommercialAgreement(
        String commercialAgreementIdentity,
        MerchantScope merchantScope,
        StandardPlanRevision planRevision,
        BillingCadence billingCadence,
        Instant effectiveFrom,
        Optional<Instant> effectiveUntilExclusive,
        CommercialAcceptanceProvenance acceptanceProvenance
) {

    private static final String GRANT_SOURCE_CLASS =
            "merchant-commercial-agreement";

    public MerchantCommercialAgreement {
        if (commercialAgreementIdentity == null
                || commercialAgreementIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial agreement identity must not be blank"
            );
        }
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(planRevision, "planRevision");
        Objects.requireNonNull(billingCadence, "billingCadence");
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        effectiveUntilExclusive = Objects.requireNonNull(
                effectiveUntilExclusive,
                "effectiveUntilExclusive"
        );
        Objects.requireNonNull(acceptanceProvenance, "acceptanceProvenance");
        effectiveUntilExclusive.ifPresent(end -> {
            if (!end.isAfter(effectiveFrom)) {
                throw new IllegalArgumentException(
                        "Commercial agreement end must be after its start"
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

    public CommercialEntitlementGrantProvenance grantProvenance() {
        return new CommercialEntitlementGrantProvenance(
                GRANT_SOURCE_CLASS,
                commercialAgreementIdentity
        );
    }
}
