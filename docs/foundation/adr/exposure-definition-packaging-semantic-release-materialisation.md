# ADR-016 — Exposure Definition Packaging & Semantic Release Materialisation

**ADR ID:** ADR-016  
**Version:** 1.0  
**Status:** ACCEPTED after governed review, falsification, ambiguity review and manual approval  
**Date:** 2 September 2026  
**Approved:** Manual approval on 2 September 2026  
**Authority type:** Production implementation architecture  
**Governed by:** `designs/DESIGN-RULES.md`  
**Amends:** ADR-010 and ADR-013 only within the packaging, exact-release coherence and materialisation of Exposure Element Contract definitions  
**Depends on:** composite MS-PROT-027 through v1.10; ADR-010; ADR-011; ADR-013  
**Closes:** `MS-PROT-027-V15-DQ-002`  
**Purpose:** Define how exact-release Exposure Element Contract definitions participate in Published Semantic Definition evidence, deployment semantic-definition bundles, deterministic materialisation and `SemanticReleaseAssembly` without transferring Exposure semantics into bootstrap infrastructure, weakening exact historical affinity or introducing a generic semantic-component framework.

---

## 1. Problem

MS-PROT-027 v1.6 establishes an immutable:

```text
ExposureElementContractRegistrySnapshot@R
```

for one exact Semantic Registry Release `R`.

It deliberately does not determine how that snapshot is represented in Published Semantic Definition evidence, carried by a deployment bundle, decoded during production bootstrap or joined to the other static authorities belonging to `R`.

The current production materialisation architecture carries:

```text
Semantic definitions
Surface Contribution definitions
Fulfilment Contract definitions
```

but no Exposure Element Contract definitions.

Production implementation has now reached that deferred boundary.

The architecture must therefore answer:

```text
How does an exact published Semantic Registry Release
carry and reconstruct its Exposure Element Contract definitions
without:

    hiding them inside another owner's definition section;
    reconstructing old releases from current code;
    permitting cross-release registry mixtures;
    making Exposure live policy part of the semantic bundle;
    or turning SemanticReleaseAssembly into an untyped generic container?
```

---

## 2. Governing Decision

Main Street SHALL extend the packaged semantic-definition materialisation architecture with one **explicit, opaque, exact-release Exposure definition constituent**.

The canonical logical flow becomes:

```text
Published Semantic Registry Release R
        ↓
Published Semantic Definition Set R
        │
        ├── Semantic definitions
        ├── Surface Contribution definitions
        ├── Fulfilment Contract definitions
        └── Exposure Element Contract definitions
        ↓
versioned immutable deployment bundle
        ↓
integrity and provenance validation
        ↓
registry-owned typed decoding
        ↓
exact-release registry snapshots
        ↓
SemanticReleaseAssembly R
        │
        ├── SemanticRegistrySnapshot
        ├── SurfaceContributionRegistrySnapshot
        ├── FulfilmentContractRegistrySnapshot
        └── ExposureElementContractRegistrySnapshot
        ↓
exact-release materialisation repository
```

`SemanticReleaseAssembly` continues to own **release coherence only**.

The addition of the Exposure registry does not transfer Exposure semantics, policy evaluation or runtime observation authority to the assembly or materialiser.

---

## 3. Ownership Boundary

Ownership remains:

```text
ExposureElementContract
    → Exposure static semantic contract authority
      under composite MS-PROT-027

ExposureElementContractRegistrySnapshot
    → immutable exact-release Exposure contract catalogue

Published Semantic Definition Set
    → immutable published release evidence

Packaged Semantic Definition Bundle
    → deployment carriage + integrity envelope

SemanticReleaseMaterialiser
    → validation, typed decoding and coherence assembly

SemanticReleaseAssembly
    → exact-release constituent coherence only

Exposure resolver
    → later runtime Exposure evaluation

merchant Exposure choice
    → applicable owning capability/fact authority

Audience Observation Context
    → trusted observation-context authority
```

Bootstrap infrastructure MUST NOT interpret:

```text
baseline Exposure decisions
merchant choice
Exposure requirements
Projection Serviceability
Actor Authorisation
Provider Readiness
candidate legitimacy
business state
```

It transports and reconstructs immutable definitions only.

---

## 4. Dedicated Exposure Definition Section

Exposure definitions SHALL have a dedicated logical section.

They MUST NOT be:

```text
embedded in Surface definitions
embedded in the general Semantic registry merely for convenience
encoded as Fulfilment definitions
inferred from current Java registration code at runtime
represented as live merchant policy
omitted while claiming complete Exposure materialisation
```

The section is opaque to the bundle envelope.

Its decoder owns reconstruction of:

```text
ExposureElementContractRegistrySnapshot
```

The envelope/materialiser does not inspect individual owner identifiers, audiences, baselines, merchant-choice references or requirement references except through the structural contract exposed by the decoded registry snapshot.

---

## 5. Bundle Format Evolution

The existing bundle format identifier:

```text
mainstreet-semantic-bundle-v1
```

SHALL retain its existing meaning.

It MUST NOT be silently redefined to mean a bundle containing a fourth Exposure section.

Complete Exposure-definition materialisation SHALL therefore use a new bundle format generation:

```text
mainstreet-semantic-bundle-v2
```

A v2 bundle logically contains:

```text
format version
exact Semantic Registry Release identifier
publication provenance
content-integrity evidence

semanticDefinitions
surfaceDefinitions
fulfilmentDefinitions
exposureDefinitions
```

The exact byte serialization, parser implementation and codec technology remain implementation details under ADR-013.

However, the integrity mechanism MUST cover the exact Exposure-definition payload in addition to the other bundle evidence.

Changing Exposure-definition bytes without correspondingly valid integrity evidence MUST invalidate the bundle.

A v2 decoder MUST NOT reinterpret a v1 envelope as v2 merely because current code can manufacture an Exposure registry.

---

## 6. Published Semantic Definition Set

The logical `Published Semantic Definition Set` for a release participating in this architecture SHALL carry or establish exact immutable Exposure definition evidence sufficient to reconstruct:

```text
ExposureElementContractRegistrySnapshot@R
```

This does not require one physical file or one particular Java field layout.

The implementation MAY use an opaque byte section in the current packaged architecture.

The published evidence MUST preserve the exact accepted definitions for the release.

Current registration code is not historical publication evidence.

Therefore:

```text
current InitialExposureElementContractPortfolio
        +
historical release identifier
```

MUST NOT be treated as proof of the historical release's Exposure definitions.

---

## 7. Exact-Release Materialisation

The materialiser SHALL consume a typed Exposure-section decoder conceptually equivalent to:

```text
SemanticDefinitionSectionDecoder<
    ExposureElementContractRegistrySnapshot
>
```

For bundle release `R`, successful decoding requires:

```text
decodedExposureRegistry.semanticRegistryReleaseIdentifier == R
```

A mismatch is a materialisation failure.

The materialiser SHALL remain generic coordination infrastructure. It SHALL NOT:

```text
switch on Exposure owner
switch on merchant/business type
interpret ExposureDecision
resolve merchant choices
invoke requirement evaluators
apply Exposure restrictions
produce EXPOSE/WITHHOLD
```

Those responsibilities remain outside E2.

---

## 8. Semantic Release Assembly

`SemanticReleaseAssembly` SHALL explicitly include:

```text
ExposureElementContractRegistrySnapshot exposureRegistry
```

as a concrete constituent.

Construction requires:

```text
semanticRegistry.version
    ==
surfaceRegistry.semanticRegistryReleaseIdentifier
    ==
fulfilmentRegistry.semanticRegistryReleaseIdentifier
    ==
exposureRegistry.semanticRegistryReleaseIdentifier
```

An inconsistent combination MUST be rejected.

The Exposure registry remains independently typed and owner-governed.

The assembly MUST NOT be replaced by:

```text
Map<String, Object>
Map<Class<?>, Object>
generic semantic-component bag
runtime service locator
arbitrary registry plugin container
```

merely to avoid adding the explicit constituent.

Evidence currently supports one concrete additional release-affined authority, not a generic registry framework.

---

## 9. All-or-Nothing Publication

ADR-013 all-or-nothing materialisation remains mandatory.

For a v2 deployment materialisation set:

```text
validate every bundle
        ↓
decode every required section
        ↓
validate every exact release affinity
        ↓
construct every coherent assembly
        ↓
construct complete immutable repository
        ↓
publish repository
```

Failure of the Exposure section in any required bundle MUST NOT produce a repository in which that release is partially materialised.

The process MUST NOT publish:

```text
R17 with Semantic/Surface/Fulfilment
but Exposure missing or unresolved
```

as a complete v2 Semantic Release Assembly.

---

## 10. Missing Versus Empty Exposure Definitions

The following are distinct:

```text
Exposure section absent
        ≠
Exposure section present and decoded as an explicit empty registry
```

A v2 bundle requires an explicit Exposure-definition constituent.

An explicitly empty `ExposureElementContractRegistrySnapshot@R` is valid only when exact published evidence for `R` establishes that the release legitimately contains zero Exposure Element Contracts.

The materialiser MUST NOT interpret:

```text
missing bytes
decoder absence
legacy v1 bundle
current code lacking a registration
```

as an empty Exposure registry.

---

## 11. Historical Releases and Legacy v1 Bundles

A legacy v1 bundle does not carry sufficient evidence to claim complete Exposure-definition materialisation under this ADR.

A process MUST NOT repair that absence by using the current code-owned Exposure portfolio.

Where a historical release must undergo Source-Release Resolution after this architecture is introduced, Main Street SHALL use one of the following legitimate paths:

```text
exact retained execution evidence means
source-release reconstruction is not required
        → ADR-011 retained-evidence exception survives

or

exact immutable published definition evidence for R exists
        → construct/repackage a v2 bundle for R
        → materialise exact R

or

required exact Exposure evidence for R cannot be established
        → source-release materialisation of R fails closed
```

Repackaging an existing immutable Semantic Registry Release into a new physical bundle format does not create a new Semantic Registry Release when the reconstructed semantic definition evidence is exactly the already-published meaning of `R`.

It changes deployment representation, not semantic identity.

If exact evidence proves that `R` had zero Exposure Element Contracts, the v2 representation contains an explicit empty Exposure registry.

No chronology assumption or current-default portfolio may substitute for that evidence.

---

## 12. Runtime Use After Materialisation

After E2 materialisation, runtime code requiring the Exposure Element Contract registry for release `R` SHALL obtain the registry from the exact resolved release assembly or another explicitly equivalent exact-release boundary governed by the same accepted evidence.

Runtime code SHALL NOT use:

```text
latest Exposure registry
current deployment portfolio
static singleton unrelated to requested release
fallback registry from another release
```

when an exact release-affined Exposure definition is required.

The code-owned initial Exposure portfolio MAY remain useful as:

```text
trusted publication/build input
test fixture
deterministic construction input for a newly published release
```

but SHALL NOT become historical release evidence merely because it is available in the current binary.

---

## 13. Failure Classification

Exposure definition packaging introduces no new business failure vocabulary.

The existing generic semantic-materialisation failure classes remain applicable to the new constituent.

Conceptually:

```text
unsupported legacy/incompatible envelope
    → UNSUPPORTED_BUNDLE_FORMAT

integrity evidence does not cover actual payload
    → CONTENT_INTEGRITY_FAILURE

Exposure decoder cannot reconstruct valid definition evidence
    → DEFINITION_DECODE_FAILURE

decoded Exposure registry belongs to another release
    → RELEASE_IDENTITY_MISMATCH

constituent registries cannot form one coherent release
    → ASSEMBLY_COHERENCE_FAILURE

required exact release is unavailable
    → REQUIRED_RELEASE_NOT_MATERIALISED
```

Implementation MAY refine internal diagnostic detail without creating Exposure-specific semantic outcomes.

If implementation evidence demonstrates that the accepted generic categories are genuinely insufficient, that finding must be reviewed separately rather than inventing a category inside E2 for convenience.

---

## 14. Explicit Non-Goals

This ADR does not define or change:

```text
Exposure Element Contract semantic meaning
EXPOSE / WITHHOLD resolution
Audience Observation Context
merchant Exposure-choice persistence
Profile Exposure mutation operations
requirement-evaluator semantics
Projection Serviceability
Projection Contract bundle packaging
Projection policy evaluator bundle packaging
Actor authentication or authorisation
Commercial Entitlement
Provider Readiness
API/HTTP representation
pagination or continuation tokens
caching
telemetry
search/index convergence
CDN revocation propagation
database representation of merchant policy
a generic semantic registry/component framework
```

Those responsibilities remain with their existing accepted authorities or deferred decisions.

In particular, this ADR does not package Projection Contract definitions merely for symmetry. Any later requirement to materialise additional static release-affined registries must be justified by its own evidence and governing authority.

