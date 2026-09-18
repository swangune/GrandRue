package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.merchantprofile.CreateMerchantServiceAreaCommand;
import grandrue.merchantprofile.MerchantServiceAreaRevision;
import grandrue.merchantprofile.RetireMerchantServiceAreaCommand;
import grandrue.merchantprofile.UpdateMerchantServiceAreaCommand;

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
