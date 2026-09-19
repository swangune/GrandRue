package grandrue.infrastructure.persistence.runtime;

import grandrue.runtime.OpaqueSessionCredential;
import grandrue.runtime.SessionRecord;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqSessionRecordStoreIT {

    private static final Instant ESTABLISHED_AT =
            Instant.parse("2026-08-28T09:00:00Z");
    private static final Instant ABSOLUTE_EXPIRY =
            ESTABLISHED_AT.plusSeconds(12 * 60 * 60);

    private DataSource authoritativeDataSource;
    private DSLContext dsl;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(authoritativeDataSource),
                SQLDialect.POSTGRES
        );
        dsl.execute("truncate table authentication_session_record");
    }

    @Test
    void session_record_survives_store_recreation_and_resolves_by_verifier_not_raw_secret() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        SessionRecord original = record("session-1", credential.verifier());

        store().create(original);

        JooqSessionRecordStore recreated = store();
        assertEquals(
                original,
                recreated.sessionByIdentity("session-1").orElseThrow()
        );
        assertEquals(
                original,
                recreated.sessionByCredentialVerifier(
                        credential.verifier()
                ).orElseThrow()
        );
        assertTrue(
                recreated.sessionByCredentialVerifier(credential.value()).isEmpty()
        );
        assertFalse(dsl.fetchExists(
                dsl.selectOne()
                        .from("authentication_session_record")
                        .where(DSL.field("credential_verifier", String.class)
                                .eq(credential.value()))
        ));
    }

    @Test
    void credential_verifier_is_unique_across_session_records() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        store().create(record("session-1", credential.verifier()));

        assertThrows(
                IllegalStateException.class,
                () -> store().create(record("session-2", credential.verifier()))
        );
        assertEquals(1, count());
    }

    @Test
    void exact_session_identity_replay_is_idempotent_but_conflicting_reuse_is_rejected() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        SessionRecord original = record("session-1", credential.verifier());
        assertEquals(original, store().create(original));
        assertEquals(original, store().create(original));

        OpaqueSessionCredential replacement = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        assertThrows(
                IllegalStateException.class,
                () -> store().create(record(
                        "session-1",
                        replacement.verifier()
                ))
        );
        assertEquals(1, count());
    }

    @Test
    void logout_revocation_is_durable_and_cannot_be_silently_rewritten() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        store().create(record("session-1", credential.verifier()));
        Instant revokedAt = ESTABLISHED_AT.plusSeconds(60);

        SessionRecord revoked = store().revoke(
                "session-1",
                revokedAt,
                "logout"
        );

        assertEquals(Optional.of(revokedAt), revoked.revokedAt());
        assertEquals(Optional.of("logout"), revoked.revocationReason());
        assertEquals(
                revoked,
                store().sessionByIdentity("session-1").orElseThrow()
        );
        assertEquals(
                revoked,
                store().revoke("session-1", revokedAt, "logout")
        );
        assertThrows(
                IllegalStateException.class,
                () -> store().revoke(
                        "session-1",
                        revokedAt.plusSeconds(1),
                        "security-reset"
                )
        );
    }

    @Test
    void identity_wide_revocation_revokes_current_sessions_without_rewriting_prior_evidence() {
        OpaqueSessionCredential first = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        OpaqueSessionCredential second = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        OpaqueSessionCredential alreadyRevoked = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        OpaqueSessionCredential otherIdentity = OpaqueSessionCredential.generate(
                new SecureRandom()
        );

        JooqSessionRecordStore store = store();
        store.create(record("session-1", "identity-1", first.verifier()));
        store.create(record("session-2", "identity-1", second.verifier()));
        store.create(record("session-3", "identity-1", alreadyRevoked.verifier()));
        store.create(record("session-4", "identity-2", otherIdentity.verifier()));

        Instant earlierRevocation = ESTABLISHED_AT.plusSeconds(30);
        store.revoke("session-3", earlierRevocation, "logout");

        Instant securityRevocation = ESTABLISHED_AT.plusSeconds(60);
        store.revokeAllForIdentity(
                "identity-1",
                securityRevocation,
                "logout-all-devices"
        );

        assertRevoked(
                store.sessionByIdentity("session-1").orElseThrow(),
                securityRevocation,
                "logout-all-devices"
        );
        assertRevoked(
                store.sessionByIdentity("session-2").orElseThrow(),
                securityRevocation,
                "logout-all-devices"
        );
        assertRevoked(
                store.sessionByIdentity("session-3").orElseThrow(),
                earlierRevocation,
                "logout"
        );
        assertFalse(
                store.sessionByIdentity("session-4").orElseThrow().revoked()
        );

        store.revokeAllForIdentity(
                "identity-1",
                securityRevocation.plusSeconds(30),
                "security-reset"
        );

        assertRevoked(
                store.sessionByIdentity("session-1").orElseThrow(),
                securityRevocation,
                "logout-all-devices"
        );
        assertRevoked(
                store.sessionByIdentity("session-2").orElseThrow(),
                securityRevocation,
                "logout-all-devices"
        );
        assertRevoked(
                store.sessionByIdentity("session-3").orElseThrow(),
                earlierRevocation,
                "logout"
        );
    }

    @Test
    void persistence_schema_contains_session_security_evidence_not_merchant_authority() {
        var columns = dsl.select(DSL.field("column_name", String.class))
                .from("information_schema.columns")
                .where(DSL.field("table_schema", String.class).eq("public"))
                .and(DSL.field("table_name", String.class)
                        .eq("authentication_session_record"))
                .fetch(DSL.field("column_name", String.class));

        assertTrue(columns.contains("session_identity"));
        assertTrue(columns.contains("identity_reference"));
        assertTrue(columns.contains("credential_verifier"));
        assertTrue(columns.contains("security_generation_reference"));
        for (String column : columns) {
            String name = column.toLowerCase();
            assertFalse(name.contains("merchant"));
            assertFalse(name.contains("role"));
            assertFalse(name.contains("privilege"));
            assertFalse(name.contains("entitlement"));
            assertFalse(name.contains("device_authorisation"));
        }
    }

    private JooqSessionRecordStore store() {
        return new JooqSessionRecordStore(dsl);
    }

    private SessionRecord record(String sessionIdentity, String verifier) {
        return record(sessionIdentity, "identity-1", verifier);
    }

    private SessionRecord record(
            String sessionIdentity,
            String identityReference,
            String verifier
    ) {
        return new SessionRecord(
                sessionIdentity,
                identityReference,
                verifier,
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

    private static void assertRevoked(
            SessionRecord record,
            Instant revokedAt,
            String reason
    ) {
        assertEquals(Optional.of(revokedAt), record.revokedAt());
        assertEquals(Optional.of(reason), record.revocationReason());
    }

    private int count() {
        return dsl.fetchCount(DSL.table("authentication_session_record"));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required integration-test environment variable is missing: " + name
            );
        }
        return value;
    }
}
