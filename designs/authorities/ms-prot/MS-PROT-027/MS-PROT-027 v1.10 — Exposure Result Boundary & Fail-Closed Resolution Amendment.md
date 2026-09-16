# MS-PROT-027 v1.10 — Exposure Result Boundary & Fail-Closed Resolution Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.10  
**Status:** **ACCEPTED after decomposition, iterative falsification, ambiguity review, corpus-conformance review and explicit instruction to close the design blocker**  
**Approved:** 2 September 2026  
**Amends:** composite MS-PROT-027 through v1.9 only within Exposure result membership, result provenance, API-result binding and structural Exposure-resolution failure  
**Closes:** `IMP-06-E3-DG-002`  
**Depends on:** MS-PROT-027 v1.2–v1.9; MS-PROT-031; MS-PROT-035 v1.1; MS-PROT-049; MS-PROT-053; MS-PROT-054; MS-PROT-062; MS-PROT-070; ADR-010; ADR-011; ADR-013  
**Governed by:** MS-DESIGN-RULES-001; MS-IMPLEMENTATION-RULES-001 v1.4; MS-IMP-001  
**Purpose:** Define the minimum production result contract required to complete the IMP-06 E3 Exposure-result boundary without transferring upstream semantic authority into Exposure, confusing legitimate withholding with structural failure, or allowing API representation/transport concerns to become Exposure semantics.

---

## 1. Governing decision

Main Street SHALL preserve the existing Exposure question:

> **May this particular audience observe this particular already-legitimate semantic element in the current authoritative context?**

The element verdict remains exactly:

```text
EXPOSE
WITHHOLD
```

This amendment adds only the bounded result semantics required after those verdicts are established.

Canonical flow:

```text
already-legitimate candidate elements
        +
exact Exposure Element Contracts
        +
trusted Audience Observation Context
        +
current audience admission
        +
applicable upstream serviceability/current owner evidence
        ↓
element-level Exposure evaluation
        ↓
EXPOSE | WITHHOLD
        ↓
positive-membership aggregation
        ↓
ApiExposedElementSet
        ↓
ApiExposureResolution
        ↓
MS-PROT-035 representation / transport
```

A structural inability to establish a trustworthy Exposure resolution follows a distinct fail-closed path and SHALL NOT be represented as a successful empty or partial result.

---

## 2. Four bounded contracts

The production boundary is decomposed into four contracts.

### 2.1 Candidate contract

An Exposure candidate SHALL already be legitimate before Exposure evaluates it.

The candidate identity SHALL use the accepted owner-qualified `ExposableElementReference`.

Where candidate-instance identity is required, the existing `ExposureCandidateObservation` boundary remains authoritative.

Exposure SHALL NOT:

- discover arbitrary business data;
- create Surface participation;
- fetch or schedule projection construction;
- establish Provider Readiness;
- manufacture Actor Authorisation;
- infer capability ownership; or
- turn API representation fields into semantic candidates.

### 2.2 Element-decision contract

For one legitimate candidate under the exact applicable contract and trusted context, the only Exposure verdicts remain:

```text
EXPOSE
WITHHOLD
```

`WITHHOLD` is a legitimate Exposure outcome. It is not:

- authentication failure;
- Actor Authorisation denial;
- Projection Serviceability failure;
- Provider Readiness failure;
- transport failure; or
- whole-resolution structural failure.

Existing accepted rules remain authoritative that an element-specific governing requirement resolving `UNSATISFIED` or `UNRESOLVED` fails closed to `WITHHOLD` where the contract says that requirement governs the candidate. In particular, a missing candidate instance reference required for an element-specific relationship remains `UNRESOLVED → WITHHOLD` under v1.9.

This amendment does not promote candidate-local uncertainty into a universal whole-request error.

### 2.3 Aggregate-result contract

A successful Exposure resolution SHALL aggregate only `EXPOSE` members.

`ApiExposedElementSet` SHALL therefore be:

- immutable;
- bounded to one resolution invocation and one established request;
- exact-release-affined through retained private provenance;
- positive-membership-only;
- keyed by the accepted owner-qualified `ExposableElementReference`;
- deterministic with respect to membership;
- capable of representing a legitimate empty set; and
- free of mutable or caller-authoritative semantic state.

A successful empty result means:

```text
resolution trustworthy
+
zero candidates resolved EXPOSE
```

It does not mean that resolution failed.

### 2.4 Structural-resolution-failure contract

A whole Exposure resolution SHALL fail closed when the system cannot establish the structural authority required to trust the resolution itself.

Initial structural failures include:

- exact API/context/request provenance mismatch;
- rejected or mismatched invocation-bound audience admission;
- exact Semantic Registry Release mismatch;
- missing exact Exposure Element Contract where one is required for a submitted candidate;
- contract/candidate identity mismatch;
- duplicate member identity presented as distinct successful resolutions;
- result/request or result/invocation binding mismatch; and
- an internal result that cannot be safely bound to the exact API contract/surface.

Structural failure SHALL NOT be converted into:

```text
WITHHOLD
```

or:

```text
RESOLVED(empty)
```

or an apparently successful partial API result.

The failure evidence remains internal and transport-neutral. MS-PROT-035 owns any safe outward transport mapping.

---

## 3. Membership identity

`ExposableElementReference` remains the canonical membership identity:

```text
ownerIdentifier
+
elementIdentifier
```

No new universal element identity is introduced.

Consequences:

1. the same local element identifier owned by different authorities does not collide;
2. two successful members with the same exact owner-qualified reference are duplicates and SHALL be rejected rather than silently de-duplicated;
3. a domain aggregate identity alone is insufficient where several independently exposable elements exist inside that aggregate; and
4. frontend component names, JSON property names, database columns and display labels SHALL NOT become result membership identity.

The exact `ExposureElementContractIdentity` used to establish a member is retained as private resolution provenance. It does not replace the exposable-element identity.

---

## 4. Result provenance

A successful result SHALL retain privately enough provenance to prove that all members belong to one coherent Exposure resolution.

At minimum, private result provenance SHALL bind:

- the exact `ObservationRequestBinding`;
- the exact `AudienceObservationInvocationBinding`;
- the exact Semantic Registry Release identifier;
- each member's `ExposableElementReference`; and
- each member's exact `ExposureElementContractIdentity`.

For API-originated resolution, the outer `ApiExposureResolution` SHALL additionally bind the exact `ApiContractIdentity` and `ApiSurfaceClass` already established by v1.9.

Private provenance is validation/evidence. It is not public business data and SHALL NOT create a generic unwrapping API.

---

## 5. Public API result boundary

`ApiExposureResolution` remains the opaque API-facing Exposure result named by v1.9.

Its public contract SHALL expose only:

- exact `ApiContractIdentity`;
- exact `ApiSurfaceClass`; and
- `ApiExposedElementSet`.

`ApiExposedElementSet` MAY expose the immutable set of owner-qualified `ExposableElementReference` members required by downstream API representation.

It SHALL NOT publicly expose:

- `AudienceObservationContext`;
- `EstablishedObservationRequest`;
- `AudienceObservationAdmissionResult`;
- invocation binding;
- Active Release internals;
- semantic-registry implementation details;
- requirement evaluation evidence;
- withheld members;
- withholding reasons;
- structural failure diagnostics;
- Projection Serviceability evidence;
- Provider Readiness;
- Actor Authorisation;
- internal Exposure results; or
- generic object/value payloads.

API representation MAY use the exposed owner-qualified references only as already-governed result membership. It SHALL NOT treat membership as authority to reacquire or expose unrelated semantic data.

---

## 6. Internal/API separation

`InternalExposureResolution` remains distinct from `ApiExposureResolution` as required by v1.9.

One internal Exposure computation SHALL be shallowly bound to the appropriate API output.

The binding boundary SHALL validate:

```text
same established request
+
same audience-admission invocation
+
same Semantic Registry Release
+
same exact API provenance
+
same permitted API surface
```

before producing `ApiExposureResolution`.

No API or representation package may depend on internal Exposure evidence merely because the API output derives from it.

