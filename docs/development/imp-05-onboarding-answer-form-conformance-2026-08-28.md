# IMP-05 Onboarding Answer Form Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding Answer Form Taxonomy  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-039 defines the bounded onboarding answer forms `SINGLE_SELECT`, `MULTI_SELECT`, `BOOLEAN`, `QUANTITY`, `DURATION`, `TEXT`, and `STRUCTURED_VALUE`.
- MS-PROT-052 §13 preserves that bounded answer-form approach and states that the vocabulary describes merchant interaction shape rather than creating new semantic value types.
- MS-PROT-052 specifically rejects treating `MULTI_SELECT` as permission for an arbitrary set where the owning configuration contract does not permit it.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred semantics.

## Executable contract

`OnboardingAnswerForm` is the exact bounded interaction-shape taxonomy:

```text
SINGLE_SELECT
MULTI_SELECT
BOOLEAN
QUANTITY
DURATION
TEXT
STRUCTURED_VALUE
```

The taxonomy describes how an onboarding answer is presented and collected. It does not create, widen or replace the semantic value types accepted by configuration-owning contracts.

## RED evidence

Commit:

`777d65c793c43421eb23c8ce5377c8f3fb5593c2`

Message:

`test: require onboarding answer form taxonomy`

GitHub Actions Maven Tests #1251 (`33187525905`) compiled 573 production sources and then failed during test compilation of 212 test sources. Nine diagnostics were emitted: eight direct missing-symbol diagnostics caused by the deliberately absent `OnboardingAnswerForm` type and one cascading assertion-overload diagnostic. No unrelated production failure appeared.

## GREEN evidence

Commit:

`aa7c2a7990d89522c50f8195a1eb654e0ba90430`

Message:

`feat: add onboarding answer form taxonomy`

GitHub Actions Maven Tests #1252 (`33187623492`) completed successfully on that exact head:

- production sources compiled: 574
- test sources compiled: 212
- unit tests: 623
- PostgreSQL integration tests: 183
- total tests: 806
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. onboarding answer form is explicit;
2. the accepted answer-form vocabulary is bounded to exactly seven values;
3. no additional answer form is silently introduced;
4. interaction shape remains distinct from the semantic value types owned by configuration contracts.

## Explicit non-claims

This child does **not** define or claim completion of:

- per-question answer-form applicability;
- option identity or option catalogues;
- question-definition aggregate registration;
- answer-option or structured-value representation;
- answer context scope;
- complete `OnboardingAnswer` representation;
- answer persistence or historical reconstruction;
- discovery mappings;
- prompt applicability, branch pruning or adaptive sequencing;
- merchant review, submission or approval;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
