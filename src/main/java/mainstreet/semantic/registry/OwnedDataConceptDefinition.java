package mainstreet.semantic.registry;

/** Reusable DataConcept whose meaning is intentionally local to one capability. */
public record OwnedDataConceptDefinition(
        String identifier
) {
    public OwnedDataConceptDefinition {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Capability DataConcept identifier must not be blank"
            );
        }
    }
}
