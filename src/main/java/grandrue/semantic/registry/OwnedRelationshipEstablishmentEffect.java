package grandrue.semantic.registry;

/** Declares establishment of one registered typed relationship. */
public record OwnedRelationshipEstablishmentEffect(
        String relationshipIdentifier
) implements OwnedOperationEffect {
    public OwnedRelationshipEstablishmentEffect {
        requireIdentifier(relationshipIdentifier);
    }

    private static void requireIdentifier(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Relationship identifier must not be blank"
            );
        }
    }
}
