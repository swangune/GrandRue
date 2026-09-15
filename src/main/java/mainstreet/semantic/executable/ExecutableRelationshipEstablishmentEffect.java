package mainstreet.semantic.executable;

/** Resolved contract to establish one registered typed relationship. */
public record ExecutableRelationshipEstablishmentEffect(
        String relationshipIdentifier
) implements ExecutableOperationEffect {
    public ExecutableRelationshipEstablishmentEffect {
        if (relationshipIdentifier == null || relationshipIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Relationship identifier must not be blank"
            );
        }
    }
}
