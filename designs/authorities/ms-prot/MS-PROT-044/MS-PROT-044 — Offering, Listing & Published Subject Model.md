# MS-PROT-044 — Offering, Listing & Published Subject Model

**Document ID:** MS-PROT-044  
**Version:** 1.0  
**Status:** **ACCEPTED after falsification and validation**  
**Depends on:** MS-PROT-020 v1.4, MS-PROT-021, MS-PROT-022, MS-PROT-027, MS-PROT-029, MS-PROT-036, MS-PROT-043 v1.2  
**Purpose:** Define the semantic distinction between an `Offering`, a `Listing`, an underlying business subject, and the act of publishing something to a merchant storefront. Prevent Main Street from forcing products, services, properties, scholarship opportunities and informational content into one overloaded `Offering` or `Listing` abstraction.

---

# 1. Governing decision

Main Street shall distinguish:

```text
UNDERLYING BUSINESS SUBJECT

OFFERING

LISTING

PUBLICATION / EXPOSURE

STOREFRONT PROJECTION
```

These concepts may interact, but they are not interchangeable.

The governing model is:

```text
Business subject
      │
      │ may participate in
      ▼
Offering / Listing / Publication semantics
      │
      ▼
Exposure configuration
      │
      ▼
Storefront projection
```

The central rule is:

> **Something being visible on a merchant website does not make it an Offering, and something being offered commercially does not require a separate Listing object.**

---

# 2. Why this distinction is necessary

The same storefront may contain:

```text
Product available for purchase

Consultation service available for Appointment

Property advertised for rent

Scholarship opportunity published for information

Merchant announcement

Customer Booking details
```

All may appear on web pages.

But they do not have the same business semantics.

If Main Street models all visible content as:

```text
Offering
```

then an informational scholarship becomes artificially commercial.

If Main Street models all visible content as:

```text
Listing
```

then every product and service acquires an unnecessary second object purely for presentation.

Both models are rejected.

---

# 3. Storefront visibility is not a domain type

This is a hard invariant.

```text
visible on storefront
```

is an exposure/presentation property.

It is not sufficient to classify something as:

```text
Offering
Listing
Publication
Product
Service
```

Therefore:

> **Presentation shall not determine semantic identity.**

---

# 4. Offering

An **Offering** represents:

> **A merchant-controlled proposition describing something the merchant makes available to a customer for a supported business interaction.**

Examples:

```text
Haircut

Structural consultation

Oil change

Bottle of milk

Room category

Equipment rental option
```

An Offering answers:

> **What can this merchant provide, sell, perform, reserve or otherwise make available through supported business operations?**

---

# 5. Offering is not merely content

A scholarship information page is not necessarily an Offering.

Example:

```text
Merchant:
Scholarship information publisher

Publishes:
Commonwealth Scholarship
```

If the publisher does not itself provide the scholarship, Main Street should not model:

```text
Commonwealth Scholarship
    = merchant Offering
```

merely because the publisher displays it.

This distinction preserves truth about the merchant's relationship to the subject.

---

# 6. OfferingSchema

The existing accepted semantic boundary remains:

> `OfferingSchema` is platform-owned semantic structure; the merchant's actual Offering is merchant-owned data conforming to it.

Conceptually:

```text
OfferingSchema
    defines permitted structure
        ↓
MerchantOffering
    supplies actual merchant values
```

Example:

```text
ConsultationOfferingSchema
        ↓
MerchantOffering
"30-minute structural consultation"
£75
```

Merchant configuration does not create a new semantic type.

---

# 7. Offering can reference capability-owned semantics

An Offering may identify which supported operations apply.

Example:

```text
Oil Change Offering
    supports Appointment

Executive Room Category
    supports Booking

Milk Product Offering
    supports Ordering
```

The Offering itself does not redefine:

```text
Appointment
Booking
Order
Payment
```

semantics.

---

# 8. Offering identity versus business subject

An Offering may itself be the business subject.

Example:

```text
Service:
30-minute Legal Consultation
```

There may be no need for a separate underlying object.

The Offering is sufficient.

But other domains may need a distinct subject.

Example:

```text
Property
    ↓
Listing
```

The semantic model must support both without requiring one universal pattern.

---

# 9. Listing

