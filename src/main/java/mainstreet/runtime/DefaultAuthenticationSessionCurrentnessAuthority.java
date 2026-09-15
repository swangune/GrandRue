package mainstreet.runtime;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Fail-closed session currentness evaluator.
 *
 * <p>It validates exact session, identity, establishment-time, revocation,
 * expiry and current Identity security-generation evidence. It grants no
 * Merchant Scope, relationship, privilege or operation authority.</p>
 */
public final class DefaultAuthenticationSessionCurrentnessAuthority
        implements AuthenticationSessionCurrentnessAuthority {

    private final SessionRecordStore sessionRecords;
    private final IdentitySecurityGenerationAuthority securityGenerations;
    private final Clock clock;

    public DefaultAuthenticationSessionCurrentnessAuthority(
            SessionRecordStore sessionRecords,
            IdentitySecurityGenerationAuthority securityGenerations,
            Clock clock
    ) {
        this.sessionRecords = Objects.requireNonNull(
                sessionRecords,
                "sessionRecords"
        );
        this.securityGenerations = Objects.requireNonNull(
                securityGenerations,
                "securityGenerations"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public AuthenticationSessionCurrentness evaluate(
            AuthenticationProvenance provenance
    ) {
        Objects.requireNonNull(provenance, "provenance");

        SessionRecord session;
        try {
            Optional<SessionRecord> resolved = sessionRecords.sessionByIdentity(
                    provenance.sessionIdentifier()
            );
            if (resolved == null || resolved.isEmpty()) {
                return AuthenticationSessionCurrentness.UNRESOLVED;
            }
            session = resolved.orElseThrow();
        } catch (RuntimeException failure) {
            return AuthenticationSessionCurrentness.UNRESOLVED;
        }

        if (!session.sessionIdentity().equals(
                provenance.sessionIdentifier()
        ) || !session.identityReference().equals(
                provenance.identityIdentifier()
        ) || !session.establishedAt().equals(
                provenance.authenticatedAt()
        )) {
            return AuthenticationSessionCurrentness.NOT_CURRENT;
        }

        Instant evaluatedAt;
        try {
            evaluatedAt = clock.instant();
        } catch (RuntimeException failure) {
            return AuthenticationSessionCurrentness.UNRESOLVED;
        }
        if (session.revoked()
                || !evaluatedAt.isBefore(session.absoluteExpiry())) {
            return AuthenticationSessionCurrentness.NOT_CURRENT;
        }

        String currentGeneration;
        try {
            currentGeneration =
                    securityGenerations.currentSecurityGenerationReference(
                            provenance.identityIdentifier()
                    );
        } catch (RuntimeException failure) {
            return AuthenticationSessionCurrentness.UNRESOLVED;
        }
        if (currentGeneration == null || currentGeneration.isBlank()) {
            return AuthenticationSessionCurrentness.UNRESOLVED;
        }
        if (!session.securityGenerationReference().equals(
                currentGeneration
        )) {
            return AuthenticationSessionCurrentness.NOT_CURRENT;
        }

        return AuthenticationSessionCurrentness.CURRENT;
    }
}
