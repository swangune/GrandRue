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

---

## GR-REN-01A-02 — Production-source lexical inventory

**Inspection baseline:** `c8c955db7702c1886a2cbb353a95faa023bed510`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Scope:** `src/main/**`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Scope and method

The current `development` tree contains exactly two direct `src/main/` source-set roots:

- `src/main/java/**`
- `src/main/resources/**`

The production Java source tree is rooted at `src/main/java/mainstreet/**`. The scan combined:

- direct current-`development` tree inspection;
- repository-index search constrained to `src/main` to establish occurrence sets and zero-result forms;
- comparison of `master` to `development` to check whether indexed source content had materially diverged; and
- direct `development` fetches for case-sensitive or compatibility-sensitive candidate occurrences.

Repository-index counts below are search evidence, not semantic classification and not mutation authority.

### Search forms

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

### Matches

#### `mainstreet`

- `src/main/java/mainstreet/**`
  - the production Java filesystem namespace itself contains `mainstreet`.
  - package declarations and imports throughout the production source tree use the `mainstreet` namespace.
  - repository-index search constrained to `src/main` returned 1,218 files containing the case-insensitive lexical form; direct current-branch tree inspection confirms the Java namespace root remains `src/main/java/mainstreet/**`.
- `src/main/resources/application-prototype.properties`
  - PostgreSQL default URL/username/password values contain lowercase `mainstreet`.
- `src/main/java/mainstreet/infrastructure/persistence/money/JooqPaymentAuthorityStore.java`
  - physical persistence-column string `mainstreet_correlation_identifier` contains lowercase `mainstreet`.
- `src/main/resources/db/migration/V24__money__create_payment_authority.sql`
  - immutable Flyway migration contains physical column name `mainstreet_correlation_identifier`; this entry is inventory evidence only and does not authorise modification of the applied migration.

#### `mainstreet.*`

- `src/main/java/mainstreet/**`
  - package declarations/imports use namespace-style forms beginning `mainstreet.` across the production tree.
  - repository-index search constrained to `src/main` returned 1,210 files matching the namespace-style `mainstreet.` form.

#### `Main Street`

Exact product-name prose occurs in production Java comments/documentation. The indexed `src/main` candidate set contains 37 files. Confirmed examples include:

- `src/main/java/mainstreet/media/MediaAsset.java`
- `src/main/java/mainstreet/money/PaymentObligation.java`
- `src/main/java/mainstreet/money/PaymentApplication.java`
- `src/main/java/mainstreet/privacy/DataSubjectReference.java`
- `src/main/java/mainstreet/workforce/MerchantMembership.java`
- `src/main/java/mainstreet/money/ProviderPaymentEvidence.java`
- `src/main/java/mainstreet/merchantaccount/MerchantAccount.java`
- `src/main/java/mainstreet/workforce/MerchantRoleDefinition.java`
- `src/main/java/mainstreet/fulfilment/FulfilmentRoleIdentity.java`
- `src/main/java/mainstreet/surface/InitialProjectionContractPortfolio.java`

The full indexed candidate set is retained by the bounded query evidence; later classification determines which occurrences are current product wording versus stable or compatibility-sensitive identities.

#### `MAINSTREET`

Direct current-`development` inspection confirmed uppercase `MAINSTREET` identifiers in:

- `src/main/resources/application-prototype.properties`
  - `MAINSTREET_PROTOTYPE_POSTGRES_URL`
  - `MAINSTREET_PROTOTYPE_POSTGRES_USER`
  - `MAINSTREET_PROTOTYPE_POSTGRES_PASSWORD`
- `src/main/java/mainstreet/infrastructure/persistence/money/JooqPaymentAuthorityStore.java`
  - Java field constant `MAINSTREET_CORRELATION_IDENTIFIER`, mapped to lowercase persisted column name `mainstreet_correlation_identifier`.

