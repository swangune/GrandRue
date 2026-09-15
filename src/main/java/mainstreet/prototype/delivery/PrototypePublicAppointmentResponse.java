package mainstreet.prototype.delivery;

import mainstreet.scheduling.Appointment;

import java.time.Instant;

/** Public Appointment receipt; internal execution and customer-context identifiers are omitted. */
public record PrototypePublicAppointmentResponse(
        String merchantIdentifier,
        String appointmentIdentifier,
        String subjectReference,
        Instant startsAt,
        Instant endsAt,
        Instant confirmedAt
) {
    public static PrototypePublicAppointmentResponse from(
            Appointment appointment,
            String publicSubjectReference
    ) {
        return new PrototypePublicAppointmentResponse(
                appointment.merchantScope().merchantIdentifier(),
                appointment.identifier(),
                publicSubjectReference,
                appointment.scheduledInterval().startsAt(),
                appointment.scheduledInterval().endsAt(),
                appointment.confirmedAt()
        );
    }
}
