package grandrue.infrastructure.persistence.audit;

import grandrue.application.MerchantScope;
import grandrue.audit.AuditActionClass;
import grandrue.audit.AuditExecutionScope;
import grandrue.audit.AuditRecord;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqAuditStoreIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T15:00:00Z");

    private DataSource authoritativeDataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
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
        transactionManager = new DataSourceTransactionManager(authoritativeDataSource);

        dsl.execute("truncate table audit_record");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-a'), ('merchant-b') on conflict do nothing");
    }

    @Test
    void record_survives_store_recreation_and_same_identity_replays_exact_evidence() {
        AuditRecord original = merchantRecord("audit-1", MERCHANT_A, T0);
        assertEquals(original, store().append(original));

        JooqAuditStore restarted = store();
        assertEquals(original, restarted.record("audit-1").orElseThrow());
        assertEquals(original, restarted.append(original));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("audit_record"))));
    }

    @Test
    void same_audit_identity_cannot_be_reused_for_different_claim() {
        JooqAuditStore store = store();
        store.append(merchantRecord("audit-1", MERCHANT_A, T0));

        AuditRecord conflicting = new AuditRecord(
                "audit-1",
                T0,
                "identity-1",
                AuditExecutionScope.MERCHANT,
                Optional.of(MERCHANT_A),
                AuditActionClass.EXECUTION_REJECTED,
                "booking.confirm",
                Optional.of("BOOKING"),
                Optional.of("booking-1"),
                "REJECTED",
                Optional.of("AUTHORITY_MISSING"),
                "correlation-1",
                Optional.of("command-1"),
                Optional.of("merchant-dashboard"),
                Optional.empty()
        );

        assertThrows(IllegalStateException.class, () -> store.append(conflicting));
        assertEquals(
                AuditActionClass.EXECUTION_ACCEPTED,
                store.record("audit-1").orElseThrow().actionClass()
        );
    }

    @Test
    void platform_and_merchant_evidence_remain_distinct_without_fake_grandrue_merchant() {
        JooqAuditStore store = store();
        AuditRecord merchant = merchantRecord("audit-merchant", MERCHANT_A, T0);
        AuditRecord platform = new AuditRecord(
                "audit-platform",
                T0.plusSeconds(1),
                "platform-operator-1",
                AuditExecutionScope.PLATFORM,
                Optional.empty(),
                AuditActionClass.ADMINISTRATIVE_ACTION,
                "session.revoke",
                Optional.of("SESSION"),
                Optional.of("session-1"),
                "COMPLETED",
                Optional.empty(),
                "correlation-platform",
                Optional.empty(),
                Optional.of("security-console"),
                Optional.empty()
        );

        store.append(merchant);
        store.append(platform);

        assertEquals(MERCHANT_A, store.record("audit-merchant").orElseThrow()
                .merchantScope().orElseThrow());
        assertTrue(store.record("audit-platform").orElseThrow().merchantScope().isEmpty());
        assertEquals(2, dsl.fetchCount(DSL.table(DSL.name("audit_record"))));
    }

    @Test
    void merchant_query_is_scope_isolated_half_open_and_bounded() {
        JooqAuditStore store = store();
        AuditRecord a0 = merchantRecord("audit-a0", MERCHANT_A, T0);
        AuditRecord a1 = merchantRecord("audit-a1", MERCHANT_A, T0.plusSeconds(10));
        AuditRecord b0 = merchantRecord("audit-b0", MERCHANT_B, T0.plusSeconds(5));
        store.append(a0);
        store.append(a1);
        store.append(b0);

        assertEquals(
                List.of(a0),
                store.merchantRecords(MERCHANT_A, T0, T0.plusSeconds(10), 10)
        );
        assertEquals(
                List.of(a0, a1),
                store.merchantRecords(MERCHANT_A, T0, T0.plusSeconds(20), 10)
        );
        assertEquals(
                List.of(a0),
                store.merchantRecords(MERCHANT_A, T0, T0.plusSeconds(20), 1)
        );
    }

    @Test
    void concurrent_duplicate_delivery_converges_on_one_audit_record() throws Exception {
        AuditRecord record = merchantRecord("audit-race", MERCHANT_A, T0);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<AuditRecord> first = executor.submit(() -> {
                start.await();
                return independentStore().append(record);
            });
            Future<AuditRecord> duplicate = executor.submit(() -> {
                start.await();
                return independentStore().append(record);
            });
            start.countDown();

            assertEquals(record, first.get());
            assertEquals(record, duplicate.get());
        }

        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("audit_record"))));
    }

    private JooqAuditStore store() {
        return new JooqAuditStore(dsl, transactionManager);
    }

    private JooqAuditStore independentStore() {
        return new JooqAuditStore(
                DSL.using(
                        new TransactionAwareDataSourceProxy(authoritativeDataSource),
                        SQLDialect.POSTGRES
                ),
                new DataSourceTransactionManager(authoritativeDataSource)
        );
    }

    private static AuditRecord merchantRecord(
            String auditIdentity,
            MerchantScope merchantScope,
            Instant occurredAt
    ) {
        return new AuditRecord(
                auditIdentity,
                occurredAt,
                "identity-1",
                AuditExecutionScope.MERCHANT,
                Optional.of(merchantScope),
                AuditActionClass.EXECUTION_ACCEPTED,
                "booking.confirm",
                Optional.of("BOOKING"),
                Optional.of("booking-1"),
                "COMPLETED",
                Optional.empty(),
                "correlation-1",
                Optional.of("command-1"),
                Optional.of("merchant-dashboard"),
                Optional.empty()
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: " + name
            );
        }
        return value;
    }
}
