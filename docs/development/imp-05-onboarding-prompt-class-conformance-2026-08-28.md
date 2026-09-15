# IMP-05 Onboarding Prompt Class Conformance — 2026-08-28

**Programme:** MS-IMP-001  
**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Onboarding prompt authority/purpose taxonomy  
**Status:** CONFORMING_COMPLETE for this taxonomy only  
**Macro status:** IMP-05 remains PARTIALLY_CONFORMING

## Accepted authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- `designs/MS-PROT-052 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract.md`;
- `designs/MS-PROT-032 —Backend Module, Bounded Context & Dependency Architecture.md`.

MS-PROT-052 classifies onboarding interactions by what each interaction is authoritative for, not by its visual presentation. The accepted taxonomy contains exactly four classes:

1. Discovery Question;
2. Configuration Decision Question;
3. Data Capture Prompt;
4. Review / Confirmation.

A fifth authority class must not be introduced merely because a UI control or presentation differs.

## Implemented contract

`mainstreet.onboarding.OnboardingPromptClass` now exposes exactly:

- `DISCOVERY_QUESTION`;
- `CONFIGURATION_DECISION_QUESTION`;
- `DATA_CAPTURE_PROMPT`;
- `REVIEW_CONFIRMATION`.

The implementation is deliberately a small semantic taxonomy. It does not define UI rendering, wording, option mappings, sequencing, persistence, configuration effects or AI authority.

## RED evidence

Commit:

`02092aa9e7d9ff6f5aade32e881766949e4e6ae1`

`test: require accepted onboarding prompt classes`

Maven Tests #1210 (`33180757260`) compiled all 558 production source files and then failed during test compilation because `OnboardingPromptClass` did not yet exist. The RED failure therefore represented the missing accepted taxonomy rather than an unrelated regression.

## GREEN implementation

Commit:

`c5580884c3416d3336b14086197a8a8731158728`

`feat: add accepted onboarding prompt classes`

Production change:

- added `src/main/java/mainstreet/onboarding/OnboardingPromptClass.java`;
- introduced only the four accepted enum constants;
- added no persistence schema or migration;
- added no Spring, web/UI, compiler, configuration, AI, provider or runtime coupling.

## Verification

Maven Tests #1211 (`33180880393`) ran against exact head:

`c5580884c3416d3336b14086197a8a8731158728`

Result:

- production sources compiled: 559;
- test sources compiled: 201;
- unit tests: 599;
- PostgreSQL integration tests: 183;
- total tests: 782;
- failures: 0;
- errors: 0;
- skipped: 0;
- Flyway migrations validated/applied: 31;
- `OnboardingPromptClassTest`: 1/1 passing;
- architecture/corpus/conformance tests: passing;
- BUILD SUCCESS.

## Explicit non-claims

This evidence does **not** claim completion of onboarding or IMP-05.

The following remain outside this child and require their own dependency classification and conformance evidence where applicable:

- Onboarding Case lifecycle;
- prompt identity/versioning;
- question-catalogue storage;
- prompt wording;
- response schemas;
- structured option-to-semantic mappings;
- applicability, requiredness and dependency rules;
- structured response persistence;
- derived-fact and inference provenance;
- AI proposal/recommendation and merchant-approval flow;
- review/confirmation presentation logic;
- Initial Configuration Intent;
- Configuration Revision creation;
- compilation and activation;
- Merchant Profile persistence;
- IMP-05 macro completion.

The onboarding prompt-class taxonomy therefore closes one exact semantic dependency without pre-empting later workflow, configuration, persistence, AI or delivery decisions.
