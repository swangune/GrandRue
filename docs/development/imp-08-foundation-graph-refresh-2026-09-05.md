# IMP-08A/B/C Remaining-Work Graph — Initial Selection

**Date:** 5 September 2026
**Status:** Current selection; IMP-07 closure verified

Navigation/evidence only, under MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6.

IMP-07 closure `f9b5073e86405cdbae08962b7b84f83685f5cc7a`, run `33995487345`, passed 1079 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips.

## Existing foundations and remaining work

| Macro | Observed implementation | Remaining proof/implementation |
| --- | --- | --- |
| IMP-08A Merchant Assistance | media identities, rendition profiles/store; purpose-bound personal media eligibility and use-basis persistence | production assistance/inference and proposal/approval composition, current purpose/privacy integration and controlled content path |
| IMP-08B Workforce | membership, roles/groups, scoped authorization, operational device context and persistence; Audit primitives | invitation/delegation lifecycle completeness and integrated staff attribution/Audit path against composite authority |
| IMP-08C Durable Execution | immutable work instruction, attempts/result classifications, technical claim/retry/finalization store; DomainEvent value and owner-specific outboxes | registered background contracts, execution binding/current revalidation, attempt progression/recovery; event occurrence/publication/reaction/acknowledgement and integrated operational evidence |

Existing code/tests are retained as foundations, not blanket macro closure. This bounded inventory establishes next selection; remaining branches require finer authority-driven decomposition before implementation.

## Dependency graph and selection

```text
IMP-07  mandatory programme gate                         CONFORMING_COMPLETE
A0      Assistance remaining-work authority decomposition ELIGIBLE
B0      Workforce remaining-work authority decomposition  ELIGIBLE
C1      registered BackgroundWorkContract definitions     READY — IMP-07 + existing scope/overdue primitives
C2      instruction/contract affinity + execution binding BLOCKED_DEPENDENCY — C1 + explicit owner composition
C3      durable attempt/retry/recovery progression         BLOCKED_DEPENDENCY — C2 + concrete owner logical-intent semantics
C4      event occurrence/publication/reaction contracts   ELIGIBLE FOR DECOMPOSITION — composite MS-PROT-026
C5      durable execution integration + evidence          BLOCKED_DEPENDENCY — required C2/C3/C4 children
```

Select **IMP-08C-C1**: immutable owner-qualified BackgroundWorkContract definition and exact semantic-release registry. MS-PROT-065 v1.1 §§2–4 explicitly requires registration and enumerates the contract responsibilities. This static contract slice is independent of event transport, scheduler execution and provider work; it supplies no executor, current authority or default owner semantics.

C1 is the smallest identified concrete missing prerequisite that can be implemented from accepted authority without inventing a workflow language or choosing a provider. Event implementation is not required merely to register a static future-responsibility contract. C2 must separately govern runtime binding and instruction affinity before a contract becomes executable.

IMP-08C becomes IN_PROGRESS on C1 selection. IMP-08A/B remain eligible, not complete. Provider/financial work retains its hard prerequisites. No Git branch creation is authorized.
