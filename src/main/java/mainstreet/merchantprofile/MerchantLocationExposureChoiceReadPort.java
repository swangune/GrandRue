package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Optional;

/** Read-only current active Merchant Location exposure choice boundary for Exposure evaluation. */
@FunctionalInterface
public interface MerchantLocationExposureChoiceReadPort
        extends MerchantLocationProgressAffinityReadPort {

    Optional<MerchantLocationExposure> currentActiveExposure(
            MerchantScope merchantScope,
            String locationIdentity
    );
}