---

## 7. Successful empty versus structural failure

The following distinction is mandatory:

```text
RESOLVED(empty)
    = trustworthy resolution with no exposed members

FAIL_CLOSED
    = no trustworthy successful resolution exists
```

Examples of legitimate empty resolution include:

- every legitimate candidate resolved `WITHHOLD`; or
- the bounded upstream composition selected no candidate elements for Exposure.

Examples that SHALL NOT become an empty successful set include:

- wrong release;
- forged/mismatched API provenance;
- rejected audience admission;
- missing exact contract for a candidate presented for evaluation; or
- result binding across different requests/invocations.

---

## 8. Partial-result boundary

Legitimate candidate-local `WITHHOLD` does not suppress unrelated successful `EXPOSE` members.

Therefore:

```text
A → EXPOSE
B → WITHHOLD
C → EXPOSE
```

may produce a successful result containing `A` and `C`.

By contrast, structural failure while establishing the bounded resolution invalidates that Exposure resolution and SHALL NOT be reclassified as a legitimate partial success.

Upstream controlled degradation remains governed by the authority that owns it. For example, an unavailable optional projection may prevent its candidate from being produced without making Exposure the owner of Projection Serviceability. Exposure does not reinterpret provider/projection failure as an element verdict.

---

## 9. Ordering, pagination and continuation

`ApiExposedElementSet` is a membership contract, not a presentation or transport sequence.

It SHALL NOT own:

- display order;
- sort semantics;
- pagination;
- cursor/continuation affinity;
- page size;
- transport field ordering; or
- storefront layout.

T4b and MS-PROT-035 retain their existing authority.

If deterministic iteration is useful in implementation/tests, that implementation detail SHALL NOT become semantic ordering authority without a separate accepted decision.

---

## 10. Caching, freshness and acquisition

This result boundary introduces no cache or freshness authority.

It SHALL NOT own:

- projection refresh/rebuild;
- provider polling;
- source acquisition strategy;
- retry/backoff;
- request-wide world-state snapshots;
- generic memoisation policy; or
- dependency scheduling.

Fact acquisition may be batched or memoised by the applicable owning authority, but optimisation SHALL NOT transfer semantic ownership into Exposure or `AudienceObservationContext`.

Exposure consumes established semantic inputs. It does not plan their acquisition.

---

## 11. Adjacency is not ownership

The following invariant is accepted explicitly because IMP-06 exposed persistent semantic-gravity pressure:

> **Adjacency is not ownership. A concern may participate in Exposure resolution without becoming an Exposure semantic.**

Therefore the Exposure result boundary SHALL NOT absorb the authority of adjacent concerns merely because they occur on the same request path.

In particular:

```text
Projection Serviceability
Provider Readiness
Actor Authorisation
Resource Protection
merchant configuration
customer relationship truth
Data Protection
execution authority
transport mapping
```

retain their existing owners.

---

## 12. Construction rules

Main Street-owned authoritative result contracts SHALL follow the v1.9 construction pattern:

- public closed/opaque contract where external use is required;
- final package-private implementation;
- package-owned inspection helper only where later trusted composition requires it;
- no public authoritative constructor/deserializer;
- immutable defensive copies at collection boundaries; and
- no generic public unwrap.

A factory/binder MAY return a closed success/failure result so that structural failure remains explicit and cannot masquerade as an empty set.

The exact Java class names below the accepted semantic contracts remain implementation scope provided all invariants hold.

---

## 13. Architecture-test requirements

Executable architecture/conformance tests SHALL protect at least these boundaries:

1. `ApiExposureResolution` exposes only contract identity, surface and exposed-element set.
2. `ApiExposedElementSet` is immutable and non-serializable unless later transport authority explicitly creates a representation type.
3. no public context/internal-result unwrap exists.
4. result construction outside trusted package-owned factories is unavailable.
5. duplicate exact `ExposableElementReference` successful membership is rejected.
6. same local element identifier under different owners remains distinct.
7. successful empty and structural failure are distinguishable.
8. withheld members and internal failure reasons are absent from the public set.
9. API/representation code does not depend on `InternalExposureResolution`.
10. Exposure result types do not depend on Provider Readiness, execution/command authority, pagination or cache contracts.

