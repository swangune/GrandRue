package grandrue.money;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * GrandRue interpretation that a bounded amount from one provider evidence
 * item is applied to one payment obligation. It does not rewrite either source.
 */
public record PaymentApplication(
        String applicationIdentity,
        MerchantScope merchantScope,
        String obligationIdentity,
        String paymentEvidenceIdentity,
        MonetaryAmount appliedAmount,
        Instant appliedAt,
        String provenanceReference
) {
    public PaymentApplication {
        require(applicationIdentity, "applicationIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(obligationIdentity, "obligationIdentity");
        require(paymentEvidenceIdentity, "paymentEvidenceIdentity");
        Objects.requireNonNull(appliedAmount, "appliedAmount");
        Objects.requireNonNull(appliedAt, "appliedAt");
        require(provenanceReference, "provenanceReference");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
