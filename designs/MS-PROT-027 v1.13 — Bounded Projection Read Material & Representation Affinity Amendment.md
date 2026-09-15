# MS-PROT-027 v1.13 — Bounded Projection Read Material & Representation Affinity Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.13  
**Status:** **ACCEPTED by explicit manual approval on 3 September 2026**  
**Approved:** 3 September 2026  
**Authority type:** Production Projection/Surface/API bounded-read and representation-affinity architecture amendment  
**Governed by:** MS-DESIGN-RULES-001; MS-IMPLEMENTATION-RULES-001; MS-IMP-001  
**Amends:** composite MS-PROT-027 through v1.12 only within bounded Projection read material, P2-to-read affinity, material-to-E4 candidate affinity and S2/T1b4 representation selection  
**Depends on:** composite MS-PROT-027 through v1.12; MS-PROT-035 v1.1; composite MS-PROT-049; applicable source-owning capability authorities  
**Closes:** `IMP-06-S2-T1B4-DG-001 — Bounded Projection Read Material & Representation Affinity Gap`  
**Leaves unchanged:** E4 value-blind decision semantics; Exposure candidate/member identity; T4 continuation semantics; command current-state revalidation; source capability ownership; Projection semantic-bundle format; provider semantics  
**Purpose:** Ensure that an audience-visible value comes from the exact bounded Projection read whose evidence passed P2 and whose corresponding candidate passed E4, without converting Exposure membership into data-acquisition authority, introducing a generic payload envelope, requiring cross-capability serialisable snapshots or imposing a second latest-state read before transport.

---

## 1. Governing decision

Production S2/T1b4 SHALL introduce an immutable **Bounded Projection Read** for audience-facing Projection material.

Canonical:

```text
trusted Observation Request
        ↓
owner-qualified bounded acquisition
        ↓
Bounded Projection Read
    ├── typed owner/query projection material
    ├── exact source evidence
    ├── exact material/source affinity
    └── exact request/release/projection affinity
        ↓
P2 Projection Serviceability
        ↓
serviceable renderable fragments
        ↓
E4 identity-only candidates
        ↓
current Exposure resolution
        ↓
select exposed fragments FROM THE SAME BOUNDED READ
        ↓
S2 / T1b4 safe representation
```

A successful Exposure membership SHALL NOT authorise a later repository or owner-state lookup for the value to be rendered.

---

## 2. Problem resolved

MS-PROT-027 v1.11 requires:

```text
candidate/value R8 passes P2
        ↓
E4 exposes stable instance C1
        ↓
representation must not re-query C1
        ↓
and accidentally return unrelated/newer R9
```

Current P2 retains exact source evidence and currentness provenance but not the representation values.

Current E4 intentionally retains membership/provenance but no business value.

Therefore the accepted architecture lacks a runtime object establishing:

```text
exact value material
        ↔
exact P2 evidence/result
        ↔
exact E4 candidate
        ↔
exact final representation
```

v1.13 supplies that missing boundary.

---

## 3. Bounded Projection Read

A **Bounded Projection Read** is:

> An immutable, derived, request-bound observation containing exact typed Projection material together with the exact source evidence and runtime affinity required to determine whether that material may participate in the current audience response.

A Bounded Projection Read is a PROJECTION/runtime observation.

It is not authoritative business state.

It owns no business mutation.

It does not replace any source capability repository, revision or aggregate.

---

## 4. Semantic ownership

Underlying business facts remain owned by their existing capability owners.

Examples:

```text
Contact Point value
    → Profile authority

Merchant Location
    → Profile authority

Public Business Hours
    → Business Hours authority

Appointment commitment
    → Appointment authority

Inventory quantity
    → Inventory authority
```

The applicable Projection/query boundary owns only the derived read representation contract.

Generic Surface/API infrastructure SHALL NOT become the owner of the underlying business facts merely because it transports their representations.

---

## 5. BoundedProjectionReadBinding

Every Bounded Projection Read SHALL have one fresh opaque runtime identity:

```text
BoundedProjectionReadBinding
```

The binding identifies exactly one established bounded read.

It SHALL be distinct from:

```text
business entity identity
domain revision identity
Projection Contract identity
Projection Read Use identity
ObservationRequestBinding
AudienceObservationInvocationBinding
ExposureCandidateEvaluationBinding
ExposureCandidateInstanceReference
ExposedElementMembership
continuation token
API resource identity
```

