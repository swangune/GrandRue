# MS-PROT-044 v1.5 — Listing Lifecycle, Immutable Revision & Merchant Authoring Access Amendment

**Document ID:** MS-PROT-044  
**Version:** 1.5  
**Status:** ACCEPTED  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after complete pre-approval presentation, Fundamental Vision Conformance, review, falsification and ambiguity review  
**Authority type:** Listing lifecycle, revision and merchant-definition access amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-044 through v1.4 and composite MS-PROT-045 through v1.1 within Listing authoritative creation, immutable revision, lifecycle and merchant-access scope  
**Resolves in part:** MS-PROT-044 v1.0 retained Listing lifecycle, revision/history and merchant-authoring gaps  
**Preserves:** Listing/subject separation; Offering/Product distinction; Merchant Subject Category separation; Listing Transaction Mode authority; Projection/Exposure separation; Enquiry subject/context authority; capability-owned schemas; Payment, Booking, Appointment, Ordering and Inventory authority; merchant authority; business-type neutrality  
**Depends on:** Composite MS-PROT-020; composite MS-PROT-021; composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-035; composite MS-PROT-037; composite MS-PROT-040; composite MS-PROT-043; composite MS-PROT-044 through v1.4; composite MS-PROT-045 through v1.1; composite MS-PROT-053; composite MS-PROT-056; composite MS-PROT-057; composite MS-PROT-059; composite MS-PROT-062; composite MS-PROT-069; MS-PROT-094; applicable Actor Authorisation, Projection, Exposure, schema and subject-owner authorities  
**Commercial effect:** NONE — exact Listing commercial classification remains a later Commercial decision  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

---

# Authority Identity Preflight

```text
target branch
    development

review basis HEAD
    010e7eb66ddb2102dba499b1e5e2acdc88c28a57

current MS-PROT-044 composition
    base + v1.1 + v1.2 + v1.3 + v1.4

MS-PROT-044 v1.5
    unoccupied

candidate
    MS-PROT-044 v1.5
```

Latest-head and identifier availability MUST be revalidated immediately before any post-approval formalisation.

---

# 0. Fundamental Vision Conformance

GrandRue already recognises that a Listing is appropriate where a merchant manages a distinct proposition concerning an identifiable subject.

Examples include:

```text
Property for rent
Property for sale
Vehicle advertised for sale
other independently managed
subject-specific propositions
```

The unresolved problem is operationally important:

> How does an ordinary merchant create, update and stop using that Listing without needing to understand GrandRue's semantic architecture?

The merchant should be able to perform business-language operations such as:

```text
Add listing
Change price
Update description
Take listing down
```

GrandRue must internally preserve:

```text
stable identity
subject affinity
immutable history
concurrency
retry safety
Exposure separation
historical customer context
```

The complexity is therefore internal rather than merchant-facing.

**Vision result:**

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

---

# 1. Governing Decision

A Listing SHALL consist conceptually of:

```text
Listing
    stable identity
    merchant scope
    immutable primary subject affinity

        ↓ current revision

ListingRevision
    immutable Listing-owned proposition truth

        +

Listing lifecycle
    ACTIVE
    WITHDRAWN
```

The authoritative distinction is:

```text
Listing identity
    ≠ ListingRevision

Listing lifecycle
    ≠ public Exposure

Listing
    ≠ underlying subject
```

---

# 2. Listing Identity

A Listing has one stable merchant-scoped identity.

That identity persists across material revisions.

Example:

```text
Listing L200

Revision 1
    rent £1,200

Revision 2
    rent £1,250

Revision 3
    revised marketing description
```

All remain:

```text
Listing L200
```

until the Listing is withdrawn.

---

# 3. Exactly One Primary Subject

Every Listing SHALL retain exactly one primary subject, preserving the existing MS-PROT-044 invariant.

Example:

```text
Listing L200
    SUBJECT → Property P100
```

or:

