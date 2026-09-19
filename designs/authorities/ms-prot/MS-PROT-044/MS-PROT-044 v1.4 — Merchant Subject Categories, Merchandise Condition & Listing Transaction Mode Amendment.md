# MS-PROT-044 v1.4 — Merchant Subject Categories, Merchandise Condition & Listing Transaction Mode Amendment

**Document ID:** MS-PROT-044  
**Version:** 1.4  
**Status:** ACCEPTED  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after complete pre-approval presentation, Fundamental Vision Conformance, review, falsification and ambiguity review  
**Authority type:** Offering / Product / Listing classification and structured-data amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-044 through v1.3 and composite MS-PROT-045 through v1.1 within merchant subject categorisation, merchandise-condition and Listing transaction-mode scope  
**Resolves:** The initial `classification/category system` deferral retained by MS-PROT-044 v1.0 §88, only within the bounded initial scope established here  
**Preserves:** Offering, Product, ProductVariant, Listing and underlying-subject ownership; Product optionality; capability-owned schemas; Ordering, Booking, Appointment, Inventory, Payment, Quotation and Invoicing authority; Projection/Exposure separation; Storefront composition; merchant authority; business-type neutrality  
**Depends on:** Composite MS-PROT-020; composite MS-PROT-027; composite MS-PROT-037; composite MS-PROT-044 through v1.3; composite MS-PROT-045 through v1.1; composite MS-PROT-056; composite MS-PROT-057; composite MS-PROT-058; composite MS-PROT-077; MS-PROT-094; applicable Listing, Property, Actor Authorisation, Projection and Exposure authorities  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

---

# Authority Identity Preflight

```text
target branch
    development

review basis HEAD
    64bf29f25f096f33e0465650e5046fddf7575cd1

current MS-PROT-044 composition
    base + v1.1 + v1.2 + v1.3

MS-PROT-044 v1.4
    unoccupied

candidate
    MS-PROT-044 v1.4

canonical destination if approved
    designs/authorities/ms-prot/MS-PROT-044/
```

Immediately before any repository formalisation GrandRue MUST repeat the latest-head and identifier preflight required by `DESIGN-RULES.md`.

No repository authority for v1.4 existed before explicit manual approval.

---

# 0. Fundamental Vision Conformance

## 0.1 Business problem

Merchants need ordinary ways to organise and describe what they provide.

Examples include:

```text
Phones
Laptops
Hair Services
Accessories
Student Properties
Featured Homes
```

They also need structured facts such as:

```text
New
Used
Refurbished

For sale
To rent
```

Those concepts are materially different.

If GrandRue models all of them as free-form categories, the system loses business meaning.

If GrandRue hard-codes every merchant's taxonomy, merchants lose control and the platform accumulates vertical-specific structure.

The design therefore separates:

```text
merchant-created organisation
        from
platform-governed structured meaning
        from
capability-owned executable behaviour
```

---

## 0.2 Merchant-facing simplicity

The merchant experience SHOULD remain ordinary business language.

Example:

```text
Category
Phones
[+ Create category]

Condition
Refurbished

How can customers get this?
Buy
```

or:

```text
Property listing

For:
[ For sale ] [ To rent ]
```

The merchant MUST NOT need to understand:

```text
MerchantSubjectCategory
typed category assignment
schema binding
semantic enumeration identity
Projection
Exposure
capability graph
```

GrandRue absorbs that complexity.

---

## 0.3 Feature admission

The design satisfies:

**Representation Test**

GrandRue cannot faithfully represent commonly required distinctions if:

```text
category
condition
transaction mode
```

are collapsed.

**Administrative-Compression Test**

Merchants should not repeatedly create artificial categories such as:

```text
New
Used
Refurbished
Buy
Rent
```

to encode information GrandRue can represent structurally.

**Coordination Test**

Customer-facing navigation and filters must compose merchant-defined organisation with existing Product, Offering and Listing truth without taking ownership from those capabilities.

---

## 0.4 Vision result

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

Stable category identity, assignments and structured semantic values introduce internal complexity, but that complexity prevents category labels from becoming accidental executable business semantics.

---

# 1. Governing Decision

GrandRue SHALL distinguish four independent dimensions:

```text
1. SEMANTIC SUBJECT
   What is this business thing?

2. MERCHANT CATEGORY
   How does the merchant organise it?

3. STRUCTURED BUSINESS ATTRIBUTES
   What materially describes it?

4. CAPABILITY PARTICIPATION
   What may customers actually do?
```

Canonical example:

```text
Product
    iPhone 15

Category
    Phones

Merchandise Condition
    REFURBISHED

Offering
    iPhone 15 — £499

Supported operation
    Ordering
```

None of those dimensions substitutes for another.

Hard principle:

> **Categories organise. Structured semantics describe. Capabilities execute.**

---

# 2. Merchant Subject Category

A **Merchant Subject Category** is a merchant-scoped, non-executable classification identity used to organise supported merchant business subjects.

Merchant-facing wording MAY simply be:

```text
Category
```

The qualified internal term exists to distinguish it from:

```text
Merchant Profile classification
PropertyType
Merchandise Condition
Listing Transaction Mode
ProductVariant
business category
capability identity
```

A Merchant Subject Category SHALL have stable identity independent of its current display label.

Conceptually:

```text
MerchantSubjectCategory
{
    categoryIdentity
    merchantScope
    currentRevision
}
```

---

# 3. Initial Category Target Portfolio

The initial eligible target families are exactly:

```text
OFFERING
PRODUCT
LISTING
```

Therefore a Category may organise:

```text
service Offerings
simple product Offerings
durable Products
property/vehicle/other legitimate Listings
```

ProductVariant is deliberately excluded from direct category assignment in the initial model.

Rationale:

```text
ProductVariant
    is a Product-owned form
```

and normal category meaning generally belongs to the Product rather than each size/colour/pack/stock form.

If future evidence demonstrates independently useful Variant categorisation, that requires deliberate extension.

---

# 4. Category Is Not a Universal Catalogue Item

This authority MUST NOT create:

```text
CatalogueItem
SellableItem
PublishedItem
CommerceItem
```

as a universal authoritative parent.

Category Assignment references the exact semantic target type and identity.

Canonical:

```text
Category "Phones"
    → PRODUCT P100

Category "Consultations"
    → OFFERING O200

Category "Student Properties"
    → LISTING L300
```

The assignment does not flatten those targets into one semantic type.

---

# 5. Category Revision

Merchant Subject Category definition SHALL be revisioned.

A category revision contains at least:

```text
category identity
merchant scope
merchant-facing label
optional parent category identity
revision identity
predecessor revision identity where applicable
authoritative commit provenance
```

Material change to:

```text
label
parent
```

creates another revision.

An earlier revision is not destructively edited.

---

# 6. Category Labels

The merchant controls Category display labels.

Examples:

```text
Phones
Hair Services
Accessories
Featured
Summer Offers
Student Properties
```

Labels are descriptive classification values only.

A label SHALL NOT create executable semantics.

Therefore:

```text
Category named "Appointments"
    ≠ Appointment capability

Category named "Rentals"
    ≠ Booking capability

Category named "Used"
    ≠ Merchandise Condition = USED
```

Runtime systems MUST NOT infer capability or structured semantic truth from Category label text.

---

# 7. Category Hierarchy

A Merchant Subject Category MAY reference at most one parent Category within the same Merchant Scope.

This permits structures such as:

```text
Electronics
    ├── Phones
    ├── Laptops
    └── Accessories
```

or:

```text
Services
    ├── Hair
    ├── Nails
    └── Makeup
```

Requirements:

```text
parent and child belong to same Merchant Scope

category cannot parent itself

category hierarchy must remain acyclic
```

A hierarchy is organisational only.

Parentage does not create executable inheritance.

Example:

```text
Category "Bookable Services"
        ↓
child "Hair"
```

does not make Hair Offerings bookable.

---

# 8. Category Label Collision

Within one current sibling set, two active Categories SHALL NOT have the same canonical comparison label.

Canonical comparison SHALL be deterministic and at minimum account for:

```text
Unicode normalisation
surrounding whitespace
case-insensitive comparison
```

Different parents MAY contain the same display label where useful.

Example:

```text
Men
    → Accessories

Women
    → Accessories
```

is permitted because the sibling contexts differ.

---

# 9. Category Assignment

A **Merchant Category Assignment** is a durable owner-qualified relationship between:

```text
one Merchant Subject Category
+
one exact eligible semantic target
```

Conceptually:

```text
MerchantCategoryAssignment
{
    categoryIdentity
    targetFamily
    targetIdentity
    merchantScope
}
```

Target family MUST be retained explicitly.

Rejected:

```text
category target = opaque UUID
```

without semantic target type.

---

# 10. Multiple Categories

One target MAY participate in zero, one or multiple Merchant Subject Categories.

Example:

```text
Product
Refurbished iPhone 15

Categories
    Phones
    Featured
    Clearance
```

This does not make:

```text
Featured
Clearance
```

Product condition or lifecycle state.

They remain merchant-controlled organisational groupings.

---

# 11. Category Ancestry

If a Product is directly assigned to:

```text
Phones
```

whose parent is:

```text
Electronics
```

a read/presentation projection MAY derive:

```text
Product appears beneath Electronics
```

through Category ancestry.

That does not create a second authoritative assignment.

Hard distinction:

```text
direct Category Assignment
    ≠ derived ancestor membership
```

---

# 12. Category Rename

Renaming a Category SHALL retain:

```text
category identity
target assignments
historical revisions
```

Example:

```text
Mobile Phones
        ↓ rename
Phones
```

does not require reassignment of every Product.

Historical evidence retains the previous label revision.

---

# 13. Category Retirement

A Category may be retired.

Retirement:

```text
prevents new current assignments
removes it from ordinary active category selection
preserves identity
preserves historical revisions
preserves historical assignment interpretation
```

Retirement MUST NOT:

```text
delete Products
delete Offerings
delete Listings
withdraw public Exposure
cancel transactions
change condition
change transaction mode
```

The category identity is not reused after retirement.

A merchant may later create a new Category with equivalent wording, but it receives a new identity.

---

# 14. Category Assignment Removal

Removing a current Category Assignment removes only the organisational relationship.

It MUST NOT mutate the target.

Example:

```text
Product P100
    assigned to "Clearance"

merchant removes assignment
```

does not:

```text
change price
change Inventory
hide Product
delete Offering
cancel Order
change Product lifecycle
```

---

# 15. Category and Exposure

Category membership does not establish public visibility.

Canonical:

```text
source subject
    +
Category Assignment
    +
independent Projection/Exposure decision
        ↓
possible customer-facing categorised representation
```

A private or withheld Product remains private/withheld even if its Category is visible.

A customer-facing Category projection MUST NOT expose a target that independently fails Exposure.

---

# 16. Category and Storefront

Storefront may use Merchant Subject Categories for:

```text
navigation
grouping
browse pages
filtering
merchant-specific emphasis
```

but Storefront remains a presentation layer.

Category data does not become Storefront-owned merely because it appears in navigation.

Likewise Storefront composition cannot mutate Category truth.

---

# 17. Merchant Classification Entry Is Separate

The existing Merchant Profile:

```text
MerchantClassificationEntryV1
```

remains separate.

It classifies the merchant/business itself through:

```text
CATEGORY
DISCOVERY_TAG
CONTEXTUAL_DESCRIPTOR
```

It MUST NOT be reused as the Product/Offering/Listing category system.

Canonical distinction:

```text
Merchant Profile classification
    "Hairdresser"

Merchant Subject Category
    "Hair Treatments"
```

or:

```text
Merchant Profile classification
    "Electronics retailer"

Merchant Subject Category
    "Phones"
```

---

# 18. Merchandise Condition

`New`, `Used` and `Refurbished` are not ordinary categories.

GrandRue SHALL recognise a structured reusable semantic concept:

```text
MerchandiseCondition
```

Initial registered values are exactly:

```text
NEW
USED
REFURBISHED
```

These are platform-controlled semantic identifiers.

Merchant-facing labels may be localised or presentation-adapted.

The merchant selects supported values; the merchant does not invent new executable enum members.

---

# 19. Merchandise Condition Applicability

Merchandise Condition is applicable only where the relevant schema registers it.

It is not universal.

Examples where it may apply:

```text
retail goods
vehicles
equipment
second-hand goods
refurbished electronics
```

Examples where it normally does not apply:

```text
haircut
consultation
appointment
restaurant service
business hours
```

Absence of condition does not mean:

```text
NEW
```

or:

```text
USED
```

It means the relevant source does not currently represent a condition value.

---

# 20. Condition Ownership

Where an independent Product exists and condition is Product-owned truth:

```text
Product/ProductVariant
    owns condition
```

An Offering referencing that Product MUST NOT independently duplicate conflicting authoritative condition truth merely for presentation.

Where no independent Product is justified and the Offering itself is the sufficient durable subject, an Offering schema MAY carry Merchandise Condition where explicitly registered.

Therefore:

```text
Product is optional
```

remains preserved.

GrandRue shall not manufacture Product merely to store condition.

---

# 21. Condition Does Not Automatically Create ProductVariant

Hard invariant:

```text
different condition
    ≠ automatically ProductVariant
```

Suppose:

```text
Laptop
NEW

Laptop
REFURBISHED
```

If those forms have materially independent:

```text
stock
SKU/barcode
price
availability
fulfilment behaviour
```

then existing ProductVariant rules may justify distinct Product forms.

If not, a Variant MUST NOT be created merely because the condition differs.

Existing rule remains:

> **Selectable or descriptive difference does not by itself imply ProductVariant identity.**

---

# 22. Physical-Unit Condition

A condition applying to one specific physical unit MUST NOT automatically be pushed upward onto the Product.

Example:

```text
Product
Laptop Model X

unit A
excellent used condition

unit B
visible cosmetic damage
```

Where condition varies by independently tracked physical unit, the appropriate future unit/Inventory/Resource semantics own that exact unit condition.

