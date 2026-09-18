package grandrue.surface;

import java.util.Objects;

/** Exact semantic merchant-choice reference to runtime evaluator implementation binding. */
public record MerchantExposureChoiceEvaluatorBinding(
        MerchantExposureChoiceSourceReference reference,
        MerchantExposureChoiceEvaluator evaluator
) {
    public MerchantExposureChoiceEvaluatorBinding {
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(evaluator, "evaluator");
    }
}
