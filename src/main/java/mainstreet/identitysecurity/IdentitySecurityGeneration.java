package mainstreet.identitysecurity;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Durable current authentication-security generation for one Identity.
 *
 * <p>The generation is non-secret continuity evidence. It contains no
 * Merchant Scope, relationship, role, privilege or entitlement authority.</p>
 */
public record IdentitySecurityGeneration(
        String identityReference,
        String generationReference,
        long version,
        Instant establishedAt,
        Optional<Instant> lastRotatedAt
) {

    public IdentitySecurityGeneration {
        require(identityReference, "identityReference");
        require(generationReference, "generationReference");
        if (version < 1) {
            throw new IllegalArgumentException(
                    "Identity security generation version must be positive"
            );
        }
        Objects.requireNonNull(establishedAt, "establishedAt");
        lastRotatedAt = Objects.requireNonNull(lastRotatedAt, "lastRotatedAt");
        if (lastRotatedAt.isPresent()
                && lastRotatedAt.orElseThrow().isBefore(establishedAt)) {
            throw new IllegalArgumentException(
                    "Identity security rotation cannot precede establishment"
            );
        }
        if (version == 1 && lastRotatedAt.isPresent()) {
            throw new IllegalArgumentException(
                    "Initial Identity security generation cannot have rotation evidence"
            );
        }
        if (version > 1 && lastRotatedAt.isEmpty()) {
            throw new IllegalArgumentException(
                    "Rotated Identity security generation requires rotation evidence"
            );
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
