# MS-PROT-027 v1.8 — Projection Serviceability Evaluation & Current Evidence Amendment

**Document ID:** MS-PROT-027
**Version:** 1.8
**Status:** **ACCEPTED after implementation-gap review, falsification and manual approval**
**Approved:** 30 August 2026
**Amends:** composite MS-PROT-027 through v1.7 only within production Projection Serviceability evaluation, exact policy-evaluator registration, current source-evidence input and decision provenance
**Closes:** `IMP-06-P2-DG-001`
**Depends on:** MS-PROT-027 v1.1–v1.7; MS-PROT-041 v1.1; MS-PROT-050; MS-PROT-051 v1.1; ADR-010; ADR-011; ADR-013
**Purpose:** Establish the minimum deterministic, exact-release production evaluator that turns the policy references registered by P1 plus server-established current source evidence into a fail-closed Projection Serviceability result for the initial Merchant Presence and Main Street Calendar read uses.

---

## 1. Governing decision

Production Projection Serviceability SHALL be evaluated by resolving the exact
Projection Contract and every applicable exact policy-evaluator binding for the
same Semantic Registry Release, then applying those evaluators to
server-established evidence for the contract's exact authoritative source set.

```text
exact release + exact contract + exact read use
        ↓
exact P1 Projection Contract definition
        ↓
exact policy-evaluator bindings
        +
server-established source evidence values
        ↓
deterministic policy assessments
        ↓
FULLY_SERVICEABLE | REDUCED_SERVICEABLE | NOT_SERVICEABLE
        +
retained decision provenance
```

No caller assertion, mutable latest alias, default evaluator, deployment
snapshot or cache state may substitute for an unresolved exact contract,
policy binding or required evidence value.

---

## 2. Problem and governed scope

MS-PROT-027 v1.7 registers stable policy identities but deliberately does not
make those identities executable. P2 requires an exact runtime boundary before
Merchant Presence or Calendar may claim Projection Serviceability.

This amendment governs only:

- exact-release registration and resolution of executable policy evaluators;
- the closed policy-category vocabulary;
- server-established current source-evidence values;
- deterministic evaluation and fail-closed composition;
- the structured serviceability result and retained provenance; and
- the initial Merchant Presence and Calendar read-use behavior.

It does not change source-fact ownership, Projection Contract registration,
Exposure, Actor Authorisation, Commercial Entitlement, Provider Readiness,
Surface membership or command authority.

---

## 3. Explicit non-goals

This amendment does not introduce or govern:

- a projection database, cache, Redis dependency or materialised read model;
- a source checkpoint persistence schema;
- projection update workers, event replay, rebuild workers or reconciliation;
- API routes, HTTP status, JSON shape or presentation wording;
- Audience Observation Context or Exposure resolution;
- source capability mutation or command eligibility;
- a generic policy DSL, expression language, reflection-based evaluator lookup
  or remote rules service;
- client-supplied freshness/serviceability booleans;
- one global TTL or elapsed-age freshness rule; or
- a generic framework beyond the minimum typed in-process registry and
  deterministic evaluator boundary.

The initial portfolio remains request-scoped. Consequently
`MS-PROT-027-V14-DQ-002` and the materialisation/rebuild questions remain
inactive.

---

## 4. Canonical terms and semantic classification

### 4.1 Policy Evaluator Binding

A **Policy Evaluator Binding** associates one exact owner-qualified
`ProjectionPolicyReference` and one exact policy category with one immutable
evaluator identity and executable evaluator for one exact Semantic Registry
Release.

The binding is implementation architecture. It does not own source facts or
merchant policy.

### 4.2 Projection Source Evidence

**Projection Source Evidence** is the server-established evidence value for
one exact `ProjectionSourceDependencyReference` during one bounded evaluation.
It records source progress, evidence identity, observation time, availability,
completeness and revocation state where applicable.

It is runtime read evidence, not a business fact and not a precomputed
serviceability verdict.

### 4.3 Projection Serviceability Result

A **Projection Serviceability Result** is the derived result of one exact
evaluation. It retains the exact contract, read use, policy bindings, source
evidence, evaluation time, outcome, reason codes and omitted source elements.

It is not a lifecycle state on a merchant, source fact or Projection Contract.

---

## 5. Closed policy-category vocabulary

The exact categories are:

