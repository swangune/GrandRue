# IMP-07 Graph Refresh — I1 Participation Source Closure

**Date:** 5 September 2026  
**Programme:** MS-IMP-001 v1.0 + v1.1  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice — IN_PROGRESS  
**Status:** CURRENT IMP-07 FINE-GRAINED GRAPH — I1 closure / I2 readiness effective on successful cycle-closing synchronization CI  
**Code-bearing head:** `1065904c9e3cabc3a20d2ba7be9296c8e6f7c5b8`  
**Code verification:** run `33981525405` — SUCCESS — 988 unit/governance + 329 PostgreSQL integration tests, zero failures/errors/skips

This is implementation navigation/evidence only. It supersedes the P5 closure graph as current navigation while retaining that graph as historical evidence. Semantic authority and macro dependencies are unchanged.

## Closure basis

P5 closure head `8bb87588b1e013f46e15d2be4c681a0b313099cf`, run `33960381639`, passed the full gate and made I1 ready.

I1 adds the explicit release-affined Publication Opportunity → enquiry/send-enquiry definition and concrete owner source. Evidence: `imp-07-i1-participation-conformance-2026-09-05.md`.

## Current dependency graph

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE
P1  Publication lifecycle/currentness domain foundation       CONFORMING_COMPLETE
P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
P2B reconstructible material revisions + Publication History  CONFORMING_COMPLETE
P3  application mutation + transaction + retry idempotency     CONFORMING_COMPLETE
P4  Publication PUBLIC Exposure contracts/evaluation          CONFORMING_COMPLETE
P5  request-scoped public Opportunity representation          CONFORMING_COMPLETE
I1  owner-qualified Opportunity → enquiry/send-enquiry source  CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONIZATION CI
I2  concrete Public Interaction Binding proof                 READY ON THE SAME CI

P6  Opportunity actionability                                BLOCKED_DEPENDENCY — authoritative Opportunity facts; separate from participation
E1  durable Enquiry submission/provenance                     BLOCKED_DEPENDENCY — I2
E2  Enquiry retry idempotency + transaction proof              BLOCKED_DEPENDENCY — E1
E3  stale subject-binding authoritative revalidation           BLOCKED_DEPENDENCY — I2 + E1
M1  MERCHANT Enquiry Exposure contracts                       BLOCKED_DEPENDENCY — E1 + G0
M2  request-scoped merchant Enquiry representation            BLOCKED_DEPENDENCY — M1
T1  concrete PUBLIC Publication query adapter                 ON_DEMAND — P5
T2  concrete PUBLIC Enquiry submission adapter                ON_DEMAND — E2/E3
T3  concrete MERCHANT Enquiry query adapter                   ON_DEMAND — M2
V1  full Publication → Enquiry integration                    BLOCKED_DEPENDENCY — downstream owners
```

## Next governed work

After the I1 cycle-closing synchronization head passes the full Maven/PostgreSQL gate, execute I2: prove the concrete source with the existing P5 bounded public representation, Exposure/S2 selection and generic S3 binding projector. Preserve missing-source, unexposed/unserviceable-subject and incompatible-context rejection; do not introduce Enquiry persistence or execution authority into the projector.

I1 remains participation only. I2 is not the full vertical slice. P6 remains separate; later macro targets remain blocked by the existing IMP-07 programme gate.
