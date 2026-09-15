package mainstreet.api;

/**
 * Trusted scope established for one API transport interaction. Merchant and
 * platform scope are closed, distinct alternatives.
 */
public sealed interface ApiTransportScope
        permits MerchantApiTransportScope, PlatformApiTransportScope {
}
