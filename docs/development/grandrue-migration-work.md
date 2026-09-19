# GrandRue Migration Prepared Work Protocol

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Status:** `ACTIVE`  
**Authority class:** non-semantic operational execution aid  
**Canonical migration ledger:** `GRANDRUE-MIGRATION.md`

This file reduces repeated migration discovery by separating decision preparation from bounded execution. It does not create semantic, architecture, programme, implementation, acceptance, verification, or approval authority. `AGENTS.md`, accepted repository authority, and `GRANDRUE-MIGRATION.md` continue to govern where applicable.

The governing execution rule is:

> Prepare the decisions once. Execute exact instructions. Verify preservation. Stop on any mismatch.

The coverage rule is:

> Every discovered migration unit must receive an explicit disposition before the migration can pass its final residual/coverage gate.

---

## 1. Responsibility split

### Preparation

Preparation builds one **dependency-bounded transactional tranche** from current live evidence.

It must:

- enumerate candidate production owners from the live tree rather than stale search order;
- target **8 independently closed leaves** by default, never exceeding **15**;
- use fewer leaves when dependency closure, conflict surface or human reviewability requires it;
- retain one stable `GR-REN-02-01X<n>` identity per leaf;
- close every leaf's live owner/production-consumer boundary;
- record exact source/destination paths, source blob identities, permitted edits, protected identities, residual expectations and structural verification;
- order leaves so that dependencies moved within the same tranche do not require avoidable transitional repairs;
- freeze one exact branch/parent for the whole tranche; and
- write the executable evidence into `docs/development/grandrue-migration-active-tranche.yaml`.

Preparation may inspect a wider boundary than the eventual tranche. It must reject package-private or otherwise inseparable candidates unless the entire required closure is included.

### Execution

Execution applies one fresh `READY` tranche. It does not discover new scope, select replacement leaves, reinterpret protected identities, broaden the write set or resolve new semantic/compatibility questions.

The executor must stop whenever the manifest ceases to describe the live repository exactly.

---

## 2. Tranche state machine and recovery

The operational states are:

```text
PREPARATION_REQUIRED
        |
        v
     PREPARED
        |
        | freshness/preflight succeeds
        v
       READY
        |
        | atomic branch attachment
        v
 CODE_COMMITTED
        |
        | leaf + aggregate structural verification succeeds
        v
    VERIFIED
        |
        | combined canonical checkpoint commit
        v
     COMPLETE
        |
        v
PREPARATION_REQUIRED
```

`PREPARATION_REQUIRED` means no executable tranche exists.

`PREPARED` means leaf selection and dependency closure are resolved but freshness is not yet proven.

`READY` means the exact parent, all declared input blobs, destination preconditions, write set, protected identities, transformations, residuals and verification are explicit and fresh.

`CODE_COMMITTED` is persisted **inside the tranche code commit itself** via the active tranche manifest. That commit contains both the manifest and exactly the declared production changes.

`VERIFIED` may be transient in the executor. If interruption occurs after code attachment, the manifest at branch ancestry is sufficient to resume verification and checkpointing without rediscovering intent.

`COMPLETE` is persisted by the combined checkpoint commit, which updates the canonical ledger, subordinate work pointer and manifest together.

Recovery is deterministic:

- interruption before branch attachment leaves `development` unchanged;
- interruption after code attachment but before checkpointing resumes from the `CODE_COMMITTED` manifest;
- interruption after the combined checkpoint is already complete;
- branch movement before attachment invalidates freshness;
- branch movement after attachment requires ancestry/manifest reconciliation and never authorises force update.

---

## 3. Leaf and tranche boundaries

A **leaf** remains the smallest dependency-bounded migration unit whose required live production dependencies are fully accounted for.

A **tranche** is an ordered set of independently closed leaves that can be applied atomically against one exact parent.

Rules:

- every constituent leaf remains individually auditable;
- leaves in a tranche use consecutive migration IDs;
- one code commit may be evidence for every leaf in that tranche;
- no tranche may intentionally leave broken production references;
- no undeclared production path may be changed;
- a candidate with package-private coupling must either include the whole required closure or be rejected;
- dependency-adjacent leaves should be co-located when doing so removes temporary imports without widening semantics;
- legacy prototype owners remain excluded exactly as governed by the canonical ledger.

