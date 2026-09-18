# GrandRue Implementation Controller

**Status:** ACTIVE  
**Authority class:** NON-AUTHORITATIVE OPERATIONAL CONTROLLER  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Controller schema:** `grandrue-implementation-controller/v1`

This file is the concise, resumable execution pointer for GrandRue implementation work.

It does **not** define product semantics, architecture, implementation-programme authority, completion authority, or supersession. If it conflicts with accepted authority, the canonical implementation graph, current conformance evidence, or the mandatory status record, this file is stale/defective and MUST be corrected before use.

Under the currently accepted `designs/IMPLEMENTATION-RULES.md`, `docs/development/implementation-status.md` remains a mandatory cycle-closing navigation/evidence record. This controller does not replace that requirement unless a separately approved governance amendment later says so.

---

## 1. Governing sources

| Responsibility | Canonical source |
|---|---|
| Which accepted authority governs | `designs/AUTHORITY-INDEX.md` |
| How implementation is executed | `designs/IMPLEMENTATION-RULES.md` |
| Macro implementation sequencing | `designs/authorities/programme/MS-IMP-001/MS-IMP-001.md` + accepted composition |
| Fine-grained dependency/readiness graph | `docs/development/implementation-programme-state.json` |
| Mandatory cycle-closing status/evidence navigation | `docs/development/implementation-status.md` |
| Repository-agent navigation/stop conditions | `AGENTS.md` |

Git commits, tests, code, this file and implementation evidence prove/describe implementation facts; they do not create missing semantic authority.

---

## 2. Efficiency model

GrandRue implementation is navigated operationally as:

```text
macro/node eligibility
        ↓
resolve exact accepted behaviour
        ↓
one dependency-complete behavioural slice
        ↓
tests/specification for that slice
        ↓
minimum conforming implementation
        ↓
checkpoint verification
        ↓
commit
        ↓
cycle-closing graph/evidence/status synchronisation when required
```

The objective is **fewer reasoning boundaries**, not merely fewer commits.

### Execution slice

An execution slice is the largest coherent implementation change that:

- realises one already-governed behaviour/responsibility or tightly coupled set of behaviours;
- has a dependency boundary that can be closed exactly;
- can be tested and falsified coherently;
- does not require an unresolved semantic/architectural decision; and
- can be committed without leaving an intentionally broken intermediate state.

A slice is **not** defined by file count, class count, package count, or an arbitrary maximum.

Where several files are merely mechanisms needed for the same accepted behaviour, they SHOULD be analysed and implemented as one slice rather than separate file-level cycles.

### Evidence granularity

Fine-grained traceability is preserved through:

```text
exact authority pointers
+
tests
+
changed paths/diff
+
Git commit
+
bounded implementation evidence where materially required
```

Fine-grained evidence does not require fine-grained workflow steps.

---

## 3. Preparation and execution

For a material implementation node:

### PREPARE ONCE

Resolve:

- exact accepted authority/invariants;
- participating capability/owner boundaries;
- dependency closure;
- affected production/test surface;
- required targeted/integration/architecture verification;
- known non-goals;
- mandatory widening triggers; and
- explicit `DESIGN_ESCALATION` conditions.

Then select one dependency-complete behavioural slice.

### EXECUTE

Within the prepared slice:

1. write/update executable tests first where accepted behaviour can be specified;
2. run the targeted RED evidence where required;
3. implement the minimum conforming code across the whole closed slice;
4. run proportional targeted/integration checks;
5. refactor only within accepted behaviour;
6. verify/falsify the checkpoint;
7. commit the coherent passing slice; and
8. perform the cycle-closing graph/evidence/status synchronisation required by accepted `IMPLEMENTATION-RULES.md`.

Do not repeatedly rediscover the same dependency/authority boundary for each file in the slice.

---

## 4. Exception-driven widening

A prepared slice remains executable only while all material behaviour is already determined.

Split/widen/escalate when implementation encounters:

