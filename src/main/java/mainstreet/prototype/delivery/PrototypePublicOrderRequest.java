package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypePublicOrderPortion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/** Public storefront Order intent using public proposition references. */
public record PrototypePublicOrderRequest(
        String orderIdentifier,
        List<Portion> portions
) {
    public PrototypePublicOrderRequest {
        requireIdentifier(orderIdentifier, "Order identifier");
        portions = List.copyOf(Objects.requireNonNull(portions, "portions"));
        if (portions.isEmpty()) {
            throw new IllegalArgumentException("Order requires at least one portion");
        }
    }

    public List<PrototypePublicOrderPortion> toPublicPortions() {
        return portions.stream().map(Portion::toPublicPortion).toList();
    }

    public record Portion(
            String portionIdentifier,
            String subjectReference,
            BigDecimal quantity
    ) {
        public Portion {
            requireIdentifier(portionIdentifier, "Portion identifier");
            requireIdentifier(subjectReference, "Subject reference");
            Objects.requireNonNull(quantity, "quantity");
        }

        PrototypePublicOrderPortion toPublicPortion() {
            return new PrototypePublicOrderPortion(
                    portionIdentifier,
                    subjectReference,
                    quantity
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
