package mainstreet.semantic.executable;

/** Fully resolved semantic field target retained by an executable effect. */
public record ExecutableFieldReference(
        String ownerCapabilityIdentifier,
        String schemaIdentifier,
        long schemaVersion,
        String fieldIdentifier
) {
    public ExecutableFieldReference {
        requireIdentifier(ownerCapabilityIdentifier, "Field owner capability identifier");
        requireIdentifier(schemaIdentifier, "Schema identifier");
        if (schemaVersion < 1) {
            throw new IllegalArgumentException("Schema version must be positive");
        }
        requireIdentifier(fieldIdentifier, "Field identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
