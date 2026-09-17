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
| `GR-REN-02-01O` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `booking/**` + nine bounded production consumers | code `9c36c0720ac5a2b48e22f4f04fc621a8298632c7`; lineage reconciliation `07acfe2a54ac62c856e644146dbadb9aa117b72c` |
| `GR-REN-02-01P` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `ordering/**` + nine bounded production consumers | `390c0713ecdab445b8558a6ed6dec6f65bdb8677` |
| `GR-REN-02-01Q` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `notification/**` + one bounded production consumer | `64ccb3b83fdeca775c226f34aa045f4200517ec1` |
| `GR-REN-02-01R` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `deployment/**` + three bounded production consumers | `f5a5d71ec17a99ed52aae9a76fdc80e54d1c12aa` |
| `GR-REN-02-01S` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `workforce/**` + five bounded production consumers | `cddb54d8c8e76ef8db7208f21dc38f2c2e300c57` |
| `GR-REN-02-01T` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `scheduling/**` + eight bounded production consumers | `11f7b8e3e7706703cadbaa27d257df1ab2752cbb` |
| `GR-REN-02-01U` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `fulfilment/**` + twelve bounded production consumers | `cf0e6497ca9e2e20a7479659458e7441072b1a63` |
| `GR-REN-02-01V+` | `EXPANSION_REQUIRED` | remaining production package roots; select by live bounded dependency evidence | pending |
| `GR-REN-02-02` | `NOT_STARTED` | current-product comments/wording only in touched production source files | pending |
| `GR-REN-02-03` | `NOT_STARTED` | production namespace structural consistency/residual gate | pending |

### Important lineage and boundary notes

