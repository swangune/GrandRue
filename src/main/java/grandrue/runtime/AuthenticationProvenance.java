package grandrue.runtime;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable provenance retained from successful principal establishment.
 * It records how attribution was established without carrying mutable access
 * authority or reusable credential material.
 */
public record AuthenticationProvenance(
        String sessionIdentifier,
        String identityIdentifier,
        Instant authenticatedAt
) {
    public AuthenticationProvenance {
        requireIdentifier(sessionIdentifier, "Session identifier");
        requireIdentifier(identityIdentifier, "Identity identifier");
        Objects.requireNonNull(authenticatedAt, "authenticatedAt");
    }

    private static void requireIdentifier(String identifier, String label) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
