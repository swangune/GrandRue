# IMP-05 Onboarding Case Ownership Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding Case Stable Identity, Purpose & Merchant Scope Ownership  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

MS-PROT-052 v1.2 establishes the ordinary initial Onboarding Case as a durable merchant-scoped case with stable identity, purpose `INITIAL_CONFIGURATION`, and ownership by Merchant Account / Merchant Scope rather than by the Controller who happened to begin it.

This child implements only the semantic ownership shell. Controller identity remains an authorisation concern to be revalidated at mutation/submission time rather than case ownership.

## RED evidence

Commit:

`fa1cb7dfe7709460a3a894fce127e314043f66f2`

Message:

`test: require onboarding case merchant ownership`

GitHub Actions run `33183748311` compiled the existing production sources and then failed during test compilation because the intended production symbols did not exist:

- `OnboardingCaseIdentity`
- `OnboardingCase`
- `OnboardingCasePurpose`

No unrelated regression masked the contract.

## GREEN evidence

Final complete GREEN head:

`c6af8fcf5054e90d7685b54fdfaeeb15f7cd735b`

Message:

`feat: add merchant-owned onboarding case`

GitHub Actions Maven Tests run `33183892763` completed successfully on that exact head:

- production sources compiled: 566
- test sources compiled: 206
- unit tests: 608
- PostgreSQL integration tests: 183
- total tests: 791
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

Two mechanically partial implementation commits occurred while the three-file semantic node was written through the repository contents API:

- `bfa6e29b90c949aa6ac63713ab056f198ff430ce` — case identity only
- `68cff58cf24896436f1c374b5b6857f4d3732896` — initial purpose added

These are not conformance checkpoints. Conformance is asserted only for the complete exact head `c6af8fcf5054e90d7685b54fdfaeeb15f7cd735b` verified by the full suite.

## Conformance claims

This child proves only that:

1. Onboarding Case identity is explicit, stable as a value, and non-blank;
2. an ordinary initial Onboarding Case is explicitly owned by `MerchantScope`;
3. ordinary initial case purpose is explicitly `INITIAL_CONFIGURATION`;
4. lifecycle state is part of the case semantic shell;
5. Controller identity is not stored as ownership of the case.

## Explicit non-claims

This child does **not** define or claim completion of:

- current case revision;
- case persistence;
- one-current-initial-case-per-Merchant-Account uniqueness;
- lifecycle transition enforcement;
- optimistic concurrency;
- material mutation operations;
- logical request identity or idempotency;
- answer revision history or supersession;
- deterministic pruning/recomputation;
- final-review affinity;
- submission or immutable Initial Configuration Intent creation;
- current Controller authorisation checks;
- Merchant Account lifecycle mutation/submission guards;
- Merchant Profile, Location or Business Hours adoption;
- Configuration Revision creation, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
