package grandrue.application;

import grandrue.commercial.OrdinaryMerchantAccountPathReadiness;
import grandrue.merchantaccount.MerchantAccountEstablisher;

import java.util.Objects;

/**
 * Delivery/application composition for the ordinary new-account path.
 *
 * <p>The rollout readiness check remains outside the Merchant Account owner
 * operation and outside its transaction. MerchantAccountEstablisher therefore
 * retains the commercially independent semantics required by MS-PROT-071,
 * while MS-PROT-056 v1.9 §11's production sequencing gate is enforced before
 * delegation.</p>
 */
public final class OrdinaryMerchantAccountEstablishmentApplicationService {

    private final OrdinaryMerchantAccountPathReadiness rolloutReadiness;
    private final MerchantAccountEstablisher ownerOperation;

    public OrdinaryMerchantAccountEstablishmentApplicationService(
            OrdinaryMerchantAccountPathReadiness rolloutReadiness,
            MerchantAccountEstablisher ownerOperation
    ) {
        this.rolloutReadiness = Objects.requireNonNull(
                rolloutReadiness,
                "rolloutReadiness"
        );
        this.ownerOperation = Objects.requireNonNull(
                ownerOperation,
                "ownerOperation"
        );
    }

    public MerchantAccountEstablisher.Result establish(
            String logicalEstablishmentRequestIdentity,
            TrustedPlatformHumanPrincipal principal
    ) {
        rolloutReadiness.requireReady();
        return ownerOperation.establish(
                logicalEstablishmentRequestIdentity,
                principal
        );
    }
}
