package grandrue.application;

/**
 * Reference to an authoritative fact owned elsewhere. Application orchestration
 * may report it as committed progress but does not become its owner.
 */
public record CommittedProgressReference(
        String ownerContextIdentifier,
        String factTypeIdentifier,
        String factReference
) {
    public CommittedProgressReference {
        require(ownerContextIdentifier, "ownerContextIdentifier");
        require(factTypeIdentifier, "factTypeIdentifier");
        require(factReference, "factReference");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