A **Listing** is:

> **A merchant-scoped Operational Object representing a distinct published proposition or advertisement concerning an identifiable subject, where the publication itself has meaningful business identity, data or lifecycle separate from that subject.**

This definition is deliberately stricter than:

> "anything shown in a list."

A Listing exists because the publication proposition has independent semantics.

---

# 10. Listing is not a UI card

A storefront card such as:

```text
[Photo]
Haircut
£25
Book appointment
```

does not imply:

```text
Listing object
```

The card may simply be a projection of an Offering.

Likewise:

```text
Product grid
```

does not automatically mean every Product requires a `Listing`.

Hard invariant:

> **UI repetition does not justify Listing semantics.**

---

# 11. When Listing is justified

A separate Listing is justified when the publication has meaningful data or lifecycle distinct from its subject.

For example:

```text
Property P100
        ↓
Listing L200
```

The Property may own:

```text
address
building characteristics
location
bedrooms
bathrooms
```

while the Listing may own:

```text
transaction mode
asking price
advertising status
publication dates
marketing description
availability-to-market
listing-specific media selection
```

This is a meaningful distinction.

---

# 12. Realtor model

Canonical realtor structure:

```text
Property P100
        │
        │ SUBJECT_OF / REPRESENTED_BY
        ▼
Listing L200
        │
        ├── FOR_RENT
        ├── £1,200 pcm
        ├── ACTIVE
        └── published marketing information
        │
        ▼
Storefront projection
```

The customer sees L200.

If they submit an Enquiry:

```text
Enquiry E300
    SUBJECT → Listing L200
```

The merchant receives the Listing context automatically under MS-PROT-043.

---

# 13. Property

A **Property** in this model is an example of an underlying business subject.

It may be a merchant-scoped Operational Object if Main Street needs durable identity and authoritative property data.

Conceptually:

```text
Property
{
    property_identity
    location
    structured address
    characteristics
    applicable media/data
}
```

However this document does not yet standardise a complete property schema.

---

# 14. Property and Listing have different lifecycle

Example:

```text
Property P100
```

continues to exist even if:

```text
Listing L200
ACTIVE → WITHDRAWN
```

Later the same property might receive:

```text
Listing L300
FOR_SALE
```

Therefore:

```text
Property
≠
Listing
```

This is strong evidence that Listing deserves separate semantics in real estate.

---

# 15. Same Property may have multiple Listings over time

Valid:

```text
Property P100
    │
    ├── Listing L1
    │      FOR_RENT
    │      2026
    │
    └── Listing L2
           FOR_SALE
           later
```

Main Street must not overwrite Property identity merely because market proposition changes.

---

# 16. Simultaneous multiple Listings are not assumed

Whether one Property may simultaneously have:

```text
FOR_RENT
+
FOR_SALE
```

Listings is domain/policy-specific.

The generic Listing model allows separate propositions.

Capability semantics determine whether they may coexist.

---

# 17. Listing data versus Property data

The distinction should follow semantic ownership.

### Property-owned examples

```text
street
building number
city
postcode
coordinates
bedrooms
bathrooms
property type
```

### Listing-owned examples

```text
FOR_SALE / TO_RENT
asking price
rent amount
listing status
market publication window
listing-specific description
```

Some fields such as media may require further design because photographs may belong to the Property but Listing may select/order them for presentation.

That detail is deferred.

---

# 18. Enquiry should normally reference Listing, not Property

In the realtor customer journey:

```text
Customer views Listing L200
        ↓
Enquires
```

the structured subject should normally be:

```text
Enquiry
    SUBJECT → Listing L200
```

not merely:

```text
SUBJECT → Property P100
```

because the customer is responding to a particular proposition:

```text
TO RENT
£1,200 pcm
```

not just the physical property in the abstract.

This preserves the exact commercial context.

---

# 19. Listing is not necessarily an Offering

This distinction is important.

A property Listing may be:

```text
merchant advertising a third-party-owned property
```

rather than the merchant providing the property as its own service/product Offering.

The realtor's actual merchant service might instead be:

```text
Property marketing
Property management
Sales representation
Letting services
```

Therefore:

> **Listing and Offering shall not be collapsed solely because both can lead to customer transactions.**

---

