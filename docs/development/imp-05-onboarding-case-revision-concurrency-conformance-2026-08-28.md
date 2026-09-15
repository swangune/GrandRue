# IMP-05 Onboarding Case Revision Concurrency Conformance Evidence

**Date:** 28 August 2026  
**Status:** `CONFORMING_COMPLETE` for this implementation child  
**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Macro status:** `PARTIALLY_CONFORMING`  
**Execution governance:** `designs/IMPLEMENTATION-RULES.md`  
**Substantive authority:** MS-PROT-052 v1.2 — mutation concurrency

## Scope

This evidence closes the narrow IMP-05 child that enforces expected-current Onboarding Case revision matching before a material onboarding mutation may proceed.

Implemented:

- `OnboardingCaseRevisionConcurrency.requireCurrent(...)`;
- exact equality between the caller's expected revision and the live case's `currentRevision`;
- dedicated `OnboardingCaseRevisionConflictException` when the expected revision is stale;
- fail-closed null handling for expected revision and live case.

The guard deliberately does not generate a successor revision and does not perform a mutation itself.

## Authority

MS-PROT-052 v1.2 requires every onboarding mutation to be conditional on the caller's expected current case revision or an equivalent concurrency predicate.

Conceptually:

```text
browser A reads revision 12
browser B reads revision 12

A writes
    12 → 13

B tries to write against 12
    → CASE_REVISION_CONFLICT
```

Main Street must reject the stale mutation rather than silently merge or overwrite material merchant intent.

## Test-first evidence

### RED

Commit:

`3aa59161ac42a9ac5d4fb1dc13c6785180bd8912`

Message:

`test: require onboarding case revision concurrency`

GitHub Actions Maven Tests run `33186031898` / #1241:

- production sources compiled: 569;
- test sources attempted: 209;
- test compilation failed with exactly three diagnostics;
- all three diagnostics were consequences of the intentionally missing `OnboardingCaseRevisionConcurrency` and `OnboardingCaseRevisionConflictException` production types;
- no unrelated regression masked the contract.

### GREEN

Exact complete implementation head:

`4f122b912a11a5535dfbc3356885f78ab14b619e`

Message:

`feat: add onboarding case revision conflict`

GitHub Actions Maven Tests run `33186233186` / #1243 completed successfully on that exact head:

- production sources compiled: 571;
- test sources compiled: 209;
- unit tests: 619;
- PostgreSQL integration tests: 183;
- total tests: 802;
- failures: 0;
- errors: 0;
- skipped: 0;
- Flyway migrations validated/applied: 31;
- result: `BUILD SUCCESS`.

A connector write created a transient intermediate commit, `acf4460b709269be4b7d00323caf2d47633bbd5a`, containing only the guard before the dedicated exception file was present. That transient state is not an accepted implementation checkpoint and is not used as conformance evidence. The evidence target is the complete head above.

## Invariants proved

1. An exact expected Onboarding Case Revision is accepted.
2. A stale expected revision is rejected with a dedicated case-revision conflict.
3. Expected revision and live Onboarding Case are both mandatory.
4. The concurrency guard does not itself generate revisions or mutate onboarding evidence.
5. Stale concurrent merchant decisions are not silently merged by this boundary.

## Explicit non-claims

This child does **not** claim or implement:

- Onboarding Case persistence;
- actual answer/evidence mutation orchestration;
- successor revision generation;
- logical request identity or mutation idempotency;
- answer supersession/history persistence;
- final-review or submission completeness;
- current Controller or Merchant Account authority;
- Initial Configuration Intent creation;
- submission atomicity or handoff;
- complete IMP-05.

## Result

Expected-current Onboarding Case revision concurrency protection is `CONFORMING_COMPLETE`. IMP-05 remains `PARTIALLY_CONFORMING`; the dependency graph must be refreshed from the latest verified `development` head before selecting the next READY child.