```text
Listing L300
    SUBJECT → Vehicle V50
```

The primary subject relationship is immutable for the lifetime of that Listing.

A Listing MUST NOT be retargeted from:

```text
Property P100
```

to:

```text
Property P101
```

through revision.

If the merchant selected the wrong underlying subject and the Listing has already become authoritative, the conforming correction is:

```text
withdraw incorrect Listing
        ↓
create another Listing
        ↓
bind correct primary subject
```

This preserves historical interpretation.

---

# 4. ListingRevision

A **ListingRevision** is an immutable representation of the exact Listing-owned proposition truth established at one point in time.

It contains at least:

```text
listing identity
revision identity
schema/version affinity
predecessor revision identity where applicable
authoritative establishment instant
Listing-owned registered field values
```

It does not own the underlying subject's fields.

Example:

```text
Property owns
    address
    bedrooms
    bathrooms

ListingRevision owns
    transaction mode
    asking rent / asking price
    marketing description
    other registered Listing-owned values
```

---

# 5. Current Revision

Every ACTIVE Listing SHALL resolve to exactly one current ListingRevision.

Canonical:

```text
Listing L200
    ↓ current
Revision R3
```

Previous revisions remain immutable historical evidence.

A successful material Listing revision atomically:

```text
creates new immutable revision
        +
moves current pointer
```

It MUST NOT mutate the previous revision.

---

# 6. Listing Schema

ListingRevision values SHALL conform to an accepted registered Listing schema under composite MS-PROT-045.

This amendment does not establish one universal fixed Listing field set.

Therefore a Listing context may register fields such as:

```text
transaction-mode
asking-price
asking-rent
marketing-description
```

where semantically applicable.

GrandRue MUST NOT permit arbitrary merchant-defined executable Listing fields.

---

# 7. Listing Transaction Mode

Where the applicable Listing schema registers:

```text
ListingTransactionMode
```

the accepted initial values remain:

```text
SALE
RENT
```

under MS-PROT-044 v1.4.

Transaction Mode is part of the Listing proposition.

It is not:

```text
Category
Property state
Order authority
Booking authority
tenancy authority
```

---

# 8. Transaction Mode Is Identity-Significant

For the initial model, Listing Transaction Mode SHALL be immutable within one Listing lineage.

Therefore:

```text
Listing L1
    transactionMode = RENT
```

MUST NOT later become:

```text
Listing L1
    transactionMode = SALE
```

through ordinary revision.

Changing the fundamental proposition from rent to sale requires:

```text
withdraw Listing L1
        ↓
create Listing L2
        ↓
same underlying Property may remain
```

This preserves the accepted distinction:

```text
Property
    ≠ Listing
```

and the historical meaning of enquiries and public observations made against the former Listing.

---

# 9. Lifecycle

The initial Listing lifecycle contains exactly:

```text
ACTIVE
WITHDRAWN
```

No other universal state is admitted by this amendment.

In particular:

```text
DRAFT
PUBLISHED
AVAILABLE
UNDER_OFFER
SOLD
LET
EXPIRED
ARCHIVED
```

are NOT established as generic Listing lifecycle values.

---

# 10. ACTIVE

`ACTIVE` means:

> The Listing remains the merchant's current authoritative proposition concerning its primary subject.

ACTIVE does not mean:

```text
publicly visible
currently available for every operation
customer may transact
inventory available
property unsold
room vacant
vehicle available
website published
```

Those meanings remain separately governed.

Therefore:

```text
ACTIVE
    ≠ EXPOSED
    ≠ AVAILABLE
```

---

# 11. WITHDRAWN

`WITHDRAWN` means:

> The merchant has ended this Listing as a current proposition for new Listing-based customer/public interaction.

WITHDRAWN is terminal for the initial model.

A withdrawn Listing:

```text
retains identity
retains revisions
retains subject relationship
retains historical Enquiry relationships
retains historical provenance
```

