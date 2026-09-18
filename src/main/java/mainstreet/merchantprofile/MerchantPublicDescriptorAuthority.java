package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Authoritative revision persistence for the logically singleton-scoped
 * Merchant Public Descriptor.
 */
public interface MerchantPublicDescriptorAuthority {

    MerchantPublicDescriptorRevision establish(
            MerchantPublicDescriptorMutationCommand command
    );

    MerchantPublicDescriptorRevision revise(
            MerchantPublicDescriptorMutationCommand command
    );

    Optional<MerchantPublicDescriptorRevision> current(
            MerchantScope merchantScope
    );

    Optional<MerchantPublicDescriptorRevision> revision(
            MerchantScope merchantScope,
            long revision
    );
}
