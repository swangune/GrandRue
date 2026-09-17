package grandrue.businesshours;

/** Distinguishable stable Business Hours mutation failures. */
public enum BusinessHoursFailureCategory {
    AUTHENTICATION_REQUIRED,
    CURRENT_CONTROLLER_REQUIRED,
    MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
    MERCHANT_LOCATION_NOT_FOUND,
    MERCHANT_LOCATION_RETIRED,
    NOT_CONFIGURED,
    REVISION_CONFLICT,
    IDEMPOTENCY_CONFLICT,
    PERSISTENCE_CONFLICT
}
