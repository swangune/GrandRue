package grandrue.merchantaccount;

import grandrue.application.MerchantScope;

import java.util.Objects;

/**
 * Authoritative GrandRue merchant tenancy identity.
 *
 * <p>This value establishes only the identity of one Merchant Account. It does
 * not imply onboarding completion, configuration activation, trust,
 * commercial entitlement, publication or operational readiness.</p>
 *
 * <p>Governed by MS-PROT-071.</p>
 */
public record MerchantAccount(MerchantScope merchantScope) {

    public MerchantAccount {
        Objects.requireNonNull(merchantScope, "merchantScope");
    }

    public String merchantIdentifier() {
        return merchantScope.merchantIdentifier();
    }
}
