# IMP-05 Onboarding Prompt Eligibility Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding Prompt Eligibility Outcome  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 defines onboarding prompt eligibility as the deterministic result of evaluating a registered applicability condition against current authoritative/candidate state.
- The accepted result vocabulary is exactly `ELIGIBLE` / `INELIGIBLE`.
- MS-PROT-052 explicitly prohibits AI from overriding prompt applicability.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred semantics.

## Executable contract

`OnboardingPromptEligibility` is the exact bounded outcome taxonomy:

```text
ELIGIBLE
INELIGIBLE
```

The type represents only the result of applicability evaluation. It does not define the registered applicability contract, choose the next prompt, establish candidate semantic state, or grant AI authority to alter eligibility.

## RED evidence

Commit:

`50d960e60677e634a9bd13de6dd13e9c80b0758d`

Message:

`test: require onboarding prompt eligibility outcome`

GitHub Actions Maven Tests #1254 (`33188028457`) compiled 574 production sources and then failed during test compilation of 213 test sources. Four diagnostics were emitted: three direct missing-symbol diagnostics caused by the deliberately absent `OnboardingPromptEligibility` type and one cascading assertion/type diagnostic. No unrelated production failure appeared.

## GREEN evidence

Commit:

`5fed21c709d0f652e982f3f0c5590d95a1d7720c`

Message:

`feat: add onboarding prompt eligibility outcome`

GitHub Actions Maven Tests #1255 (`33188144778`) completed successfully on that exact head:

- production sources compiled: 575
- test sources compiled: 213
- unit tests: 624
- PostgreSQL integration tests: 183
- total tests: 807
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. onboarding prompt eligibility outcome is explicit;
2. the accepted result vocabulary is bounded to exactly two values;
3. no third or indeterminate executable eligibility state is silently introduced;
4. eligibility result representation remains separate from applicability ownership and evaluation.

## Explicit non-claims

This child does **not** define or claim completion of:

- registered prompt applicability conditions;
- an applicability evaluator;
- candidate-configuration/frontier construction;
- adaptive prompt ranking or information-gain selection;
- AI ranking or proposal behaviour;
- initial-discovery selection consistency;
- option identity scope;
- complete onboarding-answer representation;
- answer persistence or historical reconstruction;
- merchant review, submission or approval;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
