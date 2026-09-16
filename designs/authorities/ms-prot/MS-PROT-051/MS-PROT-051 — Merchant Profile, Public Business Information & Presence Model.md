# MS-PROT-051 — Merchant Profile, Public Business Information & Presence Model

**Document ID:** MS-PROT-051  
**Version:** 1.0  
**Status:** **ACCEPTED after cross-contract review and manual approval**  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-027 v1.1, MS-PROT-028 v1.3, MS-PROT-031 v1.1, MS-PROT-036 v1.1, MS-PROT-040, MS-PROT-044 v1.0, MS-PROT-047 v1.0, MS-PROT-048 v1.1, MS-PROT-049 v1.0, MS-PROT-050 v1.1  
**Purpose:** Define the authority, ownership, exposure, lifecycle and composition rules for merchant-level descriptive/public business information without turning the Business Profile into a universal semantic aggregate, capability owner, verification authority or bespoke website model.

---

# 1. Governing decision

Main Street shall preserve the product principle:

> **The merchant manages the business, not webpages.**

However, Main Street shall not implement that principle by creating one universal `BusinessProfile` aggregate that owns every merchant fact, capability, operational object, integration, projection and analytical result.

The governing architectural rule is:

> **Business Profile is the merchant-facing composition and management surface for authoritative merchant information. It is not the universal semantic owner of everything shown within that surface. “Single source of truth” means one authoritative owner for each fact, not one object owning every fact.**

Canonical model:

```text
                         MERCHANT SCOPE
                               │
       ┌───────────────────────┼───────────────────────┐
       │                       │                       │
       ▼                       ▼                       ▼
Merchant-level facts     Capability-owned       Other authorities
                         business semantics
       │                       │                       │
public descriptor          Offerings               Branding
contact points             Publications            Integrations
merchant locations         Appointments            Trust claims
service-area descriptors   Orders                  Analytics
classification metadata    Staff                   SEO derivation
Public Business Hours      Inventory               ...
external-presence links    ...
       │                       │                       │
       └───────────────────────┼───────────────────────┘
                               ▼
                    BUSINESS PROFILE SURFACE
                               │
                  merchant edits the business
                               │
                               ▼
                    audience-safe projections
                               │
               ┌───────────────┼───────────────┐
               ▼               ▼               ▼
            Storefront         SEO       external channels
```

Hard invariant:

> **A fact appearing inside the Business Profile user experience does not transfer semantic ownership of that fact to a Business Profile domain object.**

---

# 2. Why this contract is required

Earlier product material correctly established Business Profile as the merchant's primary management experience and the source from which websites and other channels should be updated.

But that material also grouped together:

```text
identity
branding
business information
contact information
location
Business Hours
products
services
staff
announcements
customer experience
social media
payments
SEO
analytics
legal information
```

If implemented literally as one semantic aggregate, this would create a god object that duplicates ownership already established by accepted capability and platform contracts.

For example:

```text
Product / service Offering
    → Offering authority

Announcement
    → Publication authority

Staff permission
    → Identity/access authority

Payment account
    → fulfilment/integration authority

Brand colour
    → presentation authority

SEO metadata
    → derived projection/presentation concern

Analytics
    → derived analytical projection
```

The Business Profile experience may compose all of these without owning them.

This contract therefore preserves **one coherent merchant experience** while maintaining **many explicit semantic authorities**.

---

# 3. Scope

MS-PROT-051 governs:

```text
Merchant public descriptor
Merchant classification metadata
Merchant business contact points
Merchant physical locations
Merchant public service-area descriptors
Merchant external-presence links
profile exposure
merchant-level profile provenance
profile edit lifecycle
profile/public projection relationship
profile completeness guidance
AI-assisted profile content
profile-to-capability binding boundary
profile-to-integration boundary
multi-location descriptive presence
```

MS-PROT-051 does not own:

```text
capability activation
Offering semantics
Publication semantics
Appointment / Booking semantics
Order semantics
Inventory semantics
Staff authority
payment/provider configuration
trust/verification claims
branding/presentation semantics
SEO algorithm
analytics truth
customer relationship semantics
Business Hours temporal semantics
```

Those retain their existing accepted authorities.

---

# 4. Business Profile is a composition surface

The merchant-facing Business Profile may expose sections such as:

```text
About
Contact
Locations
Hours
Branding
Products / Services
Staff
Announcements
Payments
Integrations
Legal / policies
Analytics
```

but the section structure is a **merchant experience composition**, not a domain ownership map.

Conceptually:

```text
Business Profile
        │
        ├── About
        │     → merchant descriptor authority
        │
        ├── Contact
        │     → contact-point authority
        │
        ├── Locations
        │     → merchant-presence authority
        │
        ├── Hours
        │     → MS-PROT-050 authority
        │
        ├── Branding
        │     → presentation authority
        │
        ├── Products / Services
        │     → Offering authority
        │
        ├── Staff
        │     → staff/access authority
        │
        ├── Announcements
        │     → Publication authority
        │
        ├── Payments
        │     → fulfilment/integration authority
        │
        └── Analytics
              → analytical projection
```

Hard rule:

> **UI composition shall not be interpreted as semantic aggregation.**

---

# 5. One authoritative owner per fact

Main Street shall avoid duplicate merchant-editable copies of the same fact across its own surfaces.

Example:

```text
Merchant changes public telephone
        ↓
Authoritative MerchantContactPoint changes
        ↓
public projections refresh
        ├── homepage
        ├── contact surface
        ├── footer where used
        └── derived SEO data where applicable
```

Rejected:

```text
homepagePhone
contactPagePhone
footerPhone
seoPhone
```

as four separately editable values.

Likewise:

```text
Public Business Hours
        ↓
Storefront
Contact projection
CurrentBusinessOperatingStatus
SEO structured data
Scheduling input where applicable
```

shall not require multiple merchant-maintained copies.

External providers may maintain their own copies under their own authority; synchronisation remains an explicit integration concern.

---

# 6. Merchant public descriptor

Main Street requires a merchant-level public descriptor independent of legal identity and trust claims.

Conceptually:

```text
MerchantPublicDescriptor
{
    merchantScope
    displayName
    optionalTagline
    optionalShortSummary
    optionalApprovedDescription
    provenance
}
```

This is conceptual and does not mandate one Java aggregate or table.

The descriptor answers:

> **How does this merchant describe itself to its intended audience?**

It does not answer:

> **What has Main Street legally verified about this merchant?**

---

# 7. Public identity is not legal identity

The following must remain distinct:

```text
merchant display name
        ≠
legal entity name
        ≠
verified trading name
        ≠
controller legal name
        ≠
registered company name
```

A merchant may publish:

```text
Acme Structural Consulting
```

without Main Street thereby asserting:

```text
Acme Structural Consulting is a registered legal entity
Acme Structural Consulting is professionally regulated
Main Street has verified its legal trading name
```

MS-PROT-028 v1.3 remains authoritative for trust claims.

Hard invariant:

> **Publishing merchant-authored public identity does not manufacture a verification claim.**

---

# 8. Merchant description and editorial authority

A merchant description is merchant-authored or merchant-approved public content describing the merchant generally.

It is not an Offering, Publication article, Listing or capability definition merely because it appears on the storefront.

AI may:

```text
improve grammar
improve readability
propose concise wording
propose SEO-friendly wording
```

AI shall not independently establish or invent:

```text
services
products
qualifications
awards
professional registrations
physical locations
commercial claims
opening hours
trust claims
```

Canonical approval flow:

```text
Merchant source text / evidence
        ↓
AI proposal
        ↓
merchant review
        ↓
merchant approval / correction
        ↓
Authoritative approved description
        ↓
Public projection
```

Hard rule:

> **AI-generated merchant-profile wording does not become authoritative public content until the applicable merchant approval boundary is satisfied.**

---

# 9. Merchant classification metadata

Main Street may store merchant classification metadata such as public category, discovery tags or contextual descriptors.

Examples:

```text
hair salon
structural consultant
scholarship publisher
grocery
plumber
realtor
```

Classification may support:

```text
search/discovery
onboarding evidence
presentation recommendations
analytics
SEO categorisation
merchant-facing language
```

Classification shall not directly determine:

```text
capability activation
compiler/runtime branches
dashboard architecture
storefront functionality
verification eligibility
payment eligibility
Scheduling semantics
```

Hard invariant:

> **Merchant classification is contextual metadata, not executable business architecture.**

This preserves the accepted rejection of category templates.

---

# 10. Product/service/hybrid labels remain non-authoritative

Legacy product material may describe merchants as:

```text
PRODUCT
SERVICE
HYBRID
```

Such labels may remain useful for communication, analytics or onboarding evidence.

They do not own platform behaviour.

The active configuration/capability graph determines what Main Street can execute.

Therefore:

```text
businessType = SERVICE
```

must not automatically imply:

```text
Scheduling active
Appointment active
Booking active
```

and:

```text
businessType = PRODUCT
```

must not automatically imply:

```text
Ordering active
Inventory active
Payment active
```

---