---

## 14. Hard-case falsification

The amendment was attacked against the concrete cases that produced `IMP-06-E3-DG-002`.

| Case | Required behaviour | Result |
|---|---|---|
| Public merchant presence | Owner-qualified exposed members only | **PASS** |
| Successful zero-visible-elements request | `RESOLVED(empty)` | **PASS** |
| Customer-contextual observation | Existing subject/admission rules remain upstream; result shape unchanged | **PASS** |
| Merchant-operational observation | Existing subject/admission rules remain upstream; result shape unchanged | **PASS** |
| Information-only merchant | No projection machinery invented when upstream path does not require it | **PASS** |
| Product-only merchant | Result remains domain-blind | **PASS** |
| Service + Booking hybrid | Owner-qualified membership composes without business-type branching | **PASS** |
| Product + Service + Booking + Publication hybrid | Same result contract; no central capability taxonomy required | **PASS** |
| Legitimate candidate denial | `WITHHOLD`; unrelated exposed members survive | **PASS** |
| Missing candidate instance needed by relationship requirement | Existing `UNRESOLVED → WITHHOLD` preserved | **PASS** |
| Required projection not serviceable | Remains upstream serviceability outcome; not fabricated `WITHHOLD` | **PASS** |
| Provider unavailable for unrelated capability | Does not suppress unrelated exposed member | **PASS** |
| Missing exact Exposure contract for submitted candidate | Structural fail closed | **PASS** |
| Duplicate exact member identity | Reject; no silent de-duplication | **PASS** |
| Same local identifier from different owners | Distinct owner-qualified members | **PASS** |
| Semantic Registry Release mismatch | Structural fail closed | **PASS** |
| API/context/surface provenance mismatch | Structural fail closed | **PASS** |
| Wrong Merchant Scope | Existing establishment/binding failure remains upstream | **PASS** |
| Forged typed contribution | Existing v1.9 establishment rejects before result | **PASS** |
| Mixed `EXPOSE` and legitimate `WITHHOLD` | Positive partial membership allowed | **PASS** |
| Structural failure after some member decisions | No successful partial result | **PASS** |
| API/internal evidence leakage | Public result contains no internal evidence/unwrap | **PASS** |
| TOCTOU between observation and later command | Result grants no execution authority; command revalidates independently | **PASS** |
| Ordering/pagination pressure | Explicitly outside result semantics | **PASS** |
| Performance batching pressure | Owner-side optimisation allowed; no Exposure world-state | **PASS** |

**Falsification verdict:** **PASS**

No hard case requires Exposure to discover owners, acquire upstream facts, branch on merchant/business type, own serviceability/readiness, or become a generic query planner.

---

## 15. Semantic-gravity falsification

The following attempted expansions are rejected:

| Pressure | Failure if accepted | Verdict |
|---|---|---|
| Put operational/provider state in `AudienceObservationContext` | Creates request world-state object | **REJECTED** |
| Let Exposure fetch projection/provider facts | Creates query planner/orchestrator | **REJECTED** |
| Put `WITHHOLD_*` reason taxonomy in exposed set | Turns result into universal policy/error model and leaks evidence | **REJECTED** |
| Let result grant execution capability | Collapses observation and command authority | **REJECTED** |
| Put pagination/order/cache metadata in result | Transfers API/query concerns into Exposure | **REJECTED** |
| Replace purpose-specific authorities with generic proposition DAG | Duplicates accepted bounded composition mechanisms | **REJECTED** |
| Treat all `UNRESOLVED` as whole-result failure | Contradicts accepted candidate-local fail-closed `WITHHOLD` semantics | **REJECTED** |
| Treat structural failure as `WITHHOLD` | Hides architecture/integrity failure as valid business output | **REJECTED** |

Residual semantic-gravity pressure remains persistent even after this mitigation and SHALL remain on the architecture watch list.

---

## 16. Ambiguity review

