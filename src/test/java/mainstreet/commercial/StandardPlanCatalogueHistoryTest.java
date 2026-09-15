package mainstreet.commercial;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static mainstreet.commercial.CatalogueResolutionException.Reason.*;
import static org.junit.jupiter.api.Assertions.*;

/** MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md, §§6, 9–11, 13. */
class StandardPlanCatalogueHistoryTest {
    private static final Instant P0 = Instant.parse("2026-09-14T10:00:00Z");
    private static final Instant P1 = P0.plusSeconds(60);
    private static final Instant OBSERVED_AT = P1.plusSeconds(60);

    @Test
    void selects_half_open_windows_at_exact_nanosecond_boundaries() {
        var first = publication("one", P0, Optional.empty());
        var second = publication("two", P1, Optional.of("one"));
        var history = history(first, second);

        assertEquals(first, history.effectiveAt(P0));
        assertEquals(first, history.effectiveAt(P1.minusNanos(1)));
        assertEquals(second, history.effectiveAt(P1));
        assertEquals(second, history.effectiveAt(OBSERVED_AT));
        assertEquals(first.revision().freePlan(), history.effectiveFreePlanRevisionAt(P0));
    }

    @Test
    void exact_lookup_never_substitutes_latest() {
        var first = publication("one", P0, Optional.empty());
        var history = history(first, publication("two", P1, Optional.of("one")));
        assertEquals(Optional.of(first), history.exactRevision("one"));
        assertTrue(history.exactRevision("unknown").isEmpty());
    }

    @Test
    void no_publications_and_uncovered_history_have_distinct_failures() {
        assertReason(NOT_ESTABLISHED, () -> history().effectiveAt(P0));
        assertReason(HISTORY_NOT_COVERED,
                () -> history(publication("one", P0, Optional.empty())).effectiveAt(P0.minusNanos(1)));
    }

    @Test
    void future_of_the_consistent_read_is_never_resolved_under_latest_plan() {
        var history = history(publication("one", P0, Optional.empty()));
        assertReason(FUTURE_UNSUPPORTED, () -> history.effectiveAt(OBSERVED_AT.plusNanos(1)));
    }

    @Test
    void copies_input_and_orders_by_authoritative_publication_time() {
        var first = publication("one", P0, Optional.empty());
        var second = publication("two", P1, Optional.of("one"));
        var input = new ArrayList<>(List.of(second, first));
        var history = new StandardPlanCatalogueHistory(OBSERVED_AT, input);
        input.clear();
        assertEquals(first, history.effectiveAt(P0));
        assertEquals(second, history.effectiveAt(P1));
    }

    @Test
    void rejects_duplicate_publication_instants_and_catalogue_identities() {
        var first = publication("one", P0, Optional.empty());
        assertReason(INTEGRITY_FAILURE, () -> history(first, publication("two", P0, Optional.of("one"))));
        assertReason(INTEGRITY_FAILURE, () -> history(first, publication("one", P1, Optional.of("one"))));
    }

    @Test
    void rejects_missing_predecessors_and_multiple_initial_publications() {
        var first = publication("one", P0, Optional.empty());
        assertReason(INTEGRITY_FAILURE, () -> history(publication("two", P1, Optional.of("one"))));
        assertReason(INTEGRITY_FAILURE, () -> history(first, publication("two", P1, Optional.of("missing"))));
        assertReason(INTEGRITY_FAILURE, () -> history(first, publication("two", P1, Optional.empty())));
    }

    @Test
    void rejects_publication_after_the_consistent_read_instant() {
        assertReason(INTEGRITY_FAILURE,
                () -> history(publication("future", OBSERVED_AT.plusNanos(1), Optional.empty())));
    }

    @Test
    void rejects_reinterpretation_of_a_retained_plan_revision_identity() {
        var first = publication("one", P0, Optional.empty());
        var changedFree = new StandardPlanRevision(StandardPlanLevel.FREE,
                first.revision().freePlan().revisionIdentifier(), Set.of());
        var second = new PublishedStandardPlanCatalogueRevision(
                new StandardPlanCatalogueRevision("two", changedFree,
                        plan(StandardPlanLevel.BUSINESS, "business-two", "two"),
                        plan(StandardPlanLevel.GROWTH, "growth-two", "two")),
                P1, Optional.of("one"));
        assertReason(INTEGRITY_FAILURE, () -> history(first, second));
    }

    @Test
    void unchanged_plan_revisions_may_be_referenced_by_a_later_generation() {
        var first = publication("one", P0, Optional.empty());
        var second = new PublishedStandardPlanCatalogueRevision(new StandardPlanCatalogueRevision(
                "two", first.revision().freePlan(), first.revision().businessPlan(), first.revision().growthPlan()),
                P1, Optional.of("one"));
        assertEquals(second, history(first, second).effectiveAt(P1));
    }

    @Test
    void an_older_read_snapshot_does_not_silently_extend_into_a_later_publication() {
        var first = publication("one", P0, Optional.empty());
        var before = new StandardPlanCatalogueHistory(P1.minusNanos(1), List.of(first));
        var after = history(first, publication("two", P1, Optional.of("one")));
        assertEquals(before.effectiveAt(P0), after.effectiveAt(P0));
        assertReason(FUTURE_UNSUPPORTED, () -> before.effectiveAt(P1));
    }

    private static StandardPlanCatalogueHistory history(PublishedStandardPlanCatalogueRevision... entries) {
        return new StandardPlanCatalogueHistory(OBSERVED_AT, List.of(entries));
    }

    static PublishedStandardPlanCatalogueRevision publication(String id, Instant at, Optional<String> predecessor) {
        return new PublishedStandardPlanCatalogueRevision(new StandardPlanCatalogueRevision(id,
                plan(StandardPlanLevel.FREE, "free-" + id, id),
                plan(StandardPlanLevel.BUSINESS, "business-" + id, id),
                plan(StandardPlanLevel.GROWTH, "growth-" + id, id)), at, predecessor);
    }

    private static StandardPlanRevision plan(StandardPlanLevel level, String id, String entitlement) {
        return new StandardPlanRevision(level, id,
                Set.of(new CommercialEntitlementIdentity("fixture-" + entitlement)));
    }

    private static void assertReason(CatalogueResolutionException.Reason reason, Runnable action) {
        assertEquals(reason, assertThrows(CatalogueResolutionException.class, action::run).reason());
    }
}
