package grandrue.infrastructure.persistence.booking;

import grandrue.application.MerchantScope;
import grandrue.booking.Booking;
import grandrue.booking.BookingConfirmation;
import grandrue.booking.BookingOutbox;
import grandrue.booking.BookingPersistenceException;
import grandrue.booking.BookingReservationWindow;
import grandrue.booking.BookingTransaction;
import grandrue.booking.BookingUnitOfWork;
import grandrue.booking.CommandIdentityConflictException;
import grandrue.booking.ConfirmBookingCommand;
import grandrue.semantic.AllocationClaim;
import grandrue.semantic.AllocationScope;
import grandrue.semantic.DomainEvent;
import grandrue.semantic.TimeWindowAllocationScope;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/** PostgreSQL/jOOQ consistency boundary for standalone Booking confirmation. */
public final class JooqBookingUnitOfWork
        implements BookingUnitOfWork, BookingOutbox {

    private static final Table<?> BOOKING = DSL.table(DSL.name("booking_booking"));
    private static final Table<?> ALLOCATION =
            DSL.table(DSL.name("booking_allocation_claim"));
    private static final Table<?> OUTBOX = DSL.table(DSL.name("booking_outbox"));
    private static final Table<?> HANDLED =
            DSL.table(DSL.name("booking_handled_command"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> COMMAND_IDENTIFIER =
            DSL.field(DSL.name("command_identifier"), String.class);
    private static final Field<String> BOOKING_IDENTIFIER =
            DSL.field(DSL.name("booking_identifier"), String.class);
    private static final Field<String> CUSTOMER_CONTEXT_IDENTIFIER =
            DSL.field(DSL.name("customer_context_identifier"), String.class);
    private static final Field<String> BOOKED_SUBJECT_REFERENCE =
            DSL.field(DSL.name("booked_subject_reference"), String.class);
    private static final Field<String> ALLOCATION_SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("allocation_subject_identifier"), String.class);
    private static final Field<Instant> RESERVATION_STARTS_AT =
            DSL.field(DSL.name("reservation_starts_at"), Instant.class);
    private static final Field<Instant> RESERVATION_ENDS_AT =
            DSL.field(DSL.name("reservation_ends_at"), Instant.class);
    private static final Field<Instant> CONFIRMED_AT =
            DSL.field(DSL.name("confirmed_at"), Instant.class);
    private static final Field<String> ALLOCATION_IDENTIFIER =
            DSL.field(DSL.name("allocation_identifier"), String.class);
    private static final Field<String> EVENT_IDENTIFIER =
            DSL.field(DSL.name("event_identifier"), String.class);
    private static final Field<String> CLAIM_IDENTIFIER =
            DSL.field(DSL.name("claim_identifier"), String.class);
    private static final Field<String> CAPACITY_SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("capacity_subject_identifier"), String.class);
    private static final Field<Instant> CAPACITY_STARTS_AT =
            DSL.field(DSL.name("capacity_starts_at"), Instant.class);
    private static final Field<Instant> CAPACITY_ENDS_AT =
            DSL.field(DSL.name("capacity_ends_at"), Instant.class);
    private static final Field<String> USE_IDENTIFIER =
            DSL.field(DSL.name("use_identifier"), String.class);
    private static final Field<Instant> CLAIMED_AT =
            DSL.field(DSL.name("claimed_at"), Instant.class);
    private static final Field<String> GOVERNING_RELEASE_IDENTIFIER =
            DSL.field(DSL.name("governing_release_identifier"), String.class);
    private static final Field<String> FACT_IDENTIFIER =
            DSL.field(DSL.name("fact_identifier"), String.class);
    private static final Field<String> SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("subject_identifier"), String.class);
    private static final Field<String> CAUSATION_IDENTIFIER =
            DSL.field(DSL.name("causation_identifier"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);
    private static final Field<Instant> PUBLISHED_AT =
            DSL.field(DSL.name("published_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final Clock clock;

    public JooqBookingUnitOfWork(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Clock clock
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public JooqBookingUnitOfWork(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this(dsl, transactionManager, Clock.systemUTC());
    }

    @Override
    public BookingConfirmation execute(
            ConfirmBookingCommand command,
            Function<BookingTransaction, BookingConfirmation> work
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(work, "work");
        try {
            BookingConfirmation confirmation = transactionTemplate.execute(status -> {
                lockLogicalCommand(command.merchantScope(), command.identifier());
                Optional<HandledCommand> handled = handledCommand(
                        command.merchantScope(),
                        command.identifier()
                );
                if (handled.isPresent()) {
                    HandledCommand existing = handled.orElseThrow();
                    requireSameCommand(existing.command(), command);
                    return loadConfirmation(existing);
                }
                JooqBookingTransaction transaction = new JooqBookingTransaction(
                        dsl,
                        command.merchantScope()
                );
                BookingConfirmation produced = Objects.requireNonNull(
                        work.apply(transaction),
                        "Booking work returned no confirmation"
                );
                transaction.recordHandled(command, produced);
                return produced;
            });
            return Objects.requireNonNull(
                    confirmation,
                    "Booking transaction returned no confirmation"
            );
        } catch (CommandIdentityConflictException conflict) {
            throw conflict;
        } catch (RuntimeException failure) {
            Optional<BookingConfirmation> replayed = replayCommitted(command, failure);
            if (replayed.isPresent()) {
                return replayed.orElseThrow();
            }
            throw failure;
        }
    }

    public Optional<Booking> booking(
            MerchantScope merchantScope,
            String identifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Booking identifier");
        return bookingInternal(merchantScope, identifier);
    }

    public Optional<AllocationClaim> conflictingClaim(
            MerchantScope merchantScope,
            AllocationScope scope
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        TimeWindowAllocationScope timeWindow = requireTimeWindowScope(scope);
        Record record = dsl.select(
                        CLAIM_IDENTIFIER,
                        CAPACITY_SUBJECT_IDENTIFIER,
                        CAPACITY_STARTS_AT,
                        CAPACITY_ENDS_AT,
                        USE_IDENTIFIER,
                        CLAIMED_AT
                )
                .from(ALLOCATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(CAPACITY_SUBJECT_IDENTIFIER.eq(timeWindow.subjectIdentifier()))
                .and(CAPACITY_STARTS_AT.lt(timeWindow.endsAt()))
                .and(CAPACITY_ENDS_AT.gt(timeWindow.startsAt()))
                .orderBy(CLAIMED_AT, CLAIM_IDENTIFIER)
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(record).map(this::allocationClaim);
    }

    public Optional<DomainEvent> pendingEvent(
            MerchantScope merchantScope,
            String identifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Event identifier");
        Record record = dsl.select(
                        EVENT_IDENTIFIER,
                        FACT_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        CAUSATION_IDENTIFIER,
                        OCCURRED_AT
                )
                .from(OUTBOX)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(EVENT_IDENTIFIER.eq(identifier))
                .and(PUBLISHED_AT.isNull())
                .fetchOne();
        return Optional.ofNullable(record).map(this::event);
    }

    @Override
    public List<DomainEvent> pendingEvents(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        return dsl.select(
                        EVENT_IDENTIFIER,
                        FACT_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        CAUSATION_IDENTIFIER,
                        OCCURRED_AT
                )
                .from(OUTBOX)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(PUBLISHED_AT.isNull())
                .orderBy(OCCURRED_AT, EVENT_IDENTIFIER)
                .fetch(this::event);
    }

    @Override
    public void recordPublished(
            MerchantScope merchantScope,
            String eventIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(eventIdentifier, "Event identifier");
        dsl.update(OUTBOX)
                .set(PUBLISHED_AT, clock.instant())
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(EVENT_IDENTIFIER.eq(eventIdentifier))
                .and(PUBLISHED_AT.isNull())
                .execute();
    }

    private Optional<BookingConfirmation> replayCommitted(
            ConfirmBookingCommand command,
            RuntimeException originalFailure
    ) {
        try {
            Optional<HandledCommand> handled = handledCommand(
                    command.merchantScope(),
                    command.identifier()
            );
            if (handled.isEmpty()) {
                return Optional.empty();
            }
            HandledCommand existing = handled.orElseThrow();
            requireSameCommand(existing.command(), command);
            return Optional.of(loadConfirmation(existing));
        } catch (CommandIdentityConflictException conflict) {
            throw conflict;
        } catch (RuntimeException replayFailure) {
            originalFailure.addSuppressed(replayFailure);
            return Optional.empty();
        }
    }

    private Optional<HandledCommand> handledCommand(
            MerchantScope merchantScope,
            String commandIdentifier
    ) {
        Record record = dsl.select(
                        BOOKING_IDENTIFIER,
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        BOOKED_SUBJECT_REFERENCE,
                        ALLOCATION_SUBJECT_IDENTIFIER,
                        RESERVATION_STARTS_AT,
                        RESERVATION_ENDS_AT,
                        ALLOCATION_IDENTIFIER,
                        EVENT_IDENTIFIER
                )
                .from(HANDLED)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(COMMAND_IDENTIFIER.eq(commandIdentifier))
                .fetchOne();
        if (record == null) {
            return Optional.empty();
        }
        BookingReservationWindow reservation = new BookingReservationWindow(
                record.get(RESERVATION_STARTS_AT),
                record.get(RESERVATION_ENDS_AT)
        );
        ConfirmBookingCommand command = new ConfirmBookingCommand(
                merchantScope,
                commandIdentifier,
                record.get(BOOKING_IDENTIFIER),
                record.get(CUSTOMER_CONTEXT_IDENTIFIER),
                record.get(BOOKED_SUBJECT_REFERENCE),
                reservation,
                new TimeWindowAllocationScope(
                        record.get(ALLOCATION_SUBJECT_IDENTIFIER),
                        reservation.startsAt(),
                        reservation.endsAt()
                )
        );
        return Optional.of(new HandledCommand(
                command,
                record.get(ALLOCATION_IDENTIFIER),
                record.get(EVENT_IDENTIFIER)
        ));
    }

    private BookingConfirmation loadConfirmation(HandledCommand handled) {
        MerchantScope scope = handled.command().merchantScope();
        Booking booking = bookingInternal(
                scope,
                handled.command().bookingIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled command has no Booking"
        ));
        AllocationClaim claim = allocationClaimInternal(
                scope,
                handled.allocationIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled command has no Allocation"
        ));
        DomainEvent event = outboxEvent(
                scope,
                handled.eventIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled command has no event"
        ));
        return new BookingConfirmation(booking, claim, event);
    }

    private Optional<Booking> bookingInternal(
            MerchantScope merchantScope,
            String identifier
    ) {
        Record record = dsl.select(
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        BOOKED_SUBJECT_REFERENCE,
                        RESERVATION_STARTS_AT,
                        RESERVATION_ENDS_AT,
                        GOVERNING_RELEASE_IDENTIFIER,
                        CONFIRMED_AT
                )
                .from(BOOKING)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(BOOKING_IDENTIFIER.eq(identifier))
                .fetchOne();
        return Optional.ofNullable(record).map(row -> new Booking(
                merchantScope,
                identifier,
                row.get(CUSTOMER_CONTEXT_IDENTIFIER),
                row.get(BOOKED_SUBJECT_REFERENCE),
                new BookingReservationWindow(
                        row.get(RESERVATION_STARTS_AT),
                        row.get(RESERVATION_ENDS_AT)
                ),
                row.get(GOVERNING_RELEASE_IDENTIFIER),
                row.get(CONFIRMED_AT)
        ));
    }

    private Optional<AllocationClaim> allocationClaimInternal(
            MerchantScope merchantScope,
            String identifier
    ) {
        Record record = dsl.select(
                        CLAIM_IDENTIFIER,
                        CAPACITY_SUBJECT_IDENTIFIER,
                        CAPACITY_STARTS_AT,
                        CAPACITY_ENDS_AT,
                        USE_IDENTIFIER,
                        CLAIMED_AT
                )
                .from(ALLOCATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(CLAIM_IDENTIFIER.eq(identifier))
                .fetchOne();
        return Optional.ofNullable(record).map(this::allocationClaim);
    }

    private Optional<DomainEvent> outboxEvent(
            MerchantScope merchantScope,
            String identifier
    ) {
        Record record = dsl.select(
                        EVENT_IDENTIFIER,
                        FACT_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        CAUSATION_IDENTIFIER,
                        OCCURRED_AT
                )
                .from(OUTBOX)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(EVENT_IDENTIFIER.eq(identifier))
                .fetchOne();
        return Optional.ofNullable(record).map(this::event);
    }

    private AllocationClaim allocationClaim(Record record) {
        return new AllocationClaim(
                record.get(CLAIM_IDENTIFIER),
                new TimeWindowAllocationScope(
                        record.get(CAPACITY_SUBJECT_IDENTIFIER),
                        record.get(CAPACITY_STARTS_AT),
                        record.get(CAPACITY_ENDS_AT)
                ),
                record.get(USE_IDENTIFIER),
                record.get(CLAIMED_AT)
        );
    }

    private DomainEvent event(Record record) {
        return new DomainEvent(
                record.get(EVENT_IDENTIFIER),
                record.get(FACT_IDENTIFIER),
                record.get(SUBJECT_IDENTIFIER),
                record.get(CAUSATION_IDENTIFIER),
                record.get(OCCURRED_AT)
        );
    }

    private void lockLogicalCommand(
            MerchantScope merchantScope,
            String commandIdentifier
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                merchantScope.merchantIdentifier()
                        + "|booking-command|"
                        + commandIdentifier
        );
    }

    private static void requireSameCommand(
            ConfirmBookingCommand existing,
            ConfirmBookingCommand requested
    ) {
        if (!existing.equals(requested)) {
            throw new CommandIdentityConflictException(requested.identifier());
        }
    }

    private static TimeWindowAllocationScope requireTimeWindowScope(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope, "scope");
        if (!(scope instanceof TimeWindowAllocationScope timeWindow)) {
            throw new IllegalArgumentException(
                    "Booking unit of work requires a time-window Allocation"
            );
        }
        return timeWindow;
    }

    private static void requireIdentifier(String identifier, String label) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private record HandledCommand(
            ConfirmBookingCommand command,
            String allocationIdentifier,
            String eventIdentifier
    ) {
    }
}
