package grandrue.infrastructure.persistence.appointment;

import grandrue.application.MerchantScope;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.AllocationConflictException;
import mainstreet.semantic.AllocationScope;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.TimeWindowAllocationScope;
import grandrue.scheduling.Appointment;
import grandrue.scheduling.AppointmentConfirmation;
import grandrue.scheduling.AppointmentTransaction;
import grandrue.scheduling.ConfirmAppointmentCommand;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL/jOOQ mutation boundary for Appointment confirmation. */
public final class JooqAppointmentTransaction implements AppointmentTransaction {

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
    private static final Field<String> ALLOCATION_IDENTIFIER =
            DSL.field(DSL.name("allocation_identifier"), String.class);
    private static final Field<String> GOVERNING_RELEASE_IDENTIFIER =
            DSL.field(DSL.name("governing_release_identifier"), String.class);
    private static final Field<Long> APPOINTMENT_REVISION =
            DSL.field(DSL.name("appointment_revision"), Long.class);
    private static final Field<Instant> CONFIRMED_AT =
            DSL.field(DSL.name("confirmed_at"), Instant.class);
    private static final Field<String> CLAIM_IDENTIFIER =
            DSL.field(DSL.name("claim_identifier"), String.class);
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
    private static final Field<Instant> ACKNOWLEDGED_AT =
            DSL.field(DSL.name("acknowledged_at"), Instant.class);
    private static final Field<String> COMMAND_IDENTIFIER =
            DSL.field(DSL.name("command_identifier"), String.class);

    private final DSLContext dsl;
    private final MerchantScope merchantScope;

    public JooqAppointmentTransaction(
            DSLContext dsl,
            MerchantScope merchantScope
    ) {
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
        TimeWindowAllocationScope timeWindow = requireTimeWindow(scope);
        lockCapacitySubject(timeWindow.subjectIdentifier());
        Optional<AllocationClaim> conflict = conflictingClaim(timeWindow);
        if (conflict.isPresent()) {
            throw new AllocationConflictException(conflict.orElseThrow());
        }
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
        return new AllocationClaim(identifier, timeWindow, useIdentifier, claimedAt);
    }

    @Override
    public void recordAppointment(Appointment appointment) {
        Objects.requireNonNull(appointment, "appointment");
        requireMerchantScope(appointment.merchantScope(), "Appointment");
        TimeWindowAllocationScope interval = appointment.scheduledInterval();
        dsl.insertInto(APPOINTMENT)
                .columns(
                        MERCHANT_IDENTIFIER,
                        APPOINTMENT_IDENTIFIER,
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
                .values(
                        merchantScope.merchantIdentifier(),
                        appointment.identifier(),
                        appointment.customerContextIdentifier(),
                        appointment.scheduledOperationIdentifier(),
                        interval.subjectIdentifier(),
                        interval.startsAt(),
                        interval.endsAt(),
                        appointment.allocationClaimIdentifier(),
                        appointment.governingReleaseIdentifier(),
                        appointment.revision(),
                        appointment.confirmedAt()
                )
                .execute();
    }

    @Override
    public void appendPendingEvent(DomainEvent event) {
        dsl.insertInto(OUTBOX)
                .columns(
                        MERCHANT_IDENTIFIER,
                        EVENT_IDENTIFIER,
                        FACT_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        CAUSATION_IDENTIFIER,
                        OCCURRED_AT,
                        ACKNOWLEDGED_AT
                )
                .values(
                        merchantScope.merchantIdentifier(),
                        event.identifier(),
                        event.factIdentifier(),
                        event.subjectIdentifier(),
                        event.causationIdentifier(),
                        event.occurredAt(),
                        null
                )
                .execute();
    }

    public void recordHandled(
            ConfirmAppointmentCommand command,
            AppointmentConfirmation confirmation
    ) {
        Appointment appointment = confirmation.appointment();
        dsl.insertInto(HANDLED)
                .columns(
                        MERCHANT_IDENTIFIER,
                        COMMAND_IDENTIFIER,
                        APPOINTMENT_IDENTIFIER,
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        SCHEDULED_OPERATION_IDENTIFIER,
                        CAPACITY_SUBJECT_IDENTIFIER,
                        CAPACITY_STARTS_AT,
                        CAPACITY_ENDS_AT,
                        CONFIRMED_AT,
                        ALLOCATION_IDENTIFIER,
                        EVENT_IDENTIFIER
                )
                .values(
                        merchantScope.merchantIdentifier(),
                        command.identifier(),
                        command.appointmentIdentifier(),
                        command.customerContextIdentifier(),
                        command.scheduledOperationIdentifier(),
                        command.scheduledInterval().subjectIdentifier(),
                        command.scheduledInterval().startsAt(),
                        command.scheduledInterval().endsAt(),
                        appointment.confirmedAt(),
                        confirmation.allocationClaim().identifier(),
                        confirmation.pendingEvent().identifier()
                )
                .execute();
    }

    private void lockCapacitySubject(String subjectIdentifier) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                merchantScope.merchantIdentifier()
                        + "|appointment-capacity|"
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

    private void requireMerchantScope(MerchantScope scope, String label) {
        if (!merchantScope.equals(scope)) {
            throw new IllegalArgumentException(
                    label + " belongs to another merchant scope"
            );
        }
    }

    private static TimeWindowAllocationScope requireTimeWindow(
            AllocationScope scope
    ) {
        if (!(Objects.requireNonNull(scope) instanceof TimeWindowAllocationScope window)) {
            throw new IllegalArgumentException(
                    "Appointment transaction requires a time-window Allocation"
            );
        }
        return window;
    }
}