This authority does not create a serialised-unit model.

It only prevents Product-level condition from becoming false aggregate truth.

---

# 23. Condition and Category Separation

The following may coexist:

```text
Category
Phones

Condition
REFURBISHED
```

Customer presentation may therefore produce:

```text
Phones
    filter: Refurbished
```

without requiring:

```text
Category = Refurbished
```

If a merchant manually creates a Category named:

```text
Refurbished Deals
```

that remains an organisational Category.

It does not establish:

```text
MerchandiseCondition = REFURBISHED
```

for its members.

---

# 24. Listing Transaction Mode

For Listing contexts that register the concept, GrandRue SHALL recognise:

```text
ListingTransactionMode
```

Initial values are exactly:

```text
SALE
RENT
```

These values represent the Listing proposition.

They are not Category labels.

---

# 25. Customer Labels for Listing Transaction Mode

Semantic values and display wording remain distinct.

Examples:

```text
SALE
    merchant-facing: For sale
    customer navigation: Buy / For sale

RENT
    merchant-facing: To rent
    customer navigation: Rent / To rent
```

Presentation wording does not change the semantic value.

GrandRue MUST NOT create separate semantic enum values merely because different locales use different words.

---

# 26. Listing Transaction Mode Ownership

Transaction Mode belongs to the Listing proposition, not the underlying Property merely because the Listing concerns a Property.

Canonical:

```text
Property P100
    address
    bedrooms
    bathrooms
    property type

        ↓ SUBJECT

Listing L200
    transactionMode = RENT
    askingRent = £1,200/month
```

A later Listing concerning the same Property may represent a different proposition without changing Property identity.

---

# 27. Realtor Flow

A realtor-facing workflow may ask:

```text
What is this property available for?

[ For sale ]   [ To rent ]
```

If `For sale`:

```text
ListingTransactionMode = SALE
```

If `To rent`:

```text
ListingTransactionMode = RENT
```

The system may then request fields appropriate to the registered Listing schema.

Examples:

```text
SALE
    asking sale price

RENT
    asking rent
    applicable rental-period representation
```

Exact broader Property and Listing schemas remain independently governed.

---

# 28. Transaction Mode Does Not Execute a Transaction

Hard invariant:

```text
ListingTransactionMode = SALE
    ≠ Order capability

ListingTransactionMode = RENT
    ≠ Booking capability
    ≠ tenancy agreement
    ≠ lease execution
```

Transaction Mode describes the Listing proposition.

It does not grant customer commitment authority.

For a realtor:

```text
customer sees Listing
        ↓
Enquiry SUBJECT → Listing
```

remains valid without manufacturing an Order or Booking.

---

# 29. Buy/Rent Outside Listing Semantics

GrandRue SHALL NOT introduce one universal:

```text
transactionMode = BUY | RENT
```

for every Product, Offering, Property and Listing.

Example equipment merchant:

```text
Product
Excavator X20

    ├── Offering
    │   Buy X20
    │   supported operation: Ordering
    │
    └── Offering
        Rent X20
        supported operation: applicable Booking/resource semantics
```

Here:

```text
Buy
Rent
```

represent different merchant commercial propositions and capability participation.

They are not automatically Listing Transaction Mode.

This preserves Offering and capability ownership.

---

# 30. Services

Service grouping uses Merchant Subject Category.

Example:

```text
Category
Hair Services

Offerings
    Haircut
    Colouring
    Braiding
```

A Category does not determine whether an Offering supports:

```text
Appointment
Booking
Enquiry
Quotation
```

Those remain independently configured/authorised capability relationships.

---

# 31. Products

Example:

```text
Product
iPhone 15

Category
Phones

Condition
REFURBISHED

Offering
£499

Ordering
enabled through independently valid semantics
```

The customer's page may compose these facts.

No single object owns everything displayed.

---

# 32. Hybrid Merchant

A salon may legitimately have:

```text
Categories

Services
    Hair
    Nails

Products
    Hair Care
```

and:

```text
Offering
Haircut

Product
Professional Shampoo
```

The same Category mechanism supports both without introducing:

```text
SalonCategory
RetailCategory
ServiceCategory
```

subsystems.

---

# 33. Category Management Authority

For the initial authority:

```text
create category
rename/reparent category
retire category
```

require the current ACTIVE Merchant Controller.

This amendment creates no new delegated staff privilege.

A future accepted actor-authority amendment may delegate category-administration operations without redefining Category semantics.

---

# 34. Category Assignment Authority

Assigning or removing a Category from a target requires an actor who is independently authorised to maintain that target's merchant definition.

Canonical:

```text
actor authorised to maintain Offering
    → may classify that Offering

actor not authorised to maintain Product
    → cannot alter Product categorisation
```

