# IMP-07 Graph Refresh — E1 Closure

**Date:** 5 September 2026

**Status:** Current graph; E1 closure effective on successful cycle-closing synchronization CI

This is implementation navigation/evidence, superseding the I2 graph while preserving it as history. No macro dependency or semantic authority changes.

## Closure basis

I2 closure head `b6bff57c580178c8b6ee9c38fd277bb8fa1aaead`, run `33982554318`, passed the full gate. E1 implementation head `83f433e39de709c587cfa32385b7af0151cec53c`, run `33983277431`, passed 1001 unit/governance + 337 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-e1-submission-conformance-2026-09-05.md`.

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
E1  durable Enquiry submission/provenance                    CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI

E2  Enquiry retry idempotency + transaction proof             READY ON THE SAME CI — E1
E3  stale subject-binding revalidation                       READY ON THE SAME CI — I2 + E1
M1  MERCHANT Enquiry Exposure                               READY ON THE SAME CI — E1 + G0
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
M2  merchant Enquiry representation                         BLOCKED_DEPENDENCY — M1
T1  PUBLIC Publication query adapter                        ON_DEMAND — P5
T2  PUBLIC Enquiry submission adapter                       ON_DEMAND — E2/E3
T3  MERCHANT Enquiry query adapter                          ON_DEMAND — M2
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — downstream owners
```

## Next governed work

After E1 synchronization CI succeeds, select E2 under composite MS-PROT-043 and persistence/transaction authority: logical submission retry idempotency and application transaction proof. E3 is independently READY for current authority revalidation; M1 is independently READY for MERCHANT Enquiry Exposure. No public submission adapter may bypass those owners. The full vertical slice and later macro gates remain incomplete.
