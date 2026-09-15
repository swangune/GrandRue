# IMP-07 Graph Refresh — T3 Closure

**Date:** 5 September 2026

**Status:** Current graph; T3 closure effective on successful cycle-closing synchronization CI

Implementation navigation/evidence only, superseding the T2B/T2 graph. Historical graphs remain intact; no semantic or macro dependency changes.

## Closure basis

T2B/T2 closure `11010d07b76d79b8086a19cfd15c5334750aad63`, run `33992305320`, passed the full gate. T3 implementation `26674121a673015ea9155ad5222dd7eaddbb2932`, run `33993070421`, passed 1079 unit/governance + 370 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-t3-merchant-query-conformance-2026-09-05.md`.

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

T2  PUBLIC Enquiry submission adapter                       CONFORMING_COMPLETE
T2A merchant-general PUBLIC command delivery                CONFORMING_COMPLETE
T2B Opportunity binding carry-forward and submission        CONFORMING_COMPLETE
T3  MERCHANT Enquiry query adapter                          CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
V1  full Publication → Enquiry integration                   READY ON THE SAME CI — required owner nodes + T1/T2/T3
```

## Next governed work

Select V1 after synchronization CI succeeds. Compose the Publication representation, concrete Public Interaction binding, public Enquiry submission/retry and merchant Enquiry query into an integrated vertical proof. Verify exact source selection, original submission provenance, current authority, Merchant Scope isolation and stable retry meaning across publication change.

T3 composes real session establishment/currentness, merchant audience admission, M2 acquisition, P2 serviceability and M1 family Exposure before exact selected response mapping. All required authority/privilege providers remain explicit deployment inputs. The profile is implemented and tested, not deployed. Optional current subject, known CustomerContext, communication and attention material remain absent from the minimum read.

V1 is READY, not complete. P6 actionability remains separately dependency-blocked and IMP-07 stays IN_PROGRESS.
