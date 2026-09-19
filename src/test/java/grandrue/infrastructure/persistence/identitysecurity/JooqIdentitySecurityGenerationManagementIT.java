package grandrue.infrastructure.persistence.identitysecurity;

import grandrue.audit.AuditActionClass;
import grandrue.audit.AuditExecutionScope;
import grandrue.audit.AuditRecord;
import grandrue.audit.AuditStore;
import grandrue.identitysecurity.IdentitySecurityGeneration;
import grandrue.identitysecurity.IdentitySecurityGenerationException;
import grandrue.identitysecurity.IdentitySecurityGenerationFailureCategory;
import grandrue.identitysecurity.IdentitySecurityRotationCommand;
import grandrue.identitysecurity.IdentitySecurityRotationReason;
import grandrue.infrastructure.persistence.audit.JooqAuditStore;
import grandrue.infrastructure.persistence.runtime.JooqSessionRecordStore;
import grandrue.runtime.OpaqueSessionCredential;
import grandrue.runtime.SessionRecord;
import grandrue.runtime.SessionRecordStore;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqIdentitySecurityGenerationManagementIT {

    private static final Instant ESTABLISHED_AT =
            Instant.parse("2026-08-28T20:00:00Z");
    private static final Instant ROTATED_AT =
            ESTABLISHED_AT.plusSeconds(60);

    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    private DSLContext dsl;
    private SessionRecordStore sessions;
    private AuditStore audits;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
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
        audits = new JooqAuditStore(dsl, transactionManager);

        dsl.execute("truncate table audit_record");
        dsl.execute("truncate table authentication_session_record");
        dsl.execute("truncate table identity_security_generation");
    }

    @Test
    void initialization_is_idempotent_durable_and_exposes_the_runtime_reader() {
        JooqIdentitySecurityGenerationManagement management = management(audits);

        IdentitySecurityGeneration first = management.initialize(
                "identity-42",
                "generation-1",
                ESTABLISHED_AT
        );
        IdentitySecurityGeneration repeated = management.initialize(
                "identity-42",
                "different-candidate-must-not-replace",
                ESTABLISHED_AT.plusSeconds(1)
        );

        assertEquals(first, repeated);
        assertEquals("generation-1", first.generationReference());
        assertEquals(1, first.version());
        assertEquals(
                "generation-1",
                management(audits).currentSecurityGenerationReference("identity-42")
        );
        assertEquals(
                first,
                management(audits).state("identity-42").orElseThrow()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("identity_security_generation"))
        ));
    }

    @Test
    void rotation_atomically_replaces_generation_revokes_identity_sessions_and_appends_audit() {
        JooqIdentitySecurityGenerationManagement management = management(audits);
        management.initialize("identity-42", "generation-1", ESTABLISHED_AT);
        sessions.create(session("session-1", "identity-42"));
        sessions.create(session("session-2", "identity-42"));
        sessions.create(session("session-other", "identity-other"));

        IdentitySecurityGeneration rotated = management.rotate(command(
                "generation-1",
                "generation-2",
                "audit-1"
        ));

        assertEquals("generation-2", rotated.generationReference());
        assertEquals(2, rotated.version());
        assertEquals(Optional.of(ROTATED_AT), rotated.lastRotatedAt());
        assertTrue(sessions.sessionByIdentity("session-1").orElseThrow().revoked());
        assertTrue(sessions.sessionByIdentity("session-2").orElseThrow().revoked());
        assertFalse(sessions.sessionByIdentity("session-other").orElseThrow().revoked());

        AuditRecord audit = audits.record("audit-1").orElseThrow();
        assertEquals(AuditExecutionScope.PLATFORM, audit.executionScope());
        assertEquals(AuditActionClass.AUTHENTICATION_SECURITY, audit.actionClass());
        assertEquals(
                "IDENTITY_SECURITY_GENERATION_ROTATED",
                audit.actionIdentifier()
        );
        assertEquals(Optional.of("IDENTITY"), audit.subjectType());
        assertEquals(Optional.of("identity-42"), audit.subjectReference());
        assertEquals(
                Optional.of(IdentitySecurityRotationReason.AUTHENTICATOR_RESET.name()),
                audit.reasonCategory()
        );
        assertEquals(Optional.of("generation-2"), audit.evidenceReference());
    }

    @Test
    void stale_expected_generation_conflicts_without_revocation_or_audit() {
        JooqIdentitySecurityGenerationManagement management = management(audits);
        management.initialize("identity-42", "generation-1", ESTABLISHED_AT);
        sessions.create(session("session-1", "identity-42"));

        IdentitySecurityGenerationException conflict = assertThrows(
                IdentitySecurityGenerationException.class,
                () -> management.rotate(command(
                        "stale-generation",
                        "generation-2",
                        "audit-conflict"
                ))
        );

        assertEquals(
                IdentitySecurityGenerationFailureCategory.CONFLICT,
                conflict.category()
        );
        assertEquals(
                "generation-1",
                management.currentSecurityGenerationReference("identity-42")
        );
        assertFalse(sessions.sessionByIdentity("session-1").orElseThrow().revoked());
        assertTrue(audits.record("audit-conflict").isEmpty());
    }

    @Test
    void missing_state_is_distinct_from_a_concurrency_conflict() {
        IdentitySecurityGenerationException missing = assertThrows(
                IdentitySecurityGenerationException.class,
                () -> management(audits).rotate(command(
                        "generation-1",
                        "generation-2",
                        "audit-missing"
                ))
        );

        assertEquals(
                IdentitySecurityGenerationFailureCategory.NOT_ESTABLISHED,
                missing.category()
        );
        assertTrue(audits.record("audit-missing").isEmpty());
    }

    @Test
    void audit_failure_rolls_back_generation_and_session_revocation() {
        JooqIdentitySecurityGenerationManagement setup = management(audits);
        setup.initialize("identity-42", "generation-1", ESTABLISHED_AT);
        sessions.create(session("session-1", "identity-42"));

        AuditStore failingAudit = new FailingAuditStore();
        assertThrows(
                IllegalStateException.class,
                () -> management(failingAudit).rotate(command(
                        "generation-1",
                        "generation-2",
                        "audit-failure"
                ))
        );

        assertEquals(
                "generation-1",
                setup.currentSecurityGenerationReference("identity-42")
        );
        assertFalse(sessions.sessionByIdentity("session-1").orElseThrow().revoked());
        assertTrue(audits.record("audit-failure").isEmpty());
    }

    @Test
    void concurrent_rotation_from_one_expected_generation_commits_at_most_once()
            throws Exception {
        JooqIdentitySecurityGenerationManagement management = management(audits);
        management.initialize("identity-42", "generation-1", ESTABLISHED_AT);
        sessions.create(session("session-1", "identity-42"));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> first = executor.submit(() -> rotateAfterBarrier(
                    ready,
                    start,
                    command("generation-1", "generation-2a", "audit-2a")
            ));
            Future<Object> second = executor.submit(() -> rotateAfterBarrier(
                    ready,
                    start,
                    command("generation-1", "generation-2b", "audit-2b")
            ));
            ready.await();
            start.countDown();

            List<Object> outcomes = List.of(first.get(), second.get());
            long successes = outcomes.stream()
                    .filter(IdentitySecurityGeneration.class::isInstance)
                    .count();
            long conflicts = outcomes.stream()
                    .filter(IdentitySecurityGenerationException.class::isInstance)
                    .map(IdentitySecurityGenerationException.class::cast)
                    .filter(failure ->
                            failure.category()
                                    == IdentitySecurityGenerationFailureCategory.CONFLICT)
                    .count();

            assertEquals(1, successes);
            assertEquals(1, conflicts);
        }

        String current = management.currentSecurityGenerationReference("identity-42");
        assertTrue(current.equals("generation-2a") || current.equals("generation-2b"));
        assertNotEquals("generation-1", current);
        assertTrue(sessions.sessionByIdentity("session-1").orElseThrow().revoked());
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("audit_record"))
        ));
    }

    private Object rotateAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            IdentitySecurityRotationCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return management(audits).rotate(command);
        } catch (IdentitySecurityGenerationException failure) {
            return failure;
        }
    }

    private JooqIdentitySecurityGenerationManagement management(
            AuditStore auditStore
    ) {
        return new JooqIdentitySecurityGenerationManagement(
                dsl,
                transactionManager,
                sessions,
                auditStore
        );
    }

    private static IdentitySecurityRotationCommand command(
            String expected,
            String replacement,
            String auditIdentity
    ) {
        return new IdentitySecurityRotationCommand(
                "identity-42",
                expected,
                replacement,
                IdentitySecurityRotationReason.AUTHENTICATOR_RESET,
                ROTATED_AT,
                "principal-7",
                "correlation-8",
                auditIdentity,
                Optional.of("privileged-browser")
        );
    }

    private static SessionRecord session(
            String sessionIdentity,
            String identityReference
    ) {
        OpaqueSessionCredential credential =
                OpaqueSessionCredential.generate(new SecureRandom());
        return new SessionRecord(
                sessionIdentity,
                identityReference,
                credential.verifier(),
                ESTABLISHED_AT,
                "phishing-resistant",
                "webauthn-passkey",
                ESTABLISHED_AT.plusSeconds(12 * 60 * 60),
                ESTABLISHED_AT,
                "generation-1",
                Optional.empty(),
                Optional.empty()
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

    private static final class FailingAuditStore implements AuditStore {
        @Override
        public AuditRecord append(AuditRecord record) {
            throw new IllegalStateException("simulated audit failure");
        }

        @Override
        public Optional<AuditRecord> record(String auditIdentity) {
            return Optional.empty();
        }

        @Override
        public List<AuditRecord> merchantRecords(
                grandrue.application.MerchantScope merchantScope,
                Instant fromInclusive,
                Instant untilExclusive,
                int limit
        ) {
            return List.of();
        }
    }
}
