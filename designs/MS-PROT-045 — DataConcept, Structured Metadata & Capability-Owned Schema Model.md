# MS-PROT-045 — DataConcept, Structured Metadata & Capability-Owned Schema Model

**Document ID:** MS-PROT-045  
**Version:** 1.0  
**Status:** **ACCEPTED after falsification and validation**  
**Depends on:** MS-PROT-005, MS-PROT-020 v1.4, MS-PROT-021, MS-PROT-022, MS-PROT-031, MS-PROT-036, MS-PROT-043 v1.2, MS-PROT-044 v1.0  
**Purpose:** Define how Main Street represents structured business data, reusable semantic data concepts, capability-owned schemas, merchant-owned values, contextual requirements and extensible metadata without either hard-coding every business vertical into the semantic core or permitting merchants to invent arbitrary operational meaning.

---

# 1. Governing principle

Main Street shall distinguish:

```text
DATA CONCEPT
        ↓
SCHEMA
        ↓
FIELD/BINDING
        ↓
MERCHANT-OWNED VALUE
        ↓
OPERATIONAL OBJECT / OFFERING / LISTING
```

The governing rule is:

> **Main Street owns the meaning and permitted structure of business data. Capabilities own how that data is used. Merchants own the values describing their businesses and operations.**

Therefore:

```text
Platform
    owns semantic meaning

Capability
    owns applicable schema/bindings

Merchant
    owns configured/business values
```

No layer may silently assume another layer's authority.

---

# 2. Why this specification is necessary

Recent accepted models require data such as:

```text
Property
    address
    postcode
    bedrooms
    bathrooms
    coordinates

Listing
    asking price
    transaction mode
    description

Offering
    duration
    price

Enquiry
    customer question
    contextual answers

CustomerContext
    contact information

Appointment
    scheduled interval
```

Main Street needs a way to represent these without creating:

```text
PropertyAddress.java
MechanicVehicleRegistration.java
ScholarshipEligibility.java
SalonHairLength.java
...
```

inside the semantic core for every niche.

But the opposite extreme is equally unsafe:

```text
Map<String, Object> metadata
```

with merchant-defined arbitrary meaning.

Both are rejected.

---

# 3. Core semantic layers

The accepted model is:

```text
Reusable semantic meaning
        ↓
DataConcept

Capability/domain usage
        ↓
Schema

Object-specific participation
        ↓
Field/Binding

Merchant/operational reality
        ↓
Value
```

Example:

```text
POSTCODE
    DataConcept
        ↓
PropertySchema.address.postcode
        ↓
"SA1 1AA"
```

The string `"SA1 1AA"` is not the semantic definition.

---

# 4. DataConcept

A **DataConcept** is:

> **A platform-owned definition of reusable business information whose semantic meaning is stable enough to be referenced independently across capabilities.**

Examples:

```text
POSTCODE
EMAIL_ADDRESS
PHONE_NUMBER
MONETARY_AMOUNT
STREET_ADDRESS
GEOGRAPHIC_COORDINATES
TIME_INTERVAL
QUANTITY
PERSON_NAME
```

Potential domain-specific concepts may also exist where genuinely reusable:

```text
VEHICLE_REGISTRATION
PROPERTY_BEDROOM_COUNT
```

but registration must be justified.

---

# 5. DataConcept is not a value

Strict distinction:

```text
POSTCODE
    = semantic concept

"SA1 1AA"
    = value
```

Likewise:

```text
MONETARY_AMOUNT
    ≠
£1,200

EMAIL_ADDRESS
    ≠
sarah@example.com
```

---

# 6. DataConcept is not a database type

A database representation such as:

```text
VARCHAR
INTEGER
DECIMAL
JSONB
```

is an implementation concern.

A DataConcept may impose representation constraints, but:

```text
DataConcept
≠
SQL type
```

For example:

```text
POSTCODE
```

may be stored as text, but its business meaning remains postcode.

---

# 7. DataConcept is not automatically globally reusable

The fact that something is data does not mean it deserves a global DataConcept.

Example:

```text
"Pets allowed"
```

may initially belong only to a Property Listing schema.

It should not automatically become:

```text
GLOBAL_PETS_ALLOWED
```

