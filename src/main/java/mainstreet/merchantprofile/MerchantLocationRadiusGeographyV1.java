package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantServiceAreaGeographyKind;

/** Descriptive radius around one exact Merchant Location revision. */
public record MerchantLocationRadiusGeographyV1(
        String merchantLocationIdentity,
        String merchantLocationRevisionIdentity,
        long radiusMetres
) implements ServiceAreaGeographyV1 {
    public MerchantLocationRadiusGeographyV1 {
        CreateMerchantLocationCommand.require(
                merchantLocationIdentity,
                "merchantLocationIdentity"
        );
        CreateMerchantLocationCommand.require(
                merchantLocationRevisionIdentity,
                "merchantLocationRevisionIdentity"
        );
        if (radiusMetres < 1) {
            throw new IllegalArgumentException(
                    "radiusMetres must be positive"
            );
        }
    }

    @Override
    public MerchantServiceAreaGeographyKind kind() {
        return MerchantServiceAreaGeographyKind.MERCHANT_LOCATION_RADIUS;
    }
}
