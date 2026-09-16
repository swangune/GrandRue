# GrandRue Naming Migration Ledger

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Baseline:** `c4153441d8340b229a29884967d796280d949a7d`  
**Status:** `IN_PROGRESS`  
**Authority class:** non-semantic operational migration ledger

This is the canonical resumable execution registry for the Main Street → GrandRue naming migration. It does not redefine accepted semantic authority.

---

## 1. Migration Contract

This is a **minimum executable migration plus active repository controls and canonical governance**.

A file/path is a migration target only when at least one condition is true:

1. leaving its legacy naming unchanged would break or invalidate the current GrandRue build, test, package, start, configure or serve path;
2. it is an active repository-control/navigation file: `AGENTS.md`, `SEQUENCE.md`, or this ledger; or
3. it is one of the seven canonical current governance files in the root of `designs/`.

A textual `Main Street`, `mainstreet`, or `MS-*` match alone does not make a file a migration target.

### Essential executable surfaces

- `src/main/**`
- `src/test/**`
- `pom.xml`
- `.github/workflows/maven-tests.yml` — active CI test wiring; legacy PostgreSQL test identifiers must remain coherent with tests
- `.github/workflows/storefront-web-tests.yml` — active storefront CI test/build wiring; currently no legacy naming mutation required
- `compose.prototype.yml` — direct executable input to `PrototypeLocalRuntimeConfigurationTest`
- `storefront-web/**` only where needed to build, test or run the storefront
- any additional file only after concrete dependency evidence proves it essential

### Standing non-runtime exceptions

- `AGENTS.md`
- `SEQUENCE.md`
- `GRANDRUE-MIGRATION.md`
- `designs/DESIGN-RULES.md`
- `designs/DOCUMENT-GOVERNANCE.md`
- `designs/AUTHORITY-INDEX.md`
- `designs/CANONICAL-SEMANTIC-LEXICON.md`
- `designs/DEFERRED-DECISION-REGISTER.md`
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
- `designs/IMPLEMENTATION-RULES.md`

### Reference-only by default

These are not naming-migration targets unless later dependency evidence changes their status:

- `designs/authorities/**`
- `designs/system/**`
- `designs/historical/**`
- `designs/watch-list.md`
- other non-canonical `designs/**`
- `docs/**` except `docs/development/grandrue-naming-migration-inventory.md`
- `experiments/**`
- `README*`
- `build_configuration/**`
- `tools/**`
- `lifecycle.md`
- `operational-rules.md`
- `workflow-tree.md`
- historical implementation evidence and handoffs

Some reference-only files are read by conformance tests. They remain reference-only for **naming migration** where the executable assertion does not depend on the legacy product/package wording. Do not remove them or alter unrelated test sentinels.

### Protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ identifiers and accepted contract versions;
- applied Flyway migration files;
- persisted/external identifiers before compatibility is decided.

If persisted database state must change, use a forward migration after compatibility requirements are established. Never rewrite an applied migration.

---

## 2. Execution Constraints

- work on `development`;
- do not create a branch unless explicitly authorised;
- repository manipulation is permitted;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- make the smallest conforming change;
- inspect before renaming;
- do not widen scope merely for naming consistency;
- do not combine naming migration with semantic redesign or unrelated cleanup;
- stop an affected path when semantics or compatibility are materially unresolved.

No runtime/product-name mutation may begin until `GR-REN-01F` completes.

---

## 3. Required Naming Forms

Within the minimal in-scope surface inspect, as applicable:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

Also inspect active environment variables, Spring/package scanning, Maven/npm coordinates, database/runtime configuration, URLs/domains, serialized values, event/command/contract/provider/entitlement identities, FQCN strings and reflection/class-persistence uses only where they occur on the minimal executable/control/governance surface.

---

## 4. Classification and Compatibility Gate

Every material in-scope occurrence must ultimately receive one disposition:

- `RENAME_CURRENT_PRODUCT`
- `RENAME_CODE_NAMESPACE`
- `RENAME_CURRENT_DOCUMENTATION`
- `MIGRATION_REQUIRED_PERSISTED_ID`
- `COMPATIBILITY_ALIAS_REQUIRED`
- `PRESERVE_STABLE_GOVERNANCE_ID`
- `PRESERVE_HISTORICAL_EVIDENCE`
- `PRESERVE_IMMUTABLE_MIGRATION`
- `REVIEW_REQUIRED`

Reference-only material needs no migration classification.

Before changing a runtime value ask:

> Can this value already exist outside the source tree?

If `YES` or `UNKNOWN`, compatibility must be decided before mutation. `REVIEW_REQUIRED` is not rename authority.

---

## 5. Java Namespace Rule

Expected current-code target, subject to `GR-REN-01F` and compatibility review:

```text
package mainstreet.* → package grandrue.*
src/main/java/mainstreet/ → src/main/java/grandrue/
src/test/java/mainstreet/ → src/test/java/grandrue/
```

Ordinary path/package/import changes are mechanical and do **not** require per-file inventory. Inventory the non-mechanical consumers: FQCN strings, reflection, class persistence, Spring scanning/configuration and compatibility-sensitive identities.

