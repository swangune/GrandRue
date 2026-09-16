# GrandRue Naming Migration Ledger

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Baseline:** `c4153441d8340b229a29884967d796280d949a7d`  
**Status:** `IN_PROGRESS`  
**Authority class:** Non-semantic operational migration ledger  
**Purpose:** Preserve resumable, auditable state for the controlled Main Street → GrandRue repository naming migration without redefining accepted semantic authority.

---

## 1. Migration Boundary

This migration distinguishes three identity classes:

```text
GrandRue
    = current product / runtime / implementation identity

MS-PROT-* / MS-IMP-* / other stable MS-* governance identifiers
    = durable authority identity
    = preserve unless separately governed

historical Main Street evidence
    = preserve as historical terminology
```

This migration MUST NOT be implemented as a blind global search-and-replace.

A naming change MUST NOT silently alter accepted semantic meaning, authority ownership, persisted identity, replay/idempotency behaviour, external compatibility, database migration history or historical evidence.

`designs/AUTHORITY-INDEX.md` remains authoritative for current accepted authority navigation. This ledger records migration execution state only.

---

## 2. Governing Repository Rules

Before migration work, inspect and follow the current versions of:

- `AGENTS.md`
- `designs/DESIGN-RULES.md`
- `designs/DOCUMENT-GOVERNANCE.md`
- `designs/AUTHORITY-INDEX.md`
- `designs/DEFERRED-DECISION-REGISTER.md`
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
- `designs/IMPLEMENTATION-RULES.md`

Execution constraints for this migration:

- use the existing `development` branch;
- do not create a branch unless explicitly authorised in chat;
- repository manipulation is permitted;
- do not run Maven tests unless explicitly authorised;
- do not run GitHub Actions unless explicitly authorised;
- make the smallest conforming change;
- inspect before renaming;
- do not combine naming migration with semantic redesign or unrelated cleanup;
- stop the affected migration path when semantics or compatibility are materially unresolved.

---

## 3. Protected Identity Rules

### 3.1 Preserve stable governance identity

Do not mechanically rename:

- `MS-PROT-*`
- `MS-IMP-*`
- `MS-IMPLEMENTATION-RULES-*`
- `MS-DESIGN-RULES-*`
- accepted DQ identifiers
- accepted contract versions

These are durable governance/authority identities unless a separately approved authority explicitly changes them.

### 3.2 Preserve historical evidence

Do not rewrite historical terminology solely for branding consistency, including:

- `designs/historical/**`;
- historical implementation evidence;
- old conformance records;
- handoffs or records describing the project when it was named Main Street.

Historical terminology is valid repository-evolution evidence.

### 3.3 Preserve immutable database migration history

Existing applied Flyway migration files are immutable for this naming migration.

If database state contains a current value that must change, use a new forward migration after compatibility requirements have been established. Never rewrite an old migration merely to replace Main Street terminology.

---

## 4. Naming Forms Requiring Inventory

The `GR-REN-01A` through `GR-REN-01F` inventory track MUST collectively inventory at least these forms:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

The inventory track MUST also inspect naming embedded in:

- environment-variable prefixes;
- Spring properties/configuration keys;
- Maven coordinates;
- Docker/container identifiers;
- database names;
- schema/table data values where product identity may be persisted;
- URLs/domains;
- serialized values;
- event identifiers;
- command identities;
- contract identities;
- provider references;
- entitlement identities;
- test fixtures;
- filenames/directories;
- fully qualified Java class-name strings;
- reflection/class-name persistence;
- Spring component scanning and build/plugin wiring.

No rename phase may begin until `GR-REN-01F` has reconciled and classified the complete material inventory.

---

## 5. Classification Vocabulary

Every material occurrence identified by the `GR-REN-01A` through `GR-REN-01F` inventory track MUST be classified as exactly one of:

- `RENAME_CURRENT_PRODUCT`
- `RENAME_CODE_NAMESPACE`
- `RENAME_CURRENT_DOCUMENTATION`
- `MIGRATION_REQUIRED_PERSISTED_ID`
- `COMPATIBILITY_ALIAS_REQUIRED`
- `PRESERVE_STABLE_GOVERNANCE_ID`
- `PRESERVE_HISTORICAL_EVIDENCE`
- `PRESERVE_IMMUTABLE_MIGRATION`
- `REVIEW_REQUIRED`

`REVIEW_REQUIRED` occurrences MUST NOT be renamed until their semantics and compatibility requirements are established.

---

## 6. Persisted Identity Safety Gate

Before renaming any runtime string, ask:

> Can this value already exist outside the source tree?

If the answer is `YES` or `UNKNOWN`, inspect compatibility requirements before changing it.

Sensitive categories include:

- event identities;
- command identities;
- commercial entitlement identities;
- semantic contract references;
- serialized manifests;
- database values;
- message/outbox records;
- provider references;
- API payload values;
- configuration identifiers;
- idempotency keys;
- audit evidence;
- reflection/FQCN persistence.

Values participating in equality, persistence, replay, idempotency or external compatibility are migration problems, not text replacements.

---

## 7. Java Namespace Rule

The expected current-code namespace target, subject to `GR-REN-01F` classification and compatibility review, is:

```text
package mainstreet.*
    →
package grandrue.*
```

with corresponding source layout:

```text
src/main/java/mainstreet/
    → src/main/java/grandrue/

src/test/java/mainstreet/
    → src/test/java/grandrue/
```

Production package declarations, imports and filesystem paths must move coherently. Before renaming, inventory production packages, tests, imports, FQCN strings, Spring scanning, reflection, serialization, migration scripts, test resources and build/plugin configuration.

The repository MUST NOT be left indefinitely in an ambiguous half-package state.

---

## 8. Phase Model

### 8.1 Phase-sizing rule

A migration phase MUST represent one dominant concern with one bounded evidence set and one readily inspectable completion condition.

A phase MUST be subdivided before execution when it would otherwise:

- mix discovery with mutation;
- mix compatibility decisions with implementation;
- span unrelated identity categories;
- combine production, test, runtime and documentation changes unnecessarily;
- require a diff too large to inspect confidently in one bounded pass; or
- make restart state ambiguous.

Sub-phases MAY be introduced at any point using an additional suffix such as `GR-REN-05C-01`, `GR-REN-05C-02` when the discovered repository scope is still too large. Do not force a large change merely to preserve the phase table.

Prefer one coherent commit per micro-phase. A small auditable commit series is acceptable only when a single repository operation cannot safely be represented by one commit.

A micro-phase may complete with no non-ledger repository change when inspection proves that no applicable occurrence exists. The evidence and conclusion must still be recorded in this ledger.

Java namespace mutation is a special atomicity case. Do not split one bounded namespace wave into separate path-only, package-declaration-only and import-only checkpoints when doing so would intentionally leave the repository incoherent. Instead, partition the namespace into dependency-safe waves and change the filesystem path, package declaration, direct imports and safe non-persisted FQCN references for that wave together. Each wave MUST remain small enough to inspect independently and SHOULD be represented by its own child phase.

### 8.2 Inventory and classification

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-00` | Baseline and migration contract | `COMPLETE` |
| `GR-REN-01A` | Lexical search-form and repository-path inventory | `IN_PROGRESS` |
| `GR-REN-01B` | Java namespace, import, FQCN, Spring and reflection inventory | `NOT_STARTED` |
| `GR-REN-01C` | Build, runtime, configuration, environment, container and database-name inventory | `NOT_STARTED` |
| `GR-REN-01D` | Persisted, API, serialized, event, command, contract, provider and entitlement identity inventory | `NOT_STARTED` |
| `GR-REN-01E` | Current documentation, governance, historical and immutable-migration terminology inventory | `NOT_STARTED` |
| `GR-REN-01F` | Inventory reconciliation, classification and frozen rename/action map | `NOT_STARTED` |

No rename phase below may start before `GR-REN-01F` is complete.

### 8.3 Production Java namespace

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-02A` | Partition production Java namespace into dependency-safe rename waves and record the exact child-phase order | `NOT_STARTED` |
| `GR-REN-02B-*` | Execute one production namespace wave per child phase: path + package declaration + direct imports + safe non-persisted FQCN references | `NOT_STARTED` |
| `GR-REN-02C` | Reconcile remaining cross-wave production namespace references | `NOT_STARTED` |
| `GR-REN-02D` | Repair and verify production Spring scanning, reflection and runtime class wiring | `NOT_STARTED` |

`GR-REN-02B-*` is a phase family, not one large task. `GR-REN-02A` MUST instantiate concrete children such as `GR-REN-02B-01`, `GR-REN-02B-02` from the classified inventory. Each child must be independently completable and inspectable.

