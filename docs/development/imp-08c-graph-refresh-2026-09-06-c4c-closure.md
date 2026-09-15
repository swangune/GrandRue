# IMP-08C Graph Refresh — C4C Closure
Date: 6 September 2026. Navigation under MS-IMP-001, IMPLEMENTATION-RULES v1.6, MS-PROT-026 v1.1 and MS-PROT-056 v1.6.
Supersedes `imp-08c-graph-refresh-2026-09-06-c4b1-closure.md` for current navigation.
C4B1 closing head `48c991424a48f6793bede6a5336a882d250503d9`, run `33999150773`, passed 1099 unit/governance + 380 PostgreSQL integration tests.
C4C implementation `cfa5697112847f03c67e3b8b49c36aca03b5f9c6`, run `33999465139`, passed 1106 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips.

## Current graph
- IMP-07: CONFORMING_COMPLETE; IMP-08A/B: ELIGIBLE; IMP-08C: IN_PROGRESS.
- C1, C2A, C4A, C4B1: CONFORMING_COMPLETE.
- C4C reaction registration and identity: CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI.
- C4D Standing Free reaction execution and acknowledgement: READY FOR DECOMPOSITION ON THE SAME CI. Required children: durable independent reaction evidence; trusted source/worker composition; committed-baseline recovery after lost acknowledgement.
- C2B/C3: continue concrete Commercial execution/dependency assessment alongside C4D.
- C4 and C5: IN_PROGRESS / BLOCKED_DEPENDENCY respectively until required execution/recovery integration is verified.

Registration does not execute or acknowledge a reaction. One occurrence plus one owner-qualified contract determines the logical reaction; registry release remains historical affinity, not automatic new intention. StandingFreeFromMerchantAccountEstablishedHandler remains the accepted application target.
Unknown old event affinity is not retroactively assigned current meaning. No historical production backfill or automatic listener discovery is introduced.
Provider/financial and separate P6 prerequisites remain; no macro closure or branch creation.
Evidence: `imp-08c-c4c-reaction-contract-conformance-2026-09-06.md`. Graph, evidence, status and executable gate close on full synchronization CI.
