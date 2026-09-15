# MS-PROT-027 v1.14 — Observation Contribution Request-Binding Construction Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.14  
**Status:** **ACCEPTED by explicit manual approval on 3 September 2026**  
**Approved:** 3 September 2026  
**Authority type:** Production Observation Contribution request-binding construction architecture amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `designs/IMPLEMENTATION-RULES.md`; `designs/MS-IMP-001.md`  
**Amends:** MS-PROT-027 v1.9 and MS-PROT-027 v1.13  
**Depends on:** MS-PROT-027 v1.9, v1.11, v1.12 and v1.13  
**Closes:** `IMP-06-BR4-DG-001`  
**Purpose:** Establish the trusted production construction boundary by which a capability-owned `EstablishedObservationContribution` receives the exact `ObservationRequestBinding` and Merchant Scope of an already established E3 Observation Request without exposing a general request-unwrapping API, transferring contribution ownership to generic Surface infrastructure, or weakening request affinity.

---

## 1. Problem

MS-PROT-027 v1.9 requires every `EstablishedObservationContribution` to expose:

```text
owner-qualified ObservationContributionKind
ObservationRequestBinding
MerchantScope
```

and requires concrete contribution implementations to remain:

```text
final
package-private
capability-owned
```

MS-PROT-027 v1.13 additionally requires owner-qualified Material Affinity Observation Contributions for Exposure decisions semantically affined to bounded material progress.

The existing E3 request boundary deliberately exposes `EstablishedObservationRequest` as an opaque trusted result.

Its exact:

```text
ObservationRequestBinding
MerchantScope
Semantic Registry Release
request provenance
```

remain inspectable by trusted E3 implementation code rather than through a general public unwrapping contract.

Consequently, a capability package such as Profile cannot conformingly construct its required contribution because it cannot obtain the exact E3-created `ObservationRequestBinding`.

This is a missing construction contract.

It SHALL NOT be resolved by weakening contribution affinity, manufacturing another binding, moving capability semantics into Surface, or exposing the complete request internals.

---

## 2. EstablishedObservationRequest remains opaque

`EstablishedObservationRequest` SHALL remain an opaque trusted request-establishment result.

This amendment SHALL NOT add a general public accessor equivalent to:

```text
request.requestBinding()
request.merchantScope()
request.semanticRegistryRelease()
request.provenance()
```

and SHALL NOT make `DefaultEstablishedObservationRequest` publicly constructible.

The existing E3 authority to mint and inspect the exact request binding remains unchanged.

---

## 3. Trusted contribution-construction boundary

E3 SHALL provide one generic trusted **Observation Contribution Construction Boundary**.

Conceptually:

```text
exact EstablishedObservationRequest
        +
capability-owned contribution constructor
        ↓
trusted E3 construction boundary
        ↓
exact ObservationRequestBinding
trusted MerchantScope
        ↓
capability-owned constructor
        ↓
EstablishedObservationContribution
```

The boundary is infrastructure for exact request binding only.

It SHALL NOT:

```text
interpret capability business values
interpret owner progress ordering
read owner repositories
select Exposure decisions
manufacture capability contribution contents
discover capability implementations by reflection
create generic business payloads
```

---

## 4. Capability-owned constructor

A capability MAY supply a request-scoped contribution constructor through the generic construction boundary.

The constructor SHALL receive only the generic trusted information required to construct the existing contribution contract:

```text
exact ObservationRequestBinding
trusted MerchantScope
```

It MAY additionally close over capability-owned evidence already established for the bounded operation.

For a Material Affinity Observation Contribution, that evidence SHALL originate from the applicable coherent owner/bounded-material acquisition.

The constructor SHALL NOT obtain expected material progress through a later independent owner read.

Canonical:

```text
coherent bounded owner material M / progress R8
        ↓
capability-owned constructor closes over M/R8
        +
E3 supplies exact request binding Q1 and Merchant Scope
        ↓
contribution Q1 / M / R8
```

Prohibited:

```text
bounded material R8
        ↓
contribution constructor re-queries owner
        ↓
current R9
        ↓
contribution claims R9 for bounded R8
```

Current owner-state comparison remains BR5/E4 evaluator responsibility.

---

## 5. Construction ownership

Ownership remains:

