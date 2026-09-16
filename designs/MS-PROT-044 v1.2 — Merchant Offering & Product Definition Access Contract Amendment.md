# MS-PROT-044 v1.2 — Merchant Offering & Product Definition Access Contract Amendment

**Document ID:** MS-PROT-044  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 — explicit manual approval of the complete authority in ChatGPT  
**Authority type:** Offering / Product owner-qualified access-contract amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-044 through v1.1 within Offering, Product and ProductVariant source-access classification only  
**Depends on:** Composite MS-PROT-044 through v1.1; MS-PROT-020; MS-PROT-023; MS-PROT-031; composite MS-PROT-045 through v1.1; applicable Money, Data Protection, Actor Authorisation, Runtime Access and Unified Interaction authorities  
**Preserves:** Offering, Product, ProductVariant, Listing, Inventory, Ordering, Booking, Appointment, Publication, Exposure, Storefront, Media and Payment ownership boundaries  
**Commercial effect:** **NONE — no Commercial Entitlement, tier placement, grant or catalogue binding is established by this amendment**  
**Relationship to:** `MS-PROT-056-V17-DQ-001` — supplies prerequisite exact target access contracts for later commercial classification but does not resolve or narrow the deferred commercial decision  
**Implementation activation:** **NONE**

---

# 1. Purpose

Composite MS-PROT-044 already establishes:

```text
Offering
    = merchant-controlled proposition

Product
    = optional durable good/material subject

ProductVariant
    = optional Product-owned form
      where independent operational identity exists
```

It does not yet supply exact owner-qualified access contracts through which later commercial authority can refer to merchant observation or authoring of those source facts.

This amendment establishes those bounded access contracts.

It does not:

```text
invent a Product for every Offering

make ProductVariant universal

define Listing lifecycle

define a new Offering lifecycle

define transport endpoints

define persistence representation

define Commercial Entitlements

allocate subscription tiers

activate implementation
```

---

# 2. Governing Decision

The following exact owner-qualified access contracts SHALL be recognised:

| Access contract | Owner-qualified purpose |
|---|---|
| `offering/merchant-definition-observation-access@1` | Observe existing Offering-owned merchant proposition truth |
| `offering/merchant-definition-authoring-access@1` | Establish or materially revise Offering-owned merchant proposition truth |
| `product/merchant-definition-observation-access@1` | Observe existing Product/ProductVariant definition truth |
| `product/merchant-definition-authoring-access@1` | Establish or materially revise Product/ProductVariant definition truth |

These access contracts identify semantic access boundaries.

They do not themselves:

```text
grant Actor Authorisation
grant Commercial Entitlement
make an operation applicable
establish Exposure
establish public visibility
establish Inventory authority
establish transaction authority
create provider readiness
```

A later commercial authority MAY reference these contracts only within the exact scope established here.

---

# 3. Offering Ownership

Offering remains the owner of the merchant-controlled proposition describing what the merchant makes available for a supported customer interaction.

Applicable Offering-owned meaning may include otherwise-accepted source facts such as:

```text
merchant proposition identity
merchant description
supported commercial proposition values
Offering-owned commercial terms
other registered Offering-schema fields
```

This amendment does not create new Offering fields.

The exact governing schema and FieldDefinitions remain constrained by composite MS-PROT-045.

---

# 4. Offering Authoring Access

`offering/merchant-definition-authoring-access@1` SHALL qualify an otherwise-valid owner operation whose authoritative effect:

```text
establishes a new Offering proposition
```

or:

```text
materially changes Offering-owned proposition truth
```

The access contract does not establish that the requested mutation is semantically valid.

The owning operation must still satisfy all applicable:

```text
Merchant Scope
Actor Authorisation
schema
field
value
Money
currentness
concurrency
idempotency
account lifecycle
data-protection
other owner requirements
```

Where an accepted Offering operation uses field-targeted mutation, MS-PROT-045 remains authoritative.

The access contract MUST NOT become generic permission to mutate arbitrary Offering storage.

---

# 5. Offering Observation Access

`offering/merchant-definition-observation-access@1` qualifies otherwise-authorised merchant observation of existing Offering-owned source truth.

It does not itself grant:

```text
public observation
customer observation
cross-Merchant observation
historical retention
bulk export
Exposure
Storefront delivery
```

Possession of an Offering identifier is not observation authority.

Customer/public representations remain governed through the applicable Projection, Exposure and delivery authorities.

---

# 6. Product Ownership

Product remains optional.

A Product exists only where the accepted MS-PROT-044 invariants justify durable merchant-controlled good/material identity independently from one particular Offering.

`product/merchant-definition-authoring-access@1` MUST NOT cause the system to create Product merely because:

```text
something can be ordered

something appears on a website

something has a price

something has selectable options

Inventory may later apply
```