---

## 6. Binding lifetime

`BoundedProjectionReadBinding` is ephemeral.

It SHALL NOT become:

```text
authoritative persistence identity
semantic bundle identity
domain event identity
merchant configuration
public API identifier
URL identity
audit business-object identity
continuation identity
mutation authority
```

A future distributed representation protocol MAY define another mechanism only through a separate governed decision.

---

## 7. Exact bounded-read scope

A Bounded Projection Read SHALL retain exact runtime affinity to:

```text
ObservationRequestBinding
MerchantScope
Semantic Registry Release identifier
ProjectionContractIdentity
ProjectionReadUseIdentity
BoundedProjectionReadBinding
```

The Bounded Projection Read SHALL NOT silently fall forward to another Semantic Registry Release, Projection Contract or Projection Read Use.

---

## 8. Observation Request affinity

A Bounded Projection Read belongs to exactly one established Observation Request.

Material from:

```text
request R1
```

SHALL NOT be used as the bounded read for:

```text
request R2
```

merely because merchant, projection, candidate identity and values happen to be equal.

Request identity and value equality are separate.

---

## 9. No mandatory global observation instant

A Bounded Projection Read MAY combine source material from several semantic owners.

v1.13 does not require those owners to share:

```text
one database transaction
one revision number
one timestamp
one global checkpoint
one distributed lock
```

Each source retains exact owner-specific progress/evidence.

P2 determines whether the resulting source-evidence combination is serviceable under the exact Projection Contract and Projection Read Use.

---

## 10. Projection Material Fragment

Audience-renderable material inside the bounded read SHALL be represented as owner/query-specific immutable **Projection Material Fragments**.

Each renderable fragment SHALL retain generic integrity metadata sufficient to identify:

```text
the exact E4 candidate identity
the exact Projection source dependencies used by the fragment
the exact observed progress affinity for those dependencies
```

The actual represented business fields remain owner/query-specific typed material.

---

## 11. No generic business payload envelope

v1.13 SHALL NOT introduce:

```text
Map<String,Object>
Map<ExposedElementMembership,Object>
JsonNode business-value bags
reflection-driven business DTO access
generic "projection payload"
generic Exposure value envelope
```

as the production ownership model.

Generic Projection/Surface/E4 infrastructure may inspect only the integrity metadata it owns.

Owner/query-specific representation code retains typed access to its own fragment values.

---

## 12. Fragment candidate identity

Every renderable fragment SHALL identify exactly one E4 candidate observation.

That identity SHALL preserve the v1.11 distinction among:

```text
semantic element
stable member instance where required
domain revision
policy revision
```

A fragment's domain revision or source progress SHALL NOT become Exposure positive-member identity.

---

## 13. Duplicate fragment identity

Within one Bounded Projection Read, two independently renderable fragments SHALL NOT claim the same exact E4 candidate/member identity.

If one exposable semantic element logically contains several represented fields, the owning Projection/query contract SHALL represent them as one bounded fragment or otherwise define distinct exposable element identities.

Generic representation code SHALL NOT choose arbitrarily between duplicate fragments.

Duplicate competing renderable material is structural failure.

---

## 14. ProjectionMaterialSourceAffinity

For each source dependency required by a renderable fragment, the bounded read SHALL retain a source-affinity entry conceptually equivalent to:

```text
ProjectionMaterialSourceAffinity
{
    ProjectionSourceDependencyReference
    observedProgressIdentifier
}
```

The exact progress-token format remains owner-defined.

Generic infrastructure SHALL compare exact identity/equality only.

It SHALL NOT interpret ordering, age or business meaning of an owner progress token.

---

## 15. Material/evidence acquisition affinity

A source owner or trusted owner/query adapter SHALL establish material and its `ProjectionMaterialSourceAffinity` from one coherent owner observation.

For every applicable fragment/source pair:

```text
fragment observed progress
    ==
ProjectionSourceEvidence.observedProgressIdentifier
```

used by P2 for that Bounded Projection Read.

Material/evidence affinity SHALL NOT be inferred merely from:

```text
equal values
same stable entity identity
similar timestamps
Java object identity
repository call proximity
```

---

## 16. Incoherent owner acquisition

