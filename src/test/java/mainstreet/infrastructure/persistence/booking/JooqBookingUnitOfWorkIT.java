package mainstreet.booking;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.infrastructure.persistence.booking.JooqBookingUnitOfWork;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.AllocationConflictException;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.TimeWindowAllocationScope;
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
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqBookingUnitOfWorkIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant CHECK_IN = Instant.parse("2026-08-20T14:00:00Z");
    private static final Instant CHECK_OUT = Instant.parse("2026-08-22T10:00:00Z");
    private static final Instant RECORDED_AT = Instant.parse("2026-08-20T10:16:00Z");
    private static final Instant PUBLISHED_AT = Instant.parse("2026-08-20T10:20:00Z");

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
        dsl.execute("truncate table booking_handled_command, booking_outbox, booking_allocation_claim, booking_booking cascade");
    }

    @Test
    void complete_confirmation_survives_adapter_recreation_and_replays_without_duplication() {
        JooqBookingUnitOfWork first = adapter();
        ConfirmBookingCommand command = command(MERCHANT_A);
        BookingConfirmation original = bookingService(first).confirm(command);

        JooqBookingUnitOfWork restarted = adapter();

        assertEquals(original.booking(), restarted.booking(
                MERCHANT_A,
                command.bookingIdentifier()
        ).orElseThrow());
        assertEquals(original.allocationClaim(), restarted.conflictingClaim(
                MERCHANT_A,
                command.allocationScope()
        ).orElseThrow());
        assertEquals(original.pendingEvent(), restarted.pendingEvent(
                MERCHANT_A,
                original.pendingEvent().identifier()
        ).orElseThrow());
        assertEquals(RECORDED_AT, original.booking().confirmedAt());
        assertEquals(RECORDED_AT, original.allocationClaim().claimedAt());
        assertEquals(RECORDED_AT, original.pendingEvent().occurredAt());
        assertEquals(original, bookingService(restarted).confirm(command));
        assertEquals(1, count("booking_booking"));
        assertEquals(1, count("booking_allocation_claim"));
        assertEquals(1, count("booking_outbox"));
        assertEquals(1, count("booking_handled_command"));
    }

    @Test
    void identical_authority_records_are_isolated_by_merchant() {
        JooqBookingUnitOfWork unitOfWork = adapter();
        ConfirmBookingCommand firstCommand = command(MERCHANT_A);
        ConfirmBookingCommand secondCommand = new ConfirmBookingCommand(
                MERCHANT_B,
                firstCommand.identifier(),
                firstCommand.bookingIdentifier(),
                firstCommand.customerContextIdentifier(),
                firstCommand.bookedSubjectReference(),
                firstCommand.reservationWindow(),
                firstCommand.allocationScope()
        );

        BookingConfirmation first = bookingService(unitOfWork).confirm(firstCommand);
        BookingConfirmation second = bookingService(unitOfWork).confirm(secondCommand);

        unitOfWork.recordPublished(MERCHANT_A, first.pendingEvent().identifier());

        assertEquals(first.booking(), unitOfWork.booking(
                MERCHANT_A,
                firstCommand.bookingIdentifier()
        ).orElseThrow());
        assertEquals(second.booking(), unitOfWork.booking(
                MERCHANT_B,
                secondCommand.bookingIdentifier()
        ).orElseThrow());
        assertTrue(unitOfWork.pendingEvents(MERCHANT_A).isEmpty());
        assertEquals(List.of(second.pendingEvent()), unitOfWork.pendingEvents(MERCHANT_B));
    }

    @Test
    void concurrent_duplicate_commands_converge_on_one_committed_result() throws Exception {
        ConfirmBookingCommand command = command(MERCHANT_A);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<BookingConfirmation> first = executor.submit(() -> {
                start.await();
                return bookingService(newAdapter()).confirm(command);
            });
            Future<BookingConfirmation> duplicate = executor.submit(() -> {
                start.await();
                return bookingService(newAdapter()).confirm(command);
            });

            start.countDown();
            BookingConfirmation firstResult = result(first);
            BookingConfirmation duplicateResult = result(duplicate);

            assertEquals(firstResult, duplicateResult);
            assertEquals(1, count("booking_booking"));
            assertEquals(1, count("booking_handled_command"));
            assertEquals(1, adapter().pendingEvents(MERCHANT_A).size());
        }
    }

    @Test
    void concurrent_overlapping_capacity_commands_commit_exactly_one_booking() throws Exception {
        TimeWindowAllocationScope capacity = capacity();
        ConfirmBookingCommand firstCommand = command(
                MERCHANT_A,
                "command-001",
                "booking-123",
                capacity
        );
        ConfirmBookingCommand conflictingCommand = command(
                MERCHANT_A,
                "command-002",
                "booking-456",
                capacity
        );
        CyclicBarrier barrier = new CyclicBarrier(2);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Attempt> first = executor.submit(() -> attempt(firstCommand, barrier));
            Future<Attempt> conflicting = executor.submit(() -> attempt(conflictingCommand, barrier));

            Attempt firstResult = result(first);
            Attempt conflictingResult = result(conflicting);
            assertEquals(1, List.of(firstResult, conflictingResult).stream()
                    .filter(Attempt::succeeded).count());
            assertEquals(1, List.of(firstResult, conflictingResult).stream()
                    .filter(attempt -> !attempt.succeeded()).count());
            assertInstanceOf(
                    AllocationConflictException.class,
                    firstResult.failure() == null
                            ? conflictingResult.failure()
                            : firstResult.failure()
            );
            assertEquals(1, count("booking_booking"));
            assertEquals(1, count("booking_allocation_claim"));
            assertEquals(1, count("booking_outbox"));
        }
    }

    @Test
    void failed_transaction_leaves_no_partial_booking_facts() {
        JooqBookingUnitOfWork unitOfWork = adapter();
        ConfirmBookingCommand command = command(MERCHANT_A);
        Booking booking = booking(command);
        DomainEvent event = event(command);

        assertThrows(DeliberateFailure.class, () -> unitOfWork.execute(command, transaction -> {
            AllocationClaim claim = transaction.claim(
                    command.identifier() + ":allocation",
                    command.allocationScope(),
                    command.bookingIdentifier(),
                    RECORDED_AT
            );
            transaction.recordBooking(booking);
            transaction.appendPendingEvent(event);
            throw new DeliberateFailure();
        }));

        assertEquals(0, count("booking_booking"));
        assertEquals(0, count("booking_allocation_claim"));
        assertEquals(0, count("booking_outbox"));
        assertEquals(0, count("booking_handled_command"));
    }

    @Test
    void reused_command_identity_with_different_intent_is_rejected() {
        bookingService(adapter()).confirm(command(MERCHANT_A));
        ConfirmBookingCommand reused = command(
                MERCHANT_A,
                "command-001",
                "booking-456",
                new TimeWindowAllocationScope(
                        "room-capacity-002",
                        CHECK_IN,
                        CHECK_OUT
                )
        );

        assertThrows(
                CommandIdentityConflictException.class,
                () -> bookingService(adapter()).confirm(reused)
        );
        assertEquals(1, count("booking_booking"));
    }

    @Test
    void technical_publication_evidence_survives_adapter_recreation_without_rewriting_booking_truth() {
        JooqBookingUnitOfWork first = adapter();
        ConfirmBookingCommand command = command(MERCHANT_A);
        BookingConfirmation confirmation = bookingService(first).confirm(command);

        first.recordPublished(MERCHANT_A, confirmation.pendingEvent().identifier());

        JooqBookingUnitOfWork restarted = adapter();
        assertTrue(restarted.pendingEvents(MERCHANT_A).isEmpty());
        assertEquals(
                PUBLISHED_AT,
                dsl.select(DSL.field(DSL.name("published_at"), Instant.class))
                        .from(DSL.table(DSL.name("booking_outbox")))
                        .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                                .eq(MERCHANT_A.merchantIdentifier()))
                        .and(DSL.field(DSL.name("event_identifier"), String.class)
                                .eq(confirmation.pendingEvent().identifier()))
                        .fetchOne(0, Instant.class)
        );
        assertEquals(confirmation, bookingService(restarted).confirm(command));
        assertEquals(1, count("booking_booking"));
        assertEquals(1, count("booking_outbox"));
    }

    @Test
    void booking_outbox_schema_records_publication_not_global_consumer_acknowledgement() {
        assertTrue(columnExists("booking_outbox", "published_at"));
        assertFalse(columnExists("booking_outbox", "acknowledged_at"));
    }

    @Test
    void handled_command_cannot_commit_without_authoritative_booking_facts() {
        ConfirmBookingCommand command = command(MERCHANT_A);

        assertThrows(BookingPersistenceException.class, () -> adapter().execute(command, transaction -> {
            Booking booking = booking(command);
            AllocationClaim unrecordedClaim = new AllocationClaim(
                    command.identifier() + ":allocation",
                    command.allocationScope(),
                    command.bookingIdentifier(),
                    RECORDED_AT
            );
            return new BookingConfirmation(
                    booking,
                    unrecordedClaim,
                    event(command)
            );
        }));

        assertEquals(0, count("booking_handled_command"));
        BookingConfirmation recovered = bookingService(adapter()).confirm(command);
        assertEquals(command.bookingIdentifier(), recovered.booking().identifier());
    }

    private Attempt attempt(ConfirmBookingCommand command, CyclicBarrier barrier) {
        try {
            return Attempt.success(executeConfirmation(newAdapter(), command, barrier));
        } catch (RuntimeException failure) {
            return Attempt.failure(failure);
        }
    }

    private BookingConfirmation executeConfirmation(
            JooqBookingUnitOfWork unitOfWork,
            ConfirmBookingCommand command,
            CyclicBarrier barrier
    ) {
        return unitOfWork.execute(command, transaction -> {
            await(barrier);
            Booking booking = booking(command);
            AllocationClaim claim = transaction.claim(
                    command.identifier() + ":allocation",
                    command.allocationScope(),
                    command.bookingIdentifier(),
                    RECORDED_AT
            );
            DomainEvent event = event(command);
            transaction.recordBooking(booking);
            transaction.appendPendingEvent(event);
            return new BookingConfirmation(booking, claim, event);
        });
    }

    private JooqBookingUnitOfWork adapter() {
        return new JooqBookingUnitOfWork(
                dsl,
                transactionManager,
                Clock.fixed(PUBLISHED_AT, ZoneOffset.UTC)
        );
    }

    private JooqBookingUnitOfWork newAdapter() {
        DSLContext independentDsl = DSL.using(
                new TransactionAwareDataSourceProxy(authoritativeDataSource),
                SQLDialect.POSTGRES
        );
        return new JooqBookingUnitOfWork(
                independentDsl,
                new DataSourceTransactionManager(authoritativeDataSource),
                Clock.fixed(PUBLISHED_AT, ZoneOffset.UTC)
        );
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private boolean columnExists(String table, String column) {
        return dsl.fetchExists(
                DSL.selectOne()
                        .from(DSL.table(DSL.name("information_schema", "columns")))
                        .where(DSL.field(DSL.name("table_name"), String.class).eq(table))
                        .and(DSL.field(DSL.name("column_name"), String.class).eq(column))
        );
    }

    private static BookingApplicationService bookingService(JooqBookingUnitOfWork unitOfWork) {
        InMemoryCustomerContextAuthority customerContexts = new InMemoryCustomerContextAuthority();
        customerContexts.register(new CustomerContext(MERCHANT_A, "customer-123", RECORDED_AT));
        customerContexts.register(new CustomerContext(MERCHANT_B, "customer-123", RECORDED_AT));
        return new BookingApplicationService(
                unitOfWork,
                customerContexts,
                (merchantScope, principal, operation) -> {
                    throw new AssertionError("Persistence seam must not invoke runtime authority");
                },
                Clock.fixed(RECORDED_AT, ZoneOffset.UTC)
        );
    }

    private static Booking booking(ConfirmBookingCommand command) {
        return new Booking(
                command.merchantScope(),
                command.bookingIdentifier(),
                command.customerContextIdentifier(),
                command.bookedSubjectReference(),
                command.reservationWindow(),
                "test-only:unpublished-release",
                RECORDED_AT
        );
    }

    private static ConfirmBookingCommand command(MerchantScope merchantScope) {
        return command(merchantScope, "command-001", "booking-123", capacity());
    }

    private static ConfirmBookingCommand command(
            MerchantScope merchantScope,
            String identifier,
            String bookingIdentifier,
            TimeWindowAllocationScope capacity
    ) {
        return new ConfirmBookingCommand(
                merchantScope,
                identifier,
                bookingIdentifier,
                "customer-123",
                "standard-room",
                new BookingReservationWindow(CHECK_IN, CHECK_OUT),
                capacity
        );
    }

    private static TimeWindowAllocationScope capacity() {
        return new TimeWindowAllocationScope(
                "room-capacity-001",
                CHECK_IN,
                CHECK_OUT
        );
    }

    private static DomainEvent event(ConfirmBookingCommand command) {
        return new DomainEvent(
                command.identifier() + ":booking-confirmed",
                "booking.confirmed",
                command.bookingIdentifier(),
                command.identifier(),
                RECORDED_AT
        );
    }

    private static void await(CyclicBarrier barrier) {
        try {
            barrier.await(5, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new IllegalStateException("Concurrent Booking test barrier failed", exception);
        }
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

    private record Attempt(BookingConfirmation confirmation, RuntimeException failure) {
        private static Attempt success(BookingConfirmation confirmation) {
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
    }
}
