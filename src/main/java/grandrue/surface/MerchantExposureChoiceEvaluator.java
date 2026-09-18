package grandrue.surface;

import java.util.List;

/** Read-only owner extension point for exact merchant Exposure-choice evaluation. */
public interface MerchantExposureChoiceEvaluator {

    MerchantExposureChoiceBatchEvaluation evaluateBatch(
            MerchantExposureChoiceSourceReference reference,
            OwnerExposureEvaluationContext context,
            List<ExposureCandidateEvaluationSubmission> submissions
    );
}