Category Assignment does not enlarge underlying target authority.

Where Listing authoring authority is not yet admitted, this amendment does not manufacture it merely to support Listing categorisation.

---

# 35. Commercial Classification

Merchant Subject Category maintenance and assignment are supporting organisation operations.

They require:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

They do not create commercially useful business functionality without an applicable underlying target.

Therefore this amendment creates no new `CommercialEntitlementIdentity`.

Where Merchandise Condition is mutated as Product/ProductVariant definition truth, existing:

```text
product/merchant-definition-authoring-access@1
+
MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

continues to apply.

Where Merchandise Condition legitimately belongs to an Offering-only representation:

```text
offering/merchant-definition-authoring-access@1
+
MAINTAIN_MERCHANT_OFFERING_DEFINITION
```

continues to apply.

Listing Transaction Mode receives no commercial classification through this amendment because Listing authoring/access authority remains independently incomplete.

No DQ-001 commercial gap is created for Category maintenance.

---

# 36. AI Assistance

AI MAY infer candidates from merchant language.

Example:

```text
"Refurbished iPhone 15, £499, phones"
```

may produce candidate interpretation:

```text
Category = Phones
Condition = REFURBISHED
Price = £499
```

Example:

```text
"2-bedroom flat to rent in Swansea for £1,200 a month"
```

may produce candidate:

```text
property type = FLAT
bedrooms = 2
ListingTransactionMode = RENT
asking rent = £1,200/month
```

But AI MUST NOT:

```text
invent a new MerchandiseCondition enum
invent ListingTransactionMode values
silently create a capability
treat a Category label as condition
treat "rent" as Booking authority
commit inferred authoritative values without applicable merchant/actor approval
```

Canonical:

```text
merchant language
    ↓
AI candidate interpretation
    ↓
registered schema validation
    ↓
merchant confirmation where materially inferred
    ↓
authoritative owner mutation
```

---

# 37. Inline Category Creation

Merchant software MAY allow Category creation while creating/editing a Product, Offering or Listing.

Example:

```text
Category
[ Phones ▼ ]

+ Create new category
```

Inline UX does not alter authority.

Conceptually the system still performs:

```text
Create Merchant Subject Category
        ↓
establish Category identity
        ↓
assign exact target
```

The interface may compress those steps without collapsing their semantics.

---

# 38. Search and Discovery

Search/discovery MAY index:

```text
Category assignment
Merchandise Condition
Listing Transaction Mode
other authorised structured fields
```

for filtering and navigation.

Search infrastructure does not become authoritative owner.

Returned results MUST preserve enough information to distinguish:

```text
Category
Condition
Transaction Mode
semantic target type
```

A search document containing `"used"` does not establish Merchandise Condition.

---

# 39. Customer-Facing Filters

A customer website may legitimately expose independent controls such as:

```text
Category
    Phones
    Laptops

Condition
    New
    Used
    Refurbished
```

or:

```text
Buy | Rent
```

for applicable Listing projections.

Those controls are projections over structured source truth.

They are not authoritative mutation state.

---

# 40. Category Ordering

Exact merchant-facing ordering of Categories is presentation configuration and is not made semantic business truth by this amendment.

Examples:

```text
display order
menu position
featured position
mobile collapsing
```

remain downstream presentation concerns.

Category identity and hierarchy must not depend on screen position.

---

# 41. Category Does Not Determine Price

Category membership does not establish:

```text
price
discount
tax
commercial term
```

A Category called:

```text
Sale
Discounted
Clearance
```

does not change monetary truth.

Any actual commercial-price change remains owned by the relevant Offering/Listing/Money semantics.

---

# 42. Category Does Not Determine Inventory

Category:

```text
In Stock
Out of Stock
Low Stock
```

if merchant-created, remains descriptive grouping only.

Inventory authority MUST NOT infer position or availability from it.

Canonical:

```text
Category wording
    ≠ Inventory Position
```

---

# 43. Category Does Not Determine Lifecycle

Category:

```text
New Arrivals
Archived
Sold
Available
```

does not establish authoritative lifecycle.

Examples:

```text
Listing in "Sold" category
    ≠ sale fact established

Product in "Archived" category
    ≠ Product retired

Offering in "Available" category
    ≠ Operational Eligibility
