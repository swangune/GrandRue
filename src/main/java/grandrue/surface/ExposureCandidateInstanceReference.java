package grandrue.surface;

/** Stable owner-local candidate instance identity used by Exposure evaluation. */
public record ExposureCandidateInstanceReference(
        String ownerIdentifier,
        String instanceKindIdentifier,
        String instanceIdentifier
) {
    public ExposureCandidateInstanceReference {
        requireIdentifier(ownerIdentifier, "ownerIdentifier");
        requireIdentifier(instanceKindIdentifier, "instanceKindIdentifier");
        requireIdentifier(instanceIdentifier, "instanceIdentifier");
    }

    private static void requireIdentifier(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