### 8.4 Test Java namespace

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-03A` | Partition test Java namespace into dependency-safe rename waves after production namespace changes are known | `NOT_STARTED` |
| `GR-REN-03B-*` | Execute one test namespace wave per child phase: path + package declaration + direct imports + safe non-persisted FQCN references | `NOT_STARTED` |
| `GR-REN-03C` | Reconcile remaining test fixtures, resources and class-name references | `NOT_STARTED` |

`GR-REN-03B-*` follows the same child-phase rule as production namespace work. Do not create one repository-wide test namespace rename task when smaller dependency-safe waves are available.

### 8.5 Build and runtime naming

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-04A` | Maven current-product coordinates, artifact and module naming | `NOT_STARTED` |
| `GR-REN-04B` | Build/source-set/plugin wiring affected by renamed paths or classes | `NOT_STARTED` |
| `GR-REN-05A` | Spring application property and configuration-key naming | `NOT_STARTED` |
| `GR-REN-05B` | Environment-variable prefix and variable naming | `NOT_STARTED` |
| `GR-REN-05C` | Docker, container and local/test infrastructure naming | `NOT_STARTED` |
| `GR-REN-05D` | Runtime database/connection naming that is not immutable migration history | `NOT_STARTED` |
| `GR-REN-05E` | URLs, domains, provider configuration and other current runtime product strings | `NOT_STARTED` |

### 8.6 Persisted and externally visible identity

These phases separate decision from mutation deliberately.

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-06A` | Event, command and contract identity compatibility decisions | `NOT_STARTED` |
| `GR-REN-06B` | Entitlement, provider and configuration identity compatibility decisions | `NOT_STARTED` |
| `GR-REN-06C` | API payload, serialized value and persisted/reflected FQCN compatibility decisions | `NOT_STARTED` |
| `GR-REN-06D` | Database value, outbox, audit and idempotency identity migration decisions | `NOT_STARTED` |
| `GR-REN-06E` | Implement approved compatibility aliases/adapters only | `NOT_STARTED` |
| `GR-REN-06F` | Add approved forward database migration(s) only | `NOT_STARTED` |
| `GR-REN-06G` | Structural replay/idempotency/external-compatibility review of implemented identity changes | `NOT_STARTED` |

A decision phase may conclude `PRESERVE` and therefore require no implementation phase change for that identity.

### 8.7 Current product wording and active documentation

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-07A` | Runtime-visible current product wording, messages and non-identity strings | `NOT_STARTED` |
| `GR-REN-07B` | Source comments and non-authoritative current development documentation | `NOT_STARTED` |
| `GR-REN-07C` | `AGENTS.md`, README, SEQUENCE and active repository navigation wording | `NOT_STARTED` |
| `GR-REN-07D` | Current non-historical design/authority prose classified safe for product-name replacement | `NOT_STARTED` |
| `GR-REN-07E` | Review remaining current-authority Main Street wording that cannot be changed mechanically | `NOT_STARTED` |

`GR-REN-07E` MUST NOT silently amend semantic authority. Any material authority change discovered there follows its governing design/document lifecycle separately.

### 8.8 Residual audits and falsification

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-08A` | Production/test code namespace and FQCN residual audit | `NOT_STARTED` |
| `GR-REN-08B` | Build/runtime/configuration/environment residual audit | `NOT_STARTED` |
| `GR-REN-08C` | Persisted/API/serialized/compatibility residual audit | `NOT_STARTED` |
| `GR-REN-08D` | Documentation residual audit against explicit preserve classifications | `NOT_STARTED` |
| `GR-REN-08E` | Stable governance ID, historical evidence and Flyway immutability audit | `NOT_STARTED` |
| `GR-REN-09A` | Compatibility falsification across persistence, replay, idempotency and external boundaries | `NOT_STARTED` |
| `GR-REN-09B` | Semantic-authority contradiction and ownership falsification | `NOT_STARTED` |

### 8.9 Verification and closeout

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-10A` | Filesystem/package/import/source-set structural consistency verification | `NOT_STARTED` |
| `GR-REN-10B` | Spring/build/configuration/runtime wiring structural consistency verification | `NOT_STARTED` |
| `GR-REN-10C` | Git provenance, checkpoint and migration-ledger consistency verification | `NOT_STARTED` |
| `GR-REN-11A` | Full implementation verification gate where separately authorised and required | `NOT_STARTED` |
| `GR-REN-11B` | Final migration closeout and ledger completion | `NOT_STARTED` |

`GR-REN-11A` does not itself grant permission to run Maven tests or GitHub Actions. Section 14 continues to govern those actions.

A phase is complete only when:

1. its intended repository change or inspection evidence is committed;
2. that phase commit is inspected;
3. this ledger records the inspected phase commit SHA;
4. the next phase/action is explicit.

---

## 9. Checkpoint Semantics