---

## 15. Falsification

### Cross-release Exposure registry

Bundle metadata declares `R17`; the decoded Exposure registry declares `R18`.

Result:

```text
materialisation fails
R18 is not substituted
R17 is not published partially
```

**PASS**

### Tampered Exposure payload

The Exposure-definition bytes are changed without valid corresponding integrity evidence.

Result:

```text
bundle rejected
no repository publication
```

**PASS**

### Missing v2 Exposure section

A purported complete v2 bundle has no Exposure definition evidence.

Result:

```text
materialisation fails
missing is not converted to empty
```

**PASS**

### Genuine zero-contract release

Exact release evidence establishes no Exposure Element Contracts.

Result:

```text
v2 carries explicit empty registry@R
assembly remains exact and coherent
```

**PASS**

### Legacy v1 historical release

Only a v1 physical bundle is available.

Result:

```text
do not manufacture Exposure from current code
repackage from exact published evidence if available
otherwise fail required source-release materialisation
```

**PASS**

### Lost historical Exposure evidence

A retained historical release requires source reconstruction but its exact Exposure definitions cannot be proven.

Result:

```text
do not use current portfolio
do not use latest release
fail source-release resolution
```

**PASS**

### Same stable contract evolves

`profile/public-contact-point` exists in `R17` and `R18` with different accepted definitions.

Result:

```text
R17 assembly resolves R17 definition
R18 assembly resolves R18 definition
no cross-release substitution
```

**PASS**

### Merchant changes public Contact Point choice

The merchant changes current Profile-owned Exposure policy.

Result:

```text
no semantic bundle rewrite
static contract remains unchanged
live owner state remains outside bundle
```

**PASS**

### Provider outage

A provider becomes unavailable after startup.

Result:

```text
no bundle mutation
no Exposure definition mutation
current serviceability remains runtime evidence
```

**PASS**

### Additional owner

A future `reviews` owner publishes an accepted Exposure Element Contract.

Result:

```text
owner definition participates in Exposure registry
bundle/materialiser carries it opaquely
materialiser acquires no reviews-specific rule
```

**PASS**

### One bundle among several fails

Deployment contains valid `R17`, invalid `R18`, valid `R19`.

Result:

```text
complete materialisation repository is not published
no partial success masquerades as readiness
```

**PASS**

### Retained RCP avoids source resolution

A historical operation can execute correctly from exact retained RCP and separately accepted immutable evidence.

Result:

```text
ADR-011 exception survives
full historical release need not be eagerly materialised merely because Exposure packaging exists
```

**PASS**

### Generic-framework pressure

Adding the Exposure registry requires one extra typed decoder and assembly constituent.

Result:

```text
explicit composition remains adequate
no evidence yet requires a generic registry/plugin framework
```

**PASS**

### Projection symmetry pressure

Projection Contracts are also release-affined.

Result:

```text
DQ-002 does not silently broaden scope
Projection packaging requires its own governing need/decision
```

**PASS**

---

## 16. Rejected Alternatives

### Put Exposure definitions in Surface definitions

Rejected.

Surface contribution definitions and Exposure Element Contracts have distinct ownership and meaning. MS-PROT-027 v1.6 explicitly prohibits silently encoding Exposure contracts in the Surface section.

### Continue code-only Exposure registration indefinitely

Rejected as the production Source-Release Resolution mechanism.

Current executable code cannot prove the exact immutable Exposure definitions of an arbitrary historical release.

### Maintain an independent Exposure-release repository

Rejected.

Independent release resolution would recreate the incoherent-combination risk that ADR-010's Semantic Release Assembly exists to eliminate.

### Introduce a generic registry/component framework

Rejected.

A generic component map, plugin system or type-erased semantic bag adds speculative abstraction, weakens inspectable ownership and increases semantic-gravity risk. One additional concrete constituent does not justify that architecture.

### Make the Exposure registry optional in the assembly

Rejected.

Optionality would conflate:

```text
release legitimately has zero Exposure contracts
```

with:

```text
Exposure evidence was omitted or could not be reconstructed
```

The distinction must remain structural.

---

## 17. Implementation Conformance Requirements

E2 implementation SHALL be tests-first under `IMPLEMENTATION-RULES.md`.

Executable evidence must establish at least:

