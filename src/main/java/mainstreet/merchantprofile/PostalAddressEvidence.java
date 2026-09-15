package mainstreet.merchantprofile;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Exact original and deterministically normalized PostalAddressV1 evidence. */
public record PostalAddressEvidence(
        PostalAddressInput originalInput,
        PostalAddressV1 normalizedAddress
) {
    private static final Set<String> ISO_ALPHA_2 = Set.copyOf(
            Arrays.asList(Locale.getISOCountries())
    );

    public PostalAddressEvidence {
        Objects.requireNonNull(originalInput, "originalInput");
        Objects.requireNonNull(normalizedAddress, "normalizedAddress");
    }

    public static PostalAddressEvidence accept(PostalAddressInput input) {
        Objects.requireNonNull(input, "input");
        String countryCode = normalize(input.countryCode(), "countryCode")
                .toUpperCase(Locale.ROOT);
        if (!ISO_ALPHA_2.contains(countryCode)) {
            throw new PostalAddressValidationException(
                    "Country code is not present in "
                            + PostalAddressV1.COUNTRY_REGISTRY_IDENTITY
            );
        }
        if (input.addressLines().isEmpty()) {
            throw new PostalAddressValidationException(
                    "At least one address line is required"
            );
        }
        List<String> lines = input.addressLines().stream()
                .map(value -> normalize(value, "addressLine"))
                .toList();
        PostalAddressV1 normalized = new PostalAddressV1(
                PostalAddressV1.SCHEMA_IDENTITY,
                PostalAddressV1.NORMALIZATION_PROFILE_IDENTITY,
                PostalAddressV1.COUNTRY_REGISTRY_IDENTITY,
                countryCode,
                lines,
                normalize(input.dependentLocality(), "dependentLocality"),
                normalize(input.locality(), "locality"),
                normalize(input.administrativeArea(), "administrativeArea"),
                normalize(input.postalCode(), "postalCode"),
                normalize(input.sortingCode(), "sortingCode")
        );
        return new PostalAddressEvidence(input, normalized);
    }

    private static Optional<String> normalize(
            Optional<String> value,
            String label
    ) {
        return value.map(component -> normalize(component, label));
    }

    private static String normalize(String value, String label) {
        if (value == null) {
            throw new PostalAddressValidationException(
                    label + " must not be null"
            );
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFC)
                .strip();
        if (normalized.isBlank()) {
            throw new PostalAddressValidationException(
                    label + " must not be blank"
            );
        }
        if (normalized.codePoints().anyMatch(Character::isISOControl)) {
            throw new PostalAddressValidationException(
                    label + " contains a control character"
            );
        }
        return normalized;
    }
}
