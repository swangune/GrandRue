package mainstreet.ordering;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.infrastructure.persistence.ordering.JooqOrderingUnitOfWork;
import mainstreet.inventory.InsufficientQuantityException;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.QuantityAllocationScope;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqOrderingUnitOfWorkIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant COMMITTED_AT = Instant.parse("2026-08-26T18:30:00Z");

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
        dsl.execute("truncate table ordering_handled_claim, ordering_handled_requested_portion, ordering_handled_command, ordering_outbox, ordering_order_portion, ordering_order, inventory_quantity_claim, inventory_stock_position cascade");
    }

    @Test
    void committed_order_inventory_claim_and_event_survive_adapter_recreation() {
        seedStock(MERCHANT_A, "sku-1", 1L);
        JooqOrderingUnitOfWork first = adapter();
        CommitOrderCommand command = command(MERCHANT_A, "command-1", "order-1");

        OrderConfirmation original = service(first).commit(
                command,
                "semantic-release-8"
        );

        JooqOrderingUnitOfWork restarted = adapter();
        assertEquals(original.order(), restarted.order(
                MERCHANT_A,
                "order-1"
        ).orElseThrow());
        assertEquals(original.inventoryClaims(), restarted.inventoryClaims(
                MERCHANT_A,
                "order-1"
        ));
        assertEquals(List.of(original.pendingEvent()), restarted.pendingEvents(MERCHANT_A));
        assertEquals(0L, restarted.availableToPromise(MERCHANT_A, "sku-1"));

        assertEquals(
                original,
                service(restarted).commit(command, "semantic-release-8")
        );
        assertEquals(1, count("ordering_order"));
        assertEquals(1, count("ordering_order_portion"));
        assertEquals(1, count("inventory_quantity_claim"));
        assertEquals(1, count("ordering_outbox"));
        assertEquals(1, count("ordering_handled_command"));
    }

    @Test
    void concurrent_duplicate_commands_converge_on_one_committed_order() throws Exception {
        seedStock(MERCHANT_A, "sku-1", 1L);
        CommitOrderCommand command = command(MERCHANT_A, "command-1", "order-1");
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<OrderConfirmation> first = executor.submit(() -> {
                start.await();
                return service(newAdapter()).commit(command, "semantic-release-8");
            });
            Future<OrderConfirmation> duplicate = executor.submit(() -> {
                start.await();
                return service(newAdapter()).commit(command, "semantic-release-8");
            });

            start.countDown();
            assertEquals(result(first), result(duplicate));
        }

        assertEquals(1, count("ordering_order"));
        assertEquals(1, count("inventory_quantity_claim"));
        assertEquals(1, count("ordering_outbox"));
        assertEquals(1, count("ordering_handled_command"));
    }

    @Test
    void concurrent_competing_orders_for_last_stock_commit_exactly_one() throws Exception {
        seedStock(MERCHANT_A, "sku-1", 1L);
        CommitOrderCommand firstCommand = command(
                MERCHANT_A,
                "command-1",
                "order-1"
        );
        CommitOrderCommand secondCommand = command(
                MERCHANT_A,
                "command-2",
                "order-2"
        );
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Attempt> first = executor.submit(() -> attempt(firstCommand, start));
            Future<Attempt> second = executor.submit(() -> attempt(secondCommand, start));
            start.countDown();

            Attempt firstResult = result(first);
            Attempt secondResult = result(second);
            assertEquals(1, List.of(firstResult, secondResult).stream()
                    .filter(Attempt::succeeded).count());
            RuntimeException failure = firstResult.failure() == null
                    ? secondResult.failure()
                    : firstResult.failure();
            assertInstanceOf(InsufficientQuantityException.class, failure);
        }

        assertEquals(1, count("ordering_order"));
        assertEquals(1, count("ordering_order_portion"));
        assertEquals(1, count("inventory_quantity_claim"));
        assertEquals(1, count("ordering_outbox"));
        assertEquals(0L, adapter().availableToPromise(MERCHANT_A, "sku-1"));
    }

    @Test
    void failed_transaction_leaves_no_partial_order_inventory_or_event() {
        seedStock(MERCHANT_A, "sku-1", 1L);
        JooqOrderingUnitOfWork adapter = adapter();
        CommitOrderCommand command = command(MERCHANT_A, "command-1", "order-1");

        assertThrows(DeliberateFailure.class, () -> adapter.execute(command, transaction -> {
            var claim = transaction.claim(
                    "claim-1",
                    new QuantityAllocationScope("sku-1", 1L),
                    "portion-1",
                    COMMITTED_AT
            );
            Order order = new Order(
                    MERCHANT_A,
                    "order-1",
                    Optional.empty(),
                    List.of(committedPortion("sku-1")),
                    "semantic-release-8",
                    COMMITTED_AT
            );
            transaction.recordOrder(order);
            transaction.appendPendingEvent(new DomainEvent(
                    "command-1:order-committed",
                    "order.committed",
                    "order-1",
                    "command-1",
                    COMMITTED_AT
            ));
            throw new DeliberateFailure(claim.identifier());
        }));

        assertEquals(0, count("ordering_order"));
        assertEquals(0, count("inventory_quantity_claim"));
        assertEquals(0, count("ordering_outbox"));
        assertEquals(0, count("ordering_handled_command"));
        assertEquals(1L, adapter.availableToPromise(MERCHANT_A, "sku-1"));
    }

    @Test
    void reused_command_identity_with_different_intent_is_rejected_after_restart() {
        seedStock(MERCHANT_A, "sku-1", 2L);
        service(adapter()).commit(
                command(MERCHANT_A, "command-1", "order-1"),
                "semantic-release-8"
        );

        CommitOrderCommand reused = command(
                MERCHANT_A,
                "command-1",
                "order-2"
        );

        assertThrows(
                OrderCommandIdentityConflictException.class,
                () -> service(adapter()).commit(reused, "semantic-release-8")
        );
        assertEquals(1, count("ordering_order"));
        assertEquals(1, count("inventory_quantity_claim"));
    }

    @Test
    void identical_identifiers_and_inventory_subjects_are_isolated_by_merchant() {
        seedStock(MERCHANT_A, "sku-1", 1L);
        seedStock(MERCHANT_B, "sku-1", 1L);
        JooqOrderingUnitOfWork adapter = adapter();

        OrderConfirmation first = service(adapter).commit(
                command(MERCHANT_A, "command-1", "order-1"),
                "semantic-release-8"
        );
        OrderConfirmation second = service(adapter).commit(
                command(MERCHANT_B, "command-1", "order-1"),
                "semantic-release-8"
        );

        assertEquals(first.order(), adapter.order(MERCHANT_A, "order-1").orElseThrow());
        assertEquals(second.order(), adapter.order(MERCHANT_B, "order-1").orElseThrow());
        assertEquals(0L, adapter.availableToPromise(MERCHANT_A, "sku-1"));
        assertEquals(0L, adapter.availableToPromise(MERCHANT_B, "sku-1"));
        assertEquals(2, count("ordering_order"));
        assertEquals(2, count("inventory_quantity_claim"));
    }

    @Test
    void non_stock_order_is_durable_without_inventory_rows() {
        JooqOrderingUnitOfWork adapter = adapter();
        OrderingApplicationService service = new OrderingApplicationService(
                adapter,
                customerContexts(),
                (merchantScope, requested) -> new ResolvedOrderCommitment(
                        new OrderCommitmentPortion(
                                requested.identifier(),
                                requested.subjectReference(),
                                requested.quantity(),
                                money(5000),
                                "offering:made-to-order@revision-2"
                        ),
                        List.of()
                ),
                OperationExecutionGuard.allowAll(),
                Clock.fixed(COMMITTED_AT, ZoneOffset.UTC)
        );

        OrderConfirmation confirmation = service.commit(
                command(MERCHANT_A, "command-1", "order-1", "made-to-order"),
                "semantic-release-8"
        );

        assertTrue(confirmation.inventoryClaims().isEmpty());
        assertEquals(
                confirmation.order(),
                adapter().order(MERCHANT_A, "order-1").orElseThrow()
        );
        assertEquals(0, count("inventory_quantity_claim"));
    }

    private Attempt attempt(CommitOrderCommand command, CountDownLatch start) {
        try {
            start.await();
            return Attempt.success(
                    service(newAdapter()).commit(command, "semantic-release-8")
            );
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(interrupted);
        } catch (RuntimeException failure) {
            return Attempt.failure(failure);
        }
    }

    private OrderingApplicationService service(JooqOrderingUnitOfWork unitOfWork) {
        return new OrderingApplicationService(
                unitOfWork,
                customerContexts(),
                JooqOrderingUnitOfWorkIT::resolve,
                OperationExecutionGuard.allowAll(),
                Clock.fixed(COMMITTED_AT, ZoneOffset.UTC)
        );
    }

    private static ResolvedOrderCommitment resolve(
            MerchantScope merchantScope,
            RequestedOrderPortion requested
    ) {
        return new ResolvedOrderCommitment(
                new OrderCommitmentPortion(
                        requested.identifier(),
                        requested.subjectReference(),
                        requested.quantity(),
                        money(2500),
                        "offering:" + requested.subjectReference() + "@revision-3"
                ),
                List.of(new OrderInventoryClaimRequest(
                        "claim:" + requested.identifier(),
                        requested.identifier(),
                        new QuantityAllocationScope(
                                requested.subjectReference(),
                                requested.quantity().magnitude().longValueExact()
                        )
                ))
        );
    }

    private InMemoryCustomerContextAuthority customerContexts() {
        InMemoryCustomerContextAuthority authority = new InMemoryCustomerContextAuthority();
        authority.register(new CustomerContext(MERCHANT_A, "customer-1", COMMITTED_AT));
        authority.register(new CustomerContext(MERCHANT_B, "customer-1", COMMITTED_AT));
        return authority;
    }

    private JooqOrderingUnitOfWork adapter() {
        return new JooqOrderingUnitOfWork(dsl, transactionManager);
    }

    private JooqOrderingUnitOfWork newAdapter() {
        DSLContext independentDsl = DSL.using(
                new TransactionAwareDataSourceProxy(authoritativeDataSource),
                SQLDialect.POSTGRES
        );
        return new JooqOrderingUnitOfWork(
                independentDsl,
                new DataSourceTransactionManager(authoritativeDataSource)
        );
    }

    private void seedStock(
            MerchantScope merchantScope,
            String subjectIdentifier,
            long stockOnHand
    ) {
        dsl.execute(
                "insert into inventory_stock_position (merchant_identifier, subject_identifier, stock_on_hand) values (?, ?, ?)",
                merchantScope.merchantIdentifier(),
                subjectIdentifier,
                stockOnHand
        );
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private static CommitOrderCommand command(
            MerchantScope merchantScope,
            String commandIdentifier,
            String orderIdentifier
    ) {
        return command(merchantScope, commandIdentifier, orderIdentifier, "sku-1");
    }

    private static CommitOrderCommand command(
            MerchantScope merchantScope,
            String commandIdentifier,
            String orderIdentifier,
            String subjectIdentifier
    ) {
        return new CommitOrderCommand(
                merchantScope,
                commandIdentifier,
                orderIdentifier,
                Optional.empty(),
                List.of(new RequestedOrderPortion(
                        "portion-1",
                        subjectIdentifier,
                        new CommittedQuantity(BigDecimal.ONE, "EACH")
                ))
        );
    }

    private static OrderCommitmentPortion committedPortion(String subjectIdentifier) {
        return new OrderCommitmentPortion(
                "portion-1",
                subjectIdentifier,
                new CommittedQuantity(BigDecimal.ONE, "EACH"),
                money(2500),
                "offering:" + subjectIdentifier + "@revision-3"
        );
    }

    private static MonetaryAmount money(long minorUnits) {
        return new MonetaryAmount(
                new CurrencyIdentity("GBP"),
                BigInteger.valueOf(minorUnits)
        );
    }

    private static <T> T result(Future<T> future)
            throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(15, TimeUnit.SECONDS);
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

    private record Attempt(OrderConfirmation confirmation, RuntimeException failure) {
        private static Attempt success(OrderConfirmation confirmation) {
            return new Attempt(confirmation, null);
        }

        private static Attempt failure(RuntimeException failure) {
            return new Attempt(null, failure);
        }

        private boolean succeeded() {
            return confirmation != null;
        }
    }

    private static final class DeliberateFailure extends RuntimeException {
        private DeliberateFailure(String message) {
            super(message);
        }
    }
}
