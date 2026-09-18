package grandrue.scheduling;

import grandrue.semantic.AllocationClaim;
import grandrue.semantic.DomainEvent;

import java.util.Objects;

public record AppointmentConfirmation(
        Appointment appointment,
        AllocationClaim allocationClaim,
        DomainEvent pendingEvent
) {
    public AppointmentConfirmation {
        Objects.requireNonNull(appointment, "appointment");
        Objects.requireNonNull(allocationClaim, "allocationClaim");
        Objects.requireNonNull(pendingEvent, "pendingEvent");
        if (!appointment.allocationClaimIdentifier().equals(
                allocationClaim.identifier()
        ) || !appointment.scheduledInterval().equals(allocationClaim.scope())) {
            throw new IllegalArgumentException(
                    "Appointment does not match allocation claim"
            );
        }
        if (!allocationClaim.useIdentifier().equals(appointment.identifier())) {
            throw new IllegalArgumentException(
                    "Allocation claim does not belong to appointment"
            );
        }
        if (!pendingEvent.subjectIdentifier().equals(appointment.identifier())) {
            throw new IllegalArgumentException(
                    "Pending event does not belong to appointment"
            );
        }
    }
}
