package mainstreet.semantic.executable;

/** Resolved contract to remove one registered typed relationship. */
public record ExecutableRelationshipRemovalEffect(
        String relationshipIdentifier
) implements ExecutableOperationEffect {
    public ExecutableRelationshipRemovalEffect {
        if (relationshipIdentifier == null || relationshipIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Relationship identifier must not be blank"
            );
        }
    }
}