- `GR-REN-02-01J`: the initial customer commit accidentally omitted an existing three-line explanatory comment in `OrderingApplicationService.java`; `a79384f0018e2bd09b5cd28dafe60cc157607017` restored it. Aggregate pre-leaf→corrected diff is clean.
- `GR-REN-02-01L`: frozen `mainstreet_correlation_identifier` and `mainStreetCorrelationIdentity` were explicitly preserved.
- `GR-REN-02-01M`: `JooqDurableWorkStore.java` changed only nine `background` imports (`+9/-9`). The persistence lock identity `background|...`, SQL/schema identifiers, stable `MS-PROT-*` references and semantics were preserved.
- Before `GR-REN-02-01M`, accidental commit `5fc49f80ba09f7f33c74b921b57f94e605996eee` created empty `__dummy__`; correction `f877398f5ed1213dbfaa1f48ff67d38332b8d8b3` immediately removed it. Comparison from `50cc3f9462363b120de8f7e2d47fad60a180487e` to `f877398f5ed1213dbfaa1f48ff67d38332b8d8b3` has zero changed files.
- `GR-REN-02-01N`: `src/main/java/mainstreet/GrandRueApplication.java` moved to `src/main/java/grandrue/GrandRueApplication.java`; only its package declaration changed. Its test FQCN consumer remains deferred to `GR-REN-03`.
- `GR-REN-02-01O`: thirteen Booking owner files moved from `mainstreet.booking` to `grandrue.booking`. Nine live production consumers were updated only for Booking imports: `ConfigurationImpactReviewApplicationService`, three prototype Booking use-case files, two prototype Booking response files, and three Booking persistence adapters. Two stale-search appointment candidates were verified live and required no change. Test consumers remain deferred to `GR-REN-03`.
- The `GR-REN-02-01O` code commit and ledger checkpoint were initially created as sibling children of repair baseline `abda517cd8f42a197c95af8adec55d1843576ea1`. Merge commit `07acfe2a54ac62c856e644146dbadb9aa117b72c` reconciles both lineages without force-updating the branch and contains the Booking code tree plus checkpoint ledger.
- `GR-REN-02-01P`: sixteen Ordering owner files moved from `mainstreet.ordering` to `grandrue.ordering`. Nine live production consumers were updated only for Ordering imports: `ConfigurationImpactReviewApplicationService`, three prototype Ordering use-case files, three prototype Ordering delivery DTO/request files, and two Ordering persistence adapters. Existing `grandrue.booking`, `grandrue.customer`, `grandrue.inventory`, and `grandrue.money` imports were preserved. SQL/table names, the `ordering-command|` persistence lock identity, stable `MS-PROT-*` references and Ordering semantics were unchanged. Existing current-product `Main Street` strings in `OrderingAvailabilityImpactAssessment.java` remain intentionally deferred to `GR-REN-02-02`. Test consumers remain deferred to `GR-REN-03`. Aggregate structural diff from `6071ae80cf3eb0a37a0b2fc6d10afcf41a94adb5` to `390c0713ecdab445b8558a6ed6dec6f65bdb8677` contains exactly sixteen `+1/-1` owner renames and nine import-only consumer changes. Maven tests and GitHub Actions were not run.
- `GR-REN-02-01Q`: nineteen Notification owner files moved from `mainstreet.notification` to `grandrue.notification`. The only live bounded production consumer, `mainstreet.infrastructure.persistence.notification.JooqNotificationStore`, was updated only for eleven Notification imports. SQL/table/column names and persistence behaviour were unchanged. Historical notification provider-failure evidence continues to record `providerFailure.getClass().getName()` without altering the evidence format; stable `MS-PROT-075` references were preserved. Existing current-product `Main Street` prose in `NotificationContractRegistry.java` remains intentionally deferred to `GR-REN-02-02`. Test consumers remain deferred to `GR-REN-03`. Aggregate structural diff from `cf5c5a1d29dbb97cc234c68055c7906bac9c578f` to `64ccb3b83fdeca775c226f34aa045f4200517ec1` contains exactly nineteen `+1/-1` owner renames and one import-only consumer change (`+11/-11`). Maven tests and GitHub Actions were not run.
- `GR-REN-02-01R`: twenty-two Deployment owner files moved from `mainstreet.deployment` to `grandrue.deployment`. Three live bounded production consumers were updated only for Deployment imports: `mainstreet.infrastructure.persistence.deployment.JooqServingDeploymentAdmissionSnapshotAuthority`, `mainstreet.infrastructure.persistence.deployment.JooqOrdinaryServingGenerationPromotionAuthority`, and `mainstreet.infrastructure.persistence.configuration.JooqConfigurationActivationAdmissionAuthority`. SQL/table/column names, semantic-release identities, persistence behaviour, and existing source comments were unchanged. Test consumers remain deferred to `GR-REN-03`. Aggregate structural diff from `2b5cdebcb91eeed2f44cdbab4274574083d1315e` to `f5a5d71ec17a99ed52aae9a76fdc80e54d1c12aa` contains exactly twenty-two `+1/-1` owner renames and three import-only consumer changes (`+6/-6`, `+19/-19`, and `+3/-3`). The old production package path is absent and the new path is present. Maven tests and GitHub Actions were not run.
- `GR-REN-02-01S`: eighteen Workforce owner files moved from `mainstreet.workforce` to `grandrue.workforce`. Five live bounded production consumers were updated only for Workforce imports: `mainstreet.runtime.StaffOperationalTrustedExecutionContextEstablisher`, `mainstreet.surface.AudienceObservationAdmissionEvaluator`, `mainstreet.infrastructure.persistence.workforce.JooqMerchantWorkforceAuthority`, `mainstreet.infrastructure.persistence.workforce.JooqMerchantWorkforceStore`, and `mainstreet.infrastructure.persistence.workforce.JooqMerchantOperationalDeviceAuthorisationStore`. SQL/table/column names, advisory-lock behaviour, stable `MS-PROT-*` references, and source behaviour were unchanged. Existing current-product `Main Street` prose in Workforce owner sources remains intentionally deferred to `GR-REN-02-02`. Test consumers remain deferred to `GR-REN-03`. Aggregate structural diff from `fafdcc99376adb01ccb972b722a20a2e32c1f763` to `cddb54d8c8e76ef8db7208f21dc38f2c2e300c57` contains exactly eighteen `+1/-1` owner renames and five import-only consumer changes (`+2/-2`, `+1/-1`, `+2/-2`, `+10/-10`, and `+3/-3`). The old production package path is absent and the new path is present. Maven tests and GitHub Actions were not run.
- `GR-REN-02-01T`: twenty-two Scheduling owner files moved from `mainstreet.scheduling` to `grandrue.scheduling`. Eight live bounded production consumers were updated only for Scheduling imports: `mainstreet.application.ConfigurationImpactReviewApplicationService`, `mainstreet.prototype.PrototypeAppointmentUseCase`, `mainstreet.prototype.PrototypeJooqAppointmentUseCase`, `mainstreet.prototype.PrototypePublicAppointmentUseCase`, `mainstreet.prototype.delivery.PrototypeAppointmentResponse`, `mainstreet.prototype.delivery.PrototypePublicAppointmentResponse`, `mainstreet.infrastructure.persistence.appointment.JooqAppointmentUnitOfWork`, and `mainstreet.infrastructure.persistence.appointment.JooqAppointmentTransaction`. SQL/table/column names, Appointment advisory-lock identities, stable `MS-PROT-*` references, existing source comments, and Scheduling/Appointment behaviour were unchanged. Existing current-product `Main Street Scheduling` strings in `SchedulingAvailabilityImpactAssessment.java` remain intentionally deferred to `GR-REN-02-02`. Test consumers remain deferred to `GR-REN-03`. Aggregate structural diff from `b6f7efb8454190b0547f070f6c767e9f984924a9` to `11f7b8e3e7706703cadbaa27d257df1ab2752cbb` contains exactly twenty-two `+1/-1` owner renames and eight import-only consumer changes (`+1/-1`, `+5/-5`, `+1/-1`, `+1/-1`, `+1/-1`, `+6/-6`, `+4/-4`, and `+1/-1`). The old production package path is absent and the new path is present. Maven tests and GitHub Actions were not run.
- `GR-REN-02-01U`: twenty Fulfilment owner files moved from `mainstreet.fulfilment` to `grandrue.fulfilment`. Twelve live bounded production consumers were updated only for Fulfilment imports: `mainstreet.surface.SurfaceInteractionAvailabilityContract`, `mainstreet.semantic.release.SemanticReleaseAssembly`, `mainstreet.semantic.configuration.ConfigurationPackageResolver`, `mainstreet.surface.FulfilmentRoleInteractionAvailabilityAuthority`, `mainstreet.surface.ContextualSurfaceResolver`, `mainstreet.semantic.configuration.ConfigurationChangeSet`, `mainstreet.semantic.configuration.MerchantConfiguration`, `mainstreet.semantic.release.SemanticReleaseMaterialiser`, `mainstreet.application.ConfigurationImpactReviewApplicationService`, `mainstreet.semantic.configuration.ResolvedConfigurationPackage`, `mainstreet.infrastructure.persistence.configuration.JooqConfigurationChangeAuthority`, and `mainstreet.infrastructure.persistence.configuration.JooqConfigurationRevisionAuthority`. SQL/table/column names, configuration advisory-lock identities, semantic-release identities, `mainstreet.semantic` capability-seed namespace, stable `MS-PROT-*` references, and source behaviour were unchanged. Existing current-product `Main Street` prose in `FulfilmentRoleIdentity.java` remains intentionally deferred to `GR-REN-02-02`. Test consumers remain deferred to `GR-REN-03`. Aggregate structural diff from repair baseline `f254be429c14701e16249a8de5ce7360be46466e` to `cf0e6497ca9e2e20a7479659458e7441072b1a63` contains exactly twenty `+1/-1` owner renames and twelve import-only consumer changes (`+1/-1`, `+1/-1`, `+4/-4`, `+1/-1`, `+1/-1`, `+1/-1`, `+1/-1`, `+1/-1`, `+3/-3`, `+1/-1`, `+1/-1`, and `+1/-1`). The old production package path is absent and the new path is present. Maven tests and GitHub Actions were not run.

