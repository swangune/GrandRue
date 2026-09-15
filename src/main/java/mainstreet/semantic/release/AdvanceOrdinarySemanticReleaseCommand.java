package mainstreet.semantic.release;

import java.time.Instant;
import java.util.Objects;

/** Exact retry-safe intent to advance the singleton ordinary reference. */
public record AdvanceOrdinarySemanticReleaseCommand(
        String referenceRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        long expectedReferenceEpoch,
        String selectingPrincipalIdentifier,
        String selectionProvenanceReference,
        Instant selectedAt
) {
    public AdvanceOrdinarySemanticReleaseCommand {
        requireIdentifier(referenceRevisionIdentifier, "Reference revision identifier");
        requireIdentifier(semanticRegistryReleaseIdentifier, "Semantic Registry Release identifier");
        if (expectedReferenceEpoch < 0) {
            throw new IllegalArgumentException("Expected reference epoch must not be negative");
        }
        requireIdentifier(selectingPrincipalIdentifier, "Selecting principal identifier");
        requireIdentifier(selectionProvenanceReference, "Selection provenance reference");
        Objects.requireNonNull(selectedAt, "selectedAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
