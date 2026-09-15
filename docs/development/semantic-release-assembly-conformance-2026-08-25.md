# Semantic Release Assembly Conformance — 25 August 2026

**Status:** VERIFIED implementation evidence  
**Authority:** ADR-010, MS-PROT-022, MS-PROT-048, MS-PROT-049, MS-PROT-054  
**Branch:** `development`  
**Verified implementation head:** `29325c3bd6806eed48be0e6d887aa5192bb7deac`  
**GitHub Actions run:** `32897543930`

This note records implementation evidence only. It does not create semantic or implementation-architecture authority.

## Accepted architecture

Manual approval on 25 August 2026 accepted:

```text
ADR-010 — Semantic Release Assembly
```

MS-PROT-054 already establishes the semantic release as immutable and permits it to contain or reference release-affined Surface and Fulfilment contracts while deferring the exact implementation representation. ADR-010 fills that deferred representation without changing semantic-release meaning.

The accepted concrete assembly is:

```text
SemanticReleaseAssembly
│
├── SemanticRegistrySnapshot
├── SurfaceContributionRegistrySnapshot
└── FulfilmentContractRegistrySnapshot
```

`SemanticRegistrySnapshot.version()` is the canonical assembly release identifier. There is no second competing release identity.

Required release coherence is:

```text
semanticRegistry.version
    == surfaceRegistry.semanticRegistryReleaseIdentifier

semanticRegistry.version
    == fulfilmentRegistry.semanticRegistryReleaseIdentifier
```

The assembly owns release coherence only. Each constituent registry retains its existing responsibility and type.

## TDD evidence

### RED

Commit:

```text
b82732deca3baba159de6d48976aede14fc89af0
test(semantic-release): define coherent release assembly contract
```

GitHub Actions run:

```text
32897271429
```

The 420 existing production sources compiled successfully. Test compilation then failed only because the approved production types did not yet exist:

```text
SemanticReleaseAssembly
SemanticReleaseAssemblyRepository
InMemorySemanticReleaseAssemblyRepository
```

The RED failure therefore exercised the intended missing implementation boundary rather than an unrelated regression.

### Production

Commit:

```text
29325c3bd6806eed48be0e6d887aa5192bb7deac
feat(semantic-release): assemble coherent release authorities
```

Production introduced exactly three types:

```text
mainstreet.semantic.release.SemanticReleaseAssembly
mainstreet.semantic.release.SemanticReleaseAssemblyRepository
mainstreet.semantic.release.InMemorySemanticReleaseAssemblyRepository
```

No Spring bean wiring, semantic-release publication lifecycle, release activation workflow, persistence schema, compatibility/migration implementation, merchant fulfilment routing, provider readiness or generic registry framework was added.

## Verified behaviour

Executable evidence proves:

1. Matching Semantic, Surface and Fulfilment snapshots form one immutable `SemanticReleaseAssembly`.
2. A Surface snapshot from another semantic release is rejected at assembly construction.
3. A Fulfilment snapshot from another semantic release is rejected at assembly construction.
4. The assembly release identifier is derived from `SemanticRegistrySnapshot.version()` rather than independently supplied.
5. Exact repository lookup returns the assembly for the requested release identifier.
6. Multiple immutable release assemblies may coexist in the process.
7. An unknown release is reported as absent; another or newer release is not substituted.
8. Duplicate assemblies for the same release identifier are rejected as ambiguous.
9. Blank lookup identifiers are rejected.
10. `FulfilmentBindingSetRevision` remains merchant-scoped and outside the Semantic Release Assembly.

The process-local repository is immutable after construction and is intentionally only the initial adapter behind the exact-lookup port.

## Full repository gate

GitHub Actions run `32897543930` executed:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Measured result:

```text
Java target                         25
Java observed in CI                 25.0.4+7
Spring Boot                         4.1.0
PostgreSQL                          18.6
jOOQ                                3.21.5
Flyway migrations                   26
production Java sources             423
test Java sources                   150
unit tests                          445 PASS
PostgreSQL integration tests        146 PASS
total tests                         591 PASS
failures / errors / skipped         0 / 0 / 0
result                              BUILD SUCCESS
```

The six new `SemanticReleaseAssemblyTest` cases passed.

PostgreSQL ERROR entries emitted by existing negative/integrity tests are expected rejection-path evidence; the Maven gate completed with zero test failures or errors.

## Current boundary

ADR-010 provides a coherent immutable representation and exact lookup boundary for release-affined static authorities. It does not determine how concrete release definitions are sourced or bootstrapped into a production Spring application.

The next possible runtime composition step would need to answer how the application obtains concrete release-specific definitions and constructs the supported `SemanticReleaseAssembly` instances, for example:

```text
concrete registered semantic definitions
+ concrete Surface definitions
+ concrete Fulfilment definitions
        ↓
SemanticReleaseAssembly
        ↓
SemanticReleaseAssemblyRepository
        ↓
merchant/package exact release lookup
```

That sourcing/bootstrap mechanism is outside ADR-010's approved initial scope. Implementation must pause before introducing hard-coded release catalogues, resource-based definition loading, persistence/publication machinery or Spring construction conventions unless accepted architecture determines that choice.
