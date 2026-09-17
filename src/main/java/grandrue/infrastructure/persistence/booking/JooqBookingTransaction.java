package grandrue.infrastructure.persistence.booking;

import mainstreet.application.MerchantScope;
import grandrue.booking.Booking;
import grandrue.booking.BookingConfirmation;
import grandrue.booking.BookingPersistenceException;
import grandrue.booking.BookingTransaction;
import grandrue.booking.ConfirmBookingCommand;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.AllocationConflictException;
import mainstreet.semantic.AllocationScope;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.TimeWindowAllocationScope;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL/jOOQ mutation boundary for standalone Booking confirmation. */
public final class JooqBookingTransaction implements BookingTransaction {

    private static final Table<?> BOOKING = DSL.table(DSL.name("booking_booking"));
    private static final Table<?> ALLOCATION =
            DSL.table(DSL.name("booking_allocation_claim"));
    private static final Table<?> OUTBOX = DSL.table(DSL.name("booking_outbox"));
    private static final Table<?> HANDLED =
            DSL.table(DSL.name("booking_handled_command"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
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
    private static final Field<String> GOVERNING_RELEASE_IDENTIFIER =
            DSL.field(DSL.name("governing_release_identifier"), String.class);
    private static final Field<Instant> CONFIRMED_AT =
            DSL.field(DSL.name("confirmed_at"), Instant.class);
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
    private static final Field<String> EVENT_IDENTIFIER =
            DSL.field(DSL.name("event_identifier"), String.class);
    private static final Field<String> FACT_IDENTIFIER =
            DSL.field(DSL.name("fact_identifier"), String.class);
    private static final Field<String> SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("subject_identifier"), String.class);
    private static final Field<String> CAUSATION_IDENTIFIER =
            DSL.field(DSL.name("causation_identifier"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);
    private static final Field<String> COMMAND_IDENTIFIER =
            DSL.field(DSL.name("command_identifier"), String.class);
    private static final Field<String> ALLOCATION_IDENTIFIER =
            DSL.field(DSL.name("allocation_identifier"), String.class);

    private final DSLContext dsl;
    private final MerchantScope merchantScope;

    public JooqBookingTransaction(DSLContext dsl, MerchantScope merchantScope) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
    }

    @Override
    public MerchantScope merchantScope() {
        return merchantScope;
    }

    @Override
    public AllocationClaim claim(
            String identifier,
            AllocationScope scope,
            String useIdentifier,
            Instant claimedAt
    ) {
        TimeWindowAllocationScope timeWindow = requireTimeWindowScope(scope);
        lockCapacitySubject(timeWindow.subjectIdentifier());
        Optional<AllocationClaim> conflict = conflictingClaim(timeWindow);
        if (conflict.isPresent()) {
            throw new AllocationConflictException(conflict.orElseThrow());
        }
        try {
            dsl.insertInto(ALLOCATION)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            CLAIM_IDENTIFIER,
                            CAPACITY_SUBJECT_IDENTIFIER,
                            CAPACITY_STARTS_AT,
                            CAPACITY_ENDS_AT,
                            USE_IDENTIFIER,
                            CLAIMED_AT
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            identifier,
                            timeWindow.subjectIdentifier(),
                            timeWindow.startsAt(),
                            timeWindow.endsAt(),
                            useIdentifier,
                            claimedAt
                    )
                    .execute();
        } catch (RuntimeException exception) {
            throw persistenceFailure("Could not record Booking allocation", exception);
        }
        return new AllocationClaim(identifier, timeWindow, useIdentifier, claimedAt);
    }

