package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Profile-owned BR5 progress-affinity read seam for Merchant Location exposure.
 *
 * <p>The default is deliberately unresolved so legacy functional read-port
 * implementations cannot accidentally claim current-progress proof.</p>
 */
public interface MerchantLocationProgressAffinityReadPort {

    default Optional<MerchantLocationExposure> currentActiveExposureAtProgress(
            MerchantScope merchantScope,
            String locationIdentity,
            String expectedProgressIdentifier
    ) {
        return Optional.empty();
    }
}
