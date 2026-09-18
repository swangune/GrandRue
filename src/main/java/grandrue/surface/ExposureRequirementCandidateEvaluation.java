package grandrue.surface;

import java.util.Objects;

/** One requirement decision row bound to one exact E4-issued submission. */
public record ExposureRequirementCandidateEvaluation(
        ExposureCandidateEvaluationBinding evaluationBinding,
        ExposureRequirementEvaluationDecision decision
) {
    public ExposureRequirementCandidateEvaluation {
        Objects.requireNonNull(evaluationBinding, "evaluationBinding");
        Objects.requireNonNull(decision, "decision");
    }
}
