package mainstreet.commercial;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantCommercialAgreementTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-1");
    private static final Instant START = Instant.parse("2026-08-23T12:00:00Z");
    private static final Instant END = Instant.parse("2026-09-23T12:00:00Z");
    private static final CommercialAcceptanceProvenance ACCEPTANCE =
            new CommercialAcceptanceProvenance("acceptance-1");

    @Test
    void binds_merchant_to_one_exact_plan_revision_and_billing_cadence() {
        StandardPlanRevision businessV7 = new StandardPlanRevision(
                StandardPlanLevel.BUSINESS,
                "business-v7",
                Set.of(new CommercialEntitlementIdentity("booking.new-activity"))
        );
        MerchantCommercialAgreement agreement = new MerchantCommercialAgreement(
                "agreement-17",
                MERCHANT,
                businessV7,
                BillingCadence.MONTHLY,
                START,
                Optional.of(END),
                ACCEPTANCE
        );

        assertEquals("agreement-17", agreement.commercialAgreementIdentity());
        assertEquals(MERCHANT, agreement.merchantScope());
        assertEquals(businessV7, agreement.planRevision());
        assertEquals(BillingCadence.MONTHLY, agreement.billingCadence());
        assertEquals(ACCEPTANCE, agreement.acceptanceProvenance());
        assertEquals(
                new CommercialEntitlementGrantProvenance(
                        "merchant-commercial-agreement",
                        "agreement-17"
                ),
                agreement.grantProvenance()
        );
    }

    @Test
    void effectiveness_uses_half_open_interval() {
        MerchantCommercialAgreement agreement = agreement(
                Optional.of(END)
        );

        assertFalse(agreement.isEffectiveAt(START.minusNanos(1)));
        assertTrue(agreement.isEffectiveAt(START));
        assertTrue(agreement.isEffectiveAt(END.minusNanos(1)));
        assertFalse(agreement.isEffectiveAt(END));
    }

    @Test
    void open_ended_agreement_remains_effective_after_start() {
        MerchantCommercialAgreement agreement = agreement(Optional.empty());

        assertTrue(agreement.isEffectiveAt(START));
        assertTrue(agreement.isEffectiveAt(START.plusSeconds(10_000_000)));
    }

    @Test
    void rejects_invalid_identity_and_interval() {
        StandardPlanRevision plan = plan();

        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantCommercialAgreement(
                        " ",
                        MERCHANT,
                        plan,
                        BillingCadence.ANNUAL,
                        START,
                        Optional.empty(),
                        ACCEPTANCE
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantCommercialAgreement(
                        "agreement-1",
                        MERCHANT,
                        plan,
                        BillingCadence.ANNUAL,
                        START,
                        Optional.of(START),
                        ACCEPTANCE
                )
        );
    }

    private static MerchantCommercialAgreement agreement(
            Optional<Instant> end
    ) {
        return new MerchantCommercialAgreement(
                "agreement-17",
                MERCHANT,
                plan(),
                BillingCadence.ANNUAL,
                START,
                end,
                ACCEPTANCE
        );
    }

    private static StandardPlanRevision plan() {
        return new StandardPlanRevision(
                StandardPlanLevel.BUSINESS,
                "business-v7",
                Set.of(new CommercialEntitlementIdentity("booking.new-activity"))
        );
    }
}
