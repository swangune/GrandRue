package mainstreet.semantic.executable;

import java.util.Objects;

/** Resolved registered field carried into one executable schema version. */
public record ExecutableFieldDefinition(
        String identifier,
        ExecutableFieldSemanticBasis semanticBasis
) {
    public ExecutableFieldDefinition {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Field identifier must not be blank"
            );
        }
        Objects.requireNonNull(semanticBasis);
    }
}
