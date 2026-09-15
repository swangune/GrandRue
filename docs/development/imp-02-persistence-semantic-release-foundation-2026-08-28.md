# IMP-02 — Persistence & Semantic Release Foundation Closure Evidence

**Programme:** MS-IMP-001 v1.0  
**Execution rules:** IMPLEMENTATION-RULES.md v1.3  
**Date:** 28 August 2026  
**Status:** CONFORMING_COMPLETE  
**Validated implementation head:** `5ba4946a00f4e7ce7dd7307ae022a3965211b514`  
**GitHub Actions run:** 1099 (`33130907687`) — SUCCESS  

---

## 1. Scope

MS-IMP-001 requires IMP-02 to establish the reusable persistence and semantic-release foundation for later production implementation:

- PostgreSQL production persistence foundation;
- Flyway/migration baseline;
- transaction support;
- concurrency/version primitives;
- exact semantic-definition bundles;
- Published Semantic Definition evidence;
- SemanticReleaseAssembly persistence/materialisation support;
- packaged release integrity/provenance; and
- exact-release bootstrap.

Completion additionally requires executable proof of the distinctions:

```text
exact release
    ≠ latest release

schema state
    ≠ semantic release

materialisable
    ≠ executable
```

This document records implementation evidence only. It does not create semantic, architectural, deployment or business authority.

---

## 2. Verified Full Gate

The exact implementation head `5ba4946a00f4e7ce7dd7307ae022a3965211b514` was executed by GitHub Actions run 1099 using the repository's production-representative Maven/PostgreSQL verification path.

The unit and PostgreSQL integration-test step completed successfully and the workflow concluded:

```text
status:      completed
conclusion:  success
head:        5ba4946a00f4e7ce7dd7307ae022a3965211b514
run:         33130907687
```

IMP-01 remains the verified reusable engineering gate for Java 25, PostgreSQL 18.6, Flyway, jOOQ, Spring transaction support and the complete migration chain.

---

## 3. Existing Persistence Foundation Reused

IMP-02 did not introduce a second persistence architecture.

Existing conforming implementation already provides:

```text
PostgreSQL production persistence
Flyway production migrations
jOOQ SQL access
Spring transaction management
PostgreSQL integration tests
full-chain migration verification
```

Existing production configuration activation also demonstrates the required transaction/concurrency foundation through:

```text
TransactionTemplate
    ↓
transaction-scoped activation

PostgreSQL pg_advisory_xact_lock
    ↓
logical-request and merchant-activation serialisation

current configuration pointer
    ↓
compare-and-set update against expected revision
```

The current pointer update rejects a concurrent change when exactly one expected row is not updated. Exact persisted release identifiers are resolved without latest-release substitution.

No new database, ORM, migration framework, distributed lock service or generic UnitOfWork framework was added.

---

## 4. Published Semantic Definition Evidence

ADR-011 requires one coherent immutable Published Semantic Definition Set/evidence for each exact published Semantic Registry Release.

IMP-02 adds:

```text
PublishedSemanticDefinitionSet
```

The value object preserves:

```text
exact releaseIdentifier
publicationProvenance
semantic definition bytes
surface definition bytes
fulfilment definition bytes
```

Definition bytes are defensively copied so the evidence object is immutable after construction.

Construction represents the trusted publication/build input boundary. The value object does not claim that a digest independently authenticates the producer of deployment artefacts; deployment supply-chain authenticity remains outside the content-integrity mechanism as required by ADR-013.

---

## 5. Deterministic Trusted-Build Packaging

IMP-02 adds:

```text
SemanticDefinitionBundlePackager
        ↓
PackagedSemanticDefinitionBundle
```

The packager accepts `PublishedSemanticDefinitionSet` evidence and preserves its exact release identity and publication provenance. It does not relabel current definitions as historical releases and does not manufacture a `latest`, `current`, `default` or `production` semantic identity.

`PackagedSemanticDefinitionBundle` contains:

```text
formatVersion
exact releaseIdentifier
publicationProvenance
SHA-256 contentDigest
opaque semanticDefinitions
opaque surfaceDefinitions
opaque fulfilmentDefinitions
```

The digest is calculated deterministically over length-prefixed envelope identity/provenance and the exact definition bytes, avoiding ambiguous concatenation.

A content mismatch fails closed as:

```text
CONTENT_INTEGRITY_FAILURE
```

The definition sections remain opaque at the packaging/materialisation boundary so Semantic, Surface and Fulfilment registries retain ownership of their own definition languages.

---

## 6. Deployment Semantic Materialisation Set

IMP-02 adds:

```text
DeploymentSemanticMaterialisationSet
```