A narrowed indexed `MAINSTREET_` candidate search returned three files; the third, `src/main/resources/db/migration/V24__money__create_payment_authority.sql`, contains only the lowercase persisted column spelling and therefore is not an uppercase `MAINSTREET` occurrence.

#### `main-street`

Direct current-`development` inspection confirmed the hyphenated form in exactly these indexed candidates:

- `src/main/java/mainstreet/surface/InitialProjectionContractPortfolio.java`
  - policy identifier `current-main-street-commitments`.
- `src/main/java/mainstreet/surface/CalendarProjectionPolicyEvaluators.java`
  - the same policy identifier `current-main-street-commitments`.

These are recorded without deciding whether the identifier is current product wording, stable contract identity or compatibility-sensitive persisted/external identity.

### Zero-result forms in this scope

Repository-index searches constrained to `src/main` returned no files for either underscore form:

- `MAIN_STREET`
- `Main_Street`

No rename or semantic classification was performed.

### Completion evidence

The current `src/main/**` tree and all required Section 4 lexical forms were inspected using the bounded methods above. Pervasive namespace/path forms, explicit current-product prose, uppercase runtime/persistence identifiers, hyphenated policy identifiers, immutable-migration occurrences and zero-result forms are recorded without mutation or semantic disposition.

---

## GR-REN-01A-03 — Test-source lexical inventory

**Inspection baseline:** `fbaa1571dbdf38c29a025b43bea934daa0f22719`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Scope:** `src/test/**`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Scope and method

The current `development` tree contains exactly two direct `src/test/` source-set roots:

- `src/test/java/**`
- `src/test/resources/**`

The Java test tree is rooted at `src/test/java/mainstreet/**`. The scan combined direct current-branch tree inspection, repository-index searches constrained to `src/test`, and direct `development` fetches for case-sensitive candidates. The test-resource tree contains one SQL fixture, `src/test/resources/db/persistence-foundation/V1__create_persistence_foundation_probe.sql`, and that file contains none of the required naming forms.

The default-branch index was used only as occurrence-set evidence. Branch divergence was checked separately: the changed test surface was inspected directly on `development`, including `src/test/java/mainstreet/booking/AppointmentOperationalObjectOwnershipTest.java`.

### Search forms

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

### Matches

#### `mainstreet`

- `src/test/java/mainstreet/**`
  - the test filesystem namespace itself contains `mainstreet`.
  - package declarations and imports throughout the Java test tree use the `mainstreet` namespace.
  - repository-index search constrained to `src/test` returned 426 files containing the lexical form.

#### `mainstreet.*`

- `src/test/java/mainstreet/**`
  - package declarations/imports use namespace-style forms beginning `mainstreet.` across the test tree.
  - repository-index search constrained to `src/test` returned 422 files matching the namespace-style `mainstreet.` form.

#### `Main Street`

Exact product-name prose occurs in test comments and descriptive wording. The indexed candidate set contains 10 files. Confirmed examples include:

- `src/test/java/mainstreet/infrastructure/security/webauthn/SpringWebAuthnSessionBridgeTest.java`
  - comment states that Spring authorities are ignored by Main Street Session authority.
- test classes whose names/comments describe Main Street-owned identity, session or capability behaviour.

These occurrences remain unclassified until the later classification gate.

#### `MAINSTREET`

Uppercase test/runtime environment-variable names are widespread in PostgreSQL integration tests:

- `MAINSTREET_TEST_POSTGRES_URL`
- `MAINSTREET_TEST_POSTGRES_USER`
- `MAINSTREET_TEST_POSTGRES_PASSWORD`

Repository-index search for `MAINSTREET_TEST_POSTGRES_URL` returned 79 test files. Direct current-`development` examples include:

- `src/test/java/mainstreet/infrastructure/persistence/audit/JooqAuditStoreIT.java`
- `src/test/java/mainstreet/infrastructure/persistence/webauthn/JooqWebAuthnAuthenticationSubjectRepositoryIT.java`
- `src/test/java/mainstreet/surface/MerchantEnquiryQueryT3IT.java`

