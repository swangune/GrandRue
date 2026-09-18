package grandrue.semantic.release;

public enum SemanticReleaseAdmissionFailureCategory {
    DECISION_IDENTITY_CONFLICT,
    REFERENCE_IDENTITY_CONFLICT,
    REFERENCE_EPOCH_CONFLICT,
    RELEASE_NOT_ADMITTED_FOR_BOTH_PURPOSES,
    PERSISTENCE_FAILURE
}
