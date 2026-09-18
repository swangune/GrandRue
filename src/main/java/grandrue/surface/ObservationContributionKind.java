package grandrue.surface;

/** Owner-qualified identity of one capability observation contribution kind. */
public record ObservationContributionKind(
        String ownerCapabilityIdentifier,
        String contributionIdentifier
) {
    public ObservationContributionKind {
        requireIdentifier(
                ownerCapabilityIdentifier,
                "Owner capability identifier"
        );
        requireIdentifier(
                contributionIdentifier,
                "Contribution identifier"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
