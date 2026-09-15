package mainstreet.surface;

import mainstreet.application.MerchantScope;

/**
 * Surface-layer query boundary for whether a capability still has outstanding
 * merchant obligations requiring residual management. The owning capability
 * remains authoritative for the underlying commitments.
 */
@FunctionalInterface
public interface ResidualSurfaceObligationAuthority {

    boolean hasOutstandingObligations(
            MerchantScope merchantScope,
            String capabilityIdentifier
    );
}