Transaction size is a performance control, not authority to broaden scope.

---

## 4. Active tranche manifest contract

The single machine-readable active manifest is:

`docs/development/grandrue-migration-active-tranche.yaml`

There is only one active tranche manifest. Completed history remains canonical in `GRANDRUE-MIGRATION.md`; the manifest is overwritten by the next tranche after the previous tranche is checkpointed.

Minimum executable shape:

```yaml
migration: MAIN_STREET_TO_GRANDRUE
execution_mode: TRANSACTIONAL_TRANCHE
tranche: GR-REN-02-T<n>
state: READY

repository: swangune/GrandRue
branch: development
expected_parent: <full-commit-sha>
leaf_range:
  first: GR-REN-02-01X<n>
  last: GR-REN-02-01X<m>
leaf_count: <1..15>

leaves:
  - id: GR-REN-02-01X<n>
    owner:
      from: <source-path>
      to: <destination-path>
      blob_sha: <source-blob>
    production_consumers:
      - path: <path>
        blob_sha: <blob>
    permitted_changed_paths:
      - <path>
    replacements:
      - path: <path>
        from: <exact-text>
        to: <exact-text>
        expected_count: <integer>
    protected:
      exact_strings:
        - <protected-value>
    expected_residuals:
      - path: <path>
        match: <text>
        reason: <why-it-remains>
    invariants:
      - <behaviour/data identity that must remain unchanged>
    verification:
      destination_presence: required
      legacy_owner_absence: required
      consumer_repairs: exact
      protected_identity_check: required

aggregate:
  permitted_changed_paths:
    - <union-of-all-declared-paths>
  changed_path_set: exact
  diff_integrity: required
  protected_identity_check: required
  maven_tests: prohibited_unless_separately_authorised
  github_actions: prohibited_unless_separately_authorised
```

Before attaching the code commit, the in-memory manifest is `READY`. The attached code commit writes the same manifest with `state: CODE_COMMITTED`. The combined checkpoint writes `state: COMPLETE` plus the code/checkpoint commit references.

Do not maintain a second prose copy of executable tranche state.

---

## 5. Freshness and preflight

Immediately before creating/attaching a tranche code commit:

1. confirm the active branch is exactly `development`;
2. confirm branch HEAD is exactly the tranche `expected_parent`;
3. confirm every declared owner and consumer input path has the declared blob SHA;
4. confirm each source path exists and every destination satisfies its declared absence/precondition;
5. confirm every constituent dependency boundary remains closed;
6. confirm the aggregate changed-path union is complete and contains no undeclared path;
7. confirm protected identities and expected residuals remain valid;
8. confirm the tranche contains no unresolved placeholder, assumption, design question or package-private escape;
9. confirm the tranche is compatible with `GRANDRUE-MIGRATION.md` and applicable `AGENTS.md` stop/widening rules.

Any mismatch invalidates the whole prepared tranche. Do not partially attach it. Refresh/rebuild from the new live parent.

---

## 6. Transactional execution algorithm

For one fresh `READY` tranche:

1. apply only the declared leaf moves and exact dependency repairs to an unattached tree;
2. write the active manifest into that tree with `state: CODE_COMMITTED`;
3. require every exact replacement count and input assumption to match;
4. compare the aggregate diff against the manifest's exact changed-path union;
5. reject any undeclared path, protected-identity change or unrelated content change;
6. re-check `development` HEAD is still the exact prepared parent;
7. fast-forward `development` once to the tranche code commit; never force;
8. verify every leaf structurally: destination present, old owner absent, exact consumer/import/FQCN repairs, declared residuals and invariants;
9. verify aggregate diff integrity and protected identities;
10. if verification fails because the declared tranche was wrong, stop and preserve the code commit/manifest evidence for reconciliation; do not improvise widening;
11. if verification succeeds, create **one combined checkpoint commit** that:
    - records every completed leaf in `GRANDRUE-MIGRATION.md`;
    - updates canonical checkpoint fields through the tranche's last leaf;
    - advances `docs/development/grandrue-migration-work.md` coverage/pointer once;
    - changes the active manifest to `state: COMPLETE` and records code/checkpoint evidence;
