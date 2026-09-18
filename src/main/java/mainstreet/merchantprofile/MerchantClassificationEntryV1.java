package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantClassificationExposure;

import grandrue.merchantprofile.MerchantClassificationKind;

import java.text.Normalizer;
import java.util.Objects;

/** Versioned, merchant-approved and strictly non-executable classification value. */
public record MerchantClassificationEntryV1(
        MerchantClassificationKind kind,
        String merchantApprovedLabel,
        MerchantClassificationExposure exposure
) {
    public static final String SCHEMA_IDENTITY =
            "MS_MERCHANT_CLASSIFICATION_ENTRY_V1";

    public MerchantClassificationEntryV1 {
        Objects.requireNonNull(kind, "kind");
        merchantApprovedLabel = normalizeLabel(merchantApprovedLabel);
        Objects.requireNonNull(exposure, "exposure");
    }

    public String schemaIdentity() {
        return SCHEMA_IDENTITY;
    }

    static String normalizeLabel(String value) {
        Objects.requireNonNull(value, "merchantApprovedLabel");
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFC)
                .strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException(
                    "merchantApprovedLabel must not be blank"
            );
        }
        return normalized;
    }
}
