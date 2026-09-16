# MS-PROT-051 v1.1 — Merchant Profile & Location Authoritative Mutation, Revision & Onboarding Handoff Amendment

**Document ID:** MS-PROT-051  
**Version:** 1.1  
**Status:** **ACCEPTED after Target-5 review, falsification and manual approval**  
**Approved:** 27 August 2026  
**Amends:** MS-PROT-051 v1.0 within production ownership, mutation, revision, retirement, persistence and onboarding-handoff scope  
**Depends on:** MS-PROT-027 v1.2/v1.3; MS-PROT-028 v1.3; MS-PROT-031; MS-PROT-033; MS-PROT-040 v1.2; MS-PROT-050 v1.2/v1.3; MS-PROT-051 v1.0; MS-PROT-052 v1.2; MS-PROT-053; MS-PROT-059; MS-PROT-062; MS-PROT-063; MS-PROT-069; MS-PROT-072; MS-PROT-074; MS-PROT-076  
**Closes:** MS-PROT-079 Target 5 — Merchant Profile / Location  
**Purpose:** Complete the production authority for merchant profile/presence facts and Merchant Locations without introducing a universal BusinessProfile aggregate, allowing concurrent/stale writes to destroy merchant intent, or allowing onboarding, external providers, projections or configuration to usurp profile ownership.

---

## 1. Governing decision

Main Street SHALL persist merchant profile/presence information as **independently owned, revisioned fact families**, not as one universal `BusinessProfile` aggregate.

Canonical model:

```text
Merchant Scope
    │
    ├── Merchant Public Descriptor
    ├── Merchant Classification Metadata
    ├── Merchant Contact Points
    ├── Merchant Locations
    ├── Merchant Service-Area Descriptors
    └── Merchant External-Presence Links

separate authorities
    ├── Public Business Hours
    ├── Branding
    ├── Offerings
    ├── Staff
    ├── Publication
    ├── Trust claims
    └── Provider connections

             ↓ compose

       Business Profile surface
             ↓
       audience-safe projections
```

There SHALL NOT be one semantic:

```text
BusinessProfile.version
```

whose update serialises every merchant fact into one contention and transaction boundary.

That would contradict MS-PROT-051's existing ownership model.

---

## 2. Production ownership

The Merchant Profile/Presence authority SHALL own authoritative persistence for:

```text
MerchantPublicDescriptor
MerchantClassificationMetadata
MerchantContactPoint
MerchantLocation
MerchantServiceAreaDescriptor
MerchantExternalPresenceLink
```

It SHALL NOT own:

```text
Public Business Hours
Merchant Configuration
Offerings
Publications
Staff authority
Branding
Trust Claims
Provider Connections
Commercial Entitlement
analytics
```

Public Business Hours retain their separate explicit merchant/location scope and remain independently authoritative.

---

## 3. No mandatory empty Profile aggregate

Merchant Account establishment does not create a fake or empty universal Business Profile merely to satisfy persistence structure.

A valid state is:

```text
Merchant Account = OPEN
descriptor = absent
locations = none
contact points = none
service areas = none
```

until applicable profile facts are supplied.

Specific downstream operations may require particular facts, but their absence does not make the Merchant Account nonexistent.

---

## 4. Independent fact identities

Every independently mutable collection fact SHALL have stable merchant-scoped identity.

For example:

```text
MerchantContactPointIdentity
MerchantLocationIdentity
MerchantServiceAreaIdentity
MerchantExternalPresenceIdentity
```

Identity is:

```text
Merchant Scope
+
fact-specific identifier
```

A fact's current value is not its identity.

In particular:

```text
MerchantLocationIdentity
    ≠ postal address
    ≠ latitude/longitude
    ≠ Google place ID
```

---

## 5. Descriptor identity

Merchant Public Descriptor is logically singleton-scoped:

```text
Merchant M
    → zero or one current MerchantPublicDescriptor
```

It does not require an artificial collection identifier.