```

---

# 44. Category Does Not Determine Customer Operation

No Category name may grant:

```text
Order
Book
Schedule
Pay
Request quotation
Receive invoice
Enquire
```

Customer action participation remains established through accepted source/capability relationships and runtime authority.

---

# 45. Historical Commitments

Changes to:

```text
Category
Condition
current Listing presentation
```

MUST NOT rewrite existing historical commitments.

Example:

```text
Order placed when item condition = USED
```

is not changed if current Product condition later changes.

Existing Orders, Quotations, Invoices, Bookings, Appointments and Payment Obligations preserve their own applicable historical truth/provenance.

---

# 46. Category Concurrency and Idempotency

Authoritative Category structure operations SHALL use:

```text
logical request identity
expected current revision where applicable
merchant scope
acting authority
```

A successfully committed logical request replayed with identical input returns the committed result.

Reuse with materially different input is rejected.

Concurrent modifications against the same expected Category revision SHALL NOT silently overwrite one another.

---

# 47. Assignment Idempotency

For one exact:

```text
Category identity
+
target family
+
target identity
```

at most one current assignment exists.

Repeated request to establish an already-current identical assignment SHALL converge without duplicating the relationship.

Repeated removal of an already-absent assignment SHALL NOT create a second semantic effect.

---

# 48. Reparenting Safety

Category reparenting SHALL fail if it would:

```text
create a cycle
cross Merchant Scope
reference a retired/non-current parent where prohibited
```

A failed reparent operation leaves the current hierarchy unchanged.

---

# 49. Failure Separation

Applicable failure outcomes MUST preserve distinction among at least:

```text
NOT_AUTHORISED
CATEGORY_NOT_FOUND
TARGET_NOT_FOUND
TARGET_TYPE_NOT_SUPPORTED
CATEGORY_RETIRED
DUPLICATE_SIBLING_LABEL
HIERARCHY_CYCLE
EXPECTED_REVISION_CONFLICT
SCHEMA_VALUE_NOT_SUPPORTED
COMMERCIAL_PERMISSION_DENIED
COMMERCIAL_PERMISSION_UNRESOLVED
TECHNICAL_FAILURE
```

where relevant to the exact operation.

A condition-value validation failure is not a Category failure.

A Category failure is not an Inventory failure.

---

# 50. Falsification — Consultant

Merchant offers:

```text
Structural Consultation
```

Category:

```text
Professional Services
```

No Product required.

No Listing required.

Appointment capability remains independent.

**PASS**

---

# 51. Falsification — Hair Salon

Merchant creates:

```text
Services
    Hair
    Nails

Products
    Hair Care
```

`Haircut` is an Offering.

`Professional Shampoo` may be a Product.

The same Category mechanism works without salon-specific category code.

**PASS**

---

# 52. Falsification — Refurbished Phone

Merchant enters:

```text
iPhone 15
Category = Phones
Condition = REFURBISHED
```

Expected:

```text
Phones
    = Category

REFURBISHED
    = structured Merchandise Condition
```

No `Refurbished` category is required.

**PASS**

---

# 53. Falsification — Merchant Creates Category Named Used

Merchant deliberately creates:

```text
Category = Used Deals
```

A Product inside it has:

```text
Condition = NEW
```

Expected:

Category wording does not override the Product condition.

The system must not infer `USED`.

**PASS**

---

# 54. Falsification — Vehicle Dealer

Vehicle Listing:

```text
Category = SUVs
Condition = USED
```

Condition describes applicable merchandise/subject representation.

Category organises customer browsing.

Listing remains a distinct proposition.

**PASS**

---

# 55. Falsification — Realtor Rent

```text
Property P100
    type = FLAT
    bedrooms = 2

Listing L200
    transactionMode = RENT
    askingRent = £1,200/month

Category
    Student Properties
```

Expected:

```text
PropertyType
    ≠ Category

RENT
    ≠ Category

Student Properties
    = merchant Category
```

Enquiry may retain:

```text
SUBJECT → Listing L200
```

**PASS**

---

# 56. Falsification — Realtor Sale

Same Property later participates in a different legitimate Listing:

```text
transactionMode = SALE
```

Property identity is unchanged.

This authority does not decide whether SALE and RENT Listings may coexist simultaneously for the same Property; the applicable Listing/domain policy retains that question.

**PASS**

---

# 57. Falsification — Equipment Buy and Rent

Equipment merchant offers one durable Product through:

```text
Buy Offering
Rent Offering
```

Expected:

`ListingTransactionMode` is not automatically used.

Ordering and rental/Booking semantics remain capability-owned.

**PASS**

---

# 58. Falsification — ProductVariant

Product:

```text
T-shirt
```

Variants:

```text
Black / Medium
White / Medium
```

Category:

```text
Clothing
```

Expected:

Category applies at Product level.

No duplicate Category assignment is required for every Variant.

**PASS**

---

# 59. Falsification — Condition-Specific Independent Stock

Merchant separately stocks:

```text
iPhone 15 / NEW
iPhone 15 / REFURBISHED
```

with distinct SKU, stock and price.

Expected:

Existing ProductVariant rules may justify independent forms.

This amendment does not automatically create them.

**PASS**

---

# 60. Falsification — Specific Used Unit

Two physical units of one Product have materially different physical condition.

Expected:

The Product is not forced to carry one false condition.

Future legitimate unit-level semantics retain responsibility.

**PASS**

---

# 61. Falsification — Category Retirement

Merchant retires:

```text
Christmas Offers
```

Expected:

Products and Offerings survive.

Orders survive.

Exposure is not automatically withdrawn.

Historical Category assignment remains interpretable.

**PASS**

---

# 62. Falsification — Category Rename

```text
Mobiles
    ↓
