package mainstreet.commercial;

import mainstreet.application.MerchantScope;

/**
 * Resolves whether one merchant currently has one commercial entitlement.
 *
 * <p>This authority answers commercial permission only. It does not create
 * semantic applicability, actor authority, trust satisfaction, provider
 * readiness or capability-owned operational eligibility.</p>
 */
@FunctionalInterface
public interface CommercialEntitlementAuthority {

    boolean isEntitled(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity
    );
}