# 20. Listing may reference an Offering where appropriate

Some domains may legitimately relate:

```text
Listing
    → Offering
```

For example, a merchant might publish a promotional Listing around a service Offering.

But this must be an explicit typed relationship.

It is not inherent.

---

# 21. Published Subject role

Main Street requires a generic way to say:

> This object may be exposed through the storefront.

This should **not** become a `PublishedSubject` Operational Object.

Instead:

> **PUBLISHED_SUBJECT is a semantic/presentation role that an eligible object may play under exposure configuration.**

Examples:

```text
Offering
    can be published

Listing
    can be published

Opportunity
    can be published

Publication
    can be published

Announcement
    can be published
```

---

# 22. Publication role does not alter ownership

If:

```text
Listing L1
```

becomes publicly visible:

```text
Listing
    plays PUBLISHED_SUBJECT role
```

Listing capability still owns Listing semantics.

Storefront does not acquire authority over it.

---

# 23. Exposure

`Exposure` means:

> **Whether and where a supported business object is made visible to an intended audience.**

Conceptually:

```text
Object
    +
Exposure configuration
        ↓
Storefront/read-model inclusion
```

Examples:

```text
PUBLIC
CUSTOMER_ONLY
MERCHANT_ONLY
NOT_EXPOSED
```

Exact exposure vocabulary remains downstream.

---

# 24. Exposure is not lifecycle state

Consider:

```text
Listing L1
state = ACTIVE
```

but:

```text
public exposure = temporarily disabled
```

Those need not mean the same thing.

Likewise:

```text
Offering remains operational
```

while temporarily absent from public storefront discovery.

Therefore:

```text
Lifecycle
≠
Exposure
```

unless specific capability semantics intentionally link them.

---

# 25. Storefront projection

Storefront projection is:

> **A customer-facing read representation composed from active operational objects, merchant-owned data, applicable relationships and exposure configuration.**

It is not authoritative business state.

Conceptually:

```text
Operational Object
        +
relationships
        +
merchant presentation values
        +
exposure configuration
        ↓
Storefront Projection
```

---

# 26. Product scenario

Suppose:

```text
Product Offering P1
Milk 2L
£2.20
```

Main Street can expose:

```text
Product Offering P1
        ↓
Storefront projection
```

No separate:

```text
Listing L1
```

is necessary unless the merchant/domain genuinely needs an independently managed publication proposition.

Therefore:

> **Product catalogue presentation does not automatically create Listing objects.**

---

# 27. Service scenario

Merchant offers:

```text
Structural Consultation
£75
30 minutes
```

This may simply be:

```text
MerchantOffering
        ↓
Storefront projection
        ↓
Appointment availability
```

No Listing is required.

---

# 28. Consultant scenario

For an online consultant:

```text
Offering
"60-minute strategy consultation"
        │
        ├── price
        ├── duration
        └── Appointment semantics
        │
        ▼
published storefront projection
```

Again:

```text
Offering
≠
Listing
```

unless independent Listing semantics emerge.

---

# 29. Scholarship publisher scenario

A scholarship publisher presents:

```text
Scholarship Opportunity O1
```

This is not necessarily an Offering because the publisher does not supply the scholarship.

The relevant semantic may be:

```text
Opportunity
```

or another information-publication object.

Conceptually:

```text
Opportunity O1
        ↓
published
        ↓
storefront/content projection
        ↓
Enquiry SUBJECT → O1
```

No fake Offering or Listing is required merely for page rendering.

---

# 30. Opportunity

This document recognises an `Opportunity` candidate semantic role/object for information publishers but does not fully define it.

The important accepted boundary is:

> **Informational subjects need not become Offerings merely because customers can discover or enquire about them.**

Detailed Publication/Opportunity semantics remain downstream.

---

# 31. Announcement scenario

Merchant announcement:

```text
"Closed this Monday for maintenance."
```

is published content.

It is neither:

```text
Offering
Listing
```

This further falsifies any universal:

```text
Everything public = Listing
```

model.

---

# 32. Publication

`Publication` should be understood broadly as a capability concerned with making information available to an audience.

Its content objects may include:

```text
Opportunity
Article
Announcement
other structured published content
```

where supported.

Publication semantics should not be forced through commerce Offering semantics.

