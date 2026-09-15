package mainstreet.merchantprofile;

/** Descriptive country-wide geography. */
public record CountryWideGeographyV1(
        String countryCode
) implements ServiceAreaGeographyV1 {
    public CountryWideGeographyV1 {
        countryCode = ServiceAreaGeographyV1.normalizeCountry(countryCode);
    }

    @Override
    public MerchantServiceAreaGeographyKind kind() {
        return MerchantServiceAreaGeographyKind.COUNTRY_WIDE;
    }
}
