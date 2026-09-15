# IMP-05 Onboarding Case Final-Review Revision Affinity Conformance Evidence

**Date:** 28 August 2026  
**Status:** CONFORMING_COMPLETE for this implementation child  
**Programme:** MS-IMP-001 — IMP-05 Merchant Definition, Configuration & Activation  
**Execution governance:** `designs/IMPLEMENTATION-RULES.md`  
**Substantive authority:** accepted MS-PROT-052 composition, including v1.2 final-review revision affinity

## Scope

This evidence closes the narrow IMP-05 child that requires an onboarding final review to identify one exact `OnboardingCase` and one exact reviewed `OnboardingCaseRevision`.

Implemented:

- immutable `OnboardingCaseReview` affinity to exact `OnboardingCaseIdentity`;
- immutable affinity to exact reviewed `OnboardingCaseRevision`;
- null rejection for both affinity values.

The contract deliberately does not interpret a review as meaning the latest/current revision and does not establish submission, activation, persistence, authorisation, concurrency, or lifecycle-transition semantics.

## Test-first evidence

RED commit:

- `a503f6a5ec91514d547ad9850174d3c94253a368` — `test: require exact onboarding final-review affinity`
- GitHub Actions run `33185027263`
- production sources compiled successfully;
- test compilation then failed only because `OnboardingCaseReview` did not yet exist.

GREEN implementation:

- `5c7f947646bf072f3c3a4e2759a657b7d911ac64` — `feat: bind onboarding final review to exact case revision`
- GitHub Actions run `33185140440`
- `mvn --batch-mode clean verify -Ppostgres-it`
- 612 unit tests, 0 failures, 0 errors, 0 skipped;
- 183 PostgreSQL integration tests, 0 failures, 0 errors, 0 skipped;
- 31 Flyway migrations validated/applied;
- `BUILD SUCCESS`.

## Invariants proved

1. A final review identifies one exact Onboarding Case.
2. A final review identifies one exact reviewed Onboarding Case revision.
3. Case identity cannot be null.
4. Reviewed revision cannot be null.
5. Review affinity does not silently float to a later/current revision.

## Explicit non-claims

This child does not claim or implement:

- durable final-review persistence;
- review approval/submission commands;
- Configuration activation or publication;
- Onboarding Case lifecycle transition mechanics;
- optimistic concurrency or compare-and-set semantics;
- idempotency policy;
- answer/evidence revision history or pruning;
- downstream recomputation;
- review presentation UX;
- Controller authorisation guards;
- audit/event emission for review or submission.

Those remain separate implementation nodes and must be selected only when their substantive authority is already governed.

## Result

The exact final-review revision-affinity child is **CONFORMING_COMPLETE**. IMP-05 remains in progress; refresh the implementation graph from the verified `development` head before selecting the next smallest READY node.