Its revisions remain exact and historical.

---

## 6. Immutable fact revisions

Every material authoritative profile mutation SHALL produce an immutable logical fact revision.

Conceptually:

```text
MerchantLocation L1
    Revision 1
        ↓ correction
    Revision 2
        ↓ label change
    Revision 3
```

The current state points to the latest authoritative revision.

Previous revisions remain historical evidence subject to data-retention authority.

This does not mandate event sourcing.

---

## 7. Why revisions are mandatory

Profile data can become historically meaningful.

Example:

```text
Appointment committed today
    at MerchantLocation L1

merchant later changes L1
```

Main Street MUST NOT make the historical appointment silently appear to have occurred somewhere else.

A consuming capability whose commitment requires historical location/profile meaning must preserve either:

```text
exact MerchantLocation revision reference
```

or:

```text
the required immutable committed location snapshot
```

according to that capability's authority.

Profile mutation never rewrites those commitments.

---

## 8. Optimistic concurrency

Every material profile mutation SHALL identify the expected current revision of the target fact where one already exists.

Example:

```text
phone A reads Location L1 revision 8
laptop reads Location L1 revision 8

phone edits address correction
    → revision 9

laptop edits stale revision 8
    → PROFILE_REVISION_CONFLICT
```

Blind last-write-wins is rejected.

---

## 9. Independent facts should not conflict globally

Two legitimate edits to unrelated facts should not conflict merely because they occur concurrently.

Example:

```text
Controller edits merchant description

Staff administrator adds public telephone
```

These operate against different fact authorities/revisions.

No global:

```text
BusinessProfile version 144
```

should force an artificial conflict.

This follows the accepted aggregate principle that consistency boundaries should be no larger than required.

---

## 10. Mutation operations are owner-specific

Profile mutation SHALL use explicit profile-owned application operations.

Conceptually:

```text
UpdateMerchantPublicDescriptor

CreateMerchantContactPoint
UpdateMerchantContactPoint
RetireMerchantContactPoint

CreateMerchantLocation
CorrectMerchantLocation
RetireMerchantLocation

CreateMerchantServiceArea
UpdateMerchantServiceArea
RetireMerchantServiceArea

CreateExternalPresenceLink
UpdateExternalPresenceLink
RetireExternalPresenceLink
```

These names are conceptual contracts, not required Java method names.

Rejected:

```text
PATCH /business-profile
{
  arbitrary.path = arbitrary.value
}
```

as the semantic mutation model.

---

## 11. Bulk UI save does not create a giant transaction

The frontend may present:

```text
Save Business Profile
```

for usability.

That does not imply:

```text
one BusinessProfile aggregate
+
one universal transaction
```

The application layer may decompose the merchant gesture into bounded owner-specific mutations.

Atomic composition is used only where an actual invariant requires it.

---

## 12. Idempotency

Every externally retryable mutation SHALL have logical request identity.

If:

```text
Create contact point C
    commits

response lost

same request retried
```

Main Street returns the same committed result.

It MUST NOT create another contact point.

Same request identity with materially different mutation intent is rejected.

---

## 13. Merchant Location lifecycle

Merchant Location SHALL use:

```text
ACTIVE
    ↓ retire
RETIRED
```

`RETIRED` is terminal for that location identity.

Retirement means:

> The Merchant Location no longer represents a current merchant place for new ordinary bindings or current public presence, while its identity and historical revisions remain available where retention or historical commitments require them.

---

## 14. Temporary closure is not retirement

A temporarily closed branch remains an ACTIVE Merchant Location.

Examples:

```text
Christmas closure
refurbishment
closed every Sunday
temporarily closed today
```

belong to Public Business Hours / operating overrides where applicable.

Therefore:

```text
temporarily CLOSED
    ≠
MerchantLocation RETIRED
```

---

## 15. Address correction versus relocation

A stable MerchantLocation identity represents one continuing merchant place.

Corrections that preserve that place may produce a new revision:

