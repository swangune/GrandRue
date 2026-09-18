package grandrue.semantic.configuration;

/**
 * Explicit merchant choice of one value for a registered policy owned by an
 * already-selected capability.
 */
public record PolicySelection(
        String ownerCapabilityIdentifier,
        String policyIdentifier,
        String selectedValue
) {

    public PolicySelection {
        requireIdentifier(
                ownerCapabilityIdentifier,
                "Policy owner capability identifier"
        );
        requireIdentifier(policyIdentifier, "Policy identifier");
        requireIdentifier(selectedValue, "Selected policy value");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
