# IMP-05 Onboarding Question Definition Version Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding Question Definition Version Identity  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 §11 requires a material onboarding question definition to be versioned when a change could alter its option set, semantic/discovery mapping, applicability contract, answer interpretation or context meaning.
- MS-PROT-052 requires old stored answers not to silently acquire new semantic meaning because a later question definition changed.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred semantics.

## Executable contract

`OnboardingQuestionDefinitionVersion` is an opaque value object representing exact material-definition affinity.

The identifier is required and non-blank. Equality is exact value equality.

The type deliberately does not define numeric ordering, contiguous sequencing, compatibility ranges, migration semantics or serialization rules because the accepted authority does not require those semantics at this boundary.

## RED evidence

Commit:

`38c28f3e16679bda138ae642ae21381204fe0773`

Message:

`test: require onboarding question definition version`

GitHub Actions Maven Tests #1245 (`33186548195`) compiled 571 production sources and then failed during test compilation with seven diagnostics, all caused by the deliberately missing `OnboardingQuestionDefinitionVersion` type. No unrelated production or test failure appeared.

## GREEN evidence

Commit:

`00a73773a35bfafb70c95cedcbd48b2f816dcfbd`

Message:

`feat: add onboarding question definition version`

GitHub Actions Maven Tests #1246 (`33186666698`) completed successfully on that exact head:

- production sources compiled: 572
- unit tests: 621
- PostgreSQL integration tests: 183
- total tests: 804
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. material onboarding question-definition version identity is explicit;
2. the version identifier cannot be null or blank;
3. exact value equality can preserve historical definition affinity;
4. the representation does not silently impose ordering or compatibility semantics absent from accepted authority.

## Explicit non-claims

This child does **not** define or claim completion of:

- question-definition aggregate or catalogue registration;
- rules deciding whether a concrete definition change is material;
- version sequencing, ordering or compatibility;
- option identity binding to a definition version;
- answer persistence or historical answer reconstruction;
- answer-origin provenance;
- context-scoped answer representation;
- discovery mapping registration or execution;
- prompt applicability, branch pruning or adaptive sequencing;
- merchant review, submission or approval;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
