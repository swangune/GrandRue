package mainstreet.prototype.delivery;

import mainstreet.booking.Booking;

import java.time.Instant;

/** Public Booking receipt; internal booked/allocation identifiers are not exposed. */
public record PrototypePublicBookingResponse(
        String merchantIdentifier,
        String bookingIdentifier,
        String subjectReference,
        Instant startsAt,
        Instant endsAt,
        Instant confirmedAt
) {
    public static PrototypePublicBookingResponse from(
            Booking booking,
            String publicSubjectReference
    ) {
        return new PrototypePublicBookingResponse(
                booking.merchantScope().merchantIdentifier(),
                booking.identifier(),
                publicSubjectReference,
                booking.reservationWindow().startsAt(),
                booking.reservationWindow().endsAt(),
                booking.confirmedAt()
        );
    }
}
