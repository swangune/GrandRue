package mainstreet.booking;

import java.util.function.Function;

public interface BookingUnitOfWork {

    BookingConfirmation execute(
            ConfirmBookingCommand command,
            Function<BookingTransaction, BookingConfirmation> work
    );
}
