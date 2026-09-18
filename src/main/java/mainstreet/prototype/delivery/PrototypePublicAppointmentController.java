package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypePublicAppointmentUseCase;
import grandrue.semantic.AllocationConflictException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/** Prototype-only public Appointment transport adapter. */
@RestController
@RequestMapping("/prototype/public/merchants/{merchantIdentifier}/appointments")
@Profile("prototype")
public final class PrototypePublicAppointmentController {

    private final PrototypePublicAppointmentUseCase appointments;

    public PrototypePublicAppointmentController(
            PrototypePublicAppointmentUseCase appointments
    ) {
        this.appointments = Objects.requireNonNull(appointments, "appointments");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrototypePublicAppointmentResponse confirm(
            @PathVariable String merchantIdentifier,
            @RequestHeader("Idempotency-Key") String commandIdentifier,
            @RequestBody PrototypePublicAppointmentRequest request
    ) {
        requireIdentifier(commandIdentifier, "Idempotency-Key");
        try {
            return PrototypePublicAppointmentResponse.from(
                    appointments.confirm(
                            merchantIdentifier,
                            commandIdentifier,
                            request.appointmentIdentifier(),
                            request.subjectReference(),
                            request.startsAt(),
                            request.endsAt()
                    ),
                    request.subjectReference()
            );
        } catch (AllocationConflictException conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Requested appointment capacity is no longer available"
            );
        } catch (IllegalArgumentException invalidOrInapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Public Appointment subject is not applicable"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    label + " must not be blank"
            );
        }
    }
}
