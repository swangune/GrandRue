package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.CreateMerchantExternalPresenceLinkCommand;
import grandrue.merchantprofile.MerchantExternalPresenceExposure;
import grandrue.merchantprofile.MerchantExternalPresenceLifecycle;
import grandrue.merchantprofile.MerchantExternalPresenceLinkAuthority;
import grandrue.merchantprofile.MerchantExternalPresenceLinkRevision;
import grandrue.merchantprofile.MerchantProfileFailureCategory;
import grandrue.merchantprofile.MerchantProfileMutationException;
import grandrue.merchantprofile.RetireMerchantExternalPresenceLinkCommand;
import grandrue.merchantprofile.UpdateMerchantExternalPresenceLinkCommand;
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

class JooqMerchantExternalPresenceLinkAuthorityIT {
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-acme");
    private static final String PRESENCE = "presence-instagram";
    private static final Instant NOW =
            Instant.parse("2026-08-30T13:00:00Z");

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
        dsl.execute("truncate table current_merchant_external_presence_link, "
                + "merchant_external_presence_link_revision, "
                + "merchant_controller_relationship, merchant_account cascade");
        dsl.execute("insert into merchant_account (merchant_identifier) "
                + "values ('merchant-acme')");
        dsl.execute("insert into merchant_controller_relationship "
                + "(controller_relationship_identifier, merchant_identifier, "
                + "identity_identifier, lifecycle) values "
                + "('controller-rel-a','merchant-acme','controller-a','ACTIVE')");
    }

    @Test
    void creates_exact_merchant_scoped_presence_and_retains_provenance() {
        MerchantExternalPresenceLinkRevision created = authority().create(
                create("request-1", "INSTAGRAM", "https://example.com/acme"),
                context("controller-a", true)
        );

        assertEquals(1, created.revisionNumber());
        assertEquals(MerchantExternalPresenceLifecycle.ACTIVE,
                created.lifecycle());
        assertEquals("INSTAGRAM", created.platformKind());
        assertEquals("https://example.com/acme", created.publicUrl());
        assertEquals(MerchantExternalPresenceExposure.PUBLIC,
                created.exposure());
        assertEquals("controller-rel-a",
                created.controllerRelationshipIdentity());
        assertEquals(created, authority().current(MERCHANT, PRESENCE)
                .orElseThrow());
        assertEquals(created, authority().revision(created.revisionIdentity())
                .orElseThrow());
    }

    @Test
    void update_and_retirement_advance_current_without_rewriting_history() {
        MerchantExternalPresenceLinkAuthority authority = authority();
        var first = authority.create(
                create("request-1", "INSTAGRAM", "https://example.com/acme"),
                context("controller-a", true)
        );
        var updated = authority.update(
                update(first, "request-2", "YOUTUBE", "https://video.example/acme"),
                context("controller-a", true)
        );
        var retired = authority.retire(
                retire(updated, "request-3"),
                context("controller-a", true)
        );

        assertEquals(Optional.of(first.revisionIdentity()),
                updated.predecessorRevisionIdentity());
        assertEquals("YOUTUBE", updated.platformKind());
        assertEquals(first, authority.revision(first.revisionIdentity())
                .orElseThrow());
        assertEquals(MerchantExternalPresenceLifecycle.RETIRED,
                retired.lifecycle());
        assertEquals(updated.publicUrl(), retired.publicUrl());
        assertEquals(
                MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.update(
                                update(retired, "request-4", "YOUTUBE",
                                        "https://video.example/new"),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void exact_retry_survives_later_change_and_changed_intent_conflicts() {
        MerchantExternalPresenceLinkAuthority authority = authority();
        var command = create(
                "request-1", "INSTAGRAM", "https://example.com/acme"
        );
        var first = authority.create(command, context("controller-a", true));
        authority.update(
                update(first, "request-2", "INSTAGRAM",
                        "https://example.com/acme-new"),
                context("controller-a", true)
        );

        assertEquals(first,
                authority.create(command, context("controller-a", true)));
        assertEquals(
                MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                assertThrows(MerchantProfileMutationException.class, () ->
                        authority.create(
                                create("request-1", "LINKEDIN",
                                        "https://example.com/acme"),
                                context("controller-a", true)
                        )
                ).category()
        );
    }

    @Test
    void requires_authenticated_current_controller_and_open_account() {
        var command = create(
                "request-1", "INSTAGRAM", "https://example.com/acme"
        );
        assertFailure(command, context("controller-a", false),
                MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED);
        assertFailure(
                new CreateMerchantExternalPresenceLinkCommand(
                        MERCHANT,
                        PRESENCE,
                        "INSTAGRAM",
                        "https://example.com/acme",
                        MerchantExternalPresenceExposure.PUBLIC,
                        "request-staff",
                        "merchant-approved-presence",
                        "staff-b",
                        NOW
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
                create("request-1", "INSTAGRAM", "https://example.com/acme"),
                context("controller-a", true)
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> afterBarrier(
                    ready, start, update(first, "request-a", "INSTAGRAM",
                            "https://example.com/a")
            ));
            var b = executor.submit(() -> afterBarrier(
                    ready, start, update(first, "request-b", "INSTAGRAM",
                            "https://example.com/b")
            ));
            ready.await();
            start.countDown();
            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(MerchantExternalPresenceLinkRevision.class::isInstance)
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
                DSL.table(DSL.name("merchant_external_presence_link_revision"))
        ));
    }

    private Object afterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            UpdateMerchantExternalPresenceLinkCommand command
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
            CreateMerchantExternalPresenceLinkCommand command,
            TrustedExecutionContext context,
            MerchantProfileFailureCategory category
    ) {
        assertEquals(category, assertThrows(
                MerchantProfileMutationException.class,
                () -> authority().create(command, context)
        ).category());
    }

    private MerchantExternalPresenceLinkAuthority authority() {
        return new JooqMerchantExternalPresenceLinkAuthority(dsl, transactions);
    }

    private static CreateMerchantExternalPresenceLinkCommand create(
            String request,
            String platformKind,
            String publicUrl
    ) {
        return new CreateMerchantExternalPresenceLinkCommand(
                MERCHANT,
                PRESENCE,
                platformKind,
                publicUrl,
                MerchantExternalPresenceExposure.PUBLIC,
                request,
                "merchant-approved-presence",
                "controller-a",
                NOW.plusSeconds(1)
        );
    }

    private static UpdateMerchantExternalPresenceLinkCommand update(
            MerchantExternalPresenceLinkRevision expected,
            String request,
            String platformKind,
            String publicUrl
    ) {
        return new UpdateMerchantExternalPresenceLinkCommand(
                expected.merchantScope(),
                expected.presenceIdentity(),
                expected.revisionIdentity(),
                platformKind,
                publicUrl,
                expected.exposure(),
                request,
                "merchant-approved-presence-update",
                "controller-a",
                NOW.plusSeconds(expected.revisionNumber() + 1)
        );
    }

    private static RetireMerchantExternalPresenceLinkCommand retire(
            MerchantExternalPresenceLinkRevision expected,
            String request
    ) {
        return new RetireMerchantExternalPresenceLinkCommand(
                expected.merchantScope(),
                expected.presenceIdentity(),
                expected.revisionIdentity(),
                request,
                "merchant-approved-presence-retirement",
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
