# MS-PROT-027 v1.6 — Exposure Element Contract Representation & Registration Amendment

**Document ID:** MS-PROT-027
**Version:** 1.6
**Status:** **ACCEPTED after deferred-decision review, falsification and manual approval**
**Approved:** 30 August 2026
**Amends:** composite MS-PROT-027 through v1.5 only within the exact Java representation and in-process registration mechanism for Exposure Element Contracts
**Closes:** `MS-PROT-027-V15-DQ-001`
**Depends on:** MS-PROT-027 v1.2–v1.5; MS-PROT-031; MS-PROT-049 v1.0–v1.3; MS-PROT-053; ADR-010; ADR-011; ADR-013
**Purpose:** Establish the minimum immutable, exact-release Java contract and registry representation required to register Exposure Element Contracts without deciding semantic-bundle encoding, owner-policy persistence, transport representation or runtime requirement-evaluator wiring.

---

## 1. Governing decision

Production Exposure Element Contracts SHALL be represented as immutable typed
definitions held in one immutable registry snapshot for one exact Semantic
Registry Release.

```text
exact Semantic Registry Release R
        +
immutable ExposureElementContractRegistrySnapshot@R
        +
owner-qualified ExposureElementContractIdentity
        ↓
zero or one exact contract
```

Lookup never substitutes a contract from another release. Absence, ambiguity
or release mismatch leaves the contract unresolved and therefore preserves
MS-PROT-027 v1.5's fail-closed `WITHHOLD` consequence at the later resolver
boundary.

---

## 2. Scope and unchanged authority

This amendment governs only:

- the exact initial Java value types for a contract and its references;
- the immutable exact-release registry snapshot;
- registration uniqueness and structural validation;
- deterministic exact lookup; and
- the code-registered initial Merchant Presence PUBLIC portfolio.

MS-PROT-027 v1.2–v1.5 continues unchanged for candidate legitimacy,
Projection Serviceability, current Audience Observation Context, restriction
precedence, owner-qualified current policy, requirement evaluation and the
binary `EXPOSE | WITHHOLD` verdict.

This amendment does not govern:

- semantic-bundle wire encoding or materialisation;
- PostgreSQL storage of Profile-owned Exposure choices;
- mutation operations for those choices;
- the exact Audience Observation Context Java representation;
- requirement-evaluator registration or invocation;
- full Exposure resolution orchestration;
- transport/API outcomes;
- caching, telemetry, indexing, CDN or presentation; or
- any new audience-facing capability element beyond the v1.5 portfolio.

Those scopes remain owned by their existing deferred decisions and accepted
authorities.

---

## 3. Canonical Java representation

The minimum production representation is conceptually:

```text
ExposureElementContract
{
    identity: ExposureElementContractIdentity
    exposableElementReference: ExposableElementReference
    audience: SurfaceAudience
    baselineDecision: ExposureDecision
    merchantChoiceSource?: MerchantExposureChoiceSourceReference
    requirementReferences: Set<ExposureRequirementReference>
}
```

The implementation SHALL use immutable values. Java records and immutable
collections are the initial mechanism; framework annotations, database rows,
JSON properties and frontend components are not contract authority.

---

## 4. Contract identity

The exact Java identity is:

```text
ExposureElementContractIdentity
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
ExposureElementContractIdentity
```

so the same stable identity may resolve to different accepted definitions in
different releases without permitting cross-release substitution.

---

## 5. Exposable element reference

The governed element is identified by:

```text
ExposableElementReference
{
    ownerIdentifier
    elementIdentifier
}
```

Both identifiers are required and non-blank. The reference names one
authoritative field, bounded semantic group or audience-safe projection
element already accepted by MS-PROT-027 v1.5.

It is deliberately distinct from the contract identity. A contract identity
names the observation contract; the element reference names the candidate
semantic/read element being evaluated.

---

## 6. Audience and baseline

`audience` SHALL use the existing exact `SurfaceAudience` vocabulary:

```text
PUBLIC
CUSTOMER
MERCHANT
```

`baselineDecision` SHALL use the existing exact `ExposureDecision` vocabulary:

```text
EXPOSE
WITHHOLD
```

Neither value may be null. The baseline has only the v1.5 meaning after
candidate legitimacy and governing restrictions; it is not a shortcut around
Projection Serviceability, tenant validity, privacy, security or owner policy.

---

## 7. Merchant choice source reference

Where v1.5 permits merchant discretion, a contract MAY retain:

```text
MerchantExposureChoiceSourceReference
{
    ownerIdentifier
    sourceIdentifier
}
```

The reference identifies the owner-qualified current source from which a later
resolver must obtain policy evidence. It does not contain the current choice,
persist a duplicate policy value, select a repository implementation or grant
permission by its mere presence.

Absence means that the contract does not declare an additional merchant-choice
source. It does not mean that higher-order restrictions are absent.

---

## 8. Requirement references

Every contract retains an immutable set of zero or more:

```text
ExposureRequirementReference
{
    ownerIdentifier
    requirementIdentifier
}
```

Each reference is required, owner-qualified and exact. The set expresses
logical dependency only. It neither embeds a boolean expression nor registers
an evaluator.

The exact evaluator representation and runtime wiring remain downstream. A
missing or unresolved required evaluator continues to fail closed under
MS-PROT-027 v1.5.

---

## 9. Exact-release registry snapshot

The registration mechanism is:

```text
ExposureElementContractRegistrySnapshot
{
    semanticRegistryReleaseIdentifier
    contractsByIdentity
}
```

Construction SHALL:

1. require one non-blank exact release identifier;
2. defensively copy all contract definitions;
3. reject null definitions;
4. reject duplicate `ExposureElementContractIdentity` values;
5. preserve immutable contract and reference collections; and
6. expose deterministic lookup by exact release plus exact contract identity.

The registry is a static read-definition authority for its release. It is not
merchant-owned mutable state, a database, a global latest alias or a runtime
Exposure-result cache.

---

## 10. Lookup contract

The registry lookup operation accepts:

```text
requested Semantic Registry Release identifier
requested ExposureElementContractIdentity
```

and returns:

```text
exact contract present
or
unresolved
```

It SHALL return unresolved when:

- the requested release differs from the snapshot release;
- the exact identity is absent; or
- no unique structurally valid registration could be constructed.

It SHALL NOT search another snapshot, choose a numerically newer release,
match by element name, infer by audience or apply a default public contract.

The registry does not itself return the final Exposure verdict. The later
resolver converts unresolved exact contract lookup to `WITHHOLD` as already
required by v1.5.

---

## 11. Initial registration mechanism

Until `MS-PROT-027-V15-DQ-002` governs bundle encoding, production may assemble
the immutable snapshot explicitly from trusted code-owned definitions at the
release/deployment composition boundary.

The initial mechanism SHALL require the exact release identifier as input and
shall return a new immutable snapshot. It SHALL NOT inspect a mutable `latest`
pointer or infer release identity from process state.

This is an in-process modular-monolith registration mechanism. It does not
introduce a registry service, network call, policy engine, database or dynamic
merchant registration API.

---

## 12. Initial Merchant Presence PUBLIC portfolio

The code-registered initial portfolio contains exactly the v1.5 identities:

```text
profile / public-display-name
profile / public-tagline
profile / public-short-summary
profile / public-approved-description
profile / public-contact-point
profile / public-merchant-location
profile / public-service-area
profile / public-external-presence-link
business-hours / public-business-hours
```

Each contract uses an identically owner-qualified exposable-element reference
unless a later accepted authority explicitly separates them.

The registered baselines remain:

| Contract | Baseline | Merchant-choice source |
|---|---:|---|
| Profile public descriptors | `EXPOSE` | none |
| `profile / public-contact-point` | `WITHHOLD` | `profile / contact-point-public-exposure` |
| `profile / public-merchant-location` | `WITHHOLD` | `profile / merchant-location-public-exposure` |
| `profile / public-service-area` | `EXPOSE` | none at this contract layer |
| `profile / public-external-presence-link` | `EXPOSE` | none at this contract layer |
| `business-hours / public-business-hours` | `EXPOSE` | none |

All use audience `PUBLIC`. The initial contracts declare no additional
contract-specific requirement references. Governing tenant, protection and
candidate-legitimacy checks remain mandatory and are not erased by an empty
contract-specific set.

The Contact Point and Merchant Location source references do not assert that
their PostgreSQL representation already exists. They preserve the required
owner boundary while `MS-PROT-027-V15-DQ-003` remains unresolved for exact
Profile-choice persistence.

---

## 13. Release assembly and bundle boundary

The snapshot's exact release identifier makes release affinity executable at
the Java contract boundary.

This amendment does not decide:

```text
PublishedSemanticDefinitionSet exposure bytes
PackagedSemanticDefinitionBundle exposure section
codec / decoder representation
materialisation failure categories
deployment bundle compatibility
```

