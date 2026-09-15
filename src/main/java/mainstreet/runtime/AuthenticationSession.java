package mainstreet.runtime;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Current session-security state read from the authentication authority.
 * Session state establishes continuity only; it carries no business privilege.
 */
public record AuthenticationSession(
        String identifier,
        String identityIdentifier,
        Instant authenticatedAt,
        Instant expiresAt,
        Optional<Instant> revokedAt
) {
    public AuthenticationSession {
        requireIdentifier(identifier, "Session identifier");
        requireIdentifier(identityIdentifier, "Identity identifier");
        Objects.requireNonNull(authenticatedAt, "authenticatedAt");
        Objects.requireNonNull(expiresAt, "expiresAt");
        revokedAt = Objects.requireNonNull(revokedAt, "revokedAt");
        if (!expiresAt.isAfter(authenticatedAt)) {
            throw new IllegalArgumentException(
                    "Session expiry must be after authentication time"
            );
        }
        if (revokedAt.isPresent()
                && revokedAt.orElseThrow().isBefore(authenticatedAt)) {
            throw new IllegalArgumentException(
                    "Session revocation cannot precede authentication time"
            );
        }
    }

    private static void requireIdentifier(String identifier, String label) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
