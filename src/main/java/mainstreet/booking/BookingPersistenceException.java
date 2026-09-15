package mainstreet.booking;

public final class BookingPersistenceException
        extends RuntimeException {

    public BookingPersistenceException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
