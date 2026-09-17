package mainstreet.prototype;

import grandrue.booking.Booking;

import java.time.Instant;
import java.util.Optional;

/** Prototype application boundary for capability-owned Booking execution. */
public interface PrototypeBookingUseCase {

    Booking confirm(
            String merchantIdentifier,
            String commandIdentifier,
            String bookingIdentifier,
            String customerContextIdentifier,
            String bookedSubjectReference,
            Instant startsAt,
            Instant endsAt
    );

    Optional<Booking> booking(
            String merchantIdentifier,
            String bookingIdentifier
    );
}