```text
FRESHNESS
SERVICEABILITY
MISSING_EVIDENCE
STALE_SERVING
REVOCATION
REBUILD
```

Every P1 policy reference SHALL resolve under its semantic category. The same
owner-qualified text under another category is not a substitute.

The policy references required for one evaluation are:

1. the contract's freshness policy;
2. the selected read use's serviceability policy;
3. the selected read use's missing-evidence policy;
4. the selected read use's stale-serving policy;
5. the contract's revocation policy when present; and
6. the contract's rebuild policy only when the requested read path requires
   rebuild assessment.

The initial request-scoped P2 path has no rebuild policy and performs no
rebuild assessment.

---

## 6. Evaluator identity and equality

An evaluator has the immutable identity:

```text
ProjectionPolicyEvaluatorIdentity
{
    ownerIdentifier
    evaluatorIdentifier
    evaluatorVersionIdentifier
}
```

All three values are required and non-blank. Equality is exact value equality.

Evaluator version is explicit so a code change that materially changes a
policy decision cannot masquerade as the same executable decision provenance.
Changing an evaluator's material semantics requires the applicable governed
design/release lifecycle.

---

## 7. Exact-release evaluator registry

The production registration boundary is conceptually:

```text
ProjectionPolicyEvaluatorRegistrySnapshot
{
    semanticRegistryReleaseIdentifier
    bindingsBy(policyCategory, policyReference)
}
```

Construction SHALL:

1. require one non-blank exact release identifier;
2. require immutable typed bindings;
3. reject null bindings;
4. reject duplicate category/reference identities;
5. reject a binding whose evaluator identity or executable evaluator is absent;
6. defensively copy registration collections; and
7. expose exact lookup only.

Lookup SHALL return unresolved when the requested release differs, the policy
category differs or the exact policy reference is absent. It SHALL NOT select
another release, a newer evaluator version, another category, a default
implementation or a binding inferred from Java type, route or display label.

---

## 8. Server-established source evidence representation

One source evidence value is conceptually:

```text
ProjectionSourceEvidence
{
    sourceReference
    evidenceIdentifier
    observedProgressIdentifier?
    requiredCurrentProgressIdentifier?
    observedAt
    availability
    completeness
    revocationState
}
```

The closed availability values are:

```text
AVAILABLE
UNAVAILABLE
NOT_APPLICABLE
```

The closed completeness values are:

```text
COMPLETE
PARTIAL
MISSING
CORRUPT
UNVERIFIABLE
NOT_APPLICABLE
```

The closed revocation values are:

```text
CLEAR
REVOKED
UNVERIFIABLE
NOT_APPLICABLE
```

The source owner or trusted server-side application/query boundary establishes
these values. A public/client request SHALL NOT establish them.

`observedAt` and an evidence identifier are retained provenance. Observation
time alone does not prove currentness. The initial currentness predicate is:

```text
availability == AVAILABLE
AND completeness == COMPLETE
AND observedProgressIdentifier is present
AND requiredCurrentProgressIdentifier is present
AND observedProgressIdentifier == requiredCurrentProgressIdentifier
AND revocationState in { CLEAR, NOT_APPLICABLE }
```

Different progress token formats remain owned by their source boundaries; P2
requires exact equality and does not impose numeric ordering or one global
checkpoint format.

Known stale evidence exists when both progress identifiers are present and
unequal. Missing, corrupt, unverifiable, unavailable or revoked evidence is not
current.

`NOT_APPLICABLE` is valid only when the trusted boundary has established that a
conditional source does not participate in the requested read. It is not a
caller escape hatch for omitting a required source.

---

## 9. Exact evidence-set invariant

An evaluation request SHALL contain exactly one evidence value for every source
reference declared by the exact P1 Projection Contract.

Duplicate, absent or undeclared source references cause
`NOT_SERVICEABLE`. A conditional source remains represented with
`NOT_APPLICABLE`; it is not silently removed from provenance.

This exact-set rule makes evidence omission observable and prevents a caller or
query adapter from obtaining a more permissive result by withholding an
unfavourable source.

---

## 10. Evaluation request

The bounded evaluation request contains:

```text
semanticRegistryReleaseIdentifier
ProjectionContractIdentity
ProjectionReadUseIdentity
evaluationTime
exact source-evidence set
```

The request does not contain:

```text
isFresh
isServiceable
allowStale
allowReduced
expose
authorised
```

