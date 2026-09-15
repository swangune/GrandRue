package mainstreet.semantic.executable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Resolved immutable schema version in one executable merchant model. */
public record ExecutableSchemaDefinition(
        String ownerCapabilityIdentifier,
        String identifier,
        long version,
        List<ExecutableFieldDefinition> fields
) {
    public ExecutableSchemaDefinition {
        requireIdentifier(ownerCapabilityIdentifier, "Schema owner capability identifier");
        requireIdentifier(identifier, "Schema identifier");
        if (version < 1) {
            throw new IllegalArgumentException("Schema version must be positive");
        }
        fields = List.copyOf(Objects.requireNonNull(fields));
        Set<String> identifiers = new HashSet<>();
        for (ExecutableFieldDefinition field : fields) {
            if (!identifiers.add(field.identifier())) {
                throw new IllegalArgumentException(
                        "Executable schema contains a duplicate field: "
                                + field.identifier()
                );
            }
        }
    }

    public Optional<ExecutableFieldDefinition> field(String fieldIdentifier) {
        requireIdentifier(fieldIdentifier, "Field identifier");
        return fields.stream()
                .filter(field -> field.identifier().equals(fieldIdentifier))
                .findFirst();
    }

    public ExecutableSchemaReference reference() {
        return new ExecutableSchemaReference(
                ownerCapabilityIdentifier,
                identifier,
                version
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
