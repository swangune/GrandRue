package grandrue.notification;

/** Semantic notification audience relation, deliberately not a delivery endpoint. */
public record NotificationRecipient(
        String recipientIdentity,
        String recipientKindIdentifier,
        String semanticReference,
        NotificationRecipientResolutionBasis resolutionBasis
) {
    public NotificationRecipient {
        require(recipientIdentity, "Recipient identity");
        require(recipientKindIdentifier, "Recipient kind");
        require(semanticReference, "Recipient semantic reference");
        if (resolutionBasis == null) {
            throw new NullPointerException("resolutionBasis");
        }
    }

    public boolean requiresCurrentRevalidation() {
        return resolutionBasis == NotificationRecipientResolutionBasis.CURRENT_RELATIONSHIP;
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
