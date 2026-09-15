# IMP-05 Initial Discovery Question Identity Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Initial Customer-Interaction Discovery Question Identity  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

- MS-PROT-052 v1.0 §10 requires every reusable onboarding question to have a stable identity independent of merchant-facing wording.
- The accepted conceptual identity for the initial customer-interaction discovery question is `onboarding.discovery / customer-interactions`.
- MS-PROT-052 v1.1 §21 explicitly retains the stable question identity while requiring a material question-definition version change for the amended option set and interpretation.
- `OnboardingQuestionIdentity` already provides the production value-object boundary for stable onboarding question identity.
- MS-IMPLEMENTATION-RULES-001 requires test-first minimum implementation and forbids inventing deferred serialization or semantic authority.

## Executable contract

`InitialCustomerInteractionDiscoveryQuestion.IDENTITY` is the canonical production identity for the initial customer-interaction discovery question.

It is exactly:

- namespace: `onboarding.discovery`
- identifier: `customer-interactions`

The implementation deliberately establishes identity only. It does not make merchant-facing wording, option labels, discovery mappings, capability mappings, configuration values or runtime semantics authoritative.

## RED evidence

Commit:

`45795527cc4e47fc64514d157b830185dbdf9665`

Message:

`test(imp-05): require stable initial discovery question identity`

GitHub Actions Maven Tests #1260 (`33190365090`) compiled 576 production sources and then failed during compilation of 215 test sources with exactly one diagnostic: the deliberately absent `InitialCustomerInteractionDiscoveryQuestion` symbol. No unrelated failure appeared.

## GREEN evidence

Commit:

`395fa9cc763fdd88c7c3daf8f0c7809f98234e8a`

Message:

`feat(imp-05): establish stable initial discovery question identity`

GitHub Actions Maven Tests #1261 (`33190479479`) completed successfully on that exact head:

- production sources compiled: 577
- test sources compiled: 215
- unit tests: 630
- PostgreSQL integration tests: 183
- total tests: 813
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. the initial customer-interaction discovery question has one stable production identity;
2. that identity uses the accepted `onboarding.discovery / customer-interactions` conceptual split;
3. merchant-facing wording is not used as question identity;
4. the v1.1 option-set amendment does not require a new stable question identity.

## Explicit non-claims

This child does **not** define or claim completion of:

- the material question-definition version associated with v1.1;
- a generic `QuestionDefinition` aggregate;
- generic option-identity equality or serialization;
- prompt wording or localisation;
- discovery mappings or semantic-seed production;
- prompt eligibility or sequencing for this particular question;
- answer persistence or historical reconstruction;
- configuration proposal, compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from a refreshed IMP-05 dependency graph against the latest accepted authority.
