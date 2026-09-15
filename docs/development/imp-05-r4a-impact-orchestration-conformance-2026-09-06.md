# IMP-05-R4A — Exact-result impact analysis orchestration

**Rules:** IMPLEMENTATION-RULES v1.7. **State:** CONFORMING_COMPLETE for scoped orchestration. **Baseline:** clean `development@dc34e5ea55c29b108c2490a393d375e4cdb67b6b`, equal to origin; CI `34028589014` SUCCESS. Preceding implementation proof `f2c2b7d4522272314f0994d2a21c4ba28cf181be`, CI `34028256995`: 1130 unit/governance + 401 PostgreSQL tests, zero failures/errors/skips.

## Authority and decomposition

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §20 Configuration diff, §21 Impact analysis, §22 Impact classifications, §23 Existing commitments are protected, §24 Existing commitments retain semantic affinity.
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §§3–5 immutable revision, pinned registry and validated package.
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §9 Impact-Review-Evidence Identity and Minimum Affinity, §10 Business-Facing Review Boundary, §11 Impact-Evidence Production Predicate. This work does not generalize the amendment's ordinary-first approval policy.
- `designs/IMPLEMENTATION-RULES.md`, v1.7 §§30.1–30.6, §§46–48, §52 canonical loop, §§52.10–52.12 automatic fine-grained decomposition, §§52.14–52.19 evidence scope/falsification and §53.2 paradigm-fit gate.

R4 remains required before R2. R4A covers immutable-source/exact-result orchestration. R4B must provide concrete business-effect and applicable owner-backed commitment assessments with explicit coverage. R4C must prove actual initial/replacement analysis-to-durable-review composition. All three are required children of R4; fixture assessments cannot close that parent. No accepted macro authority or macro edge changes.

## Implementation scope and paradigm fit

`src/main/java/mainstreet/semantic/configuration/ConfigurationImpactAnalyzer.java`, `analyze`: owner-resolved validation/candidate/base, exact requested package reference, validated package provenance and complete required-assessment aggregation. No partial result on failure; no result without business-facing effects.

`ConfigurationImpactContext.java` binds candidate, exact merchant-owned base, validation and package. `ConfigurationImpactAssessment.java` is the required owner-assessment boundary; no permissive implementation/default is supplied. `ConfigurationImpactContribution.java` retains immutable ordered effects and classified findings.

Configuration coordinates review but does not own or mutate operational commitments. Pure immutable context/contribution values carry static affinity; operational assessment adapters must obtain live facts from their owners. This component reads source/validation authority and does not write approval, activation, revisions or commitments. No library, registry semantic concept or universal commitment policy is introduced.

## Verification and falsification

`src/test/java/mainstreet/semantic/configuration/ConfigurationImpactAnalyzerTest.java`: real compiler/package resolver with controlled source/validation ports and supplied assessment fixtures. Tests cover initial aggregation, exact historical replacement base, missing validation, wrong package reference, wrong package merchant/revision/release/compiler/generation, missing candidate/base, foreign base, failed/null assessment and absent assessments/business effects.

Tests-first RED: targeted Maven failed compilation on missing analyzer/assessment/contribution contracts before production code was added. Targeted GREEN: 16 analyzer/governance tests passed. Full local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test`: BUILD SUCCESS, 1139 tests, zero failures/errors/skips. PostgreSQL execution remains CI-only; no integration behavior was changed by this orchestration component. Final implementation `5bfd53a8b3a092e34e1345746a2b7273e076a620`, Java 25/PostgreSQL 18 CI `34030230932`, job `101478268250`: SUCCESS — 1139 unit/governance + 401 PostgreSQL tests, zero failures/errors/skips. R4B becomes READY after synchronized R4A closure is committed; R4 and IMP-05 remain PARTIALLY_CONFORMING.

Exact trace locations: `ConfigurationImpactAnalyzer.analyze` :36; `ConfigurationImpactContext` :13; `ConfigurationImpactAssessment.assess` :14; `ConfigurationImpactContribution` :11. Test methods in `ConfigurationImpactAnalyzerTest` start at :40, :65, :85, :95, :105, :118, :131, :144 and :156. The tests use real deterministic package resolution and explicit fixture authority/assessment boundaries, not a deployed production composition.

Counterevidence: interface conformance cannot prove that every material business effect or commitment conflict is covered. A nonempty assessor list cannot prove complete production registration. Empty findings cannot prove absent commitments. Package identity/provenance checks are not a cryptographic content-attestation claim; the supplied compiled artifact remains inside the trusted composition boundary. R4B/R4C must prove concrete owner coverage and actual compiler-to-review wiring. No production caller, deployment, approval or activation is certified here. IMP-05 remains PARTIALLY_CONFORMING and downstream nodes remain blocked.
