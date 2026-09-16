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

1. leaving legacy naming unchanged would break or invalidate current GrandRue build/test/package/start/configure/serve coherence;
2. it is an active repository-control/navigation file: `AGENTS.md`, `SEQUENCE.md`, or this ledger; or
3. it is one of the seven canonical current governance files in root `designs/`.

Everything else is reference-only unless concrete dependency evidence proves otherwise. A lexical match alone is not migration scope.

### Essential executable surfaces

- `src/main/**`
- `src/test/**`
- `pom.xml`
- `.github/workflows/maven-tests.yml`
- `.github/workflows/storefront-web-tests.yml` — essential wiring, no current legacy naming action
- `compose.prototype.yml`
- executable `storefront-web/**`

### Standing control/governance exceptions

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

- non-canonical `designs/**`, including authorities/system/historical/watch-list;
- `docs/**` except migration evidence;
- `experiments/**`;
- `README*`, `build_configuration/**`, `tools/**`, `lifecycle.md`, `operational-rules.md`, `workflow-tree.md`;
- historical implementation evidence and handoffs.

### Protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ IDs and accepted contract/version identifiers;
- applied Flyway migrations;
- stable compatibility/storage/serialization identifiers listed by the action map below.

Never rewrite an applied Flyway migration.

---

## 2. Execution Constraints

- work on `development`;
- no branch creation unless explicitly authorised;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- smallest conforming change only;
- no semantic redesign or unrelated cleanup.

`GR-REN-01F` is now complete. Mutation is authorised only within the frozen action map below.

---

## 3. Dispositions

- `RENAME_CURRENT_PRODUCT`
- `RENAME_CODE_NAMESPACE`
- `RENAME_CURRENT_DOCUMENTATION`
- `COMPATIBILITY_ALIAS_REQUIRED`
- `PRESERVE_COMPATIBILITY_ID`
- `PRESERVE_STABLE_GOVERNANCE_ID`
- `PRESERVE_HISTORICAL_EVIDENCE`
- `PRESERVE_IMMUTABLE_MIGRATION`
- `REVIEW_REQUIRED`

`REVIEW_REQUIRED` is not rename authority.

---

## 4. Completed Inventory

### `GR-REN-01A` — minimal surface discovery — `COMPLETE`

Evidence: `a5bb4ed9`, `29a7811c`, `afc06e36`, `9a0f69e8`, `25b1ba12`, `15b98ef0` + reconciliation, `219ac801`.

### `GR-REN-01B` — Java namespace exceptional consumers — `COMPLETE`

Evidence: `a7fc9d4a`, `c3fc871b`, `a5266155`, `ec4602c8`.

Key boundary:

- production/test filesystem roots, package declarations and imports migrate `mainstreet` → `grandrue`;
- `GrandRueApplication` moves with root so implicit Spring scan root follows `grandrue`;
- package/FQCN/path-sensitive conformance tests migrate atomically with namespace;
- compatibility-sensitive strings are excluded from blind namespace replacement.

### `GR-REN-01C` — executable build/runtime/config naming — `COMPLETE`

Evidence: `8f8bd715`, `4f3725ae`, `6a83c624`, `9c6c5ec4`.

Mapped:

- Maven `mainstreet:mainstreet`;
- CI `mainstreet_test`/`mainstreet` PostgreSQL values and `MAINSTREET_TEST_POSTGRES_*`;
- prototype Compose/Spring names and `MAINSTREET_PROTOTYPE_POSTGRES_*`;
- storefront npm `mainstreet-storefront-web`;
- storefront external `MAINSTREET_BACKEND_URL`.

### `GR-REN-01D` — compatibility-sensitive identity inventory — `COMPLETE`

Evidence: `8191a439e1fd2a6af0aa935bdfe709b0115d14ec`.

Protected compatibility boundary:

- `provider_payment_evidence.mainstreet_correlation_identifier` + `ProviderPaymentEvidence.mainStreetCorrelationIdentity`;
- historical notification failure-reference values containing `mainstreet.*` FQCNs;
- Opportunity binding v1 AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- `mainstreet-semantic-bundle-v1` / `mainstreet-semantic-bundle-v2`;
- `mainstreet-exposure-definitions-v1`;
- `calendar/current-main-street-commitments`.

### `GR-REN-01E` — controls/canonical governance boundary — `COMPLETE`

Evidence: `c7c33f3e3032ce578dd597745f1b354585b34c4e`.

Boundary:

- current product/repository/governance wording in active controls and seven canonical governance files may rename to GrandRue;
- `MS-*`, DQ/rule/accepted contract IDs remain unchanged;
- historical source references such as `Main_Street_Handoff_...` remain unchanged;
- accepted identifiers such as `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` remain unchanged.

---

## 5. `GR-REN-01F` — Final Pre-Mutation Action Map

State: `COMPLETE`

### `GR-REN-01F-01` — mechanical/current-product dispositions — `COMPLETE`

#### `RENAME_CODE_NAMESPACE`

- `src/main/java/mainstreet/**` → `src/main/java/grandrue/**`;
- `src/test/java/mainstreet/**` → `src/test/java/grandrue/**`;
- all corresponding package declarations/imports;
- `GrandRueApplication` package/root;
- package/path/FQCN-sensitive structural test strings established by `01B`.

#### `RENAME_CURRENT_PRODUCT`

- Maven project identity `mainstreet:mainstreet` → GrandRue-equivalent coordinates;
- repository-local CI PostgreSQL database/user/password/health-check values `mainstreet_test` / `mainstreet` → GrandRue equivalents;
- prototype Compose DB/user/password/health-check/default database and volume `mainstreet-prototype-postgres-v18` → GrandRue equivalents;
- storefront npm identity `mainstreet-storefront-web` → `grandrue-storefront-web`;
- current runtime-visible/product wording in already-touched executable files, including storefront metadata and current source comments where the wording denotes the product rather than a stable identifier.

#### `RENAME_CURRENT_DOCUMENTATION`

- current product/repository/governance wording in `AGENTS.md`;
- current title/product/repository/navigation wording in `SEQUENCE.md`;
- ordinary current product wording in the seven canonical root governance files;
- current implementation-navigation `mainstreet.*` references in those governance files when they describe the package namespace being migrated.

### `GR-REN-01F-02` — compatibility/persisted/external dispositions — `COMPLETE`

#### Direct cutover with coherent producer/consumer change

`RENAME_CURRENT_PRODUCT`:

- `MAINSTREET_TEST_POSTGRES_URL` → `GRANDRUE_TEST_POSTGRES_URL`;
- `MAINSTREET_TEST_POSTGRES_USER` → `GRANDRUE_TEST_POSTGRES_USER`;
- `MAINSTREET_TEST_POSTGRES_PASSWORD` → `GRANDRUE_TEST_POSTGRES_PASSWORD`.

Reason: these are test-only execution configuration controlled by the repository/authorised local verification workflow; all executable producers/consumers are already bounded and can change atomically. Historical docs remain untouched.

#### `COMPATIBILITY_ALIAS_REQUIRED`

- `MAINSTREET_PROTOTYPE_POSTGRES_{URL,USER,PASSWORD}`: introduce `GRANDRUE_PROTOTYPE_POSTGRES_*` as preferred names while retaining old-name fallback in Spring property resolution;
- `MAINSTREET_BACKEND_URL`: introduce `GRANDRUE_BACKEND_URL` as preferred runtime variable with `MAINSTREET_BACKEND_URL` fallback before localhost default.

Reason: these values may be supplied outside the repository by existing local/deployment environments. Alias fallback is low-cost and avoids unnecessary breakage.

#### `PRESERVE_COMPATIBILITY_ID`

Do **not** rename merely for branding consistency:

- physical DB column `mainstreet_correlation_identifier`;
- domain component/accessor `mainStreetCorrelationIdentity` and its persistence mapping, because it corresponds to the accepted platform-correlation concept and renaming it would add schema/semantic churn without executable benefit;
- old notification `failure_reference` values containing `mainstreet.*` FQCNs;
- Opportunity binding v1 AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- `mainstreet-semantic-bundle-v1` / `mainstreet-semantic-bundle-v2` format IDs;
- `mainstreet-exposure-definitions-v1` format ID;
- `calendar/current-main-street-commitments` policy identity.

New Java exception FQCN evidence naturally reflects `grandrue.*` after namespace migration; old persisted values remain valid evidence and require no rewrite.

#### `PRESERVE_IMMUTABLE_MIGRATION`

- every existing applied Flyway migration, including V24.

### `GR-REN-01F-03` — protected governance/history reconciliation — `COMPLETE`

#### `PRESERVE_STABLE_GOVERNANCE_ID`

- all `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ/rule/contract/version identifiers;
- accepted semantic identifiers containing `MAIN_STREET`, including `HANDLED_OUTSIDE_MAIN_STREET_RECORDED`.

#### `PRESERVE_HISTORICAL_EVIDENCE`

- historical implementation evidence/handoffs;
- historical source roadmap/file references, including `Main_Street_Handoff_MS-PROT-084_and_Digital_Operating_Infrastructure_Roadmap.md`;
- excluded designs/docs/history even where they contain legacy naming.

### `GR-REN-01F-04` — final gate — `COMPLETE`

The mutation action map is frozen. No unresolved naming decision blocks the minimal executable migration.

`mutation_authorised: true`

Mutation must follow dependency-safe groups and preserve all compatibility/protected exclusions above.

---

## 6. Mutation Programme

### `GR-REN-02` — production Java namespace migration

State: `OPEN`

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-02-01` | TASK | `READY` | move production Java namespace root + package/import declarations + `GrandRueApplication` coherently |
| `GR-REN-02-02` | TASK | `NOT_STARTED` | reconcile production current-product comments/wording only in touched source files; preserve compatibility IDs |
| `GR-REN-02-03` | GATE | `NOT_STARTED` | production namespace structural consistency/residual check |

### `GR-REN-03` — test namespace/runtime-coupled fixtures

State: `NOT_STARTED`

### `GR-REN-04` — essential build/CI naming

State: `NOT_STARTED`

### `GR-REN-05` — essential runtime/config naming

State: `NOT_STARTED`

### `GR-REN-06` — compatibility aliases/preserved identities

State: `NOT_STARTED`

### `GR-REN-07` — active controls/canonical governance wording

State: `NOT_STARTED`

### `GR-REN-08..11` — residual audit, falsification, structural/final verification

State: `NOT_STARTED`

---

## 7. Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
model: HIERARCHICAL_DEPENDENCY_GRAPH
scope: MINIMUM_EXECUTABLE_PLUS_ACTIVE_CONTROLS_AND_CANONICAL_GOVERNANCE
status: IN_PROGRESS
active_group: GR-REN-02
selected_execution_leaf: GR-REN-02-01
last_completed_task: GR-REN-01F-04
last_verified_head: c7c33f3e3032ce578dd597745f1b354585b34c4e
last_task_commit: c7c33f3e3032ce578dd597745f1b354585b34c4e
mutation_authorised: true
next_action: Execute GR-REN-02-01 only. Move the production Java namespace root from mainstreet to grandrue and update production package/import declarations plus GrandRueApplication coherently. Preserve all compatibility identifiers frozen by GR-REN-01F. Do not run Maven tests or GitHub Actions.
```

---

## 8. Restart and Verification

1. inspect `AGENTS.md` and this ledger;
2. inspect current `development` HEAD;
3. reconcile against checkpoint;
4. execute only selected bounded leaf;
5. preserve the frozen action map.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Structural verification is permitted and must not be represented as passed Maven/integration/runtime verification.
