# GrandRue Naming Migration Inventory Evidence

**Status:** NON-AUTHORITATIVE OPERATIONAL MIGRATION EVIDENCE  
**Governing ledger:** `GRANDRUE-MIGRATION.md`  
**Purpose:** Record bounded lexical-inventory evidence for executable migration leaves without creating semantic, architecture, programme or implementation authority.

This file records inspection evidence only. A recorded occurrence is **not** a rename decision or semantic classification.

---

## GR-REN-01A-01 — Root operational/build lexical inventory

**Inspection baseline:** `20b2b908fc43bdab91f6786f1780acde40a094eb`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Scope inspected

Every file in the exact ledger scope was inspected:

- `.github/workflows/maven-tests.yml`
- `.github/workflows/storefront-web-tests.yml`
- `build_configuration/package_boundary.md`
- `tools/DesignCorpusCheck.java`
- `.gitignore`
- `compose.prototype.yml`
- `lifecycle.md`
- `operational-rules.md`
- `pom.xml`
- `workflow-tree.md`

### Search forms

Searches were case-sensitive for the required forms:

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

For inventory purposes, `mainstreet.*` records lexical namespace-style occurrences beginning with `mainstreet.`; it is not treated as a literal asterisk-bearing string.

### Matches

#### `mainstreet`

- `.github/workflows/maven-tests.yml`
  - PostgreSQL database/user/password values and health-check arguments contain `mainstreet` / `mainstreet_test`.
  - JDBC URL and corresponding test database credentials contain `mainstreet` / `mainstreet_test`.
- `build_configuration/package_boundary.md`
  - current Java package/FQCN navigation contains multiple `mainstreet...` occurrences.
- `compose.prototype.yml`
  - PostgreSQL database/user/password, health-check arguments and named volume contain `mainstreet`.
- `pom.xml`
  - project `groupId` and `artifactId` are `mainstreet`.

#### `mainstreet.*`

- `build_configuration/package_boundary.md`
  - Java package/FQCN navigation includes namespace-style prefixes such as `mainstreet.semantic`, `mainstreet.runtime`, `mainstreet.infrastructure.persistence.*` and related `mainstreet.` package references.

#### `Main Street`

- `build_configuration/package_boundary.md`
  - document title contains `Main Street`.
- `lifecycle.md`
  - document title contains `Main Street`.
- `operational-rules.md`
  - title/body contain `Main Street`.
- `workflow-tree.md`
  - document title and product-workflow tree contain `Main Street`.

#### `MAINSTREET`

- `.github/workflows/maven-tests.yml`
  - test PostgreSQL environment-variable names use the `MAINSTREET_...` prefix.

### Zero-result forms in this scope

No occurrences were found for:

- `MAIN_STREET`
- `main-street`
- `Main_Street`

### Files with zero occurrences of every required form

- `.github/workflows/storefront-web-tests.yml`
- `tools/DesignCorpusCheck.java`
- `.gitignore`

### Completion evidence

All paths in `GR-REN-01A-01` were inspected for every required Section 4 naming form. Repository locations of all observed forms are recorded above, zero-result forms are recorded, and no rename or semantic classification was performed.
