package mainstreet.semantic.release;

import java.time.Instant;
import java.util.Objects;

/** Append-only platform fact governing one exact release and one purpose. */
public record SemanticReleasePurposeAdmissionDecision(
        String admissionDecisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        SemanticReleasePurpose purpose,
        SemanticReleaseAdmissionDisposition disposition,
        String decidingPrincipalIdentifier,
        String decisionProvenanceReference,
        Instant decidedAt
) {
    public SemanticReleasePurposeAdmissionDecision {
        requireIdentifier(admissionDecisionIdentifier, "Admission decision identifier");
        requireIdentifier(semanticRegistryReleaseIdentifier, "Semantic Registry Release identifier");
        Objects.requireNonNull(purpose, "purpose");
        Objects.requireNonNull(disposition, "disposition");
        requireIdentifier(decidingPrincipalIdentifier, "Deciding principal identifier");
        requireIdentifier(decisionProvenanceReference, "Decision provenance reference");
        Objects.requireNonNull(decidedAt, "decidedAt");
    }

    public boolean admitted() {
        return disposition == SemanticReleaseAdmissionDisposition.ADMITTED;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
