package mainstreet.booking;

import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.TimeWindowAllocationScope;

import java.util.Objects;

public record BookingConfirmation(
        Booking booking,
        AllocationClaim allocationClaim,
        DomainEvent pendingEvent
) {

    public BookingConfirmation {
        Objects.requireNonNull(booking, "booking");
        Objects.requireNonNull(allocationClaim, "allocationClaim");
        Objects.requireNonNull(pendingEvent, "pendingEvent");

        if (!allocationClaim.useIdentifier().equals(booking.identifier())) {
            throw new IllegalArgumentException(
                    "Allocation claim does not belong to booking"
            );
        }
        if (!(allocationClaim.scope() instanceof TimeWindowAllocationScope scope)
                || !scope.startsAt().equals(booking.reservationWindow().startsAt())
                || !scope.endsAt().equals(booking.reservationWindow().endsAt())) {
            throw new IllegalArgumentException(
                    "Allocation window does not match booking reservation"
            );
        }
        if (!pendingEvent.subjectIdentifier().equals(booking.identifier())) {
            throw new IllegalArgumentException(
                    "Pending event does not belong to booking"
            );
        }
    }
}
