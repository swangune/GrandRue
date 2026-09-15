# ADR-013 — Packaged Semantic Definition Materialisation & Production Bootstrap

> **ADR ID:** ADR-013  
> **Version:** 1.0  
> **Status:** Accepted  
> **Date:** 26 August 2026  
> **Owner:** Architecture Team  
> **Approved:** Manual approval on 26 August 2026, with the explicit condition that every deferred question be catalogued for future traceability  
> **Depends on:** MS-PROT-025, MS-PROT-040 v1.1, MS-PROT-054, MS-PROT-062, ADR-010, ADR-011, ADR-012, MS-TAS-RECOVERY-001  
> **Closes:** MS-PROT-079 Target 1 — Production semantic-release bootstrap  
> **Purpose:** Define how a Main Street production backend obtains, validates, materialises and exposes exact Published Semantic Registry Releases at deployment/startup while preserving exact semantic identity, separating definition materialisation from executable-contract support, preventing implicit fallback, and remaining compatible with configuration activation, rolling deployment and recovery.

---

## 1. Accepted Decision

Main Street's initial production semantic-release bootstrap SHALL use:

> **Versioned immutable semantic-definition bundles packaged with the backend deployment and deterministically materialised into an immutable exact-release process-local Semantic Release Assembly repository.**

The architecture is:

```text
Accepted Published Semantic Registry Release
        ↓
Published Semantic Definition Set / evidence
        ↓
immutable Packaged Semantic Definition Bundle
        ↓
Deployment Semantic Materialisation Set
        ↓
startup integrity + identity + format validation
        ↓
deterministic definition decoding
        ↓
release-affined registry snapshots
        ↓
SemanticReleaseAssembly
        ↓
immutable exact-release process-local repository
```

This establishes **definition materialisability only**.

It does not establish executable compatibility.

The complete runtime distinction is:

```text
SEMANTIC DEFINITION MATERIALISABLE
        ≠
EXECUTION CONTRACT SUPPORTED
        ≠
INVOCATION AUTHORISED
        ≠
INVOCATION OPERATIONALLY ELIGIBLE
```

ADR-012 remains authoritative for executable-contract support.

No semantic release may be resolved by `latest`, nearest-compatible, default, previous or other implicit substitution.

---

## 2. Problem and Scope

ADR-010 through ADR-012 already establish:

- coherent release-affined assembly;
- exact release lookup;
- immutable published definition evidence;
- exact historical reconstruction;
- retention requirements;
- contract-scoped executable support;
- deployment compatibility; and
- prohibition of semantic reinterpretation.

They intentionally do not select the concrete initial production mechanism by which those definitions reach a running backend.

Without that decision, independent implementations could legitimately choose materially different architectures:

```text
database-backed live registry
remote semantic service
object-store lookup
compiled Java definitions
filesystem bundle
backend-packaged immutable bundle
implicit current-version bootstrap
```

Some of those choices introduce materially different startup, failure, provenance, deployment and historical-support behaviour.

This authority closes that gap.

It governs:

- production semantic-definition packaging;
- deployment materialisation metadata;
- startup validation;
- deterministic decoding;
- immutable assembly publication;
- exact lookup;
- process-local materialisation readiness;
- current-active release coverage;
- rolling deployment compatibility;
- relationship to Merchant Configuration activation;
- historical semantic reconstruction;
- bootstrap failure;
- recovery compatibility; and
- the boundary with ADR-012 executable support.

---

## 3. Explicit Non-Goals

This authority does **not**:

- redefine Semantic Registry Release semantics;
- define semantic migration;
- create Semantic Registry Releases;
- approve or publish Semantic Registry Releases;
- define Merchant Configuration;
- create, approve or activate Configuration Revisions;
- define configuration release identity;
- redefine the RCP;
- establish Actor Authorisation;
- establish Commercial Entitlement;
- establish Provider Readiness;
- establish Surface Exposure;
- make semantic bundles business truth;
- make semantic bundles executable code;
- define every future semantic-release storage technology;
- require every historical release to be eagerly loaded;
- define final CI/CD tooling;
- prescribe a particular archive encoding;
- prescribe a specific parser library;
- prescribe Java/Spring class names;
- define compatibility-aware routing technology; or
- authorise implementation.

