package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/** Authoritative Merchant Service Area mutation and currentness boundary. */
public interface MerchantServiceAreaAuthority {
    MerchantServiceAreaRevision create(
            CreateMerchantServiceAreaCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantServiceAreaRevision update(
            UpdateMerchantServiceAreaCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantServiceAreaRevision retire(
            RetireMerchantServiceAreaCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<MerchantServiceAreaRevision> current(
            MerchantScope merchantScope,
            String serviceAreaIdentity
    );

    Optional<MerchantServiceAreaRevision> revision(String revisionIdentity);
}
