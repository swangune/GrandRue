package mainstreet.prototype;

import mainstreet.infrastructure.persistence.appointment.JooqAppointmentUnitOfWork;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrototypeJooqAppointmentUseCaseIT {

    private DataSource dataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure().dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl.execute("truncate table "
                + "appointment_handled_command, appointment_outbox, appointment_appointment, appointment_allocation_claim, "
                + "booking_handled_command, booking_outbox, booking_allocation_claim, booking_booking "
                + "cascade");
    }

    @Test
    void consultant_appointment_survives_use_case_recreation_and_idempotent_retry() {
        PrototypeAppointmentUseCase first = useCase();
        Instant startsAt = Instant.parse("2026-08-27T09:00:00Z");
        Instant endsAt = Instant.parse("2026-08-27T10:00:00Z");

        var committed = first.confirm(
                "prototype-consultant", "request-1", "appointment-1", "customer-1",
                "consultation.perform", startsAt, endsAt
        );

        PrototypeAppointmentUseCase restarted = useCase();
        var recovered = restarted.appointment(
                "prototype-consultant",
                "appointment-1"
        ).orElseThrow();
        var replayed = restarted.confirm(
                "prototype-consultant", "request-1", "appointment-1", "customer-1",
                "consultation.perform", startsAt, endsAt
        );

        assertEquals(committed, recovered);
        assertEquals(committed, replayed);
        assertEquals("consultation.perform", committed.scheduledOperationIdentifier());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("appointment_allocation_claim"))));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
    }

    @Test
    void gardener_job_uses_the_same_appointment_contract_without_booking_semantics() {
        PrototypeAppointmentUseCase appointments = useCase();
        Instant startsAt = Instant.parse("2026-08-27T13:00:00Z");
        Instant endsAt = Instant.parse("2026-08-27T15:00:00Z");

        var committed = appointments.confirm(
                "prototype-gardener", "request-gardener-1", "appointment-gardener-1",
                "customer-1", "gardening.perform", startsAt, endsAt
        );

        assertEquals("prototype-gardener", committed.merchantScope().merchantIdentifier());
        assertEquals("gardening.perform", committed.scheduledOperationIdentifier());
        assertEquals(startsAt, committed.scheduledInterval().startsAt());
        assertEquals(endsAt, committed.scheduledInterval().endsAt());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("appointment_allocation_claim"))));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
    }

    @Test
    void choice_driven_bookable_gardener_executes_the_same_appointment_path() {
        PrototypeAppointmentUseCase appointments = useCase();
        Instant startsAt = Instant.parse("2026-08-28T10:00:00Z");
        Instant endsAt = Instant.parse("2026-08-28T12:00:00Z");

        var committed = appointments.confirm(
                "prototype-gardener-bookable", "request-gardener-choice-1",
                "appointment-gardener-choice-1", "customer-1", "gardening.perform",
                startsAt, endsAt
        );

        assertEquals(
                "prototype-gardener-bookable",
                committed.merchantScope().merchantIdentifier()
        );
        assertEquals("gardening.perform", committed.scheduledOperationIdentifier());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
    }

    @Test
    void evolved_gardener_becomes_executable_after_new_configuration_activation() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        runtime.activateApprovedDiscoveryChoices(
                "prototype-gardener-evolving",
                "prototype-gardener-evolving-config-2",
                2,
                "prototype-gardener-evolving-release-2",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY,
                        PrototypeCustomerInteractionChoice.ARRANGE_APPOINTMENT
                )
        );
        PrototypeAppointmentUseCase appointments = useCase(runtime);
        Instant startsAt = Instant.parse("2026-08-29T09:00:00Z");
        Instant endsAt = Instant.parse("2026-08-29T11:00:00Z");

        var committed = appointments.confirm(
                "prototype-gardener-evolving", "request-gardener-evolved-1",
                "appointment-gardener-evolved-1", "customer-1", "gardening.perform",
                startsAt, endsAt
        );

        assertEquals(
                "prototype-gardener-evolving",
                committed.merchantScope().merchantIdentifier()
        );
        assertEquals("gardening.perform", committed.scheduledOperationIdentifier());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
    }

    @Test
    void scheduled_operation_fixtures_are_scoped_to_their_merchant() {
        PrototypeAppointmentUseCase appointments = useCase();
        Instant startsAt = Instant.parse("2026-08-27T09:00:00Z");
        Instant endsAt = Instant.parse("2026-08-27T10:00:00Z");

        assertThrows(IllegalArgumentException.class, () -> appointments.confirm(
                "prototype-gardener", "request-1", "appointment-1", "customer-1",
                "consultation.perform", startsAt, endsAt
        ));
        assertThrows(IllegalArgumentException.class, () -> appointments.confirm(
                "prototype-consultant", "request-2", "appointment-2", "customer-1",
                "gardening.perform", startsAt, endsAt
        ));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
    }

    @Test
    void non_appointment_merchant_cannot_use_appointment_path() {
        PrototypeAppointmentUseCase useCase = useCase();

        assertThrows(IllegalArgumentException.class, () -> useCase.confirm(
                "prototype-daycare", "request-1", "appointment-1", "customer-1",
                "gardening.perform", Instant.parse("2026-08-27T09:00:00Z"),
                Instant.parse("2026-08-27T10:00:00Z")
        ));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
    }

    private PrototypeAppointmentUseCase useCase() {
        return useCase(PrototypeMerchantRuntime.standard());
    }

    private PrototypeAppointmentUseCase useCase(PrototypeMerchantRuntime runtime) {
        JooqAppointmentUnitOfWork unitOfWork = new JooqAppointmentUnitOfWork(
                dsl,
                transactionManager
        );
        return new PrototypeJooqAppointmentUseCase(
                runtime,
                unitOfWork,
                unitOfWork::appointment,
                Clock.fixed(
                        Instant.parse("2026-08-26T03:00:00Z"),
                        ZoneOffset.UTC
                )
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
