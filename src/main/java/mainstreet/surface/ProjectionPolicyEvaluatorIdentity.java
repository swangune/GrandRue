package mainstreet.surface;

/** Stable owner-qualified and versioned identity for an executable evaluator. */
public record ProjectionPolicyEvaluatorIdentity(
        String ownerIdentifier,
        String evaluatorIdentifier,
        String evaluatorVersionIdentifier
) {
    public ProjectionPolicyEvaluatorIdentity {
        requireIdentifier(ownerIdentifier, "Owner identifier");
        requireIdentifier(evaluatorIdentifier, "Evaluator identifier");
        requireIdentifier(
                evaluatorVersionIdentifier,
                "Evaluator version identifier"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
