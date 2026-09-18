package grandrue.surface;

import java.util.Set;
import java.util.function.Predicate;

import static grandrue.surface.ProjectionPolicyEvaluationSupport.affected;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.binding;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.policy;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.reasonsFor;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.references;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.withReason;

/** Scheduling-owned availability projection policy evaluators. */
final class SchedulingProjectionPolicyEvaluators {

    private SchedulingProjectionPolicyEvaluators() {
    }

    static Set<ProjectionPolicyEvaluatorBinding> bindings() {
        return Set.of(
                binding(
                        ProjectionPolicyCategory.SERVICEABILITY,
                        policy(
                                "scheduling",
                                "all-required-availability-inputs-current"
                        ),
                        "calendar-availability-serviceability",
                        SchedulingProjectionPolicyEvaluators
                                ::availabilityServiceability
                ),
                binding(
                        ProjectionPolicyCategory.MISSING_EVIDENCE,
                        policy(
                                "scheduling",
                                "no-current-availability-claim"
                        ),
                        "calendar-availability-missing-evidence",
                        SchedulingProjectionPolicyEvaluators
                                ::availabilityMissingEvidence
                ),
                binding(
                        ProjectionPolicyCategory.STALE_SERVING,
                        policy(
                                "scheduling",
                                "no-stale-availability-claim"
                        ),
                        "calendar-availability-stale-serving",
                        SchedulingProjectionPolicyEvaluators
                                ::availabilityKnownStale
                )
        );
    }

    private static ProjectionPolicyAssessment availabilityServiceability(
            ProjectionPolicyEvaluationContext context
    ) {
        return availabilityIssue(context, value -> !value.isCurrent());
    }

    private static ProjectionPolicyAssessment availabilityMissingEvidence(
            ProjectionPolicyEvaluationContext context
    ) {
        return availabilityIssue(
                context,
                ProjectionSourceEvidence::hasMissingOrUnverifiableEvidence
        );
    }

    private static ProjectionPolicyAssessment availabilityKnownStale(
            ProjectionPolicyEvaluationContext context
    ) {
        return availabilityIssue(
                context,
                ProjectionSourceEvidence::isKnownStale
        );
    }

    private static ProjectionPolicyAssessment availabilityIssue(
            ProjectionPolicyEvaluationContext context,
            Predicate<ProjectionSourceEvidence> predicate
    ) {
        Set<ProjectionSourceEvidence> affected = affected(context, predicate);
        if (affected.isEmpty()) {
            return ProjectionPolicyAssessment.fullyServiceable();
        }
        return ProjectionPolicyAssessment.notServiceable(
                withReason(
                        reasonsFor(affected),
                        ProjectionServiceabilityReasonCode
                                .REQUIRED_SOURCE_NOT_CURRENT
                ),
                references(affected)
        );
    }
}
