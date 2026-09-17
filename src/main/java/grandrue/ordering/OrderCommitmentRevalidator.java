package grandrue.ordering;

import mainstreet.application.MerchantScope;

/**
 * Application port for re-establishing current authoritative proposition and
 * commercial prerequisites before Order commitment. The port does not own the
 * source Product/Offering/Money facts it consults.
 */
@FunctionalInterface
public interface OrderCommitmentRevalidator {
    ResolvedOrderCommitment revalidate(
            MerchantScope merchantScope,
            RequestedOrderPortion requestedPortion
    );
}
