# GrandRue Implementation Controller

**Status:** `PAUSED_FOR_MIGRATION_VERIFICATION`  
**Authority class:** `NON-AUTHORITATIVE OPERATIONAL CONTROLLER`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Controller schema:** `grandrue-implementation-controller/v2`  
**Governed execution rules:** `MS-IMPLEMENTATION-RULES-001 v2.0` — `designs/IMPLEMENTATION-RULES.md`

This is the single live implementation execution pointer. It does not create semantic meaning, architecture, implementation-programme authority, graph edges, completion permission or design approval.

The canonical fine-grained dependency/readiness graph is `docs/development/implementation-programme-state.json`. Current repository evidence and that graph outrank this controller if they conflict.

## 1. Current gate

Implementation is intentionally paused while the GrandRue naming migration and required post-migration verification/closure are incomplete.

```yaml
controller_schema: grandrue-implementation-controller/v2
repository: swangune/GrandRue
branch: development
observed_head_at_v2_formalisation: 058d7264836f22c03f6954a979e767127c0f8f3c

phase: MIGRATION_AND_POST_MIGRATION_VERIFICATION
implementation_state: PAUSED_FOR_MIGRATION_VERIFICATION

preserved_programme_frontier:
  macro:
    id: IMP-08C
    state: IN_PROGRESS
  node:
    id: IMP-08C-C3
    state: IN_PROGRESS
  note: Reconcile against the canonical graph/current tree after post-migration verification before resuming.

slice:
  id: null
  state: null

canonical_graph: docs/development/implementation-programme-state.json
historical_status_compatibility: docs/development/implementation-status.md

resume_gate:
  migration_execution_complete: required
  post_migration_verification_complete: required
  gr_ren_08_through_11_closed: required
  unresolved_migration_defect: false_required
  blocking_design_escalation: false_required

next_action: >
  Continue governed migration and automatic post-migration verification. When the
  verification/GR-REN closure succeeds, reconcile the canonical implementation graph
  against the verified final repository, refresh this controller, select an eligible
  READY node, prepare one dependency-complete behavioural slice and resume
  implementation automatically without another user prompt.
```

## 2. Governing sources

| Responsibility | Source |
|---|---|
| Current accepted authority navigation | `designs/AUTHORITY-INDEX.md` |
| Implementation execution rules | `designs/IMPLEMENTATION-RULES.md` |
| Macro programme | `designs/authorities/programme/MS-IMP-001/MS-IMP-001.md` + current accepted composition |
| Fine-grained dependency/readiness graph | `docs/development/implementation-programme-state.json` |
| Repository agent navigation | `AGENTS.md` |
| Migration execution | `GRANDRUE-MIGRATION.md` |
| Independent migration verification | `GRANDRUE-POST-MIGRATION-VERIFICATION.md` |

## 3. v2 execution model after resume

```text
eligible READY node
        ↓
prepare authority / dependency boundary once
        ↓
dependency-complete behavioural slice
        ↓
tests first
        ↓
minimum conforming implementation
        ↓
proportional slice verification
        ↓
durable slice checkpoint
        ↓
full applicable gate before node completion
        ↓
graph/evidence/controller synchronisation
```

A slice is bounded by one coherent accepted behaviour and its dependency closure, not file/class count.

## 4. Restart rule

On any new implementation session:

1. establish current branch/HEAD;
2. read `AGENTS.md` and this controller;
3. verify this controller against the canonical graph and current evidence;
4. resolve exact accepted authority through `AUTHORITY-INDEX.md`;
5. if migration/post-migration verification is incomplete, remain paused;
6. after successful handoff, reconcile the graph/current tree before selecting work;
7. prepare one dependency-complete behavioural slice and execute under v2.0;
8. synchronise this controller at durable slice checkpoints and terminal node transitions.

Conversation memory is never a substitute for repository state.

## 5. Controller boundary

Keep this file small and current. Do not accumulate historical implementation narratives, duplicated graph data, copied semantic rules, long test logs or per-file diaries. History belongs in Git and bounded evidence.