```text
postcode typo
address formatting
building-name correction
coordinate correction
public label change
```

But moving the merchant presence to a materially different physical place SHALL NOT mutate the old location identity into the new place.

Canonical move:

```text
Location L1
    10 High Street
        ↓ merchant moves

create Location L2
    45 Market Street

update applicable configuration/bindings
        ↓
retire L1
```

This prevents stable references from silently changing geographic meaning.

Main Street SHALL NOT attempt to decide "same physical place" from distance heuristics alone.

The merchant/application operation expresses whether the action is a correction or a relocation.

---

## 16. Coordinates are not provider authority

Coordinates MAY be part of a Merchant Location revision.

They may originate from:

```text
merchant-selected map point
address geocoding
external provider evidence
import
```

A geocoder result is candidate/evidence until the governing profile workflow accepts it.

Therefore:

```text
Google/other geocoder coordinates
    ≠ automatically authoritative Main Street coordinates
```

Provider selection remains downstream.

---

## 17. Location retirement and active configuration

A Merchant Location referenced by current configuration for new activity MUST NOT be retired while that reference would become invalid.

Example:

```text
Appointment Offering
    performedAt → L1
```

Then:

```text
Retire L1
```

requires applicable configuration evolution first.

This strengthens the existing MS-PROT-051 rule that referenced profile facts cannot simply be deleted.

---

## 18. Historical references need not block retirement forever

Historical commitments do not automatically force a location to remain ACTIVE forever.

If the owning capability preserves the exact historical semantics it needs:

```text
old Appointment
    → L1 revision/snapshot
```

L1 may be retired for new use.

Thus:

```text
historical reference
    ≠ necessarily current operational dependency
```

The referencing capability owns that distinction.

---

## 19. Retired locations cannot receive new bindings

After retirement:

```text
new Offering → retired L1
new configuration → retired L1
new location-scoped setup → retired L1
```

shall be rejected.

Historical interpretation remains possible where required.

---

## 20. Location-scoped child information

Facts explicitly scoped to a Merchant Location remain attached to that identity.

Examples may include:

```text
location-scoped contact point
location-scoped Public Business Hours
external mapping
```

Retiring L1 SHALL NOT silently transform:

```text
L1 telephone
```

into:

```text
merchant-wide telephone
```

or copy L1 hours to another location.

Scope remains exact.

---

## 21. No destructive cascading semantics

Retiring a location does not automatically erase every independently owned fact that references it.

Some data may remain required for:

```text
history
audit
external sync cleanup
existing commitments
recovery
```

Each owner determines its valid lifecycle.

Presentation and new-activity eligibility exclude the retired location appropriately.

---

## 22. Direct merchant mutation authority

Normal direct profile mutation requires:

```text
valid trusted execution context
+
resolved Merchant Scope
+
current actor authority for the requested profile operation
+
eligible Merchant Account lifecycle
```

The Merchant Controller may perform ordinary profile-management operations.

Delegated staff MAY also do so where current Merchant Membership/Role Assignment grants the applicable registered authority.

Staff operational access remains subject to the accepted authorised-device requirement.

No combination of profile privileges becomes Merchant Controller authority.

---

## 23. Controller transfer

An Onboarding Case or existing profile does not belong personally to the former Controller.

After transfer:

```text
Controller A session still open
        +
A no longer current Controller
        ↓
profile operation denied
```

Current authority is re-evaluated.

The profile facts themselves remain attached to the Merchant Account.

---

## 24. Suspension and account lifecycle

Merchant-wide suspension restricts profile mutation only according to the accepted scope of the suspension.

`CLOSING` and `CLOSED` accounts do not perform ordinary profile evolution unless a specifically authorised closure/remediation operation requires it.

Account closure does not physically erase profile history merely by changing account lifecycle.

Retention remains independently governed.

---

## 25. Onboarding candidate boundary

While the Onboarding Case remains `IN_PROGRESS`, Profile/Location answers remain:

```text
onboarding evidence
+
candidate profile intent
```

They do not automatically mutate profile authority.

This preserves the Target-4 boundary already accepted in MS-PROT-052 v1.2.

---

## 26. Exact onboarding adoption

After submission of an exact reviewed Onboarding Case Revision, application orchestration MAY adopt its profile/location candidates into authoritative Profile/Location facts through the Profile-owned operations defined here.

Canonical:

```text
Submitted Onboarding Case
exact reviewed revision
        ↓
derive exact profile mutation candidates
        ↓
Profile/Location owner operations
        ↓
authoritative profile facts
```

This is independent of:

```text
Initial Configuration Intent
        ↓
MS-PROT-040
```

The Initial Configuration Intent does NOT become Profile truth.

---

## 27. Same review may supply merchant approval evidence

Where final onboarding review explicitly showed the exact merchant-facing profile content, including AI-assisted wording where applicable, the merchant's exact review confirmation MAY satisfy the human approval evidence required for adopting those exact values.

Example:

```text
AI suggests description A

final review shows A

Controller confirms exact case revision

Profile bootstrap adopts A
```

Valid.

But:

```text
AI later generates description B

bootstrap writes B
```

is forbidden.

---

## 28. Onboarding adoption is idempotent

The application of one submitted Onboarding Case Revision SHALL have stable logical bootstrap identity.

Retry:

```text
case C / revision 27
        ↓
profile bootstrap commits
        ↓ response lost
        ↓
retry
```

returns/reconciles the same logical effects.

It MUST NOT create:

```text
second location
second telephone
second external link
```

from the same submitted intent.

---

## 29. Onboarding must not overwrite newer direct edits

This is a hard invariant.

Suppose:

```text
Onboarding reviewed:
    telephone = 111

before delayed profile adoption:
Controller directly changes profile:
    telephone = 222
```

The delayed onboarding operation MUST NOT overwrite `222`.

Every onboarding-derived profile mutation therefore carries the authoritative base-state/revision assumption against which its candidate was reviewed.

If current state differs:

```text
ONBOARDING_PROFILE_CONFLICT
```

is raised.

The system requires reconciliation rather than choosing one value silently.

---

## 30. No placeholder facts from onboarding

If onboarding contains no Merchant Location:

```text
do not create:
Location = UNKNOWN
Location = ONLINE
Location = N/A
```

If no public telephone is supplied:

```text
do not manufacture one from Controller account data
```

Absence remains absence.

This preserves the existing distinction between account contact, merchant contact and public contact.

---

## 31. Profile facts and Merchant Configuration remain independent

Ordinary changes such as:

```text
description
telephone
tagline
external link
same-place address correction
```

do not automatically create a new Configuration Revision.

MS-PROT-051 already establishes that ordinary profile edits normally commit the fact and refresh affected projections.

---

## 32. Explicit configuration bindings remain exact

Where configuration explicitly references Profile/Location authority:

```text
Offering → MerchantLocation L1
```

the binding remains configuration-owned.

Profile cannot modify that binding merely because L1 changes.

Material changes requiring a new binding follow MS-PROT-040.

This is especially why a physical relocation creates L2 rather than turning L1 into a different place.

---

## 33. Service-area descriptors remain descriptive

A service-area edit commits Profile authority only.

It does not change:

```text
delivery eligibility
appointment travel radius
order acceptance
service-call admissibility
```

unless an accepted capability contract explicitly references the same structured geographic authority.

Existing MS-PROT-051 already requires that separation.

---

## 34. Merchant classification remains non-executable

Updating:

```text
plumber
hairdresser
consultant
retailer
```

as merchant classification metadata SHALL NOT trigger configuration mutation merely because classification changed.

Classification remains contextual metadata.

---

## 35. Exposure remains separate

The existence of:

```text
home address
telephone
email
location
coordinates
```

does not mean those elements may be observed publicly.

