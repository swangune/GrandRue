# IMP-08C Graph Refresh — C2A Closure
**Date:** 5 September 2026.
Navigation/evidence under MS-IMP-001 and IMPLEMENTATION-RULES v1.6; composite MS-PROT-065 v1.1.
Supersedes `imp-08c-graph-refresh-2026-09-05-c1-closure.md` for current navigation. Selection: `imp-08c-c2-decomposition-2026-09-05.md`.

C1 final head `4e3d22dc928bdee675b619c085aa14ef43d8b240`, run `33996398540`, passed 1084 unit/governance + 374 PostgreSQL integration tests.
C2A implementation `8751a5e42775c25b92d66bc7eec50e2c98dc68ff`, run `33996910427`, passed 1089 unit/governance + 377 PostgreSQL integration tests, zero failures/errors/skips.

## Current graph
- IMP-07: CONFORMING_COMPLETE.
- IMP-08A/B: ELIGIBLE.
- IMP-08C Durable Execution Foundation: IN_PROGRESS.
- C1 registered BackgroundWorkContract definitions: CONFORMING_COMPLETE.
- C2 instruction/contract affinity and execution binding: IN_PROGRESS.
- C2A durable registered-contract affinity: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI.
- C2B owner execution-binding assessment: READY ON THE SAME CI; executable composition remains BLOCKED_DEPENDENCY on concrete owner operation/current revalidation.
- C3 attempt/retry/recovery: BLOCKED_DEPENDENCY — executable C2B plus owner/downstream intent semantics.
- C4 event occurrence/publication/reaction/acknowledgement: ELIGIBLE FOR DECOMPOSITION.
- C5 durable execution integration evidence: BLOCKED_DEPENDENCY — required children.

Static resolution is not executable admission. Exact historical registration is persisted; legacy work remains explicitly unregistered. No concrete background handler is enabled. Lifecycle reevaluation is an accepted candidate under MS-PROT-065 §§43–44; no corresponding application implementation was found in the current source inventory, so owner dependencies must be selected explicitly before runtime binding.
Provider/financial prerequisites remain in force. P6 actionability remains separately BLOCKED_DEPENDENCY. No macro completion or Git branch creation is implied.

Evidence: `imp-08c-c2a-contract-affinity-conformance-2026-09-05.md`. C2A closes with synchronized status/evidence/graph/programme gate and successful full synchronization CI.
