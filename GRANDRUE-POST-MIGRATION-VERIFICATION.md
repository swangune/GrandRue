# GrandRue Post-Migration Verification and Validation Ledger

**Audit:** `MAIN_STREET_TO_GRANDRUE_CLAIM_PRESERVATION`

**Repository:** `swangune/GrandRue`

**Comparison:** original migration-start baseline `M = c4153441d8340b229a29884967d796280d949a7d` against the final pinned migration target `D`; `master` is optional contextual evidence only

**Status:** `IN_PROGRESS`

**Activation:** `AUTOMATIC_FROM_GRANDRUE_MIGRATION_FINAL_CHECKPOINT`

**Authority class:** non-semantic operational verification ledger

**Work file:** `docs/development/grandrue-post-migration-verification-work.md`

This is the resumable results registry for independent verification of migration claims. It is not a second migration programme. `GRANDRUE-MIGRATION.md` remains the source of migration scope, claims, protected identities and migration history. Accepted repository authority and `AGENTS.md` retain their existing precedence. This file does not amend acceptance gates or confer permission to implement, repair, redesign, rename more files, or run product verification.

Verification scope begins at the original migration baseline `c4153441d8340b229a29884967d796280d949a7d`, before the first migration mutation, and includes every completed migration claim/repair through the final target `D`. Work completed before this verification ledger existed is fully in scope. This workflow remains dormant while migration execution is active and activates automatically when the migration terminal checkpoint sets `verification_handoff: READY`.

> Account for every tracked file. Validate only recorded migration claims. Compare complete content. Report discrepancies; do not repair them inside verification.

## 1. Contract and boundaries

The audit answers two related but different questions:

1. **Repository preservation:** Where did every baseline file go, where did every live file come from, and what exactly changed between the two snapshots?
2. **Claim validation:** Does every recorded migration claim remain true in the live tree, with no content loss or change beyond its independently established permitted transformation?

Whole-repository accounting is mandatory. Migration-completeness validation is restricted to the frozen claimed scope. A file outside that scope is still checked for preservation; it is not silently excluded from the inventory or required to adopt GrandRue naming.

A deferred test, unchanged historical document, or protected legacy identifier is not a failed migration merely because legacy wording remains. Conversely, an unclaimed deletion, truncation, addition, mode change or content change is a finding even outside the claimed scope. An exclusion from migration is never an exclusion from accounting.

The audit may run against the claims completed so far or after migration ends. It must describe its coverage as **the claims recorded at the pinned target**, not as all planned migration work. An `IN_PROGRESS`, `NOT_STARTED`, `PREPARED` or `READY` migration task is not a completion claim. `COMPLETE_PENDING_FINAL_VERIFICATION` is a claim to check, not evidence that the check passed.

### Read-only boundary

- Do not change production, test, build, configuration, schema, workflow, authority or historical-evidence files during checking. Do not repair the migration ledger to make a failed claim pass.
- Do not create branches, merge, rebase, force-push, reset databases, change environments or delete volumes.
- Do not run Maven tests, GitHub Actions or other product build/runtime checks without separate explicit authorisation. Structural results are not compilation, integration, runtime or database results.
- The only prospective writes are these two operational files and exact audit-evidence paths declared by a prepared packet. Preparing these documents does not itself authorise repository publication.
- Preserve existing migration states. This audit can supply evidence to later governed gates; it cannot automatically mark the migration complete.

## 2. Baseline, target and claim snapshot

Use these identities:

| Identity | Meaning | Rule |
|---|---|---|
| `M` | Original migration-start baseline recorded by `GRANDRUE-MIGRATION.md`: `c4153441d8340b229a29884967d796280d949a7d` | **Primary migration-verification baseline.** It is the state immediately before the first governed migration work and MUST never be silently replaced. |
| `D` | Final fully checkpointed migration commit captured by automatic handoff | Immutable verification target. Pin once before verification evidence commits move `development`. |
| `B` | Historical/current `master` reference where useful | Optional contextual evidence only. It may explain pre-migration repository history but MUST NOT replace `M` as the migration baseline. |

