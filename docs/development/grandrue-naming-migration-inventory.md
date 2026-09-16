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

---

## GR-REN-01A-05 — Root governance/navigation lexical inventory

**Inspection baseline:** `9b9843a0df3ef7ccc9da985c558ca037f83fd54e`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Scope:** `AGENTS.md`, `README`, `SEQUENCE.md`, `GRANDRUE-MIGRATION.md`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Matches by file

- `AGENTS.md`
  - `Main Street` occurs in the title and current operational/governance prose.
  - no lowercase `mainstreet`, namespace-style `mainstreet.`, `MAIN_STREET`, `MAINSTREET`, `main-street`, or `Main_Street` occurrence was found.
- `README`
  - `mainstreet` and `mainstreet.*` occur in current Maven coordinates, Java package/FQCN references and repository paths.
  - `MAINSTREET` occurs in the documented PostgreSQL test environment-variable names.
  - `Main Street` occurs in the repository/product-identity section as an explicitly historical/provenance reference.
  - no exact `MAIN_STREET`, `main-street`, or `Main_Street` occurrence was found.
- `SEQUENCE.md`
  - `Main Street` occurs throughout the design-dependency/provenance graph.
  - `Main_Street` occurs in the source handoff filename.
  - `MAIN_STREET` occurs inside an accepted/stable identifier referenced by the graph, including `HANDLED_OUTSIDE_MAIN_STREET_RECORDED`.
  - no lowercase `mainstreet`, namespace-style `mainstreet.`, `MAINSTREET`, or `main-street` occurrence was found.
- `GRANDRUE-MIGRATION.md`
  - all seven required search forms occur deliberately as migration vocabulary, examples, historical source-name notation or protected compatibility references.
  - these self-referential occurrences are inventory mechanics, not rename authority.

### Completion evidence

All four root governance/navigation files were inspected for every required lexical form. Current governance wording, current runtime/package/env references, historical/provenance references, stable referenced identifiers and self-referential migration vocabulary are recorded without semantic disposition or mutation.

---

## GR-REN-01A-06 — Current design-governance lexical inventory

**Inspection baseline:** `af555d420ef6da9d1dd53fdb5f02820c8266b6e1`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Scope:** only the six governance files explicitly listed by the migration contract  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Scope inspected

- `designs/DESIGN-RULES.md`
- `designs/DOCUMENT-GOVERNANCE.md`
- `designs/AUTHORITY-INDEX.md`
- `designs/DEFERRED-DECISION-REGISTER.md`
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
- `designs/IMPLEMENTATION-RULES.md`

No referenced non-governance design file was traversed for this task.

### Matches

- `designs/DESIGN-RULES.md`
  - extensive current `Main Street` governance/product-purpose wording.
  - no lowercase `mainstreet` or `MAIN_STREET` occurrence was found.
- `designs/DOCUMENT-GOVERNANCE.md`
  - current `Main Street` governance wording throughout.
  - no lowercase `mainstreet` or `MAIN_STREET` occurrence was found.
- `designs/AUTHORITY-INDEX.md`
  - current product identity is already stated as GrandRue.
  - `Main Street` remains as legacy product-name/authority wording.
  - `mainstreet.*` is explicitly recorded as an implementation/package identifier awaiting separately governed migration.
  - no `MAIN_STREET` occurrence was found.
- `designs/DEFERRED-DECISION-REGISTER.md`
  - current `Main Street` governance/deferred-decision wording.
  - protected accepted identifier `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` contributes the `MAIN_STREET` form.
  - no lowercase `mainstreet` occurrence was found.
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
  - current `Main Street` governance/conformance wording.
  - no lowercase `mainstreet` or `MAIN_STREET` occurrence was found.
- `designs/IMPLEMENTATION-RULES.md`
  - current `Main Street` implementation-governance wording.
  - no lowercase `mainstreet` or `MAIN_STREET` occurrence was found.

### Residual separator/case check

Targeted repository-index checks for `MAINSTREET`, `main-street` and `Main_Street` under `designs/` returned matches only in excluded non-governance design material; none of the six in-scope governance files introduced an additional target for those forms.

