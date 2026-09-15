package mainstreet.application;

import mainstreet.commercial.CommercialEntitlementIdentity;
import mainstreet.commercial.CatalogueResolutionException;
import mainstreet.commercial.PublishedStandardPlanCatalogueRevision;
import mainstreet.commercial.StandardPlanCatalogueHistory;
import mainstreet.commercial.StandardPlanCatalogueRevision;
import mainstreet.commercial.StandardPlanLevel;
import mainstreet.commercial.StandardPlanRevision;
import mainstreet.commercial.StandingFreeBaseline;
import mainstreet.commercial.StandingFreeBaselineStore;
import mainstreet.merchantaccount.MerchantAccountEstablished;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandingFreeFromMerchantAccountEstablishedHandlerTest {

    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");

    @Test
    void binds_standing_free_to_event_time_and_free_revision_effective_at_that_time() {
        AtomicReference<Instant> resolvedAt = new AtomicReference<>();
        InMemoryStore store = new InMemoryStore();
        StandingFreeFromMerchantAccountEstablishedHandler handler =
                new StandingFreeFromMerchantAccountEstablishedHandler(
                        instant -> {
                            resolvedAt.set(instant);
                            return freeRevision("free-r7", "entitlement-enquiry");
                        },
                        store,
                        event -> "standing-free-1"
                );

        StandingFreeBaseline baseline = handler.handle(event());

        assertEquals(T0, resolvedAt.get());
        assertEquals(T0, baseline.effectiveFrom());
        assertEquals("establishment-1", baseline.originatingMerchantAccountEstablishmentIdentity());
        assertEquals("free-r7", baseline.freePlanRevisionIdentity());
        assertEquals(
                Set.of(new CommercialEntitlementIdentity("entitlement-enquiry")),
                baseline.entitlementSnapshot()
        );
    }

    @Test
    void rejects_non_free_revision_from_catalogue_authority() {
        StandingFreeFromMerchantAccountEstablishedHandler handler =
                new StandingFreeFromMerchantAccountEstablishedHandler(
                        instant -> new StandardPlanRevision(
                                StandardPlanLevel.BUSINESS,
                                "business-r1",
                                Set.of()
                        ),
                        new InMemoryStore(),
                        event -> "standing-free-1"
                );

        assertThrows(IllegalStateException.class, () -> handler.handle(event()));
    }

    @Test
    void committed_replay_does_not_require_catalogue_or_another_candidate_identity() {
        var store = new InMemoryStore();
        var original = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> freeRevision("free-r7", "entitlement-enquiry"), store, e -> "baseline-1").handle(event());
        var recovering = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> { throw new AssertionError("Catalogue must not be needed for committed recovery"); },
                store, e -> { throw new AssertionError("Committed identity must be reused"); });
        assertEquals(original, recovering.handle(event()));
    }

    @Test
    void committed_recovery_rejects_changed_establishment_origin_or_time() {
        var store = new InMemoryStore();
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> freeRevision("free-r7", "entitlement-enquiry"), store, e -> "baseline-1");
        var original = handler.handle(event());
        assertThrows(IllegalStateException.class, () -> handler.handle(new MerchantAccountEstablished(
                "other-establishment", event().merchantScope(), "request-1", T0)));
        assertThrows(IllegalStateException.class, () -> handler.handle(new MerchantAccountEstablished(
                "establishment-1", event().merchantScope(), "request-1", T0.plusSeconds(1))));
        assertEquals(original, store.baselineFor(event().merchantScope()).orElseThrow());
    }

    @Test
    void committed_recovery_rejects_a_foreign_merchant_returned_by_the_owner_port() {
        var foreign = new StandingFreeBaseline("baseline-b", new MerchantScope("merchant-b"),
                "establishment-1", T0, "free-r7", Set.of());
        StandingFreeBaselineStore faultyStore = new StandingFreeBaselineStore() {
            public Optional<StandingFreeBaseline> baselineFor(MerchantScope scope) { return Optional.of(foreign); }
            public StandingFreeBaseline establishIfAbsent(StandingFreeBaseline candidate) { return foreign; }
        };
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> freeRevision("free-r7", "entitlement-enquiry"), faultyStore, e -> "baseline-1");
        assertThrows(IllegalStateException.class, () -> handler.handle(event()));
    }

    @Test
    void delayed_establishment_uses_real_history_selection_not_latest_plan() {
        var first = catalogue("first", T0.minusSeconds(1), Optional.empty());
        var second = catalogue("second", T0.plusSeconds(1), Optional.of("first"));
        var history = new StandardPlanCatalogueHistory(T0.plusSeconds(10), List.of(first, second));
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                history, new InMemoryStore(), e -> "baseline-1");

        var baseline = handler.handle(event());

        assertEquals(T0, baseline.effectiveFrom());
        assertEquals(first.revision().freePlan().revisionIdentifier(), baseline.freePlanRevisionIdentity());
        assertEquals(first.revision().freePlan().entitlements(), baseline.entitlementSnapshot());
    }

    @Test
    void uncovered_account_does_not_materialise_a_baseline_or_fabricate_history() {
        var store = new InMemoryStore();
        var history = new StandardPlanCatalogueHistory(T0.plusSeconds(10),
                List.of(catalogue("first", T0.plusSeconds(1), Optional.empty())));
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                history, store, e -> { throw new AssertionError("No candidate is permitted"); });

        var failure = assertThrows(CatalogueResolutionException.class, () -> handler.handle(event()));

        assertEquals(CatalogueResolutionException.Reason.HISTORY_NOT_COVERED, failure.reason());
        assertEquals(Optional.empty(), store.baselineFor(event().merchantScope()));
    }

    @Test
    void missing_catalogue_does_not_create_an_empty_free_baseline() {
        var store = new InMemoryStore();
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                new StandardPlanCatalogueHistory(T0, List.of()), store,
                e -> { throw new AssertionError("No candidate is permitted"); });
        var failure = assertThrows(CatalogueResolutionException.class, () -> handler.handle(event()));
        assertEquals(CatalogueResolutionException.Reason.NOT_ESTABLISHED, failure.reason());
        assertEquals(Optional.empty(), store.baselineFor(event().merchantScope()));
    }

    private static PublishedStandardPlanCatalogueRevision catalogue(
            String id, Instant publishedAt, Optional<String> predecessor) {
        var grants = Set.of(new CommercialEntitlementIdentity("fixture-" + id));
        return new PublishedStandardPlanCatalogueRevision(new StandardPlanCatalogueRevision(id,
                new StandardPlanRevision(StandardPlanLevel.FREE, "free-" + id, grants),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "business-" + id, grants),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "growth-" + id, grants)),
                publishedAt, predecessor);
    }

    private static MerchantAccountEstablished event() {
        return new MerchantAccountEstablished(
                "establishment-1",
                new MerchantScope("merchant-1"),
                "request-1",
                T0
        );
    }

    private static StandardPlanRevision freeRevision(
            String revision,
            String entitlement) {
        return new StandardPlanRevision(
                StandardPlanLevel.FREE,
                revision,
                Set.of(new CommercialEntitlementIdentity(entitlement))
        );
    }

    private static final class InMemoryStore implements StandingFreeBaselineStore {
        private final Map<MerchantScope, StandingFreeBaseline> baselines = new HashMap<>();

        @Override
        public StandingFreeBaseline establishIfAbsent(StandingFreeBaseline candidate) {
            return baselines.computeIfAbsent(candidate.merchantScope(), ignored -> candidate);
        }

        @Override
        public Optional<StandingFreeBaseline> baselineFor(MerchantScope merchantScope) {
            return Optional.ofNullable(baselines.get(merchantScope));
        }
    }
}