```text
Surface / E3
    owns:
        Observation Request establishment
        ObservationRequestBinding creation
        trusted extraction of request binding
        trusted extraction of Merchant Scope
        generic contribution-construction boundary

Capability owner
    owns:
        contribution kind
        concrete contribution class
        contribution contents
        material-affinity entry semantics
        derivation of expected owner progress
        owner conformance tests

Application orchestration
    may:
        coordinate already established request
        bounded owner acquisition
        owner contribution construction

Application orchestration SHALL NOT:
        manufacture ObservationRequestBinding
        reinterpret owner progress
        manufacture contribution contents
```

Generic Surface SHALL NOT depend upon Profile, Booking, Inventory or another capability merely to construct owner contributions.

---

## 6. Concrete contribution implementation

The existing v1.9 rule remains authoritative:

```text
EstablishedObservationContribution
```

is the public owner-extension interface, while each concrete production implementation remains:

```text
final
package-private
to its owning capability
```

The contribution construction boundary SHALL invoke the capability-owned constructor without transferring ownership of the resulting concrete type to Surface.

---

## 7. Construction-boundary validation

For one invocation, the E3 construction boundary SHALL:

1. accept exactly one already established Observation Request;
2. extract its exact E3-created `ObservationRequestBinding`;
3. extract its trusted Merchant Scope;
4. invoke the supplied capability constructor exactly once;
5. require a non-null `EstablishedObservationContribution`;
6. require:

```text
returnedContribution.requestBinding()
    ==
exact request binding extracted from the request
```

using exact binding identity rather than reconstructed equality;

7. require:

```text
returnedContribution.merchantScope()
    ==
trusted request Merchant Scope
```

under the existing Merchant Scope equality semantics; and

8. reject malformed construction without yielding an established contribution.

Constructor failure or malformed output SHALL fail closed.

---

## 8. Existing E3 validation remains authoritative

The construction boundary SHALL NOT replace the existing Audience Observation Context contribution checks.

Context establishment SHALL continue to validate:

```text
exact Semantic Registry Release definition snapshot
registered ObservationContributionKind
exact runtime implementation binding
permitted audience
Observation Request binding
Merchant Scope
cardinality
duplicate contribution kind
```

Therefore:

```text
successful construction
    ≠
accepted contribution in Audience Observation Context
```

and:

```text
contribution presence
    ≠
Exposure authority
```

---

## 9. No authority created by binding possession

`ObservationRequestBinding` remains:

```text
opaque
non-serialisable
request-scoped
non-credential
non-authoritative
```

Possession or retention of a binding SHALL NOT constitute:

```text
authentication
authorisation
merchant authority
Projection Serviceability
Exposure permission
repository capability
mutation authority
continuation authority
```

The contribution's existing public `requestBinding()` contract remains unchanged.

This amendment does not treat the binding as secret material.

Its purpose is affinity, not secrecy.

---

## 10. Cross-request reuse

A contribution established for:

```text
Observation Request Q1
```

SHALL NOT be accepted for:

```text
Observation Request Q2
```

even when:

```text
Merchant Scope is equal
Semantic Registry Release is equal
candidate identities are equal
business values are equal
owner progress identifiers are equal
```

Exact `ObservationRequestBinding` identity remains controlling.

---

## 11. Cross-merchant material substitution

A capability-owned Material Affinity contribution constructor SHALL reject material whose owner-established Merchant Scope differs from the trusted Merchant Scope supplied by E3.

Generic E3 SHALL NOT inspect business material to prove this condition.

The owning capability SHALL prove it through owner conformance tests.

Therefore:

```text
material for Merchant A
+
request for Merchant B
```

cannot produce a conforming Profile material-affinity contribution merely because both observations use the same progress identifier.

---

## 12. Material-affinity contribution contents

MS-PROT-027 v1.13 §§25–27 remain authoritative.

A Material Affinity Observation Contribution MAY retain only the owner-specific integrity information required to identify:

```text
candidate/member concerned
expected bounded-material source progress/revision
```

for a bounded immutable set of entries.

It SHALL NOT contain:

```text
rendered business values
generic business payloads
precomputed EXPOSE/WITHHOLD results
credentials
provider secrets
mutation authority
owner-progress ordering interpretation
```

Generic E3 SHALL treat those owner-specific contents as opaque.

---

## 13. Initial Profile contribution registration

For the initial PUBLIC Merchant Presence proof, Profile SHALL register one contribution kind:

```text
ObservationContributionKind
    ownerCapabilityIdentifier = "profile"
    contributionIdentifier    = "material-affinity"
```

Its contribution cardinality SHALL be:

```text
SINGLE
```

Its initially permitted audience SHALL be:

```text
PUBLIC
```

The contribution MAY contain multiple bounded candidate/member-to-expected-progress entries for Profile-owned material in the same Observation Request.

