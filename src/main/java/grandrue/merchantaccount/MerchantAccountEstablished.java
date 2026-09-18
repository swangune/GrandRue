package grandrue.merchantaccount;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Event statement that an authoritative Merchant Account establishment
 * fact has already committed.
 */
public record MerchantAccountEstablished(
        String establishmentIdentity,
        MerchantScope merchantScope,
        String logicalEstablishmentRequestIdentity,
        Instant occurredAt
) {
    public MerchantAccountEstablished {
        requireIdentifier(establishmentIdentity, "establishmentIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                logicalEstablishmentRequestIdentity,
                "logicalEstablishmentRequestIdentity"
        );
        Objects.requireNonNull(occurredAt, "occurredAt");
    }

    public static MerchantAccountEstablished from(
            MerchantAccountEstablishment establishment
    ) {
        Objects.requireNonNull(establishment, "establishment");
        return new MerchantAccountEstablished(
                establishment.establishmentIdentity(),
                establishment.merchantScope(),
                establishment.logicalEstablishmentRequestIdentity(),
                establishment.establishedAt()
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
