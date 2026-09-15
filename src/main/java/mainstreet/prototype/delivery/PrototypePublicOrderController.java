package mainstreet.prototype.delivery;

import mainstreet.inventory.InsufficientQuantityException;
import mainstreet.prototype.PrototypePublicOrderUseCase;
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

/** Prototype-only public Ordering transport adapter. */
@RestController
@RequestMapping("/prototype/public/merchants/{merchantIdentifier}/orders")
@Profile("prototype")
public final class PrototypePublicOrderController {

    private final PrototypePublicOrderUseCase orders;

    public PrototypePublicOrderController(PrototypePublicOrderUseCase orders) {
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrototypePublicOrderResponse commit(
            @PathVariable String merchantIdentifier,
            @RequestHeader("Idempotency-Key") String commandIdentifier,
            @RequestBody PrototypePublicOrderRequest request
    ) {
        requireIdentifier(commandIdentifier, "Idempotency-Key");
        try {
            return PrototypePublicOrderResponse.from(
                    orders.commit(
                            merchantIdentifier,
                            commandIdentifier,
                            request.orderIdentifier(),
                            request.toPublicPortions()
                    ),
                    request
            );
        } catch (InsufficientQuantityException conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Inventory is no longer available"
            );
        } catch (IllegalArgumentException invalidOrInapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Public Ordering subject is not applicable"
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