12. re-check `development` still points at the tranche code commit;
13. fast-forward once to the combined checkpoint commit;
14. final-check ledger, pointer, manifest and branch HEAD;
15. return to `PREPARATION_REQUIRED` for the next tranche.

The normal hot path is therefore **two branch commits per tranche**, not three commits per leaf.

---

## 7. Mandatory stop / return-to-preparation conditions

Stop the tranche when any of these occurs:

- branch HEAD differs from `expected_parent` before code attachment;
- a declared input blob SHA differs;
- a source/destination precondition differs;
- a replacement count differs;
- an undeclared changed path appears;
- a constituent leaf has an undeclared live production dependency;
- package-private or visibility coupling makes a declared independent move invalid;
- a protected identity would need to change;
- a persistence, schema, protocol, stable external identity, historical evidence, runtime-data, compatibility or semantic-behaviour question appears;
- repository evidence contradicts the manifest;
- an `AGENTS.md` mandatory-widening trigger appears;
- accepted authority is ambiguous or contradictory;
- verification indicates the tranche assumptions were wrong; or
- execution would require judgement not already frozen in the manifest.

A failure in one constituent leaf invalidates attachment of the entire prepared tranche before commit. After a tranche code commit has already been attached, preserve it and reconcile from the manifest; do not rewrite history.

Use `DESIGN_ESCALATION` where accepted authority requires it. Otherwise return the affected work to migration preparation.

---

## 8. Preservation requirements

### Document preservation

Do not reconstruct `GRANDRUE-MIGRATION.md` from summaries, truncated responses or remembered content. Build a ledger change from the exact current repository file and verify that existing historical records remain present. A checkpoint must not erase earlier checkpoints, commit references, protected identities, repair notes or programme state.

### Source preservation

Every removed owner path must have its declared destination. The complete code diff must contain only declared moves and edits. Unrelated content, file modes and protected strings must remain unchanged unless the packet explicitly and validly authorises otherwise.

### History preservation

Unexpected branch movement is a stop condition. It does not authorise force-pushing, overwriting intervening work, manufacturing sibling checkpoint/code commits, or improvised lineage reconciliation.

For transactional tranche execution, preserve linear lineage:

```text
verified parent -> tranche code + CODE_COMMITTED manifest -> combined checkpoint commit
```

The combined checkpoint commit updates the canonical ledger, subordinate work pointer and active manifest together. Historical one-leaf code/ledger/pointer lineages remain valid evidence and are not rewritten.

### Runtime/data preservation

A namespace/name transformation does not authorise database reset, volume deletion, data migration, schema alteration, environment retargeting, or changing the database/application instance used at runtime. Such effects require their own explicit authority and packet scope.

---

## 9. Verification claims

Structural verification may prove only the properties it actually checks: for example exact changed paths, path moves, import/package replacements, protected-string preservation and scoped residual results.

Unless separately authorised and actually executed, it must not be represented as proving:

- Maven compilation/test success;
- integration-test success;
- GitHub Actions success;
- runtime compatibility;
- database compatibility; or
- final migration completion.

Existing ledger states such as `COMPLETE_PENDING_FINAL_VERIFICATION` remain unchanged until their later governed verification gate is satisfied.

---

## 10. Coverage accounting

Coverage exists to prevent a discovered migration unit from disappearing between preparation and final verification. It is not an independent authority catalogue.

Each discovered unit must have exactly one current disposition:

```text
COMPLETE_PENDING_FINAL_VERIFICATION
PREPARATION_REQUIRED
PREPARED
READY
CODE_COMMITTED
VERIFIED
COMPLETE
DEFERRED_TO_NAMED_GROUP
EXCLUDED_WITH_REASON
```

The active coverage manifest may be compact, but it must expose whether enumeration is complete. Never report `UNCLASSIFIED = 0` while remaining migration scope has not yet been enumerated.

Final coverage/residual verification requires, at minimum:

```text
enumeration_complete = true
unclassified = 0
prepared = 0
ready = 0
in_progress = 0
unresolved_blocked = 0
```

plus the later migration gates required by `GRANDRUE-MIGRATION.md`.

Current known position at protocol adoption:

```yaml
coverage:
  completed_leaf_records_through: GR-REN-02-01X7
  completed_leaf_count: 30
  remaining_expansion_node: GR-REN-02-01X+
  remaining_enumeration: INCOMPLETE
  unclassified: UNKNOWN_UNTIL_ENUMERATION
```

