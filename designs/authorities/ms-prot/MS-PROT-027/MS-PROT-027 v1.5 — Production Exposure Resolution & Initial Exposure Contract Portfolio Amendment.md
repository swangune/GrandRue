# MS-PROT-027 v1.5 — Production Exposure Resolution & Initial Exposure Contract Portfolio Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.5  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Amends:** MS-PROT-027 v1.2–v1.4 within production Exposure resolution and initial concrete Exposure-contract scope  
**Depends on:** MS-PROT-027 v1.2–v1.4; MS-PROT-028 v1.3; MS-PROT-031; MS-PROT-049 v1.0–v1.3; MS-PROT-050; MS-PROT-051 v1.0–v1.1; MS-PROT-052 v1.1; MS-PROT-053; MS-PROT-054; MS-PROT-062; MS-PROT-063; MS-PROT-069; MS-PROT-070; MS-PROT-079  
**Resolves:** `MS-PROT-051-V11-DQ-006`  
**Closes:** MS-PROT-079 Target 8 — Exposure resolution  
**Approved:** Manual approval on 27 August 2026 after in-chat proposal, authority trace, implementation-evidence review, falsification, ambiguity review, corpus-conformance review and recommendation  
**Purpose:** Make the existing Exposure semantics production-executable through explicit registered element contracts, deterministic current-context resolution and an initial Merchant Presence Exposure portfolio without creating universal visibility state, transferring business-fact ownership, or collapsing Surface eligibility, Projection Serviceability, privacy/security, authorisation or merchant preference into Exposure.

---

# 1. Governing decision

MS-PROT-027 v1.2 remains authoritative that Exposure answers:

> **May this particular audience observe this particular already-legitimate semantic element in the current authoritative context?**

The verdict remains exactly:

```text
EXPOSE
WITHHOLD
```

Target 8 SHALL operationalise this through explicit registered **Exposure Element Contracts**.

Canonical production flow:

```text
AUTHORITATIVE BUSINESS FACTS
        ↓
legitimate Surface / read participation
        ↓
Projection Serviceability
        ↓
EXPOSABLE CANDIDATE ELEMENTS
        ↓
exact registered Exposure Element Contract
        +
current Audience Observation Context
        +
current owner-qualified policy evidence
        +
current governing restriction evidence
        ↓
DETERMINISTIC EXPOSURE RESOLUTION
        ↓
EXPOSE | WITHHOLD
        ↓
audience-visible response
```

Exposure SHALL NOT create the candidate element whose observation it evaluates.

---

# 2. Target 8 does not redefine Exposure

The following accepted separations survive unchanged:

```text
Exposure
    ≠ Semantic Applicability
    ≠ Commercial Entitlement
    ≠ Actor Authorisation
    ≠ Surface Eligibility
    ≠ Customer Surface Eligibility
    ≠ Projection Serviceability
    ≠ Provider Readiness
    ≠ Operational Eligibility
    ≠ business lifecycle
    ≠ discoverability/indexing
    ≠ presentation formatting
```

MS-PROT-027 v1.2 already establishes these boundaries.

Target 8 adds the runtime contract required to preserve them.

---

# 3. Exposure applies only to legitimate candidates

Exposure SHALL NOT manufacture Surface membership.

For PUBLIC:

```text
legitimate public-presence / public-Surface candidate
        ↓
Projection Serviceability
        ↓
Exposure
```

For CUSTOMER:

```text
registered CUSTOMER contribution
        ↓
Customer Surface Eligibility
        ↓
Projection Serviceability
        ↓
candidate elements
        ↓
Exposure
```

For MERCHANT:

```text
registered MERCHANT contribution / legitimate owner read
        ↓
applicable actor/context eligibility
        ↓
Projection Serviceability where required
        ↓
candidate elements
        ↓
Exposure
```

This preserves MS-PROT-049's distinction between Surface contribution, contextual eligibility and business authority.

---

# 4. Projection failure is not a WITHHOLD verdict

If required projection evidence cannot establish that an element is serviceable:

```text
PROJECTION NOT SERVICEABLE
```

The system SHALL NOT pretend that Exposure evaluated the element and returned:

```text
WITHHOLD
```

The distinction matters operationally.

```text
element legitimate but projection unavailable
        ≠
element available but audience forbidden to observe it
```

MS-PROT-027 v1.3 already requires these failure classes to remain distinguishable.

---

# 5. Exposure Element Contract

An **Exposure Element Contract** is:

> **A registered, owner-qualified, release-affined definition establishing how one class of exposable semantic/projection element may be evaluated for one audience without owning the underlying business fact.**

Conceptually:

```text
ExposureElementContract
{
    identity

    exposableElementReference
    audience

    baselineDecision

    merchantChoiceSource?

    requirementReferences
}
```

This is a logical contract.

It does not mandate one Java class.

---

# 6. Exposure Element Contract identity

Every contract SHALL have stable owner-qualified identity.

Conceptually:

```text
ExposureElementContractIdentity
{
    ownerIdentifier
    contractIdentifier
}
```

Examples:

```text
profile / public-contact-point
profile / public-merchant-location
business-hours / public-business-hours
```

A display label, database column, frontend component or JSON property SHALL NOT become Exposure contract identity.

---

# 7. Exposable element identity

The contract SHALL identify the exact semantic/projection element it governs.

An exposable element may be:

```text
one authoritative field
one bounded semantic group
one derived audience-safe projection element
```

provided the boundary is explicit.

Rejected:

```text
BusinessProfile
    exposure = PUBLIC
```

Accepted:

```text
public contact point
public merchant location representation
public approved description
```

Exposure SHALL remain sufficiently granular that one protected field does not become visible merely because another element from the same source object is exposable.

---

# 8. Contracts are release-affined

Exposure Element Contracts are registered semantic/read definitions.

Where a request is resolved against Semantic Registry Release `R`:

```text
Surface definition = R
Projection dependency contract = R
Exposure Element Contract = R
```

where those definitions are release-affined.

Main Street SHALL NOT resolve:

```text
Surface from R17
+
Exposure contract from "latest" R19
```

merely because R19 is newer.

Existing semantic-release assembly/materialisation architecture remains authoritative.

This amendment does not require another registry service.

---

# 9. No universal Exposure database or rules engine

Acceptance of Exposure Element Contracts SHALL NOT imply:

```text
ExposureRuleServer
ExposureDatabase
VisibilityEngine DSL
Policy microservice
merchant-authored expression language
```

Initial production may register the definitions through the same ordinary exact-release semantic materialisation mechanism used for other registered semantics.

The requirement is deterministic identity and behaviour, not infrastructure ceremony.

---

# 10. Baseline decision

Every Exposure Element Contract SHALL define one registered baseline:

```text
EXPOSE
or
WITHHOLD
```

The baseline applies only after:

```text
the element is a legitimate candidate
+
required governing restrictions are satisfied
```

A baseline of `EXPOSE` SHALL NOT bypass privacy/security restrictions.

A baseline of `WITHHOLD` MAY be broadened only where the contract explicitly allows current merchant-owned Exposure choice.

---

# 11. Merchant Exposure choice

Where merchant discretion is legitimate, the contract MAY identify an authoritative merchant-choice source.

Conceptually:

```text
merchantChoiceSource
    → owning capability / fact authority
```

Exposure resolution SHALL consume the current choice.

Exposure itself SHALL NOT own the choice.

Therefore:

```text
Exposure resolver
    reads merchant choice

Exposure resolver
    does not persist merchant choice
```

---

# 12. No generic ExposurePolicy aggregate

Main Street SHALL NOT introduce:

```text
MerchantExposurePolicy
{
    every field in the platform
}
```

as a giant authoritative aggregate.

Merchant Exposure choice remains with the authority that owns the applicable fact/configuration.

Examples:

```text
MerchantContactPoint public choice
    → Profile authority

MerchantLocation public choice
    → Profile authority

Offering price exposure
    → applicable Offering/configuration authority

customer Appointment history choice
    → applicable capability/configuration authority
```

Exposure resolves those choices.

It does not own them.

---

# 13. Profile Exposure choice is revision-affined

MS-PROT-051 v1.1 already requires material Profile mutations to create exact logical fact revisions.

Therefore, where public Exposure choice belongs to a Profile fact, the value and its current public-observation choice SHALL be owner-consistent.

For example:

```text
Contact Point C1
Revision 7
    value = 01792...
    public exposure = EXPOSE
```

Changing:

```text
EXPOSE → WITHHOLD
```

is a material mutation of that fact's current public-presence meaning.

It SHALL participate in the same same-fact concurrency discipline as other material Profile changes.

---

# 14. No value/policy race

Rejected:

```text
telephone revision 7
+
exposure decision accidentally read from revision 5
```

if that combination did not represent an authoritative current state.

For an independently revisioned Profile fact, the request SHALL establish sufficiently coherent current owner evidence for:

```text
current value/revision
+
current Exposure choice applicable to that value
```

This does not create a global Business Profile transaction.

Unrelated Profile facts remain independently current.

---

# 15. Audience Observation Context

Exposure SHALL receive a server-established **Audience Observation Context**.

Conceptually:

```text
AudienceObservationContext
{
    merchantScope
    audience

    trustedPrincipalContext?
    relationshipContext?
    secureContextEvidence?
    other registered contextual evidence?
}
```

The exact representation is downstream.

The key rule is:

> **Audience/context evidence is established by trusted Main Street boundaries, not asserted authoritatively by the client.**

---

# 16. Audience is not client-controlled authority

A request SHALL NOT become legitimate merely because a browser sends:

```text
audience = MERCHANT
```

or:

```text
relatedCustomer = true
```

Such client values may be locators or transport hints at most.

Main Street must establish the applicable current context independently.

The initial Surface audience vocabulary remains:

```text
MERCHANT
CUSTOMER
PUBLIC
```

as already represented in accepted Surface semantics and current implementation evidence.

---

# 17. Exposure requirement references

An Exposure Element Contract MAY reference one or more bounded, owner-qualified current requirements.

Examples may include:

```text
related customer context
authorised staff context
purpose-bound data use
capability-specific relationship requirement
current security/trust restriction
```

A requirement reference SHALL identify accepted semantics.

It SHALL NOT contain an arbitrary merchant-authored expression.

---

# 18. Requirement evaluation

Current requirement evaluation may conceptually result in:

```text
SATISFIED
UNSATISFIED
UNRESOLVED
```

These are evaluation evidence, not Exposure verdicts and not business lifecycle states.

Required behaviour:

```text
all required evidence SATISFIED
    → continue Exposure resolution

any required evidence UNSATISFIED
    → WITHHOLD

required evidence UNRESOLVED
    → WITHHOLD
```

Therefore:

> **Unknown permission is not permission.**

---

# 19. No generic boolean policy language

Rejected:

```text
IF
    customer.age > 30
AND region != "X"
OR staff.role = ...
THEN EXPOSE
```

unless those semantics are independently accepted and represented through bounded registered contracts.

Where alternative or compound logic is genuinely required, the applicable owner SHALL define one accepted named requirement whose semantics are explicit.

The Exposure layer SHALL NOT become a second programming language.

---

# 20. Governing restriction precedence

The effective resolution SHALL respect the accepted precedence.

Conceptually:

```text
legitimate candidate
        ↓
merchant scope / tenant validity
        ↓
data-protection / purpose restriction
        ↓
relationship / trusted contextual requirements
        ↓
capability-owned Exposure constraints
        ↓
applicable current merchant Exposure choice
        ↓
registered baseline where applicable
        ↓
EXPOSE | WITHHOLD
```

No lower layer can overturn a higher-level restriction.

---

# 21. Merchant choice cannot override platform restrictions

Suppose:

```text
merchant choice = EXPOSE
```

but current data-protection/security authority says:

```text
observation prohibited
```

Result:

```text
WITHHOLD
```

Merchant freedom exists within Main Street's accepted bounds.

It is not authority to bypass privacy, tenant isolation, law, security or capability invariants.

MS-PROT-053 already establishes that handling classification/purpose constrain Exposure without owning business semantics.

---

# 22. STANDARD does not mean public

The following SHALL remain invalid:

```text
DataHandlingClass = STANDARD
        ↓
PUBLIC Exposure
```

`STANDARD` establishes only that no elevated handling class has been identified.

Exposure must still be established independently.

Likewise:

```text
PERSONAL
```

does not universally mean:

```text
WITHHOLD from every audience
```

A related customer or authorised merchant actor may legitimately observe PERSONAL information where all governing conditions permit it.

---

# 23. Authentication does not imply Exposure

Rejected:

```text
CustomerAccount authenticated
        ↓
customer data EXPOSE
```

For relationship-bound CUSTOMER Surfaces, MS-PROT-049 v1.3 requires owner-qualified relationship/context eligibility before Exposure is reached.

Exposure then determines which candidate elements may actually be observed.

---

# 24. Actor Authorisation does not become Exposure authority

Actor Authorisation may supply trusted evidence needed by an Exposure requirement.

It SHALL NOT mean:

```text
actor authorised for operation X
    → all fields visible
```

Conversely:

```text
element EXPOSED
```

does not authorise mutation.

Existing runtime authorisation remains independently enforced.

---

# 25. Surface inclusion does not imply Exposure

A contribution may legitimately belong on a Surface while some projected elements are withheld.

Example:

```text
related customer Booking Surface eligible
        ↓
Booking date           EXPOSE
Booking service        EXPOSE
internal merchant note WITHHOLD
```

The Surface remains.

Only the protected element is withheld.

This is exactly the separation required by MS-PROT-049 v1.3.

---

# 26. Exposure does not imply Public Interaction Binding

For a public subject:

```text
Subject exposed
+
Public Interaction Contribution active
```

is still insufficient to manufacture:

```text
Subject → Interaction
```

MS-PROT-049 v1.2 requires independently authoritative subject-interaction participation.

Therefore Exposure SHALL NOT become participation authority.

---

# 27. Provider readiness remains independent

A provider outage SHALL NOT automatically WITHHOLD unrelated information.

Example:

```text
merchant descriptor current
payment provider unavailable
```

does not imply:

```text
merchant descriptor WITHHOLD
```

Provider/trust restrictions apply only to the smallest justified scope, consistent with MS-PROT-028 v1.3 and MS-PROT-049 v1.1.

---

# 28. Initial resolution architecture is request-scoped

Initial production SHALL NOT require:

```text
Exposure decision table
Redis Exposure cache
persisted visible/not-visible snapshot
precomputed MerchantVisibility document
```

For the initial Merchant Presence path:

```text
current authoritative source queries
        ↓
platform / merchant-presence
request-scoped projection
        ↓
current Exposure resolution
        ↓
request-scoped storefront composition
```

This is intentionally conservative and frugal.

It also gives immediate effect to current revocation evidence.

---

# 29. No Exposure cache without evidence

A future implementation MAY cache resolved Exposure results or exposed storefront representations only if performance evidence justifies it.

Such caching immediately engages the existing Projection Contract rules, particularly revocation-sensitive Trigger G.

Cached:

```text
EXPOSE
```

SHALL NOT become durable permission after current authority says:

```text
WITHHOLD
```

---

# 30. No per-request Exposure audit requirement

Initial production SHALL NOT require every ordinary Exposure decision to become a durable AuditRecord.

That would produce:

```text
every storefront field view
    → permanent audit event
```

without demonstrated need.

Exposure resolution MAY generate ordinary operational metrics/logging subject to data minimisation.

Durable audit evidence is required only where another accepted security, legal or business authority specifically requires it.

---

# 31. Initial Merchant Presence PUBLIC portfolio

Target 8 SHALL register the minimum Exposure Element Contracts necessary to make `platform / merchant-presence` safe for the PUBLIC audience.

