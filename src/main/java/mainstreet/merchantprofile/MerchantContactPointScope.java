package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/** Explicit merchant or same-merchant Location scope for a contact point. */
public record MerchantContactPointScope(
        MerchantScope merchantScope,
        MerchantContactPointScopeKind kind,
        Optional<String> merchantLocationIdentity
) {
    public MerchantContactPointScope {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(kind, "kind");
        merchantLocationIdentity = Objects.requireNonNull(
                merchantLocationIdentity,
                "merchantLocationIdentity"
        );
        merchantLocationIdentity.ifPresent(value ->
                CreateMerchantLocationCommand.require(
                        value,
                        "merchantLocationIdentity"
                )
        );
        if ((kind == MerchantContactPointScopeKind.MERCHANT_LOCATION)
                != merchantLocationIdentity.isPresent()) {
            throw new IllegalArgumentException(
                    "Merchant Location scope requires exactly one Location identity"
            );
        }
    }

    public static MerchantContactPointScope merchant(MerchantScope merchant) {
        return new MerchantContactPointScope(
                merchant,
                MerchantContactPointScopeKind.MERCHANT,
                Optional.empty()
        );
    }

    public static MerchantContactPointScope merchantLocation(
            MerchantScope merchant,
            String locationIdentity
    ) {
        return new MerchantContactPointScope(
                merchant,
                MerchantContactPointScopeKind.MERCHANT_LOCATION,
                Optional.of(locationIdentity)
        );
    }
}
