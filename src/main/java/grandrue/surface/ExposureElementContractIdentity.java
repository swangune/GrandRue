package grandrue.surface;

/** Stable owner-qualified identity for one Exposure Element Contract. */
public record ExposureElementContractIdentity(
        String ownerIdentifier,
        String contractIdentifier
) {
    public ExposureElementContractIdentity {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(contractIdentifier, "Contract identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
