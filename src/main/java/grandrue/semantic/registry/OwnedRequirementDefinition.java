package grandrue.semantic.registry;

/**
 * Platform-owned unconditional prerequisite attached to one registered
 * operation. Runtime satisfaction remains separate from applicability.
 */
public record OwnedRequirementDefinition(String identifier) {

    public OwnedRequirementDefinition {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Requirement identifier must not be blank"
            );
        }
    }
}