The trusted boundary supplies `evaluationTime`; evaluators SHALL NOT read an
uncontrolled system clock. The initial policies do not derive currentness from
elapsed age.

---

## 11. Deterministic evaluation operation

The operation SHALL execute in this order:

1. resolve the exact P1 contract by exact release and contract identity;
2. resolve the exact read-use contract;
3. verify the exact evidence-set invariant;
4. derive the exact required policy reference/category set;
5. resolve every required exact evaluator binding from the same release;
6. invoke every resolved evaluator using the same immutable request evidence;
7. validate each assessment against the contract source set; and
8. compose the final result and retained provenance.

An unresolved step before evaluator invocation produces `NOT_SERVICEABLE`.
No partially resolved set may be treated as permission to serve.

Evaluator execution is deterministic ordinary typed code. It performs no
source mutation, database write, network access or hidden latest lookup.

---

## 12. Outcomes and composition

The exact closed outcome vocabulary is:

```text
FULLY_SERVICEABLE
REDUCED_SERVICEABLE
NOT_SERVICEABLE
```

Policy assessments compose conservatively:

```text
NOT_SERVICEABLE
    outranks REDUCED_SERVICEABLE

REDUCED_SERVICEABLE
    outranks FULLY_SERVICEABLE
```

Therefore the final outcome is the most restrictive assessment. An evaluator
exception, null/invalid assessment, unresolved evaluator, invalid omitted
source or invalid provenance produces `NOT_SERVICEABLE` with a stable reason.

`REDUCED_SERVICEABLE` requires at least one truthful independently serviceable
element and an explicit set of omitted source responsibilities. It cannot mean
"serve everything with a warning".

---

## 13. Structured result and provenance

Every result SHALL retain:

- exact Semantic Registry Release identifier;
- exact Projection Contract identity;
- exact read-use identity;
- exact evaluation time;
- final outcome;
- stable reason codes;
- every exact policy category/reference/evaluator identity consumed;
- the complete immutable source-evidence set supplied;
- the exact omitted source-reference set; and
- unresolved policy references where fail-closed evaluation could not consume
  them.

The result SHALL expose immutable collections. It SHALL reject construction
whose provenance identities do not match the result's contract evaluation.

Transport mapping and audience-facing wording remain downstream. The result is
sufficient for later API/Surface code to map without inventing whether serving
was full, reduced or prohibited.

---

## 14. Stable reason classification

The production result SHALL distinguish at least:

```text
EXACT_CONTRACT_UNRESOLVED
EXACT_READ_USE_UNRESOLVED
POLICY_EVALUATOR_UNRESOLVED
EVIDENCE_SET_MISMATCH
SOURCE_UNAVAILABLE
SOURCE_EVIDENCE_MISSING
SOURCE_EVIDENCE_PARTIAL
SOURCE_EVIDENCE_CORRUPT
SOURCE_EVIDENCE_UNVERIFIABLE
SOURCE_KNOWN_STALE
SOURCE_REVOKED
REQUIRED_SOURCE_NOT_CURRENT
REDUCED_TRUTHFUL_REPRESENTATION
POLICY_EVALUATION_FAILED
```

These are read-decision reasons, not business lifecycle states and not HTTP
status codes.

---

## 15. Merchant Presence evaluation

For `platform / merchant-presence` and
`platform / public-merchant-presence`:

1. current `profile / merchant-public-descriptor` evidence is the minimum
   serviceability requirement;
2. if descriptor evidence is not current, the result is
   `NOT_SERVICEABLE`;
3. when descriptor evidence is current and every applicable declared source is
   current, the result is `FULLY_SERVICEABLE`;
4. when descriptor evidence is current but another applicable declared source
   is unavailable, incomplete, missing, corrupt, unverifiable, known stale or
   revoked, the affected source is omitted and the result is
   `REDUCED_SERVICEABLE`;
5. a known-stale Profile, Contact Point, Location, Service Area, External
   Presence or Public Business Hours value is never served as current; and
6. current revocation evidence outranks ordinary stale/reduced serving.

The result does not decide PUBLIC Exposure. Exposure remains a later current
constraint over the already-serviceable candidate elements.

---

## 16. Calendar committed-work evaluation

For `calendar / merchant-calendar` and
`calendar / committed-work-overview`:

1. `appointment / commitments` and `booking / applicable-timing` are the
   independently material commitment sources;
2. at least one of those two sources must have current evidence;
3. when neither commitment source is current, the result is
   `NOT_SERVICEABLE`;
4. when all applicable declared sources are current, the result is
   `FULLY_SERVICEABLE`; and
5. when at least one commitment source is current but another declared source
   is not current, the current commitment source may remain, every unproven
   source is omitted and the result is `REDUCED_SERVICEABLE`.

External calendar unavailability therefore cannot erase independently current
Main Street commitments. It also cannot establish availability.

---

## 17. Calendar availability evaluation

For `calendar / merchant-calendar` and
`calendar / availability-oriented`:

1. every applicable declared source required by the trusted Scheduling/query
   boundary must be current;
2. a conditionally absent source must be explicitly represented as
   `NOT_APPLICABLE` by that trusted boundary;
3. when every applicable source is current, the result is
   `FULLY_SERVICEABLE`;
4. when any applicable source is unavailable, incomplete, missing, corrupt,
   unverifiable, known stale or revoked, the result is
   `NOT_SERVICEABLE`; and
5. this initial availability read use has no reduced outcome that may claim
   availability.

Unknown external constraints never mean free time. A caller that needs current
availability must not substitute the separately serviceable committed-work
result.

---

## 18. Exposure, Surface and command boundary

The complete ordering remains:

```text
authoritative owner evidence
        ↓
Projection Serviceability evaluation
        ↓
current Exposure / security / privacy constraints
        ↓
Surface contribution and transport mapping
        ↓
audience response
```

`FULLY_SERVICEABLE` or `REDUCED_SERVICEABLE` does not establish Exposure,
Actor Authorisation, Commercial Entitlement, Surface membership, Provider
Readiness or command eligibility.

No Projection Serviceability result may authorise an authoritative mutation.
Commands continue to revalidate at their owning capability.

---

## 19. Failure, retry, concurrency and persistence

Evaluation is a side-effect-free deterministic read operation over immutable
definitions, bindings and evidence. Repeating the same exact request produces
an equal semantic result.

No database transaction, optimistic lock, idempotency record or concurrency
state is introduced by P2. Source queries independently retain their owning
consistency contracts.

Technical evaluator failure fails closed and remains distinguishable from:

```text
source business rejection
Exposure/security/privacy withhold
projection infrastructure failure
projection rebuild/catch-up
```

---

## 20. Falsification record

### Caller asserts fresh data

A caller supplies `isFresh=true` without source progress evidence. The request
shape has no such authority and evaluation cannot be made permissive. **PASS**

### Exact policy missing

The P1 contract resolves but one stale-serving evaluator does not. The result
is `NOT_SERVICEABLE`; another policy or release is not substituted. **PASS**

### Evidence source omitted

The Calendar request omits external-busy evidence instead of declaring trusted
`NOT_APPLICABLE`. The exact source set fails and the result is
`NOT_SERVICEABLE`. **PASS**

### Merchant hours unavailable

Descriptor evidence is current and Business Hours is unavailable. Presence is
`REDUCED_SERVICEABLE`, hours is explicitly omitted and no stale hours are
served. **PASS**

### Essential merchant descriptor unavailable

Other presence evidence is current but descriptor evidence is unavailable.
The result is `NOT_SERVICEABLE`. **PASS**

### External calendar unavailable

Current Main Street commitments remain `REDUCED_SERVICEABLE`; availability is
`NOT_SERVICEABLE`. **PASS**

### Optional integration disabled

The trusted boundary establishes external-busy constraints as
`NOT_APPLICABLE`. Availability evaluation ignores that source rather than
classifying it unavailable. **PASS**

### Stale progress with recent timestamp

`observedAt` is recent but observed and required progress identifiers differ.
The evidence is known stale. Time does not manufacture currentness. **PASS**

### Revoked cached element

Ordinary source progress matches but revocation state is `REVOKED`. The element
cannot be served merely because other freshness evidence is current. **PASS**

### Command consumes serviceability

A command receives `FULLY_SERVICEABLE` projection evidence. The result grants
no mutation authority and the command still revalidates its owner state.
**PASS**

---

## 21. Rejected alternatives

