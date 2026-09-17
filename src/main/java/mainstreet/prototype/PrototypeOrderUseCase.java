package mainstreet.prototype;

import grandrue.ordering.Order;
import grandrue.ordering.RequestedOrderPortion;

import java.util.List;
import java.util.Optional;

/**
 * Prototype application boundary for committing and reading Orders.
 * Transport maps into this boundary; authoritative semantics remain Ordering-owned.
 */
public interface PrototypeOrderUseCase {

    Order commit(
            String merchantIdentifier,
            String commandIdentifier,
            String orderIdentifier,
            List<RequestedOrderPortion> requestedPortions
    );

    Optional<Order> order(
            String merchantIdentifier,
            String orderIdentifier
    );
}
