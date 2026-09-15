package mainstreet.infrastructure.persistence.appointment;

import mainstreet.application.MerchantScope;
import mainstreet.booking.CommandIdentityConflictException;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.TimeWindowAllocationScope;
import mainstreet.scheduling.Appointment;
import mainstreet.scheduling.AppointmentConfirmation;
import mainstreet.scheduling.AppointmentTransaction;
import mainstreet.scheduling.AppointmentUnitOfWork;
import mainstreet.scheduling.ConfirmAppointmentCommand;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/** PostgreSQL/jOOQ consistency boundary for Appointment confirmation. */
public final class JooqAppointmentUnitOfWork implements AppointmentUnitOfWork {

    private static final Table<?> APPOINTMENT =
            DSL.table(DSL.name("appointment_appointment"));
    private static final Table<?> ALLOCATION =
            DSL.table(DSL.name("appointment_allocation_claim"));
    private static final Table<?> OUTBOX =
            DSL.table(DSL.name("appointment_outbox"));
    private static final Table<?> HANDLED =
            DSL.table(DSL.name("appointment_handled_command"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> COMMAND_IDENTIFIER =
            DSL.field(DSL.name("command_identifier"), String.class);
    private static final Field<String> APPOINTMENT_IDENTIFIER =
            DSL.field(DSL.name("appointment_identifier"), String.class);
    private static final Field<String> CUSTOMER_CONTEXT_IDENTIFIER =
            DSL.field(DSL.name("customer_context_identifier"), String.class);
    private static final Field<String> SCHEDULED_OPERATION_IDENTIFIER =
            DSL.field(DSL.name("scheduled_operation_identifier"), String.class);
    private static final Field<String> CAPACITY_SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("capacity_subject_identifier"), String.class);
    private static final Field<Instant> CAPACITY_STARTS_AT =
            DSL.field(DSL.name("capacity_starts_at"), Instant.class);
    private static final Field<Instant> CAPACITY_ENDS_AT =
            DSL.field(DSL.name("capacity_ends_at"), Instant.class);
    private static final Field<Instant> CONFIRMED_AT =
            DSL.field(DSL.name("confirmed_at"), Instant.class);
    private static final Field<String> ALLOCATION_IDENTIFIER =
            DSL.field(DSL.name("allocation_identifier"), String.class);
    private static final Field<String> EVENT_IDENTIFIER =
            DSL.field(DSL.name("event_identifier"), String.class);
    private static final Field<String> CLAIM_IDENTIFIER =
            DSL.field(DSL.name("claim_identifier"), String.class);
    private static final Field<String> USE_IDENTIFIER =
            DSL.field(DSL.name("use_identifier"), String.class);
    private static final Field<Instant> CLAIMED_AT =
            DSL.field(DSL.name("claimed_at"), Instant.class);
    private static final Field<String> GOVERNING_RELEASE_IDENTIFIER =
            DSL.field(DSL.name("governing_release_identifier"), String.class);
    private static final Field<Long> APPOINTMENT_REVISION =
            DSL.field(DSL.name("appointment_revision"), Long.class);
    private static final Field<String> FACT_IDENTIFIER =
            DSL.field(DSL.name("fact_identifier"), String.class);
    private static final Field<String> SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("subject_identifier"), String.class);
    private static final Field<String> CAUSATION_IDENTIFIER =
            DSL.field(DSL.name("causation_identifier"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqAppointmentUnitOfWork(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public AppointmentConfirmation execute(
            ConfirmAppointmentCommand command,
            Function<AppointmentTransaction, AppointmentConfirmation> work
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(work, "work");
        AppointmentConfirmation confirmation = transactionTemplate.execute(status -> {
            lockLogicalCommand(command.merchantScope(), command.identifier());
            Optional<HandledCommand> handled = handledCommand(
                    command.merchantScope(),
                    command.identifier()
            );
            if (handled.isPresent()) {
                HandledCommand existing = handled.orElseThrow();
                if (!existing.command().equals(command)) {
                    throw new CommandIdentityConflictException(command.identifier());
                }
                return loadConfirmation(existing);
            }
            JooqAppointmentTransaction transaction =
                    new JooqAppointmentTransaction(dsl, command.merchantScope());
            AppointmentConfirmation produced = Objects.requireNonNull(
                    work.apply(transaction),
                    "Appointment work returned no confirmation"
            );
            transaction.recordHandled(command, produced);
            return produced;
        });
        return Objects.requireNonNull(
                confirmation,
                "Appointment transaction returned no confirmation"
        );
    }

    public Optional<Appointment> appointment(
            MerchantScope merchantScope,
            String identifier
    ) {
        Record record = dsl.select(
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        SCHEDULED_OPERATION_IDENTIFIER,
                        CAPACITY_SUBJECT_IDENTIFIER,
                        CAPACITY_STARTS_AT,
                        CAPACITY_ENDS_AT,
                        ALLOCATION_IDENTIFIER,
                        GOVERNING_RELEASE_IDENTIFIER,
                        APPOINTMENT_REVISION,
                        CONFIRMED_AT
                )
                .from(APPOINTMENT)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(APPOINTMENT_IDENTIFIER.eq(identifier))
                .fetchOne();
        return Optional.ofNullable(record).map(row -> new Appointment(
                merchantScope,
                identifier,
                row.get(CUSTOMER_CONTEXT_IDENTIFIER),
                row.get(SCHEDULED_OPERATION_IDENTIFIER),
                interval(row),
                row.get(ALLOCATION_IDENTIFIER),
                row.get(GOVERNING_RELEASE_IDENTIFIER),
                row.get(APPOINTMENT_REVISION),
                row.get(CONFIRMED_AT)
        ));
    }

    public Optional<AllocationClaim> conflictingClaim(
            MerchantScope merchantScope,
            TimeWindowAllocationScope interval
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
                .and(CAPACITY_SUBJECT_IDENTIFIER.eq(interval.subjectIdentifier()))
                .and(CAPACITY_STARTS_AT.lt(interval.endsAt()))
                .and(CAPACITY_ENDS_AT.gt(interval.startsAt()))
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(record).map(this::allocation);
    }

    private Optional<HandledCommand> handledCommand(
            MerchantScope merchantScope,
            String commandIdentifier
    ) {
        Record record = dsl.select(
                        APPOINTMENT_IDENTIFIER,
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        SCHEDULED_OPERATION_IDENTIFIER,
                        CAPACITY_SUBJECT_IDENTIFIER,
                        CAPACITY_STARTS_AT,
                        CAPACITY_ENDS_AT,
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
        ConfirmAppointmentCommand command = new ConfirmAppointmentCommand(
                merchantScope,
                commandIdentifier,
                record.get(APPOINTMENT_IDENTIFIER),
                record.get(CUSTOMER_CONTEXT_IDENTIFIER),
                record.get(SCHEDULED_OPERATION_IDENTIFIER),
                interval(record)
        );
        return Optional.of(new HandledCommand(
                command,
                record.get(ALLOCATION_IDENTIFIER),
                record.get(EVENT_IDENTIFIER)
        ));
    }

    private AppointmentConfirmation loadConfirmation(HandledCommand handled) {
        MerchantScope scope = handled.command().merchantScope();
        Appointment appointment = appointment(
                scope,
                handled.command().appointmentIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled command has no Appointment"
        ));
        AllocationClaim allocation = allocation(
                scope,
                handled.allocationIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled command has no Allocation"
        ));
        DomainEvent event = event(
                scope,
                handled.eventIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled command has no event"
        ));
        return new AppointmentConfirmation(appointment, allocation, event);
    }

    private Optional<AllocationClaim> allocation(
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
        return Optional.ofNullable(record).map(this::allocation);
    }

    private Optional<DomainEvent> event(
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
        return Optional.ofNullable(record).map(row -> new DomainEvent(
                row.get(EVENT_IDENTIFIER),
                row.get(FACT_IDENTIFIER),
                row.get(SUBJECT_IDENTIFIER),
                row.get(CAUSATION_IDENTIFIER),
                row.get(OCCURRED_AT)
        ));
    }

    private AllocationClaim allocation(Record row) {
        return new AllocationClaim(
                row.get(CLAIM_IDENTIFIER),
                interval(row),
                row.get(USE_IDENTIFIER),
                row.get(CLAIMED_AT)
        );
    }

    private TimeWindowAllocationScope interval(Record row) {
        return new TimeWindowAllocationScope(
                row.get(CAPACITY_SUBJECT_IDENTIFIER),
                row.get(CAPACITY_STARTS_AT),
                row.get(CAPACITY_ENDS_AT)
        );
    }

    private void lockLogicalCommand(
            MerchantScope merchantScope,
            String commandIdentifier
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                merchantScope.merchantIdentifier()
                        + "|appointment-command|"
                        + commandIdentifier
        );
    }

    private record HandledCommand(
            ConfirmAppointmentCommand command,
            String allocationIdentifier,
            String eventIdentifier
    ) {
    }
}
