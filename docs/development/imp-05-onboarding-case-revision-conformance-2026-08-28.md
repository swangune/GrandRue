# IMP-05 Onboarding Case Revision Conformance Evidence

**Date:** 28 August 2026  
**Status:** CONFORMING_COMPLETE for this implementation child  
**Programme:** MS-IMP-001 — IMP-05 Merchant Definition, Configuration & Activation  
**Execution governance:** `designs/IMPLEMENTATION-RULES.md`  
**Substantive authority:** accepted MS-PROT-052 composition (v1.0 + v1.1 + v1.2)

## Scope

This evidence closes the narrow IMP-05 child that binds an ordinary initial `OnboardingCase` to its exact current logical revision.

Implemented:

- `OnboardingCaseRevision` as an opaque, non-blank exact revision identity;
- `OnboardingCase.currentRevision` as a required value;
- exact revision preservation through the Onboarding Case identity shell.

The revision type deliberately does **not** imply numeric sequencing, ordering, persistence strategy, lifecycle transition semantics, optimistic concurrency, or replacement rules.

## Test-first evidence

RED commit:

- `12b29a2fef6c53f66640b33a4aa43898aee0240a` — `test: require exact onboarding case revision`
- GitHub Actions run `33184350572` / Maven Tests #1230
- production sources compiled first;
- test compilation then failed only because `OnboardingCaseRevision`, the four-argument `OnboardingCase` constructor, and `currentRevision()` did not yet exist.

GREEN implementation:

- `10803e3962e2025bc7e7c09f493bbb1af7d884ae` — `feat: bind onboarding case to exact current revision`
- GitHub Actions run `33184742493` / Maven Tests #1232
- `mvn --batch-mode clean verify -Ppostgres-it`
- 609 unit tests, 0 failures, 0 errors, 0 skipped;
- 183 PostgreSQL integration tests, 0 failures, 0 errors, 0 skipped;
- 31 Flyway migrations validated/applied;
- `BUILD SUCCESS`.

## Invariants proved

1. An Onboarding Case revision has an explicit stable value identity.
2. Blank/null revision values are rejected.
3. An `OnboardingCase` cannot exist without a current revision.
4. The case exposes the exact revision supplied to it.
5. Revision identity is opaque; this implementation does not introduce ordering or version-number semantics.

## Explicit non-claims

This child does not claim or implement:

- durable Onboarding Case persistence;
- one-current-initial-case uniqueness per Merchant Scope;
- revision ordering or monotonic numbering;
- revision mutation/transition mechanics;
- optimistic concurrency or compare-and-set behaviour;
- idempotency policy;
- answer/evidence revision history;
- answer pruning or downstream recomputation;
- final-review revision affinity;
- submission/activation semantics;
- Controller authorisation guards beyond previously accepted ownership boundaries.

Those remain separate implementation nodes and must be selected only when their substantive authority is already governed.

## Result

The exact-current-revision identity child is **CONFORMING_COMPLETE**. IMP-05 remains in progress; the implementation graph must be refreshed from the verified `development` head before selecting the next smallest READY node.
