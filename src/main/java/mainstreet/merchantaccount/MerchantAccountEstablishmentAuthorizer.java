package mainstreet.merchantaccount;

import mainstreet.application.TrustedPlatformHumanPrincipal;

/**
 * Authorisation boundary for the narrow PLATFORM-scoped
 * ESTABLISH_OWN_MERCHANT_ACCOUNT permission.
 *
 * <p>Authentication is already established before this contract is invoked;
 * this contract answers only the separate authorisation question.</p>
 */
@FunctionalInterface
public interface MerchantAccountEstablishmentAuthorizer {
    boolean mayEstablishOwnMerchantAccount(TrustedPlatformHumanPrincipal principal);
}
