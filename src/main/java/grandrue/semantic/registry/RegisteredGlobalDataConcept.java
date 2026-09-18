package grandrue.semantic.registry;

/** Globally reusable DataConcept published in one semantic registry release. */
public record RegisteredGlobalDataConcept(
        String identifier
) implements RegisteredDefinition {
    public RegisteredGlobalDataConcept {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Global DataConcept identifier must not be blank"
            );
        }
    }

    @Override
    public SemanticIdentity identity() {
        return new SemanticIdentity(identifier, SemanticDefinitionKind.DATA_CONCEPT);
    }
}