### Underscore candidate inspection

GitHub code search is case-insensitive, so searches for `MAIN_STREET` and `Main_Street` surfaced six lowercase `main_street` test-method names. Direct current-branch inspection confirmed these are lowercase descriptive method identifiers, not occurrences of either required uppercase/title-case form. Confirmed candidates include:

- `src/test/java/mainstreet/merchantaccount/MerchantAccountTest.java` — `exposes_the_immutable_main_street_merchant_identity`
- `src/test/java/mainstreet/workforce/MerchantWorkforceModelTest.java` — `role_definition_accepts_only_privileges_registered_by_main_street_semantics`
- `src/test/java/mainstreet/infrastructure/persistence/audit/JooqAuditStoreIT.java` — `platform_and_merchant_evidence_remain_distinct_without_fake_main_street_merchant`
- `src/test/java/mainstreet/infrastructure/security/webauthn/SpringWebAuthnSessionBridgeTest.java` — `verified_webauthn_establishes_a_fresh_main_street_session_from_authoritative_identity_mapping` and `repeated_successful_webauthn_proofs_create_independent_main_street_sessions`
- `src/test/java/mainstreet/infrastructure/security/session/PrivilegedSessionRequestResolverTest.java` — `exact_privileged_cookie_is_resolved_through_main_street_session_authority`
- `src/test/java/mainstreet/infrastructure/persistence/webauthn/JooqWebAuthnAuthenticationSubjectRepositoryIT.java` — `main_street_establishes_one_opaque_subject_handle_for_one_identity`

### Zero-result forms in this scope

After direct case-sensitive inspection of the indexed candidates, no exact occurrences were found for:

- `MAIN_STREET`
- `main-street`
- `Main_Street`

The single test-resource SQL fixture also contains none of the required naming forms.

No rename or semantic classification was performed.

### Completion evidence

The complete `src/test/**` surface was accounted for through its Java and resource roots; all Section 4 lexical forms were searched; namespace, prose and uppercase environment-variable occurrences were recorded; case-insensitive underscore false positives were resolved by direct current-branch inspection; zero-result forms were recorded; and no mutation or semantic classification was performed.

---

## GR-REN-01A-04 — Storefront lexical inventory

**Inspection baseline:** `f711bda24417140b3941b2a3412e494492f15518`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Scope:** `storefront-web/**`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Scope and method

The complete `storefront-web/**` tree is byte-identical between `master` and `development` at the time of inspection, so the repository code index is branch-safe for this leaf. The indexed candidate set was then verified directly on current `development` for the material matches.

### Search forms

```text
mainstreet
mainstreet.*
Main Street
MAIN_STREET
MAINSTREET
main-street
Main_Street
```

### Matches

#### `mainstreet`

Exactly three files in the storefront scope contain the lexical form:

- `storefront-web/package.json`
  - npm package name: `mainstreet-storefront-web`.
- `storefront-web/package-lock.json`
  - root package name and package metadata repeat `mainstreet-storefront-web`.
- `storefront-web/src/lib/grandrue-api.ts`
  - environment-variable reference `MAINSTREET_BACKEND_URL` contains the form case-insensitively.

#### `Main Street`

- `storefront-web/app/layout.tsx`
  - metadata description is `Main Street capability-driven storefront prototype`.

#### `MAINSTREET`

- `storefront-web/src/lib/grandrue-api.ts`
  - runtime environment-variable name `MAINSTREET_BACKEND_URL`.

### Zero-result forms in this scope

No occurrences were found for:

- `mainstreet.*`
- `MAIN_STREET`
- `main-street`
- `Main_Street`

No rename or semantic classification was performed.

### Completion evidence

The complete `storefront-web/**` tree was searched for every Section 4 lexical form. All observed package, runtime environment-variable and current metadata occurrences are recorded above; zero-result forms are recorded; and no mutation or semantic classification was performed.
