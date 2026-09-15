package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/** Authoritative current/revision boundary for Merchant Location Exposure choice. */
public interface MerchantLocationExposureChoiceAuthority {
    MerchantLocationExposureChoiceRevision set(
            SetMerchantLocationExposureChoiceCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<MerchantLocationExposureChoiceRevision> current(
            MerchantScope merchantScope,
            String locationIdentity
    );

    Optional<MerchantLocationExposureChoiceRevision> revision(
            String revisionIdentity
    );
}
