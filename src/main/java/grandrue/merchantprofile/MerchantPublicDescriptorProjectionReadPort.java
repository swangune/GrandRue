package grandrue.merchantprofile;

import grandrue.application.MerchantScope;

import java.time.Instant;

/**
 * Profile-owned query boundary for one coherent current Merchant Public
 * Descriptor projection observation.
 *
 * <p>The port establishes typed owner material, exact revision affinity and
 * corresponding P2 evidence in one owner read. Generic Surface infrastructure
 * receives only the generic projection metadata contracts carried by the
 * returned Profile-owned observation; it does not acquire Profile authority.</p>
 */
public interface MerchantPublicDescriptorProjectionReadPort {

    MerchantPublicDescriptorProjectionObservation observe(
            MerchantScope merchantScope,
            Instant observedAt
    );
}
