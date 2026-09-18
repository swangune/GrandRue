package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantServiceAreaGeographyKind;

/** Descriptive named-area geography qualified by one country. */
public record NamedAreaGeographyV1(
        String countryCode,
        String areaName
) implements ServiceAreaGeographyV1 {
    public NamedAreaGeographyV1 {
        countryCode = ServiceAreaGeographyV1.normalizeCountry(countryCode);
        areaName = ServiceAreaGeographyV1.normalizeText(areaName, "areaName");
    }

    @Override
    public MerchantServiceAreaGeographyKind kind() {
        return MerchantServiceAreaGeographyKind.NAMED_AREA;
    }
}
