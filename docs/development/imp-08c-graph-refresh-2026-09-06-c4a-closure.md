# IMP-08C Graph Refresh — C4A Closure
**Date:** 6 September 2026.
Navigation/evidence under MS-IMP-001 and IMPLEMENTATION-RULES v1.6; MS-PROT-026 v1.1 §§3–6.
Supersedes `imp-08c-graph-refresh-2026-09-05-c2a-closure.md` for current navigation.
Selection and C2B assessment: `imp-08c-c2b-assessment-c4a-selection-2026-09-06.md`.

C2A closing head `2a3304325ca2f6c1755e4d227da518e06f8ea77a`, run `33997178701`, passed 1089 unit/governance + 377 PostgreSQL integration tests.
C4A implementation `41a4e0fabd7c6f029bbd23b5f9c7b321dca64366`, run `33997829375`, passed 1095 unit/governance + 377 PostgreSQL integration tests, zero failures/errors/skips.

## Current graph
- IMP-07: CONFORMING_COMPLETE.
- IMP-08A/B: ELIGIBLE.
- IMP-08C Durable Execution Foundation: IN_PROGRESS.
- C1 and C2A: CONFORMING_COMPLETE.
- C2 instruction/contract affinity and execution binding: IN_PROGRESS.
- C2B executable owner binding: BLOCKED_DEPENDENCY — concrete owner operation/current revalidation and bounded execution context. Assessment complete; runtime implementation not complete.
- C3 attempt/retry/recovery: BLOCKED_DEPENDENCY — executable C2B plus owner/downstream intent semantics.
- C4 event occurrence/publication/reaction/acknowledgement: IN_PROGRESS.
- C4A registered Event Contracts: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI.
- C4B owner event occurrence/publication mapping: READY FOR DECOMPOSITION ON THE SAME CI — C4A plus accepted owner fact and existing local consistency boundary.
- C4C reaction registration/identity: ELIGIBLE ON THE SAME CI; execution/acknowledgement requires explicit owner composition.
- C5 durable execution integration evidence: BLOCKED_DEPENDENCY — required children.

Registration is not publication or reaction authority. C4A resolves only original owner-qualified contract meaning at the exact release. Existing DomainEvent and owner-specific outboxes are unchanged. C4B must select a concrete accepted owner publication mapping; generic registration is not integrated durable event conformance.
C2B lifecycle execution lacks a corresponding application implementation; Notification external delivery retains its provider/principal/progression prerequisites. No generic callback bypass is permitted.
Provider/financial and separate P6 prerequisites remain. No macro closure or branch creation is implied.

Evidence: `imp-08c-c4a-event-contract-conformance-2026-09-06.md`. C4A closes when status, evidence, graph and executable programme gate are synchronized on successful full closing CI.
