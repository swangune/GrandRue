package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantLocationExposure;

import grandrue.merchantprofile.MerchantLocationAuthority;
import grandrue.merchantprofile.MerchantLocationExposureChoiceAuthority;
import grandrue.merchantprofile.MerchantLocationExposureChoiceReadPort;
import grandrue.merchantprofile.MerchantLocationLifecycle;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Profile-owned adapter that exposes Merchant Location choice only when the
 * stable Location identity is currently active and an explicit current choice exists.
 * BR5 bounded evaluation additionally requires exact current Location progress.
 */
public final class AuthorityBackedMerchantLocationExposureChoiceReadPort
        implements MerchantLocationExposureChoiceReadPort {

    private final MerchantLocationAuthority locationAuthority;
    private final MerchantLocationExposureChoiceAuthority choiceAuthority;

    public AuthorityBackedMerchantLocationExposureChoiceReadPort(
            MerchantLocationAuthority locationAuthority,
            MerchantLocationExposureChoiceAuthority choiceAuthority
    ) {
        this.locationAuthority = Objects.requireNonNull(
                locationAuthority,
                "locationAuthority"
        );
        this.choiceAuthority = Objects.requireNonNull(
                choiceAuthority,
                "choiceAuthority"
        );
    }

    @Override
    public Optional<MerchantLocationExposure> currentActiveExposure(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        return currentActiveRevision(merchantScope, locationIdentity)
                .flatMap(ignored -> currentChoice(merchantScope, locationIdentity));
    }

    @Override
    public Optional<MerchantLocationExposure> currentActiveExposureAtProgress(
            MerchantScope merchantScope,
            String locationIdentity,
            String expectedProgressIdentifier
    ) {
        requireProgress(expectedProgressIdentifier);
        return currentActiveRevision(merchantScope, locationIdentity)
                .filter(revision -> expectedProgressIdentifier.equals(
                        revision.revisionIdentity()
                ))
                .flatMap(ignored -> currentChoice(merchantScope, locationIdentity));
    }

    private Optional<MerchantLocationRevision> currentActiveRevision(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(locationIdentity, "locationIdentity");
        return locationAuthority.current(merchantScope, locationIdentity)
                .filter(revision -> revision.lifecycle()
                        == MerchantLocationLifecycle.ACTIVE);
    }

    private Optional<MerchantLocationExposure> currentChoice(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        return choiceAuthority.current(merchantScope, locationIdentity)
                .map(MerchantLocationExposureChoiceRevision::exposure);
    }

    private static void requireProgress(String expectedProgressIdentifier) {
        if (expectedProgressIdentifier == null
                || expectedProgressIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Expected Merchant Location progress identifier must not be blank"
            );
        }
    }
}
