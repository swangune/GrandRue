# IMP-07 Graph Refresh — I2 Binding Proof Closure

**Date:** 5 September 2026

**Programme:** MS-IMP-001 v1.0 + v1.1

**Macro:** IMP-07 — Publication → Enquiry vertical slice — IN_PROGRESS

**Status:** CURRENT IMP-07 FINE-GRAINED GRAPH — I2 closure / E1 readiness effective on successful cycle-closing synchronization CI

**Proof head:** `c499439951d594f310992807805ba97d2d294605`, run `33982320477` — SUCCESS — 996 unit/governance + 329 PostgreSQL integration tests, zero failures/errors/skips

This is implementation navigation/evidence, superseding the I1 graph as current navigation while preserving it as history. No macro dependency or semantic authority changes.

## Closure basis

I1 closure head `db733f35950d2a22faef6d5dc163358b751bbb18`, run `33981821452`, passed the full gate. I2 now proves the real Opportunity source with P5 representation, Publication Exposure, S2 assembly and generic S3 binding. Evidence: `imp-07-i2-binding-conformance-2026-09-05.md`.

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
I2  concrete Public Interaction Binding proof                CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
E1  durable Enquiry submission/provenance                    READY ON THE SAME CI

P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
E2  Enquiry retry idempotency + transaction proof             BLOCKED_DEPENDENCY — E1
E3  stale subject-binding revalidation                       BLOCKED_DEPENDENCY — I2 + E1
M1  MERCHANT Enquiry Exposure                               BLOCKED_DEPENDENCY — E1 + G0
M2  merchant Enquiry representation                         BLOCKED_DEPENDENCY — M1
T1  PUBLIC Publication query adapter                        ON_DEMAND — P5
T2  PUBLIC Enquiry submission adapter                       ON_DEMAND — E2/E3
T3  MERCHANT Enquiry query adapter                          ON_DEMAND — M2
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — downstream owners
```

## Next governed work

After I2 synchronization CI succeeds, execute E1 using accepted composite MS-PROT-043, MS-PROT-049 and persistence authority: durable Enquiry submission and immutable subject provenance. Resolve the smallest contract from current accepted authority before implementation. Enquiry runtime revalidation, retry/transaction proof, merchant observation and transport retain their separate dependencies. The full vertical slice and later macro gates are not complete.
