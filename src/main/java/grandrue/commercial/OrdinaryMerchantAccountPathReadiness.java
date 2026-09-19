package grandrue.commercial;

/**
 * Rollout-only readiness boundary for enabling the ordinary Merchant Account
 * establishment delivery path.
 *
 * <p>This does not become a Merchant Account semantic predicate. MS-PROT-056
 * v1.9 §11 requires production rollout to establish the initial catalogue
 * before enabling the ordinary new-account path that depends on catalogue
 * coverage, while MS-PROT-071 keeps Merchant Account establishment itself
 * commercially independent.</p>
 */
@FunctionalInterface
public interface OrdinaryMerchantAccountPathReadiness {

    /**
     * Require the accepted initial standard catalogue to be authoritatively
     * published and suitable for ordinary new-account rollout.
     */
    void requireReady();
}