---

# 33. Offering versus Publication

Canonical distinction:

```text
Offering
    merchant makes something available
    for supported customer operation

Publication
    merchant makes information available
    for consumption/discovery
```

They may coexist.

Example:

```text
Consultant
    publishes article
    offers consultation
```

The article is Publication content.

The consultation is Offering.

---

# 34. Offering versus Listing

Canonical distinction:

```text
Offering
    "what the merchant provides"

Listing
    "a distinct published proposition
     concerning a subject"
```

These can overlap conceptually in some businesses but are not the same semantic role.

---

# 35. Listing versus Publication

A Listing is itself publishable content, but its semantics include a specific proposition concerning an identifiable subject.

A general publication may not.

Example:

```text
Property Listing
    specific subject + market proposition

Scholarship article
    informational publication
```

Therefore:

```text
Listing
⊂ published content conceptually
```

does not require Java inheritance or one global hierarchy.

---

# 36. No universal `PublishedItem` object

Rejected:

```text
PublishedItem
    ├── Product
    ├── Service
    ├── Property
    ├── Scholarship
    └── Announcement
```

as the mandatory authoritative domain hierarchy.

This would allow presentation concerns to dominate business semantics.

Instead:

```text
independent business semantics
        ↓
eligible for exposure
        ↓
storefront projections
```

---

# 37. Typed relationships remain the composition mechanism

Relevant relationships may include:

```text
Listing
    SUBJECT → Property

Enquiry
    SUBJECT → Listing

Appointment
    OFFERING → MerchantOffering

Publication
    SUBJECT → Opportunity
```

where registered.

The exact relationship roles must remain platform-owned and typed.

---

# 38. `REPRESENTS` / `SUBJECT` relationship for Listing

A Listing requires a relationship to the underlying subject it concerns.

Conceptually:

```text
Listing L1
    SUBJECT → Property P1
```

The source Listing capability owns:

> L1 is a market/publication proposition concerning P1.

Property capability owns P1.

This does not grant Listing unlimited mutation authority over Property.

---

# 39. Listing subject cardinality

Initial accepted rule:

> **A Listing has exactly one primary subject.**

Reason:

A published proposition should have a clear primary thing it concerns.

Examples:

```text
one property
one vehicle
one job opportunity
one identified asset
```

Associated data can still reference other objects.

If a future valid domain requires a multi-subject Listing, that requirement must reopen this invariant.

---

# 40. Offering does not require underlying subject

Unlike Listing, an Offering may be semantically self-contained.

Example:

```text
"30-minute Consultation"
```

needs no separate:

```text
ConsultationSubject
```

Therefore Listing and Offering cannot share one mandatory subject model.

---

# 41. Merchant-owned commercial values

Main Street must distinguish values owned by:

```text
underlying subject
```

from values owned by:

```text
Offering
Listing
```

For example:

```text
Property.address
```

belongs to Property.

```text
Listing.askingRent
```

belongs to Listing.

Likewise a Product may own:

```text
product identity/description
```

while an Offering may own:

```text
merchant price
availability terms
```

The exact Product/Offering split requires later detailed design.

---

# 42. Storefront display can combine values

The customer page may display:

```text
property address
listing price
photos
description
map
merchant information
```

from different owners.

That is acceptable because read-model composition may combine authoritative sources.

It does not mean one object owns everything displayed.

---

# 43. Context-carry-forward uses the exact viewed object

If customer is viewing:

```text
Listing L200
```

and clicks Enquire:

```text
Enquiry SUBJECT → L200
```

If customer is viewing:

```text
Opportunity O300
```

then:

```text
Enquiry SUBJECT → O300
```

If customer is viewing:

```text
Offering O400
```

then:

```text
Enquiry SUBJECT → O400
```

No generic published-item identity is necessary.

---

# 44. Search/discovery

Search and discovery operate over projections/indexes of exposed subjects.

They do not create semantic ownership.

Conceptually:

```text
Published eligible objects
        ↓
search projection/index
        ↓
customer discovery
```

Search results may therefore contain heterogeneous subject types.

---

# 45. Search result type must be preserved

Main Street shall not flatten:

```text
Listing
Offering
Opportunity
Publication
```