unless cross-capability reuse justifies that concept.

Hard rule:

> **Promote data to globally addressable DataConcept only when semantic reuse is demonstrated.**

---

# 8. Schema

A **Schema** is:

> **A platform-owned or capability-owned structured contract defining the data permitted or required for a particular semantic context.**

Examples:

```text
PropertySchema
ListingSchema
OfferingSchema
CustomerContextSchema
EnquirySubmissionSchema
```

A Schema may define:

```text
fields
nested structures
data concepts
cardinality
requiredness
validation constraints
applicability
```

It does not contain merchant values.

---

# 9. Schema ownership

Schemas are owned by the semantic context that gives their fields meaning.

Example:

```text
Property capability
    owns PropertySchema

Listing capability
    owns ListingSchema

Appointment capability
    owns Appointment data contract
```

The semantic core shall not attempt to own every field in every schema.

---

# 10. Schema is not merchant-defined executable structure

Merchants may populate and configure supported schema fields.

They may not create arbitrary executable semantics such as:

```text
custom field:
    "VIP Property"
    if true:
        bypass verification
        auto-confirm booking
```

The schema layer defines data.

It does not become a rule/workflow language.

---

# 11. FieldDefinition

A schema contains **FieldDefinitions**.

Conceptually:

```text
FieldDefinition
{
    identifier
    dataConcept
    cardinality
    requiredness/applicability
    constraints
    presentation hints where permitted
}
```

A FieldDefinition specifies how a DataConcept participates in a particular schema.

---

# 12. Same DataConcept may be bound differently

Example:

```text
POSTCODE
```

may appear in:

```text
Property.address.postcode
CustomerAddress.postcode
ServiceLocation.postcode
```

with different:

```text
requiredness
visibility
edit authority
```

The DataConcept meaning remains stable.

The field binding supplies context.

---

# 13. Requiredness is contextual

A DataConcept shall not carry universal requiredness.

Incorrect:

```text
POSTCODE
    required = true
```

Correct:

```text
Property.address.postcode
    REQUIRED

General Enquiry.customerPostcode
    NOT_APPLICABLE

ConstructionEnquiry.projectPostcode
    REQUIRED
```

This aligns with MS-PROT-005.

---

# 14. Requiredness must not duplicate Requirement semantics carelessly

There are two levels.

### Structural requiredness

Example:

```text
Property requires identity
Listing requires primary subject
```

### Operational Requirement

Example:

```text
Project postcode required
before submitting this Enquiry
```

A schema may mark a field structurally required where the object cannot exist validly without it.

Operational conditions remain Requirement semantics.

Do not collapse all Requirements into schema `required=true`.

---

# 15. Structured value

A value may itself be structured.

Example:

```text
Address
{
    buildingNumber
    street
    city
    postcode
    country
}
```

Main Street shall support nested typed structures.

This must not require flattening:

```text
address_line_1
address_line_2
address_line_3
```

into one universal core representation prematurely.

---

# 16. Address

`Address` qualifies as a strong reusable structured DataConcept candidate.

Conceptually:

```text
Address
{
    buildingNumber?
    buildingName?
    street?
    locality?
    city?
    postcode?
    country
}
```

Exact fields must accommodate jurisdictions beyond the UK.

Therefore the current realtor-specific:

```text
building number
street
city
postcode
```

should not be frozen as the universal global address structure without international validation.

---

# 17. Geographic location is distinct from postal address

A subject may have:

```text
Address
```

and:

```text
GeographicCoordinates
```

These are related but not identical.

Examples:

```text
rural site with coordinates but weak postal address
service area without exact street
property with both
```

Therefore:

```text
Address
≠
Coordinates
```

---

# 18. Map integration

Map presentation may consume:

```text
Address
Coordinates
```

but:

```text
MapProvider
```

is not a DataConcept.

Correct:

```text
Property.location data
        ↓
map integration
        ↓
map projection
```

Provider-specific identifiers must not become core business semantics unless explicitly justified.

---

# 19. Monetary value

Prices require more than:

```text
decimal amount
```

Canonical conceptual structure:

```text
Money
{
    amount
    currency
}
```

Therefore:

```text
1200
```

