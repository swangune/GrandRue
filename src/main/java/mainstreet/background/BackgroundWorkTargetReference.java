package mainstreet.background;

/** Owner operation/process-evaluation reference. It is not a class name, handler or execution grant. */
public record BackgroundWorkTargetReference(String ownerIdentifier, String targetIdentifier) {
    public BackgroundWorkTargetReference {
        BackgroundWorkContractIdentity.requireReference(ownerIdentifier, "target owner");
        BackgroundWorkContractIdentity.requireReference(targetIdentifier, "target identifier");
    }
}
