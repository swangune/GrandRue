package grandrue.commercial;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerchantCommercialAgreementTransitionTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant T0 = Instant.parse("2026-08-24T05:15:00Z");

    @Test
    void binds_one_logical_request_to_expected_head_and_candidate_agreement() {
        MerchantCommercialAgreement agreement = agreement("agreement-1", "business-v7");

        MerchantCommercialAgreementTransition transition =
                new MerchantCommercialAgreementTransition(
                        "commercial-request-1",
                        Optional.of("agreement-0"),
                        agreement
                );

        assertEquals("commercial-request-1", transition.logicalRequestIdentity());
        assertEquals(Optional.of("agreement-0"), transition.expectedCurrentAgreementIdentity());
        assertEquals(agreement, transition.candidateAgreement());
        assertEquals(MERCHANT, transition.merchantScope());
    }

    @Test
    void rejects_blank_logical_request_identity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantCommercialAgreementTransition(
                        " ",
                        Optional.empty(),
                        agreement("agreement-1", "business-v7")
                )
        );
    }

    @Test
    void standing_free_baseline_cannot_be_represented_as_paid_agreement_transition() {
        MerchantCommercialAgreement freeAgreement = new MerchantCommercialAgreement(
                "agreement-free",
                MERCHANT,
                new StandardPlanRevision(
                        StandardPlanLevel.FREE,
                        "free-v3",
                        Set.of(new CommercialEntitlementIdentity("catalogue.basic"))
                ),
                BillingCadence.MONTHLY,
                T0,
                Optional.empty(),
                new CommercialAcceptanceProvenance("acceptance-free")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantCommercialAgreementTransition(
                        "commercial-request-free",
                        Optional.empty(),
                        freeAgreement
                )
        );
    }

    private static MerchantCommercialAgreement agreement(
            String agreementIdentity,
            String planRevisionIdentity
    ) {
        return new MerchantCommercialAgreement(
                agreementIdentity,
                MERCHANT,
                new StandardPlanRevision(
                        StandardPlanLevel.BUSINESS,
                        planRevisionIdentity,
                        Set.of(
                                new CommercialEntitlementIdentity("booking.new-activity"),
                                new CommercialEntitlementIdentity("analytics.advanced")
                        )
                ),
                BillingCadence.MONTHLY,
                T0,
                Optional.empty(),
                new CommercialAcceptanceProvenance("acceptance-1")
        );
    }
}
