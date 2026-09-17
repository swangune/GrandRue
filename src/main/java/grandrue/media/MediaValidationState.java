package grandrue.media;

/** Technical source-validation state; validation does not create Exposure. */
public enum MediaValidationState {
    PENDING_VALIDATION,
    VALIDATED,
    QUARANTINED,
    REJECTED,
    LOGICALLY_UNAVAILABLE
}
