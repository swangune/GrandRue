package grandrue.semantic.executable;

/**
 * Fully qualified semantic identity of a capability-owned Operational Object
 * type in an executable merchant model.
 */
public record ExecutableOperationalObjectTypeIdentity(
        String ownerCapabilityIdentifier,
        String objectIdentifier
) {
    public ExecutableOperationalObjectTypeIdentity {
        requireIdentifier(ownerCapabilityIdentifier, "Owner capability identifier");
        requireIdentifier(objectIdentifier, "Operational object identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
