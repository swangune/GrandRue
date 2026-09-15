package mainstreet.api;

/**
 * Owner-qualified reference to the accepted application operation, query,
 * callback responsibility or media coordination contract exposed by an API
 * contract. The reference does not transfer semantic ownership to transport.
 */
public record ApiOwnerContractReference(
        String ownerIdentifier,
        String contractIdentifier
) {
    public ApiOwnerContractReference {
        requireIdentifier(ownerIdentifier, "API owner identifier");
        requireIdentifier(contractIdentifier, "Owner contract identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
