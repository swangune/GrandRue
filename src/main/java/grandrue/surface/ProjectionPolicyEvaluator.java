package grandrue.surface;

/** Side-effect-free deterministic evaluator for one exact policy binding. */
@FunctionalInterface
public interface ProjectionPolicyEvaluator {

    ProjectionPolicyAssessment evaluate(
            ProjectionPolicyEvaluationContext context
    );
}
