package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.CreateMerchantContactPointCommand;
import grandrue.merchantprofile.CreateMerchantLocationCommand;
import grandrue.merchantprofile.MerchantContactPointAuthority;
import grandrue.merchantprofile.MerchantContactPointExposure;
import grandrue.merchantprofile.MerchantContactPointKind;
import grandrue.merchantprofile.MerchantContactPointLifecycle;
import grandrue.merchantprofile.MerchantContactPointRevision;
import grandrue.merchantprofile.MerchantContactPointScope;
import grandrue.merchantprofile.MerchantLocationRevision;
import grandrue.merchantprofile.MerchantProfileFailureCategory;
import grandrue.merchantprofile.MerchantProfileMutationException;
import grandrue.merchantprofile.PostalAddressInput;
import grandrue.merchantprofile.RetireMerchantContactPointCommand;
import grandrue.merchantprofile.RetireMerchantLocationCommand;
import grandrue.merchantprofile.UpdateMerchantContactPointCommand;
import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
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

class JooqMerchantContactPointAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final String CONTACT = "contact-public-phone";
    private static final String LOCATION = "location-high-street";
    private static final Instant NOW =
            Instant.parse("2026-08-30T09:00:00Z");

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
        dsl.execute("truncate table current_merchant_contact_point, "
                + "merchant_contact_point_revision, "
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
    void creates_exact_merchant_scoped_contact_and_retains_provenance() {
        MerchantContactPointRevision created = authority().create(
                create(merchantScope(), "request-1", "+44 1792 000000"),
                context("controller-a", true)
        );

        assertEquals(1, created.revisionNumber());
        assertEquals(MerchantContactPointLifecycle.ACTIVE, created.lifecycle());
        assertEquals(MerchantContactPointKind.TELEPHONE, created.kind());
        assertEquals(MerchantContactPointExposure.PUBLIC, created.exposure());
        assertEquals("+44 1792 000000", created.value());
        assertEquals(Optional.empty(), created.merchantLocationRevisionIdentity());
        assertEquals("controller-rel-a", created.controllerRelationshipIdentity());
        assertEquals(created, authority().current(
                MERCHANT,
                CONTACT
        ).orElseThrow());
        assertEquals(created, authority().revision(
                created.revisionIdentity()
        ).orElseThrow());
    }

    @Test
    void update_and_retirement_advance_current_without_rewriting_history() {
        MerchantContactPointAuthority authority = authority();
        var first = authority.create(
                create(merchantScope(), "request-1", "+44 1792 000000"),
                context("controller-a", true)
        );
        var updated = authority.update(
                update(first, "request-2", "+44 1792 111111"),
                context("controller-a", true)
        );
        var retired = authority.retire(
                retire(updated, "request-3"),
                context("controller-a", true)
        );

        assertEquals(Optional.of(first.revisionIdentity()),
                updated.predecessorRevisionIdentity());
        assertEquals("+44 1792 111111", updated.value());
        assertEquals(first, authority.revision(first.revisionIdentity()).orElseThrow());
        assertEquals(MerchantContactPointLifecycle.RETIRED, retired.lifecycle());
        assertEquals(updated.value(), retired.value());
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.update(
                                update(retired, "request-4", "+44 1792 222222"),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void location_scope_requires_current_same_merchant_active_location() {
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority().create(
                                create(locationScope(), "request-missing", "hello@example.com"),
                                context("controller-a", true)
                        )
                ).category()
        );

        MerchantLocationRevision location = createLocation();
        var created = authority().create(
                create(locationScope(), "request-location", "hello@example.com"),
                context("controller-a", true)
        );
        assertEquals(Optional.of(location.revisionIdentity()),
                created.merchantLocationRevisionIdentity());

        locationAuthority().retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        LOCATION,
                        location.revisionIdentity(),
                        "location-retire",
                        "merchant-approved-retirement",
                        "controller-a",
                        NOW.plusSeconds(3)
                ),
                context("controller-a", true)
        );
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority().create(
                                new CreateMerchantContactPointCommand(
                                        locationScope(),
                                        "contact-location-second",
                                        MerchantContactPointKind.EMAIL,
                                        "second@example.com",
                                        MerchantContactPointExposure.PRIVATE_INTERNAL,
                                        Optional.empty(),
                                        "request-retired",
                                        "merchant-approved-contact",
                                        "controller-a",
                                        NOW.plusSeconds(4)
                                ),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void exact_retry_survives_later_change_and_changed_intent_conflicts() {
        MerchantContactPointAuthority authority = authority();
        var create = create(merchantScope(), "request-1", "+44 1792 000000");
        var first = authority.create(create, context("controller-a", true));
        authority.update(
                update(first, "request-2", "+44 1792 111111"),
                context("controller-a", true)
        );

        assertEquals(first, authority.create(create, context("controller-a", true)));
        assertEquals(
                MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.create(
                                create(merchantScope(), "request-1", "+44 1792 999999"),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void requires_authenticated_current_controller_and_open_account() {
        var command = create(merchantScope(), "request-1", "+44 1792 000000");
        assertFailure(command, context("controller-a", false),
                MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED);
        assertFailure(
                new CreateMerchantContactPointCommand(
                        merchantScope(),
                        CONTACT,
                        MerchantContactPointKind.TELEPHONE,
                        "+44 1792 000000",
                        MerchantContactPointExposure.PUBLIC,
                        Optional.of("Reception"),
                        "request-staff",
                        "merchant-approved-contact",
                        "staff-b",
                        NOW
                ),
                context("staff-b", true),
                MerchantProfileFailureCategory.AUTHORISATION_REJECTION
        );
        dsl.execute("update merchant_account set lifecycle='CLOSING' "
                + "where merchant_identifier='merchant-acme'");
        assertFailure(command, context("controller-a", true),
                MerchantProfileFailureCategory.MERCHANT_ACCOUNT_OPERATION_RESTRICTED);
    }

    @Test
    void concurrent_updates_from_one_revision_commit_at_most_once()
            throws Exception {
        var first = authority().create(
                create(merchantScope(), "request-1", "+44 1792 000000"),
                context("controller-a", true)
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(
                    ready, start, update(first, "request-a", "+44 1792 111111")
            ));
            var b = executor.submit(() -> afterBarrier(
                    ready, start, update(first, "request-b", "+44 1792 222222")
            ));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantContactPointRevision.class::isInstance).count());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantProfileMutationException.class::isInstance)
                    .map(MerchantProfileMutationException.class::cast)
                    .filter(failure -> failure.category()
                            == MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT)
                    .count());
        }
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_contact_point_revision"))
        ));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            UpdateMerchantContactPointCommand command
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
            CreateMerchantContactPointCommand command,
            TrustedExecutionContext context,
            MerchantProfileFailureCategory category
    ) {
        assertEquals(category, assertThrows(
                MerchantProfileMutationException.class,
                () -> authority().create(command, context)
        ).category());
    }

    private MerchantContactPointAuthority authority() {
        return new JooqMerchantContactPointAuthority(dsl, transactions);
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
                        "location-create",
                        "merchant-approved-location",
                        "controller-a",
                        NOW
                ),
                context("controller-a", true)
        );
    }

    private static CreateMerchantContactPointCommand create(
            MerchantContactPointScope scope,
            String request,
            String value
    ) {
        return new CreateMerchantContactPointCommand(
                scope,
                CONTACT,
                value.contains("@")
                        ? MerchantContactPointKind.EMAIL
                        : MerchantContactPointKind.TELEPHONE,
                value,
                MerchantContactPointExposure.PUBLIC,
                Optional.of("Reception"),
                request,
                "merchant-approved-contact",
                "controller-a",
                NOW.plusSeconds(1)
        );
    }

    private static UpdateMerchantContactPointCommand update(
            MerchantContactPointRevision expected,
            String request,
            String value
    ) {
        return new UpdateMerchantContactPointCommand(
                expected.scope(),
                expected.contactPointIdentity(),
                expected.revisionIdentity(),
                expected.kind(),
                value,
                expected.exposure(),
                expected.label(),
                request,
                "merchant-approved-contact-update",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber() + 1)
        );
    }

    private static RetireMerchantContactPointCommand retire(
            MerchantContactPointRevision expected,
            String request
    ) {
        return new RetireMerchantContactPointCommand(
                expected.scope(),
                expected.contactPointIdentity(),
                expected.revisionIdentity(),
                request,
                "merchant-approved-contact-retirement",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber() + 1)
        );
    }

    private static MerchantContactPointScope merchantScope() {
        return MerchantContactPointScope.merchant(MERCHANT);
    }

    private static MerchantContactPointScope locationScope() {
        return MerchantContactPointScope.merchantLocation(MERCHANT, LOCATION);
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
