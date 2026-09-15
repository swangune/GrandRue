# IMP-08C-C2B — Standing Free Owner Binding / Current Revalidation Conformance

**Date:** 14 September 2026  
**Programme node:** `IMP-08C-C2B`  
**Parent:** `IMP-08C-C2`  
**Classification:** Class C — cross-boundary durable execution / authority-sensitive owner composition  
**Branch:** `development`

## Accepted authority

This closure is bounded by the accepted authority already identified for C2B:

- composite MS-PROT-065 v1.1 — durable Background Work execution, current eligibility and bounded scheduled-principal rules;
- composite MS-PROT-067 v1.1 — execution evidence / reliability boundaries applicable to durable work;
- composite MS-PROT-076 v1.1 — trusted execution / authority composition applicable to the owner path;
- MS-PROT-041 §12 — current authority must be revalidated at the owning boundary;
- `designs/IMPLEMENTATION-RULES.md` §64.1 / §64.4 — durable instruction is not future authority and owner semantics remain with the owning operation.

The prior assessment `docs/development/imp-08c-c2b-assessment-c4a-selection-2026-09-06.md` correctly rejected a generic callback bypass. The missing executable owner path later became available through the accepted Standing Free / Merchant Account establishment path.

## Implementation evidence

Implementation commit:

`3fd21066319c7f1bd8720eb775e669348f5c4165`

Primary implementation locations:

- `src/main/java/mainstreet/application/StandingFreeBackgroundWorkExecution.java`
- `src/main/java/mainstreet/application/StandingFreeBackgroundWorkContract.java`
- `src/main/java/mainstreet/runtime/ScheduledBackgroundWorkExecutionAuthority.java`
- `src/main/java/mainstreet/runtime/RegisteredScheduledBackgroundWorkExecutionAuthority.java`
- `src/main/java/mainstreet/merchantaccount/MerchantAccountEstablishedOccurrenceLookup.java`
- `src/main/java/mainstreet/infrastructure/persistence/merchantaccount/JooqMerchantAccountEstablishmentPublicationOutbox.java`

Focused executable proof:

- `src/test/java/mainstreet/application/StandingFreeBackgroundWorkExecutionTest.java`

The focused C2B suite proves the bounded path through registered historical contract affinity, current contract registration, due-time gating, authoritative Merchant Account establishment reconstruction, exact Merchant Scope revalidation, bounded scheduled-principal establishment, current Commercial eligibility and invocation of the real Standing Free owner operation. It also rejects retired/unregistered contracts, missing authoritative source, merchant-scope substitution, premature execution and unauthorised scheduled execution.

## Completion-contract mapping

C2B completion contract:

> At least one registered work kind invokes its real owning operation and revalidates current execution eligibility before side effects.

Satisfied by the registered Standing Free reconciliation work kind:

1. the durable instruction resolves its exact captured `BackgroundWorkContractAffinity` against the historical registry;
2. the captured Standing Free contract must still be present in the current Background Work Contract registry;
3. execution must be due;
4. the Merchant Account establishment is reconstructed from authoritative durable owner evidence rather than trusted from the work payload;
5. the captured Merchant Scope must equal the authoritative occurrence scope;
6. the current contract resolves a registered bounded scheduled principal;
7. the real Commercial owner operation is invoked;
8. that owner operation revalidates the current Standing Free / actor-authority conditions before creating or resolving the owner outcome.

The durable instruction therefore remains historical intent, not future authority.

## Positive conformance evidence

- Historical contract affinity is preserved and resolved exactly.
- Current contract registration is required at execution time.
- Current owner/source state is re-read from authoritative Merchant Account evidence.
- Merchant Scope is revalidated rather than accepted from the durable instruction alone.
- Scheduled execution authority is explicit and contract-bounded.
- Business semantics and side effects remain in the Commercial owner operation.
- Committed Standing Free outcome reuse remains owner-controlled and idempotent.

## Negative / falsification evidence

The executable tests reject the following counterexamples:

- legacy or unregistered work being treated as executable authority;
- a historical contract whose current registration has been retired;
- executing before `dueAt`;
- a missing/non-authoritative establishment source;
- substitution of another Merchant Scope;
- a scheduled principal not registered for the current contract;
- a scheduled authority returning a different Merchant Scope;
- bypassing the actual Commercial owner operation with a generic callback.

## Full verification evidence

Authoritative repository gate:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Current `development` head before this closure: `28a7df04f89f9ff3aa2e09e8bbd1b33ec29f8cc3`.

GitHub Actions run `34791015293`, job `103815114636`, completed successfully; the `Run unit and PostgreSQL integration tests` step is `success` on the repository PostgreSQL 18 service.

A separate local execution of the same full gate on 14 September 2026, after correcting the test PostgreSQL endpoint, completed:

```text
Tests run: 1244, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Total time: 46.944 s
```

The preceding all-integration-test failure was environmental: Flyway could not connect to the configured PostgreSQL port. It is not counted as semantic or implementation failure evidence for C2B.

## Explicit non-claims

This closure does **not** claim implementation of:

- a generic durable-work callback framework;
- arbitrary work-payload execution;
- C3 attempt start/progression/failure/outcome/retry/uncertainty ledger semantics;
- scheduler non-overlap or generic scheduler backend selection;
- C4B2 event discovery/publication integration;
- C4E Booking acknowledgement correction;
- C5 operational evidence/reconciliation portfolio;
- broader recurring-work semantics.

## Closure

`IMP-08C-C2B`: **CONFORMING_COMPLETE**.

With C2A already `CONFORMING_COMPLETE`, parent `IMP-08C-C2` is now **CONFORMING_COMPLETE** for registered contract affinity plus at least one real owner-bound execution path with current revalidation.

The hard dependency for `IMP-08C-C3 — Execution failures/outcome ledger` is satisfied. C3 may therefore move from `BLOCKED_DEPENDENCY` to `READY`; no claim is made that C3 itself is implemented.
