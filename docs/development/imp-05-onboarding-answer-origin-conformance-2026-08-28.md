# IMP-05 Onboarding Answer Origin Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding Answer Origin Provenance  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 §27 requires material onboarding answers to retain provenance explaining how evidence or selection entered the proposal and identifies the conceptual origins `MERCHANT_SELECTED`, `MERCHANT_APPROVED_IN_REVIEW`, `INFERRED_PROPOSAL`, `DERIVED`, `DEFAULTED`, and `IMPORTED_EVIDENCE`.
- MS-PROT-052 explicitly states that onboarding provenance does not replace configuration provenance and that raw onboarding answers are not runtime authority.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred semantics.

## Executable contract

`OnboardingAnswerOrigin` is the exact bounded provenance taxonomy:

```text
MERCHANT_SELECTED
MERCHANT_APPROVED_IN_REVIEW
INFERRED_PROPOSAL
DERIVED
DEFAULTED
IMPORTED_EVIDENCE
```

The taxonomy records how onboarding evidence or selection entered a configuration proposal. It confers no configuration authority, runtime authority, ordering, precedence, trust level, or approval power by itself.

## RED evidence

Commit:

`f3f27bfbb5db809ae5fb5c25a759aa1c51ee6148`

Message:

`test: require onboarding answer origin provenance`

GitHub Actions Maven Tests #1248 (`33187157351`) compiled 572 production sources and then failed during test compilation of 211 test sources. All eight diagnostics were caused by the deliberately missing `OnboardingAnswerOrigin` type: seven direct missing-symbol diagnostics plus one cascading assertion-overload diagnostic. No unrelated production failure appeared.

## GREEN evidence

Commit:

`ff5b5eb512c3c363254e6a635e9dfb92616b167a`

Message:

`feat: add onboarding answer origin provenance`

GitHub Actions Maven Tests #1249 (`33187245689`) completed successfully on that exact head:

- production sources compiled: 573
- test sources compiled: 211
- unit tests: 622
- PostgreSQL integration tests: 183
- total tests: 805
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. onboarding answer origin is explicit;
2. the accepted provenance vocabulary is bounded to exactly six values;
3. no additional provenance origin is silently introduced;
4. onboarding provenance is represented separately from configuration/runtime authority.

## Explicit non-claims

This child does **not** define or claim completion of:

- a complete `OnboardingAnswer` representation;
- answer-option identity or structured-value representation;
- answer-form taxonomy;
- answer context scope;
- `answeredBy` identity/authority semantics;
- `answeredAt` temporal semantics;
- semantic-registry-release affinity;
- answer persistence or history reconstruction;
- discovery-mapping execution;
- merchant review, submission or approval;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
