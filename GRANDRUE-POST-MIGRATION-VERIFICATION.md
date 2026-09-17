# GrandRue Post-Migration Verification and Validation Ledger

**Audit:** `MAIN_STREET_TO_GRANDRUE_CLAIM_PRESERVATION`

**Repository:** `swangune/GrandRue`

**Comparison:** baseline `master` against a pinned live `development` snapshot

**Status:** `DRAFT_NOT_EXECUTED`

**Authority class:** non-semantic operational verification ledger

**Work file:** `docs/development/grandrue-post-migration-verification-work.md`

This is the resumable results registry for independent verification of migration claims. It is not a second migration programme. `GRANDRUE-MIGRATION.md` remains the source of migration scope, claims, protected identities and migration history. Accepted repository authority and `AGENTS.md` retain their existing precedence. This file does not amend acceptance gates or confer permission to implement, repair, redesign, rename more files, or run product verification.

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

Use three explicitly different identities:

| Identity | Meaning | Rule |
|---|---|---|
| `B` | Fixed baseline commit selected from `master` | Primary comparison baseline; never silently replace it. |
| `D` | Live `development` commit captured when a run opens | Immutable target for that run; never mix reads from moving branch names. |
| `M` | Migration-start baseline recorded by the migration ledger | Secondary attribution checkpoint only; it does not replace `B`. |

Always inventory and compare **the complete endpoint trees `B` and `D`**. A merge-base comparison, a pull-request file list, a migration commit being an ancestor, or a clean working directory is not a substitute.

Where `B != M`, retain a separately evidenced `B → M` bridge and inspect `M → D` in addition to `B → D`. Determine ancestry rather than assuming it. Every earlier rename, addition or content change must have exact path/blob provenance and an independently identifiable explanation. An earlier commit is not, merely by existing, authority to disregard a difference.

Pre-existing non-migration changes must remain visible. They may explain a difference without making the files equal. In particular, an executable non-naming difference, whether before or during migration, prevents an unqualified claim of “no logic-bearing source change since master”. Unresolved attribution blocks the relevant conclusion; do not reset the baseline to avoid it.

Freeze the complete migration ledger, its blob identity, applicable work-packet evidence, frozen action-map evidence, relevant repair receipts and claim IDs at `D`. Derive expected changes from that material, not from whichever live difference is convenient to accept. If the evidence is incomplete or contradictory, the affected check is `BLOCKED`, not inferred.

### Preparation observations — not an opened audit run

These repository identities were inspected on **17 September 2026**. Reconfirm them at activation. The target will normally be newer when this post-migration audit is actually run.

```yaml
observation:
  repository: swangune/GrandRue
  observed_on: '2026-09-17'
  master_commit: 5c73ed82248315a9e318289f032ed445573f8d37
  master_tree: 6538f5c5030eb0a443b1f43dec04434cb1cc709c
  development_commit: cacbe071ecd5df6b99ef79923872971af0ee3607
  development_tree: 529f287c5ba798fb3b45bc3ab30d1fc7984f6d3f
  migration_start_commit: c4153441d8340b229a29884967d796280d949a7d
  migration_ledger_blob: 5b7a0783d0f8c27297394c024ca41dabe97cc162
  migration_work_blob: 88eead8627a33df715698f3d4614b87d067ca44e
  agent_instructions_blob: a48013249eb72f3bb31a106bb559c148e552a913
  frozen_action_map_commit: 08dcfda8a8b23bc442c3d63a4754c2ed6b74ab52
  audit_executed: false
```

If `master` has moved before the first run, disclose the movement and establish the intended fixed baseline. Do not automatically replace the observed baseline or assume the newer `master` is pre-migration. Once a run opens, `B` remains immutable.

## 3. Lowest-level preservation rule

For an unchanged file, require identical full content and Git mode/type. For a rename, also require the exact destination, declared source-path disposition and unambiguous file identity.

For a claimed naming change, construct an expected file from the complete baseline bytes using only independently justified, exact, location-bounded edits. Compare that expected file with the **entire** live file, byte for byte:

```text
expected = exact_permitted_transform(complete_baseline_bytes)
PASS only when expected == complete_live_bytes
          and paths, type, mode and claim obligations also match
```

When an evidenced pre-migration bridge is needed, reconstruct it separately and retain both raw `B → D` differences and the bridge evidence. Do not replace the baseline wholesale with `M` and call the original content preserved.

