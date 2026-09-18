package grandrue.infrastructure.security.webauthn;

import java.time.Duration;
import java.util.Objects;

/**
 * Bounded post-verification evidence supplied to GrandRue Session
 * establishment for one configured WebAuthn authentication path.
 *
 * <p>This policy does not decide whether a WebAuthn assertion satisfied user
 * verification. The configured Spring WebAuthn/Relying Party path must prove
 * the applicable authentication property before this bridge is invoked.</p>
 */
public record WebAuthnSessionEstablishmentPolicy(
        Duration absoluteLifetime,
        String authenticationAssuranceReference,
        String authenticationMethodReference
) {
    private static final Duration MAX_CONTROLLER_ABSOLUTE_LIFETIME =
            Duration.ofHours(12);

    public WebAuthnSessionEstablishmentPolicy {
        Objects.requireNonNull(absoluteLifetime, "absoluteLifetime");
        if (absoluteLifetime.isZero() || absoluteLifetime.isNegative()) {
            throw new IllegalArgumentException(
                    "WebAuthn Session absolute lifetime must be positive"
            );
        }
        if (absoluteLifetime.compareTo(MAX_CONTROLLER_ABSOLUTE_LIFETIME) > 0) {
            throw new IllegalArgumentException(
                    "WebAuthn Controller Session absolute lifetime"
                            + " must not exceed "
                            + MAX_CONTROLLER_ABSOLUTE_LIFETIME
            );
        }
        require(authenticationAssuranceReference, "authenticationAssuranceReference");
        require(authenticationMethodReference, "authenticationMethodReference");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
