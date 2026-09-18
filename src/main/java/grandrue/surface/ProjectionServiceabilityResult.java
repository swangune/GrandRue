package grandrue.surface;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable exact-evidence result of one Projection Serviceability evaluation. */
public record ProjectionServiceabilityResult(
        String semanticRegistryReleaseIdentifier,
        ProjectionContractIdentity contractIdentity,
        ProjectionReadUseIdentity readUseIdentity,
        Instant evaluatedAt,
        ProjectionServiceabilityOutcome outcome,
        Set<ProjectionServiceabilityReasonCode> reasonCodes,
        Set<ProjectionConsumedPolicyEvaluator> consumedPolicyEvaluators,
        Set<ProjectionSourceEvidence> sourceEvidence,
        Set<ProjectionSourceDependencyReference> omittedSourceReferences,
        Set<ProjectionPolicyReference> unresolvedPolicyReferences,
        Optional<BoundedProjectionReadBinding> boundedReadBinding
) {
    public ProjectionServiceabilityResult(
            String semanticRegistryReleaseIdentifier,
            ProjectionContractIdentity contractIdentity,
            ProjectionReadUseIdentity readUseIdentity,
            Instant evaluatedAt,
            ProjectionServiceabilityOutcome outcome,
            Set<ProjectionServiceabilityReasonCode> reasonCodes,
            Set<ProjectionConsumedPolicyEvaluator> consumedPolicyEvaluators,
            Set<ProjectionSourceEvidence> sourceEvidence,
            Set<ProjectionSourceDependencyReference> omittedSourceReferences,
            Set<ProjectionPolicyReference> unresolvedPolicyReferences
    ) {
        this(
                semanticRegistryReleaseIdentifier,
                contractIdentity,
                readUseIdentity,
                evaluatedAt,
                outcome,
                reasonCodes,
                consumedPolicyEvaluators,
                sourceEvidence,
                omittedSourceReferences,
                unresolvedPolicyReferences,
                Optional.empty()
        );
    }

    public ProjectionServiceabilityResult {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        contractIdentity = Objects.requireNonNull(
                contractIdentity,
                "contractIdentity"
        );
        readUseIdentity = Objects.requireNonNull(
                readUseIdentity,
                "readUseIdentity"
        );
        evaluatedAt = Objects.requireNonNull(evaluatedAt, "evaluatedAt");
        outcome = Objects.requireNonNull(outcome, "outcome");
        reasonCodes = immutable(reasonCodes, "reasonCodes");
        consumedPolicyEvaluators = immutable(
                consumedPolicyEvaluators,
                "consumedPolicyEvaluators"
        );
        sourceEvidence = immutable(sourceEvidence, "sourceEvidence");
        omittedSourceReferences = immutable(
                omittedSourceReferences,
                "omittedSourceReferences"
        );
        unresolvedPolicyReferences = immutable(
                unresolvedPolicyReferences,
                "unresolvedPolicyReferences"
        );
        boundedReadBinding = Objects.requireNonNull(
                boundedReadBinding,
                "boundedReadBinding"
        );
        validateProvenance(
                consumedPolicyEvaluators,
                sourceEvidence,
                omittedSourceReferences
        );
        if (outcome != ProjectionServiceabilityOutcome.FULLY_SERVICEABLE
                && reasonCodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Non-full result must contain a reason"
            );
        }
        if (outcome == ProjectionServiceabilityOutcome.FULLY_SERVICEABLE
                && (!omittedSourceReferences.isEmpty()
                || !unresolvedPolicyReferences.isEmpty())) {
            throw new IllegalArgumentException(
                    "Fully serviceable result cannot omit sources or policies"
            );
        }
        if (outcome == ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE
                && omittedSourceReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "Reduced result must identify omitted sources"
            );
        }
    }

    /**
     * Returns whether this result preserves P2 provenance affinity to the exact bounded-read
     * carrier supplied by Main Street. This is an identity-only correlation check; it grants no
     * authorization and introduces no alternate semantic facts.
     */
    public boolean hasExactBoundedReadAffinity(BoundedProjectionRead boundedRead) {
        Objects.requireNonNull(boundedRead, "boundedRead");
        return boundedReadBinding
                .map(binding -> binding == boundedRead.binding())
                .orElse(false);
    }

    private static <T> Set<T> immutable(Set<T> values, String label) {
        return Set.copyOf(Objects.requireNonNull(values, label));
    }

    private static void validateProvenance(
            Set<ProjectionConsumedPolicyEvaluator> consumed,
            Set<ProjectionSourceEvidence> evidence,
            Set<ProjectionSourceDependencyReference> omitted
    ) {
        Set<ProjectionSourceDependencyReference> sourceReferences =
                new HashSet<>();
        for (ProjectionSourceEvidence value : evidence) {
            if (!sourceReferences.add(value.sourceReference())) {
                throw new IllegalArgumentException(
                        "Duplicate result source evidence: "
                                + value.sourceReference()
                );
            }
        }
        if (!sourceReferences.containsAll(omitted)) {
            throw new IllegalArgumentException(
                    "Omitted result source has no retained evidence"
            );
        }

        Set<String> policyBindingIdentities = new HashSet<>();
        for (ProjectionConsumedPolicyEvaluator value : consumed) {
            String identity = value.policyCategory().name()
                    + "\u0000"
                    + value.policyReference().ownerIdentifier()
                    + "\u0000"
                    + value.policyReference().policyIdentifier();
            if (!policyBindingIdentities.add(identity)) {
                throw new IllegalArgumentException(
                        "Duplicate consumed policy binding: " + identity
                );
            }
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
