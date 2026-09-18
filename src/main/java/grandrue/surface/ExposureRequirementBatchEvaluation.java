package grandrue.surface;

import java.util.List;
import java.util.Objects;

/**
 * Raw requirement evaluator batch response. Result ordering carries no semantic
 * meaning; duplicates remain representable so E4 can detect malformed coverage.
 */
public record ExposureRequirementBatchEvaluation(
        ExposureRequirementReference requirementReference,
        List<ExposureRequirementCandidateEvaluation> results
) {
    public ExposureRequirementBatchEvaluation {
        Objects.requireNonNull(requirementReference, "requirementReference");
        results = List.copyOf(Objects.requireNonNull(results, "results"));
    }
}
