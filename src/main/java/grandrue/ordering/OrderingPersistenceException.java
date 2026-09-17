package grandrue.ordering;

/** Infrastructure persistence failure without reinterpretation as business rejection. */
public final class OrderingPersistenceException extends RuntimeException {
    public OrderingPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