If the responsible owner/query boundary cannot establish that material and evidence concern the same owner progress:

```text
the fragment SHALL NOT be treated as current serviceable material
```

The owner/query boundary shall instead produce evidence that P2 can classify under existing stale, missing, unavailable, corrupt or unverifiable semantics.

A generic representation layer SHALL NOT repair incoherent evidence.

---

## 17. Exact source-evidence set

The Bounded Projection Read SHALL retain the exact `ProjectionSourceEvidence` set supplied to P2.

P2 evaluation for the bounded representation path SHALL consume that exact immutable evidence set.

A caller SHALL NOT construct:

```text
Bounded Read B1
+
different/reconstructed evidence E2
```

and obtain a serviceability result governing B1.

---

## 18. P2 bounded-read affinity

`ProjectionServiceabilityEvaluationRequest` for the bounded representation path SHALL carry or otherwise be privately bound to the exact `BoundedProjectionReadBinding`.

`ProjectionServiceabilityResult` SHALL retain the same binding.

Therefore:

```text
P2(B1) result
```

SHALL NOT govern:

```text
B2 material
```

even where B1 and B2 use the same Projection Contract, read-use identity and business values.

---

## 19. P2 remains value-blind

P2 SHALL NOT interpret owner-specific material values merely because the material and evidence now share a bounded-read carrier.

P2 continues to evaluate:

```text
exact Projection Contract
exact Projection Read Use
exact source evidence
exact policies/evaluators
```

Its existing outcomes remain:

```text
FULLY_SERVICEABLE
REDUCED_SERVICEABLE
NOT_SERVICEABLE
```

v1.13 changes affinity, not serviceability meaning.

---

## 20. NOT_SERVICEABLE

Where P2 returns:

```text
NOT_SERVICEABLE
```

the bounded representation path SHALL NOT submit renderable candidates from that Projection read to E4 for audience representation.

E4 SHALL NOT rescue a non-serviceable Projection.

---

## 21. Reduced serviceability

Every renderable fragment SHALL identify all Projection source dependencies required for that fragment.

Where P2 returns:

```text
REDUCED_SERVICEABLE
```

a fragment is eligible only when none of the sources required by that fragment belongs to P2's omitted/unserviceable source set.

Therefore:

```text
one failed source
```

does not automatically remove unrelated independently serviceable fragments.

Conversely:

```text
one serviceable source
```

does not make a fragment serviceable when that fragment also depends upon an omitted source.

---

## 22. NOT_APPLICABLE evidence

A renderable fragment SHALL NOT depend upon a source that P2 has legitimately classified as `NOT_APPLICABLE` for that bounded read.

A conditional source that is not applicable may remain in P2's exact evidence set as required by v1.8, but no rendered fragment may claim to have been derived from that non-participating source.

---

## 23. Candidate establishment

Only P2-eligible fragments become candidate observations for E4.

Canonical:

```text
Bounded Projection Read
        ↓
P2 result
        ↓
eligible fragment set
        ↓
identity-only ExposureCandidateObservation set
        ↓
E4
```

The existence of a material fragment alone SHALL NOT establish Surface membership or Exposure permission.

---

## 24. E4 remains value-blind

v1.13 SHALL NOT add business values to:

```text
ExposureCandidateObservation
ExposureCandidateEvaluationSubmission
ExposureRequirementCandidateEvaluation
MerchantExposureChoiceCandidateEvaluation
ExposedElementMembership
ApiExposedElementSet
```

E4 continues to process identity, trusted context, contracts and owner decisions.

---

## 25. Material Affinity Observation Contribution

Where an E4 requirement or merchant-choice decision is semantically affined to the exact revision/progress of the projected value, the bounded-read acquisition path SHALL establish owner-qualified **Material Affinity Observation Contribution** evidence.

This evidence SHALL use the existing exact-release typed `EstablishedObservationContribution` mechanism.

It SHALL carry only the owner-specific integrity information needed to identify:

```text
candidate/member concerned
expected bounded-material source progress/revision
```

It SHALL NOT carry:

```text
the rendered business value
a generic business payload
a precomputed EXPOSE/WITHHOLD verdict
authentication credential
provider secret
mutation authority
```

---

## 26. Material Affinity Observation Contribution ownership

Each contribution remains capability-owned.

