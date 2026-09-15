# IMP-08C Graph Refresh — C1 Closure

**Date:** 5 September 2026
**Authority:** MS-IMP-001 v1.0 + v1.1; IMPLEMENTATION-RULES v1.6; composite MS-PROT-065 v1.1.
Implementation navigation only. Supersedes `imp-08-foundation-graph-refresh-2026-09-05.md` for current navigation.

IMP-07 closure `f9b5073e86405cdbae08962b7b84f83685f5cc7a`, run `33995487345`, succeeded with 1079 unit/governance + 374 PostgreSQL integration tests.
C1 implementation `93445b32e17efb9915001934ec753a7866a6212b`, run `33995933695`, succeeded with 1084 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips.

## Current graph

- IMP-07: CONFORMING_COMPLETE.
- IMP-08A Merchant Assistance: ELIGIBLE; remaining owner-bound assistance decomposition required.
- IMP-08B Workforce: ELIGIBLE; remaining invitation/delegation and attribution/Audit composition required.
- IMP-08C Durable Execution Foundation: IN_PROGRESS.
- C1 registered BackgroundWorkContract definitions: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI.
- C2 instruction/contract affinity and execution binding: READY FOR DECOMPOSITION ON THE SAME CI. Identify the first accepted owner responsibility and explicit runtime composition before admitting executable work.
- C3 durable attempt/retry/recovery composition: BLOCKED_DEPENDENCY — C2 and concrete owner logical-intent semantics.
- C4 event occurrence/publication/reaction/acknowledgement: ELIGIBLE FOR DECOMPOSITION under its accepted event authority.
- C5 durable execution integration evidence: BLOCKED_DEPENDENCY — required C2/C3/C4 children.

C1 declares mandatory semantic references and exact-release, owner-qualified lookup. Registration alone is not executable admission; current authority and owner semantics remain required. Existing technical persistence is not represented as enforcing this registry.

P6 Opportunity actionability remains separately BLOCKED_DEPENDENCY on authoritative Opportunity facts. Provider/financial work retains its own hard dependencies, including the required durable-execution foundation. No macro closure or Git branch creation follows from C1.

Conformance record: `imp-08c-c1-background-contract-conformance-2026-09-05.md`. This graph, status, evidence and executable programme gate close together on successful full synchronization CI.
