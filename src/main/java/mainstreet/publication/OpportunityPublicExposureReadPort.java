package mainstreet.publication;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Narrow Publication-owned read boundary for current public Opportunity Exposure evidence.
 *
 * <p>Presence means that the Opportunity is currently PUBLISHED and the exact revision bound for
 * publication can be reconstructed. Absence is fail-closed and grants no public candidacy.</p>
 */
@FunctionalInterface
public interface OpportunityPublicExposureReadPort {

    Optional<OpportunityPublicExposureEvidence> currentPublishedExposure(
            MerchantScope merchantScope,
            String opportunityIdentity
    );

    /**
     * Reads current public evidence only when the still-published revision matches the bounded
     * material progress that was acquired earlier in the same Observation Request.
     */
    default Optional<OpportunityPublicExposureEvidence> currentPublishedExposureAtProgress(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedPublishedRevisionIdentity
    ) {
        Objects.requireNonNull(expectedPublishedRevisionIdentity, "expectedPublishedRevisionIdentity");
        if (expectedPublishedRevisionIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "expectedPublishedRevisionIdentity must not be blank"
            );
        }
        return currentPublishedExposure(merchantScope, opportunityIdentity)
                .filter(evidence -> expectedPublishedRevisionIdentity.equals(
                        evidence.publishedRevisionIdentity()
                ));
    }
}
