package mainstreet.surface;

import java.util.List;
import java.util.Objects;

/**
 * Raw merchant-choice evaluator batch response. Result ordering carries no
 * semantic meaning; duplicates remain representable for E4 coverage checks.
 */
public record MerchantExposureChoiceBatchEvaluation(
        MerchantExposureChoiceSourceReference choiceSourceReference,
        List<MerchantExposureChoiceCandidateEvaluation> results
) {
    public MerchantExposureChoiceBatchEvaluation {
        Objects.requireNonNull(choiceSourceReference, "choiceSourceReference");
        results = List.copyOf(Objects.requireNonNull(results, "results"));
    }
}
