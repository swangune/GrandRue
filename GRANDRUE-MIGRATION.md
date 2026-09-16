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
- `docs/**` except migration evidence;
- `experiments/**`;
- `README*`, `build_configuration/**`, `tools/**`, `lifecycle.md`, `operational-rules.md`, `workflow-tree.md`;
- historical implementation evidence and handoffs.

Some reference-only files are read by conformance tests. They remain reference-only for naming migration where asserted executable sentinels do not depend on legacy naming. Preserve required file presence and unrelated sentinel text.

### Protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ identifiers and accepted contract versions;
- applied Flyway migration files;
- persisted/external identifiers before compatibility is decided;
- published/serialized format identifiers whose old values remain decode inputs.

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

Also inspect active environment variables, Spring/package scanning, Maven/npm coordinates, database/runtime configuration, URLs/domains, serialized formats, event/command/contract/provider/entitlement identities, FQCN strings and reflection/class-persistence uses only on the minimal executable/control/governance surface.

---

## 4. Classification Gate

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

Expected target, subject to `GR-REN-01F`:

```text
package mainstreet.* → package grandrue.*
src/main/java/mainstreet/ → src/main/java/grandrue/
src/test/java/mainstreet/ → src/test/java/grandrue/
```

Ordinary path/package/import changes are mechanical. Non-mechanical FQCN/reflection/persistence/scanning consumers are handled separately. Namespace mutation must use coherent dependency-safe waves.

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

`COMPLETE` — `26d22025fc536b010e29327724b47f0a4b34b12a`.

### `GR-REN-01A` — minimal surface discovery

State: `COMPLETE`

| Node | State | Evidence / result |
|---|---|---|
| `GR-REN-01A-01` | `COMPLETE` | root operational/build discovery — `a5bb4ed90ee402ce88c46c0707070cb42488f6bb` |
| `GR-REN-01A-02` | `COMPLETE` | `src/main/**` — `29a7811c737508ec80214ceff1bb1b28b7762bac` |
| `GR-REN-01A-03` | `COMPLETE` | `src/test/**` — `afc06e36d4091977b3d8bfb29b02e2eed6343003` |
| `GR-REN-01A-04` | `COMPLETE` | `storefront-web/**` — `9a0f69e80561a400b963582195d5c75437cc13bb` |
| `GR-REN-01A-05` | `COMPLETE` | root controls/navigation — `25b1ba128d8ea47cf864c41e531de7482c1ba93c` |
| `GR-REN-01A-06` | `COMPLETE` | executable-path essentiality — `15b98ef041f35a326fd0f6a7661dca0d8b86a0e1` + reconciliation |
| `GR-REN-01A-07` | `COMPLETE` | seven canonical root governance files — `219ac801ef2eb09a8f15d9e99c6403ebf34c39b7` |
| `GR-REN-01A-08..12` | `EXCLUDED` | reference-only surfaces |
| `GR-REN-01A-13` | `COMPLETE` | minimal-surface reconciliation |

Historical `GR-REN-01A-06E` (`6e23bd1...`) used an overly narrow local-only scope interpretation and is superseded for classification; historical evidence remains untouched.

### `GR-REN-01B` — Java namespace exceptional-consumer inventory

State: `COMPLETE`

| Node | State | Evidence / result |
|---|---|---|
| `GR-REN-01B-01` | `COMPLETE` | implicit Spring root `mainstreet`; no explicit scan override — `a7fc9d4a022e62f2023673f7e4c61fb201a1e9df` |
| `GR-REN-01B-02` | `COMPLETE_WITH_ADDENDUM` | production non-mechanical consumers — `c3fc871bbe2d2e32ee3e327bf07fcb6d33880e48`, addendum `a52661553c15bfd38e4d9bb4fb911ba73f90f84c` |
| `GR-REN-01B-03` | `COMPLETE` | package/FQCN/path-sensitive tests — `a52661553c15bfd38e4d9bb4fb911ba73f90f84c` |
| `GR-REN-01B-04` | `COMPLETE` | Java namespace boundary frozen — `ec4602c85075f2470c2363fc3996578eb0f2b439` |

