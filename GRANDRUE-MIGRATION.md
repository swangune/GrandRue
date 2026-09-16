# GrandRue Naming Migration Ledger

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Baseline:** `c4153441d8340b229a29884967d796280d949a7d`  
**Status:** `IN_PROGRESS`  
**Authority class:** Non-semantic operational migration ledger  
**Purpose:** Preserve resumable, auditable state for the controlled Main Street → GrandRue repository naming migration without redefining accepted semantic authority.

---

## 1. Migration Boundary

This migration distinguishes three identity classes:

```text
GrandRue
    = current product / runtime / implementation identity

MS-PROT-* / MS-IMP-* / other stable MS-* governance identifiers
    = durable authority identity
    = preserve unless separately governed

historical Main Street evidence
    = preserve as historical terminology
```

The active migration scope is deliberately **runtime-centred**.

A file is an active migration target only when it is directly required to build, configure, test, deploy, serve or govern the current GrandRue runtime/repository operation. Design explanation, historical evidence, implementation evidence, experiments and other reference material are not migration targets merely because they contain Main Street terminology.

Current governance/navigation documents remain in scope where their wording or references must describe the present GrandRue repository accurately. Reference-only files may be consulted without being renamed.

This migration MUST NOT be implemented as a blind global search-and-replace.

A naming change MUST NOT silently alter accepted semantic meaning, authority ownership, persisted identity, replay/idempotency behaviour, external compatibility, database migration history or historical evidence.

`designs/AUTHORITY-INDEX.md` remains authoritative for current accepted authority navigation. This ledger records migration execution state only.

---

## 2. Governing Repository Rules

Before migration work, inspect and follow the current versions of:

- `AGENTS.md`
- `designs/DESIGN-RULES.md`
- `designs/DOCUMENT-GOVERNANCE.md`
- `designs/AUTHORITY-INDEX.md`
- `designs/DEFERRED-DECISION-REGISTER.md`
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
- `designs/IMPLEMENTATION-RULES.md`

Execution constraints for this migration:

- use the existing `development` branch;
- do not create a branch unless explicitly authorised in chat;
- repository manipulation is permitted;
- do not run Maven tests unless explicitly authorised;
- do not run GitHub Actions unless explicitly authorised;
- make the smallest conforming change;
- inspect before renaming;
- do not combine naming migration with semantic redesign or unrelated cleanup;
- stop the affected migration path when semantics or compatibility are materially unresolved.

---

## 3. Protected and Reference-Only Identity Rules

### 3.1 Preserve stable governance and authority identity

Do not mechanically rename:

- `MS-PROT-*`
- `MS-IMP-*`
- `MS-IMPLEMENTATION-RULES-*`
- `MS-DESIGN-RULES-*`
- accepted DQ identifiers
- accepted contract versions

These are durable governance/authority identities unless a separately approved authority explicitly changes them.

### 3.2 Reference-only design corpus

Within `designs/**`, only the current governance/navigation documents listed below are active migration surfaces:

- `designs/DESIGN-RULES.md`
- `designs/DOCUMENT-GOVERNANCE.md`
- `designs/AUTHORITY-INDEX.md`
- `designs/DEFERRED-DECISION-REGISTER.md`
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
- `designs/IMPLEMENTATION-RULES.md`

All other files under `designs/**` are **outside the scope of this naming migration**, including:

- `designs/authorities/**`;
- `designs/system/**`;
- `designs/historical/**`; and
- any other non-governance design file.

Do not lexical-inventory, rename, restructure or rewrite those reference-only files merely to replace Main Street terminology. Current governance/navigation documents may continue to reference them by stable identifiers and existing paths.

Reference-only design files may be read when a semantic, ownership, historical or compatibility question requires consultation. Read-only consultation does not make them migration targets.

### 3.3 Preserve historical and implementation evidence

Do not rewrite historical or implementation evidence solely for branding consistency, including:

- historical implementation evidence;
- old conformance records;
- handoffs or records describing the project when it was named Main Street;
- development evidence under `docs/**` that is not required to run or govern current GrandRue; and
- experiments or prototypes retained as evidence rather than current runtime surfaces.

Historical terminology is valid repository-evolution evidence.

The operational migration evidence file `docs/development/grandrue-naming-migration-inventory.md` remains writable because this ledger uses it to record migration evidence. It is not itself a product-branding migration target.

### 3.4 Preserve immutable database migration history

Existing applied Flyway migration files are immutable for this naming migration.

If database state contains a current value that must change, use a new forward migration after compatibility requirements have been established. Never rewrite an old migration merely to replace Main Street terminology.

---

## 4. Naming Forms Requiring Inventory

The `GR-REN-01A` through `GR-REN-01F` inventory track MUST collectively inventory at least these forms within the **in-scope runtime/governance migration surface**:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

The inventory track MUST also inspect naming embedded in active runtime/repository surfaces including:

