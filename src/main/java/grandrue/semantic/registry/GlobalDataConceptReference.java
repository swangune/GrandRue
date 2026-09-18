package grandrue.semantic.registry;

/** Reference from a capability-owned field to a globally registered DataConcept. */
public record GlobalDataConceptReference(
        String identifier
) implements FieldSemanticBasis {
    public GlobalDataConceptReference {
        requireIdentifier(identifier, "Global DataConcept identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
