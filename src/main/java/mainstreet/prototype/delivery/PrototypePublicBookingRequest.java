package mainstreet.prototype.delivery;

import java.time.Instant;
import java.util.Objects;

/** Public storefront Booking intent; contains no Allocation identity. */
public record PrototypePublicBookingRequest(
        String bookingIdentifier,
        String subjectReference,
        Instant startsAt,
        Instant endsAt
) {
    public PrototypePublicBookingRequest {
        requireIdentifier(bookingIdentifier, "Booking identifier");
        requireIdentifier(subjectReference, "Subject reference");
        Objects.requireNonNull(startsAt, "startsAt");
        Objects.requireNonNull(endsAt, "endsAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