- environment-variable prefixes;
- Spring properties/configuration keys;
- Maven coordinates;
- Docker/container identifiers;
- database names;
- schema/table data values where product identity may be persisted;
- URLs/domains;
- serialized values;
- event identifiers;
- command identities;
- contract identities;
- provider references;
- entitlement identities;
- runtime-coupled test fixtures;
- filenames/directories;
- fully qualified Java class-name strings;
- reflection/class-name persistence;
- Spring component scanning and build/plugin wiring; and
- the current governance/navigation documents explicitly retained by Section 3.2.

Explicitly excluded reference-only material does not require lexical inventory or migration classification. References to excluded files that appear in in-scope governance/navigation documents may be inventoried as references without traversing or changing the referenced files.

No mutation task may begin until `GR-REN-01F` has reconciled and classified the complete **in-scope** material inventory.

---

## 5. Classification Vocabulary

Every material in-scope occurrence identified by the `GR-REN-01A` through `GR-REN-01F` inventory track MUST be classified as exactly one of:

- `RENAME_CURRENT_PRODUCT`
- `RENAME_CODE_NAMESPACE`
- `RENAME_CURRENT_DOCUMENTATION`
- `MIGRATION_REQUIRED_PERSISTED_ID`
- `COMPATIBILITY_ALIAS_REQUIRED`
- `PRESERVE_STABLE_GOVERNANCE_ID`
- `PRESERVE_HISTORICAL_EVIDENCE`
- `PRESERVE_IMMUTABLE_MIGRATION`
- `REVIEW_REQUIRED`

Occurrences inside explicitly excluded reference-only scopes do not require migration classification.

`REVIEW_REQUIRED` occurrences MUST NOT be renamed until their semantics and compatibility requirements are established.

---

## 6. Persisted Identity Safety Gate

Before renaming any runtime string, ask:

> Can this value already exist outside the source tree?

If the answer is `YES` or `UNKNOWN`, inspect compatibility requirements before changing it.

Sensitive categories include:

- event identities;
- command identities;
- commercial entitlement identities;
- semantic contract references;
- serialized manifests;
- database values;
- message/outbox records;
- provider references;
- API payload values;
- configuration identifiers;
- idempotency keys;
- audit evidence;
- reflection/FQCN persistence.

Values participating in equality, persistence, replay, idempotency or external compatibility are migration problems, not text replacements.

---

## 7. Java Namespace Rule

The expected current-code namespace target, subject to `GR-REN-01F` classification and compatibility review, is:

```text
package mainstreet.*
    →
package grandrue.*
```

with corresponding source layout:

```text
src/main/java/mainstreet/
    → src/main/java/grandrue/

src/test/java/mainstreet/
    → src/test/java/grandrue/
```

Production package declarations, imports and filesystem paths must move coherently. Before renaming, inventory production packages, tests, imports, FQCN strings, Spring scanning, reflection, serialization, migration scripts, test resources and build/plugin configuration.

The repository MUST NOT be left indefinitely in an ambiguous half-package state.

---

## 8. Hierarchical Task and Dependency Model

The migration plan is represented as one canonical **hierarchical dependency graph** in this ledger.

Three relationships are deliberately separate:

```text
parent
    = where a node belongs in the migration decomposition

depends_on
    = what must be satisfied before an executable node may run

evidence
    = what proves the node actually completed
```

The hierarchy provides human navigation and scope roll-up. Dependency edges provide safe execution ordering. Git commits plus recorded inspection evidence provide the durable trail.

Phase numbering is navigational. Numeric order alone MUST NOT be treated as execution authority.

### 8.1 Node types

Only three node types are used:

- `GROUP` — a scope container. It is never executed directly.
- `TASK` — one bounded action or inspection with one coherent stopping point.
- `GATE` — an explicit condition check whose result controls dependent work.

Only a concrete `TASK` or `GATE` may be the active execution target.

A wildcard or family expression such as `GR-REN-02B-*` is notation only and MUST NOT be treated as an executable node.

### 8.2 Node state

Executable nodes use:

- `NOT_STARTED`
- `READY`
- `IN_PROGRESS`
- `BLOCKED`
- `COMPLETE`
- `NOT_APPLICABLE`

Groups use:

- `EXPANSION_REQUIRED` — scope exists but must be decomposed further before execution reaches it;
- `OPEN` — children exist and the group is not yet complete;
- `COMPLETE` — the complete group scope is accounted for and all required descendants/closeout conditions are satisfied;
- `EXCLUDED` — the complete group scope is deliberately outside this migration and requires no execution or child decomposition.

`READY` is valid only when all explicit dependencies are satisfied, the node scope is concrete, required authority/decisions are available, no blocker is open, and the requested action is authorised.

### 8.3 Canonical node record

Every executable node MUST ultimately have these fields recorded in this ledger or in an explicitly linked non-authoritative migration inventory artifact:

```yaml
id: stable node identifier
parent: exactly one parent group
kind: TASK | GATE
purpose: one bounded outcome
scope: exact files/paths/inventory entries/identity family
inputs: relevant inventory entries, authority or prior decisions
depends_on: zero or more prerequisite node ids
done_when: objective completion condition
state: executable-node state
evidence: result, commit sha where applicable, inspection and verification
```

Rules:

1. Record `parent` once. Children and ancestors are derived; do not maintain independent ancestor lists.
2. A node may have multiple `depends_on` edges.
3. Parentage does not by itself imply execution order.
4. A `GROUP` is complete only when its entire declared in-scope work has been allocated, all required descendants are `COMPLETE` or validly `NOT_APPLICABLE`, any excluded child scope is explicitly recorded as `EXCLUDED`, and any group closeout gate is complete.
5. An unexpanded group may remain in the plan, but execution MUST NOT enter it while it is `EXPANSION_REQUIRED`.
6. If an unstarted task proves too large, preserve its identifier as the parent `GROUP`, create children, and record the decomposition. Do not silently redefine the old task boundary.
7. If a started task must be decomposed, preserve already-created evidence, allocate the unfinished scope explicitly to children, and retain traceability to the original node.
8. A completed task is historical evidence. Corrective work receives a new task; completion history is not rewritten.
9. An `EXCLUDED` group is not a deferred task. It is an explicit scope boundary and MUST NOT later be treated as unfinished migration work unless the migration contract is deliberately amended.

### 8.4 Task sizing and safe stopping points

A task MUST be subdivided before execution when it would otherwise:

- mix discovery with mutation;
- mix compatibility decisions with implementation;
- span unrelated identity categories;
- have an unbounded or discover-as-you-go mutation scope;
- require a diff or evidence set too large to inspect confidently in one bounded pass;
- make restart state ambiguous; or
- combine independent changes merely because they share the Main Street spelling.

Task size is determined by a **safe stopping point**, not by an arbitrary file-count limit.

A valid executable leaf must have:

1. one identifiable outcome;
2. an explicit bounded scope;
3. a coherent repository stopping point;
4. an objective `done_when` condition; and
5. a bounded evidence set.

For discovery tasks, a bounded search scope is sufficient. For mutation tasks, freeze the intended change manifest before mutation begins.

A task may complete without a non-ledger repository change when inspection proves that no applicable occurrence exists. The zero-result evidence must still be recorded.

### 8.5 Atomicity rule

Do not create smaller tasks by deliberately creating a broken intermediate repository state.

Java namespace migration is the primary example. One dependency-safe namespace wave must include every change required for that bounded wave to be internally coherent, which may include:

- filesystem path moves;
- package declarations;
- direct imports;
- safe non-persisted FQCN references;
- directly affected tests;
- source-set/build wiring; and
- Spring/reflection wiring required by that same wave.

If those items cannot be separated safely, they belong in one leaf even when they cross the former production/test/build phase boundaries.

Residual-audit nodes are for discovering accidental omissions. They MUST NOT be used as permission to knowingly leave broken references for a later phase.

### 8.6 Dependency and readiness rules

The dependency graph MUST remain acyclic.

If a dependency cycle appears:

1. inspect whether an artificial ordering constraint created the cycle;
2. remove the artificial edge when safe; otherwise
3. redefine the affected work as one atomic task or escalate the unresolved design/compatibility issue.

Do not break a cycle by ignoring a material prerequisite.

Additional rules:

- No rename/mutation leaf may become `READY` before `GR-REN-01F` is `COMPLETE`.
- A mutation affecting a persisted or externally visible identity must also depend on the specific compatibility-decision task that governs that identity, even when that decision lives under a numerically later group such as `GR-REN-06`.
- A decision result of `PRESERVE` or equivalent may make a planned mutation node `NOT_APPLICABLE`; it does not authorise the mutation.
- On the shared `development` branch, execute one mutation leaf at a time. Independent inspection/planning leaves may exist concurrently in the graph, but the ledger must identify one selected execution leaf.
- When several leaves are eligible, choose the smallest coherent leaf that advances the active group without crossing an unresolved dependency. Do not infer that the next numeric identifier is automatically correct.
- Excluded reference-only files may be consulted read-only when required to resolve a semantic or compatibility dependency; no dependency may convert that consultation into migration work.

### 8.7 Canonical hierarchy

`GR-REN` is the migration root.

