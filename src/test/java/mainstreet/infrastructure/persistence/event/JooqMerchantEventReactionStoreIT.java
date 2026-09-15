package mainstreet.infrastructure.persistence.event;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.event.*;
import org.flywaydb.core.Flyway;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.datasource.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class JooqMerchantEventReactionStoreIT {
    private static final Instant T0 = Instant.parse("2026-09-06T00:00:00Z");
    private static final EventReactionContractIdentity CONTRACT = new EventReactionContractIdentity("commercial", "baseline");
    private DSLContext dsl;
    private DataSourceTransactionManager transactions;

    @BeforeEach void setup() {
        var source = new DriverManagerDataSource(System.getenv("MAINSTREET_TEST_POSTGRES_URL"),
                System.getenv("MAINSTREET_TEST_POSTGRES_USER"), System.getenv("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        transactions = new DataSourceTransactionManager(source);
        dsl.execute("truncate table merchant_event_reaction");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('receipt-merchant-a'), ('receipt-merchant-b') on conflict do nothing");
    }
    private JooqMerchantEventReactionStore store() { return new JooqMerchantEventReactionStore(dsl, transactions); }
    private static MerchantEventReactionReceipt receipt(String event, EventReactionContractIdentity contract, Instant at) {
        return new MerchantEventReactionReceipt(new EventReactionIdentity(event, contract),
                new EventReactionContractAffinity(contract, "reaction-r1"),
                new EventContractAffinity(new EventContractIdentity("merchant-account", "established"), "event-r1"),
                new MerchantScope("receipt-merchant-a"), "baseline/receipt-merchant-a", at);
    }
    private static MerchantEventReactionReceipt receipt() { return receipt("event-1", CONTRACT, T0); }

    @Test void receipt_survives_recreation_and_redelivery_preserves_first_acceptance() {
        var original = receipt();
        assertEquals(original, store().accept(original));
        assertEquals(original, store().accept(receipt("event-1", CONTRACT, T0.plusSeconds(60))));
        assertEquals(original, store().receipt(original.identity()).orElseThrow());
        assertEquals(List.of(original), store().pending(original.contractAffinity(), 10));
        assertTrue(store().pending(new EventReactionContractAffinity(CONTRACT, "other-release"), 10).isEmpty());
    }
    @Test void conflicting_source_release_scope_or_downstream_intent_cannot_replace_a_receipt() {
        var r = store().accept(receipt());
        var changed = List.of(
                new MerchantEventReactionReceipt(r.identity(), new EventReactionContractAffinity(CONTRACT, "r2"),
                        r.sourceEventContract(), r.merchantScope(), r.downstreamLogicalIntentReference(), T0),
                new MerchantEventReactionReceipt(r.identity(), r.contractAffinity(),
                        new EventContractAffinity(r.sourceEventContract().contractIdentity(), "s2"), r.merchantScope(), r.downstreamLogicalIntentReference(), T0),
                new MerchantEventReactionReceipt(r.identity(), r.contractAffinity(), r.sourceEventContract(),
                        new MerchantScope("receipt-merchant-b"), r.downstreamLogicalIntentReference(), T0),
                new MerchantEventReactionReceipt(r.identity(), r.contractAffinity(), r.sourceEventContract(),
                        r.merchantScope(), "different-intent", T0));
        for (var candidate : changed) assertThrows(IllegalStateException.class, () -> store().accept(candidate));
        assertEquals(r, store().receipt(r.identity()).orElseThrow());
    }
    @Test void acknowledging_one_consumer_neither_completes_another_consumer_nor_another_event() {
        var first = store().accept(receipt());
        var otherConsumer = store().accept(receipt("event-1", new EventReactionContractIdentity("projection", "refresh"), T0));
        var otherEvent = store().accept(receipt("event-2", CONTRACT, T0));
        store().acknowledge(new EventReactionAcknowledgement(first.identity(), "baseline-1", T0.plusSeconds(1)));
        assertEquals(List.of(otherEvent), store().pending(first.contractAffinity(), 10));
        assertEquals(List.of(otherConsumer), store().pending(otherConsumer.contractAffinity(), 10));
        assertTrue(store().acknowledgement(otherConsumer.identity()).isEmpty());
    }
    @Test void concurrent_acceptance_converges_on_one_durable_logical_reaction() throws Exception {
        var start = new CountDownLatch(1);
        try (var workers = Executors.newFixedThreadPool(2)) {
            Future<MerchantEventReactionReceipt> a = workers.submit(() -> { start.await(); return store().accept(receipt()); });
            Future<MerchantEventReactionReceipt> b = workers.submit(() -> { start.await(); return store().accept(receipt()); });
            start.countDown();
            assertEquals(a.get(20, TimeUnit.SECONDS), b.get(20, TimeUnit.SECONDS));
        }
        assertEquals(1, dsl.fetchCount(DSL.table("merchant_event_reaction")));
    }
    @Test void lost_acknowledgement_replay_returns_original_evidence_and_conflicting_outcomes_fail() {
        var r = receipt();
        assertThrows(IllegalStateException.class, () -> store().acknowledge(new EventReactionAcknowledgement(r.identity(), "outcome", T0)));
        store().accept(r);
        var first = new EventReactionAcknowledgement(r.identity(), "baseline-1", T0.plusSeconds(1));
        assertEquals(first, store().acknowledge(first));
        assertEquals(first, store().acknowledge(new EventReactionAcknowledgement(r.identity(), "baseline-1", T0.plusSeconds(50))));
        assertThrows(IllegalStateException.class, () -> store().acknowledge(
                new EventReactionAcknowledgement(r.identity(), "different-baseline", T0.plusSeconds(50))));
        assertEquals(first, store().acknowledgement(r.identity()).orElseThrow());
        assertEquals(r, store().receipt(r.identity()).orElseThrow());
    }
    @Test void enclosing_transaction_rollback_does_not_leak_receipt_or_acknowledgement() {
        var transaction = new org.springframework.transaction.support.TransactionTemplate(transactions);
        transaction.executeWithoutResult(status -> {
            store().accept(receipt());
            store().acknowledge(new EventReactionAcknowledgement(receipt().identity(), "baseline-1", T0));
            status.setRollbackOnly();
        });
        assertTrue(store().receipt(receipt().identity()).isEmpty());
        assertTrue(store().acknowledgement(receipt().identity()).isEmpty());
    }

    @Test void database_constraints_reject_partial_acknowledgement_and_blank_provenance() {
        var r = store().accept(receipt());
        assertThrows(DataAccessException.class, () -> dsl.execute("update merchant_event_reaction set outcome_reference = 'outcome'"));
        assertThrows(DataAccessException.class, () -> dsl.execute("update merchant_event_reaction set source_semantic_release = ' '"));
        assertTrue(store().acknowledgement(r.identity()).isEmpty());
        assertEquals(r, store().receipt(r.identity()).orElseThrow());
    }
}
