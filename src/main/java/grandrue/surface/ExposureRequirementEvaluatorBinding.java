package grandrue.surface;

import java.util.Objects;

/** Exact semantic requirement reference to runtime evaluator implementation binding. */
public record ExposureRequirementEvaluatorBinding(
        ExposureRequirementReference reference,
        ExposureRequirementEvaluator evaluator
) {
    public ExposureRequirementEvaluatorBinding {
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(evaluator, "evaluator");
    }
}
