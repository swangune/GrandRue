package mainstreet.customer;

/** Raised when a scoped identity is reused for a different relationship. */
public final class CustomerContextIdentityConflictException
        extends RuntimeException {

    public CustomerContextIdentityConflictException(String identifier) {
        super("Customer context identifier already represents another relationship: "
                + identifier);
    }
}
