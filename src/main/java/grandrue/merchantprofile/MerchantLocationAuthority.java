package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.merchantprofile.CorrectMerchantLocationCommand;
import grandrue.merchantprofile.CreateMerchantLocationCommand;
import grandrue.merchantprofile.MerchantLocationRevision;
import grandrue.merchantprofile.RetireMerchantLocationCommand;

import java.util.Optional;

/** Authoritative Merchant Location mutation, revision and currentness boundary. */
public interface MerchantLocationAuthority {
    MerchantLocationRevision create(
            CreateMerchantLocationCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantLocationRevision correct(
            CorrectMerchantLocationCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantLocationRevision retire(
            RetireMerchantLocationCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<MerchantLocationRevision> current(
            MerchantScope merchantScope,
            String locationIdentity
    );

    Optional<MerchantLocationRevision> revision(String revisionIdentity);
}
