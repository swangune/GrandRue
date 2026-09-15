package mainstreet.semantic.executable;

/**
 * Runtime-ready identity of an unconditionally applicable operation
 * requirement. Presence establishes applicability, not satisfaction.
 */
public record ExecutableRequirementDefinition(String identifier) {

    public ExecutableRequirementDefinition {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Requirement identifier must not be blank"
            );
        }
    }
}
