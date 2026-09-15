# ADR-011 — Semantic Release Publication, Retention & Bootstrap

> **ADR ID:** ADR-011  
> **Version:** 1.0  
> **Status:** Accepted  
> **Date:** 26 August 2026  
> **Owner:** Architecture Team  
> **Approved:** Manual approval on 26 August 2026 to carry out the final governed EAC-001 recommendation  
> **Depends on:** MS-PROT-022 v1.5, MS-PROT-040 v1.1, MS-PROT-054, ADR-010, MS-TAS-RECOVERY-001  
> **Closes:** EAC-001 — Semantic Release Publication, Retention & Bootstrap Architecture  
> **Purpose:** Define the implementation-architecture contract by which an accepted immutable Semantic Registry Release remains exactly reproducible or verifiable across publication, ordinary process restart and recovery without making application deployment, current registration code, mutable merchant state or a new distributed service implicit semantic authority.

---

## 1. Context

MS-PROT-054 establishes that every published Semantic Registry Release is immutable, that application deployment is not semantic migration authority, that historical contexts may remain required, and that compatibility/support is scoped rather than one release-global boolean.

MS-PROT-022 v1.5 and MS-PROT-040 v1.1 bind a Merchant Configuration Revision and its Resolved Configuration Package (RCP) to one exact Semantic Registry Release.

ADR-010 establishes one coherent immutable `SemanticReleaseAssembly` for release-affined static registries and exact lookup by Semantic Registry Release identifier, but deliberately leaves concrete publication, durable retention and ordinary bootstrap outside its scope.

MS-TAS-RECOVERY-001 requires recoverable semantic-release evidence for disaster recovery, but disaster-recovery packaging is not the ordinary publication/bootstrap authority.

The EAC-001 review and two falsification passes established that Main Street requires durable exact source-release resolution while rejecting three over-broad conclusions:

1. every historical/residual execution path does **not** require full source-release materialisation when an exact retained RCP and other accepted immutable execution evidence are sufficient;
2. one logical release does **not** require one physical file/blob/package; and
3. ordinary restart does **not** require eager loading of every historical release or recompilation of every active merchant configuration.

---

## 2. Problem Statement

How shall Main Street publish, retain and resolve a Semantic Registry Release so that an exact historical or current release can be reproduced or verified whenever a compiler, runtime, interpretation or recovery path actually requires source-release resolution, without silently reconstructing historical meaning from current definitions or introducing unnecessary infrastructure?

---

## 3. Scope

ADR-011 governs:

- coherent publication of registered static semantic definitions for one Semantic Registry Release;
- exact release-identity resolution after process restart;
- integrity and provenance of published release-definition evidence;
- retention obligations for source-release evidence;
- interaction with retained exact RCPs;
- withdrawal of an immutable release from new use without rewriting it;
- ordinary bootstrap and recovery reuse of the same release meaning.

---

## 4. Explicit Non-Goals

ADR-011 does **not** define:

- Merchant Configuration or RCP business semantics;
- semantic compatibility or migration dispositions owned by MS-PROT-054;
- executable runtime compatibility, governed by ADR-012;
- one required physical storage technology;
- one required packaging format;
- one required eager/lazy caching strategy;
- a dedicated semantic-registry network service;
- merchant operational state, Provider Readiness or credentials inside release content;
- automatic garbage collection of published releases; or
- a new semantic release lifecycle competing with MS-PROT-054.

---

## 5. Options Considered

### Option A — Reconstruct historical releases from current registration code

On restart, run the currently deployed registration code and label its output with whatever historical release identifier is requested.

**Rejected.** A deployment could change produced meaning while preserving the old identifier. Application deployment would become implicit reinterpretation authority.

### Option B — Mutable current semantic catalogue

Store definitions as ordinary mutable rows/state and treat their current contents as the meaning of a release identifier.

**Rejected as the governing model.** A database may physically store immutable release evidence, but ordinary mutable-row semantics must not permit `R8` to change meaning in place.

### Option C — Coherent immutable published definition set/evidence

