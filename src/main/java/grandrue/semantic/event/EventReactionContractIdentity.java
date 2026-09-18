package grandrue.semantic.event;

/** Owner-qualified reaction contract identity. */
public record EventReactionContractIdentity(String ownerIdentifier, String contractIdentifier) {
    public EventReactionContractIdentity {
        EventContractIdentity.requireReference(ownerIdentifier, "ownerIdentifier");
        EventContractIdentity.requireReference(contractIdentifier, "contractIdentifier");
    }
}