but cannot receive new ordinary Listing revisions.

---

# 12. No Reactivation

A withdrawn Listing SHALL NOT be reactivated in the initial model.

If the merchant later wishes to advertise the same underlying subject again:

```text
Property P100
        ↓
new Listing L300
```

is created.

This provides clear historical boundaries between propositions.

Example:

```text
2026
Listing L200
    RENT
    £1,200/month
    WITHDRAWN

2027
Listing L300
    RENT
    £1,400/month
    ACTIVE
```

The Property remains the same subject.

The Listings remain distinct propositions.

---

# 13. Preparation Before Listing Creation

A merchant MAY prepare information before an authoritative Listing exists.

Preparation may include:

```text
form editing
AI interpretation
validation
media selection
category selection
price candidate
description candidate
```

Preparation:

```text
≠ Listing
≠ ListingRevision
≠ ACTIVE Listing
```

GrandRue MUST NOT create authoritative Listing identity merely because the merchant starts completing a form.

Exact durable UI-draft storage remains an implementation/presentation matter unless later evidence requires independent semantic authority.

---

# 14. Create Listing

The authoritative Listing creation operation conceptually requires:

```text
logical request identity
Merchant Scope
exact primary subject identity/type
exact applicable Listing schema/version
complete initial Listing-owned values
required by that schema
trusted execution context
applicable actor authority
```

Success atomically establishes:

```text
stable Listing identity
+
immutable primary subject relationship
+
initial immutable ListingRevision
+
current revision pointer
+
lifecycle = ACTIVE
+
logical request/result affinity
```

No partially authoritative Listing may result.

---

# 15. Creation Does Not Publish

Creating an ACTIVE Listing does NOT automatically:

```text
publish it on the website
make it public
add it to search
enable Enquiry
send notifications
create an Order
create a Booking
```

Canonical:

```text
Create Listing
        ↓
authoritative Listing exists

then independently:

Projection
Exposure
Storefront composition
customer interaction participation
```

This preserves the existing publication/exposure boundary.

---

# 16. Creation Does Not Require Public Exposure

A merchant may legitimately create and maintain an ACTIVE Listing that is not currently public.

Examples:

```text
preparing marketing internally
temporarily withholding public exposure
sharing through a separately governed channel
merchant-only management
```

No fake `DRAFT` lifecycle state is required merely to represent:

```text
ACTIVE Listing
+
NOT EXPOSED
```

---

# 17. Revise Listing

Material change to Listing-owned proposition truth SHALL establish another immutable ListingRevision.

Examples include:

```text
change asking price
change asking rent
change marketing description
change another registered
Listing-owned mutable field
```

The operation SHALL require:

```text
Listing is ACTIVE
expected current revision identity
logical request identity
applicable Actor Authorisation
valid registered field semantics
all applicable business validation
```

Success atomically establishes:

```text
new immutable ListingRevision
+
new current revision pointer
```

---

# 18. Revision Is Field-Bounded

Composite MS-PROT-045 remains authoritative.

Listing mutation MUST NOT become:

```text
PATCH /listing/{id}
with arbitrary JSON
```

as semantic authority.

The owning Listing operation must have registered mutation authority over the affected Listing fields.

Implementation MAY provide a convenient merchant editor while resolving changes to bounded owner operations internally.

---

# 19. Subject Mutation Is Prohibited

Listing revision SHALL NOT directly mutate its underlying subject.

Example:

```text
Listing L200
    SUBJECT → Property P100
```

Changing:

```text
Property.address
Property.bedrooms
```

requires Property-owned authority.

Listing revision may then project the updated subject information where appropriate.

Hard invariant:

```text
Listing editor
    ≠ universal Property editor
```

---

# 20. Category Is Separate

Merchant Subject Category Assignment remains independent under MS-PROT-044 v1.4.

A merchant-facing save flow MAY appear to perform:

```text
Create Listing
+
assign Category "Student Properties"
```

