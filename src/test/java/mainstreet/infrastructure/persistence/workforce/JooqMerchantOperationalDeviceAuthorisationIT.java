package mainstreet.infrastructure.persistence.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.application.TrustedDeviceApplicationContext;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.workforce.MerchantOperationalDeviceAuthorisation;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantOperationalDeviceAuthorisationIT {

    private static final MerchantScope MERCHANT_A =
            new MerchantScope("merchant-device-a");
    private static final MerchantScope MERCHANT_B =
            new MerchantScope("merchant-device-b");
    private static final ExecutionPrincipal CONTROLLER_A =
            new ExecutionPrincipal("controller-a");
    private static final ExecutionPrincipal CONTROLLER_B =
            new ExecutionPrincipal("controller-b");
    private static final TrustedDeviceApplicationContext DEVICE =
            new TrustedDeviceApplicationContext("verified-device-binding-1");
    private static final Instant T0 = Instant.parse("2026-08-24T04:30:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private JooqMerchantOperationalDeviceAuthorisationStore store;

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

        TransactionAwareDataSourceProxy transactionAware =
                new TransactionAwareDataSourceProxy(dataSource);
        dsl = DSL.using(transactionAware, SQLDialect.POSTGRES);
        store = new JooqMerchantOperationalDeviceAuthorisationStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );

        clearDeviceAuthorisationTables();
        clearMerchantAndControllerFixtures();
        prepareMerchantAndController(
                MERCHANT_A,
                CONTROLLER_A,
                "device-controller-rel-a"
        );
        prepareMerchantAndController(
                MERCHANT_B,
                CONTROLLER_B,
                "device-controller-rel-b"
        );
    }

    @AfterEach
    void tearDown() {
        clearDeviceAuthorisationTables();
        clearMerchantAndControllerFixtures();
    }

    @Test
    void current_controller_can_authorise_verified_device_context_and_fact_survives_store_recreation() {
        MerchantOperationalDeviceAuthorisation first = store.establishIfCurrentController(
                "request-1",
                MERCHANT_A,
                CONTROLLER_A,
                DEVICE,
                "device-auth-1",
                T0
        );

        JooqMerchantOperationalDeviceAuthorisationStore recreated =
                new JooqMerchantOperationalDeviceAuthorisationStore(
                        dsl,
                        new DataSourceTransactionManager(dataSource)
                );

        assertTrue(recreated.isActive(MERCHANT_A, DEVICE));
        assertEquals(first, recreated.find("device-auth-1").orElseThrow());
        assertEquals(1, count("workforce_operational_device_authorisation"));
        assertEquals(1, count("workforce_operational_device_authorisation_request"));
    }

    @Test
    void non_controller_cannot_authorise_device_even_with_verified_binding() {
        assertThrows(
                AuthorizationException.class,
                () -> store.establishIfCurrentController(
                        "request-1",
                        MERCHANT_A,
                        new ExecutionPrincipal("staff-a"),
                        DEVICE,
                        "device-auth-1",
                        T0
                )
        );

        assertFalse(store.isActive(MERCHANT_A, DEVICE));
        assertEquals(0, count("workforce_operational_device_authorisation"));
    }

    @Test
    void same_logical_request_replays_original_fact_without_duplication() {
        MerchantOperationalDeviceAuthorisation first = store.establishIfCurrentController(
                "request-1",
                MERCHANT_A,
                CONTROLLER_A,
                DEVICE,
                "device-auth-1",
                T0
        );
        MerchantOperationalDeviceAuthorisation replay = store.establishIfCurrentController(
                "request-1",
                MERCHANT_A,
                CONTROLLER_A,
                DEVICE,
                "ignored-new-id",
                T0.plusSeconds(30)
        );

        assertEquals(first, replay);
        assertEquals(1, count("workforce_operational_device_authorisation"));
        assertEquals(1, count("workforce_operational_device_authorisation_request"));
    }

    @Test
    void committed_request_identity_cannot_be_reused_for_different_device_intent() {
        store.establishIfCurrentController(
                "request-1",
                MERCHANT_A,
                CONTROLLER_A,
                DEVICE,
                "device-auth-1",
                T0
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> store.establishIfCurrentController(
                        "request-1",
                        MERCHANT_A,
                        CONTROLLER_A,
                        new TrustedDeviceApplicationContext("verified-device-binding-2"),
                        "device-auth-2",
                        T0.plusSeconds(60)
                )
        );

        assertEquals(1, count("workforce_operational_device_authorisation"));
    }

    @Test
    void device_authorisation_is_merchant_scoped() {
        store.establishIfCurrentController(
                "request-a",
                MERCHANT_A,
                CONTROLLER_A,
                DEVICE,
                "device-auth-a",
                T0
        );

        assertTrue(store.isActive(MERCHANT_A, DEVICE));
        assertFalse(store.isActive(MERCHANT_B, DEVICE));
    }

    @Test
    void revoked_authorisation_stays_revoked_on_old_request_replay_and_same_binding_can_be_reauthorised_as_new_fact() {
        MerchantOperationalDeviceAuthorisation first = store.establishIfCurrentController(
                "request-1",
                MERCHANT_A,
                CONTROLLER_A,
                DEVICE,
                "device-auth-1",
                T0
        );

        assertTrue(store.revokeIfCurrentController(
                MERCHANT_A,
                CONTROLLER_A,
                first.authorisationIdentifier(),
                T0.plusSeconds(60)
        ));
        assertFalse(store.isActive(MERCHANT_A, DEVICE));

        MerchantOperationalDeviceAuthorisation oldReplay =
                store.establishIfCurrentController(
                        "request-1",
                        MERCHANT_A,
                        CONTROLLER_A,
                        DEVICE,
                        "ignored-id",
                        T0.plusSeconds(120)
                );
        assertFalse(oldReplay.isActive());
        assertFalse(store.isActive(MERCHANT_A, DEVICE));

        MerchantOperationalDeviceAuthorisation second =
                store.establishIfCurrentController(
                        "request-2",
                        MERCHANT_A,
                        CONTROLLER_A,
                        DEVICE,
                        "device-auth-2",
                        T0.plusSeconds(180)
                );

        assertNotEquals(first.authorisationIdentifier(), second.authorisationIdentifier());
        assertTrue(second.isActive());
        assertTrue(store.isActive(MERCHANT_A, DEVICE));
        assertEquals(2, count("workforce_operational_device_authorisation"));
    }

    @Test
    void concurrent_replay_of_one_enrolment_request_commits_one_authorisation_fact() throws Exception {
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<MerchantOperationalDeviceAuthorisation> firstFuture = executor.submit(() -> {
                start.await();
                return store.establishIfCurrentController(
                        "request-race",
                        MERCHANT_A,
                        CONTROLLER_A,
                        DEVICE,
                        "device-auth-a",
                        T0
                );
            });
            Future<MerchantOperationalDeviceAuthorisation> secondFuture = executor.submit(() -> {
                start.await();
                return store.establishIfCurrentController(
                        "request-race",
                        MERCHANT_A,
                        CONTROLLER_A,
                        DEVICE,
                        "device-auth-b",
                        T0
                );
            });

            start.countDown();
            List<MerchantOperationalDeviceAuthorisation> outcomes = List.of(
                    firstFuture.get(),
                    secondFuture.get()
            );

            assertEquals(outcomes.get(0), outcomes.get(1));
        }

        assertEquals(1, count("workforce_operational_device_authorisation"));
        assertEquals(1, count("workforce_operational_device_authorisation_request"));
    }

    private void prepareMerchantAndController(
            MerchantScope merchantScope,
            ExecutionPrincipal controller,
            String relationshipIdentifier
    ) {
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(merchantScope.merchantIdentifier())
                .onConflictDoNothing()
                .execute();

        dsl.deleteFrom(DSL.table(DSL.name("merchant_controller_relationship")))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .eq(merchantScope.merchantIdentifier()))
                .execute();

        dsl.insertInto(DSL.table(DSL.name("merchant_controller_relationship")))
                .columns(
                        DSL.field(DSL.name("controller_relationship_identifier")),
                        DSL.field(DSL.name("merchant_identifier")),
                        DSL.field(DSL.name("identity_identifier")),
                        DSL.field(DSL.name("lifecycle"))
                )
                .values(
                        relationshipIdentifier,
                        merchantScope.merchantIdentifier(),
                        controller.identifier(),
                        "ACTIVE"
                )
                .execute();
    }

    private void clearDeviceAuthorisationTables() {
        dsl.deleteFrom(DSL.table(
                        DSL.name("workforce_operational_device_authorisation_request")
                ))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .in(
                                MERCHANT_A.merchantIdentifier(),
                                MERCHANT_B.merchantIdentifier()
                        ))
                .execute();
        dsl.deleteFrom(DSL.table(
                        DSL.name("workforce_operational_device_authorisation")
                ))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .in(
                                MERCHANT_A.merchantIdentifier(),
                                MERCHANT_B.merchantIdentifier()
                        ))
                .execute();
    }

    private void clearMerchantAndControllerFixtures() {
        dsl.deleteFrom(DSL.table(DSL.name("merchant_controller_relationship")))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .in(
                                MERCHANT_A.merchantIdentifier(),
                                MERCHANT_B.merchantIdentifier()
                        ))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("merchant_account")))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .in(
                                MERCHANT_A.merchantIdentifier(),
                                MERCHANT_B.merchantIdentifier()
                        ))
                .execute();
    }

    private int count(String tableName) {
        return dsl.fetchCount(DSL.table(DSL.name(tableName)));
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
