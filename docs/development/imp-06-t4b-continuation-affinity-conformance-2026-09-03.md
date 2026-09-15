# IMP-06 T4b Continuation Affinity / Validation Conformance Record

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Node:** `T4b — continuation affinity/validation`  
> **Result:** **CONFORMING_COMPLETE**  
> **Verified code-bearing baseline:** `development@fdd859a2a68e62407bff8cc6fe50f52ff4c54b45`

This record closes T4b under accepted MS-IMP-001 sequencing and IMPLEMENTATION-RULES v1.6. It is implementation evidence only and does not create semantic, architectural, API or programme authority.

---

## 1. Governing authority and scope

T4b is governed principally by:

- `MS-PROT-035 v1.1 — Production API Contract Registration, Transport Outcome & Initial Surface Portfolio Amendment`, especially bounded-collection continuation semantics and scope/query affinity;
- `MS-PROT-027 v1.9 — Audience Observation Context Establishment & Surface-Bound Exposure Amendment`, especially exact Merchant Scope, Audience Observation Context, Semantic Registry Release and request-scoped observation boundaries;
- completed T4a `ApiBoundedCollectionContract`;
- completed T1b4 first production bounded query definition/path;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md` v1.6.

Accepted continuation requirements preserved by T4b include:

```text
continuation token, where used
    → opaque to client
    → bound to applicable query semantics
    → bound to Merchant/Audience scope where required

continuation token
    ≠ authentication credential
    ≠ resource authority
    ≠ Merchant Scope
```

T4b implements only the semantic affinity/validation boundary required before a later concrete opaque continuation representation can be trusted.

---

## 2. Governing invariant

A decoded continuation-affinity candidate is untrusted input.

It is valid only when it matches both:

1. the current registered bounded-query contract; and
2. the server-established API Audience Observation Context for the current request.

Canonical T4b comparison:

```text
ApiBoundedCollectionContract
    query identity
    maximum page size
    stable ordering basis
    permitted filter vocabulary
    continuation-semantics reference
    scope/query-affinity-rule reference
        +
server-established ApiAudienceObservationContext
    exact API contract provenance
    exact API surface provenance
    exact Merchant Scope
    exact observer Audience
    exact Semantic Registry Release
        +
untrusted decoded ApiContinuationAffinityCandidate
        ↓
ApiContinuationAffinityValidator
        ↓
exact match
    → semantic affinity valid

any mismatch
    → fail closed with a bounded mismatch reason
```

`ObservationRequestBinding` remains request-local establishment affinity and is not continuation identity.

`BoundedProjectionReadBinding` remains bounded-read observation affinity and is not continuation identity.

---

## 3. Tests-first RED evidence

Tests-only RED commit:

- `51bb31fbc72413cbae1e5edd4d7eace00507d31d` — `test(imp-06): add T4b continuation affinity RED`.

GitHub Actions:

- run `33804624914` / job `100812014052` — **FAILED as intended**.

The production source set compiled successfully. Test compilation then failed specifically because the new T4b production types did not yet exist:

- `ApiContinuationAffinityCandidate`;
- `ApiContinuationAffinityValidator`; and
- `ApiContinuationAffinityValidationFailure`.

No unrelated repository failure was used as RED evidence.

---

## 4. Minimum production implementation

Production commits:

- `1ac925f74af96287548a5f825a9892606b3a5250` — `feat(imp-06): add T4b continuation affinity candidate`;
- `1d1ead752c0482f3aad91c6930caa842dd226c16` — `feat(imp-06): add T4b affinity failures`;
- `86fb043f6cb5cbb9963a29cfa326817f68ecc1f7` — `feat(imp-06): complete T4b continuation affinity validator`.

The implementation adds only:

- `ApiContinuationAffinityCandidate` — immutable, structurally validated, untrusted decoded affinity material;
- `ApiContinuationAffinityValidationFailure` — closed fail-closed mismatch vocabulary; and
- `ApiContinuationAffinityValidator` — semantic comparison against the registered bounded-query contract and server-established API Audience Observation Context.

The validator checks:

1. current bounded-query identity against the exposed API context;
2. current API provenance contract identity and API surface against that context;
3. exact bounded-query static semantics:
   - query identity;
   - maximum page size;
   - stable ordering basis;
   - permitted filter references;
   - continuation-semantics reference; and
   - scope/query-affinity-rule reference;
4. exact Merchant Scope;
5. exact observer Audience plus permitted API-surface/Audience pairing; and
6. exact Semantic Registry Release identifier.

A mismatch fails closed as one of:

- `CURRENT_QUERY_CONTEXT_MISMATCH`;
- `QUERY_SEMANTICS_MISMATCH`;
- `MERCHANT_SCOPE_MISMATCH`;
- `AUDIENCE_MISMATCH`; or
- `SEMANTIC_RELEASE_MISMATCH`.

No token codec, cursor representation, HTTP adapter, repository read, query execution or transport mapping is introduced.

---

## 5. Minimum GREEN evidence

Minimum implementation-head verification:

- code head `86fb043f6cb5cbb9963a29cfa326817f68ecc1f7`;
- GitHub Actions run `33804817024` / job `100812640995`;
- configured full gate `mvn --batch-mode clean verify -Ppostgres-it`;
- result **SUCCESS**.

---

## 6. Adversarial and architecture review

Adversarial hardening commit:

- `fdd859a2a68e62407bff8cc6fe50f52ff4c54b45` — `test(imp-06): harden T4b continuation affinity invariants`.

The hardened suite falsifies the principal continuation-affinity failure modes.

### 6.1 Static query-semantic drift

Each of the following independently invalidates the candidate:

- cross-query identity reuse;
- maximum-page-size drift;
- stable-ordering-basis drift;
- permitted-filter-vocabulary drift;
- continuation-semantics drift; and
- scope/query-affinity-rule drift.

Result: `QUERY_SEMANTICS_MISMATCH`.

### 6.2 Cross-context replay

The suite rejects:

- Merchant A affinity replay under Merchant B;
- PUBLIC affinity replay as CUSTOMER; and
- affinity from one Semantic Registry Release replayed under another release.

The corresponding failure remains distinguishable as Merchant Scope, Audience or Semantic Registry Release mismatch.

### 6.3 Current-context/provenance laundering

The suite rejects cases where:

- the API context claims a different query from the bounded contract;
- the API context claims the expected query while underlying request provenance names a different query; or
- exposed API surface and underlying request-provenance surface disagree.

Result: `CURRENT_QUERY_CONTEXT_MISMATCH`.

### 6.4 Surface/Audience laundering

A synthetically inconsistent surface/subject combination cannot make a candidate valid merely because its other fields match. Surface/Audience compatibility is checked from the server-established context.

### 6.5 Candidate integrity

The candidate:

- defensively copies permitted-filter membership;
- exposes an immutable filter set;
- rejects non-positive page size; and
- rejects blank stable-order, filter, continuation, affinity-rule and Semantic Registry Release references.

### 6.6 Architectural separation

Reflection-based architecture tests confirm that T4b does not absorb:

- `ObservationRequestBinding`;
- `BoundedProjectionReadBinding`;
- a generic `Map` payload;
- Java `Serializable` token representation; or
- token encoder/decoder methods.

The validator exposes only its semantic `validate(...)` operation.

### Falsification conclusion

```text
structurally valid but cross-query candidate
    → rejected