Each published Semantic Registry Release has one logically coherent immutable definition set/evidence from which the registered static definitions required by a source-release-resolution path can be reproduced or verified. Physical representation remains replaceable.

**Accepted.**

### Option D — Dedicated semantic-registry service

Require a separate network service to own publication and lookup.

**Rejected for the initial architecture.** The modular monolith does not currently justify another distributed dependency. A future storage/distribution architecture may be reviewed without changing this ADR's invariants.

---

## 6. Decision

Main Street adopts **Option C: coherent immutable published Semantic Release definition set/evidence**.

For every published Semantic Registry Release `R`, Main Street MUST preserve one logical published definition set/evidence `D(R)` such that:

```text
release identity R
        +
D(R)
        ↓
reproduce or verify the registered static definitions
required by a source-release-resolution path for R
```

`D(R)` is a logical architectural artefact. ADR-011 does not require `D(R)` to be one file, one database row, one archive or one physical object.

A process MUST NOT fabricate `R` by taking definitions produced by the current application and merely relabelling those definitions as `R`.

---

## 7. Canonical Definitions

### 7.1 Published Semantic Definition Set

`Published Semantic Definition Set` means the coherent immutable release-affined static definition evidence for one published Semantic Registry Release.

It may contain or reference the accepted static definitions required by that release, including release-affined semantic, Surface Contribution and Fulfilment Contract definitions where those definitions participate in the release.

It MUST NOT contain merchant-scoped or live operational authority merely to simplify bootstrap.

### 7.2 Source-Release Resolution

`Source-Release Resolution` means a compiler/runtime/interpretation/recovery path that must reproduce or verify registered source definitions belonging to one exact Semantic Registry Release.

A path that can execute correctly from an exact retained RCP plus other accepted immutable evidence does not automatically become a Source-Release Resolution path.

### 7.3 Release Publication

`Release Publication` means the platform-controlled transition by which one complete immutable Semantic Registry Release definition set becomes resolvable as published evidence under its canonical release identity.

Publication does not mean activation for every merchant, permission for new activity, deployment or migration.

---

## 8. Publication Coherence Invariants

A published release MUST become usable only when its required constituent definition evidence is coherent and complete for that release.

The following is invalid:

```text
semantic definitions R10       published
Surface definitions R10        published
Fulfilment definitions R10     missing/corrupt
        ↓
R10 treated as a valid complete published release
```

Publication MUST therefore expose either:

```text
R10 not yet published/usable
```

or:

```text
R10 coherently published
```

for the release-definition set required by the accepted release composition.

Partial constituent publication MUST NOT create a valid release that ADR-010 can assemble by silently mixing versions.

---

## 9. Integrity and Provenance

Published Semantic Definition Set evidence MUST provide sufficient integrity/provenance to reject substitution or corruption of release content.

At minimum, an implementation MUST be able to establish that the definition evidence resolved as release `R` is the immutable evidence that Main Street published for `R`.

An integrity mechanism MAY use cryptographic hashes, signed manifests, immutable artefact identifiers, protected database constraints or another mechanism that provides equivalent evidence.

This ADR does not select the mechanism.

A release identifier alone is insufficient if storage can silently replace the bytes/records associated with that identifier.

---

## 10. Static Content Boundary

Published Semantic Definition Set evidence MAY contain or reference registered static definitions governed by accepted semantic authorities.

It MUST NOT become an aggregate of live merchant/platform state.

The following MUST remain outside the static release definition set:

```text
Merchant Configuration values/revisions
merchant-scoped Fulfilment Binding Set Revisions
credentials/secrets
Provider Readiness
current Commercial Entitlement
current Actor Authorisation
merchant operational records
current Inventory position/claims
current Booking/Order/Payment state
current projection data
```

Reference to a semantic type/contract does not import the referenced live state into the release artefact.

---

## 11. Exact Resolution

Source-Release Resolution MUST use the exact canonical Semantic Registry Release identity required by the requesting context.

Rejected:

```text
requested R8
R8 not found
        ↓
use latest/current R12
```

Required:

```text
requested R8
        ↓
resolve exact R8 definition evidence
        OR
report exact-resolution failure
```

