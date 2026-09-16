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

A file/path is a migration target only when:

1. leaving its legacy naming unchanged would break or invalidate the current GrandRue build, test, package, start, configure or serve path;
2. it is an active repository-control/navigation file: `AGENTS.md`, `SEQUENCE.md`, or this ledger; or
3. it is one of the seven canonical current governance files in the root of `designs/`.

A textual `Main Street`, `mainstreet`, or `MS-*` match alone does not make a file a migration target.

### Essential executable surfaces

- `src/main/**`
- `src/test/**`
- `pom.xml`
- `.github/workflows/maven-tests.yml`
- `.github/workflows/storefront-web-tests.yml` — essential wiring; currently no legacy naming action
- `compose.prototype.yml`
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

Unless later dependency evidence changes their status:

- non-canonical `designs/**`, including `authorities/**`, `system/**`, `historical/**`, and `watch-list.md`;
- `docs/**` except the migration inventory evidence;
- `experiments/**`;
- `README*`, `build_configuration/**`, `tools/**`, `lifecycle.md`, `operational-rules.md`, `workflow-tree.md`;
- historical implementation evidence and handoffs.

Some reference-only files are read by conformance tests. They remain reference-only for **naming migration** where the asserted executable sentinel does not depend on legacy naming. Preserve required file presence and unrelated sentinel text.

### Protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ identifiers and accepted contract versions;
- applied Flyway migration files;
- persisted/external identifiers before compatibility is decided.

Never rewrite an applied Flyway migration. Use a forward migration if persisted database state must change.

---

## 2. Execution Constraints

- work on `development`;
- do not create a branch unless explicitly authorised;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- make the smallest conforming change;
- inspect before renaming;
- do not widen scope merely for naming consistency;
- do not combine migration with semantic redesign/unrelated cleanup;
- stop an affected path when semantics or compatibility remain materially unresolved.

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

Also inspect active environment variables, Spring/package scanning, Maven/npm coordinates, database/runtime configuration, URLs/domains, serialized values, event/command/contract/provider/entitlement identities, FQCN strings and reflection/class-persistence uses only on the minimal executable/control/governance surface.

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

Reference-only material needs no migration classification. Before changing a runtime value ask whether it can already exist outside the source tree. If `YES` or `UNKNOWN`, compatibility must be decided before mutation. `REVIEW_REQUIRED` is not rename authority.

---

## 5. Java Namespace Rule

Expected current-code target, subject to `GR-REN-01F` and compatibility review:

```text
package mainstreet.* → package grandrue.*
src/main/java/mainstreet/ → src/main/java/grandrue/
src/test/java/mainstreet/ → src/test/java/grandrue/
```

Ordinary path/package/import changes are mechanical and do not require per-file inventory. Inventory non-mechanical FQCN/reflection/persistence/scanning consumers. Namespace mutation must use coherent dependency-safe waves.

---

## 6. Task Model

- `GROUP` — scope container; never directly executed;
- `TASK` — one bounded action/inspection;
- `GATE` — explicit completion/readiness check.

Executable states: `NOT_STARTED`, `READY`, `IN_PROGRESS`, `BLOCKED`, `COMPLETE`, `NOT_APPLICABLE`.  
Group states: `EXPANSION_REQUIRED`, `OPEN`, `COMPLETE`, `EXCLUDED`.

Cost-control rule:

> Do not enumerate or migrate reference-only material, and do not enumerate mechanically equivalent imports where a bounded exception scan establishes the risky boundary.

---

## 7. Canonical Task Registry

### `GR-REN-00` — baseline and contract

`COMPLETE` — establishment evidence `26d22025fc536b010e29327724b47f0a4b34b12a`.

### `GR-REN-01A` — minimal surface discovery

State: `COMPLETE`