| Node | Parent | Kind | Purpose | State |
|---|---|---|---|---|
| `GR-REN` | — | `GROUP` | Entire Main Street → GrandRue naming migration | `OPEN` |
| `GR-REN-00` | `GR-REN` | `TASK` | Baseline and migration contract | `COMPLETE` |
| `GR-REN-01` | `GR-REN` | `GROUP` | Runtime/governance inventory and classification | `OPEN` |
| `GR-REN-01A` | `GR-REN-01` | `GROUP` | Lexical search-form and repository-path inventory | `OPEN` |
| `GR-REN-01B` | `GR-REN-01` | `GROUP` | Java namespace/import/FQCN/Spring/reflection inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01C` | `GR-REN-01` | `GROUP` | Build/runtime/config/environment/container/database-name inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01D` | `GR-REN-01` | `GROUP` | Persisted/API/serialized/event/command/contract/provider/entitlement inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01E` | `GR-REN-01` | `GROUP` | Current governance/navigation and protected-reference boundary inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01F` | `GR-REN-01` | `GATE` | Reconcile all in-scope inventory, classify every material occurrence and freeze the action map | `NOT_STARTED` |
| `GR-REN-02` | `GR-REN` | `GROUP` | Production Java namespace migration | `EXPANSION_REQUIRED` |
| `GR-REN-02A` | `GR-REN-02` | `GROUP` | Partition production namespace into dependency-safe waves | `EXPANSION_REQUIRED` |
| `GR-REN-02B` | `GR-REN-02` | `GROUP` | Execute production namespace waves | `EXPANSION_REQUIRED` |
| `GR-REN-02C` | `GR-REN-02` | `GROUP` | Reconcile cross-wave production namespace references | `EXPANSION_REQUIRED` |
| `GR-REN-02D` | `GR-REN-02` | `GROUP` | Production Spring/reflection/runtime wiring closeout | `EXPANSION_REQUIRED` |
| `GR-REN-03` | `GR-REN` | `GROUP` | Test Java namespace migration | `EXPANSION_REQUIRED` |
| `GR-REN-03A` | `GR-REN-03` | `GROUP` | Partition test namespace work where it is not already atomic with production waves | `EXPANSION_REQUIRED` |
| `GR-REN-03B` | `GR-REN-03` | `GROUP` | Execute standalone test namespace waves where safe | `EXPANSION_REQUIRED` |
| `GR-REN-03C` | `GR-REN-03` | `GROUP` | Runtime-coupled test fixture/resource/class-name residual reconciliation | `EXPANSION_REQUIRED` |
| `GR-REN-04` | `GR-REN` | `GROUP` | Build naming and wiring | `EXPANSION_REQUIRED` |
| `GR-REN-04A` | `GR-REN-04` | `GROUP` | Maven coordinates/artifact/module naming | `EXPANSION_REQUIRED` |
| `GR-REN-04B` | `GR-REN-04` | `GROUP` | Build/source-set/plugin wiring not already atomic with namespace leaves | `EXPANSION_REQUIRED` |
| `GR-REN-05` | `GR-REN` | `GROUP` | Runtime/configuration/infrastructure naming | `EXPANSION_REQUIRED` |
| `GR-REN-05A` | `GR-REN-05` | `GROUP` | Spring property/configuration-key naming | `EXPANSION_REQUIRED` |
| `GR-REN-05B` | `GR-REN-05` | `GROUP` | Environment-variable naming | `EXPANSION_REQUIRED` |
| `GR-REN-05C` | `GR-REN-05` | `GROUP` | Docker/container/local-test infrastructure naming | `EXPANSION_REQUIRED` |
| `GR-REN-05D` | `GR-REN-05` | `GROUP` | Runtime database/connection naming excluding immutable migration history | `EXPANSION_REQUIRED` |
| `GR-REN-05E` | `GR-REN-05` | `GROUP` | URLs/domains/provider configuration/current runtime product strings | `EXPANSION_REQUIRED` |
| `GR-REN-06` | `GR-REN` | `GROUP` | Persisted and externally visible identity compatibility | `EXPANSION_REQUIRED` |
| `GR-REN-06A` | `GR-REN-06` | `GROUP` | Event/command/contract identity decisions | `EXPANSION_REQUIRED` |
| `GR-REN-06B` | `GR-REN-06` | `GROUP` | Entitlement/provider/configuration identity decisions | `EXPANSION_REQUIRED` |
| `GR-REN-06C` | `GR-REN-06` | `GROUP` | API/serialized/persisted-reflected FQCN decisions | `EXPANSION_REQUIRED` |
| `GR-REN-06D` | `GR-REN-06` | `GROUP` | Database/outbox/audit/idempotency identity decisions | `EXPANSION_REQUIRED` |
| `GR-REN-06E` | `GR-REN-06` | `GROUP` | Approved compatibility aliases/adapters | `EXPANSION_REQUIRED` |
| `GR-REN-06F` | `GR-REN-06` | `GROUP` | Approved forward database migrations | `EXPANSION_REQUIRED` |
| `GR-REN-06G` | `GR-REN-06` | `GROUP` | Structural replay/idempotency/external-compatibility review | `EXPANSION_REQUIRED` |
| `GR-REN-07` | `GR-REN` | `GROUP` | Current runtime product wording and repository governance | `EXPANSION_REQUIRED` |
| `GR-REN-07A` | `GR-REN-07` | `GROUP` | Runtime-visible non-identity product wording | `EXPANSION_REQUIRED` |
| `GR-REN-07B` | `GR-REN-07` | `GROUP` | Source comments and runtime-coupled operational wording | `EXPANSION_REQUIRED` |
| `GR-REN-07C` | `GR-REN-07` | `GROUP` | AGENTS/README/SEQUENCE/active navigation wording | `EXPANSION_REQUIRED` |
| `GR-REN-07D` | `GR-REN-07` | `GROUP` | Current prose in the Section 3.2 governance files safe for naming replacement | `EXPANSION_REQUIRED` |
| `GR-REN-07E` | `GR-REN-07` | `GROUP` | Current governance/navigation wording requiring non-mechanical review | `EXPANSION_REQUIRED` |
| `GR-REN-08` | `GR-REN` | `GROUP` | Residual audits | `EXPANSION_REQUIRED` |
| `GR-REN-08A` | `GR-REN-08` | `GROUP` | Code namespace/FQCN residual audit | `EXPANSION_REQUIRED` |
| `GR-REN-08B` | `GR-REN-08` | `GROUP` | Build/runtime/config/environment residual audit | `EXPANSION_REQUIRED` |
| `GR-REN-08C` | `GR-REN-08` | `GROUP` | Persisted/API/serialized/compatibility residual audit | `EXPANSION_REQUIRED` |
| `GR-REN-08D` | `GR-REN-08` | `GROUP` | In-scope governance/navigation residual audit | `EXPANSION_REQUIRED` |
| `GR-REN-08E` | `GR-REN-08` | `GROUP` | Reference-only boundary, protected stable-reference and Flyway-immutability audit | `EXPANSION_REQUIRED` |
| `GR-REN-09` | `GR-REN` | `GROUP` | Falsification | `EXPANSION_REQUIRED` |
| `GR-REN-09A` | `GR-REN-09` | `GROUP` | Persistence/replay/idempotency/external compatibility falsification | `EXPANSION_REQUIRED` |
| `GR-REN-09B` | `GR-REN-09` | `GROUP` | Semantic-authority contradiction/ownership falsification using excluded design authorities as read-only evidence where required | `EXPANSION_REQUIRED` |
| `GR-REN-10` | `GR-REN` | `GROUP` | Structural verification | `EXPANSION_REQUIRED` |
| `GR-REN-10A` | `GR-REN-10` | `GROUP` | Filesystem/package/import/source-set consistency | `EXPANSION_REQUIRED` |
| `GR-REN-10B` | `GR-REN-10` | `GROUP` | Spring/build/config/runtime wiring consistency | `EXPANSION_REQUIRED` |
| `GR-REN-10C` | `GR-REN-10` | `GROUP` | Git provenance/checkpoint/ledger consistency | `EXPANSION_REQUIRED` |
| `GR-REN-11` | `GR-REN` | `GROUP` | Final verification and closeout | `EXPANSION_REQUIRED` |
| `GR-REN-11A` | `GR-REN-11` | `GATE` | Full implementation verification where separately authorised and required | `NOT_STARTED` |
| `GR-REN-11B` | `GR-REN-11` | `GATE` | Final migration closeout | `NOT_STARTED` |

