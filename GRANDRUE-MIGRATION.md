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

`GR-REN-01` MUST inventory at least these forms:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

It MUST also inspect naming embedded in:

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

---

## 5. Classification Vocabulary

Every material occurrence identified by `GR-REN-01` MUST be classified as exactly one of:

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

The expected current-code namespace target, subject to `GR-REN-01` classification and compatibility review, is:

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

| Phase | Purpose | State |
|---|---|---|
| `GR-REN-00` | Baseline and migration contract | `IN_PROGRESS` |
| `GR-REN-01` | Repository-wide naming inventory/classification | `NOT_STARTED` |
| `GR-REN-02` | Production Java package namespace | `NOT_STARTED` |
| `GR-REN-03` | Test Java package namespace | `NOT_STARTED` |
| `GR-REN-04` | Imports, filesystem layout, Spring and build wiring | `NOT_STARTED` |
| `GR-REN-05` | Runtime/configuration/environment naming | `NOT_STARTED` |
| `GR-REN-06` | Persisted/API/serialized identifier assessment and migration | `NOT_STARTED` |
| `GR-REN-07` | Current product wording and source documentation | `NOT_STARTED` |
| `GR-REN-08` | AGENTS / README / SEQUENCE / active repository navigation | `NOT_STARTED` |
| `GR-REN-09` | Residual-name audit | `NOT_STARTED` |
| `GR-REN-10` | Compatibility and semantic falsification | `NOT_STARTED` |
| `GR-REN-11` | Verification gate | `NOT_STARTED` |

Prefer one coherent commit or a small auditable commit series per phase.

A phase is complete only when:

1. its intended repository change is committed;
2. that phase commit is inspected;
3. this ledger records the inspected phase commit SHA;
4. the next phase/action is explicit.

---

## 9. Checkpoint Semantics

A Git commit cannot contain its own final SHA because the SHA depends on the committed file content. Therefore this ledger distinguishes:

- **phase commit** — the commit containing the substantive phase change;
- **ledger checkpoint commit** — a subsequent ledger-only commit that records the already-created and inspected phase commit SHA.

`last_verified_head` means **the latest inspected substantive phase commit recorded by the ledger**, not the SHA of the ledger checkpoint commit containing that field.

On restart, if repository `HEAD` differs from `last_verified_head`, inspect the difference before continuing. A direct descendant whose only change is the expected `GRANDRUE-MIGRATION.md` checkpoint update is an expected reconciliation case; verify it and continue. Any other difference is unexpected and MUST be reconciled before migration work resumes.

This rule prevents an impossible self-referential commit-SHA requirement while preserving auditable phase provenance.

---

## 10. Machine-Readable Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
status: IN_PROGRESS
current_phase: GR-REN-00
last_completed_phase: NONE
last_verified_head: c4153441d8340b229a29884967d796280d949a7d
last_phase_commit: NONE
last_ledger_checkpoint_commit: NONE
next_action: Inspect the GR-REN-00 ledger-establishment commit, record its SHA in this ledger, mark GR-REN-00 complete, and begin GR-REN-01 repository-wide naming inventory/classification.
```

---

## 11. Phase Evidence

### GR-REN-00 — Baseline and migration contract

- Baseline verified: `c4153441d8340b229a29884967d796280d949a7d`
- Baseline commit message: `docs: stabilize remaining production authority trace anchors`
- Intervening commits before migration start: none
- Governing files inspected: yes
- Ledger-establishment commit: `PENDING`
- Commit inspection: `PENDING`
- Phase status: `IN_PROGRESS`
- Next phase: `GR-REN-01`

### GR-REN-01 — Repository-wide naming inventory/classification

- Phase status: `NOT_STARTED`
- Inventory baseline: `PENDING`
- Inventory artifact/location: this ledger, Section 12, unless size requires a separately named non-authoritative inventory file explicitly linked here
- Rename actions authorised by inventory: none until classification is complete

---

## 12. Naming Inventory

`GR-REN-01` inventory entries will be recorded here after `GR-REN-00` is checkpointed.

No occurrence is safe to rename merely because its spelling matches a migration search term.

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
