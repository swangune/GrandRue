# ADR-012 — Runtime Semantic Execution & Deployment Compatibility

> **ADR ID:** ADR-012  
> **Version:** 1.0  
> **Status:** Accepted  
> **Date:** 26 August 2026  
> **Owner:** Architecture Team  
> **Approved:** Manual approval on 26 August 2026 to carry out the final governed EAC-002 recommendation  
> **Depends on:** MS-PROT-023, MS-PROT-040 v1.1, MS-PROT-054, MS-PROT-065, MS-PROT-069, MS-PROT-070, MS-PROT-072, ADR-010, ADR-011  
> **Closes:** EAC-002 — Runtime Semantic Execution Support & Deployment Compatibility  
> **Purpose:** Define the runtime/deployment architecture by which an invocation bound to an exact semantic/RCP context is executed only through an implementation path proven to support the affected semantic execution contract and required participant/effect contracts, without making current deployment/class identity implicit semantic authority or requiring every process to execute every historical context.

---

## 1. Context

MS-PROT-054 establishes that application deployment version and Semantic Registry Release identity are distinct and that historical execution/interpretation support may remain required after later releases exist.

MS-PROT-040 v1.1 requires every configuration-dependent invocation to bind the exact applicable RCP before execution. ADR-010 and ADR-011 preserve exact release-affined static definition resolution.

Those rules establish what semantics apply; they do not by themselves prove that the currently reached executable implementation conforms to those semantics.

The EAC-002 review and two falsification passes rejected two over-broad implementation interpretations:

1. one release-global `compatible=true/false` flag is too coarse because compatibility is reference-/contract-scoped; and
2. every serving instance does not need to support every historical context if a safe architecture can route or isolate the exceptional execution path.

The surviving invariant is narrower: **an invocation may execute only on an implementation path that supports its exact affected semantic execution contract, and Main Street must not knowingly orphan the last safe path for a contract that accepted authority still requires to be executable.**

---

## 2. Problem Statement

How shall a Main Street runtime establish that the implementation selected for one invocation can correctly honour the invocation's exact accepted semantic/RCP contract across application deployments, historical commitments, rolling upgrades and cross-capability execution without silently applying newer behaviour or unnecessarily cloning the entire platform per release?

---

## 3. Scope

ADR-012 governs:

- executable-support resolution for configuration-dependent invocations;
- contract-scoped support evidence;
- cross-capability participant/effect support;
- process/instance admission for an invocation;
- deployment planning against still-required execution contracts;
- durable/background work after deployment;
- missing/unproven execution support;
- bounded degradation when one support path is unavailable.

---

## 4. Explicit Non-Goals

ADR-012 does **not** define:

- semantic compatibility/migration dispositions owned by MS-PROT-054;
- the business semantics of any capability operation;
- one mandatory Java annotation, registry, class hierarchy or Spring mechanism;
- one mandatory routing/load-balancing topology;
- one deployment per Semantic Registry Release;
- permanent retention of unsafe historical code;
- a generic workflow/saga engine;
- Semantic Release publication/retention governed by ADR-011; or
- a business-visible universal execution-support status.

---

## 5. Options Considered

### Option A — Always call the current implementation

Bind the exact RCP, then dispatch to whatever handler is current for the operation identifier.

**Rejected.** A newer deployment would become implicit semantic reinterpretation authority.

### Option B — One whole application deployment per semantic release

Retain and route each historical release to a complete old application deployment.

**Rejected as the universal architecture.** It duplicates the entire platform even where one generic implementation correctly interprets several semantic contexts or only a narrow operation differs.

### Option C — Contract-scoped executable support with explicit historical adaptation where required

Bind the exact semantic/RCP context and resolve an implementation path whose support evidence covers the affected semantic execution contract plus required participant/effect contracts. One implementation may support multiple contexts when conformance is established; incompatible historical behaviour may use a narrow adapter/implementation path.

**Accepted.**

### Option D — Dynamically start arbitrary old binaries on demand

**Not selected as the initial architecture.** It may remain an exceptional future implementation technique but is not the governing model.

---

## 6. Decision

Every configuration-dependent invocation MUST bind:

```text
exact semantic / RCP context
        +
exact affected semantic execution contract
        +
required participant/effect contracts
        ↓
executable implementation path with proven support
        ↓
execution
```

An executable implementation path MUST NOT be selected solely because its operation identifier, Java class, package, bean name or deployment is current.

If required support is absent or unproven, the process MUST NOT execute or reinterpret the invocation.

---

## 7. Canonical Definitions

### 7.1 Semantic Execution Contract

`Semantic Execution Contract` means the exact accepted operation/effect/relationship/configuration semantics that determine the authoritative behaviour of one invocation in its bound semantic/RCP context.

The execution contract is semantic authority owned by accepted MS-PROT documents; ADR-012 does not redefine it.

### 7.2 Executable Support Evidence

`Executable Support Evidence` means implementation/conformance evidence establishing that a particular executable path can honour a defined semantic execution contract and its required participant/effect contracts.

Executable Support Evidence is not semantic authority and cannot manufacture compatibility that accepted semantics do not establish.

### 7.3 Executable Implementation Path

`Executable Implementation Path` means the concrete runtime path selected to perform the invocation, including all required capability-owned participants/effects whose execution is necessary for that authoritative operation.

### 7.4 Execution-Support Orphaning

A still-required execution contract is `orphaned` only when all are true:

1. accepted current authority still requires an operation to remain executable for new activity or an existing commitment;
2. the exact semantic/RCP context is known;
3. no currently available or recoverable executable implementation path can conform to the required contract; and
4. no accepted safety restriction, semantic migration, compensation/remediation or manual-intervention outcome intentionally supersedes execution.

---

## 8. Contract-Scoped Support

Executable support MUST be reference-/contract-scoped rather than one coarse release-global boolean.

Rejected as governing proof:

```text
release R8 supported = true
```

when R8 contains materially independent semantic contracts whose implementation support may differ.

A support representation MAY use a release identifier as an index or optimisation, but a release-wide declaration MUST NOT hide a narrower unsupported affected contract.

---

## 9. One Implementation May Support Multiple Contexts

One implementation MAY serve multiple semantic contexts when the implementation is deliberately generic or semantically equivalent for the affected contracts and conformance evidence establishes the required behaviour.

Example:

```text
R8 booking.confirm
R9 booking.confirm

material difference represented entirely by exact bound RCP data
        ↓
one generic implementation interprets that accepted data correctly
```

ADR-012 does not require a historical adapter merely because release identifiers differ.

---

## 10. Material Semantic Difference

Where a historical semantic execution contract requires behaviour that the current general implementation cannot correctly provide, Main Street MUST retain or provide a conforming execution path while accepted authority still requires safe execution.

That path MAY be:

- a versioned capability implementation;
- a narrow historical adapter;
- a compatibility-aware isolated runtime path;
- an intent-preserving migration already accepted and completed; or
- another implementation that demonstrably conforms.

The architecture MUST NOT silently execute the invocation under materially different newer semantics.

---

## 11. Cross-Capability Participant Support

Support validation MUST include every required participant/effect contract whose participation is necessary for the invoked authoritative operation.

If:

```text
Order commit
    requires Ordering contract O7
    + Inventory claim contract I4
    + Money obligation contract M2
```

then proof that the Ordering entry handler supports `O7` is insufficient if the selected Inventory or Money participant cannot honour `I4` or `M2`.

Application orchestration remains progression authority under MS-PROT-072; each participating capability retains its own business truth.

---

## 12. Process/Instance Admission

A process or instance MUST reject an invocation when the required Executable Support Evidence for that invocation's exact execution contract is absent or invalid.

Rejected:

```text
unsupported process receives R8 invocation
        ↓
falls through to newest handler
```

Allowed architectures include:

```text
A. homogeneous pool
   every process in the pool supports every context routed to the pool

B. compatibility-aware routing
   an invocation is routed only to a compatible process/path

C. isolated historical adapter/path
   exceptional context is executed through a narrower compatible path
```

ADR-012 does not select one topology.

---

## 13. Deployment Admission and Planning

Before intentionally retiring an executable path, deployment planning MUST determine whether accepted active/residual authority still requires the affected execution contract.

Main Street MUST NOT knowingly create Execution-Support Orphaning.

The deployment gate is therefore not:

```text
every process must support every historical release
```

