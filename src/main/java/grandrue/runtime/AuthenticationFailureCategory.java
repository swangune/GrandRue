package grandrue.runtime;

/**
 * Diagnostic categories for trusted-principal establishment failures.
 */
public enum AuthenticationFailureCategory {
    AUTHENTICATION_FAILED,
    SESSION_EXPIRED,
    SESSION_REVOKED,
    SESSION_SECURITY_INVALID,
    PRINCIPAL_ESTABLISHMENT_FAILED
}
