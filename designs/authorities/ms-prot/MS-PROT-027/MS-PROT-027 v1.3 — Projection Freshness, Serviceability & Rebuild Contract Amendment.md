# MS-PROT-027 v1.3 — Projection Freshness, Serviceability & Rebuild Contract Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.3  
**Status:** **ACCEPTED after executable-architecture review, two falsification passes and manual approval**  
**Amends:** MS-PROT-027 v1.1 and v1.2 within projection freshness/serviceability/rebuild scope  
**Depends on:** MS-PROT-025, MS-PROT-026, MS-PROT-027 v1.1, MS-PROT-027 v1.2, MS-PROT-035, MS-PROT-049 v1.1, MS-PROT-053 v1.1, MS-PROT-059, MS-PROT-069, MS-PROT-070, MS-PROT-072  
**Closes:** EAC-003 — Projection Contract, Freshness & Availability Boundary  
**Purpose:** Define when a projection/read representation requires an explicit freshness/serviceability contract, what that contract must establish, and how stale, unavailable, multi-source or rebuilding projections remain safe without turning projections into business authority, imposing one global TTL/availability enum, or erasing independently required Surface responsibilities.

---

# 1. Amendment Scope

MS-PROT-027 v1.1 already establishes that projections are derived, may lag authoritative state where safe, and cannot authorise mutation. MS-PROT-027 v1.2 establishes Exposure as an independent audience-observation decision.

The previous rule:

> a projection may lag authoritative state where no invariant depends on immediate consistency

remains correct but is insufficiently precise for persisted/cached/asynchronous projections because `where safe` does not itself identify the authority or predicate that determines safe serving.

This amendment adds the missing projection freshness/serviceability contract. It does not replace MS-PROT-027 v1.1 or v1.2 outside this scope.

---

# 2. Governing Decision

> **Where an explicit Projection Contract applicability trigger exists, exactly one declared Projection Owner MUST define the evidence required to determine projection currentness/staleness, the conditions under which the projection may still be served, the bounded consequence of missing evidence, and the rebuild/provenance guarantees the projection can truthfully claim. Authoritative source capabilities retain business-fact ownership; Exposure, privacy/security and mandatory Surface responsibilities remain independent constraints.**

Canonical separation:

```text
AUTHORITATIVE SOURCE FACTS
        ↓
projection derivation/materialisation
        ↓
PROJECTION DATA
        +
FRESHNESS / PROVENANCE EVIDENCE
        ↓
PROJECTION CONTRACT EVALUATION
        ↓
may this representation be served in this read context?
        ↓
EXPOSURE / SECURITY / PRIVACY / SURFACE constraints
        ↓
audience response
```

Projection serviceability does not create mutation authority.

---

# 3. Canonical Terms

## 3.1 Projection Contract

A **Projection Contract** is the declared contract owned by one Projection Owner that governs a specific projection/read identity where one or more applicability triggers in Section 5 are true.

The Projection Contract specifies:

- authoritative source dependencies;
- freshness/currentness evidence;
- staleness interpretation where applicable;
- serving conditions under dependency degradation or known lag;
- missing-evidence consequence;
- multi-source evidence requirements where applicable;
- rebuild/provenance guarantees where applicable; and
- applicable audience/use-case restrictions that are projection-specific rather than Exposure/security/privacy authority.

A Projection Contract is not a second source of business truth.

## 3.2 Projection Owner

The **Projection Owner** owns the derivation/materialisation/read-contract responsibility for one projection identity.

The Projection Owner MAY combine facts owned by multiple capabilities, but MUST NOT gain mutation authority over those source facts.

Exactly one Projection Owner MUST own each Projection Contract identity.

## 3.3 Freshness Evidence

**Freshness Evidence** is evidence used by the Projection Contract to determine how the projection relates to current authoritative-source progress.

Freshness Evidence MAY include, according to the owning contract:

```text
source revision/version
source event/log position
projection checkpoint
last successfully incorporated authoritative fact
source observation time
projection generation time
other deterministic provenance evidence
```

