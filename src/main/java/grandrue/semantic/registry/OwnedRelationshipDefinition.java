package grandrue.semantic.registry;

import java.util.Objects;

/** Capability-owned definition of a typed relationship between business objects. */
public record OwnedRelationshipDefinition(
        String identifier,
        String role,
        String sourceObjectIdentifier,
        OwnedOperationalObjectTypeReference targetObjectType,
        RelationshipCardinality cardinality,
        RelationshipScopeConstraint scopeConstraint
) {
    public OwnedRelationshipDefinition {
        requireIdentifier(identifier, "Relationship identifier");
        requireIdentifier(role, "Relationship role");
        requireIdentifier(sourceObjectIdentifier, "Relationship source object identifier");
        Objects.requireNonNull(targetObjectType);
        Objects.requireNonNull(cardinality);
        Objects.requireNonNull(scopeConstraint);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