# 11. Account contact and business contact are distinct

Main Street shall distinguish:

```text
ACCOUNT / CONTROLLER CONTACT
        ≠
MERCHANT BUSINESS CONTACT
        ≠
PUBLIC CONTACT POINT
        ≠
ENQUIRY INTERACTION
```

Example:

```text
controller login email:
owner@example.com
```

must not automatically become:

```text
public business email:
owner@example.com
```

merely because Main Street possesses the controller email.

Hard invariant:

> **Possession of account/contact data does not imply public exposure.**

---

# 12. Merchant contact point

A merchant business contact point represents a channel through which the merchant chooses to be reachable for a supported purpose.

Conceptually:

```text
MerchantContactPoint
{
    merchantScope
    contactPointIdentity
    kind
    value
    exposure
    optionalLabel
    provenance
}
```

Initial kinds may include platform-defined forms such as:

```text
TELEPHONE
EMAIL
MOBILE
WEB_LINK
```

Additional kinds require explicit supported meaning; this contract does not mandate a universal arbitrary contact-channel DSL.

---

# 13. Public contact point does not imply Enquiry

These remain separate:

```text
public phone number
        ≠
Enquiry capability
```

A public telephone link may route the customer outside Main Street.

Likewise:

```text
Enquiry capability
        ≠
public email address
```

A merchant may expose an Enquiry form while keeping its direct email private.

Valid information-publisher example:

```text
Public:
    display name
    description
    Enquiry interaction

Not public:
    telephone
    email
    physical location
```

**PASS**

---

# 14. Contact exposure

Each merchant-owned contact point shall have explicit audience exposure or be consumed through an explicit projection rule.

At minimum, Main Street must preserve the distinction between:

```text
PRIVATE / INTERNAL
PUBLIC
```

More detailed audience models remain governed by MS-PROT-027 and later exposure contracts.

Rejected:

```text
field exists
    therefore publish it
```

Accepted:

```text
authoritative contact point exists
        +
exposure permits public audience
        ↓
public contact projection
```

---

# 15. Merchant location

A Merchant Location represents a merchant-scoped physical place associated with the merchant's own presence or operation.

Conceptually:

```text
MerchantLocation
{
    merchantScope
    locationIdentity
    optionalPublicLabel
    structuredAddress
    optionalCoordinates
    exposure
    optionalPlatformDefinedRoles
    provenance
}
```

This contract does not require every merchant to have a Merchant Location.

Valid cardinality:

```text
Merchant
    ├── zero physical locations
    ├── one physical location
    └── multiple physical locations
```

---

# 16. Merchant Location identity

Merchant Location identity shall remain merchant-scoped.

Conceptually:

```text
MerchantLocationIdentity
{
    merchantScope
    locationIdentifier
}
```

A Merchant Location is not the tenant boundary.

MS-PROT-031 remains authoritative:

```text
Merchant Scope
    ≠
Merchant Location
```

A merchant may add, remove or change locations without becoming a different tenant.

---

# 17. Merchant Location is not a Resource

The existence of a Merchant Location does not automatically mean that it is:

```text
bookable
allocatable
capacity-constraining
schedulable
inventory-bearing
```

Therefore:

```text
MerchantLocation
        ≠
Resource
```

A capability may explicitly reference a location where its registered contract requires location semantics.

For example:

```text
Appointment Offering
    performedAt → MerchantLocation L1
```

may be valid where a registered relationship/binding supports it.

The profile itself does not invent that operational relationship.

---

# 18. Merchant Location is not a customer/service address

These must remain distinct:

```text
MerchantLocation
        ≠
Customer delivery address
        ≠
Customer service-visit address
        ≠
Booking destination
```

A mobile tradesperson may travel to a customer's address while exposing no merchant premises publicly.

A customer-provided service location remains customer/contextual business data under the applicable capability semantics.

---

# 19. Merchant Location is not a Property

A realtor falsifies any universal location model.

Example:

```text
Merchant office:
12 High Street
    → MerchantLocation

Property advertised:
45 Park Road
    → Property-owned location
```

The Property address shall not be treated as the merchant's business address merely because the merchant publishes the property.

Hard invariant:

> **The geographic location of a merchant-controlled subject does not automatically become Merchant Location data.**

---

# 20. Merchant Location does not imply public exposure

A Merchant Location may be private.

Examples:

```text
home-based consultant
mobile tradesperson
storage/workshop not open to customers
administrative office
```

Therefore:

```text
MerchantLocation exists
        ≠
address is public
```

A public storefront may instead expose:

```text
service area
public contact
remote-service information
```