Elapsed age alone is not universally sufficient.

## 3.4 Projection Serviceability

**Projection Serviceability** is the derived runtime decision answering:

> **May this projection representation be served for this read use/context under its Projection Contract and currently established evidence?**

Projection Serviceability is not an authoritative lifecycle state on the underlying business object and this amendment does not require a universal serviceability enum.

## 3.5 Rebuildability

**Rebuildability** is a contract claim that the projection can be reconstructed to the required semantic meaning from retained authoritative/provenance evidence.

A Projection Contract MUST NOT claim rebuildability where the required source/provenance evidence is not retained.

---

# 4. Semantic Classification

The concepts introduced by this amendment are classified as follows:

```text
Projection Contract
    registered/read-architecture contract; non-business mutation authority

Projection data
    DERIVED / PROJECTION

Freshness Evidence
    runtime/read evidence; not business truth

Projection Serviceability
    DERIVED runtime read decision

Rebuildability
    projection contract property backed by retained source/provenance evidence
```

None becomes Merchant Configuration merely because merchant configuration influences what a projection contains.

---

# 5. Projection Contract Applicability Triggers

A dedicated Projection Contract is REQUIRED for a projection/read identity when at least one of the following predicates is true:

### Trigger A — persisted/cached beyond authoritative request

The representation is persisted or cached beyond the authoritative query transaction/request that produced it.

### Trigger B — asynchronous update

The representation is updated asynchronously from authoritative state.

### Trigger C — registered dependency

Another registered contract, including a Surface Contribution or other accepted read contract, references the projection identity or depends on its serviceability.

### Trigger D — source-outage serving

The system may serve the representation while one or more authoritative source dependencies are unavailable.

### Trigger E — deliberate known lag

The system may intentionally serve the representation while it is known not to reflect the latest authoritative observation.

### Trigger F — divergent multi-source freshness

The representation combines multiple authoritative sources whose currentness/serviceability may diverge materially.

### Trigger G — revocation-sensitive serving

Privacy, security or Exposure revocation timing can make continued serving of an older representation unsafe.

These are determinable applicability predicates. An engineer MUST NOT substitute the undefined phrase `material projection` for these triggers.

---

# 6. Synchronous Request-Scoped Projection Exception

A synchronous request-scoped projection computed through accepted query/application boundaries directly from currently established authoritative sources does NOT require a separate freshness/serviceability Projection Contract when none of Triggers A–G applies.

This exception does not permit transport/UI code to query capability persistence directly or bypass accepted application/query boundaries.

The ordinary query contract remains responsible for technical/business read rejection semantics for that request.

---

# 7. Projection Contract Identity and Equality

A Projection Contract MUST have stable identity sufficient to distinguish materially different read contracts.

Identity MUST be qualified by the projection/read responsibility and owning scope required by the contract; identical display labels or endpoint paths MUST NOT imply contract identity.

Two Projection Contracts are not semantically equal merely because they use the same TTL, cache technology or fields.

Where a registered Surface Contribution depends on a projection, that dependency MUST resolve to the intended Projection Contract/read identity rather than a stringly inferred UI label.

---

# 8. Establishment and Change

A Projection Contract becomes governing only through accepted/registered Main Street design/configuration authority applicable to that projection.

A material change to:

- authoritative source dependency;
- stale-serving permission;
- missing-evidence consequence;
- rebuildability guarantee;
- revocation-sensitive handling; or
- a Surface's dependency on the projection

MUST be versioned/reviewed through the applicable accepted design/configuration lifecycle rather than changed invisibly as a cache implementation tweak when observable semantics change.

Changing only physical cache/index technology with materially equivalent behaviour remains an implementation detail.

---

# 9. Freshness Is Not One Global TTL

Main Street MUST NOT define one platform-wide TTL as the meaning of projection correctness.

Elapsed time MAY be one input to a Projection Contract only when time since an accepted anchor is sufficient to establish the contract's currentness/staleness predicate.

Examples of valid non-time evidence include:

```text
sourcePositionApplied == authoritativeSourcePosition
sourceRevisionProjected == sourceRevisionRequired
all required source checkpoints >= declared consistency point
```

This amendment does not prescribe one universal checkpoint algorithm.

---

# 10. Freshness and Serviceability Are Distinct

A projection may be current but not serviceable because another governing authority prohibits serving it.

Example:

```text
projection current
+
PUBLIC Exposure revoked
        ↓
WITHHOLD
```

A projection may also be stale but serviceable where the owning Projection Contract explicitly permits that read use.

Example:

```text
analytics summary
last incorporated source position behind current source
+
contract permits bounded stale analytics read
        ↓
serve with the contractually permitted representation/evidence
```

Therefore:

```text
FRESHNESS/CURRENTNESS
    ≠
PROJECTION SERVICEABILITY
    ≠
EXPOSURE
```

---

# 11. Serviceability Predicate

When a Projection Contract applies, the Projection Owner MUST define a deterministic serviceability predicate or explicitly delegate a required input to an accepted authority.

The predicate MUST determine, for the requested read use/context, whether the available projection evidence permits serving.

A contract MAY distinguish uses of the same projection where risk differs.

For example:

```text
Inventory public summary
    stale informational display may be permitted

Order commit
    MUST NOT use that stale projection as stock mutation authority
```

The command path remains governed by authoritative revalidation under MS-PROT-059 and the owning capability.

---

# 12. Missing Evidence

The Projection Contract MUST define the consequence when evidence required by its serviceability predicate is absent, corrupt or unverifiable.

Permitted consequences include only outcomes that preserve truth, such as:

```text
withhold affected data
return projection-unavailable/degraded read outcome
serve a separately permitted reduced projection
continue an independently serviceable residual Surface contribution
```

The system MUST NOT invent freshness, assume `current`, or silently substitute data from a semantically different projection.

---

# 13. Multi-Source Projections

A projection combining multiple authoritative sources MUST preserve enough independently material source/provenance evidence to evaluate the Projection Contract correctly when source progress diverges.

Rejected universal rule:

```text
projection generatedAt = 10:00
        ↓
all source facts are equally current
```

If one source can lag while another is current, the contract MUST either:

- retain distinguishable evidence required to evaluate the combined representation; or
- define a conservative serviceability rule that does not falsely imply equivalent freshness.

The Projection Owner does not acquire authoritative ownership of the source facts by combining them.

---

# 14. Source Dependency Failure

Where Trigger D applies, the Projection Contract MUST define whether a previously materialised representation may be served while an authoritative source is unavailable.

A source outage MUST NOT automatically mean stale data is safe.

A source outage also MUST NOT automatically erase all unrelated read capability.

MS-PROT-070 remains authoritative for controlled degradation and dependency criticality.

---

# 15. Privacy, Security and Exposure Revocation

Privacy, security and Exposure restrictions outrank ordinary stale-serving tolerance.

Where older projection data contains an element that current accepted authority requires to be withheld, the system MUST NOT continue serving that element merely because the Projection Contract otherwise permits stale reads.

For revocation-sensitive projections under Trigger G, the Projection Contract MUST define how current revocation evidence constrains serving or causes the affected projection element to converge to a safe result.

Possession of cached data does not grant permission to expose it.

---

# 16. Surface Contribution Boundary

MS-PROT-049 remains authoritative for Surface inclusion and interaction availability.

Projection unavailability MAY make a projection-dependent contribution unavailable or degraded according to its registered dependency contract.

However, projection failure MUST NOT erase an independently required residual workspace or mandatory Surface responsibility that remains required by separate accepted authority.

Conceptually:

```text
required residual workspace
    +
one projection contribution unavailable
        ↓
workspace remains if other required/eligible responsibility survives
```

Projection Serviceability is not Surface membership authority.

---

# 17. Rebuild Contract

Where a Projection Contract claims rebuildability, it MUST identify the retained authoritative/provenance evidence sufficient to reconstruct the projection's required semantic meaning.

