# IMP-07 Graph Refresh — E2 Closure

**Date:** 5 September 2026

**Status:** Current graph; E2 closure effective on successful cycle-closing synchronization CI

This is implementation navigation/evidence, superseding the E1 graph while preserving it as history. No macro dependency or semantic authority changes.

## Closure basis

E1 closure `5ee6b62ef963856776d34c7aba41806f2cb9490a`, run `33983582172`, passed the full gate. E2 implementation `415ba5c534b5e12ddbdb087583fd87401bd8dcd6`, run `33984114714`, passed 1004 unit/governance + 346 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-e2-idempotency-conformance-2026-09-05.md`.

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
E2  Enquiry retry idempotency + transaction proof             CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI

E3  stale subject-binding revalidation                       READY — I2 + E1
M1  MERCHANT Enquiry Exposure                               READY — E1 + G0
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
M2  merchant Enquiry representation                         BLOCKED_DEPENDENCY — M1
T1  PUBLIC Publication query adapter                        ON_DEMAND — P5
T2  PUBLIC Enquiry submission adapter                       ON_DEMAND — E2/E3
T3  MERCHANT Enquiry query adapter                          ON_DEMAND — M2
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — downstream owners
```

## Next governed work

After E2 synchronization CI succeeds, select E3 under composite MS-PROT-043, MS-PROT-049 and Publication/Exposure authority. Implement current authoritative merchant/subject/participation/Exposure revalidation with fail-closed stale-reference handling, composing with the E2 transaction/preparation boundary. E2 is an implementation seam available to E3, not a new semantic dependency. M1 remains independently READY. The full vertical slice and later macro gates remain incomplete.