`GR-REN-07E` MUST NOT silently amend semantic authority. Excluded design files remain outside migration scope; if an in-scope governance/document change would require a material authority amendment, that issue follows its governing design/document lifecycle separately rather than widening this naming migration.

`GR-REN-11A` does not itself grant permission to run Maven tests or GitHub Actions. Section 14 continues to govern those actions.

### 8.8 Expanded active group — `GR-REN-01A`

`GR-REN-01A` is the first group expanded into bounded executable work. The repository tree inspected at pre-model-update HEAD `38124236816186368190dccfb23a5bdd221aee9a` is the scope baseline for this decomposition.

Every lexical task below searches all required forms from Section 4 within its exact in-scope surface and records both matches and an evidenced zero-match result where applicable. These tasks perform **no rename** and make **no semantic classification decision**.

| Node | Parent | Kind | Exact scope | Depends on | State |
|---|---|---|---|---|---|
| `GR-REN-01A-01` | `GR-REN-01A` | `TASK` | `.github/**`, `build_configuration/**`, `tools/**`, `.gitignore`, `compose.prototype.yml`, `lifecycle.md`, `operational-rules.md`, `pom.xml`, `workflow-tree.md` | `GR-REN-00` | `COMPLETE` |
| `GR-REN-01A-02` | `GR-REN-01A` | `TASK` | `src/main/**` | `GR-REN-00` | `COMPLETE` |
| `GR-REN-01A-03` | `GR-REN-01A` | `TASK` | `src/test/**` | `GR-REN-00` | `COMPLETE` |
| `GR-REN-01A-04` | `GR-REN-01A` | `TASK` | `storefront-web/**` | `GR-REN-00` | `READY` |
| `GR-REN-01A-05` | `GR-REN-01A` | `TASK` | Root current governance/navigation files: `AGENTS.md`, `README`, `SEQUENCE.md`, `GRANDRUE-MIGRATION.md` | `GR-REN-00` | `NOT_STARTED` |
| `GR-REN-01A-06` | `GR-REN-01A` | `TASK` | Section 3.2 governance files under `designs/` only | `GR-REN-00` | `NOT_STARTED` |
| `GR-REN-01A-07` | `GR-REN-01A` | `GROUP` | `designs/authorities/**` and all other non-governance design material not otherwise listed in `GR-REN-01A-06` | — | `EXCLUDED` |
| `GR-REN-01A-08` | `GR-REN-01A` | `GROUP` | `designs/system/**` | — | `EXCLUDED` |
| `GR-REN-01A-09` | `GR-REN-01A` | `GROUP` | `designs/historical/**` | — | `EXCLUDED` |
| `GR-REN-01A-10` | `GR-REN-01A` | `GROUP` | `docs/**` except the maintained migration evidence artifact | — | `EXCLUDED` |
| `GR-REN-01A-11` | `GR-REN-01A` | `GROUP` | `experiments/**` | — | `EXCLUDED` |
| `GR-REN-01A-12` | `GR-REN-01A` | `GATE` | Reconcile lexical-inventory coverage against runtime/governance in-scope surfaces and every explicitly excluded reference-only scope | all required in-scope `GR-REN-01A` leaves | `NOT_STARTED` |

