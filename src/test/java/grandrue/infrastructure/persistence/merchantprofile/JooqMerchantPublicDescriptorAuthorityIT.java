package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.MerchantProfileFailureCategory;
import grandrue.merchantprofile.MerchantProfileMutationException;
import grandrue.merchantprofile.MerchantPublicDescriptor;
import grandrue.merchantprofile.MerchantPublicDescriptorMutationCommand;
import grandrue.merchantprofile.MerchantPublicDescriptorRevision;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantPublicDescriptorAuthorityIT {

    private static final Instant NOW =
            Instant.parse("2026-08-28T22:00:00Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        dsl.execute("truncate table merchant_public_descriptor_revision");
    }

    @Test
    void establishment_is_durable_and_independent_per_merchant() {
        JooqMerchantPublicDescriptorAuthority authority = authority();

        MerchantPublicDescriptorRevision acme = authority.establish(
                establish("merchant-acme", "Acme", "request-acme")
        );
        MerchantPublicDescriptorRevision beta = authority.establish(
                establish("merchant-beta", "Beta", "request-beta")
        );

        assertEquals(1, acme.revision());
        assertEquals(1, beta.revision());
        assertEquals(
                acme,
                authority().current(new MerchantScope("merchant-acme"))
                        .orElseThrow()
        );
        assertEquals(
                acme,
                authority().revision(
                        new MerchantScope("merchant-acme"),
                        1
                ).orElseThrow()
        );
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_public_descriptor_revision"))
        ));
    }

    @Test
    void revision_changes_current_state_without_rewriting_history() {
        JooqMerchantPublicDescriptorAuthority authority = authority();
        MerchantPublicDescriptorRevision original = authority.establish(
                establish("merchant-acme", "Acme", "request-1")
        );

        MerchantPublicDescriptorRevision revised = authority.revise(
                revise("merchant-acme", "Acme Updated", 1, "request-2")
        );

        assertEquals(2, revised.revision());
        assertEquals(
                "Acme Updated",
                authority.current(new MerchantScope("merchant-acme"))
                        .orElseThrow()
                        .descriptor()
                        .displayName()
        );
        assertEquals(
                original,
                authority.revision(
                        new MerchantScope("merchant-acme"),
                        1
                ).orElseThrow()
        );
    }

    @Test
    void stale_expected_revision_conflicts_without_mutation() {
        JooqMerchantPublicDescriptorAuthority authority = authority();
        authority.establish(establish(
                "merchant-acme",
                "Acme",
                "request-1"
        ));
        authority.revise(revise(
                "merchant-acme",
                "Acme Updated",
                1,
                "request-2"
        ));

        MerchantProfileMutationException conflict = assertThrows(
                MerchantProfileMutationException.class,
                () -> authority.revise(revise(
                        "merchant-acme",
                        "Stale",
                        1,
                        "request-stale"
                ))
        );

        assertEquals(
                MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                conflict.category()
        );
        assertEquals(2, authority.current(
                new MerchantScope("merchant-acme")
        ).orElseThrow().revision());
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_public_descriptor_revision"))
        ));
    }

    @Test
    void retry_returns_the_exact_committed_revision_after_later_changes() {
        JooqMerchantPublicDescriptorAuthority authority = authority();
        authority.establish(establish(
                "merchant-acme",
                "Acme",
                "request-1"
        ));
        MerchantPublicDescriptorMutationCommand requestTwo = revise(
                "merchant-acme",
                "Acme Two",
                1,
                "request-2"
        );
        MerchantPublicDescriptorRevision second =
                authority.revise(requestTwo);
        authority.revise(revise(
                "merchant-acme",
                "Acme Three",
                2,
                "request-3"
        ));

        assertEquals(second, authority.revise(requestTwo));
        assertEquals(3, authority.current(
                new MerchantScope("merchant-acme")
        ).orElseThrow().revision());
    }

    @Test
    void request_identity_reuse_with_different_intent_is_rejected() {
        JooqMerchantPublicDescriptorAuthority authority = authority();
        authority.establish(establish(
                "merchant-acme",
                "Acme",
                "request-1"
        ));

        MerchantProfileMutationException conflict = assertThrows(
                MerchantProfileMutationException.class,
                () -> authority.establish(establish(
                        "merchant-beta",
                        "Beta",
                        "request-1"
                ))
        );

        assertEquals(
                MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                conflict.category()
        );
        assertTrue(authority.current(
                new MerchantScope("merchant-beta")
        ).isEmpty());
    }

    @Test
    void concurrent_revisions_from_one_expected_revision_commit_at_most_once()
            throws Exception {
        JooqMerchantPublicDescriptorAuthority authority = authority();
        authority.establish(establish(
                "merchant-acme",
                "Acme",
                "request-1"
        ));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> first = executor.submit(() -> reviseAfterBarrier(
                    ready,
                    start,
                    revise(
                            "merchant-acme",
                            "Acme A",
                            1,
                            "request-a"
                    )
            ));
            Future<Object> second = executor.submit(() -> reviseAfterBarrier(
                    ready,
                    start,
                    revise(
                            "merchant-acme",
                            "Acme B",
                            1,
                            "request-b"
                    )
            ));
            ready.await();
            start.countDown();

            List<Object> outcomes = List.of(first.get(), second.get());
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(MerchantPublicDescriptorRevision.class::isInstance)
                            .count()
            );
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(MerchantProfileMutationException.class::isInstance)
                            .map(MerchantProfileMutationException.class::cast)
                            .filter(failure ->
                                    failure.category()
                                            == MerchantProfileFailureCategory
                                                    .PROFILE_REVISION_CONFLICT)
                            .count()
            );
        }

        assertEquals(2, authority.current(
                new MerchantScope("merchant-acme")
        ).orElseThrow().revision());
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("merchant_public_descriptor_revision"))
        ));
    }

    private Object reviseAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            MerchantPublicDescriptorMutationCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return authority().revise(command);
        } catch (MerchantProfileMutationException failure) {
            return failure;
        }
    }

    private JooqMerchantPublicDescriptorAuthority authority() {
        return new JooqMerchantPublicDescriptorAuthority(
                dsl,
                transactionManager
        );
    }

    private static MerchantPublicDescriptorMutationCommand establish(
            String merchant,
            String name,
            String request
    ) {
        return command(merchant, name, Optional.empty(), request);
    }

    private static MerchantPublicDescriptorMutationCommand revise(
            String merchant,
            String name,
            long expectedRevision,
            String request
    ) {
        return command(
                merchant,
                name,
                Optional.of(expectedRevision),
                request
        );
    }

    private static MerchantPublicDescriptorMutationCommand command(
            String merchant,
            String name,
            Optional<Long> expectedRevision,
            String request
    ) {
        return new MerchantPublicDescriptorMutationCommand(
                new MerchantPublicDescriptor(
                        new MerchantScope(merchant),
                        name,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(name + " description")
                ),
                expectedRevision,
                request,
                "merchant-approved-profile",
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: " + name
            );
        }
        return value;
    }
}
