# IMP-07 Graph Refresh — T1B / T1 Closure

**Date:** 5 September 2026

**Status:** Current graph; T1B and T1 closure effective on successful cycle-closing synchronization CI

Implementation navigation/evidence only, superseding the T1A graph while retaining its history. No semantic or macro dependency changes.

## Closure basis

T1A closure `7ef3aa3b46f5188ff6749b6b0bbd378bd90f530f`, run `33988912106`, passed the full gate. T1B implementation `5cd020b5d60c2ff15241eeb128b3d22868b647af`, run `33989663158`, passed 1052 unit/governance + 357 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-t1b-delivery-conformance-2026-09-05.md`.

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
T1B trusted request/query execution + delivery               CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
T1  PUBLIC Publication query adapter                        CONFORMING_COMPLETE ON THE SAME CI — T1A + T1B
T2  PUBLIC Enquiry submission adapter                       READY — E2/E3; selected next
T3  MERCHANT Enquiry query adapter                          READY — M2; required for V1
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
V1  full Publication → Enquiry integration                   BLOCKED_DEPENDENCY — complete T2/T3 adapters + integrated path proof
```

## Next governed work

Select T2 after synchronization CI succeeds. Compose trusted public delivery/request context, exact E2 logical-request idempotency and E3 current Opportunity subject revalidation, with accepted Enquiry requirement preparation and safe transport outcomes under composite MS-PROT-035/043/049/059. Identifiers and retry keys grant no access.

T1 has a tested opt-in production HTTP path with explicit runtime dependencies; this is not a deployment claim. T3 remains required for merchant Enquiry delivery. V1 still requires the complete Publication → Enquiry → merchant observation integration proof. P6 remains separate, and IMP-07 stays IN_PROGRESS.
