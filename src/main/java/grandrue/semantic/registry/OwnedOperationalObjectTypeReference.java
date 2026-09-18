package grandrue.semantic.registry;

/**
 * Owner-qualified reference to a capability-owned Operational Object type.
 */
public record OwnedOperationalObjectTypeReference(
        String ownerCapabilityIdentifier,
        String objectIdentifier
) {
    public OwnedOperationalObjectTypeReference {
        requireIdentifier(ownerCapabilityIdentifier, "Owner capability identifier");
        requireIdentifier(objectIdentifier, "Operational object identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
