package mainstreet.semantic.registry;

/** Reference from a field to a DataConcept owned by the same capability. */
public record CapabilityDataConceptReference(
        String identifier
) implements FieldSemanticBasis {
    public CapabilityDataConceptReference {
        requireIdentifier(identifier, "Capability DataConcept identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
