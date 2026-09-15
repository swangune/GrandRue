package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;

/**
 * Opaque API-surface-bound observation context.
 *
 * <p>It deliberately exposes no underlying observation context or generic
 * unwrap operation.</p>
 */
public sealed interface ApiAudienceObservationContext
        permits DefaultApiAudienceObservationContext {

    ApiContractIdentity contractIdentity();

    ApiSurfaceClass surface();
}
