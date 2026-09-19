# MS-PROT-044 v1.3 — Merchant Offering & Product Commercial Access Classification Amendment

**Document ID:** MS-PROT-044  
**Version:** 1.3  
**Status:** ACCEPTED  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after Fundamental Vision Conformance, review, falsification and complete pre-approval presentation  
**Authority type:** Offering / Product commercial-access classification amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-044 through v1.2 within commercial-access classification only  
**Depends on:** Composite MS-PROT-044 through v1.2; composite MS-PROT-056 through v1.9; applicable Commercial, Actor Authorisation, Projection, Exposure, Storefront, Inventory, Ordering, Booking, Appointment and Payment authorities  
**Preserves:** Offering, Product and ProductVariant semantic ownership; Listing exclusion; Inventory ownership; transaction ownership; Exposure ownership; Storefront ownership; merchant authority; business-type neutrality  
**Relationship to:** `MS-PROT-056-V17-DQ-001`  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING`

---

# 0. Fundamental Vision Conformance

GrandRue's accepted FREE proposition includes:

```text
Product and service presentation
```

according to the merchant's actual business model.

MS-PROT-044 v1.2 already establishes the exact source-access boundaries through which merchants maintain Offering, Product and ProductVariant definition truth.

Commercial classification of those contracts is now required so GrandRue can later construct the exact standard Commercial catalogue without inferring permission from feature names or tier labels.

The merchant-facing meaning remains simple:

```text
FREE
    → describe and maintain
      the products and/or services
      the business offers
```

The merchant does not need to understand:

```text
Offering
ProductVariant
Commercial Access Binding
CommercialEntitlementIdentity
target families
grant graphs
```

GrandRue absorbs that internal complexity.

**Vision result:**

```text
VISION-CONFORMING
```

---

# 1. Governing Decision

Composite MS-PROT-044 SHALL establish the following commercial classifications:

| Exact owner-qualified contract | Commercial classification | Standard allocation |
|---|---|---|
| `offering/merchant-definition-observation-access@1` | No Commercial Entitlement | Plan-independent bounded observation |
| `offering/merchant-definition-authoring-access@1` | `MAINTAIN_MERCHANT_OFFERING_DEFINITION` | FREE + BUSINESS + GROWTH |
| `product/merchant-definition-observation-access@1` | No Commercial Entitlement | Plan-independent bounded observation |
| `product/merchant-definition-authoring-access@1` | `MAINTAIN_MERCHANT_PRODUCT_DEFINITION` | FREE + BUSINESS + GROWTH |

The protected authoring contracts use target family:

```text
OPERATION_ACCESS
```

Concrete `CommercialEntitlementIdentity` values remain Commercial-owned under:

```text
MS-PROT-056-V17-DQ-001
```

This amendment does not publish a catalogue.

---

# 2. Commercial Purpose Separation

The two protected commercial purposes are deliberately distinct:

```text
MAINTAIN_MERCHANT_OFFERING_DEFINITION

MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

because:

```text
Offering
    ≠
Product
```

and Product remains optional.

A merchant may therefore possess only Offering semantics without GrandRue manufacturing Product semantics.

The standard catalogue may grant both purposes at the same tier without collapsing their semantic ownership.

No generic purpose such as:

```text
MAINTAIN_CATALOGUE

MANAGE_SELLABLE_ITEMS

MANAGE_COMMERCE_DATA
```

is established.

Such abstraction would conflict with the accepted rejection of a universal authoritative catalogue model.

---

# 3. Offering Authoring

The exact contract:

```text
offering/merchant-definition-authoring-access@1
```

requires:

```text
MAINTAIN_MERCHANT_OFFERING_DEFINITION
```

The purpose covers otherwise-valid owner operations that:

```text
establish an Offering

or

materially revise Offering-owned proposition truth
```

This includes supported Offering-owned commercial proposition fields where already authorised by composite MS-PROT-044 and MS-PROT-045.

Commercial permission does not waive:

```text
Actor Authorisation
Merchant Scope
schema validation
field validation
Money semantics
currentness
concurrency
idempotency
account lifecycle
data protection
other owner preconditions
```

---

