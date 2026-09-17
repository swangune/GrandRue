package grandrue.workforce;

import mainstreet.application.MerchantScope;

/**
 * Current merchant-scoped workforce relationship authority used when a staff
 * execution context is established.
 *
 * <p>This answers only whether one Identity currently has an ACTIVE Merchant
 * Membership in one Merchant Scope. It does not answer privilege, device,
 * authentication or commercial-entitlement questions.</p>
 */
@FunctionalInterface
public interface MerchantMembershipAuthority {

    boolean isActive(
            MerchantScope merchantScope,
            String identityReference
    );
}
