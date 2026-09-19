package grandrue.governance;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Keeps frontier evidence local and terminal pointers resolvable.
 * MS-IMPLEMENTATION-RULES-001 v1.9, designs/IMPLEMENTATION-RULES.md,
 * §30.1 — Exact Authority Pointer Contract; §52.19 — Evidence Locality Rule.
 */
class C4CFrontierEvidenceConformanceTest {
    private static final Path REVIEW = Path.of("docs/development/imp-08c-c4c-v17-impact-conformance-2026-09-06.md");
    private static final Path HISTORY = Path.of("docs/development",
            "implementation-status-history-2026-09-14-pre-c2b-closure.md");

    @Test
    void frontier_points_to_current_scoped_evidence_while_historical_evidence_is_preserved() throws Exception {
        String evidence = Files.readString(REVIEW);
        String graph = Files.readString(Path.of("docs/development/imp-08c-live-frontier-v17-2026-09-06.md"));
        String status = Files.readString(Path.of("docs/development/implementation-status.md"));
        String history = Files.readString(HISTORY);
        for (String current : List.of(evidence, graph, history)) {
            assertTrue(current.contains("c357953c2217689be21dbe50b84bc03d3092b0f0"));
            assertTrue(current.contains("34008837843"));
            assertTrue(current.contains("1108 unit/governance + 380 PostgreSQL integration tests"));
            assertTrue(current.contains("v1.7"));
        }
        assertTrue(history.contains("CONFORMING_COMPLETE under current v1.7 evidence"));
        assertTrue(status.contains("| `IMP-08C-C4D2B` | `CONFORMING_COMPLETE` |"));
        assertTrue(status.contains("`IMP-08C-C2B — Registered work owner binding/current revalidation` — **CONFORMING_COMPLETE**"));
        assertTrue(status.contains("`IMP-08C-C3 — Execution failures/outcome ledger` — **IN_PROGRESS**"));
        assertTrue(status.contains("imp-08c-c3-attempt-ledger-checkpoint-2026-09-14.md"));
        assertTrue(graph.contains(REVIEW.getFileName().toString()));
        assertTrue(evidence.contains("Counterevidence and completion falsification"));
        assertTrue(evidence.contains("UNCERTAIN / outside evidence"));
        byte[] historical = Files.readString(Path.of(
                "docs/development/imp-08c-c4c-reaction-contract-conformance-2026-09-06.md"))
                .replace("\r\n", "\n").getBytes(StandardCharsets.UTF_8);
        MessageDigest gitBlob = MessageDigest.getInstance("SHA-1");
        gitBlob.update(("blob " + historical.length + "\0").getBytes(StandardCharsets.UTF_8));
        assertEquals("2de4ac61d9753c18e4079be0453417c214fa9ed6",
                HexFormat.of().formatHex(gitBlob.digest(historical)));
    }

    @Test
    void current_review_terminal_authority_pointers_resolve_to_exact_constituent_headings() throws Exception {
        String evidence = Files.readString(REVIEW);
        var anchors = Map.of(
                "designs/IMPLEMENTATION-RULES.md", List.of(
                        "30.1 Exact Authority Pointer Contract", "30.8 Authority-Change Impact Rule",
                        "47. Implementation Evidence", "52.14 Mandatory implementation-status synchronisation",
                        "52.18 Counterevidence Before Completion", "52.19 Evidence Locality Rule"),
                "designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md",
                List.of("13. EventReactionContract", "14. Registration Is Required", "16. Event Reaction Identity",
                        "17. Per-Reaction Acknowledgement", "23. Reaction Deduplication",
                        "24. Downstream Idempotency Remains Separate", "32. Historical Affinity",
                        "47. Event Receipt Does Not Carry Actor Authority", "48. Merchant Scope"),
                "designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md",
                List.of("1. Governing decision", "2. Semantic ownership", "3. Standing Free baseline fact",
                        "5. Idempotency and duplicate delivery"),
                "designs/MS-PROT-063 — Authentication, Session & Trusted Execution Principal Establishment Model.md",
                List.of("12. System and scheduled principals"),
                "designs/MS-IMP-001.md", List.of("16. IMP-08C — Durable Execution Foundation"));
        for (var anchor : anchors.entrySet()) {
            assertTrue(evidence.contains(anchor.getKey()), anchor.getKey());
            String authority = Files.readString(Path.of(anchor.getKey()));
            for (String heading : anchor.getValue()) {
                assertTrue(authority.contains("# " + heading), heading);
            }
        }
        assertTrue(Files.readString(Path.of("designs/IMPLEMENTATION-RULES.md")).contains("**Version:** 1.9"));
    }
}
