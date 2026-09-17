package grandrue.fulfilment;

/** Stable Main Street identity for one fulfilment responsibility. */
public record FulfilmentRoleIdentity(
        String ownerContextIdentifier,
        String roleIdentifier
) {
    public FulfilmentRoleIdentity {
        requireIdentifier(ownerContextIdentifier, "Owner context identifier");
        requireIdentifier(roleIdentifier, "Role identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
