package grandrue.semantic.configuration;

/** Stable internal failure categories for validation-evidence persistence. */
public enum ConfigurationValidationEvidenceFailureCategory {
    CONFIGURATION_REVISION_NOT_FOUND,
    RESOLVED_PACKAGE_AFFINITY_MISMATCH,
    VALIDATION_EVIDENCE_IDENTITY_CONFLICT,
    RESOLVED_PACKAGE_EVIDENCE_IDENTITY_CONFLICT,
    PERSISTENCE_FAILURE
}
