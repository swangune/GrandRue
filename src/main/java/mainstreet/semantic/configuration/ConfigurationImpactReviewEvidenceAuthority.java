package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;

import java.util.Optional;

/** Durable authority for exact business-facing impact-review evidence. */
public interface ConfigurationImpactReviewEvidenceAuthority {

    ConfigurationImpactReviewEvidence recordCompletedReview(
            RecordConfigurationImpactReviewEvidenceCommand command
    );

    Optional<ConfigurationImpactReviewEvidence> evidence(
            MerchantScope merchantScope,
            String impactReviewEvidenceIdentifier
    );
}
