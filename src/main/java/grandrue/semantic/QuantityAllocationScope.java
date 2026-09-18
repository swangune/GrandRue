package grandrue.semantic;

public record QuantityAllocationScope(
        String subjectIdentifier,
        long quantity
) implements AllocationScope {

    public QuantityAllocationScope {
        if (subjectIdentifier == null
                || subjectIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation subject identifier must not be blank"
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Allocation quantity must be positive"
            );
        }
    }
}
