# IMP-05-R1A — Immutable replacement change-set contract

**Rules:** IMPLEMENTATION-RULES v1.7. **Scope:** pure Configuration-owned intent, complete candidate construction and structural diff for currently represented fields. No durable revision, approval or activation closure.

**Current classification:** CONFORMING_COMPLETE within that bounded scope. R1B becomes READY; R1/R2/R3 and IMP-05 remain unfinished.

## Baseline and authority

Baseline `development@219df20f47271cde523c952864079a4b8a3a6907`, GitHub Actions run `34012610870`: SUCCESS, 1121 unit/governance + 388 PostgreSQL integration tests, zero failures/errors/skips. This is the committed R0 assessment/graph closure. Historical evidence is unchanged.

Exact authority:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §13 **Change set**, §14 **Change-set provenance**, §19 **Candidate generation**, §20 **Configuration diff**, §44 **Base-revision concurrency invariant**, §45 **Automatic merge is not assumed**.
- Same v1.0 §15 **Merchant-initiated changes**, §16 **Inference-proposed changes**, §17 **Registered derivation**, §18 **Platform-required behaviour**: retained attribution does not itself authorize mutation, approve or activate.
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §3 **Configuration Revision identity and immutability**, §§4–8: materialized revision identity/release/validation/approval/concurrency remain owner lifecycle obligations.
- `designs/MS-PROT-040 v1.5 — Release-Purpose Admission & Ordinary Release Reference Amendment.md`, §9 **Configuration-Revision Creation and Validation**: the future durable writer must supply the release from the ordinary authority. A string accepted by this pure constructor is not proof that this gate ran.
- `designs/IMPLEMENTATION-RULES.md`, v1.7 §30.1 **Exact Authority Pointer Contract**, §47 **Implementation Evidence**, §§52.18–52.19, §53.2 **Paradigm-fit gate**.

## Implementation and tests

`src/main/java/mainstreet/semantic/configuration/ConfigurationChangeSet.java`:

- :24–35 immutable desired capability/policy/binding selections plus exact merchant, change-set and base identities. Complete desired selections are the implementation representation of intent; the delta is derived against the identified base, so removals cannot disappear in an automatic merge.
- :46 `candidateFrom`: uses the existing `MerchantConfiguration` structural checks and produces a complete candidate with exact base and supplied pinned release. It does not allocate a durable version, resolve current registry authority or establish semantic validation.
- :59 `differenceFrom`: retains enabled/disabled capabilities, selected/deselected policy values and before/after binding references. Policy-value replacement is represented by both deselection and selection of the same policy key.
- :69 `requireBase`: rejects foreign merchant and different base identities. R1B must resolve immutable stored base contents; this value object cannot authenticate a caller-supplied snapshot merely because its identifier matches.
- :94 `Origin` and :102 `Provenance`: distinguish all four accepted origin categories and retain source/principal/time without conferring authority.
- :117 `Difference`: immutable structural comparison, not capability-specific impact analysis.

`src/test/java/mainstreet/semantic/configuration/ConfigurationChangeSetTest.java`:

- :26 complete candidate, exact affinity, deterministic repeat and unchanged source;
- :44 wrong merchant/base and self-base rejection;
- :56 capability additions/removals, policy replacement and binding removal;
- :68 defensive copies and immutable outputs;
- :84 all origin categories and required attribution;
- :100 existing candidate shape checks (duplicate policy key and invalid version);
- :109 binding replacement without invented unrelated differences;
- :127 missing identity/base/provenance rejection.

## Falsification and evidence locality

**OBSERVED:** collection mutation cannot rewrite intent or diff; different base/merchant cannot be substituted; candidate construction preserves old contents and release affinity. The implementation has no persistence, approval or activation operation.

**DEDUCED:** this supplies the bounded typed input and comparison foundation needed by R1B. It does not establish the missing writer or macro completion.

Attempted counterexamples include removing a capability, replacing an existing policy value, removing/replacing a binding, mutating source collections, using another merchant with the same revision ID, silently rebasing to another revision, reusing the base as candidate identity, duplicate policy keys and absent provenance. Covered rejection/comparison tests survive these cases.

Explicit limits: identical claimed base identity with fabricated contents is not detectable here; stored immutable resolution belongs to R1B. Current-active checks belong to activation. Supplied release provenance belongs to the durable release authority. All origins can express intent but origin-specific admission and approval are not proven. The represented structural diff does not certify scheduling/access/exposure/resource portfolios or business impact findings. No PostgreSQL behavior has changed; R1B and R1C require real persistence/validation integration proofs.

Paradigm fit: Configuration owns this immutable declarative value; ordinary typed transformations fit candidate/diff responsibility. No infrastructure dependency, event workflow, new library or competing authority is introduced. Exact trace anchors are attached at the type and origin boundary; method names carry ordinary mechanics.

## Verification

Tests-first RED: compilation failed because `ConfigurationChangeSet` did not exist. After implementation the initial six tests passed; two additional falsification tests cover binding replacement and missing provenance/identity.

Full local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test`: **1129 tests, zero failures/errors/skips**, BUILD SUCCESS. This includes the eight R1A tests, existing semantic/application tests and architecture/governance checks. `git diff --check` passed.

**Proof commit:** `1c2d006d46c660a493ea40035ad0b1290eaeee27`. **Full CI:** `34013021750` — SUCCESS — **1129 unit/governance + 388 PostgreSQL integration tests**, zero failures/errors/skips, `mvn --batch-mode clean verify -Ppostgres-it` on Java 25/PostgreSQL 18. Existing persistence regressions passed; no new persistence behavior is claimed. The synchronized graph/status selects R1B only after this bounded closure is committed.
