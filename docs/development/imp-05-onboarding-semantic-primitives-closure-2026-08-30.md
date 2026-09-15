# IMP-05 C1 — Onboarding Semantic Primitives Closure

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Child node:** C1 — Onboarding semantic primitives
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE — existing-code satisfaction**

## Closure basis

The graph previously marked C1 `PARTIAL` while the current conformance map
already classified onboarding question/answer semantics as a conforming
foundation. This evidence resolves that stale implementation classification;
it does not create or amend semantic authority.

Composite MS-PROT-052 through v1.2 governs the semantic primitives. The
repository already contains the required bounded types for:

- the four accepted prompt classes;
- stable question identity independent of wording;
- material question-definition version affinity;
- bounded answer forms and answer constraints;
- stable registered option identity independent of display labels;
- prompt applicability, exact context scope and deterministic eligibility;
- the five accepted prompt-priority classes;
- completion requirements and blocking/unresolved outcomes;
- registered discovery mappings and non-executable semantic seeds;
- answer origin/provenance; and
- registered prompt definitions and catalogue lookup.

These primitives remain separate from C2 durable case evidence, C3
deterministic recomputation, C4 exact review/submission and C5 Initial
Configuration Intent, each of which already has its own completion evidence.

## Existing RED → GREEN trace

The primitives were introduced through individually recorded RED → GREEN
children, including:

- prompt class and priority taxonomy;
- question identity and question-definition version;
- answer form and answer origin;
- prompt eligibility;
- case/review identity and currentness support used by later C children; and
- registered discovery option/definition/selection consistency.

Their evidence documents retain the original missing-symbol RED failures,
minimum production changes and canonical verification runs. Existing-code
satisfaction under MS-IMP-001 therefore applies; reimplementing the same
primitives would be ceremonial duplication.

## Current verification

The isolated A3d canonical run also compiled the complete current onboarding
model and passed all onboarding unit/conformance tests as part of:

```text
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          297 PASS
total Maven tests:                     971 PASS
Flyway migrations:                     51 validated
result:                                BUILD SUCCESS
```

## AI condition classification

MS-IMP-001 scopes an AI proposal boundary only **where required**. The current
IMP-05 slice contains no production AI inference/provider path and composite
MS-PROT-052 requires deterministic onboarding to remain fully operable without
AI. No inference is being allowed to mutate Onboarding, Profile or
Configuration authority.

The production merchant-assistance/inference boundary remains explicitly owned
by IMP-08A after the IMP-07 programme gate. It is therefore
`CONDITION_NOT_ACTIVE` for current IMP-05 completion, not a license to add an
ungoverned AI shortcut and not a claim that IMP-08A is complete.

## Graph consequence

C1 through C5 are now **CONFORMING_COMPLETE**. The remaining IMP-05 critical
path is the E runtime applicability/eligibility composition assessment followed
by the establishment-to-definition-to-configuration-to-activation proof.
