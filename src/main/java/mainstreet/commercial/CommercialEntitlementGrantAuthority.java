package mainstreet.commercial;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.List;

/**
 * Commercial-owned query boundary for provenance-bearing entitlement grants
 * that are effective for one merchant, entitlement and evaluation instant.
 *
 * <p>Independent implementations may represent standing baseline, trial,
 * paid-agreement, remediation or other accepted Commercial sources. This is
 * not a runtime decision engine and it does not own semantic applicability.</p>
 */
@FunctionalInterface
public interface CommercialEntitlementGrantAuthority {

    List<CommercialEntitlementGrant> effectiveGrants(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity,
            Instant instant
    );
}
