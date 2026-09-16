package grandrue.identitysecurity;

/**
 * Governed authentication-security reasons that may rotate an Identity
 * security generation under ADR-014 v1.1.
 */
public enum IdentitySecurityRotationReason {
    AUTHENTICATOR_RESET,
    ACCOUNT_RECOVERY,
    SUSPECTED_COMPROMISE
}
