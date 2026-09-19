# MS-PROT-045 v1.2 — Reusable Physical Location, Provider-Neutral Resolution & Map Presentation Amendment

**Document ID:** MS-PROT-045  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after complete pre-approval presentation, Fundamental Vision Conformance, review, falsification and ambiguity review  
**Authority type:** Reusable physical-location value, schema-binding and map-presentation boundary amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-045 through v1.1; MS-PROT-051 v1.2/v1.7 only within reusable physical-location representation and resolution principles; MS-PROT-044 retained location/map semantic deferrals  
**Depends on:** Composite MS-PROT-027; composite MS-PROT-036; composite MS-PROT-044 through v1.5; composite MS-PROT-045 through v1.1; composite MS-PROT-048; composite MS-PROT-049; composite MS-PROT-051 through v1.7; composite MS-PROT-053; composite MS-PROT-057; MS-PROT-094  
**Resolves:** MS-PROT-044's retained semantic `location/coordinates model` and `map provider integration` questions within the bounded source/presentation scope defined here  
**Narrows:** `MS-PROT-051-V11-DQ-003` to concrete geocoding/map-provider implementation selection, provider-specific normalisation and deployment details  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

---

# Authority Identity Preflight

```text
target branch
    development

review basis HEAD
    c3f3cc8cdbf45ab0d5a2b488d37ff2c750a3cc3e

current MS-PROT-045 composition
    base + v1.1

MS-PROT-045 v1.2
    unoccupied
```

Latest-head and identity preflight MUST be repeated immediately before any post-approval repository write.

---

# 0. Fundamental Vision Conformance

Merchants understand:

```text
Location
Address
Map
Directions
Street view
```

They should not need to understand:

```text
latitude
longitude
Place ID
geocoder
provider binding
coordinate reference systems
map SDK
```

GrandRue therefore needs a reusable semantic location model that supports familiar merchant forms and rich customer maps while keeping provider technology out of business truth.

The design deliberately permits presentations such as:

```text
Photos | Map | Street-level view
```

or:

```text
Video tour
Location
[interactive map]
Key features
```

from the same underlying source truth.

**Vision result:**

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

---

# 1. Governing Decision

GrandRue SHALL distinguish:

```text
PHYSICAL LOCATION TRUTH

        ≠

LOCATION RESOLUTION EVIDENCE

        ≠

MAP PROVIDER INTEGRATION

        ≠

MAP / DIRECTIONS / STREET-LEVEL PRESENTATION
```

Canonical flow:

```text
Capability-owned subject
        ↓
Physical Location field
        ↓
provider-neutral resolution
        ↓
Projection + Exposure
        ↓
map presentation adapter
        ↓
customer-facing map / directions / street-level imagery
```

Hard principle:

> **The merchant provides a Location; GrandRue resolves and presents it. The merchant does not author map-provider infrastructure.**

---

# 2. Physical Location Is Not a Universal Operational Object

This amendment SHALL NOT introduce:

```text
LocationObject
MapObject
GooglePlace
MapListing
```

as universal domain objects.

Physical location is normally reusable structured value/reference semantics participating in an owner-controlled schema.

Examples:

```text
Property.location
Venue.location
Offering.service-location
Resource.location
```

only where the owning schema legitimately registers that field.

---

# 3. PhysicalLocationBindingV1

The initial reusable location binding has exactly two forms:

```text
PhysicalLocationBindingV1

    MERCHANT_LOCATION_REFERENCE

    INDEPENDENT_PHYSICAL_PLACE
```

This distinction prevents merchants from repeatedly copying their shop address into every service while still supporting subjects whose location exists independently of the merchant's premises.

---

# 4. Merchant Location Reference

`MERCHANT_LOCATION_REFERENCE` contains an exact reference to an existing Merchant Location identity.

Conceptually:

```text
MERCHANT_LOCATION_REFERENCE
{
    merchantLocationIdentity
}
```

Example:

```text
Offering
Haircut

Location
    Swansea Branch
```

GrandRue references:

```text
MerchantLocation L20
```

rather than copying:

```text
24 High Street
Swansea
...
```

into the Offering.

Merchant Location retains its own lifecycle, address revision and resolution authority.

---

# 5. Independent Physical Place

`INDEPENDENT_PHYSICAL_PLACE` represents a physical place whose meaning belongs to the owning subject rather than to a Merchant Location.

Conceptually:

```text
INDEPENDENT_PHYSICAL_PLACE
{
    physicalPlaceValue
}
```

Examples:

```text
Property.location

EventVenue.location

independently located asset
```

This does not manufacture a Merchant Location.

---

# 6. PhysicalPlaceValueV1

The initial independent physical-place value may contain:

```text
PhysicalPlaceValueV1
{
    postalAddress?
    merchantApprovedPlaceLabel?
    acceptedNavigationPoint?
}
```

At least enough source information must exist for the owning schema's intended use.

GrandRue SHALL NOT require a postal address where a jurisdiction or legitimate physical place lacks one.

---

# 7. PostalAddressV1 Becomes Reusable Representation

The internationally structured `PostalAddressV1` already accepted by MS-PROT-051 v1.2 SHALL be reusable as a structured address representation outside Merchant Location where an owning schema requires postal-address semantics.

Reuse of:

```text
PostalAddressV1
```

does NOT mean:

```text
Property
    = Merchant Location
```

or:

```text
Venue
    = Merchant Location
```

It reuses value semantics only.

Merchant Location identity and lifecycle remain separately owned by MS-PROT-051.

---

# 8. Address Is Still Not Location Identity

Hard invariant:

```text
postal address
    ≠ physical subject identity
```

Two different subjects may share the same address.

Examples:

```text
Flat 1
Flat 2

Shop 4
Shop 5

separate rooms in one building
```

Likewise identical navigation coordinates MUST NOT merge subjects.

---

# 9. Accepted Navigation Point

GrandRue SHALL recognise a provider-neutral:

```text
AcceptedNavigationPoint
```

representing the precise physical point accepted by the applicable owning operation for navigation/presentation purposes.

Internally it may require:

```text
latitude
longitude
resolution provenance
accepted resolution version/evidence
```

Ordinary merchants SHALL NOT need to see or edit those coordinates.

---

# 10. Navigation Point Is Not Postal Address

The existing distinction remains:

```text
PostalAddressV1
    ≠ AcceptedNavigationPoint
```

A valid physical place may have:

```text
precise address + precise navigation point

precise navigation point + weak postal address

shared address + distinct unit information
```

The model supports all three.

---

# 11. Traditional Merchant Field

Where a schema admits physical location, merchant applications SHOULD present an ordinary field such as:

```text
Location

[ Search address or place... ]

24 High Street
Swansea
SA1 ...

[ map preview ]
```

Appropriate optional actions may include:

```text
Use business location

Change location

Enter address manually
```

Merchant-facing language SHOULD NOT expose:

```text
Place ID
Latitude
Longitude
Geocoder result
Provider location reference
```

---

# 12. Search Is Assistance, Not Authority

A merchant typing:

```text
24 Hanover...
```

may receive candidate address/place suggestions from an external provider.

Those candidates are:

```text
resolution candidates
```

not authoritative GrandRue business truth.

Canonical:

```text
merchant input
        ↓
provider candidates
        ↓
merchant selection / confirmed intent
        ↓
GrandRue validation/resolution
        ↓
owner operation
        ↓
authoritative Physical Location value
```

---

# 13. Provider Candidate Does Not Commit Location

A provider returning:

```text
address
coordinates
place identifier
formatted text
```

does not by itself establish authoritative location.

The owning capability operation must accept the resulting location semantics.

---

# 14. No Mandatory Map Manipulation

Dragging or positioning a map pin SHALL NOT be mandatory for ordinary location entry.

Normal path:

```text
type/search
    ↓
select
    ↓
review
    ↓
save
```

A map may be used for:

```text
preview
optional confirmation
optional correction
fallback
```

but not as a universal merchant requirement.

---

# 15. Manual Entry Remains Supported

Ordinary operation MUST retain a deterministic non-map route where location semantics otherwise permit it.

This is particularly important for:

```text
poorly addressed regions
rural locations
digital-address systems
remote merchant administration
accessibility constraints
```

GrandRue SHALL NOT make ability to manipulate a map a prerequisite for representing a physical place.

---

# 16. Precise Map Requires Precise Resolved Location

A customer-facing precise map marker requires:

```text
an accepted precise navigation point
+
applicable public Exposure
```

GrandRue SHALL NOT knowingly substitute:

```text
postcode centroid
district centre
city centre
road midpoint
approximate search candidate
```

and present it as the exact subject location.

---

# 17. Incomplete Resolution Does Not Destroy Source Address

A subject may legitimately retain an accepted human-readable address even if map resolution is temporarily unavailable.

Therefore:

```text
valid address
+
map resolution unavailable
```