Always inventory and compare the complete endpoint trees `M` and `D`. The primary migration-preservation question is:

```text
What changed from immediately before the first migration (M)
to the final migration result (D),
and is every change exactly accounted for by recorded migration claims/repairs/protected dispositions?
```

The verifier MUST extract and validate every completed migration claim from the first migration onward, regardless of whether it predates this verification ledger, the transactional-tranche protocol or the closed-subgraph protocol.

`B` may be inspected to understand history before `M`, but a `B → M` difference is not itself a migration defect and does not need to be reclassified as migration work. If an overall claim about preservation since `master` is requested, report that separately from migration preservation.

Freeze at `D`: the complete migration ledger blob, migration work blob, active/final manifests, frozen action-map evidence, repair/reconciliation receipts and all completed claim IDs/evidence. Derive expected transformations from those frozen sources, never from the observed final diff alone.

### Preparation observations — not an opened audit run

The historical observations below are navigation evidence only and must be refreshed where relevant when the automatic handoff activates. The immutable migration baseline remains:

```yaml
migration_start:
  commit: c4153441d8340b229a29884967d796280d949a7d
  role: PRIMARY_VERIFICATION_BASELINE
final_target:
  commit: null
  role: PIN_AT_AUTOMATIC_HANDOFF
master_context:
  commit: null
  role: OPTIONAL_CONTEXT_ONLY
audit_executed: false
```

Once `D` is pinned, it is immutable for that run.

## 3. Lowest-level preservation rule

For an unchanged file, require identical full content and Git mode/type. For a rename, also require the exact destination, declared source-path disposition and unambiguous file identity.

For a claimed naming change, construct an expected file from the complete migration-start (`M`) bytes, or from an independently evidenced post-`M` introduction state where the file did not yet exist at `M`, using only independently justified, exact, location-bounded edits. Compare that expected file with the **entire** live file, byte for byte:

```text
expected = exact_permitted_transform(complete_baseline_bytes)
PASS only when expected == complete_live_bytes
          and paths, type, mode and claim obligations also match
```

For migration verification, no `B → M` bridge is required to establish the primary baseline because `M` is canonical. If optional master-context analysis is performed, keep it separate from the `M → D` migration result.

Each edit must identify its input object, exact old/new bytes, position or uniquely constrained context, expected occurrence count, and justification. Every byte outside those edits must survive unchanged. A whole-file replacement justified only by the observed live file is circular evidence and prohibited.

Do not globally replace `mainstreet` with `grandrue` on both sides. Do not ignore comments, whitespace, line endings, strings, annotations or imports to obtain a pass. Do not accept line-count equality, rename similarity, matching method counts or a small diff as preservation proof. Those are diagnostics only.

This check catches missing tails, removed middle sections and same-size alterations as well as obvious deletions. A verified namespace-only textual transformation establishes source preservation outside the approved edits; it does **not**, by itself, establish equivalent runtime behaviour after a package/configuration change.

## 4. Complete accounting and traceability

Enumerate all tracked leaf entries in both snapshots, including dotfiles, documentation, fixtures, workflows, lockfiles, scripts, binaries, symlinks and submodule pointers when present. No extension, directory or legacy-name search may define the inventory boundary.

Maintain one machine-readable file manifest. Each baseline entry appears exactly once on its baseline side; each target entry appears exactly once on its target side. A row normally links one baseline file to one target file, with an explicit missing side for an addition or deletion. Many claims may reference one file row; they must not create duplicate coverage.

Every row records paths, object identities, type, mode, size, content comparison, provenance, relevant claims and a verification disposition. Every changed hunk must be attributed. Missing files, collisions, unexpected copies, splits/merges and unresolved path identity must not disappear into a rename heuristic.

New operational records absent from `master` need independently established introduction/checkpoint evidence. Preserve their prior recorded content through their current version. A new file cannot pass a baseline-content comparison against an empty invented baseline. Unexplained additions and missing content in such records remain findings.