This intentionally does not guess the number or identity of remaining production leaves.

---

## 11. Active queue

At protocol adoption no new production leaf has been selected or declared `READY`.

| Work item | State | Required next action |
|---|---|---|
| `GR-REN-02-01X+` | `PREPARATION_REQUIRED` | Inspect current live production dependency evidence, enumerate remaining migration units, update coverage, and prepare the first exact bounded packet. |

Protocol-adoption parent: `d3e20efdfa3acda54ed511e8c2f2374d3ae2d2ce`.

The first substantive migration action after adoption is preparation. Production mutation must not begin until an exact packet has passed freshness/preflight and is `READY`.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

---

## 12. Current migration execution state

This section is the current operational pointer. It supersedes the adoption snapshot in sections 10–11 for resume purposes without turning this file into a second historical ledger. Completed-leaf history remains canonical in `GRANDRUE-MIGRATION.md`.

```yaml
coverage:
  completed_leaf_records_through: GR-REN-02-01X739
  completed_leaf_count: 762
  remaining_expansion_node: null
  remaining_enumeration: COMPLETE
  unclassified: 0

execution_mode: CLOSED_SUBGRAPH_TRANCHE
selection_unit: DEPENDENCY_CLOSED_NORMAL_SUBGRAPH
leaf_role: AUDIT_COORDINATE_NOT_EXECUTION_UNIT
numeric_leaf_limit: NONE_USE_NATURAL_GRAPH_CUTS
active_tranche_manifest: docs/development/grandrue-migration-active-tranche.yaml
active_tranche: null
active_state: IN_PROGRESS_TEST_EXPECTATION_REPAIR_APPLIED_RERUN_REQUIRED
last_completed_leaf: GR-REN-02-01X739
last_completed_task: GR-REN-09
last_code_commit: 01e26e1a099c3451165545b5c01f25dd1cbdbb5e
last_validation_gate: GR-VV-R003_POST_MIGRATION_VERIFICATION
last_validation_target: 923db363003976856683a1b353845ecc9a29a714
symbolic_standalone_production_residuals: 0

gr_ren_03:
  baseline_in_scope_test_java_files: 376
  completed_tranches:
    - GR-REN-03-T001
    - GR-REN-03-T002
    - GR-REN-03-T003
    - GR-REN-03-T004
    - GR-REN-03-T005
    - GR-REN-03-T006
    - GR-REN-03-T007
    - GR-REN-03-T008
    - GR-REN-03-T009
    - GR-REN-03-T010
    - GR-REN-03-T011
    - GR-REN-03-T012
    - GR-REN-03-T013
    - GR-REN-03-T014
    - GR-REN-03-T015
    - GR-REN-03-T016
    - GR-REN-03-T017
    - GR-REN-03-T018
    - GR-REN-03-T019
    - GR-REN-03-T020
    - GR-REN-03-T021
    - GR-REN-03-T022
    - GR-REN-03-T023
    - GR-REN-03-T024
    - GR-REN-03-T025
    - GR-REN-03-T026
    - GR-REN-03-T027
    - GR-REN-03-T028
    - GR-REN-03-T029
    - GR-REN-03-T030
    - GR-REN-03-T031
    - GR-REN-03-T032
    - GR-REN-03-T033
    - GR-REN-03-T034
    - GR-REN-03-T035
    - GR-REN-03-T036
    - GR-REN-03-T037
    - GR-REN-03-T038
    - GR-REN-03-T039
    - GR-REN-03-T040
    - GR-REN-03-T041
    - GR-REN-03-T042
    - GR-REN-03-T043
    - GR-REN-03-T044
    - GR-REN-03-T045
    - GR-REN-03-T046
    - GR-REN-03-T047
  migrated_test_java_files: 376
  remaining_in_scope_legacy_test_java_files: 0
  excluded_legacy_prototype_test_java_files: 18
  embedded_runtime_compatibility_identifiers: CLASSIFIED_T047
  current_test_naming_cleanup_files: 32
  deferred_gr_ren_04_test_postgres_consumers: 74
  protected_test_residuals: CLASSIFIED_PRESERVE
  state: COMPLETE_PENDING_FINAL_VERIFICATION

gr_ren_04:\n  state: COMPLETE_PENDING_FINAL_VERIFICATION\n  tranche: GR-REN-04-T001\n  test_postgres_consumers: 74\n  changed_paths: 80\n  code_commit: 89b314bf27c7c50944efb5257fa38c087f412183\n  reconciliation_commit: c8821cf489735d34de341e50c4c2508bda8c4b4f\n\ngr_ren_05:
  state: COMPLETE_PENDING_FINAL_VERIFICATION
  evidence_target: 7591466bf16b165d549161cd9e8369a1dac41674
  executable_mutations: 0
  excluded_prototype_profile_configuration: preserved

gr_ren_06:
  state: COMPLETE_PENDING_FINAL_VERIFICATION
  evidence_target: 7591466bf16b165d549161cd9e8369a1dac41674
  protected_identity_mutations: 0
  compatibility_aliases: preserved

gr_ren_07:
  state: COMPLETE_PENDING_FINAL_VERIFICATION
  tranche: GR-REN-07-T001
  changed_paths: 9
  code_commit: 4752cd2193fa8e895a1784668fe64a71b7329155
  current_product_wording_migrated: true
  historical_titles_preserved: true
  symbolic_identifiers_preserved: true

post_verification_repair:
  tranche: GR-REN-REPAIR-T001
  source_findings:
    - GR-VV-F001
    - GR-VV-F002
  code_commit: 01e26e1a099c3451165545b5c01f25dd1cbdbb5e
  changed_source_paths: 2
  F001_output_blob: 95b85896aa89326927b084a83ea54fcdfde20cc8
  F002_output_blob: ea858a667a4f3fc0ad1227ddbdd4d04d6cc92bb3
  exact_expected_object_match: true
  structural_result: PASS
  semantic_change: none
  maven_tests: not_run_not_authorised
  github_actions: not_run_not_authorised

closure_gates:
  gr_ren_08:
    state: COMPLETE
    evidence: GR-VV-R003
    file_accounting: PASS
    claims: 847_PASS_0_FAIL_0_BLOCKED
    unexpected_paths: 0
  gr_ren_09:
    state: COMPLETE
    evidence: GR-VV-R003
    migration_preservation: PASS
    protected_identities: PASS
    open_findings: 0
    negative_controls: RETAINED_PASS_FROM_GR_VV_R001
  gr_ren_10:
    state: IN_PROGRESS_TEST_EXPECTATION_REPAIR_APPLIED_RERUN_REQUIRED
    structural_migration_validation: PASS
    command: mvn --batch-mode clean verify -Ppostgres-it
    latest_main_compile: PASS_1094_SOURCES
    latest_test_compile: PASS_376_SOURCES
    latest_unit_governance_tests: 1252
    latest_failures: 28
    latest_errors: 2
    latest_skipped: 0
    failure_classes:
      stale_current_product_wording_expectations: 19
      stale_surface_package_identity_expectations: 4
      stale_governance_controller_authority_navigation: 7
    test_repair_commit: a55de294fba72fe071194c13a7ee123d518ea7dc
    test_repair_changed_paths: 13
    production_changes_in_test_repair: 0
    rerun_required: true
    github_actions: NOT_RUN_NOT_AUTHORISED
  gr_ren_11:
    state: BLOCKED_BY_GR_REN_10_FINAL_RERUN

migration_execution_state: COMPLETE_PENDING_EXECUTABLE_VALIDATION
verification_handoff: COMPLETE_PASS
verification_start_baseline: c4153441d8340b229a29884967d796280d949a7d
verification_target: THIS_CHECKPOINT_COMMIT
next_action: RERUN_MVN_BATCH_MODE_CLEAN_VERIFY_POSTGRES_IT
```

