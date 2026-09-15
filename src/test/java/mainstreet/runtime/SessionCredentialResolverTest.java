package mainstreet.runtime;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionCredentialResolverTest {

    private static final Instant NOW = Instant.parse("2026-08-28T10:00:00Z");

    @Test
    void presented_bearer_resolves_only_through_its_one_way_verifier_and_current_security_generation() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(60),
                NOW.plusSeconds(600),
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        ));
        AtomicInteger generationLookups = new AtomicInteger();
        SessionCredentialResolver resolver = resolver(
                store,
                identityReference -> {
                    generationLookups.incrementAndGet();
                    assertEquals("identity-1", identityReference);
                    return "security-generation-1";
                }
        );

        AuthenticationSession resolved = resolver.resolve(credential.value());

        assertEquals("session-1", resolved.identifier());
        assertEquals("identity-1", resolved.identityIdentifier());
        assertEquals(NOW.minusSeconds(60), resolved.authenticatedAt());
        assertEquals(NOW.plusSeconds(600), resolved.expiresAt());
        assertEquals(1, generationLookups.get());
    }

    @Test
    void session_identity_is_not_accepted_as_a_bearer_credential() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(60),
                NOW.plusSeconds(600),
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        ));

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver(store).resolve("session-1")
        );
        assertEquals(
                AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                failure.category()
        );
    }

    @Test
    void unknown_bearer_fails_closed_without_security_generation_lookup() {
        InMemoryStore store = new InMemoryStore();
        AtomicInteger generationLookups = new AtomicInteger();

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver(
                        store,
                        identityReference -> {
                            generationLookups.incrementAndGet();
                            return "security-generation-1";
                        }
                ).resolve(OpaqueSessionCredential.generate(new SecureRandom()).value())
        );
        assertEquals(
                AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                failure.category()
        );
        assertEquals(0, generationLookups.get());
    }

    @Test
    void revoked_session_record_cannot_establish_authenticated_continuity_without_generation_lookup() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(120),
                NOW.plusSeconds(600),
                "security-generation-1",
                Optional.of(NOW.minusSeconds(1)),
                Optional.of("logout")
        ));
        AtomicInteger generationLookups = new AtomicInteger();

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver(
                        store,
                        identityReference -> {
                            generationLookups.incrementAndGet();
                            return "security-generation-1";
                        }
                ).resolve(credential.value())
        );
        assertEquals(
                AuthenticationFailureCategory.SESSION_REVOKED,
                failure.category()
        );
        assertEquals(0, generationLookups.get());
    }

    @Test
    void session_at_or_after_absolute_expiry_cannot_establish_continuity_without_generation_lookup() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(600),
                NOW,
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        ));
        AtomicInteger generationLookups = new AtomicInteger();

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver(
                        store,
                        identityReference -> {
                            generationLookups.incrementAndGet();
                            return "security-generation-1";
                        }
                ).resolve(credential.value())
        );
        assertEquals(
                AuthenticationFailureCategory.SESSION_EXPIRED,
                failure.category()
        );
        assertEquals(0, generationLookups.get());
    }

    @Test
    void changed_identity_security_generation_invalidates_an_otherwise_current_session() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(60),
                NOW.plusSeconds(600),
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        ));

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver(
                        store,
                        identityReference -> "security-generation-2"
                ).resolve(credential.value())
        );

        assertEquals(
                AuthenticationFailureCategory.SESSION_SECURITY_INVALID,
                failure.category()
        );
    }

    @Test
    void missing_current_security_generation_fails_closed() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(60),
                NOW.plusSeconds(600),
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        ));

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver(store, identityReference -> " ")
                        .resolve(credential.value())
        );

        assertEquals(
                AuthenticationFailureCategory.SESSION_SECURITY_INVALID,
                failure.category()
        );
    }

    @Test
    void current_security_generation_is_re_read_on_every_request() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        InMemoryStore store = new InMemoryStore();
        store.create(record(
                "session-1",
                credential.verifier(),
                NOW.minusSeconds(60),
                NOW.plusSeconds(600),
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        ));
        AtomicInteger generationLookups = new AtomicInteger();
        SessionCredentialResolver resolver = resolver(
                store,
                identityReference -> generationLookups.incrementAndGet() == 1
                        ? "security-generation-1"
                        : "security-generation-2"
        );

        resolver.resolve(credential.value());
        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> resolver.resolve(credential.value())
        );

        assertEquals(2, generationLookups.get());
        assertEquals(
                AuthenticationFailureCategory.SESSION_SECURITY_INVALID,
                failure.category()
        );
    }

    private static SessionCredentialResolver resolver(SessionRecordStore store) {
        return resolver(store, identityReference -> "security-generation-1");
    }

    private static SessionCredentialResolver resolver(
            SessionRecordStore store,
            IdentitySecurityGenerationAuthority securityGenerationAuthority
    ) {
        return new SessionCredentialResolver(
                store,
                securityGenerationAuthority,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    private static SessionRecord record(
            String sessionIdentity,
            String verifier,
            Instant establishedAt,
            Instant absoluteExpiry,
            String securityGenerationReference,
            Optional<Instant> revokedAt,
            Optional<String> revocationReason
    ) {
        return new SessionRecord(
                sessionIdentity,
                "identity-1",
                verifier,
                establishedAt,
                "phishing-resistant",
                "webauthn-passkey",
                absoluteExpiry,
                establishedAt,
                securityGenerationReference,
                revokedAt,
                revocationReason
        );
    }

    private static final class InMemoryStore implements SessionRecordStore {
        private final Map<String, SessionRecord> byIdentity = new HashMap<>();
        private final Map<String, SessionRecord> byVerifier = new HashMap<>();

        @Override
        public SessionRecord create(SessionRecord candidate) {
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
            SessionRecord current = byIdentity.get(sessionIdentity);
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
            throw new UnsupportedOperationException("Not required by this resolver test");
        }
    }
}
