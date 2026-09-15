package mainstreet.infrastructure.persistence.businesshours;

import mainstreet.application.MerchantScope;
import mainstreet.businesshours.BusinessHoursFailureCategory;
import mainstreet.businesshours.BusinessHoursMutationException;
import mainstreet.businesshours.BusinessHoursScope;
import mainstreet.businesshours.ConfigureStandardBusinessHoursCommand;
import mainstreet.businesshours.StandardBusinessHours;
import mainstreet.businesshours.StandardBusinessHoursRevision;
import mainstreet.businesshours.StandardBusinessHoursRevisionDisposition;
import mainstreet.businesshours.WeeklyOperatingInterval;
import mainstreet.businesshours.WithdrawStandardBusinessHoursCommand;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqStandardBusinessHoursAuthorityIT {
    private static final MerchantScope MERCHANT = new MerchantScope("merchant-acme");
    private static final BusinessHoursScope SCOPE = BusinessHoursScope.merchant(MERCHANT);
    private static final Instant NOW = Instant.parse("2026-08-30T06:00:00Z");

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
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table standard_business_hours_interval, "
                + "current_standard_business_hours, standard_business_hours_revision, "
                + "merchant_controller_relationship, merchant_account cascade");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-acme')");
        dsl.execute("insert into merchant_controller_relationship "
                + "(controller_relationship_identifier, merchant_identifier, identity_identifier, lifecycle) "
                + "values ('controller-rel-a','merchant-acme','controller-a','ACTIVE')");
    }

    @Test
    void configures_exact_revision_and_current_pointer() {
        StandardBusinessHoursRevision first = authority().configure(
                configure(hours(9, 17), Optional.empty(), "request-1"),
                context("controller-a", true)
        );

        assertEquals(1, first.revisionNumber());
        assertEquals(StandardBusinessHoursRevisionDisposition.CONFIGURED, first.disposition());
        assertEquals(Optional.empty(), first.predecessorRevisionIdentity());
        assertEquals("controller-rel-a", first.controllerRelationshipIdentity());
        assertEquals(hours(9, 17), first.standardBusinessHours().orElseThrow());
        assertEquals(first, authority().current(SCOPE).orElseThrow());
        assertEquals(first, authority().revision(first.revisionIdentity()).orElseThrow());
    }

    @Test
    void replacement_withdrawal_and_closed_week_preserve_distinct_history() {
        var authority = authority();
        var first = authority.configure(
                configure(hours(9, 17), Optional.empty(), "request-1"),
                context("controller-a", true)
        );
        var closedWeek = new StandardBusinessHours(SCOPE, "Europe/London", Set.of());
        var second = authority.configure(
                configure(closedWeek, Optional.of(first.revisionIdentity()), "request-2"),
                context("controller-a", true)
        );
        var withdrawn = authority.withdraw(
                new WithdrawStandardBusinessHoursCommand(
                        SCOPE, second.revisionIdentity(), "request-3", "controller-a", NOW.plusSeconds(3)
                ),
                context("controller-a", true)
        );

        assertTrue(second.standardBusinessHours().orElseThrow()
                .weeklyOperatingIntervals().isEmpty());
        assertEquals(StandardBusinessHoursRevisionDisposition.WITHDRAWN, withdrawn.disposition());
        assertTrue(withdrawn.standardBusinessHours().isEmpty());
        assertEquals(3, withdrawn.revisionNumber());
        assertEquals(first, authority.revision(first.revisionIdentity()).orElseThrow());
        assertEquals(withdrawn, authority.current(SCOPE).orElseThrow());
    }

    @Test
    void stale_write_and_changed_retry_intent_fail_closed() {
        var authority = authority();
        var originalCommand = configure(hours(9, 17), Optional.empty(), "request-1");
        var first = authority.configure(originalCommand, context("controller-a", true));
        authority.configure(
                configure(hours(10, 18), Optional.of(first.revisionIdentity()), "request-2"),
                context("controller-a", true)
        );

        assertEquals(first, authority.configure(originalCommand, context("controller-a", true)));
        assertEquals(
                BusinessHoursFailureCategory.IDEMPOTENCY_CONFLICT,
                assertThrows(BusinessHoursMutationException.class, () -> authority.configure(
                        configure(hours(8, 16), Optional.empty(), "request-1"),
                        context("controller-a", true)
                )).category()
        );
        assertEquals(
                BusinessHoursFailureCategory.REVISION_CONFLICT,
                assertThrows(BusinessHoursMutationException.class, () -> authority.configure(
                        configure(hours(11, 19), Optional.of(first.revisionIdentity()), "request-stale"),
                        context("controller-a", true)
                )).category()
        );
        assertEquals(2, dsl.fetchCount(DSL.table(DSL.name("standard_business_hours_revision"))));
    }

    @Test
    void requires_authenticated_current_controller_open_account_and_merchant_scope() {
        var command = configure(hours(9, 17), Optional.empty(), "request-1");
        assertFailure(command, context("controller-a", false), BusinessHoursFailureCategory.AUTHENTICATION_REQUIRED);
        assertFailure(
                configure(
                        hours(9, 17),
                        Optional.empty(),
                        "request-staff",
                        "staff-b"
                ),
                context("staff-b", true),
                BusinessHoursFailureCategory.CURRENT_CONTROLLER_REQUIRED
        );

        dsl.execute("update merchant_account set lifecycle='CLOSING' where merchant_identifier='merchant-acme'");
        assertFailure(command, context("controller-a", true),
                BusinessHoursFailureCategory.MERCHANT_ACCOUNT_OPERATION_RESTRICTED);

        dsl.execute("update merchant_account set lifecycle='OPEN' where merchant_identifier='merchant-acme'");

        var locationCommand = new ConfigureStandardBusinessHoursCommand(
                new StandardBusinessHours(
                        BusinessHoursScope.merchantLocation(MERCHANT, "location-missing"),
                        "Europe/London",
                        Set.of()
                ),
                Optional.empty(),
                "request-location",
                "controller-a",
                NOW
        );
        assertFailure(locationCommand, context("controller-a", true),
                BusinessHoursFailureCategory.MERCHANT_LOCATION_NOT_FOUND);
    }

    @Test
    void concurrent_successors_from_one_revision_commit_at_most_once() throws Exception {
        var authority = authority();
        var first = authority.configure(
                configure(hours(9, 17), Optional.empty(), "request-1"),
                context("controller-a", true)
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(ready, start,
                    configure(hours(10, 18), Optional.of(first.revisionIdentity()), "request-a")));
            var b = executor.submit(() -> afterBarrier(ready, start,
                    configure(hours(11, 19), Optional.of(first.revisionIdentity()), "request-b")));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(StandardBusinessHoursRevision.class::isInstance).count());
            assertEquals(1, outcomes.stream()
                    .filter(BusinessHoursMutationException.class::isInstance)
                    .map(BusinessHoursMutationException.class::cast)
                    .filter(failure -> failure.category() == BusinessHoursFailureCategory.REVISION_CONFLICT)
                    .count());
        }
        assertEquals(2, dsl.fetchCount(DSL.table(DSL.name("standard_business_hours_revision"))));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            ConfigureStandardBusinessHoursCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return authority().configure(command, context("controller-a", true));
        } catch (BusinessHoursMutationException failure) {
            return failure;
        }
    }

    private void assertFailure(
            ConfigureStandardBusinessHoursCommand command,
            TrustedExecutionContext context,
            BusinessHoursFailureCategory expected
    ) {
        assertEquals(expected, assertThrows(
                BusinessHoursMutationException.class,
                () -> authority().configure(command, context)
        ).category());
    }

    private JooqStandardBusinessHoursAuthority authority() {
        return new JooqStandardBusinessHoursAuthority(dsl, transactions);
    }

    private static ConfigureStandardBusinessHoursCommand configure(
            StandardBusinessHours hours,
            Optional<String> expected,
            String request
    ) {
        return configure(hours, expected, request, "controller-a");
    }

    private static ConfigureStandardBusinessHoursCommand configure(
            StandardBusinessHours hours,
            Optional<String> expected,
            String request,
            String actor
    ) {
        return new ConfigureStandardBusinessHoursCommand(
                hours, expected, request, actor, NOW
        );
    }

    private static StandardBusinessHours hours(int start, int end) {
        return new StandardBusinessHours(
                SCOPE,
                "Europe/London",
                Set.of(new WeeklyOperatingInterval(
                        DayOfWeek.MONDAY,
                        LocalTime.of(start, 0),
                        LocalTime.of(end, 0)
                ))
        );
    }

    private static TrustedExecutionContext context(String principal, boolean authenticated) {
        return new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal(principal),
                authenticated
                        ? Optional.of(new AuthenticationProvenance(
                                "session-1", principal, NOW.minusSeconds(1)
                        ))
                        : Optional.empty()
        );
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required environment variable is missing: " + name);
        }
        return value;
    }
}
