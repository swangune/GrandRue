# MS-PROT-027 v1.11 — Instance-Aware Exposure Membership & Owner-Filtered Deterministic Resolution Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.11  
**Status:** **ACCEPTED by explicit manual approval on 2 September 2026**  
**Approved:** 2 September 2026  
**Authority type:** Production Exposure/read-spine architecture amendment  
**Governed by:** MS-DESIGN-RULES-001; MS-IMPLEMENTATION-RULES-001; MS-IMP-001  
**Amends:** composite MS-PROT-027 through v1.10 only within candidate/member identity, Exposure Element Contract resolution, owner-evaluator binding, deterministic E4 resolution, positive result membership and E4-to-representation authority separation  
**Depends on:** composite MS-PROT-027 through v1.10; MS-PROT-051 v1.5; MS-PROT-031; MS-PROT-035 v1.1; MS-PROT-049; MS-PROT-053; MS-PROT-054; MS-PROT-062; MS-PROT-070; ADR-010; ADR-011; ADR-013; ADR-016  
**Closes:** `IMP-06-E4-DG-001`  
**Leaves downstream gate:** S2/T1b4 bounded-read representation affinity  
**Purpose:** Make production E4 deterministic and multi-instance-safe while preserving capability ownership, exact-release semantics, opaque Audience Observation Context, Projection Serviceability separation, fail-closed behaviour and value-blind Exposure.

---

## 1. Governing decision

E4 SHALL remain a generic mechanical Exposure resolver.

Canonical:

```text
already-legitimate serviceable candidates
        +
trusted Audience Observation Context
        +
current invocation-bound audience admission
        +
exact-release Exposure Element Contract registry
        +
exact-release owner evaluator bindings
        ↓
deterministic owner-filtered evaluation
        ↓
EXPOSE | WITHHOLD
        ↓
instance-aware positive membership
```

E4 SHALL NOT acquire business payloads, own business facts or contain capability-specific policy.

---

## 2. Read-spine identity-role invariant

Within the Surface/Exposure/read spine, the following identities SHALL remain distinct:

```text
ExposableElementReference
    → semantic/read element type

ExposureCandidateInstanceReference
    → stable owner-qualified business/read instance locator

domain revision identity
    → exact historical/current owner evidence

ExposureElementContractIdentity
    → static Exposure-definition identity

merchant-choice revision identity
    → owner policy evidence

ObservationRequest / invocation binding
    → request execution provenance

ExposedElementMembership
    → positive result membership for one Exposure invocation
```

No boundary may silently substitute one role for another.

In particular:

```text
stable instance identity
    ≠ revision identity

contract identity
    ≠ candidate identity

result membership
    ≠ data-acquisition authority
```

This is a read-spine invariant.

It does not introduce a universal Main Street identity framework.

---

## 3. Exposable element identity remains semantic

`ExposableElementReference` remains:

```text
ownerIdentifier
+
elementIdentifier
```

It identifies the bounded semantic/read element.

Examples:

```text
profile / public-contact-point
profile / public-merchant-location
business-hours / public-business-hours
```

It does not by itself identify:

```text
Contact Point C1
Location L2
Business Hours Scope BH3
```

---

## 4. Candidate instance reference

The accepted optional candidate-instance concept is refined conceptually as:

```text
ExposureCandidateInstanceReference
{
    ownerIdentifier
    instanceKindIdentifier
    instanceIdentifier
}
```

It identifies one stable owner-local candidate instance.

It SHALL NOT contain or imply:

```text
revision identity
database row identity
payload/value
permission
relationship verdict
Projection Serviceability
Exposure verdict
```

Possession of the reference grants no authority.

---

## 5. Exposure membership identity specification

Every Exposure Element Contract SHALL carry an exact immutable:

```text
ExposureMemberIdentitySpecification
```

with one of:

```text
SINGLETON

INSTANCE_QUALIFIED(
    ExposureCandidateInstanceKindReference
)
```

where:

