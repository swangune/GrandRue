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

Migration scope is deliberately minimal:

1. files/paths whose legacy naming must change for the current GrandRue build, test, package, start, configure or serve path to remain coherent;
2. active repository controls: `AGENTS.md`, `SEQUENCE.md`, this ledger;
3. the seven canonical current governance files in root `designs/`.

Everything else is reference-only unless concrete dependency evidence proves otherwise. A lexical match alone is not migration scope.

### Essential executable surfaces

- `src/main/**`
- `src/test/**`
- `pom.xml`
- `.github/workflows/maven-tests.yml`
- `.github/workflows/storefront-web-tests.yml` — essential wiring; no current legacy naming action
- `compose.prototype.yml`
- executable `storefront-web/**`

### Standing governance/control exceptions

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

Reference-only files may still be read by tests; preserve required file presence and unrelated sentinels.

### Protected identities

Do not mechanically rename:

- `MS-PROT-*`, `MS-IMP-*`, `MS-IMPLEMENTATION-RULES-*`, `MS-DESIGN-RULES-*`;
- accepted DQ identifiers and accepted contract versions;
- applied Flyway migrations;
- persisted/external identifiers before compatibility disposition;
- published/serialized format identifiers whose old values remain decode inputs.

Never rewrite an applied Flyway migration.

---

## 2. Execution Constraints

- work on `development`;
- no new branch unless explicitly authorised;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- smallest conforming change only;
- no semantic redesign or unrelated cleanup;
- no runtime/product-name mutation until `GR-REN-01F` completes.

---

## 3. Required Naming Forms

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

Inspect active env/config, Maven/npm identities, database/runtime names, serialized formats, event/command/contract/provider/entitlement identities, FQCN/reflection/class persistence only on the minimal in-scope surface.

---

## 4. Dispositions

- `RENAME_CURRENT_PRODUCT`
- `RENAME_CODE_NAMESPACE`
- `RENAME_CURRENT_DOCUMENTATION`
- `MIGRATION_REQUIRED_PERSISTED_ID`
- `COMPATIBILITY_ALIAS_REQUIRED`
- `PRESERVE_STABLE_GOVERNANCE_ID`
- `PRESERVE_HISTORICAL_EVIDENCE`
- `PRESERVE_IMMUTABLE_MIGRATION`
- `REVIEW_REQUIRED`

`REVIEW_REQUIRED` is not rename authority.

---

## 5. Java Namespace Rule

Expected target after `01F`:

```text
package mainstreet.* → package grandrue.*
src/main/java/mainstreet/ → src/main/java/grandrue/
src/test/java/mainstreet/ → src/test/java/grandrue/
```

Ordinary package/path/import changes are mechanical. Non-mechanical consumers remain separately governed.

---

## 6. Completed Inventory Groups

### `GR-REN-01A` — minimal surface discovery — `COMPLETE`

Evidence: `a5bb4ed9`, `29a7811c`, `afc06e36`, `9a0f69e8`, `25b1ba12`, `15b98ef0` + reconciliation, `219ac801`.

Key result: source/test/storefront + `pom.xml`, Maven CI workflow and `compose.prototype.yml` are naming-relevant executable surfaces. Nonessential docs/design/history stay reference-only.

### `GR-REN-01B` — Java namespace exceptional consumers — `COMPLETE`

Evidence:

- `GR-REN-01B-01` — `a7fc9d4a022e62f2023673f7e4c61fb201a1e9df`
- `GR-REN-01B-02` — `c3fc871bbe2d2e32ee3e327bf07fcb6d33880e48` + addendum `a52661553c15bfd38e4d9bb4fb911ba73f90f84c`
- `GR-REN-01B-03` — `a52661553c15bfd38e4d9bb4fb911ba73f90f84c`
- `GR-REN-01B-04` — `ec4602c85075f2470c2363fc3996578eb0f2b439`

Frozen wave:

1. production path/packages/imports `mainstreet` → `grandrue`;
2. move `GrandRueApplication` with root so implicit Spring scan root follows `grandrue`;
3. test path/packages/imports plus package/path/FQCN-sensitive conformance assertions migrate in same coherent wave.

Do not blindly rename:

- Opportunity enquiry v1 AES-GCM AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- persisted/external identities;
- stable `MS-*` identifiers/reference-only material.

### `GR-REN-01C` — executable build/runtime/config naming — `COMPLETE`

Evidence:

- `01C-01` — `8f8bd71510e5b68b9a22bd06284c7de269652d2d`
- `01C-02` — `4f3725ae5686a1de6395559bee33162b9a1b79bf`
- `01C-03` — `6a83c624ecb2bff17b9cb4e8d30a0388d0cb0058`
- `01C-04` — `9c6c5ec441936bfaf62ee12a564bdecb60558f96`

Frozen boundary:

- Maven identity `mainstreet:mainstreet`;
- CI test DB/user/JDBC/health-check values + `MAINSTREET_TEST_POSTGRES_*` + all executable consumers;
- prototype Compose DB/user/password/health-check/volume + Spring defaults + `MAINSTREET_PROTOTYPE_POSTGRES_*` + contract test;
- storefront npm identity `mainstreet-storefront-web`;
- external env contract `MAINSTREET_BACKEND_URL`.

