# MS-PROT-027 v1.7 — Projection Contract Representation & Registration Amendment

**Document ID:** MS-PROT-027
**Version:** 1.7
**Status:** **ACCEPTED after deferred-decision review, falsification and manual approval**
**Approved:** 30 August 2026
**Amends:** composite MS-PROT-027 through v1.6 only within the exact Java representation and in-process registration mechanism for Projection Contracts
**Closes:** `MS-PROT-027-V14-DQ-001`
**Depends on:** MS-PROT-027 v1.1–v1.6; MS-PROT-041 v1.1; MS-PROT-050; MS-PROT-051 v1.1; ADR-010; ADR-011; ADR-013
**Purpose:** Establish the minimum immutable, exact-release Java contract and registry representation required to register Projection Contracts without implementing Projection Serviceability evaluation, projection persistence, source checkpoints, transport representation or a generic projection framework.

---

## 1. Governing decision

Production Projection Contracts SHALL be immutable typed definitions held in
one immutable registry snapshot for one exact Semantic Registry Release.

```text
exact Semantic Registry Release R
        +
immutable ProjectionContractRegistrySnapshot@R
        +
owner-qualified ProjectionContractIdentity
        ↓
zero or one exact Projection Contract definition
```

The registry is a release-affined specification catalogue. It records which
authoritative sources and policy identities govern a projection; it does not
contain projection data and does not itself determine current serviceability.

Lookup never substitutes a contract from another release. An absent contract,
wrong release or unresolved policy reference cannot support a claim that a
projection is serviceable.

---

## 2. Problem and governed scope

MS-PROT-027 v1.3 requires an explicit owner-scoped Projection Contract when an
applicability trigger exists. MS-PROT-027 v1.4 establishes the initial
`platform / merchant-presence` and `calendar / merchant-calendar` contracts but
defers their exact Java representation and registration mechanism.

This amendment governs only:

- immutable Java values for Projection Contract identity and references;
- the closed applicability-trigger and materialisation vocabularies;
- immutable read-use contract definitions;
- immutable exact-release registry construction and lookup; and
- code registration of the two initial Projection Contracts.

MS-PROT-027 v1.1–v1.6 remains authoritative for source ownership, Projection
Serviceability, Exposure, stale-serving, missing evidence, rebuild claims and
the request-scoped initial architecture.

---

## 3. Explicit non-goals

This amendment does not govern or introduce:

- a Projection Serviceability evaluator or runtime serviceability verdict;
- source-revision/checkpoint value representation;
- a projection database, cache, Redis dependency or materialised read model;
- projection update workers, replay, retry or reconciliation;
- transport/API response representation;
- CDN/static generation or storefront snapshots;
- Exposure resolution or Audience Observation Context;
- an executable policy DSL, expression engine or generic rules engine;
- a generic projection service, server or framework;
- source capability queries or composition orchestration; or
- mutation/command authority derived from projection state.

P1 registration therefore establishes specifications only. P2 remains a
separate implementation node for exact serviceability evaluation.

---

## 4. Canonical Java representation

The minimum production representation is conceptually:

```text
ProjectionContractDefinition
{
    identity: ProjectionContractIdentity
    applicabilityTriggers: Set<ProjectionContractApplicabilityTrigger>
    authoritativeSourceReferences: Set<ProjectionSourceDependencyReference>
    materialisationKind: ProjectionMaterialisationKind
    freshnessPolicyReference: ProjectionPolicyReference
    readUseContracts: Set<ProjectionReadUseContract>
    revocationPolicyReference?: ProjectionPolicyReference
    rebuildPolicyReference?: ProjectionPolicyReference
}
```

The implementation SHALL use immutable values and immutable collections. Java
records and ordinary immutable code registration are the initial mechanism.
Framework annotations, database rows, JSON properties, cache keys and frontend
components are not Projection Contract authority.

---

## 5. Projection Contract identity

The exact Java identity is:

```text
ProjectionContractIdentity
{
    ownerIdentifier
    contractIdentifier
}
```

Both identifiers are required non-blank stable semantic identifiers. Equality
is exact value equality over both fields.

The identity remains stable across releases when the same semantic contract
continues. Exact runtime identity is nevertheless:

```text
Semantic Registry Release identifier
        +
ProjectionContractIdentity
```

so the same stable identity may resolve to different accepted definitions in
different releases without cross-release substitution.

---

## 6. Source dependency reference

An authoritative source dependency is represented by:

```text
ProjectionSourceDependencyReference
{
    ownerIdentifier
    sourceIdentifier
}
```

Both identifiers are required and non-blank. The reference identifies an
accepted authoritative source responsibility. It neither copies source facts
nor transfers their mutation authority to the Projection Owner.

The set records all source responsibilities that can materially participate in
the registered contract. Whether a conditional source participates in a
particular request is later determined by the owning query/application and
serviceability contracts.

---

## 7. Applicability-trigger vocabulary

The exact closed Java vocabulary is:

```text
PERSISTED_OR_CACHED
ASYNCHRONOUS_UPDATE
REGISTERED_DEPENDENCY
SOURCE_OUTAGE_SERVING
DELIBERATE_KNOWN_LAG
DIVERGENT_MULTI_SOURCE
REVOCATION_SENSITIVE
```

These values correspond exactly to MS-PROT-027 v1.3 Triggers A–G. A registered
contract SHALL retain at least one trigger. The set records why an explicit
contract is required; it does not assert that the triggering runtime evidence
is currently satisfied.

---

## 8. Materialisation-kind vocabulary

The exact closed Java vocabulary is:

```text
REQUEST_SCOPED
MATERIALISED
```

`REQUEST_SCOPED` means the initial representation is derived for the bounded
read request from accepted owner query boundaries. `MATERIALISED` means a
representation persists beyond the request and therefore activates every
applicable source-progress, rebuild and update obligation from MS-PROT-027
v1.3.

Registration of `MATERIALISED` does not itself create a store, checkpoint or
worker. Those require their governing authority and implementation evidence.

---

## 9. Policy reference

A policy required by a Projection Contract is referenced by:

```text
ProjectionPolicyReference
{
    ownerIdentifier
    policyIdentifier
}
```

Both identifiers are required and non-blank. The reference names one accepted
owner-qualified policy responsibility. It does not contain executable logic,
current source evidence or a precomputed verdict.

P1 registers these references. A later serviceability evaluator MUST resolve
and apply the exact referenced policy under accepted authority. Missing or
unresolved policy authority MUST NOT be treated as success, currentness or
permission to serve.

---

## 10. Read-use identity and contract

Materially different read purposes are represented explicitly:

```text
ProjectionReadUseIdentity
{
    ownerIdentifier
    readUseIdentifier
}

ProjectionReadUseContract
{
    readUseIdentity
    serviceabilityPolicyReference
    missingEvidencePolicyReference
    staleServingPolicyReference
}
```

All identities and references are required. A Projection Contract SHALL retain
at least one read-use contract and SHALL reject duplicate read-use identities.

The three policy references remain distinct because:

```text
evidence is present but does not support this use
        ≠
required evidence is missing or unverifiable
        ≠
evidence proves the representation is known stale
```

The read-use contract registers these distinctions; it does not calculate an
outcome.

---

## 11. Revocation and rebuild policy references

`revocationPolicyReference` is optional only when the registered contract has
no contract-specific revocation rule beyond separately mandatory current
privacy, security and Exposure authority. Where present, it identifies the
policy that constrains continued serving after revocation-sensitive evidence.

`rebuildPolicyReference` is optional only when no separate materialised state
exists and recomputation is fully described by the request-scoped owner-query
contract. Where present, it identifies the policy governing reconstruction and
the claims that may be made during reconstruction.

Neither reference grants stale-serving permission or proves that retained
evidence is sufficient for rebuild.

---

## 12. Structural validation

