package grandrue.infrastructure.persistence.commercial;

import grandrue.application.TrustedPlatformExecutionContext;
import grandrue.commercial.*;
import grandrue.runtime.ExecutionPrincipal;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/** MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md, §§7–13 — publication authority, operation, history and recovery. */
class JooqCommercialCatalogueStoreIT {
    private static final Instant T0 = Instant.parse("2026-09-14T10:00:00Z");
    private static final TrustedPlatformExecutionContext CALLER =
            new TrustedPlatformExecutionContext(new ExecutionPrincipal("fixture-publisher"));
    private final AtomicReference<Instant> now = new AtomicReference<>(T0);
    private DSLContext dsl;
    private DataSourceTransactionManager transactions;
    private JooqCommercialCatalogueStore store;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(System.getenv("GRANDRUE_TEST_POSTGRES_URL"),
                System.getenv("GRANDRUE_TEST_POSTGRES_USER"), System.getenv("GRANDRUE_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        transactions = new DataSourceTransactionManager(source);
        dsl.execute("truncate commercial_catalogue_head, commercial_catalogue_publication");
        dsl.execute("insert into commercial_catalogue_head (singleton) values (true)");
        store = adapter(admission());
    }

    @Test
    void complete_manifest_and_receipt_survive_store_recreation_and_acknowledgement_loss() {
        var request = request("one", Optional.empty());
        var first = store.publish(request, CALLER);
        now.set(T0.plusSeconds(10));
        var recreated = adapter(admission());
        assertEquals(first, recreated.publish(request, CALLER));
        assertEquals(first, recreated.exactGeneration("one").orElseThrow());
        assertEquals(request.manifest(), first.manifest());
        assertEquals(T0, first.publishedAt());
        assertEquals(1, count());
    }

    @Test
    void approved_initial_rollout_becomes_ready_only_after_committed_postgres_publication() {
        var admitted = adapter(new InitialStandardCommercialCataloguePublicationAdmission(
                context -> context.equals(CALLER)));
        var rollout = new InitialStandardCommercialCatalogueRollout(admitted);

        assertResolution(
                CatalogueResolutionException.Reason.NOT_ESTABLISHED,
                rollout::requireOrdinaryMerchantAccountPathReady);

        var publication = rollout.publishInitial("initial-standard-catalogue", CALLER);

        assertEquals("standard-commercial-catalogue@1",
                publication.manifest().revision().catalogueRevisionIdentifier());
        assertEquals(Optional.empty(), publication.predecessor());
        assertEquals(T0, publication.publishedAt());
        assertDoesNotThrow(rollout::requireOrdinaryMerchantAccountPathReady);

        var recreated = adapter(new InitialStandardCommercialCataloguePublicationAdmission(
                context -> context.equals(CALLER)));
        assertEquals(publication,
                recreated.exactGeneration("standard-commercial-catalogue@1").orElseThrow());
        assertDoesNotThrow(new InitialStandardCommercialCatalogueRollout(recreated)
                ::requireOrdinaryMerchantAccountPathReady);
        assertEquals(1, count());
    }

    @Test
    void storage_backed_selection_uses_half_open_intervals_and_retained_snapshots() {
        var first = store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(10));
        var second = store.publish(request("two", Optional.of("one")), CALLER);
        assertEquals(first, store.effectiveAt(T0));
        assertEquals(first, store.effectiveAt(second.publishedAt().minusNanos(1)));
        assertEquals(second, store.effectiveAt(second.publishedAt()));
        assertEquals(first.manifest().revision().freePlan(), store.effectiveFreePlanRevisionAt(T0));
        assertTrue(store.exactGeneration("missing").isEmpty());
    }

    @Test
    void coverage_and_future_failures_never_fabricate_a_free_revision() {
        assertResolution(CatalogueResolutionException.Reason.NOT_ESTABLISHED, () -> store.effectiveAt(T0));
        store.publish(request("one", Optional.empty()), CALLER);
        assertResolution(CatalogueResolutionException.Reason.HISTORY_NOT_COVERED, () -> store.effectiveAt(T0.minusNanos(1)));
        assertResolution(CatalogueResolutionException.Reason.FUTURE_UNSUPPORTED, () -> store.effectiveAt(T0.plusNanos(1)));
    }