Its `ObservationContributionKind` SHALL be owner-qualified and exact-release registered under existing E3 contribution-definition machinery.

The generic E3 context establishment boundary validates:

```text
exact definition release
permitted audience
runtime implementation binding
Observation Request binding
Merchant Scope
contribution kind/cardinality
```

without interpreting the owner-specific progress semantics.

---

## 27. Contribution cardinality

The existing `SINGLE` contribution cardinality remains sufficient.

One owner-qualified Material Affinity Observation Contribution MAY contain a bounded immutable set of candidate-to-expected-progress entries for that owner's material in the request.

v1.13 does not require one contribution kind per candidate.

---

## 28. E4 owner evaluator progress check

Where the governing E4 owner semantics are value/revision-affined, the corresponding owner evaluator SHALL:

```text
receive current candidate submission
        +
receive owner-filtered Material Affinity Observation Contribution
        ↓
read current owner authority coherently
        ↓
compare current owner revision/progress
with expected bounded-material progress
        ↓
equal
    → evaluate current governing choice/requirement

not equal / unavailable
    → UNRESOLVED
    → candidate WITHHOLD
```

The evaluator SHALL NOT use a current choice from a newer source revision to authorise rendering material from an older bounded source progress.

---

## 29. Contact Point example

For a bounded Contact Point fragment:

```text
C1
material revision/progress = R8
```

the Profile material-affinity contribution records:

```text
C1 expected progress = R8
```

The Profile merchant-choice evaluator reads the coherent current Contact Point state.

If current state is:

```text
C1 / R8 / PUBLIC
```

the evaluator may return:

```text
EXPOSE
```

If current state is:

```text
C1 / R9 / PUBLIC
```

the evaluator SHALL NOT use R9's public choice to authorise R8 material.

Result:

```text
UNRESOLVED
→ WITHHOLD
```

If current R8 choice is private:

```text
WITHHOLD
```

---

## 30. Merchant Location example

For Merchant Location, the Profile evaluator SHALL preserve MS-PROT-051 v1.5's separation between:

```text
current active Location revision
and
current Location Exposure Choice revision
```

The current active Location's source progress/revision must match the bounded material's expected progress.

Only then may the evaluator apply the current Profile-owned Location Exposure Choice.

A later Location revision SHALL NOT silently authorise material from an earlier bounded revision.

---

## 31. Non-material-affined requirements

Not every E4 requirement is affined to Projection material progress.

Examples such as:

```text
current authenticated principal
current customer relationship
current platform protection
current purpose/security restriction
```

remain governed by their existing current context/evaluator authority.

They SHALL NOT be frozen into Projection material merely because the same request also contains a Bounded Projection Read.

---

## 32. Current restriction precedence

A valid material-affinity match does not bypass higher restrictions.

Existing E4 precedence remains:

```text
candidate legitimacy
        ↓
current governing requirements
        ↓
current merchant choice where declared
        ↓
baseline
```

A current privacy, security, relationship or platform restriction may still WITHHOLD material whose P2/read affinity is valid.

---

## 33. Same-read positive selection

After E4 produces positive membership, S2/T1b4 SHALL select material only from the same Bounded Projection Read whose eligible fragments created the candidate set.

Canonical:

```text
B1 contains C1/R8
        ↓
P2(B1) allows C1
        ↓
E4 evaluates C1
        ↓
C1 EXPOSE
        ↓
representation selects C1/R8 FROM B1
```

---

## 34. No post-E4 value acquisition

The following is prohibited:

```text
E4 exposes C1
        ↓
repository.current(C1)
        ↓
serialize returned value
```

The following is also prohibited:

```text
E4 exposes C1 from B1
        ↓
look up C1 in B2
        ↓
serialize B2 value
```

Exposure membership is not repository access capability.

---

## 35. ApiExposureResolution affinity

Before final representation, the trusted Surface/API composition boundary SHALL establish that the `ApiExposureResolution` belongs to the same:

```text
Observation Request
Merchant Scope
Semantic Registry Release
```

as the Bounded Projection Read.

The Exposure result remains separately bound to its exact Audience Observation invocation under existing v1.10-v1.12 authority.

v1.13 SHALL NOT weaken that invocation affinity.

---

## 36. Exposed membership must exist in the eligible read set