without publishing the merchant's private location.

---

# 21. Merchant Location does not imply verification

These remain separate:

```text
MerchantLocation recorded
        ≠
MerchantLocation verified by Main Street
        ≠
Google Business Profile verified
        ≠
premises validated
```

MS-PROT-028 v1.3 remains authoritative for trust claims.

External providers retain authority for their own verification claims.

No location field may silently recreate universal premises verification.

---

# 22. Merchant Location roles

Where useful, Main Street may support a bounded platform-defined role vocabulary for merchant locations.

Examples may eventually include:

```text
CUSTOMER_VISIT_LOCATION
COLLECTION_LOCATION
ADMINISTRATIVE_LOCATION
SERVICE_BASE
```

Such roles shall be introduced only when accepted semantics require them.

They must not become free-form executable labels.

A role may affect projection or capability applicability only through an explicit accepted contract.

---

# 23. Multiple locations

Main Street shall support merchants with multiple independently described locations.

Example:

```text
Merchant: Smith & Co

Location L1
    Swansea

Location L2
    Cardiff
```

These locations may legitimately differ in:

```text
public address
contact points
Business Hours
services offered where explicitly bound
customer-facing labels
external-provider mappings
```

No assumption of identical per-location configuration is allowed merely because the locations share one Merchant Scope.

---

# 24. Location-specific facts require explicit scope

Where a fact varies by Merchant Location, it must carry explicit location scope or be owned by an object that already has location identity.

Rejected:

```text
Merchant.phone = X
Merchant.phone = Y
```

without scope.

Accepted conceptual patterns include:

```text
MerchantContactPoint
    scope = merchant
```

or:

```text
MerchantContactPoint
    scope = location L1
```

where such scope is supported.

This contract establishes the semantic requirement for explicit scope; exact persistence representation is downstream.

---

# 25. Service-area descriptor

A Merchant Service Area Descriptor represents public information describing where the merchant generally says it serves customers.

Examples:

```text
Swansea and surrounding areas
South Wales
UK-wide remote service
within 15 miles of Neath
```

Conceptually:

```text
MerchantServiceAreaDescriptor
{
    merchantScope
    descriptorIdentity
    structuredOrBoundedGeography
    publicDescription
    exposure
    provenance
}
```

This contract does not mandate one geographic representation.

---

# 26. Service-area description is not automatically operational eligibility

A public service-area descriptor answers:

> **Where does the merchant generally represent itself as serving customers?**

It does not automatically answer:

> **May this exact Order, Appointment, delivery or service request be accepted?**

Therefore:

```text
Profile service area
        ≠
capability-owned geographic eligibility rule
```

If a capability must reject an operation outside a geographic boundary, it shall do so through an explicit registered configuration/requirement contract.

An accepted capability may reference the same structured geographic source through an explicit binding.

Hard rule:

> **Descriptive profile geography shall not silently become executable runtime policy.**

---

# 27. External-presence link

A merchant may expose links to its presence on external platforms.

Examples:

```text
Instagram profile URL
LinkedIn page URL
Facebook page URL
YouTube channel URL
Google Business Profile/public place URL where supported
```

Conceptually:

```text
MerchantExternalPresenceLink
{
    merchantScope
    presenceIdentity
    platformKind
    publicUrl
    exposure
    provenance
}
```

This represents public profile content.

It is not itself an authenticated integration connection.

---

# 28. External link is not ProviderConnection

These must remain distinct:

```text
public Instagram URL
        ≠
connected Instagram publishing integration
```

and:

```text
public Google profile URL
        ≠
Google Business Profile OAuth/provider connection
```

A ProviderConnection may hold credentials, scopes, health and provider-specific identifiers under integration/fulfilment authority.

A public link is merely merchant-profile information unless explicitly bound otherwise.

---

# 29. External provider mapping

Where Main Street connects a Merchant Location or merchant presence to an external provider representation, the mapping must be explicit.

Example:

```text
Main Street MerchantLocation L1
        ↕ explicit integration mapping
Google Business Profile location G1
```

The mapping does not merge authorities.

```text
Main Street location fact
        ≠
Google stored location fact
```

The provider owns its own record and verification claims.

Main Street owns its merchant-profile record.

Sync direction, conflict handling and supported fields must be defined by the relevant integration contract.

---

# 30. Branding is composed into Business Profile but not owned by it

The Business Profile experience may expose merchant branding controls such as:

```text
logo
cover image
brand colour
font choice
theme
```

but branding remains presentation authority.

A branding change may refresh storefront presentation without changing merchant business semantics.

Rejected:

```text
BusinessProfile domain aggregate owns all presentation behaviour
```

Accepted:

```text
Business Profile surface
    edits registered presentation values
        ↓
Presentation authority
        ↓
Storefront composition
```

---

# 31. Products and services are not profile fields

Products/services may appear inside the Business Profile user experience for convenience.

But they remain Offerings or other capability-owned constructs where accepted semantics require them.

Therefore:

```text
BusinessProfile.products[]
```

must not become a universal semantic model merely because a UI section is named Products.

MS-PROT-021 and MS-PROT-044 remain authoritative.

---

# 32. Staff is not a profile-owned array

Staff may appear within the Business Profile management experience.

But staff identity, merchant relationship, privileges and runtime authority remain governed by identity/access and staff semantics.

A public staff biography or profile projection is not the same thing as the staff principal's authentication/authorisation record.

Hard rule:

> **Public staff presentation and staff execution authority shall not be collapsed.**

---

# 33. Announcements/publications remain capability-owned

Announcements and other merchant-authored published content may appear within the Business Profile experience.

They remain governed by Publication semantics.

The profile surface may provide an entry point to manage them.

It does not own their lifecycle, audience, revision or notification consequences.

---

# 34. Payment configuration is not profile-owned truth

The Business Profile may expose payment setup status or configuration entry points.

Actual payment-provider accounts, credentials, onboarding/trust requirements, fulfilment bindings and provider health remain under fulfilment/integration/trust authorities.

Example:

```text
Business Profile → Payments section
        ↓
shows: Setup required
```

This is a projection/action surface.

It does not mean:

```text
BusinessProfile.paymentVerified = false
```

is the canonical trust model.

---

# 35. SEO is derived, not a competing source of business truth

Main Street may generate:

```text
page titles
meta descriptions
structured data
Open Graph metadata
canonical URLs
sitemaps
```

from authoritative public facts and published capability state.

SEO output shall not become a separately merchant-maintained duplicate of those facts by default.

Example:

```text
Public telephone
        ↓
public projection
        ↓
SEO structured data where applicable
```

not:

```text
profile phone
+
SEO phone
```

as separate sources.

Selected bounded SEO presentation overrides may be supported later without changing this ownership rule.

---

# 36. Analytics is derived read state

Analytics shown within the Business Profile/dashboard are derived projections from customer/public/merchant activity.

Therefore:

```text
BusinessProfile
    does not own visitorCount
    does not own bookingTrend
    does not own orderTrend
```

as merchant-editable source facts.

The direction of truth is:

```text
profile + capability state
        ↓
public/customer interactions
        ↓
recorded activity
        ↓
analytics projections
```

Analytics may be displayed alongside profile-management sections without becoming profile semantics.

---

# 37. Legal/policy content requires explicit authority

Merchant-authored legal or policy text may be managed through the Business Profile experience.

Its semantic effect depends on accepted policy/requirement/publication contracts.

A free-form legal-text field must not silently create executable cancellation, refund or eligibility rules.

Therefore:

```text
merchant legal/public copy
        ≠
executable PolicySelection
```

unless an explicit registered relationship binds the two.

---

# 38. Public Business Hours remain a separate authority

MS-PROT-050 remains authoritative for Public Business Hours and `CurrentBusinessOperatingStatus`.

MS-PROT-051 establishes that Business Hours appear within the Business Profile management surface while preserving their distinct temporal semantics.

```text
Business Profile surface
        ↓ edits
Public Business Hours authority
        ↓
Storefront / status / scheduling consumers
```

Business Hours are not generic text content.

---

# 39. Profile data does not automatically become public

MS-PROT-027 remains authoritative:

```text
authoritative fact
        ≠
public projection
```

A profile datum shall be public only where:

```text
fact exists
        +
applicable exposure permits audience
        +
projection includes that fact
```

Examples:

```text
private home address
    → retained internally
    → not public
```

```text
public service area
    → storefront-visible
```

```text
controller login email
    → authentication/account use
    → not public business contact unless explicitly copied/entered as such
```

---

# 40. Exposure cannot redefine semantic ownership

Marking a fact `PUBLIC` does not turn it into Publication capability content.

Likewise, marking a Merchant Location private does not alter the existence of the Merchant Location.

Therefore:

```text
semantic ownership
        ≠
exposure
        ≠
projection placement
```

These remain independent dimensions.

---

# 41. Profile edit lifecycle

Merchant-level descriptive/profile data has a different lifecycle from compiled executable configuration.

Examples of ordinary profile edits:

```text
change public telephone
edit approved description
change public tagline
update service-area wording
add public social link
correct location label/address
```

