package mainstreet.prototype;

import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.Order;
import mainstreet.ordering.RequestedOrderPortion;

import java.util.List;
import java.util.Objects;

/** Public Ordering adapter resolving storefront proposition references before commit. */
public final class PrototypePublicOrderUseCase {

    private final PrototypeOrderUseCase orders;

    public PrototypePublicOrderUseCase(PrototypeOrderUseCase orders) {
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    public Order commit(
            String merchantIdentifier,
            String commandIdentifier,
            String orderIdentifier,
            List<PrototypePublicOrderPortion> publicPortions
    ) {
        Objects.requireNonNull(publicPortions, "publicPortions");
        if (publicPortions.isEmpty()) {
            throw new IllegalArgumentException("Order requires at least one portion");
        }
        List<RequestedOrderPortion> resolved = publicPortions.stream()
                .map(portion -> resolve(merchantIdentifier, portion))
                .toList();
        return orders.commit(
                merchantIdentifier,
                commandIdentifier,
                orderIdentifier,
                resolved
        );
    }

    private static RequestedOrderPortion resolve(
            String merchantIdentifier,
            PrototypePublicOrderPortion portion
    ) {
        PrototypeOrderingSubjectConfiguration.Subject subject =
                PrototypeOrderingSubjectConfiguration.byPublicReference(
                                merchantIdentifier,
                                portion.subjectReference()
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Public Ordering subject is not applicable"
                        ));
        return new RequestedOrderPortion(
                portion.portionIdentifier(),
                subject.orderableSubjectReference(),
                new CommittedQuantity(
                        portion.quantity(),
                        subject.orderQuantityUnitIdentifier()
                )
        );
    }
}