is insufficient as universal price semantics.

---

# 20. Price is not the same as Money

`Money` may be reusable.

`AskingRent`, `SalePrice`, `OfferingPrice`, `DepositAmount` are contextual semantic bindings.

Thus:

```text
Money
    reusable DataConcept

Listing.askingRent
    FieldDefinition using Money
```

This avoids creating one ambiguous universal `price`.

---

# 21. Time and temporal concepts

Main Street shall distinguish reusable concepts such as:

```text
Instant
LocalDate
LocalTime
DateRange
TimeInterval
Duration
```

according to actual semantic need.

Do not represent every time-related concept as one generic string.

---

# 22. Appointment interval

Example:

```text
Appointment.scheduledInterval
```

uses:

```text
TimeInterval
```

but the business field remains:

```text
SCHEDULED_INTERVAL
```

The DataConcept alone does not mean Appointment.

---

# 23. Booking reservation scope

A Booking may use:

```text
DateRange
Quantity
TimeInterval
ResourceCategory
```

depending on capability semantics.

This proves again:

> DataConcepts are reusable vocabulary, not complete business meaning.

---

# 24. Boolean fields require scrutiny

Uncontrolled Boolean proliferation is discouraged.

Example:

```text
petsAllowed = true
```

may be valid if the domain genuinely has binary semantics.

But many apparently Boolean concepts are actually multi-state:

```text
parking:
    NONE
    ON_STREET
    OFF_STREET
    GARAGE
```

or:

```text
furnished:
    FURNISHED
    PART_FURNISHED
    UNFURNISHED
```

Therefore:

> **Use Boolean only when the domain distinction is genuinely binary.**

---

# 25. Enumerated values

Capability-owned schemas may define bounded enumerated values.

Example:

```text
Listing.transactionMode
    SALE
    RENT
```

or:

```text
PropertyType
    FLAT
    DETACHED_HOUSE
    TERRACED_HOUSE
    ...
```

The allowed vocabulary remains platform/capability-controlled.

Merchant configuration selects values.

It does not invent semantic enum members.

---

# 26. Merchant-defined display labels

A merchant may potentially customise display labels where presentation policy permits.

But:

```text
display label
≠
semantic identifier
```

Example:

```text
semantic:
    RENT

display:
    "To Let"
```

The compiler/runtime retains the registered semantic value.

---

# 27. Free text

Free text remains necessary for data such as:

```text
description
customer enquiry message
merchant notes
article body
```

Free text is data.

It does not become executable semantics.

Hard invariant:

> **Runtime shall not infer authoritative behaviour solely from arbitrary text.**

---

# 28. Rich text

Published descriptions may support bounded rich-text structure.

Examples:

```text
paragraph
heading
bullet list
link
```

where presentation supports it.

Rich text remains presentation/content data.

It cannot embed:

```text
scripts
runtime rules
arbitrary executable code
```

---

# 29. Media references

Schemas may contain typed references to media assets:

```text
image
video
document
```

where supported.

The media asset itself should not be copied into semantic definitions.

Conceptually:

```text
Property.media
    → MediaAsset reference
```

Exact media semantics remain deferred.

---

# 30. Capability-owned metadata

Main Street shall use the term **metadata** carefully.

For this architecture:

> **Capability-owned metadata means structured business data whose interpretation belongs to a capability-owned schema.**

It does not mean:

```text
arbitrary key/value extras
```

Example:

```text
Property:
    bedroomCount
    bathroomCount
    propertyType
```

are legitimate structured property metadata.

---

# 31. Extension without semantic fragmentation

A new business need may require a field absent from the current schema.

Example:

```text
energy performance rating
```

The wrong solution is:

```text
merchant creates arbitrary custom field
```

The correct lifecycle is:

```text
business need identified
        ↓
semantic/domain review
        ↓
supported field/DataConcept added
        ↓
schema version evolves
        ↓
merchant may use it
```

This preserves controlled extensibility.

---

# 32. Not every new field requires a new global primitive

Suppose Property capability adds:

```text
GardenType
```

That might remain local to:

```text
PropertySchema
```

rather than become a top-level registry DataConcept.

Only demonstrated cross-context reuse should promote it.