These changes shall not automatically require recompilation of the merchant's Resolved Configuration Package merely because they affect public presentation.

Canonical:

```text
Profile fact change
        ↓
validate profile value + authority + exposure
        ↓
commit authoritative profile revision/fact
        ↓
invalidate/refresh affected projections
```

---

# 42. Profile facts may become configuration inputs only through explicit binding

Some profile facts may also be referenced by executable configuration where an accepted contract requires it.

Examples:

```text
Appointment offering performed at MerchantLocation L1

Collection operation uses MerchantLocation L2

Scheduling uses Business Hours scoped to location L1
```

In such cases:

```text
Profile fact / identity
        ↓ explicit registered reference
Capability/configuration contract
        ↓
Resolved Configuration Package
```

The profile fact itself still does not become capability-owned.

A change that affects a compiled reference/binding may require a new validated configuration revision according to MS-PROT-022/MS-PROT-040.

Hard rule:

> **Profile data affects executable semantics only through explicit registered binding; presentation use alone does not make it compiler input.**

---

# 43. Deletion and referenced profile facts

A profile fact that is referenced by active configuration or outstanding obligations cannot be blindly deleted.

Example:

```text
MerchantLocation L1
    referenced by active Appointment offering
```

The merchant cannot remove L1 in a way that leaves a dangling executable reference.

Valid consequences may include:

```text
reject deletion pending configuration change
create coordinated change set
retire future use while preserving historical commitments
```

Exact lifecycle behaviour follows MS-PROT-040 and the referencing capability contract.

Hard invariant:

> **Profile editing shall not violate referential integrity of accepted executable or historical business commitments.**

---

# 44. Merchant profile revisions and provenance

Main Street should preserve sufficient provenance to explain material public/profile changes.

Conceptually, profile facts may carry:

```text
merchant scope
fact identity
value
source/provenance
last approved/changed by
change time
exposure
```

This contract does not mandate event sourcing or one universal profile revision aggregate.

The key requirement is traceability where materially useful.

---

# 45. External imports are proposals/evidence unless authority is explicit

Main Street may import data from supported external sources, for example an external business profile.

Imported values shall not silently overwrite merchant-authoritative Main Street facts merely because the external provider supplied them.

Canonical:

```text
External provider value
        ↓
import evidence / candidate update
        ↓
registered sync policy
        ↓
merchant-authoritative Main Street fact
```

Where an accepted integration explicitly defines provider-to-Main-Street authority for a field, that contract governs.

Otherwise:

> **External possession of a similar field does not establish write authority over the Main Street profile.**

---

# 46. Main Street-to-provider synchronisation remains integration work

Changing a Main Street profile fact may trigger downstream synchronisation to connected providers where configured.

Example:

```text
Merchant changes public phone
        ↓
Main Street profile updated
        ↓
Storefront refreshed
        ↓
optional external provider sync attempt
```

Provider sync failure shall not roll back the Main Street profile fact unless a specific integration contract requires transactional coupling, which is not the default.

Provider consistency state remains separate from profile truth.

---

# 47. Website generation consumes projections, not the raw profile monolith

The shared storefront runtime shall consume audience-safe projections composed from authoritative facts and capability state.

Preferred:

```text
Merchant-level public facts
        +
capability public projections
        +
presentation configuration
        ↓
Storefront composition
```

Rejected:

```text
serialize entire BusinessProfile object
        ↓
website decides what it means
```

This prevents frontend code from becoming an implicit semantic resolver.

---

# 48. Website sections are not merchant-owned truth

A merchant may see or influence website presentation through bounded presentation controls.

But a section such as:

```text
Opening Hours
Contact
Products
Services
Announcements
```

is a projection/composition decision.

The underlying facts/objects remain owned by their authorities.

A merchant should not need to maintain website-specific copies of those facts.

---

# 49. Profile completeness is contextual

Main Street may provide merchant-facing guidance about missing or useful profile information.

Completeness shall be evaluated against:

```text
current merchant configuration
current public surfaces
selected capabilities
applicable integrations
merchant goals/evidence
missing required or useful facts
```

It shall not be one universal checklist.

Rejected:

```text
no physical address
    → profile incomplete
```

for an online publisher.

Rejected:

```text
no public phone
    → profile incomplete
```

for a merchant intentionally using Enquiry as the contact channel.

Accepted:

```text
Merchant wants local map discovery
    +
no public Merchant Location
        ↓
contextual recommendation
```

---

# 50. Profile completeness is not merchant eligibility

A profile-completeness recommendation shall not silently become a publication/trust gate.

