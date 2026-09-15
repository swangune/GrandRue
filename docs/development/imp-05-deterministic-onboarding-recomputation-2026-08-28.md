# IMP-05 — Deterministic Onboarding Recomputation

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation  
**Fine-grained node:** C3 — Deterministic onboarding recomputation  
**Date:** 28 August 2026  
**Implementation head verified:** `276cc08bdfe8310c401875d8ddc049d8ca0eb0fe`  
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This node implements the deterministic onboarding-resolution boundary required by:

- `designs/MS-PROT-052 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract.md`;
- `designs/MS-PROT-052 v1.1 — Baseline Public Presence & Discovery-Axis Amendment.md`;
- `designs/MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

C3 consumes durable C2 evidence. It produces a non-executable candidate and eligible unresolved prompt frontier for one exact Onboarding Case revision.

## 2. Implemented production boundary

The implementation provides:

- an immutable prompt registry distinct from the Semantic Registry;
- exact question identity, material definition version, answer form, priority and context-scoped prompt keys;
- registered discovery mappings from exact question-version-option triples to candidate semantic seeds;
- v1.1 initial customer-interaction mappings, including no semantic seed for `NOTHING_ELSE_FOR_NOW`;
- deterministic all-of/alternative applicability clauses;
- fixed-point recomputation from effective evidence;
- branch pruning that excludes historically retained but currently inapplicable answers from candidate influence;
- preservation of shared branches where another current registered dependency independently applies;
- explicit per-version answer interpretation contracts, preventing silent reinterpretation of historical v1 evidence as current v2 evidence;
- governing priority-class ordering followed by registered information-gain ordering and a stable identity/context tie-break;
- prompt-local answer consistency and `SINGLE_SELECT`/`BOOLEAN` cardinality enforcement;
- exact PostgreSQL reconstruction of effective evidence at any durable case revision; and
- an application boundary that records an answer and recomputes from the exact committed revision.

No AI provider, model response or probabilistic decision is required.

## 3. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Current discovery answers propose only exact registered seeds | `DeterministicOnboardingRecomputationTest` | PASS |
| Corrected root answer prunes stale branch evidence | `DeterministicOnboardingRecomputationTest` | PASS |
| Independent dependency preserves a shared branch | `DeterministicOnboardingRecomputationTest` | PASS |
| Historical material question version is not silently reinterpreted | `DeterministicOnboardingRecomputationTest` | PASS |
| Explicitly registered historical interpretation remains usable | `DeterministicOnboardingRecomputationTest` | PASS |
| Priority class precedes registered information gain | `DeterministicOnboardingRecomputationTest` | PASS |
| Context-scoped prompt instances require distinct answers | `DeterministicOnboardingRecomputationTest` | PASS |
| Contradictory `NOTHING_ELSE_FOR_NOW` selection remains unresolved | `DeterministicOnboardingRecomputationTest` | PASS |
| Duplicate prompt keys and unregistered mappings are rejected | `OnboardingPromptRegistryTest` | PASS |
| Alternative applicability clauses are deterministic | `OnboardingPromptRegistryTest` | PASS |
| Single-select cardinality is enforced | `OnboardingPromptRegistryTest` | PASS |
| Historical effective evidence is reconstructed at exact revisions | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Answer application path recomputes the exact committed revision | `JooqOnboardingCaseEvidenceStoreIT` | PASS |

## 4. IMPLEMENTATION-RULES trace

The repository preserves three falsification loops:

1. `587d9023d540d8b0c20f5651222a7f27a06a1001` — initial C3 tests; Maven Tests #1294 failed at test compilation because the deterministic registry/recomputation contracts did not exist.
2. `112c3237a307b75b2f615e7754a0dd9516ba635f` — first implementation; Maven Tests #1295 passed all 652 unit and 204 PostgreSQL integration tests.
3. `2deb5ca997f3b44ae18f25054c5b18605e08a2d7` — revision-affined mutation-path test; Maven Tests #1296 failed at test compilation because the application recomputation boundary did not exist.
4. `f13555f654f625bcb7eb14e890dde1edb74b09c9` — exact committed-revision orchestration; Maven Tests #1297 passed all 652 unit and 205 PostgreSQL integration tests.
5. `6de46cf7a36115f0e95600827e5350203b6738c6` — answer-integrity falsification; Maven Tests #1298 failed exactly two unit assertions for contradictory discovery selection and single-select cardinality.
6. `276cc08bdfe8310c401875d8ddc049d8ca0eb0fe` — registered answer consistency and cardinality enforcement; Maven Tests #1299 passed the full suite.

## 5. Verified baseline

```text
workflow:                           Maven Tests
run number:                         1299
run id:                             33216777007
head:                               276cc08bdfe8310c401875d8ddc049d8ca0eb0fe
result:                             SUCCESS
Java target / CI JDK:               25 / Temurin 25
PostgreSQL:                         18.6
Flyway migrations:                 34
production Java sources compiled:  614
test Java sources compiled:        228
unit tests:                         654 PASS
PostgreSQL integration tests:      205 PASS
total Maven tests:                  859 PASS
failures / errors / skipped:       0 / 0 / 0
```

Focused C3 evidence comprises twelve unit tests and two PostgreSQL integration tests.

## 6. Explicit non-claims

C3 does not claim completion of:

- AI inference, prompt-ranking assistance or natural-language interpretation;
- the complete future capability-specific question catalogue;
- C4 final review, stale-review rejection, atomic submission or lifecycle transition;
- C5 immutable Initial Configuration Intent;
- Controller/delegated-staff application authorization;
- profile/location/business-hours fact adoption;
- Merchant Configuration revision materialisation;
- transport/UI delivery; or
- end-to-end IMP-05 completion.

Candidate semantic seeds are proposal state only. They do not activate capabilities, establish entitlement, choose the Configuration Revision semantic release or create runtime authority.

## 7. Graph consequence

C3 is **CONFORMING_COMPLETE**. C4 exact final review/submission is now the selected **READY** node. IMP-05 remains **PARTIALLY_CONFORMING** until all required children and the establishment-to-active completion proof conform.
