package grandrue.scheduling;

import java.util.Objects;

/**
 * Appointment-owned authority for constructing and staging authoritative
 * Appointment state. Application coordinators may invoke this contract but do
 * not acquire Appointment mutation ownership by doing so.
 */
@FunctionalInterface
public interface AppointmentConfirmationAuthority {

    Appointment confirm(
            AppointmentConfirmationRequest request,
            AppointmentMutation mutation
    );

    static AppointmentConfirmationAuthority standard() {
        return (request, mutation) -> {
            Objects.requireNonNull(request, "request");
            Objects.requireNonNull(mutation, "mutation");
            Appointment appointment = new Appointment(
                    request.merchantScope(),
                    request.appointmentIdentifier(),
                    request.customerContextIdentifier(),
                    request.scheduledOperationIdentifier(),
                    request.scheduledInterval(),
                    request.allocationClaimIdentifier(),
                    request.governingReleaseIdentifier(),
                    1,
                    request.confirmedAt()
            );
            mutation.recordAppointment(appointment);
            return appointment;
        };
    }
}