### Completion evidence

All six and only the six in-scope `designs/**` governance files were inspected for the required naming forms. Ordinary current governance wording, the explicit legacy package reference in the Authority Index and the protected DDR contract identifier are recorded. Excluded design material was not traversed, renamed or classified.

---

## GR-REN-01A-06E — Current build/test/run essential-path manifest

**Inspection baseline:** `7cf719855bba0d55b4c15db7afe1dc14829337d1`  
**Parent:** `GR-REN-01A`  
**Kind:** `GATE`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

`GR-REN-01A-06` was already used by commit `1aab28d5d8b42746eacad2bf694d762e3d584023` under the superseded pre-minimisation task model for the six-file governance lexical inventory above. Historical evidence is not rewritten. The current essentiality gate therefore uses `GR-REN-01A-06E` to keep task identity unambiguous.

### Dependency evidence used

- The backend standard verification command is `mvn --batch-mode clean verify -Ppostgres-it`.
- Root `pom.xml` directly owns Maven coordinates, dependencies, Spring Boot packaging, unit-test execution and the `postgres-it` Failsafe profile. It does not invoke `compose.prototype.yml`, `build_configuration/**`, `tools/**`, root documentation, or GitHub workflow files.
- PostgreSQL integration tests obtain connection details from process environment variables; an external PostgreSQL instance can satisfy them without `compose.prototype.yml`.
- `storefront-web/package.json` defines the active `dev`, `build`, `start`, and `test` commands.
- The currently recorded storefront automation executes `npm install`, `npm test`, and `npm run build`. `npm install` consumes the package manifest and lockfile when the lockfile is present.
- Next.js build/start uses `storefront-web/next.config.ts`; the TypeScript build configuration is `storefront-web/tsconfig.json`, which explicitly includes `next-env.d.ts`.
- GitHub Actions remain unauthorised for this migration. Workflow files were read only as dependency evidence and are not part of the authorised local build/test/run execution path.

### ESSENTIAL repository files/paths

| Path | Essentiality evidence | Migration consequence |
|---|---|---|
| `pom.xml` | Maven build/test/package/profile entry point | In scope; its current `mainstreet` Maven coordinates must later be classified. |
| `src/main/**` | Maven production source/resource set | Already in scope from `GR-REN-01A-02`; includes runtime properties and Flyway resources subject to protected-migration rules. |
| `src/test/**` | Maven unit/integration test source/resource set | Already in scope from `GR-REN-01A-03`; includes PostgreSQL environment-variable consumers. |
| `storefront-web/package.json` | npm command/dependency manifest for dev/test/build/start | In scope; package name is a current naming occurrence. |
| `storefront-web/package-lock.json` | consumed by npm dependency installation and must remain coherent with the package manifest | In scope; root package name must track any approved package-name migration. |
| `storefront-web/next.config.ts` | loaded by Next.js build/start; `output: "standalone"` affects runtime packaging | Essential path, but current inventory records no legacy naming occurrence; no naming action unless a later dependency change introduces one. |
| `storefront-web/tsconfig.json` | TypeScript/Next build configuration | Essential path, but current inventory records no legacy naming occurrence. |
| `storefront-web/next-env.d.ts` | included by `tsconfig.json` as a current type/build input | Essential path, but contains no legacy naming occurrence and is not to be edited merely for migration. |
| `storefront-web/app/**` and `storefront-web/src/**` | current storefront runtime/application source | Already in scope from `GR-REN-01A-04`; only recorded naming occurrences require later classification. |

### REFERENCE_ONLY repository files/paths

