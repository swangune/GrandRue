package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypePublicBookingUseCase;
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

/** Prototype-only public Booking transport adapter. */
@RestController
@RequestMapping("/prototype/public/merchants/{merchantIdentifier}/bookings")
@Profile("prototype")
public final class PrototypePublicBookingController {

    private final PrototypePublicBookingUseCase bookings;

    public PrototypePublicBookingController(PrototypePublicBookingUseCase bookings) {
        this.bookings = Objects.requireNonNull(bookings, "bookings");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrototypePublicBookingResponse confirm(
            @PathVariable String merchantIdentifier,
            @RequestHeader("Idempotency-Key") String commandIdentifier,
            @RequestBody PrototypePublicBookingRequest request
    ) {
        requireIdentifier(commandIdentifier, "Idempotency-Key");
        try {
            return PrototypePublicBookingResponse.from(
                    bookings.confirm(
                            merchantIdentifier,
                            commandIdentifier,
                            request.bookingIdentifier(),
                            request.subjectReference(),
                            request.startsAt(),
                            request.endsAt()
                    ),
                    request.subjectReference()
            );
        } catch (AllocationConflictException conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Requested booking capacity is no longer available"
            );
        } catch (IllegalArgumentException invalidOrInapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Public Booking subject is not applicable"
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