Phones
```

Expected:

same Category identity  
same target assignments  
new revision  
old label preserved historically

**PASS**

---

# 63. Falsification — Hidden Product

Product belongs to:

```text
Phones
```

but fails public Exposure.

Expected:

public Category page does not expose it merely because the Category is public.

**PASS**

---

# 64. Falsification — AI Misclassification

Merchant writes:

```text
"Like new phone"
```

AI proposes:

```text
USED
```

Merchant rejects proposal.

Expected:

no authoritative condition change.

AI confidence cannot create Merchandise Condition truth.

**PASS**

---

# 65. Falsification — Information Publisher

Information-only merchant has:

```text
Publication
Enquiry
```

and no Offering/Product/Listing semantics.

Expected:

no Category configuration is required merely because GrandRue knows Merchant Subject Categories.

**PASS**

---

# 66. Rejected Alternative — Use Merchant Profile Categories

Rejected.

Merchant Profile classification answers:

```text
what kind of merchant/business is this?
```

Merchant Subject Category answers:

```text
how does this merchant organise these business subjects?
```

They are not interchangeable.

---

# 67. Rejected Alternative — Everything Is a Category

Rejected.

This would turn:

```text
New
Used
Refurbished
Sale
Rent
PropertyType
```

into uncontrolled merchant labels and destroy structured meaning.

---

# 68. Rejected Alternative — Hard-Code Universal Industry Categories

Rejected.

GrandRue SHALL NOT require one global hierarchy such as:

```text
Electronics
Fashion
Property
Beauty
Food
```

as authoritative merchant taxonomy.

Merchants control ordinary Category labels within the bounded non-executable model.

---

# 69. Rejected Alternative — Merchant-Defined Executable Enums

Rejected.

Merchants cannot invent semantic values such as:

```text
SUPER_NEW
LEASE_AND_BUY
BOOKABLE_PLUS
```

and expect them to alter execution.

New structured semantics require governed schema evolution.

---

# 70. Rejected Alternative — Condition Is Variant

Rejected.

Condition alone does not demonstrate independent ProductVariant identity.

Existing operational-identity tests continue to govern.

---

# 71. Rejected Alternative — One Universal Transaction Mode

Rejected.

```text
SALE / RENT
```

is appropriate for registered Listing contexts.

Other merchant operations are represented by their actual Offering/capability semantics.

A universal `BUY/RENT/BOOK/QUOTE` field would duplicate capability participation.

---

# 72. Rejected Alternative — Category Drives Capability

Rejected.

Categories are intentionally non-executable.

Runtime authority cannot depend on arbitrary merchant wording.

---

# 73. Hard Invariants

1. Merchant Subject Category is merchant-scoped.
2. Category identity is stable independently of display label.
3. Category is non-executable.
4. Initial target families are exactly Offering, Product and Listing.
5. ProductVariant is not directly categorised in the initial portfolio.
6. Category target type and identity are retained explicitly.
7. One target may belong to multiple Categories.
8. Category hierarchy is same-merchant and acyclic.
9. Category ancestry may be derived without manufacturing direct assignment.
10. Category rename preserves identity and assignments.
11. Category retirement does not mutate classified targets.
12. Category retirement preserves history.
13. Category wording never creates capability semantics.
14. Category wording never creates Inventory truth.
15. Category wording never creates lifecycle truth.
16. Category wording never creates price truth.
17. Merchant Profile classification remains separate.
18. Merchandise Condition is structured semantics, not Category.
19. Initial Merchandise Condition values are `NEW`, `USED`, `REFURBISHED`.
20. Condition absence is not interpreted as a particular value.
21. Condition does not automatically create ProductVariant.
22. Unit-specific condition must not be falsely promoted to Product truth.
23. Listing Transaction Mode is structured semantics, not Category.
24. Initial Listing Transaction Modes are `SALE` and `RENT`.
25. Listing Transaction Mode belongs to Listing, not Property.
26. SALE does not grant Ordering.
27. RENT does not grant Booking, tenancy or lease execution.
28. General Product/Offering buy/rent behaviour is not represented by Listing Transaction Mode merely because the words match.
29. Category membership does not grant Exposure.
30. Storefront presentation does not own Category truth.
31. Search does not own Category, condition or transaction-mode truth.
32. AI may propose but may not invent or commit structured semantics autonomously.
33. Category maintenance creates no independent Commercial Entitlement.
34. Product/Offering condition mutation remains under existing v1.3 commercial authoring boundaries where applicable.
35. Listing commercial-authoring authority is not created by this amendment.
36. Historical commitments are not rewritten by later categorisation or condition changes.
37. No universal CatalogueItem is introduced.
38. No implementation is activated.

---

# 74. Deferred Decisions Preserved

This amendment does not resolve:

```text
complete Listing lifecycle

