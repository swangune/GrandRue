package grandrue.semantic.event;

/** Stable fact-contract identity qualified by the semantic owner, not a consumer or delivery identity. */
public record EventContractIdentity(String ownerIdentifier, String contractIdentifier) {
    public EventContractIdentity {
        requireReference(ownerIdentifier, "ownerIdentifier");
        requireReference(contractIdentifier, "contractIdentifier");
    }

    static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