- caller-provided `fresh`, `available`, `serviceable` or `allowStale` flags;
- policy lookup by latest release, Java class, route or display name;
- a default permissive evaluator;
- one evaluator that silently ignores unresolved P1 policy references;
- one `generatedAt` or global TTL as currentness authority;
- omission of unfavourable source evidence;
- one boolean result without reason/provenance;
- treating `REDUCED_SERVICEABLE` as permission to serve omitted values;
- converting Projection Serviceability into Exposure or command authority;
- a generic DSL, remote policy service, database or cache for the initial
  request-scoped portfolio; and
- activating checkpoint/materialisation decisions before a non-request-scoped
  projection exists.

---

## 22. Trade-offs and accepted consequences

Main Street accepts several typed evidence and provenance values plus explicit
evaluator registration. This is more verbose than caller booleans or scattered
conditionals, but makes exact release, policy ownership, source evidence,
fail-closed behavior and reduced-result omissions inspectable and testable.

The design deliberately uses ordinary typed in-process code rather than a
generic rules engine. Reusable projection infrastructure remains revisitable
only after multiple implementations prove common requirements under
`MS-PROT-027-V14-DQ-012`.

The exact source progress token remains opaque to P2. This avoids making P2 the
owner of every source's revision scheme while still requiring equality against
the owner-established required current progress.

---

## 23. Hard invariants

1. Evaluation is exact-release, exact-contract and exact-read-use.
2. Every applicable P1 policy reference resolves under its exact category.
3. Evaluator identities are owner-qualified and versioned.
4. Missing or failed evaluator resolution is never success.
5. Evidence is established server-side and contains no caller serviceability
   verdict.
6. Every declared source has exactly one evidence value.
7. Observation time alone does not establish currentness.
8. Currentness requires exact observed/required progress equality plus
   available, complete and non-revoked evidence.
9. Missing, corrupt, unverifiable, unavailable, stale or revoked evidence is
   never implicitly current.
10. The final outcome is the most restrictive policy assessment.
11. Reduced results explicitly identify omitted sources.
12. Results retain exact evaluator and evidence provenance.
13. Merchant Presence requires a current merchant descriptor.
14. Merchant Presence may omit independently unserviceable non-descriptor
    elements without inventing them.
15. Calendar committed-work may preserve independently current Main Street
    commitments during optional-source failure.
16. Calendar availability requires every applicable source to be current.
17. Unknown external constraints never mean free availability.
18. Projection Serviceability remains distinct from Exposure, Surface,
    authorisation, entitlement, provider readiness and command authority.
19. P2 introduces no projection persistence, cache, worker or API contract.
20. Existing materialisation/checkpoint deferred decisions remain inactive.

---

## 24. Implementation consequence

`IMP-06-P2-DG-001` is resolved by this amendment.

IMP-06 P2 may implement:

- the typed policy categories, evaluator identities and exact-release registry;
- immutable source-evidence values and deterministic currentness predicates;
- the evaluation request, policy assessments and structured result;
- fail-closed exact resolution and conservative composition; and
- the initial Merchant Presence and Calendar policy evaluators.

Implementation SHALL use RED → GREEN and SHALL stop before adding projection
persistence, source queries/orchestration, Exposure resolution, Surface/API
mapping or materialisation/checkpoint machinery unless their independent nodes
are READY under accepted authority.

---

## 25. Amendment effect

This amendment:

1. preserves the composite MS-PROT-027 Projection Contract and portfolio;
2. executes P1 policy references through exact typed evaluator bindings;
3. defines server-established source evidence without caller verdicts;
4. defines deterministic fail-closed evaluation and structured provenance;
5. establishes the initial Merchant Presence and Calendar serviceability
   behavior;
6. preserves source ownership, Exposure separation and no-command-authority;
7. resolves only `IMP-06-P2-DG-001`; and
8. leaves `MS-PROT-027-V14-DQ-002` through `DQ-014` unchanged and inactive
   unless their existing revisit conditions are independently reached.

---

## 26. Acceptance statement

> **Projection Serviceability is a deterministic exact-release read decision,
> produced by exact executable bindings for every applicable P1 policy and
> server-established evidence for every declared source. Missing authority or
> evidence fails closed; reduced serving explicitly omits unproven elements;
> the result retains exact policy/evaluator/evidence provenance; Merchant
> Presence and Calendar preserve truthful independently serviceable facts
> without inventing missing facts or availability; and Projection
> Serviceability never becomes Exposure or command authority.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
