package mainstreet.api;

/** Owner-qualified logical API contract identity, independent from physical routing. */
public record ApiContractIdentity(
        String ownerIdentifier,
        String contractIdentifier
) {
    public ApiContractIdentity {
        requireIdentifier(ownerIdentifier, "API contract owner identifier");
        requireIdentifier(contractIdentifier, "API contract identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