Hard invariant:

> **Completeness guidance is advisory unless an accepted operation, projection or trust contract identifies a specific missing requirement whose scope is explicit.**

MS-PROT-028 v1.3 remains authoritative for minimally scoped trust restrictions.

---

# 51. Progressive completion

The Business Profile may evolve over time.

A merchant may begin with:

```text
display name
description
Enquiry
```

and later add:

```text
public contact points
location
Business Hours
Offerings
external links
branding
```

No architecture migration is required.

This preserves Main Street's adaptive onboarding and evolvable merchant configuration principles.

---

# 52. Information publisher falsification

Scenario:

```text
Merchant:
Scholarship information publisher

Public merchant-level facts:
    display name
    description
    Enquiry entry point

No:
    physical location
    public phone
    payment
    Scheduling
```

Result:

- profile remains valid;
- storefront can publish merchant descriptor and Publication content;
- absence of address does not block publication;
- no category-specific template is required.

**PASS**

---

# 53. Online consultant falsification

Scenario:

```text
Merchant:
remote strategy consultant

Public facts:
    display name
    description
    public email optional
    Business Hours optional

Capabilities:
    Offering
    Enquiry
    Scheduling
    Appointment
```

Result:

- no physical Merchant Location required;
- Appointment semantics remain capability-owned;
- profile description does not activate Scheduling;
- external calendar link/connection remains separate integration state.

**PASS**

---

# 54. Physical retailer falsification

Scenario:

```text
Merchant:
retailer

Location L1:
    public shop address
    location-specific Business Hours

Capabilities:
    Catalogue
    Ordering optional
    Inventory optional
```

Result:

- Merchant Location is profile/presence data;
- catalogue/inventory remain capability-owned;
- location existence does not automatically create a Resource;
- website projects location/hours where exposure permits.

**PASS**

---

# 55. Multi-location practice falsification

Scenario:

```text
Merchant:
Smith & Co

Location L1:
    Swansea

Location L2:
    Cardiff
```

Result:

- one Merchant Scope;
- two stable MerchantLocation identities;
- facts/hours may vary by location;
- no second tenant is created;
- no synthetic assumption that all locations share one hours/contact set.

**PASS**

---

# 56. Mobile tradesperson falsification

Scenario:

```text
Merchant:
plumber

Private operational base:
    exists internally
    not public

Public service area:
    Swansea and surrounding areas
```

Result:

- home/base address need not be exposed;
- service-area description may be public;
- operational geographic acceptance must still come from an explicit capability contract if enforced;
- no physical-premises verification gate.

**PASS**

---

# 57. Realtor falsification

Scenario:

```text
Merchant office:
MerchantLocation L1

Property P100:
45 Park Road

Listing L200:
TO RENT
```

Result:

- merchant office and property address remain separate authorities;
- storefront may show both in different contexts;
- Property address does not redefine merchant presence;
- Enquiry may reference Listing L200 without involving MerchantLocation L1.

**PASS**

---

# 58. Hybrid merchant falsification

Scenario:

```text
Merchant:
physical retailer + online consultation

MerchantLocation L1:
shop

Offerings:
products
remote consultation
```

Result:

- one Business Profile experience can manage merchant descriptor/location plus capability-owned Offerings;
- remote consultation need not inherit shop location merely because the merchant has one;
- explicit Offering/location binding controls location applicability.

**PASS**

---

# 59. Trust-claim falsification

Scenario:

```text
Merchant display name:
Acme Financial Advice
```

Result:

- display name may be published subject to normal content rules;
- Main Street does not infer regulatory authorisation;
- any regulated operation requiring trust evidence is scoped under MS-PROT-028 v1.3;
- profile presentation cannot create a `merchantVerified` state.

**PASS**

---

# 60. External-provider falsification

Scenario:

```text
Main Street phone:
01234 567890

Google profile phone:
01234 111111
```

Result:

- disagreement is an integration consistency question;
- external value does not silently overwrite Main Street truth;
- Main Street storefront continues from its authoritative profile fact;
- sync/conflict policy belongs to the integration contract.

**PASS**

---

# 61. Rejected alternatives

## A. One giant BusinessProfile aggregate

Rejected because it would own or duplicate capabilities, integrations, presentation and analytics with incompatible lifecycles.

## B. Website as source of public business data

Rejected because merchants would maintain page-specific copies and presentation would become authority.

## C. Every profile datum automatically public

Rejected because account, private location and internal operational data may legitimately remain non-public.

## D. Business category determines profile/schema/runtime

Rejected because it recreates category templates and semantic gatekeeping.

