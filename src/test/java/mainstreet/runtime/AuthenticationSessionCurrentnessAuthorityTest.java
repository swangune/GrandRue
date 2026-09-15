package mainstreet.runtime;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationSessionCurrentnessAuthorityTest {

    private static final Instant NOW =
            Instant.parse("2026-09-01T08:30:00Z");
    private static final AuthenticationProvenance PROVENANCE =
            new AuthenticationProvenance(
                    "session-1",
                    "identity-1",
                    NOW.minusSeconds(60)
            );

    @Test
    void returns_current_only_for_exact_live_session_and_security_generation() {
        SessionRecord session = session(
                "session-1",
                "identity-1",
                PROVENANCE.authenticatedAt(),
                NOW.plusSeconds(3600),
                "generation-2",
                Optional.empty()
        );
        AuthenticationSessionCurrentnessAuthority authority = authority(
                Optional.of(session),
                identity -> "generation-2"
        );

        assertEquals(
                AuthenticationSessionCurrentness.CURRENT,
                authority.evaluate(PROVENANCE)
        );
    }

    @Test
    void returns_not_current_for_session_identity_or_establishment_mismatch() {
        assertEquals(
                AuthenticationSessionCurrentness.NOT_CURRENT,
                authority(
                        Optional.of(session(
                                "different-session",
                                "identity-1",
                                PROVENANCE.authenticatedAt(),
                                NOW.plusSeconds(3600),
                                "generation-2",
                                Optional.empty()
                        )),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
        assertEquals(
                AuthenticationSessionCurrentness.NOT_CURRENT,
                authority(
                        Optional.of(session(
                                "session-1",
                                "different-identity",
                                PROVENANCE.authenticatedAt(),
                                NOW.plusSeconds(3600),
                                "generation-2",
                                Optional.empty()
                        )),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
        assertEquals(
                AuthenticationSessionCurrentness.NOT_CURRENT,
                authority(
                        Optional.of(session(
                                "session-1",
                                "identity-1",
                                PROVENANCE.authenticatedAt().minusSeconds(1),
                                NOW.plusSeconds(3600),
                                "generation-2",
                                Optional.empty()
                        )),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
    }

    @Test
    void returns_not_current_for_revoked_expired_or_generation_stale_session() {
        assertEquals(
                AuthenticationSessionCurrentness.NOT_CURRENT,
                authority(
                        Optional.of(session(
                                "session-1",
                                "identity-1",
                                PROVENANCE.authenticatedAt(),
                                NOW.plusSeconds(3600),
                                "generation-2",
                                Optional.of(NOW.minusSeconds(1))
                        )),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
        assertEquals(
                AuthenticationSessionCurrentness.NOT_CURRENT,
                authority(
                        Optional.of(session(
                                "session-1",
                                "identity-1",
                                PROVENANCE.authenticatedAt(),
                                NOW,
                                "generation-2",
                                Optional.empty()
                        )),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
        assertEquals(
                AuthenticationSessionCurrentness.NOT_CURRENT,
                authority(
                        Optional.of(session(
                                "session-1",
                                "identity-1",
                                PROVENANCE.authenticatedAt(),
                                NOW.plusSeconds(3600),
                                "generation-1",
                                Optional.empty()
                        )),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
    }

    @Test
    void returns_unresolved_when_session_or_current_generation_is_unavailable() {
        assertEquals(
                AuthenticationSessionCurrentness.UNRESOLVED,
                authority(
                        Optional.empty(),
                        identity -> "generation-2"
                ).evaluate(PROVENANCE)
        );
        assertEquals(
                AuthenticationSessionCurrentness.UNRESOLVED,
                authority(
                        Optional.of(session(
                                "session-1",
                                "identity-1",
                                PROVENANCE.authenticatedAt(),
                                NOW.plusSeconds(3600),
                                "generation-2",
                                Optional.empty()
                        )),
                        identity -> {
                            throw new IllegalStateException("generation unavailable");
                        }
                ).evaluate(PROVENANCE)
        );
        assertEquals(
                AuthenticationSessionCurrentness.UNRESOLVED,
                new DefaultAuthenticationSessionCurrentnessAuthority(
                        new FailingSessionStore(),
                        identity -> "generation-2",
                        fixedClock()
                ).evaluate(PROVENANCE)
        );
    }

    private static AuthenticationSessionCurrentnessAuthority authority(
            Optional<SessionRecord> session,
            IdentitySecurityGenerationAuthority generations
    ) {
        return new DefaultAuthenticationSessionCurrentnessAuthority(
                new FixedSessionStore(session),
                generations,
                fixedClock()
        );
    }

    private static Clock fixedClock() {
        return Clock.fixed(NOW, ZoneOffset.UTC);
    }

    private static SessionRecord session(
            String sessionIdentity,
            String identity,
            Instant establishedAt,
            Instant expiry,
            String generation,
            Optional<Instant> revokedAt
    ) {
        return new SessionRecord(
                sessionIdentity,
                identity,
                "verifier-" + sessionIdentity,
                establishedAt,
                "assurance",
                "passkey",
                expiry,
                establishedAt,
                generation,
                revokedAt,
                revokedAt.map(ignored -> "security-revocation")
        );
    }

    private record FixedSessionStore(Optional<SessionRecord> result)
            implements SessionRecordStore {

        @Override
        public SessionRecord create(SessionRecord candidate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<SessionRecord> sessionByIdentity(
                String sessionIdentity
        ) {
            return result;
        }

        @Override
        public Optional<SessionRecord> sessionByCredentialVerifier(
                String credentialVerifier
        ) {
            return Optional.empty();
        }

        @Override
        public SessionRecord revoke(
                String sessionIdentity,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void revokeAllForIdentity(
                String identityReference,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class FailingSessionStore
            implements SessionRecordStore {

        @Override
        public SessionRecord create(SessionRecord candidate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<SessionRecord> sessionByIdentity(
                String sessionIdentity
        ) {
            throw new IllegalStateException("session store unavailable");
        }

        @Override
        public Optional<SessionRecord> sessionByCredentialVerifier(
                String credentialVerifier
        ) {
            return Optional.empty();
        }

        @Override
        public SessionRecord revoke(
                String sessionIdentity,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void revokeAllForIdentity(
                String identityReference,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException();
        }
    }
}