---

## 4. Canonical Terminology

### 4.1 Packaged Semantic Definition Bundle

A **Packaged Semantic Definition Bundle** is:

> An immutable deployment artefact containing or carrying exact evidence sufficient to deterministically reconstruct the release-affined semantic definition state required to construct one `SemanticReleaseAssembly`.

A bundle corresponds to exactly one Semantic Registry Release.

A bundle does not create that release.

### 4.2 Deployment Semantic Materialisation Set

A **Deployment Semantic Materialisation Set** is:

> The finite immutable set of exact Semantic Registry Releases whose definition bundles are carried by a particular deployed backend artefact and can therefore be materialised by that deployment.

Conceptually:

```text
Deployment Semantic Materialisation Set
{
    release A
    release B
    release C
}
```

Membership means:

```text
definition material can be reconstructed
```

It does **not** mean:

```text
every contract in the release is executable
```

### 4.3 Semantic Materialisation Repository

A **Semantic Materialisation Repository** is the immutable process-local repository of successfully validated `SemanticReleaseAssembly` instances.

Its lookup key is the exact Semantic Registry Release identifier.

Its lookup semantics are exact-only.

### 4.4 Semantic Materialisation Readiness

**Semantic Materialisation Readiness** is the technical predicate that the declared deployment bundle set has been successfully validated, decoded and atomically published as a coherent process-local repository.

It is not Actor Authorisation, Provider Readiness, Commercial Entitlement or business availability.

### 4.5 Deployment Semantic Coverage

**Deployment Semantic Coverage** is the deployment-level predicate that the serving deployment can materialise every exact currently active Semantic Registry Release that requires runtime semantic-definition availability.

For Main Street's initial ordinary serving topology, the stricter rule in Section 11 applies.

### 4.6 Executable Contract Support

**Executable Contract Support** retains exactly the meaning established by ADR-012.

It is contract-scoped evidence that an implementation can safely execute the required semantic/RCP execution contract and associated participant/effect contracts.

It MUST NOT be reduced to a release-level boolean.

---

## 5. Semantic and Architectural Ownership

Ownership remains:

```text
Published Semantic Registry Release
    → semantic publication authority

Published Semantic Definition Set
    → ADR-011 publication/evidence boundary

SemanticReleaseAssembly
    → ADR-010 coherence architecture

Packaged bundle validation/materialisation
    → bootstrap infrastructure

Current Configuration Revision
    → Merchant Configuration authority

semantic release reference used by that Configuration Revision
    → Merchant Configuration reference to semantic authority

Executable Contract Support
    → ADR-012 execution-compatibility architecture

business operation
    → owning capability
```

Bootstrap infrastructure MUST NOT acquire semantic ownership merely because it stores or reconstructs definitions.

---

## 6. Bundle Contract

Each bundle MUST logically contain or establish:

```text
bundle format version
exact Semantic Registry Release identifier
content-integrity digest
published definition evidence
publication provenance
definition material sufficient to reconstruct
    Semantic Registry snapshot
    Surface Contribution Registry snapshot
    Fulfilment Contract Registry snapshot
    where those registries participate in that release
```

The exact serialization format is an implementation detail provided the accepted semantics remain equivalent.

### 6.1 One-release invariant

One bundle represents exactly one Semantic Registry Release.

A multi-release archive MAY physically contain several bundles, but each release remains independently identifiable and verifiable.

### 6.2 Integrity invariant

The bundle's actual contents MUST match its recorded integrity evidence.

A digest mismatch is a hard bootstrap defect.

### 6.3 Identity invariant

The release identity declared by bundle metadata MUST equal the release identity reconstructed from its contained publication evidence.

A mismatch MUST fail.

### 6.4 Provenance invariant

A deployment build MUST package definition evidence originating from an accepted Published Semantic Registry Release.

