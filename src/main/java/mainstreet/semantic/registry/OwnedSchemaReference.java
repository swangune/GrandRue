package mainstreet.semantic.registry;

/** Capability-local reference to one immutable schema version. */
public record OwnedSchemaReference(
        String schemaIdentifier,
        long schemaVersion
) {
    public OwnedSchemaReference {
        if (schemaIdentifier == null || schemaIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Schema identifier must not be blank"
            );
        }
        if (schemaVersion < 1) {
            throw new IllegalArgumentException(
                    "Schema version must be positive"
            );
        }
    }
}