into semantically anonymous search documents at application boundaries.

Search infrastructure may use common indexing fields internally, but returned results must preserve sufficient subject type and identity to route the customer correctly.

---

# 46. Categories and classification

Classification/category systems may apply to:

```text
Offerings
Listings
Opportunities
Publications
```

without changing their semantic type.

For example:

```text
Listing
category = Flat

Opportunity
category = Scholarship

Offering
category = Legal Consultation
```

Classification is orthogonal to semantic identity.

---

# 47. Business category remains irrelevant to runtime semantics

A merchant being classified as:

```text
REALTOR
```

must not cause runtime to automatically treat all public objects as Listings.

Instead the active capability graph determines:

```text
Listing capability
Property semantics
Enquiry capability
Publication/exposure
```

Business category remains onboarding/context information.

---

# 48. Listing state

A Listing likely requires a minimal lifecycle such as:

```text
DRAFT
ACTIVE
WITHDRAWN
```

or equivalent.

However this exact vocabulary is not yet accepted by this document.

Why?

Because:

```text
ACTIVE
PUBLISHED
AVAILABLE
SOLD
LET
EXPIRED
```

may represent different concerns:

```text
listing lifecycle
exposure
underlying subject state
transaction outcome
```

Those must not be collapsed prematurely.

---

# 49. `SOLD` is not necessarily Listing state

If a property is sold:

```text
Transaction / sale semantics
```

may establish the business fact.

The Listing may then be:

```text
WITHDRAWN
CLOSED
```

as a consequence.

But:

```text
SOLD
```

is not automatically accepted as generic Listing lifecycle state.

The owner of sale/transaction semantics must be explicit.

---

# 50. `AVAILABLE` is not Listing state

Likewise:

```text
Property Listing ACTIVE
```

does not necessarily mean:

```text
available for every customer action
```

Operational availability may depend on:

```text
current transaction
merchant policy
other commitments
subject status
```

Avoid:

```text
Listing.available = true
```

as generic authoritative truth.

---

# 51. Listing creation

Conceptually:

```text
CreateListing
```

may produce:

```text
CREATE_OBJECT Listing
+
ESTABLISH_RELATIONSHIP
    SUBJECT → Property
```

with registered Listing data.

The underlying Property is not recreated.

---

# 52. Listing mutation

Examples:

```text
Change asking price
Update marketing description
Change publication terms
```

use:

```text
MUTATE_DATA
```

on Listing.

Changing actual Property address would require an authorised Property operation, not Listing mutation.

---

# 53. Listing withdrawal

A supported:

```text
WithdrawListing
```

operation should change Listing lifecycle through:

```text
TRANSITION_STATE
```

where applicable.

It shall not delete:

```text
Property
Enquiries
customer history
```

---

# 54. Property deletion is not Listing lifecycle

Similarly:

```text
Property lifecycle/data governance
```

shall not be conflated with:

```text
Listing withdrawal
```

The relationship survives sufficiently for historical interpretation.

---

# 55. Enquiry after withdrawal

Existing:

```text
Enquiry E1
    SUBJECT → Listing L1
```

remains valid if L1 is later withdrawn.

New public Enquiry initiation from L1 may cease because the Listing is no longer exposed.

Historical context remains.

---

# 56. Listing exposure

Typical flow:

```text
Listing created
        ↓
validated
        ↓
eligible for public exposure
        ↓
Storefront projection
```

But Listing lifecycle and exposure remain separate.

For example:

```text
ACTIVE Listing
+
NOT_PUBLIC
```

may be valid during preparation, moderation or temporary suppression depending on future policy.

---

# 57. Offering exposure

Likewise:

```text
Offering
```

can be valid operationally but hidden from storefront.

Example:

```text
merchant accepts phone bookings
but does not advertise service publicly
```

Therefore:

```text
Offering exists
≠
Offering publicly visible
```

---

# 58. Merchant dashboard

Dashboard projections should preserve business language.

For realtor:

```text
Properties
Listings
Enquiries
Viewing Appointments
```

where applicable.

For consultant:

```text
Services
Appointments
Enquiries
```

No empty `Listings` section merely because the platform knows Listing semantics.

This preserves MS-PROT-037.

---

# 59. Information publisher dashboard

Publisher may see:

```text
Opportunities
Publications
Enquiries
Subscribers
```

without:

```text
Products
Appointments
Listings
Bookings
```

unless those capabilities are active.

---

# 60. Hybrid example

A property consultancy could have:

```text
Property Listings
        +
Consultation Offerings
        +
Viewing Appointments
        +
Professional Appointments
        +
Enquiries
```

The same merchant may therefore use:

```text
Listing
Offering
Appointment
```

concurrently.

This strongly validates keeping the semantics separate.

---

# 61. Realtor who sells own development

Suppose a property developer directly owns units and sells them.

A unit might participate in:

```text
Property
+
Listing
```

and the merchant may also conceptually be making that property available commercially.

This does not require collapsing Listing and Offering.

The semantic graph may explicitly relate them if an Offering is needed.

---

# 62. Vehicle marketplace-style merchant

Suppose a dealer sells vehicles.

A vehicle may be:

```text
Operational Object
Resource? not necessarily
```

and:

```text
Listing
    SUBJECT → Vehicle
```

The Listing owns:

```text
asking price
advertising status
```

This demonstrates Listing semantics beyond real estate.

Therefore Listing is not realtor-specific.

---

# 63. Equipment rental

A merchant owns:

```text
Excavator E1
```

The customer may reserve E1 through:

```text
Offering
+
Booking/resource semantics
```

A separate Listing may be unnecessary if the merchant simply exposes the rental Offering.

If the merchant instead advertises independently managed market propositions around particular equipment, Listing may become justified.

The architecture supports both.

---

# 64. Product marketplace concern

Main Street is not a marketplace.

Therefore Listing semantics must not imply:

```text
central Main Street catalogue
cross-merchant Listing discovery
platform-owned marketplace Listing
```

Listings remain merchant-scoped.

Any future cross-merchant discovery would be a separate product decision.

---

# 65. Merchant data freedom

The merchant controls values within supported data schemas:

```text
description
photos
price
address where applicable
other supported metadata
```

but cannot invent new executable Listing semantics.

This remains consistent with controlled merchant freedom.

---

# 66. Metadata schemas

The realtor case proves that capability-owned structured metadata is necessary.

Examples:

```text
Property:
    address
    bedrooms
    bathrooms
    location

Listing:
    transaction mode
    price
    listing status
```

Main Street should represent these through:

```text
platform-supported DataConcepts
+
capability-owned schemas
+
merchant-owned values
```

not arbitrary runtime JSON semantics.

---

# 67. Flexible metadata without semantic chaos

A schema may permit:

```text
optional supported fields
required supported fields
category-specific supported fields
```

but not:

```text
merchant defines arbitrary executable field meaning
```

This preserves extensibility without turning Main Street into an uncontrolled CMS/schema builder.

---

# 68. Media

Images and other media are content assets.

They may relate to:

```text
Property
Listing
Offering
Publication
```

depending on ownership.

Media itself does not determine semantic type.

Detailed media ownership is deferred.

---

# 69. Address

Structured address should likely become a reusable `DataConcept`.

For the realtor scenario:

```text
Address
{
    building number
    street
    city
    postcode
    country
}
```

plus optional:

```text
coordinates
```

However exact Address semantics remain deferred to a dedicated data-concept specification.

---

# 70. Map

Map presentation is derived from:

```text
structured location
+
map provider integration
```

It is not:

```text
Listing semantic authority
Enquiry semantic authority
```

Map failure must not corrupt the underlying Property/Listing identity.

---

# 71. Falsification — consultant

Consultant exposes:

```text
60-minute Consultation
```

A separate Listing adds no distinct lifecycle or proposition.

Correct:

```text
Offering
    ↓
storefront projection
```

**PASS**

The model does not require Listing.

---

# 72. Falsification — grocery

Grocery sells:

```text
Milk
Bread
Rice
```

Product/Offering projections can appear in catalogue.

No separate Listing required merely because they appear in a grid.

**PASS**

---

# 73. Falsification — realtor

Property exists independently of market advertisement.

Listing adds:

```text
FOR_RENT
price
marketing state
```

and can be withdrawn while Property remains.

Separate Listing is justified.

**PASS**

---

# 74. Falsification — same property relisted

Property P1 has:

```text
Listing L1
then
Listing L2
```

Property identity remains stable.

**PASS**

This would be awkward if Property and Listing were the same object.

---

# 75. Falsification — scholarship publisher

Publisher publishes external scholarship information.

Scholarship is not something the publisher itself offers.

Forcing Offering would misrepresent domain meaning.

Correct:

```text
Opportunity / Publication semantics
```

**PASS**

---

# 76. Falsification — announcement

Announcement is publicly visible.

It is neither Listing nor Offering.

**PASS**

Therefore public exposure cannot define either semantic type.

---

# 77. Falsification — property Enquiry

Customer is looking at Listing L1.

Enquiry should reference:

```text
L1
```

because price/transaction proposition belongs to the Listing.

**PASS**

---

# 78. Falsification — property address update

Merchant changes actual Property postcode.

Correct:

```text
Property operation
```

not:

```text
Listing mutation
```

Storefront later reflects updated Property data through composed projection.

**PASS**

---

# 79. Falsification — Listing price change

Merchant changes asking rent.

Correct:

```text
MUTATE_DATA on Listing
```

Property identity unchanged.

**PASS**

---

# 80. Falsification — Listing withdrawn

Listing withdrawal:

```text
Listing state changes
```

Property remains.

Existing Enquiries remain historically related.

**PASS**

---

# 81. Falsification — hidden Offering

Consultation remains available by merchant telephone but hidden online.

Offering remains operational.

Exposure changes.

**PASS**

This proves:

```text
Offering lifecycle
≠
Exposure
```

---

# 82. Falsification — hybrid realtor consultant

Merchant simultaneously advertises:

```text
Property Listings
+
Consultation Offerings
```

and supports:

```text
Viewing Appointments
Professional Appointments
Enquiries
```

No semantic collision.

**PASS**

---

# 83. Falsification — dealer vehicle Listing

Vehicle dealer advertises specific Vehicle V1.

Listing L1 concerns V1.

This validates Listing outside real estate.

**PASS**

---

# 84. Falsification — search

Search returns:

```text
Listing result
Opportunity result
Offering result
Publication result
```

Search can use common projection infrastructure while retaining typed result identity.

**PASS**

No universal PublishedItem semantic object required.

---

# 85. Falsification — merchant category

Two merchants both classified as:

```text
Professional Services
```

one uses Offerings, another only Publications/Enquiries.

Runtime follows active semantics rather than category.

**PASS**

---

# 86. Rejected models

The following are rejected:

1. Everything public is an Offering.
2. Everything public is a Listing.
3. Listing is merely a UI card.
4. Product catalogue item automatically requires Listing.
5. Service Offering automatically requires Listing.
6. Scholarship opportunity automatically becomes Offering.
7. Listing and Property are always the same object.
8. Listing and Offering are always the same object.
9. Storefront visibility determines semantic type.
10. `PublishedSubject` as mandatory Operational Object.
11. `PublishedItem` universal business superclass.
12. Search result indexing as semantic ownership.
13. Listing owns all underlying subject metadata.
14. Property mutation performed through Listing operation.
15. Listing lifecycle automatically equals exposure.
16. `AVAILABLE` as universal Listing state.
17. `SOLD` as universal Listing state.
18. Merchant business category determines Listing semantics.
19. Listing implies cross-merchant marketplace.
20. Arbitrary merchant-defined metadata semantics.

---

# 87. Accepted invariants

