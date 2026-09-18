package mainstreet.merchantprofile;

import grandrue.merchantprofile.PostalAddressValidationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Provider-neutral normalized international postal-address value. */
public record PostalAddressV1(
        String schemaIdentity,
        String normalizationProfileIdentity,
        String countryRegistryIdentity,
        String countryCode,
        List<String> addressLines,
        Optional<String> dependentLocality,
        Optional<String> locality,
        Optional<String> administrativeArea,
        Optional<String> postalCode,
        Optional<String> sortingCode
) {
    public static final String SCHEMA_IDENTITY = "POSTAL_ADDRESS_V1";
    public static final String NORMALIZATION_PROFILE_IDENTITY =
            "MS_POSTAL_ADDRESS_NORMALIZATION_V1";
    public static final String COUNTRY_REGISTRY_IDENTITY =
            "ISO_3166_1_ALPHA_2_2026_08_30";

    public PostalAddressV1 {
        if (!SCHEMA_IDENTITY.equals(schemaIdentity)) {
            throw new PostalAddressValidationException(
                    "Unsupported postal-address schema"
            );
        }
        if (!NORMALIZATION_PROFILE_IDENTITY.equals(
                normalizationProfileIdentity
        )) {
            throw new PostalAddressValidationException(
                    "Unsupported postal-address normalization profile"
            );
        }
        if (!COUNTRY_REGISTRY_IDENTITY.equals(countryRegistryIdentity)) {
            throw new PostalAddressValidationException(
                    "Unsupported country registry"
            );
        }
        Objects.requireNonNull(countryCode, "countryCode");
        addressLines = List.copyOf(
                Objects.requireNonNull(addressLines, "addressLines")
        );
        Objects.requireNonNull(dependentLocality, "dependentLocality");
        Objects.requireNonNull(locality, "locality");
        Objects.requireNonNull(administrativeArea, "administrativeArea");
        Objects.requireNonNull(postalCode, "postalCode");
        Objects.requireNonNull(sortingCode, "sortingCode");
    }
}
