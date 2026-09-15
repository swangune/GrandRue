package mainstreet.surface;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/**
 * Read-side authority establishing whether one declared projection requirement
 * may be served in the current merchant/read context under its owning
 * Projection Contract. Empty means serviceability cannot currently be safely
 * established and therefore fails closed at the consuming Surface boundary.
 */
@FunctionalInterface
public interface ProjectionServiceabilityAuthority {

    Optional<Boolean> currentServiceability(
            MerchantScope merchantScope,
            String projectionRequirement,
            Optional<String> requestContext
    );
}
