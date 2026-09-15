# IMP-08C Graph Refresh — C4B1 Closure
Date: 6 September 2026. Navigation under MS-IMP-001, IMPLEMENTATION-RULES v1.6, MS-PROT-026 v1.1 and MS-PROT-071 v1.2.
Supersedes `imp-08c-graph-refresh-2026-09-06-c4a-closure.md` for current navigation.
C4A final head `b2ffc0b3ee1d7ef4ec84722a4befdbde23548f2d`, run `33998151474`, passed 1095 unit/governance + 377 PostgreSQL integration tests.
C4B1 implementation `cf6fd38f00690d7fc13019af2b7cbcf03be817fa`, run `33998884504`, passed 1099 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips.

## Current graph
- IMP-07: CONFORMING_COMPLETE; IMP-08A/B: ELIGIBLE.
- IMP-08C: IN_PROGRESS.
- C1, C2A, C4A: CONFORMING_COMPLETE.
- C4B1 Merchant Account event occurrence mapping: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI.
- C4B occurrence/publication integration: IN_PROGRESS until its required consumer/discovery integration is identified and verified; no blanket closure of other owner outboxes.
- C4C reaction registration and identity: READY ON THE SAME CI.
- C4D concrete Standing Free reaction/independent acknowledgement: BLOCKED_DEPENDENCY on C4C and trusted execution composition. MS-PROT-056 v1.6 governs the consequence; existing StandingFreeFromMerchantAccountEstablishedHandler and persistence tests supply owner behavior.
- C2B/C3: dependency assessment must include this concrete Commercial candidate; earlier lifecycle/Notification limitations remain true but are not universal blockers.
- C5: BLOCKED_DEPENDENCY on required execution/recovery/acknowledgement evidence.

Mapping is not reaction execution or acknowledgement. Legacy event registration remains unknown; V59 does not backfill meaning. The accepted temporal anchor and existing atomic bootstrap boundary are preserved.
Provider/financial prerequisites and separate P6 actionability block remain. No branch creation or macro closure.
Evidence: `imp-08c-c4b1-merchant-event-conformance-2026-09-06.md`. This graph, evidence, status and programme gate close together on full synchronization CI.