It is:

```text
for every still-required execution contract
        ↓
there remains at least one safe, reachable, governed execution outcome
        OR
an accepted migration/restriction/remediation/manual-intervention outcome
has already taken effect
```

A deployment MAY remain partially available where unaffected operations and contexts remain safe.

---

## 14. Unexpected Support Gap

If a support gap occurs unexpectedly after deployment:

- the affected invocation MUST NOT execute through an unproven path;
- the affected scope MUST fail closed, become explicitly unavailable, or enter an intervention/remediation path according to accepted authority;
- unrelated operations MUST remain eligible according to their own authorities unless a higher security/privacy/integrity/platform authority requires broader restriction.

A support gap MUST NOT be reported as successful business execution.

---

## 15. Durable and Background Work

A durable/background instruction governed by MS-PROT-065 MUST preserve or recover the semantic/configuration affinity required by its owning operation before executable support is resolved.

Rejected:

```text
work created under R8
application deploys R12
work wakes
        ↓
invoke latest implementation without resolving R8 contract
```

Required:

```text
durable work wakes
        ↓
recover owning semantic/configuration context
        ↓
resolve exact execution contract
        ↓
resolve compatible executable path
        ↓
revalidate current authoritative conditions required by owning operation
        ↓
execute or fail/degrade under governing authority
```

---

## 16. Safety Precedence

Historical execution support does not override current accepted security, privacy, legal, integrity or platform-safety restrictions.

If retaining the literal historical implementation would violate a current higher-order safety authority, Main Street MUST NOT execute unsafe code merely because a historical commitment exists.

A safe outcome MAY require:

- intent-preserving migration;
- bounded restriction;
- compensation/remediation;
- manual intervention; or
- another accepted resolution.

The historical commitment remains business truth even when unsafe execution is prohibited.

---

## 17. Implementation Evidence Is Not Semantic Authority

The following MAY participate in implementation support declarations but MUST NOT themselves create semantic compatibility:

```text
annotations
configuration files
manifests
class names
bean names
package names
deployment tags
test suites
```

Tests and declarations are conformance evidence. Accepted semantic authority determines the contract they must satisfy.

---

## 18. Failure and Rejection Semantics

ADR-012 distinguishes:

### Semantic/contract support rejection

The invocation's exact required execution contract is known but no compatible executable path is available/proven. Execution MUST NOT occur through another contract.

### Deployment/configuration defect

Deployment planning knowingly or accidentally leaves a still-required execution contract without an accepted safe execution/remediation path. The defect MUST be surfaced operationally and corrected; unrelated scope need not fail unless required by higher authority.

### Technical path failure

A compatible executable path exists but is temporarily unavailable. MS-PROT-069 and MS-PROT-070 govern execution uncertainty, retry and degradation; another incompatible implementation MUST NOT be used as fallback.

### Business rejection

The compatible implementation executes its accepted validation and rejects the business command. This is not an execution-support failure.

---

## 19. Retry and Idempotency

Retry does not change the required semantic execution contract.

A retry of the same logical invocation MUST preserve the same semantic/configuration affinity required by the owning operation and MUST continue to require compatible executable support.

If the first attempt may have committed but acknowledgement was lost, MS-PROT-069 execution-uncertainty and MS-PROT-059 idempotency/reconciliation rules apply. Routing a retry to an incompatible implementation is prohibited.

---

## 20. Concurrency and Rolling Deployment

During rolling deployment, multiple process versions MAY coexist.

The deployment/routing architecture MUST ensure that an invocation is not accepted by a process lacking support for its exact required execution contract.

This MAY be achieved by homogeneous pool guarantees, compatibility-aware routing, process-local rejection plus safe retry/reroute, or another accepted implementation that does not create duplicate authoritative effects.

Routing/rejection logic MUST preserve MS-PROT-059 idempotency and MS-PROT-069 uncertainty semantics.

---

## 21. Relationship to Semantic Release Support Dimensions

MS-PROT-054 distinguishes support needs including:

```text
new configuration validation
new business activity
existing commitment execution
historical interpretation
```

ADR-012 governs executable operation support only where execution is required.

A release or semantic contract MAY be:

```text
prohibited for new configuration/new activity
+
still executable for an existing commitment
```

