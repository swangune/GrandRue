package mainstreet.surface;

import mainstreet.application.MerchantScope;
import grandrue.fulfilment.FulfilmentRoleIdentity;

import java.util.Optional;

/**
 * Surface-facing query port for the current operability projection of one
 * fulfilment role. Implementations adapt owning provider/readiness authority;
 * this port does not own ProviderConnection health or resilience policy.
 */
@FunctionalInterface
public interface FulfilmentRoleInteractionAvailabilityAuthority {

    Optional<SurfaceInteractionAvailability> currentAvailability(
            MerchantScope merchantScope,
            FulfilmentRoleIdentity roleIdentity
    );
}
