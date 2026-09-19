package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import grandrue.commercial.InitialFullExperienceTrial;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class JooqInitialFullExperienceTrialStoreIT {

    private static final Instant START = Instant.parse("2026-08-24T03:30:00Z");

    private DataSource authoritativeDataSource;
    private DSLContext dsl;
    private JooqInitialFullExperienceTrialStore store;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );

        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        TransactionAwareDataSourceProxy transactionAwareDataSource =
                new TransactionAwareDataSourceProxy(authoritativeDataSource);
        dsl = DSL.using(transactionAwareDataSource, SQLDialect.POSTGRES);

        store = new JooqInitialFullExperienceTrialStore(
                dsl,
                new DataSourceTransactionManager(authoritativeDataSource)
        );

        dsl.deleteFrom(DSL.table(DSL.name("initial_full_experience_trial")))
                .execute();
    }

    @Test
    void first_trial_persists_exact_first_activation_provenance_and_survives_adapter_restart() {
        InitialFullExperienceTrial candidate = trial(
                "trial-1",
                "merchant-a",
                "configuration-1",
                "activation-1",
                START
        );

        InitialFullExperienceTrial established = store.establishIfAbsent(candidate);

        assertEquals(candidate, established);
        assertEquals(1, countTrials());
        assertEquals(
                "configuration-1",
                dsl.select(DSL.field(
                                DSL.name("origin_configuration_revision_identifier"),
                                String.class
                        ))
                        .from(DSL.table(DSL.name("initial_full_experience_trial")))
                        .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                                .eq("merchant-a"))
                        .fetchOne(0, String.class)
        );
        assertEquals(
                "activation-1",
                dsl.select(DSL.field(
                                DSL.name("originating_first_activation_identity"),
                                String.class
                        ))
                        .from(DSL.table(DSL.name("initial_full_experience_trial")))
                        .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                                .eq("merchant-a"))
                        .fetchOne(0, String.class)
        );

        JooqInitialFullExperienceTrialStore restarted =
                new JooqInitialFullExperienceTrialStore(
                        dsl,
                        new DataSourceTransactionManager(authoritativeDataSource)
                );

        assertEquals(candidate, restarted.establishIfAbsent(candidate));
        assertEquals(1, countTrials());
    }

    @Test
    void later_duplicate_attempt_returns_original_trial_without_restart_or_extension() {
        InitialFullExperienceTrial original = trial(
                "trial-original",
                "merchant-a",
                "configuration-1",
                "activation-1",
                START
        );
        InitialFullExperienceTrial laterAttempt = trial(
                "trial-later-attempt",
                "merchant-a",
                "configuration-2",
                "activation-2",
                START.plusSeconds(86_400)
        );

        InitialFullExperienceTrial first = store.establishIfAbsent(original);
        InitialFullExperienceTrial replay = store.establishIfAbsent(laterAttempt);

        assertEquals(original, first);
        assertEquals(original, replay);
        assertEquals(START, replay.startsAt());
        assertEquals(START.plusSeconds(720L * 3_600L), replay.expiresAt());
        assertEquals(1, countTrials());
    }

    @Test
    void concurrent_duplicate_attempts_establish_one_authoritative_trial() throws Exception {
        InitialFullExperienceTrial firstCandidate = trial(
                "trial-race-a",
                "merchant-a",
                "configuration-1",
                "activation-1",
                START
        );
        InitialFullExperienceTrial secondCandidate = trial(
                "trial-race-b",
                "merchant-a",
                "configuration-1",
                "activation-1",
                START
        );
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<InitialFullExperienceTrial> firstFuture = executor.submit(() -> {
                start.await();
                return store.establishIfAbsent(firstCandidate);
            });
            Future<InitialFullExperienceTrial> secondFuture = executor.submit(() -> {
                start.await();
                return store.establishIfAbsent(secondCandidate);
            });

            start.countDown();

            List<InitialFullExperienceTrial> outcomes = List.of(
                    firstFuture.get(),
                    secondFuture.get()
            );

            assertEquals(outcomes.get(0), outcomes.get(1));
        }

        assertEquals(1, countTrials());
    }

    @Test
    void different_merchants_have_independent_initial_trials() {
        InitialFullExperienceTrial first = store.establishIfAbsent(trial(
                "trial-a",
                "merchant-a",
                "configuration-a",
                "activation-a",
                START
        ));
        InitialFullExperienceTrial second = store.establishIfAbsent(trial(
                "trial-b",
                "merchant-b",
                "configuration-b",
                "activation-b",
                START.plusSeconds(30)
        ));

        assertNotEquals(first.merchantScope(), second.merchantScope());
        assertEquals(2, countTrials());
    }

    private int countTrials() {
        return dsl.fetchCount(DSL.table(DSL.name("initial_full_experience_trial")));
    }

    private static InitialFullExperienceTrial trial(
            String trialIdentity,
            String merchantIdentifier,
            String configurationRevisionIdentifier,
            String activationIdentity,
            Instant startsAt
    ) {
        return new InitialFullExperienceTrial(
                trialIdentity,
                new MerchantScope(merchantIdentifier),
                configurationRevisionIdentifier,
                activationIdentity,
                startsAt
        );
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