Construction of a Projection Contract definition SHALL:

1. require a non-null identity;
2. require at least one non-null applicability trigger;
3. require at least one non-null authoritative source reference;
4. require a non-null materialisation kind;
5. require a non-null freshness-policy reference;
6. require at least one non-null read-use contract;
7. reject duplicate read-use identities;
8. defensively copy every collection; and
9. expose only immutable collections.

For `REQUEST_SCOPED` definitions, rebuild-policy absence means recomputation
from current owner queries only; it does not claim a materialised rebuild
facility.

---

## 13. Exact-release registry snapshot

The registration mechanism is:

```text
ProjectionContractRegistrySnapshot
{
    semanticRegistryReleaseIdentifier
    contractsByIdentity
}
```

Construction SHALL:

1. require one non-blank exact release identifier;
2. defensively copy all contract definitions;
3. reject null definitions;
4. reject duplicate `ProjectionContractIdentity` values;
5. preserve immutable contract/reference collections; and
6. expose deterministic lookup by exact release plus exact contract identity.

The registry is a static release-affined definition authority. It is not a
projection store, current-serviceability cache, mutable latest alias or
business-fact owner.

---

## 14. Lookup contract

Registry lookup accepts:

```text
requested Semantic Registry Release identifier
requested ProjectionContractIdentity
```

and returns:

```text
exact contract present
or
unresolved
```

Lookup SHALL return unresolved when the requested release differs from the
snapshot release or the exact identity is absent. It SHALL NOT search another
snapshot, select a newer release, match by display label, route, source type or
Java class, or infer a contract from a cache implementation.

The registry does not return Projection Serviceability. A later evaluator may
claim serviceability only after resolving the exact registered definition and
the current evidence required by its exact policy references.

---

## 15. Initial registration mechanism

Until accepted authority requires a projection-definition bundle encoding,
production may assemble the immutable snapshot explicitly from trusted
code-owned definitions at the release/deployment composition boundary.

The initial mechanism SHALL require the exact release identifier as input and
return a new immutable snapshot. It SHALL NOT inspect a mutable `latest`
pointer or infer release identity from process state.

This is an in-process modular-monolith registration mechanism. It introduces
no registry server, network call, database, dynamic merchant registration API,
policy engine or generic projection framework.

---

## 16. Initial Merchant Presence contract

The initial portfolio SHALL register:

```text
identity
    platform / merchant-presence

materialisation
    REQUEST_SCOPED

applicability triggers
    REGISTERED_DEPENDENCY
    DIVERGENT_MULTI_SOURCE

authoritative source responsibilities
    profile / merchant-public-descriptor
    profile / contact-points
    profile / merchant-locations
    profile / service-areas
    profile / external-presence-links
    business-hours / public-business-hours

freshness policy
    platform / merchant-presence-current-owner-evidence

read use
    platform / public-merchant-presence

serviceability policy
    platform / merchant-presence-truthful-serviceability

missing-evidence policy
    platform / merchant-presence-reduced-or-unserviceable

stale-serving policy
    platform / no-known-stale-profile-or-location

revocation policy
    exposure / current-observation-restrictions

rebuild policy
    absent — request-scoped recomputation from current owner queries
```

The definition preserves the v1.4 behaviour: independently current descriptor,
contact or location elements may remain serviceable when Business Hours cannot
be established; the affected hours element is omitted/unavailable; no stale or
invented hours are served; and the result is not misclassified as an Exposure
`WITHHOLD` outcome.

One `generatedAt` value is not sufficient evidence for all source
responsibilities. Known-stale Profile or Location facts are not intentionally
served as current.

---

## 17. Initial Main Street Calendar contract

The initial portfolio SHALL register:

```text
identity
    calendar / merchant-calendar

materialisation
    REQUEST_SCOPED

applicability triggers
    REGISTERED_DEPENDENCY
    SOURCE_OUTAGE_SERVING
    DIVERGENT_MULTI_SOURCE

authoritative source responsibilities
    appointment / commitments
    booking / applicable-timing
    calendar / merchant-schedule-intents
    scheduling / applicable-configuration
    business-hours / operating-windows
    scheduling / resource-capacity
    calendar-integration / external-busy-constraints

freshness policy
    calendar / merchant-calendar-independent-source-evidence

read uses
    calendar / committed-work-overview
    calendar / availability-oriented

committed-work serviceability policy
    calendar / current-main-street-commitments

committed-work missing-evidence policy
    calendar / independently-established-commitments-only

committed-work stale-serving policy
    calendar / no-unproven-commitment-currentness

availability serviceability policy
    scheduling / all-required-availability-inputs-current

availability missing-evidence policy
    scheduling / no-current-availability-claim

availability stale-serving policy
    scheduling / no-stale-availability-claim

revocation policy
    absent — current security, privacy and Exposure authority remains mandatory

rebuild policy
    absent — request-scoped recomputation from current owner queries
```

The definition preserves the v1.4 distinction: an optional external constraint
source being unavailable does not erase independently current Main Street
commitments, but the same reduced representation MUST NOT claim current
availability or infer that unknown time is free.

Calendar remains projection owner only. Appointment, Booking, Scheduling,
Business Hours, resource/capacity and provider evidence remain owned by their
accepted authorities.

---

## 18. Failure and immutability semantics

Invalid contract or registry construction is a deterministic
programming/registration failure and SHALL reject construction.

Ordinary exact lookup absence is unresolved rather than substituted. A later
serviceability evaluator owns the exact runtime failure/result representation.

Definitions do not mutate after construction. Registration for a new release
creates a new snapshot; it does not alter a snapshot already associated with an
earlier release.

No database transaction, retry, concurrency or idempotency semantics are
introduced because P1 represents immutable static definitions, not mutable
business or projection data.

---

## 19. Projection, Exposure and command boundary

The registered flow remains:

```text
authoritative facts
        ↓
exact Projection Contract lookup
        ↓
later Projection Serviceability evaluation
        ↓
serviceable / reduced / not serviceable read result
        ↓
current Exposure, security, privacy and Surface constraints
        ↓
audience response
```

Registration success does not establish source currentness, serviceability,
Exposure, Actor Authorisation, Commercial Entitlement, Provider Readiness or
command eligibility. Projection data never authorises authoritative mutation.

---

## 20. Falsification record

### Duplicate contract identity

Two definitions in one release claim `platform / merchant-presence`. Registry
construction rejects the ambiguity. **PASS**

### Duplicate read-use identity

One Calendar definition registers two different policies for
`calendar / availability-oriented`. Contract construction rejects the
ambiguity. **PASS**

### Wrong release lookup

A request pinned to `R17` is given a registry snapshot for `R18`. Lookup is
unresolved; `R18` is not substituted. **PASS**

### Missing policy implementation

The exact contract resolves, but its serviceability policy cannot be resolved
by P2. P1 cannot claim serviceability and cannot replace the policy with a
default. **PASS**

### Business Hours unavailable

Current Merchant Presence descriptor/contact evidence may form a reduced
result while Business Hours is omitted. Missing hours is neither invented nor
relabelled as Exposure `WITHHOLD`. **PASS**

### External Calendar unavailable

Current Main Street commitments may remain visible. Availability-oriented use
does not claim current availability and does not infer unknown time as free.
**PASS**

### Request-scoped registration

The contract exists because accepted triggers apply, but no cache, database,
checkpoint or rebuild worker is introduced. **PASS**

### Command attempts to use projection

Registration provides no mutation authority or operational eligibility. The
owning command still performs authoritative revalidation. **PASS**

---

## 21. Rejected alternatives

