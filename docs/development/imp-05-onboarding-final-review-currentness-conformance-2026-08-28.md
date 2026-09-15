# IMP-05 Onboarding Final Review Currentness Conformance Evidence

**Date:** 28 August 2026  
**Status:** `CONFORMING_COMPLETE` for this implementation child  
**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Macro status:** `PARTIALLY_CONFORMING`  
**Execution governance:** `designs/IMPLEMENTATION-RULES.md`  
**Substantive authority:** MS-PROT-052 v1.2 — final review identity and stale-review protection

## Scope

This evidence closes the narrow IMP-05 child that determines whether a final onboarding review still describes the live Onboarding Case evidence state.

Implemented:

- exact case-identity comparison between the review and the live Onboarding Case;
- exact reviewed-revision comparison against the live case's `currentRevision`;
- a review is current only when both affinities match;
- null review or live-case inputs fail closed.

This currentness predicate deliberately does **not** establish submission completeness, merchant approval, lifecycle eligibility, current Controller authority, account eligibility or any configuration authority.

## Authority

MS-PROT-052 v1.2 requires final review to be bound to one exact Onboarding Case Revision and states that if the case changes after review, the earlier review cannot authorise submission of the newer revision.

Conceptually:

```text
reviewed revision = 14
current revision = 15
        ↓
previous review is stale
```

A new review of the new effective state is required before submission may rely on review affinity.

## Test-first evidence

### RED

Commit:

`91b57a582b404aca724701011b24061ef23e758f`

Message:

`test: require stale onboarding review detection`

GitHub Actions Maven Tests run `33185528949`:

- production sources compiled: 568;
- test sources attempted: 208;
- test compilation failed with exactly two diagnostics;
- both diagnostics were consequences of the intentionally missing `OnboardingCaseReviewCurrentness` type;
- no unrelated regression masked the contract.

### GREEN

Exact implementation head:

`a4fd0748ad5f9d6c75f7a00008624cdd856a6efd`

Message:

`feat: detect stale onboarding final review`

GitHub Actions Maven Tests run `33185667236` / #1239 completed successfully on that exact head:

- production sources compiled: 569;
- test sources compiled: 208;
- unit tests: 616;
- PostgreSQL integration tests: 183;
- total tests: 799;
- failures: 0;
- errors: 0;
- skipped: 0;
- Flyway migrations validated/applied: 31;
- result: `BUILD SUCCESS`.

## Invariants proved

1. A review is current only for the same Onboarding Case identity and exact live case revision.
2. A review becomes stale when the case advances to a different revision.
3. A review for another case is not current even when revision tokens happen to be equal.
4. Currentness evaluation requires both a review and a live Onboarding Case.

## Explicit non-claims

This child does **not** claim or implement:

- merchant approval evidence;
- review persistence;
- review generation or presentation;
- lifecycle eligibility for submission;
- submission completeness;
- current Controller or Merchant Account authority;
- automatic review invalidation side effects;
- Initial Configuration Intent creation;
- optimistic mutation concurrency;
- mutation idempotency;
- Onboarding Case persistence;
- complete IMP-05.

## Result

Exact final-review currentness/staleness detection is `CONFORMING_COMPLETE`. IMP-05 remains `PARTIALLY_CONFORMING`; the dependency graph must be refreshed from the latest verified `development` head before selecting the next READY child.
