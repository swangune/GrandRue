package grandrue.surface;

import java.util.Objects;

/** One exact candidate submission to one logical evaluator batch invocation. */
public record ExposureCandidateEvaluationSubmission(
        ExposureCandidateEvaluationBinding evaluationBinding,
        ExposureCandidateObservation candidate
) {
    public ExposureCandidateEvaluationSubmission {
        Objects.requireNonNull(evaluationBinding, "evaluationBinding");
        Objects.requireNonNull(candidate, "candidate");
    }
}
