package grandrue.surface;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/** Cross-owner mechanics for typed Projection Serviceability evaluators. */
final class ProjectionPolicyEvaluationSupport {

    private ProjectionPolicyEvaluationSupport() {
    }

    static Set<ProjectionSourceEvidence> affected(
            ProjectionPolicyEvaluationContext context,
            Predicate<ProjectionSourceEvidence> predicate
    ) {
        return context.request().sourceEvidence().stream()
                .filter(value -> !value.isNotApplicable())
                .filter(predicate)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    static Set<ProjectionSourceDependencyReference> references(
            Set<ProjectionSourceEvidence> evidence
    ) {
        return evidence.stream()
                .map(ProjectionSourceEvidence::sourceReference)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    static Set<ProjectionServiceabilityReasonCode> reasonsFor(
            Set<ProjectionSourceEvidence> evidence
    ) {
        Set<ProjectionServiceabilityReasonCode> reasons = new HashSet<>();
        for (ProjectionSourceEvidence value : evidence) {
            if (value.availability()
                    == ProjectionSourceAvailability.UNAVAILABLE) {
                reasons.add(
                        ProjectionServiceabilityReasonCode.SOURCE_UNAVAILABLE
                );
            }
            switch (value.completeness()) {
                case PARTIAL -> reasons.add(
                        ProjectionServiceabilityReasonCode
                                .SOURCE_EVIDENCE_PARTIAL
                );
                case MISSING -> reasons.add(
                        ProjectionServiceabilityReasonCode
                                .SOURCE_EVIDENCE_MISSING
                );
                case CORRUPT -> reasons.add(
                        ProjectionServiceabilityReasonCode
                                .SOURCE_EVIDENCE_CORRUPT
                );
                case UNVERIFIABLE -> reasons.add(
                        ProjectionServiceabilityReasonCode
                                .SOURCE_EVIDENCE_UNVERIFIABLE
                );
                default -> {
                }
            }
            if (value.observedProgressIdentifier().isEmpty()
                    || value.requiredCurrentProgressIdentifier().isEmpty()) {
                reasons.add(
                        ProjectionServiceabilityReasonCode
                                .SOURCE_EVIDENCE_MISSING
                );
            }
            if (value.isKnownStale()) {
                reasons.add(
                        ProjectionServiceabilityReasonCode.SOURCE_KNOWN_STALE
                );
            }
            if (value.revocationState()
                    == ProjectionSourceRevocationState.REVOKED) {
                reasons.add(
                        ProjectionServiceabilityReasonCode.SOURCE_REVOKED
                );
            } else if (value.revocationState()
                    == ProjectionSourceRevocationState.UNVERIFIABLE) {
                reasons.add(
                        ProjectionServiceabilityReasonCode
                                .SOURCE_EVIDENCE_UNVERIFIABLE
                );
            }
        }
        return Set.copyOf(reasons);
    }

    static Set<ProjectionServiceabilityReasonCode> withReason(
            Set<ProjectionServiceabilityReasonCode> reasons,
            ProjectionServiceabilityReasonCode extra
    ) {
        Set<ProjectionServiceabilityReasonCode> combined =
                new HashSet<>(reasons);
        combined.add(extra);
        return Set.copyOf(combined);
    }

    static ProjectionPolicyEvaluatorBinding binding(
            ProjectionPolicyCategory category,
            ProjectionPolicyReference reference,
            String evaluatorIdentifier,
            ProjectionPolicyEvaluator evaluator
    ) {
        return new ProjectionPolicyEvaluatorBinding(
                category,
                reference,
                new ProjectionPolicyEvaluatorIdentity(
                        reference.ownerIdentifier(),
                        evaluatorIdentifier,
                        "v1"
                ),
                evaluator
        );
    }

    static ProjectionSourceDependencyReference source(
            String owner,
            String identifier
    ) {
        return new ProjectionSourceDependencyReference(owner, identifier);
    }

    static ProjectionPolicyReference policy(
            String owner,
            String identifier
    ) {
        return new ProjectionPolicyReference(owner, identifier);
    }
}
