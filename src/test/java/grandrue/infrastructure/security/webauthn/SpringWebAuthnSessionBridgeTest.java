package grandrue.infrastructure.security.webauthn;

import grandrue.runtime.EstablishedHumanSession;
import grandrue.runtime.HumanSessionEstablishmentService;
import grandrue.runtime.IdentitySecurityGenerationAuthority;
import grandrue.runtime.SessionRecord;
import grandrue.runtime.SessionRecordStore;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringWebAuthnSessionBridgeTest {

    private static final Instant AUTHENTICATED_AT =
            Instant.parse("2026-08-28T08:00:00Z");

    @Test
    void verified_webauthn_establishes_a_fresh_main_street_session_from_authoritative_identity_mapping() {
        InMemorySessionStore store = new InMemorySessionStore();
        PublicKeyCredentialUserEntity springPrincipal = springPrincipal(
                "human-readable@example.test"
        );
        AtomicInteger identityLookups = new AtomicInteger();
        AtomicInteger generationLookups = new AtomicInteger();

        SpringWebAuthnSessionBridge bridge = bridge(
                store,
                principal -> {
                    identityLookups.incrementAndGet();
                    assertEquals(springPrincipal, principal);
                    return "identity-42";
                },
                identityReference -> {
                    generationLookups.incrementAndGet();
                    assertEquals("identity-42", identityReference);
                    return "security-generation-9";
                }
        );

        WebAuthnAuthentication authentication = successfulAuthentication(
                springPrincipal,
                "ROLE_SUPERUSER"
        );

        EstablishedHumanSession established = bridge.establish(authentication);
        SessionRecord record = store.sessionByIdentity(established.sessionIdentity())
                .orElseThrow();

        assertEquals("identity-42", record.identityReference());
        assertNotEquals(authentication.getName(), record.identityReference());
        assertEquals("webauthn-user-verified", record.authenticationAssuranceReference());
        assertEquals("webauthn-passkey", record.authenticationMethodReference());
        assertEquals(AUTHENTICATED_AT, record.establishedAt());
        assertEquals(AUTHENTICATED_AT.plus(Duration.ofHours(12)), record.absoluteExpiry());
        assertEquals(AUTHENTICATED_AT, record.lastActivityAt());
        assertEquals("security-generation-9", record.securityGenerationReference());
        assertFalse(record.revoked());
        assertEquals(1, identityLookups.get());
        assertEquals(1, generationLookups.get());

        // Spring authorities are deliberately ignored by Main Street Session authority.
        assertEquals(11, SessionRecord.class.getRecordComponents().length);
    }

    @Test
    void repeated_successful_webauthn_proofs_create_independent_main_street_sessions() {
        InMemorySessionStore store = new InMemorySessionStore();
        PublicKeyCredentialUserEntity springPrincipal = springPrincipal("controller@example.test");
        SpringWebAuthnSessionBridge bridge = bridge(
                store,
                principal -> "identity-1",
                identityReference -> "security-generation-1"
        );
        WebAuthnAuthentication authentication = successfulAuthentication(
                springPrincipal,
                "ROLE_CONTROLLER"
        );

        EstablishedHumanSession first = bridge.establish(authentication);
        EstablishedHumanSession second = bridge.establish(authentication);

        SessionRecord firstRecord = store.sessionByIdentity(first.sessionIdentity()).orElseThrow();
        SessionRecord secondRecord = store.sessionByIdentity(second.sessionIdentity()).orElseThrow();

        assertNotEquals(first.sessionIdentity(), second.sessionIdentity());
        assertNotEquals(first.credential().value(), second.credential().value());
        assertNotEquals(firstRecord.credentialVerifier(), secondRecord.credentialVerifier());
        assertEquals(2, store.createCalls);
    }

    @Test
    void unauthenticated_webauthn_token_is_rejected_before_identity_or_session_authority_is_touched() {
        InMemorySessionStore store = new InMemorySessionStore();
        AtomicInteger identityLookups = new AtomicInteger();
        AtomicInteger generationLookups = new AtomicInteger();
        PublicKeyCredentialUserEntity springPrincipal = springPrincipal("controller@example.test");
        SpringWebAuthnSessionBridge bridge = bridge(
                store,
                principal -> {
                    identityLookups.incrementAndGet();
                    return "identity-1";
                },
                identityReference -> {
                    generationLookups.incrementAndGet();
                    return "security-generation-1";
                }
        );
        WebAuthnAuthentication authentication = successfulAuthentication(
                springPrincipal,
                "ROLE_CONTROLLER"
        );
        authentication.setAuthenticated(false);

        assertThrows(
                UnverifiedWebAuthnAuthenticationException.class,
                () -> bridge.establish(authentication)
        );
        assertEquals(0, identityLookups.get());
        assertEquals(0, generationLookups.get());
        assertEquals(0, store.createCalls);
    }

    @Test
    void blank_authoritative_identity_mapping_fails_closed_before_session_creation() {
        InMemorySessionStore store = new InMemorySessionStore();
        SpringWebAuthnSessionBridge bridge = bridge(
                store,
                principal -> " ",
                identityReference -> "security-generation-1"
        );

        assertThrows(
                IllegalStateException.class,
                () -> bridge.establish(successfulAuthentication(
                        springPrincipal("controller@example.test"),
                        "ROLE_CONTROLLER"
                ))
        );
        assertEquals(0, store.createCalls);
    }

    private static SpringWebAuthnSessionBridge bridge(
            SessionRecordStore store,
            WebAuthnIdentityReferenceAuthority identityReferenceAuthority,
            IdentitySecurityGenerationAuthority securityGenerationAuthority
    ) {
        return new SpringWebAuthnSessionBridge(
                new HumanSessionEstablishmentService(store),
                identityReferenceAuthority,
                securityGenerationAuthority,
                new WebAuthnSessionEstablishmentPolicy(
                        Duration.ofHours(12),
                        "webauthn-user-verified",
                        "webauthn-passkey"
                ),
                Clock.fixed(AUTHENTICATED_AT, ZoneOffset.UTC)
        );
    }

    private static WebAuthnAuthentication successfulAuthentication(
            PublicKeyCredentialUserEntity principal,
            String springAuthority
    ) {
        return new WebAuthnAuthentication(
                principal,
                List.of(new SimpleGrantedAuthority(springAuthority))
        );
    }

    private static PublicKeyCredentialUserEntity springPrincipal(String name) {
        PublicKeyCredentialUserEntity principal = mock(PublicKeyCredentialUserEntity.class);
        when(principal.getName()).thenReturn(name);
        return principal;
    }

    private static final class InMemorySessionStore implements SessionRecordStore {
        private final Map<String, SessionRecord> byIdentity = new HashMap<>();
        private final Map<String, SessionRecord> byVerifier = new HashMap<>();
        private int createCalls;

        @Override
        public SessionRecord create(SessionRecord candidate) {
            createCalls++;
            if (byIdentity.putIfAbsent(candidate.sessionIdentity(), candidate) != null) {
                throw new IllegalStateException("Duplicate Session Identity");
            }
            if (byVerifier.putIfAbsent(candidate.credentialVerifier(), candidate) != null) {
                throw new IllegalStateException("Duplicate Session credential verifier");
            }
            return candidate;
        }

        @Override
        public Optional<SessionRecord> sessionByIdentity(String sessionIdentity) {
            return Optional.ofNullable(byIdentity.get(sessionIdentity));
        }

        @Override
        public Optional<SessionRecord> sessionByCredentialVerifier(String credentialVerifier) {
            return Optional.ofNullable(byVerifier.get(credentialVerifier));
        }

        @Override
        public SessionRecord revoke(String sessionIdentity, Instant revokedAt, String reason) {
            throw new UnsupportedOperationException("Not required by this bridge test");
        }

        @Override
        public void revokeAllForIdentity(
                String identityReference,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException("Not required by this bridge test");
        }
    }
}
