package mainstreet.prototype.delivery;

import grandrue.booking.Booking;

import java.time.Instant;

/** Read/command response DTO; internal Allocation identity is not Booking truth. */
public record PrototypeBookingResponse(
        String merchantIdentifier,
        String bookingIdentifier,
        String customerContextIdentifier,
        String bookedSubjectReference,
        Instant startsAt,
        Instant endsAt,
        String governingReleaseIdentifier,
        Instant confirmedAt
) {

    public static PrototypeBookingResponse from(Booking booking) {
        return new PrototypeBookingResponse(
                booking.merchantScope().merchantIdentifier(),
                booking.identifier(),
                booking.customerContextIdentifier(),
                booking.bookedSubjectReference(),
                booking.reservationWindow().startsAt(),
                booking.reservationWindow().endsAt(),
                booking.governingReleaseIdentifier(),
                booking.confirmedAt()
        );
    }
}
