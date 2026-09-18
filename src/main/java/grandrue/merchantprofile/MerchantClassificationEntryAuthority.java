package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.merchantprofile.CreateMerchantClassificationEntryCommand;
import grandrue.merchantprofile.MerchantClassificationEntryRevision;
import grandrue.merchantprofile.RetireMerchantClassificationEntryCommand;
import grandrue.merchantprofile.UpdateMerchantClassificationEntryCommand;

import java.util.Optional;

/** Authoritative Merchant Classification Entry mutation/currentness boundary. */
public interface MerchantClassificationEntryAuthority {
    MerchantClassificationEntryRevision create(
            CreateMerchantClassificationEntryCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantClassificationEntryRevision update(
            UpdateMerchantClassificationEntryCommand command,
            TrustedExecutionContext trustedContext
    );

    MerchantClassificationEntryRevision retire(
            RetireMerchantClassificationEntryCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<MerchantClassificationEntryRevision> current(
            MerchantScope merchantScope,
            String classificationIdentity
    );

    Optional<MerchantClassificationEntryRevision> revision(
            String revisionIdentity
    );
}