| Node | State | Evidence / scope |
|---|---|---|
| `GR-REN-01A-01` | `COMPLETE` | root operational/build discovery — `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | `COMPLETE` | `src/main/**` — `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | `COMPLETE` | `src/test/**` — `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | `COMPLETE` | `storefront-web/**` — `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | `COMPLETE` | root controls/navigation — `25b1ba128d8ea47cf864c41e531de7482c1ba93c` |
| `GR-REN-01A-06` | `COMPLETE` | executable-path essentiality — `15b98ef041f35a326fd0f6a7661dca0d8b86a0e1`, reconciled by direct source inspection |
| `GR-REN-01A-07` | `COMPLETE` | seven canonical root governance files — `219ac801ef2eb09a8f15d9e99c6403ebf34c39b7` |
| `GR-REN-01A-08..12` | `EXCLUDED` | reference-only surfaces established by Section 1 |
| `GR-REN-01A-13` | `COMPLETE` | minimal-surface reconciliation |

Essential naming-coherence additions established by `01A`: `pom.xml`, `.github/workflows/maven-tests.yml`, `compose.prototype.yml`, source/test/storefront surfaces. The storefront workflow is essential wiring but has no legacy naming occurrence.

Historical collision note: `GR-REN-01A-06E` (`6e23bd1...`) used an overly narrow local-only interpretation and is superseded for scope classification. Historical evidence remains untouched.

### `GR-REN-01B` — Java namespace exceptional-consumer inventory

State: `COMPLETE`

| Node | State | Result |
|---|---|---|
| `GR-REN-01B-01` | `COMPLETE` | sole `@SpringBootApplication` is `src/main/java/mainstreet/GrandRueApplication.java`; implicit scan root `mainstreet`; no explicit scan override — `a7fc9d4a022e62f2023673f7e4c61fb201a1e9df` |
| `GR-REN-01B-02` | `COMPLETE_WITH_ADDENDUM` | production non-mechanical consumers identified — `c3fc871bbe2d2e32ee3e327bf07fcb6d33880e48`, addendum `a52661553c15bfd38e4d9bb4fb911ba73f90f84c` |
| `GR-REN-01B-03` | `COMPLETE` | package/FQCN/path-sensitive test families mapped — `a52661553c15bfd38e4d9bb4fb911ba73f90f84c` |
| `GR-REN-01B-04` | `COMPLETE` | Java namespace boundary frozen — `ec4602c85075f2470c2363fc3996578eb0f2b439` |

Frozen namespace wave after `01F` authorisation:

1. `src/main/java/mainstreet/**` → `src/main/java/grandrue/**` plus production packages/imports;
2. move `GrandRueApplication` with root so Spring scanning follows `grandrue`;
3. `src/test/java/mainstreet/**` → `src/test/java/grandrue/**` plus test packages/imports and structural namespace assertions;
4. include any directly coupled build references whose separation would break compilation/test discovery/scanning.

Do **not** blindly rename:

- `OpportunityEnquiryBindingCodec` v1 AES-GCM AAD `mainstreet/enquiry/opportunity-binding/v1/`; existing tokens require compatibility-preserving treatment;
- persisted/external identifiers and stable `MS-*` references;
- historical/reference-only material.

`NotificationDeliveryCoordinator` stores `providerFailure.getClass().getName()` in delivery-attempt evidence; future class-name output changes after package migration and requires `01D/01F` classification.

### `GR-REN-01C` — executable build/runtime/config naming inventory

State: `COMPLETE`

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-01C-01` | TASK | `COMPLETE` | backend Maven + CI/test PostgreSQL naming |
| `GR-REN-01C-02` | TASK | `COMPLETE` | prototype runtime naming: compose + application properties + coupled test |
| `GR-REN-01C-03` | TASK | `COMPLETE` | storefront package/runtime environment naming |
| `GR-REN-01C-04` | GATE | `COMPLETE` | executable build/runtime rename boundary frozen below |

#### `GR-REN-01C-01` result

**Maven identity**

- `pom.xml` declares project coordinates `mainstreet:mainstreet:0.1.0-SNAPSHOT`.
- The `postgres-it` Maven profile controls Failsafe integration-test execution but does not itself define PostgreSQL environment variables.
- Maven coordinates are therefore a build identity to classify later, separate from the PostgreSQL test-environment contract.

**PostgreSQL CI producer**

`.github/workflows/maven-tests.yml` supplies one coherent test database contract:

```text
POSTGRES_DB=mainstreet_test
POSTGRES_USER=mainstreet
POSTGRES_PASSWORD=mainstreet
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:5432/mainstreet_test
MAINSTREET_TEST_POSTGRES_USER=mainstreet
MAINSTREET_TEST_POSTGRES_PASSWORD=mainstreet
```

Its health check also uses `mainstreet` / `mainstreet_test`, and the workflow executes `mvn --batch-mode clean verify -Ppostgres-it`.

**Test consumers**

- PostgreSQL integration tests under `src/test/**` read the exact names `MAINSTREET_TEST_POSTGRES_URL`, `MAINSTREET_TEST_POSTGRES_USER`, and `MAINSTREET_TEST_POSTGRES_PASSWORD` through `env(...)` / `requiredEnvironment(...)` helpers.
- Prior bounded inventory established a broad integration-test consumer family (79 URL-name consumers); exact enumeration is unnecessary because all consumers use the same three-name contract.
- Representative consumers include persistence, prototype, enquiry and surface integration tests, including `FlywayMigrationChainIT`, `PostgresPersistenceFoundationIT`, `PrototypeApplicationStartupIT`, `PrototypeJooqOrderingUseCaseIT`, `MerchantEnquiryQueryT3IT`, and `EnquiryMerchantExposureM1IT`.

**Coupling consequence**

If these test environment names are renamed, the workflow producer and all executable test lookup strings must change atomically. Database name/user/password/JDBC URL/health-check values must remain mutually coherent. Historical docs/README occurrences are reference-only and do not join this migration wave.

#### `GR-REN-01C-02` result

The prototype runtime has one tightly coupled naming contract across Compose, Spring prototype configuration, and its executable contract test.

**Compose producer** — `compose.prototype.yml`:

```text
POSTGRES_DB=mainstreet
POSTGRES_USER=mainstreet
POSTGRES_PASSWORD=mainstreet
healthcheck: pg_isready -U mainstreet -d mainstreet
volume: mainstreet-prototype-postgres-v18
```

**Spring prototype configuration** — `src/main/resources/application-prototype.properties`:

```text
MAINSTREET_PROTOTYPE_POSTGRES_URL
    default = jdbc:postgresql://localhost:55432/mainstreet
MAINSTREET_PROTOTYPE_POSTGRES_USER
    default = mainstreet
MAINSTREET_PROTOTYPE_POSTGRES_PASSWORD
    default = mainstreet
```

The file therefore has two naming layers: the externally overridable environment-variable contract and its current local defaults.

**Executable coupling test** — `PrototypeLocalRuntimeConfigurationTest`:

- directly reads `compose.prototype.yml` and `application-prototype.properties`;
- asserts the prototype JDBC default contains `jdbc:postgresql://localhost:55432/mainstreet`;
- asserts the Compose volume is exactly `mainstreet-prototype-postgres-v18:/var/lib/postgresql`;
- independently protects the PostgreSQL 18 parent mount and host-port contract.

**Coupling consequence**

The future prototype rename must update atomically:

1. Compose database/user/password/health-check values;
2. Compose versioned volume name;
3. `MAINSTREET_PROTOTYPE_POSTGRES_{URL,USER,PASSWORD}` names if `01F` classifies them for rename;
4. Spring defaults embedded in those property expressions;
5. exact naming assertions in `PrototypeLocalRuntimeConfigurationTest`.

The PostgreSQL host port `55432`, image `postgres:18-alpine`, `/var/lib/postgresql` mount path, and profile semantics are not product naming and must remain unchanged unless separately required.

#### `GR-REN-01C-03` result

**npm package identity**

- `storefront-web/package.json` declares `name: "mainstreet-storefront-web"`.
- `storefront-web/package-lock.json` repeats `mainstreet-storefront-web` at the lockfile root and root-package metadata.
- These are one repository/build identity and must remain coherent; if classified for rename they change together.

**runtime backend origin**

- `storefront-web/src/lib/grandrue-api.ts` reads `process.env.MAINSTREET_BACKEND_URL` and falls back to `http://localhost:8080`.
- No executable repository producer for `MAINSTREET_BACKEND_URL` was found. The only other indexed occurrence is `docs/development/prototype-runbook.md`, which is reference-only under the migration contract.
- `MAINSTREET_BACKEND_URL` can therefore be supplied outside the source tree by a deployment/runtime environment. It is compatibility-sensitive external configuration and must not be blindly renamed.
- `01F` must decide whether to preserve the old name, support a compatibility alias/fallback while introducing `GRANDRUE_BACKEND_URL`, or otherwise define an explicit cutover. The localhost fallback is not product naming and requires no change.

#### `GR-REN-01C-04` frozen executable naming boundary

Repository-local naming that can be migrated only as coherent producer/consumer units after `01F` classification:

1. Maven build identity `mainstreet:mainstreet` in `pom.xml`.
2. PostgreSQL integration-test wiring: workflow `POSTGRES_*` values, `MAINSTREET_TEST_POSTGRES_*` producer names, JDBC/database/user values, health check, and all executable test consumers of those environment-variable names.
3. Prototype local runtime: Compose database/user/password/health-check/volume, Spring prototype defaults, and exact contract-test assertions.
4. Storefront npm identity `mainstreet-storefront-web` in `package.json` and root lockfile metadata.

Compatibility-sensitive environment contracts that may already exist outside the source tree and therefore require explicit `01F` disposition before rename:

- `MAINSTREET_TEST_POSTGRES_{URL,USER,PASSWORD}` — repository CI produces them, but local/external test execution can also supply them;
- `MAINSTREET_PROTOTYPE_POSTGRES_{URL,USER,PASSWORD}` — explicitly externally overridable by Spring property expressions;
- `MAINSTREET_BACKEND_URL` — external storefront runtime/deployment configuration with no repository producer.

No naming action is required for `.github/workflows/storefront-web-tests.yml`, `http://localhost:8080`, ports `5432`/`55432`, PostgreSQL image/version, or `/var/lib/postgresql`; these are executable wiring but not legacy product naming.

No mutation was performed by `GR-REN-01C`.

### `GR-REN-01D` — compatibility-sensitive persisted/external identity inventory

State: `OPEN`

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-01D-01` | TASK | `READY` | known persisted/serialized naming: payment correlation column, delivery failure class-name evidence, enquiry binding AAD |
| `GR-REN-01D-02` | TASK | `NOT_STARTED` | active API/event/command/contract/provider/entitlement identifiers containing legacy product naming |
| `GR-REN-01D-03` | TASK | `NOT_STARTED` | immutable Flyway/protected persistence boundary and live-code consumers |
| `GR-REN-01D-04` | GATE | `NOT_STARTED` | freeze compatibility-sensitive identity boundary |

### Remaining inventory/classification groups

| Group | Purpose | State |
|---|---|---|
| `GR-REN-01E` | active controls + seven canonical governance files + protected-reference boundary classification | `EXPANSION_REQUIRED` |
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

---

## 8. Completed Evidence Trail

| Task | Verified evidence |
|---|---|
| `GR-REN-00` | `26d22025fc536b010e29327724b47f0a4b34b12a` |
| `GR-REN-01A-01` | `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | `25b1ba128d8ea47cf864c41e531de7482c1ba93c` |
| `GR-REN-01A-06` | `15b98ef041f35a326fd0f6a7661dca0d8b86a0e1` + reconciliation |
| `GR-REN-01A-07` | `219ac801ef2eb09a8f15d9e99c6403ebf34c39b7` |
| `GR-REN-01B-01` | `a7fc9d4a022e62f2023673f7e4c61fb201a1e9df` |
| `GR-REN-01B-02` | `c3fc871bbe2d2e32ee3e327bf07fcb6d33880e48` + addendum `a52661553c15bfd38e4d9bb4fb911ba73f90f84c` |
| `GR-REN-01B-03` | `a52661553c15bfd38e4d9bb4fb911ba73f90f84c` |
| `GR-REN-01B-04` | `ec4602c85075f2470c2363fc3996578eb0f2b439` |
| `GR-REN-01C-01` | `8f8bd71510e5b68b9a22bd06284c7de269652d2d` |
| `GR-REN-01C-02` | `4f3725ae5686a1de6395559bee33162b9a1b79bf` |
| `GR-REN-01C-03` | `6a83c624ecb2bff17b9cb4e8d30a0388d0cb0058` |
| `GR-REN-01C-04` | evidence recorded in this task commit; checkpoint SHA recorded by the next ledger update |

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
active_group: GR-REN-01D
selected_execution_leaf: GR-REN-01D-01
last_completed_task: GR-REN-01C-04
last_verified_head: 6a83c624ecb2bff17b9cb4e8d30a0388d0cb0058
last_task_commit: 6a83c624ecb2bff17b9cb4e8d30a0388d0cb0058
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
mutation_authorised: false
next_action: Execute GR-REN-01D-01 only. Inspect the known persisted/serialized naming cases: payment correlation column/live code, delivery failure class-name evidence, and Opportunity enquiry binding v1 AAD. Do not rename anything.
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
