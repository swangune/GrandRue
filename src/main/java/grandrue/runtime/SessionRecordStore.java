package grandrue.runtime;

import mainstreet.runtime.SessionRecord;

import java.time.Instant;
import java.util.Optional;

/**
 * Durable server-controlled Session Record persistence port under ADR-014.
 */
public interface SessionRecordStore {

    SessionRecord create(SessionRecord candidate);

    Optional<SessionRecord> sessionByIdentity(String sessionIdentity);

    Optional<SessionRecord> sessionByCredentialVerifier(String credentialVerifier);

    SessionRecord revoke(String sessionIdentity, Instant revokedAt, String reason);

    /**
     * Revokes every still-current Session Record for one authenticated Identity.
     * Existing revocation evidence is append-like and must not be rewritten.
     */
    void revokeAllForIdentity(
            String identityReference,
            Instant revokedAt,
            String reason
    );
}
