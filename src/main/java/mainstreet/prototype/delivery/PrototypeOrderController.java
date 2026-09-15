package mainstreet.prototype.delivery;

import mainstreet.inventory.InsufficientQuantityException;
import mainstreet.prototype.PrototypeOrderUseCase;
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

/** Prototype-only Order transport adapter. */
@RestController
@RequestMapping("/prototype/merchants/{merchantIdentifier}/orders")
@Profile("prototype")
public final class PrototypeOrderController {

    private final PrototypeOrderUseCase orders;

    public PrototypeOrderController(PrototypeOrderUseCase orders) {
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrototypeOrderResponse commit(
            @PathVariable String merchantIdentifier,
            @RequestHeader("Idempotency-Key") String commandIdentifier,
            @RequestBody PrototypeOrderRequest request
    ) {
        requireIdentifier(commandIdentifier, "Idempotency-Key");
        try {
            return PrototypeOrderResponse.from(orders.commit(
                    merchantIdentifier,
                    commandIdentifier,
                    request.orderIdentifier(),
                    request.toRequestedPortions()
            ));
        } catch (InsufficientQuantityException conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Inventory is no longer available"
            );
        } catch (IllegalArgumentException invalidOrInapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Order operation is not applicable"
            );
        }
    }

    @GetMapping("/{orderIdentifier}")
    public PrototypeOrderResponse order(
            @PathVariable String merchantIdentifier,
            @PathVariable String orderIdentifier
    ) {
        return orders.order(merchantIdentifier, orderIdentifier)
                .map(PrototypeOrderResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found"
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