```text
ExposureCandidateInstanceKindReference
{
    ownerIdentifier
    instanceKindIdentifier
}
```

No global candidate-instance-kind registry is introduced.

The owner-qualified kind is an exact semantic discriminator interpreted by its owner and mechanically compared by E4.

---

## 6. Instance owner affinity

For `INSTANCE_QUALIFIED`, the instance-kind owner SHALL equal the governed `ExposableElementReference` owner.

Example:

```text
profile/public-contact-point
    → profile/contact-point
```

not:

```text
profile/public-contact-point
    → booking/booking
```

A cross-capability fact may govern an Exposure requirement without becoming the candidate's membership owner.

---

## 7. Singleton meaning

`SINGLETON` means:

> At most one positive membership for this `ExposableElementReference` may exist in one bounded Exposure resolution.

A singleton candidate MAY carry an optional candidate-instance reference when a governing evaluator needs it.

That optional evaluation reference does not participate in result membership.

Therefore:

```text
singleton element + candidate instance
```

does not become:

```text
instance-qualified member
```

merely because an evaluator used the instance as evidence.

---

## 8. Instance-qualified meaning

`INSTANCE_QUALIFIED(kind)` means:

1. candidate instance reference is mandatory;
2. its owner/kind must equal the contract's exact required kind;
3. stable instance identity participates in positive result membership.

Example:

```text
profile/public-contact-point
+
profile/contact-point/C1
```

is distinct from:

```text
profile/public-contact-point
+
profile/contact-point/C2
```

---

## 9. Missing instance: structural versus unresolved

Two cases are distinct.

### Contractually required instance missing

For:

```text
INSTANCE_QUALIFIED(kind)
```

candidate instance absence or wrong kind is a structural candidate/contract mismatch.

The bounded Exposure resolution fails structurally.

### Evaluator-specific optional instance missing

For a structurally valid candidate where the contract does not require instance-qualified membership but an individual requirement evaluator needs an optional candidate instance:

```text
missing evidence
    → evaluator returns UNRESOLVED
    → candidate WITHHOLD
```

This preserves the accepted v1.9 fail-closed rule.

---

## 10. Exposure contract registry resolution

The exact-release registry SHALL retain:

```text
contract identity → contract
```

and additionally a deterministic immutable element index:

```text
ExposableElementReference
    →
audience variants
```

For one exact release:

```text
(element, audience)
```

may map to at most one contract.

Two contracts claiming the same:

```text
ExposableElementReference
+
SurfaceAudience
```

shall cause registry construction/materialisation failure.

---

## 11. Audience variants

One semantic element MAY have different contracts for different target audiences.

However, all audience variants for the same exact:

```text
ExposableElementReference
```

SHALL use the same:

```text
ExposureMemberIdentitySpecification
```

Audience may vary:

```text
baseline
merchant choice source
requirements
```

but SHALL NOT change whether the semantic element is singleton versus instance-qualified.

If differing cardinality is genuinely required, the owner must define a different semantic/read element.

---

## 12. Contract resolution semantics

For a submitted legitimate candidate:

```text
element absent from exact-release Exposure registry entirely
    → structural failure

element exists
but no contract for current observer/target audience
    → WITHHOLD due audience mismatch

exact audience variant exists
    → evaluate that exact contract
```

The resolver SHALL NOT ask the candidate to nominate its own contract identity.

Caller-selected contract identity is not authority.

---

## 13. Candidate ingress uniqueness

Candidate uniqueness SHALL be validated before conversion into any set/map that could silently deduplicate input.

For a `SINGLETON` element:

```text
membership identity
    = ExposableElementReference
```

so two candidate observations for the same singleton element in one resolution are structural duplicates even if their optional evaluator-instance references differ.

For `INSTANCE_QUALIFIED`:

```text
membership identity
    =
ExposableElementReference
+
exact stable candidate instance reference
```

Two distinct instances are legitimate.

The same exact member presented twice is structural failure.

---

## 14. Candidate legitimacy remains upstream

E4 receives already-legitimate candidates.

It SHALL NOT:

```text
discover Profile facts
discover Booking instances
query arbitrary domain state to create candidates
manufacture Surface participation
perform Projection Serviceability
infer business type
```

Instance qualification makes membership precise.

It does not transfer candidate-formation authority into Exposure.

---

## 15. Audience admission remains one current invocation decision

One current `AudienceObservationAdmissionResult` SHALL be established at the start of one bounded E4 resolution.

Rejected or structurally mismatched admission invalidates the whole resolution.

Audience admission SHALL NOT be repeated independently for every candidate.

The single accepted invocation evaluation instant SHALL be reused for time-dependent E4 evaluator interpretation.

---

## 16. Owner Exposure Evaluation Context

E4 SHALL NOT expose or unwrap `AudienceObservationContext` to capability evaluators.

It SHALL construct an immutable invocation-scoped:

```text
OwnerExposureEvaluationContext
```

for the exact evaluator/reference owner.

Its shared core MAY contain only:

```text
exact MerchantScope
SurfaceAudience
ObservationSubjectBinding
single invocation evaluation instant
owner-filtered established observation contributions
owner-filtered contextual-access proof where applicable
```

Private E4 machinery retains request/invocation/release bindings needed for integrity validation.

---

## 17. Evaluation context exclusions

`OwnerExposureEvaluationContext` SHALL NOT contain:

```text
raw session
token
credential
guest link
generic claims map
Map<String,Object>
Active Release internals
Projection Serviceability result
Provider Readiness
command authority
arbitrary business payload
precomputed relationship verdict
precomputed permission verdict
another owner's unfiltered contributions
global request-world-state
```

The context carries trusted request evidence.

It is not a policy snapshot.

---

## 18. Evaluator owner is the semantic-reference owner

The applicable evaluator owner is the owner of:

```text
ExposureRequirementReference
```

or:

```text
MerchantExposureChoiceSourceReference
```

not necessarily the candidate owner.

Example:

```text
candidate owner
    profile

requirement owner
    data-protection
```

is legitimate.

The Data Protection evaluator receives:

```text
shared trusted core
+
data-protection-owned evidence
```

not Profile's private contribution collection merely because the candidate is Profile-owned.

---

## 19. Cross-owner dependency rule

If one evaluator legitimately requires another capability's current business fact, that dependency SHALL use an accepted owner-to-owner contract.

Exposure context SHALL NOT be used as a hidden cross-owner data bus.

The generic resolver SHALL NOT acquire the other owner's business fact on the evaluator's behalf.

---

## 20. Exact-release evaluator binding

Production E4 SHALL use immutable exact-release runtime-binding snapshots for:

```text
ExposureRequirementReference
    → ExposureRequirementEvaluator

MerchantExposureChoiceSourceReference
    → MerchantExposureChoiceEvaluator
```

Bindings are exact owner-qualified references associated with one exact Semantic Registry Release.

No:

```text
latest
nearest version
Java-type inference
field-name inference
business-category inference
```

is permitted.

Runtime evaluator implementations are not semantic bundle definitions.

The exact semantic references they implement remain release-affined.

---

## 21. Batch-capable evaluator contract

The production evaluator boundary SHALL be batch-capable.

Conceptually:

```text
evaluateBatch(
    exact semantic evaluator reference,
    OwnerExposureEvaluationContext,
    immutable candidates
)
```

E4 SHALL group work by at least:

```text
exact Semantic Registry Release
+
exact evaluator semantic reference
+
exact evaluator owner
```

Candidates sharing one binding are submitted in one logical evaluator invocation.

This prevents E4 from imposing a mandatory one-evaluator-call-per-candidate execution shape.

---

## 22. Batch does not prescribe database technology

An owner MAY internally use:

```text
one set-based query
owner-local read model
owner-local request-scoped memoisation
another accepted owner-local mechanism
```

E4 SHALL NOT prescribe SQL shape, cache technology or repository implementation.

Batch-capable does not guarantee that an owner implementation performs one physical query.

---

## 23. Requirement evaluator result

