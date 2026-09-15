package mainstreet.infrastructure.persistence.merchantaccount;

import mainstreet.application.TrustedPlatformHumanPrincipal;
import mainstreet.merchantaccount.MerchantAccountEstablished;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantAccountEstablishmentPublicationOutboxIT {

    private static final Instant T0 = Instant.parse("2026-08-24T12:00:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(authoritativeDataSource),
                SQLDialect.POSTGRES
        );
        transactionManager = new DataSourceTransactionManager(authoritativeDataSource);
        dsl.execute("truncate table merchant_account cascade");
    }

    @Test
    void bootstrap_commits_exact_temporal_establishment_and_publication_intent() {
        JooqMerchantAccountBootstrapStore store = store(
                () -> "establishment-1",
                () -> "publication-1"
        );

        var outcome = store.establishIfAbsent(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        String merchantIdentifier = outcome.merchantAccount().merchantIdentifier();
        assertEquals(1, count("merchant_account_establishment"));
        assertEquals(1, count("merchant_account_establishment_publication_intent"));

        assertEquals(
                T0,
                dsl.select(DSL.field(DSL.name("established_at"), Instant.class))
                        .from(DSL.table(DSL.name("merchant_account_establishment")))
                        .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                                .eq(merchantIdentifier))
                        .fetchOne(0, Instant.class)
        );

        MerchantAccountEstablishmentPublicationIntent intent =
                new JooqMerchantAccountEstablishmentPublicationOutbox(dsl)
                        .intentForEstablishment("establishment-1")
                        .orElseThrow();
        MerchantAccountEstablished event = intent.event();

        assertEquals("publication-1", intent.publicationIntentIdentifier());
        assertEquals("establishment-1", event.establishmentIdentity());
        assertEquals(merchantIdentifier, event.merchantScope().merchantIdentifier());
        assertEquals("request-1", event.logicalEstablishmentRequestIdentity());
        assertEquals(T0, event.occurredAt());
        assertTrue(intent.publishedAt().isEmpty());
    }

    @Test
    void replay_preserves_original_establishment_time_without_duplication() {
        JooqMerchantAccountBootstrapStore store = store(
                () -> "establishment-1",
                () -> "publication-1"
        );
        TrustedPlatformHumanPrincipal principal =
                new TrustedPlatformHumanPrincipal("identity-1");

        var first = store.establishIfAbsent("request-1", principal);
        var replay = store.establishIfAbsent("request-1", principal);

        assertEquals(first.merchantAccount(), replay.merchantAccount());
        assertTrue(replay.alreadyEstablished());
        assertEquals(1, count("merchant_account_establishment"));
        assertEquals(1, count("merchant_account_establishment_publication_intent"));
        assertEquals(
                T0,
                dsl.select(DSL.field(DSL.name("established_at"), Instant.class))
                        .from(DSL.table(DSL.name("merchant_account_establishment")))
                        .fetchOne(0, Instant.class)
        );
    }

    @Test
    void publication_intent_failure_rolls_back_entire_second_bootstrap() {
        AtomicInteger establishmentSequence = new AtomicInteger();
        JooqMerchantAccountBootstrapStore store = store(
                () -> "establishment-" + establishmentSequence.incrementAndGet(),
                () -> "publication-fixed"
        );

        store.establishIfAbsent(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        assertThrows(
                DataAccessException.class,
                () -> store.establishIfAbsent(
                        "request-2",
                        new TrustedPlatformHumanPrincipal("identity-2")
                )
        );

        assertEquals(1, count("merchant_account"));
        assertEquals(1, count("merchant_controller_relationship"));
        assertEquals(1, count("merchant_account_establishment_request"));
        assertEquals(1, count("merchant_account_establishment"));
        assertEquals(1, count("merchant_account_establishment_publication_intent"));
    }

    @Test
    void publication_completion_preserves_historical_establishment_evidence() {
        JooqMerchantAccountBootstrapStore store = store(
                () -> "establishment-1",
                () -> "publication-1"
        );
        store.establishIfAbsent(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        JooqMerchantAccountEstablishmentPublicationOutbox outbox =
                new JooqMerchantAccountEstablishmentPublicationOutbox(dsl);
        Instant publishedAt = T0.plusSeconds(30);

        assertTrue(outbox.markPublished("publication-1", publishedAt));
        assertFalse(outbox.markPublished("publication-1", publishedAt.plusSeconds(30)));
        assertTrue(outbox.pending(10).isEmpty());

        MerchantAccountEstablishmentPublicationIntent historical =
                outbox.intentForEstablishment("establishment-1").orElseThrow();
        assertEquals(publishedAt, historical.publishedAt().orElseThrow());
        assertEquals(T0, historical.occurredAt());
        assertEquals(1, count("merchant_account_establishment"));
    }

    @Test
    void registered_occurrence_survives_store_recreation_replay_and_publication_completion() {
        var principal = new TrustedPlatformHumanPrincipal("identity-1");
        store(() -> "establishment-1", () -> "publication-1").establishIfAbsent("request-1", principal);
        var registry = mainstreet.merchantaccount.MerchantAccountEstablishedEventContract.registry();
        var before = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl).pending(10).getFirst()
                .registeredOccurrence(registry).orElseThrow();
        store(() -> "must-not-be-generated", () -> "must-not-be-generated").establishIfAbsent("request-1", principal);
        new JooqMerchantAccountEstablishmentPublicationOutbox(dsl).markPublished("publication-1", T0.plusSeconds(500));
        var after = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl)
                .intentForEstablishment("establishment-1").orElseThrow().registeredOccurrence(registry).orElseThrow();
        assertEquals(before, after);
        assertEquals(T0, after.fact().occurredAt());
        assertEquals(1, count("merchant_account_establishment_publication_intent"));
    }

    @Test
    void missing_or_unknown_historical_affinity_is_not_replaced_by_the_current_contract() {
        store(() -> "establishment-1", () -> "publication-1")
                .establishIfAbsent("request-1", new TrustedPlatformHumanPrincipal("identity-1"));
        var registry = mainstreet.merchantaccount.MerchantAccountEstablishedEventContract.registry();
        dsl.execute("update merchant_account_establishment_publication_intent set event_semantic_release = 'unknown'");
        var unknown = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl).pending(10).getFirst();
        assertTrue(unknown.registeredOccurrence(registry).isEmpty());
        assertEquals("unknown", unknown.eventContractAffinity().orElseThrow().semanticRegistryReleaseIdentifier());
        dsl.execute("update merchant_account_establishment_publication_intent set event_owner_identifier = null, event_contract_identifier = null, event_semantic_release = null");
        var legacy = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl).pending(10).getFirst();
        assertTrue(legacy.eventContractAffinity().isEmpty());
        assertTrue(legacy.registeredOccurrence(registry).isEmpty());
        assertEquals(T0, legacy.event().occurredAt());
    }

    @Test
    void database_rejects_partial_or_blank_event_contract_affinity() {
        store(() -> "establishment-1", () -> "publication-1")
                .establishIfAbsent("request-1", new TrustedPlatformHumanPrincipal("identity-1"));
        assertThrows(DataAccessException.class, () -> dsl.execute(
                "update merchant_account_establishment_publication_intent set event_semantic_release = null"));
        assertThrows(DataAccessException.class, () -> dsl.execute(
                "update merchant_account_establishment_publication_intent set event_contract_identifier = ' '"));
        assertTrue(new JooqMerchantAccountEstablishmentPublicationOutbox(dsl).pending(10).getFirst()
                .registeredOccurrence(mainstreet.merchantaccount.MerchantAccountEstablishedEventContract.registry()).isPresent());
    }

    private JooqMerchantAccountBootstrapStore store(
            java.util.function.Supplier<String> establishmentIdentityFactory,
            java.util.function.Supplier<String> publicationIdentityFactory) {
        return new JooqMerchantAccountBootstrapStore(
                dsl,
                transactionManager,
                Clock.fixed(T0, ZoneOffset.UTC),
                establishmentIdentityFactory,
                publicationIdentityFactory
        );
    }

    private int count(String tableName) {
        return dsl.fetchCount(DSL.table(DSL.name(tableName)));
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