Each edit must identify its input object, exact old/new bytes, position or uniquely constrained context, expected occurrence count, and justification. Every byte outside those edits must survive unchanged. A whole-file replacement justified only by the observed live file is circular evidence and prohibited.

Do not globally replace `mainstreet` with `grandrue` on both sides. Do not ignore comments, whitespace, line endings, strings, annotations or imports to obtain a pass. Do not accept line-count equality, rename similarity, matching method counts or a small diff as preservation proof. Those are diagnostics only.

This check catches missing tails, removed middle sections and same-size alterations as well as obvious deletions. A verified namespace-only textual transformation establishes source preservation outside the approved edits; it does **not**, by itself, establish equivalent runtime behaviour after a package/configuration change.

## 4. Complete accounting and traceability

Enumerate all tracked leaf entries in both snapshots, including dotfiles, documentation, fixtures, workflows, lockfiles, scripts, binaries, symlinks and submodule pointers when present. No extension, directory or legacy-name search may define the inventory boundary.

Maintain one machine-readable file manifest. Each baseline entry appears exactly once on its baseline side; each target entry appears exactly once on its target side. A row normally links one baseline file to one target file, with an explicit missing side for an addition or deletion. Many claims may reference one file row; they must not create duplicate coverage.

Every row records paths, object identities, type, mode, size, content comparison, provenance, relevant claims and a verification disposition. Every changed hunk must be attributed. Missing files, collisions, unexpected copies, splits/merges and unresolved path identity must not disappear into a rename heuristic.

New operational records absent from `master` need independently established introduction/checkpoint evidence. Preserve their prior recorded content through their current version. A new file cannot pass a baseline-content comparison against an empty invented baseline. Unexplained additions and missing content in such records remain findings.

Use the work file's coverage invariants. Counts remain `null` until enumeration and reconciliation establish them. Zero is a measured result, never a default.

## 5. Verification programme

Preparation resolves decisions. Execution performs bounded, read-only checks. The usual content unit is **one logical file pair**, including all claimed edits affecting that file. Unlike a mutation packet, a verification packet need not modify all consumers atomically; claim completion must still cover every owner and consumer.

| Group | Work | Completion evidence |
|---|---|---|
| `GR-VV-00` | Pin snapshots and freeze claims | Exact refs, full source objects, claim snapshot and freshness receipt. |
| `GR-VV-01` | Enumerate both trees and establish the master bridge | Complete manifests; all entries represented, unresolved rows exposed. |
| `GR-VV-02` | Prepare exact file mappings and expected transformations | Closed read boundaries, claim/provenance links and no inferred permissions. |
| `GR-VV-03` | Validate the checking method, then verify file pairs | Negative-control results and full-content/mode/path receipts. |
| `GR-VV-04` | Reconcile claims, consumers and protected identities | Every claimed obligation checked; all residuals classified. |
| `GR-VV-05` | Aggregate findings, coverage and final freshness | Evidence-backed conclusions for the pinned target and a live-head comparison. |

Do not select the “next useful file” during execution. Prepare a small deterministic queue from the manifests; execute one fresh packet, checkpoint its result, then advance. Identical objects may be checked in bounded batches, but every file still receives a separate row/result. No sampling.

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

## 8. Checkpoint and restart

This structured record is the sole current run pointer. Packet definitions and active execution state live in the work file; detailed results live in immutable evidence, not copied into both documents.

```yaml
checkpoint:
  audit: MAIN_STREET_TO_GRANDRUE_CLAIM_PRESERVATION
  status: DRAFT_NOT_EXECUTED
  run_id: null
  baseline_commit: null
  target_commit: null
  claim_snapshot_digest: null
  evidence_root: null
  latest_receipt: null
  coverage:
    enumeration_complete: false
    baseline_entries: null
    target_entries: null
    represented_baseline_entries: null
    represented_target_entries: null
    unclassified_files: null
    claims_total: null
    claims_verified: null
    open_findings: null
  conclusions:
    file_accounting: NOT_RUN
    claim_validation: NOT_RUN
    migration_preservation: NOT_RUN
    master_non_naming_executable_preservation: NOT_RUN
    live_freshness: NOT_RUN
  next_action: Prepare GR-VV-00-01 from fresh repository evidence; open no audit result until snapshots and claims are pinned.
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
