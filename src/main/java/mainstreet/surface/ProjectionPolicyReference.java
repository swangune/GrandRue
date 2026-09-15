package mainstreet.surface;

/** Stable owner-qualified reference to one Projection Contract policy. */
public record ProjectionPolicyReference(
        String ownerIdentifier,
        String policyIdentifier
) {
    public ProjectionPolicyReference {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(policyIdentifier, "Policy identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