MS-PROT-027 defines Exposure independently as an `EXPOSE | WITHHOLD` audience decision rather than a universal object visibility state.

Target 8 will complete the production Exposure resolution path.

Target 5 SHALL NOT pre-empt it by creating:

```text
profile.public = true
```

as universal authority.

---

## 36. Privacy-sensitive withdrawal

When an accepted exposure/privacy decision requires a location/contact element to become withheld, stale projections MUST NOT continue exposing it merely because they still contain an older copy.

MS-PROT-027 v1.3 already establishes revocation-sensitive projection constraints.

Target 7 will complete the corresponding production Projection Contract.

---

## 37. Profile mutation does not publish automatically

Canonical:

```text
profile fact commits
        ↓
projection convergence
        ↓
surface composition
        ↓
Exposure resolution
        ↓
audience result
```

Not:

```text
merchant saved phone
    → website publication transaction
```

Profile owner does not own storefront publication.

---

## 38. External imports

External source values remain evidence/candidates unless an accepted integration contract explicitly grants write authority.

Example:

```text
Main Street phone = 111
Google phone = 222
```

does not mean:

```text
Main Street phone ← 222
```

automatically.

This preserves the existing MS-PROT-051 external-import rule.

---

## 39. Verification remains independent

A Merchant Location may simultaneously be:

```text
recorded by Main Street
private
Google-verified externally
not verified by Main Street
```

without contradiction.

Google verification ordinarily constrains the Google integration, not Main Street publication as a whole.

MS-PROT-028 v1.3 rejects turning external verification into a universal Main Street gate.

---

## 40. Persistence architecture

Profile/Location authoritative state SHALL use the accepted relational/repository architecture.

Conceptually:

```text
Profile/Presence owner
    ↓
domain-oriented repository contracts
    ↓
relational persistence
```

No unrelated capability may directly mutate Profile tables.

A relational foreign key may protect integrity but does not transfer semantic ownership.

---

## 41. Required persistent evidence

Production persistence must be able to establish, where applicable:

```text
Merchant Scope
fact identity
fact revision
current/retired state
value
source/provenance
actor/origin
committed time
logical request identity
scope/reference relationships
```

This does not mandate one table or persistence structure.

---

## 42. Hard deletion is not semantic retirement

These remain distinct:

```text
MerchantLocation RETIRED
        ≠
database row physically deleted
        ≠
personal data erased
```

Semantic lifecycle is owned here.

Retention, erasure, anonymisation and legally required deletion remain owned by MS-PROT-053.

---

## 43. Location data and personal data

A home-based merchant's private location may itself constitute sensitive personal/business information in context.

Its storage does not grant public exposure.

Profile persistence, Exposure and data-protection authority remain separate throughout the lifecycle.

---

## 44. Profile projections

Profile projections are derived read models.

If cached, persisted, asynchronous, multi-source or revocation-sensitive, they fall under MS-PROT-027 v1.3 Projection Contract applicability.

Target 7 will define the exact production Profile projection contracts.

Target 5 therefore defines source authority, not projection-cache technology.

---

## 45. Failure semantics

The production application boundary SHALL preserve distinguishable outcomes where recovery differs, including:

```text
PROFILE_FACT_NOT_FOUND

PROFILE_REVISION_CONFLICT

PROFILE_FACT_RETIRED

LOCATION_NOT_FOUND

LOCATION_RETIRED

LOCATION_REFERENCE_CONFLICT

LOCATION_RELOCATION_REQUIRES_NEW_IDENTITY

INVALID_PROFILE_SCOPE

AUTHENTICATION_REQUIRED

AUTHORISATION_REJECTION

MERCHANT_ACCOUNT_OPERATION_RESTRICTED

ONBOARDING_PROFILE_CONFLICT

VALIDATION_REJECTION

TECHNICAL_FAILURE_BEFORE_COMMIT

EXECUTION_UNCERTAIN

SUCCESS

ALREADY_APPLIED
```