    @Override
    public void recordBooking(Booking booking) {
        Objects.requireNonNull(booking, "booking");
        requireMerchantScope(booking.merchantScope(), "Booking");
        try {
            dsl.insertInto(BOOKING)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            BOOKING_IDENTIFIER,
                            CUSTOMER_CONTEXT_IDENTIFIER,
                            BOOKED_SUBJECT_REFERENCE,
                            RESERVATION_STARTS_AT,
                            RESERVATION_ENDS_AT,
                            GOVERNING_RELEASE_IDENTIFIER,
                            CONFIRMED_AT
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            booking.identifier(),
                            booking.customerContextIdentifier(),
                            booking.bookedSubjectReference(),
                            booking.reservationWindow().startsAt(),
                            booking.reservationWindow().endsAt(),
                            booking.governingReleaseIdentifier(),
                            booking.confirmedAt()
                    )
                    .execute();
        } catch (RuntimeException exception) {
            throw persistenceFailure("Could not record Booking", exception);
        }
    }

    @Override
    public void appendPendingEvent(DomainEvent event) {
        Objects.requireNonNull(event, "event");
        try {
            dsl.insertInto(OUTBOX)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            EVENT_IDENTIFIER,
                            FACT_IDENTIFIER,
                            SUBJECT_IDENTIFIER,
                            CAUSATION_IDENTIFIER,
                            OCCURRED_AT
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            event.identifier(),
                            event.factIdentifier(),
                            event.subjectIdentifier(),
                            event.causationIdentifier(),
                            event.occurredAt()
                    )
                    .execute();
        } catch (RuntimeException exception) {
            throw persistenceFailure("Could not append Booking event", exception);
        }
    }

    public void recordHandled(
            ConfirmBookingCommand command,
            BookingConfirmation confirmation
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(confirmation, "confirmation");
        requireMerchantScope(command.merchantScope(), "Command");
        Booking booking = confirmation.booking();
        TimeWindowAllocationScope allocation = command.allocationScope();
        if (!command.bookingIdentifier().equals(booking.identifier())
                || !command.customerContextIdentifier().equals(
                booking.customerContextIdentifier()
        ) || !command.bookedSubjectReference().equals(
                booking.bookedSubjectReference()
        ) || !command.reservationWindow().equals(booking.reservationWindow())) {
            throw new IllegalArgumentException(
                    "Booking confirmation does not match command"
            );
        }
        try {
            dsl.insertInto(HANDLED)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            COMMAND_IDENTIFIER,
                            BOOKING_IDENTIFIER,
                            CUSTOMER_CONTEXT_IDENTIFIER,
                            BOOKED_SUBJECT_REFERENCE,
                            ALLOCATION_SUBJECT_IDENTIFIER,
                            RESERVATION_STARTS_AT,
                            RESERVATION_ENDS_AT,
                            CONFIRMED_AT,
                            ALLOCATION_IDENTIFIER,
                            EVENT_IDENTIFIER
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            command.identifier(),
                            command.bookingIdentifier(),
                            command.customerContextIdentifier(),
                            command.bookedSubjectReference(),
                            allocation.subjectIdentifier(),
                            command.reservationWindow().startsAt(),
                            command.reservationWindow().endsAt(),
                            booking.confirmedAt(),
                            confirmation.allocationClaim().identifier(),
                            confirmation.pendingEvent().identifier()
                    )
                    .execute();
        } catch (RuntimeException exception) {
            throw persistenceFailure(
                    "Could not record handled Booking command",
                    exception
            );
        }
    }

    private void lockCapacitySubject(String subjectIdentifier) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                merchantScope.merchantIdentifier()
                        + "|booking-capacity|"
                        + subjectIdentifier
        );
    }

    private Optional<AllocationClaim> conflictingClaim(
            TimeWindowAllocationScope scope
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
                .and(CAPACITY_SUBJECT_IDENTIFIER.eq(scope.subjectIdentifier()))
                .and(CAPACITY_STARTS_AT.lt(scope.endsAt()))
                .and(CAPACITY_ENDS_AT.gt(scope.startsAt()))
                .orderBy(CLAIMED_AT, CLAIM_IDENTIFIER)
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(record).map(row -> new AllocationClaim(
                row.get(CLAIM_IDENTIFIER),
                new TimeWindowAllocationScope(
                        row.get(CAPACITY_SUBJECT_IDENTIFIER),
                        row.get(CAPACITY_STARTS_AT),
                        row.get(CAPACITY_ENDS_AT)
                ),
                row.get(USE_IDENTIFIER),
                row.get(CLAIMED_AT)
        ));
    }

    private void requireMerchantScope(MerchantScope actual, String label) {
        if (!merchantScope.equals(actual)) {
            throw new IllegalArgumentException(
                    label + " belongs to another merchant scope"
            );
        }
    }

    private static TimeWindowAllocationScope requireTimeWindowScope(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope, "scope");
        if (!(scope instanceof TimeWindowAllocationScope timeWindow)) {
            throw new IllegalArgumentException(
                    "Booking transaction requires a time-window Allocation"
            );
        }
        return timeWindow;
    }

    private static BookingPersistenceException persistenceFailure(
            String message,
            RuntimeException cause
    ) {
        return new BookingPersistenceException(message, cause);
    }
}