- hard-coded `if`/`switch` selection scattered across query code;
- a generic Projection DSL, policy engine or rules engine;
- one global TTL or `generatedAt` serviceability rule;
- a mutable map exposed to callers;
- lookup by route, frontend property, Java class or display label;
- implicit latest-release fallback;
- one global identity without an owner qualifier;
- embedding executable policy expressions in contract definitions;
- embedding current source evidence or serviceability results in definitions;
- introducing Redis, a database, background workers or a registry service; and
- treating a Projection Contract as Projection Serviceability, Exposure or
  command authority.

---

## 22. Trade-offs and accepted consequences

Main Street accepts explicit typed records and owner-qualified policy
references rather than either scattered code branches or a universal
projection framework.

This creates several small value types and requires later P2 work to resolve
policy references explicitly. In exchange, release affinity, ownership,
read-use differences and missing/stale evidence boundaries are inspectable and
testable without pre-building persistence or an executable DSL.

A reusable framework may be reconsidered only after multiple concrete
implementations demonstrate common behaviour under
`MS-PROT-027-V14-DQ-012`.

---

## 23. Hard invariants

1. Every registry snapshot belongs to one exact Semantic Registry Release.
2. Every contract, source, read-use and policy identity is owner-qualified.
3. Duplicate contract identities in one snapshot are rejected.
4. Duplicate read-use identities in one definition are rejected.
5. Definitions and registry collections are immutable.
6. Every definition declares at least one accepted applicability trigger,
   source dependency and read-use contract.
7. Materialisation kind uses the closed `REQUEST_SCOPED | MATERIALISED`
   vocabulary.
8. Wrong-release and absent-contract lookups are unresolved.
9. No lookup may substitute latest or infer a contract from implementation
   shape.
10. Policy references are stable identities, not executable expressions,
    source evidence or verdicts.
11. Missing policy/evidence authority cannot establish serviceability.
12. Initial registration contains exactly Merchant Presence and Main Street
    Calendar.
13. Both initial contracts are request-scoped and introduce no projection
    store.
14. Merchant Presence preserves independently current elements without
    inventing missing source values.
15. Calendar distinguishes committed-work overview from
    availability-oriented use.
16. External-source uncertainty never means free availability.
17. Registration does not manufacture Projection Serviceability, Exposure,
    authorisation, entitlement, provider readiness or command authority.
18. P2 evaluation, source checkpoints, persistence, workers, transport and a
    generic framework remain outside this amendment.

---

## 24. Deferred-decision and implementation consequence

`MS-PROT-027-V14-DQ-001` is resolved by this amendment.

IMP-06 P1 may now implement the minimum typed values, immutable exact-release
registry snapshot and two-contract initial portfolio using RED → GREEN.

Implementation SHALL stop before claiming Projection Serviceability if the
exact policy-evaluator representation, current evidence inputs or query
orchestration required for P2 are not already governed. Existing
`MS-PROT-027-V14-DQ-002` through `DQ-014` remain unchanged and are not activated
merely by P1 registration.

---

## 25. Amendment effect

This amendment:

1. preserves MS-PROT-027 v1.3 as generic Projection Contract authority;
2. preserves MS-PROT-027 v1.4's initial projection semantics;
3. resolves only `MS-PROT-027-V14-DQ-001`;
4. establishes immutable owner-qualified Java values and closed vocabularies;
5. establishes an immutable exact-release registry snapshot and exact lookup;
6. code-registers `platform / merchant-presence` and
   `calendar / merchant-calendar`;
7. records read-use distinctions through policy references without evaluating
   them;
8. preserves request-scoped initial materialisation and source ownership;
9. preserves Projection Serviceability, Exposure and command authority as
   distinct downstream boundaries; and
10. leaves every other v1.4 deferred question unchanged.

---

## 26. Acceptance statement

> **A Projection Contract is an immutable owner-qualified, exact-release
> specification that declares why the contract applies, which authoritative
> sources participate, how materialisation is classified and which exact
> policies govern freshness, read-use serviceability, missing evidence, stale
> serving, revocation and rebuild where applicable. Registration resolves the
> specification only; current evidence and a later evaluator determine
> serviceability, Exposure remains subsequent, and no projection can become
> command authority.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