Exact Target-20 transport names remain downstream.

---

## 46. Falsification — brochure merchant

Merchant has:

```text
display name
description
telephone
no location
no additional capability
```

Valid.

No physical address or fake location required.

**PASS**

---

## 47. Falsification — online consultant

Merchant has:

```text
descriptor
remote service
optional public email
zero Merchant Locations
```

Valid.

**PASS**

---

## 48. Falsification — mobile tradesperson

Merchant records:

```text
private home/service base
public service area
```

The private base is not automatically exposed.

Service area does not automatically become operational eligibility.

**PASS**

---

## 49. Falsification — multi-location merchant

```text
L1 Swansea
L2 Cardiff
```

Each has stable identity/revisions.

Business Hours may independently reference L1 and L2 without tenant duplication.

**PASS**

---

## 50. Falsification — concurrent edits

Controller changes description while authorised staff adds telephone.

No global profile-version conflict is required.

**PASS**

Two actors modify L1 revision 7 concurrently.

At most one succeeds against revision 7.

**PASS**

---

## 51. Falsification — branch moves premises

L1 at Swansea High Street is actively referenced.

Merchant moves branch to another physical address.

System creates L2; applicable configuration/bindings move deliberately; L1 can then retire.

Historical L1 meaning is preserved.

**PASS**

---

## 52. Falsification — typo correction

Merchant corrects L1 postcode without changing the actual physical place.

New L1 revision is valid.

New MerchantLocation identity is unnecessary.

**PASS**

---

## 53. Falsification — retirement with active binding

Active Appointment configuration points to L1.

Retire L1 requested.

Result:

```text
LOCATION_REFERENCE_CONFLICT
```

until applicable configuration evolves.

**PASS**

---

## 54. Falsification — retirement with historical appointments only

No new-activity configuration references L1.

Old appointments preserve the historical location meaning they require.

L1 may retire.

**PASS**

---

## 55. Falsification — stale onboarding

Onboarding confirms phone `111`.

Merchant directly updates authoritative phone to `222` before delayed adoption.

Bootstrap observes revision mismatch and does not overwrite `222`.

**PASS**

---

## 56. Falsification — lost onboarding acknowledgement

Profile adoption commits but response is lost.

Retry of the same source case/revision returns the already-applied logical result.

No duplicate location/contact appears.

**PASS**

---

## 57. Falsification — AI description

AI proposes an inaccurate qualification.

Merchant does not approve it.

It never becomes authoritative public description.

**PASS**

---

## 58. Falsification — Google disagreement

Main Street and Google contain different telephone/address values.

No silent overwrite occurs.

**PASS**

---

## 59. Falsification — Google verification

Google profile is not verified.

Main Street brochure storefront/profile remains independently valid unless an exact Main Street operation requires that claim.

**PASS**

---

## 60. Falsification — account suspension

Profile state remains durable.

Ordinary mutation is denied only according to the actual merchant-wide restriction.

Existing history is not erased.

**PASS**

---

## 61. Rejected alternatives

### A. One giant `BusinessProfile` aggregate

**Rejected.**

Would recreate exactly the god object prohibited by MS-PROT-051.

### B. One global profile revision

**Rejected.**

Creates false concurrency between unrelated facts.

### C. Mutable location identified by address

**Rejected.**

Moving address would silently change the identity referenced elsewhere.

### D. Hard-delete Merchant Locations

**Rejected.**

Breaks historical references and auditability.

### E. Never permit location retirement while any historical reference exists

**Rejected.**

Would make operational location records immortal unnecessarily.

### F. Onboarding directly owns profile facts

**Rejected.**

Violates Target-4 ownership separation.

### G. Initial Configuration Intent owns Profile

**Rejected.**

Configuration intent is not Profile authority.

### H. Delayed onboarding always overwrites profile

**Rejected.**

Can destroy newer merchant-authoritative changes.

### I. Google/external provider as Main Street source of truth by default

**Rejected.**

