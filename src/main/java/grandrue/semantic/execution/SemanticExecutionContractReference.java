package grandrue.semantic.execution;

/**
 * Exact reference to one semantic execution contract within one immutable
 * Semantic Registry Release. The reference is technical affinity evidence;
 * it does not define or amend the referenced semantics.
 */
public record SemanticExecutionContractReference(
        String semanticRegistryReleaseIdentifier,
        String contractIdentifier
) {
    public SemanticExecutionContractReference {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        requireIdentifier(contractIdentifier, "Execution contract identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
