# IMP-07 Graph Refresh — T2B / T2 Closure

**Date:** 5 September 2026

**Status:** Current graph; T2B/T2 closure effective on successful cycle-closing synchronization CI

Implementation navigation/evidence only, superseding the T2A graph and completing `imp-07-t2-decomposition-2026-09-05.md`. Historical graphs remain intact; no semantic or macro dependency changes.

## Closure basis

T2A closure `c092b10944281f5582f484331c0a4ce988a644fd`, run `33991054297`, passed the full gate. T2B implementation `b9702d030b2a3dc9af181e97bf8bdd8ffa073fd2`, run `33992025633`, passed 1069 unit/governance + 366 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-t2b-binding-delivery-conformance-2026-09-05.md`.

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

T2  PUBLIC Enquiry submission adapter                       CONFORMING_COMPLETE ON THE SAME CI — T2A + T2B
T2A merchant-general PUBLIC command delivery                CONFORMING_COMPLETE
T2B Opportunity binding carry-forward and submission        CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
T3  MERCHANT Enquiry query adapter                          READY — M2; required for V1
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — T3 adapter + integrated path proof
```

## Next governed work

Select T3 after synchronization CI succeeds. Compose the concrete MERCHANT Enquiry query adapter over completed M2 with trusted request establishment, current observation admission, exact P2/Exposure selection and safe delivery. V1 remains blocked until T3 and the integrated Publication-to-Enquiry proof conform.

T2A provides the general command. T2B issues an opaque locator from real I2 and exact P5 selection, retains structured subject/revision meaning through E2 retry reconciliation and applies E3 to fresh submissions. Both paths retain mandatory current admission and owner requirements, content-free acknowledgement and supplied-contact semantics. Persistent locator keys are explicit deployment inputs and old keys must be retained for supported retries. No browser UI or deployed service is claimed. IMP-07 stays IN_PROGRESS.