# 4. Offering Observation

The exact contract:

```text
offering/merchant-definition-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised merchant inspection of existing Offering-owned truth.

This prevents subscription loss or commercial transition from turning legitimate retained business records into commercially unreadable state.

Observation does not grant:

```text
authoring
public Exposure
Storefront delivery
cross-merchant access
bulk export
transaction execution
historical access beyond applicable retention authority
```

---

# 5. Product and ProductVariant Authoring

The exact contract:

```text
product/merchant-definition-authoring-access@1
```

requires:

```text
MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

The purpose covers otherwise-valid establishment or material revision of:

```text
Product

and, where semantically justified,

ProductVariant
```

ProductVariant receives no independent Commercial purpose merely because it has distinct operational identity.

Canonical:

```text
Product owner
    ↓
Product / ProductVariant definition access
    ↓
one Product-definition commercial purpose
```

This avoids commercial fragmentation driven by implementation structure.

---

# 6. Product Observation

The exact contract:

```text
product/merchant-definition-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised inspection of retained Product and ProductVariant definition truth.

This observation classification grants no authority over:

```text
Inventory position
Inventory claims
stock availability
Order commitments
Fulfilment state
payment state
customer history
```

Those remain separately governed.

---

# 7. FREE Allocation

Both protected authoring purposes belong to FREE.

Canonical:

```text
FREE
    → merchant may maintain
      applicable Offering definitions

    → merchant may maintain
      applicable Product/ProductVariant definitions
```

BUSINESS and GROWTH SHALL explicitly include the same grants through the standard hierarchy.

This directly implements the accepted MS-PROT-056 v1.7 allocation:

```text
Product and service presentation
    → FREE
```

without making transaction execution FREE.

---

# 8. Product/Service Presentation Does Not Mean Transaction Execution

Hard distinction:

```text
merchant may describe what it offers
        ≠
customer transaction may be committed
```

Therefore neither protected purpose grants:

```text
ESTABLISH_BOOKING_RESERVATION_COMMITMENT

ESTABLISH_APPOINTMENT_TIME_COMMITMENT

Order commitment

Payment participation

Inventory mutation

Fulfilment execution

Quotation issuance

Invoice issuance
```

A FREE merchant may describe services or products even when none of these operating capabilities is commercially enabled.

---

# 9. Inventory Boundary

Hard invariant:

```text
Product definition
    ≠
Inventory position
```

Therefore:

```text
MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

does not grant authority to:

```text
receive stock
increase stock
decrease stock
reserve stock
release stock
correct stock
receive returned stock
establish stock availability
```

Inventory retains its own exact commercial target and semantic authority.

A Product may legitimately exist without Inventory semantics.

---

# 10. Ordering Boundary

Neither Offering nor Product commercial permission grants Ordering permission.

Canonical:

```text
Product / Offering exists
        ↓
may be represented

but

Order commitment
        ↓
requires independently valid
Ordering commercial permission
```

FREE therefore supports useful merchant presentation without silently turning into free transactional commerce.

---

# 11. Booking and Appointment Boundary

An Offering describing:

```text
Haircut — £25

60-minute consultation

Room hire

Vehicle inspection
```

may be maintained under FREE.

That does not authorise:

```text
Booking commitment
Appointment commitment
Scheduling capacity reservation
```

Those remain BUSINESS-scoped where applicable under their existing exact access contracts.

---

# 12. Payment Boundary

Changing or establishing an Offering/Product definition does not establish:

```text
Payment Obligation
Amount Due
Payment execution
PaymentApplication
Refund
```

Money-valued Offering fields retain their accepted Money semantics.

Historical Payment and commitment truth is not rewritten when current source definitions change.

---

# 13. Quotation and Invoicing Boundary

FREE Offering/Product maintenance does not grant:

```text
ISSUE_QUOTATION_COMMERCIAL_OFFER

ISSUE_CUSTOMER_INVOICE
```

Quotation and Invoicing remain separately protected BUSINESS/GROWTH operations.

Likewise, issuing a Quotation or Invoice does not grant authority to mutate Offering or Product definitions.

---

# 14. Storefront and Exposure Boundary

Hard distinction:

```text
Offering/Product source truth
        ↓
Projection
        ↓
Exposure
        ↓
Storefront presentation
        ↓
website delivery
```

The protected authoring purposes do not grant:

```text
PUBLISH_STOREFRONT_COMPOSITION

SERVE_CUSTOMER_WEBSITE

USE_PLATFORM_WEBSITE_NAMESPACE

USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN
```

Conversely, Storefront or website-delivery permission does not grant source mutation.

FREE may contain all applicable grants, but each requirement must be satisfied through its exact binding.

No plan-name inference is permitted.

---

# 15. Public Observation

MS-PROT-044 v1.2 deliberately creates no generic:

```text
offering/public-source-observation-access@1

product/public-source-observation-access@1
```

This amendment does not invent either contract.

Customer/public observation remains governed by:

```text
Projection
Exposure
applicable public/customer contracts
Storefront delivery
```

The merchant-observation exemption must not be repurposed as public access.

---

# 16. Listing Remains Excluded

Listing remains outside this amendment.

No classification is established for:

```text
Listing authoring

Listing observation

Listing lifecycle
```

The following remains prohibited:

```text
Offering/Product entitlement
        ↓
implicit Listing authority
```

Listing may only enter the Commercial catalogue after its semantic ownership and lifecycle are sufficiently resolved through separately accepted authority.

---

# 17. Business-Type Neutrality

The classification applies according to semantics, not business category.

Examples:

```text
consultant
    Offering only

salon
    Offering
    optional Product

grocer
    Offering
    optional Product/ProductVariant

mechanic
    service Offerings
    optional spare-part Products

information-only publisher
    neither may be required
```

GrandRue SHALL NOT manufacture Product semantics merely because the FREE plan contains the Product-definition entitlement.

Commercial grant:

```text
≠ semantic applicability
```

---

# 18. No Mandatory Product Model

The standard FREE catalogue may grant:

```text
MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

to every standard plan without requiring every merchant to possess a Product.

Canonical:

```text
grant exists
        +
Product semantics not applicable
        ↓
no Product surface
no Product configuration
no fabricated Product
```

This preserves capability-driven dashboard composition and progressive onboarding.

---

# 19. Merchant Observation After Commercial Change

Commercial transition SHALL NOT delete or conceal legitimate retained source truth merely because a protected authoring grant changes.

Observation remains plan-independent.

Commercial loss does not:

```text
delete Offerings

delete Products

delete ProductVariants

rewrite historical versions

withdraw Exposure

change Storefront composition

cancel Orders

cancel Bookings

cancel Appointments

alter Payment Obligations
```

Any such effect requires its independently governed owner operation.

---

# 20. Authoring After Commercial Change

A new authoritative Offering or Product mutation must satisfy the current protected commercial purpose at the time of the new owner effect.

Recovery of an already committed mutation remains governed by its existing retry/idempotency semantics and must not manufacture a second mutation merely because the current grant source differs.

---

# 21. Actor Authorisation

Commercial permission never establishes actor authority.

Canonical:

```text
Commercial Entitlement valid
        +
Actor Authorisation absent
        ↓
mutation denied
```

Likewise:

```text
Actor Authorisation valid
        +
required Commercial permission absent
        ↓
protected new mutation denied
```

No staff privilege is created by this amendment.

---

# 22. AI Boundary

AI may assist with:

```text
service description
product description
candidate price wording
candidate Product forms
candidate Offering fields
```

but:

```text
AI candidate
    ≠
authoritative Offering/Product mutation
```

The authoritative owner operation still requires:

```text
semantic validity
+
Actor Authorisation
+
current Commercial permission
+
all other applicable preconditions
```

AI cannot manufacture either protected purpose.

---

# 23. Preparation Boundary

Non-authoritative:

```text
drafting
form editing
import parsing
AI suggestion
validation
preview
```

does not require a new independent Offering/Product entitlement merely because the final committed operation is protected.

This amendment does not create a generic preparation contract.

It also does not permit preparation to bypass the protected authoritative authoring boundary.

---

# 24. Target-Binding Consequence

Following acceptance, the final Commercial catalogue may create one exact canonical `CommercialEntitlementIdentity` for each protected pair:

```text
offering/merchant-definition-authoring-access@1
+
MAINTAIN_MERCHANT_OFFERING_DEFINITION
```

and:

```text
product/merchant-definition-authoring-access@1
+
MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

