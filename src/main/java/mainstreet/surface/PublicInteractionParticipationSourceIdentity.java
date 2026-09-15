package mainstreet.surface;

/** Owner-qualified identity for one registered Public Interaction participation source. */
public record PublicInteractionParticipationSourceIdentity(
        String ownerIdentifier,
        String sourceIdentifier
) {
    public PublicInteractionParticipationSourceIdentity {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(sourceIdentifier, "Source identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
