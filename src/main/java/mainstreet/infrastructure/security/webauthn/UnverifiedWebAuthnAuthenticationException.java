package mainstreet.infrastructure.security.webauthn;

/**
 * Raised when the post-verification bridge is invoked without a successful
 * authenticated Spring WebAuthn result.
 */
public final class UnverifiedWebAuthnAuthenticationException
        extends RuntimeException {

    public UnverifiedWebAuthnAuthenticationException() {
        super("Verified WebAuthn authentication is required before Session establishment");
    }
}
