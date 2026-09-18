package grandrue.semantic.executable;

import mainstreet.semantic.registry.RelationshipCardinality;
import mainstreet.semantic.registry.RelationshipScopeConstraint;

import java.util.Objects;

/** Resolved typed relationship definition carried into an executable model. */
public record ExecutableRelationshipDefinition(
        String identifier,
        String role,
        ExecutableOperationalObjectTypeIdentity sourceObjectType,
        ExecutableOperationalObjectTypeIdentity targetObjectType,
        RelationshipCardinality cardinality,
        RelationshipScopeConstraint scopeConstraint
) {
    public ExecutableRelationshipDefinition {
        requireIdentifier(identifier, "Relationship identifier");
        requireIdentifier(role, "Relationship role");
        Objects.requireNonNull(sourceObjectType);
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