For every positive `ExposedElementMembership`, the bounded representation path SHALL identify exactly one corresponding P2-eligible fragment in the same Bounded Projection Read.

Zero corresponding fragments:

```text
structural failure
```

Multiple competing corresponding fragments:

```text
structural failure
```

The representation path SHALL NOT interpret either case as legitimate `WITHHOLD`.

---

## 37. Structural failure is not partial success

A structural same-read/affinity failure invalidates the bounded representation attempt.

The system SHALL NOT externally commit a partially assembled positive response when structural validation later fails.

This preserves v1.11 whole-resolution structural-failure principles at the downstream representation boundary.

---

## 38. Request-bounded observation semantics

After a Bounded Projection Read has been established, its material is immutable.

If authoritative source state changes later:

```text
R8 bounded material
        ↓
authoritative source later commits R9
```

the bounded material remains R8.

R9 belongs to a later bounded read.

v1.13 does not claim R8 is still the globally latest value.

It claims only that R8 belongs to the exact bounded observation being resolved.

---

## 39. Mutation before E4

Where source state changes after bounded acquisition but before E4:

```text
material R8
        ↓
source becomes R9
        ↓
E4 owner evaluation
```

a value/revision-affined owner evaluator detects:

```text
expected R8
!=
current R9
```

and returns:

```text
UNRESOLVED
→ WITHHOLD
```

This prevents pairing old material with a policy decision belonging to newer source state.

---

## 40. Exposure choice change without value change

Where an owner permits Exposure choice to change independently of the projected value revision, the E4 evaluator continues to use the current owner choice.

Therefore:

```text
bounded material remains same
+
merchant changes current choice to WITHHOLD before E4
        ↓
E4 WITHHOLD
```

The material-affinity contribution does not freeze merchant policy.

---

## 41. Mutation after E4

Suppose:

```text
T1 bounded read establishes R8
T2 P2 accepts R8
T3 E4 validly exposes R8 candidate
T4 authoritative source changes to R9
T5 response serialises
```

v1.13 MAY complete the response using R8 from the established bounded read.

It SHALL NOT substitute R9.

The later mutation governs later bounded observations.

---

## 42. No final-currentness fence

v1.13 SHALL NOT introduce:

```text
FINAL_CURRENTNESS_FENCE
CURRENT_THROUGH_SERIALISATION
CURRENT_THROUGH_FINAL_BYTE
```

as ordinary Projection semantics.

After a candidate has validly passed the material-affinity check and E4 evaluation, no second latest-value acquisition is required merely because transport has not completed.

A future concrete requirement for linearizable revocation through transport completion requires a separate governed design decision.

---

## 43. New-request revocation

A subsequent Observation Request SHALL establish a new Bounded Projection Read and current E4 evidence.

Previous:

```text
BoundedProjectionReadBinding
ProjectionServiceabilityResult
ApiExposureResolution
Material Affinity Observation Contribution
```

SHALL NOT be reused as authority for the new request.

This preserves current revocation behaviour for later requests.

---

## 44. Retry

If a bounded representation attempt fails structurally or must be restarted after an applicable technical failure, the retry SHALL establish a new bounded observation.

Canonical:

```text
new Observation Request / governed retry context
        ↓
new Bounded Projection Read
        ↓
new P2
        ↓
new current E4
        ↓
new representation
```

Old and new material/evidence/result objects SHALL NOT be mixed.

---

## 45. Technical transport retry

Where accepted transport semantics permit retry of the same logical read operation, implementation MAY repeat acquisition.

The repeated attempt remains a new bounded runtime read unless an independently accepted immutable materialised Projection contract provides the exact reusable projection snapshot.

No retry may convert stale execution identity into current authority.

---

## 46. Materialised Projection Contracts

For `MATERIALISED` Projection Contracts, the durable materialised Projection retains its own existing identity/currentness/rebuild semantics.

One audience request SHALL wrap the exact materialised state being consumed in a fresh Bounded Projection Read.

`BoundedProjectionReadBinding` SHALL NOT replace the durable projection identity.

---

## 47. REQUEST_SCOPED Projection Contracts

For `REQUEST_SCOPED` Projection Contracts, a Bounded Projection Read MAY exist only in memory for the current request.

v1.13 does not require:

```text
database persistence
Redis
cache
event stream
checkpoint table
```

merely to preserve same-read affinity.