The exact `done_when` condition for executable lexical tasks `GR-REN-01A-01` through `GR-REN-01A-06` is:

> Every path in the node scope has been searched for every required Section 4 naming form; repository locations of every match are recorded; zero-result searches are recorded; and no rename or semantic classification has been performed.

`GR-REN-01A-07` through `GR-REN-01A-11` are explicit reference-only exclusions. They MUST NOT be decomposed or executed as migration targets. Their contents may be consulted by reference when necessary. The maintained migration inventory artifact under `docs/development/` is writable operational evidence but is not a lexical migration target.

`GR-REN-01A-12` may complete only when every runtime/governance in-scope surface is accounted for by executable lexical inventory nodes and every non-runtime/reference-only surface is explicitly accounted for as excluded, with no unallocated migration target.

### 8.9 Dependency gates after inventory

`GR-REN-01F` depends on completion of the full **in-scope runtime/governance** `GR-REN-01A` through `GR-REN-01E` inventory/classification track. Explicitly excluded reference-only scope does not block this gate.

After `GR-REN-01F` is complete:

- safe current-product/current-code mutations may become eligible according to their own dependencies;
- persisted/external identity changes remain blocked until their specific `GR-REN-06*` compatibility decision leaves complete;
- a compatibility decision may preserve an old identity, require an alias/adapter, require a forward migration, or make a proposed rename not applicable.

This means execution may legitimately traverse the hierarchy non-numerically. The graph, not phase numbering, determines safety.

---

## 9. Checkpoint and Commit Semantics

A Git commit cannot contain its own final SHA because the SHA depends on the committed file content. Therefore this ledger distinguishes:

- **task commit** — the commit containing the substantive task change or task evidence;
- **ledger checkpoint commit** — a subsequent ledger-only commit that records the already-created and inspected task commit SHA;
- **ledger-model commit** — a ledger-only change to operational migration structure rather than migration substance.

`last_verified_head` means **the latest inspected substantive migration task commit recorded by the ledger**, not necessarily the repository HEAD and not the SHA of a ledger-only checkpoint/model commit.

On restart, if repository `HEAD` differs from `last_verified_head`, inspect the difference before continuing. A direct descendant consisting only of expected `GRANDRUE-MIGRATION.md` operational/checkpoint/model updates is an expected reconciliation case. Any other difference MUST be reconciled before migration work resumes.

For task commits, use a traceable commit message and include the stable task identifier when practical, for example:

```text
Migration-Task: GR-REN-02B-01
```

Ledger-only task-model changes are exempt from inventing a migration task id and should identify themselves as ledger/model changes instead.

A task is complete only when:

1. its `done_when` condition is satisfied;
2. its intended change or inspection evidence is committed where a commit is applicable;
3. that task commit/evidence is inspected;
4. this ledger records the evidence and inspected task commit SHA where applicable; and
5. the selected next executable leaf is explicit.

---

## 10. Machine-Readable Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
model: HIERARCHICAL_DEPENDENCY_GRAPH
status: IN_PROGRESS
root: GR-REN
active_group: GR-REN-01A
selected_execution_leaf: GR-REN-01A-04
active_path:
  - GR-REN
  - GR-REN-01
  - GR-REN-01A
  - GR-REN-01A-04