Frozen namespace wave after `01F` authorisation:

1. move `src/main/java/mainstreet/**` to `src/main/java/grandrue/**` and update production packages/imports;
2. move `GrandRueApplication` with the root so implicit Spring scanning follows `grandrue`;
3. move `src/test/java/mainstreet/**` to `src/test/java/grandrue/**`, updating test packages/imports and structural package/path/FQCN assertions;
4. include directly coupled source-set/build references whose separation would break compilation, discovery or scanning.

Exceptions withheld from blind rename:

- `OpportunityEnquiryBindingCodec` v1 AES-GCM AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- persisted/external identities;
- stable `MS-*` identifiers and reference-only material.

`NotificationDeliveryCoordinator` stores `providerFailure.getClass().getName()` in durable delivery-attempt failure evidence, so future package names require compatibility treatment.

### `GR-REN-01C` — executable build/runtime/config naming inventory

State: `COMPLETE`

| Node | State | Evidence / result |
|---|---|---|
| `GR-REN-01C-01` | `COMPLETE` | Maven + CI/test PostgreSQL naming — `8f8bd71510e5b68b9a22bd06284c7de269652d2d` |
| `GR-REN-01C-02` | `COMPLETE` | prototype runtime naming — `4f3725ae5686a1de6395559bee33162b9a1b79bf` |
| `GR-REN-01C-03` | `COMPLETE` | storefront package/runtime env naming — `6a83c624ecb2bff17b9cb4e8d30a0388d0cb0058` |
| `GR-REN-01C-04` | `COMPLETE` | executable build/runtime boundary — `9c6c5ec441936bfaf62ee12a564bdecb60558f96` |

Frozen executable naming boundary:

- Maven identity: `mainstreet:mainstreet`.
- CI test contract: workflow `mainstreet_test`/`mainstreet` values + `MAINSTREET_TEST_POSTGRES_{URL,USER,PASSWORD}` + all test consumers.
- Prototype runtime: Compose DB/user/password/health-check/volume + Spring defaults + `MAINSTREET_PROTOTYPE_POSTGRES_{URL,USER,PASSWORD}` + contract test.
- Storefront npm identity: `mainstreet-storefront-web` in package and lockfile.
- External configuration requiring compatibility disposition: `MAINSTREET_TEST_POSTGRES_*`, `MAINSTREET_PROTOTYPE_POSTGRES_*`, `MAINSTREET_BACKEND_URL`.

### `GR-REN-01D` — compatibility-sensitive persisted/external identity inventory

State: `COMPLETE`

| Node | State | Result |
|---|---|---|
| `GR-REN-01D-01` | `COMPLETE` | known persisted/serialized cases mapped |
| `GR-REN-01D-02` | `COMPLETE` | active runtime format/policy identifiers containing legacy product naming mapped |
| `GR-REN-01D-03` | `COMPLETE` | immutable Flyway/protected persistence boundary mapped |
| `GR-REN-01D-04` | `COMPLETE` | compatibility-sensitive identity boundary frozen below |

#### `GR-REN-01D-01` — known persisted/serialized cases

1. **Payment correlation persistence**
   - applied `V24__money__create_payment_authority.sql` created physical column `provider_payment_evidence.mainstreet_correlation_identifier`;
   - `JooqPaymentAuthorityStore` reads/writes that exact column;
   - `ProviderPaymentEvidence` exposes the corresponding `mainStreetCorrelationIdentity` component;
   - V24 is immutable. Any physical column rename requires a new forward migration plus coordinated live-code change.

2. **Notification delivery failure evidence**
   - `NotificationDeliveryCoordinator` creates `provider-exception:<FQCN>` from `providerFailure.getClass().getName()`;
   - `JooqNotificationStore` persists it verbatim in `notification_delivery_attempt.failure_reference` and reads it back verbatim;
   - old rows can therefore retain `mainstreet.*` class names after the Java namespace moves. Historical values must remain readable as evidence; no data rewrite is implied by package migration.

