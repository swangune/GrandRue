package grandrue.infrastructure.persistence.booking;

import grandrue.application.MerchantScope;
import grandrue.booking.BookingAvailabilityImpactAssessment;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.*;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqBookingResidualObligationAuthorityIT {

    private DSLContext dsl;

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

        dsl = DSL.using(dataSource, SQLDialect.POSTGRES);
        clearBookingTables();
    }

    @Test
    void in_force_booking_remains_outstanding_even_after_reservation_interval_elapsed() {
        insertBooking(
                "merchant-residual-a",
                "booking-past",
                Instant.parse("2026-08-01T09:00:00Z"),
                Instant.parse("2026-08-01T10:00:00Z"),
                "IN_FORCE"
        );

        JooqBookingResidualObligationAuthority authority =
                new JooqBookingResidualObligationAuthority(dsl);

        assertTrue(authority.hasOutstandingBookingObligation(
                new MerchantScope("merchant-residual-a")
        ));
    }

    @Test
    void released_booking_no_longer_requires_booking_residual_management() {
        insertBooking(
                "merchant-residual-b",
                "booking-released",
                Instant.parse("2026-09-01T09:00:00Z"),
                Instant.parse("2026-09-01T10:00:00Z"),
                "RELEASED"
        );

        JooqBookingResidualObligationAuthority authority =
                new JooqBookingResidualObligationAuthority(dsl);

        assertFalse(authority.hasOutstandingBookingObligation(
                new MerchantScope("merchant-residual-b")
        ));
    }

    @Test
    void residual_obligation_query_is_merchant_scoped() {
        insertBooking(
                "merchant-residual-owner",
                "booking-owner",
                Instant.parse("2026-09-01T09:00:00Z"),
                Instant.parse("2026-09-01T10:00:00Z"),
                "IN_FORCE"
        );

        JooqBookingResidualObligationAuthority authority =
                new JooqBookingResidualObligationAuthority(dsl);

        assertTrue(authority.hasOutstandingBookingObligation(
                new MerchantScope("merchant-residual-owner")
        ));
        assertFalse(authority.hasOutstandingBookingObligation(
                new MerchantScope("merchant-residual-other")
        ));
    }

    @Test
    void deactivation_impact_uses_stored_booking_truth_without_discharging_elapsed_reservations() {
        insertBooking("merchant-impact", "booking-impact", Instant.parse("2026-08-01T09:00:00Z"),
                Instant.parse("2026-08-01T10:00:00Z"), "IN_FORCE");
        var before = dsl.fetch("select * from booking_booking");

        var result = deactivationImpact("merchant-impact");

        assertEquals(List.of("New bookings will no longer be enabled."), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL, ConfigurationImpactClassification.INFORMATIONAL),
                result.findings().stream().map(ConfigurationImpactFinding::classification).toList());
        assertEquals(before, dsl.fetch("select * from booking_booking"));
    }

    @Test
    void deactivation_impact_does_not_report_released_or_foreign_bookings_as_residual_obligations() {
        insertBooking("merchant-impact", "booking-released-impact", Instant.parse("2026-09-10T09:00:00Z"),
                Instant.parse("2026-09-10T10:00:00Z"), "RELEASED");
        insertBooking("merchant-foreign", "booking-foreign-impact", Instant.parse("2026-09-10T09:00:00Z"),
                Instant.parse("2026-09-10T10:00:00Z"), "IN_FORCE");

        var result = deactivationImpact("merchant-impact");

        assertEquals(1, result.findings().size());
        assertEquals(ConfigurationImpactClassification.CONSEQUENTIAL, result.findings().getFirst().classification());
    }

    private ConfigurationImpactContribution deactivationImpact(String merchant) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot("release-1",
                Set.of(new RegisteredCapability("booking", List.of(), List.of()))));
        var compiler = new ConfigurationCompiler(registry);
        var base = new MerchantConfiguration(merchant, "c1", 1, "release-1", Set.of("booking"));
        var candidate = new MerchantConfiguration(merchant, "c2", 2, "release-1",
                Set.of(), Set.of(), Optional.of("c1"), Optional.empty());
        var at = Instant.parse("2026-09-06T12:00:00Z");
        var resolved = new ConfigurationPackageResolver(compiler).resolve(candidate, "compiler-1", at);
        var validation = new ConfigurationValidationEvidence("v1", merchant, "c2", "release-1", "p1",
                ConfigurationValidationOutcome.SUCCEEDED, "compiler-1", at, at);
        return new BookingAvailabilityImpactAssessment(compiler, new JooqBookingResidualObligationAuthority(dsl))
                .assess(new ConfigurationImpactContext(candidate, Optional.of(base), validation, resolved));
    }

    private void insertBooking(
            String merchantIdentifier,
            String bookingIdentifier,
            Instant startsAt,
            Instant endsAt,
            String reservationCommitmentState
    ) {
        dsl.execute(
                """
                insert into booking_booking (
                    merchant_identifier,
                    booking_identifier,
                    customer_context_identifier,
                    booked_subject_reference,
                    reservation_starts_at,
                    reservation_ends_at,
                    governing_release_identifier,
                    confirmed_at,
                    reservation_commitment_state
                ) values (
                    ?, ?, ?, ?,
                    cast(? as timestamp with time zone),
                    cast(? as timestamp with time zone),
                    ?,
                    cast(? as timestamp with time zone),
                    ?
                )
                """,
                merchantIdentifier,
                bookingIdentifier,
                "customer-" + bookingIdentifier,
                "subject-" + bookingIdentifier,
                startsAt,
                endsAt,
                "release-1",
                startsAt.minusSeconds(60),
                reservationCommitmentState
        );
    }

    private void clearBookingTables() {
        dsl.deleteFrom(DSL.table(DSL.name("booking_handled_command"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("booking_allocation_claim"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("booking_outbox"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("booking_booking"))).execute();
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
