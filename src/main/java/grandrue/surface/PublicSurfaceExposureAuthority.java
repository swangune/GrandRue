package grandrue.surface;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Audience-observation authority for an already-candidate PUBLIC Surface
 * Contribution. It can withhold a candidate but cannot manufacture Surface
 * membership. Empty means no safe current exposure decision is established.
 */
@FunctionalInterface
public interface PublicSurfaceExposureAuthority {

    Optional<ExposureDecision> currentDecision(
            MerchantScope merchantScope,
            SurfaceContributionIdentity contributionIdentity,
            Optional<String> requestContext
    );
}
