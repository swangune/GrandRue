package grandrue.merchantaccount;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Final CLOSING to CLOSED transition after external closure-readiness evidence. */
public record FinalizeMerchantAccountClosureCommand(
        String logicalRequestIdentity,
        MerchantScope merchantScope,
        String expectedCurrentControllerRelationshipIdentity,
        String closureReadinessEvidenceIdentity,
        String actingAuthorityIdentifier,
        Instant closedAt
) {
    public FinalizeMerchantAccountClosureCommand {
        require(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(expectedCurrentControllerRelationshipIdentity, "Expected Controller relationship");
        require(closureReadinessEvidenceIdentity, "Closure-readiness evidence");
        require(actingAuthorityIdentifier, "Acting authority");
        Objects.requireNonNull(closedAt, "closedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