1. Offering and Listing are distinct.
2. Public exposure does not determine semantic type.
3. Offering represents something the merchant makes available through supported business interaction.
4. Informational publication does not automatically constitute Offering.
5. Listing is a distinct published proposition concerning an identifiable subject.
6. Listing exists only where the publication proposition has meaningful independent identity/data/lifecycle.
7. Listing is an Operational Object.
8. Listing is not a UI construct.
9. Product and service pages do not inherently require Listing.
10. Listing has exactly one primary subject in the current model.
11. Listing subject is represented through a typed relationship.
12. The target subject retains its own authority.
13. Listing relationship does not transfer mutation authority.
14. Property and Listing can have independent lifecycle.
15. One Property may have multiple Listings over time.
16. Listing-owned commercial/publication data remains separate from subject-owned data.
17. Enquiry originating from a Listing normally references the Listing rather than only its underlying subject.
18. Offering may be self-contained and does not require an underlying subject.
19. `PUBLISHED_SUBJECT` is a role, not a mandatory object.
20. Exposure is distinct from lifecycle.
21. Storefront projection is derived read state.
22. Search/discovery are projection concerns.
23. Search results preserve semantic target identity/type.
24. Classification does not redefine semantic type.
25. Publication and Offering remain distinct.
26. Announcement is neither Offering nor Listing merely because it is public.
27. Opportunity may be published without becoming Offering.
28. Merchant configuration cannot invent Listing semantics.
29. Structured metadata remains platform-supported and capability-owned.
30. Business category does not determine runtime Listing behaviour.
31. Listing remains merchant-scoped.
32. Listing does not imply marketplace participation.
33. Existing Enquiry relationships survive Listing withdrawal.
34. Listing withdrawal does not delete underlying subject.
35. Underlying subject mutation does not require Listing replacement unless semantics demand it.
36. Offering may remain operational while not publicly exposed.
37. Listing may remain operationally meaningful while not publicly exposed.
38. One merchant may simultaneously use Offering, Listing, Publication, Booking, Appointment and Enquiry semantics.
39. Media and map presentation do not determine semantic ownership.
40. Presentation composition may combine data from multiple semantic owners without merging those owners.

---

# 88. Deferred decisions

The following remain unresolved:

```text
exact Listing lifecycle states

exact Property schema

exact Listing schema

Listing exposure policy

Property ownership representation

property agent/vendor/landlord relationships

property sale transaction semantics

property letting transaction semantics

product versus Offering boundary

inventory versus Offering relationship

Offering pricing model

Offering variants

Opportunity semantics

Publication content model

Announcement model

Published-subject exposure vocabulary

search/index architecture

classification/category system

media ownership model

structured Address DataConcept

location/coordinates model

map provider integration

Listing revision/history

whether simultaneous Listings over one subject
are allowed by particular capabilities

multi-subject Listing if future evidence requires it
```

---

# 89. Governance review

## PROPOSE

The model distinguishes:

```text
Underlying subject
Offering
Listing
Publication/exposure
Storefront projection
```

**PASS**

## REVIEW / FALSIFICATION

The design was challenged against:

```text
consultant
grocery
realtor
property relisting
scholarship publisher
announcement
property Enquiry
property mutation
Listing price mutation
Listing withdrawal
hidden Offering
hybrid realtor/consultant
vehicle dealer
search
business-category variation
```

No valid counterexample requires:

```text
Listing = Offering
```

or:

```text
Everything public = Listing
```

**PASS**

## VALIDATE

The model supports:

```text
commerce
services
real estate
information publishing
hybrid businesses
```

through the same capability/Operational Object/typed-relationship architecture.

**PASS**

## ACCEPT

**MS-PROT-044 v1.0 is ACCEPTED.**

---

# 90. Canonical decision

> **Main Street shall distinguish an Offering, a Listing, an underlying business subject and public exposure. An Offering represents something the merchant itself makes available through supported customer operations. A Listing is a merchant-scoped Operational Object representing a distinct published proposition concerning one primary identifiable subject where that proposition has meaningful data, identity or lifecycle independent of the subject. A Listing is not required merely because an item appears on a storefront, catalogue or search result. Products and services may normally be projected directly from their Offering semantics, while a real-estate property may exist independently from one or more Listings concerning its sale or rental. Informational subjects such as scholarship opportunities need not become Offerings simply because a publisher exposes them. Public visibility is an exposure role and storefront projection concern rather than a universal business type. Typed relationships preserve associations such as `Listing → Property` and `Enquiry → Listing` without transferring ownership or mutation authority. This allows Main Street to support rich storefronts without turning presentation concepts into the semantic core or forcing materially different merchant subjects into one universal content abstraction.**

The next consequential specification should be **MS-PROT-045 — DataConcept, Structured Metadata & Capability-Owned Schema Model**. That is now necessary because Property, Listing, Offering, Enquiry and CustomerContext all require structured business data, and we need to define how Main Street supports rich metadata without either hard-coding every vertical field into the core or allowing merchants to invent arbitrary semantics.
