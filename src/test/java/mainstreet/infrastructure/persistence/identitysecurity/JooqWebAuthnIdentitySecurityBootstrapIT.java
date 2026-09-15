package mainstreet.infrastructure.persistence.identitysecurity;

import mainstreet.audit.AuditStore;
import mainstreet.identitysecurity.IdentitySecurityGenerationService;
import mainstreet.infrastructure.persistence.audit.JooqAuditStore;
import mainstreet.infrastructure.persistence.runtime.JooqSessionRecordStore;
import mainstreet.infrastructure.persistence.webauthn.JooqWebAuthnAuthenticationSubjectRepository;
import mainstreet.infrastructure.security.webauthn.SpringWebAuthnSessionBridge;
import mainstreet.infrastructure.security.webauthn.WebAuthnAuthenticationSubjectService;
import mainstreet.infrastructure.security.webauthn.WebAuthnSessionEstablishmentPolicy;
import mainstreet.runtime.EstablishedHumanSession;
import mainstreet.runtime.HumanSessionEstablishmentService;
import mainstreet.runtime.SessionRecord;
import mainstreet.runtime.SessionRecordStore;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JooqWebAuthnIdentitySecurityBootstrapIT {

    private static final Instant NOW =
            Instant.parse("2026-08-28T21:00:00Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;
    private SessionRecordStore sessions;
    private JooqIdentitySecurityGenerationManagement securityGenerations;
    private JooqWebAuthnAuthenticationSubjectRepository subjects;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        sessions = new JooqSessionRecordStore(dsl);
        AuditStore audits = new JooqAuditStore(dsl, transactionManager);
        securityGenerations = new JooqIdentitySecurityGenerationManagement(
                dsl,
                transactionManager,
                sessions,
                audits
        );
        subjects = new JooqWebAuthnAuthenticationSubjectRepository(dsl);

        dsl.execute("truncate table webauthn_credential_record");
        dsl.execute("truncate table webauthn_authentication_subject");
        dsl.execute("truncate table audit_record");
        dsl.execute("truncate table authentication_session_record");
        dsl.execute("truncate table identity_security_generation");
    }

    @Test
    void webauthn_subject_establishment_idempotently_bootstraps_identity_security() {
        AtomicInteger candidates = new AtomicInteger();
        IdentitySecurityGenerationService initializer =
                new IdentitySecurityGenerationService(
                        securityGenerations,
                        Clock.fixed(NOW, ZoneOffset.UTC),
                        () -> "generation-" + candidates.incrementAndGet()
                );
        WebAuthnAuthenticationSubjectService service =
                new WebAuthnAuthenticationSubjectService(subjects, initializer);

        service.establish(
                "identity-42",
                "controller@example.test",
                "Controller"
        );
        service.establish(
                "identity-42",
                "updated@example.test",
                "Updated Controller"
        );

        assertEquals(
                "generation-1",
                securityGenerations.currentSecurityGenerationReference(
                        "identity-42"
                )
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("identity_security_generation"))
        ));
    }

    @Test
    void webauthn_session_bridge_snapshots_the_same_durable_generation_authority() {
        IdentitySecurityGenerationService initializer =
                new IdentitySecurityGenerationService(
                        securityGenerations,
                        Clock.fixed(NOW, ZoneOffset.UTC),
                        () -> "generation-1"
                );
        PublicKeyCredentialUserEntity principal =
                new WebAuthnAuthenticationSubjectService(subjects, initializer)
                        .establish(
                                "identity-42",
                                "controller@example.test",
                                "Controller"
                        );
        WebAuthnAuthentication authentication =
                mock(WebAuthnAuthentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        SpringWebAuthnSessionBridge bridge = new SpringWebAuthnSessionBridge(
                new HumanSessionEstablishmentService(sessions),
                subjects,
                securityGenerations,
                new WebAuthnSessionEstablishmentPolicy(
                        Duration.ofHours(12),
                        "webauthn-user-verified",
                        "webauthn-passkey"
                ),
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        EstablishedHumanSession established = bridge.establish(authentication);
        SessionRecord record = sessions.sessionByIdentity(
                established.sessionIdentity()
        ).orElseThrow();

        assertEquals("identity-42", record.identityReference());
        assertEquals("generation-1", record.securityGenerationReference());
        assertEquals(
                securityGenerations.currentSecurityGenerationReference(
                        "identity-42"
                ),
                record.securityGenerationReference()
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: " + name
            );
        }
        return value;
    }
}
