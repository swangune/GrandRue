# Main Street Repository Document Hygiene Review — 26 August 2026

**Status:** REVIEW COMPLETE — NON-SEMANTIC CLEANUP ONLY  
**Authority class:** Repository/documentation hygiene evidence  
**Governed by:** `designs/DOCUMENT-GOVERNANCE.md`, `designs/DESIGN-CORPUS-CONFORMANCE.md`  
**Branch:** `development`  
**Purpose:** Reduce navigation mistakes without deleting historical design reasoning or reorganising the corpus in a way that obscures authority provenance.

---

## 1. Governing Approach

The earlier recommendation to improve repository hygiene is implemented conservatively.

The repository MUST prefer:

```text
preserve historical reasoning
+
make current authority obvious
+
remove provably content-free noise
+
avoid mass rename/move churn
```

rather than reorganising every historical file into new directories merely for aesthetic uniformity.

`AUTHORITY-INDEX.md` remains the authoritative navigation mechanism for accepted semantic/design and TAS/ADR authority.

---

## 2. Safe Cleanup Performed

### Removed empty stray file

Deleted:

`docs/development/PRD/ana`

Reason:

- zero-byte file;
- no semantic/product content to preserve;
- no role in current governance;
- likely abandoned filename fragment for Analytics.

This is content-free cleanup, not semantic deletion.

---

## 3. Historical / Navigation Findings Preserved

### 3.1 Two AI PRD documents

```text
docs/development/PRD/ai-platfome-and automation-engine.md
docs/development/PRD/ai-platform.md
```

Both contain substantive product reasoning. They are not deleted or merged automatically.

Issues:

- first filename contains spelling/spacing defects;
- both cover overlapping AI-platform product territory;
- either may contain historical product requirements not present in the other.

Action:

**PRESERVE.** Future product-document consolidation may merge them only after a product-level comparison confirms no requirement is lost. Current semantic authority is MS-PROT-057 and applicable capability authorities, not either filename.

---

### 3.2 Merchant personas filename typo

Existing:

`docs/development/PRD/merchnat-personas.md`

Issue:

`merchnat` is a filename typo.

Action:

**PRESERVE FOR NOW.** Renaming without checking external/internal references is unnecessary churn and does not improve semantic authority. The traceability view treats it as strategic/persona evidence.

---

### 3.3 MS-PROT-043 historical/proposed duplicate naming

The `designs/` directory contains more than one similarly named MS-PROT-043 base document because an earlier PROPOSED version and the later ACCEPTED authority were preserved.

Action:

**PRESERVE.** The Authority Index resolves the accepted v1.2 + v1.3 composite authority. Proposed evidence must not be treated as current merely because its filename looks canonical.

A future navigation linter may warn when proposed and accepted files share the same stable ID with unclear filename distinction; this is a WARNING, not an authority collision.

---

### 3.4 ADR-005 Business Order Domain Model

`docs/foundation/adr/business-order-domain-model.md` is substantive historical reasoning and MUST NOT be deleted.

Its universal Business Order proposition conflicts with later accepted capability distinctions and is not indexed as current implementation authority.

Current `AUTHORITY-INDEX.md` now explicitly warns that it cannot be used to fill the promoted Order/Ordering semantic gap.

Action:

**PRESERVE AS HISTORICAL EVIDENCE.** If the reviewed Order authority is later approved, record a narrow supersession of ADR-005's universalisation claim rather than deleting the ADR.

---

### 3.5 Unindexed legacy TAS documents

Current `docs/development/TAS/` includes historical logical-architecture material plus the indexed accepted recovery TAS.

Action:

**PRESERVE.** Only indexed accepted TAS/ADR documents are current implementation-architecture authority. TAS consolidation is handled by a separate review rather than destructive file moves.

---

### 3.6 Miscellaneous design evidence filenames

Files such as:

```text
designs/Operation Execution Contract
designs/Executable Semantic Model.md
designs/Main Street Prototype Specification.md
designs/Prototype 1 technology choices.md
```

remain useful historical/prototype evidence but are not current accepted semantic authority merely by residing under `designs/`.

Action:

**PRESERVE.** Current authority is determined through lifecycle metadata and `AUTHORITY-INDEX.md`.

---

## 4. Rejected Hygiene Approach

The review rejects a mass migration such as:

```text
designs/accepted/
designs/proposed/
designs/review/
designs/historical/
```

for the existing corpus at this stage.

Reasons:

- large path churn would break references and reduce useful Git/path continuity;
- semantic amendment chains need scope-aware reading, not merely folder classification;
- `AUTHORITY-INDEX.md` already solves the current-authority navigation problem;
- historical preservation is an explicit conformance goal;
- implementation does not benefit from moving hundreds of stable paths.

New review evidence should continue to live under `docs/development/`; accepted semantic amendments remain with the existing `designs/` corpus unless governance changes deliberately.

---

## 5. Recommended Mechanical Checks

When repository automation is implemented, use high-confidence checks only:

```text
ERROR
    required canonical governance file missing
    duplicate ACCEPTED Document ID + Version
    accepted MS-PROT absent from AUTHORITY-INDEX
    nonexistent normative dependency
    competing current governance companion file

WARNING
    PROPOSED and ACCEPTED documents share stable ID and ambiguous filenames
    obvious zero-byte document under governed documentation tree
    accepted/lifecycle metadata missing on a newly created authority
    high-risk canonical term used ambiguously
```

Do not make a linter infer semantic equivalence or choose an authority from filename ordering.

---

## 6. Current Result

```text
empty PRD placeholder                REMOVED
substantive historical evidence      PRESERVED
current authority navigation         IMPROVED through AUTHORITY-INDEX v3.9
mass directory restructure           REJECTED
legacy ADR-005                        PRESERVED + explicitly quarantined from current Order semantics
filename typos/overlapping PRDs       RECORDED for future product-doc consolidation
```

> **Repository hygiene now reduces proven navigation noise without rewriting history or creating a second authority system.**
