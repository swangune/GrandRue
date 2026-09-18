package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeAppointmentUseCase;
import grandrue.semantic.AllocationConflictException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/prototype/merchants/{merchantIdentifier}/appointments")
@Profile("prototype")
public final class PrototypeAppointmentController {

    private final PrototypeAppointmentUseCase appointments;

    public PrototypeAppointmentController(PrototypeAppointmentUseCase appointments) {
        this.appointments = Objects.requireNonNull(appointments, "appointments");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrototypeAppointmentResponse confirm(
            @PathVariable String merchantIdentifier,
            @RequestHeader("Idempotency-Key") String commandIdentifier,
            @RequestBody PrototypeAppointmentRequest request
    ) {
        requireIdentifier(commandIdentifier, "Idempotency-Key");
        try {
            return PrototypeAppointmentResponse.from(appointments.confirm(
                    merchantIdentifier,
                    commandIdentifier,
                    request.appointmentIdentifier(),
                    request.customerContextIdentifier(),
                    request.scheduledOperationIdentifier(),
                    request.startsAt(),
                    request.endsAt()
            ));
        } catch (AllocationConflictException conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Requested appointment capacity is no longer available"
            );
        } catch (IllegalArgumentException invalidOrInapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Appointment operation is not applicable"
            );
        }
    }

    @GetMapping("/{appointmentIdentifier}")
    public PrototypeAppointmentResponse appointment(
            @PathVariable String merchantIdentifier,
            @PathVariable String appointmentIdentifier
    ) {
        return appointments.appointment(merchantIdentifier, appointmentIdentifier)
                .map(PrototypeAppointmentResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found"
                ));
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
