package mainstreet.prototype.delivery;

import java.time.Instant;

/** Transport request DTO for the prototype Booking command surface. */
public record PrototypeBookingRequest(
        String bookingIdentifier,
        String customerContextIdentifier,
        String bookedSubjectReference,
        Instant startsAt,
        Instant endsAt
) {
    public PrototypeBookingRequest {
        requireIdentifier(bookingIdentifier, "Booking identifier");
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(bookedSubjectReference, "Booked subject reference");
        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException(
                    "Booking interval must have a positive duration"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