Use the work file's coverage invariants. Counts remain `null` until enumeration and reconciliation establish them. Zero is a measured result, never a default.

## 5. Verification programme — whole-set first, exception-driven

The verification unit is no longer one file at a time. The audit still produces one exact result row per tracked file and checks every claimed obligation, but it obtains those results through the largest deterministic closed verification regions that can be proved exactly.

Canonical execution:

```text
pin B / D / M + claim snapshot once
        ↓
enumerate both complete trees once
        ↓
object-ID + mode equality fast path
        ↓
changed / moved / claimed population
        ↓
classify into deterministic transformation classes
        +
isolate exceptions / ambiguous mappings / protected-risk cases
        ↓
build dependency-/claim-closed verification regions
        ↓
bulk reconstruct exact expected bytes
        ↓
bulk full-content / path / mode / protection checks
        ↓
mechanically emit per-file and per-claim results
        ↓
deep reasoning only for exceptions and counterexamples
```

This changes execution granularity, not evidentiary strictness.

### 5.1 Equality fast path

If a baseline and target entry have the same immutable Git object ID, object type and mode, and no migration claim requires a path/content change that is absent, the content-equality result may be established without rereading/reasoning over that file independently. The file still receives its own manifest row and result.

A same-object fast path never proves a required rename, required transformed content, protected-path obligation or claim that is not actually satisfied.

### 5.2 Deterministic transformation classes

Changed/claimed files SHOULD be grouped where their expected outputs are governed by the same independently established transformation class, for example a closed package/path/import namespace move with identical preservation rules.

For each class, preparation freezes the exact input objects, file mappings, transformations, protected identities, expected residuals and claim obligations before execution. Expected outputs are then generated and compared in bulk, but every output retains a separate file result and digest.

### 5.3 Closed verification regions

A verification region is the largest finite set of files/claims that can be checked from one frozen snapshot and one closed deterministic rule set without unresolved mapping, semantic or compatibility judgement.

Region size is determined by closure and proofability, not an arbitrary file count. Partition only at natural claim/dependency boundaries, exception boundaries, evidence/tool limits or where the exact region can no longer be reviewed and reproduced reliably.

### 5.4 Exceptions

The following are not absorbed into an ordinary deterministic region: ambiguous identity mapping; split/merge/delete semantics; unexplained additions; non-naming executable deltas; protected persisted/external identity questions; compatibility-sensitive residuals; package-private/visibility ambiguity; historical bridge uncertainty; incomplete bytes/objects; and any authority/semantic uncertainty.

Exceptions receive 100% explicit checking and disposition. An exception does not force unrelated deterministic files back to one-file-at-a-time verification.

### 5.5 No sampling does not mean one reasoning cycle per file

No sampling remains mandatory for accounting and exact mechanical checking: every tracked endpoint is represented, every changed/claimed file is checked, and every claim obligation resolves to evidence.

Human/agent deep reasoning may be targeted at exceptions, protected-risk classes and falsification counterexamples because deterministic populations are already checked exhaustively by exact set/object/byte invariants.

### 5.6 Verification groups

| Group | Work | Completion evidence |
|---|---|---|
| `GR-VV-00` | Pin snapshots and freeze claims | Exact refs, complete source objects, claim snapshot and freshness receipt. |
| `GR-VV-01` | Enumerate both trees once and establish the master bridge | Complete manifests, exact two-sided set equality/accounting, immutable-object fast-path population and unresolved rows exposed. |
| `GR-VV-02` | Classify changed/claimed population and prepare closed regions | Exact mappings, transformation classes, exception registry, provenance links and frozen region manifests. |
| `GR-VV-03` | Validate the checker, then verify deterministic regions in bulk | Negative controls plus complete per-file path/mode/content/protection results mechanically emitted from region checks. |
| `GR-VV-04` | Reconcile claims, exceptions, consumers and protected identities | Every claim obligation and every exception/protected class explicitly resolved; all residuals classified. |
| `GR-VV-05` | Aggregate findings, falsify closure and refresh live target | Evidence-backed conclusions, zero unexplained residuals for any passing conclusion, and final live-head reconciliation. |

