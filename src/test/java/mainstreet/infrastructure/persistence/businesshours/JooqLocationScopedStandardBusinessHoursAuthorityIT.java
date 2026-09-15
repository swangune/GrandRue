package mainstreet.infrastructure.persistence.businesshours;

import mainstreet.application.MerchantScope;
import mainstreet.businesshours.BusinessHoursFailureCategory;
import mainstreet.businesshours.BusinessHoursMutationException;
import mainstreet.businesshours.BusinessHoursScope;
import mainstreet.businesshours.ConfigureStandardBusinessHoursCommand;
import mainstreet.businesshours.StandardBusinessHours;
import mainstreet.businesshours.WeeklyOperatingInterval;
import mainstreet.infrastructure.persistence.merchantprofile.JooqMerchantLocationAuthority;
import mainstreet.merchantprofile.CreateMerchantLocationCommand;
import mainstreet.merchantprofile.MerchantLocationRevision;
import mainstreet.merchantprofile.PostalAddressInput;
import mainstreet.merchantprofile.RetireMerchantLocationCommand;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;
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
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqLocationScopedStandardBusinessHoursAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final String LOCATION = "location-high-street";
    private static final Instant NOW =
            Instant.parse("2026-08-30T08:00:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactions;

    @BeforeEach
    void setUp() {
        DataSource source = new DriverManagerDataSource(
                required("MAINSTREET_TEST_POSTGRES_URL"),
                required("MAINSTREET_TEST_POSTGRES_USER"),
                required("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        transactions = new DataSourceTransactionManager(source);
        Flyway.configure().dataSource(source).locations("classpath:db/migration")
                .load().migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(source),
                SQLDialect.POSTGRES
        );
        dsl.execute("truncate table standard_business_hours_interval, "
                + "current_standard_business_hours, standard_business_hours_revision, "
                + "merchant_location_original_address_line, "
                + "merchant_location_normalized_address_line, "
                + "current_merchant_location, merchant_location_revision, "
                + "merchant_controller_relationship, merchant_account cascade");
        dsl.execute("insert into merchant_account (merchant_identifier) "
                + "values ('merchant-acme')");
        dsl.execute("insert into merchant_controller_relationship "
                + "(controller_relationship_identifier, merchant_identifier, "
                + "identity_identifier, lifecycle) values "
                + "('controller-rel-a','merchant-acme','controller-a','ACTIVE')");
    }

    @Test
    void configures_location_scope_only_from_exact_current_active_location() {
        MerchantLocationRevision location = createLocation("location-request-1");
        ConfigureStandardBusinessHoursCommand command = configure("hours-request-1");

        var revision = hoursAuthority().configure(
                command,
                context()
        );

        assertEquals(Optional.of(location.revisionIdentity()),
                revision.merchantLocationRevisionIdentity());
        assertEquals(command.standardBusinessHours(),
                revision.standardBusinessHours().orElseThrow());
        assertTrue(hoursAuthority().current(
                BusinessHoursScope.merchant(MERCHANT)
        ).isEmpty());
    }

    @Test
    void missing_and_retired_locations_fail_with_distinguishable_outcomes() {
        assertEquals(
                BusinessHoursFailureCategory.MERCHANT_LOCATION_NOT_FOUND,
                assertThrows(BusinessHoursMutationException.class, () ->
                        hoursAuthority().configure(
                                configure("hours-missing"),
                                context()
                        )
                ).category()
        );

        MerchantLocationRevision location = createLocation("location-request-1");
        locationAuthority().retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        location.revisionIdentity(),
                        "location-retire",
                        "merchant-approved-retirement",
                        "controller-a",
                        NOW.plusSeconds(2)
                ),
                context()
        );

        assertEquals(
                BusinessHoursFailureCategory.MERCHANT_LOCATION_RETIRED,
                assertThrows(BusinessHoursMutationException.class, () ->
                        hoursAuthority().configure(
                                configure("hours-retired"),
                                context()
                        )
                ).category()
        );
    }

    @Test
    void exact_retry_retains_consumed_location_revision_after_retirement() {
        MerchantLocationRevision location = createLocation("location-request-1");
        ConfigureStandardBusinessHoursCommand command = configure("hours-request-1");
        var committed = hoursAuthority().configure(command, context());
        locationAuthority().retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        location.revisionIdentity(),
                        "location-retire",
                        "merchant-approved-retirement",
                        "controller-a",
                        NOW.plusSeconds(2)
                ),
                context()
        );

        assertEquals(committed, hoursAuthority().configure(command, context()));
        assertEquals(Optional.of(location.revisionIdentity()),
                committed.merchantLocationRevisionIdentity());
    }

    private MerchantLocationRevision createLocation(String request) {
        return locationAuthority().create(
                new CreateMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        Optional.of("High Street"),
                        new PostalAddressInput(
                                "GB",
                                List.of("10 High Street"),
                                Optional.empty(),
                                Optional.of("Swansea"),
                                Optional.of("Wales"),
                                Optional.of("SA1 1AA"),
                                Optional.empty()
                        ),
                        Optional.empty(),
                        request,
                        "merchant-approved-location",
                        "controller-a",
                        NOW
                ),
                context()
        );
    }

    private ConfigureStandardBusinessHoursCommand configure(String request) {
        BusinessHoursScope scope = BusinessHoursScope.merchantLocation(
                MERCHANT,
                LOCATION
        );
        return new ConfigureStandardBusinessHoursCommand(
                new StandardBusinessHours(
                        scope,
                        "Europe/London",
                        Set.of(new WeeklyOperatingInterval(
                                DayOfWeek.MONDAY,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0)
                        ))
                ),
                Optional.empty(),
                request,
                "controller-a",
                NOW.plusSeconds(1)
        );
    }

    private JooqStandardBusinessHoursAuthority hoursAuthority() {
        return new JooqStandardBusinessHoursAuthority(dsl, transactions);
    }

    private JooqMerchantLocationAuthority locationAuthority() {
        return new JooqMerchantLocationAuthority(dsl, transactions);
    }

    private static TrustedExecutionContext context() {
        return new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal("controller-a"),
                Optional.of(new AuthenticationProvenance(
                        "session-1",
                        "controller-a",
                        NOW.minusSeconds(1)
                ))
        );
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: " + name
            );
        }
        return value;
    }
}
