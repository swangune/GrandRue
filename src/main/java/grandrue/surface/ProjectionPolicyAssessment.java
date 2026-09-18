package grandrue.surface;

import java.util.Objects;
import java.util.Set;

/** Immutable assessment returned by one exact policy evaluator. */
public record ProjectionPolicyAssessment(
        ProjectionServiceabilityOutcome outcome,
        Set<ProjectionServiceabilityReasonCode> reasonCodes,
        Set<ProjectionSourceDependencyReference> omittedSourceReferences
) {
    public ProjectionPolicyAssessment {
        outcome = Objects.requireNonNull(outcome, "outcome");
        reasonCodes = Set.copyOf(Objects.requireNonNull(
                reasonCodes,
                "reasonCodes"
        ));
        omittedSourceReferences = Set.copyOf(Objects.requireNonNull(
                omittedSourceReferences,
                "omittedSourceReferences"
        ));
        if (outcome == ProjectionServiceabilityOutcome.FULLY_SERVICEABLE
                && !omittedSourceReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "Fully serviceable assessment cannot omit sources"
            );
        }
        if (outcome == ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE
                && omittedSourceReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "Reduced assessment must identify omitted sources"
            );
        }
        if (outcome != ProjectionServiceabilityOutcome.FULLY_SERVICEABLE
                && reasonCodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Non-full assessment must contain a reason"
            );
        }
    }

    public static ProjectionPolicyAssessment fullyServiceable() {
        return new ProjectionPolicyAssessment(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                Set.of(),
                Set.of()
        );
    }

    public static ProjectionPolicyAssessment reduced(
            Set<ProjectionServiceabilityReasonCode> reasons,
            Set<ProjectionSourceDependencyReference> omittedSources
    ) {
        return new ProjectionPolicyAssessment(
                ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE,
                reasons,
                omittedSources
        );
    }

    public static ProjectionPolicyAssessment notServiceable(
            Set<ProjectionServiceabilityReasonCode> reasons,
            Set<ProjectionSourceDependencyReference> omittedSources
    ) {
        return new ProjectionPolicyAssessment(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                reasons,
                omittedSources
        );
    }
}
