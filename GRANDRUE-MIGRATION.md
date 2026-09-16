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

The migration is a **minimum executable migration with active repository-control migration**.

A file or path is a migration target only when at least one of these conditions is true:

1. it is directly required to **build, test, start, configure, or serve the current GrandRue runtime**;
2. it is an active repository-control/navigation file required to govern continuing GrandRue work: `AGENTS.md` or `SEQUENCE.md`; or
3. it is one of the canonical current governance files in the root of `designs/` defined by `designs/DOCUMENT-GOVERNANCE.md`.

Everything else is **reference-only by default**. A file does not enter migration scope merely because it contains `Main Street`, `mainstreet`, an `MS-*` identifier, or other legacy naming.

```text
GrandRue
    = current product/runtime/implementation identity

MS-PROT-* / MS-IMP-* / other stable MS-* identifiers
    = durable governance/authority identity
    = preserve unless separately governed

minimum executable surface
    = migrate only what current build/test/run paths actually require

active repository controls
    = AGENTS.md + SEQUENCE.md
    = migrate because they govern current execution/navigation

reference-only design/history/evidence
    = consult when needed
    = do not migrate merely for naming consistency
```

Never perform a blind global replacement. A naming change must not silently alter semantic meaning, ownership, persisted identity, replay/idempotency behaviour, external compatibility, database migration history, or historical evidence.

### Essentiality gate

Before inventorying or mutating a candidate outside the obvious source/runtime surface and the explicit active-control/governance exceptions, ask:

> Would GrandRue fail to build, execute the authorised tests, start, configure, or serve correctly if this file/path were left unchanged?

- `YES` → candidate may enter the migration action map, subject to compatibility classification.
- `NO` → reference-only.
- `UNKNOWN` → inspect the actual build/test/run dependency before adding it to scope.

A textual naming match is never sufficient evidence of essentiality.

`AGENTS.md`, `SEQUENCE.md`, and the seven canonical root `designs/` governance files do not require runtime-essentiality proof; they are explicit current-control/governance exceptions.

### In-scope surfaces

The in-scope surface is intentionally small:

- `src/main/**` — current production source/resources required by the backend build/runtime;
- `src/test/**` — tests/fixtures required by the authorised test path and coupled to migrated runtime names;
- `storefront-web/**` — current storefront source/runtime only where required to build, test, or run the storefront;
- `pom.xml` and any specific build/configuration/runtime file proven to be consumed by the current build/test/run path;
- current runtime configuration, container/database configuration, scripts, or infrastructure files only when the selected build/test/run path directly consumes them;
- `GRANDRUE-MIGRATION.md` — operational migration ledger;
- `AGENTS.md` — active repository execution/navigation adapter;
- `SEQUENCE.md` — active design/implementation sequence and dependency navigation;
- the canonical current governance set in the root of `designs/`:
  - `designs/DESIGN-RULES.md`
  - `designs/DOCUMENT-GOVERNANCE.md`
  - `designs/AUTHORITY-INDEX.md`
  - `designs/CANONICAL-SEMANTIC-LEXICON.md`
  - `designs/DEFERRED-DECISION-REGISTER.md`
  - `designs/DESIGN-CORPUS-CONFORMANCE.md`
  - `designs/IMPLEMENTATION-RULES.md`

The two active root controls and the seven `designs/` files above are explicit exceptions to the run/test-only rule because they govern current GrandRue repository work.

### Reference-only by default

Do **not** inventory, rename, restructure, or rewrite these merely to replace Main Street terminology:

- `designs/authorities/**`;
- `designs/system/**`;
- `designs/historical/**`;
- `designs/watch-list.md` unless separately reclassified by governance;
- any other non-canonical-governance file under `designs/**`;
- `docs/**` except `docs/development/grandrue-naming-migration-inventory.md`, which is writable migration evidence rather than a branding target;
- `experiments/**`;
- `README*` and other root documentation/navigation except `AGENTS.md`, `SEQUENCE.md`, and `GRANDRUE-MIGRATION.md`;
- `.github/**`, `tools/**`, `build_configuration/**`, scripts, compose files, and other infrastructure merely because they exist — they become migration targets only when an authorised/current build/test/run path actually consumes them;
- historical implementation evidence, old conformance records, and handoffs.

Excluded material may be consulted by reference when a semantic, ownership, historical, compatibility, or execution-dependency question requires it. Consultation never by itself makes the file a migration target.