The identities themselves remain MS-PROT-056-owned.

Wildcards remain prohibited.

Rejected:

```text
FREE
→ all Offering operations
```

Rejected:

```text
FREE
→ all Product operations
```

Rejected:

```text
FREE
→ all catalogue management
```

Only exact owner-qualified bindings may be granted.

---

# 25. Falsification

| Scenario | Required result |
|---|---|
| Consultant maintains “60-minute consultation” | Offering authoring is commercially satisfied by FREE; no Product required |
| Information publisher has no products or services | No Offering/Product object is fabricated merely because FREE contains grants |
| Grocer defines milk without stock tracking | Product definition is permitted; Inventory semantics are not manufactured |
| Retailer defines size/colour ProductVariants | Product authoring purpose applies; no separate Variant entitlement |
| Salon edits shampoo description | Product authoring purpose applies |
| Salon increases shampoo stock | Product-definition entitlement is insufficient; Inventory authority remains required |
| Merchant describes a bookable service | Offering may be FREE; Booking/Appointment commitment remains independently protected |
| Merchant describes a product for sale | Product/Offering authoring does not grant Order commitment |
| Merchant changes a price | Existing Orders, Invoices, Payments and Quotations are not rewritten |
| FREE merchant has no website | Source definition may still exist; Storefront delivery is independent |
| Public visitor sees Product | Merchant-observation exemption is not used as public access authority |
| Merchant moves to a different commercial source | Existing Offering/Product records remain readable according to retained access |
| Staff user lacks source-authoring privilege | FREE/GROWTH grant does not create actor authority |
| AI drafts a new service | Candidate remains non-authoritative until valid owner mutation |
| Realtor needs unresolved Listing semantics | Offering/Product grants do not silently authorise Listing |

All pass without new domain semantics.

---

# 26. Alternatives

## A. Put Offering/Product authoring in BUSINESS

**REJECTED.**

MS-PROT-056 v1.7 explicitly places product and service presentation in FREE.

Requiring BUSINESS merely to define the merchant's products or services would make a useful public presence depend on the operating tier.

## B. Make all authoring plan-independent

**REJECTED.**

The service is explicitly part of the FREE commercial proposition.

Protected source authoring therefore requires explicit Commercial catalogue admission rather than being treated as an accidental exemption.

## C. One universal `MANAGE_CATALOGUE` entitlement

**REJECTED.**

GrandRue has deliberately rejected a universal authoritative catalogue abstraction.

It would collapse Offering, Product, ProductVariant and eventually Listing into inappropriate semantic gravity.

## D. Use Storefront publication permission

**REJECTED.**

Source definition and presentation composition are different authorities.

A merchant may maintain an Offering without publishing it to a website.

## E. Use one Product entitlement for Offering and Product

**REJECTED.**

Offering is sufficient for many service businesses and remains semantically independent from Product.

## F. Charge separately for ProductVariant

**REJECTED.**

ProductVariant is Product-owned and does not justify additional commercial fragmentation.

---

# 27. Hard Invariants

1. Offering and Product remain distinct semantic owners.
2. Product remains optional.
3. ProductVariant remains Product-owned.
4. Offering authoring requires `MAINTAIN_MERCHANT_OFFERING_DEFINITION`.
5. Product/ProductVariant authoring requires `MAINTAIN_MERCHANT_PRODUCT_DEFINITION`.
6. Both protected purposes belong to FREE + BUSINESS + GROWTH.
7. Merchant observation of existing Offering truth requires no independent Commercial Entitlement.
8. Merchant observation of existing Product/ProductVariant truth requires no independent Commercial Entitlement.
9. Commercial grants do not create semantic applicability.
10. Commercial grants do not create Actor Authorisation.
11. Offering/Product grants do not grant Inventory.
12. Offering/Product grants do not grant Ordering.
13. Offering/Product grants do not grant Booking or Appointment commitments.
14. Offering/Product grants do not grant Payment.
15. Offering/Product grants do not grant Quotation.
16. Offering/Product grants do not grant Invoicing.
17. Offering/Product grants do not grant public Exposure.
18. Offering/Product grants do not grant Storefront composition or website delivery.
19. Listing remains excluded.
20. ProductVariant receives no separate entitlement.
21. Commercial change does not rewrite retained source or historical commitment truth.
22. AI cannot establish Commercial or source-authoring authority.
23. Exact entitlement identities remain Commercial-owned.
24. No catalogue publication is authorised.
25. No implementation is activated.