Do not select the next arbitrary file during execution. Prepare the next closed region or exception set from the frozen manifests; execute it once; checkpoint its results; then advance.

Leaf/file cardinality is an audit cardinality, not a reasoning-work multiplier.

## 6. Outcomes, findings and hand-off

A check result is `PASS`, `FAIL` or `BLOCKED`. `NOT_RUN` means no result. Workflow states in the work file do not stand in for check results.

- `FAIL`: complete evidence contradicts a claim or preservation requirement, including missing content or unpermitted logic-bearing changes.
- `BLOCKED`: evidence, full bytes, mapping, authority or a reliable comparison method is unavailable or ambiguous.
- `PASS`: all declared obligations actually succeeded at the exact input identities. Finding a plausible explanation is not enough.

Record the smallest reproducible discrepancy: file ID, baseline/live paths and objects, claim IDs, byte range or complete evidence reference, expected versus actual, affected conclusions and the required hand-off. Do not copy secret values into evidence; retain necessary object/offset references and redact display-only diagnostics without weakening the underlying check.

Ordinary migration defects return to separately authorised migration repair. Material semantic, persistence, compatibility or authority uncertainty follows the existing `AGENTS.md` widening / `DESIGN_ESCALATION` route. Independent checks may continue when unaffected. No failure is fixed or waived by the verifier.

Keep failed receipts immutable. A later repair creates a new target/run or explicit revalidation receipt; it does not rewrite the old result.

## 7. Completion gates and permitted wording

Publish separate conclusions, not a single ambiguous green status:

| Conclusion | A successful result requires |
|---|---|
| `file_accounting` | Complete endpoint inventories and exactly-once representation of both sides. This is enumeration, not content approval. |
| `claim_validation` | Every frozen claimed obligation passes; no unverified owner/consumer, stale claim or missing evidence. |
| `migration_preservation` | Complete content outside exact migration edits is preserved; protected content/modes are intact; operational-record preservation is checked. |
| `master_non_naming_executable_preservation` | No executable-content difference from `master` beyond individually validated naming edits. A pre-existing or authorised non-naming executable delta must be disclosed as `DIFFERENT`, not hidden. |
| `live_freshness` | The inspected live head is still `D`, or a separately recorded audit-receipt-only delta has been completely reconciled. |

`claim_validation` and `migration_preservation` require no relevant failed or blocked checks and complete claim-to-file-to-edit coverage. All unclaimed differences also require explicit findings or independently evidenced dispositions. Open unexplained differences prevent the overall audit from passing even if the narrower claim set passes.

“No truncation” may be stated only for a completely checked scope with no unexplained removal. For intentionally revised non-migration documents, disclose the evidenced revisions; do not describe their old bytes as unchanged. “No logic change since master” must not be stated when the executable-preservation conclusion is `DIFFERENT`, `BLOCKED` or `NOT_RUN`.

A successful report may say:

> All tracked entries are accounted for between baseline B and target D. Every frozen migration claim was checked against complete file content. No unapproved content loss or change outside the evidenced migration transformations was found within the checked scope. Separately attributed non-migration differences are listed. Build and runtime verification were not performed.

That wording is a **report template**, not the current result. This audit does not by itself close `GR-REN-08..11`, authorise a merge, certify all planned migration work, or establish runtime equivalence.

## 7.1 Successful Verification → Implementation Handoff

Successful audit conclusions do not by themselves authorise implementation re-entry. The handoff becomes `READY` only when the migration ledger additionally records the required `GR-REN-08..11` closure and no unresolved migration finding or blocking design escalation remains.

When those conditions are satisfied:

```text
verified final migration target D
        ↓
implementation_handoff = READY
        ↓
reconcile docs/development/implementation-programme-state.json against D/current tree
        ↓
refresh /IMPLEMENTATION.md
        ↓
resume eligible READY implementation automatically under MS-IMPLEMENTATION-RULES-001 v2.0
```

