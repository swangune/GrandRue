package grandrue.surface;

import java.util.Objects;

/** Exact category-qualified binding from policy identity to executable code. */
public record ProjectionPolicyEvaluatorBinding(
        ProjectionPolicyCategory policyCategory,
        ProjectionPolicyReference policyReference,
        ProjectionPolicyEvaluatorIdentity evaluatorIdentity,
        ProjectionPolicyEvaluator evaluator
) {
    public ProjectionPolicyEvaluatorBinding {
        policyCategory = Objects.requireNonNull(
                policyCategory,
                "policyCategory"
        );
        policyReference = Objects.requireNonNull(
                policyReference,
                "policyReference"
        );
        evaluatorIdentity = Objects.requireNonNull(
                evaluatorIdentity,
                "evaluatorIdentity"
        );
        evaluator = Objects.requireNonNull(evaluator, "evaluator");
    }
}
