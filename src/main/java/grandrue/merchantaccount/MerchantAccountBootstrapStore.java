package grandrue.merchantaccount;

import grandrue.application.TrustedPlatformHumanPrincipal;

/**
 * Atomic persistence/application boundary for ordinary Merchant Account
 * bootstrap.
 *
 * <p>The implementation MUST atomically establish both Merchant Account
 * existence and the initiating identity's initial Merchant Controller
 * relationship, and MUST be idempotent for one logical request identity.</p>
 */
public interface MerchantAccountBootstrapStore {

    BootstrapOutcome establishIfAbsent(
            String logicalEstablishmentRequestIdentity,
            TrustedPlatformHumanPrincipal initialController);

    record BootstrapOutcome(MerchantAccount merchantAccount, boolean alreadyEstablished) {
        public BootstrapOutcome {
            if (merchantAccount == null) {
                throw new IllegalArgumentException("merchantAccount is required");
            }
        }
    }
}