in one coherent interface.

Internally:

```text
Listing creation
    ≠ Category Assignment
```

Category Assignment failure MUST NOT corrupt or redefine the valid Listing.

It may be retried independently.

---

# 21. Category Change Does Not Create Listing Revision

Changing:

```text
Student Properties
        ↓
Featured Homes
```

category assignment does not modify ListingRevision.

Category is separate organisational truth.

Therefore no new ListingRevision is required solely because Category membership changes.

---

# 22. Withdraw Listing

Withdrawal SHALL require at least:

```text
Listing identity
expected ACTIVE lifecycle/currentness evidence
logical request identity
trusted execution context
applicable Actor Authorisation
```

Success atomically establishes:

```text
Listing lifecycle
    ACTIVE → WITHDRAWN
+
immutable withdrawal evidence
+
logical request/result affinity
```

The current ListingRevision remains preserved as the final revision of that Listing.

---

# 23. Withdrawal Does Not Delete

Listing withdrawal MUST NOT delete:

```text
underlying subject
Listing revisions
Category history
Enquiries
CustomerContext relationships
Quotation history
Invoice history
Payment truth
analytics evidence
audit evidence
```

Data lifecycle remains governed separately.

---

# 24. Withdrawal and Exposure

Exposure remains independently owned.

However a WITHDRAWN Listing is no longer an eligible current proposition for new public Listing exposure.

Therefore owner-qualified Exposure evaluation MUST fail closed for new ordinary public exposure of a withdrawn Listing.

This does not mean:

```text
Listing lifecycle
    = Exposure
```

Rather:

```text
source lifecycle
    contributes eligibility

Exposure
    remains final audience-visibility authority
```

An ACTIVE Listing can still be unexposed.

A WITHDRAWN Listing cannot be newly presented as a current active public proposition.

---

# 25. Temporary Website Hiding Is Not Withdrawal

If the merchant merely wants to remove a Listing temporarily from the website while keeping the proposition current:

```text
change Exposure
```

rather than:

```text
Withdraw Listing
```

This distinction prevents merchants from destroying Listing continuity merely to hide a web page.

Merchant-facing software SHOULD communicate the difference in ordinary language.

For example:

```text
Hide from website

vs

End listing
```

rather than exposing internal terms unnecessarily.

---

# 26. Historical Enquiry Affinity

Where a customer initiated an Enquiry against:

```text
Listing L200
```

that relationship remains:

```text
Enquiry SUBJECT → Listing L200
```

after later:

```text
Listing revision
or
Listing withdrawal
```

The Enquiry's submission-time contextual evidence MUST remain historically interpretable under the applicable Enquiry/Data Protection authority.

A later Listing price change MUST NOT rewrite what the customer saw or responded to earlier.

---

# 27. Revision Context for Customer History

Where existing authority requires preservation of materially relevant Listing context at customer interaction time, GrandRue SHALL retain enough provenance to identify the applicable Listing representation/revision evidence.

This does not transfer Listing ownership to:

```text
Enquiry
CustomerContext
Storefront
```

Those consumers retain references/projections only.

---

# 28. Asking Price / Rent Changes

Example:

```text
Listing L200
    RENT

R1
    £1,200/month

R2
    £1,250/month
```

R2 becoming current does not rewrite:

```text
earlier Enquiry context
earlier Quotation
earlier Invoice
earlier customer commitment
```

where those independently retained their own commercial truth.

---

# 29. Listing Transaction Mode Change

A material change:

```text
RENT → SALE
```

is prohibited as ordinary Listing revision.

Required:

```text
withdraw RENT Listing
        ↓
create SALE Listing
```

Likewise:

```text
SALE → RENT
```

creates another Listing identity.

This preserves proposition identity and avoids ambiguous history.

---

# 30. Subject Reuse Across Listings

The same subject MAY participate in multiple Listings over time.

Example:

```text
Property P100
    ├── Listing L100
    │      RENT
    │      WITHDRAWN
    │
    └── Listing L200
           SALE
           ACTIVE
```

Whether simultaneous active Listings over one subject are permitted remains governed separately.

This amendment does NOT resolve that retained policy question.

---

# 31. Listing Merchant Observation Access

This amendment establishes:

```text
listing/merchant-definition-observation-access@1
```

It qualifies otherwise-authorised merchant observation of:

```text
Listing identity
current lifecycle
current revision
retained Listing revisions
primary subject reference
authorised mutation/withdrawal result evidence
```

within applicable retention/access rules.

This contract does not itself grant Actor Authorisation or Commercial Entitlement.

---

# 32. Listing Merchant Authoring Access

This amendment establishes:

```text
listing/merchant-definition-authoring-access@1
```

It qualifies otherwise-valid:

```text
Listing creation
material ListingRevision establishment
```

It does not qualify:

```text
underlying subject mutation
Category Assignment
Exposure mutation
customer commitment
Payment
```

---

# 33. Listing Withdrawal Access

This amendment establishes:

```text
listing/merchant-withdrawal-access@1
```

for otherwise-valid terminal Listing withdrawal.

Withdrawal remains distinct from ordinary authoring because:

```text
authoring
    establishes/changes current proposition truth

withdrawal
    terminates current proposition lifecycle
```

---

# 34. Commercial Classification Deferred

This amendment makes no tier-placement decision for:

```text
listing/merchant-definition-authoring-access@1
```

or:

```text
listing/merchant-withdrawal-access@1
```

Concrete commercial classification belongs to the Commercial catalogue follow-on work.

Until then:

```text
Listing access contract exists
    ≠ runtime Commercial permission inferred
```

The merchant-observation contract likewise receives no commercial classification from this authority.

---

# 35. Actor Authority

For the initial semantic authority, Listing creation, revision and withdrawal require an actor independently authorised to maintain the merchant's Listing definition.

This amendment does not create a generic staff privilege.

Merchant Controller authority remains sufficient where applicable existing account/Controller authority permits the operation.

Future delegated Listing privileges may be admitted separately.

---

# 36. AI Assistance

AI MAY assist a merchant in creating or editing a Listing.

Example:

```text
"2-bedroom flat to rent in Swansea
for £1,200 a month"
```

AI may propose:

```text
subject candidate
    Property P100 or new subject candidate

transaction mode
    RENT

asking rent
    £1,200/month

description candidate
```

But existing merchant-intent rules remain binding.

If the merchant explicitly says:

```text
"List this flat for rent at £1,200 a month."
```

GrandRue does not need to ask whether they intended a rental Listing.

If the merchant says:

```text
"Put this property on my website."
```

and it is unclear whether they mean:

```text
information only

or

a market Listing
```

GrandRue MUST confirm that material interpretation before promoting it to Listing intent.

---

# 37. AI Cannot Commit Listing Authority

AI MUST NOT independently:

```text
create authoritative Listing
change transaction mode
revise price
withdraw Listing
change primary subject
publish Listing
grant customer-operation authority
```

AI produces candidates.

The authoritative operation still requires:

```text
confirmed/explicit merchant intent
+
registered semantic validation
+
Actor Authorisation
+
other applicable access predicates
```

---

# 38. Ordinary Deterministic Path

AI is optional.

The merchant MUST be able to perform the ordinary Listing workflow deterministically through business-facing controls.

Example:

```text
Add listing

Choose subject

For sale / To rent

Price / rent

Description

Category

Save
```

No AI dependency is permitted for ordinary Listing administration.

---

# 39. Mobile-First Merchant Experience

Ordinary Listing operations SHALL be feasible from a phone.

The design MUST NOT require:

```text
desktop-only tables
wide multi-pane editors
manual semantic identifiers
schema knowledge
capability graph knowledge
```

High-frequency merchant actions SHOULD present business language such as:

```text
Add listing
Edit listing
Hide from website
End listing
```

rather than internal semantic terminology.

---

# 40. Listing and Customer Action

An ACTIVE Listing does not itself create a customer executable operation.

For example:

```text
Property Listing
    RENT
```

may support:

```text
Enquire
Request viewing
```

only where those independent capabilities/participation contracts apply.

Listing authoring MUST NOT silently enable:

```text
Order
Booking
Appointment
Payment
Quotation
```

---

# 41. Realtor Example

Merchant:

```text
Property P100
2-bedroom flat
Swansea
```

creates:

```text
Listing L200

SUBJECT → P100
transactionMode = RENT
askingRent = £1,200/month
description = ...
lifecycle = ACTIVE
```

Category may separately be:

```text
Student Properties
```

Exposure may separately be:

```text
PUBLIC
```

Customer interaction may independently expose:

```text
Enquire
Arrange viewing
```

The model does not collapse these responsibilities.

---

# 42. Vehicle Dealer Example

Subject:

```text
Vehicle V100
```

Listing:

```text
Listing L300
SUBJECT → V100
transactionMode = SALE
askingPrice = £12,500
lifecycle = ACTIVE
```

Vehicle condition, mileage or fuel type remain subject/schema-owned where applicable.

They need not become Listing fields merely because the customer page displays them.

---

# 43. Consultant Falsification

Consultant offers:

```text
60-minute consultation
```

No independently meaningful published proposition concerning another durable subject exists.

Correct:

```text
Offering
```

not Listing.

**PASS**

---

# 44. Grocery Falsification

Grocery merchant sells:

```text
Milk
Bread
Rice
```

Normal Product/Offering presentation requires no Listing.

**PASS**

---

# 45. Realtor Falsification

Property remains after Listing withdrawal.

Listing proposition has independent:

```text
transaction mode
asking value
description
history
```

Separate Listing remains justified.

**PASS**

---

# 46. Same Property Relisted

```text
L1 RENT
    withdrawn

L2 RENT
    later

or

L3 SALE
```

Property identity remains stable.

Each Listing has clean historical meaning.

**PASS**

---

# 47. Hidden Listing

ACTIVE Listing is temporarily hidden from the website.

Expected:

```text
Listing remains ACTIVE
Exposure changes
```

No lifecycle change required.

**PASS**

---

# 48. End Listing

Merchant chooses:

```text
End listing
```

Expected:

```text
ACTIVE → WITHDRAWN
```

Website/current public eligibility ends through source-lifecycle composition.

History remains.

**PASS**

---

# 49. Price Revision Race

Two authorised edits both expect revision R4.

One commits R5.

The second must fail current-revision validation.

It MUST NOT silently overwrite R5.

**PASS**

---

# 50. Lost Acknowledgement

Create Listing or Revise Listing commits successfully but response is lost.

Retry with:

```text
same logical request identity
+
same input
```

returns the committed result.

It MUST NOT create:

```text
second Listing
or
second revision
```

**PASS**

---

# 51. Logical Request Conflict

A committed request identity reused with materially different input is rejected.

Example:

```text
same request identity

first:
rent £1,200

retry:
rent £1,400
```

must fail as idempotency conflict.

**PASS**

---

# 52. Withdrawal Race

Listing is withdrawn while another revision request still expects its former ACTIVE/current state.

The stale revision MUST fail.

No post-withdrawal revision may become current.

**PASS**

---

# 53. Category Failure

Listing creation succeeds.

Subsequent Category Assignment fails.

Expected:

```text
Listing remains valid

Category assignment may retry
```

No rollback of authoritative Listing truth is required merely for optional organisation.

**PASS**

---

# 54. Subject Change Attempt

Merchant attempts to edit:

```text
SUBJECT P100 → P101
```

Expected:

rejected as ordinary Listing revision.

The system may guide:

```text
End listing
Create new listing for P101
```

