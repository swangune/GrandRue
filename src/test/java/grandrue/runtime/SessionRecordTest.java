package grandrue.runtime;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionRecordTest {

    private static final Instant ESTABLISHED_AT =
            Instant.parse("2026-08-28T09:00:00Z");
    private static final Instant ABSOLUTE_EXPIRY =
            ESTABLISHED_AT.plusSeconds(12 * 60 * 60);

    @Test
    void opaque_session_credential_uses_256_bits_and_never_renders_the_bearer_secret() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );

        byte[] raw = Base64.getUrlDecoder().decode(credential.value());
        byte[] verifier = Base64.getUrlDecoder().decode(credential.verifier());

        assertEquals(32, raw.length);
        assertEquals(32, verifier.length);
        assertFalse(credential.value().equals(credential.verifier()));
        assertFalse(credential.toString().contains(credential.value()));
        assertTrue(credential.toString().contains("REDACTED"));
        assertTrue(credential.matchesVerifier(credential.verifier()));
        assertFalse(credential.matchesVerifier(
                OpaqueSessionCredential.generate(new SecureRandom()).verifier()
        ));
    }

    @Test
    void session_record_preserves_security_evidence_without_merchant_business_authority() {
        SessionRecord record = record();

        assertEquals("session-1", record.sessionIdentity());
        assertEquals("identity-1", record.identityReference());
        assertEquals("phishing-resistant", record.authenticationAssuranceReference());
        assertEquals("webauthn-passkey", record.authenticationMethodReference());
        assertEquals("security-generation-7", record.securityGenerationReference());
        assertTrue(record.revokedAt().isEmpty());
        assertTrue(record.revocationReason().isEmpty());

        for (var component : SessionRecord.class.getRecordComponents()) {
            String name = component.getName().toLowerCase();
            assertFalse(name.contains("merchant"));
            assertFalse(name.contains("role"));
            assertFalse(name.contains("privilege"));
            assertFalse(name.contains("entitlement"));
            assertFalse(name.contains("deviceauthorisation"));
        }
    }

    @Test
    void session_record_rejects_invalid_time_and_revocation_evidence() {
        SessionRecord valid = record();

        assertThrows(
                IllegalArgumentException.class,
                () -> new SessionRecord(
                        valid.sessionIdentity(),
                        valid.identityReference(),
                        valid.credentialVerifier(),
                        ESTABLISHED_AT,
                        valid.authenticationAssuranceReference(),
                        valid.authenticationMethodReference(),
                        ESTABLISHED_AT,
                        ESTABLISHED_AT,
                        valid.securityGenerationReference(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SessionRecord(
                        valid.sessionIdentity(),
                        valid.identityReference(),
                        valid.credentialVerifier(),
                        ESTABLISHED_AT,
                        valid.authenticationAssuranceReference(),
                        valid.authenticationMethodReference(),
                        ABSOLUTE_EXPIRY,
                        ESTABLISHED_AT.minusSeconds(1),
                        valid.securityGenerationReference(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SessionRecord(
                        valid.sessionIdentity(),
                        valid.identityReference(),
                        valid.credentialVerifier(),
                        ESTABLISHED_AT,
                        valid.authenticationAssuranceReference(),
                        valid.authenticationMethodReference(),
                        ABSOLUTE_EXPIRY,
                        ABSOLUTE_EXPIRY.plusSeconds(1),
                        valid.securityGenerationReference(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SessionRecord(
                        valid.sessionIdentity(),
                        valid.identityReference(),
                        valid.credentialVerifier(),
                        ESTABLISHED_AT,
                        valid.authenticationAssuranceReference(),
                        valid.authenticationMethodReference(),
                        ABSOLUTE_EXPIRY,
                        ESTABLISHED_AT,
                        valid.securityGenerationReference(),
                        Optional.of(ESTABLISHED_AT.plusSeconds(1)),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SessionRecord(
                        valid.sessionIdentity(),
                        valid.identityReference(),
                        valid.credentialVerifier(),
                        ESTABLISHED_AT,
                        valid.authenticationAssuranceReference(),
                        valid.authenticationMethodReference(),
                        ABSOLUTE_EXPIRY,
                        ESTABLISHED_AT,
                        valid.securityGenerationReference(),
                        Optional.empty(),
                        Optional.of("logout")
                )
        );
    }

    private static SessionRecord record() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        return new SessionRecord(
                "session-1",
                "identity-1",
                credential.verifier(),
                ESTABLISHED_AT,
                "phishing-resistant",
                "webauthn-passkey",
                ABSOLUTE_EXPIRY,
                ESTABLISHED_AT,
                "security-generation-7",
                Optional.empty(),
                Optional.empty()
        );
    }
}