---

# 28. Effect on MS-PROT-056-V17-DQ-001

Upon acceptance, the explicit MS-PROT-044 commercial-classification gap recorded in the Deferred Decision Register is resolved.

The path becomes:

```text
MS-PROT-044 v1.0–v1.1
    semantic Offering/Product boundary

+
MS-PROT-044 v1.2
    exact owner-qualified access contracts

+
MS-PROT-044 v1.3
    commercial classifications
        ↓
Commercial catalogue binding-ready
for Offering/Product definition scope
```

`MS-PROT-056-V17-DQ-001` remains OPEN until:

```text
all remaining owner/supporting classifications
are confirmed complete

+
concrete CommercialEntitlementIdentity values
are defined

+
every exact target binding is defined

+
FREE / BUSINESS / GROWTH grant snapshots
are complete

+
the complete manifest receives explicit approval
```

Acceptance of this amendment therefore narrows DQ-001 but does not itself close it.

---

# 29. Next Governed Design Step

After this amendment, GrandRue SHALL perform the final DQ-001 completeness audit.

If that audit finds no additional owner-classification gap, the next design authority SHALL be:

```text
MS-PROT-056 v1.10
Initial Standard Commercial Catalogue Manifest
```

That authority should:

```text
mint the concrete CommercialEntitlementIdentity set

bind every identity to one exact
owner-qualified target + protected purpose

construct explicit FREE grants

construct explicit BUSINESS grants

construct explicit GROWTH grants

prove:
FREE ⊆ BUSINESS ⊆ GROWTH

preserve all explicit
NO INDEPENDENT COMMERCIAL ENTITLEMENT classifications

contain no wildcard grants

contain no unresolved reserved capability

leave prices and quantitative allowances
under DQ-003
```

Quotation and native Invoicing may be included only through their already accepted exact BUSINESS/GROWTH classifications.

No additional capability shall be introduced merely to complete the catalogue.

---

# 30. Implementation Activation

**NONE**

Acceptance does not authorise:

```text
production entitlement checks

new Offering/Product persistence

new API endpoints

new UI

catalogue publication

Standing Free materialisation

plan migration

pricing

deployment
```

Implementation remains governed independently.

---

# 31. Governance Recommendation

**Feature Admission:** PASS — Coordination/commercial completion of existing semantics  
**Fundamental Vision Conformance:** VISION-CONFORMING  
**Business-type neutrality:** PASS  
**FREE-allocation conformity:** PASS  
**Semantic ownership preservation:** PASS  
**Product optionality:** PASS  
**Inventory separation:** PASS  
**Transaction separation:** PASS  
**Storefront/Exposure separation:** PASS  
**Anti-wildcard review:** PASS  
**AI non-authority:** PASS  
**Commercial-loss/history review:** PASS  
**Cross-domain falsification:** PASS  
**Ambiguity review:** PASS

```text
RECOMMENDATION: ACCEPT
```

**Manual approval:** GRANTED — 19 September 2026  
**Implementation activation:** NONE

---

# 32. Acceptance Statement

> **GrandRue shall include merchant maintenance of applicable Offering and Product/ProductVariant definitions within the FREE public-presence proposition without collapsing their semantic ownership or granting transaction execution. Offering authoring and Product authoring use separate exact owner-qualified targets and protected commercial purposes, while authorised observation of existing source truth remains plan-independent. These grants do not establish Inventory, Ordering, Booking, Appointment, Payment, Quotation, Invoicing, Exposure, Storefront or Listing authority and do not manufacture Product semantics for merchants that do not require them.**
