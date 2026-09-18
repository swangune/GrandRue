package grandrue.runtime;

import mainstreet.runtime.SessionRecord;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Resolves an ADR-014 opaque bearer credential to current authenticated
 * continuity through the durable server-authoritative Session Record.
 *
 * <p>This component establishes session continuity only. It does not establish
 * Merchant Scope, merchant relationship, device authorisation, role,
 * privilege, entitlement or capability authority.</p>
 */
public final class SessionCredentialResolver {

    private final SessionRecordStore sessionRecordStore;
    private final IdentitySecurityGenerationAuthority securityGenerationAuthority;
    private final Clock clock;

    public SessionCredentialResolver(
            SessionRecordStore sessionRecordStore,
            IdentitySecurityGenerationAuthority securityGenerationAuthority,
            Clock clock
    ) {
        this.sessionRecordStore = Objects.requireNonNull(
                sessionRecordStore,
                "sessionRecordStore"
        );
        this.securityGenerationAuthority = Objects.requireNonNull(
                securityGenerationAuthority,
                "securityGenerationAuthority"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public AuthenticationSession resolve(String presentedCredential) {
        String verifier = OpaqueSessionCredential
                .verifierForPresentedCredential(presentedCredential);
        SessionRecord session = sessionRecordStore
                .sessionByCredentialVerifier(verifier)
                .orElseThrow(() -> new AuthenticationException(
                        AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                        "Session credential is not current"
                ));

        Instant observedAt = clock.instant();
        if (session.revoked()) {
            throw new AuthenticationException(
                    AuthenticationFailureCategory.SESSION_REVOKED,
                    "Authentication session is revoked"
            );
        }
        if (!observedAt.isBefore(session.absoluteExpiry())) {
            throw new AuthenticationException(
                    AuthenticationFailureCategory.SESSION_EXPIRED,
                    "Authentication session is expired"
            );
        }

        String currentSecurityGeneration =
                securityGenerationAuthority.currentSecurityGenerationReference(
                        session.identityReference()
                );
        if (currentSecurityGeneration == null
                || currentSecurityGeneration.isBlank()
                || !session.securityGenerationReference().equals(currentSecurityGeneration)) {
            throw new AuthenticationException(
                    AuthenticationFailureCategory.SESSION_SECURITY_INVALID,
                    "Authentication session security generation is no longer current"
            );
        }

        return new AuthenticationSession(
                session.sessionIdentity(),
                session.identityReference(),
                session.establishedAt(),
                session.absoluteExpiry(),
                session.revokedAt()
        );
    }
}
