package mainstreet.background;

/** Stable owner-qualified responsibility contract identity; distinct from work, attempt and command IDs. */
public record BackgroundWorkContractIdentity(String ownerIdentifier, String contractIdentifier) {
    public BackgroundWorkContractIdentity {
        requireReference(ownerIdentifier, "ownerIdentifier");
        requireReference(contractIdentifier, "contractIdentifier");
    }
    static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be blank");
    }
}