Packaging must not create provenance by assertion.

### 6.5 Authenticity boundary

A content digest proves integrity relative to the recorded bundle evidence; it does not independently prove who produced the deployment artefact.

Deployment-artifact authenticity remains a responsibility of the trusted build/deployment supply chain.

A future signing mechanism MAY strengthen that boundary without changing this architecture. That question is catalogued as `ADR-013-DQ-007`.

---

## 7. Prohibited Bundle Content

A Semantic Definition Bundle MUST NOT contain live merchant or customer authority.

It MUST NOT contain:

```text
Merchant Configuration instances
active configuration pointers
merchant operational state
customer operational state
payment-provider live state
provider credentials
API secrets
authentication credentials
personal operational data
session state
live stock
orders
bookings
appointments
payment evidence
notification delivery state
```

Semantic definition packaging and operational data remain separate.

---

## 8. Build and Publication Boundary

The production packaging path is:

```text
accepted published release evidence
        ↓
trusted build input
        ↓
deterministic bundle construction
        ↓
integrity metadata
        ↓
backend deployment artefact
```

The build process MAY package:

- the currently required release;
- several simultaneously active releases;
- a release intended for imminent configuration activation;
- selected historical releases required for execution/reconstruction.

It MUST NOT package a mutable alias such as:

```text
latest
current
default
production
```

as a substitute for exact release identity.

---

## 9. Bootstrap Lifecycle

At process bootstrap:

```text
process initialises
        ↓
read declared Deployment Semantic Materialisation Set
        ↓
for each bundle
    validate bundle-format compatibility
    validate exact release identity
    validate content integrity
    validate publication provenance
    deterministically decode definition evidence
    construct release-affined registry snapshots
    construct SemanticReleaseAssembly
        ↓
validate cross-registry release coherence
        ↓
reject duplicate release identity
        ↓
construct complete immutable repository
        ↓
atomically publish repository
        ↓
Semantic Materialisation Readiness = SATISFIED
```

No partially constructed repository may become visible to runtime consumers.

---

## 10. Exact Repository Resolution

Repository resolution is:

```text
resolve(exactSemanticReleaseIdentifier)
```

Possible outcomes:

```text
EXACT ASSEMBLY FOUND
or
EXACT ASSEMBLY NOT MATERIALISED
```

There is no:

```text
nearest
latest
fallback
compatible-enough
previous
default
```

resolution.

Absence is evidence of absence from that process's materialisation set, not permission to reinterpret the request.

---

## 11. Initial Production Serving-Cohort Rule

For the initial Main Street production topology:

> **Processes eligible to receive ordinary unpartitioned merchant traffic within the same serving cohort MUST expose the same Deployment Semantic Materialisation Set and the same content identity for every shared Semantic Registry Release identifier.**

This deliberately favours operational simplicity over compatibility-aware request routing.

Therefore, if currently active Merchant Configurations reference:

```text
Semantic Release R17
Semantic Release R18
```

every ordinary process in that unpartitioned serving cohort must be capable of materialising both R17 and R18.

A process carrying:

```text
R17 → digest A
```

and another process carrying:

```text
R17 → digest B
```

is a hard deployment integrity defect.

Compatibility-aware partitioning/routing MAY later relax uniform materialisation across cohorts, but only under ADR-012-compatible architecture. That question is catalogued as `ADR-013-DQ-010`.

---

## 12. Current Active Configuration Coverage

Configuration Release identity and Semantic Registry Release identity are distinct.

The authoritative relationship is conceptually:

```text
CURRENT_ACTIVE Merchant Configuration
        ↓
exact Configuration Revision / Release
        ↓
exact Semantic Registry Release reference
```

The bootstrap system MUST NOT infer semantic release identity from a Configuration Release identifier.

Before an initial ordinary serving cohort is admitted for unrestricted merchant traffic, deployment validation MUST establish that every distinct currently active Semantic Registry Release reference required by those merchants exists in that cohort's Deployment Semantic Materialisation Set.

This validation:

- reads configuration authority;
- does not mutate configuration;
- does not activate configuration;
- does not redefine semantic meaning.

---

## 13. Configuration Activation After Startup

A configuration may become active after backend startup.

Therefore startup validation alone cannot preserve the deployment invariant.

The later configuration-bootstrap/activation design MUST consume an exact deployment-support boundary equivalent to:

```text
Can this intended semantic context be safely admitted
by the current serving deployment?
```

Target 1 establishes the required dependency:

```text
proposed Configuration activation
        ↓
exact semantic release reference
        ↓
deployment materialisation availability
        +
ADR-012 executable-contract support
        ↓
activation eligibility consideration
```

Target 1 does **not** own the activation decision.

Target 3 — Initial merchant configuration bootstrap — remains responsible for designing the exact activation transaction/admission contract.

A configuration MUST NOT become current merely because its semantic release exists somewhere in historical storage.

This Target 3 dependency is an explicitly scheduled downstream design obligation, not a deferred ADR-013 question.

---

## 14. Definition Materialisation vs Executable Support

This distinction is a hard invariant.

Example:

```text
Deployment contains bundle R21
        ↓
R21 definitions materialise successfully
```

This establishes:

```text
R21 MATERIALISABLE
```

It does not establish:

```text
all R21 operations executable
```

ADR-012 may establish only:

```text
R21 / contract A → supported
R21 / contract B → supported
R21 / contract C → unsupported
```

Therefore:

```text
materialisation availability
        +
exact execution-context affinity
        +
ADR-012 contract support
        =
candidate executable semantic path
```

No release-level `supported = true` flag may replace contract-scoped evidence.

---

## 15. Invocation-Time Admission

Every configuration-dependent invocation continues to bind its exact semantic/RCP execution context as governed by ADR-012.

Where source semantic materialisation is required:

```text
incoming invocation
        ↓
exact semantic/RCP context
        ↓
exact assembly lookup
        ↓
exact executable-contract support check
        ↓
other runtime admission dimensions
        ↓
capability-owned execution
```

If exact semantic material is unavailable:

```text
do not substitute another release
```

The invocation must instead be:

- rejected safely; or
- routed to a compatible execution path where an accepted routing mechanism exists.

---

## 16. Retained RCP and Historical Execution

ADR-011's retained-RCP exception survives unchanged.

An exact retained RCP/evidence path MAY satisfy an operation without reconstructing the Published Semantic Definition Set when the operation genuinely does not require source-release reconstruction.

Therefore:

```text
historical configuration reference
        ≠
automatic requirement to eagerly load historical bundle
```

If a historical operation **does** require exact source semantic reconstruction, its exact release must be materialisable somewhere on an admissible execution path.

Failure to materialise it does not permit reinterpretation using a newer release.

---

## 17. Rolling Deployment

During a rolling deployment:

```text
old serving cohort
        +
new serving cohort
```

may coexist.

ADR-012's no-orphan rule remains authoritative.

Under the initial uniform-serving model, each cohort receiving unrestricted merchant traffic MUST independently cover the currently required active semantic-release set.

A release may be removed from a new deployment only when removal cannot orphan:

- current execution;
- residual execution;
- required historical execution;
- background work;
- callbacks;
- recovery requirements; or
- another still-supported execution contract.

If routing later becomes compatibility-aware, a cohort MAY carry a narrower set only when admission/routing prevents incompatible invocations from reaching it.

---

## 18. Background Work and Deferred Execution

A background task, timer, callback or delayed operation may execute after the deployment that originally accepted the business intent has been replaced.

Such work MUST preserve its governed semantic/configuration affinity.

At execution:

```text
durable work
        ↓
exact historical context
        ↓
exact materialisation requirement if any
        ↓
ADR-012 executable support
        ↓
authoritative revalidation
        ↓
execution
```

Deployment turnover MUST NOT cause durable work to be silently reinterpreted under the newest semantic release.

---

## 19. Recovery Compatibility

Recovery remains governed by MS-TAS-RECOVERY-001.

