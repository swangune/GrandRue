package grandrue.merchantaccount;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable Merchant Account-owned evidence of when one Merchant Account was
 * authoritatively established.
 *
 * <p>The establishment instant is semantic evidence, not generic persistence
 * metadata. It may anchor accepted post-commit consequences but does not itself
 * create Commercial, Configuration, trust or publication state.</p>
 */
public record MerchantAccountEstablishment(
        String establishmentIdentity,
        MerchantScope merchantScope,
        String logicalEstablishmentRequestIdentity,
        Instant establishedAt
) {
    public MerchantAccountEstablishment {
        requireIdentifier(establishmentIdentity, "establishmentIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                logicalEstablishmentRequestIdentity,
                "logicalEstablishmentRequestIdentity"
        );
        Objects.requireNonNull(establishedAt, "establishedAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
