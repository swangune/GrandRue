package mainstreet.prototype;

import java.math.BigDecimal;
import java.util.Objects;

/** Public storefront order intent expressed with a public subject reference. */
public record PrototypePublicOrderPortion(
        String portionIdentifier,
        String subjectReference,
        BigDecimal quantity
) {
    public PrototypePublicOrderPortion {
        requireIdentifier(portionIdentifier, "Portion identifier");
        requireIdentifier(subjectReference, "Subject reference");
        Objects.requireNonNull(quantity, "quantity");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
