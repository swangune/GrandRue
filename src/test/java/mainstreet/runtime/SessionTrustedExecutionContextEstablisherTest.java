package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import grandrue.testing.TestSessionRecordStore;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionTrustedExecutionContextEstablisherTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant AUTHENTICATED_AT =
            Instant.parse("2026-08-25T09:00:00Z");
    private static final Instant NOW =
            Instant.parse("2026-08-25T10:00:00Z");
    private static final Instant EXPIRES_AT =
            Instant.parse("2026-08-25T17:00:00Z");

    @Test
    void bearer_credential_establishes_context_but_revocation_removes_continuity() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        TestSessionRecordStore store = new TestSessionRecordStore();
        store.create(record(credential));
        ExecutionPrincipal principal = new ExecutionPrincipal("staff-1");
        ScopedExecutionPrincipalResolver principals =
                (scope, identityIdentifier) -> {
                    assertSame(MERCHANT, scope);
                    assertEquals("identity-123", identityIdentifier);
                    return principal;
                };
        SessionTrustedExecutionContextEstablisher establisher =
                new SessionTrustedExecutionContextEstablisher(
                        new SessionCredentialResolver(
                                store,
                                identityReference -> "security-generation-1",
                                Clock.fixed(NOW, ZoneOffset.UTC)
                        ),
                        principals
                );

        TrustedExecutionContext context = establisher.establish(
                MERCHANT,
                credential.value()
        );

        assertSame(MERCHANT, context.merchantScope());
        assertSame(principal, context.principal());
        AuthenticationProvenance authentication =
                context.authentication().orElseThrow();
        assertEquals("session-1", authentication.sessionIdentifier());
        assertEquals("identity-123", authentication.identityIdentifier());
        assertEquals(AUTHENTICATED_AT, authentication.authenticatedAt());

        store.revoke("session-1", NOW, "logout");

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> establisher.establish(MERCHANT, credential.value())
        );
        assertEquals(
                AuthenticationFailureCategory.SESSION_REVOKED,
                failure.category()
        );
    }

    @Test
    void non_secret_session_identity_cannot_replace_the_bearer_credential() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        TestSessionRecordStore store = new TestSessionRecordStore();
        store.create(record(credential));
        SessionTrustedExecutionContextEstablisher establisher =
                new SessionTrustedExecutionContextEstablisher(
                        new SessionCredentialResolver(
                                store,
                                identityReference -> "security-generation-1",
                                Clock.fixed(NOW, ZoneOffset.UTC)
                        ),
                        (scope, identity) -> new ExecutionPrincipal("staff-1")
                );

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> establisher.establish(MERCHANT, "session-1")
        );
        assertEquals(
                AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                failure.category()
        );
    }

    private static SessionRecord record(OpaqueSessionCredential credential) {
        return new SessionRecord(
                "session-1",
                "identity-123",
                credential.verifier(),
                AUTHENTICATED_AT,
                "phishing-resistant",
                "webauthn-passkey",
                EXPIRES_AT,
                AUTHENTICATED_AT,
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        );
    }
}
