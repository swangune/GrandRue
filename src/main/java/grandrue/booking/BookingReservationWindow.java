package grandrue.booking;

import java.time.Instant;
import java.util.Objects;

/**
 * Booking-owned reservation scope for the current time-window slice. This is
 * commitment truth and is deliberately distinct from the Resource/capacity
 * subject used by an Allocation that protects the Booking.
 */
public record BookingReservationWindow(
        Instant startsAt,
        Instant endsAt
) {
    public BookingReservationWindow {
        Objects.requireNonNull(startsAt, "startsAt");
        Objects.requireNonNull(endsAt, "endsAt");
        if (!startsAt.isBefore(endsAt)) {
            throw new IllegalArgumentException(
                    "Booking reservation window must have positive duration"
            );
        }
    }
}
