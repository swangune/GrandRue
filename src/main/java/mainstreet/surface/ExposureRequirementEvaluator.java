package mainstreet.surface;

import java.util.List;

/** Read-only owner extension point for exact requirement evaluation. */
public interface ExposureRequirementEvaluator {

    ExposureRequirementBatchEvaluation evaluateBatch(
            ExposureRequirementReference reference,
            OwnerExposureEvaluationContext context,
            List<ExposureCandidateEvaluationSubmission> submissions
    );
}
