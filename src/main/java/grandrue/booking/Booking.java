package grandrue.booking;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Merchant-owned reservation commitment. */
public record Booking(
        MerchantScope merchantScope,
        String identifier,
        String customerContextIdentifier,
        String bookedSubjectReference,
        BookingReservationWindow reservationWindow,
        String governingReleaseIdentifier,
        Instant confirmedAt
) {

    public Booking {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Booking identifier");
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(bookedSubjectReference, "Booked subject reference");
        Objects.requireNonNull(reservationWindow, "reservationWindow");
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(confirmedAt, "confirmedAt");
    }

    private static void requireIdentifier(String identifier, String label) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