| Check | Result |
|---|---|
| Candidate legitimacy versus Exposure is explicit | **PASS** |
| Candidate-local unresolved versus structural failure is explicit | **PASS** |
| Successful empty versus failure is explicit | **PASS** |
| Membership identity and collision rule are explicit | **PASS** |
| Public versus private provenance is explicit | **PASS** |
| API/internal result separation is explicit | **PASS** |
| Ordering/pagination/continuation ownership is explicit | **PASS** |
| Serviceability/readiness/authorisation ownership is explicit | **PASS** |
| Construction invariants are testable | **PASS** |
| No new generic composition mechanism is implied | **PASS** |
| Another engineer can implement E3 result boundary without inventing a semantic rule | **PASS** |

**Ambiguity review:** **PASS**

---

## 17. Corpus-conformance finding

This amendment:

- preserves v1.2 `EXPOSE | WITHHOLD` semantics;
- preserves v1.5 legitimate-candidate and requirement-evaluation semantics;
- reuses v1.6 exact Exposure Element Contract identity/registration;
- does not alter v1.8 Projection Serviceability;
- preserves v1.9 trusted context, admission and opaque API/internal separation;
- preserves MS-PROT-049 Surface/eligibility ownership;
- preserves MS-PROT-053 restriction ownership;
- preserves MS-PROT-062 Actor Authorisation/execution separation;
- preserves MS-PROT-070 degradation/failure ownership;
- leaves representation and transport mapping under MS-PROT-035;
- introduces no new database, cache, workflow engine, query planner or semantic graph; and
- resolves only the implementation-discovered result-boundary gap.

**Corpus conformance:** **PASS**

---

## 18. Implementation consequence

`IMP-06-E3-DG-002` is design-resolved by this amendment.

The E3 result-boundary implementation becomes `READY`, not conforming.

The first RED→GREEN implementation slice SHALL prove at minimum:

- immutable positive-only exposed membership;
- successful empty result;
- owner-qualified duplicate/collision behaviour;
- retained private request/invocation/release/contract provenance;
- exact API contract/surface binding;
- no public internal unwrap;
- structural fail-closed outcome distinct from successful empty; and
- API/internal result separation.

Implementation SHALL stop before E4 capability evaluators, actual business-candidate acquisition, continuation, HTTP representation, pagination or caching.

After GREEN verification, implementation-level semantic-gravity falsification SHALL inspect dependency fan-in, capability branching, generic maps/bags, context leakage, duplicated authority checks, fixture amplification and change amplification before E3 is declared conforming.

---

## 19. Amendment effect

This amendment:

1. closes `IMP-06-E3-DG-002` without reopening `IMP-06-E3-DG-001`;
2. defines the minimum `ApiExposedElementSet` membership contract;
3. preserves `EXPOSE | WITHHOLD` as the only element verdicts;
4. distinguishes candidate-local fail-closed withholding from structural resolution failure;
5. distinguishes successful empty from failure;
6. retains exact private provenance without exposing internal evidence;
7. prohibits Exposure-result semantic gravity into serviceability, readiness, authorisation, execution, acquisition or transport; and
8. makes the bounded E3 result implementation READY for tests-first execution.

---

## 20. Acceptance statement

> **An Exposure result is a bounded observation result, not a new source of semantic truth: it contains only owner-qualified elements that resolved `EXPOSE`, retains privately the exact request/invocation/release/contract provenance needed to prove that membership, permits a trustworthy empty result, and fails closed when the resolution itself cannot be trusted. Legitimate `WITHHOLD` remains an element outcome; structural failure never masquerades as withholding, emptiness or transport success. Exposure consumes established inputs and does not plan their acquisition.**

**Review:** PASS  
**Falsification:** PASS  
**Semantic-gravity falsification:** PASS with persistent watch requirement  
**Ambiguity review:** PASS  
**Corpus conformance:** PASS  
**Recommendation:** ACCEPT  
**Approval:** GRANTED by explicit instruction to close the design blocker — 2 September 2026  
**Governance verdict:** **ACCEPTED**