does not make the subject invalid unless its owning operation specifically requires precise navigation.

The map simply cannot be rendered as a precise location until resolution succeeds.

---

# 18. Underlying Subject Owns Its Location

Where location materially describes the underlying subject, that subject owns the field.

Canonical realtor example:

```text
Property P100
    location
    bedrooms
    bathrooms
    property type

        ↓ subject of

Listing L200
    transactionMode = RENT
    askingRent
    marketing description
```

The Listing page may display the Property's location.

The Listing SHALL NOT duplicate authoritative Property location merely for page construction.

---

# 19. Listing Projection Does Not Transfer Location Ownership

Customer page:

```text
Listing
    photos
    price
    address
    map
    bedrooms
```

may compose values from multiple owners.

This does not make Listing owner of:

```text
Property.location
Property.bedrooms
Property.EPC
```

Presentation composition remains non-authoritative.

---

# 20. Service at Merchant Premises

Where a service occurs at an existing merchant branch:

```text
Offering
    Location → MERCHANT_LOCATION_REFERENCE
```

Example:

```text
Haircut
    at Swansea Branch
```

The branch address is not duplicated into the Offering.

---

# 21. Service at Another Fixed Place

Where the merchant provides a service at another fixed place that is not one of its Merchant Locations:

```text
Offering
    Location → INDEPENDENT_PHYSICAL_PLACE
```

may be used where the Offering schema admits it.

Example:

```text
Training workshop
    Convention Centre, Cardiff
```

---

# 22. Service at Customer Location

A mobile service such as:

```text
mobile mechanic
home cleaning
home nursing
```

SHALL NOT receive a fake fixed physical location merely because GrandRue supports location fields.

The Offering instead participates in:

```text
service-area semantics
```

and the customer's exact place belongs to the later applicable customer/Appointment/Order context.

---

# 23. Remote Service

For:

```text
online consultation
remote tutoring
virtual advice
```

no physical-location field is required.

Absence of the field is semantically valid.

---

# 24. Product Location Is Not Stock Location by Default

GrandRue SHALL NOT use:

```text
Product.location
```

as a shortcut for:

```text
Inventory location
stock location
fulfilment location
pickup availability
```

Those meanings remain capability-owned.

A Product location field is justified only where the Product itself genuinely has durable physical-location meaning.

---

# 25. Pickup / Collection Location

Where customers collect an Offering/Order from a merchant branch, the applicable fulfilment/Ordering configuration SHOULD reference the relevant Merchant Location.

The Product itself need not copy the branch address.

---

# 26. Map Provider Is Not a DataConcept

The existing MS-PROT-045 rule becomes explicit:

```text
MapProvider
    ≠ DataConcept
```

and:

```text
GooglePlaceId
OpenStreetMap identifier
provider URL
panorama identifier
```

are not core business semantics merely because a provider uses them.

---

# 27. Provider-Specific Location Binding

GrandRue MAY retain provider-specific technical binding/evidence required to operate an integration.

Conceptually:

```text
MapProviderLocationBinding
{
    provider
    externalReference
    sourceLocationAffinity
    resolution provenance
}
```

This is integration evidence.

It MUST NOT replace:

```text
PhysicalLocationBindingV1
```

as business truth.

---

# 28. Provider Binding May Be Rebuilt

Because provider identifiers, datasets or integrations may change, GrandRue MUST be able to rebuild or replace provider bindings from authoritative provider-neutral location truth.

Canonical:

```text
Physical Location
    remains

Google integration removed
    ↓

OpenStreetMap-based provider connected
    ↓

new provider binding
```

No Property, Offering or Listing migration is required merely because the map provider changed.

---

# 29. Map Presentation Is Derived

A map displayed to the customer is a presentation projection.

Canonical:

```text
authoritative location
        +
Exposure
        +
provider readiness
        +
Storefront composition
        ↓
Map Presentation
```

The rendered map is not authoritative domain state.

---

# 30. No Fixed Map Position

This authority deliberately does NOT prescribe where the map appears.

Both are valid:

```text
Photos | Map | Street-level view
```

and:

```text
Video tour

Location
[map]

Key features
```

MS-PROT-094 / Storefront Composition determines layout.

Location semantics do not.

---

# 31. Map Block Is Optional

A merchant's website may expose the exact location without rendering an interactive map.

Example:

```text
24 Hanover Street
Swansea
SA1
```

with no map.

Likewise an internal location may exist without public presentation.

---

# 32. Street-Level Imagery Is Derived

