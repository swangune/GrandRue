package grandrue.inventory;

public final class InsufficientQuantityException
        extends IllegalStateException {

    private final long requestedQuantity;
    private final long availableQuantity;

    public InsufficientQuantityException(
            String subjectIdentifier,
            long requestedQuantity,
            long availableQuantity
    ) {
        super(
                "Insufficient quantity for "
                        + subjectIdentifier
                        + ": requested "
                        + requestedQuantity
                        + ", available "
                        + availableQuantity
        );
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public long requestedQuantity() {
        return requestedQuantity;
    }

    public long availableQuantity() {
        return availableQuantity;
    }
}
