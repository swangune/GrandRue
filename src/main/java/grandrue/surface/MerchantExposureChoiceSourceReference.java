package grandrue.surface;

/**
 * Owner-qualified reference to current merchant policy evidence. The reference
 * is not the policy value and does not transfer ownership to Exposure.
 */
public record MerchantExposureChoiceSourceReference(
        String ownerIdentifier,
        String sourceIdentifier
) {
    public MerchantExposureChoiceSourceReference {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(sourceIdentifier, "Source identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
