package grandrue.semantic.event;

/** Owner-qualified target responsibility reference. */
public record EventReactionTargetReference(String ownerIdentifier, String targetIdentifier) {
    public EventReactionTargetReference {
        EventContractIdentity.requireReference(ownerIdentifier, "ownerIdentifier");
        EventContractIdentity.requireReference(targetIdentifier, "targetIdentifier");
    }
}