---

# 33. Three semantic scopes for data

Data definitions may exist at three useful scopes:

```text
GLOBAL DATA CONCEPT
        reusable across capabilities

CAPABILITY DATA CONCEPT
        reusable inside one capability/domain

FIELD-SPECIFIC SEMANTICS
        meaningful only at one binding
```

This prevents registry explosion.

---

# 34. Example — EMAIL_ADDRESS

Likely global:

```text
EMAIL_ADDRESS
```

because it appears across:

```text
CustomerContext
Enquiry
Merchant
Staff
Notification
```

---

# 35. Example — PROPERTY_BEDROOM_COUNT

Probably capability-scoped:

```text
Property.BedroomCount
```

There is little reason for:

```text
Booking.BedroomCount
Customer.BedroomCount
```

to share a global semantic definition.

---

# 36. Example — preferred move-in date

Potentially field-specific:

```text
PropertyEnquiry.preferredMoveInDate
```

unless reuse later justifies promotion.

---

# 37. Schema composition

Schemas may reuse structured concepts.

Example:

```text
PropertySchema
{
    address: Address
    location: GeographicCoordinates?
    bedroomCount: PropertyBedroomCount
}
```

Reuse must be explicit and typed.

Do not achieve composition by copying arbitrary field maps.

---

# 38. Schema inheritance is not assumed

Main Street shall not immediately introduce:

```text
BaseListingSchema
    ↓
PropertyListingSchema
    ↓
RentalPropertyListingSchema
```

as universal inheritance.

Composition and contextual fields are preferred.

Inheritance may be introduced only where it materially simplifies semantics without creating fragile hierarchies.

---

# 39. Offering schema

Offering remains a key schema user.

Conceptually:

```text
OfferingSchema
{
    merchant-facing identity
    title
    description
    supported commercial values
    capability-specific parameters
}
```

Different capability contexts may extend Offering structure through registered bindings.

---

# 40. Offering values are merchant-owned

Example:

```text
Consultation Offering

title = "Initial Consultation"
duration = 30 minutes
price = £75
```

The merchant owns:

```text
title
duration value
price value
```

within allowed schema.

Main Street owns:

```text
what duration means
what Money means
what Appointment interaction means
```

---

# 41. Listing schema

A property Listing might conceptually contain:

```text
ListingSchema
{
    subject
    transactionMode
    askingPrice/rent
    marketingDescription
    status
}
```

The schema is capability-owned.

Actual Listing values belong to merchant operational data.

---

# 42. Property schema

Conceptually:

```text
PropertySchema
{
    address
    coordinates?
    propertyType
    bedroomCount
    bathroomCount
    characteristics
    media references
}
```

This remains an illustrative capability schema.

Exact fields are not finalised here.

---

# 43. Enquiry schema

Enquiry data should be divided into:

```text
SYSTEM-KNOWN CONTEXT

CUSTOMER-SUPPLIED DATA
```

For example:

```text
Enquiry
    SUBJECT → Listing L1

CustomerSubmissionSchema
    name
    reply contact
    question
```

The subject metadata does not need to appear as customer-input fields.

---

# 44. Dynamic Enquiry form derivation

Main Street may derive customer fields from:

```text
Enquiry schema
+
Requirements
+
existing context
+
known customer data
```

Example:

```text
Required:
    project postcode
    name
    contact

Known:
    name
    contact

Unknown:
    project postcode
```

Customer sees only:

```text
Project postcode
```

This is deterministic resolution, not AI-generated form semantics.

---

# 45. AI and schema

AI may:

```text
infer likely supported data concept
suggest populated values
extract candidate data from merchant prose
suggest schema configuration
```

AI may not:

```text
create new DataConcept
invent schema fields
invent enum values
change requiredness
invent executable validation
```

without registered platform semantics.

---

# 46. Natural-language onboarding

Merchant says:

> "I let two-bedroom flats around Swansea."

Inference may propose:

```text
Property capability
Listing capability
transactionMode = RENT
```

and infer candidate information.

It cannot create:

```text
Property.magicLocationRankingField
```

because that field does not exist in the supported schema.

---

# 47. Data value provenance

Where important, Main Street should preserve how a value was established.