Test namespace/runtime-coupled changes remain deferred to `GR-REN-03`. Maven tests and GitHub Actions remain prohibited unless separately authorised.


---

## 13. Closed-subgraph execution supersession — effective after `GR-REN-02-T013`

This section controls future preparation/execution where earlier sections prescribe numeric tranche sizing or independent per-leaf preparation. Earlier sections remain historical protocol evidence.

### 13.1 Efficiency objective

Future migration work SHALL minimise **reasoning boundaries**, not merely Git commits.

Canonical:

```text
leaf-level traceability
+
subgraph-level discovery
+
subgraph-level preparation
+
subgraph-level execution
+
aggregate-first verification
+
exception-only deep reasoning
```

Batch committing independently reasoned leaves is insufficient optimisation.

### 13.2 Region analysis

Preparation selects a natural live migration region (for example a remaining package/domain cluster) and performs one dependency analysis across that region.

It MUST establish:

- all remaining in-scope legacy owners in the analysed region;
- all in-scope production consumers/edges required for those owners;
- package-private/visibility coupling;
- protected identities and expected residuals;
- excluded legacy-prototype interactions; and
- any compatibility/semantic/security/persistence exception.

Each discovered owner receives one disposition:

```text
NORMAL_CLOSED
EXCEPTION_REQUIRES_REASONING
DEFERRED_TO_NAMED_GROUP
EXCLUDED_WITH_REASON
ALREADY_COMPLETE
```

