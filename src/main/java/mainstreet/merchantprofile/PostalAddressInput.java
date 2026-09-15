package mainstreet.merchantprofile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Exact caller-supplied structured postal-address components. */
public record PostalAddressInput(
        String countryCode,
        List<String> addressLines,
        Optional<String> dependentLocality,
        Optional<String> locality,
        Optional<String> administrativeArea,
        Optional<String> postalCode,
        Optional<String> sortingCode
) {
    public PostalAddressInput {
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