For every submitted candidate, a requirement evaluator returns exactly one:

```text
SATISFIED
UNSATISFIED
UNRESOLVED
```

Meaning:

```text
SATISFIED
    → continue

UNSATISFIED
    → candidate WITHHOLD

UNRESOLVED
    → candidate WITHHOLD
```

A requirement evaluator SHALL NOT return final `EXPOSE`.

No individual requirement may override another governing restriction.

---

## 24. Merchant-choice evaluator result

For every submitted candidate, a merchant-choice evaluator returns exactly one:

```text
EXPOSE
WITHHOLD
UNRESOLVED
```

Meaning:

```text
EXPOSE
    → merchant broadening is permitted
      only after higher restrictions succeed

WITHHOLD
    → candidate WITHHOLD

UNRESOLVED
    → candidate WITHHOLD
```

Where no merchant-choice source is declared, the registered contract baseline applies after higher restrictions succeed.

---

## 25. Missing evaluator

If an otherwise structurally coherent exact-release runtime-binding snapshot lacks an evaluator required by a candidate contract:

```text
current required owner evidence cannot be established
    → candidate WITHHOLD
```

The missing binding SHALL be observable operationally.

It SHALL NOT be replaced from another release.

A mismatched/corrupt evaluator-binding registry itself is structural failure.

---

## 26. Read-only evaluator invariant

Exposure evaluators SHALL be read/evaluation authorities only.

They SHALL NOT:

```text
mutate authoritative business state
advance lifecycle
change merchant choice
dispatch a business command
create a business fact
perform provider effect
emit mutation events as business consequences
```

Owner-local non-authoritative request-scoped memoisation and operational metrics are permitted.

Implementations SHOULD depend on narrow owner-owned read/evaluation ports rather than mutation/application-service interfaces.

---

## 27. Exact batch-result coverage

For every batch invocation:

```text
submitted candidate observations
```

and:

```text
returned candidate-result identities
```

must match exactly.

Structural failure includes:

```text
missing submitted candidate result
result for unsubmitted candidate
duplicate candidate result
candidate-owner mismatch
candidate-instance mismatch
wrong semantic evaluator reference
cross-request result
cross-invocation result
```

Ordering of results carries no semantic meaning.

---

## 28. Expected unresolved versus implementation failure

Expected inability to establish current owner evidence is:

```text
UNRESOLVED
```

and fails closed at candidate level.

Unexpected conditions such as:

```text
uncaught implementation exception
malformed evaluator result
impossible invariant breach
runtime corruption
```

produce structural failure of the bounded Exposure resolution.

Unexpected evaluator failure SHALL NOT masquerade as legitimate `WITHHOLD`.

---

## 29. Deterministic precedence

For one structurally valid candidate:

```text
exact request/release/admission validation
        ↓
resolve element definition
        ↓
validate membership identity specification
        ↓
resolve current audience contract variant
        ↓
no audience variant
        → WITHHOLD
        ↓
evaluate governing requirements
        ↓
any UNSATISFIED / UNRESOLVED
        → WITHHOLD
        ↓
evaluate merchant choice when declared
        ↓
WITHHOLD / UNRESOLVED
        → WITHHOLD
        ↓
EXPOSE choice
        → EXPOSE
        ↓
otherwise registered baseline
        ↓
EXPOSE | WITHHOLD
```

Merchant `EXPOSE` never overrides an unsatisfied higher restriction.

---

## 30. Structural resolution failure

Whole-resolution fail-closed conditions include at least:

```text
rejected/mismatched audience admission
request/invocation mismatch
Semantic Registry Release mismatch
Exposure registry release mismatch
evaluator-binding registry release mismatch
candidate element absent from Exposure registry
contract/candidate structural mismatch
required membership instance absent
required membership instance wrong kind/owner
duplicate submitted member identity
malformed batch coverage
duplicate successful member identity
unexpected evaluator execution failure
unsafe API/result binding
```

Structural failure SHALL NOT become:

```text
WITHHOLD
RESOLVED(empty)
successful partial result
```

---

## 31. Legitimate partial membership

Candidate-local withholding remains independent.

Therefore:

```text
C1 → EXPOSE
C2 → WITHHOLD
C3 → EXPOSE
```

may produce:

```text
{ C1, C3 }
```

provided no structural failure occurred.

Structural failure after earlier positive decisions invalidates the entire bounded resolution.

No positive set is externally committed until whole-resolution structural validation succeeds.

---

## 32. Positive member identity

The positive result member SHALL be:

```text
ExposedElementMembership
{
    ExposableElementReference
    memberInstanceReference?
}
```

For:

```text
SINGLETON
```

`memberInstanceReference` is absent.

For:

```text
INSTANCE_QUALIFIED(kind)
```

the exact stable candidate instance reference is present.

---

## 33. Multiple instances of one semantic element

The following is legitimate:

```text
profile/public-contact-point
    + profile/contact-point/C1
        → EXPOSE

profile/public-contact-point
    + profile/contact-point/C2
        → EXPOSE

profile/public-contact-point
    + profile/contact-point/C3
        → WITHHOLD
```

Successful membership is:

```text
(
    profile/public-contact-point,
    profile/contact-point/C1
)

(
    profile/public-contact-point,
    profile/contact-point/C2
)
```

C1 and C2 are not duplicates.

---

## 34. API exposed-element set

`ApiExposedElementSet` SHALL expose immutable positive membership as:

```text
Set<ExposedElementMembership>
```

rather than treating:

```text
Set<ExposableElementReference>
```

as universally sufficient membership identity.

The member's exact `ExposureElementContractIdentity` remains private provenance.

The result remains:

```text
positive-membership-only
request-bound
invocation-bound
release-affined
immutable
non-authoritative for mutation
```

---

## 35. No transport leakage

An instance reference present in `ApiExposedElementSet` is an internal application/representation selection identity.

Its presence SHALL NOT automatically make the identifier itself part of an HTTP/JSON/customer-facing representation.

Transport exposure of any domain identifier requires its own accepted API/representation contract.

---

## 36. Membership is not acquisition authority

An exposed member means only:

> This already-established legitimate candidate resolved `EXPOSE` in this bounded invocation.

It does not mean:

> Representation may now query arbitrary owner state using the instance reference.

Therefore:

```text
Exposure membership
    ≠
repository access capability
```

---

## 37. E4 remains value-blind

E4 SHALL NOT carry:

```text
Map<ExposedElementMembership,Object>
generic candidate payload
generic value envelope
business DTO
projection value
```

The resolver processes identities, contracts, trusted context and owner decisions.

It returns membership/provenance only.

---

## 38. Bounded-read representation gate

S2/T1b4 representation SHALL ultimately prove:

> The value rendered for an exposed member belongs to the bounded read material from which that invocation's legitimate/serviceable candidate was established.

Representation SHALL NOT do:

```text
candidate/value R8 passes P2
        ↓
E4 exposes instance C1
        ↓
representation re-queries C1
        ↓
returns unrelated/newer R9 merely because C1 was exposed
```

The exact concrete read-material carrier is not defined by E4.

If existing accepted S2/T1b4 authority is insufficient to implement this invariant, implementation SHALL stop at a separate downstream design gate.

E4 SHALL NOT solve that gap by becoming a value container.

---

## 39. Projection Serviceability remains upstream

P2 remains responsible for:

```text
projection/source currentness
serviceability
source progress
refresh/rebuild
controlled degradation
```

E4 SHALL NOT reinterpret:

```text
NOT_SERVICEABLE
```

as:

```text
WITHHOLD
```

Only already-legitimate candidates reach E4.

---

## 40. Initial Merchant Presence membership specifications

The initial portfolio SHALL use:

| Element | Membership specification |
|---|---|
| `profile/public-display-name` | `SINGLETON` |
| `profile/public-tagline` | `SINGLETON` |
| `profile/public-short-summary` | `SINGLETON` |
| `profile/public-approved-description` | `SINGLETON` |
| `profile/public-contact-point` | `INSTANCE_QUALIFIED(profile/contact-point)` |
| `profile/public-merchant-location` | `INSTANCE_QUALIFIED(profile/merchant-location)` |
| `profile/public-service-area` | `INSTANCE_QUALIFIED(profile/service-area)` |
| `profile/public-external-presence-link` | `INSTANCE_QUALIFIED(profile/external-presence-link)` |
| `business-hours/public-business-hours` | `INSTANCE_QUALIFIED(business-hours/business-hours-scope)` |

Business Hours owns the stable semantic representation of its scope instance.

E4 SHALL NOT define how Business Hours encodes that owner-local stable instance identifier.

---

## 41. Profile merchant-choice bindings

The initial Profile merchant-choice runtime bindings SHALL include:

```text
profile/contact-point-public-exposure
profile/merchant-location-public-exposure
```

For Contact Point, the evaluator uses current active Contact Point authority and current revision-owned `MerchantContactPointExposure`.

For Merchant Location, the evaluator uses the current active Location plus the current Profile-owned Location Exposure Choice established by MS-PROT-051 v1.5.

Both remain Profile-owned.

---

## 42. Generic resolver dependency isolation

The generic E4 implementation SHALL NOT directly depend on:

```text
merchantprofile
booking
ordering
inventory
payment
fulfilment
scheduling
publication
```

or equivalent business-owner packages for business semantics.

Owner packages supply runtime evaluator implementations through generic E4 contracts.

The generic resolver SHALL contain no:

```text
if PROFILE
if BOOKING
switch capability
switch merchant type
```

semantic dispatch.

---

## 43. Change-amplification invariant

Adding a new Exposure-participating capability SHOULD require:

```text
owner semantic contract
+
owner evaluator implementation where required
+
runtime binding
+
owner tests
```

It SHALL NOT require modification of the generic E4 decision algorithm.

If a legitimate new owner cannot participate without changing the generic algorithm, architecture review SHALL reopen before adding a business-specific branch.

---

## 44. Evaluation-context growth gate

Adding new shared data to `OwnerExposureEvaluationContext` is architecture-sensitive.

The shared core SHALL not grow merely because multiple owners find one fact convenient.

A proposed shared field must establish that it is:

```text
trusted request evidence
not a business decision
genuinely cross-owner
not better owned through an existing owner contract
not creating a request-world-state object
```

Otherwise the evidence remains owner-local.

---

## 45. Exposure definition packaging

`ExposureMemberIdentitySpecification` is static Exposure contract meaning.

It SHALL therefore participate in the exact Exposure-definition bytes carried under ADR-016.

The outer bundle remains:

```text
mainstreet-semantic-bundle-v2
```

because ADR-016 already defines the Exposure section as an opaque constituent.

Changing membership specification changes the exact Exposure-definition payload and corresponding content digest.

---

## 46. No historical defaulting

A published Exposure definition that did not explicitly establish membership identity semantics SHALL NOT be reinterpreted as:

```text
SINGLETON
```

or:

```text
INSTANCE_QUALIFIED
```

merely because current code needs one.

There is no:

```text
missing field → SINGLETON
```

compatibility default.

The owner Exposure-definition codec SHALL explicitly encode the membership specification for releases governed by this amendment.

---

## 47. Semantic release evolution

Already-published semantic definition evidence remains immutable.

Where an already-published release lacks the membership semantics required by this amendment:

```text
do not mutate historical release bytes
do not use current portfolio as historical evidence
do not infer cardinality
```

A release intended for production E4 serving SHALL explicitly establish the amended Exposure contract meaning in its exact published evidence.

Where that requires semantic evolution, Main Street SHALL publish/use a new Semantic Registry Release under existing semantic-release governance.

---

## 48. Legacy release serving

A semantic release whose exact Exposure definitions cannot establish the membership semantics required by this resolver is not eligible for successful production E4 serving merely because its other semantic constituents materialise.

