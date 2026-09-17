# GrandRue Naming Migration Ledger

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Baseline:** `c4153441d8340b229a29884967d796280d949a7d`  
**Status:** `IN_PROGRESS`  
**Authority class:** non-semantic operational migration ledger

This is the canonical resumable execution registry for the Main Street → GrandRue naming migration. It records operational scope and evidence; it does not redefine accepted semantic authority. Detailed file-level evidence is retained in the referenced commits rather than duplicated here.

---

## 1. Migration Contract

This is a **minimum executable migration plus active repository controls and canonical governance**.

A path is a migration target only when:

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

---

## 2. Execution Constraints

- work only on `development`;
- no branch creation unless explicitly authorised;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- smallest conforming dependency-bounded change only;
- no semantic redesign or unrelated cleanup;
- production namespace leaves include all production cross-package consumers atomically;
- test namespace/runtime-coupled changes remain deferred to `GR-REN-03`;
- current-product prose/comments in moved production sources remain deferred to `GR-REN-02-02`;
- structural verification must not be represented as Maven/integration/runtime verification.

Mutation is authorised only within the frozen `GR-REN-01F` action map.

---

## 3. Frozen Pre-Mutation Action Map

`GR-REN-01A` through `GR-REN-01F` are complete.

Final action-map evidence: `08dcfda8a8b23bc442c3d63a4754c2ed6b74ab52`.

### Authorised current-identity changes

- production/test Java package roots `mainstreet` → `grandrue`, including package/import/path-sensitive test changes in their later governed group;
- Maven/build/CI/prototype/storefront current product naming → GrandRue equivalents;
- preferred `GRANDRUE_PROTOTYPE_POSTGRES_*` and `GRANDRUE_BACKEND_URL`, retaining frozen legacy external-env fallbacks where required;
- current wording in `AGENTS.md`, `SEQUENCE.md`, and the seven canonical governance files → GrandRue.

### Protected identities — do not mechanically rename

- applied Flyway migration contents;
- `mainstreet_correlation_identifier` / `mainStreetCorrelationIdentity`;
- historical notification failure FQCN evidence;
- Opportunity binding v1 AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- `mainstreet-semantic-bundle-v1/v2`;
- `mainstreet-exposure-definitions-v1`;
- `calendar/current-main-street-commitments`;
- stable `MS-*`, accepted DQ/rule/contract identifiers, and historical evidence.

---

## 4. Mutation Programme

### `GR-REN-02` — production Java namespace migration

State: `OPEN`

`GR-REN-02-01` is a parent group. Each completed leaf below is `COMPLETE_PENDING_FINAL_VERIFICATION`. The referenced code commits are the authoritative file-level evidence.

| Node | State | Production scope | Evidence |
|---|---|---|---|
| `GR-REN-02-01A` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `identitysecurity/**` + bounded consumers | code `6396afba5fdf3e76e2099536d8a3451184650056`; lineage reconciliation `b7149659134714b0baf15899f59ad3f44d1c7014` |
| `GR-REN-02-01B` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `audit/**` + bounded consumers | `fbabeed82d767d2454923f7767f3655ca690642f` |
| `GR-REN-02-01C` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `businesshours/**` + bounded consumer | `8b23ee7ca70e84159d4f9dfbe5bf4e2b06b63516` |
| `GR-REN-02-01D` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `credential/**` + bounded consumer | `bd7df15233f61cc7968447e49bfa1dedb0b5c8fb` |
| `GR-REN-02-01E` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `media/**` + bounded consumer | `d1d9059e071ef6362aefabba0423a6f5298b5cd4` |
| `GR-REN-02-01F` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `protection/**` + bounded consumers | `f7a729aebd9c93a9ecf5f9af7c39bac05331ef5c` |
| `GR-REN-02-01G` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `resilience/**`; no external production consumers | `87a51a1679d13b04d0cc9fde02edb0d24cb91e79` |
| `GR-REN-02-01H` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `observability/**`; no external production consumers | `833fc7428d9e61e03a612d7b99e358112fd660b0` |
| `GR-REN-02-01I` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `privacy/**` + bounded consumer | `f00d5e57ad24b8be80e7cdaf1a3894d614b08903` |
| `GR-REN-02-01J` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `customer/**` + six bounded consumers | code `de95f490823ab080f832c07b9cf8f933b409df84`; boundary correction `a79384f0018e2bd09b5cd28dafe60cc157607017` |
| `GR-REN-02-01K` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `inventory/**` + five bounded consumers | `6b641099a6694647d947339031b8185c6a2cd0a8` |
| `GR-REN-02-01L` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `money/**` + six bounded consumers | `50cc3f9462363b120de8f7e2d47fad60a180487e` |
| `GR-REN-02-01M` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `background/**` + eight bounded consumers | `6a610d767a5d8decfdc839d621127d9a73901490` |
| `GR-REN-02-01N` | `COMPLETE_PENDING_FINAL_VERIFICATION` | root production `GrandRueApplication.java`; no production consumers | `6ba9245b16dc70aa5a9da58d739ce4b422a1e2bb` |
| `GR-REN-02-01O+` | `EXPANSION_REQUIRED` | remaining production package roots; select by live bounded dependency evidence | pending |
| `GR-REN-02-02` | `NOT_STARTED` | current-product comments/wording only in touched production source files | pending |
| `GR-REN-02-03` | `NOT_STARTED` | production namespace structural consistency/residual gate | pending |

