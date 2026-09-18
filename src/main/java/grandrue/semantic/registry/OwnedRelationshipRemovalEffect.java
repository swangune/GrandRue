package grandrue.semantic.registry;

/** Declares removal of one registered typed relationship. */
public record OwnedRelationshipRemovalEffect(
        String relationshipIdentifier
) implements OwnedOperationEffect {
    public OwnedRelationshipRemovalEffect {
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
