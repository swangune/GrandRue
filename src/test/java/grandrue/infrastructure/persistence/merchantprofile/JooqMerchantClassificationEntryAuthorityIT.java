package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.CreateMerchantClassificationEntryCommand;
import grandrue.merchantprofile.MerchantClassificationEntryAuthority;
import grandrue.merchantprofile.MerchantClassificationEntryRevision;
import grandrue.merchantprofile.MerchantClassificationEntryV1;
import grandrue.merchantprofile.MerchantClassificationExposure;
import grandrue.merchantprofile.MerchantClassificationKind;
import grandrue.merchantprofile.MerchantClassificationLifecycle;
import grandrue.merchantprofile.MerchantProfileFailureCategory;
import grandrue.merchantprofile.MerchantProfileMutationException;
import grandrue.merchantprofile.RetireMerchantClassificationEntryCommand;
import grandrue.merchantprofile.UpdateMerchantClassificationEntryCommand;
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

class JooqMerchantClassificationEntryAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final String CLASSIFICATION = "classification-plumber";
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
        dsl.execute("truncate table current_merchant_classification_entry, "
                + "merchant_classification_entry_revision, "
                + "merchant_controller_relationship, merchant_account cascade");
        dsl.execute("insert into merchant_account (merchant_identifier) "
                + "values ('merchant-acme')");
        dsl.execute("insert into merchant_controller_relationship "
                + "(controller_relationship_identifier, merchant_identifier, "
                + "identity_identifier, lifecycle) values "
                + "('controller-rel-a','merchant-acme','controller-a','ACTIVE')");
    }

    @Test
    void creates_normalized_entry_and_retains_exact_provenance() {
        MerchantClassificationEntryRevision created = authority().create(
                create("request-1", CLASSIFICATION,
                        MerchantClassificationKind.CATEGORY,
                        "  Plu\u006d\u0301ber  ",
                        MerchantClassificationExposure.PUBLIC),
                context("controller-a", true)
        );

        assertEquals(1, created.revisionNumber());
        assertEquals(MerchantClassificationLifecycle.ACTIVE,
                created.lifecycle());
        assertEquals("MS_MERCHANT_CLASSIFICATION_ENTRY_V1",
                created.entry().schemaIdentity());
        assertEquals(MerchantClassificationKind.CATEGORY,
                created.entry().kind());
        assertEquals("Plu\u1e3fber", created.entry().merchantApprovedLabel());
        assertEquals(MerchantClassificationExposure.PUBLIC,
                created.entry().exposure());
        assertEquals("controller-rel-a",
                created.controllerRelationshipIdentity());
        assertEquals(created, authority().current(MERCHANT, CLASSIFICATION)
                .orElseThrow());
        assertEquals(created, authority().revision(created.revisionIdentity())
                .orElseThrow());
    }

    @Test
    void keeps_all_kinds_and_equal_labels_as_independent_facts() {
        MerchantClassificationEntryAuthority authority = authority();
        var category = authority.create(create(
                "request-category", "classification-category",
                MerchantClassificationKind.CATEGORY, "Local",
                MerchantClassificationExposure.PUBLIC
        ), context("controller-a", true));
        var tag = authority.create(create(
                "request-tag", "classification-tag",
                MerchantClassificationKind.DISCOVERY_TAG, "Local",
                MerchantClassificationExposure.PRIVATE_INTERNAL
        ), context("controller-a", true));
        var descriptor = authority.create(create(
                "request-descriptor", "classification-descriptor",
                MerchantClassificationKind.CONTEXTUAL_DESCRIPTOR, "Local",
                MerchantClassificationExposure.PUBLIC
        ), context("controller-a", true));

        assertEquals(List.of(
                MerchantClassificationKind.CATEGORY,
                MerchantClassificationKind.DISCOVERY_TAG,
                MerchantClassificationKind.CONTEXTUAL_DESCRIPTOR
        ), List.of(
                category.entry().kind(),
                tag.entry().kind(),
                descriptor.entry().kind()
        ));
        assertEquals(3, dsl.fetchCount(
                DSL.table(DSL.name("current_merchant_classification_entry"))
        ));
    }

    @Test
    void update_preserves_kind_and_retirement_is_terminal() {
        MerchantClassificationEntryAuthority authority = authority();
        var first = authority.create(create(
                "request-1", CLASSIFICATION,
                MerchantClassificationKind.CATEGORY, "Plumber",
                MerchantClassificationExposure.PRIVATE_INTERNAL
        ), context("controller-a", true));
        var updated = authority.update(update(
                first, "request-2", "Plumbing specialist",
                MerchantClassificationExposure.PUBLIC
        ), context("controller-a", true));
        var retired = authority.retire(
                retire(updated, "request-3"),
                context("controller-a", true)
        );

        assertEquals(Optional.of(first.revisionIdentity()),
                updated.predecessorRevisionIdentity());
        assertEquals(first.entry().kind(), updated.entry().kind());
        assertEquals("Plumbing specialist",
                updated.entry().merchantApprovedLabel());
        assertEquals(MerchantClassificationExposure.PUBLIC,
                updated.entry().exposure());
        assertEquals(first, authority.revision(first.revisionIdentity())
                .orElseThrow());
        assertEquals(MerchantClassificationLifecycle.RETIRED,
                retired.lifecycle());
        assertEquals(updated.entry(), retired.entry());
        assertEquals(MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.update(update(
                                retired, "request-4", "Changed",
                                MerchantClassificationExposure.PUBLIC
                        ), context("controller-a", true))
                ).category());
    }

    @Test
    void exact_retry_survives_later_retirement_and_changed_intent_conflicts() {
        MerchantClassificationEntryAuthority authority = authority();
        var command = create(
                "request-1", CLASSIFICATION,
                MerchantClassificationKind.CATEGORY, "Plumber",
                MerchantClassificationExposure.PUBLIC
        );
        var first = authority.create(command, context("controller-a", true));
        authority.retire(retire(first, "request-2"),
                context("controller-a", true));

        assertEquals(first,
                authority.create(command, context("controller-a", true)));
        assertEquals(MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.create(create(
                                "request-1", CLASSIFICATION,
                                MerchantClassificationKind.DISCOVERY_TAG,
                                "Plumber",
                                MerchantClassificationExposure.PUBLIC
                        ), context("controller-a", true))
                ).category());
    }

    @Test
    void requires_authenticated_current_controller_and_open_unsuspended_account() {
        var command = create(
                "request-1", CLASSIFICATION,
                MerchantClassificationKind.CATEGORY, "Plumber",
                MerchantClassificationExposure.PUBLIC
        );
        assertFailure(command, context("controller-a", false),
                MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED);
        assertFailure(new CreateMerchantClassificationEntryCommand(
                        MERCHANT,
                        CLASSIFICATION,
                        new MerchantClassificationEntryV1(
                                MerchantClassificationKind.CATEGORY,
                                "Plumber",
                                MerchantClassificationExposure.PUBLIC
                        ),
                        "request-staff",
                        "merchant-approved-classification",
                        "staff-b",
                        NOW
                ), context("staff-b", true),
                MerchantProfileFailureCategory.AUTHORISATION_REJECTION);
        dsl.execute("insert into merchant_account_suspension "
                + "(suspension_identity, merchant_identifier, "
                + "source_authority_identifier, reason_class_identifier, "
                + "established_at, established_by_identifier, "
                + "release_authority_identifier, provenance_identifier) "
                + "values ('suspension-1','merchant-acme','security-authority', "
                + "'SECURITY',now(),'security-actor','security-authority', "
                + "'security-evidence')");
        assertFailure(command, context("controller-a", true),
                MerchantProfileFailureCategory
                        .MERCHANT_ACCOUNT_OPERATION_RESTRICTED);
    }

    @Test
    void concurrent_updates_from_one_revision_commit_at_most_once()
            throws Exception {
        var first = authority().create(create(
                "request-1", CLASSIFICATION,
                MerchantClassificationKind.CATEGORY, "Plumber",
                MerchantClassificationExposure.PUBLIC
        ), context("controller-a", true));
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(
                    ready, start, update(
                            first, "request-a", "Plumber A",
                            MerchantClassificationExposure.PUBLIC
                    )
            ));
            var b = executor.submit(() -> afterBarrier(
                    ready, start, update(
                            first, "request-b", "Plumber B",
                            MerchantClassificationExposure.PRIVATE_INTERNAL
                    )
            ));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantClassificationEntryRevision.class::isInstance)
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
                DSL.table(DSL.name("merchant_classification_entry_revision"))
        ));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            UpdateMerchantClassificationEntryCommand command
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
            CreateMerchantClassificationEntryCommand command,
            TrustedExecutionContext context,
            MerchantProfileFailureCategory category
    ) {
        assertEquals(category, assertThrows(
                MerchantProfileMutationException.class,
                () -> authority().create(command, context)
        ).category());
    }

    private MerchantClassificationEntryAuthority authority() {
        return new JooqMerchantClassificationEntryAuthority(dsl, transactions);
    }

    private static CreateMerchantClassificationEntryCommand create(
            String request,
            String identity,
            MerchantClassificationKind kind,
            String label,
            MerchantClassificationExposure exposure
    ) {
        return new CreateMerchantClassificationEntryCommand(
                MERCHANT,
                identity,
                new MerchantClassificationEntryV1(kind, label, exposure),
                request,
                "merchant-approved-classification",
                "controller-a",
                NOW.plusSeconds(1)
        );
    }

    private static UpdateMerchantClassificationEntryCommand update(
            MerchantClassificationEntryRevision expected,
            String request,
            String label,
            MerchantClassificationExposure exposure
    ) {
        return new UpdateMerchantClassificationEntryCommand(
                expected.merchantScope(),
                expected.classificationIdentity(),
                expected.revisionIdentity(),
                label,
                exposure,
                request,
                "merchant-approved-classification-update",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber() + 1)
        );
    }

    private static RetireMerchantClassificationEntryCommand retire(
            MerchantClassificationEntryRevision expected,
            String request
    ) {
        return new RetireMerchantClassificationEntryCommand(
                expected.merchantScope(),
                expected.classificationIdentity(),
                expected.revisionIdentity(),
                request,
                "merchant-approved-classification-retirement",
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
