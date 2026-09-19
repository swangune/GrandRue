package grandrue.observability;

/** Stable owner-qualified identity for one operational diagnosability contract. */
public record OperationalEvidenceContractIdentity(
        String ownerIdentifier,
        String contractIdentifier
) {
    public OperationalEvidenceContractIdentity {
        requireReference(ownerIdentifier, "ownerIdentifier");
        requireReference(contractIdentifier, "contractIdentifier");
    }

    static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }
}
