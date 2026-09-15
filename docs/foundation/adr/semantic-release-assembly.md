# ADR-010 — Semantic Release Assembly

> **ADR ID:** ADR-010
> **Status:** Accepted
> **Date:** 25 August 2026
> **Owner:** Architecture Team
> **Approved:** Manual approval on 25 August 2026

---

## 1. Context

MS-PROT-054 establishes that a published Semantic Registry Release is immutable and may contain or reference registered definitions including fulfilment contracts and Surface contributions. It deliberately defers the exact implementation representation.

MS-PROT-022 requires each materialised Merchant Configuration Revision and its Resolved Configuration Package to remain pinned to one exact Semantic Registry Release. MS-PROT-048 and MS-PROT-049 additionally make Fulfilment Contract and Surface Contribution snapshots release-affined to that semantic release.

The current implementation therefore has three immutable static authorities that must agree on one release identity:

```text
SemanticRegistrySnapshot
SurfaceContributionRegistrySnapshot
FulfilmentContractRegistrySnapshot
```

Passing these independently through application wiring permits structurally invalid combinations even when downstream checks can detect some mismatches.

---

## 2. Problem Statement

How shall Main Street resolve and retain the static semantic, Surface and Fulfilment contract authorities for one semantic release so that multiple immutable releases can coexist safely and historical merchant packages can always resolve the exact release-affined authorities they require?

---

## 3. Options Considered

### Option A — One global current snapshot per registry

Expose one application-wide `SemanticRegistrySnapshot`, `SurfaceContributionRegistrySnapshot` and `FulfilmentContractRegistrySnapshot` representing the current release.

Rejected because merchants and historical packages may remain pinned to older semantic releases. A current singleton could cause release-affinity mismatches or force accidental reinterpretation.

### Option B — Independently versioned registry repositories

Maintain separate repositories and resolve each snapshot independently by release identifier.

Rejected for the initial architecture because callers could assemble impossible combinations and every consumer would need to reproduce cross-registry affinity validation.

### Option C — Generic registry/bundle framework

Introduce parameterised registry, snapshot, bundle, publication and lifecycle abstractions.

Rejected because no evidence requires a generic registry framework and it would add speculative machinery beyond the accepted semantic model.

### Option D — Immutable Semantic Release Assembly

Represent one semantic release and its release-affined supporting static contracts as one immutable concrete assembly:

```text
SemanticReleaseAssembly
│
├── SemanticRegistrySnapshot
├── SurfaceContributionRegistrySnapshot
└── FulfilmentContractRegistrySnapshot
```

Resolve assemblies by the canonical Semantic Registry Release identifier.

---

## 4. Decision

Main Street adopts **Option D: immutable Semantic Release Assembly**.

`SemanticRegistrySnapshot.version()` is the canonical release identifier for the assembly. The assembly shall not introduce an independent competing release identifier.

Construction shall require:

```text
semanticRegistry.version
    == surfaceRegistry.semanticRegistryReleaseIdentifier

semanticRegistry.version
    == fulfilmentRegistry.semanticRegistryReleaseIdentifier
```

An inconsistent assembly is rejected immediately.

The constituent snapshots retain their existing ownership and types. The assembly does not merge their semantics or move Surface/Fulfilment definitions into `SemanticRegistrySnapshot`.

The initial runtime lookup boundary shall be a narrow `SemanticReleaseAssemblyRepository` capable of resolving one immutable assembly by exact release identifier.

The initial repository implementation may be process-local and immutable. It shall support multiple releases concurrently and shall reject ambiguous duplicate assemblies for the same release identifier.

---

## 5. Rationale

The assembly makes an already-accepted invariant structural rather than relying on each caller to reconstruct it:

```text
one semantic release
        ↓
one coherent static authority set
```

This preserves historical reproducibility and allows different merchants to remain pinned to different releases without using a mutable "current registry" singleton.

It also gives application composition one deterministic source from which to obtain the Surface and Fulfilment snapshots that correspond to a Merchant Configuration Revision or Resolved Configuration Package's pinned semantic release.

The design remains deliberately concrete. Main Street does not need a generic registry framework, publication workflow or second semantic lifecycle to achieve this invariant.

---

## 6. Assembly Invariants

1. `SemanticRegistrySnapshot.version()` is the assembly release identifier.
2. Surface and Fulfilment snapshots must declare that exact semantic release identifier.
3. An assembly is immutable after construction.
4. Runtime lookup is by exact release identifier; there is no implicit "latest" substitution for a pinned merchant package.
5. Multiple immutable release assemblies may coexist in one application process.
6. More than one assembly for the same release identifier is rejected as ambiguous.
7. Absence of a requested release is reported as absence/failure to resolve; another release must not be substituted silently.
8. Merchant-scoped `FulfilmentBindingSetRevision` is not part of the Semantic Release Assembly.

---

## 7. Ownership Boundary

```text
SemanticReleaseAssembly
        │
        ├── SemanticRegistrySnapshot
        │       semantic definitions / capability graph
        │
        ├── SurfaceContributionRegistrySnapshot
        │       capability-owned Surface contribution definitions
        │
        └── FulfilmentContractRegistrySnapshot
                fulfilment roles / requirements / provider support contracts
```

The assembly owns **release coherence only**.

It does not own:

```text
merchant configuration
merchant fulfilment routing
provider connection/readiness
live actor authority
operational state
compatibility/migration decisions
configuration activation
semantic publication lifecycle
```

Those remain with their existing authorities.

---

## 8. Consequences

### Positive

- Cross-registry release affinity becomes structural and testable.
- Historical semantic releases can coexist safely.
- Merchant packages can resolve exactly the static authorities matching their pinned semantic release.
- Surface and Fulfilment runtime composition can consume one coherent release source.
- No generic registry framework or mutable global-current snapshot is required.

### Negative

- Application assembly must construct and retain one additional concrete object per supported semantic release.
- Every supported release must supply all three constituent snapshots in the initial architecture.
- Runtime startup/assembly must reject inconsistent or duplicate release assemblies rather than attempting recovery by substitution.

---

## 9. Affected Components

- semantic registry runtime composition;
- Surface contribution registry composition;
- Fulfilment contract registry composition;
- configuration-package/runtime release lookup;
- future Spring Boot application composition;
- historical package interpretation.

`FulfilmentBindingSetRevision`, provider connection/readiness, Merchant Configuration lifecycle and compatibility/migration semantics are not changed.

---

## 10. Initial Implementation Scope

The first implementation shall add only:

```text
SemanticReleaseAssembly
SemanticReleaseAssemblyRepository
immutable process-local repository adapter
```

Tests shall prove:

- matching constituent release identifiers assemble successfully;
- Surface mismatch is rejected;
- Fulfilment mismatch is rejected;
- exact release lookup returns the corresponding assembly;
- unknown release lookup does not substitute another release;
- multiple releases coexist;
- duplicate release identifiers are rejected.

No Spring bean wiring, semantic release publication workflow, activation lifecycle, persistence schema or compatibility/migration implementation is introduced by this slice.

---

## 11. Future Considerations

A later accepted architecture may define persistent/distributed storage, release publication, retention or deployment mechanics if operational evidence requires them. Such work must preserve MS-PROT-054's semantic release immutability and compatibility/migration authority.

The repository adapter may later change without changing the `SemanticReleaseAssembly` coherence invariant.
