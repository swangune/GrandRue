# IMP-05 Onboarding Prompt Priority Conformance — 2026-08-28

**Programme:** MS-IMP-001  
**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Adaptive onboarding prompt priority taxonomy  
**Status:** CONFORMING_COMPLETE for this taxonomy only  
**Macro status:** IMP-05 remains PARTIALLY_CONFORMING

## Accepted authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- `designs/MS-PROT-052 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract.md`;
- `designs/MS-PROT-032 —Backend Module, Bounded Context & Dependency Architecture.md`.

MS-PROT-052 establishes five governing priority classes for adaptive onboarding sequencing, in order of unresolved-information/completion criticality:

1. high-branch discovery;
2. mandatory configuration decisions;
3. required structured data;
4. optional integrations;
5. optional profile enrichment.

This taxonomy is semantic sequencing authority only. It does not by itself implement an information-gain calculation, dynamic prompt ordering engine or completion/readiness evaluator.

## Implemented contract

`mainstreet.onboarding.OnboardingPromptPriority` now exposes exactly, in declaration/governing order:

- `HIGH_BRANCH_DISCOVERY`;
- `MANDATORY_CONFIGURATION_DECISION`;
- `REQUIRED_STRUCTURED_DATA`;
- `OPTIONAL_INTEGRATION`;
- `OPTIONAL_PROFILE_ENRICHMENT`.

## RED evidence

Commit:

`6140160454e792e71e79fb995bcfd37c5a1a7d2d`

`test: require onboarding prompt priority taxonomy`

Maven Tests #1213 (`33181336065`) compiled all 559 production source files and then failed during test compilation because `OnboardingPromptPriority` did not yet exist. The additional compiler noise was derivative of that unresolved symbol; no production-source regression preceded the RED failure.

## GREEN implementation

Commit:

`fcb812045dfbe6721eb0d865c75d9fef9b229f41`

`feat: add onboarding prompt priority taxonomy`

Production change:

- added `src/main/java/mainstreet/onboarding/OnboardingPromptPriority.java`;
- introduced only the five accepted ordered enum constants;
- added no persistence schema or migration;
- added no Spring, UI, compiler, configuration, AI, provider or runtime coupling;
- introduced no sequencing algorithm or scoring formula.

## Verification

Maven Tests #1214 (`33181434192`) ran against exact head:

`fcb812045dfbe6721eb0d865c75d9fef9b229f41`

Result:

- production sources compiled: 560;
- test sources compiled: 202;
- unit tests: 600;
- PostgreSQL integration tests: 183;
- total tests: 783;
- failures: 0;
- errors: 0;
- skipped: 0;
- Flyway migrations validated/applied: 31;
- `OnboardingPromptPriorityTest`: 1/1 passing;
- `OnboardingPromptClassTest`: 1/1 passing;
- architecture/corpus/conformance tests: passing;
- prototype application startup: passing;
- BUILD SUCCESS.

## Explicit non-claims

This evidence does **not** claim completion of adaptive onboarding or IMP-05.

The following remain outside this child and require their own dependency classification and conformance evidence where applicable:

- information-gain or uncertainty-reduction calculation;
- dynamic prompt-ordering engine;
- completion/readiness evaluation;
- Question Identity and question versioning;
- applicability, requiredness and dependency evaluation;
- response schema and structured-response persistence;
- option-to-semantic mappings;
- derived-fact and inference provenance;
- AI proposal/recommendation and merchant approval;
- UI rendering or answer-form selection;
- Onboarding Case lifecycle;
- Initial Configuration Intent;
- Configuration Revision creation;
- compilation and activation;
- IMP-05 macro completion.

The onboarding prompt-priority taxonomy therefore closes one exact dependency without pre-empting the later sequencing, persistence, AI, configuration or delivery architecture.
