# Residual Authority Composition Conformance — 25 August 2026

**Status:** VERIFIED implementation evidence  
**Authority:** ADR-009, MS-PROT-040, MS-PROT-042 v1.4, MS-PROT-049  
**Branch:** `development`  
**Verified implementation head:** `f15b37a82577682bf8aee8f364137181751e3af1`  
**GitHub Actions run:** `32895813602`

This note records implementation evidence only. It does not create semantic or implementation-architecture authority.

## Accepted architecture

Manual approval on 25 August 2026 accepted:

```text
ADR-009 — Capability-Owned Residual Authority Composition
```

The accepted application-composition boundary is:

```text
ContextualSurfaceResolver
        ↓
ResidualSurfaceObligationAuthority
        ↓
immutable capability-owned authority composite
        ↓
capability-qualified delegate
        ↓
owning capability residual authority
```

The composite owns identity-to-delegate composition only. The owning capability remains authoritative for residual-obligation meaning.

The implementation must not introduce:

- capability-name branching inside Surface;
- a generic residual lifecycle;
- generic residual-obligation persistence;
- semantic-registry service lookup;
- Spring bean-name semantic identity;
- reflection/classpath discovery of semantic routing.

## TDD evidence

### RED

Commit:

```text
976fbdf59ce6aab0c040c76fec7d36bacef98dc3
test(surface): define residual authority composition contract
```

GitHub Actions run:

```text
32895554528
```

Production sources compiled successfully. Test compilation then failed only because the approved production types did not yet exist:

```text
CompositeResidualSurfaceObligationAuthority
ResidualSurfaceObligationBinding
```

The RED failure therefore exercised the intended missing implementation boundary rather than an unrelated regression.

### Production

Commit:

```text
f15b37a82577682bf8aee8f364137181751e3af1
feat(surface): compose capability-owned residual authorities
```

Production introduced exactly three composition types:

```text
ResidualObligationProbe
ResidualSurfaceObligationBinding
CompositeResidualSurfaceObligationAuthority
```

No Booking lifecycle, cancellation, fulfilment, payment, notification, provider or timestamp semantics were added.

## Verified behavior

Executable evidence proves:

1. Booking residual truth can be delegated exactly once through an explicit `booking` binding to `BookingResidualObligationAuthority`.
2. Duplicate residual bindings for the same capability identifier are rejected during assembly.
3. A registered MERCHANT contribution with `ACTIVE_OR_RESIDUAL` eligibility requires a residual binding for its owning capability.
4. A MERCHANT contribution that is active-only does not require a residual binding.
5. Runtime lookup of an unbound capability fails closed instead of silently returning `false`.
6. The immutable composite routes by capability identity only and does not interpret capability-owned residual semantics.
7. `ContextualSurfaceResolver` remains unchanged and contains no Booking-specific branch.

## Full repository gate

GitHub Actions run `32895813602` executed:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Measured result:

```text
production Java sources             420
test Java sources                   149
unit tests                          439 PASS
PostgreSQL integration tests        146 PASS
total tests                         585 PASS
failures / errors / skipped         0 / 0 / 0
Flyway migrations                   26
PostgreSQL                          18.6
result                              BUILD SUCCESS
```

The five new `CompositeResidualSurfaceObligationAuthorityTest` cases all passed.

PostgreSQL ERROR entries emitted by existing negative/integrity tests are expected rejection-path evidence; the Maven gate completed with zero test failures or errors.

## Current boundary

ADR-009 now establishes and implements the reusable Surface-side composition mechanism.

The next possible step is concrete application/runtime assembly of:

```text
pinned SurfaceContributionRegistrySnapshot
        +
BookingResidualObligationAuthority implementation
        +
ResidualSurfaceObligationBinding("booking", ...)
        ↓
CompositeResidualSurfaceObligationAuthority
        ↓
ContextualSurfaceResolver
```

That wiring shall proceed only if the accepted implementation architecture already determines the application composition root, registry-snapshot acquisition and infrastructure-authority construction. If those choices are not already governed, implementation must pause rather than invent a new dependency-injection/application-assembly architecture.
