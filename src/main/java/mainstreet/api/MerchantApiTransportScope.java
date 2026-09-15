package mainstreet.api;

import mainstreet.application.MerchantScope;

import java.util.Objects;

/** Trusted merchant scope resolved by a registered transport authority. */
public record MerchantApiTransportScope(MerchantScope merchantScope)
        implements ApiTransportScope {

    public MerchantApiTransportScope {
        Objects.requireNonNull(merchantScope, "merchantScope");
    }
}
