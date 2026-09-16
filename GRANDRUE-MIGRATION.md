# GrandRue Naming Migration Ledger

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Baseline:** `c4153441d8340b229a29884967d796280d949a7d`  
**Status:** `IN_PROGRESS`  
**Authority class:** non-semantic operational migration ledger

This file is the canonical resumable execution registry for the Main Street → GrandRue naming migration. It does not redefine accepted semantic authority.

---

## 1. Migration Contract

The migration is **runtime-centred**.

A file is a migration target only when it is directly required to **build, configure, test, deploy, serve, or govern the current GrandRue runtime/repository**.

```text
GrandRue
    = current product/runtime/implementation identity

MS-PROT-* / MS-IMP-* / other stable MS-* identifiers
    = durable governance/authority identity
    = preserve unless separately governed

reference-only design/history/evidence
    = consult when needed
    = do not rename merely for branding consistency
```

Never perform a blind global replacement. A naming change must not silently alter semantic meaning, ownership, persisted identity, replay/idempotency behaviour, external compatibility, database migration history, or historical evidence.

### In-scope surfaces

- `src/main/**` — current production/runtime code and resources;
- `src/test/**` — tests and fixtures coupled to the current runtime;
- `storefront-web/**` — current storefront runtime;
- runtime/build/infrastructure files such as `.github/**`, `build_configuration/**`, `tools/**`, `compose.prototype.yml`, `pom.xml`, and directly applicable root operational files;
- root governance/navigation: `AGENTS.md`, `README`, `SEQUENCE.md`, `GRANDRUE-MIGRATION.md`;
- only these governance/navigation files under `designs/**`:
  - `designs/DESIGN-RULES.md`
  - `designs/DOCUMENT-GOVERNANCE.md`
  - `designs/AUTHORITY-INDEX.md`
  - `designs/DEFERRED-DECISION-REGISTER.md`
  - `designs/DESIGN-CORPUS-CONFORMANCE.md`
  - `designs/IMPLEMENTATION-RULES.md`

### Explicit reference-only exclusions

Do **not** inventory, rename, restructure, or rewrite these merely to replace Main Street terminology:

- every file under `designs/**` except the six governance files above;
- therefore including `designs/authorities/**`, `designs/system/**`, `designs/historical/**`, and other non-governance design files;
- `docs/**` except `docs/development/grandrue-naming-migration-inventory.md`, which is writable migration evidence rather than a branding target;
- `experiments/**`;
- historical implementation evidence, old conformance records, and handoffs.

Excluded material may be consulted by reference when a semantic, ownership, historical, or compatibility question requires it. Consultation never makes the file a migration target.

### Immutable and protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ identifiers and accepted contract versions;
- applied Flyway migration files.

If current persisted database state must change, use a forward migration after compatibility requirements are established. Never rewrite an applied migration.

---

## 2. Execution Constraints

Before work, follow the current repository governance listed above and `AGENTS.md`.

- work on `development`;
- do not create a branch unless explicitly authorised;
- repository manipulation is permitted;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- make the smallest conforming change;
- inspect before renaming;
- do not combine naming migration with semantic redesign or unrelated cleanup;
- stop an affected path when semantics or compatibility are materially unresolved.

No mutation may begin until `GR-REN-01F` completes.

---

## 3. Required Inventory Forms

Within the in-scope runtime/governance surface, inventory at least:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

Also inspect active runtime naming embedded in environment variables, Spring configuration, Maven coordinates, Docker/container identifiers, database names/current values, URLs/domains, serialized values, event/command/contract/provider/entitlement identities, runtime-coupled fixtures, filenames/directories, FQCN strings, reflection/class persistence, Spring scanning, and build/plugin wiring.

For lexical inventory, `mainstreet.*` means namespace-style occurrences beginning `mainstreet.` rather than a literal asterisk-bearing string.

---

## 4. Classification and Compatibility Gate

Every material **in-scope** occurrence must ultimately receive exactly one disposition:

- `RENAME_CURRENT_PRODUCT`
- `RENAME_CODE_NAMESPACE`
- `RENAME_CURRENT_DOCUMENTATION`
- `MIGRATION_REQUIRED_PERSISTED_ID`
- `COMPATIBILITY_ALIAS_REQUIRED`
- `PRESERVE_STABLE_GOVERNANCE_ID`
- `PRESERVE_HISTORICAL_EVIDENCE`
- `PRESERVE_IMMUTABLE_MIGRATION`
- `REVIEW_REQUIRED`

