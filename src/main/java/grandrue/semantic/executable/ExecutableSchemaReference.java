package grandrue.semantic.executable;

/** Fully owner-qualified reference to one immutable executable schema version. */
public record ExecutableSchemaReference(
        String ownerCapabilityIdentifier,
        String schemaIdentifier,
        long schemaVersion
) {
    public ExecutableSchemaReference {
        requireIdentifier(ownerCapabilityIdentifier, "Schema owner capability identifier");
        requireIdentifier(schemaIdentifier, "Schema identifier");
        if (schemaVersion < 1) {
            throw new IllegalArgumentException(
                    "Schema version must be positive"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