### 13.3 Closed normal subgraph

The executable tranche is the largest subset of `NORMAL_CLOSED` owners whose required in-scope production consumer closure can be frozen exactly under one deterministic transformation plan.

No arbitrary leaf count governs tranche size.

Partition only at natural graph cuts, exception boundaries, freshness/integrity constraints, connector/atomicity limits, or when exact review/verification would otherwise become unreliable.

### 13.4 Leaf evidence without leaf workflow

Stable `GR-REN-02-01X<n>` IDs remain required for auditability.

For normal owners, preparation MAY generate their evidence records from the single region/subgraph analysis. Execution and verification MUST NOT repeat repository discovery merely to satisfy each leaf identity.

The manifest SHALL retain enough owner-level data to prove, per audit coordinate:

- source/destination;
- source blob;
- relevant production consumers;
- permitted transformation;
- protected identities/residuals; and
- resulting code commit.

### 13.5 Exception isolation

An exception node is excluded from the current normal subgraph. Preparation records why and continues with independent normal work when dependency closure permits.

An exception MUST be reasoned separately when it involves unresolved compatibility, identity, persistence/schema/runtime-data, semantic/architecture/security/trust, visibility/ownership or accepted-authority questions.

An exception MUST NOT cause unrelated deterministic owners to fall back to leaf-by-leaf processing.

### 13.6 Aggregate-first verification

Verification first proves the frozen closed-subgraph transaction as a whole:

```text
actual changed paths == manifest changed paths
+
all declared destinations present
+
all migrated legacy owners absent
+
all frozen production consumers repaired
+
no undeclared reference remains in analysed closure
+
protected identities preserved
+
expected residuals exactly classified
```

Owner/leaf evidence is then checked or mechanically derived from that proved aggregate.

### 13.7 Manifest evolution

The next prepared active manifest SHOULD use:

```yaml
execution_mode: CLOSED_SUBGRAPH_TRANCHE
region:
  identity: <natural region>
  closure_basis: <dependency/ownership boundary>
classification:
  normal_closed: [...]
  exceptions: [...]
subgraph:
  owners: [...]
  production_consumer_closure: [...]
  deterministic_transformations: [...]
  permitted_changed_paths: [...]
verification:
  aggregate_changed_path_set: exact
  owner_evidence: required
```

The exact manifest may retain the existing `leaves:` records for compatibility, but those records are evidence coordinates rather than independent execution instructions.

All existing freshness, preservation, linear-lineage, no-force-update, prototype-exclusion and test-claim restrictions remain applicable.

---

## 15. Terminal automatic handoff

When migration execution through `GR-REN-07` has no remaining executable migration work, finish the final migration checkpoint and automatically hand off to `GRANDRUE-POST-MIGRATION-VERIFICATION.md`; do not wait for another user prompt.

The verification start point is always the original migration baseline `c4153441d8340b229a29884967d796280d949a7d`, not the verification-ledger adoption date or the latest tranche.

```text
original migration baseline M
        ↓
all historical migration leaves / tranches / repairs
        ↓
final fully checkpointed target D
        ↓
automatic post-migration verification
```

The handoff is valid only from a fully checkpointed migration state. A `CODE_COMMITTED`, stale, unresolved or partially prepared tranche must be reconciled first.
