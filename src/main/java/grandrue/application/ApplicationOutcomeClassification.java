package grandrue.application;

/** Truthful application-layer progression outcomes from MS-PROT-072. */
public enum ApplicationOutcomeClassification {
    COMPLETED,
    ACCEPTED_PENDING,
    REJECTED,
    CONFLICT,
    EXECUTION_UNCERTAIN,
    MANUAL_INTERVENTION_REQUIRED
}
