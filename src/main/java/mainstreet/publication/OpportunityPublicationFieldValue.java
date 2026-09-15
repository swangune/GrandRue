package mainstreet.publication;

import java.util.Objects;

/**
 * One persisted merchant-owned value for a registered field in an Opportunity schema revision.
 *
 * <p>The field identifier carries semantic identity through the governing registered schema.
 * The canonical value is a persistence representation only; it does not create field semantics,
 * executable rules, or arbitrary metadata authority.</p>
 */
public record OpportunityPublicationFieldValue(
        String fieldIdentifier,
        String canonicalValue
) {
    public OpportunityPublicationFieldValue {
        if (fieldIdentifier == null || fieldIdentifier.isBlank()) {
            throw new IllegalArgumentException("Field identifier must not be blank");
        }
        Objects.requireNonNull(canonicalValue, "canonicalValue");
    }
}
