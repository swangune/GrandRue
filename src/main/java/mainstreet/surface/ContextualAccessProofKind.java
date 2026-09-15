package mainstreet.surface;

/** Owner-qualified identity of one contextual-access proof kind. */
public record ContextualAccessProofKind(
        String ownerCapabilityIdentifier,
        String contextualAccessKindIdentifier
) {
    public ContextualAccessProofKind {
        requireIdentifier(
                ownerCapabilityIdentifier,
                "Owner capability identifier"
        );
        requireIdentifier(
                contextualAccessKindIdentifier,
                "Contextual-access kind identifier"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
