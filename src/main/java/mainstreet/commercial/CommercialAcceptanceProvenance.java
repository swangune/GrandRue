package mainstreet.commercial;

/**
 * Identifies the authoritative evidence/provenance for merchant acceptance of
 * one commercial agreement.
 *
 * <p>This value is a reference to acceptance provenance, not authentication,
 * payment evidence or agreement authority by itself.</p>
 */
public record CommercialAcceptanceProvenance(String identifier) {

    public CommercialAcceptanceProvenance {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial acceptance provenance identifier must not be blank"
            );
        }
    }
}