    @Test
    void changed_request_input_conflicts_instead_of_replaying_or_retargeting() {
        var request = request("one", Optional.empty());
        store.publish(request, CALLER);
        var changed = new CommercialCataloguePublicationRequest(request.requestIdentifier(),
                request("two", Optional.empty()).manifest(), Optional.empty());
        assertPublication(CataloguePublicationException.Reason.IDENTITY_CONFLICT, () -> store.publish(changed, CALLER));
        var changedPredecessor = new CommercialCataloguePublicationRequest(request.requestIdentifier(), request.manifest(), Optional.of("one"));
        assertPublication(CataloguePublicationException.Reason.IDENTITY_CONFLICT, () -> store.publish(changedPredecessor, CALLER));
        assertEquals(1, count());
    }

    @Test
    void stale_predecessor_and_reused_catalogue_identity_are_rejected() {
        store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(1));
        assertPublication(CataloguePublicationException.Reason.PREDECESSOR_CONFLICT,
                () -> store.publish(request("two", Optional.empty()), CALLER));
        var reused = new CommercialCataloguePublicationRequest("another-request", request("one", Optional.empty()).manifest(), Optional.of("one"));
        assertPublication(CataloguePublicationException.Reason.IDENTITY_CONFLICT, () -> store.publish(reused, CALLER));
        assertEquals(1, count());
    }

    @Test
    void binding_identity_is_checked_against_all_retained_generations_not_only_the_head() {
        var oldest = request("one", Optional.empty()).manifest();
        store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(1));
        store.publish(request("two", Optional.of("one")), CALLER);
        var original = oldest.bindings().iterator().next();
        var changed = new CommercialAccessBinding(original.entitlementIdentity(), original.targetKind(), original.target(),
                "DIFFERENT_PURPOSE", original.governingAuthority(), original.supportingAccessRequirements(), original.newUseAndResidualBoundaryAuthority());
        var grants = Set.of(original.entitlementIdentity());
        var revision = new StandardPlanCatalogueRevision("three",
                new StandardPlanRevision(StandardPlanLevel.FREE, "three-free", grants),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "three-business", grants),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "three-growth", grants));
        var invalid = new CommercialCatalogueManifest(revision, Set.of(changed), oldest.allocationConformanceEvidence(), "fixture-approval");
        now.set(T0.plusSeconds(2));
        assertPublication(CataloguePublicationException.Reason.IDENTITY_CONFLICT,
                () -> store.publish(new CommercialCataloguePublicationRequest("request-three", invalid, Optional.of("two")), CALLER));
        assertEquals(2, count());
        assertEquals(oldest, store.exactGeneration("one").orElseThrow().manifest());
    }

    @Test
    void concurrent_identical_requests_return_one_committed_receipt() throws Exception {
        var start = new CountDownLatch(1);
        var request = request("one", Optional.empty());
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> { await(start); return adapter(admission()).publish(request, CALLER); });
            var b = executor.submit(() -> { await(start); return adapter(admission()).publish(request, CALLER); });
            start.countDown();
            assertEquals(a.get(10, TimeUnit.SECONDS), b.get(10, TimeUnit.SECONDS));
        }
        assertEquals(1, count());
    }

    @Test
    void publication_requires_authorisation_even_for_a_committed_retry() {
        var request = request("one", Optional.empty());
        store.publish(request, CALLER);
        var denied = adapter(new CommercialCataloguePublicationAdmission() {
            public void requirePublicationAuthority(TrustedPlatformExecutionContext context) {
                throw new CataloguePublicationException(CataloguePublicationException.Reason.AUTHORISATION_REJECTED, "fixture denied");
            }
            public void requireApprovedManifest(CommercialCatalogueManifest manifest) { fail("Not authorised"); }
        });
        assertPublication(CataloguePublicationException.Reason.AUTHORISATION_REJECTED, () -> denied.publish(request, CALLER));
        assertEquals(1, count());
    }

    @Test
    void rejected_exact_manifest_admission_commits_nothing() {
        var rejected = adapter(new CommercialCataloguePublicationAdmission() {
            public void requirePublicationAuthority(TrustedPlatformExecutionContext context) { }
            public void requireApprovedManifest(CommercialCatalogueManifest manifest) {
                throw new CataloguePublicationException(CataloguePublicationException.Reason.VALIDATION_REJECTED, "fixture approval mismatch");
            }
        });
        assertPublication(CataloguePublicationException.Reason.VALIDATION_REJECTED,
                () -> rejected.publish(request("one", Optional.empty()), CALLER));
        assertEquals(0, count());
        assertNull(dsl.fetchValue("select catalogue_identifier from commercial_catalogue_head"));
    }

    @Test
    void exact_retry_does_not_reinterpret_old_manifest_using_current_definitions() {
        var request = request("one", Optional.empty());
        var first = store.publish(request, CALLER);
        var recovering = adapter(new CommercialCataloguePublicationAdmission() {
            public void requirePublicationAuthority(TrustedPlatformExecutionContext context) { }
            public void requireApprovedManifest(CommercialCatalogueManifest manifest) { fail("Do not re-admit committed history"); }
        });
        assertEquals(first, recovering.publish(request, CALLER));
    }

    @Test
    void publication_failure_rolls_back_receipt_and_head_together() {
        dsl.execute("alter table commercial_catalogue_head add constraint fixture_reject_head check (catalogue_identifier is null)");
        try {
            assertPublication(CataloguePublicationException.Reason.TECHNICAL_FAILURE,
                    () -> store.publish(request("one", Optional.empty()), CALLER));
            assertEquals(0, count());
        } finally {
            dsl.execute("alter table commercial_catalogue_head drop constraint fixture_reject_head");
        }
        assertEquals(T0, store.publish(request("one", Optional.empty()), CALLER).publishedAt());
    }

    @Test
    void equal_or_regressed_publication_time_fails_without_inventing_future_effectiveness() {
        store.publish(request("one", Optional.empty()), CALLER);
        for (var time : Set.of(T0, T0.minusSeconds(1))) {
            now.set(time);
            assertPublication(CataloguePublicationException.Reason.TECHNICAL_FAILURE,
                    () -> store.publish(request("two", Optional.of("one")), CALLER));
        }
        assertEquals(1, count());
    }

    @Test
    void a_successful_historical_read_fences_later_publication_at_supported_precision() {
        var first = store.publish(request("one", Optional.empty()), CALLER);
        var selection = T0.plusSeconds(10).plusNanos(500);
        now.set(selection.plusSeconds(1));
        assertEquals(first, store.effectiveAt(selection));
        now.set(T0.plusSeconds(10));
        assertPublication(CataloguePublicationException.Reason.TECHNICAL_FAILURE,
                () -> adapter(admission()).publish(request("two", Optional.of("one")), CALLER));
        now.set(T0.plusSeconds(10).plusNanos(1000));
        var second = store.publish(request("two", Optional.of("one")), CALLER);
        assertEquals(first, store.effectiveAt(selection));
        assertTrue(second.publishedAt().isAfter(selection));
    }

    @Test
    void competing_publishers_cannot_both_append_to_one_predecessor() throws Exception {
        store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(1));
        var start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var a = executor.submit(() -> compete(start, "two"));
            var b = executor.submit(() -> compete(start, "three"));
            start.countDown();
            assertEquals(Set.of("committed", "PREDECESSOR_CONFLICT"), Set.of(a.get(10, TimeUnit.SECONDS), b.get(10, TimeUnit.SECONDS)));
        }
        assertEquals(2, count());
    }

    @Test
    void historical_read_waits_for_an_in_flight_publication_and_sees_its_committed_generation() throws Exception {
        store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(10));
        var publishing = new CountDownLatch(1);
        var release = new CountDownLatch(1);
        var blockedAdmission = new CommercialCataloguePublicationAdmission() {
            public void requirePublicationAuthority(TrustedPlatformExecutionContext context) { admission().requirePublicationAuthority(context); }
            public void requireApprovedManifest(CommercialCatalogueManifest manifest) {
                publishing.countDown();
                await(release);
                admission().requireApprovedManifest(manifest);
            }
        };
        try (var executor = Executors.newFixedThreadPool(2)) {
            var publication = executor.submit(() -> adapter(blockedAdmission).publish(request("two", Optional.of("one")), CALLER));
            try {
                assertTrue(publishing.await(10, TimeUnit.SECONDS));
                var reading = executor.submit(() -> adapter(admission()).effectiveAt(T0.plusSeconds(10)));
                awaitCatalogueLockWait();
                assertFalse(reading.isDone());
                release.countDown();
                assertEquals(publication.get(10, TimeUnit.SECONDS), reading.get(10, TimeUnit.SECONDS));
            } finally {
                release.countDown();
            }
        }
    }

    @Test
    void a_read_fence_commits_even_when_the_callers_unrelated_transaction_rolls_back() {
        var first = store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(10));
        var outer = new TransactionTemplate(transactions);
        outer.executeWithoutResult(status -> {
            assertEquals(first, store.effectiveAt(T0.plusSeconds(10)));
            status.setRollbackOnly();
        });
        assertPublication(CataloguePublicationException.Reason.TECHNICAL_FAILURE,
                () -> store.publish(request("two", Optional.of("one")), CALLER));
    }

    @Test
    void publication_waits_for_an_in_flight_read_and_cannot_change_its_selection() throws Exception {
        var first = store.publish(request("one", Optional.empty()), CALLER);
        now.set(T0.plusSeconds(10));
        var reading = new CountDownLatch(1);
        var release = new CountDownLatch(1);
        var pausedReader = new JooqCommercialCatalogueStore(dsl, transactions, admission(), () -> {
            reading.countDown();
            await(release);
            return now.get();
        });
        try (var executor = Executors.newFixedThreadPool(2)) {
            var selected = executor.submit(() -> pausedReader.effectiveAt(T0.plusSeconds(10)));
            try {
                assertTrue(reading.await(10, TimeUnit.SECONDS));
                var publication = executor.submit(() -> {
                    try {
                        adapter(admission()).publish(request("two", Optional.of("one")), CALLER);
                        return "unexpected commit";
                    } catch (CataloguePublicationException failure) {
                        return failure.reason().name();
                    }
                });
                awaitCatalogueLockWait();
                assertFalse(publication.isDone());
                release.countDown();
                assertEquals(first, selected.get(10, TimeUnit.SECONDS));
                assertEquals("TECHNICAL_FAILURE", publication.get(10, TimeUnit.SECONDS));
            } finally {
                release.countDown();
            }
        }
        assertEquals(first, store.effectiveAt(T0.plusSeconds(10)));
        assertEquals(1, count());
    }

    @Test
    void returned_publication_is_not_rolled_back_by_an_unrelated_callers_transaction() {
        var outer = new TransactionTemplate(transactions);
        var result = outer.execute(status -> {
            var publication = store.publish(request("one", Optional.empty()), CALLER);
            status.setRollbackOnly();
            return publication;
        });
        assertEquals(result, adapter(admission()).exactGeneration("one").orElseThrow());
    }

    @Test
    void production_constructor_uses_database_time_inside_the_publication_boundary() {
        Instant before = dsl.select(DSL.field("clock_timestamp()", Instant.class)).fetchSingle().value1();
        var productionClockStore = new JooqCommercialCatalogueStore(dsl, transactions, admission());
        var publication = productionClockStore.publish(request("one", Optional.empty()), CALLER);
        Instant after = dsl.select(DSL.field("clock_timestamp()", Instant.class)).fetchSingle().value1();
        assertFalse(publication.publishedAt().isBefore(before));
        assertFalse(publication.publishedAt().isAfter(after));
        assertEquals(publication, productionClockStore.effectiveAt(publication.publishedAt()));
    }

    @Test
    void missing_head_and_corrupt_payload_fail_explicitly_not_as_empty_history() {
        store.publish(request("one", Optional.empty()), CALLER);
        dsl.execute("update commercial_catalogue_publication set manifest_content = ? where catalogue_identifier = 'one'", new byte[]{0, 0});
        assertResolution(CatalogueResolutionException.Reason.INTEGRITY_FAILURE, () -> store.effectiveAt(T0));
        dsl.execute("delete from commercial_catalogue_head");
        assertResolution(CatalogueResolutionException.Reason.INTEGRITY_FAILURE, () -> store.effectiveAt(T0));
    }

    @Test
    void unavailable_storage_is_a_technical_resolution_failure() {
        dsl.execute("alter table commercial_catalogue_publication rename to fixture_unavailable_catalogue");
        try {
            assertResolution(CatalogueResolutionException.Reason.TECHNICAL_FAILURE, () -> store.effectiveAt(T0));
        } finally {
            dsl.execute("alter table fixture_unavailable_catalogue rename to commercial_catalogue_publication");
        }
    }

    private String compete(CountDownLatch start, String id) throws Exception {
        start.await();
        try {
            adapter(admission()).publish(request(id, Optional.of("one")), CALLER);
            return "committed";
        } catch (CataloguePublicationException failure) {
            return failure.reason().name();
        }
    }

    private void awaitCatalogueLockWait() {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        while (System.nanoTime() < deadline) {
            Boolean waiting = dsl.fetchOne("select exists (select 1 from pg_locks l join pg_stat_activity a using (pid) "
                    + "where not l.granted and a.query like '%commercial_catalogue_head%')").get(0, Boolean.class);
            if (Boolean.TRUE.equals(waiting)) return;
        }
        fail("Historical reader did not reach the PostgreSQL catalogue lock wait");
    }

    private static void await(CountDownLatch latch) {
        try {
            assertTrue(latch.await(10, TimeUnit.SECONDS));
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new AssertionError(interrupted);
        }
    }

    private JooqCommercialCatalogueStore adapter(CommercialCataloguePublicationAdmission admission) {
        return new JooqCommercialCatalogueStore(dsl, transactions, admission, now::get);
    }

    private static CommercialCataloguePublicationAdmission admission() {
        // Explicit isolated test authority only; never registered in production.
        return new CommercialCataloguePublicationAdmission() {
            public void requirePublicationAuthority(TrustedPlatformExecutionContext context) { assertEquals(CALLER, context); }
            public void requireApprovedManifest(CommercialCatalogueManifest manifest) {
                assertEquals("fixture-approval", manifest.approvalProvenanceReference());
            }
        };
    }

    static CommercialCataloguePublicationRequest request(String id, Optional<String> predecessor) {
        var entitlement = new CommercialEntitlementIdentity("fixture-entitlement-" + id);
        var target = new CommercialAccessTarget("fixture-owner", "fixture/read/" + id, "1");
        var support = new CommercialSupportingAccessRequirement(
                new CommercialAccessTarget("fixture-support", "fixture/present", "1"), Set.of(), "fixture-classification");
        var binding = new CommercialAccessBinding(entitlement, CommercialEntitlementTargetKind.OPERATION_ACCESS,
                target, "READ", "fixture-authority", Set.of(support), "fixture-boundary");
        var grants = Set.of(entitlement);
        var revision = new StandardPlanCatalogueRevision(id,
                new StandardPlanRevision(StandardPlanLevel.FREE, id + "-free", grants),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, id + "-business", grants),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, id + "-growth", grants));
        return new CommercialCataloguePublicationRequest("fixture-request-" + id,
                new CommercialCatalogueManifest(revision, Set.of(binding), Set.of("fixture-allocation"), "fixture-approval"), predecessor);
    }

    private int count() { return dsl.fetchCount(DSL.table("commercial_catalogue_publication")); }

    private static void assertPublication(CataloguePublicationException.Reason reason, Runnable action) {
        assertEquals(reason, assertThrows(CataloguePublicationException.class, action::run).reason());
    }

    private static void assertResolution(CatalogueResolutionException.Reason reason, Runnable action) {
        assertEquals(reason, assertThrows(CatalogueResolutionException.class, action::run).reason());
    }
}
