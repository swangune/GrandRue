package mainstreet.ordering;

import java.math.BigDecimal;
import java.util.Objects;

/** Exact committed quantity with explicit unit semantics. */
public record CommittedQuantity(
        BigDecimal magnitude,
        String unitIdentifier
) {
    public CommittedQuantity {
        Objects.requireNonNull(magnitude);
        if (magnitude.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Committed quantity magnitude must be positive"
            );
        }
        magnitude = magnitude.stripTrailingZeros();
        if (unitIdentifier == null || unitIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Committed quantity unit identifier must not be blank"
            );
        }
    }
}
