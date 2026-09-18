package grandrue.publication;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Publication-owned read evidence required to decide the public Opportunity exposure window.
 *
 * <p>This is not a projection, Exposure verdict or storefront representation. It identifies the
 * exact revision currently bound for publication and carries only the Publication-owned temporal
 * evidence needed by the P4 Exposure requirement evaluator.</p>
 */
public record OpportunityPublicExposureEvidence(
        MerchantScope merchantScope,
        String opportunityIdentity,
        String publishedRevisionIdentity,
        Optional<OpportunityTemporalBoundary> publishFrom,
        Optional<OpportunityTemporalBoundary> publishUntil
) {
    public OpportunityPublicExposureEvidence {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        requireText(publishedRevisionIdentity, "publishedRevisionIdentity");
        Objects.requireNonNull(publishFrom, "publishFrom");
        Objects.requireNonNull(publishUntil, "publishUntil");
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
