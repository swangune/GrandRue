package mainstreet.inventory;

import java.time.Instant;
import java.util.Objects;

public record InventoryMovement(
        String identifier,
        String subjectIdentifier,
        long quantity,
        String allocationClaimIdentifier,
        Instant occurredAt
) {

    public InventoryMovement {
        requireIdentifier(identifier, "Inventory movement identifier");
        requireIdentifier(subjectIdentifier, "Stock subject identifier");
        requireIdentifier(
                allocationClaimIdentifier,
                "Allocation claim identifier"
        );

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Inventory movement quantity must be positive"
            );
        }

        Objects.requireNonNull(occurredAt);
    }

    private static void requireIdentifier(
            String identifier,
            String label
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }
}
