package grandrue.surface;

import java.util.Set;
import java.util.function.Predicate;

import static grandrue.surface.ProjectionPolicyEvaluationSupport.affected;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.binding;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.policy;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.reasonsFor;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.references;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.source;
import static grandrue.surface.ProjectionPolicyEvaluationSupport.withReason;

/** Exposure-owned projection revocation evaluator. */
final class ExposureProjectionPolicyEvaluators {

    private static final ProjectionSourceDependencyReference DESCRIPTOR =
            source("profile", "merchant-public-descriptor");

    private ExposureProjectionPolicyEvaluators() {
    }

    static Set<ProjectionPolicyEvaluatorBinding> bindings() {
        return Set.of(binding(
                ProjectionPolicyCategory.REVOCATION,
                policy("exposure", "current-observation-restrictions"),
                "merchant-presence-revocation",
                ExposureProjectionPolicyEvaluators::presenceRevocation
        ));
    }

    private static ProjectionPolicyAssessment presenceRevocation(
            ProjectionPolicyEvaluationContext context
    ) {
        return presenceIssue(
                context,
                ProjectionSourceEvidence::hasRevocationConstraint
        );
    }

    private static ProjectionPolicyAssessment presenceIssue(
            ProjectionPolicyEvaluationContext context,
            Predicate<ProjectionSourceEvidence> predicate
    ) {
        Set<ProjectionSourceEvidence> affected = affected(context, predicate);
        if (affected.isEmpty()) {
            return ProjectionPolicyAssessment.fullyServiceable();
        }
        Set<ProjectionSourceDependencyReference> omitted = references(affected);
        Set<ProjectionServiceabilityReasonCode> reasons = reasonsFor(affected);
        if (omitted.contains(DESCRIPTOR)) {
            return ProjectionPolicyAssessment.notServiceable(
                    withReason(
                            reasons,
                            ProjectionServiceabilityReasonCode
                                    .REQUIRED_SOURCE_NOT_CURRENT
                    ),
                    omitted
            );
        }
        return ProjectionPolicyAssessment.reduced(
                withReason(
                        reasons,
                        ProjectionServiceabilityReasonCode
                                .REDUCED_TRUTHFUL_REPRESENTATION
                ),
                omitted
        );
    }
}