Excluded reference-only material requires no migration classification.

Before changing a runtime value, ask:

> Can this value already exist outside the source tree?

If `YES` or `UNKNOWN`, compatibility must be decided before mutation. This includes event/command/contract/entitlement/provider identities, serialized manifests, database/outbox/audit/idempotency values, API/config identifiers, and reflected/persisted FQCNs.

`REVIEW_REQUIRED` is not rename authority.

---

## 5. Java Namespace Rule

Expected current-code target, subject to `GR-REN-01F` and compatibility review:

```text
package mainstreet.* → package grandrue.*
src/main/java/mainstreet/ → src/main/java/grandrue/
src/test/java/mainstreet/ → src/test/java/grandrue/
```

Namespace mutation uses dependency-safe atomic waves. Do not split filesystem path, package declaration, direct imports, necessary consumers/tests, safe non-persisted FQCNs, source-set wiring, or required Spring/reflection wiring when separation would knowingly leave the repository incoherent.

---

## 6. Task Model

The migration is one hierarchical dependency graph.

```text
parent      = where a node belongs
depends_on = what must be satisfied before execution
evidence    = what proves completion
```

Node kinds:

- `GROUP` — scope container; never executed directly;
- `TASK` — one bounded action/inspection;
- `GATE` — explicit completion/readiness check.

Executable states: `NOT_STARTED`, `READY`, `IN_PROGRESS`, `BLOCKED`, `COMPLETE`, `NOT_APPLICABLE`.

Group states: `EXPANSION_REQUIRED`, `OPEN`, `COMPLETE`, `EXCLUDED`.

A leaf is executable only when its scope is concrete, prerequisites are satisfied, required decisions exist, no blocker remains, and the intended action is authorised.

Every executable leaf must have one outcome, explicit scope, coherent stopping point, objective `done_when`, and bounded evidence. If a task is too large, preserve its identifier as a parent group and create children. Completed evidence is historical and is never rewritten.

The dependency graph, not numeric ordering, determines safe execution.

---

## 7. Canonical Task Registry

### `GR-REN-00` — baseline and contract

- Kind: `TASK`
- State: `COMPLETE`
- Baseline: `c4153441d8340b229a29884967d796280d949a7d`
- Ledger-establishment commit: `26d22025fc536b010e29327724b47f0a4b34b12a`

### `GR-REN-01` — runtime/governance inventory and classification

State: `OPEN`

#### `GR-REN-01A` — lexical/path inventory

State: `OPEN`

