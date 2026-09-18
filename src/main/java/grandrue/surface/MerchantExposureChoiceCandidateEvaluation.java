package grandrue.surface;

import java.util.Objects;

/** One merchant-choice decision row bound to one exact E4-issued submission. */
public record MerchantExposureChoiceCandidateEvaluation(
        ExposureCandidateEvaluationBinding evaluationBinding,
        MerchantExposureChoiceEvaluationDecision decision
) {
    public MerchantExposureChoiceCandidateEvaluation {
        Objects.requireNonNull(evaluationBinding, "evaluationBinding");
        Objects.requireNonNull(decision, "decision");
    }
}