Candidate origins include:

```text
MERCHANT_ENTERED
CUSTOMER_ENTERED
INFERRED
IMPORTED
DERIVED
SYSTEM_GENERATED
EXTERNAL_PROVIDER
```

The exact vocabulary requires downstream design.

Provenance does not change the semantic meaning of the value.

---

# 48. Inferred value requires appropriate authority

An inferred value may require merchant confirmation before becoming authoritative where the value affects merchant business representation.

Example:

```text
AI infers:
propertyType = FLAT
```

from merchant description.

Main Street should not silently publish that as authoritative without the applicable approval model.

---

# 49. Derived values

Some data should not be stored as independently merchant-edited fields.

Example:

```text
customer-facing availability
```

is derived.

Likewise:

```text
formatted full address
```

may be derived from structured Address.

Hard invariant:

> **Derived data shall not become a second independently mutable source of truth.**

---

# 50. Canonical versus derived representations

Example:

```text
Address
{
    buildingNumber = 24
    street = High Street
    city = Swansea
    postcode = SA1 1AA
}
```

Derived:

```text
"24 High Street, Swansea, SA1 1AA"
```

The formatted value can be cached/projected.

It must not diverge semantically from the structured source.

---

# 51. Search/index values

Search infrastructure may derive:

```text
normalised text
tokens
geospatial index
price index
category index
```

from authoritative structured values.

Those are read/search representations.

They do not become merchant business data.

---

# 52. Validation

Schemas may define structural validation such as:

```text
type compatibility
allowed values
numeric range
format constraints
cardinality
```

Examples:

```text
bedroomCount >= 0

currency is registered

transactionMode in allowed set
```

Validation remains platform-owned.

---

# 53. Validation is not verification

A postcode may be structurally valid:

```text
SA1 1AA
```

without proving:

> this property genuinely exists at that location.

Therefore:

```text
Validation
≠
Verification
```

This retains the accepted trust boundary.

---

# 54. External enrichment

Main Street may eventually enrich structured data from external services.

Example:

```text
postcode
    ↓
geocoding provider
    ↓
coordinates
```

But provider output must retain provenance.

External enrichment does not transfer semantic authority to the provider.

---

# 55. Provider identifiers

External identifiers such as:

```text
Google Place ID
provider geocode ID
```

should be integration data.

They must not become the primary semantic identity of:

```text
Property
Merchant
Listing
```

unless a specific integration contract explicitly demands it.

---

# 56. Merchant configuration versus merchant operational data

This distinction is essential.

### Merchant configuration

```text
supports property enquiries
requires project postcode
uses RENT and SALE transaction modes
```

### Merchant operational/business data

```text
Listing L1
price = £1,200

Property P1
postcode = SA1 1AA
```

Configuration defines permitted behaviour.

Business data supplies actual values.

---

# 57. Schema evolution

Schemas will evolve.

Evolution must preserve:

```text
existing operational records
interpretability
configuration compatibility
historical commitments
```

Future schema releases may:

```text
add optional field
deprecate field
tighten future validation
introduce new supported enum value
```

but shall not silently invalidate historical merchant records without migration strategy.

---

# 58. Optional field addition

Safest evolution:

```text
PropertySchema v1
    address
    bedroomCount

v2
    + energyRating optional
```

Existing Property objects remain valid.

---

# 59. New required field

More dangerous:

```text
v2
    energyRating REQUIRED
```

Existing data may not have that value.

Therefore required-field introduction needs explicit:

```text
migration rule
applicability rule
grandfathering
or merchant remediation
```

It cannot simply appear in runtime and invalidate all old objects.

---

# 60. Enum evolution

Adding:

```text
PropertyType = BUNGALOW
```

may be backward compatible.

Removing:

```text
FLAT
```

while merchant data uses it is not.

Registry/schema evolution must account for existing values.

---

# 61. Historical record interpretation

A Listing submitted under one schema revision must remain interpretable even if the schema later evolves.

Objects should therefore maintain sufficient schema/provenance affinity.

Exact persistence form is deferred.

---

# 62. Data mutation authority

An Operation must explicitly own its permitted data mutations.

Example:

```text
ChangeListingPrice
    may mutate Listing.askingPrice
```

It may not mutate:

```text
Property.address
```

unless the relevant Property operation is invoked.

This follows the authoritative effect algebra.

---

# 63. Generic patch APIs are rejected at the domain boundary

Rejected:

```text
PATCH /objects/{id}

{
    "anything": "anything"
}
```

as the semantic operation model.

Transport APIs may use PATCH technically, but application semantics must resolve to capability-owned permitted mutations.

---

# 64. Customer-submitted data also follows schema

A customer may not submit arbitrary new authoritative fields.

For an Enquiry:

```text
supported:
    name
    email
    question
    project postcode
```

where applicable.

Unknown fields should not silently create merchant data semantics.

---

# 65. Merchant-defined custom fields

A tempting feature is:

> Let merchant create any custom field.

This is **not accepted as a core semantic mechanism**.

Unrestricted custom fields create problems:

```text
unknown meaning
unvalidated type
poor portability
weak AI reasoning
inconsistent reporting
uncontrolled privacy collection
runtime ambiguity
```

Therefore arbitrary merchant semantic fields are rejected.

---

# 66. Limited extension slots

A future capability may support bounded merchant extension fields for purely descriptive/non-executable purposes.

For example:

```text
custom informational label
custom descriptive attribute
```

This is deferred.

If introduced, such fields must be explicitly classified as:

```text
NON_EXECUTABLE
NON_SEMANTIC
PRESENTATION/DESCRIPTIVE
```

and cannot influence runtime decisions without promotion to registered semantics.

---

# 67. Privacy and minimisation

Schemas should collect only business-relevant data.

Main Street shall not design universal customer records containing every field any merchant might ever want.

Example:

```text
CustomerContext
    should not universally require
    date of birth
    address
    gender
    occupation
```

unless applicable business semantics require them.

---

# 68. Sensitive data

Some future capabilities may require sensitive information.

This document does not establish permission to collect it.

Sensitive-field registration requires separate:

```text
security
privacy
regulatory
retention
access
```

review.

---

# 69. Access control at field level

Some data may require stricter access than the object containing it.

Example:

```text
merchant note
customer contact
private property access instructions
```

This document accepts that field-level access concerns may exist but does not mandate a universal field-ACL engine.

Access semantics remain downstream.

---

# 70. Storefront projection

The storefront may select customer-safe fields from underlying schemas.

Example property Listing:

```text
public:
    address
    price
    bedrooms
    description
    photos
    map

internal:
    merchant notes
    internal reference
```

The distinction between stored field and exposed field remains explicit.

---

# 71. Dashboard projection

Merchant dashboard may expose a broader projection than customer storefront.

Again:

```text
same authoritative object
different authorised projections
```

not:

```text
duplicate merchant object
duplicate customer object
```

---

# 72. Enquiry context projection

Merchant Enquiry view may combine:

```text
Enquiry.question

Listing.askingPrice

Property.address

CustomerSubmission.contact
```

without making Enquiry owner of all those fields.

Read-model composition remains legitimate.

---

# 73. Falsification — realtor property

Required:

```text
address
postcode
bedrooms
bathrooms
coordinates
```

Property capability can define structured schema.

No core-specific `RealtorProperty` primitive required.

**PASS**

---

# 74. Falsification — realtor Listing

Listing requires:

```text
transaction mode
asking price
description
```

separate from Property fields.

Schema ownership preserves distinction.

**PASS**

---

# 75. Falsification — consultant Offering

Offering requires:

```text
title
duration
price
description
```

same schema mechanism works.

No property assumptions leak.

**PASS**

---

# 76. Falsification — scholarship publisher

Opportunity needs:

```text
title
provider
deadline
eligibility information
link
description
```

These can be capability-owned data.

No commerce Offering requirement.

**PASS**

---

# 77. Falsification — mechanic Enquiry

Mechanic may require:

```text
vehicle registration
vehicle model
question
```

where applicable.

Those are contextual Enquiry requirements rather than universal Customer fields.

**PASS**

---

# 78. Falsification — customer already known

CustomerContext already contains:

```text
name
verified email
```

Enquiry requires name, email, project postcode.

Only project postcode is unresolved.

