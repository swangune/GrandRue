package grandrue.surface;

/** Owner-qualified reference to an authoritative projection source. */
public record ProjectionSourceDependencyReference(
        String ownerIdentifier,
        String sourceIdentifier
) {
    public ProjectionSourceDependencyReference {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(sourceIdentifier, "Source identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
