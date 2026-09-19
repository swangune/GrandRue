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
observed_head_at_resume_reconciliation: 895aab8729e863040b212cab5cdfbe9d01bda1d7

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
    id: IMP-08C-C4B2
    state: READY
  note: >
    IMP-08C-C3 is CONFORMING_COMPLETE from the 19 September rollout-ordering
    closure and full Maven/PostgreSQL gate. IMP-08C remains IN_PROGRESS; C4B2,
    C4E and C5A are READY siblings and C5 remains dependency-blocked.
    verification remain.

slice:
  id: C3-TRUSTED-CATALOGUE-ADMISSION
  state: READY
  behaviour: >
    Admit only the exact approved standard-commercial-catalogue@1 at the trusted
    publication boundary while keeping exact-content approval distinct from
    explicit platform publication authorisation.

canonical_graph: docs/development/implementation-programme-state.json
historical_status_compatibility: docs/development/implementation-status.md

next_action: >
  Prepare the exact C4B2 authority/dependency boundary once, derive the first
  dependency-complete behavioural slice and tests, then implement the minimum
  accepted source/discovery/publication integration under IMPLEMENTATION-RULES.
  then run the full C3 completion gate before any closure claim.
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
| Initial standard commercial catalogue | `designs/authorities/ms-prot/MS-PROT-056/MS-PROT-056 v1.10 — Initial Standard Commercial Catalogue Manifest.md` |

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