Offering-only representation remains valid where no separate Product meaning is required.

---

# 7. ProductVariant Boundary

ProductVariant remains Product-bounded.

For this amendment:

```text
product/merchant-definition-authoring-access@1
```

also qualifies otherwise-valid authoring of a Product-owned ProductVariant definition.

No independent:

```text
product-variant/... commercial/access family
```

is created merely because ProductVariant has separate operational identity.

This avoids turning implementation structure into unnecessary access or commercial fragmentation.

The Product owner remains responsible for proving that ProductVariant identity is justified under v1.1.

---

# 8. Product Observation Access

`product/merchant-definition-observation-access@1` qualifies otherwise-authorised merchant observation of Product and ProductVariant definition truth.

It grants no authority over:

```text
Inventory quantities
Inventory claims
stock availability
Order commitments
Fulfilment state
physical serialised instances
customer transaction history
```

Those facts remain independently owned.

---

# 9. Product Authoring Is Not Inventory Mutation

Hard invariant:

```text
Product definition
    ≠
Inventory position
```

Therefore:

```text
product/merchant-definition-authoring-access@1
```

MUST NOT authorise:

```text
increase stock
decrease stock
reserve stock
release stock
receive returned stock
establish availability
correct Inventory position
```

Those operations remain governed by composite MS-PROT-058 and its applicable access authority.

A Product or ProductVariant may exist without Inventory applicability.

---

# 10. Offering Authoring Is Not Transaction Authority

Hard invariant:

```text
Offering exists
    ≠
customer may execute every operation associated with it
```

Offering authoring therefore grants no authority to:

```text
create Order
create Booking
create Appointment
establish Payment Obligation
reserve capacity
establish Inventory Claim
execute Fulfilment
```

Those operations remain subject to their independently owned semantics and access requirements.

A merchant may therefore describe and maintain an Offering without receiving commercial or operational permission for every downstream transaction type.

---

# 11. Price and Money Boundary

Where a price or another monetary proposition is an accepted Offering-owned field:

```text
Offering owns the proposition field
```

while accepted Money semantics govern its monetary representation.

Changing an Offering price does not:

```text
rewrite an existing Order
rewrite an existing Booking
rewrite an existing Appointment
rewrite an existing Payment Obligation
```

Historical commitments retain their own accepted commercial truth.

This amendment creates no pricing engine, discount model, tax authority or customer Payment authority.

---

# 12. Product-to-Offering Relationships

This amendment does not invent a new generic mutation operation for relationships between Product and Offering.

Where an accepted relationship exists:

```text
Product
    participates in
Offering
```

its establishment, amendment and termination require the applicable accepted relationship/owner authority.

Neither access contract may be interpreted as generic authority to construct arbitrary Product–Offering graphs.

---

# 13. Publication, Exposure and Storefront Separation

Hard distinction:

```text
Offering/Product source authoring
        ≠
public Exposure
        ≠
Storefront composition
        ≠
website delivery
```

An Offering may remain valid while not publicly exposed.

A Product may remain valid while not appearing on a storefront.

Conversely, a Storefront presentation decision does not acquire mutation authority over Offering or Product source truth.

Existing Projection, Exposure, Storefront composition and website-delivery authorities remain unchanged.

---

# 14. Public Observation Is Not Introduced

This amendment deliberately does not create:

```text
offering/public-source-observation-access@1
product/public-source-observation-access@1
```

Public/customer consumption is already an audience-qualified projection/delivery concern.

The authoritative source owner may contribute data to those projections without creating a second public source API merely for commercial classification.

---

# 15. Listing Is Explicitly Excluded

This amendment does not establish:

```text
listing/merchant-definition-observation-access@1
listing/merchant-definition-authoring-access@1
```

Composite MS-PROT-044 v1.0 deliberately leaves material Listing questions unresolved, including:

```text
exact Listing lifecycle
exact Listing schema
Listing revision/history
Listing exposure policy
domain-specific coexistence rules
```

The existence of an Offering/Product access contract MUST NOT be used as authority for Listing mutation.

Likewise:

```text
Listing
    ≠ Offering
    ≠ Product
```

remains authoritative.

Listing commercial classification requires its own sufficiently resolved owner authority.

---

# 16. No Universal Catalogue Abstraction

These contracts MUST NOT create a universal authoritative:

```text
CatalogueItem
PublishedItem
SellableItem
ProductListing
```

supertype.

The access contracts are bounded coordination points over already-distinct semantics.

They do not merge:

```text
Offering
Product
ProductVariant
Listing
Publication
Opportunity
```

into one business object.

---

# 17. Commercial Boundary

This amendment makes **no commercial allocation decision**.

Specifically, it does not declare any contract:

```text
FREE
BUSINESS
GROWTH
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

and does not mint:

```text
CommercialEntitlementIdentity
CommercialEntitlementDefinition
Commercial Access Binding
plan-revision grant
```

A later governed commercial amendment may evaluate the contracts against composite MS-PROT-056.

Until then:

```text
MS-PROT-056 v1.7 tier allocation
    ≠
runtime permission inferred for these contracts
```

No caller may infer commercial permission merely because v1.7 places product/service presentation in FREE.

---

# 18. Preparation

Non-authoritative drafting, UI editing, AI suggestion, import parsing or candidate validation does not become source truth merely because one of these access contracts exists.

This amendment does not define a generic preparation-access contract.

Any later preparation classification must preserve:

```text
candidate
    ≠
authoritative mutation
```

and must not use preparation to bypass the authoritative authoring boundary.

---

# 19. AI Boundary

AI may assist the merchant in describing:

```text
a service
a product
an Offering proposition
a Product form
```

but AI does not obtain source-authoring authority from these contracts.

Canonical:

```text
merchant intent
    ↓
AI candidate
    ↓
registered semantic validation
    ↓
required merchant/actor authority
    ↓
authoritative owner operation
```

This amendment creates no AI mutation principal and no autonomous catalogue-management authority.

---

# 20. Failure Separation

A failed owner operation must preserve the distinction among at least:

```text
NOT_AUTHORISED
NOT_APPLICABLE
INVALID_SOURCE_VALUE
SCHEMA_MISMATCH
CONFLICT
COMMERCIAL_PERMISSION_DENIED
COMMERCIAL_PERMISSION_UNRESOLVED
TECHNICAL_FAILURE
```

where those dimensions apply.

This amendment does not itself make `COMMERCIAL_PERMISSION_DENIED` possible for these contracts because commercial classification remains unresolved.

It merely preserves the dimension for later composition.

---

# 21. Historical Commitments

Changing current Offering, Product or ProductVariant source truth MUST NOT rewrite independently committed historical business facts.

Examples include:

```text
Order commitment
Booking commitment
Appointment commitment
Payment Obligation
Fulfilment evidence
Inventory history
```

Those owners retain the exact facts applicable to their commitments.

An Offering price edited today does not retroactively modify yesterday's Order.

---

# 22. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING**

The amendment supports the Fundamental Vision because:

1. merchants continue to describe real products and services rather than software modules;
2. Offering remains sufficient for simple businesses;
3. Product/ProductVariant appear only when business reality requires them;
4. no Listing, catalogue hierarchy or e-commerce structure is forced universally;
5. commercial machinery receives precise future binding points without becoming business-semantic authority;
6. no additional merchant-facing configuration is introduced; and
7. the design preserves progressive business evolution.

The internal access-contract distinction is architectural complexity absorbed by GrandRue rather than exposed to ordinary merchants or staff.

---

# 23. Review

## 23.1 Correctness

The access boundaries correspond to already accepted semantic ownership:

```text
Offering
    owns proposition truth

Product
    owns optional durable good/material truth

ProductVariant
    is Product-owned

Inventory
    owns stock truth

transaction capabilities
    own customer commitments

Projection / Exposure / Storefront
    own public representation/delivery boundaries
