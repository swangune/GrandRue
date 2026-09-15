package mainstreet.infrastructure.persistence.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.merchantprofile.CountryWideGeographyV1;
import mainstreet.merchantprofile.CorrectMerchantLocationCommand;
import mainstreet.merchantprofile.CreateMerchantLocationCommand;
import mainstreet.merchantprofile.CreateMerchantServiceAreaCommand;
import mainstreet.merchantprofile.MerchantLocationRevision;
import mainstreet.merchantprofile.MerchantLocationRadiusGeographyV1;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.MerchantServiceAreaAuthority;
import mainstreet.merchantprofile.MerchantServiceAreaExposure;
import mainstreet.merchantprofile.MerchantServiceAreaLifecycle;
import mainstreet.merchantprofile.MerchantServiceAreaRevision;
import mainstreet.merchantprofile.NamedAreaGeographyV1;
import mainstreet.merchantprofile.PostalAddressInput;
import mainstreet.merchantprofile.RemoteCountriesGeographyV1;
import mainstreet.merchantprofile.RetireMerchantLocationCommand;
import mainstreet.merchantprofile.RetireMerchantServiceAreaCommand;
import mainstreet.merchantprofile.ServiceAreaGeographyV1;
import mainstreet.merchantprofile.UpdateMerchantServiceAreaCommand;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqMerchantServiceAreaAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final String AREA = "service-area-south-wales";
    private static final String LOCATION = "location-high-street";
    private static final Instant NOW =
            Instant.parse("2026-08-30T15:00:00Z");

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
        dsl.execute("truncate table merchant_service_area_remote_country, "
                + "current_merchant_service_area, "
                + "merchant_service_area_revision, "
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
    void creates_exact_named_area_and_retains_current_provenance() {
        MerchantServiceAreaRevision created = authority().create(
                create(
                        new NamedAreaGeographyV1(" gb ", " Swansea "),
                        "Swansea and surrounding areas",
                        "request-1"
                ),
                context("controller-a", true)
        );

        assertEquals(1, created.revisionNumber());
        assertEquals(MerchantServiceAreaLifecycle.ACTIVE,
                created.lifecycle());
        assertEquals(
                new NamedAreaGeographyV1("GB", "Swansea"),
                created.geography()
        );
        assertEquals(MerchantServiceAreaExposure.PUBLIC, created.exposure());
        assertEquals("controller-rel-a",
                created.controllerRelationshipIdentity());
        assertEquals(created, authority().current(MERCHANT, AREA).orElseThrow());
        assertEquals(created, authority().revision(created.revisionIdentity())
                .orElseThrow());
    }

    @Test
    void normalizes_country_wide_and_remote_country_variants() {
        var country = authority().create(
                create(new CountryWideGeographyV1(" gb "),
                        "Available throughout the UK", "request-country"),
                context("controller-a", true)
        );
        var remote = authority().create(
                new CreateMerchantServiceAreaCommand(
                        MERCHANT,
                        "service-area-remote",
                        new RemoteCountriesGeographyV1(
                                List.of("us", "GB", " us ")
                        ),
                        "Remote service in the UK and US",
                        MerchantServiceAreaExposure.PUBLIC,
                        "request-remote",
                        "merchant-approved-service-area",
                        "controller-a",
                        NOW.plusSeconds(2)
                ),
                context("controller-a", true)
        );

        assertEquals(new CountryWideGeographyV1("GB"), country.geography());
        assertEquals(
                new RemoteCountriesGeographyV1(List.of("GB", "US")),
                remote.geography()
        );
    }

    @Test
    void location_radius_requires_exact_current_same_merchant_active_revision() {
        MerchantLocationRevision location = createLocation();
        var radius = new MerchantLocationRadiusGeographyV1(
                LOCATION,
                location.revisionIdentity(),
                24_140
        );
        MerchantServiceAreaRevision created = authority().create(
                create(radius, "Within 15 miles of Swansea", "request-radius"),
                context("controller-a", true)
        );
        assertEquals(radius, created.geography());

        MerchantLocationRevision corrected = locationAuthority().correct(
                correctLocation(location),
                context("controller-a", true)
        );
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority().create(
                                new CreateMerchantServiceAreaCommand(
                                        MERCHANT,
                                        "service-area-stale-radius",
                                        radius,
                                        "Old-radius anchor",
                                        MerchantServiceAreaExposure.PUBLIC,
                                        "request-stale",
                                        "merchant-approved-service-area",
                                        "controller-a",
                                        NOW.plusSeconds(4)
                                ),
                                context("controller-a", true)
                        )
                ).category()
        );

        locationAuthority().retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        corrected.revisionIdentity(),
                        "location-retire",
                        "merchant-approved-location-retirement",
                        "controller-a",
                        NOW.plusSeconds(5)
                ),
                context("controller-a", true)
        );
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority().create(
                                new CreateMerchantServiceAreaCommand(
                                        MERCHANT,
                                        "service-area-retired-radius",
                                        new MerchantLocationRadiusGeographyV1(
                                                LOCATION,
                                                corrected.revisionIdentity(),
                                                1_000
                                        ),
                                        "Retired-radius anchor",
                                        MerchantServiceAreaExposure.PUBLIC,
                                        "request-retired",
                                        "merchant-approved-service-area",
                                        "controller-a",
                                        NOW.plusSeconds(6)
                                ),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void update_and_retirement_advance_current_without_rewriting_history() {
        MerchantServiceAreaAuthority authority = authority();
        var first = authority.create(
                create(new NamedAreaGeographyV1("GB", "Swansea"),
                        "Swansea", "request-1"),
                context("controller-a", true)
        );
        var updated = authority.update(
                update(first, new CountryWideGeographyV1("GB"),
                        "UK-wide", "request-2"),
                context("controller-a", true)
        );
        var retired = authority.retire(
                retire(updated, "request-3"),
                context("controller-a", true)
        );

        assertEquals(Optional.of(first.revisionIdentity()),
                updated.predecessorRevisionIdentity());
        assertEquals(new CountryWideGeographyV1("GB"), updated.geography());
        assertEquals(first, authority.revision(first.revisionIdentity())
                .orElseThrow());
        assertEquals(MerchantServiceAreaLifecycle.RETIRED,
                retired.lifecycle());
        assertEquals(updated.geography(), retired.geography());
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.update(
                                update(retired, updated.geography(),
                                        "Cannot reactivate", "request-4"),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void exact_retry_survives_later_location_retirement_and_conflicts_on_change() {
        MerchantLocationRevision location = createLocation();
        var command = create(
                new MerchantLocationRadiusGeographyV1(
                        LOCATION,
                        location.revisionIdentity(),
                        5_000
                ),
                "Within five kilometres",
                "request-1"
        );
        MerchantServiceAreaRevision first = authority().create(
                command,
                context("controller-a", true)
        );
        locationAuthority().retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        location.revisionIdentity(),
                        "location-retire",
                        "merchant-approved-location-retirement",
                        "controller-a",
                        NOW.plusSeconds(3)
                ),
                context("controller-a", true)
        );

        assertEquals(first,
                authority().create(command, context("controller-a", true)));
        assertEquals(
                MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority().create(
                                create(command.geography(), "Changed wording",
                                        "request-1"),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void requires_authenticated_current_controller_and_open_account() {
        var command = create(new NamedAreaGeographyV1("GB", "Swansea"),
                "Swansea", "request-1");
        assertFailure(command, context("controller-a", false),
                MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED);
        assertFailure(
                new CreateMerchantServiceAreaCommand(
                        MERCHANT,
                        AREA,
                        command.geography(),
                        command.publicDescription(),
                        command.exposure(),
                        "request-staff",
                        command.provenanceReference(),
                        "staff-b",
                        command.committedAt()
                ),
                context("staff-b", true),
                MerchantProfileFailureCategory.AUTHORISATION_REJECTION
        );
        dsl.execute("update merchant_account set lifecycle='CLOSING' "
                + "where merchant_identifier='merchant-acme'");
        assertFailure(command, context("controller-a", true),
                MerchantProfileFailureCategory
                        .MERCHANT_ACCOUNT_OPERATION_RESTRICTED);
    }

    @Test
    void concurrent_updates_from_one_revision_commit_at_most_once()
            throws Exception {
        var first = authority().create(
                create(new NamedAreaGeographyV1("GB", "Swansea"),
                        "Swansea", "request-1"),
                context("controller-a", true)
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(
                    ready, start, update(first,
                            new NamedAreaGeographyV1("GB", "Cardiff"),
                            "Cardiff", "request-a")
            ));
            var b = executor.submit(() -> afterBarrier(
                    ready, start, update(first,
                            new CountryWideGeographyV1("GB"),
                            "UK-wide", "request-b")
            ));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantServiceAreaRevision.class::isInstance)
                    .count());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantProfileMutationException.class::isInstance)
                    .map(MerchantProfileMutationException.class::cast)
                    .filter(failure -> failure.category()
                            == MerchantProfileFailureCategory
                            .PROFILE_REVISION_CONFLICT)
                    .count());
        }
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_service_area_revision"))
        ));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            UpdateMerchantServiceAreaCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return authority().update(command, context("controller-a", true));
        } catch (MerchantProfileMutationException failure) {
            return failure;
        }
    }

    private void assertFailure(
            CreateMerchantServiceAreaCommand command,
            TrustedExecutionContext context,
            MerchantProfileFailureCategory category
    ) {
        assertEquals(category, assertThrows(
                MerchantProfileMutationException.class,
                () -> authority().create(command, context)
        ).category());
    }

    private MerchantServiceAreaAuthority authority() {
        return new JooqMerchantServiceAreaAuthority(dsl, transactions);
    }

    private JooqMerchantLocationAuthority locationAuthority() {
        return new JooqMerchantLocationAuthority(dsl, transactions);
    }

    private MerchantLocationRevision createLocation() {
        return locationAuthority().create(
                new CreateMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        Optional.of("High Street"),
                        address("10 High Street"),
                        Optional.empty(),
                        "location-create",
                        "merchant-approved-location",
                        "controller-a",
                        NOW
                ),
                context("controller-a", true)
        );
    }

    private CorrectMerchantLocationCommand correctLocation(
            MerchantLocationRevision expected
    ) {
        return new CorrectMerchantLocationCommand(
                MERCHANT,
                LOCATION,
                expected.revisionIdentity(),
                Optional.of("High Street"),
                address("10A High Street"),
                Optional.empty(),
                "location-correct",
                "merchant-approved-location-correction",
                "controller-a",
                NOW.plusSeconds(3)
        );
    }

    private static PostalAddressInput address(String line) {
        return new PostalAddressInput(
                "GB",
                List.of(line),
                Optional.empty(),
                Optional.of("Swansea"),
                Optional.of("Wales"),
                Optional.of("SA1 1AA"),
                Optional.empty()
        );
    }

    private static CreateMerchantServiceAreaCommand create(
            ServiceAreaGeographyV1 geography,
            String description,
            String request
    ) {
        return new CreateMerchantServiceAreaCommand(
                MERCHANT,
                AREA,
                geography,
                description,
                MerchantServiceAreaExposure.PUBLIC,
                request,
                "merchant-approved-service-area",
                "controller-a",
                NOW.plusSeconds(1)
        );
    }

    private static UpdateMerchantServiceAreaCommand update(
            MerchantServiceAreaRevision expected,
            ServiceAreaGeographyV1 geography,
            String description,
            String request
    ) {
        return new UpdateMerchantServiceAreaCommand(
                expected.merchantScope(),
                expected.serviceAreaIdentity(),
                expected.revisionIdentity(),
                geography,
                description,
                expected.exposure(),
                request,
                "merchant-approved-service-area-update",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber() + 1)
        );
    }

    private static RetireMerchantServiceAreaCommand retire(
            MerchantServiceAreaRevision expected,
            String request
    ) {
        return new RetireMerchantServiceAreaCommand(
                expected.merchantScope(),
                expected.serviceAreaIdentity(),
                expected.revisionIdentity(),
                request,
                "merchant-approved-service-area-retirement",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber() + 1)
        );
    }

    private static TrustedExecutionContext context(
            String principal,
            boolean authenticated
    ) {
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
            throw new IllegalStateException(
                    "Required environment variable is missing: " + name
            );
        }
        return value;
    }
}