complete Property schema

complete Listing schema beyond
the bounded transaction-mode concept

simultaneous SALE + RENT Listing policy
for the same underlying Property

property sale transaction execution

property tenancy / lease semantics

unit-level merchandise condition model

future MerchandiseCondition values

ProductVariant rules beyond existing authority

public Category layout

Category display ordering

multilingual Category-label management

category-specific SEO behaviour

future direct ProductVariant categorisation

categories for Publication / Opportunity

merchant-defined non-category tags

delegated staff Category-administration privilege

full Listing commercial-access classification

prices
allowances
implementation activation
```

Those decisions MUST NOT be inferred from this authority.

---

# 75. Effect on MS-PROT-044 Deferred Classification Work

Upon acceptance, the generic initial:

```text
classification/category system
```

deferral retained by MS-PROT-044 v1.0 is resolved for the bounded portfolio:

```text
Merchant Subject Category

targets:
    Offering
    Product
    Listing

plus structured separation from:
    Merchandise Condition
    Listing Transaction Mode
```

This does not mean every future classification problem is solved.

A new classification dimension still requires its appropriate owner/schema authority.

---

# 76. Effect on MS-PROT-045

MS-PROT-045's rules remain authoritative:

```text
schemas are capability/platform owned

merchant owns values

merchant does not invent executable field meaning

enumerated semantic values remain registered

new supported values require governed schema evolution
```

This amendment concretely applies those rules to:

```text
MerchandiseCondition

ListingTransactionMode
```

No arbitrary metadata map is introduced.

---

# 77. Effect on the Commercial Catalogue

This amendment does not mint a new protected Commercial purpose.

Category maintenance/assignment is explicitly:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

Product/Offering definition mutation continues to use the already accepted MS-PROT-044 v1.3 purposes.

Therefore acceptance does not reopen the Offering/Product commercial classification already completed by v1.3.

The final MS-PROT-056 catalogue must not invent another:

```text
CATEGORY_MANAGEMENT
```

entitlement merely because the supporting Category system exists.

---

# 78. Implementation Activation

**NONE**

Acceptance does not authorise:

```text
new database tables
new Java classes
new Category APIs
new Product fields
new Listing endpoints
new merchant UI
new Storefront filters
new search indexing
new commercial catalogue publication
```

Implementation requires separately governed implementation promotion.

---

# 79. Governance Recommendation

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`  
**Merchant simplicity:** PASS  
**Business-type neutrality:** PASS  
**Category / structured-semantics separation:** PASS  
**Merchant Profile classification separation:** PASS  
**Product optionality:** PASS  
**Variant boundary:** PASS  
**Listing / Property ownership separation:** PASS  
**Commercial-catalogue non-expansion:** PASS  
**Projection / Exposure separation:** PASS  
**AI non-authority:** PASS  
**Concurrency / idempotency review:** PASS  
**Historical-truth review:** PASS  
**Cross-domain falsification:** PASS  
**Ambiguity review:** PASS

```text
RECOMMENDATION: ACCEPT
```

**Manual approval:** GRANTED — 19 September 2026

---

# 80. Acceptance Statement

> **GrandRue shall distinguish merchant-created organisational Categories from structured business attributes and executable capability semantics. Merchant Subject Categories provide stable merchant-scoped, non-executable grouping of Offerings, Products and Listings and may form an acyclic merchant-defined hierarchy without changing the business meaning, lifecycle, availability, price, inventory, exposure or executable operations of the classified target. `NEW`, `USED` and `REFURBISHED` are initial registered Merchandise Condition values rather than ordinary Category semantics, while `SALE` and `RENT` are initial Listing Transaction Mode values describing an applicable Listing proposition rather than Category labels or transaction-execution authority. ProductVariant remains justified only by materially independent operational identity, and Listing Transaction Mode does not become a universal buy/rent abstraction for Products or Offerings. AI may suggest Category assignments and registered structured values but cannot invent semantic enum members, activate capabilities or commit materially inferred meaning without applicable merchant authority. Category maintenance introduces no independent Commercial Entitlement and does not reopen the Offering/Product commercial classifications accepted in MS-PROT-044 v1.3.**