```

No source owner is displaced.

## 23.2 Why not classify commercial access immediately?

Rejected.

MS-PROT-056 requires exact owner-qualified target references before executable entitlement definitions are admitted.

The current problem is therefore:

```text
missing target access contracts
```

before:

```text
commercial placement of those contracts
```

Combining the two would make the commercial decision depend on access boundaries being invented inside the same classification step rather than reviewed independently.

## 23.3 Why not reuse Storefront publication access?

Rejected.

Storefront publication answers:

```text
what presentation composition may be published?
```

Offering/Product authoring answers:

```text
what merchant source truth exists?
```

Conflating them would make presentation authority a source-data mutation authority.

## 23.4 Why not one generic catalogue access contract?

Rejected.

A universal catalogue contract would obscure the accepted distinction among:

```text
Offering
Product
ProductVariant
Listing
Publication
```

and would encourage conventional e-commerce structure to become platform semantics.

## 23.5 Why exclude Listing?

Because its owner model deliberately retains unresolved lifecycle/history/schema questions.

Commercial convenience is insufficient reason to silently resolve those questions.

---

# 24. Falsification

| Scenario | Required result |
|---|---|
| Consultant offers only a 60-minute consultation | Offering contract applies; Product is not manufactured |
| Small grocer represents milk directly as an Offering | Valid; Product is not mandatory |
| Salon uses one shampoo Product for retail and internal consumption | Product definition remains distinct from retail Offering |
| T-shirt has independently meaningful size/colour stock forms | ProductVariant may be Product-owned; Inventory stock remains separately owned |
| Merchant edits an Offering price | Offering authoring boundary applies; existing Orders/Payments remain unchanged |
| Merchant adds stock | Product authoring contract does not authorise it |
| Merchant enables Ordering for an Offering | Offering authoring contract does not supply Ordering authority |
| Merchant hides an Offering from website | Exposure/presentation authority remains separate |
| Public visitor views an exposed Offering | Merchant source-observation contract is not public access authority |
| Realtor creates a Property Listing | Offering/Product access contracts do not authorise Listing creation |
| AI drafts a service description | Candidate remains non-authoritative until accepted owner mutation |
| Merchant has no Product semantics at all | No Product UI/configuration is required |
| Low-software-capacity merchant edits “Haircut — £25” | Merchant interacts in business language; access-contract complexity remains internal |

No scenario requires a universal Product hierarchy, Listing collapse, Inventory ownership transfer or commercial decision in this amendment.

---

# 25. Invariants

1. Offering source truth and Product source truth remain distinct.
2. Product remains optional.
3. ProductVariant remains Product-bounded.
4. Offering authoring does not grant transaction execution.
5. Product authoring does not grant Inventory execution.
6. Source authoring does not establish public Exposure.
7. Public representation does not grant source mutation.
8. Listing is outside this amendment.
9. Access-contract identity does not grant Actor Authorisation.
10. Access-contract identity does not grant Commercial Entitlement.
11. Commercial authority may later reference but may not redefine these contracts.
12. Historical customer commitments are not rewritten by current source edits.
13. AI preparation does not become owner authority.
14. No runtime tier grant may be inferred from this amendment.

---

# 26. Deferred Decisions Preserved

This amendment does not resolve:

```text
exact Offering persistence representation
exact Product persistence representation
exact ProductVariant persistence representation

exact API routes / DTOs

exact merchant/staff privilege portfolio

exact Listing lifecycle
exact Listing schema
exact Listing revision/history
Listing commercial allocation

remaining Offering pricing-model details
remaining Product/Offering relationship mechanics

CommercialEntitlementIdentity values
commercial tier classification of the new access contracts
complete MS-PROT-056 standard manifest

prices
allowances
quotas
implementation activation
```

A future implementation must not invent these answers merely because this amendment exists.

---

# 27. Relationship to MS-PROT-056-V17-DQ-001

`MS-PROT-056-V17-DQ-001` remains **OPEN**.

This amendment supplies prerequisite exact target access contracts that a later commercial-classification amendment may evaluate.

It does not determine:

```text
which target receives a Commercial Entitlement
which target requires no independent entitlement
tier placement
entitlement identity
binding identity
manifest membership
```

Therefore no DQ-001 status reduction is claimed by this amendment alone.

---

# 28. Implementation Activation

**NONE**

Acceptance does not authorise:

```text
new runtime access checks
new Product or Offering persistence
new API endpoints
new UI
new entitlement checks
new catalogue grants
new Storefront behaviour
new semantic-registry definitions
```

Separate implementation authority and the applicable implementation gate remain required.

---

# 29. Acceptance Effect

If accepted, this amendment becomes the owner-qualified access-contract layer for the existing Offering/Product/ProductVariant semantic boundary.

Composite MS-PROT-044 becomes:

```text
v1.0
    Offering / Listing / published-subject boundary

+
v1.1
    Product / Offering / ProductVariant boundary

+
v1.2
    Offering / Product owner-qualified
    merchant definition access contracts
```

Listing remains governed by surviving v1.0 authority and its unresolved questions.

No prior accepted semantic rule is superseded outside the exact scope of this amendment.

---

# 30. Governance Recording After Approval

If accepted:

1. add MS-PROT-044 v1.2 to `AUTHORITY-INDEX.md`;
2. retain `MS-PROT-056-V17-DQ-001` as **OPEN**;
3. record that exact Offering/Product target access contracts now exist but remain commercially unclassified;
4. correct the existing DDR navigation omission so accepted MS-PROT-042 v1.16 is included among the bounded classifications already narrowing DQ-001;
5. make no Listing commercial-classification assertion;
6. make no implementation-sequence completion assertion;
7. run the applicable design-corpus conformance review; and
8. commit only the approved authority and its mechanical governance consequences to `development`.

---

# 31. Recommendation

**RECOMMENDATION: ACCEPT**

The amendment is the smallest conforming prerequisite for the remaining Offering/Product commercial work.

It creates exact owner-qualified binding targets without:

```text
inventing runtime grants
forcing Product semantics
pulling Listing into scope
collapsing source authoring into Storefront delivery
or prematurely resolving the commercial catalogue
```

The next governed design step after acceptance should be the commercial classification of these exact contracts against MS-PROT-056 v1.7.
