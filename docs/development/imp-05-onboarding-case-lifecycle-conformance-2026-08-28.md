# IMP-05 Onboarding Case Lifecycle Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding Case Lifecycle  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

MS-PROT-052 v1.2 establishes the initial Onboarding Case lifecycle as exactly:

```text
IN_PROGRESS
SUBMITTED
COMPLETED
ABANDONED
```

`COMPLETED` and `ABANDONED` are terminal. `IN_PROGRESS` and `SUBMITTED` are non-terminal. This child implements only that lifecycle vocabulary and terminal-state property.

## RED evidence

Commit:

`a53f66ce15761ea6b4de6df031b6eff4054fd0fa`

Message:

`test: require onboarding case lifecycle semantics`

GitHub Actions run `33183240021` compiled 562 production sources and then failed during test compilation because `OnboardingCaseLifecycle` did not exist. The RED condition was therefore the intended absent production contract rather than an unrelated regression.

## GREEN evidence

Commit:

`f9dfd47e22ae6f896116ffe862ae6b1c690a2726`

Message:

`feat: add onboarding case lifecycle semantics`

GitHub Actions Maven Tests run `33183356606` completed successfully on that exact head:

- production sources compiled: 563
- test sources compiled: 205
- unit tests: 605
- PostgreSQL integration tests: 183
- total tests: 788
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. the accepted initial Onboarding Case lifecycle vocabulary is explicit;
2. the lifecycle exposes exactly the four accepted states;
3. `COMPLETED` and `ABANDONED` are terminal;
4. `IN_PROGRESS` and `SUBMITTED` are non-terminal.

## Explicit non-claims

This child does **not** define or claim completion of:

- lifecycle transition enforcement;
- stable Onboarding Case identity;
- Merchant Scope ownership;
- one-current-initial-case uniqueness;
- durable persistence;
- case revisioning;
- optimistic concurrency;
- request idempotency;
- answer history or supersession;
- pruning/recomputation;
- review affinity;
- submission or Initial Configuration Intent creation;
- Controller authority or Merchant Account lifecycle guards;
- profile/location/business-hours adoption;
- configuration compilation or activation;
- IMP-05 as a whole.

The next target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
