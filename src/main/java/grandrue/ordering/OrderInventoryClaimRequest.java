package grandrue.ordering;

import mainstreet.semantic.AllocationScope;

import java.util.Objects;

/** Inventory-owned claim requirement resolved for one Order commitment portion. */
public record OrderInventoryClaimRequest(
        String identifier,
        String commitmentPortionIdentifier,
        AllocationScope scope
) {
    public OrderInventoryClaimRequest {
        requireIdentifier(identifier, "Inventory claim request identifier");
        requireIdentifier(
                commitmentPortionIdentifier,
                "Order commitment portion identifier"
        );
        Objects.requireNonNull(scope);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