Provider-supported street-level imagery SHALL NOT be stored as a source field such as:

```text
streetViewUrl
```

merely for convenience.

Instead:

```text
public exact location
        +
compatible provider
        +
imagery available
        +
presentation choice
        ↓
street-level imagery contribution
```

If unavailable, the presentation is omitted.

---

# 33. Provider Branding Does Not Define Semantics

A customer UI may use provider-specific terminology where legally/licensingly appropriate.

For example, one provider may brand its feature as:

```text
Street View
```

while another provides another street-level imagery experience.

The business semantic remains:

```text
street-level imagery presentation
```

not a provider brand.

---

# 34. Directions Are Derived

A customer-facing:

```text
Directions
```

action may be generated from an authorised precise navigation point.

The merchant does not need to enter:

```text
Google Maps URL
Apple Maps URL
Waze URL
```

as source business fields.

---

# 35. Exposure Remains Independent

A precise internal Physical Location does not imply:

```text
PUBLIC
```

A merchant or owning subject may retain:

```text
exact internal location
+
no public Exposure
```

without contradiction.

Map presentation MUST respect the applicable Exposure decision.

---

# 36. No Privacy Bypass Through Map Provider

If exact location is not publicly exposable, GrandRue MUST NOT leak it through:

```text
map iframe
directions URL
street-level imagery
provider place link
embedded coordinates
page metadata
```

Presentation adapters must consume already-authorised projections rather than raw internal location state.

---

# 37. Approximate Public Location Is Not Invented Here

This amendment does not establish a generic:

```text
APPROXIMATE_LOCATION
```

public mode.

If exact location is not exposable, GrandRue SHALL NOT simulate precision using an arbitrary nearby point.

A future explicit approximate-area presentation policy may be admitted separately where justified.

---

# 38. Merchant Location Reuse

For merchant premises, existing Merchant Location resolution remains authoritative.

A service referencing:

```text
MerchantLocation L20
```

does not independently geocode its copied address.

Projection resolves the applicable current Merchant Location location truth.

This prevents location drift.

---

# 39. Merchant Location Relocation

If:

```text
Offering
    → MerchantLocation L20
```

and the merchant physically relocates that shop according to MS-PROT-051 v1.7, the Offering relationship remains to the appropriate continuing/replacement business location semantics according to the governed relocation workflow.

The merchant should not manually edit every service address.

---

# 40. Independent Subject Location Revision

Where an owner-controlled subject stores:

```text
INDEPENDENT_PHYSICAL_PLACE
```

material location change is an owner field mutation.

Example:

```text
Venue.location
Property.location correction
```

The applicable source-owner revision/history rules govern the change.

Map-provider binding is downstream.

---

# 41. Historical Truth

Later location correction MUST NOT rewrite historical business commitments whose owning capability retained earlier location meaning.

Example:

```text
Appointment
committed at Location Revision R2
```

does not silently move because the current location field later becomes R3.

Current presentation may show R3 where appropriate, while historical commitment provenance remains intact.

---

# 42. Location Field Mutation Uses Existing Owner Access

This amendment creates no generic:

```text
CHANGE_ANY_LOCATION
```

permission.

Examples:

```text
Property.location
    → Property owner operation

Offering.location
    → Offering owner operation

Resource.location
    → Resource owner operation
```

under the accepted field-targeted mutation model.

Cross-capability direct mutation remains prohibited.

---

# 43. No Independent Commercial Entitlement

Physical-location field support introduces:

```text
NO NEW INDEPENDENT COMMERCIAL ENTITLEMENT
```

Mutation remains protected by the owning source operation's existing commercial rules where applicable.

Map rendering likewise does not by itself create a new Commercial Entitlement through this amendment.

Provider-cost limits, quotas or premium presentation policy remain Commercial/Resource Protection concerns if later needed.

---

# 44. AI Assistance

AI MAY interpret merchant language such as:

```text
"This service is at our Swansea branch."

"The house is on Hanover Street."

"The workshop is at Swansea Arena."
```

into candidate location semantics.

AI MUST NOT:

```text
invent coordinates
silently choose among materially ambiguous places
publish a private location
manufacture a Merchant Location
treat a map-provider result as authoritative
```

Material ambiguity follows existing merchant-intent confirmation rules.

---

# 45. Explicit Merchant Selection

If the merchant deliberately selects one returned location candidate from a clearly presented set, that selection may serve as the merchant's location intent for the normal owner operation.

GrandRue need not ask:

```text
"Are you sure you meant the location you just selected?"
```