Customer sees only necessary unresolved input.

**PASS**

---

# 79. Falsification — arbitrary merchant field

Merchant creates:

```text
priorityLevel = GOLD_CUSTOMER
```

and expects runtime to automatically prioritise Bookings.

Rejected unless supported semantics exist.

**PASS BY REJECTION**

---

# 80. Falsification — descriptive merchant extension

Merchant wants a non-executable field:

```text
"Nearest landmark"
```

for property presentation.

Potentially useful, but unrestricted extension semantics are not yet accepted.

Deferred rather than forcing it into the core.

**PASS**

---

# 81. Falsification — map provider change

Merchant changes map provider.

Property:

```text
Address
Coordinates
```

remain unchanged.

Map integration changes.

**PASS**

Provider does not own the data semantics.

---

# 82. Falsification — price currency

Merchant operates in GBP.

Listing:

```text
Money
{
    amount = 1200
    currency = GBP
}
```

avoids ambiguous raw `1200`.

**PASS**

---

# 83. Falsification — international address

A universal UK-only:

```text
houseNumber
street
city
postcode
```

fails globally.

The Address DataConcept must allow jurisdiction-sensitive structure.

Therefore exact Address schema remains deferred.

**PASS BY REVISION**

---

# 84. Falsification — schema revision

New optional Property field introduced.

Existing Properties remain valid.

**PASS**

---

# 85. Falsification — new mandatory field

Schema update suddenly requires EPC rating for all historical Properties.

Without migration/applicability strategy this is invalid.

Model requires explicit evolution handling.

**PASS BY REJECTION**

---

# 86. Falsification — AI invents field

Merchant says:

> "My premium properties are prestige grade."

AI attempts to create:

```text
prestigeGrade
```

as semantic field.

Rejected unless a registered supported concept exists.

AI may store appropriate descriptive text instead where schema permits.

**PASS**

---

# 87. Falsification — derived formatted address

Merchant changes:

```text
postcode
```

Structured Address updates.

Formatted address projection recomputes.

No independent manual formatted-address truth.

**PASS**

---

# 88. Rejected models

The following are rejected:

1. Universal arbitrary metadata map.
2. Hard-coding every industry field into semantic core.
3. Merchant-created executable custom fields.
4. DataConcept = database type.
5. DataConcept = value.
6. Every field becomes globally registered DataConcept.
7. Every Requirement becomes `required=true`.
8. All temporal data represented as generic string.
9. Universal unqualified `price` field.
10. Boolean for every configurable distinction.
11. Free text interpreted as authoritative runtime behaviour.
12. Provider identifiers becoming core semantic identity.
13. AI inventing schema definitions.
14. Storefront projection owning authoritative data.
15. CustomerContext containing universal superset of all possible business fields.
16. Generic data PATCH meaning arbitrary semantic mutation.
17. Derived values as independently editable sources of truth.
18. Schema changes silently invalidating historical data.
19. Search/index representations becoming business authority.
20. UK-specific Address model becoming universal without validation.

---

# 89. Accepted invariants

