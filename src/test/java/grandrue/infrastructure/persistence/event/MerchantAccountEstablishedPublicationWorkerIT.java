package grandrue.infrastructure.persistence.event;

import grandrue.application.MerchantAccountEstablishedPublicationWorker;
import grandrue.application.StandingFreeEventReactionContract;
import grandrue.application.TrustedPlatformHumanPrincipal;
import grandrue.infrastructure.persistence.merchantaccount.JooqMerchantAccountBootstrapStore;
import grandrue.infrastructure.persistence.merchantaccount.JooqMerchantAccountEstablishmentPublicationOutbox;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.merchantaccount.MerchantAccountEstablishedPublicationSource;
import grandrue.semantic.event.EventReactionIdentity;
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
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantAccountEstablishedPublicationWorkerIT {
    private static final Instant OCCURRED_AT = Instant.parse("2026-09-15T09:00:00Z");
    private static final Instant PUBLISHED_AT = Instant.parse("2026-09-15T09:01:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(dataSource).locations("classpath:db/migration")
                .load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl.execute("truncate table merchant_account cascade");
    }

    @Test
    void durable_source_discovery_records_reaction_responsibility_before_publication_without_acknowledgement() {
        MerchantAccountEstablishedOccurrence occurrence = establishOccurrence();
        var outbox = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl);
        var reactions = new JooqMerchantEventReactionStore(dsl, transactionManager);

        assertEquals(1, worker(outbox, reactions, PUBLISHED_AT).runOnce(10));

        assertTrue(outbox.pending(10).isEmpty());
        assertEquals(PUBLISHED_AT,
                outbox.intentForEstablishment("establishment-1")
                        .orElseThrow().publishedAt().orElseThrow());
        EventReactionIdentity identity = reactionIdentity(occurrence);
        var pendingReaction = reactions.pending(StandingFreeEventReactionContract.AFFINITY, 10);
        assertEquals(1, pendingReaction.size());
        assertEquals(identity, pendingReaction.getFirst().identity());
        assertTrue(reactions.acknowledgement(identity).isEmpty());
        assertEquals(1, count("merchant_event_reaction"));
        assertEquals(0, count("standing_free_baseline"));
    }

    @Test
    void failure_after_receipt_before_publication_replays_to_same_responsibility() {
        MerchantAccountEstablishedOccurrence occurrence = establishOccurrence();
        var outbox = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl);
        var reactions = new JooqMerchantEventReactionStore(dsl, transactionManager);
        EventReactionIdentity identity = reactionIdentity(occurrence);

        assertThrows(IllegalStateException.class, () -> worker(
                new FailBeforePublicationSource(outbox),
                reactions,
                PUBLISHED_AT).runOnce(10));

        assertEquals(1, outbox.pending(10).size());
        var firstReceipt = reactions.receipt(identity).orElseThrow();
        assertEquals(PUBLISHED_AT, firstReceipt.acceptedAt());
        assertTrue(reactions.acknowledgement(identity).isEmpty());

        var restartedOutbox = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl);
        var restartedReactions = new JooqMerchantEventReactionStore(dsl, transactionManager);
        assertEquals(1, worker(
                restartedOutbox,
                restartedReactions,
                PUBLISHED_AT.plusSeconds(60)).runOnce(10));

        assertTrue(restartedOutbox.pending(10).isEmpty());
        assertEquals(firstReceipt, restartedReactions.receipt(identity).orElseThrow());
        assertTrue(restartedReactions.acknowledgement(identity).isEmpty());
        assertEquals(1, count("merchant_event_reaction"));
        assertEquals(0, count("standing_free_baseline"));
    }

    @Test
    void unknown_historical_affinity_remains_unpublished_and_undiscovered() {
        establishOccurrence();
        dsl.execute("update merchant_account_establishment_publication_intent "
                + "set event_semantic_release = 'unknown'");
        var outbox = new JooqMerchantAccountEstablishmentPublicationOutbox(dsl);
        var reactions = new JooqMerchantEventReactionStore(dsl, transactionManager);

        assertEquals(0, worker(outbox, reactions, PUBLISHED_AT).runOnce(10));

        assertEquals(1, outbox.pending(10).size());
        assertTrue(outbox.intentForEstablishment("establishment-1")
                .orElseThrow().publishedAt().isEmpty());
        assertEquals(0, count("merchant_event_reaction"));
        assertEquals(0, count("standing_free_baseline"));
    }

    private MerchantAccountEstablishedPublicationWorker worker(
            MerchantAccountEstablishedPublicationSource source,
            JooqMerchantEventReactionStore reactions,
            Instant now) {
        return new MerchantAccountEstablishedPublicationWorker(
                source,
                StandingFreeEventReactionContract::registry,
                reactions,
                Clock.fixed(now, ZoneOffset.UTC));
    }

    private MerchantAccountEstablishedOccurrence establishOccurrence() {
        var bootstrap = new JooqMerchantAccountBootstrapStore(
                dsl,
                transactionManager,
                Clock.fixed(OCCURRED_AT, ZoneOffset.UTC),
                () -> "establishment-1",
                () -> "publication-1");
        bootstrap.establishIfAbsent(
                "request-1", new TrustedPlatformHumanPrincipal("identity-1"));
        return new JooqMerchantAccountEstablishmentPublicationOutbox(dsl)
                .authoritativeOccurrence("establishment-1")
                .orElseThrow();
    }

    private static EventReactionIdentity reactionIdentity(
            MerchantAccountEstablishedOccurrence occurrence) {
        return new EventReactionIdentity(
                occurrence.eventIdentity(), StandingFreeEventReactionContract.IDENTITY);
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required PostgreSQL test environment: " + name);
        }
        return value;
    }

    private static final class FailBeforePublicationSource
            implements MerchantAccountEstablishedPublicationSource {
        private final MerchantAccountEstablishedPublicationSource delegate;

        private FailBeforePublicationSource(MerchantAccountEstablishedPublicationSource delegate) {
            this.delegate = delegate;
        }

        @Override
        public List<PendingPublication> pendingPublications(int limit) {
            return delegate.pendingPublications(limit);
        }

        @Override
        public boolean recordPublished(String publicationIntentIdentifier, Instant publishedAt) {
            throw new IllegalStateException("publication completion unavailable");
        }
    }
}