structurally valid but changed query contract
    → rejected

cross-merchant continuation replay
    → rejected

cross-audience continuation replay
    → rejected

cross-release continuation replay
    → rejected

API-context/provenance laundering
    → rejected

request binding promoted to continuation identity
    → structurally absent

bounded-read binding promoted to continuation identity
    → structurally absent

token/codec concern absorbed into T4b
    → structurally absent
```

No implementation-discovered material design ambiguity remains for T4b.

---

## 7. Final verification

Final code-bearing baseline:

`development@fdd859a2a68e62407bff8cc6fe50f52ff4c54b45`

GitHub Actions:

- workflow: `Maven Tests`;
- run: `33805294267`;
- job/check: `100814217917`;
- command configured by workflow: `mvn --batch-mode clean verify -Ppostgres-it`;
- result: **SUCCESS**.

The hardened T4b tests therefore pass together with the repository's unit/conformance and PostgreSQL integration verification. This record does not invent an aggregate test count that is not required for the closure decision.

---

## 8. Composite-architecture conformance

T4b uses the smallest fitting responsibility split:

```text
immutable declarative bounded-query contract
    +
immutable untrusted affinity candidate
    +
server-established opaque Audience Observation Context
    +
stateless fail-closed semantic validator
```

It does not introduce persistence, a repository abstraction, transport codec, HTTP/controller concern, generic claims bag, token serialization framework, distributed workflow or business-capability dependency.

Result: **PASS**.

---

## 9. Explicit non-claims and deferred responsibilities

T4b closure does **not** claim completion of:

- opaque continuation token encoding or decoding;
- cursor/keyset position representation;
- concrete applied filter-value preservation across pages;
- a concrete HTTP/query adapter;
- concrete query execution;
- owner repository reads;
- transport DTO construction;
- exact ActiveRelease identity as continuation affinity where no accepted continuation rule requires it;
- T2d concrete adapters generally;
- T3c concrete mappings generally;
- T4c opaque continuation encoding/adapter;
- S3 Public Interaction Binding; or
- IMP-06 as a macro target.

The separation is deliberate:

```text
request-local ObservationRequestBinding
    ≠ continuation identity

BoundedProjectionReadBinding
    ≠ continuation identity

T4b semantic affinity validation
    ≠ concrete token representation
    ≠ concrete cursor position
    ≠ T4c encoding/adapter

static permitted-filter vocabulary affinity
    ≠ concrete applied filter-value/cursor-state affinity
```

Concrete applied filter values, position/keyset state and opaque token representation belong to the later concrete bounded-query adapter/T4c path under its accepted authority.

---

## 10. Dependency consequence

T4b is **CONFORMING_COMPLETE**.

Its completion satisfies the T4b prerequisite of T4c, but it does not satisfy T4c's separate concrete-query-adapter prerequisite:

```text
T4b
    CONFORMING_COMPLETE
        ↓
T4c
    still BLOCKED_DEPENDENCY on concrete query adapter
```

No blanket `T2d` or `T3c` implementation is promoted merely to unblock T4c. The current fine-grained graph classifies those concrete adapters/mappings as ON_DEMAND, activated by a concrete production vertical slice under accepted API authority.

Accordingly, T4b closure does not itself identify another dependency-complete production-code node. The next governed action is an IMP-06 macro applicability/completion review after this evidence, the T4b graph refresh and `implementation-status.md` synchronisation are committed consistently and the cycle-closing head verifies successfully.

No broader promotion follows automatically:

- `IMP-06` remains **PARTIALLY_CONFORMING** pending that macro review;
- `S3` remains **BLOCKED_DEPENDENCY** on authoritative capability-owned Public Interaction participation sources under `MS-WATCH-002`;
- `T4c` remains **BLOCKED_DEPENDENCY** on a concrete query adapter;
- `T2d` and `T3c` remain **ON_DEMAND**; and
- `IMP-07` remains **BLOCKED_DEPENDENCY** on complete IMP-06.
