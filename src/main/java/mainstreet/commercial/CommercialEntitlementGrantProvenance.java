package mainstreet.commercial;

/**
 * Identifies the authoritative commercial source from which an entitlement
 * grant was derived.
 *
 * <p>The source class distinguishes kinds of commercial authority while the
 * source identifier points to the specific authoritative source instance. This
 * value records provenance only; it does not make the source effective or
 * create semantic applicability.</p>
 *
 * <p>Governed by MS-PROT-056 v1.5.</p>
 */
public record CommercialEntitlementGrantProvenance(
        String sourceClassIdentifier,
        String sourceIdentifier
) {

    public CommercialEntitlementGrantProvenance {
        if (sourceClassIdentifier == null || sourceClassIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial grant source class identifier must not be blank"
            );
        }
        if (sourceIdentifier == null || sourceIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial grant source identifier must not be blank"
            );
        }
    }
}