---

## 48. Collections

A Bounded Projection Read MAY contain one bounded collection page.

All normal T4 rules remain applicable:

```text
bounded page size
stable ordering
registered filters
scope/query affinity
continuation semantics
```

v1.13 does not redefine those rules.

---

## 49. Pagination

Each later page/request MAY establish a new Bounded Projection Read.

Therefore:

```text
page 1 → B1
page 2 → B2
```

is valid.

A continuation token does not imply a frozen multi-page business snapshot unless a future accepted query contract explicitly requires that stronger semantic.

---

## 50. Continuation identity remains separate

`BoundedProjectionReadBinding` SHALL NOT be encoded into a continuation token as semantic authority.

T4b remains responsible for continuation affinity.

The following remain distinct:

```text
bounded-read identity
query identity
continuation identity
merchant scope
resource identity
authentication
```

---

## 51. Commands remain current

A rendered bounded observation does not authorise mutation.

Where the audience observes:

```text
stock
availability
price
appointment
public information
```

and subsequently issues a command, the command owner SHALL perform the accepted current-state/concurrency/authorisation validation.

The bounded read SHALL NOT create:

```text
inventory reservation
appointment hold
price guarantee
resource authority
mutation permission
```

unless an accepted owning capability explicitly establishes such a commitment.

---

## 52. Security and data protection

Possession of material inside a Bounded Projection Read does not imply permission to expose it.

Only material corresponding to positive E4 membership may enter the audience representation.

Platform/data-protection/security requirements remain able to WITHHOLD an otherwise serviceable candidate.

Generic diagnostics SHALL NOT expose withheld fragment values.

---

## 53. Generic Surface isolation

Generic S2 infrastructure MAY:

```text
validate exact runtime affinity
validate P2 result/read binding
determine P2-eligible fragment identities
submit identity-only candidates to E4
match positive membership to eligible fragments
group/order contributions according to existing Surface authority
```

Generic S2 infrastructure SHALL NOT:

```text
interpret Profile fields
interpret Booking fields
interpret Inventory fields
interpret business payloads
re-query owner repositories
invent domain labels/status
override P2
override E4
```

---

## 54. API query mapping

T1b4 query handling SHALL consume the exact bounded-read representation path.

An `ApiQueryContract` continues to identify its owning query/Projection Contract, Projection Serviceability requirement, Exposure contract(s), filter/sort contract, pagination bound and safe representation.

v1.13 does not permit API code to bypass those contracts by directly serialising arbitrary owner entities.

---

## 55. Owner-specific mapper

The owning query/representation mapper MAY inspect its own typed Projection Material Fragments after the generic affinity/exposure path selects them.

It SHALL NOT receive unselected/withheld material as permission to serialize it.

The mapper SHALL NOT reacquire owner state merely to fill missing fields.

---

## 56. Missing representation material

If a safe response contract requires an exposed fragment but the exact selected bounded material is unavailable due to structural inconsistency, the attempt SHALL fail according to the applicable internal/API problem mapping.

The mapper SHALL NOT:

```text
fetch latest
invent a default
substitute a stale unrelated object
reconstruct a value from an Exposure identity
```

---

## 57. Owner adapter responsibility

Each source/query adapter remains responsible for truthfully establishing:

```text
the typed material it read
the exact progress/revision from which it read it
the corresponding P2 source evidence
any required Material Affinity Observation Contribution
```

Generic infrastructure cannot prove that a dishonest owner adapter represented its business data correctly.

Owner conformance tests SHALL establish that contract.

---

## 58. Batch acquisition

Where one Projection source contains multiple members, the owner/query implementation SHOULD support bounded set/batch acquisition rather than mandatory one-query-per-member execution.

v1.13 does not prescribe:

```text
SQL shape
jOOQ shape
repository shape
cache technology
```

The requirement is coherent bounded material/evidence, not one physical query per candidate.

---

## 59. Conservative coarse progress

An owner progress token MAY cover a broader source scope than one candidate.

If another change advances that token while one candidate's business value remains equal, a material-affinity comparison may conservatively return `UNRESOLVED`.

That false-negative withholding is conforming.

The system SHALL NOT weaken affinity merely to avoid such conservative withholding.

A future owner may introduce a finer accepted progress scope without changing generic bounded-read semantics.

---