1. DataConcept defines reusable semantic meaning.
2. DataConcept is distinct from data value.
3. DataConcept is distinct from persistence type.
4. Schema defines structured data permitted in a semantic context.
5. Schema is platform/capability owned.
6. Merchants own values within supported schemas.
7. Merchants cannot invent executable data semantics.
8. FieldDefinition binds DataConcept to contextual meaning.
9. Requiredness is contextual.
10. Operational Requirements remain distinct from simple structural requiredness.
11. Structured nested values are supported.
12. Address and coordinates are distinct concepts.
13. Map provider does not own location semantics.
14. Money includes currency where monetary semantics require it.
15. Price-like meanings remain contextual bindings.
16. Temporal concepts remain typed.
17. Boolean is used only for genuinely binary semantics.
18. Enumerated values remain bounded and registered.
19. Display labels do not redefine semantic values.
20. Free text remains non-executable.
21. Rich text remains bounded content.
22. Metadata means capability-owned structured business data, not arbitrary key/value storage.
23. New supported fields require semantic/schema evolution.
24. Not every field requires global registration.
25. Data concepts may be global, capability-scoped or field-specific.
26. Schema composition is preferred to uncontrolled inheritance.
27. OfferingSchema remains platform-owned structure with merchant-owned values.
28. Listing and Property maintain separate schemas where their semantic ownership differs.
29. Enquiry customer-input schema remains separate from carried subject context.
30. Forms may be derived from Requirements and known context.
31. AI may infer values but not invent semantics.
32. Inferred authoritative values follow approval/authority rules.
33. Derived data is not independently mutable authority.
34. Search/index data is derived.
35. Structural validation is distinct from business verification.
36. External enrichment retains provenance.
37. Merchant configuration and merchant operational data remain distinct.
38. Schema evolution must preserve historical interpretability.
39. Required-field evolution requires migration/applicability handling.
40. Enum removal/change must consider existing data.
41. Operations explicitly own permissible data mutation.
42. Arbitrary domain-level patch semantics are rejected.
43. Customer submissions cannot create unknown business fields.
44. Unrestricted merchant custom fields are rejected.
45. Future descriptive extensions must remain non-executable unless promoted to registered semantics.
46. Data minimisation applies to schema design.
47. Sensitive fields require separate governance.
48. Authorised projections may expose different subsets of one authoritative object.
49. Read models may combine fields from multiple owners.
50. Capability-owned structured schemas allow new business domains without runtime category branching.

---

# 90. Deferred decisions

The following remain unresolved:

```text
exact Java schema representation

global versus capability-local DataConcept registry

schema identity/version model

field cardinality representation

nested object implementation

schema validation engine

schema migration framework

Address DataConcept exact international structure

Money implementation

Currency definition/source

media asset schema

document/file data concepts

field-level access control

customer-data privacy classification

sensitive-field governance

rich-text format

merchant descriptive extension fields

localised labels/content

multi-language values

units and measurements

geospatial types

property-specific metadata catalogue

product-specific metadata catalogue

Opportunity schema

Offering variant schema

CustomerContext schema

Organisation customer schema

data provenance persistence
```

---

# 91. Governance review

## PROPOSE

A four-layer data model was proposed:

```text
DataConcept
    ↓
Schema
    ↓
Field binding
    ↓
Merchant/operational value
```

**PASS**

## REVIEW / FALSIFICATION

The model was challenged against:

```text
real-estate Property
real-estate Listing
consultant Offering
scholarship Opportunity
mechanic Enquiry
known customer context
merchant custom field
descriptive extension
map integration
currency
international addresses
schema evolution
AI inference
derived fields
```

No case requires:

```text
arbitrary semantic JSON
```

or:

```text
business-specific fields in semantic core
```

**PASS**

## VALIDATE

The model supports rich merchant domains while retaining:

```text
platform-owned meaning
capability-owned schema
merchant-owned values
deterministic validation
generic runtime
```

**PASS**

## ACCEPT

**MS-PROT-045 v1.0 is ACCEPTED.**

---

# 92. Canonical decision

> **Main Street shall represent structured business information through a controlled hierarchy of DataConcepts, capability-owned Schemas, contextual FieldDefinitions and merchant-owned or operational Values. A DataConcept defines reusable meaning but is neither a value nor a storage type. Schemas define which structured information applies to a particular capability or Operational Object, while merchants supply only supported values. Requiredness, validation, cardinality, visibility and operation-specific use remain contextual rather than universally attached to the DataConcept. Main Street shall support nested typed structures, bounded enumerations, free text, monetary values, temporal values, structured location and other necessary data without reducing business information to arbitrary metadata maps. Merchants and AI may not invent executable schema semantics at runtime. New business fields enter the platform through controlled semantic/schema evolution. Read models may combine data owned by several capabilities, but ownership remains with the authoritative source. This allows Main Street to represent rich domains such as Property, Listing, Offering, Opportunity, Enquiry and CustomerContext while avoiding both vertical hard-coding and uncontrolled schema generation.**

The next consequential specification is **MS-PROT-046 — Publication, Opportunity, Announcement & Information Dissemination Model**, because information-only merchants are now fully valid but still lack a precise operational model for what they publish, how structured opportunities differ from general publications, and how Enquiry, Subscription, classification and notifications attach to those subjects.
