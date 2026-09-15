package mainstreet.infrastructure.persistence.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.merchantprofile.CorrectMerchantLocationCommand;
import mainstreet.merchantprofile.CreateMerchantLocationCommand;
import mainstreet.merchantprofile.ExpectedMerchantLocationExposureChoice;
import mainstreet.merchantprofile.MerchantLocationExposure;
import mainstreet.merchantprofile.MerchantLocationExposureChoiceRevision;
import mainstreet.merchantprofile.MerchantLocationExposureChoiceAuthority;
import mainstreet.merchantprofile.MerchantLocationRevision;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.PostalAddressInput;
import mainstreet.merchantprofile.RetireMerchantLocationCommand;
import mainstreet.merchantprofile.SetMerchantLocationExposureChoiceCommand;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantLocationExposureChoiceAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final Instant NOW =
            Instant.parse("2026-09-02T12:00:00Z");

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
        dsl.execute("truncate table merchant_location_exposure_choice_revision, "
                + "current_merchant_location_exposure_choice, "
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
    void new_location_has_no_choice_and_absence_is_not_fabricated_private_intent() {
        createLocation("location-a", "location-request-a", "10 High Street");

        assertTrue(choiceAuthority().current(MERCHANT, "location-a").isEmpty());
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("merchant_location_exposure_choice_revision"))
        ));
    }

    @Test
    void first_explicit_choice_creates_revision_one_and_exact_current_pointer() {
        createLocation("location-a", "location-request-a", "10 High Street");

        MerchantLocationExposureChoiceRevision first = choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.absent(),
                        MerchantLocationExposure.PUBLIC,
                        "choice-request-1",
                        NOW.plusSeconds(1)
                ),
                context()
        );

        assertEquals(1, first.revisionNumber());
        assertTrue(first.predecessorRevisionIdentity().isEmpty());
        assertEquals(MerchantLocationExposure.PUBLIC, first.exposure());
        assertEquals(first, choiceAuthority().current(MERCHANT, "location-a")
                .orElseThrow());
        assertEquals(first, choiceAuthority().revision(first.revisionIdentity())
                .orElseThrow());
    }

    @Test
    void changing_choice_advances_only_choice_revision_not_location_revision() {
        MerchantLocationRevision location = createLocation(
                "location-a",
                "location-request-a",
                "10 High Street"
        );
        MerchantLocationExposureChoiceRevision first = choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.absent(),
                        MerchantLocationExposure.PUBLIC,
                        "choice-request-1",
                        NOW.plusSeconds(1)
                ),
                context()
        );
        MerchantLocationExposureChoiceRevision second = choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.revision(
                                first.revisionIdentity()
                        ),
                        MerchantLocationExposure.PRIVATE_INTERNAL,
                        "choice-request-2",
                        NOW.plusSeconds(2)
                ),
                context()
        );

        assertEquals(2, second.revisionNumber());
        assertEquals(Optional.of(first.revisionIdentity()),
                second.predecessorRevisionIdentity());
        assertEquals(MerchantLocationExposure.PRIVATE_INTERNAL, second.exposure());
        assertEquals(location, locationAuthority().current(
                MERCHANT,
                "location-a"
        ).orElseThrow());
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_location_revision"))
        ));
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_location_exposure_choice_revision"))
        ));
    }

    @Test
    void same_place_correction_preserves_choice_without_rebinding_or_advancing_it() {
        MerchantLocationRevision firstLocation = createLocation(
                "location-a",
                "location-request-a",
                "10 Hgh Street"
        );
        MerchantLocationExposureChoiceRevision choice = choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.absent(),
                        MerchantLocationExposure.PUBLIC,
                        "choice-request-1",
                        NOW.plusSeconds(1)
                ),
                context()
        );

        MerchantLocationRevision corrected = locationAuthority().correct(
                new CorrectMerchantLocationCommand(
                        MERCHANT,
                        "location-a",
                        firstLocation.revisionIdentity(),
                        Optional.of("High Street"),
                        address("10 High Street"),
                        Optional.empty(),
                        "location-request-b",
                        "merchant-approved-correction",
                        "controller-a",
                        NOW.plusSeconds(2)
                ),
                context()
        );

        assertNotEquals(firstLocation.revisionIdentity(), corrected.revisionIdentity());
        assertEquals(choice, choiceAuthority().current(MERCHANT, "location-a")
                .orElseThrow());
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_location_exposure_choice_revision"))
        ));
    }

    @Test
    void relocation_new_identity_does_not_inherit_existing_location_choice() {
        createLocation("location-a", "location-request-a", "10 High Street");
        choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.absent(),
                        MerchantLocationExposure.PUBLIC,
                        "choice-request-1",
                        NOW.plusSeconds(1)
                ),
                context()
        );

        createLocation("location-b", "location-request-b", "45 Market Street");

        assertTrue(choiceAuthority().current(MERCHANT, "location-b").isEmpty());
        assertEquals(MerchantLocationExposure.PUBLIC,
                choiceAuthority().current(MERCHANT, "location-a")
                        .orElseThrow().exposure());
    }

    @Test
    void stale_expected_choice_conflicts() {
        createLocation("location-a", "location-request-a", "10 High Street");
        MerchantLocationExposureChoiceRevision first = choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.absent(),
                        MerchantLocationExposure.PUBLIC,
                        "choice-request-1",
                        NOW.plusSeconds(1)
                ),
                context()
        );
        choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.revision(
                                first.revisionIdentity()
                        ),
                        MerchantLocationExposure.PRIVATE_INTERNAL,
                        "choice-request-2",
                        NOW.plusSeconds(2)
                ),
                context()
        );

        assertEquals(
                MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        choiceAuthority().set(
                                choice(
                                        "location-a",
                                        ExpectedMerchantLocationExposureChoice.revision(
                                                first.revisionIdentity()
                                        ),
                                        MerchantLocationExposure.PUBLIC,
                                        "choice-request-3",
                                        NOW.plusSeconds(3)
                                ),
                                context()
                        )
                ).category()
        );
    }

    @Test
    void exact_retry_is_idempotent_and_changed_intent_conflicts() {
        createLocation("location-a", "location-request-a", "10 High Street");
        SetMerchantLocationExposureChoiceCommand command = choice(
                "location-a",
                ExpectedMerchantLocationExposureChoice.absent(),
                MerchantLocationExposure.PUBLIC,
                "choice-request-1",
                NOW.plusSeconds(1)
        );
        MerchantLocationExposureChoiceRevision first = choiceAuthority().set(
                command,
                context()
        );

        assertEquals(first, choiceAuthority().set(command, context()));
        assertEquals(
                MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        choiceAuthority().set(
                                choice(
                                        "location-a",
                                        ExpectedMerchantLocationExposureChoice.absent(),
                                        MerchantLocationExposure.PRIVATE_INTERNAL,
                                        "choice-request-1",
                                        NOW.plusSeconds(1)
                                ),
                                context()
                        )
                ).category()
        );
    }

    @Test
    void retired_location_rejects_new_choice_and_historical_public_choice_remains_history_only() {
        MerchantLocationRevision location = createLocation(
                "location-a",
                "location-request-a",
                "10 High Street"
        );
        MerchantLocationExposureChoiceRevision publicChoice = choiceAuthority().set(
                choice(
                        "location-a",
                        ExpectedMerchantLocationExposureChoice.absent(),
                        MerchantLocationExposure.PUBLIC,
                        "choice-request-1",
                        NOW.plusSeconds(1)
                ),
                context()
        );
        locationAuthority().retire(
                new RetireMerchantLocationCommand(
                        MERCHANT,
                        "location-a",
                        location.revisionIdentity(),
                        "location-request-retire",
                        "merchant-approved-retirement",
                        "controller-a",
                        NOW.plusSeconds(2)
                ),
                context()
        );

        assertEquals(publicChoice, choiceAuthority().current(MERCHANT, "location-a")
                .orElseThrow());
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        choiceAuthority().set(
                                choice(
                                        "location-a",
                                        ExpectedMerchantLocationExposureChoice.revision(
                                                publicChoice.revisionIdentity()
                                        ),
                                        MerchantLocationExposure.PRIVATE_INTERNAL,
                                        "choice-request-after-retire",
                                        NOW.plusSeconds(3)
                                ),
                                context()
                        )
                ).category()
        );
    }

    @Test
    void concurrent_first_choices_commit_at_most_once() throws Exception {
        createLocation("location-a", "location-request-a", "10 High Street");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(
                    ready,
                    start,
                    choice(
                            "location-a",
                            ExpectedMerchantLocationExposureChoice.absent(),
                            MerchantLocationExposure.PUBLIC,
                            "choice-request-a",
                            NOW.plusSeconds(1)
                    )
            ));
            var b = executor.submit(() -> afterBarrier(
                    ready,
                    start,
                    choice(
                            "location-a",
                            ExpectedMerchantLocationExposureChoice.absent(),
                            MerchantLocationExposure.PRIVATE_INTERNAL,
                            "choice-request-b",
                            NOW.plusSeconds(1)
                    )
            ));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantLocationExposureChoiceRevision.class::isInstance)
                    .count());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantProfileMutationException.class::isInstance)
                    .map(MerchantProfileMutationException.class::cast)
                    .filter(failure -> failure.category()
                            == MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT)
                    .count());
        }
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_location_exposure_choice_revision"))
        ));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            SetMerchantLocationExposureChoiceCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return choiceAuthority().set(command, context());
        } catch (MerchantProfileMutationException failure) {
            return failure;
        }
    }

    private MerchantLocationRevision createLocation(
            String location,
            String request,
            String line
    ) {
        return locationAuthority().create(
                new CreateMerchantLocationCommand(
                        MERCHANT,
                        location,
                        Optional.of("High Street"),
                        address(line),
                        Optional.empty(),
                        request,
                        "merchant-approved-location",
                        "controller-a",
                        NOW
                ),
                context()
        );
    }

    private static SetMerchantLocationExposureChoiceCommand choice(
            String location,
            ExpectedMerchantLocationExposureChoice expected,
            MerchantLocationExposure exposure,
            String request,
            Instant committedAt
    ) {
        return new SetMerchantLocationExposureChoiceCommand(
                MERCHANT,
                location,
                expected,
                exposure,
                request,
                "merchant-approved-location-exposure",
                "controller-a",
                committedAt
        );
    }

    private JooqMerchantLocationAuthority locationAuthority() {
        return new JooqMerchantLocationAuthority(dsl, transactions);
    }

    private MerchantLocationExposureChoiceAuthority choiceAuthority() {
        return new JooqMerchantLocationExposureChoiceAuthority(dsl, transactions);
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
