package grandrue.onboarding;

/**
 * Current external authority/account predicate supplied to onboarding
 * submission-readiness evaluation.
 */
public enum OnboardingSubmissionAuthority {
    AUTHORISED,
    AUTHENTICATION_REQUIRED,
    AUTHORISATION_REJECTED,
    ACCOUNT_OPERATION_RESTRICTED,
    INITIAL_ONBOARDING_NOT_APPLICABLE
}
