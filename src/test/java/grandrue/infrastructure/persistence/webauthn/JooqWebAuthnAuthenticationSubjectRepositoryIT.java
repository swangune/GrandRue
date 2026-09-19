package grandrue.infrastructure.persistence.webauthn;

import grandrue.infrastructure.security.webauthn.WebAuthnAuthenticationSubjectService;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqWebAuthnAuthenticationSubjectRepositoryIT {

    private DSLContext dsl;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
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
        dsl.execute("truncate table webauthn_authentication_subject");
    }

    @Test
    void main_street_establishes_one_opaque_subject_handle_for_one_identity() {
        JooqWebAuthnAuthenticationSubjectRepository repository = repository();
        WebAuthnAuthenticationSubjectService service =
                subjectService(repository);

        PublicKeyCredentialUserEntity first = service.establish(
                "identity-42",
                "controller@example.test",
                "Controller"
        );
        PublicKeyCredentialUserEntity again = service.establish(
                "identity-42",
                "new-controller-name@example.test",
                "Updated Controller"
        );

        assertEquals(first.getId(), again.getId());
        assertEquals(32, first.getId().getBytes().length);
        assertNotEquals(
                "identity-42",
                new String(first.getId().getBytes(), StandardCharsets.UTF_8)
        );
        assertEquals("identity-42", repository.identityReference(again));
        assertEquals("new-controller-name@example.test", again.getName());
        assertEquals("Updated Controller", again.getDisplayName());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("webauthn_authentication_subject"))));
    }

    @Test
    void different_identities_receive_different_handles_and_resolve_independently() {
        JooqWebAuthnAuthenticationSubjectRepository repository = repository();
        WebAuthnAuthenticationSubjectService service =
                subjectService(repository);

        PublicKeyCredentialUserEntity first = service.establish(
                "identity-1", "one@example.test", "One"
        );
        PublicKeyCredentialUserEntity second = service.establish(
                "identity-2", "two@example.test", "Two"
        );

        assertNotEquals(first.getId(), second.getId());
        assertEquals("identity-1", repository.identityReference(first));
        assertEquals("identity-2", repository.identityReference(second));
        assertEquals(first, repository.findByUsername("one@example.test"));
        assertEquals(second, repository.findById(second.getId()));
    }

    @Test
    void spring_save_may_refresh_human_readable_fields_but_cannot_create_a_caller_selected_handle() {
        JooqWebAuthnAuthenticationSubjectRepository repository = repository();
        PublicKeyCredentialUserEntity established =
                subjectService(repository).establish(
                        "identity-1", "one@example.test", "One"
                );

        PublicKeyCredentialUserEntity renamed = ImmutablePublicKeyCredentialUserEntity.builder()
                .id(established.getId())
                .name("renamed@example.test")
                .displayName("Renamed")
                .build();
        repository.save(renamed);

        assertEquals(renamed, repository.findById(established.getId()));
        assertNull(repository.findByUsername("one@example.test"));
        assertEquals("identity-1", repository.identityReference(renamed));

        PublicKeyCredentialUserEntity callerSelected =
                ImmutablePublicKeyCredentialUserEntity.builder()
                        .id(new Bytes(new byte[]{1, 2, 3, 4}))
                        .name("attacker@example.test")
                        .displayName("Attacker")
                        .build();
        assertThrows(IllegalStateException.class, () -> repository.save(callerSelected));
    }

    @Test
    void an_existing_handle_or_identity_cannot_be_rebound() {
        JooqWebAuthnAuthenticationSubjectRepository repository = repository();
        PublicKeyCredentialUserEntity established =
                subjectService(repository).establish(
                        "identity-1", "one@example.test", "One"
                );

        PublicKeyCredentialUserEntity sameHandle =
                ImmutablePublicKeyCredentialUserEntity.builder()
                        .id(established.getId())
                        .name("two@example.test")
                        .displayName("Two")
                        .build();

        assertThrows(
                IllegalStateException.class,
                () -> repository.establish("identity-2", sameHandle)
        );

        PublicKeyCredentialUserEntity anotherHandle =
                ImmutablePublicKeyCredentialUserEntity.builder()
                        .id(new Bytes(new byte[32]))
                        .name("other@example.test")
                        .displayName("Other")
                        .build();
        assertThrows(
                IllegalStateException.class,
                () -> repository.establish("identity-1", anotherHandle)
        );

        assertArrayEquals(
                established.getId().getBytes(),
                repository.findByUsername("one@example.test").getId().getBytes()
        );
        assertEquals("identity-1", repository.identityReference(established));
    }

    @Test
    void delete_removes_only_the_technical_subject_and_never_semantic_identity_authority() {
        JooqWebAuthnAuthenticationSubjectRepository repository = repository();
        PublicKeyCredentialUserEntity established =
                subjectService(repository).establish(
                        "identity-1", "one@example.test", "One"
                );

        repository.delete(established.getId());

        assertNull(repository.findById(established.getId()));
        assertThrows(IllegalStateException.class, () -> repository.identityReference(established));
    }

    private static WebAuthnAuthenticationSubjectService subjectService(
            JooqWebAuthnAuthenticationSubjectRepository repository
    ) {
        return new WebAuthnAuthenticationSubjectService(
                repository,
                ignoredIdentity -> {
                    // This repository-focused test supplies a bounded initializer.
                }
        );
    }

    private JooqWebAuthnAuthenticationSubjectRepository repository() {
        return new JooqWebAuthnAuthenticationSubjectRepository(dsl);
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required environment variable is missing: " + name);
        }
        return value;
    }
}