Those are `MS-PROT-027-V15-DQ-002` scope. Until that decision is accepted, the
registry snapshot SHALL NOT be silently encoded into an existing unrelated
section or omitted while claiming complete exposure-definition
materialisation.

---

## 14. Failure and immutability semantics

Invalid registry construction is a deterministic programming/registration
failure and SHALL reject construction.

Ordinary exact lookup absence is represented as unresolved rather than by
inventing a substitute definition. The later Exposure resolver owns the
fail-closed audience outcome.

Registry definitions do not mutate after construction. Re-registration for a
new release creates a new snapshot; it does not alter a snapshot already
associated with an earlier release.

No retry, concurrency or database transaction semantics are introduced because
this authority represents immutable release input, not mutable business state.

---

## 15. Falsification record

### Duplicate contract identity

Two definitions in one release claim `profile / public-contact-point`.
Snapshot construction rejects the ambiguity. **PASS**

### Wrong release lookup

A request resolved against `R17` is given a registry snapshot for `R18`.
Lookup is unresolved; `R18` is not substituted. **PASS**

### Missing internal-risk contract

A candidate references `profile / merchant-internal-risk-score`, which is not
registered. Lookup is unresolved and later resolution withholds it. **PASS**

### Merchant choice reference

The contact contract refers to Profile-owned current policy but stores no
merchant value itself. Profile remains the unique fact/policy owner. **PASS**

### Empty requirement set

A public descriptor contract has no contract-specific requirements. Tenant,
data-protection and candidate-legitimacy restrictions still precede the
baseline. **PASS**

### Cross-release evolution

The same stable contract identity exists in `R17` and `R18` with different
accepted definitions. Exact lookup resolves only within the requested release.
**PASS**

### Bundle deferral

The in-process snapshot can be tested and composed without inventing an
exposure bundle section. DQ-002 remains visible and active when materialisation
is attempted. **PASS**

---

## 16. Rejected alternatives

- a mutable map exposed to callers;
- contract lookup by field name, Java type, route or frontend property;
- implicit latest-release fallback;
- one global contract identity without an owner qualifier;
- raw strings for audience or baseline where canonical enums exist;
- embedding merchant current choices inside static contract definitions;
- embedding arbitrary boolean expressions in requirement references;
- using the registry as an Exposure decision cache;
- silently serialising contracts into the existing surface-definition section;
- introducing a database, microservice or generic policy framework; and
- treating registration success as permission to expose an illegitimate or
  unserviceable candidate.

---

## 17. Hard invariants

1. Every registry snapshot belongs to one exact Semantic Registry Release.
2. Every contract and element reference is owner-qualified.
3. Contract identity equality is exact owner plus contract identifier equality.
4. Duplicate identities in one snapshot are rejected.
5. Contracts and registry collections are immutable.
6. Audience and baseline use the existing canonical enums.
7. Merchant-choice references identify owner sources but store no current
   merchant policy value.
8. Requirement references are bounded identities, not expressions or
   evaluator registrations.
9. Wrong-release and absent-contract lookups are unresolved.
10. No lookup may substitute latest or infer a contract from implementation
    shape.
11. Registry lookup does not itself create an Exposure verdict.
12. Initial registration contains exactly the v1.5 Merchant Presence PUBLIC
    contract portfolio.
13. Registration does not manufacture candidate legitimacy, Projection
    Serviceability or current policy evidence.
14. DQ-002 bundle encoding, DQ-003 Profile persistence and DQ-004 transport
    representation remain unresolved and outside this amendment.
15. No dedicated service, database, cache, policy DSL or mutable global
    registry is introduced.

---

## 18. Deferred-decision and implementation consequence

`MS-PROT-027-V15-DQ-001` is resolved by this amendment.

IMP-06 may now implement the minimum typed values, immutable exact-release
registry snapshot and initial code-registered PUBLIC Merchant Presence
portfolio using RED → GREEN.

The implementation SHALL stop before claiming release-bundle materialisation
or full runtime Exposure resolution if doing so requires
`MS-PROT-027-V15-DQ-002`, `DQ-003`, the exact Audience Observation Context
representation or requirement-evaluator wiring that is not yet governed.

---

## 19. Acceptance statement

> **An Exposure Element Contract is an immutable owner-qualified typed
> definition registered in one exact Semantic Registry Release snapshot.
> Registration rejects ambiguity and exact lookup never falls forward to
> another release; it records only static contract meaning and owner-qualified
> references, never candidate legitimacy, current merchant policy, runtime
> permission or a cached Exposure result.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
