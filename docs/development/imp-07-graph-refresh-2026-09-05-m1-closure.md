# IMP-07 Graph Refresh — M1 Closure

**Date:** 5 September 2026

**Status:** Current graph; M1 closure effective on successful cycle-closing synchronization CI

This is implementation navigation/evidence, superseding the E3 graph while preserving it as history. No macro dependency or semantic authority changes.

## Closure basis

E3 closure `cf276ec7142072ed332f59354da672894ccf7a79`, run `33985515264`, passed the full gate. M1 implementation `8fe221d9acbbbd4e772be9eee5896f9058ffa4e7`, run `33986535598`, passed 1025 unit/governance + 353 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-m1-exposure-conformance-2026-09-05.md`.

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
M1  MERCHANT Enquiry Exposure                               CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI

M2  merchant Enquiry representation                         READY ON THE SAME CI — M1
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
T1  PUBLIC Publication query adapter                        ON_DEMAND — P5
T2  PUBLIC Enquiry submission adapter                       ON_DEMAND — E2/E3
T3  MERCHANT Enquiry query adapter                          ON_DEMAND — M2
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — downstream owners
```

## Next governed work

After M1 synchronization CI succeeds, select M2 under composite MS-PROT-043 and Projection/Exposure authority: request-scoped merchant Enquiry representation over permitted exact material, preserving original provenance separately from current permitted subject information. Communication material remains absent without its own authoritative source. T3 remains on demand after M2. The full vertical slice and later macro gates remain incomplete.
