package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.merchantprofile.CreateMerchantExternalPresenceLinkCommand;
import grandrue.merchantprofile.MerchantExternalPresenceLinkRevision;
import grandrue.merchantprofile.RetireMerchantExternalPresenceLinkCommand;
import grandrue.merchantprofile.UpdateMerchantExternalPresenceLinkCommand;

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
