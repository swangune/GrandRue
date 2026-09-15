# IMP-05 Onboarding Question Identity Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Stable Onboarding Question Identity  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 v1.0 §10 requires every reusable onboarding question to have a stable identity independent of merchant-facing wording, conceptually `{ namespace, identifier }`, while leaving exact serialization downstream.
- MS-PROT-052 v1.1 preserves that stable identity across a material definition change and requires the affected question definition to receive a new version rather than changing question identity.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred semantics.

## Executable contract

`OnboardingQuestionIdentity` is a value object whose equality is exactly the pair:

```text
namespace
identifier
```

Both components are required and non-blank. The object contains no merchant-facing wording and defines no serialization format.

## RED evidence

Commit:

`93be982de59ff9dd936df37e5ed7697ddc0d0000`

Message:

`test: require stable onboarding question identity`

GitHub Actions Maven Tests #1216 (`33181857858`) compiled the existing production sources and then failed during test compilation because `OnboardingQuestionIdentity` did not exist. The failure therefore represented the intended missing production contract rather than an unrelated regression.

## GREEN evidence

Commit:

`249d5f90f44ac7dfa1e360f51df44d6025f33888`

Message:

`feat: add stable onboarding question identity`

GitHub Actions Maven Tests #1217 (`33182317148`) completed successfully on that exact head:

- production sources compiled: 561
- test sources compiled: 203
- unit tests: 602
- PostgreSQL integration tests: 183
- total tests: 785
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. reusable onboarding question identity is explicit;
2. identity is independent of presentation wording;
3. identity consists of the accepted namespace/identifier pair;
4. blank identity components are rejected;
5. ordinary Java value equality preserves stable pair identity.

## Explicit non-claims

This child does **not** define or claim completion of:

- question-identity serialization;
- question-definition version representation or lifecycle;
- option identity;
- question catalogue registration;
- prompt applicability or branch pruning;
- same-priority information-gain ranking;
- discovery mappings;
- answer forms;
- answer provenance or storage;
- wording/localisation;
- save/resume persistence;
- configuration proposal, approval, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority, including MS-PROT-052 v1.1.