Initial contract families are:

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

These identities are semantic contract identities, not frontend sections or API field names.

---

# 32. Public descriptor contracts

The following Profile facts are semantically public descriptor content:

```text
display name
tagline
short summary
approved description
```

For a legitimate PUBLIC Merchant Presence candidate:

```text
baseline = EXPOSE
```

subject to all governing restrictions.

This does not force every field to appear on every website.

Presentation/Surface composition may omit an optional descriptor element entirely.

Therefore:

```text
element not selected for composition
    ≠
Exposure WITHHOLD
```

---

# 33. Descriptor approval remains upstream

An AI-generated description does not become exposable merely because the Exposure baseline is EXPOSE.

Required:

```text
AI candidate wording
        ↓
merchant approval
        ↓
authoritative approved description
        ↓
legitimate Merchant Presence candidate
        ↓
Exposure
```

The Exposure resolver never approves content.

---

# 34. Public Contact Point contract

`profile / public-contact-point` SHALL have:

```text
baseline = WITHHOLD
merchant Exposure choice permitted
```

Public observation requires current Profile-owned evidence that the Contact Point is intended for PUBLIC exposure.

Therefore:

```text
controller login email exists
```

is irrelevant.

Likewise:

```text
MerchantContactPoint exists
```

alone is insufficient.

MS-PROT-051 already explicitly rejects possession of account/contact data as public authority.

---

# 35. Contact point is one bounded public element

A publicly exposed Contact Point may project the bounded public representation required for that contact, such as:

```text
kind
public label where present
public contact value
```

Exposure SHALL NOT thereby reveal:

```text
revision metadata
internal provenance
controller identity
internal administrative notes
unrelated contact data
```

---

# 36. Public Merchant Location contract

`profile / public-merchant-location` SHALL have:

```text
baseline = WITHHOLD
merchant Exposure choice permitted
```

A Merchant Location must also be:

```text
current
ACTIVE
legitimate for current public presence
```

before it becomes an Exposure candidate.

A RETIRED location is not made public by a historical Exposure choice.

Retirement/lifecycle determines candidate legitimacy before Exposure.

---

# 37. Public location element is bounded

The initial public Merchant Location element MAY include the current approved public representation required to describe the merchant place:

```text
public label
structured public address
```

where present.

It SHALL NOT automatically expose:

```text
raw latitude/longitude
provider place identifiers
internal location roles
revision identifiers
provenance evidence
trust evidence
```

Possession of those source facts is not public authority.

---

# 38. Coordinates require separate public semantics

Coordinates MAY be used internally to derive an audience-safe public map representation where a later accepted mapping/presentation contract permits it.

This amendment does not establish:

```text
MerchantLocation coordinates
    → automatically public
```

If exact coordinate observation later becomes semantically material, it requires an explicit Exposure contract or an explicitly bounded safe projection contract.

---

# 39. Private location / public service area

The architecture SHALL support:

```text
Merchant Location
    address = WITHHOLD

Service Area
    = EXPOSE
```

Example:

```text
home-based plumber
```

may truthfully publish:

> Serving Swansea, Neath and surrounding areas

without publishing the home address.

This directly preserves the accepted Profile model.

---

# 40. Public Service Area contract

`profile / public-service-area` governs an explicitly public descriptive Service Area candidate.

Because this Profile concept is itself descriptive public-presence information rather than operational serviceability authority:

```text
legitimate current public Service Area candidate
    → baseline EXPOSE
```

It SHALL NOT imply:

```text
delivery eligibility
appointment travel eligibility
pricing zone
operational coverage guarantee
```

Those remain separately capability/configuration-owned.

---

# 41. Public External Presence Link contract

`profile / public-external-presence-link` governs a current merchant-approved External Presence Link included in public presence.

A legitimate public-presence candidate has:

```text
baseline EXPOSE
```

The link does not imply:

```text
provider verification
Main Street endorsement
external account control
trust status
```

unless separately established.

---

# 42. Public Business Hours contract

`business-hours / public-business-hours` governs current Public Business Hours included in Merchant Presence.

Because the accepted fact is explicitly **Public Business Hours**:

```text
legitimate current configured Public Business Hours candidate
    → baseline EXPOSE
```

No second duplicate generic Exposure setting is required merely to say the same hours are public.

Whether Public Business Hours exist/apply to a merchant or location remains owned by Business Hours semantics.

Their scope remains exact.

---

# 43. Business Hours scope remains visible in meaning

Exposure SHALL NOT flatten:

```text
Merchant Scope hours
Merchant Location L1 hours
Merchant Location L2 hours
```

into one merchant-wide claim.

The projected element retains its Business Hours Scope.

Exposure decides whether the audience may observe it.

It does not alter the scope-qualified temporal truth.

---

# 44. Classification metadata is not initially public Merchant Presence

`MerchantClassificationMetadata` is not part of the initial `platform / merchant-presence` source portfolio established by MS-PROT-027 v1.4.

Target 8 SHALL NOT expose it merely because Profile authority stores it.

Any future public discovery/SEO use must follow its applicable projection, discovery and Exposure contracts.

---

# 45. Provenance is not public by inheritance

The fact that public content has provenance does not mean provenance itself is public.

Example:

```text
description      EXPOSE
approval metadata WITHHOLD
```

unless a separate accepted contract explicitly exposes provenance.

The same applies to:

```text
source revisions
provider evidence
trust evidence
internal identifiers
```

---

# 46. Reduced Merchant Presence

Suppose:

```text
descriptor current
contact current
Business Hours source unavailable
```

MS-PROT-027 v1.4 permits a truthful reduced Merchant Presence representation.

Target 8 then evaluates only the serviceable candidate elements:

```text
descriptor candidate → Exposure

contact candidate → Exposure

hours absent because projection not serviceable
```

It SHALL NOT generate:

```text
hours WITHHOLD
```

as a substitute for source/projection failure.

---

# 47. Revocation-sensitive Exposure

Suppose:

```text
T1 Contact Point public
T2 merchant changes it to private
T3 new storefront request
```

The new request SHALL use current Profile authority and return:

```text
WITHHOLD
```

A previous Exposure result does not create a public right.

The request-scoped initial architecture makes this immediate once current authority is observable.

---

# 48. Concurrent Profile change

Suppose:

```text
device A reads Contact C revision 8
device B reads Contact C revision 8

A changes telephone
    → revision 9

B tries stale public Exposure change
    based on revision 8
```

The Profile same-fact concurrency contract governs.

Target 8 SHALL NOT introduce:

```text
last-write-wins Exposure
```

as a separate shortcut around Profile revisions.

---

# 49. Independent fact changes remain independent

Changing:

```text
Contact Point C1 exposure
```

shall not conflict merely because another actor concurrently updates:

```text
Merchant Location L2
```

No global:

```text
MerchantExposurePolicyVersion
```

is required.

This preserves the Profile fact-family architecture.

---

# 50. Missing Exposure contract fails closed

If a candidate reaches the Exposure boundary and Main Street cannot resolve its exact applicable Exposure Element Contract:

```text
WITHHOLD
```

for the affected element.

It SHALL NOT infer Exposure from:

```text
field name
Java type
business category
page
route
frontend component
provider
AI output
handling class
historical behaviour
```

The missing contract is a design/registration defect, not permission.

---

# 51. Missing requirement evaluator fails closed

If a contract requires owner-qualified contextual evidence but its evaluator/authority cannot be established:

```text
WITHHOLD
```

for that element.

Example:

```text
CUSTOMER field requires related Booking customer
relationship evaluator unavailable/unresolved
        ↓
WITHHOLD
```

Main Street SHALL NOT substitute:

```text
email matches
customer is logged in
identifier looks valid
```

---

# 52. Exact contract mismatch fails closed

A candidate claiming:

```text
ExposureElementContract C@R17
```

shall not silently resolve:

```text
C@R18
```

if release meaning changed.

An unresolvable exact contract is a failure, not permission to use latest semantics.

---

# 53. Later capability contract requirement

Every later capability target that introduces audience-facing semantic elements SHALL classify each relevant read element as one of:

```text
already governed by an accepted Exposure Element Contract

or

requires a new owner-qualified Exposure Element Contract
```

No later target may rely on:

```text
"obviously public"
"obviously customer-visible"
"frontend only shows it to staff"
```

as Exposure architecture.

---

# 54. Later PUBLIC capability elements

Targets such as Publication, Offering/Ordering and Booking may later register PUBLIC Exposure contracts for elements including, where accepted:

```text
Publication title/body
Offering name
Offering price
availability summary
public interaction subject data
```

Target 8 does not pre-accept those exact elements.

Their owning capability targets determine them.

---

# 55. Later CUSTOMER capability elements

A CUSTOMER contract may consume current relationship/context evidence established through MS-PROT-049 v1.3 and the owning capability.

It SHALL NOT duplicate the relationship graph inside Exposure.

Canonical:

```text
Customer Surface Eligibility
        ↓
candidate Booking element
        ↓
Exposure Element Contract
        ↓
related-customer evidence
        ↓
EXPOSE / WITHHOLD
```

---

# 56. Later MERCHANT capability elements

MERCHANT Exposure may consume established Actor Authorisation or trusted staff-context evidence where the registered element contract requires it.

It SHALL NOT itself issue privileges or Role Assignments.

Example:

```text
merchant workspace legitimate
+
staff actor authorised for bounded read context
+
Exposure contract permits observation
        ↓
EXPOSE
```

---

# 57. AI boundary

AI MAY:

```text
interpret:
"hide my address"

map to:
current registered Location Exposure choice

explain consequence

prepare candidate Profile mutation
```

AI SHALL NOT:

```text
create Exposure Element Contracts
invent audience predicates
decide runtime Exposure
bypass data protection
turn inference into public visibility
```

An AI recommendation such as:

> “Your website would be more useful if you showed your address.”

remains advice.

Only an approved owner-authoritative change may alter current Exposure choice.

This composes with accepted MS-PROT-057 through v1.2.

---

# 58. Website Population boundary

Website Population may identify:

> “Your location is not currently displayed.”

It MAY ask whether the merchant wants to expose it.

It SHALL NOT interpret:

```text
Location exists
        ↓
publish address
```

If the merchant chooses to make the location public:

```text
Website assistance
        ↓
confirmed merchant intent
        ↓
Profile-owned exposure mutation
        ↓
new current Profile revision
        ↓
subsequent Exposure resolution
```

Website assistance remains non-authoritative.

---

# 59. Exposure and website presentation remain separate

If:

```text
description = EXPOSE
```

the Website presentation layer is not required to place the description:

```text
homepage
footer
About section
every page
```

Exposure grants observation eligibility for a legitimate candidate.

Presentation determines placement.

Likewise a presentation decision not to use an otherwise exposable field is not:

```text
WITHHOLD
```

---

# 60. Exposure and discoverability remain separate

A publicly exposed storefront may legitimately be:

```text
viewable
but not indexed
```

Search-engine indexing, recommendation and discovery remain separate policies.

No:

```text
PUBLIC_NO_INDEX
```

Exposure verdict is introduced.

---

# 61. Exposure and masking remain separate

Suppose:

```text
protected payment source
        ↓
projection creates
"Card ending 4242"
```

Exposure evaluates the safe projected element.

It does not return:

```text
MASK
```

The binary Exposure algebra remains:

```text
EXPOSE
WITHHOLD
```

---

# 62. Exposure results are request evidence, not business facts

A resolution such as:

```text
EXPOSE at request T
```

is evidence of the decision made for that request/context.

It is not:

```text
business lifecycle state
permanent permission
merchant configuration revision
domain event
```

unless another accepted contract explicitly requires durable evidence of that decision.

---

# 63. Runtime responsibility

Main Street SHALL have one logical **Exposure Resolution responsibility**.

This responsibility owns:

```text
contract lookup
context assembly validation
bounded requirement evaluation coordination
current-policy resolution
deterministic EXPOSE/WITHHOLD result
```

It does not own:

```text
Profile
Booking
Customer relationships
Data Protection
Trust
Actor Authorisation
Surface membership
Projection Serviceability
```

This responsibility does not imply a microservice.

---

# 64. No speculative generic policy framework

Initial implementation should remain the smallest system capable of satisfying these contracts.

Rejected absent evidence:

```text
OPA/Rego deployment
SpEL
general ABAC framework
policy database
distributed policy service
merchant rule scripting
AI-generated policies
```

A future generic mechanism may be considered only if repeated concrete Exposure contracts prove sufficient common behaviour and preserve accepted ownership.

---

# 65. Falsification — brochure merchant

Merchant has:

```text
display name
description
public telephone
service area
```

and no additional capability.

Result:

```text
valid public Merchant Presence
```

No synthetic public-information capability is needed.

**PASS**

This is consistent with the accepted baseline-public-presence model.

---

# 66. Falsification — controller email

Main Street knows:

```text
controller email = owner@example.com
```

but no public MerchantContactPoint exists.

Result:

```text
owner@example.com
    → not an Exposure candidate
```

**PASS**

---

# 67. Falsification — private contact

MerchantContactPoint exists but current Profile exposure choice is WITHHOLD.

Result:

```text
PUBLIC → WITHHOLD
```

**PASS**

---

# 68. Falsification — private home address

Home-based consultant has:

```text
MerchantLocation L1
public exposure = WITHHOLD

Service Area = Swansea
```

Result:

```text
address      WITHHOLD
service area EXPOSE
```

where the Service Area is a legitimate public-presence candidate.

**PASS**

---

# 69. Falsification — retired location

Old Location L1 was once public but is now RETIRED.

Result:

```text
L1 is not a current public-presence candidate
```

Exposure does not resurrect it.

**PASS**

---

# 70. Falsification — coordinates

MerchantLocation has exact coordinates for internal mapping.

Location is publicly exposed.

Result:

```text
public location representation may be exposed

raw coordinate source is not automatically exposed
```

**PASS**

---

# 71. Falsification — payment setup failure

Public merchant description is otherwise valid.

Payment-provider onboarding is incomplete.

Result:

```text
description remains public
payment-dependent interaction may be restricted
```

No universal merchant verification/publication gate.

**PASS**

---

# 72. Falsification — merchant choice versus protection

Merchant chooses:

```text
EXPOSE
```

for an element that current protection/security authority forbids.

Result:

```text
WITHHOLD
```

**PASS**

---

# 73. Falsification — STANDARD classification

An element is classified:

```text
STANDARD
```

with no registered PUBLIC Exposure contract.

Result:

```text
WITHHOLD
```

**PASS**

---

# 74. Falsification — authenticated CustomerAccount

CustomerAccount is authenticated but no qualifying relationship exists.

The relationship-bound CUSTOMER contribution fails eligibility before Exposure.

No customer-specific data is exposed.

**PASS**

---

# 75. Falsification — related Booking customer

Customer relationship is valid.

Booking surface is eligible.

Candidate elements:

```text
service
date/time
internal note
```

Contracts yield:

```text
service       EXPOSE
date/time     EXPOSE
internal note WITHHOLD
```

**PASS**

---

# 76. Falsification — missing Business Hours source

Merchant descriptor is current.

Business Hours source cannot produce serviceable evidence.

Result:

```text
descriptor remains candidate
hours omitted as unserviceable
```

Exposure does not invent hours or misclassify their failure as privacy withholding.

**PASS**

---

# 77. Falsification — Exposure revocation

Public telephone is changed to private.

Next request resolves current Profile evidence and WITHHOLDs it.

Previous request's EXPOSE result is not retained as authority.

**PASS**

---

# 78. Falsification — concurrent contact edit

One actor changes a Contact Point's value while another issues a stale Exposure mutation.

Profile revision concurrency resolves the conflict.

Exposure does not implement independent last-write-wins.

**PASS**

---