Authorities remain independent.

### J. Address/location presence implies public exposure

**Rejected.**

Exposure is separately governed.

### K. Every profile edit recompiles RCP

**Rejected.**

Violates the established profile/configuration lifecycle distinction.

---

## 62. Hard invariants

```text
Business Profile UI ≠ BusinessProfile aggregate.

One authoritative owner exists for each profile fact family.

Profile collection facts have stable merchant-scoped identity.

Material profile mutations create exact revisions.

Unrelated fact mutations do not require one global profile revision.

Stale concurrent mutation fails rather than silently overwrites.

Externally retryable mutation is idempotent.

MerchantLocation identity ≠ address.

Same-place correction may revise one location identity.

Material relocation creates a new MerchantLocation identity.

MerchantLocation retirement preserves history.

Retired locations cannot receive new ordinary bindings.

Active executable references prevent unsafe retirement.

Historical references do not necessarily require permanent ACTIVE status.

Profile facts do not automatically become public.

Profile facts do not automatically become Merchant Configuration.

Profile facts affect executable semantics only through explicit binding.

In-progress onboarding profile data remains candidate evidence.

Submitted onboarding may be adopted only through Profile-owned mutations.

Onboarding bootstrap cannot overwrite a newer authoritative Profile revision.

Initial Configuration Intent does not become Profile authority.

AI proposal ≠ approved Profile fact.

External provider value ≠ Main Street profile authority.

External verification ≠ universal Main Street eligibility.

Profile mutation commit ≠ storefront publication commit.

Semantic retirement ≠ physical data deletion.
```

---

## 63. Deferred question catalogue

The following stable identifiers remain deferred and inactive:

| ID | Deferred question | Future owner / classification | Revisit trigger |
|---|---|---|---|
| `MS-PROT-051-V11-DQ-001` | Exact PostgreSQL table/index layout for descriptor/contact/location/service-area/external-presence revisions | Persistence implementation | During production Profile persistence implementation |
| `MS-PROT-051-V11-DQ-002` | Exact structured international postal-address representation | Profile/location implementation architecture | Before production Merchant Location storage is finalised |
| `MS-PROT-051-V11-DQ-003` | Exact geocoding/map provider and coordinate-normalisation process | Provider/location implementation | When coordinate/geocoding support is introduced |
| `MS-PROT-051-V11-DQ-004` | Exact UI/API workflow distinguishing same-place correction from relocation | Target 20 / presentation implementation | During Production API and merchant UX design |
| `MS-PROT-051-V11-DQ-005` | Exact Profile projection contracts, freshness and rebuild evidence | Target 7 | During Projection Contracts |
| `MS-PROT-051-V11-DQ-006` | Exact runtime Profile/Location exposure-policy representation and resolver | Target 8 | During Exposure Resolution |
| `MS-PROT-051-V11-DQ-007` | Exact registered profile-management privilege identifiers/default Role templates | Access implementation | Before delegated staff Profile administration is exposed |
| `MS-PROT-051-V11-DQ-008` | Exact durable mechanism for submitted-Onboarding → Profile bootstrap consequence | Target 18 / orchestration implementation | During Events/background-process completion |
| `MS-PROT-051-V11-DQ-009` | Exact Profile/Location REST/transport contracts | Target 20 | During Production APIs |
| `MS-PROT-051-V11-DQ-010` | Historical Profile/Location revision retention periods | Target 17 | During Data Protection Lifecycle |
| `MS-PROT-051-V11-DQ-011` | Exact external profile-sync conflict policy/provider adapters | Provider/integration architecture | When two-way external profile synchronisation is introduced |
| `MS-PROT-051-V11-DQ-012` | Exact merchant classification taxonomy/governance | Product/discovery metadata | When classification must become standardised across production features |
| `MS-PROT-051-V11-DQ-013` | Bulk multi-location editing/copy semantics | Application/UX implementation | When merchant scale demonstrates need |
| `MS-PROT-051-V11-DQ-014` | Future reactivation of a RETIRED location identity | Semantic candidate | Only if production evidence shows new identity on reopening causes material operational harm |

