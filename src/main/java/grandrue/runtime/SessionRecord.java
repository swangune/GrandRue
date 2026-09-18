package grandrue.runtime;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Durable server-authoritative human-session security evidence under ADR-014.
 *
 * <p>A Session Record establishes authenticated continuity only. It deliberately
 * contains no Merchant Scope, merchant relationship, role, privilege,
 * entitlement, device-authorisation or other mutable business authority.</p>
 */
public record SessionRecord(
        String sessionIdentity,
        String identityReference,
        String credentialVerifier,
        Instant establishedAt,
        String authenticationAssuranceReference,
        String authenticationMethodReference,
        Instant absoluteExpiry,
        Instant lastActivityAt,
        String securityGenerationReference,
        Optional<Instant> revokedAt,
        Optional<String> revocationReason
) {

    public SessionRecord {
        require(sessionIdentity, "sessionIdentity");
        require(identityReference, "identityReference");
        require(credentialVerifier, "credentialVerifier");
        Objects.requireNonNull(establishedAt, "establishedAt");
        require(
                authenticationAssuranceReference,
                "authenticationAssuranceReference"
        );
        require(authenticationMethodReference, "authenticationMethodReference");
        Objects.requireNonNull(absoluteExpiry, "absoluteExpiry");
        Objects.requireNonNull(lastActivityAt, "lastActivityAt");
        require(securityGenerationReference, "securityGenerationReference");
        revokedAt = Objects.requireNonNull(revokedAt, "revokedAt");
        revocationReason = normalise(revocationReason, "revocationReason");

        if (!absoluteExpiry.isAfter(establishedAt)) {
            throw new IllegalArgumentException(
                    "Session absolute expiry must be after establishment"
            );
        }
        if (lastActivityAt.isBefore(establishedAt)) {
            throw new IllegalArgumentException(
                    "Session last activity cannot precede establishment"
            );
        }
        if (lastActivityAt.isAfter(absoluteExpiry)) {
            throw new IllegalArgumentException(
                    "Session last activity cannot follow absolute expiry"
            );
        }
        if (revokedAt.isPresent() != revocationReason.isPresent()) {
            throw new IllegalArgumentException(
                    "Session revocation time and reason must be present together"
            );
        }
        if (revokedAt.isPresent()) {
            Instant revoked = revokedAt.orElseThrow();
            if (revoked.isBefore(establishedAt)) {
                throw new IllegalArgumentException(
                        "Session revocation cannot precede establishment"
                );
            }
            if (revoked.isBefore(lastActivityAt)) {
                throw new IllegalArgumentException(
                        "Session revocation cannot precede recorded activity"
                );
            }
        }
    }

    public boolean revoked() {
        return revokedAt.isPresent();
    }

    public SessionRecord revoke(Instant at, String reason) {
        Objects.requireNonNull(at, "at");
        require(reason, "reason");
        if (revoked()) {
            if (revokedAt.orElseThrow().equals(at)
                    && revocationReason.orElseThrow().equals(reason)) {
                return this;
            }
            throw new IllegalStateException(
                    "Session is already revoked with different evidence"
            );
        }
        return new SessionRecord(
                sessionIdentity,
                identityReference,
                credentialVerifier,
                establishedAt,
                authenticationAssuranceReference,
                authenticationMethodReference,
                absoluteExpiry,
                lastActivityAt,
                securityGenerationReference,
                Optional.of(at),
                Optional.of(reason)
        );
    }

    private static Optional<String> normalise(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(item -> require(item, label));
        return value;
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
