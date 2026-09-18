package grandrue.merchantprofile;

import mainstreet.merchantprofile.MerchantContactPointRevision;

import grandrue.merchantprofile.MerchantContactPointExposure;

import grandrue.merchantprofile.MerchantContactPointAuthority;
import grandrue.merchantprofile.MerchantContactPointExposureChoiceReadPort;
import grandrue.merchantprofile.MerchantContactPointLifecycle;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Profile-owned adapter that exposes current active Contact Point exposure
 * choice and, for BR5 bounded evaluation, proves exact current source progress.
 */
public final class AuthorityBackedMerchantContactPointExposureChoiceReadPort
        implements MerchantContactPointExposureChoiceReadPort {

    private final MerchantContactPointAuthority authority;

    public AuthorityBackedMerchantContactPointExposureChoiceReadPort(
            MerchantContactPointAuthority authority
    ) {
        this.authority = Objects.requireNonNull(authority, "authority");
    }

    @Override
    public Optional<MerchantContactPointExposure> currentActiveExposure(
            MerchantScope merchantScope,
            String contactPointIdentity
    ) {
        return currentActiveRevision(merchantScope, contactPointIdentity)
                .map(MerchantContactPointRevision::exposure);
    }

    @Override
    public Optional<MerchantContactPointExposure> currentActiveExposureAtProgress(
            MerchantScope merchantScope,
            String contactPointIdentity,
            String expectedProgressIdentifier
    ) {
        requireProgress(expectedProgressIdentifier);
        return currentActiveRevision(merchantScope, contactPointIdentity)
                .filter(revision -> expectedProgressIdentifier.equals(
                        revision.revisionIdentity()
                ))
                .map(MerchantContactPointRevision::exposure);
    }

    private Optional<MerchantContactPointRevision> currentActiveRevision(
            MerchantScope merchantScope,
            String contactPointIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(contactPointIdentity, "contactPointIdentity");
        return authority.current(merchantScope, contactPointIdentity)
                .filter(revision -> revision.lifecycle()
                        == MerchantContactPointLifecycle.ACTIVE);
    }

    private static void requireProgress(String expectedProgressIdentifier) {
        if (expectedProgressIdentifier == null
                || expectedProgressIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Expected Contact Point progress identifier must not be blank"
            );
        }
    }
}
