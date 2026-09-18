package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Merchant-authored or merchant-approved public descriptive information.
 *
 * <p>MS-PROT-051 keeps this concept separate from legal identity, trust claims,
 * classification-driven behaviour and executable capability configuration.</p>
 */
public record MerchantPublicDescriptor(
        MerchantScope merchantScope,
        String displayName,
        Optional<String> tagline,
        Optional<String> shortSummary,
        Optional<String> approvedDescription
) {

    public MerchantPublicDescriptor {
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(displayName, "displayName");
        Objects.requireNonNull(tagline, "tagline");
        Objects.requireNonNull(shortSummary, "shortSummary");
        Objects.requireNonNull(approvedDescription, "approvedDescription");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