- missing, ambiguous or contradictory accepted semantics;
- a newly affected capability/semantic owner;
- new public/internal contract meaning;
- persistence/schema/runtime-data consequences not already governed;
- transaction, concurrency, retry/idempotency or historical-affinity consequences outside the prepared boundary;
- security/authorisation/trust consequences outside the prepared boundary;
- provider-responsibility or Exposure/projection ownership uncertainty;
- a repository fact contradicting the prepared dependency closure; or
- any other `AGENTS.md` mandatory-widening trigger.

If widening resolves the issue from current accepted authority, rebuild the slice boundary and continue.

If authority remains insufficient, the affected path enters `DESIGN_ESCALATION`. Independent READY work may continue only where accepted governance permits it.

---

## 5. Checkpoint discipline

The preferred checkpoint is the **smallest dependency-complete, independently verifiable behavioural slice**, not the smallest edit.

A checkpoint commit SHOULD normally contain the production code and relevant tests for that slice together. Intermediate RED/GREEN/evidence commits remain permitted where current `IMPLEMENTATION-RULES.md` allows them.

Do not create dedicated evidence documents for trivial implementation mechanics when Git + tests + existing evidence already provide sufficient traceability. Create bounded evidence where material completion, architecture, concurrency/recovery, provider/security behaviour, or programme-node conformance requires durable explanation/proof.

Full-suite verification remains governed by `IMPLEMENTATION-RULES.md`; targeted checks during a slice do not substitute for a required cycle/node completion gate.

---

## 6. Current implementation frontier

This is an operational snapshot only. Always reconcile it against the current graph/evidence before execution.

```yaml
controller_schema: grandrue-implementation-controller/v1
repository: swangune/GrandRue
branch: development
repository_head_observed_at_formalisation: 8e386d0e901f4e4ba18f061a45b4cabad9b4285d

macro:
  id: IMP-08C
  state: IN_PROGRESS

selected_node:
  id: IMP-08C-C3
  state: IN_PROGRESS
  execution_slice: null
  slice_state: PREPARATION_REQUIRED

other_ready_work:
  - IMP-08C-C4B2
  - IMP-08C-C4E
  - IMP-08A-A0
  - IMP-08B-B0

canonical_graph: docs/development/implementation-programme-state.json
mandatory_status: docs/development/implementation-status.md
blocker:
  type: DESIGN_ESCALATION
  scope: IMP-08C-C3 complete concrete production manifest
  authority_reference: MS-PROT-056-V17-DQ-001
  note: This blocker applies only to the affected C3 path; verify current authority/evidence before relying on it.

next_action: >
  Before implementation resumes, verify branch/HEAD, reconcile this snapshot with the
  canonical graph/status/evidence, resolve the exact authority for the selected work,
  and prepare one dependency-complete behavioural slice. Do not begin a file-level
  sequence merely because multiple files are involved.
```

---

## 7. Restart protocol

A fresh implementation session should:

1. establish current branch and HEAD;
2. read `AGENTS.md`;
3. read this controller for the live operational pointer;
4. verify the pointer against `implementation-programme-state.json`, `implementation-status.md` and relevant current evidence;
5. resolve the exact accepted authority through `AUTHORITY-INDEX.md`;
6. invalidate/rebuild the pointer if branch, graph, authority, evidence or scope changed materially;
7. prepare one dependency-complete behavioural slice;
8. execute tests-first/minimum-code/verification under `IMPLEMENTATION-RULES.md`;
9. commit the coherent passing checkpoint; and
10. close the implementation cycle through the canonical graph/evidence/status process before selecting the next governed node where required.

Conversation memory is never a substitute for these repository-resident checkpoints.

---

## 8. Controller maintenance boundary

This controller SHOULD stay small and current.

It SHOULD contain:

- current macro/node;
- current selected slice or preparation state;
- immediate blockers/escalations;
- exact canonical navigation pointers;
- last relevant checkpoint/commit when useful; and
- immediate next action.

It SHOULD NOT accumulate:

- historical implementation narrative;
- completed-node catalogues already represented in the graph/evidence;
- copied design semantics;
- duplicated dependency graphs;
- long test logs; or
- per-file diaries.

History belongs in Git and bounded evidence. Dependency/readiness truth belongs in the canonical programme graph. Accepted meaning belongs in accepted authority.