### Important lineage and boundary notes

- `GR-REN-02-01J`: the initial customer commit accidentally omitted an existing three-line explanatory comment in `OrderingApplicationService.java`; `a79384f0018e2bd09b5cd28dafe60cc157607017` restored it. Aggregate pre-leaf→corrected diff is clean.
- `GR-REN-02-01L`: frozen `mainstreet_correlation_identifier` and `mainStreetCorrelationIdentity` were explicitly preserved.
- `GR-REN-02-01M`: `JooqDurableWorkStore.java` changed only nine `background` imports (`+9/-9`). The persistence lock identity `background|...`, SQL/schema identifiers, stable `MS-PROT-*` references and semantics were preserved.
- Before `GR-REN-02-01M`, accidental commit `5fc49f80ba09f7f33c74b921b57f94e605996eee` created empty `__dummy__`; correction `f877398f5ed1213dbfaa1f48ff67d38332b8d8b3` immediately removed it. Comparison from `50cc3f9462363b120de8f7e2d47fad60a180487e` to `f877398f5ed1213dbfaa1f48ff67d38332b8d8b3` has zero changed files.
- `GR-REN-02-01N`: `src/main/java/mainstreet/GrandRueApplication.java` moved to `src/main/java/grandrue/GrandRueApplication.java`; only its package declaration changed. Its test FQCN consumer remains deferred to `GR-REN-03`.

### Ledger integrity repair

Checkpoint commit `ee7b9c659b8038cd8ef14af9c80ac29102322516` correctly recorded the `GR-REN-02-01N` table entry but accidentally truncated later portions of this non-semantic ledger during file replacement. No production source was affected. This ledger revision reconstructs the canonical operational record from the last intact checkpoint and retained commit evidence, while compacting repeated file-level detail into commit references.

### Remaining programme

- `GR-REN-03` — test namespace/runtime-coupled fixtures: `NOT_STARTED`
- `GR-REN-04` — essential build/CI naming: `NOT_STARTED`
- `GR-REN-05` — essential runtime/config naming: `NOT_STARTED`
- `GR-REN-06` — compatibility aliases/preserved identities: `NOT_STARTED`
- `GR-REN-07` — active controls/canonical governance wording: `NOT_STARTED`
- `GR-REN-08..11` — residual audit, falsification, structural/final verification: `NOT_STARTED`

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
selected_execution_leaf: GR-REN-02-01N
last_completed_task: GR-REN-02-01N
last_verified_head: 6ba9245b16dc70aa5a9da58d739ce4b422a1e2bb
last_task_commit: 6ba9245b16dc70aa5a9da58d739ce4b422a1e2bb
next_action: Select and execute the next bounded production namespace leaf under GR-REN-02-01O+ using live production dependency evidence. Keep test namespace changes for GR-REN-03. Do not run Maven tests or GitHub Actions.
```

---

## 6. Restart and Verification

1. inspect `AGENTS.md` and this ledger;
2. inspect current `development` HEAD;
3. reconcile branch HEAD against the checkpoint and any ledger-only checkpoint commit;
4. select the smallest conforming production namespace leaf using live dependency evidence;
5. move the owner package and all production cross-package consumers atomically;
6. structurally verify aggregate diff, new-path presence, old-path absence, and consumer imports;
7. checkpoint the completed leaf here;
8. preserve the frozen action map and protected identities.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Structural verification must not be represented as passed Maven/integration/runtime verification.
