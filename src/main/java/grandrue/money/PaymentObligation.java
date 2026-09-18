package grandrue.money;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Authoritative GrandRue obligation; not a provider transaction. */
public record PaymentObligation(
        String obligationIdentity,
        MerchantScope merchantScope,
        String commercialSubjectReference,
        MonetaryAmount obligationAmount,
        String sourceCommercialCommitmentReference,
        String dueConditionIdentifier,
        String provenanceReference,
        Instant establishedAt
) {
    public PaymentObligation {
        require(obligationIdentity, "obligationIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(commercialSubjectReference, "commercialSubjectReference");
        Objects.requireNonNull(obligationAmount, "obligationAmount");
        require(sourceCommercialCommitmentReference, "sourceCommercialCommitmentReference");
        require(dueConditionIdentifier, "dueConditionIdentifier");
        require(provenanceReference, "provenanceReference");
        Objects.requireNonNull(establishedAt, "establishedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
