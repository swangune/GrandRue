package mainstreet.infrastructure.persistence.configuration;

import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationPublication;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.ConfigurationRevisionApproval;
import mainstreet.semantic.configuration.InMemoryConfigurationPublication;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.testing.ConfigurationActivationApprovals;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqConfigurationActivationPublicationOutboxIT {

    private static final Instant ACTIVATED_AT =
            Instant.parse("2026-08-24T04:00:00Z");
    private static final Instant PUBLISHED_AT =
            Instant.parse("2026-08-24T04:01:00Z");
    private static final String PRINCIPAL = "merchant-controller-1";

    private DataSource authoritativeDataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

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
        transactionManager = new DataSourceTransactionManager(authoritativeDataSource);

        clearTables();
    }

    @Test
    void successful_activation_commits_durable_publication_intent_with_exact_fact_affinity() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        publication.publish(first);
        JooqConfigurationReleaseActivation activation = activation(
                publication,
                () -> "publication-1"
        );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request("activate-1", first, Optional.empty())).status()
        );

        JooqConfigurationActivationPublicationOutbox restarted = outbox();
        ConfigurationActivationPublicationIntent intent = restarted
                .intentForActivation("activate-1")
                .orElseThrow();

        assertEquals("publication-1", intent.publicationIntentIdentifier());
        assertEquals("activate-1", intent.activationRequestIdentifier());
        assertEquals("merchant-a", intent.merchantIdentifier());
        assertEquals("configuration-1", intent.configurationRevisionIdentifier());
        assertEquals(first.releaseIdentifier(), intent.releaseIdentifier());
        assertEquals(ACTIVATED_AT, intent.occurredAt());
        assertTrue(intent.publishedAt().isEmpty());
        assertEquals(1, restarted.pending(10).size());
    }

    @Test
    void replay_of_committed_activation_does_not_duplicate_publication_intent() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        publication.publish(first);
        JooqConfigurationReleaseActivation activation = activation(
                publication,
                () -> "publication-1"
        );
        ConfigurationActivationRequest request = request(
                "activate-1",
                first,
                Optional.empty()
        );

        activation.activate(request);
        activation.activate(request);

        assertEquals(1, count("configuration_activation"));
        assertEquals(1, count("configuration_activation_publication_intent"));
    }

    @Test
    void rejected_activation_creates_no_publication_intent() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        JooqConfigurationReleaseActivation activation = activation(
                publication,
                () -> "publication-1"
        );

        ConfigurationActivationStatus status = activation.activate(
                new ConfigurationActivationRequest(
                        "activate-missing",
                        "missing-release",
                        Optional.empty(),
                        PRINCIPAL
                )
        ).status();

        assertEquals(ConfigurationActivationStatus.VALIDATION_REJECTION, status);
        assertEquals(0, count("configuration_activation"));
        assertEquals(0, count("configuration_activation_publication_intent"));
    }

    @Test
    void publication_intent_insert_failure_rolls_back_activation_and_current_pointer() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease merchantA = release("merchant-a", 1, Optional.empty());
        ConfigurationRelease merchantB = release("merchant-b", 1, Optional.empty());
        publication.publish(merchantA);
        publication.publish(merchantB);
        JooqConfigurationReleaseActivation activation = activation(
                publication,
                () -> "fixed-publication-id"
        );

        activation.activate(request("activate-a", merchantA, Optional.empty()));

        assertThrows(
                DataAccessException.class,
                () -> activation.activate(request(
                        "activate-b",
                        merchantB,
                        Optional.empty()
                ))
        );

        assertTrue(activation.current("merchant-b").isEmpty());
        assertTrue(activation.committedActivation("activate-b").isEmpty());
        assertEquals(1, count("configuration_activation"));
        assertEquals(1, count("merchant_current_configuration_activation"));
        assertEquals(1, count("configuration_activation_publication_intent"));
    }

    @Test
    void publication_completion_removes_intent_from_pending_without_erasing_history() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        publication.publish(first);
        activation(publication, () -> "publication-1")
                .activate(request("activate-1", first, Optional.empty()));
        JooqConfigurationActivationPublicationOutbox outbox = outbox();

        assertEquals(1, outbox.pending(10).size());
        assertTrue(outbox.markPublished("publication-1", PUBLISHED_AT));
        assertTrue(outbox.pending(10).isEmpty());

        ConfigurationActivationPublicationIntent persisted = outbox
                .intentForActivation("activate-1")
                .orElseThrow();
        assertEquals(Optional.of(PUBLISHED_AT), persisted.publishedAt());
        assertFalse(outbox.markPublished("publication-1", PUBLISHED_AT.plusSeconds(60)));
        assertEquals(
                Optional.of(PUBLISHED_AT),
                outbox.intentForActivation("activate-1")
                        .orElseThrow()
                        .publishedAt()
        );
    }

    @Test
    void competing_first_activations_create_publication_intent_only_for_the_committed_winner()
            throws Exception {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        ConfigurationRelease competing = release("merchant-a", 2, Optional.empty());
        publication.publish(first);
        publication.publish(competing);
        AtomicInteger publicationSequence = new AtomicInteger();
        JooqConfigurationReleaseActivation activation = activation(
                publication,
                () -> "publication-" + publicationSequence.incrementAndGet()
        );
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<ConfigurationActivationStatus> firstFuture = executor.submit(() -> {
                start.await();
                return activation.activate(request(
                        "activate-first",
                        first,
                        Optional.empty()
                )).status();
            });
            Future<ConfigurationActivationStatus> competingFuture = executor.submit(() -> {
                start.await();
                return activation.activate(request(
                        "activate-competing",
                        competing,
                        Optional.empty()
                )).status();
            });

            start.countDown();

            List<ConfigurationActivationStatus> statuses = List.of(
                    firstFuture.get(),
                    competingFuture.get()
            );
            assertEquals(
                    1,
                    statuses.stream()
                            .filter(status -> status == ConfigurationActivationStatus.SUCCESS)
                            .count()
            );
        }

        assertEquals(1, count("configuration_activation"));
        assertEquals(1, count("configuration_activation_publication_intent"));
        assertEquals(1, outbox().pending(10).size());
    }

    private JooqConfigurationReleaseActivation activation(
            ConfigurationPublication publication,
            Supplier<String> publicationIdentityFactory
    ) {
        return new JooqConfigurationReleaseActivation(
                publication,
                (merchant, revision) -> Optional.of(
                        new ConfigurationRevisionApproval(
                                merchant,
                                revision,
                                PRINCIPAL,
                                ACTIVATED_AT.minusSeconds(60)
                        )
                ),
                ConfigurationActivationApprovals.currentInitialApproval(
                        publication,
                        PRINCIPAL,
                        ACTIVATED_AT.minusSeconds(60)
                ),
                (principal, merchant, revision) -> true,
                Clock.fixed(ACTIVATED_AT, ZoneOffset.UTC),
                dsl,
                transactionManager,
                publicationIdentityFactory
        );
    }

    private JooqConfigurationActivationPublicationOutbox outbox() {
        return new JooqConfigurationActivationPublicationOutbox(dsl);
    }

    private int count(String tableName) {
        return dsl.fetchCount(DSL.table(DSL.name(tableName)));
    }

    private void clearTables() {
        dsl.deleteFrom(DSL.table(DSL.name("configuration_activation_publication_intent")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("merchant_current_configuration_activation")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("configuration_activation")))
                .execute();
    }

    private static ConfigurationActivationRequest request(
            String requestIdentifier,
            ConfigurationRelease release,
            Optional<String> expectedCurrent
    ) {
        return new ConfigurationActivationRequest(
                requestIdentifier,
                release.releaseIdentifier(),
                expectedCurrent,
                PRINCIPAL
        );
    }

    private static ConfigurationRelease release(
            String merchantIdentifier,
            long version,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                Set.of(),
                baseConfigurationIdentifier
        );
        return new ConfigurationRelease(
                "release-" + merchantIdentifier + "-" + version,
                configuration,
                model(merchantIdentifier, version),
                "mainstreet-compiler-1",
                ACTIVATED_AT.minusSeconds(120)
        );
    }

    private static ExecutableMerchantModel model(
            String merchantIdentifier,
            long version
    ) {
        return new ExecutableMerchantModel(
                merchantIdentifier,
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(),
                List.of()
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