Recovery artefacts MUST preserve the exact semantic identities necessary to reconstruct recovered authoritative state.

A Recovery Release Bundle MUST therefore contain or make available the exact semantic-definition material required by recovered execution contexts.

Recovery MUST reject:

- release identity mismatch;
- content integrity mismatch;
- missing required exact release;
- incompatible executable contract;
- reconstruction under a newer semantic release.

Recovery does not create a new Semantic Registry Release merely because an old deployment artefact is unavailable.

---

## 20. Failure Semantics

The bootstrap architecture distinguishes:

### `UNSUPPORTED_BUNDLE_FORMAT`

The deployed binary cannot deterministically interpret the bundle format.

Result: hard bootstrap/deployment defect.

### `CONTENT_INTEGRITY_FAILURE`

Bundle content does not match recorded integrity evidence.

Result: hard bootstrap/deployment defect.

### `RELEASE_IDENTITY_MISMATCH`

Declared and reconstructed semantic release identities differ.

Result: hard bootstrap/deployment defect.

### `DUPLICATE_RELEASE_IDENTITY`

More than one bundle claims the same Semantic Registry Release within one Deployment Semantic Materialisation Set.

Result: hard bootstrap/deployment defect.

Duplicate identifiers are rejected even when their digests happen to match.

### `DEFINITION_DECODE_FAILURE`

Published definition evidence cannot be deterministically reconstructed.

Result: hard bootstrap/deployment defect.

### `ASSEMBLY_COHERENCE_FAILURE`

Release-affined registry snapshots cannot form the coherent assembly required by ADR-010.

Result: hard bootstrap/deployment defect.

### `REQUIRED_RELEASE_NOT_MATERIALISED`

An exact release required by an execution or deployment-coverage check is absent.

Result: affected deployment/invocation is not admitted.

### `EXECUTION_CONTRACT_UNSUPPORTED`

Definitions exist, but ADR-012 cannot prove the requested exact execution contract.

Result: affected invocation/deployment path is not admitted.

### `ACTIVE_REQUIREMENT_DISCOVERY_UNAVAILABLE`

Authoritative current Configuration requirements cannot be determined during deployment validation.

Result: current semantic coverage is unknown and unrestricted serving admission MUST NOT be asserted.

---

## 21. Retry and Idempotency

Bootstrap over one immutable deployment artefact is deterministic and idempotent.

Repeated successful bootstrap over identical bytes MUST produce materially equivalent immutable assemblies.

A deterministic artefact defect such as:

```text
digest mismatch
identity mismatch
unsupported format
decode failure
coherence failure
```

will not become valid through blind retry.

Transient inability to read an authoritative external dependency used by deployment-admission validation MAY be retried according to the governing resilience architecture.

Retry MUST NOT change release identity or substitute different semantic evidence.

---

## 22. Concurrency and Atomic Publication

Bootstrap assembly construction MUST have an all-or-nothing publication boundary.

Runtime consumers observe either:

```text
no published materialisation repository
```

or:

```text
one complete immutable validated repository
```

They MUST NOT observe partially loaded release sets.

After publication, the process-local materialisation repository is immutable for that process lifetime.

Supporting a different materialisation set requires a new deployment/process generation rather than mutation of the live repository.

---

## 23. Security and Data-Protection Boundary

Semantic bundles are deployment artefacts, not operational-data containers.

They MUST be safe to handle as architecture/configuration artefacts without embedding:

- secrets;
- session credentials;
- merchant operational data;
- customer personal data; or
- provider credentials.

Build/deployment compromise remains a security concern separate from semantic content integrity.

Logs and diagnostics MUST NOT dump arbitrary bundle contents where those contents could expose non-public platform definition material unnecessarily.

---

## 24. Provider, Entitlement, Authorisation and Exposure Boundary

Semantic materialisation does not establish:

```text
Provider Readiness
Commercial Entitlement
Actor Authorisation
Operational Eligibility
Surface Exposure
```

Those dimensions remain independently governed.

