package grandrue.booking;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.TimeWindowAllocationScope;

import java.util.Objects;

/**
 * Booking reservation-commitment intent within an explicitly established
 * merchant boundary. Delivery adapters must resolve merchant scope and any
 * internal Allocation target rather than accept either blindly from
 * customer-controlled input.
 */
public record ConfirmBookingCommand(
        MerchantScope merchantScope,
        String identifier,
        String bookingIdentifier,
        String customerContextIdentifier,
        String bookedSubjectReference,
        BookingReservationWindow reservationWindow,
        TimeWindowAllocationScope allocationScope
) {

    public ConfirmBookingCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Command identifier");
        requireIdentifier(bookingIdentifier, "Booking identifier");
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(bookedSubjectReference, "Booked subject reference");
        Objects.requireNonNull(reservationWindow, "reservationWindow");
        Objects.requireNonNull(allocationScope, "allocationScope");

        if (!reservationWindow.startsAt().equals(allocationScope.startsAt())
                || !reservationWindow.endsAt().equals(allocationScope.endsAt())) {
            throw new IllegalArgumentException(
                    "Booking Allocation window must match reservation window"
            );
        }
    }

    private static void requireIdentifier(String identifier, String label) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
