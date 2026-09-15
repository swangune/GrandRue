package mainstreet.semantic.executable;

/** Resolved reference to a DataConcept owned by the schema's capability. */
public record ExecutableCapabilityDataConceptReference(
        String identifier
) implements ExecutableFieldSemanticBasis {
    public ExecutableCapabilityDataConceptReference {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Capability DataConcept identifier must not be blank"
            );
        }
    }
}
