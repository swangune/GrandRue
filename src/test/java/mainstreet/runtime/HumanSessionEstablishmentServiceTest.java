package mainstreet.runtime;

import grandrue.testing.TestSessionRecordStore;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HumanSessionEstablishmentServiceTest {

    private static final Instant AUTHENTICATED_AT =
            Instant.parse("2026-08-28T09:00:00Z");
    private static final Instant ABSOLUTE_EXPIRY =
            Instant.parse("2026-08-28T21:00:00Z");

    @Test
    void successful_authentication_establishes_fresh_independent_session_identity_and_bearer() {
        TestSessionRecordStore store = new TestSessionRecordStore();
        AtomicInteger identities = new AtomicInteger();
        HumanSessionEstablishmentService service =
                new HumanSessionEstablishmentService(
                        store,
                        new SequenceSecureRandom(),
                        () -> "session-" + identities.incrementAndGet()
                );

        EstablishedHumanSession first = establish(service);
        EstablishedHumanSession second = establish(service);

        assertNotEquals(first.sessionIdentity(), second.sessionIdentity());
        assertNotEquals(first.credential().value(), second.credential().value());

        SessionRecord firstRecord = store.sessionByIdentity(
                first.sessionIdentity()
        ).orElseThrow();
        assertEquals("identity-123", firstRecord.identityReference());
        assertEquals(AUTHENTICATED_AT, firstRecord.establishedAt());
        assertEquals("phishing-resistant", firstRecord.authenticationAssuranceReference());
        assertEquals("webauthn-passkey", firstRecord.authenticationMethodReference());
        assertEquals(ABSOLUTE_EXPIRY, firstRecord.absoluteExpiry());
        assertEquals(AUTHENTICATED_AT, firstRecord.lastActivityAt());
        assertEquals("security-generation-7", firstRecord.securityGenerationReference());
        assertEquals(Optional.empty(), firstRecord.revokedAt());
        assertEquals(Optional.empty(), firstRecord.revocationReason());
        assertEquals(first.credential().verifier(), firstRecord.credentialVerifier());

        assertFalse(store.sessionByCredentialVerifier(
                first.credential().value()
        ).isPresent());
        assertEquals(
                firstRecord,
                store.sessionByCredentialVerifier(
                        first.credential().verifier()
                ).orElseThrow()
        );
    }

    @Test
    void returned_session_representation_does_not_render_raw_bearer_secret() {
        TestSessionRecordStore store = new TestSessionRecordStore();
        HumanSessionEstablishmentService service =
                new HumanSessionEstablishmentService(
                        store,
                        new SequenceSecureRandom(),
                        () -> "session-1"
                );

        EstablishedHumanSession established = establish(service);

        assertFalse(established.toString().contains(
                established.credential().value()
        ));
    }

    @Test
    void invalid_security_evidence_does_not_create_a_session_record() {
        TestSessionRecordStore store = new TestSessionRecordStore();
        HumanSessionEstablishmentService service =
                new HumanSessionEstablishmentService(
                        store,
                        new SequenceSecureRandom(),
                        () -> "session-1"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.establish(
                        "identity-123",
                        AUTHENTICATED_AT,
                        "phishing-resistant",
                        "webauthn-passkey",
                        AUTHENTICATED_AT,
                        "security-generation-7"
                )
        );
        assertFalse(store.sessionByIdentity("session-1").isPresent());
    }

    private static EstablishedHumanSession establish(
            HumanSessionEstablishmentService service
    ) {
        return service.establish(
                "identity-123",
                AUTHENTICATED_AT,
                "phishing-resistant",
                "webauthn-passkey",
                ABSOLUTE_EXPIRY,
                "security-generation-7"
        );
    }

    private static final class SequenceSecureRandom extends SecureRandom {
        private int next = 1;

        @Override
        public void nextBytes(byte[] bytes) {
            for (int index = 0; index < bytes.length; index++) {
                bytes[index] = (byte) next;
            }
            next++;
        }
    }
}
