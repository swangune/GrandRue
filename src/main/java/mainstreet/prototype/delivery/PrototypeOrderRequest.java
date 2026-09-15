package mainstreet.prototype.delivery;

import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.RequestedOrderPortion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/** Transport request DTO for the prototype Order command surface. */
public record PrototypeOrderRequest(
        String orderIdentifier,
        List<Portion> portions
) {

    public PrototypeOrderRequest {
        requireIdentifier(orderIdentifier, "Order identifier");
        portions = List.copyOf(Objects.requireNonNull(portions, "portions"));
        if (portions.isEmpty()) {
            throw new IllegalArgumentException("Order requires at least one portion");
        }
    }

    public List<RequestedOrderPortion> toRequestedPortions() {
        return portions.stream().map(Portion::toRequestedPortion).toList();
    }

    public record Portion(
            String portionIdentifier,
            String subjectReference,
            BigDecimal quantity,
            String unitIdentifier
    ) {
        public Portion {
            requireIdentifier(portionIdentifier, "Portion identifier");
            requireIdentifier(subjectReference, "Subject reference");
            Objects.requireNonNull(quantity, "quantity");
            requireIdentifier(unitIdentifier, "Unit identifier");
        }

        RequestedOrderPortion toRequestedPortion() {
            return new RequestedOrderPortion(
                    portionIdentifier,
                    subjectReference,
                    new CommittedQuantity(quantity, unitIdentifier)
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