without another material ambiguity.

---

# 46. Provider Failure

If a map provider is temporarily unavailable:

```text
authoritative location
    remains valid
```

and unrelated source operations remain valid.

Customer presentation may degrade to:

```text
human-readable address only
```

or omit the map contribution.

Provider failure MUST NOT corrupt Property, Offering, Listing or Merchant Location state.

---

# 47. Provider Switch

Switching map provider:

```text
Google
    ↓
another provider
```

must not require:

```text
new Property identity
new Offering
new Listing
location re-entry by merchant
```

provided the authoritative provider-neutral location remains usable.

---

# 48. Rural Location Falsification

Merchant/subject has:

```text
weak postal addressing
+
precise accepted navigation point
```

Expected:

location remains representable.

No invented street/postcode is required.

**PASS**

---

# 49. Shared Building Falsification

Two properties share a building address but have:

```text
Flat 2
Flat 5
```

Expected:

they remain distinct subjects.

Coordinate equality does not merge them.

**PASS**

---

# 50. Realtor Falsification

```text
Property P100
    physical location

Listing L200
    RENT
    £1,200
```

Customer Listing page renders:

```text
address
map
directions
```

from Property location without copying location authority into Listing.

**PASS**

---

# 51. Salon Falsification

```text
Haircut Offering
    Location → Swansea Branch
```

Expected:

Merchant Location is referenced.

No duplicated address.

**PASS**

---

# 52. Mobile Mechanic Falsification

```text
Mobile repair Offering
```

Expected:

service-area/customer-location semantics apply.

No fake fixed map field.

**PASS**

---

# 53. Online Consultant Falsification

```text
Online consultation
```

Expected:

no physical location required.

**PASS**

---

# 54. Hotel Falsification

Hotel rooms all operate from one Merchant Location.

Expected:

rooms/services need not each copy the full postal address.

The website may compose the hotel's Location into relevant pages.

**PASS**

---

# 55. Provider ID Change Falsification

External provider replaces or changes its place identifier.

Expected:

GrandRue's Physical Location remains stable.

Only integration binding/evidence changes.

**PASS**

---

# 56. Street-Level Imagery Missing

Exact public location exists but provider has no street-level imagery.

Expected:

map may still appear.

Street-level contribution is absent.

No source error.

**PASS**

---

# 57. Map Provider Down

Provider unavailable.

Expected:

address remains authoritative.

Page may degrade gracefully.

**PASS**

---

# 58. Hidden Location

Exact subject location exists internally but Exposure denies public access.

Expected:

no exact map, directions link, street-level imagery or provider URL may leak the location.

**PASS**

---

# 59. Rejected Alternative — Google Maps Field

Rejected:

```text
googleMapsUrl
googlePlaceId
googleCoordinates
```

as authoritative merchant business fields.

They couple business truth to one provider.

---

# 60. Rejected Alternative — Universal Latitude/Longitude Merchant Fields

Rejected.

Merchants should not need to understand coordinates.

Coordinates remain internal to precise navigation semantics.

---

# 61. Rejected Alternative — Copy Merchant Address Into Every Offering

Rejected.

This creates drift during correction or relocation.

Use Merchant Location references.

---

# 62. Rejected Alternative — Listing Owns Property Location

Rejected.

The fact that a map appears on a Listing page does not transfer Property location ownership to Listing.

---

# 63. Rejected Alternative — Map Determines Location Truth

Rejected.

A rendered map is a projection.

Provider response and presentation never become source authority merely by being visible.

---

# 64. Rejected Alternative — One Fixed Realtor Page Template

Rejected.

A map may appear:

```text
as a top-level tab

inside a Location section

beside details

below media

or another centrally governed composition
```

without changing source semantics.

---

# 65. Hard Invariants

1. Physical Location and map presentation are distinct.
2. Map provider is not a business DataConcept.
3. Provider identifiers are not source identity.
4. Merchant-facing location controls use ordinary business language.
5. Merchants are not required to edit coordinates.
6. Map-pin manipulation is not mandatory.
7. Manual/non-map entry remains available where applicable.
8. `PostalAddressV1` is reusable structured address representation.
9. Address does not become subject identity.
10. Accepted Navigation Point is provider-neutral.
11. Precise public maps require a precise accepted navigation point.
12. Approximate provider results are not presented as exact.
13. Merchant Location references avoid duplicate branch addresses.
14. Independent subjects may own independent Physical Locations.
15. Underlying subjects retain location ownership when Listings display their location.
16. Listing projection does not duplicate source location authority.
17. Mobile/customer-location services are not assigned fake fixed locations.
18. Remote services need no physical location.
19. Product location is not Inventory location by default.
20. Directions are derived presentation.
21. Street-level imagery is derived presentation.
22. Map placement is Storefront Composition, not domain semantics.
23. Exposure is required independently.
24. Private exact locations cannot leak through provider integrations.
25. Provider failure cannot corrupt source location truth.
26. Provider switching cannot require business-object recreation.
27. AI may propose location interpretations but cannot manufacture location authority.
28. Existing owner commercial/access contracts remain authoritative.
29. No independent map/location Commercial Entitlement is introduced.
30. No implementation is activated.