| Path | Reason |
|---|---|
| `.github/workflows/maven-tests.yml` | CI orchestration only; not consumed by Maven and GitHub Actions are not authorised for this migration. Revisit only if Actions becomes an authorised/current verification path. |
| `.github/workflows/storefront-web-tests.yml` | CI orchestration only; not required by local npm/Next execution and Actions are not authorised. |
| `compose.prototype.yml` | optional PostgreSQL convenience environment; neither Maven nor the storefront commands invoke it. External PostgreSQL supplied through environment variables satisfies the integration-test dependency. |
| `build_configuration/package_boundary.md` | documentation/navigation only; no build/test/runtime consumer found. |
| `tools/DesignCorpusCheck.java` | design-corpus governance/conformance utility, not a dependency of the current Maven or storefront run/test commands. |
| `.gitignore` | repository hygiene only; no naming occurrence and no runtime/test dependency. |
| `README*` | documentation only; not required to build/test/run GrandRue. |
| `lifecycle.md` | historical/operational documentation; not consumed by build/test/run. |
| `operational-rules.md` | root operational documentation; current repository execution is governed through `AGENTS.md` and canonical governance, not this file as a runtime dependency. |
| `workflow-tree.md` | documentation/navigation only; not consumed by build/test/run. |
| other unproven scripts/tools/infrastructure files | remain reference-only until concrete dependency evidence demonstrates current build/test/run consumption. |

### Standing non-runtime exceptions

The essentiality classifications above do not remove the explicit migration exceptions established by the ledger:

- `AGENTS.md`;
- `SEQUENCE.md`;
- `GRANDRUE-MIGRATION.md`;
- the seven canonical current governance files in the root of `designs/`.

These remain in scope because they govern current GrandRue repository work, not because Maven, npm, Next.js, or the runtime consumes them.

### Gate result

`GR-REN-01A-06E` is satisfied. The minimal non-source executable surface is now bounded to `pom.xml` plus the active storefront package/build configuration files listed as `ESSENTIAL`; source/resource trees remain in their already-established scopes. All other discovered root/build/infrastructure paths remain reference-only unless later dependency evidence proves otherwise.

No naming mutation or semantic classification was performed.

---

## GR-REN-01A-07 — Seven-file canonical governance lexical inventory

**Inspection baseline:** `6e23bd1b022f3c8beb50b96cb9df123ff8dc484f`  
**Parent:** `GR-REN-01A`  
**Kind:** `TASK`  
**Rename performed:** `false`  
**Semantic classification performed:** `false`

### Reused unchanged evidence

The six files inspected by the historical `GR-REN-01A-06` evidence remain byte-identical on current `development`; their current blob SHAs match the previously inspected blobs. Their prior lexical evidence is therefore reused without rescanning:

- `designs/DESIGN-RULES.md` — `5aed8d791caa2fbb991f91b41ef03790ffc6577d`
- `designs/DOCUMENT-GOVERNANCE.md` — `7b333f8b65064bf6db00df33ca6754de2d38457e`
- `designs/AUTHORITY-INDEX.md` — `4891c44b3f8d60e049d263ae385fc488fd06ae0b`
- `designs/DEFERRED-DECISION-REGISTER.md` — `1e575e90a9f7e9ac457549ef7a87cdd6d3f65a52`
- `designs/DESIGN-CORPUS-CONFORMANCE.md` — `cc64221aa3597feba7fe70a066477f4f2139d6b7`
- `designs/IMPLEMENTATION-RULES.md` — `b385e12d33680acc57ed628f69bc2e4e48ea9d50`

No non-governance design file was traversed.

### Newly inspected seventh governance file

`designs/CANONICAL-SEMANTIC-LEXICON.md` — blob `c1d9ca401bdd1642e8cd478c4dc6e96df612ca37`

Required-form results:

- `Main Street` — present throughout current canonical terminology/governance prose, including the document title, purpose, policy/configuration terminology and owner-qualified platform concepts.
- `mainstreet` — no occurrence.
- `mainstreet.*` — no namespace-style occurrence.
- `MAIN_STREET` — no occurrence.
- `MAINSTREET` — no occurrence.
- `main-street` — no occurrence.
- `Main_Street` — no occurrence.

Stable `MS-PROT-*` authority references are present throughout and remain protected authority identifiers; this lexical task makes no rename decision about them.

### Completion evidence

All seven canonical current governance files in the root of `designs/` are now covered by current evidence: six through verified unchanged-blob reuse and `CANONICAL-SEMANTIC-LEXICON.md` through direct current-branch inspection. The required naming forms are accounted for without traversing excluded design material, without semantic classification and without mutation.
