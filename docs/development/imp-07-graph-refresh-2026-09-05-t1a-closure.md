# IMP-07 Graph Refresh — T1A Closure

**Date:** 5 September 2026

**Status:** Current graph; T1A closure effective on successful cycle-closing synchronization CI

Implementation navigation/evidence only. This supersedes the M2 graph and incorporates `imp-07-t1-decomposition-2026-09-05.md`, preserving both as history. No semantic or macro dependency changes.

## Closure basis

M2 closure `a3c919ed67e69695d5412c5af42e12318a31ce9d`, run `33988065345`, passed the full gate. T1A implementation `fade9d97cecf4542b968c2145293ef28ce5e9859`, run `33988676029`, passed 1043 unit/governance + 355 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-t1a-response-conformance-2026-09-05.md`.

## Current graph

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE
P1  Publication lifecycle/currentness                        CONFORMING_COMPLETE
P2A durable revision/currentness persistence                 CONFORMING_COMPLETE
P2B reconstructible material revisions + history             CONFORMING_COMPLETE
P3  mutation + transaction + retry idempotency                CONFORMING_COMPLETE
P4  PUBLIC Exposure contracts/evaluation                     CONFORMING_COMPLETE
P5  request-scoped public representation                     CONFORMING_COMPLETE
I1  Opportunity → enquiry/send-enquiry source                CONFORMING_COMPLETE
I2  concrete Public Interaction Binding proof                CONFORMING_COMPLETE
E1  durable Enquiry submission/provenance                    CONFORMING_COMPLETE
E2  Enquiry retry idempotency + transaction proof             CONFORMING_COMPLETE
E3  stale subject-binding revalidation                       CONFORMING_COMPLETE
M1  MERCHANT Enquiry Exposure                               CONFORMING_COMPLETE
M2  merchant Enquiry representation                         CONFORMING_COMPLETE

T1  PUBLIC Publication query adapter                        IN_PROGRESS — T1A + T1B
T1A query contract + bounded safe response mapping           CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
T1B trusted request/query execution + delivery               READY ON THE SAME CI — T1A
T2  PUBLIC Enquiry submission adapter                       READY — E2/E3; required for V1
T3  MERCHANT Enquiry query adapter                          READY — M2; required for V1
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — complete T1/T2/T3 adapters
```

## Next governed work

Select T1B after T1A synchronization CI succeeds. Compose trusted scope/request/audience establishment, current P2 and PUBLIC Exposure, exact P5 read and T1A response mapping with concrete delivery and safe unavailable outcomes under composite MS-PROT-035/027/046. Definition references and deterministic P2/admission fixtures in T1A do not establish deployment wiring.

T1 remains incomplete until T1B proves the actual query delivery path. T2/T3 remain required by the original V1 adapter dependency, and P6 remains separate. IMP-07 stays IN_PROGRESS.
