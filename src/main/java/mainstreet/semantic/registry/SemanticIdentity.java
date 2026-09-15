package mainstreet.semantic.registry;

import java.util.Objects;

/**
 * Stable identity of a platform-owned semantic definition. Registry release
 * versioning is deliberately not part of semantic identity.
 */
public record SemanticIdentity(
        String identifier,
        SemanticDefinitionKind kind
) {

    public SemanticIdentity {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic definition identifier must not be blank"
            );
        }
        Objects.requireNonNull(kind);
    }
}
