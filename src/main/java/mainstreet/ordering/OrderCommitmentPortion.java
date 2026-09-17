package mainstreet.ordering;

import grandrue.money.MonetaryAmount;

import java.util.Objects;

/**
 * Immutable original portion of a committed Order.
 *
 * <p>The current executable slice represents fixed/no-charge monetary terms
 * using an exact MonetaryAmount. Other accepted commercial-term shapes must be
 * introduced explicitly rather than represented by null or guessed values.</p>
 */
public record OrderCommitmentPortion(
        String identifier,
        String committedSubjectReference,
        CommittedQuantity quantity,
        MonetaryAmount committedUnitAmount,
        String commercialTermsProvenanceReference
) {
    public OrderCommitmentPortion {
        requireIdentifier(identifier, "Order commitment portion identifier");
        requireIdentifier(
                committedSubjectReference,
                "Committed subject reference"
        );
        Objects.requireNonNull(quantity);
        Objects.requireNonNull(committedUnitAmount);
        requireIdentifier(
                commercialTermsProvenanceReference,
                "Commercial terms provenance reference"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