It is an immutable exact set of bundles assigned to one deployment.

It rejects two bundles claiming the same exact Semantic Registry Release as:

```text
DUPLICATE_RELEASE_IDENTITY
```

It also exposes deterministic serving-cohort content identity as:

```text
releaseIdentifier → immutable contentDigest
```

The map is order-independent and digest-sensitive, providing the implementation evidence needed to compare the ADR-013 initial uniform serving-cohort materialisation set without introducing business state or executable-support state.

---

## 7. Registry-Owned Definition Decoding

IMP-02 adds the narrow port:

```text
SemanticDefinitionSectionDecoder<T>
```

and composes three owner-supplied decoders in:

```text
SemanticReleaseMaterialiser
```

This deliberately does not create a universal semantic-definition parser. Each registry remains responsible for interpreting its own immutable definition material.

Concrete evolution of Semantic Registry / Capability Registry / compiler / runtime execution belongs to IMP-03 and is not pulled backward into IMP-02 merely to make materialisation concrete.

A decoder failure or null result fails closed as:

```text
DEFINITION_DECODE_FAILURE
```

---

## 8. Exact SemanticReleaseAssembly Materialisation

For every assigned bundle, `SemanticReleaseMaterialiser` performs:

```text
validate bundle format
        ↓
validate content integrity
        ↓
decode semantic section
        ↓
decode surface section
        ↓
decode fulfilment section
        ↓
prove every decoded registry has exact bundle release identity
        ↓
construct SemanticReleaseAssembly
```

Failure classes are explicit:

```text
UNSUPPORTED_BUNDLE_FORMAT
CONTENT_INTEGRITY_FAILURE
RELEASE_IDENTITY_MISMATCH
DEFINITION_DECODE_FAILURE
ASSEMBLY_COHERENCE_FAILURE
```

All bundles are materialised before the immutable `InMemorySemanticReleaseAssemblyRepository` is returned. A failure in any assigned bundle therefore prevents partial repository publication.

Existing `SemanticReleaseAssembly` remains the coherence authority for release-affined Semantic, Surface and Fulfilment registry snapshots.

---

## 9. Deterministic Packaged Resource Bootstrap

IMP-02 adds:

```text
PackagedSemanticDefinitionBundleTextCodec
ClasspathSemanticDefinitionBundleLoader
SemanticReleaseBootstrap
```

The text codec provides a deterministic UTF-8 resource envelope. Release identity, provenance and definition sections are encoded explicitly; malformed, incomplete or unrecognisable bundle resources fail as:

```text
UNSUPPORTED_BUNDLE_FORMAT
```

The classpath loader loads only caller-declared exact resource paths.

It deliberately performs no:

```text
classpath release scanning
latest-release selection
current-release aliasing
nearest-release lookup
fallback-release selection
```

`SemanticReleaseBootstrap` then materialises the complete deployment set and proves all exact release identifiers required by the serving cohort are present before returning the repository.

Missing exact coverage fails as:

```text
REQUIRED_RELEASE_NOT_MATERIALISED
```

Configuration activation itself remains outside this bootstrap owner. Later Merchant Configuration implementation may consume this exact support boundary without transferring configuration activation authority into semantic materialisation.

---

## 10. Exact Release ≠ Latest Release

Executable conformance evidence protects exact-only lookup.

`SemanticReleaseAssemblyRepository` exposes only:

```text
release(exactReleaseIdentifier)
```

There is no repository API for:

```text
latest
current
nearest
fallback
compatible-enough
previous
default
```

Tests prove multiple releases can coexist and an absent release remains absent rather than resolving to another release.

**Verdict:** PASS.

---

## 11. Schema State ≠ Semantic Release

`SemanticReleaseBoundaryConformanceTest` prevents the semantic-release materialisation package from depending on infrastructure/Flyway/schema-version state.

The protected boundary excludes, among other things:

```text
mainstreet.infrastructure.*
org.flywaydb.*
flyway_schema_history
schemaVersion
migrationVersion
```

Database migration state may determine whether a deployment can persist its database model; it does not create or identify a Semantic Registry Release.

**Verdict:** PASS.

---

## 12. Materialisable ≠ Executable

`SemanticReleaseBoundaryConformanceTest` prevents the semantic-release materialisation package from importing runtime/executable/configuration authority.

The protected boundary excludes direct dependency on:

```text
mainstreet.semantic.executable.*
mainstreet.runtime.*
mainstreet.semantic.configuration.*
```

Therefore successful materialisation establishes only:

```text
exact immutable semantic definition material is available
```

It does not establish:

```text
operation is executable
actor is authorised
merchant is entitled
provider is ready
operation is operationally eligible
```

