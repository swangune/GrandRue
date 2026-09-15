package mainstreet.ordering;

/** Stable command identity was reused for materially different Order intent. */
public final class OrderCommandIdentityConflictException
        extends IllegalArgumentException {
    public OrderCommandIdentityConflictException(String commandIdentifier) {
        super(
                "Order command identifier already used for different intent: "
                        + commandIdentifier
        );
    }
}
