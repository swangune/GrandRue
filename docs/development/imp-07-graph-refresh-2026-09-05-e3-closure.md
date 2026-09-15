# IMP-07 Graph Refresh — E3 Closure

**Date:** 5 September 2026

**Status:** Current graph; E3 closure effective on successful cycle-closing synchronization CI

This is implementation navigation/evidence, superseding the E2 graph while preserving it as history. No macro dependency or semantic authority changes.

## Closure basis

E2 closure `d25961238b99af5a1edf893495fceeaec41760a1`, run `33984462969`, passed the full gate. E3 implementation `b39f4db5dbfff4ed3472d3d47f659d4f436079e7`, run `33985246923`, passed 1015 unit/governance + 351 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-e3-revalidation-conformance-2026-09-05.md`.

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
E3  stale subject-binding revalidation                       CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI

M1  MERCHANT Enquiry Exposure                               READY — E1 + G0
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
M2  merchant Enquiry representation                         BLOCKED_DEPENDENCY — M1
T1  PUBLIC Publication query adapter                        ON_DEMAND — P5
T2  PUBLIC Enquiry submission adapter                       ON_DEMAND — E2/E3
T3  MERCHANT Enquiry query adapter                          ON_DEMAND — M2
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — downstream owners
```

## Next governed work

After E3 synchronization CI succeeds, select M1 under composite MS-PROT-043 and Exposure/Actor Authorisation authority: registered MERCHANT Enquiry Exposure for legitimate observation. M2 follows M1. T2 can consume E2/E3 on demand but still owns trusted delivery/access establishment and must supply concrete remaining Enquiry requirements. The full vertical slice and later macro gates remain incomplete.
