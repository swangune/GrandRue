package grandrue.fulfilment;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Read authority for one exact immutable merchant Fulfilment Binding Set Revision.
 *
 * <p>The authority resolves historical routing by exact merchant scope and exact
 * revision reference. It does not own activation and must not substitute a
 * mutable current/latest revision.</p>
 *
 * <p>Authority: MS-PROT-048 v1.2 §§3–6, 13, 17–18.</p>
 */
@FunctionalInterface
public interface FulfilmentBindingSetRevisionAuthority {

    Optional<FulfilmentBindingSetRevision> revision(
            MerchantScope merchantScope,
            FulfilmentBindingSetRevisionReference reference
    );
}
