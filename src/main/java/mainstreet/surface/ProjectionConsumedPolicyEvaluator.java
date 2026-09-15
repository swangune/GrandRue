package mainstreet.surface;

import java.util.Objects;

/** Exact policy and executable evaluator identity retained in a result. */
public record ProjectionConsumedPolicyEvaluator(
        ProjectionPolicyCategory policyCategory,
        ProjectionPolicyReference policyReference,
        ProjectionPolicyEvaluatorIdentity evaluatorIdentity
) {
    public ProjectionConsumedPolicyEvaluator {
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
    }
}
