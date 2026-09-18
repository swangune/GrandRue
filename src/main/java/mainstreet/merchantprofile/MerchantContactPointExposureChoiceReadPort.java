package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Optional;

/** Read-only current active Contact Point exposure choice boundary for Exposure evaluation. */
@FunctionalInterface
public interface MerchantContactPointExposureChoiceReadPort
        extends MerchantContactPointProgressAffinityReadPort {

    Optional<MerchantContactPointExposure> currentActiveExposure(
            MerchantScope merchantScope,
            String contactPointIdentity
    );
}
