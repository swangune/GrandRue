package mainstreet.surface;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable server-established request for one exact serviceability decision. */
public record ProjectionServiceabilityEvaluationRequest(
        String semanticRegistryReleaseIdentifier,
        ProjectionContractIdentity contractIdentity,
        ProjectionReadUseIdentity readUseIdentity,
        Instant evaluatedAt,
        Set<ProjectionSourceEvidence> sourceEvidence,
        Optional<BoundedProjectionReadBinding> boundedReadBinding
) {
    public ProjectionServiceabilityEvaluationRequest(
            String semanticRegistryReleaseIdentifier,
            ProjectionContractIdentity contractIdentity,
            ProjectionReadUseIdentity readUseIdentity,
            Instant evaluatedAt,
            Set<ProjectionSourceEvidence> sourceEvidence
    ) {
        this(
                semanticRegistryReleaseIdentifier,
                contractIdentity,
                readUseIdentity,
                evaluatedAt,
                sourceEvidence,
                Optional.empty()
        );
    }

    public ProjectionServiceabilityEvaluationRequest {
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
        sourceEvidence = immutableEvidence(sourceEvidence);
        boundedReadBinding = Objects.requireNonNull(
                boundedReadBinding,
                "boundedReadBinding"
        );
    }

    public static ProjectionServiceabilityEvaluationRequest forBoundedRead(
            BoundedProjectionRead read,
            Instant evaluatedAt
    ) {
        Objects.requireNonNull(read, "read");
        return new ProjectionServiceabilityEvaluationRequest(
                read.semanticRegistryReleaseIdentifier(),
                read.contractIdentity(),
                read.readUseIdentity(),
                evaluatedAt,
                read.sourceEvidence(),
                Optional.of(read.binding())
        );
    }

    public ProjectionSourceEvidence evidenceFor(
            ProjectionSourceDependencyReference sourceReference
    ) {
        Objects.requireNonNull(sourceReference, "sourceReference");
        return sourceEvidence.stream()
                .filter(value -> value.sourceReference().equals(
                        sourceReference
                ))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Source evidence is absent: " + sourceReference
                ));
    }

    private static Set<ProjectionSourceEvidence> immutableEvidence(
            Set<ProjectionSourceEvidence> evidence
    ) {
        Set<ProjectionSourceEvidence> copy = Set.copyOf(
                Objects.requireNonNull(evidence, "sourceEvidence")
        );
        Set<ProjectionSourceDependencyReference> sources = new HashSet<>();
        for (ProjectionSourceEvidence value : copy) {
            Objects.requireNonNull(value, "source evidence");
            if (!sources.add(value.sourceReference())) {
                throw new IllegalArgumentException(
                        "Duplicate source evidence: "
                                + value.sourceReference()
                );
            }
        }
        return copy;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
