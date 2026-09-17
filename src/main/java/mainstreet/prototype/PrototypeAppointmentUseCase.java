package mainstreet.prototype;

import grandrue.scheduling.Appointment;

import java.time.Instant;
import java.util.Optional;

/** Prototype application boundary for capability-owned Appointment execution. */
public interface PrototypeAppointmentUseCase {

    Appointment confirm(
            String merchantIdentifier,
            String commandIdentifier,
            String appointmentIdentifier,
            String customerContextIdentifier,
            String scheduledOperationIdentifier,
            Instant startsAt,
            Instant endsAt
    );

    Optional<Appointment> appointment(
            String merchantIdentifier,
            String appointmentIdentifier
    );
}