A rebuild MAY run asynchronously.

During rebuild, the Projection Contract MUST determine whether:

- no data may be served;
- an older retained representation remains serviceable;
- a reduced representation may be served; or
- serving resumes only after required source progress is reached.

This amendment does not require a universal `REBUILDING` lifecycle enum.

A rebuild process MUST NOT write authoritative source business truth merely to make the projection consistent.

---

# 18. Projection Update Operations

Projection update/materialisation operations are derived-state operations.

Their inputs are committed authoritative facts/query results plus required provenance/checkpoint context.

Their authoritative mutation is limited to projection/read-model state and associated projection evidence.

They MUST NOT mutate the source capability merely because projection update fails or detects lag.

Where events drive updates, MS-PROT-026 and MS-PROT-072 govern post-commit propagation/recovery.

---

# 19. Transaction Boundary

Projection updates MUST NOT be placed inside an authoritative business transaction merely to guarantee read-model freshness unless an accepted invariant explicitly requires the projection itself to participate in that atomic boundary.

The default composite pattern remains:

```text
authoritative business commit
        ↓
post-commit propagation / query materialisation
        ↓
projection convergence
```

where eventual consistency is permitted.

Commands that require current business truth revalidate at the authoritative owner rather than extending the transaction to a read projection.

---

# 20. Retry and Duplicate Semantics

Projection update delivery MAY repeat.

Repeated delivery of the same committed source fact MUST NOT corrupt or multiply the projected semantic result.

The projection update mechanism MUST preserve enough event/source identity or checkpoint semantics to make replay/retry deterministic for the contract.

A duplicate projection update MUST NOT create a duplicate business fact at the source authority.

---

# 21. Failure Classes

Projection handling MUST distinguish at least:

```text
SOURCE BUSINESS REJECTION
    source query/authority says requested read subject/context is not legitimate

EXPOSURE / SECURITY / PRIVACY WITHHOLD
    data exists but current audience handling is prohibited

PROJECTION NOT SERVICEABLE
    projection evidence does not satisfy its serving contract

PROJECTION INFRASTRUCTURE FAILURE
    cache/index/materialisation infrastructure cannot currently provide the representation

PROJECTION REBUILD / CATCH-UP
    derived state is being reconstructed or advanced under its contract
```

These classifications need not be exposed as one public enum. They MUST remain distinguishable where caller/recovery behaviour differs.

---

# 22. Commercial Entitlement and Actor Authorisation

Projection Serviceability is independent of Commercial Entitlement and Actor Authorisation.

A projection can be technically current/serviceable while the merchant is not entitled to a new paid operation or the actor is not authorised to view/use a protected Surface.

Likewise a merchant may retain a residual right to manage an existing commitment while one convenience projection is unavailable.

No `available` flag introduced by projection infrastructure may collapse these dimensions.

---

# 23. Provider Boundary

A Projection Contract MAY depend on provider-derived evidence where an accepted capability permits it, but provider status/evidence does not become authoritative business truth.

Provider outage stale-serving behaviour must be declared by the Projection Contract when Trigger D applies and remains constrained by MS-PROT-070 and applicable provider authorities.

Provider cache semantics MUST NOT redefine Exposure, merchant policy or source capability state.

---

# 24. API and Presentation Consequences

MS-PROT-035 remains authoritative for transport/API contracts.

A projection API MAY expose provenance such as `asOf`, checkpoint or degraded-read information when the use case needs it, but this amendment does not mandate one universal response envelope.

Presentation labels such as:

```text
Updated 5 minutes ago
Temporarily unavailable
Some data may be delayed
```

are projections of the owning contract outcome, not new domain lifecycle states.

---

# 25. Explicit Exclusions

This amendment rejects:

- one global projection TTL;
- one universal `CURRENT/STALE/DEGRADED/REBUILDING` domain enum;
- engineer-defined `material projection` applicability;
- cache age as universal correctness evidence;
- stale projection data as mutation authority;
- projection failure as authority to remove an independently required residual workspace;
- projection owner becoming source-fact owner;
- privacy/Exposure revocation being ignored because cached data remains within ordinary TTL;
- mandatory materialisation of synchronous request-scoped projections; and
- a generic projection DSL/framework mandated without evidence.

---

# 26. Falsification Record

The accepted amendment was tested against:

- synchronous request-scoped projection;
- public catalogue cache;
- stale Inventory availability display followed by authoritative Order commit;
- asynchronous Booking/calendar projection;
- mixed-freshness merchant dashboard;
- analytics projection with high lag tolerance;
- provider-source outage while cached public information exists;
- privacy/Exposure revocation during projection lag;
- mandatory residual workspace with one failed projection contribution;
- destroyed projection requiring rebuild from retained authoritative evidence;
- command completion followed by delayed projection convergence;
- information publisher, consultant, motel, retailer and hybrid merchant domains.

The first recommendation failed because `material projection` was not a determinable applicability predicate and a global freshness/availability model could over-constrain synchronous reads or erase independent Surface responsibilities.

The explicit A–G triggers and owner-scoped contract survived second-order falsification.

---

# 27. Trade-Offs

Main Street accepts per-projection contract responsibility rather than one universal freshness framework.

This creates more explicit ownership work for projections that genuinely cross a consistency boundary, but avoids:

- global TTL semantics;
- false freshness certainty;
- forcing all reads into CQRS/materialised-view infrastructure;
- presentation availability becoming domain authority.

Physical storage/index/cache technology remains replaceable.

---

# 28. Deferred Future Scope

The following remain downstream or evidence-driven:

- concrete cache/index technology;
- exact TTLs where a Projection Contract legitimately uses time;
- universal observability dashboards for projection lag;
- one transport representation for degraded/stale reads;
- generic projection framework/DSL;
- automated repair tooling;
- per-projection SLOs unless operational evidence requires them.

These items MUST NOT be filled by implementation assumptions when a Projection Contract requires an explicit semantic serviceability decision.

---

# 29. Conformance Criteria

A conforming implementation MUST demonstrate at least:

1. a synchronous request-scoped projection with no A–G trigger is not forced into a separate materialised-projection lifecycle;
2. every projection with an A–G trigger resolves exactly one Projection Owner/Contract;
3. the contract has explicit authoritative source dependencies and freshness evidence;
4. absence/corruption of required evidence cannot be treated as implicitly current;
5. stale serving occurs only where the owning contract permits the read use/context;
6. multi-source divergence cannot be hidden behind one misleading generation timestamp when independent evidence is required;
7. stale projection data cannot authorise mutation;
8. privacy/security/Exposure revocation can withhold otherwise stale-serviceable data;
9. projection failure does not by itself erase an independently required residual Surface responsibility;
10. rebuild claims are backed by retained authoritative/provenance evidence; and
11. duplicate/replayed projection updates do not multiply authoritative business effects.

---

# 30. Amendment Effect

This amendment **narrowly replaces** the under-specified interpretation of MS-PROT-027 v1.1 Section 10 only to the extent necessary to determine when eventual-consistency/stale-serving requires an explicit Projection Contract and how such serviceability is governed.

The following remain unchanged:

- authoritative state remains business truth;
- projections remain derived/non-authoritative;
- merchant/customer/public projections may differ;
- customer accounts remain optional;
- MS-PROT-027 v1.2 Exposure semantics remain authoritative;
- Surface composition remains governed by MS-PROT-049;
- controlled degradation remains governed by MS-PROT-070;
- API transport remains governed by MS-PROT-035.

---

# 31. Accepted Result

> **Persisted, cached, asynchronous, source-outage-capable, deliberately stale, divergent multi-source, registered-dependent or revocation-sensitive projections require an explicit owner-scoped Projection Contract. That contract determines freshness evidence and read serviceability without creating business mutation authority; privacy/security/Exposure and Surface obligations remain separate, and synchronous request-scoped reads are not forced into unnecessary projection machinery.**