No separate `continue implementation` prompt is required.

A `FAIL`, relevant `BLOCKED` result, unresolved migration repair or unresolved `DESIGN_ESCALATION` sets `implementation_handoff = BLOCKED`.

## 8. Checkpoint and restart

This structured record is the sole current run pointer. Packet definitions and active execution state live in the work file; detailed results live in immutable evidence, not copied into both documents.

```yaml
checkpoint:
  audit: MAIN_STREET_TO_GRANDRUE_CLAIM_PRESERVATION
  status: IN_PROGRESS
  activation_mode: AUTOMATIC_FROM_GRANDRUE_MIGRATION_FINAL_CHECKPOINT
  activation_state: ACTIVE
  execution_mode: CLOSED_REGION_BULK
  run_id: GR-VV-R001
  baseline_commit: c4153441d8340b229a29884967d796280d949a7d
  baseline_role: ORIGINAL_MIGRATION_START
  target_commit: afb0d3631fcd916f3ba9c830c9c19108bb7a4011
  target_role: TERMINAL_MIGRATION_CHECKPOINT
  claim_snapshot_digest: 20dccce2678f207356db714fcefdca954fc8b1cf04faf0993bc8b771eaaea362
  evidence_root: docs/development/grandrue-post-migration-verification/GR-VV-R001
  latest_receipt: docs/development/grandrue-post-migration-verification/GR-VV-R001/receipts/GR-VV-03-R002.json
  implementation_handoff: BLOCKED
  coverage:
    enumeration_complete: true
    baseline_entries: 2258
    target_entries: 2266
    represented_baseline_entries: 2258
    represented_target_entries: 2266
    unclassified_files: 0
    equality_fast_path_files: 744
    deterministic_regions: 4
    exception_files: 41
    claims_total: 847
    claims_verified: 0
    open_findings: 2
  conclusions:
    file_accounting: PASS
    claim_validation: NOT_RUN
    migration_preservation: NOT_RUN
    master_non_naming_executable_preservation: NOT_RUN
    live_freshness: NOT_RUN
  next_action: Execute GR-VV-03-R003 against the frozen 5-file build/CI/storefront current-naming deterministic region. GR-VV-03-R002 is checkpointed PASS at 376/376; GR-VV-03-R001 remains checkpointed FAIL with open findings GR-VV-F001 and GR-VV-F002 preserved. Continue unaffected verification without repair.
```

On restart: read `AGENTS.md`, the migration ledger, this checkpoint and the work file; verify their exact current inputs; identify the last durable receipt; and resume only a fresh `READY` packet. Reconcile unexpected HEAD movement before proceeding. Never recreate a ledger from a summary or truncated response.

Audit receipts must refer to the target they checked. If publication is separately authorised, commit only declared audit files on `development` after rechecking the expected publication parent and possible workflow triggers. A receipt commit's SHA cannot be embedded in that same commit; record the checked target inside it and use the commit envelope or a later receipt for publication identity. No recursive self-certification.

## 9. Preparation evidence

Repository sources inspected for this draft:

- `AGENTS.md`, `GRANDRUE-MIGRATION.md` and `docs/development/grandrue-migration-work.md` at observed `development` commit `cacbe071ecd5df6b99ef79923872971af0ee3607`.
- `master` commit `5c73ed82248315a9e318289f032ed445573f8d37` and migration-start commit `c4153441d8340b229a29884967d796280d949a7d`.
- The migration ledger's Enquiry repair record: a completed rename disappeared from the live tree despite ancestry, then repair commit `075fe5ae53a0e513960c6965634a5720cd1a23eb` restored it. This motivates live-tree validation; it is not an audit result produced here.
- The ledger's documented earlier truncation/repair motivates complete-document retrieval and preservation checks. Historical repair descriptions remain assertions to verify when relevant, not permission to assume present integrity.

**Preparation result:** two complete operational document drafts. No repository-wide inventory, file-preservation audit, product tests or migration certification has been performed by preparing this file.