---

# 66. Deferred Decisions Preserved

This authority does not choose:

```text
Google Maps
OpenStreetMap tile/provider stack
Mapbox
Apple Maps
other provider
```

It also does not settle:

```text
exact provider SDK
provider account architecture
API key management
map styling
tile caching
provider billing
geocoder confidence thresholds
coordinate-normalisation algorithm
street-level imagery provider
map component library
public approximate-area mode
map zoom level
mobile component layout
```

Those remain implementation/presentation/provider decisions.

---

# 67. Effect on MS-PROT-051-V11-DQ-003

`MS-PROT-051-V11-DQ-003` is narrowed.

The semantic questions are now resolved:

```text
provider response
    ≠ authority

provider identifier
    ≠ business identity

precise navigation point
    = provider-neutral accepted domain value

map
    = derived presentation
```

The remaining DQ concerns only concrete:

```text
provider selection
geocoding implementation
coordinate-normalisation implementation
provider binding lifecycle
```

when implementation is promoted.

---

# 68. Effect on MS-PROT-044 Map Deferrals

The retained:

```text
location/coordinates model

map provider integration
```

questions are resolved within the Listing/subject semantic boundary.

A Listing may display an underlying subject's location without owning it.

The map provider remains a presentation/integration concern.

---

# 69. Merchant Experience

The resulting ordinary merchant interaction should be approximately:

```text
Location

[ Search address or place ]

or

[ Use business location ]

Selected:
24 Hanover Street, Swansea

[ Map preview ]

[ Change ]
```

Nothing in that experience exposes GrandRue's internal location architecture.

---

# 70. Customer Experience

Storefront composition may legitimately produce:

```text
Photos | Map | Street-level view
```

or:

```text
LOCATION

[ interactive map ]
```

or simply:

```text
24 Hanover Street
Swansea
```

depending on applicable source data, Exposure, provider readiness and the merchant website composition.

---

# 71. Implementation Activation

**NONE**

Acceptance does not authorise:

```text
Google Maps integration
OpenStreetMap integration
geocoding API calls
database migrations
new Java models
new REST endpoints
new merchant UI
new storefront components
new provider credentials
new Commercial Entitlements
```

Those require later implementation promotion.

---

# 72. Governance Recommendation

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`  
**Provider neutrality:** PASS  
**Merchant simplicity:** PASS  
**International address compatibility:** PASS  
**Subject ownership preservation:** PASS  
**Listing/Property separation:** PASS  
**Merchant Location reuse:** PASS  
**Exposure/privacy:** PASS  
**Map-provider failure:** PASS  
**Provider-switch portability:** PASS  
**Mobile/non-map accessibility:** PASS  
**Cross-domain falsification:** PASS  
**Ambiguity review:** PASS

```text
RECOMMENDATION: ACCEPT
```

**Manual approval:** GRANTED — 19 September 2026

---

# 73. Acceptance Statement

> **GrandRue shall provide reusable provider-neutral Physical Location semantics that can participate in capability-owned schemas without turning map providers, coordinates or addresses into universal business identities. A location field may either reference an existing Merchant Location or represent an independently located physical place. `PostalAddressV1` may be reused as structured international address value semantics, while an accepted precise Navigation Point remains distinct and internal to navigation resolution. Merchant applications shall expose ordinary Location controls such as address/place search, manual entry and optional map preview rather than raw coordinates or provider identifiers. Provider results are candidates and integration evidence, not authoritative business truth. Customer-facing maps, directions and street-level imagery are derived Storefront presentations requiring applicable Exposure and provider readiness; their placement is determined by Storefront Composition rather than domain semantics. A Listing may therefore display the location of its underlying Property or other subject without acquiring that location as Listing-owned truth. Map providers may be replaced without recreating merchant business objects, and provider failure must not corrupt authoritative location state.**
