# IMP-07 Graph Refresh — V1 / Macro Closure

**Date:** 5 September 2026

**Status:** Current graph; V1/IMP-07 closure effective on successful cycle-closing synchronization CI

Implementation navigation/evidence only, superseding the T3 graph. Historical graphs remain intact; no semantic or macro dependency changes.

## Closure basis

T3 closure `6657a12b20218970312a1f0ace907e36d1db4d08`, run `33993321981`, passed the full gate. V1 proof `fccef99cf13789ac9302bd4efe3bec237c61eede`, run `33995174577`, passed 1079 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips. Evidence: `imp-07-v1-vertical-conformance-2026-09-05.md`. Macro closure: `imp-07-publication-enquiry-vertical-slice-closure-2026-09-05.md`.

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
T3  MERCHANT Enquiry query adapter                          CONFORMING_COMPLETE
P6  Opportunity actionability                               BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate
V1  full Publication → Enquiry integration                   CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
```

## Macro consequence and next governed work

IMP-07 — Publication → Enquiry vertical slice: CONFORMING_COMPLETE ON THE SAME CI.

V1 proves the mandatory established/configured merchant and exposed profile path through Publication application/persistence, public observation/binding, atomic Enquiry creation/retry and merchant observation. It includes current authority, scope isolation, exact provenance and concurrent logical-request reconciliation. Existing conforming owner and adapter nodes remain intact.

IMP-08A Merchant Assistance, IMP-08B Workforce and IMP-08C Durable Execution become ELIGIBLE ON THE SAME CI for remaining-work/dependency graph refresh. Inspect existing conforming work and select the smallest dependency-complete executable node under MS-IMP-001. This does not complete those foundations or authorize Git branch creation. Consequential provider/financial nodes retain their separate hard dependencies.

P6 remains separately dependency-blocked on authoritative Opportunity facts; visibility and Enquiry participation do not absorb actionability. No deployment or browser UI is claimed.
