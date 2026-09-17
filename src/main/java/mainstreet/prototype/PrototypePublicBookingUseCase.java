package mainstreet.prototype;

import grandrue.booking.Booking;

import java.time.Instant;
import java.util.Objects;

/** Public Booking adapter resolving a storefront subject into Booking-owned input. */
public final class PrototypePublicBookingUseCase {

    private static final String PROTOTYPE_CUSTOMER_CONTEXT = "customer-1";

    private final PrototypeBookingUseCase bookings;

    public PrototypePublicBookingUseCase(PrototypeBookingUseCase bookings) {
        this.bookings = Objects.requireNonNull(bookings, "bookings");
    }

    public Booking confirm(
            String merchantIdentifier,
            String commandIdentifier,
            String bookingIdentifier,
            String publicSubjectReference,
            Instant startsAt,
            Instant endsAt
    ) {
        PrototypeBookingSubjectConfiguration.Subject subject =
                PrototypeBookingSubjectConfiguration.byPublicReference(
                                merchantIdentifier,
                                publicSubjectReference
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Public Booking subject is not applicable"
                        ));
        return bookings.confirm(
                merchantIdentifier,
                commandIdentifier,
                bookingIdentifier,
                PROTOTYPE_CUSTOMER_CONTEXT,
                subject.bookedSubjectReference(),
                startsAt,
                endsAt
        );
    }
}
