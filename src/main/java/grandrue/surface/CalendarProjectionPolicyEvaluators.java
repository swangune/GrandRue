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

/** Calendar-owned projection policy evaluators. */
final class CalendarProjectionPolicyEvaluators {

    private static final ProjectionSourceDependencyReference APPOINTMENTS =
            source("appointment", "commitments");
    private static final ProjectionSourceDependencyReference BOOKING_TIMING =
            source("booking", "applicable-timing");
    private static final ProjectionReadUseIdentity AVAILABILITY_READ =
            new ProjectionReadUseIdentity("calendar", "availability-oriented");

    private CalendarProjectionPolicyEvaluators() {
    }

    static Set<ProjectionPolicyEvaluatorBinding> bindings() {
        return Set.of(
                binding(
                        ProjectionPolicyCategory.FRESHNESS,
                        policy(
                                "calendar",
                                "merchant-calendar-independent-source-evidence"
                        ),
                        "merchant-calendar-freshness",
                        CalendarProjectionPolicyEvaluators::calendarFreshness
                ),
                binding(
                        ProjectionPolicyCategory.SERVICEABILITY,
                        policy("calendar", "current-main-street-commitments"),
                        "calendar-committed-work-serviceability",
                        CalendarProjectionPolicyEvaluators
                                ::committedWorkServiceability
                ),
                binding(
                        ProjectionPolicyCategory.MISSING_EVIDENCE,
                        policy(
                                "calendar",
                                "independently-established-commitments-only"
                        ),
                        "calendar-committed-work-missing-evidence",
                        CalendarProjectionPolicyEvaluators
                                ::committedWorkMissingEvidence
                ),
                binding(
                        ProjectionPolicyCategory.STALE_SERVING,
                        policy(
                                "calendar",
                                "no-unproven-commitment-currentness"
                        ),
                        "calendar-committed-work-stale-serving",
                        CalendarProjectionPolicyEvaluators
                                ::committedWorkKnownStale
                )
        );
    }

    private static ProjectionPolicyAssessment calendarFreshness(
            ProjectionPolicyEvaluationContext context
    ) {
        if (isAvailabilityRead(context)) {
            return availabilityIssue(context, value -> !value.isCurrent());
        }
        return committedWorkIssue(context, value -> !value.isCurrent());
    }

    private static ProjectionPolicyAssessment committedWorkServiceability(
            ProjectionPolicyEvaluationContext context
    ) {
        if (hasCurrentCommitment(context)) {
            return ProjectionPolicyAssessment.fullyServiceable();
        }
        Set<ProjectionSourceEvidence> commitments = Set.of(
                context.request().evidenceFor(APPOINTMENTS),
                context.request().evidenceFor(BOOKING_TIMING)
        );
        return ProjectionPolicyAssessment.notServiceable(
                withReason(
                        reasonsFor(commitments),
                        ProjectionServiceabilityReasonCode
                                .REQUIRED_SOURCE_NOT_CURRENT
                ),
                references(commitments)
        );
    }

    private static ProjectionPolicyAssessment committedWorkMissingEvidence(
            ProjectionPolicyEvaluationContext context
    ) {
        return committedWorkIssue(
                context,
                ProjectionSourceEvidence::hasMissingOrUnverifiableEvidence
        );
    }

    private static ProjectionPolicyAssessment committedWorkKnownStale(
            ProjectionPolicyEvaluationContext context
    ) {
        return committedWorkIssue(
                context,
                ProjectionSourceEvidence::isKnownStale
        );
    }

    private static ProjectionPolicyAssessment committedWorkIssue(
            ProjectionPolicyEvaluationContext context,
            Predicate<ProjectionSourceEvidence> predicate
    ) {
        Set<ProjectionSourceEvidence> affected = affected(context, predicate);
        if (affected.isEmpty()) {
            return ProjectionPolicyAssessment.fullyServiceable();
        }
        Set<ProjectionSourceDependencyReference> omitted = references(affected);
        Set<ProjectionServiceabilityReasonCode> reasons = reasonsFor(affected);
        if (!hasCurrentCommitment(context)) {
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

    private static boolean hasCurrentCommitment(
            ProjectionPolicyEvaluationContext context
    ) {
        return context.request().evidenceFor(APPOINTMENTS).isCurrent()
                || context.request().evidenceFor(BOOKING_TIMING).isCurrent();
    }

    private static boolean isAvailabilityRead(
            ProjectionPolicyEvaluationContext context
    ) {
        return context.readUseContract().readUseIdentity().equals(
                AVAILABILITY_READ
        );
    }
}