Contract-scoped executable support remains ADR-012 / IMP-03 responsibility.

**Verdict:** PASS.

---

## 13. Failure Classification

IMP-02 implements ADR-013 materialisation failure classes through:

```text
SemanticMaterialisationFailure
SemanticMaterialisationException
```

Implemented classes:

```text
UNSUPPORTED_BUNDLE_FORMAT
CONTENT_INTEGRITY_FAILURE
RELEASE_IDENTITY_MISMATCH
DUPLICATE_RELEASE_IDENTITY
DEFINITION_DECODE_FAILURE
ASSEMBLY_COHERENCE_FAILURE
REQUIRED_RELEASE_NOT_MATERIALISED
```

`EXECUTION_CONTRACT_UNSUPPORTED` is intentionally absent because it belongs to ADR-012 executable-support authority, not semantic materialisation.

---

## 14. Authority-to-Test Traceability

| Accepted authority / rule | Executable evidence |
|---|---|
| ADR-010 coherent exact release assembly | `SemanticReleaseAssemblyTest`, materialiser tests |
| ADR-011 immutable Published Semantic Definition evidence | `PublishedSemanticDefinitionSetTest` |
| ADR-011 exact resolution / no latest substitution | `SemanticReleaseAssemblyTest`, `SemanticDefinitionBundleMaterialisationTest`, boundary conformance |
| ADR-013 bundle integrity/provenance | bundle materialisation and publication-evidence tests |
| ADR-013 deterministic packaging/bootstrap | `SemanticReleaseBootstrapTest` |
| ADR-013 duplicate release rejection | `SemanticMaterialisationFailureTest` |
| ADR-013 explicit failure classification | `SemanticMaterialisationFailureTest`, bootstrap tests |
| ADR-013 serving-cohort content identity | `SemanticMaterialisationFailureTest` |
| ADR-013 all-or-nothing materialisation | `SemanticDefinitionBundleMaterialisationTest` |
| ADR-012 materialisable ≠ executable | `SemanticReleaseBoundaryConformanceTest` |
| MS-IMP-001 schema state ≠ semantic release | `SemanticReleaseBoundaryConformanceTest` |
| MS-IMP-001 persistence/migration/transaction foundation | IMP-01 PostgreSQL/Flyway gate + existing production transaction/concurrency implementation |

---

## 15. Dependency and Architecture Review

IMP-02 introduced no new Maven dependency and no new infrastructure technology.

Specifically, it did not add:

- another database;
- another migration framework;
- JSON/YAML serialization dependency;
- Java object serialization as semantic storage;
- a semantic-registry network service;
- a message broker;
- a distributed cache;
- a generic plugin framework;
- compatibility-aware routing;
- a release-level executable-support boolean; or
- a second semantic release lifecycle.

The physical resource envelope remains replaceable implementation detail behind the accepted exact identity/integrity/provenance invariants.

---

## 16. Downstream Handoffs

IMP-02 intentionally does not absorb responsibilities scheduled later by MS-IMP-001.

### IMP-03 — Semantic Execution Spine

Owns implementation completion/proof for:

```text
Semantic registries
Capability Registry
Capability composition
Configuration Compiler
Resolved Configuration Package
Operation Runtime
exact semantic/configuration execution affinity
registered operation execution
ADR-012 contract-scoped executable support
```

### IMP-05 — Merchant Definition, Configuration & Activation

Owns later consumption of exact deployment semantic availability during configuration bootstrap/activation. IMP-02 does not mutate Merchant Configuration or decide activation.

### Later deployment/recovery targets

May reuse exact release materialisation/content identity without redefining semantic meaning.

---

## 17. Closure Verdict

```text
IMP-02
Persistence & Semantic Release Foundation

PostgreSQL production persistence foundation      PASS
Flyway / migration baseline                       PASS
transaction support                               PASS
concurrency / version primitives                  PASS
exact semantic-definition bundle                  PASS
Published Semantic Definition evidence            PASS
SemanticReleaseAssembly materialisation           PASS
packaged release integrity / provenance           PASS
exact-release bootstrap                           PASS
exact release != latest release                   PASS
schema state != semantic release                  PASS
materialisable != executable                      PASS
full production-representative gate               PASS

VERDICT: CONFORMING_COMPLETE
```

No unresolved material design or architecture question was encountered by IMP-02.

Under MS-IMP-001 dependency governance:

```text
IMP-00 COMPLETE
    ↓
IMP-01 COMPLETE
    ↓
IMP-02 COMPLETE
    ├──► IMP-03 READY
    └──► IMP-04 READY
```

The implementation loop may therefore continue automatically into the next eligible READY macro target without manual approval.