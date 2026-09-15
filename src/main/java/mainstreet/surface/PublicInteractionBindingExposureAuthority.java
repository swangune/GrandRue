package mainstreet.surface;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/**
 * Audience-observation authority for an already-derived public interaction
 * subject binding candidate. It can withhold a subject binding but owns no
 * subject-interaction participation truth.
 */
@FunctionalInterface
public interface PublicInteractionBindingExposureAuthority {

    Optional<ExposureDecision> currentDecision(
            MerchantScope merchantScope,
            SurfaceContributionIdentity contributionIdentity,
            String publicSubjectReference,
            Optional<String> requestContext
    );
}