A Git commit cannot contain its own final SHA because the SHA depends on the committed file content. Therefore this ledger distinguishes:

- **phase commit** — the commit containing the substantive phase change;
- **ledger checkpoint commit** — a subsequent ledger-only commit that records the already-created and inspected phase commit SHA.

`last_verified_head` means **the latest inspected substantive phase commit recorded by the ledger**, not the SHA of the ledger checkpoint commit containing that field.

On restart, if repository `HEAD` differs from `last_verified_head`, inspect the difference before continuing. A direct descendant whose only change is an expected `GRANDRUE-MIGRATION.md` operational/checkpoint update is an expected reconciliation case; verify it and continue. Any other difference is unexpected and MUST be reconciled before migration work resumes.

This rule prevents an impossible self-referential commit-SHA requirement while preserving auditable phase provenance.

---

## 10. Machine-Readable Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
status: IN_PROGRESS
current_phase: GR-REN-01A
last_completed_phase: GR-REN-00
last_verified_head: 26d22025fc536b010e29327724b47f0a4b34b12a
last_phase_commit: 26d22025fc536b010e29327724b47f0a4b34b12a
last_ledger_checkpoint_commit: SELF_NOT_RECORDABLE
next_action: Complete GR-REN-01A only: inventory the required lexical naming forms and record their repository locations before performing deeper semantic classification or any rename.
```

---

## 11. Phase Evidence

### GR-REN-00 — Baseline and migration contract

- Baseline verified: `c4153441d8340b229a29884967d796280d949a7d`
- Baseline commit message: `docs: stabilize remaining production authority trace anchors`
- Intervening commits before migration start: none
- Governing files inspected: yes
- Ledger-establishment commit: `26d22025fc536b010e29327724b47f0a4b34b12a`
- Commit inspection: `COMPLETE` — only `GRANDRUE-MIGRATION.md` was added; no runtime or accepted-authority file changed
- Phase status: `COMPLETE`
- Next phase: `GR-REN-01A`

### GR-REN-01A — Lexical search-form and repository-path inventory

- Phase status: `IN_PROGRESS`
- Inventory baseline: current `development` state after the GR-REN-00 ledger checkpoint and subsequent ledger-only phase-model decomposition
- Inventory artifact/location: this ledger, Section 12, unless size requires a separately named non-authoritative inventory file explicitly linked here
- Scope: required lexical forms and their repository locations only
- Rename actions authorised by this phase: none
- Next phase after completion: `GR-REN-01B`

### Phase-model decomposition

- Reason: the original `GR-REN-01` through `GR-REN-11` model combined multiple discovery, decision, mutation and verification concerns into phases that were too large for reliable bounded completion.
- Effect: the migration scope and protected-identity rules are unchanged; only execution granularity and checkpointability are refined.
- Prior broad `GR-REN-01` had not completed and is replaced by `GR-REN-01A` through `GR-REN-01F` before rename work begins.
- Java namespace work is additionally constrained to small dependency-safe atomic waves so phase boundaries do not deliberately create half-renamed source states.

### Checkpoint history

| Phase | Verified phase commit | Inspection | Ledger checkpoint |
|---|---|---|---|
| `GR-REN-00` | `26d22025fc536b010e29327724b47f0a4b34b12a` | `COMPLETE` | recorded by the ledger-only commit containing this row |

---

## 12. Naming Inventory

Inventory entries are recorded by the active `GR-REN-01A` through `GR-REN-01F` micro-phase.

No occurrence is safe to rename merely because its spelling matches a migration search term.

### GR-REN-01A — Lexical search-form and repository-path inventory

`IN_PROGRESS`

---

## 13. Restart Procedure

At the beginning of every migration session:

1. inspect `AGENTS.md`;
2. inspect this ledger;
3. fetch current `development` HEAD;
4. compare it to `last_verified_head` using Section 9 checkpoint semantics;
5. inspect any intervening commit(s);
6. confirm the recorded migration phase;
7. continue from `next_action`.

Never infer migration progress from conversation memory alone.

If HEAD and ledger state disagree unexpectedly:

```text
STOP
→ inspect the difference
→ reconcile the ledger
→ only then continue
```

---

## 14. Verification Constraints

Until explicitly authorised otherwise:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Structural verification remains required and may include:

- Git diff/commit inspection;
- package/import consistency checks;
- path existence checks;
- duplicate-source detection;
- residual-name searches;
- stable-identifier checks;
- migration-ledger updates.

The normal full implementation verification gate remains separate and is not implicitly authorised by this migration ledger.
