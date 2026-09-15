package mainstreet.merchantprofile;

/**
 * Recoverable failure classifications for authoritative profile mutation.
 */
public enum MerchantProfileFailureCategory {
    PROFILE_FACT_NOT_FOUND,
    PROFILE_FACT_RETIRED,
    PROFILE_REVISION_CONFLICT,
    REQUEST_IDENTITY_CONFLICT,
    AUTHENTICATION_REQUIRED,
    AUTHORISATION_REJECTION,
    MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
    TECHNICAL_FAILURE_BEFORE_COMMIT
}
