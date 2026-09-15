package mainstreet.semantic.registry;

import java.util.Objects;

/** One registered semantic field within a capability-owned schema version. */
public record OwnedFieldDefinition(
        String identifier,
        FieldSemanticBasis semanticBasis
) {
    public OwnedFieldDefinition {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Field identifier must not be blank"
            );
        }
        Objects.requireNonNull(semanticBasis);
    }
}
