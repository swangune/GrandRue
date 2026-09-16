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

Everything else is reference-only unless concrete dependency evidence proves otherwise.

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

### Protected identities

Do not mechanically rename stable `MS-*`/DQ/contract identifiers, applied Flyway migrations, or compatibility/storage/serialization identifiers frozen by `GR-REN-01F`.

---

## 2. Execution Constraints

- work on `development`;
- no branch creation unless explicitly authorised;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- smallest conforming change only;
- no semantic redesign or unrelated cleanup.

Mutation is authorised only within the frozen `GR-REN-01F` action map.

---

## 3. Frozen Pre-Mutation Action Map

`GR-REN-01A` through `GR-REN-01F` are complete.

Key authorised actions:

- production/test Java package roots `mainstreet` → `grandrue` with package/import/path-sensitive test changes;
- Maven/build/CI/prototype/storefront current product naming → GrandRue equivalents;
- preferred `GRANDRUE_PROTOTYPE_POSTGRES_*` and `GRANDRUE_BACKEND_URL` with legacy external-env fallbacks where frozen by `01F`;
- current wording in `AGENTS.md`, `SEQUENCE.md`, and seven canonical governance files → GrandRue.

Key preserved identities:

- applied Flyway contents;
- `mainstreet_correlation_identifier` / `mainStreetCorrelationIdentity`;
- historical notification failure FQCN evidence;
- Opportunity binding v1 AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- `mainstreet-semantic-bundle-v1/v2`;
- `mainstreet-exposure-definitions-v1`;
- `calendar/current-main-street-commitments`;
- stable `MS-*`, accepted DQ/rule/contract IDs and historical evidence.

Final action-map evidence: `08dcfda8a8b23bc442c3d63a4754c2ed6b74ab52`.

---

## 4. Mutation Programme

### `GR-REN-02` — production Java namespace migration

State: `OPEN`

`GR-REN-02-01` is a parent group. Production package moves must be executed as bounded package leaves with all production cross-package consumers included atomically. Do not migrate test packages in this group; test namespace migration is `GR-REN-03`.

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-02-01` | GROUP | `OPEN` | production Java namespace root migration, decomposed below |
| `GR-REN-02-01A` | TASK | `READY` | `identitysecurity/**` + bounded production consumers |
| `GR-REN-02-01B+` | GROUP | `EXPANSION_REQUIRED` | remaining production package roots; select by bounded dependency evidence |
| `GR-REN-02-02` | TASK | `NOT_STARTED` | reconcile current-product comments/wording only in touched production source files |
| `GR-REN-02-03` | GATE | `NOT_STARTED` | production namespace structural consistency/residual check |

#### `GR-REN-02-01A` scope

Move these eight owner files from `src/main/java/mainstreet/identitysecurity/` to `src/main/java/grandrue/identitysecurity/`, changing only their package declarations:

- `IdentitySecurityGeneration.java`
- `IdentitySecurityGenerationException.java`
- `IdentitySecurityGenerationFailureCategory.java`
- `IdentitySecurityGenerationInitializer.java`
- `IdentitySecurityGenerationManagement.java`
- `IdentitySecurityGenerationService.java`
- `IdentitySecurityRotationCommand.java`
- `IdentitySecurityRotationReason.java`

Update the exact production consumers discovered by bounded search:

- `src/main/java/mainstreet/infrastructure/persistence/identitysecurity/JooqIdentitySecurityGenerationManagement.java`
- `src/main/java/mainstreet/infrastructure/security/webauthn/WebAuthnAuthenticationSubjectService.java`

Reference-only `build_configuration/package_boundary.md` and historical implementation evidence remain untouched. Test consumers remain under `GR-REN-03`.

`done_when`: all eight production owner files exist only under `grandrue.identitysecurity`, the two bounded production consumers import `grandrue.identitysecurity`, and no production import/package declaration still uses `mainstreet.identitysecurity`.

No compatibility identity in the frozen action map may change in this leaf.

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

## 5. Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
status: IN_PROGRESS
mutation_authorised: true
active_group: GR-REN-02
selected_execution_leaf: GR-REN-02-01A
last_completed_task: GR-REN-01F-04
last_verified_head: 08dcfda8a8b23bc442c3d63a4754c2ed6b74ab52
last_task_commit: 08dcfda8a8b23bc442c3d63a4754c2ed6b74ab52
next_action: Execute GR-REN-02-01A only. Move identitysecurity production package plus the two bounded production consumer imports. Structural verification only; do not run Maven tests or GitHub Actions.
```

---

## 6. Restart and Verification

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

Structural verification must not be represented as passed Maven/integration/runtime verification.
