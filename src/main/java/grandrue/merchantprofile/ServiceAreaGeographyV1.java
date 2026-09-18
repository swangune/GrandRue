package grandrue.merchantprofile;

import grandrue.merchantprofile.MerchantServiceAreaGeographyKind;

import java.text.Normalizer;
import java.util.Locale;

/** Versioned descriptive geography value; never executable eligibility itself. */
public sealed interface ServiceAreaGeographyV1
        permits NamedAreaGeographyV1,
        MerchantLocationRadiusGeographyV1,
        CountryWideGeographyV1,
        RemoteCountriesGeographyV1 {
    String SCHEMA_IDENTITY = "MS_SERVICE_AREA_GEOGRAPHY_V1";

    MerchantServiceAreaGeographyKind kind();

    default String schemaIdentity() {
        return SCHEMA_IDENTITY;
    }

    static String normalizeText(String value, String label) {
        CreateMerchantLocationCommand.require(value, label);
        String normalized = Normalizer.normalize(
                value,
                Normalizer.Form.NFC
        ).strip();
        CreateMerchantLocationCommand.require(normalized, label);
        return normalized;
    }

    static String normalizeCountry(String value) {
        String normalized = normalizeText(value, "countryCode")
                .toUpperCase(Locale.ROOT);
        if (!normalized.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException(
                    "countryCode must be two ASCII letters"
            );
        }
        return normalized;
    }
}
