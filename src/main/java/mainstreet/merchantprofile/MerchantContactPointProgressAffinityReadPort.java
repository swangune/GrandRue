package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Profile-owned BR5 progress-affinity read seam for Contact Point exposure.
 *
 * <p>The default is deliberately unresolved so legacy functional read-port
 * implementations cannot accidentally claim current-progress proof.</p>
 */
public interface MerchantContactPointProgressAffinityReadPort {

    default Optional<MerchantContactPointExposure> currentActiveExposureAtProgress(
            MerchantScope merchantScope,
            String contactPointIdentity,
            String expectedProgressIdentifier
    ) {
        return Optional.empty();
    }
}
