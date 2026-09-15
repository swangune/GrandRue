package mainstreet.surface;

/** Stable owner-qualified identity for one projection read purpose. */
public record ProjectionReadUseIdentity(
        String ownerIdentifier,
        String readUseIdentifier
) {
    public ProjectionReadUseIdentity {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(readUseIdentifier, "Read-use identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
