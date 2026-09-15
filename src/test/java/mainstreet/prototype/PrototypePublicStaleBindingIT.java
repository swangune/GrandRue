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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression evidence for MS-PROT-049 v1.2: a previously exposed public subject
 * reference never becomes execution authority. Current active semantics are
 * revalidated when the intent reaches the authoritative operation path.
 */
class PrototypePublicStaleBindingIT {

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl.execute("truncate table "
                + "appointment_handled_command, appointment_outbox, "
                + "appointment_appointment, appointment_allocation_claim, "
                + "booking_handled_command, booking_outbox, "
                + "booking_allocation_claim, booking_booking cascade");
    }

    @Test
    void previously_exposed_public_binding_is_revalidated_after_configuration_change() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        Clock clock = Clock.fixed(
                Instant.parse("2026-08-26T03:00:00Z"),
                ZoneOffset.UTC
        );
        JooqAppointmentUnitOfWork unitOfWork = new JooqAppointmentUnitOfWork(
                dsl,
                transactionManager
        );
        PrototypePublicAppointmentUseCase publicAppointments =
                new PrototypePublicAppointmentUseCase(new PrototypeJooqAppointmentUseCase(
                        runtime,
                        unitOfWork,
                        unitOfWork::appointment,
                        clock
                ));
        Instant startsAt = Instant.parse("2026-08-29T09:00:00Z");
        Instant endsAt = Instant.parse("2026-08-29T11:00:00Z");

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

        var exposedSurface = PrototypeStorefrontSurfaceProjection.standard(runtime)
                .surface("prototype-gardener-evolving");
        assertTrue(exposedSurface.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .flatMap(contribution -> contribution.bindings().stream())
                .anyMatch(binding -> "garden-maintenance".equals(
                        binding.subjectReference()
                )));

        // Simulate a visitor retaining release-2 browser state while the
        // merchant activates release 3 without Appointment capability.
        runtime.activateApprovedDiscoveryChoices(
                "prototype-gardener-evolving",
                "prototype-gardener-evolving-config-3",
                3,
                "prototype-gardener-evolving-release-3",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> publicAppointments.confirm(
                        "prototype-gardener-evolving",
                        "stale-public-intent-3",
                        "stale-public-appointment-3",
                        "garden-maintenance",
                        startsAt,
                        endsAt
                )
        );
        assertEquals(
                0,
                dsl.fetchCount(DSL.table(DSL.name("appointment_appointment")))
        );

        // Restoring Appointment in a later approved release makes the same
        // semantic public subject reference applicable again. The reference
        // itself never granted or revoked authority.
        runtime.activateApprovedDiscoveryChoices(
                "prototype-gardener-evolving",
                "prototype-gardener-evolving-config-4",
                4,
                "prototype-gardener-evolving-release-4",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY,
                        PrototypeCustomerInteractionChoice.ARRANGE_APPOINTMENT
                )
        );

        var committed = publicAppointments.confirm(
                "prototype-gardener-evolving",
                "current-public-intent-4",
                "current-public-appointment-4",
                "garden-maintenance",
                startsAt,
                endsAt
        );

        assertEquals(
                "prototype-gardener-evolving",
                committed.merchantScope().merchantIdentifier()
        );
        assertEquals("gardening.perform", committed.scheduledOperationIdentifier());
        assertEquals(
                1,
                dsl.fetchCount(DSL.table(DSL.name("appointment_appointment")))
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: "
                            + name
            );
        }
        return value;
    }
}