## E. Every Merchant Location is a Resource

Rejected because many locations are descriptive presence, not constrained capacity.

## F. Every merchant requires a physical address

Rejected by online consultants, publishers and mobile service operators.

## G. Service-area text directly controls runtime eligibility

Rejected because descriptive geography is not executable policy without an explicit capability contract.

## H. External provider copy is automatically authoritative

Rejected because provider and Main Street facts have separate ownership and lifecycle.

## I. Profile completeness is publication eligibility

Rejected because completeness is contextual/advisory except for explicitly scoped requirements.

---

# 62. Accepted invariants

1. Business Profile is a merchant-facing composition/management surface, not a universal semantic aggregate.
2. “Single source of truth” means one authoritative owner per fact, not one object owning every fact.
3. UI section placement does not transfer semantic ownership.
4. Merchant public descriptor is separate from legal identity and verification claims.
5. Merchant classification metadata cannot activate capabilities or determine runtime architecture.
6. Account/controller contact information is separate from merchant business/public contact points.
7. Possession of a contact value does not imply public exposure.
8. Public contact points and Enquiry are separate concerns.
9. Physical Merchant Location is optional.
10. Merchant Location is merchant-scoped but is not the tenant boundary.
11. Merchant Location is not automatically a Resource, customer address, service address or Property.
12. Merchant Location existence does not imply public exposure or verification.
13. Multiple Merchant Locations are first-class within one Merchant Scope.
14. Location-specific facts must carry explicit scope.
15. Public service-area description is not automatically executable geographic eligibility.
16. Public external-presence links are separate from authenticated ProviderConnections.
17. External-provider mappings do not merge provider and Main Street authority.
18. Branding remains presentation-owned even when edited through Business Profile.
19. Offerings, Staff, Publications, Payments and Analytics retain their own authorities.
20. SEO is normally derived from authoritative public facts rather than maintained as duplicate business truth.
21. Profile facts are not automatically public; exposure remains explicit.
22. Ordinary profile edits refresh affected projections and do not automatically trigger semantic recompilation.
23. Profile data affects executable semantics only through explicit registered binding.
24. Referenced profile facts cannot be removed in a way that leaves invalid executable or historical references.
25. External imports are evidence/proposals unless an integration contract explicitly grants write authority.
26. Profile completeness is contextual and advisory by default.
27. Profile completeness does not create universal merchant eligibility.
28. AI profile proposals require the appropriate merchant approval before becoming authoritative public content.
29. Storefront generation consumes audience-safe projections rather than interpreting a raw profile god object.
30. The same profile architecture supports physical, online, information-only, mobile-service, multi-location and hybrid merchants.

---

# 63. Relationship to MS-PROT-050

MS-PROT-050 remains authoritative for Public Business Hours temporal semantics.

This document establishes the profile/presence structures needed to scope hours meaningfully, including Merchant Location identity.

A coordinated MS-PROT-050 amendment shall define:

```text
BusinessHoursScope
    ├── MERCHANT
    └── MERCHANT_LOCATION(locationIdentity)
```

and location-qualified `CurrentBusinessOperatingStatus`.

Until that amendment is applied, MS-PROT-050 v1.1 remains authoritative for existing merchant-scope hours semantics.

---

# 64. Implementation consequence

No implementation is required during the active design-first phase.

When implementation resumes, this contract does not justify one giant:

```text
BusinessProfile.java
```

or one giant profile table.

Implementation should preserve semantic ownership and introduce types only as required by accepted slices.

A possible implementation organisation may eventually include merchant/presence/profile modules, but exact package/database design is downstream.

---

# 65. Continuous-improvement checkpoint

The principal improvement over the earlier product model is:

> **Keep one Business Profile experience while refusing one Business Profile semantic authority.**

This preserves merchant simplicity without sacrificing modular ownership.

The second important improvement is treating profile edits and compiled configuration as different lifecycle classes:

```text
public description/phone edit
    → projection refresh

explicit location reference used by capability changes
    → configuration validation/release where required
```

The third improvement is the explicit separation of descriptive service-area/location information from executable business rules.

No further material correction was found within the bounded MS-PROT-051 scope after falsification against publisher, consultant, retailer, multi-location practice, mobile tradesperson, realtor and hybrid scenarios.

---

# Governance verdict

**ACCEPTED.** Main Street shall provide a unified Business Profile management experience while retaining explicit semantic ownership for each underlying fact, capability, integration and projection. Merchant-level public information is structured, scoped, exposure-aware and reusable across storefront/SEO/integrations without creating duplicate editable copies or a universal BusinessProfile god object.