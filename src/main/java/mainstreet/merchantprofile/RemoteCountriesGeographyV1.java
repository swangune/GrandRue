package mainstreet.merchantprofile;

import java.util.List;
import java.util.Objects;

/** Descriptive remote-service geography over a canonical country set. */
public record RemoteCountriesGeographyV1(
        List<String> countryCodes
) implements ServiceAreaGeographyV1 {
    public RemoteCountriesGeographyV1 {
        Objects.requireNonNull(countryCodes, "countryCodes");
        countryCodes = countryCodes.stream()
                .map(ServiceAreaGeographyV1::normalizeCountry)
                .distinct()
                .sorted()
                .toList();
        if (countryCodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "countryCodes must not be empty"
            );
        }
    }

    @Override
    public MerchantServiceAreaGeographyKind kind() {
        return MerchantServiceAreaGeographyKind.REMOTE_COUNTRIES;
    }
}
