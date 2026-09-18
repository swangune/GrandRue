package grandrue.surface;

/** Capability-scoped identity for one registered surface contribution. */
public record SurfaceContributionIdentity(
        String ownerCapabilityIdentifier,
        String contributionIdentifier
) {
    public SurfaceContributionIdentity {
        requireIdentifier(
                ownerCapabilityIdentifier,
                "Owner capability identifier"
        );
        requireIdentifier(contributionIdentifier, "Contribution identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
