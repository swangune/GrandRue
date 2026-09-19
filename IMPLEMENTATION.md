# GrandRue Implementation Controller

**Status:** `ACTIVE`  
**Authority class:** `NON-AUTHORITATIVE OPERATIONAL CONTROLLER`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Controller schema:** `grandrue-implementation-controller/v2`  
**Governed execution rules:** `MS-IMPLEMENTATION-RULES-001 v2.1` — `designs/IMPLEMENTATION-RULES.md`

This is the single live implementation execution pointer. It does not create semantic meaning, architecture, implementation-programme authority, graph edges, completion permission or design approval.

The canonical fine-grained dependency/readiness graph is `docs/development/implementation-programme-state.json`. Current repository evidence and that graph outrank this controller if they conflict.

## 1. Current gate

GrandRue naming migration, independent post-migration preservation verification and GR-REN-08 through GR-REN-11 closure are complete. Implementation has re-entered the preserved programme frontier.

```yaml
controller_schema: grandrue-implementation-controller/v2
repository: swangune/GrandRue
branch: development
observed_head_at_resume_reconciliation: 345bd7b7295a6b387b8af3d5b1c25bf0019c8afd

phase: IMPLEMENTATION
implementation_state: IN_PROGRESS

migration_handoff:
  migration_execution_complete: true
  post_migration_verification_complete: true
  gr_ren_08_through_11_closed: true
  unresolved_migration_defect: false
  implementation_handoff: READY

programme_frontier:
  macro:
    id: IMP-08C
    state: IN_PROGRESS
  node:
    id: IMP-08C-C5A
    state: READY
  note: >
    IMP-08C-C3, C4B2 and C4E are CONFORMING_COMPLETE. C4 is therefore
    CONFORMING_COMPLETE. C5A is the remaining READY child required before C5;
    IMP-08C remains IN_PROGRESS.

slice:
  id: C5A-OPERATIONAL-EVIDENCE-FOUNDATION
  state: PREPARATION_REQUIRED
  behaviour: >
    Register bounded operational-evidence semantics for the active durable
    Event Reaction and Background Work responsibilities, preserve UNKNOWN
    evidence honestly, and expose only read-only progression evidence needed
    for diagnosis and recovery.

canonical_graph: docs/development/implementation-programme-state.json
historical_status_compatibility: docs/development/implementation-status.md

next_action: >
  Execute C5A tests first from accepted MS-PROT-068 v1.1 / MS-PROT-069 v1.1:
  add the minimum OperationalEvidenceContract registration needed by the active
  Event Reaction and Durable Background Work paths, preserve UNKNOWN rather
  than manufacturing health, and add read-only progression evidence where the
  existing stores cannot diagnose backlog safely. Do not make telemetry,
  alerts or evidence readers mutation/completion authority.
```

## 2. Governing sources

| Responsibility | Source |
|---|---|
| Current accepted authority navigation | `designs/AUTHORITY-INDEX.md` |
| Implementation execution rules | `designs/IMPLEMENTATION-RULES.md` |
| Macro programme | `designs/authorities/programme/MS-IMP-001/MS-IMP-001.md` + current accepted composition |
| Fine-grained dependency/readiness graph | `docs/development/implementation-programme-state.json` |
| Repository agent navigation | `AGENTS.md` |
| Migration execution closure | `GRANDRUE-MIGRATION.md` |
| Independent migration verification | `GRANDRUE-POST-MIGRATION-VERIFICATION.md` |
| Current operational-evidence authority | `designs/authorities/ms-prot/MS-PROT-068/MS-PROT-068 v1.1 — Production Operational Evidence, Health & Alerting Contract Amendment.md` + `designs/authorities/ms-prot/MS-PROT-069/MS-PROT-069 v1.1 — Production Reconciliation Responsibility, Evidence & Resolution Contract Amendment.md` |

## 3. Current execution model

```text
active IN_PROGRESS node
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
5. continue the selected READY/IN_PROGRESS node while its authority/dependencies remain unchanged;
6. use tests first and source-rooted correction under `IMPLEMENTATION-RULES.md`;
7. synchronise this controller at durable slice checkpoints and terminal node transitions.

Conversation memory is never a substitute for repository state.

## 5. Controller boundary

Keep this file small and current. Do not accumulate historical implementation narratives, duplicated graph data, copied semantic rules, long test logs or per-file diaries. History belongs in Git and bounded evidence.
