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

`GR-REN-02-01` is a parent group. Production package moves are bounded package leaves with all production cross-package consumers included atomically. Test packages remain for `GR-REN-03`.

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-02-01` | GROUP | `OPEN` | production Java namespace migration |
| `GR-REN-02-01A` | TASK | `COMPLETE_PENDING_FINAL_VERIFICATION` | `identitysecurity/**` + bounded production consumers; code commit `6396afba5fdf3e76e2099536d8a3451184650056`; lineage reconciliation `b7149659134714b0baf15899f59ad3f44d1c7014` |
| `GR-REN-02-01B` | TASK | `COMPLETE_PENDING_FINAL_VERIFICATION` | `audit/**` + bounded production consumers; code commit `fbabeed82d767d2454923f7767f3655ca690642f` |
| `GR-REN-02-01C` | TASK | `COMPLETE_PENDING_FINAL_VERIFICATION` | `businesshours/**` + bounded production consumer; code commit `8b23ee7ca70e84159d4f9dfbe5bf4e2b06b63516` |
| `GR-REN-02-01D` | TASK | `COMPLETE_PENDING_FINAL_VERIFICATION` | `credential/**` + bounded production consumer; code commit `bd7df15233f61cc7968447e49bfa1dedb0b5c8fb` |
| `GR-REN-02-01E` | TASK | `COMPLETE_PENDING_FINAL_VERIFICATION` | `media/**` + bounded production consumer; code commit `d1d9059e071ef6362aefabba0423a6f5298b5cd4` |
| `GR-REN-02-01F+` | GROUP | `EXPANSION_REQUIRED` | remaining production package roots; select by bounded dependency evidence |
| `GR-REN-02-02` | TASK | `NOT_STARTED` | current-product comments/wording only in touched production source files |
| `GR-REN-02-03` | GATE | `NOT_STARTED` | production namespace structural consistency/residual check |

#### `GR-REN-02-01A` exact scope

Moved eight owner files from `src/main/java/mainstreet/identitysecurity/` to `src/main/java/grandrue/identitysecurity/`, changing only package declarations:

- `IdentitySecurityGeneration.java`
- `IdentitySecurityGenerationException.java`
- `IdentitySecurityGenerationFailureCategory.java`
- `IdentitySecurityGenerationInitializer.java`
- `IdentitySecurityGenerationManagement.java`
- `IdentitySecurityGenerationService.java`
- `IdentitySecurityRotationCommand.java`
- `IdentitySecurityRotationReason.java`

Updated bounded production consumers:

- `src/main/java/mainstreet/infrastructure/persistence/identitysecurity/JooqIdentitySecurityGenerationManagement.java`
- `src/main/java/mainstreet/infrastructure/security/webauthn/WebAuthnAuthenticationSubjectService.java`

No current-product prose/comments, stable identifiers, schema names or compatibility strings were changed. Reference-only docs/history and test consumers were not modified.

Structural verification evidence on `development`: owner path resolves under `grandrue.identitysecurity`, former `mainstreet.identitysecurity` owner path is absent, and the bounded production consumer imports resolve to `grandrue.identitysecurity`. The source commit and earlier checkpoint had diverged from `ee39bfa3b204b11779a177f3eaa7bf8e1e6baaa0`; merge commit `b7149659134714b0baf15899f59ad3f44d1c7014` reconciled both lineages without force-updating the branch.

`done_when`: owner files exist only under `grandrue.identitysecurity`; bounded production consumers import `grandrue.identitysecurity`; no production package/import declaration remains for `mainstreet.identitysecurity`.

#### `GR-REN-02-01B` exact scope

Moved four owner files from `src/main/java/mainstreet/audit/` to `src/main/java/grandrue/audit/`, changing only package declarations:

- `AuditActionClass.java`
- `AuditExecutionScope.java`
- `AuditRecord.java`
- `AuditStore.java`

Updated bounded production consumers:

- `src/main/java/mainstreet/infrastructure/persistence/audit/JooqAuditStore.java`
- `src/main/java/mainstreet/infrastructure/persistence/identitysecurity/JooqIdentitySecurityGenerationManagement.java`

No current-product prose/comments, stable `MS-PROT-064` authority reference, schema names, persistence identifiers, or test consumers were changed.

Structural verification evidence on `development`: `AuditRecord.java` resolves under `grandrue.audit`, its former `mainstreet.audit` owner path is absent, and both bounded production consumers import `grandrue.audit`. Maven tests and GitHub Actions were not run.

`done_when`: owner files exist only under `grandrue.audit`; bounded production consumers import `grandrue.audit`; no production package/import declaration remains for `mainstreet.audit`.

#### `GR-REN-02-01C` exact scope

Moved eleven owner files from `src/main/java/mainstreet/businesshours/` to `src/main/java/grandrue/businesshours/`, changing only package declarations:

- `BusinessHoursFailureCategory.java`
- `BusinessHoursMutationException.java`
- `BusinessHoursScope.java`
- `BusinessHoursScopeKind.java`
- `ConfigureStandardBusinessHoursCommand.java`
- `StandardBusinessHours.java`
- `StandardBusinessHoursAuthority.java`
- `StandardBusinessHoursRevision.java`
- `StandardBusinessHoursRevisionDisposition.java`
- `WeeklyOperatingInterval.java`
- `WithdrawStandardBusinessHoursCommand.java`

Updated bounded production consumer:

- `src/main/java/mainstreet/infrastructure/persistence/businesshours/JooqStandardBusinessHoursAuthority.java`

No current-product prose/comments, stable `MS-PROT-050` authority reference, SQL/schema names, persistence identifiers, business-hours revision identities, or test consumers were changed.

Structural verification evidence on `development`: `StandardBusinessHours.java` resolves under `grandrue.businesshours`, its former `mainstreet.businesshours` owner path is absent, and the bounded production consumer imports `grandrue.businesshours`. Maven tests and GitHub Actions were not run.

`done_when`: owner files exist only under `grandrue.businesshours`; bounded production consumers import `grandrue.businesshours`; no production package/import declaration remains for `mainstreet.businesshours`.

#### `GR-REN-02-01D` exact scope

Moved seven owner files from `src/main/java/mainstreet/credential/` to `src/main/java/grandrue/credential/`, changing only package declarations:

- `CredentialBinding.java`
- `CredentialBindingScope.java`
- `CredentialGeneration.java`
- `CredentialGenerationPolicy.java`
- `CredentialGenerationState.java`
- `CredentialSecurityStore.java`
- `CredentialTechnicalUse.java`

Updated bounded production consumer:

- `src/main/java/mainstreet/infrastructure/persistence/credential/JooqCredentialSecurityStore.java`

The existing cross-package `mainstreet.application.MerchantScope` dependency was preserved because `application/**` is outside this leaf. No current-product prose/comments, schema/table/field names, credential identities, protected-material references, persistence semantics, or test consumers were changed.

Structural verification evidence on `development`: `CredentialBinding.java` resolves under `grandrue.credential`, its former `mainstreet.credential` owner path is absent, and the bounded production consumer imports `grandrue.credential`. Maven tests and GitHub Actions were not run.

`done_when`: owner files exist only under `grandrue.credential`; bounded production consumers import `grandrue.credential`; no production package/import declaration remains for `mainstreet.credential`.

#### `GR-REN-02-01E` exact scope

Moved ten owner files from `src/main/java/mainstreet/media/` to `src/main/java/grandrue/media/`, changing only package declarations:

- `DeliveryFidelity.java`
- `MediaAsset.java`
- `MediaKind.java`
- `MediaProcessingOutcome.java`
- `MediaRendition.java`
- `MediaRole.java`
- `MediaStore.java`
- `MediaValidationState.java`
- `RenditionProfile.java`
- `RenditionProfileRegistry.java`

Updated bounded production consumer:

- `src/main/java/mainstreet/infrastructure/persistence/media/JooqMediaStore.java`

The existing cross-package `mainstreet.application.MerchantScope` dependency was preserved because `application/**` is outside this leaf. Existing current-product prose in `MediaAsset.java` was deliberately left unchanged for `GR-REN-02-02`. Stable `MS-PROT-066`, schema/table/field names, asset/rendition identities, storage/source references, persistence semantics, and test consumers were not changed.

Structural verification evidence on `development`: `MediaAsset.java` resolves under `grandrue.media`, its former `mainstreet.media` owner path is absent, and the bounded production consumer imports `grandrue.media`. Maven tests and GitHub Actions were not run.

`done_when`: owner files exist only under `grandrue.media`; bounded production consumers import `grandrue.media`; no production package/import declaration remains for `mainstreet.media`.

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
selected_execution_leaf: GR-REN-02-01E
last_completed_task: GR-REN-02-01E
last_verified_head: d1d9059e071ef6362aefabba0423a6f5298b5cd4
last_task_commit: d1d9059e071ef6362aefabba0423a6f5298b5cd4
next_action: Select the next bounded production package leaf under GR-REN-02-01F+ using production dependency evidence. Keep test namespace changes for GR-REN-03. Do not run Maven tests or GitHub Actions.
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