### Immutable and protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ identifiers and accepted contract versions;
- applied Flyway migration files.

If current persisted database state must change, use a forward migration after compatibility requirements are established. Never rewrite an applied migration.

---

## 2. Execution Constraints

Before work, follow `AGENTS.md`, `SEQUENCE.md` where programme/design sequencing applies, and the current canonical governance set listed in Section 1.

- work on `development`;
- do not create a branch unless explicitly authorised;
- repository manipulation is permitted;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- make the smallest conforming change;
- prove essentiality before widening migration scope beyond the explicit active-control/governance exceptions;
- inspect before renaming;
- do not combine naming migration with semantic redesign or unrelated cleanup;
- stop an affected path when semantics or compatibility are materially unresolved.

No runtime/product-name mutation may begin until `GR-REN-01F` completes.

---

## 3. Required Inventory Forms

Inventory the following forms **only within the minimal in-scope surface established by Section 1**:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

Also inspect active naming embedded in environment variables, Spring configuration, Maven coordinates, Docker/container identifiers, database names/current values, URLs/domains, serialized values, event/command/contract/provider/entitlement identities, runtime-coupled fixtures, filenames/directories, FQCN strings, reflection/class persistence, Spring scanning, and build/plugin wiring **only when those values occur on an essential build/test/run path**.

For lexical inventory, `mainstreet.*` means namespace-style occurrences beginning `mainstreet.` rather than a literal asterisk-bearing string.

A broad repository search may be used as a discovery aid, but matches outside the minimal in-scope surface remain reference-only and require no migration work.

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

Reference-only material requires no migration classification.

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

Do not migrate an otherwise unrelated path merely because it references the old namespace in documentation or historical evidence.

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

Cost control rule:

> Do not create an inventory, classification, mutation, verification, or checkpoint task for a reference-only path unless evidence first proves that the path is required by the current build/test/run dependency. `AGENTS.md`, `SEQUENCE.md`, and the seven-file canonical governance set are the only standing non-runtime exceptions.

---

## 7. Canonical Task Registry

### `GR-REN-00` — baseline and contract

- Kind: `TASK`
- State: `COMPLETE`
- Baseline: `c4153441d8340b229a29884967d796280d949a7d`
- Ledger-establishment commit: `26d22025fc536b010e29327724b47f0a4b34b12a`

### `GR-REN-01` — minimal runtime/control/governance inventory and classification

State: `OPEN`

#### `GR-REN-01A` — lexical/path inventory

State: `OPEN`

