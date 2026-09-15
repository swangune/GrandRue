package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/** Authoritative Merchant Contact Point mutation and currentness boundary. */
public interface MerchantContactPointAuthority {
    MerchantContactPointRevision create(
            CreateMerchantContactPointCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantContactPointRevision update(
            UpdateMerchantContactPointCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantContactPointRevision retire(
            RetireMerchantContactPointCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<MerchantContactPointRevision> current(
            MerchantScope merchantScope,
            String contactPointIdentity
    );

    Optional<MerchantContactPointRevision> revision(String revisionIdentity);
}