```text
v2 Exposure payload participates in integrity evidence

bundle/definition values defensively copy Exposure bytes

valid Exposure bytes deterministically reconstruct
ExposureElementContractRegistrySnapshot

decoded Exposure registry must match exact bundle release

SemanticReleaseAssembly rejects Exposure release mismatch

missing Exposure evidence cannot become an empty registry

explicit exact-release zero-contract evidence may become an empty registry

v1 is never silently interpreted as complete v2 Exposure materialisation

historical release materialisation never falls back to current Exposure portfolio

two releases with the same stable contract identity may retain different
exact definitions and resolve independently

failure of one Exposure section prevents publication of the complete
materialisation repository

runtime exact-release lookup exposes the corresponding Exposure registry

materialiser contains no owner-specific Exposure policy interpretation

existing Projection policy semantic-ownership conformance remains GREEN

full Maven + PostgreSQL verification remains GREEN
```

The implementation shall prefer the smallest code change satisfying these requirements.

No new database, service, cache, broker, generic semantic framework or dynamic registration mechanism is required by this ADR.

---

## 18. Trade-Offs

The accepted cost is:

```text
new bundle format generation
additional publication/build evidence
one additional typed decoder
one additional explicit assembly constituent
historical repackaging where exact source reconstruction is genuinely required
```

The benefit is:

```text
structural exact-release coherence
complete integrity coverage
no current-code historical reinterpretation
no Surface/Exposure ownership leakage
deterministic all-or-nothing bootstrap
clear path from published definitions to runtime Exposure contracts
```

Main Street deliberately accepts the explicit constituent rather than introducing a generic framework prematurely.

If repeated future release-affined registries make concrete assembly evolution materially costly, ADR-010 may later be revisited through a separate governed architecture cycle.

That possibility is not evidence that such a framework is justified today.

---

## 19. Hard Invariants

1. Exposure definitions remain semantically owned by Exposure authority, not bootstrap infrastructure.
2. One decoded Exposure registry belongs to exactly one Semantic Registry Release.
3. Complete v2 materialisation includes explicit Exposure definition evidence.
4. Missing Exposure evidence is never equivalent to an explicit empty registry.
5. Exposure definitions are not encoded inside the Surface definition section.
6. `mainstreet-semantic-bundle-v1` retains its existing meaning.
7. Complete Exposure materialisation uses a distinct versioned bundle format.
8. Integrity evidence covers the Exposure payload.
9. Exact physical codec technology remains replaceable under ADR-013.
10. `SemanticReleaseAssembly` explicitly validates Exposure registry release affinity.
11. The assembly owns release coherence only.
12. Materialisation remains all-or-nothing.
13. Current code registration is not historical publication evidence.
14. No release may fall forward to a current/latest Exposure portfolio.
15. Repackaging exact immutable evidence does not itself create a new Semantic Registry Release.
16. Merchant policy, operational state, Projection Serviceability, authorisation and Provider Readiness remain outside the static definition bundle.
17. E2 materialisation creates no Exposure verdict.
18. No generic semantic-component framework is authorised by this decision.
19. Projection Contract packaging is outside this decision.
20. E2 conformance does not imply E4 conformance.

---

## 20. Implementation and Sequencing Consequence

With this ADR accepted and formalised:

```text
MS-PROT-027-V15-DQ-002
        → RESOLVED

E2
        → eligible for graph re-evaluation

if all E2 dependencies are satisfied
        → READY

READY E2
        → IMPLEMENTATION-RULES RED → GREEN

verified E2
        ↓
refresh IMP-06 fine-grained graph
        ↓
re-evaluate E4
```

This ADR does not itself mark E2 complete or E4 READY.

`MS-IMP-001.md` remains the implementation-sequence authority.

`IMPLEMENTATION-RULES.md` remains the implementation-execution authority.

---

## 21. Acceptance Statement

> **Exposure Element Contract definitions that participate in a published Semantic Registry Release shall be carried as a dedicated integrity-covered definition constituent, deterministically decoded into one immutable exact-release `ExposureElementContractRegistrySnapshot`, and incorporated explicitly into that release's `SemanticReleaseAssembly`. The bundle/materialisation layer owns only exact evidence, validation and release coherence; it neither interprets Exposure semantics nor substitutes current code for historical release evidence.**

**Review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Manual approval:** GRANTED — 2 September 2026  
**Governance verdict:** **ACCEPTED**