### `GR-REN-01D` — compatibility-sensitive persisted/external identity inventory — `COMPLETE`

Evidence: `8191a439e1fd2a6af0aa935bdfe709b0115d14ec`.

Frozen compatibility boundary:

- applied Flyway contents remain immutable;
- physical `provider_payment_evidence.mainstreet_correlation_identifier` is live through `JooqPaymentAuthorityStore`; any rename requires a forward migration;
- `ProviderPaymentEvidence.mainStreetCorrelationIdentity` is the live domain-side correlate;
- historical notification `failure_reference` values may contain `mainstreet.*` FQCNs and remain valid evidence;
- Opportunity enquiry v1 AAD must preserve old authentication/decode semantics;
- `mainstreet-semantic-bundle-v1` and `mainstreet-semantic-bundle-v2` are serialized format IDs; v2 participates in canonical digest/integrity evidence;
- `mainstreet-exposure-definitions-v1` is a decoder-enforced format marker;
- `calendar/current-main-street-commitments` is an active semantic policy identity, not branding prose;
- test-only labels such as `mainstreet-compiler-1` do not enter production migration scope without live producer/consumer evidence.

### `GR-REN-01E` — active controls/canonical governance boundary — `COMPLETE`

Evidence/classification recorded by this checkpoint.

#### `GR-REN-01E-01` — `AGENTS.md` + `SEQUENCE.md`

`AGENTS.md`:

- ordinary title/current governance/product prose using `Main Street` → `RENAME_CURRENT_DOCUMENTATION`;
- `MS-*` authority pointers → `PRESERVE_STABLE_GOVERNANCE_ID`.

`SEQUENCE.md`:

- title/current product-purpose labels/current repository identity (`swangune/MainStreet`) → `RENAME_CURRENT_DOCUMENTATION`;
- source roadmap filename `Main_Street_Handoff_MS-PROT-084_and_Digital_Operating_Infrastructure_Roadmap.md` → `PRESERVE_HISTORICAL_EVIDENCE`;
- `MS-*` authority identifiers → `PRESERVE_STABLE_GOVERNANCE_ID`;
- accepted identifiers such as `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` → `PRESERVE_STABLE_GOVERNANCE_ID`.

#### `GR-REN-01E-02` — seven canonical root governance files

Across the seven canonical current governance files:

- ordinary current titles/prose/product references using `Main Street` → `RENAME_CURRENT_DOCUMENTATION`;
- stable `MS-PROT-*`, `MS-IMP-*`, rule IDs, DQ IDs and accepted semantic/contract identifiers → `PRESERVE_STABLE_GOVERNANCE_ID`;
- historical/provenance references → `PRESERVE_HISTORICAL_EVIDENCE`;
- `mainstreet.*` package references in current implementation-navigation prose may follow the approved code namespace migration where they describe current implementation, but must not cause traversal/mutation of excluded authority/history files.

#### `GR-REN-01E-03` — frozen control/governance boundary

Only current control/governance wording is eligible for product-name replacement. Stable authority/contract identities and historical provenance are preserved exactly. No substantive semantic wording may be altered beyond the naming substitution authorised by the final action map.

---

## 7. Final Pre-Mutation Action Map (`GR-REN-01F`)

State: `OPEN`

| Node | Kind | State | Scope |
|---|---|---|---|
| `GR-REN-01F-01` | TASK | `READY` | assign mechanical/current-product rename dispositions |
| `GR-REN-01F-02` | TASK | `NOT_STARTED` | assign compatibility/persisted/external dispositions |
| `GR-REN-01F-03` | TASK | `NOT_STARTED` | reconcile protected governance/history dispositions |
| `GR-REN-01F-04` | GATE | `NOT_STARTED` | freeze exact mutation action map and authorise post-inventory groups |

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

## 8. Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
model: HIERARCHICAL_DEPENDENCY_GRAPH
scope: MINIMUM_EXECUTABLE_PLUS_ACTIVE_CONTROLS_AND_CANONICAL_GOVERNANCE
status: IN_PROGRESS
active_group: GR-REN-01F
selected_execution_leaf: GR-REN-01F-01
last_completed_task: GR-REN-01E-03
last_verified_head: 8191a439e1fd2a6af0aa935bdfe709b0115d14ec
last_task_commit: 8191a439e1fd2a6af0aa935bdfe709b0115d14ec
inventory_artifact: docs/development/grandrue-naming-migration-inventory.md
mutation_authorised: false
next_action: Execute GR-REN-01F-01 only. Assign dispositions to mechanical namespace/current-product/build/runtime naming already proven in scope. Do not mutate production/runtime names yet.
```

---

## 9. Restart and Verification

1. inspect `AGENTS.md` and this ledger;
2. inspect `SEQUENCE.md` only when programme/design sequencing materially applies;
3. fetch current `development` HEAD;
4. reconcile against `last_verified_head` and expected ledger-only descendants;
5. execute only selected bounded leaf.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Structural inspection must never be represented as passed Maven/integration/runtime verification.
