package grandrue.infrastructure.persistence.commercial;

import grandrue.application.StandingFreeEventReactionContract;
import grandrue.application.StandingFreeFromMerchantAccountEstablishedHandler;
import grandrue.application.StandingFreeMerchantAccountEstablishedReaction;
import grandrue.application.TrustedPlatformHumanPrincipal;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.infrastructure.persistence.event.JooqMerchantEventReactionStore;
import grandrue.infrastructure.persistence.merchantaccount.JooqMerchantAccountBootstrapStore;
import grandrue.infrastructure.persistence.merchantaccount.JooqMerchantAccountEstablishmentPublicationOutbox;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.RegisteredScheduledEventReactionExecutionAuthority;
import grandrue.semantic.event.EventReactionAcknowledgement;
import grandrue.semantic.event.EventReactionContractAffinity;
import grandrue.semantic.event.EventReactionIdentity;
import grandrue.semantic.event.MerchantEventReactionReceipt;
import grandrue.semantic.event.MerchantEventReactionStore;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandingFreeMerchantAccountEstablishedReactionIT {
    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final Instant REACTION_AT = Instant.parse("2026-09-12T09:00:00Z");
    private static final CommercialEntitlementIdentity ENQUIRY =
            new CommercialEntitlementIdentity("entitlement-enquiry");

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
    void durable_owner_source_and_registered_scheduled_principal_compose_to_exact_acknowledged_baseline() {
        MerchantAccountEstablishedOccurrence occurrence = establishOccurrence();

        StandingFreeBaseline first = reaction(
                new JooqMerchantEventReactionStore(dsl, transactionManager),
                ordinaryHandler()).react(occurrence);
        StandingFreeBaseline duplicateAfterRestart = reaction(
                new JooqMerchantEventReactionStore(dsl, transactionManager),
                recoveringHandler()).react(occurrence);

        assertEquals(first, duplicateAfterRestart);
        assertEquals(T0, first.effectiveFrom());
        assertEquals("establishment-1",
                first.originatingMerchantAccountEstablishmentIdentity());
        assertEquals("free-r7", first.freePlanRevisionIdentity());
        assertEquals(Set.of(ENQUIRY), first.entitlementSnapshot());
        var identity = reactionIdentity(occurrence);
        var durableReactions = new JooqMerchantEventReactionStore(dsl, transactionManager);
        assertEquals(first.baselineIdentity(),
                durableReactions.acknowledgement(identity).orElseThrow().outcomeReference());
        assertTrue(durableReactions.pending(StandingFreeEventReactionContract.AFFINITY, 10).isEmpty());
        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("merchant_event_reaction"));
    }

    @Test
    void caller_supplied_event_identity_cannot_create_scope_receipt_or_business_truth() {
        MerchantAccountEstablishedOccurrence authoritative = establishOccurrence();
        var forgedIdentity = new MerchantAccountEstablishedOccurrence(
                authoritative.eventIdentity() + "-forged",
                authoritative.contractAffinity(),
                authoritative.fact());
        var forgedScope = new MerchantAccountEstablishedOccurrence(
                authoritative.eventIdentity(),
                authoritative.contractAffinity(),
                new grandrue.merchantaccount.MerchantAccountEstablished(
                        authoritative.fact().establishmentIdentity(),
                        new grandrue.application.MerchantScope("merchant-forged"),
                        authoritative.fact().logicalEstablishmentRequestIdentity(),
                        authoritative.fact().occurredAt()));

        var reaction = reaction(
                new JooqMerchantEventReactionStore(dsl, transactionManager),
                ordinaryHandler());

        assertThrows(IllegalStateException.class, () -> reaction.react(forgedIdentity));
        assertThrows(IllegalStateException.class, () -> reaction.react(forgedScope));

        assertEquals(0, count("standing_free_baseline"));
        assertEquals(0, count("merchant_event_reaction"));
    }

    @Test
    void acknowledgement_failure_leaves_pending_reaction_and_retry_recovers_committed_owner_outcome() {
        MerchantAccountEstablishedOccurrence occurrence = establishOccurrence();
        var durableStore = new JooqMerchantEventReactionStore(dsl, transactionManager);

        assertThrows(IllegalStateException.class, () -> reaction(
                new FailBeforeAcknowledgementStore(durableStore),
                ordinaryHandler()).react(occurrence));

        StandingFreeBaseline committed = new JooqStandingFreeBaselineStore(dsl, transactionManager)
                .baselineFor(occurrence.fact().merchantScope()).orElseThrow();
        assertEquals(List.of(durableStore.receipt(reactionIdentity(occurrence)).orElseThrow()),
                durableStore.pending(StandingFreeEventReactionContract.AFFINITY, 10));
        assertTrue(durableStore.acknowledgement(reactionIdentity(occurrence)).isEmpty());

        StandingFreeBaseline recovered = reaction(
                new JooqMerchantEventReactionStore(dsl, transactionManager),
                recoveringHandler()).react(occurrence);

        assertEquals(committed, recovered);
        assertEquals(committed.baselineIdentity(),
                new JooqMerchantEventReactionStore(dsl, transactionManager)
                        .acknowledgement(reactionIdentity(occurrence))
                        .orElseThrow().outcomeReference());
        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("merchant_event_reaction"));
    }

    private StandingFreeMerchantAccountEstablishedReaction reaction(
            MerchantEventReactionStore reactionStore,
            StandingFreeFromMerchantAccountEstablishedHandler handler) {
        return new StandingFreeMerchantAccountEstablishedReaction(
                new JooqMerchantAccountEstablishmentPublicationOutbox(dsl),
                StandingFreeEventReactionContract::registry,
                new RegisteredScheduledEventReactionExecutionAuthority(Map.of(
                        StandingFreeEventReactionContract.IDENTITY,
                        new ExecutionPrincipal("scheduled/standing-free"))),
                reactionStore,
                handler,
                Clock.fixed(REACTION_AT, ZoneOffset.UTC));
    }

    private StandingFreeFromMerchantAccountEstablishedHandler ordinaryHandler() {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> new StandardPlanRevision(
                        StandardPlanLevel.FREE, "free-r7", Set.of(ENQUIRY)),
                new JooqStandingFreeBaselineStore(dsl, transactionManager),
                ignored -> "standing-free-1");
    }

    private StandingFreeFromMerchantAccountEstablishedHandler recoveringHandler() {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> { throw new AssertionError("Recovery must not query the catalogue"); },
                new JooqStandingFreeBaselineStore(dsl, transactionManager),
                ignored -> { throw new AssertionError("Recovery must reuse committed identity"); });
    }

    private MerchantAccountEstablishedOccurrence establishOccurrence() {
        var bootstrap = new JooqMerchantAccountBootstrapStore(
                dsl,
                transactionManager,
                Clock.fixed(T0, ZoneOffset.UTC),
                () -> "establishment-1",
                () -> "publication-1");
        bootstrap.establishIfAbsent(
                "request-1", new TrustedPlatformHumanPrincipal("identity-1"));
        return new JooqMerchantAccountEstablishmentPublicationOutbox(dsl)
                .intentForEstablishment("establishment-1")
                .orElseThrow()
                .registeredOccurrence(MerchantAccountEstablishedEventContract.registry())
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

    private static final class FailBeforeAcknowledgementStore
            implements MerchantEventReactionStore {
        private final MerchantEventReactionStore delegate;

        private FailBeforeAcknowledgementStore(MerchantEventReactionStore delegate) {
            this.delegate = delegate;
        }

        @Override public MerchantEventReactionReceipt accept(MerchantEventReactionReceipt candidate) {
            return delegate.accept(candidate);
        }
        @Override public Optional<MerchantEventReactionReceipt> receipt(EventReactionIdentity identity) {
            return delegate.receipt(identity);
        }
        @Override public List<MerchantEventReactionReceipt> pending(
                EventReactionContractAffinity affinity, int limit) {
            return delegate.pending(affinity, limit);
        }
        @Override public EventReactionAcknowledgement acknowledge(EventReactionAcknowledgement candidate) {
            throw new IllegalStateException("acknowledgement unavailable");
        }
        @Override public Optional<EventReactionAcknowledgement> acknowledgement(
                EventReactionIdentity identity) {
            return delegate.acknowledgement(identity);
        }
    }
}