A release may be materialised successfully while a particular operation is still unavailable for any of those reasons.

---

## 25. Falsification Cases

### 25.1 Two simultaneously active semantic releases

R17 and R18 are referenced by current merchants.

**Result:** survives. Both bundles may coexist in one immutable materialisation set and are resolved exactly.

### 25.2 Same release identifier, different content

Two deployment processes claim R17 with different digests.

**Result:** fail deployment integrity. Semantic release immutability prohibits both interpretations.

### 25.3 Historical operation does not require source reconstruction

Historical R12 is not packaged, but exact retained RCP evidence is sufficient.

**Result:** survives. ADR-011 retained-RCP path remains valid.

### 25.4 Historical operation requires source definitions

Historical R12 is not materialisable anywhere.

**Result:** operation is not admitted. R13 or `latest` MUST NOT substitute.

### 25.5 New configuration references R19 after startup

Current deployment only materialises R17/R18.

**Result:** startup success does not authorise R19 activation. Target 3 must use the deployment-support boundary before activation.

### 25.6 Corrupt bundle

Digest verification fails.

**Result:** bootstrap fails deterministically. No partial repository is published.

### 25.7 Bundle materialises but implementation does not support operation

R18 is present, but one historical execution contract is unsupported.

**Result:** ADR-012 rejects/reroutes that contract. Materialisation does not manufacture executable support.

### 25.8 Rolling deployment removes old release too soon

New cohort removes R17 while current or residual R17 execution still exists.

**Result:** deployment admission fails unless a compatible routed execution path remains.

### 25.9 Background work survives deployment turnover

A delayed R16 operation wakes after R18 deployment.

**Result:** it preserves its exact affinity. It is never reinterpreted as R18 work.

### 25.10 Active-configuration query unavailable during deployment admission

Backend bundles are otherwise valid.

**Result:** materialisation bootstrap may be structurally valid, but unrestricted deployment coverage cannot be asserted.

### 25.11 Unbounded historical release growth

Hundreds of old releases exist.

**Result:** no requirement to eagerly package every historical release. Only releases required by the deployment's admitted execution responsibilities need runtime materialisation.

### 25.12 Recovery contains semantic mismatch

Recovered configuration references R14 but recovery semantic artefact claims R14 with different content.

**Result:** recovery fails closed rather than reconstructing altered history.

### 25.13 Bundle accidentally contains merchant data

A bundle includes merchant configuration or customer data.

**Result:** architecture violation. Semantic bundles are definition artefacts only.

### 25.14 `latest` release convenience fallback

Requested release is absent but the current deployment carries a newer release.

**Result:** explicit rejection. No fallback path exists.

---

## 26. Alternatives Considered

### A. Backend-packaged immutable bundles — SELECTED

Advantages:

- deterministic startup;
- strongest coupling between executable binary and known definition material;
- simple deployment reasoning;
- no runtime network dependency for semantic definitions;
- exact immutable provenance;
- easy multi-release support;
- straightforward recovery artefact composition.

Cost:

- adding newly materialisable semantics requires deployment artefact promotion;
- deployment package grows with included releases.

For Main Street's initial modular-monolith architecture, this is the best trade-off.

### B. Operational-database semantic registry — REJECTED FOR INITIAL PRODUCTION

Advantages:

- semantic publication can occur independently of binary deployment.

Disadvantages:

- bootstrap becomes dependent on mutable operational infrastructure;
- weaker separation between published immutable evidence and live database state;
- more complicated recovery and provenance;
- creates an unnecessary availability dependency.

It may be reconsidered if independent semantic publication cadence becomes operationally necessary.

### C. Dedicated semantic-registry service / remote object retrieval — DEFERRED

Advantages:

- independent publication;
- potentially efficient large-history retention.

Disadvantages:

- network/bootstrap dependency;
- caching and consistency complexity;
- additional service topology;
- routing and availability concerns that Main Street does not currently need.

This deferred option is catalogued as `ADR-013-DQ-009` together with the related remote archival-storage question `ADR-013-DQ-008`.

