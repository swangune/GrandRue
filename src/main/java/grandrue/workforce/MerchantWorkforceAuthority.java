package grandrue.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.Privilege;

import java.time.Instant;

/**
 * Current merchant-scoped workforce authority query.
 *
 * <p>This boundary derives only workforce Actor Authorisation from authoritative
 * Merchant Membership, Group Membership and Role Assignment facts. It does not
 * establish authentication, device trust, Commercial Entitlement or capability
 * business eligibility.</p>
 */
@FunctionalInterface
public interface MerchantWorkforceAuthority {

    boolean hasPrivilege(
            MerchantScope merchantScope,
            String identityReference,
            Privilege privilege,
            Instant instant
    );
}
