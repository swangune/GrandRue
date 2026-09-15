package mainstreet.testing;

import mainstreet.runtime.SessionRecord;
import mainstreet.runtime.SessionRecordStore;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Deterministic in-memory Session Record persistence for runtime tests.
 */
public final class TestSessionRecordStore implements SessionRecordStore {

    private final Map<String, SessionRecord> byIdentity = new HashMap<>();
    private final Map<String, SessionRecord> byVerifier = new HashMap<>();

    @Override
    public SessionRecord create(SessionRecord candidate) {
        SessionRecord identityExisting = byIdentity.get(candidate.sessionIdentity());
        if (identityExisting != null) {
            if (identityExisting.equals(candidate)) {
                return identityExisting;
            }
            throw new IllegalStateException(
                    "Session identity already exists with different evidence"
            );
        }
        SessionRecord verifierExisting = byVerifier.get(candidate.credentialVerifier());
        if (verifierExisting != null) {
            throw new IllegalStateException(
                    "Credential verifier is already bound to another session"
            );
        }
        byIdentity.put(candidate.sessionIdentity(), candidate);
        byVerifier.put(candidate.credentialVerifier(), candidate);
        return candidate;
    }

    @Override
    public Optional<SessionRecord> sessionByIdentity(String sessionIdentity) {
        return Optional.ofNullable(byIdentity.get(sessionIdentity));
    }

    @Override
    public Optional<SessionRecord> sessionByCredentialVerifier(
            String credentialVerifier
    ) {
        return Optional.ofNullable(byVerifier.get(credentialVerifier));
    }

    @Override
    public SessionRecord revoke(
            String sessionIdentity,
            Instant revokedAt,
            String reason
    ) {
        SessionRecord current = sessionByIdentity(sessionIdentity).orElseThrow();
        SessionRecord revoked = current.revoke(revokedAt, reason);
        byIdentity.put(sessionIdentity, revoked);
        byVerifier.put(revoked.credentialVerifier(), revoked);
        return revoked;
    }

    @Override
    public void revokeAllForIdentity(
            String identityReference,
            Instant revokedAt,
            String reason
    ) {
        if (identityReference == null || identityReference.isBlank()) {
            throw new IllegalArgumentException("identityReference must not be blank");
        }
        if (revokedAt == null) {
            throw new NullPointerException("revokedAt");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }

        for (SessionRecord current : Map.copyOf(byIdentity).values()) {
            if (!current.identityReference().equals(identityReference)
                    || current.revoked()) {
                continue;
            }
            SessionRecord revoked = current.revoke(revokedAt, reason);
            byIdentity.put(revoked.sessionIdentity(), revoked);
            byVerifier.put(revoked.credentialVerifier(), revoked);
        }
    }
}
