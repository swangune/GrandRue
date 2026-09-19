package grandrue.runtime;

import grandrue.testing.TestSessionRecordStore;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionLogoutServiceTest {

    private static final Instant ESTABLISHED_AT =
            Instant.parse("2026-08-28T01:00:00Z");
    private static final Instant LOGOUT_AT =
            Instant.parse("2026-08-28T02:00:00Z");
    private static final Instant EXPIRY =
            Instant.parse("2026-08-28T13:00:00Z");
    private static final String BEARER_ONE =
            "test-bearer-one-AAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String BEARER_TWO =
            "test-bearer-two-BBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Test
    void logout_durably_revokes_only_the_session_selected_by_the_bearer() {
        TestSessionRecordStore store = new TestSessionRecordStore();
        store.create(record("session-1", BEARER_ONE));
        store.create(record("session-2", BEARER_TWO));
        SessionLogoutService service = service(store);

        SessionRecord revoked = service.logout(BEARER_ONE);

        assertTrue(revoked.revoked());
        assertEquals(LOGOUT_AT, revoked.revokedAt().orElseThrow());
        assertEquals("logout", revoked.revocationReason().orElseThrow());
        assertFalse(store.sessionByIdentity("session-2").orElseThrow().revoked());

        SessionCredentialResolver resolver = new SessionCredentialResolver(
                store,
                identityReference -> "security-generation-1",
                Clock.fixed(LOGOUT_AT.plusSeconds(1), ZoneOffset.UTC)
        );
        AuthenticationException revokedFailure = assertThrows(
                AuthenticationException.class,
                () -> resolver.resolve(BEARER_ONE)
        );
        assertEquals(
                AuthenticationFailureCategory.SESSION_REVOKED,
                revokedFailure.category()
        );
        assertEquals("identity-123", resolver.resolve(BEARER_TWO).identityIdentifier());
    }

    @Test
    void repeated_logout_after_acknowledgement_loss_preserves_original_revocation_evidence() {
        TestSessionRecordStore store = new TestSessionRecordStore();
        store.create(record("session-1", BEARER_ONE));
        SessionLogoutService firstAttempt = service(store);

        SessionRecord committed = firstAttempt.logout(BEARER_ONE);
        SessionLogoutService retryAfterLostAcknowledgement = new SessionLogoutService(
                store,
                Clock.fixed(LOGOUT_AT.plusSeconds(30), ZoneOffset.UTC)
        );
        SessionRecord replay = retryAfterLostAcknowledgement.logout(BEARER_ONE);

        assertEquals(committed, replay);
        assertEquals(LOGOUT_AT, replay.revokedAt().orElseThrow());
        assertEquals("logout", replay.revocationReason().orElseThrow());
    }

    @Test
    void unknown_bearer_fails_authentication_without_revoking_any_session() {
        TestSessionRecordStore store = new TestSessionRecordStore();
        store.create(record("session-1", BEARER_ONE));
        SessionLogoutService service = service(store);

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> service.logout("unknown-test-bearer-CCCCCCCCCCCCCCCCCCCCCCCC")
        );

        assertEquals(
                AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                failure.category()
        );
        assertFalse(store.sessionByIdentity("session-1").orElseThrow().revoked());
    }

    private static SessionLogoutService service(SessionRecordStore store) {
        return new SessionLogoutService(
                store,
                Clock.fixed(LOGOUT_AT, ZoneOffset.UTC)
        );
    }

    private static SessionRecord record(String sessionIdentity, String bearer) {
        return new SessionRecord(
                sessionIdentity,
                "identity-123",
                OpaqueSessionCredential.verifierForPresentedCredential(bearer),
                ESTABLISHED_AT,
                "phishing-resistant",
                "webauthn-passkey",
                EXPIRY,
                ESTABLISHED_AT,
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        );
    }
}
