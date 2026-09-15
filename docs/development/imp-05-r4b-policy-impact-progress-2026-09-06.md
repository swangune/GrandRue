# IMP-05-R4B — Effective policy change and owner-coverage increment

**Rules:** IMPLEMENTATION-RULES v1.7. **Node state:** IN_PROGRESS. **Entry baseline:** clean `development@c2e69753b218e719baf9f8db57532099b8a25301`, equal to origin; baseline CI `34031221257` SUCCESS. Preceding Booking implementation `ffa057b51c70177c20fb2fcd2ad87ae5f118ce9d`, CI `34030941696`: SUCCESS, 1146 unit/governance + 403 PostgreSQL tests, zero failures/errors/skips.

## Exact authority

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §20 Configuration diff; §21 Impact analysis; §22 Impact classifications; §§23–24 protected commitments and affinity.
- `designs/MS-PROT-047 — Capability Configuration Contract.md`, v1.0 §4 Semantic ownership, §5 Configuration definition is not configuration presentation, §11 Resolution states and provenance. Policy identifiers and enum spellings do not themselves authorize business-effect interpretation. The example confirmation policy in §5 is not installed as a universal production policy.
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §4 Semantic-registry affinity, §5 Validation and Resolved Configuration Package production.
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §10 Business-Facing Review Boundary, §11 Impact-Evidence Production Predicate.
- `designs/IMPLEMENTATION-RULES.md`, v1.7 §§30, 46–48, §52 canonical loop, §§52.14–52.19 and §53.2.

## Implemented scope

`src/main/java/mainstreet/semantic/configuration/ResolvedPolicyImpactAssessment.java`, `assess`: compare effective applicable policy values from the candidate's validated executable model and the exact base compiled under its pinned historical release. Owner and policy identity remain separate keys. Initial applicability and deactivation are represented by absent before/after values. Deterministic owner/policy ordering does not depend on map iteration.

`ResolvedPolicyChange.java` retains each effective value's governing semantic release. Equal value spellings under different releases still require owner assessment; this is a review-coverage requirement, not an assertion that every release change materially changes business behavior. Within the same release, DEFAULTED to EXPLICITLY_SELECTED with an identical effective value does not invent a value change. Raw selection/provenance differences remain part of immutable revision history, not automatically operational effects.

`PolicyImpactAssessment.java` requires read-only owner interpretation for the exact before/after releases and applicable commitment facts. The aggregate preflights registration for every changed applicable policy before invoking any owner. Missing coverage, owner failure/null result or absent business-facing content prevents completion. No fallback description is generated from internal identifiers, and no permissive no-op implementation is supplied.

Paradigm fit: the compiler resolves static semantics; Configuration compares immutable resolved inputs and checks coverage; policy owners interpret business effects. Presentation is not added to semantic policy definitions. No new policy meaning, migration, external mutation or library is introduced.

## Verification and falsification scope

`src/test/java/mainstreet/semantic/configuration/ResolvedPolicyImpactAssessmentTest.java`: real compiler/package resolver, registered fixture policies, and fixture owner assessments. Cases cover changed release defaults without selection edits, same-release provenance-only changes, initial applicability/deactivation, equal spellings across releases, missing coverage before any owner call, capability-scoped identities and stable ordering, failed/null/empty owner contributions, and invalid unchanged-change construction.

Tests-first RED failed compilation on missing policy assessment/change contracts. Targeted GREEN passed all 17 policy/orchestration tests. Full local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test`: BUILD SUCCESS, 1154 tests, zero failures/errors/skips. `git diff --check` passed. Implementation baseline `3e5bd88c4f6ea0358ec512330d05bf642c0b5f6e`, Java 25/PostgreSQL 18 CI `34031701439`, job `101482291922`: SUCCESS, 1154 unit/governance + 403 PostgreSQL tests, zero failures/errors/skips. This verifies the bounded policy-comparison/coverage increment, not full R4B completion.

Exact trace locations: `ResolvedPolicyImpactAssessment.assess` :34 and `effectiveValues` :70; `ResolvedPolicyChange` :14 and `EffectiveValue` :28; `PolicyImpactAssessment.assess` :14. Test methods in `ResolvedPolicyImpactAssessmentTest` begin at :29, :47, :60, :75, :88, :99, :115 and :128. These prove comparison/coverage behavior with fixture-owned meanings, not production registration of the example policies.

Counterevidence found during review: comparing only selected values misses changed defaults; comparing only effective value spellings misses possible changed release semantics. Both cases now have explicit regressions. This does not establish the truth of fixture-owned business descriptions. Actual per-policy production assessment registrations and supported-release interpretation remain unfinished R4B work. Binding/routing effects, other capability effects and applicable owner-backed commitment conflicts also remain open. R4C durable end-to-end integration stays blocked. No R4B, R4 or IMP-05 completion is claimed.