This registration is intended initially for Profile elements whose governing Exposure decision is materially revision/progress-affined, including:

```text
profile / public-contact-point
profile / public-merchant-location
```

Baseline-exposed Profile elements do not require a material-affinity contribution merely because material exists for them.

A later CUSTOMER or other audience registration requires accepted authority for that audience; PUBLIC registration SHALL NOT silently imply it.

---

## 14. Exact-release registration

The Profile material-affinity contribution definition SHALL use the existing exact-release `ObservationContributionDefinitionRegistrySnapshot` mechanism.

Its exact Java implementation SHALL use the existing separate `ObservationContributionRuntimeBindingSnapshot` mechanism.

No new semantic-definition encoding format is introduced by this amendment.

The existing distinction remains:

```text
semantic contribution definition
    ≠
Java runtime implementation binding
```

---

## 15. No generic contribution payload abstraction

This amendment SHALL NOT introduce:

```text
Map<String,Object>
JsonNode
reflection-driven contribution data
generic capability payload
generic policy evidence bag
generic owner-progress schema
```

to represent capability-owned contribution contents.

A generic construction boundary is not a generic business-data container.

---

## 16. Failure classification

The following are structural contribution-construction failures:

```text
missing established request
missing capability constructor
constructor failure
null contribution
wrong request binding
wrong Merchant Scope
malformed contribution metadata
```

They SHALL NOT be converted into:

```text
EXPOSE
WITHHOLD
Projection NOT_SERVICEABLE
owner UNRESOLVED
```

Those outcomes belong to their existing governing boundaries.

No successful partial externally committed representation may be produced from a structural construction failure.

---

## 17. BR4 boundary

This amendment authorises BR4 to implement:

```text
generic trusted contribution-construction binding
Profile owner-qualified material-affinity contribution type
Profile material-affinity contribution definition registration
Profile exact runtime binding
request-affinity validation
Merchant-Scope-affinity validation
bounded immutable material-affinity entries
architecture/conformance tests
```

BR4 SHALL NOT implement:

```text
BR5 current owner-progress evaluation
current Contact Point or Location choice comparison
BR6 same-read E4-positive fragment selection
S2 final Surface assembly
T1b4 transport/query delivery
post-E4 material reacquisition
generic owner progress interpretation
```

---

## 18. Required BR4 falsification

BR4 conformance tests SHALL demonstrate at least:

```text
Q1 contribution accepted only for Q1
Q1 contribution rejected for Q2
Merchant A material cannot bind to Merchant B request
unregistered contribution definition rejected
wrong exact runtime class rejected
wrong audience rejected
duplicate SINGLE contribution rejected
constructor invoked no more than once
constructor failure fails closed
material-affinity contents contain no rendered business value
generic Surface does not depend on Profile
Profile contribution implementation remains final and package-private
```

Owner-specific tests SHALL additionally establish that expected material progress is derived from the bounded owner material/acquisition evidence and not from an independent later owner read.

---

## 19. Architectural consequences

Accepted consequence:

```text
E3 gains one narrow owner-extension construction seam
```

in exchange for preserving all of:

```text
opaque EstablishedObservationRequest
exact request affinity
capability ownership
generic Surface isolation
no generic payload
no Profile → Surface ownership inversion
no Surface → Profile dependency
BR4 / BR5 semantic separation
```

The construction seam is intentionally smaller than exposing a general request-introspection API.

---

## 20. Non-goals

This amendment does not define:

```text
new business status
new Exposure decision
new merchant policy
new Projection Serviceability semantics
new Contact Point semantics
new Merchant Location semantics
new persistence model
new transport endpoint
continuation semantics
new provider responsibility
new generic business payload
new Surface-to-capability dependency
```

---

## 21. Implementation sequence after approval

Implementation SHALL resume:

```text
formalise MS-PROT-027 v1.14
        ↓
update Authority Index / applicable governance navigation
        ↓
corpus-conformance review
        ↓
commit accepted authority
        ↓
refresh dynamic IMP-06 dependency graph if necessary
        ↓
BR4 tests-only RED
        ↓
observe intended RED
        ↓
minimum BR4 GREEN
        ↓
targeted + architecture verification
        ↓
BR4 adversarial falsification
        ↓
full PostgreSQL verification
        ↓
BR4 evidence
        ↓
mark BR4 CONFORMING_COMPLETE only if all gates pass
        ↓
promote BR5
```

Any newly discovered material decision remains subject to the same manual approval lifecycle.
