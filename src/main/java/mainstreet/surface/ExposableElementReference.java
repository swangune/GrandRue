package mainstreet.surface;

/** Owner-qualified reference to one bounded exposable semantic/read element. */
public record ExposableElementReference(
        String ownerIdentifier,
        String elementIdentifier
) {
    public ExposableElementReference {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(elementIdentifier, "Element identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
