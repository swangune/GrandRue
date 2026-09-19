package grandrue.infrastructure.persistence.webauthn;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.security.web.webauthn.api.AuthenticatorTransport;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutableCredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqWebAuthnUserCredentialRepositoryIT {

    private static final Instant CREATED = Instant.parse("2026-08-28T08:00:00Z");
    private static final Instant FIRST_USED = Instant.parse("2026-08-28T08:05:00Z");
    private static final Instant SECOND_USED = Instant.parse("2026-08-28T08:10:00Z");

    private DataSource dataSource;
    private DSLContext dsl;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
        dsl.execute("truncate table webauthn_credential_record");
    }

    @Test
    void registered_credential_survives_repository_recreation() {
        CredentialRecord registered = credential(
                bytes(1, 2, 3),
                bytes(11, 12),
                new byte[]{21, 22, 23},
                4,
                true,
                false,
                FIRST_USED,
                "Phone passkey"
        );

        repository().save(registered);

        CredentialRecord reloaded = repository().findByCredentialId(bytes(1, 2, 3));
        assertRecordEquals(registered, reloaded);
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("webauthn_credential_record"))));
    }

    @Test
    void spring_save_can_progress_runtime_authenticator_state_without_rebinding_registration_identity() {
        CredentialRecord registered = credential(
                bytes(1, 2, 3),
                bytes(11, 12),
                new byte[]{21, 22, 23},
                4,
                true,
                false,
                FIRST_USED,
                "Phone passkey"
        );
        repository().save(registered);

        CredentialRecord progressed = credential(
                bytes(1, 2, 3),
                bytes(11, 12),
                new byte[]{21, 22, 23},
                5,
                true,
                true,
                SECOND_USED,
                "Primary passkey"
        );
        repository().save(progressed);

        assertRecordEquals(
                progressed,
                repository().findByCredentialId(bytes(1, 2, 3))
        );
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("webauthn_credential_record"))));
    }

    @Test
    void credential_identifier_cannot_be_rebound_to_another_user_or_public_key() {
        CredentialRecord registered = credential(
                bytes(1, 2, 3),
                bytes(11, 12),
                new byte[]{21, 22, 23},
                4,
                true,
                false,
                FIRST_USED,
                "Phone passkey"
        );
        repository().save(registered);

        CredentialRecord anotherUser = credential(
                bytes(1, 2, 3),
                bytes(99, 98),
                new byte[]{21, 22, 23},
                5,
                true,
                false,
                SECOND_USED,
                "Conflicting passkey"
        );
        assertThrows(IllegalStateException.class, () -> repository().save(anotherUser));

        CredentialRecord anotherKey = credential(
                bytes(1, 2, 3),
                bytes(11, 12),
                new byte[]{77, 78, 79},
                5,
                true,
                false,
                SECOND_USED,
                "Conflicting passkey"
        );
        assertThrows(IllegalStateException.class, () -> repository().save(anotherKey));

        assertRecordEquals(registered, repository().findByCredentialId(bytes(1, 2, 3)));
    }

    @Test
    void lookup_by_user_handle_is_exact_and_delete_removes_only_selected_credential() {
        CredentialRecord first = credential(
                bytes(1, 2, 3),
                bytes(11, 12),
                new byte[]{21, 22, 23},
                0,
                true,
                false,
                FIRST_USED,
                "First"
        );
        CredentialRecord second = credential(
                bytes(4, 5, 6),
                bytes(11, 12),
                new byte[]{31, 32, 33},
                0,
                true,
                false,
                FIRST_USED,
                "Second"
        );
        CredentialRecord anotherUser = credential(
                bytes(7, 8, 9),
                bytes(44, 45),
                new byte[]{41, 42, 43},
                0,
                true,
                false,
                FIRST_USED,
                "Other"
        );

        repository().save(first);
        repository().save(second);
        repository().save(anotherUser);

        List<CredentialRecord> owned = repository().findByUserId(bytes(11, 12));
        assertEquals(2, owned.size());
        assertEquals(Set.of("First", "Second"), owned.stream()
                .map(CredentialRecord::getLabel)
                .collect(java.util.stream.Collectors.toSet()));

        repository().delete(bytes(1, 2, 3));

        assertNull(repository().findByCredentialId(bytes(1, 2, 3)));
        assertRecordEquals(second, repository().findByCredentialId(bytes(4, 5, 6)));
        assertRecordEquals(anotherUser, repository().findByCredentialId(bytes(7, 8, 9)));
    }

    private JooqWebAuthnUserCredentialRepository repository() {
        return new JooqWebAuthnUserCredentialRepository(dsl);
    }

    private static CredentialRecord credential(
            Bytes credentialId,
            Bytes userId,
            byte[] publicKey,
            long signatureCount,
            boolean uvInitialized,
            boolean backupState,
            Instant lastUsed,
            String label
    ) {
        return ImmutableCredentialRecord.builder()
                .credentialType(PublicKeyCredentialType.PUBLIC_KEY)
                .credentialId(credentialId)
                .userEntityUserId(userId)
                .publicKey(new ImmutablePublicKeyCose(publicKey))
                .signatureCount(signatureCount)
                .uvInitialized(uvInitialized)
                .transports(Set.of(AuthenticatorTransport.INTERNAL, AuthenticatorTransport.HYBRID))
                .backupEligible(true)
                .backupState(backupState)
                .attestationObject(bytes(51, 52, 53))
                .attestationClientDataJSON(bytes(61, 62, 63))
                .created(CREATED)
                .lastUsed(lastUsed)
                .label(label)
                .build();
    }

    private static void assertRecordEquals(CredentialRecord expected, CredentialRecord actual) {
        assertEquals(expected.getCredentialType(), actual.getCredentialType());
        assertEquals(expected.getCredentialId(), actual.getCredentialId());
        assertEquals(expected.getUserEntityUserId(), actual.getUserEntityUserId());
        assertArrayEquals(expected.getPublicKey().getBytes(), actual.getPublicKey().getBytes());
        assertEquals(expected.getSignatureCount(), actual.getSignatureCount());
        assertEquals(expected.isUvInitialized(), actual.isUvInitialized());
        assertEquals(expected.getTransports(), actual.getTransports());
        assertEquals(expected.isBackupEligible(), actual.isBackupEligible());
        assertEquals(expected.isBackupState(), actual.isBackupState());
        assertEquals(expected.getAttestationObject(), actual.getAttestationObject());
        assertEquals(expected.getAttestationClientDataJSON(), actual.getAttestationClientDataJSON());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getLastUsed(), actual.getLastUsed());
        assertEquals(expected.getLabel(), actual.getLabel());
    }

    private static Bytes bytes(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return new Bytes(bytes);
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required environment variable is missing: " + name);
        }
        return value;
    }
}
