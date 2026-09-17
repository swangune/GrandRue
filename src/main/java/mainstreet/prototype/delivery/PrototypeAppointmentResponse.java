package mainstreet.prototype.delivery;

import grandrue.scheduling.Appointment;

import java.time.Instant;

public record PrototypeAppointmentResponse(
        String merchantIdentifier,
        String appointmentIdentifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        Instant startsAt,
        Instant endsAt,
        String governingReleaseIdentifier,
        Instant confirmedAt
) {
    public static PrototypeAppointmentResponse from(Appointment appointment) {
        return new PrototypeAppointmentResponse(
                appointment.merchantScope().merchantIdentifier(),
                appointment.identifier(),
                appointment.customerContextIdentifier(),
                appointment.scheduledOperationIdentifier(),
                appointment.scheduledInterval().startsAt(),
                appointment.scheduledInterval().endsAt(),
                appointment.governingReleaseIdentifier(),
                appointment.confirmedAt()
        );
    }
}