Deployment/serving admission SHALL fail closed or require governed release evolution.

No current/default portfolio may repair historical ambiguity.

---

## 49. Required architecture and runtime tests

Implementation SHALL prove at least:

1. singleton positive membership remains one element;
2. two distinct Contact Point instances may both expose;
3. duplicate same Contact Point member is rejected;
4. wrong instance kind is rejected structurally;
5. instance-qualified candidate missing instance is rejected structurally;
6. singleton optional evaluator instance does not become member identity;
7. duplicate singleton candidates are rejected;
8. same local instance identifier under different owners remains distinct;
9. duplicate `(element,audience)` contract registration is rejected;
10. same element cannot vary membership specification by audience;
11. element with no current audience variant produces WITHHOLD;
12. completely unknown submitted element produces structural failure;
13. exact-release evaluator lookup has no latest fallback;
14. owner-filtered contributions do not cross owners;
15. cross-owner requirement owner may differ from candidate owner;
16. requirement SATISFIED / UNSATISFIED / UNRESOLVED behave exactly;
17. merchant-choice EXPOSE / WITHHOLD / UNRESOLVED behave exactly;
18. missing evaluator fails closed at candidate level;
19. malformed batch coverage is structural failure;
20. unexpected evaluator exception is structural failure;
21. evaluator APIs expose no mutation authority;
22. multiple candidates sharing one binding execute through one logical batch invocation;
23. mixed legitimate EXPOSE/WITHHOLD produces positive partial membership;
24. structural failure after earlier positive decisions produces no successful partial result;
25. API result contains no withheld members/reasons/internal evidence;
26. Profile Location absent choice withholds;
27. Profile Location PUBLIC choice may expose only after higher restrictions pass;
28. retired Location cannot be exposed by historical PUBLIC choice;
29. adding a synthetic new owner requires no generic E4 switch branch;
30. legacy Exposure definition lacking explicit membership semantics is not silently defaulted.

---

## 50. Cross-domain conformance examples

The generic model SHALL represent without algorithm changes:

```text
Profile descriptor
    singleton

Contact Points C1/C2/C3
    independently exposable instances

Locations L1/L2
    independently exposable instances

Service Areas SA1/SA2
    independently exposable instances

Business Hours
    merchant / location scope instances

Publication P1/P2
    owner-defined instances when later registered

Booking B1/B2
    owner-defined instances with relationship requirements

Order / Inventory resource instances
    owner-defined instances when later registered
```

No business-category taxonomy is introduced.

---

## 51. Non-goals

This amendment introduces no:

```text
universal identity framework
global identity registry
generic entity loader
global policy engine
rules DSL
Exposure database
Exposure cache
generic payload envelope
CQRS mandate
event-sourcing mandate
microservice
business-type switch
transport DTO
pagination semantics
ordering semantics
Provider Readiness semantics
Projection Serviceability semantics
```

---

## 52. Implementation sequence if approved

Implementation SHALL proceed in dependency order:

```text
MS-PROT-051 v1.5 Profile choice persistence
        ↓
Exposure contract/member identity RED
        ↓
registry audience-variant/index RED
        ↓
exact-release Exposure-definition encoding update
        ↓
instance-aware positive result boundary
        ↓
OwnerExposureEvaluationContext
        ↓
exact-release evaluator binding snapshots
        ↓
batch evaluator contracts
        ↓
Profile choice evaluators
        ↓
deterministic E4 resolver
        ↓
targeted tests
        ↓
PostgreSQL integration tests
        ↓
architecture/conformance tests
        ↓
full Maven verification
        ↓
E4 conformance review
```

S2/T1b4 remains separately gated by bounded-read representation affinity.

---

## 53. Completion consequence

If approved and conformingly implemented:

```text
IMP-06 E4
    → eligible for CONFORMING_COMPLETE

S2 / T1b4
    → still requires bounded-read representation-affinity proof

IMP-07
    → remains blocked until the remaining IMP-06 dependency graph closes
```

Approval alone does not establish implementation conformance.