without contradiction.

Likewise historical interpretation may require release evidence under ADR-011 without requiring executable operation support.

---

## 22. Provider Boundary

Provider compatibility/readiness remains separate from executable semantic support.

A Main Street implementation may fully support an operation contract while its external provider is currently unavailable.

Conversely, a provider may be healthy while the deployed Main Street implementation lacks support for the required historical semantic contract.

Therefore:

```text
Executable Support
    ≠
Provider Readiness
```

MS-PROT-048, MS-PROT-049 and MS-PROT-070 remain authoritative for provider participation/degradation.

---

## 23. Commercial Entitlement, Authorisation and Exposure

Executable Support MUST NOT collapse with:

```text
Semantic Applicability
Commercial Entitlement
Actor Authorisation
Operational Eligibility
Resource Protection Admission
Provider Readiness
Surface Exposure
```

An implementation can support an operation that the current actor is not authorised to perform or that the merchant is not commercially entitled to initiate for new activity.

Executable Support answers only whether the implementation path can honour the already-determined semantic execution contract.

---

## 24. Consequences

### Positive

- Deployment cannot silently reinterpret historical commitments.
- One generic implementation may legitimately support several semantic contexts.
- Historical differences can be isolated to narrow adapters rather than cloned deployments.
- Cross-capability contracts are included in compatibility checks.
- Partial deployment availability is preserved where safe.
- Durable work remains tied to its governing semantics after upgrades.

### Negative

- Deployment tooling/runtime composition must maintain explicit executable-support evidence.
- Historical execution paths may need to be retained longer than current new-activity semantics.
- Rolling deployments require admission/routing discipline where process support differs.

---

## 25. Falsification Cases Preserved

The accepted rule has survived:

- release-global compatibility being too coarse;
- one generic handler correctly serving multiple releases through exact RCP data;
- same operation identifier with materially changed behaviour;
- cross-capability operation with one incompatible participant;
- rolling deployment where one instance lacks an exceptional historical path;
- durable work waking after a deployment;
- historical implementation becoming unsafe under current security authority;
- misleading implementation declarations that do not prove semantic compatibility;
- a rare historical residual obligation that should not disable unrelated new activity.

---

## 26. Initial Implementation Constraint

The first implementation SHOULD prefer the simplest mechanism that makes support explicit and testable within the modular monolith.

It MAY use a concrete immutable support registry/manifest at application composition time, but ADR-012 does not require a generic executable-plugin framework.

The implementation SHOULD fail composition/readiness early for support defects that are statically foreseeable for the contexts assigned to that process, while preserving scoped degradation for genuinely isolated contexts.

---

## 27. Conformance Criteria

A conforming implementation MUST demonstrate at least:

1. an invocation cannot execute merely because an operation identifier matches a current handler;
2. one implementation can explicitly prove support for more than one semantic context where behaviour is conformant;
3. a materially incompatible historical contract resolves to a distinct compatible path or explicit non-execution outcome;
4. required cross-capability participant/effect support is validated;
5. a process rejects a context it cannot support;
6. a deployment plan cannot knowingly remove the last safe required path without an accepted replacement outcome;
7. unexpected support gaps affect only governed scope unless higher authority requires broader restriction;
8. durable work recovers semantic/configuration affinity before support resolution;
9. historical support does not override current security/privacy/legal/integrity restrictions; and
10. implementation metadata/test declarations remain evidence rather than semantic authority.

---

## 28. Trade-Off and Revisit Condition

Main Street chooses explicit contract-scoped support over both implicit newest-handler execution and universal full-application version retention.

This adds support metadata/routing discipline but preserves capability ownership, deployment flexibility and modular-monolith simplicity.

Revisit the physical execution topology only if operational evidence demonstrates a need for dedicated historical pools, dynamic old binaries or another deployment model. Any future topology MUST preserve exact contract binding and the prohibition on silent reinterpretation.

---

## 29. Acceptance Statement

> **Every configuration-dependent invocation executes only through a path proven to support its exact affected semantic execution contract and required participant/effect contracts. Unsupported processes do not reinterpret invocations, deployment does not knowingly orphan still-required execution contracts, and unaffected scopes remain available unless a higher accepted authority requires broader restriction.**