`MS-PROT-051-V11-DQ-014` is deliberate: the initial safe rule is **RETIRED is terminal**. Main Street shall not introduce location reactivation until evidence shows its benefits outweigh stale-reference ambiguity.

Traceability rules:

1. The `MS-PROT-051-V11-DQ-*` identifier MUST remain stable once used in governance or implementation evidence.
2. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation details proceed under `IMPLEMENTATION-RULES.md` where appropriate.
3. Resolution MUST record the resolving accepted authority or implementation evidence.
4. No deferred item may silently weaken the accepted fact-identity, revision, retirement, onboarding-conflict or ownership boundaries.

---

## 64. Target-5 conformance gate

Target 5 is Design-Closed because the accepted corpus now guarantees:

```text
Business Profile remains composition, not god aggregate
production owners of profile fact families are explicit
merchant-scoped fact identities are stable
MerchantLocation identity is independent of address
material mutations have revision provenance
same-fact concurrency fails safely
unrelated fact edits do not require global profile lock
mutations are idempotent
location correction and relocation are distinct
retirement semantics are explicit
active references block unsafe retirement
historical meaning survives Profile changes
retired locations cannot accept new bindings
Controller/staff mutation authority composes with accepted IAM
onboarding evidence remains non-authoritative while IN_PROGRESS
submitted onboarding adopts profile facts through owner operations
stale onboarding cannot overwrite newer profile state
AI output still requires human approval
external imports remain proposals/evidence
Google verification remains separate
exposure remains separate
profile mutation does not equal publication
ordinary profile edit does not imply RCP recompilation
exact configuration bindings retain their own authority
semantic retirement remains separate from data erasure
all narrower questions have stable traceability IDs
```

---

## 65. Amendment effect

MS-PROT-051 v1.1:

1. establishes the production Profile/Presence authority boundary;
2. rejects a universal `BusinessProfile` aggregate/version;
3. requires stable merchant-scoped identities for collection facts;
4. requires exact profile/location revision semantics;
5. establishes safe optimistic concurrency;
6. requires mutation idempotency;
7. establishes `ACTIVE → RETIRED` Merchant Location lifecycle;
8. makes `RETIRED` terminal initially;
9. distinguishes address correction from real relocation;
10. requires a new identity for materially relocated merchant presence;
11. preserves historical location meaning;
12. prohibits new bindings to retired locations;
13. formalises Controller/delegated-staff profile mutation composition;
14. establishes the exact Target-4 onboarding → Profile ownership handoff;
15. prevents stale onboarding from overwriting newer authoritative facts;
16. preserves Profile/Configuration separation;
17. preserves Profile/Exposure/Projection separation;
18. preserves Main Street/external-provider authority separation;
19. catalogues `MS-PROT-051-V11-DQ-001` through `MS-PROT-051-V11-DQ-014`;
20. closes **MS-PROT-079 Target 5 — Merchant Profile / Location**; and
21. activates **Target 6 — AI inference boundary**.

---

## 66. Acceptance statement

The governing production principle is:

> **Merchant Profile is one coherent management experience over several independently authoritative, revisioned merchant-presence facts. Profile changes are explicit, idempotent and concurrency-safe; Merchant Location identity survives ordinary corrections but not a material relocation; historical meaning is never rewritten; onboarding can supply reviewed candidates but cannot overwrite newer authority; and Profile ownership remains separate from Configuration, Exposure, projections, trust and external providers.**

Canonical boundary:

```text
Merchant / reviewed onboarding evidence
        ↓
Profile-owned mutation contract
        ↓
revisioned authoritative presence fact
        ↓
projections
        ↓
Exposure
        ↓
storefront / other audiences
```

**Review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Manual approval:** GRANTED — 27 August 2026  
**Governance verdict:** **ACCEPTED**
