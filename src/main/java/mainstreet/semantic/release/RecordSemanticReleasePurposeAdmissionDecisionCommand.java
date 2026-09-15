package mainstreet.semantic.release;

import java.time.Instant;
import java.util.Objects;

/** Exact immutable intent for recording a release-purpose decision. */
public record RecordSemanticReleasePurposeAdmissionDecisionCommand(
        String admissionDecisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        SemanticReleasePurpose purpose,
        SemanticReleaseAdmissionDisposition disposition,
        String decidingPrincipalIdentifier,
        String decisionProvenanceReference,
        Instant decidedAt
) {
    public RecordSemanticReleasePurposeAdmissionDecisionCommand {
        new SemanticReleasePurposeAdmissionDecision(
                admissionDecisionIdentifier,
                semanticRegistryReleaseIdentifier,
                purpose,
                disposition,
                decidingPrincipalIdentifier,
                decisionProvenanceReference,
                Objects.requireNonNull(decidedAt, "decidedAt")
        );
    }

    public SemanticReleasePurposeAdmissionDecision decision() {
        return new SemanticReleasePurposeAdmissionDecision(
                admissionDecisionIdentifier,
                semanticRegistryReleaseIdentifier,
                purpose,
                disposition,
                decidingPrincipalIdentifier,
                decisionProvenanceReference,
                decidedAt
        );
    }
}
