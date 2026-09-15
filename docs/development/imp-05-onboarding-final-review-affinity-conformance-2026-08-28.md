# IMP-05 Onboarding Final Review Affinity Conformance Evidence

**Date:** 28 August 2026  
**Status:** `CONFORMING_COMPLETE` for this implementation child  
**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Macro status:** `PARTIALLY_CONFORMING`  
**Execution governance:** `designs/IMPLEMENTATION-RULES.md`  
**Substantive authority:** MS-PROT-052 v1.2 — final review identity

## Scope

This evidence closes the narrow IMP-05 child that represents final-review affinity to one exact Onboarding Case revision.

Implemented:

- `OnboardingCaseReview` as an immutable association between one `OnboardingCaseIdentity` and one exact `OnboardingCaseRevision`;
- exact equality semantics inherited from the record representation, so changing the reviewed revision changes the review affinity;
- mandatory non-null case identity and reviewed revision.

The review representation deliberately does **not** itself authorise submission, establish merchant approval, determine staleness against a live case, or create an Initial Configuration Intent.

## Authority

MS-PROT-052 v1.2 requires final review to be bound to one exact Onboarding Case Revision:

```text
Review
{
    onboardingCaseIdentity
    reviewedRevision
}
```

If the case changes after review, that previous review cannot authorise submission of the newer revision. The current child implements only the immutable affinity representation required before that currentness/staleness rule can be enforced.

## Test-first evidence

### RED

Commit:

`a503f6a5ec91514d547ad9850174d3c94253a368`

Message:

`test: require exact onboarding final review affinity`

GitHub Actions Maven Tests run `33185027263` / #1234:

- production sources compiled: 567;
- test sources attempted: 207;
- test compilation failed only because `OnboardingCaseReview` did not exist;
- eight compiler diagnostics were all consequences of that missing intended production type;
- no unrelated regression masked the contract.

### GREEN

Exact implementation head:

`5c7f947646bf072f3c3a4e2759a657b7d911ac64`

Message:

`feat: bind onboarding final review to exact case revision`

GitHub Actions Maven Tests run `33185140440` / #1235 completed successfully on that exact head:

- production sources compiled: 568;
- test sources compiled: 207;
- unit tests: 612;
- PostgreSQL integration tests: 183;
- total tests: 795;
- failures: 0;
- errors: 0;
- skipped: 0;
- Flyway migrations validated/applied: 31;
- result: `BUILD SUCCESS`.

## Invariants proved

1. A final review identifies exactly one Onboarding Case.
2. A final review identifies exactly one reviewed Onboarding Case Revision.
3. Two review affinities for the same case but different reviewed revisions are distinct.
4. Case identity and reviewed revision are mandatory.

## Explicit non-claims

This child does **not** claim or implement:

- review persistence;
- review generation or presentation;
- merchant approval evidence;
- currentness/stale-review evaluation against the live Onboarding Case;
- invalidation after mutation;
- submission completeness;
- current Controller authorisation;
- Initial Configuration Intent creation;
- submission atomicity;
- optimistic mutation concurrency;
- logical request idempotency;
- Onboarding Case persistence;
- complete IMP-05.

## Result

The exact final-review affinity representation is `CONFORMING_COMPLETE`. IMP-05 remains `PARTIALLY_CONFORMING`; the dependency graph must be refreshed from the latest verified `development` head before selecting the next READY child.