## 60. No new Projection semantic-bundle format

v1.13 does not require adding a Java/business-material type identifier to `ProjectionReadUseContract` or `ProjectionContractDefinition`.

Owner-specific material types remain implementation/internal query contracts.

Existing semantic-bundle format need not change merely to encode Java material classes.

---

## 61. Observation contribution registration

Where a Material Affinity Observation Contribution is required, its owner-qualified contribution kind SHALL be registered using the existing exact-release observation-contribution definition mechanism.

This is new definition content in an applicable Semantic Registry Release.

It does not introduce a new definition format.

---

## 62. E4 submission-affinity remains unchanged

`ExposureCandidateEvaluationBinding` from v1.12 continues to prove:

```text
this result row belongs to this exact E4 evaluator submission
```

It SHALL NOT become:

```text
BoundedProjectionReadBinding
Projection material identity
source-progress identity
repository capability
```

v1.13 and v1.12 solve different affinity problems.

---

## 63. Failure classifications

Expected P2 inability remains:

```text
NOT_SERVICEABLE
```

Expected E4 inability to establish current material-affined owner evidence remains:

```text
UNRESOLVED
→ candidate WITHHOLD
```

Expected merchant/security decision remains:

```text
WITHHOLD
```

Structural failures include at least:

```text
wrong BoundedProjectionReadBinding
wrong Observation Request
wrong Merchant Scope
wrong Semantic Registry Release
wrong Projection Contract
wrong Projection Read Use
P2 result from another bounded read
material/evidence progress mismatch
undeclared material source
duplicate competing fragment identity
positive E4 membership absent from eligible bounded material
cross-read material substitution
cross-request bounded material substitution
malformed material-affinity contribution
unsafe post-E4 owner re-query
```

These classes SHALL NOT be collapsed.

---

## 64. No partial structural success

Where structural bounded-read/representation validation fails:

```text
successful partial representation
```

SHALL NOT be externally committed.

Candidate-local E4 withholding remains independently legitimate.

---

## 65. Initial Merchant Presence proof

The first conforming production proof SHOULD use:

```text
platform / merchant-presence
platform / public-merchant-presence
```

and exercise at least:

```text
public display name
tagline
short summary
approved description
multiple Contact Points
multiple Merchant Locations
Service Areas
External Presence Links
Public Business Hours
```

The proof SHALL demonstrate both singleton and instance-qualified Exposure membership.

---

## 66. Merchant Presence reduced-read proof

The initial implementation SHALL prove at least one case where:

```text
Profile descriptor evidence current
Contact Point evidence current
Merchant Location evidence current
Business Hours source unavailable
```

P2 produces the permitted reduced outcome.

Eligible Profile fragments survive.

Business Hours material does not.

E4 then independently determines which surviving candidates are actually exposed.

---

## 67. Value/policy race proof

The initial implementation SHALL prove:

```text
bounded Contact Point C1 / R8
        ↓
source advances to R9 before E4
        ↓
Profile evaluator sees expected R8 != current R9
        ↓
UNRESOLVED
        ↓
C1 WITHHOLD
```

It SHALL also prove:

```text
bounded Location L1 revision unchanged
        ↓
current Location Exposure Choice changes to PRIVATE
        ↓
E4 WITHHOLD
```

No latest value shall be substituted.

---

## 68. Post-E4 mutation proof

The implementation SHALL prove:

```text
B1/R8 passes P2
E4 validly exposes candidate
source later advances to R9
```

and representation still selects:

```text
B1/R8
```

rather than re-querying R9.

This test proves request-bounded observation semantics.

---

## 69. Cross-request proof

The implementation SHALL prove:

```text
B1 belongs to Observation Request Q1
B2 belongs to Observation Request Q2
```

and that:

```text
P2(B1) + E4(Q2)
```

or:

```text
P2(B2) + material(B1)
```

cannot form a valid response.

---

## 70. Required architecture tests

Architecture/conformance tests SHALL establish at least:

```text
E4 production package carries no generic business payload
Exposure membership does not expose repository lookup capability
BoundedProjectionReadBinding does not enter domain identity
BoundedProjectionReadBinding does not enter continuation identity
generic Surface has no Profile/Booking/Inventory business switch
ProjectionReadUseContract does not acquire Java material-type semantics
Material Affinity Observation Contributions are exact-release and owner-qualified
owner evaluator mismatch fails closed
no post-E4 value reacquisition path exists in the bounded query spine
```

