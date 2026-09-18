package grandrue.semantic.executable;

/** Resolved reference to a globally registered DataConcept. */
public record ExecutableGlobalDataConceptReference(
        String identifier
) implements ExecutableFieldSemanticBasis {
    public ExecutableGlobalDataConceptReference {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Global DataConcept identifier must not be blank"
            );
        }
    }
}
