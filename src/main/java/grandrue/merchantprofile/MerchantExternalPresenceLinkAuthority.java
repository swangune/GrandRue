package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import mainstreet.merchantprofile.CreateMerchantExternalPresenceLinkCommand;
import mainstreet.merchantprofile.MerchantExternalPresenceLinkRevision;
import mainstreet.merchantprofile.RetireMerchantExternalPresenceLinkCommand;
import mainstreet.merchantprofile.UpdateMerchantExternalPresenceLinkCommand;

import java.util.Optional;

/** Authoritative Merchant External Presence mutation and currentness boundary. */
public interface MerchantExternalPresenceLinkAuthority {
    MerchantExternalPresenceLinkRevision create(
            CreateMerchantExternalPresenceLinkCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantExternalPresenceLinkRevision update(
            UpdateMerchantExternalPresenceLinkCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantExternalPresenceLinkRevision retire(
            RetireMerchantExternalPresenceLinkCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<MerchantExternalPresenceLinkRevision> current(
            MerchantScope merchantScope,
            String presenceIdentity
    );

    Optional<MerchantExternalPresenceLinkRevision> revision(
            String revisionIdentity
    );
}