Namespace mutation must use coherent dependency-safe waves; do not knowingly split filesystem path, package declaration, imports, necessary tests or required runtime wiring into an uncompilable state.

---

## 6. Task Model

- `GROUP` — scope container; never directly executed;
- `TASK` — one bounded action/inspection;
- `GATE` — explicit completion/readiness check.

Executable states: `NOT_STARTED`, `READY`, `IN_PROGRESS`, `BLOCKED`, `COMPLETE`, `NOT_APPLICABLE`.  
Group states: `EXPANSION_REQUIRED`, `OPEN`, `COMPLETE`, `EXCLUDED`.

Every executable leaf must have one bounded outcome and objective `done_when`. Completed historical evidence is never rewritten.

Cost-control rule:

> Do not enumerate or migrate reference-only material, and do not enumerate thousands of mechanically equivalent namespace imports where a bounded exception scan can establish the risky boundary.

---

## 7. Canonical Task Registry

### `GR-REN-00` — baseline and contract

`COMPLETE` — establishment evidence `26d22025fc536b010e29327724b47f0a4b34b12a`.

### `GR-REN-01A` — minimal surface discovery

State: `COMPLETE`

| Node | Kind | Scope | State | Evidence |
|---|---|---|---|---|
| `GR-REN-01A-01` | TASK | root operational/build discovery | `COMPLETE` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | TASK | `src/main/**` lexical/path inventory | `COMPLETE` | `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | TASK | `src/test/**` lexical/path inventory | `COMPLETE` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | TASK | `storefront-web/**` lexical/path inventory | `COMPLETE` | `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | TASK | root controls/navigation discovery | `COMPLETE` | `25b1ba128d8ea47cf864c41e531de7482c1ba93c` |
| `GR-REN-01A-06` | GATE | exact executable-path essentiality | `COMPLETE` | `15b98ef041f35a326fd0f6a7661dca0d8b86a0e1`; source verification below |
| `GR-REN-01A-07` | TASK | seven canonical root `designs/` governance files | `COMPLETE` | `219ac801ef2eb09a8f15d9e99c6403ebf34c39b7` |
| `GR-REN-01A-08` | GROUP | non-governance `designs/**` + watch list | `EXCLUDED` | reference-only |
| `GR-REN-01A-09` | GROUP | root docs/navigation except active controls/ledger | `EXCLUDED` | reference-only for naming migration |
| `GR-REN-01A-10` | GROUP | `docs/**` except migration evidence | `EXCLUDED` | reference-only |
| `GR-REN-01A-11` | GROUP | `experiments/**` | `EXCLUDED` | reference-only |
| `GR-REN-01A-12` | GROUP | unproven tools/scripts/infrastructure | `EXCLUDED` | reference-only unless dependency is proven |
| `GR-REN-01A-13` | GATE | reconcile minimal runtime/control/governance coverage + exclusions | `COMPLETE` | reconciled evidence set above |

#### `GR-REN-01A-06` verified essentiality result

`ESSENTIAL` for naming coherence:

- `pom.xml`;
- `.github/workflows/maven-tests.yml` — supplies `MAINSTREET_TEST_POSTGRES_*` consumed by integration tests;
- `compose.prototype.yml` — read directly by `PrototypeLocalRuntimeConfigurationTest`, including legacy DB/volume assertions;
- already in-scope source/test/storefront executable surfaces.

`ESSENTIAL TEST/BUILD WIRING, CURRENTLY NO LEGACY NAMING ACTION`:

- `.github/workflows/storefront-web-tests.yml`;
- storefront build/configuration files without legacy naming occurrences.

`REFERENCE_ONLY FOR NAMING MIGRATION`:

- `build_configuration/package_boundary.md`, `README`, `workflow-tree.md`, `lifecycle.md`, `operational-rules.md`, `.gitignore`, `tools/DesignCorpusCheck.java`.

Direct verification showed these reference-only documents may be read by conformance tests, but their legacy naming is not part of the asserted sentinel contract. Preserve the required sentinel text and file presence.

#### Evidence reconciliation note

Historical task labels collided during concurrent execution:

- `1aab28d5d8b42746eacad2bf694d762e3d584023` used `GR-REN-01A-06` under a superseded model for the old six-file governance inventory;
- `15b98ef041f35a326fd0f6a7661dca0d8b86a0e1` then used `GR-REN-01A-06` for the current essentiality gate;
- `6e23bd1b022f3c8beb50b96cb9df123ff8dc484f` recorded `GR-REN-01A-06E` with an overly narrow local-only interpretation that treated CI workflows and `compose.prototype.yml` as reference-only.

The `GR-REN-01A-06E` interpretation is **superseded for scope classification**. Direct inspection of `PrototypeLocalRuntimeConfigurationTest`, the Maven workflow and integration-test environment consumers verifies the essentiality result recorded above. Historical evidence remains untouched.

Canonical discovery evidence remains `docs/development/grandrue-naming-migration-inventory.md`.

### `GR-REN-01B` — Java namespace exceptional-consumer inventory

State: `OPEN`

The pervasive ordinary `mainstreet.*` package/import population is already established by `GR-REN-01A-02/03`; do not enumerate it again.

| Node | Kind | Scope | State | Done when |
|---|---|---|---|---|
| `GR-REN-01B-01` | TASK | production entrypoint/package-root/Spring scan assumptions | `READY` | application entrypoint and any explicit component/entity/repository scan roots are identified |
| `GR-REN-01B-02` | TASK | `src/main/**` string/reflection/FQCN/class-persistence namespace consumers | `NOT_STARTED` | every non-mechanical production namespace consumer is recorded or zero-result proven |
| `GR-REN-01B-03` | TASK | `src/test/**` string/reflection/FQCN/package-sensitive test consumers | `NOT_STARTED` | every non-mechanical test namespace consumer is recorded or zero-result proven |
| `GR-REN-01B-04` | GATE | freeze dependency-safe Java namespace migration boundary | `NOT_STARTED` | mechanical wave plus exceptional consumers are explicit |

### Remaining inventory/classification groups

| Group | Purpose | State |
|---|---|---|
| `GR-REN-01C` | exact build/runtime/config/env/container/database-name inventory on proven executable paths | `EXPANSION_REQUIRED` |
| `GR-REN-01D` | persisted/API/serialized/event/command/contract/provider/entitlement identity inventory reachable from executable paths | `EXPANSION_REQUIRED` |
| `GR-REN-01E` | `AGENTS.md`, `SEQUENCE.md`, seven canonical governance files + protected-reference boundary classification | `EXPANSION_REQUIRED` |
| `GR-REN-01F` | reconcile/classify minimal inventory and freeze action map | `NOT_STARTED` |

### Post-inventory groups

| Group | Purpose |
|---|---|
| `GR-REN-02` | production Java namespace migration |
| `GR-REN-03` | test namespace/runtime-coupled fixture reconciliation |
| `GR-REN-04` | essential build/CI naming and wiring |
| `GR-REN-05` | essential runtime/configuration naming |
| `GR-REN-06` | approved persisted/external compatibility migrations/aliases |
| `GR-REN-07` | runtime-visible wording + active controls + seven canonical governance files |
| `GR-REN-08` | minimal-surface residual audits |
| `GR-REN-09` | compatibility/semantic falsification using excluded authority only as read-only evidence when needed |
| `GR-REN-10` | structural consistency verification |
| `GR-REN-11` | final authorised verification and closeout |

`GR-REN-11` does not grant Maven/GitHub Actions permission.

---

## 8. Checkpoint Semantics

A commit cannot contain its own SHA:

- **task commit** — inspection/mutation evidence;
- **ledger checkpoint commit** — later ledger-only recording of inspected task evidence;
- **ledger-model commit** — operational task/scope-model change.

On restart, reconcile `development` HEAD with `last_verified_head`; inspect any unexpected substantive descendant before continuing.

### Completed evidence trail

| Task | Verified evidence |
|---|---|
| `GR-REN-00` | `26d22025fc536b010e29327724b47f0a4b34b12a` |
| `GR-REN-01A-01` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | `25b1ba128d8ea47cf864c41e531de7482c1ba93c` |
| `GR-REN-01A-06` | `15b98ef041f35a326fd0f6a7661dca0d8b86a0e1`, reconciled by direct source inspection |
| `GR-REN-01A-07` | `219ac801ef2eb09a8f15d9e99c6403ebf34c39b7` |
| `GR-REN-01A-13` | reconciled from complete `GR-REN-01A` evidence + exclusions |

---

## 9. Machine-Readable Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
model: HIERARCHICAL_DEPENDENCY_GRAPH
scope: MINIMUM_EXECUTABLE_PLUS_ACTIVE_CONTROLS_AND_CANONICAL_GOVERNANCE
status: IN_PROGRESS
active_group: GR-REN-01B
selected_execution_leaf: GR-REN-01B-01
last_completed_task: GR-REN-01A-13
last_verified_head: 219ac801ef2eb09a8f15d9e99c6403ebf34c39b7
last_task_commit: 219ac801ef2eb09a8f15d9e99c6403ebf34c39b7
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
mutation_authorised: false
next_action: Execute GR-REN-01B-01 only. Identify the production application entrypoint, package root and any explicit Spring component/entity/repository scan roots. Do not enumerate ordinary imports and do not rename anything.
```

---

## 10. Restart and Verification

1. inspect `AGENTS.md` and this ledger;
2. inspect `SEQUENCE.md` only when programme/design sequencing materially applies;
3. fetch current `development` HEAD;
4. reconcile against `last_verified_head` and expected ledger-only descendants;
5. execute only the selected bounded leaf.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Permitted verification is structural/read-only inspection, Git diff/commit inspection, package/import/path consistency, duplicate-source checks, residual-name searches within the minimal surface, protected-reference checks, dependency/readiness validation, exclusion coverage and ledger updates.

Structural inspection must never be represented as passed Maven/integration/runtime verification.