**PASS**

---

# 55. RENT to SALE Attempt

Merchant changes:

```text
To rent
        ↓
For sale
```

Expected semantic effect:

```text
withdraw existing Listing
+
create another Listing
```

not revision of transaction mode.

The UI may make this easy but must preserve the semantic boundary.

**PASS**

---

# 56. Listing Revision and Exposure

Merchant revises asking rent while Listing is public.

Expected:

```text
new ListingRevision
```

Exposure does not become the mutation owner.

Read projections subsequently resolve the current authorised revision.

**PASS**

---

# 57. Existing Enquiry After Withdrawal

Customer previously enquired about Listing L200.

Merchant withdraws L200.

Expected:

```text
Enquiry SUBJECT → L200
```

remains historically valid.

The Enquiry is not deleted or retargeted.

**PASS**

---

# 58. Rejected Alternative — Mutable Listing Row

Rejected.

Overwriting current Listing data in place destroys historical proposition meaning.

Listing revision must be immutable.

---

# 59. Rejected Alternative — PUBLISHED Lifecycle State

Rejected.

Publication/Exposure is independently governed.

A Listing can be:

```text
ACTIVE
+
not exposed
```

without semantic contradiction.

---

# 60. Rejected Alternative — AVAILABLE Lifecycle State

Rejected.

Availability belongs to the applicable subject/capability context.

A Listing being ACTIVE does not prove operational availability.

---

# 61. Rejected Alternative — SOLD / LET Generic States

Rejected.

Those words imply transaction/outcome semantics that cannot be universally owned by Listing.

The applicable transaction capability must establish the business fact.

Listing may subsequently be withdrawn.

---

# 62. Rejected Alternative — DRAFT Listing State

Rejected for the initial authoritative lifecycle.

Merchant preparation can occur before authoritative Listing creation.

Introducing DRAFT merely because a UI has an incomplete form would make presentation/workflow state into domain lifecycle.

If future durable collaborative draft semantics become materially necessary, they require separate admission.

---

# 63. Rejected Alternative — Reactivate Withdrawn Listing

Rejected initially.

A new Listing identity provides cleaner history and avoids silently reviving old proposition context.

---

# 64. Rejected Alternative — Retarget Listing

Rejected.

A Listing proposition is about one primary subject.

Changing that subject changes the identity of the proposition materially enough to require a new Listing.

---

# 65. Rejected Alternative — Generic Listing PATCH

Rejected.

MS-PROT-045 field-targeted mutation remains authoritative.

Transport convenience must not become unrestricted semantic mutation.

---

# 66. Hard Invariants

1. Listing has stable merchant-scoped identity.
2. Listing has exactly one immutable primary subject.
3. ListingRevision is immutable.
4. Every ACTIVE Listing has exactly one current revision.
5. Historical revisions remain interpretable.
6. Material Listing-owned changes create another revision.
7. Underlying subject fields are not Listing-owned merely because displayed.
8. Initial lifecycle is exactly ACTIVE and WITHDRAWN.
9. WITHDRAWN is terminal.
10. Reactivation is not admitted.
11. Relisting creates a new Listing identity.
12. ACTIVE does not mean public.
13. ACTIVE does not mean operationally available.
14. WITHDRAWN is ineligible for new ordinary public Listing exposure.
15. Temporary website hiding uses Exposure, not Listing withdrawal.
16. Listing creation does not publish.
17. Listing creation does not create customer transaction authority.
18. Listing revision does not mutate primary subject.
19. Category Assignment remains separate from ListingRevision.
20. Category changes do not create ListingRevision.
21. Listing Transaction Mode remains Listing-owned.
22. SALE↔RENT change requires another Listing identity in the initial model.
23. Listing withdrawal does not delete subject or history.
24. Historical Enquiry linkage survives revision and withdrawal.
25. Current revision changes do not rewrite historical customer/commercial truth.
26. Creation/revision/withdrawal require idempotent logical request handling.
27. Concurrent stale revision attempts fail rather than overwrite.
28. AI may prepare candidates but does not obtain Listing mutation authority.
29. Ordinary Listing management remains usable without AI.
30. Merchant-facing interaction remains business-language and mobile-first.
31. Listing access contracts do not themselves grant Actor Authorisation.
32. Listing commercial classification is not decided here.
33. No generic Product/Offering is converted into Listing merely for presentation.
34. No implementation is activated.

