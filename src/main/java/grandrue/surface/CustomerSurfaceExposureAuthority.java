package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;

import java.util.Optional;

/**
 * Audience-observation authority for an already-eligible CUSTOMER Surface
 * Contribution. It can withhold a candidate but cannot manufacture customer
 * Surface membership. Empty means no safe current exposure decision exists.
 */
@FunctionalInterface
public interface CustomerSurfaceExposureAuthority {

    Optional<ExposureDecision> currentDecision(
            MerchantScope merchantScope,
            TrustedExecutionContext trustedExecutionContext,
            SurfaceContributionIdentity contributionIdentity,
            Optional<String> requestContext
    );
}