### D. Compile definitions directly into Java code — REJECTED

Advantages:

- trivial runtime lookup.

Disadvantages:

- weak separation between definition evidence and executable implementation;
- historical reconstruction becomes opaque;
- provenance is harder to inspect;
- risks turning implementation code into semantic publication authority.

### E. Runtime fallback to remote history when a bundle is missing — REJECTED FOR INITIAL PRODUCTION

Automatic remote fallback adds hidden runtime dependency and weakens deterministic deployment reasoning.

A future explicit historical-materialisation service may be designed, but runtime MUST NOT silently fetch and reinterpret arbitrary semantic history as a fallback. Dynamic historical materialisation is catalogued as `ADR-013-DQ-011`.

---

## 27. Trade-Offs

The selected architecture intentionally couples the **materialisable semantic-definition set** to deployment promotion.

This sacrifices independent release-materialisation cadence in exchange for:

- deterministic startup;
- simple provenance;
- predictable rollback;
- simple recovery;
- no runtime semantic-service dependency;
- strong exact-release integrity.

The architecture does **not** couple semantic meaning to the application binary.

The published semantic release remains independently authoritative; the backend bundle is only a deployment materialisation of that authority.

---

## 28. Deferred Question Catalogue

Every question deferred by ADR-013 has a stable identifier and is mirrored in `designs/DEFERRED-DECISION-REGISTER.md` for future traceability.

A catalogued item is **not automatically an active design target**. It becomes active only when its revisit condition is met and the governing lifecycle class requires a design decision rather than an implementation choice.

| ID | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|
| ADR-013-DQ-001 | Exact bundle encoding / serialization representation | Implementation detail — production bootstrap implementation | Before the first production bundle codec is selected or interoperability makes the representation architecturally material |
| ADR-013-DQ-002 | Exact cryptographic digest algorithm | Security/build implementation detail unless policy makes it architectural | Before production integrity metadata is finalised, or earlier if security/compliance policy constrains the algorithm |
| ADR-013-DQ-003 | Archive compression | Implementation/operations detail | When deployment artefact size, startup latency or distribution cost makes compression material |
| ADR-013-DQ-004 | Exact filesystem/classpath/resource layout | Runtime packaging implementation detail | When the concrete bundle-loading implementation is selected |
| ADR-013-DQ-005 | Build-plugin implementation | Build engineering implementation detail | When automated deterministic bundle construction is implemented |
| ADR-013-DQ-006 | CI publication/promotion workflow | Release engineering implementation detail | Before production release automation must publish/promote semantic bundles |
| ADR-013-DQ-007 | Deployment artefact signing / KMS-backed authenticity mechanism | Security architecture candidate | When threat modelling, compliance, multi-party distribution or supply-chain assurance requires authenticity beyond digest integrity |
| ADR-013-DQ-008 | Remote archival store technology for retained semantic releases | Production/recovery architecture candidate | When deployment-packaged artefacts alone are insufficient for required retention, recovery or historical reconstruction scale |
| ADR-013-DQ-009 | Dedicated semantic-registry service / remote definition retrieval | Platform topology architecture candidate | When independent semantic publication cadence, scale or topology demonstrates that deployment packaging is materially constraining |
| ADR-013-DQ-010 | Compatibility-aware request routing / serving-cohort partitioning | Runtime/deployment architecture candidate | When uniform serving-cohort materialisation becomes operationally too restrictive and mixed support cohorts are required |
| ADR-013-DQ-011 | Dynamic historical semantic materialisation | Historical execution architecture candidate | When required historical release volume makes packaging all required runtime materialisation impractical |
| ADR-013-DQ-012 | Automatic safe semantic-release retirement | Release-lifecycle architecture candidate | When manual retirement is operationally material and the no-orphan/recovery safety predicates can be proven mechanically |
| ADR-013-DQ-013 | Exact observability dashboard/alerting for semantic materialisation state | Observability/operations implementation detail | When production support requires a dedicated operational view beyond baseline logs/metrics/health evidence |

