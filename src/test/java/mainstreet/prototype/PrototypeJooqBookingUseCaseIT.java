package mainstreet.prototype;

import mainstreet.infrastructure.persistence.booking.JooqBookingUnitOfWork;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrototypeJooqBookingUseCaseIT {

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
                + "booking_handled_command, booking_outbox, booking_allocation_claim, booking_booking, "
                + "appointment_handled_command, appointment_outbox, appointment_appointment, appointment_allocation_claim "
                + "cascade");
    }

    @Test
    void motel_booking_survives_use_case_recreation_and_idempotent_retry() {
        PrototypeBookingUseCase first = useCase();
        Instant startsAt = Instant.parse("2026-08-27T14:00:00Z");
        Instant endsAt = Instant.parse("2026-08-29T10:00:00Z");

        var committed = first.confirm(
                "prototype-motel", "request-1", "booking-1", "customer-1",
                "standard-room", startsAt, endsAt
        );

        PrototypeBookingUseCase restarted = useCase();
        var recovered = restarted.booking("prototype-motel", "booking-1").orElseThrow();
        var replayed = restarted.confirm(
                "prototype-motel", "request-1", "booking-1", "customer-1",
                "standard-room", startsAt, endsAt
        );

        assertEquals(committed, recovered);
        assertEquals(committed, replayed);
        assertEquals("standard-room", committed.bookedSubjectReference());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("booking_allocation_claim"))));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
    }

    @Test
    void daycare_session_uses_the_same_booking_contract_without_appointment_semantics() {
        PrototypeBookingUseCase bookings = useCase();
        Instant startsAt = Instant.parse("2026-08-27T08:00:00Z");
        Instant endsAt = Instant.parse("2026-08-27T17:00:00Z");

        var committed = bookings.confirm(
                "prototype-daycare", "request-daycare-1", "booking-daycare-1",
                "customer-1", "daycare-session", startsAt, endsAt
        );

        assertEquals("prototype-daycare", committed.merchantScope().merchantIdentifier());
        assertEquals("daycare-session", committed.bookedSubjectReference());
        assertEquals(startsAt, committed.reservationWindow().startsAt());
        assertEquals(endsAt, committed.reservationWindow().endsAt());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("booking_allocation_claim"))));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("appointment_appointment"))));
    }

    @Test
    void booked_subject_fixtures_are_scoped_to_their_merchant() {
        PrototypeBookingUseCase bookings = useCase();
        Instant startsAt = Instant.parse("2026-08-27T08:00:00Z");
        Instant endsAt = Instant.parse("2026-08-27T17:00:00Z");

        assertThrows(IllegalArgumentException.class, () -> bookings.confirm(
                "prototype-daycare", "request-1", "booking-1", "customer-1",
                "standard-room", startsAt, endsAt
        ));
        assertThrows(IllegalArgumentException.class, () -> bookings.confirm(
                "prototype-motel", "request-2", "booking-2", "customer-1",
                "daycare-session", startsAt, endsAt
        ));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
    }

    @Test
    void non_booking_merchant_cannot_use_booking_path() {
        PrototypeBookingUseCase useCase = useCase();
        assertThrows(IllegalArgumentException.class, () -> useCase.confirm(
                "prototype-gardener", "request-1", "booking-1", "customer-1",
                "daycare-session", Instant.parse("2026-08-27T09:00:00Z"),
                Instant.parse("2026-08-27T10:00:00Z")
        ));
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("booking_booking"))));
    }

    private PrototypeBookingUseCase useCase() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-08-26T03:00:00Z"),
                ZoneOffset.UTC
        );
        JooqBookingUnitOfWork unitOfWork = new JooqBookingUnitOfWork(
                dsl,
                transactionManager,
                clock
        );
        return new PrototypeJooqBookingUseCase(
                PrototypeMerchantRuntime.standard(),
                unitOfWork,
                unitOfWork::booking,
                clock
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
