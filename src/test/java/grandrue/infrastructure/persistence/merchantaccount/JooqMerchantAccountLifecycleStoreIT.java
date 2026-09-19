package grandrue.infrastructure.persistence.merchantaccount;

import grandrue.application.MerchantScope;
import grandrue.merchantaccount.BeginMerchantAccountClosureCommand;
import grandrue.merchantaccount.FinalizeMerchantAccountClosureCommand;
import grandrue.merchantaccount.MerchantAccountLifecycle;
import grandrue.merchantaccount.MerchantAccountLifecycleConflictException;
import grandrue.merchantaccount.MerchantAccountSuspendedException;
import grandrue.merchantaccount.MerchantAccountSuspensionCommand;
import grandrue.merchantaccount.MerchantAccountSuspensionReleaseCommand;
import grandrue.merchantaccount.MerchantControllerRelationship;
import grandrue.merchantaccount.MerchantControllerRelationshipLifecycle;
import grandrue.merchantaccount.MerchantControllerTransferCommand;
import grandrue.merchantaccount.MerchantControllerTransferConflictException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantAccountLifecycleStoreIT {

    private static final MerchantScope MERCHANT_A =
            new MerchantScope("merchant-lifecycle-a");
    private static final MerchantScope MERCHANT_B =
            new MerchantScope("merchant-lifecycle-b");
    private static final Instant T0 = Instant.parse("2026-08-24T06:00:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private JooqMerchantAccountLifecycleStore store;

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
        store = new JooqMerchantAccountLifecycleStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );
        clearMerchant(MERCHANT_A);
        clearMerchant(MERCHANT_B);
        prepareMerchant(MERCHANT_A, "controller-rel-a", "identity-a");
        prepareMerchant(MERCHANT_B, "controller-rel-b", "identity-b");
    }

    @AfterEach
    void tearDown() {
        clearMerchant(MERCHANT_A);
        clearMerchant(MERCHANT_B);
    }

    @Test
    void transfer_atomically_ends_old_controller_and_activates_recipient_with_replay() {
        MerchantControllerTransferCommand command = transfer(
                "transfer-1",
                MERCHANT_A,
                "controller-rel-a",
                "identity-a",
                "identity-new",
                "controller-rel-new"
        );

        MerchantControllerRelationship first = store.transferController(command);
        MerchantControllerRelationship replay = store.transferController(command);

        assertEquals(first.relationshipIdentifier(), replay.relationshipIdentifier());
        assertEquals("identity-new", store.activeController(MERCHANT_A)
                .orElseThrow().identityIdentifier());
        assertEquals(1, activeControllerCount(MERCHANT_A));
        assertEquals(
                MerchantControllerRelationshipLifecycle.ENDED.name(),
                controllerLifecycle("controller-rel-a")
        );
        assertEquals(1, countForMerchant(
                "merchant_controller_transfer_request",
                MERCHANT_A
        ));
    }

    @Test
    void concurrent_transfers_from_same_controller_allow_at_most_one_commit()
            throws Exception {
        MerchantControllerTransferCommand firstCommand = transfer(
                "transfer-race-1",
                MERCHANT_A,
                "controller-rel-a",
                "identity-a",
                "identity-c",
                "controller-rel-c"
        );
        MerchantControllerTransferCommand secondCommand = transfer(
                "transfer-race-2",
                MERCHANT_A,
                "controller-rel-a",
                "identity-a",
                "identity-d",
                "controller-rel-d"
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Boolean> first = executor.submit(() -> transferAttempt(
                    firstCommand,
                    ready,
                    start
            ));
            Future<Boolean> second = executor.submit(() -> transferAttempt(
                    secondCommand,
                    ready,
                    start
            ));
            ready.await();
            start.countDown();

            List<Boolean> outcomes = List.of(first.get(), second.get());
            assertEquals(1, outcomes.stream().filter(Boolean::booleanValue).count());
        }
        assertEquals(1, activeControllerCount(MERCHANT_A));
        assertFalse(store.activeController(MERCHANT_A)
                .orElseThrow().identityIdentifier().equals("identity-a"));
    }

    @Test
    void merchant_wide_suspension_blocks_normal_transfer_without_ending_controller() {
        store.establishSuspension(suspension(
                "suspend-request-1",
                "suspension-1",
                "security-authority"
        ));

        assertThrows(
                MerchantAccountSuspendedException.class,
                () -> store.transferController(transfer(
                        "transfer-1",
                        MERCHANT_A,
                        "controller-rel-a",
                        "identity-a",
                        "identity-new",
                        "controller-rel-new"
                ))
        );
        assertEquals("identity-a", store.activeController(MERCHANT_A)
                .orElseThrow().identityIdentifier());
    }

    @Test
    void independent_suspensions_compose_and_releasing_one_does_not_clear_other() {
        store.establishSuspension(suspension(
                "suspend-request-1",
                "suspension-1",
                "security-authority"
        ));
        store.establishSuspension(suspension(
                "suspend-request-2",
                "suspension-2",
                "legal-authority"
        ));
        assertEquals(2, store.effectiveSuspensions(MERCHANT_A).size());

        store.releaseSuspension(new MerchantAccountSuspensionReleaseCommand(
                "release-request-1",
                MERCHANT_A,
                "suspension-1",
                "security-authority",
                T0.plusSeconds(30),
                "security-principal-2",
                "release-evidence-1"
        ));

        assertEquals(1, store.effectiveSuspensions(MERCHANT_A).size());
        assertEquals(
                "suspension-2",
                store.effectiveSuspensions(MERCHANT_A).getFirst().suspensionIdentity()
        );
    }

    @Test
    void suspension_can_only_be_released_by_its_declared_release_authority() {
        store.establishSuspension(suspension(
                "suspend-request-1",
                "suspension-1",
                "security-authority"
        ));

        assertThrows(
                SecurityException.class,
                () -> store.releaseSuspension(
                        new MerchantAccountSuspensionReleaseCommand(
                                "release-request-1",
                                MERCHANT_A,
                                "suspension-1",
                                "merchant-controller",
                                T0.plusSeconds(30),
                                "identity-a",
                                "release-evidence-1"
                        )
                )
        );
        assertEquals(1, store.effectiveSuspensions(MERCHANT_A).size());
    }

    @Test
    void begin_closure_moves_open_to_closing_without_ending_controller() {
        BeginMerchantAccountClosureCommand command = new BeginMerchantAccountClosureCommand(
                "closure-begin-1",
                MERCHANT_A,
                "controller-rel-a",
                "identity-a",
                "strong-auth-evidence-1",
                T0
        );

        assertEquals(MerchantAccountLifecycle.CLOSING, store.beginClosure(command));
        assertEquals(MerchantAccountLifecycle.CLOSING, store.beginClosure(command));
        assertEquals(MerchantAccountLifecycle.CLOSING, store.lifecycle(MERCHANT_A));
        assertTrue(store.activeController(MERCHANT_A).isPresent());
    }

    @Test
    void ordinary_self_service_closure_is_blocked_while_suspension_is_effective() {
        store.establishSuspension(suspension(
                "suspend-request-1",
                "suspension-1",
                "security-authority"
        ));

        assertThrows(
                MerchantAccountSuspendedException.class,
                () -> store.beginClosure(new BeginMerchantAccountClosureCommand(
                        "closure-begin-1",
                        MERCHANT_A,
                        "controller-rel-a",
                        "identity-a",
                        "strong-auth-evidence-1",
                        T0
                ))
        );
        assertEquals(MerchantAccountLifecycle.OPEN, store.lifecycle(MERCHANT_A));
    }

    @Test
    void final_closure_atomically_closes_account_and_ends_controller() {
        store.beginClosure(new BeginMerchantAccountClosureCommand(
                "closure-begin-1",
                MERCHANT_A,
                "controller-rel-a",
                "identity-a",
                "strong-auth-evidence-1",
                T0
        ));
        FinalizeMerchantAccountClosureCommand command =
                new FinalizeMerchantAccountClosureCommand(
                        "closure-final-1",
                        MERCHANT_A,
                        "controller-rel-a",
                        "closure-ready-evidence-1",
                        "merchant-closure-authority",
                        T0.plusSeconds(60)
                );

        assertEquals(MerchantAccountLifecycle.CLOSED, store.finalizeClosure(command));
        assertEquals(MerchantAccountLifecycle.CLOSED, store.finalizeClosure(command));
        assertEquals(MerchantAccountLifecycle.CLOSED, store.lifecycle(MERCHANT_A));
        assertTrue(store.activeController(MERCHANT_A).isEmpty());
        assertEquals(0, activeControllerCount(MERCHANT_A));
        assertEquals(
                MerchantControllerRelationshipLifecycle.ENDED.name(),
                controllerLifecycle("controller-rel-a")
        );
    }

    @Test
    void final_closure_requires_closing_and_closed_account_cannot_receive_transfer() {
        assertThrows(
                MerchantAccountLifecycleConflictException.class,
                () -> store.finalizeClosure(new FinalizeMerchantAccountClosureCommand(
                        "closure-final-1",
                        MERCHANT_A,
                        "controller-rel-a",
                        "closure-ready-evidence-1",
                        "merchant-closure-authority",
                        T0.plusSeconds(60)
                ))
        );

        store.beginClosure(new BeginMerchantAccountClosureCommand(
                "closure-begin-1",
                MERCHANT_A,
                "controller-rel-a",
                "identity-a",
                "strong-auth-evidence-1",
                T0
        ));
        store.finalizeClosure(new FinalizeMerchantAccountClosureCommand(
                "closure-final-2",
                MERCHANT_A,
                "controller-rel-a",
                "closure-ready-evidence-1",
                "merchant-closure-authority",
                T0.plusSeconds(60)
        ));

        assertThrows(
                MerchantControllerTransferConflictException.class,
                () -> store.transferController(transfer(
                        "transfer-after-close",
                        MERCHANT_A,
                        "controller-rel-a",
                        "identity-a",
                        "identity-new",
                        "controller-rel-new"
                ))
        );
    }

    @Test
    void lifecycle_and_suspension_facts_remain_merchant_scoped() {
        store.establishSuspension(suspension(
                "suspend-request-1",
                "suspension-1",
                "security-authority"
        ));

        assertEquals(1, store.effectiveSuspensions(MERCHANT_A).size());
        assertEquals(0, store.effectiveSuspensions(MERCHANT_B).size());
        assertEquals(MerchantAccountLifecycle.OPEN, store.lifecycle(MERCHANT_B));
        assertEquals("identity-b", store.activeController(MERCHANT_B)
                .orElseThrow().identityIdentifier());
    }

    private boolean transferAttempt(
            MerchantControllerTransferCommand command,
            CountDownLatch ready,
            CountDownLatch start
    ) throws Exception {
        ready.countDown();
        start.await();
        try {
            store.transferController(command);
            return true;
        } catch (MerchantControllerTransferConflictException expected) {
            return false;
        }
    }

    private MerchantControllerTransferCommand transfer(
            String request,
            MerchantScope merchant,
            String expectedRelationship,
            String initiatingIdentity,
            String receivingIdentity,
            String replacementRelationship
    ) {
        return new MerchantControllerTransferCommand(
                request,
                merchant,
                expectedRelationship,
                initiatingIdentity,
                receivingIdentity,
                replacementRelationship,
                "strong-auth-evidence-1",
                "recipient-acceptance-evidence-1",
                T0
        );
    }

    private MerchantAccountSuspensionCommand suspension(
            String request,
            String suspensionIdentity,
            String sourceAuthority
    ) {
        return new MerchantAccountSuspensionCommand(
                request,
                MERCHANT_A,
                suspensionIdentity,
                sourceAuthority,
                "merchant-wide-security-condition",
                T0,
                sourceAuthority + "-principal",
                sourceAuthority,
                sourceAuthority + "-case-1"
        );
    }

    private void prepareMerchant(
            MerchantScope merchant,
            String controllerRelationship,
            String controllerIdentity
    ) {
        dsl.insertInto(DSL.table(DSL.name("merchant_account")))
                .columns(DSL.field(DSL.name("merchant_identifier")))
                .values(merchant.merchantIdentifier())
                .execute();
        dsl.insertInto(DSL.table(DSL.name("merchant_controller_relationship")))
                .columns(
                        DSL.field(DSL.name("controller_relationship_identifier")),
                        DSL.field(DSL.name("merchant_identifier")),
                        DSL.field(DSL.name("identity_identifier")),
                        DSL.field(DSL.name("lifecycle"))
                )
                .values(
                        controllerRelationship,
                        merchant.merchantIdentifier(),
                        controllerIdentity,
                        "ACTIVE"
                )
                .execute();
    }

    private void clearMerchant(MerchantScope merchant) {
        String id = merchant.merchantIdentifier();
        deleteForMerchant("merchant_account_closure_finalize_request", id);
        deleteForMerchant("merchant_account_closure_begin_request", id);
        deleteForMerchant("merchant_account_suspension_release_request", id);
        deleteForMerchant("merchant_account_suspension_request", id);
        deleteForMerchant("merchant_account_suspension", id);
        deleteForMerchant("merchant_controller_transfer_request", id);
        deleteForMerchant("merchant_account_establishment_request", id);
        deleteForMerchant("merchant_controller_relationship", id);
        deleteForMerchant("merchant_account", id);
    }

    private void deleteForMerchant(String table, String merchantIdentifier) {
        dsl.deleteFrom(DSL.table(DSL.name(table)))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .eq(merchantIdentifier))
                .execute();
    }

    private int activeControllerCount(MerchantScope merchant) {
        return dsl.fetchCount(
                DSL.table(DSL.name("merchant_controller_relationship")),
                DSL.field(DSL.name("merchant_identifier"), String.class)
                        .eq(merchant.merchantIdentifier())
                        .and(DSL.field(DSL.name("lifecycle"), String.class)
                                .eq("ACTIVE"))
        );
    }

    private String controllerLifecycle(String relationshipIdentifier) {
        return dsl.select(DSL.field(DSL.name("lifecycle"), String.class))
                .from(DSL.table(DSL.name("merchant_controller_relationship")))
                .where(DSL.field(
                        DSL.name("controller_relationship_identifier"),
                        String.class
                ).eq(relationshipIdentifier))
                .fetchOne(0, String.class);
    }

    private int countForMerchant(String table, MerchantScope merchant) {
        return dsl.fetchCount(
                DSL.table(DSL.name(table)),
                DSL.field(DSL.name("merchant_identifier"), String.class)
                        .eq(merchant.merchantIdentifier())
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
