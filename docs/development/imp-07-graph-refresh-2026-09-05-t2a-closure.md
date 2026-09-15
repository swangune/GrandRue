# IMP-07 Graph Refresh — T2A Closure

**Date:** 5 September 2026

**Status:** Current graph; T2A closure effective on successful cycle-closing synchronization CI

Implementation navigation/evidence only, superseding the T1B graph and incorporating `imp-07-t2-decomposition-2026-09-05.md`. Historical graphs remain intact; no semantic or macro dependency changes.

## Closure basis

T1B/T1 closure `eca3df9d76e271b4d2c982fbcf3b2c427d743cf2`, run `33989939024`, passed the full gate. T2A implementation `115db6a825beafc5a6cd58cba5a0fbbe37c87332`, run `33990790405`, passed 1061 unit/governance + 361 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-t2a-general-delivery-conformance-2026-09-05.md`.

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
T1A query contract + bounded safe response mapping           CONFORMING_COMPLETE
T1B trusted request/query execution + delivery               CONFORMING_COMPLETE
T1  PUBLIC Publication query adapter                        CONFORMING_COMPLETE

T2  PUBLIC Enquiry submission adapter                       IN_PROGRESS — T2A + T2B
T2A merchant-general PUBLIC command delivery                CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
T2B Opportunity binding carry-forward and submission        READY ON THE SAME CI — T2A + I2/E3
T3  MERCHANT Enquiry query adapter                          READY — M2; required for V1
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — complete T2/T3 adapters + integrated path proof
```

## Next governed work

Select T2B after synchronization CI succeeds. Implement the concrete browser binding handoff from the completed I2 owner participation path into E2/E3, retaining structured subject meaning and stable logical retry intent. T1's DTO does not expose internal revision evidence, so T2B must not assume clients can manufacture it or silently resolve a different submission meaning on replay. Current scope, participation, Exposure, interaction and owner requirements remain authoritative.

T2A provides a tested general-only public command with content-free acknowledgements and mandatory admission/requirements providers. It rejects subject-specific input. T2 remains incomplete until its binding-aware child conforms; T3 and V1 remain required. IMP-07 stays IN_PROGRESS.
