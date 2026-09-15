package mainstreet.semantic.configuration;

/** Stable internal failure categories for impact-review evidence persistence. */
public enum ConfigurationImpactReviewEvidenceFailureCategory {
    VALIDATION_EVIDENCE_NOT_FOUND,
    VALIDATION_EVIDENCE_AFFINITY_MISMATCH,
    IMPACT_REVIEW_EVIDENCE_IDENTITY_CONFLICT,
    PERSISTENCE_FAILURE
}
