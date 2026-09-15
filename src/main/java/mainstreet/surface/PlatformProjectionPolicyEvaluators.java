package mainstreet.surface;

import java.util.Set;
import java.util.function.Predicate;

import static mainstreet.surface.ProjectionPolicyEvaluationSupport.affected;
import static mainstreet.surface.ProjectionPolicyEvaluationSupport.binding;
import static mainstreet.surface.ProjectionPolicyEvaluationSupport.policy;
import static mainstreet.surface.ProjectionPolicyEvaluationSupport.reasonsFor;
import static mainstreet.surface.ProjectionPolicyEvaluationSupport.references;
import static mainstreet.surface.ProjectionPolicyEvaluationSupport.source;
import static mainstreet.surface.ProjectionPolicyEvaluationSupport.withReason;

/** Platform-owned Merchant Presence projection policy evaluators. */
final class PlatformProjectionPolicyEvaluators {

    private static final ProjectionSourceDependencyReference DESCRIPTOR =
            source("profile", "merchant-public-descriptor");

    private PlatformProjectionPolicyEvaluators() {
    }

    static Set<ProjectionPolicyEvaluatorBinding> bindings() {
        return Set.of(
                binding(
                        ProjectionPolicyCategory.FRESHNESS,
                        policy(
                                "platform",
                                "merchant-presence-current-owner-evidence"
                        ),
                        "merchant-presence-freshness",
                        PlatformProjectionPolicyEvaluators::presenceFreshness
                ),
                binding(
                        ProjectionPolicyCategory.SERVICEABILITY,
                        policy(
                                "platform",
                                "merchant-presence-truthful-serviceability"
                        ),
                        "merchant-presence-serviceability",
                        PlatformProjectionPolicyEvaluators::presenceServiceability
                ),
                binding(
                        ProjectionPolicyCategory.MISSING_EVIDENCE,
                        policy(
                                "platform",
                                "merchant-presence-reduced-or-unserviceable"
                        ),
                        "merchant-presence-missing-evidence",
                        PlatformProjectionPolicyEvaluators::presenceMissingEvidence
                ),
                binding(
                        ProjectionPolicyCategory.STALE_SERVING,
                        policy(
                                "platform",
                                "no-known-stale-profile-or-location"
                        ),
                        "merchant-presence-stale-serving",
                        PlatformProjectionPolicyEvaluators::presenceKnownStale
                )
        );
    }

    private static ProjectionPolicyAssessment presenceFreshness(
            ProjectionPolicyEvaluationContext context
    ) {
        return presenceIssue(context, value -> !value.isCurrent());
    }

    private static ProjectionPolicyAssessment presenceServiceability(
            ProjectionPolicyEvaluationContext context
    ) {
        ProjectionSourceEvidence descriptor =
                context.request().evidenceFor(DESCRIPTOR);
        if (descriptor.isCurrent()) {
            return ProjectionPolicyAssessment.fullyServiceable();
        }
        Set<ProjectionServiceabilityReasonCode> reasons =
                reasonsFor(Set.of(descriptor));
        return ProjectionPolicyAssessment.notServiceable(
                withReason(
                        reasons,
                        ProjectionServiceabilityReasonCode
                                .REQUIRED_SOURCE_NOT_CURRENT
                ),
                Set.of(DESCRIPTOR)
        );
    }

    private static ProjectionPolicyAssessment presenceMissingEvidence(
            ProjectionPolicyEvaluationContext context
    ) {
        return presenceIssue(
                context,
                ProjectionSourceEvidence::hasMissingOrUnverifiableEvidence
        );
    }

    private static ProjectionPolicyAssessment presenceKnownStale(
            ProjectionPolicyEvaluationContext context
    ) {
        return presenceIssue(context, ProjectionSourceEvidence::isKnownStale);
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