| Node | Kind | Scope | State | Evidence |
|---|---|---|---|---|
| `GR-REN-01A-01` | TASK | root runtime/build/operational surfaces recorded in inventory evidence | `COMPLETE` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | TASK | `src/main/**` | `COMPLETE` | `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | TASK | `src/test/**` | `COMPLETE` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | TASK | `storefront-web/**` | `COMPLETE` | `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | TASK | `AGENTS.md`, `README`, `SEQUENCE.md`, `GRANDRUE-MIGRATION.md` | `READY` | — |
| `GR-REN-01A-06` | TASK | six governance files listed in Section 1 | `NOT_STARTED` | — |
| `GR-REN-01A-07` | GROUP | all non-governance `designs/**` | `EXCLUDED` | reference-only |
| `GR-REN-01A-08` | GROUP | `designs/system/**` | `EXCLUDED` | reference-only |
| `GR-REN-01A-09` | GROUP | `designs/historical/**` | `EXCLUDED` | reference-only |
| `GR-REN-01A-10` | GROUP | `docs/**` except migration evidence artifact | `EXCLUDED` | reference-only |
| `GR-REN-01A-11` | GROUP | `experiments/**` | `EXCLUDED` | reference-only |
| `GR-REN-01A-12` | GATE | reconcile runtime/governance coverage + explicit exclusions | `NOT_STARTED` | — |

Lexical task `done_when`:

> Every path in the exact task scope has been searched for every Section 3 form; all match locations and zero-result forms are recorded; no rename or semantic classification is performed.

Canonical evidence artifact: `docs/development/grandrue-naming-migration-inventory.md`.

#### Remaining inventory groups

| Node | Purpose | State |
|---|---|---|
| `GR-REN-01B` | Java namespace/import/FQCN/Spring/reflection inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01C` | build/runtime/config/env/container/database-name inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01D` | persisted/API/serialized/event/command/contract/provider/entitlement inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01E` | current governance/navigation + protected-reference boundary inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01F` | reconcile/classify all in-scope inventory and freeze action map | `NOT_STARTED` |

### Post-inventory groups

These remain `EXPANSION_REQUIRED` until inventory evidence is sufficient to define bounded leaves:

| Group | Purpose |
|---|---|
| `GR-REN-02` | production Java namespace migration |
| `GR-REN-03` | test namespace/runtime-coupled fixture reconciliation |
| `GR-REN-04` | build naming and wiring |
| `GR-REN-05` | runtime/configuration/infrastructure naming |
| `GR-REN-06` | persisted/external identity compatibility and approved migrations/aliases |
| `GR-REN-07` | runtime-visible product wording and current repository governance wording |
| `GR-REN-08` | residual audits |
| `GR-REN-09` | compatibility/semantic falsification using excluded design authority only as read-only evidence when needed |
| `GR-REN-10` | structural consistency verification |
| `GR-REN-11` | final verification and closeout |

`GR-REN-11` does not grant Maven/GitHub Actions permission.

---

## 8. Checkpoint Semantics

A commit cannot contain its own SHA, so distinguish:

- **task commit** — substantive mutation or inspection evidence;
- **ledger checkpoint commit** — later ledger-only commit recording an inspected task SHA;
- **ledger-model commit** — operational task/scope model change.

A task is `COMPLETE` only when its `done_when` is satisfied, evidence is committed where applicable, the evidence commit is inspected, the ledger records the result/SHA, and the next executable leaf is explicit.

Task commits should include `Migration-Task: <id>` where practical.

On restart, compare `development` HEAD with the checkpoint. Expected descendant ledger-only commits may be reconciled; any unexpected substantive commit must be inspected before continuing.

### Completed task trail

| Task | Verified task commit | Inspection |
|---|---|---|
| `GR-REN-00` | `26d22025fc536b010e29327724b47f0a4b34b12a` | `COMPLETE` |
| `GR-REN-01A-01` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` | `COMPLETE` |
| `GR-REN-01A-02` | `29a7811c737508ec80214ceff1bb1b28b7762bac` | `COMPLETE` |
| `GR-REN-01A-03` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` | `COMPLETE` |
| `GR-REN-01A-04` | `9a0f69e80561a400b963582195d5c75437cc13bb` | `COMPLETE` |

### Operational model history

| Commit | Effect |
|---|---|
| `b46394ba2ce57412481fa0d3436dc52e802a3318` | bounded phase decomposition |
| `38124236816186368190dccfb23a5bdd221aee9a` | dependency-safe namespace waves |
| `84c9637fca39df636799537a910fafa7f8f7cdcf` | hierarchical dependency task model |
| `6a907f85d8134a93af464692067389b6f6295760` | accepted-authority corpus exclusion |
| `2dd6aedbb5418e3ac390873c4000f97f8e5fbc14` | runtime-centred scope; non-governance designs/docs/experiments reference-only |
| `f711bda24417140b3941b2a3412e494492f15518` | checkpointed runtime-centred scope model |

---

## 9. Machine-Readable Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
model: HIERARCHICAL_DEPENDENCY_GRAPH
scope: RUNTIME_AND_GOVERNANCE_ONLY
status: IN_PROGRESS
active_group: GR-REN-01A
selected_execution_leaf: GR-REN-01A-05
last_completed_task: GR-REN-01A-04
last_verified_head: 9a0f69e80561a400b963582195d5c75437cc13bb
last_task_commit: 9a0f69e80561a400b963582195d5c75437cc13bb
last_inspected_ledger_model_head: 2dd6aedbb5418e3ac390873c4000f97f8e5fbc14
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
mutation_authorised: false
next_action: Execute GR-REN-01A-05 only. Inventory the four root governance/navigation files for every required naming form; do not rename or classify.
```

---

## 10. Restart and Verification

At every restart:

1. inspect `AGENTS.md` and this ledger;
2. fetch current `development` HEAD;
3. reconcile HEAD against `last_verified_head` and expected ledger-only descendants;
4. verify the selected leaf scope/dependencies/readiness;
5. execute only that leaf.

When execution reaches `EXPANSION_REQUIRED`, inspect real runtime/inventory evidence, create bounded child tasks, and select one eligible leaf. Never execute a group directly.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Permitted structural verification includes Git diff/commit inspection, package/import/path consistency, duplicate-source checks, residual-name searches within the in-scope runtime/governance surface, protected-reference checks, dependency/readiness validation, exclusion coverage, and ledger updates.

Structural inspection must never be represented as passed Maven/integration/runtime verification.
