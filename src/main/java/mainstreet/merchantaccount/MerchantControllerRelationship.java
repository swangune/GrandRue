package mainstreet.merchantaccount;

import mainstreet.application.MerchantScope;

import java.util.Objects;

/** Main Street control relationship; it does not assert legal ownership. */
public record MerchantControllerRelationship(
        String relationshipIdentifier,
        MerchantScope merchantScope,
        String identityIdentifier,
        MerchantControllerRelationshipLifecycle lifecycle
) {
    public MerchantControllerRelationship {
        requireIdentifier(relationshipIdentifier, "Controller relationship identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identityIdentifier, "Controller identity");
        Objects.requireNonNull(lifecycle, "lifecycle");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
