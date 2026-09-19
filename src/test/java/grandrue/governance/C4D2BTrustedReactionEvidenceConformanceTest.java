package grandrue.governance;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Keeps C4D2B closure evidence, programme graph and later frontier navigation aligned. */
class C4D2BTrustedReactionEvidenceConformanceTest {
    private static final Path EVIDENCE = Path.of("docs/development",
            "imp-08c-c4d2b-trusted-reaction-conformance-2026-09-12.md");
    private static final Path HISTORY = Path.of("docs/development",
            "implementation-status-history-2026-09-14-pre-c2b-closure.md");

    @Test
    void exact_source_principal_owner_and_acknowledgement_proof_is_local() throws Exception {
        String evidence = Files.readString(EVIDENCE);
        for (String required : List.of(
                "MerchantAccountEstablishedOccurrenceAuthority",
                "RegisteredScheduledEventReactionExecutionAuthority",
                "StandingFreeMerchantAccountEstablishedReaction",
                "StandingFreeMerchantAccountEstablishedReactionIT",
                "Receipt-as-permission failed",
                "PostgreSQL 18.6 / Flyway V64",
                "1,234 unit/governance tests plus 422 PostgreSQL integration tests",
                "zero failures, zero errors and zero skipped",
                "MS-PROT-065 v1.1 §38")) {
            assertTrue(evidence.contains(required), required);
        }
    }

    @Test
    void c4d_closure_is_preserved_as_later_frontier_work_advances() throws Exception {
        String graph = Files.readString(Path.of("docs/development",
                "implementation-programme-state.json"));
        String controller = Files.readString(Path.of("IMPLEMENTATION.md"));
        String history = Files.readString(HISTORY);
        String frontier = Files.readString(Path.of("docs/development",
                "imp-08c-live-frontier-v17-2026-09-06.md"));
        for (String record : List.of(graph, history, frontier)) {
            assertTrue(record.contains("imp-08c-c4d2b-trusted-reaction-conformance-2026-09-12.md"));
            assertTrue(record.contains("IMP-08C-C2B"));
        }
        assertTrue(graph.contains("\"id\":\"IMP-08C-C4D2B\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(graph.contains("\"id\":\"IMP-08C-C4D2\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(graph.contains("\"id\":\"IMP-08C-C4D\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(graph.contains("\"id\":\"IMP-08C-C2B\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(graph.contains("\"id\":\"IMP-08C-C3\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(graph.contains("imp-08c-c3-attempt-ledger-checkpoint-2026-09-14.md"));
        assertTrue(graph.contains("imp-08c-c3-rollout-ordering-closure-2026-09-19.md"));
        assertTrue(controller.contains("id: IMP-08C"));
        assertTrue(controller.contains("canonical_graph: docs/development/implementation-programme-state.json"));
        assertTrue(history.contains("`IMP-08C-C2B — Registered work owner binding/current revalidation` — **READY**"));
    }
}
