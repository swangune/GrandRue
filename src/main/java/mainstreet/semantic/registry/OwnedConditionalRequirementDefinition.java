package mainstreet.semantic.registry;

/**
 * Platform-owned prerequisite whose applicability is determined by a
 * registered semantic condition at command-evaluation time. The condition is
 * an identity, not merchant-authored executable logic.
 */
public record OwnedConditionalRequirementDefinition(
        String identifier,
        String applicabilityConditionIdentifier
) {

    public OwnedConditionalRequirementDefinition {
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
