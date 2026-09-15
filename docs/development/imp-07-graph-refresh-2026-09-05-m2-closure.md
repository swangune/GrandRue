# IMP-07 Graph Refresh — M2 Closure

**Date:** 5 September 2026

**Status:** Current graph; M2 closure effective on successful cycle-closing synchronization CI

This is implementation navigation/evidence, superseding the M1 graph while preserving it as history. No macro dependency or semantic authority changes.

## Closure basis

M1 closure `8e9fc480f07acaf1b6c83c55cd4a9f7bc1bbbc13`, run `33986872166`, passed the full gate. M2 implementation `7ec5667f845b1e098a44da5e2b9b21c6d6674e2a`, run `33987788979`, passed 1035 unit/governance + 355 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-m2-representation-conformance-2026-09-05.md`.

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
M2  merchant Enquiry representation                         CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI

P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
T1  PUBLIC Publication query adapter                        READY — P5; selected for V1
T2  PUBLIC Enquiry submission adapter                       READY — E2/E3; required for V1
T3  MERCHANT Enquiry query adapter                          READY ON THE SAME CI — M2; required for V1
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — required T1/T2/T3 adapters
```

## Next governed work

The original fine graph in `imp-07-graph-refresh-2026-09-04-publication-currentness.md` explicitly gives V1 prerequisites as P5/I2/E2/E3/M2 + required adapters. MS-IMP-001 §13 requires public API, merchant API and the full integration path as applicable. With owner prerequisites satisfied on M2 synchronization, activate the previously ON_DEMAND adapters for that mandatory path. This refines the abbreviated “downstream owners” blocker; it does not silently remove the adapter dependency.

Select T1 next after M2 synchronization CI succeeds. It is the earliest dependency-complete adapter: concrete PUBLIC Publication query delivery over the completed P5 and generic IMP-06 boundaries. T2 follows the E2/E3 owner operation and must establish trusted delivery/access context and supply remaining concrete Enquiry requirements. T3 consumes M2 through current merchant admission, serviceability and Exposure. Their detailed test-first implementation must follow current composite owner/API authority.

M2 supplies request-scoped selected immutable material and original submission-time provenance. Optional current subject and communication material remain absent without their authorities. P6 remains separate. IMP-07 and the full vertical gate remain incomplete until the required concrete path is proven.
