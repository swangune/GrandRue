package mainstreet.ordering;

import java.util.Objects;

/** Customer/merchant intent before authoritative commercial revalidation. */
public record RequestedOrderPortion(
        String identifier,
        String subjectReference,
        CommittedQuantity quantity
) {
    public RequestedOrderPortion {
        requireIdentifier(identifier, "Requested portion identifier");
        requireIdentifier(subjectReference, "Requested subject reference");
        Objects.requireNonNull(quantity);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
