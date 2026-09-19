package grandrue.governance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Protects the MS-IMP-001 dependency chain, IMP-07 macro closure and subsequent eligibility. */
class ImplementationProgrammeGateConformanceTest {

    @Test
    void retained_macro_proofs_and_corrected_current_eligibility_remain_synchronized()
            throws IOException {
        String correction = Files.readString(Path.of(
                "docs", "development",
                "imp-04-macro-closure-correction-2026-08-28.md"
        ));
        assertTrue(correction.contains(
                "Status:** **RESOLVED — REQUIRED CHILD CONFORMING**"
        ));

        String replacementClosure = Files.readString(Path.of(
                "docs", "development",
                "imp-04-merchant-account-scope-identity-trust-closure-v2-2026-08-28.md"
        ));
        assertTrue(replacementClosure.contains(
                "Status:** **CURRENT — CONFORMING_COMPLETE**"
        ));

        String imp05Closure = Files.readString(Path.of(
                "docs", "development",
                "imp-05-establishment-to-active-runtime-closure-2026-08-30.md"
        ));
        assertTrue(imp05Closure.contains(
                "IMP-05 — Merchant Definition, Configuration & Activation: CONFORMING_COMPLETE"
        ));

        String imp06Closure = Files.readString(Path.of(
                "docs", "development",
                "imp-06-read-exposure-transport-spine-closure-2026-09-03.md"
        ));
        assertTrue(imp06Closure.contains(
                "Macro-node:** IMP-06 — Read, Exposure & Transport Spine"
        ));
        assertTrue(imp06Closure.contains("Status:** **CONFORMING_COMPLETE**"));

        String p2aEvidence = Files.readString(Path.of(
                "docs", "development",
                "imp-07-publication-p2a-persistence-conformance-2026-09-04.md"
        ));
        assertTrue(p2aEvidence.contains(
                "Implementation node:** IMP-07-P2A — Durable Opportunity revision/currentness persistence foundation"
        ));

        String p2bEvidence = Files.readString(Path.of(
                "docs", "development",
                "imp-07-publication-p2b-v13-conformance-2026-09-04.md"
        ));
        assertTrue(p2bEvidence.contains(
                "Implementation node:** IMP-07-P2B — Reconstructible material revisions + Publication History"
        ));
        assertTrue(p2bEvidence.contains("33849375998"));

        String p3Evidence = Files.readString(Path.of(
                "docs", "development",
                "imp-07-publication-p3-conformance-2026-09-04.md"
        ));
        assertTrue(p3Evidence.contains(
                "Implementation node:** IMP-07-P3 — Application mutation + transaction + retry idempotency"
        ));
        assertTrue(p3Evidence.contains("f481ec46c4253d0db3dbfa95e397cf596c010860"));
        assertTrue(p3Evidence.contains("33883111122"));
        assertTrue(p3Evidence.contains(
                "CONFORMING_COMPLETE effective when the cycle-closing head"
        ));
        assertTrue(p3Evidence.contains(
                "concurrent same logical deliveries serialize/reconcile to one business effect PASS"
        ));

        String p3ClosureGraph = Files.readString(Path.of(
                "docs", "development",
                "imp-07-graph-refresh-2026-09-04-publication-p3-closure.md"
        ));
        assertTrue(p3ClosureGraph.contains(
                "Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P3 closure effective when this cycle-closing head passes full verification**"
        ));
        assertTrue(p3ClosureGraph.contains("33883111122"));

        String p4Evidence = Files.readString(Path.of(
                "docs", "development",
                "imp-07-publication-p4-conformance-2026-09-05.md"
        ));
        assertTrue(p4Evidence.contains(
                "Implementation node:** IMP-07-P4 — Publication PUBLIC Exposure contracts/evaluation"
        ));
        assertTrue(p4Evidence.contains(
                "published revision rather than the unpublished current revision"
        ));
        assertTrue(p4Evidence.contains(
                "missing current PUBLISHED evidence withholds fail closed"
        ));
        assertTrue(p4Evidence.contains(
                "concurrent published-binding change between authority reads fails closed"
        ));
        assertTrue(p4Evidence.contains(
                "mismatched published-revision affinity is rejected"
        ));
        assertTrue(p4Evidence.contains(
                "P6 Opportunity actionability remains separate"
        ));
        assertTrue(p4Evidence.contains("968 unit/governance tests"));
        assertTrue(p4Evidence.contains("329 PostgreSQL integration tests"));
        assertTrue(p4Evidence.contains("96550fb6e8e04fad233b8d3e9e1193d43d4fb7b8"));
        assertTrue(p4Evidence.contains(
                "CONFORMING_COMPLETE effective when the cycle-closing head"
        ));

        String p4ClosureGraph = Files.readString(Path.of(
                "docs", "development",
                "imp-07-graph-refresh-2026-09-05-publication-p4-closure.md"
        ));
        assertTrue(p4ClosureGraph.contains(
                "P4 closure / P5 readiness effective when the cycle-closing synchronisation head"
        ));
        assertTrue(p4ClosureGraph.contains("859119784657768b3d96a4627237ede66f8907ef"));
        assertTrue(p4ClosureGraph.contains("33947743143"));

        String p5Evidence = Files.readString(Path.of(
                "docs", "development",
                "imp-07-publication-p5-conformance-2026-09-05.md"
        ));
        assertTrue(p5Evidence.contains(
                "Implementation node:** IMP-07-P5 — Request-scoped public Opportunity representation"
        ));
        assertTrue(p5Evidence.contains(
                "CONFORMING_COMPLETE effective when the cycle-closing head"
        ));
        assertTrue(p5Evidence.contains(
                "exact published revision rather than the unpublished current revision supplies public material"
        ));
        assertTrue(p5Evidence.contains("revision identifiers"));
        assertTrue(p5Evidence.contains("internal provenance"));
        assertTrue(p5Evidence.contains(
                "published-binding change during bounded acquisition produces no public material fragment"
        ));
        assertTrue(p5Evidence.contains(
                "published-binding change / republish after acquisition but before E4 resolves WITHHOLD fail closed"
        ));
        assertTrue(p5Evidence.contains("request-binding rebinding is rejected"));
        assertTrue(p5Evidence.contains("Merchant-Scope rebinding is rejected"));
        assertTrue(p5Evidence.contains(
                "P6 Opportunity actionability remains separate"
        ));
        assertTrue(p5Evidence.contains(
                "Participation / Enquiry / transport are not introduced"
        ));
        assertTrue(p5Evidence.contains("3f7958fca49874ffd606f6f360d049e053d31420"));
        assertTrue(p5Evidence.contains("33958778665"));
        assertTrue(p5Evidence.contains("3d2775ec2add3aadbdba4b2736cf716f581bad46"));
        assertTrue(p5Evidence.contains("33959091152"));
        assertTrue(p5Evidence.contains("2fcc452f0b8df452a2eba42cc10824d24ff0039e"));
        assertTrue(p5Evidence.contains("33959274014"));

        String p5ClosureGraph = Files.readString(Path.of(
                "docs", "development",
                "imp-07-graph-refresh-2026-09-05-publication-p5-closure.md"
        ));
        assertTrue(p5ClosureGraph.contains(
                "Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P5 closure / I1 readiness effective when the cycle-closing synchronisation head containing this graph, implementation-status.md and the programme-gate assertion passes full verification**"
        ));
        assertTrue(p5ClosureGraph.contains(
                "P5  request-scoped public Opportunity representation          CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONISATION CI"
        ));
        assertTrue(p5ClosureGraph.contains(
                "I1 owner-qualified Opportunity → enquiry/send-enquiry source  READY ON THE SAME CI"
        ));
        assertTrue(p5ClosureGraph.contains("2fcc452f0b8df452a2eba42cc10824d24ff0039e"));
        assertTrue(p5ClosureGraph.contains("33959274014"));
        assertTrue(p5ClosureGraph.contains(
                "P6 Opportunity actionability remains separate"
        ));
        assertTrue(p5ClosureGraph.contains(
                "Do not begin I1 before this synchronisation gate succeeds"
        ));

        String status = Files.readString(Path.of(
                "docs", "development", "implementation-status-history-2026-09-14-pre-c2b-closure.md"
        ));
        String controller = Files.readString(Path.of("IMPLEMENTATION.md"));
        String currentGraph = Files.readString(Path.of(
                "docs", "development", "implementation-programme-state.json"
        ));
        assertTrue(controller.contains("id: IMP-08C"));
        assertTrue(currentGraph.contains("\"id\":\"IMP-08C-C2B\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(currentGraph.contains("\"id\":\"IMP-08C-C2\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(currentGraph.contains("\"id\":\"IMP-08C-C3\",\"macro\":\"IMP-08C\",\"state\":\"CONFORMING_COMPLETE\""));
        assertTrue(currentGraph.contains("imp-08c-c3-rollout-ordering-closure-2026-09-19.md"));
        assertTrue(controller.contains("canonical_graph: docs/development/implementation-programme-state.json"));
        assertTrue(controller.contains("historical_status_compatibility: docs/development/implementation-status.md"));
        String compatibility = Files.readString(Path.of(
                "docs", "development", "implementation-status.md"
        ));
        assertTrue(compatibility.contains("implementation-status-history-2026-09-14-pre-c2b-closure.md"));
        assertTrue(status.contains(
                "Restored programme target:** `IMP-07 — Publication → Enquiry vertical slice` — **CONFORMING_COMPLETE from retained scoped proof after prerequisite restoration**"
        ));
        assertTrue(status.contains(
                "`IMP-07-P1 — Opportunity Publication lifecycle/currentness domain foundation` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "`IMP-07-P2A — Durable Opportunity revision/currentness persistence foundation` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "`IMP-07-P2B — Reconstructible material revisions + Publication History` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "`IMP-07-P3 — Application mutation + transaction + retry idempotency` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "`IMP-07-P4 — Publication PUBLIC Exposure contracts/evaluation` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "`IMP-07-P5 — Request-scoped public Opportunity representation` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "`IMP-07-I1 — Owner-qualified Opportunity → enquiry/send-enquiry participation source` — **CONFORMING_COMPLETE**"
        ));
        assertTrue(status.contains(
                "Current fine-grained graph:** `docs/development/implementation-programme-state.json`"
        ));
        assertTrue(status.contains("2fcc452f0b8df452a2eba42cc10824d24ff0039e"));
        assertTrue(status.contains("33959274014"));

        String i1Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-i1-participation-conformance-2026-09-05.md"));
        String i1Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-i1-closure.md"));
        for (String record : java.util.List.of(status, i1Evidence, i1Graph)) {
            assertTrue(record.contains("1065904c9e3cabc3a20d2ba7be9296c8e6f7c5b8"));
            assertTrue(record.contains("33981525405"));
            assertTrue(record.contains("I1"));
            assertTrue(record.contains("I2"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(status.contains(
                "`IMP-07-I2 — Concrete Public Interaction Binding proof` — **CONFORMING_COMPLETE**"));
        assertTrue(i1Graph.contains(
                "I1  owner-qualified Opportunity → enquiry/send-enquiry source  CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(i1Graph.contains(
                "I2  concrete Public Interaction Binding proof                 READY ON THE SAME CI"));
        assertTrue(i1Graph.contains("E1  durable Enquiry submission/provenance                     BLOCKED_DEPENDENCY — I2"));
        assertTrue(i1Evidence.contains("MS-PROT-046 v1.2"));
        assertTrue(i1Evidence.contains("MS-PROT-049 v1.4"));
        assertTrue(i1Evidence.contains("988 tests, zero failures/errors/skips"));

        String i2Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-i2-binding-conformance-2026-09-05.md"));
        String i2Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-i2-closure.md"));
        for (String record : java.util.List.of(status, i2Evidence, i2Graph)) {
            assertTrue(record.contains("c499439951d594f310992807805ba97d2d294605"));
            assertTrue(record.contains("33982320477"));
            assertTrue(record.contains("db733f35950d2a22faef6d5dc163358b751bbb18"));
            assertTrue(record.contains("33981821452"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(i2Evidence.contains("996 tests, zero failures/errors/skips"));
        assertTrue(i2Evidence.contains("MS-PROT-049 v1.4"));
        assertTrue(i2Graph.contains(
                "I2  concrete Public Interaction Binding proof                CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(i2Graph.contains(
                "E1  durable Enquiry submission/provenance                    READY ON THE SAME CI"));
        assertTrue(i2Graph.contains("BLOCKED_DEPENDENCY — E1"));
        assertTrue(status.contains(
                "`IMP-07-E1 — Durable Enquiry submission/provenance foundation` — **CONFORMING_COMPLETE**"));

        String e1Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-e1-submission-conformance-2026-09-05.md"));
        String e1Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-e1-closure.md"));
        for (String record : java.util.List.of(status, e1Evidence, e1Graph)) {
            assertTrue(record.contains("83f433e39de709c587cfa32385b7af0151cec53c"));
            assertTrue(record.contains("33983277431"));
            assertTrue(record.contains("b6bff57c580178c8b6ee9c38fd277bb8fa1aaead"));
            assertTrue(record.contains("33982554318"));
            assertTrue(record.contains("1001 unit/governance + 337 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(e1Evidence.contains("MS-PROT-043"));
        assertTrue(e1Evidence.contains("1001 tests, zero failures/errors/skips"));
        assertTrue(e1Graph.contains(
                "E1  durable Enquiry submission/provenance                    CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(e1Graph.contains(
                "E2  Enquiry retry idempotency + transaction proof             READY ON THE SAME CI — E1"));
        assertTrue(e1Graph.contains(
                "E3  stale subject-binding revalidation                       READY ON THE SAME CI — I2 + E1"));
        assertTrue(e1Graph.contains(
                "M1  MERCHANT Enquiry Exposure                               READY ON THE SAME CI — E1 + G0"));
        assertTrue(status.contains(
                "`IMP-07-E2 — Enquiry retry idempotency + transaction proof` — **CONFORMING_COMPLETE**"));

        String e2Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-e2-idempotency-conformance-2026-09-05.md"));
        String e2Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-e2-closure.md"));
        for (String record : java.util.List.of(status, e2Evidence, e2Graph)) {
            assertTrue(record.contains("415ba5c534b5e12ddbdb087583fd87401bd8dcd6"));
            assertTrue(record.contains("33984114714"));
            assertTrue(record.contains("5ee6b62ef963856776d34c7aba41806f2cb9490a"));
            assertTrue(record.contains("33983582172"));
            assertTrue(record.contains("1004 unit/governance + 346 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(e2Evidence.contains("MS-PROT-043"));
        assertTrue(e2Evidence.contains("MS-PROT-059"));
        assertTrue(e2Evidence.contains("1004 tests, zero failures/errors/skips"));
        assertTrue(e2Graph.contains(
                "E2  Enquiry retry idempotency + transaction proof             CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(e2Graph.contains(
                "E3  stale subject-binding revalidation                       READY — I2 + E1"));
        assertTrue(e2Graph.contains(
                "M1  MERCHANT Enquiry Exposure                               READY — E1 + G0"));
        assertTrue(status.contains(
                "`IMP-07-E3 — Stale subject-binding authoritative revalidation` — **CONFORMING_COMPLETE**"));

        String e3Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-e3-revalidation-conformance-2026-09-05.md"));
        String e3Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-e3-closure.md"));
        for (String record : java.util.List.of(status, e3Evidence, e3Graph)) {
            assertTrue(record.contains("b39f4db5dbfff4ed3472d3d47f659d4f436079e7"));
            assertTrue(record.contains("33985246923"));
            assertTrue(record.contains("d25961238b99af5a1edf893495fceeaec41760a1"));
            assertTrue(record.contains("33984462969"));
            assertTrue(record.contains("1015 unit/governance + 351 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(e3Evidence.contains("MS-PROT-043"));
        assertTrue(e3Evidence.contains("MS-PROT-049"));
        assertTrue(e3Evidence.contains("1015 tests, zero failures/errors/skips"));
        assertTrue(e3Evidence.contains("55P03"));
        assertTrue(e3Graph.contains(
                "E3  stale subject-binding revalidation                       CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(e3Graph.contains(
                "M1  MERCHANT Enquiry Exposure                               READY — E1 + G0"));
        assertTrue(status.contains(
                "`IMP-07-M1 — MERCHANT Enquiry Exposure` — **CONFORMING_COMPLETE**"));

        String m1Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-m1-exposure-conformance-2026-09-05.md"));
        String m1Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-m1-closure.md"));
        for (String record : java.util.List.of(status, m1Evidence, m1Graph)) {
            assertTrue(record.contains("8fe221d9acbbbd4e772be9eee5896f9058ffa4e7"));
            assertTrue(record.contains("33986535598"));
            assertTrue(record.contains("cf276ec7142072ed332f59354da672894ccf7a79"));
            assertTrue(record.contains("33985515264"));
            assertTrue(record.contains("1025 unit/governance + 353 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(m1Evidence.contains("MS-PROT-043"));
        assertTrue(m1Evidence.contains("MS-PROT-027"));
        assertTrue(m1Evidence.contains("1025 tests, zero failures/errors/skips"));
        assertTrue(m1Graph.contains(
                "M1  MERCHANT Enquiry Exposure                               CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(m1Graph.contains(
                "M2  merchant Enquiry representation                         READY ON THE SAME CI — M1"));
        assertTrue(status.contains(
                "`IMP-07-M2 — Request-scoped merchant Enquiry representation` — **CONFORMING_COMPLETE**"));

        String m2Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-m2-representation-conformance-2026-09-05.md"));
        String m2Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-m2-closure.md"));
        for (String record : java.util.List.of(status, m2Evidence, m2Graph)) {
            assertTrue(record.contains("7ec5667f845b1e098a44da5e2b9b21c6d6674e2a"));
            assertTrue(record.contains("33987788979"));
            assertTrue(record.contains("8e9fc480f07acaf1b6c83c55cd4a9f7bc1bbbc13"));
            assertTrue(record.contains("33986872166"));
            assertTrue(record.contains("1035 unit/governance + 355 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(m2Evidence.contains("MS-PROT-043"));
        assertTrue(m2Evidence.contains("MS-PROT-027"));
        assertTrue(m2Evidence.contains("1035 tests, zero failures/errors/skips"));
        assertTrue(m2Graph.contains(
                "M2  merchant Enquiry representation                         CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(m2Graph.contains("READY — P5; selected for V1"));
        assertTrue(m2Graph.contains("BLOCKED_DEPENDENCY — required T1/T2/T3 adapters"));
        assertTrue(status.contains(
                "`IMP-07-T1 — Concrete PUBLIC Publication query adapter` — **CONFORMING_COMPLETE**"));

        String t1aEvidence = Files.readString(Path.of("docs", "development",
                "imp-07-t1a-response-conformance-2026-09-05.md"));
        String t1aGraph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-t1a-closure.md"));
        for (String record : java.util.List.of(status, t1aEvidence, t1aGraph)) {
            assertTrue(record.contains("fade9d97cecf4542b968c2145293ef28ce5e9859"));
            assertTrue(record.contains("33988676029"));
            assertTrue(record.contains("a3c919ed67e69695d5412c5af42e12318a31ce9d"));
            assertTrue(record.contains("33988065345"));
            assertTrue(record.contains("1043 unit/governance + 355 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(t1aEvidence.contains("MS-PROT-035"));
        assertTrue(t1aEvidence.contains("MS-PROT-046"));
        assertTrue(t1aEvidence.contains("MS-PROT-027"));
        assertTrue(t1aEvidence.contains("1043 tests, zero failures/errors/skips"));
        assertTrue(t1aGraph.contains(
                "T1A query contract + bounded safe response mapping           CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(t1aGraph.contains("READY ON THE SAME CI — T1A"));
        assertTrue(t1aGraph.contains("BLOCKED_DEPENDENCY — complete T1/T2/T3 adapters"));
        assertTrue(status.contains(
                "`IMP-07-T1A — Query contract + bounded safe response mapping` — **CONFORMING_COMPLETE**"));
        assertTrue(status.contains(
                "`IMP-07-T1B — Trusted PUBLIC query execution and delivery` — **CONFORMING_COMPLETE**"));

        String t1bEvidence = Files.readString(Path.of("docs", "development",
                "imp-07-t1b-delivery-conformance-2026-09-05.md"));
        String t1bGraph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-t1b-closure.md"));
        for (String record : java.util.List.of(status, t1bEvidence, t1bGraph)) {
            assertTrue(record.contains("5cd020b5d60c2ff15241eeb128b3d22868b647af"));
            assertTrue(record.contains("33989663158"));
            assertTrue(record.contains("7ef3aa3b46f5188ff6749b6b0bbd378bd90f530f"));
            assertTrue(record.contains("33988912106"));
            assertTrue(record.contains("1052 unit/governance + 357 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(t1bEvidence.contains("MS-PROT-035"));
        assertTrue(t1bEvidence.contains("MS-PROT-046"));
        assertTrue(t1bEvidence.contains("MS-PROT-027"));
        assertTrue(t1bEvidence.contains("1052 tests, zero failures/errors/skips"));
        assertTrue(t1bEvidence.contains("publication-public-api"));
        assertTrue(t1bGraph.contains(
                "T1B trusted request/query execution + delivery               CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(t1bGraph.contains("CONFORMING_COMPLETE ON THE SAME CI — T1A + T1B"));
        assertTrue(t1bGraph.contains("BLOCKED_DEPENDENCY — complete T2/T3 adapters + integrated path proof"));
        assertTrue(status.contains(
                "`IMP-07-T2 — Concrete PUBLIC Enquiry submission adapter` — **CONFORMING_COMPLETE**"));

        String t2aEvidence = Files.readString(Path.of("docs", "development",
                "imp-07-t2a-general-delivery-conformance-2026-09-05.md"));
        String t2aGraph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-t2a-closure.md"));
        for (String record : java.util.List.of(status, t2aEvidence, t2aGraph)) {
            assertTrue(record.contains("115db6a825beafc5a6cd58cba5a0fbbe37c87332"));
            assertTrue(record.contains("33990790405"));
            assertTrue(record.contains("eca3df9d76e271b4d2c982fbcf3b2c427d743cf2"));
            assertTrue(record.contains("33989939024"));
            assertTrue(record.contains("1061 unit/governance + 361 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(t2aEvidence.contains("MS-PROT-035"));
        assertTrue(t2aEvidence.contains("MS-PROT-043"));
        assertTrue(t2aEvidence.contains("MS-PROT-059"));
        assertTrue(t2aEvidence.contains("1061 tests, zero failures/errors/skips"));
        assertTrue(t2aEvidence.contains("OUTCOME_UNCERTAIN"));
        assertTrue(t2aGraph.contains(
                "T2A merchant-general PUBLIC command delivery                CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(t2aGraph.contains("READY ON THE SAME CI — T2A + I2/E3"));
        assertTrue(t2aGraph.contains("IN_PROGRESS — T2A + T2B"));
        assertTrue(status.contains(
                "`IMP-07-T2A — Merchant-general PUBLIC Enquiry command delivery` — **CONFORMING_COMPLETE**"));
        assertTrue(status.contains(
                "`IMP-07-T2B — Opportunity binding carry-forward and submission` — **CONFORMING_COMPLETE**"));

        String t2bEvidence = Files.readString(Path.of("docs", "development",
                "imp-07-t2b-binding-delivery-conformance-2026-09-05.md"));
        String t2bGraph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-t2b-closure.md"));
        for (String record : java.util.List.of(status, t2bEvidence, t2bGraph)) {
            assertTrue(record.contains("b9702d030b2a3dc9af181e97bf8bdd8ffa073fd2"));
            assertTrue(record.contains("33992025633"));
            assertTrue(record.contains("c092b10944281f5582f484331c0a4ce988a644fd"));
            assertTrue(record.contains("33991054297"));
            assertTrue(record.contains("1069 unit/governance + 366 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(t2bEvidence.contains("MS-PROT-035"));
        assertTrue(t2bEvidence.contains("MS-PROT-043"));
        assertTrue(t2bEvidence.contains("MS-PROT-059"));
        assertTrue(t2bEvidence.contains("1069 tests, zero failures/errors/skips"));
        assertTrue(t2bEvidence.contains("OUTCOME_UNCERTAIN"));
        assertTrue(t2bEvidence.contains("AES-256-GCM"));
        assertTrue(t2bEvidence.contains("not part of logical intent"));
        assertTrue(t2bGraph.contains(
                "T2B Opportunity binding carry-forward and submission        CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(t2bGraph.contains("CONFORMING_COMPLETE ON THE SAME CI — T2A + T2B"));
        assertTrue(t2bGraph.contains("BLOCKED_DEPENDENCY — T3 adapter + integrated path proof"));
        assertTrue(status.contains(
                "`IMP-07-T3 — MERCHANT Enquiry query adapter` — **CONFORMING_COMPLETE**"));

        String t3Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-t3-merchant-query-conformance-2026-09-05.md"));
        String t3Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-t3-closure.md"));
        for (String record : java.util.List.of(status, t3Evidence, t3Graph)) {
            assertTrue(record.contains("26674121a673015ea9155ad5222dd7eaddbb2932"));
            assertTrue(record.contains("33993070421"));
            assertTrue(record.contains("11010d07b76d79b8086a19cfd15c5334750aad63"));
            assertTrue(record.contains("33992305320"));
            assertTrue(record.contains("1079 unit/governance + 370 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(t3Evidence.contains("MS-PROT-035"));
        assertTrue(t3Evidence.contains("MS-PROT-043"));
        assertTrue(t3Evidence.contains("MS-PROT-027"));
        assertTrue(t3Evidence.contains("ADR-014"));
        assertTrue(t3Evidence.contains("1079 tests, zero failures/errors/skips"));
        assertTrue(t3Evidence.contains("enquiry-merchant-api"));
        assertTrue(t3Evidence.contains("submissionRevisionIdentity"));
        assertTrue(t3Graph.contains(
                "T3  MERCHANT Enquiry query adapter                          CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(t3Graph.contains("READY ON THE SAME CI — required owner nodes + T1/T2/T3"));
        assertTrue(status.contains(
                "`IMP-07-V1 — Full Publication → Enquiry integration proof` — **CONFORMING_COMPLETE**"));

        String v1Evidence = Files.readString(Path.of("docs", "development",
                "imp-07-v1-vertical-conformance-2026-09-05.md"));
        String v1Graph = Files.readString(Path.of("docs", "development",
                "imp-07-graph-refresh-2026-09-05-v1-closure.md"));
        String imp07Closure = Files.readString(Path.of("docs", "development",
                "imp-07-publication-enquiry-vertical-slice-closure-2026-09-05.md"));
        for (String record : java.util.List.of(status, v1Evidence, v1Graph, imp07Closure)) {
            assertTrue(record.contains("fccef99cf13789ac9302bd4efe3bec237c61eede"));
            assertTrue(record.contains("33995174577"));
            assertTrue(record.contains("6657a12b20218970312a1f0ace907e36d1db4d08"));
            assertTrue(record.contains("33993321981"));
            assertTrue(record.contains("1079 unit/governance + 374 PostgreSQL integration tests"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(v1Evidence.contains("MS-IMP-001"));
        assertTrue(v1Evidence.contains("MS-PROT-043"));
        assertTrue(v1Evidence.contains("MS-PROT-059"));
        assertTrue(v1Evidence.contains("1079 tests, zero failures/errors/skips"));
        assertTrue(v1Evidence.contains("Four concurrent HTTP deliveries"));
        assertTrue(v1Evidence.contains("OUTCOME_UNCERTAIN"));
        assertTrue(v1Evidence.contains("all 11 assertions"));
        assertTrue(v1Graph.contains(
                "V1  full Publication → Enquiry integration                   CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(v1Graph.contains("IMP-07 — Publication → Enquiry vertical slice: CONFORMING_COMPLETE ON THE SAME CI"));
        assertTrue(imp07Closure.contains("Macro-node:** IMP-07 — First Complete Vertical Slice"));
        assertTrue(imp07Closure.contains("P6 Opportunity actionability remains separately BLOCKED_DEPENDENCY"));
        assertTrue(imp07Closure.contains("Provider/financial work still requires its own accepted hard dependencies"));
        assertTrue(status.contains(
                "`IMP-08C-C4D2B — Trusted reaction composition` — **CONFORMING_COMPLETE**"));
        assertTrue(status.contains(
                "`IMP-08C-C2B — Registered work owner binding/current revalidation` — **READY**"));

        String c1Evidence = Files.readString(Path.of("docs", "development",
                "imp-08c-c1-background-contract-conformance-2026-09-05.md"));
        String c1Graph = Files.readString(Path.of("docs", "development",
                "imp-08c-graph-refresh-2026-09-05-c1-closure.md"));
        for (String record : java.util.List.of(status, c1Evidence, c1Graph)) {
            assertTrue(record.contains("93445b32e17efb9915001934ec753a7866a6212b"));
            assertTrue(record.contains("33995933695"));
            assertTrue(record.contains("f9b5073e86405cdbae08962b7b84f83685f5cc7a"));
            assertTrue(record.contains("33995487345"));
            assertTrue(record.contains("1084 unit/governance + 374 PostgreSQL integration tests"));
            assertTrue(record.contains("Registration alone is not executable admission"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(c1Evidence.contains("MS-PROT-065 v1.1"));
        assertTrue(c1Evidence.contains("1084 tests, zero failures/errors/skips"));
        assertTrue(c1Graph.contains("C1 registered BackgroundWorkContract definitions: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(c1Graph.contains("READY FOR DECOMPOSITION ON THE SAME CI"));
        assertTrue(c1Graph.contains("IMP-08C Durable Execution Foundation: IN_PROGRESS"));
        assertTrue(status.contains("Current programme target:** `IMP-08C — Durable Execution Foundation` — **IN_PROGRESS**"));
        assertTrue(status.contains("`IMP-08C-C1 — Registered BackgroundWorkContract definitions` — **CONFORMING_COMPLETE**"));

        String c2aEvidence = Files.readString(Path.of("docs", "development",
                "imp-08c-c2a-contract-affinity-conformance-2026-09-05.md"));
        String c2aGraph = Files.readString(Path.of("docs", "development",
                "imp-08c-graph-refresh-2026-09-05-c2a-closure.md"));
        for (String record : java.util.List.of(status, c2aEvidence, c2aGraph)) {
            assertTrue(record.contains("8751a5e42775c25b92d66bc7eec50e2c98dc68ff"));
            assertTrue(record.contains("33996910427"));
            assertTrue(record.contains("4e3d22dc928bdee675b619c085aa14ef43d8b240"));
            assertTrue(record.contains("33996398540"));
            assertTrue(record.contains("1089 unit/governance + 377 PostgreSQL integration tests"));
            assertTrue(record.contains("Static resolution is not executable admission"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(c2aEvidence.contains("MS-PROT-065 v1.1"));
        assertTrue(c2aEvidence.contains("1089 tests, zero failures/errors/skips"));
        assertTrue(c2aEvidence.contains("V58"));
        assertTrue(c2aGraph.contains("C2A durable registered-contract affinity: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(c2aGraph.contains("C2 instruction/contract affinity and execution binding: IN_PROGRESS"));
        assertTrue(status.contains("`IMP-08C-C2A — Durable registered-contract affinity` — **CONFORMING_COMPLETE**"));

        String c4aEvidence = Files.readString(Path.of("docs", "development",
                "imp-08c-c4a-event-contract-conformance-2026-09-06.md"));
        String c4aGraph = Files.readString(Path.of("docs", "development",
                "imp-08c-graph-refresh-2026-09-06-c4a-closure.md"));
        for (String record : java.util.List.of(status, c4aEvidence, c4aGraph)) {
            assertTrue(record.contains("41a4e0fabd7c6f029bbd23b5f9c7b321dca64366"));
            assertTrue(record.contains("33997829375"));
            assertTrue(record.contains("2a3304325ca2f6c1755e4d227da518e06f8ea77a"));
            assertTrue(record.contains("33997178701"));
            assertTrue(record.contains("1095 unit/governance + 377 PostgreSQL integration tests"));
            assertTrue(record.contains("Registration is not publication or reaction authority"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(c4aEvidence.contains("MS-PROT-026 v1.1"));
        assertTrue(c4aEvidence.contains("1095 tests, zero failures/errors/skips"));
        assertTrue(c4aGraph.contains("C4A registered Event Contracts: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(c4aGraph.contains("C2B executable owner binding: BLOCKED_DEPENDENCY"));
        assertTrue(status.contains("`IMP-08C-C4A — Registered Event Contracts` — **CONFORMING_COMPLETE**"));

        String c4bEvidence = Files.readString(Path.of("docs", "development",
                "imp-08c-c4b1-merchant-event-conformance-2026-09-06.md"));
        String c4bGraph = Files.readString(Path.of("docs", "development",
                "imp-08c-graph-refresh-2026-09-06-c4b1-closure.md"));
        for (String record : java.util.List.of(status, c4bEvidence, c4bGraph)) {
            assertTrue(record.contains("cf6fd38f00690d7fc13019af2b7cbcf03be817fa"));
            assertTrue(record.contains("33998884504"));
            assertTrue(record.contains("b2ffc0b3ee1d7ef4ec84722a4befdbde23548f2d"));
            assertTrue(record.contains("33998151474"));
            assertTrue(record.contains("1099 unit/governance + 380 PostgreSQL integration tests"));
            assertTrue(record.contains("Mapping is not reaction execution or acknowledgement"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(c4bEvidence.contains("MS-PROT-071 v1.2"));
        assertTrue(c4bEvidence.contains("1099 tests, zero failures/errors/skips"));
        assertTrue(c4bEvidence.contains("V59"));
        assertTrue(c4bGraph.contains("C4B1 Merchant Account event occurrence mapping: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(status.contains("`IMP-08C-C4B1 — Merchant Account event occurrence mapping` — **CONFORMING_COMPLETE**"));

        String c4cEvidence = Files.readString(Path.of("docs", "development",
                "imp-08c-c4c-reaction-contract-conformance-2026-09-06.md"));
        String c4cGraph = Files.readString(Path.of("docs", "development",
                "imp-08c-graph-refresh-2026-09-06-c4c-closure.md"));
        for (String record : java.util.List.of(status, c4cEvidence, c4cGraph)) {
            assertTrue(record.contains("cfa5697112847f03c67e3b8b49c36aca03b5f9c6"));
            assertTrue(record.contains("33999465139"));
            assertTrue(record.contains("48c991424a48f6793bede6a5336a882d250503d9"));
            assertTrue(record.contains("33999150773"));
            assertTrue(record.contains("1106 unit/governance + 380 PostgreSQL integration tests"));
            assertTrue(record.contains("Registration does not execute or acknowledge a reaction"));
            org.junit.jupiter.api.Assertions.assertFalse(record.contains("PENDING_FULL_GATE"));
        }
        assertTrue(c4cEvidence.contains("MS-PROT-026 v1.1"));
        assertTrue(c4cEvidence.contains("MS-PROT-056 v1.6"));
        assertTrue(c4cEvidence.contains("1106 tests, zero failures/errors/skips"));
        assertTrue(c4cGraph.contains("C4C reaction registration and identity: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI"));
        assertTrue(status.contains("`IMP-08C-C4C — Reaction registration and identity` — **CONFORMING_COMPLETE under current v1.7 evidence**"));

        String authority = Files.readString(Path.of(
                "designs", "authorities", "ms-prot", "MS-PROT-046",
                "MS-PROT-046 v1.3 — Opportunity Material Revision & Publication History Amendment.md"
        ));
        assertTrue(authority.contains(
                "Status:** **ACCEPTED by manual approval on 4 September 2026**"
        ));
        assertTrue(authority.contains("publication / opportunity@1"));
        assertTrue(authority.replace("\r\n", "\n").contains(
                "PUBLISH\nWITHDRAW\nREPUBLISH"
        ));
    }
}
