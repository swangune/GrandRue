package mainstreet.semantic.registry;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable capability-owned structured data contract. */
public record OwnedSchemaDefinition(
        String identifier,
        long version,
        List<OwnedFieldDefinition> fields
) {
    public OwnedSchemaDefinition {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Schema identifier must not be blank"
            );
        }
        if (version < 1) {
            throw new IllegalArgumentException(
                    "Schema version must be positive"
            );
        }
        fields = List.copyOf(Objects.requireNonNull(fields));
        Set<String> identifiers = new HashSet<>();
        for (OwnedFieldDefinition field : fields) {
            if (!identifiers.add(field.identifier())) {
                throw new IllegalArgumentException(
                        "Schema contains a duplicate field: " + field.identifier()
                );
            }
        }
    }

    public Optional<OwnedFieldDefinition> field(String fieldIdentifier) {
        if (fieldIdentifier == null || fieldIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Field identifier must not be blank"
            );
        }
        return fields.stream()
                .filter(field -> field.identifier().equals(fieldIdentifier))
                .findFirst();
    }

    public OwnedSchemaReference reference() {
        return new OwnedSchemaReference(identifier, version);
    }
}
