package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeBookingUseCase;
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

/** Prototype-only Booking transport adapter. */
@RestController
@RequestMapping("/prototype/merchants/{merchantIdentifier}/bookings")
@Profile("prototype")
public final class PrototypeBookingController {

    private final PrototypeBookingUseCase bookings;

    public PrototypeBookingController(PrototypeBookingUseCase bookings) {
        this.bookings = Objects.requireNonNull(bookings, "bookings");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrototypeBookingResponse confirm(
            @PathVariable String merchantIdentifier,
            @RequestHeader("Idempotency-Key") String commandIdentifier,
            @RequestBody PrototypeBookingRequest request
    ) {
        requireIdentifier(commandIdentifier, "Idempotency-Key");
        try {
            return PrototypeBookingResponse.from(bookings.confirm(
                    merchantIdentifier,
                    commandIdentifier,
                    request.bookingIdentifier(),
                    request.customerContextIdentifier(),
                    request.bookedSubjectReference(),
                    request.startsAt(),
                    request.endsAt()
            ));
        } catch (AllocationConflictException conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Requested booking capacity is no longer available"
            );
        } catch (IllegalArgumentException invalidOrInapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking operation is not applicable"
            );
        }
    }

    @GetMapping("/{bookingIdentifier}")
    public PrototypeBookingResponse booking(
            @PathVariable String merchantIdentifier,
            @PathVariable String bookingIdentifier
    ) {
        return bookings.booking(merchantIdentifier, bookingIdentifier)
                .map(PrototypeBookingResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
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
