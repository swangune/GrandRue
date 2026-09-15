# IMP-05 Initial Discovery Selection Consistency Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Initial Customer-Interaction Discovery Selection Consistency  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 v1.1 §6 defines `NOTHING_ELSE_FOR_NOW` as an onboarding-control outcome that requests no additional customer-interaction semantic and does not become capability, configuration or runtime semantics.
- MS-PROT-052 v1.1 §7 makes `NOTHING_ELSE_FOR_NOW` mutually exclusive with positive additional-interaction selections in the same discovery prompt.
- The same section states that `OTHER` represents additional desired interaction not captured by listed options and that, where it expresses material intent, `NOTHING_ELSE_FOR_NOW` no longer applies.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred semantics.

## Executable contract

`InitialCustomerInteractionDiscoverySelectionConsistency` determines only whether one initial customer-interaction discovery selection set is internally contradictory under the accepted control-outcome rule.

A selection is consistent when either:

1. `NOTHING_ELSE_FOR_NOW` is absent; or
2. `NOTHING_ELSE_FOR_NOW` is the sole selected option.

Accordingly, `NOTHING_ELSE_FOR_NOW` combined with any other selected option is inconsistent. Ordinary positive selections and `OTHER` are not rejected by this rule merely because several are selected together.

The policy does not decide answer completeness, minimum cardinality, semantic seed production, clarification, configuration gaps or onboarding progression.

## RED evidence

Commit:

`d87a9b050f10334afb9009e962d41215cf66ec68`

Message:

`test: require initial discovery selection consistency`

GitHub Actions Maven Tests #1257 (`33188508764`) compiled 575 production sources and then failed during test compilation of 214 test sources with exactly two diagnostics, both caused by the deliberately absent `InitialCustomerInteractionDiscoverySelectionConsistency` type. No unrelated failure appeared.

## GREEN evidence

Commit:

`b5c09f6371a2fa1ad4b50ee062a9e36a4136c9f4`

Message:

`feat: enforce initial discovery control consistency`

GitHub Actions Maven Tests #1258 (`33188612352`) completed successfully on that exact head:

- production sources compiled: 576
- test sources compiled: 214
- unit tests: 629
- PostgreSQL integration tests: 183
- total tests: 812
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. `NOTHING_ELSE_FOR_NOW` selected alone is not internally contradictory;
2. `NOTHING_ELSE_FOR_NOW` selected with any other initial-discovery option is contradictory;
3. ordinary positive selections may coexist under this consistency rule;
4. `OTHER` may coexist with a positive selection without creating the `NOTHING_ELSE_FOR_NOW` control conflict;
5. the control rule creates no executable business semantic.

## Explicit non-claims

This child does **not** define or claim completion of:

- whether an empty selection is valid or complete;
- minimum or maximum answer cardinality beyond the accepted control contradiction;
- semantic seed mapping for positive selections;
- the `NOTHING_ELSE_FOR_NOW` no-seed execution path;
- `OTHER` clarification or configuration-gap handling;
- generic onboarding option-identity equality scope;
- complete onboarding-answer representation;
- answer persistence or historical reconstruction;
- merchant review, submission or approval;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
