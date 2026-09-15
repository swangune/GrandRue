package mainstreet.surface;

/**
 * Owner-qualified identity of one registered CUSTOMER Surface eligibility
 * requirement under MS-PROT-049 v1.3.
 */
public record CustomerSurfaceEligibilityRequirementIdentity(
        String ownerIdentifier,
        String requirementIdentifier
) {
    public CustomerSurfaceEligibilityRequirementIdentity {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(requirementIdentifier, "Requirement identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
