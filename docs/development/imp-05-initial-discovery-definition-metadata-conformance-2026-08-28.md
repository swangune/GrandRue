# IMP-05 Initial Discovery Definition Metadata Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Initial Customer-Interaction Discovery Question Material Definition Metadata  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 v1.0 classifies the initial customer-interaction prompt as a `DISCOVERY QUESTION`, defines it as `MULTI_SELECT`, and places high-information branch discovery ahead of lower-value prompts.
- MS-PROT-052 v1.1 materially amends the initial option set and interpretation while retaining the stable question identity.
- MS-PROT-052 v1.1 §21 explicitly states that the affected question definition must receive a new material definition version and conceptually identifies the amended definition as `v2`.
- Existing production value types remain authoritative for question-definition version, prompt class, answer form and prompt priority.

## Executable contract

`InitialCustomerInteractionDiscoveryQuestion` now exposes the accepted material definition metadata for the current question:

- question definition version: `v2`
- prompt class: `DISCOVERY_QUESTION`
- answer form: `MULTI_SELECT`
- prompt priority: `HIGH_BRANCH_DISCOVERY`

These values describe the onboarding definition only. They do not create capability identity, semantic mappings, configuration values or runtime authority.

## RED evidence

Commit:

`2251ec9d511741c0aab7b73c90c1cffd47bd315e`

Message:

`test(imp-05): require initial discovery definition metadata`

GitHub Actions Maven Tests #1263 (`33191132754`) compiled 577 production sources and then attempted to compile 216 test sources. It failed with exactly four missing-method diagnostics on `InitialCustomerInteractionDiscoveryQuestion`:

- `definitionVersion()`
- `promptClass()`
- `answerForm()`
- `priority()`

No unrelated compilation failure was present.

## GREEN evidence

Commit:

`2d63a6bd9126d857442f864ca551b86fc7d1cf60`

Message:

`feat(imp-05): bind initial discovery definition metadata`

GitHub Actions Maven Tests #1264 (`33191279556`) completed successfully on that exact head:

- production sources compiled: 577
- test sources compiled: 216
- unit tests: 631
- PostgreSQL integration tests: 183
- total tests: 814
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. the current initial customer-interaction discovery definition is materially versioned as `v2`;
2. its prompt class is `DISCOVERY_QUESTION`;
3. its answer shape is `MULTI_SELECT`;
4. its sequencing class is `HIGH_BRANCH_DISCOVERY`;
5. those metadata values are bound to the stable initial discovery question identity without making merchant-facing copy semantic authority.

## Explicit non-claims

This child does **not** define or claim completion of:

- generic question-definition registration;
- discovery option-to-semantic mappings;
- semantic seed identity or capability activation;
- `NOTHING_ELSE_FOR_NOW` downstream consequence beyond already-proven selection consistency;
- `OTHER` clarification/gap handling;
- historical answer persistence/replay;
- prompt eligibility/branch-pruning implementation for this specific question;
- Initial Configuration Intent creation or handoff;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
