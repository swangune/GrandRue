package mainstreet.infrastructure.persistence.merchantaccount;

import mainstreet.application.TrustedPlatformHumanPrincipal;
import mainstreet.merchantaccount.MerchantAccountBootstrapStore;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantAccountBootstrapStoreIT {

    private DSLContext dsl;
    private JooqMerchantAccountBootstrapStore store;

    @BeforeEach
    void setUp() {
        DataSource authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );

        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        TransactionAwareDataSourceProxy transactionAwareDataSource =
                new TransactionAwareDataSourceProxy(authoritativeDataSource);
        dsl = DSL.using(transactionAwareDataSource, SQLDialect.POSTGRES);

        store = new JooqMerchantAccountBootstrapStore(
                dsl,
                new DataSourceTransactionManager(authoritativeDataSource)
        );

        clearMerchantAccountBootstrapTables();
    }

    @Test
    void first_establishment_atomically_persists_account_initial_controller_and_request_evidence() {
        MerchantAccountBootstrapStore.BootstrapOutcome outcome = store.establishIfAbsent(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        assertFalse(outcome.alreadyEstablished());
        assertEquals(1, count("merchant_account"));
        assertEquals(1, count("merchant_controller_relationship"));
        assertEquals(1, count("merchant_account_establishment_request"));

        String merchantIdentifier = outcome.merchantAccount().merchantIdentifier();

        assertEquals(
                "identity-1",
                dsl.select(DSL.field(DSL.name("identity_identifier"), String.class))
                        .from(DSL.table(DSL.name("merchant_controller_relationship")))
                        .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                                .eq(merchantIdentifier))
                        .fetchOne(0, String.class)
        );
        assertEquals(
                "ACTIVE",
                dsl.select(DSL.field(DSL.name("lifecycle"), String.class))
                        .from(DSL.table(DSL.name("merchant_controller_relationship")))
                        .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                                .eq(merchantIdentifier))
                        .fetchOne(0, String.class)
        );
        assertEquals(
                merchantIdentifier,
                dsl.select(DSL.field(DSL.name("merchant_identifier"), String.class))
                        .from(DSL.table(DSL.name("merchant_account_establishment_request")))
                        .where(DSL.field(
                                        DSL.name("logical_establishment_request_identity"),
                                        String.class
                                )
                                .eq("request-1"))
                        .fetchOne(0, String.class)
        );
    }

    @Test
    void replay_of_same_logical_request_returns_the_original_bootstrap_without_duplication() {
        TrustedPlatformHumanPrincipal principal =
                new TrustedPlatformHumanPrincipal("identity-1");

        MerchantAccountBootstrapStore.BootstrapOutcome first =
                store.establishIfAbsent("request-1", principal);
        MerchantAccountBootstrapStore.BootstrapOutcome replay =
                store.establishIfAbsent("request-1", principal);

        assertFalse(first.alreadyEstablished());
        assertTrue(replay.alreadyEstablished());
        assertEquals(first.merchantAccount(), replay.merchantAccount());
        assertEquals(1, count("merchant_account"));
        assertEquals(1, count("merchant_controller_relationship"));
        assertEquals(1, count("merchant_account_establishment_request"));
    }

    @Test
    void distinct_logical_requests_from_same_controller_remain_distinct_accounts() {
        TrustedPlatformHumanPrincipal principal =
                new TrustedPlatformHumanPrincipal("identity-1");

        MerchantAccountBootstrapStore.BootstrapOutcome first =
                store.establishIfAbsent("request-1", principal);
        MerchantAccountBootstrapStore.BootstrapOutcome second =
                store.establishIfAbsent("request-2", principal);

        assertNotEquals(
                first.merchantAccount().merchantIdentifier(),
                second.merchantAccount().merchantIdentifier()
        );
        assertEquals(2, count("merchant_account"));
        assertEquals(2, count("merchant_controller_relationship"));
        assertEquals(2, count("merchant_account_establishment_request"));
    }

    @Test
    void concurrent_duplicate_request_produces_one_authoritative_bootstrap() throws Exception {
        TrustedPlatformHumanPrincipal principal =
                new TrustedPlatformHumanPrincipal("identity-1");
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<MerchantAccountBootstrapStore.BootstrapOutcome> firstFuture =
                    executor.submit(() -> {
                        start.await();
                        return store.establishIfAbsent("request-race", principal);
                    });
            Future<MerchantAccountBootstrapStore.BootstrapOutcome> secondFuture =
                    executor.submit(() -> {
                        start.await();
                        return store.establishIfAbsent("request-race", principal);
                    });

            start.countDown();

            List<MerchantAccountBootstrapStore.BootstrapOutcome> outcomes = List.of(
                    firstFuture.get(),
                    secondFuture.get()
            );

            assertEquals(
                    outcomes.get(0).merchantAccount(),
                    outcomes.get(1).merchantAccount()
            );
            assertEquals(
                    1,
                    outcomes.stream().filter(outcome -> !outcome.alreadyEstablished()).count()
            );
            assertEquals(
                    1,
                    outcomes.stream().filter(MerchantAccountBootstrapStore.BootstrapOutcome::alreadyEstablished).count()
            );
        }

        assertEquals(1, count("merchant_account"));
        assertEquals(1, count("merchant_controller_relationship"));
        assertEquals(1, count("merchant_account_establishment_request"));
    }

    private int count(String tableName) {
        return dsl.fetchCount(DSL.table(DSL.name(tableName)));
    }

    private void clearMerchantAccountBootstrapTables() {
        dsl.execute("truncate table merchant_account cascade");
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