### Ledger integrity repair

Checkpoint commit `ee7b9c659b8038cd8ef14af9c80ac29102322516` correctly recorded the `GR-REN-02-01N` table entry but accidentally truncated later portions of this non-semantic ledger during file replacement. No production source was affected. Repair commit `abda517cd8f42a197c95af8adec55d1843576ea1` reconstructed the canonical operational record from the last intact checkpoint and retained commit evidence, while compacting repeated file-level detail into commit references.

During `GR-REN-02-01U` staging, accidental connector commit `749af81b99b0a788c09653ff390138a51fe89901` created an empty root `__nonexistent__` placeholder. Repair commit `f254be429c14701e16249a8de5ce7360be46466e` immediately removed it. Comparison from Scheduling checkpoint `e20f47166f836723014fc405f73c50cdebafc6a9` to the repair commit has zero changed files. The verified Fulfilment code tree was then re-parented onto the repaired HEAD without force-updating the branch.

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
selected_execution_leaf: GR-REN-02-01U
last_completed_task: GR-REN-02-01U
last_verified_head: cf0e6497ca9e2e20a7479659458e7441072b1a63
last_task_commit: cf0e6497ca9e2e20a7479659458e7441072b1a63
next_action: Select and execute the next bounded production namespace leaf under GR-REN-02-01V+ using live production dependency evidence. Keep test namespace changes for GR-REN-03. Do not run Maven tests or GitHub Actions.
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