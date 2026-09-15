# Main Street TAS Consolidation Review — 26 August 2026

**Status:** REVIEW COMPLETE — CONSOLIDATION RECOMMENDATION DEFERRED UNTIL ACTIVE SEMANTIC GATE CLOSES  
**Authority class:** Architecture-documentation review evidence only  
**Governed by:** `designs/DOCUMENT-GOVERNANCE.md`, `designs/DESIGN-RULES.md`  
**Branch:** `development`  
**Purpose:** Determine how the legacy/high-level TAS material should evolve to describe the architecture now governed by the MS-PROT series and accepted ADR/TAS authorities without duplicating semantic documents or creating another architecture source of truth.

---

## 1. Current TAS Situation

`docs/development/TAS/` currently contains a small historical set including:

```text
vision.md
context.md
high-level-architecture.md
backup-restore-disaster-recovery.md
```

`backup-restore-disaster-recovery.md` is indexed accepted implementation/production architecture as MS-TAS-RECOVERY-001.

The earlier `high-level-architecture.md` describes a conventional logical modular-monolith architecture and explicitly does not define deployment architecture. Its broad module list predates much of the later executable semantic architecture.

The later accepted design corpus now governs substantially more precise boundaries including:

```text
Semantic Registry Release
SemanticReleaseAssembly
published release evidence / bootstrap
Merchant Configuration Revision
Resolved Configuration Package
atomic activation
capability-owned runtime execution
contract-scoped executable support
cross-capability orchestration
provider fulfilment
persistence / events / projections
projection serviceability
network uncertainty / resilience
background work / observability / security
recovery
```

Therefore the old TAS is useful context but is not an adequate current architectural map by itself.

---

## 2. Governing Decision for Consolidation

Do **not** create one TAS document per MS-PROT.

That would duplicate semantic ownership and cause authority drift.

The appropriate TAS role is:

> **Describe how accepted semantic responsibilities are structurally realised and deployed, while referencing rather than restating the semantic authorities.**

---

## 3. Recommended Current TAS Views

When TAS consolidation is deliberately activated, use a small set of stable views.

### TAS View A — System Context & Architectural Principles

Should describe:

- Main Street as merchant infrastructure rather than marketplace/gatekeeper;
- users/actors/external providers at system boundary;
- modular-monolith starting point;
- tenant/merchant isolation;
- semantic authority hierarchy;
- why external providers do not become business truth.

Must reference accepted authorities rather than redefine them.

### TAS View B — Runtime & Modular-Monolith Architecture

Should describe:

```text
transport/adapters
application use cases/orchestration
capability modules
capability-owned persistence
provider adapters
background-work infrastructure
projection/read delivery
```

It should make dependency direction and composition-root responsibilities concrete while preserving MS-PROT-032/033 and ADR-009 boundaries.

### TAS View C — Semantic Configuration, Release & Execution Architecture

Should compose:

```text
MS-PROT-054
ADR-010
ADR-011
MS-PROT-022/040
ADR-012
```

into one implementation/deployment view showing:

```text
registered definitions
→ immutable published release evidence
→ SemanticReleaseAssembly
→ configuration compilation
→ exact RCP activation
→ invocation RCP binding
→ executable-support resolution
```

This view must not invent new semantic lifecycle states.

### TAS View D — Persistence, Events, Projection & Integration Architecture

Should structurally map:

```text
MS-PROT-025/033/034
MS-PROT-026
MS-PROT-027 v1.3
MS-PROT-048/067
MS-PROT-065
MS-PROT-069/070/072
```

with particular attention to:

- capability-owned write models;
- cross-capability transaction boundaries;
- outbox/post-commit work where applicable;
- projection/materialisation boundaries;
- provider timeout/reconciliation;
- credential boundaries.

### TAS View E — Deployment, Operations, Recovery & Observability

Should compose:

```text
MS-PROT-068
MS-PROT-069
MS-PROT-070
MS-PROT-073
ADR-011
ADR-012
MS-TAS-RECOVERY-001
```

and later concrete deployment selections.

This is the appropriate home for physical topology, health/readiness, rollout, backup/recovery and operational support decisions that do not belong in semantic MS-PROT authority.

---

## 4. What the TAS Must Not Do

Consolidated TAS documents MUST NOT:

- redefine Order, Booking, Appointment, CustomerContext or other business concepts;
- copy complete MS-PROT semantic definitions;
- introduce a second lifecycle for Merchant Configuration or Semantic Release;
- turn provider topology into business semantics;
- choose business policy because a deployment technology prefers it;
- duplicate `AUTHORITY-INDEX.md` as another current-authority list;
- declare a component `source of truth` where accepted capability authority says otherwise; or
- require microservices merely to mirror bounded-context names.

---

## 5. Treatment of Existing TAS Files

### `backup-restore-disaster-recovery.md`

**KEEP AS ACCEPTED AUTHORITY.** It is already indexed as MS-TAS-RECOVERY-001.

A future View E may reference it or become a later accepted replacement only through explicit scope-aware supersession.

### `context.md` / `vision.md`

**PRESERVE AS HISTORICAL/CONTEXT EVIDENCE.** They may inform View A but should not be silently rewritten as current semantic authority.

### `high-level-architecture.md`

**PRESERVE UNTIL A REPLACEMENT TAS IS GOVERNED AND ACCEPTED.** It is not indexed as current implementation architecture. A later Runtime TAS may supersede its structural scope after review/approval.

---

## 6. Why Consolidation Is Not Formalised Now

The current commerce-inclusive vertical-slice falsification still has one promoted material semantic blocker:

```text
Order / Ordering commitment authority
```

Formalising a new runtime TAS before that boundary is accepted risks encoding invented Order dependencies into the implementation architecture.

Therefore:

```text
TAS consolidation review       COMPLETE
new accepted TAS rewrite        DEFERRED
```

until the active semantic gate is closed and the complete vertical slice is rerun.

This is a deliberate stopping rule, not missing work.

---

## 7. Implementation-Readiness Trigger

After the Order authority is either approved/formalised or deliberately rejected/revised, and the vertical slice passes for the selected implementation scope, TAS consolidation should proceed only where an engineer still needs a structural/deployment decision not already derivable from accepted authority.

The first likely candidate is **Semantic Configuration, Release & Execution Architecture**, because ADR-010/011/012 now form a coherent implementation seam that benefits from one structural diagram/view.

---

## 8. Review Verdict

```text
one TAS per MS-PROT                 REJECTED
five stable implementation views   RECOMMENDED
mass rewrite before Order closure  REJECTED
MS-TAS-RECOVERY-001                PRESERVE ACCEPTED
old high-level TAS                 PRESERVE as unindexed historical/context evidence
```

> **Consolidate the TAS around implementation views after the active semantic gate closes; do not duplicate the semantic corpus or turn documentation structure into architecture.**