excluded_scopes:
  - designs/authorities/**
  - designs/system/**
  - designs/historical/**
  - designs/** except Section 3.2 governance files
  - docs/** except docs/development/grandrue-naming-migration-inventory.md
  - experiments/**
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
last_completed_task: GR-REN-01A-03
last_verified_head: afc06e36d4091977b3d8bfb29b02e2eed6343003
last_task_commit: afc06e36d4091977b3d8bfb29b02e2eed6343003
last_inspected_ledger_model_head: 6a907f85d8134a93af464692067389b6f6295760
mutation_authorised: false
next_action: Execute GR-REN-01A-04 only. Search storefront-web/** for every Section 4 naming form, append matches and zero-results to the inventory artifact, and perform no rename or semantic classification.
```

---

## 11. Task Evidence and Operational Model History

### GR-REN-00 — Baseline and migration contract

- Parent: `GR-REN`
- Kind: `TASK`
- Baseline verified: `c4153441d8340b229a29884967d796280d949a7d`
- Baseline commit message: `docs: stabilize remaining production authority trace anchors`
- Intervening commits before migration start: none
- Governing files inspected: yes
- Ledger-establishment commit: `26d22025fc536b010e29327724b47f0a4b34b12a`
- Commit inspection: `COMPLETE` — only `GRANDRUE-MIGRATION.md` was added; no runtime or accepted-authority file changed
- Task status: `COMPLETE`

### GR-REN-01A-01 — Root operational/build lexical inventory

- Parent: `GR-REN-01A`
- Kind: `TASK`
- State: `COMPLETE`
- Scope: exactly as recorded in Section 8.8
- Depends on: `GR-REN-00` — satisfied
- Rename actions performed: none
- Semantic classification performed: none
- Evidence artifact: `docs/development/grandrue-naming-migration-inventory.md`
- Evidence commit: `a5bb4ed90ee402ce88c46c0707070cb42488f6bb`
- Commit inspection: `COMPLETE` — added only the non-authoritative inventory evidence file; no product/runtime, accepted-authority or existing repository file was changed
- Next selected task: `GR-REN-01A-02`

### GR-REN-01A-02 — Production-source lexical inventory

- Parent: `GR-REN-01A`
- Kind: `TASK`
- State: `COMPLETE`
- Scope: `src/main/**`
- Depends on: `GR-REN-00` — satisfied
- Rename actions performed: none
- Semantic classification performed: none
- Evidence artifact: `docs/development/grandrue-naming-migration-inventory.md`
- Evidence commit: `29a7811c737508ec80214ceff1bb1b28b7762bac`
- Commit inspection: `COMPLETE` — changed only the non-authoritative inventory evidence artifact; no production source, runtime configuration, immutable migration, accepted authority or other repository file was modified
- Next selected task: `GR-REN-01A-03`

### GR-REN-01A-03 — Test-source lexical inventory

- Parent: `GR-REN-01A`
- Kind: `TASK`
- State: `COMPLETE`
- Scope: `src/test/**`
- Depends on: `GR-REN-00` — satisfied
- Rename actions performed: none
- Semantic classification performed: none
- Evidence artifact: `docs/development/grandrue-naming-migration-inventory.md`
- Evidence commit: `afc06e36d4091977b3d8bfb29b02e2eed6343003`
- Commit inspection: `COMPLETE` — changed only the non-authoritative inventory evidence artifact; no test source, test resource, runtime configuration, accepted authority or other repository file was modified
- Next selected task: `GR-REN-01A-04`

### GR-REN-01A-04 — Storefront lexical inventory

- Parent: `GR-REN-01A`
- Kind: `TASK`
- State: `READY`
- Scope: `storefront-web/**`
- Depends on: `GR-REN-00` — satisfied
- Rename actions authorised: none
- Semantic classification authorised: none
- Evidence: not yet created

### Runtime-centred scope refinement

The migration contract was narrowed after `GR-REN-01A-03` so that only files directly required to build, configure, test, deploy, serve or govern current GrandRue are migration targets. Non-governance design material, historical/development evidence and experiments are reference-only exclusions. Completed inventory evidence remains valid and is not rewritten.

### Operational model revision history

| Revision | Commit | Effect |
|---|---|---|
| Initial migration ledger | `26d22025fc536b010e29327724b47f0a4b34b12a` | Established migration contract and checkpoint semantics |
| Bounded phase decomposition | `b46394ba2ce57412481fa0d3436dc52e802a3318` | Split the original broad phase model into smaller named phase families |
| Dependency-safe namespace refinement | `38124236816186368190dccfb23a5bdd221aee9a` | Replaced path/package/import-only namespace checkpoints with atomic dependency-safe waves |
| Hierarchical dependency task model | `84c9637fca39df636799537a910fafa7f8f7cdcf` | Introduced parent/child decomposition, explicit dependency edges, leaf-only execution, scope coverage and evidence rules |
| Accepted-authority corpus exclusion | `6a907f85d8134a93af464692067389b6f6295760` | Removed `designs/authorities/**` from lexical inventory, rename, decomposition and completion scope while retaining stable governance references and read-only consultation where required |

### Task checkpoint history

| Task | Verified task commit | Inspection | Ledger checkpoint |
|---|---|---|---|
| `GR-REN-00` | `26d22025fc536b010e29327724b47f0a4b34b12a` | `COMPLETE` | recorded by subsequent ledger state |
| `GR-REN-01A-01` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` | `COMPLETE` | recorded by subsequent ledger state |
| `GR-REN-01A-02` | `29a7811c737508ec80214ceff1bb1b28b7762bac` | `COMPLETE` | recorded by subsequent ledger state |
| `GR-REN-01A-03` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` | `COMPLETE` | recorded by subsequent ledger state |

---

## 12. Naming Inventory

Inventory evidence is recorded by the active `GR-REN-01A` through `GR-REN-01E` task hierarchy for the in-scope runtime/governance migration surface.

Canonical operational inventory artifact: `docs/development/grandrue-naming-migration-inventory.md`.

Explicitly excluded design, historical, development-evidence and experiment scopes are not part of the naming inventory. Stable references encountered in governance/navigation files may be recorded without traversing or changing the referenced material.

No occurrence is safe to rename merely because its spelling matches a migration search term.

### GR-REN-01A — Lexical search-form and repository-path inventory

State: `OPEN`

Selected execution leaf: `GR-REN-01A-04`

Accepted lexical evidence:

- `GR-REN-01A-01` — `COMPLETE`; evidence commit `a5bb4ed90ee402ce88c46c0707070cb42488f6bb`; detailed matches and zero-results recorded in `docs/development/grandrue-naming-migration-inventory.md`.
- `GR-REN-01A-02` — `COMPLETE`; evidence commit `29a7811c737508ec80214ceff1bb1b28b7762bac`; production namespace, runtime/persistence identifier, immutable-migration, hyphenated-policy and prose occurrences recorded in `docs/development/grandrue-naming-migration-inventory.md` without classification or mutation.
- `GR-REN-01A-03` — `COMPLETE`; evidence commit `afc06e36d4091977b3d8bfb29b02e2eed6343003`; test namespace, Main Street prose, `MAINSTREET_TEST_POSTGRES_*` environment variables and zero-result separator variants recorded without classification or mutation.

When a lexical leaf completes, record at minimum:

```yaml
task: <leaf id>
scope: <exact scope>
searched_forms:
  - mainstreet
  - mainstreet.*
  - Main Street
  - MAIN_STREET
  - MAINSTREET
  - main-street
  - Main_Street
matches: <recorded repository locations>
zero_results: <forms with no result in the bounded scope>
rename_performed: false
classification_performed: false
evidence_commit: <sha if applicable>
inspection: <result>
```

---

## 13. Restart Procedure

At the beginning of every migration session:

1. inspect `AGENTS.md`;
2. inspect this ledger;
3. fetch current `development` HEAD;
4. compare it with `last_verified_head` using Section 9 checkpoint semantics;
5. inspect every intervening non-substantive ledger commit or reconcile any unexpected substantive commit;
6. locate `selected_execution_leaf` and its parent chain;
7. verify the leaf still has a concrete scope and objective `done_when` condition;
8. verify every `depends_on` edge is satisfied and no new blocker/authority conflict invalidates readiness;
9. if the selected leaf is no longer executable, mark/reconcile it appropriately and select another eligible leaf without crossing a dependency; and
10. execute only the selected leaf.

Never infer migration progress from conversation memory alone.

If HEAD and ledger state disagree unexpectedly:

```text
STOP
→ inspect the difference
→ reconcile the ledger
→ recompute executable leaves
→ only then continue
```

If execution reaches an `EXPANSION_REQUIRED` group:

```text
DO NOT execute the group
→ inspect its real repository/inventory scope
→ create bounded child tasks/gates
→ record parent + dependency edges + done_when
→ choose one eligible leaf
```

An `EXCLUDED` group is not expanded or executed. It remains visible only to preserve an explicit scope boundary and audit trail.

---

## 14. Verification Constraints

Until explicitly authorised otherwise:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Structural verification remains required and may include:

- Git diff/commit inspection;
- package/import consistency checks;
- path existence checks;
- duplicate-source detection;
- residual-name searches within the in-scope runtime/governance migration surface;
- stable-identifier/reference checks in in-scope governance/navigation files;
- dependency/readiness validation;
- hierarchy coverage checks including explicit reference-only exclusions;
- migration-ledger updates.

Structural inspection MUST NOT be represented as a passed Maven, integration or runtime verification gate.

The normal full implementation verification gate remains separate and is not implicitly authorised by this migration ledger.
