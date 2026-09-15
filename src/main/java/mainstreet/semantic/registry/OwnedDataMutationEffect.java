package mainstreet.semantic.registry;

/** Declares authority to mutate one registered field on one Operational Object. */
public record OwnedDataMutationEffect(
        String targetObjectIdentifier,
        String targetFieldIdentifier
) implements OwnedOperationEffect {
    public OwnedDataMutationEffect {
        requireIdentifier(targetObjectIdentifier, "Target object identifier");
        requireIdentifier(targetFieldIdentifier, "Target field identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