# 79. Falsification — missing contract

New developer adds projected field:

```text
merchantInternalRiskScore
```

to a storefront candidate without an Exposure Element Contract.

Result:

```text
WITHHOLD
+
governance/registration defect
```

not:

```text
field name seems harmless → EXPOSE
```

**PASS**

---

# 80. Falsification — AI recommendation

Website assistant suggests:

> “Show your address to make it easier for customers to find you.”

Merchant ignores suggestion.

Result:

```text
no Profile mutation
no Exposure change
```

**PASS**

---

# 81. Falsification — search indexing

Merchant storefront is PUBLIC-exposable but indexing is disabled.

Result:

```text
direct public observation permitted
search indexing still disabled
```

**PASS**

---

# 82. Falsification — Public Interaction Binding

Offering O1 is exposed.

Appointment Public Interaction Contribution exists.

No authoritative O1 → Appointment participation exists.

Result:

```text
no Public Interaction Binding
```

Exposure does not invent participation.

**PASS**

---

# 83. Falsification — provider outage

External provider fails.

Unrelated public Profile elements remain observable.

Only explicitly dependent interaction/serviceability contracts are affected.

**PASS**

---

# 84. Falsification — no persistent decision log

Thousands of public storefront reads resolve ordinary Profile Exposure.

No durable business/audit record is created per element merely because Exposure was evaluated.

**PASS**

---

# 85. Rejected designs

The following are rejected:

1. universal `VisibilityStatus` on business objects;
2. `PUBLIC/PRIVATE/CUSTOMER/STAFF/HIDDEN` object lifecycle;
3. one giant Merchant Exposure Policy aggregate;
4. a merchant-authored boolean policy DSL;
5. `STANDARD ⇒ PUBLIC`;
6. authentication as customer Exposure authority;
7. Surface eligibility as Exposure;
8. Projection Serviceability as Exposure;
9. Provider Readiness as Exposure;
10. Actor Authorisation as universal field visibility;
11. Exposure as operation authority;
12. fact existence as public permission;
13. public Location automatically exposing coordinates;
14. controller contact automatically becoming business contact;
15. missing Exposure contracts defaulting to public;
16. implicit latest Exposure contract lookup;
17. cached EXPOSE results surviving current revocation;
18. Exposure resolver storing duplicate copies of capability-owned policy;
19. AI-generated Exposure predicates;
20. Website Population directly altering runtime Exposure;
21. one persistent audit record for every ordinary Exposure evaluation;
22. a speculative general-purpose policy microservice.

---

# 86. Hard invariants

1. Exposure remains a deterministic element-level `EXPOSE | WITHHOLD` decision.
2. Exposure operates only on already-legitimate candidate elements.
3. Surface membership does not imply Exposure.
4. Projection Serviceability does not imply Exposure.
5. Projection failure is not silently converted into WITHHOLD.
6. Exposure does not grant operation authority.
7. Exposure does not own source business facts.
8. Exposure Element Contracts are explicit and owner-qualified.
9. Release-affined Exposure contracts resolve exactly; implicit latest substitution is prohibited.
10. Missing Exposure contract fails closed.
11. Missing/unresolved required evidence fails closed.
12. Client-supplied audience/relationship claims are not authority.
13. Merchant Exposure choice remains owned by the applicable fact/capability authority.
14. No universal Merchant Exposure Policy aggregate is introduced.
15. Profile Exposure mutations preserve Profile revision/concurrency semantics.
16. Merchant choice cannot override privacy/security/platform constraints.
17. Data-handling classification does not itself determine Exposure.
18. Authentication does not itself establish relationship-bound CUSTOMER Exposure.
19. Actor-authorisation evidence may inform Exposure without transferring authorisation ownership.
20. Trust/provider failure affects only justified scope.
21. Initial Merchant Presence Exposure resolution is request-scoped.
22. Initial production requires no dedicated Exposure cache/database/service.
23. Current Exposure revocation outranks stale historical visibility.
24. Public Contact Points require current Profile-owned public choice.
25. Merchant Location existence does not imply public address.
26. Raw Merchant Location coordinates are not automatically public.
27. Retired Merchant Locations are not revived by historic Exposure choices.
28. Public Service Area does not become operational serviceability authority.
29. Public External Presence does not manufacture provider verification.
30. Public Business Hours retain exact Business Hours Scope.
31. Profile classification metadata is not automatically part of initial public Merchant Presence.
32. Provenance/internal metadata does not inherit Exposure from the fact it describes.
33. AI may interpret/propose Exposure intent but is never runtime Exposure authority.
34. Website assistance may recommend Exposure changes but must use owner-authoritative mutation.
35. Exposure and presentation remain separate.
36. Exposure and discoverability/indexing remain separate.
37. Masking/redaction remain projection/data-handling transformations rather than Exposure verdicts.
38. Ordinary Exposure decisions need not become durable AuditRecords.
39. Later capability targets must explicitly classify their audience-facing elements under accepted Exposure contracts.
40. No later capability may rely on frontend convention or “obviously public” reasoning.

---

# 87. Deferred questions

The following narrower questions remain traceable.