A missing or corrupt required release MUST fail the affected source-release-resolution path. Another release MUST NOT be substituted silently.

---

## 12. Eager and Lazy Resolution

ADR-011 does not require all published releases to be materialised in every process at startup.

Both of the following conform when exactness and integrity are preserved:

```text
EAGER
startup resolves a bounded required set

LAZY
runtime resolves exact release evidence on first required use
```

A process MAY cache immutable resolved releases. Cache eviction MUST NOT mutate release meaning.

---

## 13. Ordinary Bootstrap

Ordinary application bootstrap MUST establish enough release-resolution infrastructure for the process to resolve every Semantic Registry Release context that the process is expected to serve or to reject unsupported resolution explicitly.

Ordinary restart MUST NOT itself:

- create a new semantic release;
- migrate Merchant Configuration;
- recompile every active Merchant Configuration Revision when an exact retained RCP is already the accepted runtime package;
- relabel current definitions as historical releases; or
- require every historical release to be loaded eagerly.

Where runtime execution can proceed correctly from an exact retained RCP and accepted immutable supporting evidence without Source-Release Resolution, ADR-011 does not require full release reconstruction merely because the process restarted.

---

## 14. Retained RCP Boundary

MS-PROT-022 v1.5 permits exact historical RCP retention or deterministic reconstruction from immutable inputs.

Accordingly:

```text
exact retained RCP
+
compatible executable support
+
required authoritative operational state
```

MAY satisfy an execution path without materialising the complete source Semantic Release, provided no accepted authority for that path requires source-definition reconstruction/verification.

ADR-011 therefore MUST NOT be interpreted as:

```text
any historical semantic affinity
        =>
load complete historical SemanticReleaseAssembly
```

---

## 15. Retention Rule

Complete Published Semantic Definition Set evidence MUST remain retrievable while at least one accepted current support or recovery obligation requires Source-Release Resolution for that release.

Examples of such obligations include a compiler/reconstruction path, an interpretation path or a recovery path whose accepted contract requires source definitions.

Existing commitment existence alone does not prove that full source-release material is required if the commitment is safely executable/interpretable from retained exact RCP/evidence and compatible executable support.

The four MS-PROT-054 support dimensions remain distinct:

```text
new configuration validation
new business activity
existing commitment execution
historical interpretation/recovery
```

Withdrawal from one dimension MUST NOT be interpreted as release mutation or automatic withdrawal from every other dimension.

---

## 16. Release Withdrawal From New Use

If Main Street determines that a published release is defective or no longer permitted for new configuration/new activity:

```text
R10 published immutable
        ↓
R10 disallowed for selected future use
```

Main Street MUST NOT edit `R10` in place.

A corrected semantic definition requires a new Semantic Registry Release according to MS-PROT-054.

Safe residual/history treatment remains governed by MS-PROT-054, ADR-012 and applicable platform/security/privacy authority.

---

## 17. Recovery Consistency

MS-TAS-RECOVERY-001 `RecoveryReleaseBundle` evidence MUST reference, contain or verifiably reproduce the same immutable release meaning published under ADR-011.

Disaster recovery MUST NOT create an independently interpreted `R8` whose definitions differ from ordinary `R8` publication evidence.

Recovery packaging MAY use a different physical container provided semantic identity and integrity remain equivalent and verifiable.

---

## 18. Failure Semantics

ADR-011 distinguishes:

### Publication rejection

The candidate release definition set is incomplete, internally inconsistent or fails integrity/publication validation. The release MUST NOT become published/usable.

### Exact-resolution failure

A requested published release cannot be resolved or its integrity cannot be established. The affected Source-Release Resolution path MUST fail explicitly and MUST NOT substitute another release.

### Infrastructure failure

The selected physical store/distribution mechanism is temporarily inaccessible. Existing paths that do not require source-release resolution MAY continue under their own accepted authority; ADR-011 does not impose whole-platform failure merely because one release store is unavailable.

### Corruption/substitution evidence

Evidence indicates that resolved content is not the accepted immutable evidence for the requested release. The affected release resolution MUST fail closed.

---

## 19. Concurrency and Idempotency

