package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import mainstreet.merchantprofile.CreateMerchantServiceAreaCommand;
import mainstreet.merchantprofile.MerchantServiceAreaRevision;
import mainstreet.merchantprofile.RetireMerchantServiceAreaCommand;
import mainstreet.merchantprofile.UpdateMerchantServiceAreaCommand;

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