| ID | Status | Deferred question | Future owner / revisit |
|---|---|---|---|
| `MS-PROT-027-V15-DQ-001` | DEFERRED — INACTIVE | Exact Java representation and registration mechanism for Exposure Element Contracts | Production Exposure implementation |
| `MS-PROT-027-V15-DQ-002` | DEFERRED — INACTIVE | Exact semantic-bundle encoding/materialisation representation for Exposure definitions | Semantic materialisation implementation |
| `MS-PROT-027-V15-DQ-003` | DEFERRED — INACTIVE | Exact PostgreSQL representation of Profile-owned public Exposure choices | Profile persistence implementation |
| `MS-PROT-027-V15-DQ-004` | DEFERRED — INACTIVE | Exact transport/application operation representation for changing Profile Exposure choices | Target 20 / merchant API |
| `MS-PROT-027-V15-DQ-005` | DEFERRED — INACTIVE | Exact CUSTOMER Exposure requirement evaluators for Booking/Appointment/Ordering/etc. | Owning capability targets |
| `MS-PROT-027-V15-DQ-006` | DEFERRED — INACTIVE | Exact MERCHANT field-observation privilege/context mappings where a capability requires them | Owning capability/access implementation |
| `MS-PROT-027-V15-DQ-007` | DEFERRED — INACTIVE | Concrete data-protection restriction-evaluator integration and lifecycle consequences | Target 17 |
| `MS-PROT-027-V15-DQ-008` | DEFERRED — INACTIVE | Exact API distinction among withheld, absent, not-serviceable and unavailable representations | Target 20 |
| `MS-PROT-027-V15-DQ-009` | DEFERRED — INACTIVE | Exposure-resolution operational telemetry and diagnostic metrics | Target 19 |
| `MS-PROT-027-V15-DQ-010` | DEFERRED — INACTIVE | Whether Exposure-result caching/memoisation is ever justified | Only after measured need |
| `MS-PROT-027-V15-DQ-011` | DEFERRED — INACTIVE | CDN/static-storefront Exposure-revocation propagation contract | If static/CDN storefront delivery is introduced |
| `MS-PROT-027-V15-DQ-012` | DEFERRED — INACTIVE | Search/index convergence when an exposed element becomes withheld | When search/indexing is introduced |

No deferred item weakens the fail-closed, exact-release, owner-authority or revocation-sensitive invariants above.

---

# 88. Implementation evidence assessment

Current source code already separates static Surface composition and MERCHANT contextual Surface eligibility, but it does not contain production Exposure resolution.

`ContextualSurfaceResolver` currently evaluates merchant contribution eligibility, actor privileges, residual obligations and interaction availability. It does not evaluate element-level `EXPOSE/WITHHOLD`.

`StaticSurfaceContributionComposer` likewise deliberately leaves final contextual visibility outside static composition.

No production `ExposureResolver`/`ExposureDecision` implementation or production MerchantLocation model was found in the repository inspection.

Therefore:

```text
current implementation
    ≠ contradictory implementation

current implementation
    = incomplete evidence consistent with Target-8 gap
```

No implementation change is authorised by this authority.

---

# 89. Corpus-conformance assessment

The authority preserves:

- MS-PROT-027 v1.2's binary, element-level Exposure semantics;
- MS-PROT-027 v1.3's serviceability/Exposure separation and revocation priority;
- MS-PROT-027 v1.4's request-scoped initial Merchant Presence projection and explicit Target-8 handoff;
- MS-PROT-049's Surface ownership boundaries;
- MS-PROT-049 v1.2's Public Interaction Binding separation;
- MS-PROT-049 v1.3's Customer Surface Eligibility separation;
- MS-PROT-051's private/public Profile and Location distinctions;
- MS-PROT-051 v1.1's revision/concurrency ownership;
- MS-PROT-052 v1.1's baseline public-presence model;
- MS-PROT-053's protection-versus-Exposure separation;
- MS-PROT-028 v1.3's minimum-restriction principle.

No accepted semantic owner is transferred.

---

# 90. Programme consequence

This authority closes:

```text
MS-PROT-051-V11-DQ-006
    → RESOLVED

MS-PROT-079 Target 8
    → DESIGN-CLOSED

Target 9 — Provider readiness
    → CURRENT ACTIVE TARGET
```

Target 8 is specifically the point at which backend Exposure resolution becomes implementation-constraining while remaining separate from the neighbouring runtime dimensions.

This authority does **not** authorise:

```text
production code
tests
migrations
API implementation
UI
storefront implementation
provider work
prototype work
```

---

# 91. Final governing principle

> **Main Street shall expose only explicit, legitimate semantic elements governed by exact registered Exposure Element Contracts. Exposure resolution is request-contextual, deterministic and fail-closed: it consumes current owner-qualified business policy, trusted audience/context evidence and governing privacy/security restrictions, but owns none of them. Fact existence, Surface membership, Projection Serviceability, authentication, provider readiness, AI inference and presentation convention shall never substitute for Exposure authority.**

---

## Governance assessment

**DESIGN / PROPOSE:** COMPLETE  
**AUTHORITY TRACE:** COMPLETE  
**IMPLEMENTATION-EVIDENCE REVIEW:** COMPLETE  
**FALSIFICATION:** **PASS**  
**AMBIGUITY REVIEW:** **PASS within Target-8 scope**  
**CORPUS CONFORMANCE:** **PASS**  
**RECOMMENDATION:** **ACCEPT**  
**MANUAL APPROVAL:** **GRANTED on 27 August 2026**  
**STATUS:** **ACCEPTED**
