package mainstreet.semantic.executable;

/**
 * Runtime-ready conditional requirement and the platform-owned semantic
 * condition that controls its applicability. Compilation preserves this
 * relationship but never evaluates it.
 */
public record ExecutableConditionalRequirementDefinition(
        String identifier,
        String applicabilityConditionIdentifier
) {

    public ExecutableConditionalRequirementDefinition {
        requireIdentifier(identifier, "Requirement identifier");
        requireIdentifier(
                applicabilityConditionIdentifier,
                "Applicability condition identifier"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