| Node | Kind | Scope | State | Evidence |
|---|---|---|---|---|
| `GR-REN-01A-01` | TASK | previously inventoried root runtime/build/operational discovery surface | `COMPLETE` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | TASK | `src/main/**` | `COMPLETE` | `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | TASK | `src/test/**` | `COMPLETE` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | TASK | `storefront-web/**` | `COMPLETE` | `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | TASK | inventory of `AGENTS.md`, `README`, `SEQUENCE.md`, `GRANDRUE-MIGRATION.md` | `COMPLETE` | `25b1ba128d8ea47cf864c41e531de7482c1ba93c` |
| `GR-REN-01A-06` | GATE | prune discovered root/build/infrastructure matches to an exact build/test/run essential-path manifest | `IN_PROGRESS` | evidence recorded below |
| `GR-REN-01A-07` | TASK | seven canonical governance files in root `designs/` listed in Section 1 | `NOT_STARTED` | — |
| `GR-REN-01A-08` | GROUP | non-governance `designs/**` and `designs/watch-list.md` | `EXCLUDED` | reference-only |
| `GR-REN-01A-09` | GROUP | root docs/navigation except `AGENTS.md`, `SEQUENCE.md`, and `GRANDRUE-MIGRATION.md` | `EXCLUDED` | reference-only |
| `GR-REN-01A-10` | GROUP | `docs/**` except migration evidence artifact | `EXCLUDED` | reference-only |
| `GR-REN-01A-11` | GROUP | `experiments/**` | `EXCLUDED` | reference-only |
| `GR-REN-01A-12` | GROUP | unproven `.github/**`, tools, scripts, build/infrastructure/configuration paths | `EXCLUDED` | reference-only unless essentiality is proven |
| `GR-REN-01A-13` | GATE | reconcile minimal runtime/control/governance coverage + explicit exclusions | `NOT_STARTED` | — |

Completed discovery evidence is retained even where the essentiality gate excludes a discovered match from migration. Prior inventory does not authorise mutation.

`GR-REN-01A-06` `done_when`:

> The exact non-source files/paths consumed by the current build/test/run path are identified from real dependency/wiring evidence; each candidate is marked `ESSENTIAL` or `REFERENCE_ONLY`; no naming mutation or semantic classification is performed.

### `GR-REN-01A-06` — essential-path evidence

The discovery candidates from `GR-REN-01A-01` were checked against actual Maven, CI, prototype-runtime and executable-test wiring.

| Candidate | Result | Evidence / consequence |
|---|---|---|
| `pom.xml` | `ESSENTIAL` | Maven build entry point; current `mainstreet` group/artifact coordinates participate directly in the build. |
| `.github/workflows/maven-tests.yml` | `ESSENTIAL` | Active CI test path runs `mvn --batch-mode clean verify -Ppostgres-it` and injects the `MAINSTREET_TEST_POSTGRES_*` variables/database values consumed by PostgreSQL integration tests. |
| `.github/workflows/storefront-web-tests.yml` | `ESSENTIAL` | Active storefront CI path installs, tests and builds `storefront-web`; this file currently contains no legacy naming match, so no naming mutation is implied. |
| `compose.prototype.yml` | `ESSENTIAL` | Current local prototype PostgreSQL configuration and direct executable input to `PrototypeLocalRuntimeConfigurationTest`; its database/user/volume naming must remain coherent with `src/main/resources/application-prototype.properties` and the coupled test. |
| `build_configuration/package_boundary.md` | `REFERENCE_ONLY` | `ImplementationBoundaryConformanceTest` reads it only for `test-scope` and `ConfigurationCompiler`; leaving its legacy product/package prose unchanged does not break build/test/run. |
| `README` | `REFERENCE_ONLY` | `ImplementationBoundaryConformanceTest` checks only Java/PostgreSQL/H2 sentinel statements; legacy naming is not part of that executable assertion. |
| `workflow-tree.md` | `REFERENCE_ONLY` | Conformance test checks `Non-authoritative` and an obsolete-claim absence only; legacy product naming is not execution-critical. |
| `lifecycle.md` | `REFERENCE_ONLY` | Conformance test checks only `Non-authoritative`; legacy product naming is not execution-critical. |
| `operational-rules.md` | `REFERENCE_ONLY` | `AgentInstructionsConformanceTest` checks compatibility-pointer structure/canonical references; its legacy product wording is not part of the executable requirement. |
| `.gitignore` | `REFERENCE_ONLY` | No legacy naming match and no demonstrated build/test/run naming dependency. |
| `tools/DesignCorpusCheck.java` | `REFERENCE_ONLY` | No legacy naming match and no current Maven/CI/build/test/run consumer was found. It remains a callable governance helper, not a naming-migration dependency. |

`src/main/resources/application-prototype.properties` is already within the essential `src/main/**` surface and is not a separate non-source candidate for this gate.

The standing control/governance exceptions (`AGENTS.md`, `SEQUENCE.md`, `GRANDRUE-MIGRATION.md`, and the seven canonical root `designs/` governance files) are unaffected by this essentiality classification.

No naming mutation or semantic classification was performed.

Lexical task `done_when`:

> Every path in the exact task scope has been searched for every Section 3 form; all match locations and zero-result forms are recorded; no rename or semantic classification is performed.

Canonical evidence artifact: `docs/development/grandrue-naming-migration-inventory.md`.

#### Remaining inventory groups

| Node | Purpose | State |
|---|---|---|
| `GR-REN-01B` | Java namespace/import/FQCN/Spring/reflection inventory within essential source/test runtime paths | `EXPANSION_REQUIRED` |
| `GR-REN-01C` | exact build/runtime/config/env/container/database-name inventory for files proven essential by `GR-REN-01A-06` | `EXPANSION_REQUIRED` |
| `GR-REN-01D` | persisted/API/serialized/event/command/contract/provider/entitlement inventory reachable from essential runtime paths | `EXPANSION_REQUIRED` |
| `GR-REN-01E` | `AGENTS.md`, `SEQUENCE.md`, seven canonical governance files + protected-reference boundary inventory | `EXPANSION_REQUIRED` |
| `GR-REN-01F` | reconcile/classify only the minimal in-scope inventory and freeze the action map | `NOT_STARTED` |

### Post-inventory groups

These remain `EXPANSION_REQUIRED` until inventory evidence is sufficient to define bounded leaves:

| Group | Purpose |
|---|---|
| `GR-REN-02` | production Java namespace migration |
| `GR-REN-03` | test namespace/runtime-coupled fixture reconciliation |
| `GR-REN-04` | build naming/wiring required by the selected build/test/run path |
| `GR-REN-05` | runtime/configuration/infrastructure naming required to start/serve GrandRue |
| `GR-REN-06` | persisted/external identity compatibility and approved migrations/aliases encountered by essential runtime paths |
| `GR-REN-07` | runtime-visible product wording + `AGENTS.md` + `SEQUENCE.md` + seven canonical root `designs/` governance files |
| `GR-REN-08` | residual audits limited to the minimal runtime/control/governance surface |
| `GR-REN-09` | compatibility/semantic falsification using excluded design authority only as read-only evidence when needed |
| `GR-REN-10` | structural consistency verification of build/test/run-critical paths and active controls |
| `GR-REN-11` | final authorised verification and closeout |

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
| `GR-REN-01A-05` | `25b1ba128d8ea47cf864c41e531de7482c1ba93c` | `COMPLETE` |

### Operational model history

| Commit | Effect |
|---|---|
| `b46394ba2ce57412481fa0d3436dc52e802a3318` | bounded phase decomposition |
| `38124236816186368190dccfb23a5bdd221aee9a` | dependency-safe namespace waves |
| `84c9637fca39df636799537a910fafa7f8f7cdcf` | hierarchical dependency task model |
| `6a907f85d8134a93af464692067389b6f6295760` | accepted-authority corpus exclusion |
| `2dd6aedbb5418e3ac390873c4000f97f8e5fbc14` | runtime-centred scope; non-governance designs/docs/experiments reference-only |
| `f711bda24417140b3941b2a3412e494492f15518` | checkpointed runtime-centred scope model |
| `9b9843a0df3ef7ccc9da985c558ca037f83fd54e` | compacted the operational ledger without changing migration semantics |
| `af555d420ef6da9d1dd53fdb5f02820c8266b6e1` | checkpointed completed root governance/navigation inventory before scope minimisation |
| `f8a2a42c18926e2af33bdd279755e53115efc96c` | constrained migration to build/test/run essentials plus the seven canonical root `designs/` governance files |
| `d247740c14b92732da5ecd0674f3750f8d00fbe2` | checkpointed the minimum executable migration model before active-control correction |

---

## 9. Machine-Readable Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
model: HIERARCHICAL_DEPENDENCY_GRAPH
scope: MINIMUM_RUN_TEST_PLUS_ACTIVE_CONTROLS_AND_CANONICAL_GOVERNANCE
status: IN_PROGRESS
active_group: GR-REN-01A
selected_execution_leaf: GR-REN-01A-06
last_completed_task: GR-REN-01A-05
last_verified_head: d247740c14b92732da5ecd0674f3750f8d00fbe2
last_task_commit: 25b1ba128d8ea47cf864c41e531de7482c1ba93c
last_inspected_ledger_model_head: d247740c14b92732da5ecd0674f3750f8d00fbe2
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
mutation_authorised: false
next_action: Inspect the committed GR-REN-01A-06 essential-path evidence, checkpoint the task if complete, then advance only to the next eligible leaf.
```

---

## 10. Restart and Verification

At every restart:

1. inspect `AGENTS.md` and this ledger;
2. inspect `SEQUENCE.md` when current design/implementation sequencing or dependency selection matters;
3. fetch current `development` HEAD;
4. reconcile HEAD against `last_verified_head` and expected ledger-only descendants;
5. verify the selected leaf scope/dependencies/readiness;
6. execute only that leaf.

When execution reaches `EXPANSION_REQUIRED`, inspect real runtime/inventory evidence, create bounded child tasks, and select one eligible leaf. Never execute a group directly.

Do not widen scope to make repository terminology globally consistent. If a nonessential file can safely continue to reference the old name or stable `MS-*` authority, leave it unchanged and reference it when needed.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Permitted structural verification includes Git diff/commit inspection, package/import/path consistency, duplicate-source checks, residual-name searches within the minimal runtime/control/governance surface, protected-reference checks, dependency/readiness validation, exclusion coverage, and ledger updates.

Structural inspection must never be represented as passed Maven/integration/runtime verification.