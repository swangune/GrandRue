package grandrue.merchantaccount;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Ordinary current-Controller request to transition OPEN to CLOSING. */
public record BeginMerchantAccountClosureCommand(
        String logicalRequestIdentity,
        MerchantScope merchantScope,
        String expectedCurrentControllerRelationshipIdentity,
        String initiatingControllerIdentity,
        String authenticationAssuranceEvidenceIdentity,
        Instant requestedAt
) {
    public BeginMerchantAccountClosureCommand {
        require(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(expectedCurrentControllerRelationshipIdentity, "Expected Controller relationship");
        require(initiatingControllerIdentity, "Initiating Controller identity");
        require(authenticationAssuranceEvidenceIdentity, "Authentication assurance evidence");
        Objects.requireNonNull(requestedAt, "requestedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
