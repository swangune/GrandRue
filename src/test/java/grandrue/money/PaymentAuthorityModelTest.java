package grandrue.money;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PaymentAuthorityModelTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final CurrencyIdentity GBP = new CurrencyIdentity("GBP");
    private static final Instant T0 = Instant.parse("2026-08-24T16:00:00Z");

    @Test
    void provider_evidence_does_not_redefine_payment_obligation() {
        PaymentObligation obligation = obligation("obligation-1", 10_000);
        ProviderPaymentEvidence evidence = evidence("evidence-1", 4_000);

        assertEquals(BigInteger.valueOf(10_000), obligation.obligationAmount().minorUnitAmount());
        assertEquals(BigInteger.valueOf(4_000), evidence.amount().minorUnitAmount());
        assertNotEquals(obligation.obligationAmount(), evidence.amount());
    }

    @Test
    void payment_application_is_a_separate_interpretation_fact() {
        PaymentApplication application = new PaymentApplication(
                "application-1",
                MERCHANT,
                "obligation-1",
                "evidence-1",
                amount(4_000),
                T0.plusSeconds(2),
                "reconciliation-v1"
        );

        assertEquals("obligation-1", application.obligationIdentity());
        assertEquals("evidence-1", application.paymentEvidenceIdentity());
        assertEquals(BigInteger.valueOf(4_000), application.appliedAmount().minorUnitAmount());
    }

    private static PaymentObligation obligation(String identity, long amount) {
        return new PaymentObligation(
                identity,
                MERCHANT,
                "commercial-subject-1",
                amount(amount),
                "commitment-1",
                "DUE_NOW",
                "commercial-policy-v1",
                T0
        );
    }

    private static ProviderPaymentEvidence evidence(String identity, long amount) {
        return new ProviderPaymentEvidence(
                identity,
                MERCHANT,
                "provider-a",
                "provider-tx-1",
                "payment-request-1",
                amount(amount),
                "CARD",
                "CAPTURED",
                T0.plusSeconds(1),
                Optional.empty()
        );
    }

    private static MonetaryAmount amount(long minorUnits) {
        return new MonetaryAmount(GBP, BigInteger.valueOf(minorUnits));
    }
}