---

# 67. Deferred Decisions Preserved

This amendment does not resolve:

```text
complete Property schema
all domain-specific Listing schemas
simultaneous active Listings over one subject
property sale transaction semantics
property tenancy / lease semantics
vehicle ownership/title semantics
unit-level merchandise condition
Listing-specific media ownership details
full Listing commercial classification
delegated staff Listing privileges
public Listing layout
search ranking
SEO policy
cross-merchant Listing discovery
marketplace behaviour
exact transport routes / DTOs
UI component design
durable collaborative draft semantics
```

These remain separately governed.

---

# 68. Commercial Follow-On

Acceptance makes the Listing slice ready for a later exact commercial classification.

The next Commercial design may evaluate:

```text
listing/merchant-definition-observation-access@1
listing/merchant-definition-authoring-access@1
listing/merchant-withdrawal-access@1
```

against the accepted FREE/BUSINESS/GROWTH proposition.

No runtime tier grant may be inferred before that classification is accepted.

---

# 69. Merchant-Journey Consequence

After this authority and its later commercial classification, GrandRue has enough accepted semantics to design the actual merchant-facing creation journey without asking merchants to choose internal architecture.

Examples:

```text
Add product
Add service
Add listing
```

can map to the already-governed:

```text
Offering
Product
Listing
Category
Condition
Transaction Mode
```

while preserving AI-assistance and authority boundaries.

That downstream UX/orchestration work should not invent new domain meaning.

---

# 70. Implementation Activation

**NONE**

Acceptance does not authorise:

```text
Listing database tables
ListingRevision persistence
Listing APIs
Listing merchant UI
Listing Storefront components
new Commercial Entitlements
search indexing
AI implementation
Property implementation
```

Implementation remains separately governed.

---

# 71. Governance Recommendation

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`  
**Listing/subject separation:** PASS  
**Revision/history model:** PASS  
**Lifecycle/Exposure separation:** PASS  
**Transaction Mode identity review:** PASS  
**Category separation:** PASS  
**Enquiry historical-context preservation:** PASS  
**AI merchant-intent boundary:** PASS  
**Mobile/business-language review:** PASS  
**Concurrency/idempotency review:** PASS  
**Cross-domain falsification:** PASS  
**Ambiguity review:** PASS

```text
RECOMMENDATION: ACCEPT
```

**Manual approval:** GRANTED — 19 September 2026

---

# 72. Acceptance Statement

> **GrandRue shall represent a Listing as one stable merchant-scoped proposition concerning exactly one immutable primary subject, with immutable Listing revisions and an initial lifecycle of exactly ACTIVE and WITHDRAWN. Material Listing-owned changes create a new current revision rather than mutating history. ACTIVE identifies a current merchant proposition but does not itself establish public Exposure, operational availability or transaction authority; temporary website hiding therefore uses Exposure rather than withdrawal. WITHDRAWN is terminal for the initial model, preserves all Listing and subject history, remains historically referencable by Enquiries and other authorised consumers, and is ineligible for new ordinary public Listing exposure. A change of primary subject or Listing Transaction Mode between SALE and RENT requires a new Listing identity rather than ordinary revision. Listing merchant observation, authoring and withdrawal receive exact owner-qualified access contracts, but their Commercial tier classification remains separately governed. AI may assist with Listing candidates under existing merchant-intent confirmation rules but cannot independently create, revise, retarget, withdraw or publish a Listing.**
