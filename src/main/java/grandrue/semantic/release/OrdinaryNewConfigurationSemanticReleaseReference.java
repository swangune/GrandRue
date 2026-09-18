package grandrue.semantic.release;

import java.time.Instant;
import java.util.Objects;

/** Immutable versioned selection behind the singleton ordinary reference. */
public record OrdinaryNewConfigurationSemanticReleaseReference(
        String referenceRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String validationAdmissionDecisionIdentifier,
        String businessActivityAdmissionDecisionIdentifier,
        long referenceEpoch,
        String selectingPrincipalIdentifier,
        String selectionProvenanceReference,
        Instant selectedAt
) {
    public OrdinaryNewConfigurationSemanticReleaseReference {
        requireIdentifier(referenceRevisionIdentifier, "Reference revision identifier");
        requireIdentifier(semanticRegistryReleaseIdentifier, "Semantic Registry Release identifier");
        requireIdentifier(validationAdmissionDecisionIdentifier, "Validation admission decision identifier");
        requireIdentifier(businessActivityAdmissionDecisionIdentifier, "Business-activity admission decision identifier");
        if (referenceEpoch <= 0) {
            throw new IllegalArgumentException("Reference epoch must be positive");
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
