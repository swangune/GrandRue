# IMP-05-R0 — Replacement Configuration contract/dependency assessment

**Rules:** IMPLEMENTATION-RULES v1.7. **Classification:** CONFORMING_COMPLETE for assessment/decomposition only. **Date:** 6 September 2026.

## Baseline and evidence scope

Inspected clean `development@58a5eb21b5a0a7ef4579f2b15b0ec11e1e1ffdee`, equal to freshly fetched `origin/development`. GitHub Actions `34011874174` is completed/successful for that exact SHA (1121 unit/governance + 388 PostgreSQL integration tests, recorded by the preceding programme review). This assessment adds no production behavior. Existing initial-configuration evidence and historical C4C evidence remain untouched. Passing baseline tests does not establish the missing replacement writer.

## Exact accepted authority

All paths below are relative to the repository; section numbers identify exact headings within the stated version.

| Authority | Applicable contract |
|---|---|
| `designs/MS-IMP-001.md`, v1.0 §11, §33 | IMP-05 includes Configuration Revision/approval; every required child must conform before macro closure. |
| `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §13 Change set, §14 Change-set provenance, §§15–19 | Exact merchant/base-affined delta; distinct merchant, inference, registered-derivation and platform origins; produce a complete candidate; intent never bypasses validation/authority. |
| Same v1.0, §20 Configuration diff, §21 Impact analysis, §§23–24 | Retain meaningful differences and impact; preserve existing commitments and historical semantic affinity. |
| Same v1.0, §§35–37 | Informed authorized merchant commit may constitute approval; inference cannot approve; authority is contextual. |
| Same v1.0, §§43–49 | Check base against current active at activation; no automatic merge; reinstatement is a new current decision; preserve history. |
| `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §§2–8 | Configuration owns immutable revision identity/lineage; pin registry; exact revision approval; enforce explicit activation predicates. |
| Same v1.1, §§10–16 | Atomic activation, package outside short transaction, historical retry distinct from reinstatement, exact runtime/historical affinity. |
| `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §§2–3, §12 | Detailed first-configuration approval is scoped; later delegated approval is explicitly outside this amendment. Do not copy first-Controller policy into a universal replacement rule. |
| `designs/MS-PROT-040 v1.5 — Release-Purpose Admission & Ordinary Release Reference Amendment.md`, §§9–12 | Ordinary creation consumes the current ordinary reference; already-created revision remains pinned; activation consumes current admission and serving proof; exact committed retry returns historical evidence. |
| `designs/IMPLEMENTATION-RULES.md`, v1.7 §§46–48, §§52.14–52.20, §53 | Commit synchronized evidence/graph/status; scope proof, seek counterevidence, preserve ownership and infrastructure boundaries. |

## Current implementation observations

1. **OBSERVED:** `src/main/java/mainstreet/semantic/configuration/ConfigurationRevisionAuthority.java:11` exposes only `materialiseInitial` as a writer. `MerchantConfigurationRevision.java:27` requires initial-intent/onboarding provenance, rejects a base and requires version one. A replacement must not manufacture onboarding provenance to fit this type.
2. **OBSERVED:** `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationRevisionAuthority.java:193` materialises from the Onboarding owner and at :221 enforces the version-one shape. Its reader at :160 maps the same initial-only type. `src/main/resources/db/migration/V36__configuration__create_revision_authority.sql:13` requires initial source columns and at :86 constrains the entire table to initial shape; V37 adds mandatory onboarding completion. A new forward migration is required, preserving historical initial constraints conditionally rather than editing applied migrations.
3. **OBSERVED:** `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationValidationEvidenceAuthority.java:139` reads that initial-only type before checking exact package/source affinity. Therefore a separate replacement writer alone would leave the real validation path incomplete.
4. **OBSERVED:** `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqInitialConfigurationRevisionApprovalAuthority.java:45` deliberately restricts the revision query at :61 to version one/no base. `src/main/java/mainstreet/semantic/configuration/ConfigurationRevisionApprovalAuthority.java:12` is a read-only approval port, not durable production approval establishment.
5. **OBSERVED:** `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationReleaseActivation.java:409` already checks replacement/current affinity; :432 selects first-approval applicability and :453 uses the general approval reader for replacement. Preserve this existing behavior while replacing supplied-only proof with real owner composition.
6. **OBSERVED:** `src/test/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationRevisionAuthorityIT.java:116`, :187, :217, :245 and :268 prove initial persistence, historical retry, rollback, intent conflict and concurrency. They do not exercise a non-initial writer. Existing `JooqConfigurationReleaseActivationIT.java` proves generic activation with supplied approval fixtures; it does not fill the missing writer/approval chain.

## Executable decomposition

The accepted delta/base/immutability rules are sufficient for the smallest pure Configuration-owned contract implementation. No new semantic authority or macro edge is needed.

| Node | Current state | Required outcome |
|---|---|---|
| R0 | CONFORMING_COMPLETE | This scoped contract/dependency assessment. |
| R1 | PARTIALLY_CONFORMING | Parent: replacement revision/change-set persistence and usable validation source. All R1 children must close. |
| R1A | READY | Immutable base-affined change intent and deterministic complete-candidate construction for the currently represented capability/policy/binding fields; truthful origin. No approval, persistence or activation claim. |
| R1B | BLOCKED_DEPENDENCY on R1A | Durable replacement change/revision identity, exact source/base, ordinary-release pinning, retry/conflict/rollback/concurrency; common revision-content read without fabricated initial provenance. Freshly assess concrete trusted mutation admission before wiring a caller. |
| R1C | BLOCKED_DEPENDENCY on R1B | Real persisted replacement through validation/package evidence; preserve initial reader/handoff behavior and immutable source affinity. |
| R2 | BLOCKED_DEPENDENCY on R1 | Applicable durable replacement approval. Resolve concrete contextual approving authority and impact visibility from accepted authority before implementation; escalate only an actual unresolved semantic decision. Neither a permissive lambda nor inferred universal Controller policy closes this node. |
| R3 | BLOCKED_DEPENDENCY on R1/R2 | Real replacement/reinstatement composition; current approval/admission, stale base, competing candidates, lost acknowledgement after newer activation, historical affinity and rollback. |

R1A is ordinary typed deterministic code. R1B/C use Configuration owner ports and PostgreSQL adapters; compiler output remains derived. Candidate generation does not determine current active truth. A base may cease to be active while a candidate is being prepared: the accepted mandatory conflict boundary is activation, not an invented automatic rebase or merge. Subordinate configuration portfolios beyond fields currently represented are not certified by R1A.

## Counterevidence and falsification

- Could existing optional `MerchantConfiguration.baseConfigurationIdentifier` prove persistence? No: the outer revision type, mandatory provenance and SQL shape reject it.
- Could adding only a replacement table satisfy the lifecycle? No: validation reads the initial-only owner port. R1C is an explicit required child.
- Could first-approval evidence be reused? No: exact revision affinity forbids transfer, and v1.3's scope does not establish general delegated policy.
- Could constructing a coherent object establish semantic validity or activation? No: full compiler validation, impact, approval, current concurrency/admission remain required.
- Could green historical tests or R0 closure unlock C4D? No: IMP-05 stays PARTIALLY_CONFORMING; all corrected downstream macro blocks remain.
- **UNCERTAIN:** exact future delegated approval and capability-specific impact composition are not established by this assessment. They remain explicit R2/R3 work, not assumptions in R1A.

## Verification

Read-only authority/source/constraint/caller inspection and exact remote baseline CI status verification completed. All 8 focused tests in `ImplementationGraphIntegrityTest`, `ImplementationProgrammeGateConformanceTest` and `C4CFrontierEvidenceConformanceTest` passed. Full local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test` passed: 1121 tests, zero failures/errors/skips. `git diff --check` passed. Assessment closing commit `219df20f47271cde523c952864079a4b8a3a6907`, full Java 25/PostgreSQL 18 CI run `34012610870`: SUCCESS, 1121 unit/governance + 388 integration tests, zero failures/errors/skips. No replacement production verification is claimed by R0.