---

## 71. No broad endpoint requirement

Closing v1.13 does not require a broad production endpoint catalogue.

IMP-06 may prove the bounded query/representation architecture with the smallest concrete API query sufficient to demonstrate T1b4.

Additional concrete domain API adapters remain later/on-demand vertical-slice work.

---

## 72. Explicit non-goals

v1.13 does not introduce or require:

```text
CQRS
event sourcing
microservices
distributed transactions
global snapshot isolation
Redis
a generic Projection service
a generic payload container
a Visibility database
a second policy engine
final-byte currentness
frozen pagination snapshots
new business status
new provider authority
new command semantics
```

---

## 73. Existing P2 semantics remain authoritative

v1.13 does not change:

```text
FULLY_SERVICEABLE
REDUCED_SERVICEABLE
NOT_SERVICEABLE
Projection policy ownership
source currentness predicates
source omission semantics
read-use policy meaning
```

Composite MS-PROT-027 through v1.8 remains authoritative for those matters.

---

## 74. Existing E4 semantics remain authoritative

v1.13 does not change:

```text
Exposure contract selection
candidate/member identity
requirement precedence
merchant-choice precedence
EXPOSE/WITHHOLD meaning
UNRESOLVED fail-closed behaviour
submission-affinity semantics
whole-resolution structural failure
```

Composite MS-PROT-027 v1.9-v1.12 remains authoritative except for the material-affinity evidence consumption explicitly added here.

---

## 75. Existing Surface semantics remain authoritative

v1.13 does not let Projection material create:

```text
Surface Contribution membership
Public Interaction participation
Customer relationship
actor authority
interaction availability
```

Composite MS-PROT-049 remains authoritative for those responsibilities.

---

## 76. Existing API semantics remain authoritative

v1.13 does not redefine:

```text
API contract identity
transport scope
problem/outcome mapping
bounded collection semantics
continuation semantics
HTTP mapping
callback semantics
media transfer
```

Composite MS-PROT-035 remains authoritative.

---

## 77. Downstream continuation boundary

T4b/T4c remain responsible for bounded-query continuation affinity and concrete opaque continuation representation.

A successful v1.13 implementation makes T1b4 eligible to provide the query-definition prerequisite required by T4b.

v1.13 itself does not implement continuation encoding.

---

## 78. Implementation ordering

After acceptance and formalisation, the minimum implementation order SHALL be:

```text
BR1
Bounded Projection Read / binding / fragment integrity primitives
        ↓
BR2
P2 exact bounded-read binding
        ↓
BR3
owner/query material acquisition + source-affinity proof
        ↓
BR4
Material Affinity Observation Contribution registration/runtime binding
        ↓
BR5
owner evaluator current-progress comparison
        ↓
BR6
same-read E4-positive fragment selection
        ↓
S2
final PUBLIC/CUSTOMER Surface assembly
        ↓
T1b4
first production bounded query definition/path
        ↓
v1.13 adversarial falsification
        ↓
full Maven/PostgreSQL/architecture/corpus gate
```

No later node SHALL be treated as GREEN merely because a prerequisite type exists.

---

## 79. Acceptance statement

v1.13 is conforming only if the production representation can mechanically demonstrate:

> The value selected for every exposed member is the value from the exact bounded Projection read whose exact source evidence passed P2, whose material-affined owner state remained coherent when E4 evaluated applicable value/revision-affined policy, and whose candidate received positive E4 membership in the same Observation Request.

The production architecture SHALL NOT satisfy that invariant by moving values into E4 or by re-querying owner state after E4.

---

## 80. Final invariant

Canonical:

```text
authoritative owner observation
        ↓
typed bounded material
        +
exact source progress/evidence
        ↓
P2 bound to exact bounded read
        ↓
P2-eligible fragments
        ↓
identity-only E4 candidates
        +
current owner policy
        +
expected material-progress affinity where required
        ↓
EXPOSED MEMBERSHIP
        ↓
select exact fragment FROM SAME BOUNDED READ
        ↓
safe Surface/API representation
```

Hard negative boundary:

```text
EXPOSED MEMBERSHIP
        ≠
permission to fetch "whatever is current now"
```
