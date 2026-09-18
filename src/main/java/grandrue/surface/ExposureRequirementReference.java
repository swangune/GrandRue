package grandrue.surface;

/** Owner-qualified reference to one bounded Exposure requirement. */
public record ExposureRequirementReference(
        String ownerIdentifier,
        String requirementIdentifier
) {
    public ExposureRequirementReference {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(requirementIdentifier, "Requirement identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