3. **Opportunity enquiry binding v1 AAD**
   - AES-GCM AAD is `mainstreet/enquiry/opportunity-binding/v1/<key-id>` for both encode and decode;
   - already-issued v1 tokens authenticate only with the same AAD;
   - the existing v1 AAD must be preserved, or a separately versioned compatibility strategy must decode old tokens. Blind rename is prohibited.

#### `GR-REN-01D-02` — active legacy-named runtime identifiers

The bounded executable search identified active values whose spelling is part of runtime/serialized identity rather than display wording:

- `mainstreet-semantic-bundle-v1` — legacy semantic bundle format identifier;
- `mainstreet-semantic-bundle-v2` — current semantic bundle format identifier used in packaging, digest calculation, decoding/materialisation and tests;
- `mainstreet-exposure-definitions-v1` — Exposure definition codec format marker checked by the decoder;
- `calendar/current-main-street-commitments` — active Projection policy identity registered/consumed by the initial portfolio/evaluator.

The semantic bundle format identifier participates in the canonical content digest, so changing the string also changes integrity evidence. Existing v1/v2 material therefore requires stable old-format decoding. The Exposure codec rejects any format marker other than the exact current marker. These are compatibility/protocol identifiers, not branding prose.

Test-only fixture labels such as `mainstreet-compiler-1` or `mainstreet-booking-storage` do not by themselves become production migration targets unless a live executable producer/consumer is established.

#### `GR-REN-01D-03` — immutable persistence boundary

- Applied Flyway files remain byte/history immutable.
- The only identified product-named physical schema identifier in the minimal executable surface is `mainstreet_correlation_identifier` from V24.
- Live jOOQ code currently binds directly to that name.
- If `01F` later authorises a schema rename, implementation must add a forward migration and update live code/tests atomically; V24 itself remains unchanged.
- Generic columns such as notification `failure_reference` are not renamed merely because stored values may contain historical `mainstreet.*` FQCNs.

#### `GR-REN-01D-04` frozen compatibility boundary

Do not mechanically rename any of the following during namespace/product migration:

- applied Flyway migration contents;
- `mainstreet_correlation_identifier` without an approved forward schema migration;
- existing notification failure-reference values containing `mainstreet.*` FQCNs;
- Opportunity binding v1 AAD;
- semantic bundle v1/v2 format identifiers without backward decode/integrity compatibility;
- Exposure definition v1 format marker without backward decode compatibility;
- `current-main-street-commitments` policy identity unless classification establishes an approved identity migration/alias.

These items proceed to `GR-REN-01F` for disposition. No mutation was performed by `GR-REN-01D`.

### `GR-REN-01E` — active controls and canonical-governance boundary

State: `OPEN`

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-01E-01` | TASK | `READY` | classify current-product wording vs protected/historical references in `AGENTS.md` and `SEQUENCE.md` |
| `GR-REN-01E-02` | TASK | `NOT_STARTED` | classify current-product wording vs stable authority identifiers in seven canonical root `designs/` governance files |
| `GR-REN-01E-03` | GATE | `NOT_STARTED` | freeze active-control/governance rename boundary |

### `GR-REN-01F` — reconcile/classify minimal inventory and freeze action map

State: `NOT_STARTED`

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
| `GR-REN-01C-04` | `9c6c5ec441936bfaf62ee12a564bdecb60558f96` |
| `GR-REN-01D-01..04` | evidence recorded in `GR-REN-01D-04` task commit; checkpoint SHA recorded by next ledger update |

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
active_group: GR-REN-01E
selected_execution_leaf: GR-REN-01E-01
last_completed_task: GR-REN-01D-04
last_verified_head: 9c6c5ec441936bfaf62ee12a564bdecb60558f96
last_task_commit: 9c6c5ec441936bfaf62ee12a564bdecb60558f96
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
mutation_authorised: false
next_action: Execute GR-REN-01E-01 only. Classify legacy naming in AGENTS.md and SEQUENCE.md as current-product wording, stable/protected identifier, or historical/provenance reference. Do not rename anything.
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
