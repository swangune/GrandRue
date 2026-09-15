# IMP-08C Live Frontier — v1.7 Revalidation
**Date:** 6 September 2026; navigation refreshed 14 September 2026
**Governing rules:** MS-IMPLEMENTATION-RULES-001 v1.8, `designs/IMPLEMENTATION-RULES.md`, §30.8 — Authority-Change Impact Rule; §52.14 — Mandatory implementation-status synchronisation; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.
**Current eligibility correction:** The full programme review at `imp-programme-integrity-review-2026-09-06.md` remains the correction record and `implementation-programme-state.json` is the complete current graph. The later R3B proof restored IMP-05/06/07 eligibility. C4D2B has closed, C2B subsequently closed, and C3 is now the active durable-execution node without inventing a universal C2/C3/C4 edge.

**Historical scope:** C4C → C4D. This document originally superseded the C4C v1.6 closure graph as current frontier navigation, not as historical evidence. The 14 September navigation refresh preserves that v1.7 evidence while recording the later C2B/C3 frontier selected by the canonical graph.

## Current baseline and proof
Review baseline: `1b3acaac3c933b0c0bc7cc3c97735b81b3b62780`.
Review proof: `c357953c2217689be21dbe50b84bc03d3092b0f0`, run `34008837843` — SUCCESS — 1108 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips.
Current C4D2B proof: `imp-08c-c4d2b-trusted-reaction-conformance-2026-09-12.md` — local PostgreSQL 18.6 / Flyway V64 full gate: 1234 unit/governance + 422 PostgreSQL integration tests, zero failures/errors/skips. The retained C4C evidence remains `imp-08c-c4c-v17-impact-conformance-2026-09-06.md`.
C2B proof: `imp-08c-c2b-standing-free-owner-binding-conformance-2026-09-14.md`.
C3 bounded persistence checkpoint: `imp-08c-c3-attempt-ledger-checkpoint-2026-09-14.md`. C3 remains IN_PROGRESS; later owner-bound claimed execution/worker work does not become closure evidence until its cycle-closing verification and falsification complete.

## Frontier
| Node | Current classification | Scope / dependency consequence |
| --- | --- | --- |
| C4C reaction registration and identity | CONFORMING_COMPLETE under current v1.7 evidence | Original static declaration/identity scope survived fresh inspection and adversarial tests; traceability gaps repaired |
| C4D Standing Free reaction execution and acknowledgement | CONFORMING_COMPLETE | C4D1 receipt + C4D2A owner recovery + C4D2B trusted composition now prove the bounded Standing Free reaction path |
| C4D1 durable receipt and independent acknowledgement | CONFORMING_COMPLETE, scoped proof retained | `imp-08c-c4d1-v17-conformance-2026-09-06.md`; proof `b3dbf2ce2d4ba0c93055dd5658796cc93bbc6d54`, run `34009941352`: 1113 unit/governance + 387 PostgreSQL integration tests, zero failures/errors/skips; no trusted execution claim |
| C4D2 trusted Standing Free execution | CONFORMING_COMPLETE | Exact owner recovery and trusted source/current-contract/scheduled-principal composition are both proven |
| C4D2A exact owner recovery | CONFORMING_COMPLETE, scoped proof retained | `imp-08c-c4d2a-v17-conformance-2026-09-06.md`; proof `2284b47a32e71018f8d648eba154fd6471b651eb`, run `34010435261`: 1116 unit/governance + 388 PostgreSQL integration tests, zero failures/errors/skips |
| C4D2B trusted reaction composition | CONFORMING_COMPLETE | Exact durable source and scope, current reaction contract, contract-bounded scheduled principal, C4D1 receipt, C4D2A owner operation and independent acknowledgement/recovery are composed and falsified |
| IMP-08C-C2B | CONFORMING_COMPLETE | Registered Standing Free durable work resolves historical affinity, current contract/source/scope/principal authority and invokes the real Commercial owner boundary |
| IMP-08C-C2 | CONFORMING_COMPLETE | C2A registered affinity plus C2B real owner binding/current revalidation are complete |
| IMP-08C-C3 | IN_PROGRESS | Attempt start/outcome persistence and bounded Standing Free claimed execution/contract-scoped worker path exist; full gate, completion falsification, evidence synchronization and production reachability proof remain required |
| IMP-08C | IN_PROGRESS | C3, C4B2, C4E and C5 remain independently incomplete even though C2 and C4D are complete |

OBSERVED: C4C has no durable receipt/acknowledgement or execution path. This is counterevidence against a broader completion claim, not proof that C4C's originally bounded contract-registration scope failed.
DEDUCED: C4D is conforming complete for the registered Standing Free reaction. Source and principal trust are independently revalidated and are not inferred from identifiers, registry references or a prior receipt.
OBSERVED: C2B is now complete and C3 has moved from READY to IN_PROGRESS. The C3 path persists physical-attempt start separately from outcome, resolves committed Standing Free owner evidence before retry, and uses an owner-qualified contract-scoped technical claim boundary so a Standing Free worker cannot deliberately lease unrelated work.
UNCERTAIN / outside evidence: C3 cycle-closing verification and production composition reachability, other background-work owners, C4B2 discovery/publication, C4E Booking acknowledgement correction, C5 operational evidence/reconciliation and macro integration remain separately governed.

Other completed-node labels and sibling dependency states are inherited from prior navigation and were not re-audited. Reopen one only if a live dependency supplies a freshness reason. The old C4C v1.6 evidence remains untouched at Git blob `2de4ac61d9753c18e4079be0453417c214fa9ed6`.
No new design authority, historical completion certification, macro completion or branch creation is implied.