### 28.1 Traceability rule

For every `ADR-013-DQ-*` item:

1. the identifier MUST remain stable;
2. status changes MUST be reflected in the canonical Deferred Decision Register;
3. promotion to material design work MUST follow `DESIGN-RULES.md`;
4. an implementation-detail item MAY be closed under `IMPLEMENTATION-RULES.md` without inventing new semantic authority, provided no material architecture choice is discovered;
5. a resolving authority or implementation evidence reference MUST replace the deferred status when resolved; and
6. closure MUST NOT retroactively change ADR-013's accepted invariants unless a governed amendment explicitly does so.

### 28.2 Explicit downstream dependency — not deferred

Target 3's exact Configuration activation/admission contract is not one of these deferred questions. It is already scheduled by MS-PROT-079 and MUST consume ADR-013 materialisation availability together with ADR-012 executable-contract support.

---

## 29. Implementation Constraints

Later implementation MUST preserve:

```text
one exact release identity per bundle
immutable packaged definition evidence
deterministic validation
deterministic decoding
exact assembly coherence
immutable repository after publication
exact-only lookup
no latest/default fallback
materialisation ≠ executable support
no merchant/customer/provider live authority in bundles
configuration activation remains separately owned
```

Class names, package structure, serialization libraries and framework hooks remain implementation details.

---

## 30. Conformance / Acceptance Criteria

Target 1 is Design-Closed only if the accepted architecture ensures:

```text
[ ] exact Semantic Registry Release identity is preserved
[ ] published releases remain immutable
[ ] one bundle corresponds to one exact release
[ ] bundle integrity is verifiable
[ ] publication provenance survives packaging
[ ] decoding is deterministic
[ ] SemanticReleaseAssembly coherence is validated
[ ] process-local repository is immutable
[ ] repository publication is atomic
[ ] lookup is exact-only
[ ] no latest/nearest/default fallback exists
[ ] multiple active releases may coexist
[ ] current active release coverage is checked
[ ] Configuration Release ID is not confused with Semantic Registry Release ID
[ ] post-startup configuration activation has an exact support dependency
[ ] bootstrap infrastructure does not own configuration activation
[ ] materialisability is separate from ADR-012 executable support
[ ] executable support remains contract-scoped
[ ] historical retained-RCP paths remain permitted
[ ] required historical reconstruction fails closed when exact material is unavailable
[ ] rolling deployment cannot orphan required execution
[ ] durable/background execution preserves semantic affinity
[ ] recovery preserves exact semantic meaning
[ ] bundles exclude operational authority/secrets/personal data
[ ] prototype fixtures have no production authority
[ ] every deferred ADR-013 question has a stable catalogue identifier and DDR entry
```

All criteria are satisfied at the design-authority level by this accepted decision and its governance formalisation. Implementation conformance remains future work under `IMPLEMENTATION-RULES.md`.

---

## 31. Amendment / Supersession Effect

ADR-013:

1. selects backend-packaged immutable semantic-definition bundles as Main Street's initial production Semantic Registry Release materialisation mechanism;
2. preserves MS-PROT-054 as semantic authority;
3. preserves ADR-010 as `SemanticReleaseAssembly` authority;
4. preserves ADR-011's publication, retention and reconstruction rules;
5. preserves ADR-012's contract-scoped execution-support model;
6. preserves MS-TAS-RECOVERY-001 recovery authority;
7. establishes the Deployment Semantic Materialisation Set;
8. establishes immutable exact-release process-local materialisation;
9. establishes the initial uniform-serving-cohort rule;
10. requires current-active exact-release coverage before unrestricted serving admission;
11. establishes the materialisation-support dependency that Target 3 must consume during configuration activation design;
12. prohibits release-level executable-support inference;
13. prohibits implicit semantic-release fallback;
14. establishes stable traceability identifiers for every deferred ADR-013 question; and
15. closes MS-PROT-079 Target 1 after authority-index, DDR and corpus-conformance completion.

The next active design target is therefore:

```text
Target 2
Authentication / session establishment
```