Publication of one logical release identity MUST be idempotent with respect to the same exact immutable definition evidence.

Concurrent publication attempts for the same release identity with materially different content MUST be rejected as a conflict; one release identifier MUST NOT resolve nondeterministically to multiple definitions.

Repeated exact-resolution reads are read-only with respect to semantic meaning.

---

## 20. Security and Privacy

Semantic release evidence MUST NOT be used as a convenient container for secrets or personal/merchant operational data.

Access to publication/storage infrastructure is an implementation-security concern governed by applicable security authority, including MS-PROT-067 where credentials participate.

Security restrictions MAY prevent use of otherwise retained historical semantics; retention of release evidence does not grant execution authority.

---

## 21. Consequences

### Positive

- Historical/current release identity survives deployment and restart.
- Current registration code cannot silently redefine an old release.
- Publication coherence prevents partial mixed release assemblies.
- RCP retention remains useful; Main Street does not load or reconstruct unnecessary release material.
- Recovery and ordinary runtime share one semantic meaning rather than DR-specific reinterpretation.
- Storage/distribution remains replaceable.

### Negative

- Main Street must preserve immutable release evidence beyond process lifetime where source-resolution obligations persist.
- Publication requires integrity/provenance validation.
- Release retirement cannot be implemented as unreviewed storage cleanup.

---

## 22. Falsification Cases Preserved

The accepted rule has survived the following governed cases:

- restart where an exact retained RCP is sufficient and full historical release materialisation is unnecessary;
- compiler/reconstruction path that requires exact historical source definitions;
- multiple physical artefacts representing one logical release;
- partial constituent publication;
- corrupt/substituted release evidence;
- release withdrawal from new use while residual/history obligations remain;
- startup serving only a bounded subset of releases;
- recovery of an older release;
- provider/merchant live-state changes that must remain outside the static release definition set;
- information-publisher merchants with no Booking-specific release requirement.

---

## 23. Implementation Constraints

The first conforming implementation SHOULD remain simple:

```text
platform-controlled immutable publication
+
exact release identifier lookup
+
integrity/provenance verification
+
ADR-010 assembly construction when required
+
retained-RCP bypass where source-release reconstruction is unnecessary
```

The following remain implementation choices:

- packaged application resources;
- protected artefact/object storage;
- immutable relational representation;
- content-addressed storage;
- eager versus lazy caches;
- build/promotion tooling.

No dedicated semantic-registry microservice is authorised by this ADR alone.

---

## 24. Conformance Criteria

A conforming implementation MUST demonstrate at least:

1. two published releases with different definitions remain independently resolvable by exact identity;
2. current definitions cannot be returned under an older release identity merely because the old release is absent;
3. partial/mismatched release constituent publication is rejected;
4. corrupted/substituted required evidence is detected or exact resolution otherwise fails safely;
5. restart can resolve an exact required source release without creating a new semantic release;
6. restart can continue from an exact retained RCP without mandatory recompilation where accepted runtime authority permits;
7. one process need not eagerly load all historical releases;
8. withdrawal of a release from new use does not mutate its published definition evidence;
9. recovery uses the same semantic release meaning as ordinary publication; and
10. release content excludes mutable merchant/provider operational authority.

---

## 25. Trade-Off and Revisit Condition

Main Street chooses durable immutable release evidence over reconstruction from current code, accepting additional retention/publication machinery to preserve historical correctness.

Main Street rejects a mandatory distributed semantic-registry service to preserve modular-monolith simplicity.

This decision SHOULD be revisited only if operational scale, independent release-distribution requirements or deployment topology demonstrate that the current physical storage/distribution mechanism cannot satisfy exact resolution, integrity and availability requirements. Such a revisit may change infrastructure without weakening this ADR's semantic-preservation invariants.

---

## 26. Acceptance Statement

> **A published Semantic Registry Release has one coherent immutable published definition set/evidence. Main Street resolves source releases exactly when a path actually requires them, never recreates an old release by relabelling current definitions, may execute from retained exact RCP/evidence where source reconstruction is unnecessary, and preserves the same release meaning across ordinary bootstrap and recovery.**
