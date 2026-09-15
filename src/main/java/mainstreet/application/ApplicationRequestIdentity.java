package mainstreet.application;

/**
 * Stable identity for one logical caller intention reaching one application use
 * case. It is distinct from transport, trace, merchant and business-object IDs.
 */
public record ApplicationRequestIdentity(String value) {
    public ApplicationRequestIdentity {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Application request identity must not be blank");
        }
    }
}
