package grandrue.merchantaccount;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Accepted normal Controller transfer intent after recipient acceptance evidence exists. */
public record MerchantControllerTransferCommand(
        String logicalRequestIdentity,
        MerchantScope merchantScope,
        String expectedCurrentControllerRelationshipIdentity,
        String initiatingControllerIdentity,
        String receivingIdentity,
        String replacementControllerRelationshipIdentity,
        String authenticationAssuranceEvidenceIdentity,
        String recipientAcceptanceEvidenceIdentity,
        Instant transferredAt
) {
    public MerchantControllerTransferCommand {
        require(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(expectedCurrentControllerRelationshipIdentity, "Expected Controller relationship");
        require(initiatingControllerIdentity, "Initiating Controller identity");
        require(receivingIdentity, "Receiving identity");
        if (initiatingControllerIdentity.equals(receivingIdentity)) {
            throw new IllegalArgumentException(
                    "Controller transfer requires a distinct receiving identity"
            );
        }
        require(replacementControllerRelationshipIdentity, "Replacement relationship identity");
        require(authenticationAssuranceEvidenceIdentity, "Authentication assurance evidence");
        require(recipientAcceptanceEvidenceIdentity, "Recipient acceptance evidence");
        Objects.requireNonNull(transferredAt, "transferredAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
