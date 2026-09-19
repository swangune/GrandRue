package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.CorrectMerchantLocationCommand;
import grandrue.merchantprofile.CreateMerchantLocationCommand;
import grandrue.merchantprofile.MerchantLocationLifecycle;
import grandrue.merchantprofile.MerchantLocationRevision;
import grandrue.merchantprofile.MerchantProfileFailureCategory;
import grandrue.merchantprofile.MerchantProfileMutationException;
import grandrue.merchantprofile.PostalAddressInput;
import grandrue.merchantprofile.RetireMerchantLocationCommand;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantLocationAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final Instant NOW =
            Instant.parse("2026-08-30T07:00:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactions;

    @BeforeEach
    void setUp() {
        DataSource source = new DriverManagerDataSource(
                required("GRANDRUE_TEST_POSTGRES_URL"),
                required("GRANDRUE_TEST_POSTGRES_USER"),
                required("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );
        transactions = new DataSourceTransactionManager(source);
        Flyway.configure().dataSource(source).locations("classpath:db/migration")
                .load().migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(source),
                SQLDialect.POSTGRES
        );
        dsl.execute("truncate table merchant_location_original_address_line, "
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
    void creates_exact_current_location_and_retains_address_provenance() {
        MerchantLocationRevision created = authority().create(
                create("request-1", address(" gb ", " 10 High Street ")),
                context("controller-a", true)
        );

        assertEquals(1, created.revisionNumber());
        assertEquals(MerchantLocationLifecycle.ACTIVE, created.lifecycle());
        assertEquals("GB", created.addressEvidence()
                .normalizedAddress().countryCode());
        assertEquals(" gb ", created.addressEvidence()
                .originalInput().countryCode());
        assertEquals("controller-rel-a", created.controllerRelationshipIdentity());
        assertEquals(created, authority().current(
                MERCHANT,
                "location-high-street"
        ).orElseThrow());
        assertEquals(created, authority().revision(
                created.revisionIdentity()
        ).orElseThrow());
    }

    @Test
    void correction_advances_current_without_rewriting_history() {
        var authority = authority();
        var first = authority.create(
                create("request-1", address("GB", "10 Hgh Street")),
                context("controller-a", true)
        );
        var corrected = authority.correct(
                correct(first, "request-2", address("GB", "10 High Street")),
                context("controller-a", true)
        );

        assertEquals(2, corrected.revisionNumber());
        assertEquals(Optional.of(first.revisionIdentity()),
                corrected.predecessorRevisionIdentity());
        assertEquals("10 High Street", corrected.addressEvidence()
                .normalizedAddress().addressLines().getFirst());
        assertEquals(first, authority.revision(
                first.revisionIdentity()
        ).orElseThrow());
    }

    @Test
    void retirement_is_terminal_and_preserves_address_history() {
        var authority = authority();
        var first = authority.create(
                create("request-1", address("GB", "10 High Street")),
                context("controller-a", true)
        );
        var retired = authority.retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        "location-high-street",
                        first.revisionIdentity(),
                        "request-2",
                        "merchant-approved-retirement",
                        "controller-a",
                        NOW.plusSeconds(2)
                ),
                context("controller-a", true)
        );

        assertEquals(MerchantLocationLifecycle.RETIRED, retired.lifecycle());
        assertEquals(first.addressEvidence(), retired.addressEvidence());
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.correct(
                                correct(retired, "request-3",
                                        address("GB", "10 High Street")),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void exact_retry_survives_later_change_and_changed_intent_conflicts() {
        var authority = authority();
        var create = create("request-1", address("GB", "10 High Street"));
        var first = authority.create(create, context("controller-a", true));
        authority.correct(
                correct(first, "request-2", address("GB", "10 High Street West")),
                context("controller-a", true)
        );

        assertEquals(first, authority.create(create, context("controller-a", true)));
        assertEquals(
                MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.create(
                                create("request-1", address("GB", "11 High Street")),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void requires_authenticated_current_controller_and_open_account() {
        var command = create("request-1", address("GB", "10 High Street"));
        assertFailure(command, context("controller-a", false),
                MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED);
        assertFailure(
                new CreateMerchantLocationCommand(
                        MERCHANT,
                        "location-high-street",
                        Optional.of("High Street"),
                        address("GB", "10 High Street"),
                        Optional.empty(),
                        "request-staff",
                        "merchant-approved-location",
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
    void concurrent_corrections_from_one_revision_commit_at_most_once()
            throws Exception {
        var authority = authority();
        var first = authority.create(
                create("request-1", address("GB", "10 High Street")),
                context("controller-a", true)
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(
                    ready, start, correct(first, "request-a",
                            address("GB", "10 High Street A"))
            ));
            var b = executor.submit(() -> afterBarrier(
                    ready, start, correct(first, "request-b",
                            address("GB", "10 High Street B"))
            ));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantLocationRevision.class::isInstance).count());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantProfileMutationException.class::isInstance)
                    .map(MerchantProfileMutationException.class::cast)
                    .filter(failure -> failure.category()
                            == MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT)
                    .count());
        }
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_location_revision"))
        ));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            CorrectMerchantLocationCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return authority().correct(command, context("controller-a", true));
        } catch (MerchantProfileMutationException failure) {
            return failure;
        }
    }

    private void assertFailure(
            CreateMerchantLocationCommand command,
            TrustedExecutionContext context,
            MerchantProfileFailureCategory category
    ) {
        assertEquals(category, assertThrows(
                MerchantProfileMutationException.class,
                () -> authority().create(command, context)
        ).category());
    }

    private JooqMerchantLocationAuthority authority() {
        return new JooqMerchantLocationAuthority(dsl, transactions);
    }

    private static CreateMerchantLocationCommand create(
            String request,
            PostalAddressInput address
    ) {
        return new CreateMerchantLocationCommand(
                MERCHANT,
                "location-high-street",
                Optional.of("High Street"),
                address,
                Optional.empty(),
                request,
                "merchant-approved-location",
                "controller-a",
                NOW
        );
    }

    private static CorrectMerchantLocationCommand correct(
            MerchantLocationRevision expected,
            String request,
            PostalAddressInput address
    ) {
        return new CorrectMerchantLocationCommand(
                MERCHANT,
                expected.locationIdentity(),
                expected.revisionIdentity(),
                Optional.of("High Street"),
                address,
                Optional.empty(),
                request,
                "merchant-approved-correction",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber())
        );
    }

    private static PostalAddressInput address(String country, String line) {
        return new PostalAddressInput(
                country,
                List.of(line),
                Optional.empty(),
                Optional.of("Swansea"),
                Optional.of("Wales"),
                Optional.of("SA1 1AA"),
                Optional.empty()
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
