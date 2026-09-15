package mainstreet.money;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Authenticated/correlated external payment execution evidence. Provider result
 * vocabulary remains evidence and is not copied into a Main Street lifecycle.
 */
public record ProviderPaymentEvidence(
        String evidenceIdentity,
        MerchantScope merchantScope,
        String providerIdentifier,
        String providerTransactionReference,
        String mainStreetCorrelationIdentity,
        MonetaryAmount amount,
        String paymentMethodCategory,
        String providerResultCategory,
        Instant observedAt,
        Optional<String> receiptEvidenceReference
) {
    public ProviderPaymentEvidence {
        require(evidenceIdentity, "evidenceIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(providerIdentifier, "providerIdentifier");
        require(providerTransactionReference, "providerTransactionReference");
        require(mainStreetCorrelationIdentity, "mainStreetCorrelationIdentity");
        Objects.requireNonNull(amount, "amount");
        require(paymentMethodCategory, "paymentMethodCategory");
        require(providerResultCategory, "providerResultCategory");
        Objects.requireNonNull(observedAt, "observedAt");
        receiptEvidenceReference = Objects.requireNonNull(
                receiptEvidenceReference,
                "receiptEvidenceReference"
        );
        receiptEvidenceReference.ifPresent(value -> require(value, "receiptEvidenceReference"));
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
