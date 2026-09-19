# MS-PROT-044 v1.6 — Listing Commercial Access Classification Amendment

**Document ID:** MS-PROT-044  
**Version:** 1.6  
**Status:** ACCEPTED  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after complete pre-approval presentation, Fundamental Vision Conformance, review, falsification and ambiguity review  
**Authority type:** Listing commercial-access classification amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-044 through v1.5 within Listing commercial-access classification only  
**Depends on:** Composite MS-PROT-044 through v1.5; composite MS-PROT-056 through v1.9; applicable Actor Authorisation, Projection, Exposure, Storefront, Enquiry and subject-owner authorities  
**Relationship to:** `MS-PROT-056-V17-DQ-001`  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING`

---

## 0. Authority Identity Preflight

```text
target branch
    development

review basis HEAD
    46be925c9d40b4a07f573600b8f1c31e955f4342

current MS-PROT-044 composition
    base + v1.1 + v1.2 + v1.3 + v1.4 + v1.5

MS-PROT-044 v1.6
    unoccupied
```

Latest-head and identifier availability MUST be repeated immediately before any post-approval formalisation.

---

## 1. Fundamental Vision Conformance

GrandRue's FREE plan exists to let a merchant establish a useful professional public presence.

The accepted commercial hierarchy already includes:

```text
FREE — Establish
```

and MS-PROT-056 v1.1 explicitly identifies:

```text
basic catalogue/listing presentation
```

as representative FREE value.

MS-PROT-056 v1.7 subsequently formalises FREE around establishment and presentation rather than transaction execution.

A realtor, vehicle dealer or another legitimate Listing-based merchant therefore must not need BUSINESS merely to describe and maintain what it is advertising.

Canonical distinction:

```text
create / maintain a Listing
        = establishment / presentation

customer transaction execution
        ≠ Listing authoring
```

**Vision result:**

```text
VISION-CONFORMING
```

---

# 2. Governing Decision

The three exact Listing access contracts accepted by MS-PROT-044 v1.5 SHALL be classified as follows:

| Exact contract | Commercial classification | Standard allocation |
|---|---|---|
| `listing/merchant-definition-observation-access@1` | No independent Commercial Entitlement | Plan-independent bounded observation |
| `listing/merchant-definition-authoring-access@1` | `MAINTAIN_MERCHANT_LISTING_DEFINITION` | FREE + BUSINESS + GROWTH |
| `listing/merchant-withdrawal-access@1` | No independent Commercial Entitlement | Plan-independent bounded terminal withdrawal |

The protected authoring target family is:

```text
OPERATION_ACCESS
```

Exact `CommercialEntitlementIdentity` values remain Commercial-owned under:

```text
MS-PROT-056-V17-DQ-001
```

This authority does not publish a catalogue.

---

# 3. Protected Commercial Purpose

The Listing authoring purpose SHALL be:

```text
MAINTAIN_MERCHANT_LISTING_DEFINITION
```

It is deliberately distinct from:

```text
MAINTAIN_MERCHANT_OFFERING_DEFINITION

MAINTAIN_MERCHANT_PRODUCT_DEFINITION
```

because:

```text
Listing
    ≠ Offering
    ≠ Product
```

Granting all three purposes in FREE does not merge their semantic ownership.

---

# 4. Authoring Scope

The exact contract:

```text
listing/merchant-definition-authoring-access@1
```

requires:

```text
MAINTAIN_MERCHANT_LISTING_DEFINITION
```

for otherwise-valid operations that:

```text
create a new Listing

or

establish a new current ListingRevision
```

Examples include:

```text
create property rental Listing

create property sale Listing

change asking rent

change asking sale price

change Listing-owned marketing description

change another valid registered Listing-owned field
```

Commercial permission never bypasses Listing lifecycle or source-owner invariants.

---

# 5. FREE Allocation

`MAINTAIN_MERCHANT_LISTING_DEFINITION` belongs to:

```text
FREE
BUSINESS
GROWTH
```

within one standard catalogue generation.

This gives Listing-based merchants the same establishment principle already accepted for Product and Offering definition.

Canonical:

```text
FREE merchant

may describe and maintain:
    applicable Products
    applicable Offerings
    applicable Listings
```

without thereby receiving paid operational capabilities.

---

# 6. Listing Presentation Is Not Transaction Execution

A FREE merchant may establish:

```text
Property Listing
    FOR RENT
    £1,200/month
```

without gaining:

```text
Booking commitment

Appointment commitment

Order commitment

Payment execution

Quotation issuance

Invoice issuance
```

Likewise:

```text
Vehicle Listing
    FOR SALE
```

does not grant Ordering merely because it advertises a commercial proposition.

Hard invariant:

```text
Listing presentation
    ≠ transaction execution
```

---

# 7. Observation

The exact contract:

```text
listing/merchant-definition-observation-access@1
```

requires:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for otherwise-authorised merchant inspection of:

```text
Listing identity

lifecycle

current revision

historical retained revisions

primary subject reference

authorised mutation / withdrawal evidence
```

within applicable retention and Actor Authorisation rules.

Commercial change must not make legitimate retained Listing history unreadable.

---

# 8. Observation Is Not Public Access

The merchant observation contract MUST NOT be reused for customer or anonymous public access.

Canonical distinction:

```text
merchant definition observation
    ≠
public Listing presentation
```

Public/customer Listing visibility remains composed through:

```text
Projection
+
Exposure
+
Storefront delivery
```

No generic Listing public-source access contract is invented by this amendment.

---

# 9. Withdrawal

The exact contract:

```text
listing/merchant-withdrawal-access@1
```

requires:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for otherwise-valid terminal Listing withdrawal.

Withdrawal still requires:

```text
Actor Authorisation

Merchant Scope

Listing ACTIVE

expected currentness

logical request identity

lifecycle validation
```

No paid entitlement is required merely to stop an existing proposition.

---

# 10. Why Withdrawal Is Not Commercially Protected

Withdrawal creates no new commercial proposition.

It terminates existing current activity.

Therefore monetising withdrawal would create an undesirable situation where a merchant could lose the ability to stop presenting a Listing after a commercial change.

Canonical:

```text
create / revise new proposition
        → protected authoring purpose

end existing proposition
        → no independent Commercial Entitlement
```

This follows the accepted Profile/Publication/Storefront residual-management pattern.

---

# 11. Withdrawal Does Not Grant Authoring

Because withdrawal is unentitled does not mean the merchant may:

```text
reactivate Listing

change asking price

change description

change transaction mode

create another Listing
```

through the withdrawal contract.

MS-PROT-044 v1.5 remains authoritative that WITHDRAWN is terminal.

---

# 12. Commercial Loss and Existing Listings

Loss of an authoring grant MUST NOT:

```text
delete Listing

delete Listing revisions

rewrite Listing lifecycle

withdraw Listing automatically

delete Property / Vehicle / subject

remove historical Enquiries

rewrite historical customer context
```

Commercial entitlement does not own source lifecycle.

---

# 13. Authoring After Commercial Change

A materially new Listing creation or ListingRevision establishment must satisfy the current protected commercial purpose at the time of the authoritative effect.

Canonical:

```text
current authoring entitlement valid
    → may proceed if all other predicates pass

current authoring entitlement absent
    → protected new authoring denied
```

Observation and withdrawal remain independently available where otherwise authorised.

---

# 14. Lost-Acknowledgement Recovery

If a Listing authoring operation:

```text
committed successfully
```

but acknowledgement was lost, retry recovery of that exact logical request MUST NOT be treated as a second new commercial action.

The existing idempotency/recovery semantics remain authoritative.

GrandRue MUST NOT manufacture:

```text
another Listing

another ListingRevision
```

because entitlement source changed between commit and retry.

---

# 15. Category Boundary

Merchant Subject Category operations remain:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

under MS-PROT-044 v1.4.

Therefore:

```text
Listing authoring entitlement
    ≠ Category entitlement
```

and no new Category commercial purpose is introduced.

A merchant may classify an otherwise-authorised Listing without creating a separate paid feature.

---

# 16. Physical Location Boundary

Physical-location representation and map presentation under MS-PROT-045 v1.2 introduce:

```text
NO NEW INDEPENDENT COMMERCIAL ENTITLEMENT
```

Listing authoring therefore does not mint separate commercial products for:

```text
Location field
Map
Directions
Street-level imagery
```

The underlying source mutation/access, Exposure and provider-readiness requirements still apply independently.

---

# 17. Listing Transaction Mode

`SALE` and `RENT` remain Listing-owned structured meanings under MS-PROT-044 v1.4.

Choosing:

```text
SALE
```

or:

```text
RENT
```

during Listing creation uses the same Listing authoring purpose.

No separate:

```text
SALE_LISTING_ENTITLEMENT
RENT_LISTING_ENTITLEMENT
```

is permitted.

---

# 18. SALE ↔ RENT Boundary

MS-PROT-044 v1.5 remains authoritative that:

```text
SALE ↔ RENT
```

is not ordinary revision.

A change requires:

```text
withdraw old Listing
+
create new Listing
```

Commercially:

```text
withdraw old Listing
    → no independent entitlement

create replacement Listing
    → MAINTAIN_MERCHANT_LISTING_DEFINITION
```

This preserves semantic correctness without charging for termination.

---

# 19. Subject Ownership

Listing commercial access does not grant mutation authority over the primary subject.

Example:

```text
Property P100
    address
    bedrooms
    bathrooms

Listing L200
    asking rent
```

Listing authoring permission allows:

```text
change asking rent
```

where valid.

It does not allow:

```text
change Property.address
```

unless the actor independently satisfies the Property owner's mutation authority.

---

# 20. Exposure Boundary

`MAINTAIN_MERCHANT_LISTING_DEFINITION` does not grant public Exposure.

Canonical:

```text
Listing exists
        ≠
Listing publicly visible
```

A merchant may have:

```text
ACTIVE Listing
+
NOT EXPOSED
```

without contradiction.

---

# 21. Storefront Boundary

Listing authoring does not grant:

```text
Storefront composition publication

website delivery

custom domain

platform namespace use
```

Those remain independently governed.

FREE may ultimately contain all applicable required grants, but runtime cannot infer them from the word FREE.

Each exact binding must independently resolve.

---

# 22. Search and Discoverability

Listing authoring does not automatically grant:

```text
search indexing

cross-merchant discovery

marketplace participation

SEO services
```

Basic merchant-site presentation and broader discovery remain separate concerns.

GrandRue remains not a marketplace.

---

# 23. Enquiry Boundary

Listing authoring does not grant Enquiry execution.

Where a Listing participates as an Enquiry subject:

```text
Enquiry SUBJECT → Listing
```

that interaction still requires its independently accepted Public Interaction/Enquiry participation and access conditions.

---

# 24. Viewing Boundary

A property Listing being ACTIVE does not automatically establish:

```text
viewing Appointment

Book a viewing

available viewing slots
```

Those require the independently applicable Appointment/Booking/Scheduling semantics.

---

# 25. Payment Boundary

A Listing may contain:

```text
asking price
asking rent
```

using Money semantics.

That does not establish:

```text
Payment Obligation
Amount Due
Payment execution
```

Payment remains independently governed.

---

# 26. Quotation and Invoice Boundary

Listing authoring does not grant:

```text
ISSUE_QUOTATION_COMMERCIAL_OFFER

ISSUE_CUSTOMER_INVOICE
```

Likewise Quotation or Invoicing access does not grant Listing mutation.

---

# 27. Actor Authorisation

Commercial Entitlement and Actor Authorisation remain independent.

Canonical:

```text
Commercial permission valid
+
actor not authorised
    → deny

Actor Authorisation valid
+
commercial authoring permission absent
    → deny protected new authoring
```

This amendment creates no staff privilege.

---

# 28. Semantic Applicability

A FREE grant does not manufacture Listing semantics for every merchant.

Canonical:

```text
Listing grant exists
+
merchant has no applicable Listing semantics
        ↓
no Listing surface required
```

An information publisher, ordinary grocer or consultant need not see Listing administration merely because the standard plan contains the entitlement.

---

# 29. AI Boundary

AI may assist with:

```text
description drafting

location interpretation

category candidate

transaction-mode interpretation

price/rent extraction

feature extraction
```

but:

```text
AI candidate
    ≠ authoritative Listing authoring
```

The final operation still requires all current commercial, actor and semantic predicates.

---

# 30. No Tier-Name Runtime Checks

Runtime MUST NOT implement:

```text
if plan == FREE:
    allow Listing
```

or:

```text
if plan >= BUSINESS:
    allow Listing
```

Correct resolution remains:

```text
CommercialEntitlementIdentity
        ↓ exact binding
target contract
+
protected purpose
```

Plan labels are catalogue composition concepts, not source-operation authority.

---

# 31. Target-Binding Consequence

The final Commercial catalogue may create one exact stable `CommercialEntitlementIdentity` for:

```text
listing/merchant-definition-authoring-access@1
+
MAINTAIN_MERCHANT_LISTING_DEFINITION
```

The identity itself remains owned by MS-PROT-056.

Rejected:

```text
FREE → all Listing operations
```

Rejected:

```text
FREE → Listing.*
```

Rejected:

```text
FREE → catalogue/listing management wildcard
```

Only the exact authoring binding may be protected by the entitlement identity.

---

# 32. Observation and Withdrawal Do Not Need Entitlement Identities

Because:

```text
listing/merchant-definition-observation-access@1
```

and:

```text
listing/merchant-withdrawal-access@1
```

require no independent Commercial Entitlement, the standard catalogue MUST NOT mint unnecessary entitlement identities for them merely to achieve structural symmetry.

---

# 33. Falsification — Realtor on FREE

Merchant creates:

```text
2-bedroom flat
For rent
£1,200/month
```

Expected:

```text
Listing authoring commercially permitted
```

assuming all other conditions pass.

No Booking, Payment or Invoice permission follows.

**PASS**

---

# 34. Falsification — Vehicle Dealer on FREE

Merchant creates:

```text
Vehicle Listing
For sale
£12,500
```

Expected:

Listing presentation permitted.

No Order commitment permission follows.

**PASS**

---

# 35. Falsification — Information Publisher

Merchant uses Publication and Enquiry but no Listing semantics.

Expected:

no Listing object or dashboard surface is manufactured merely because the FREE plan contains the Listing entitlement.

**PASS**

---

# 36. Falsification — Downgrade / Entitlement Loss

Merchant has existing Listing history but current authoring permission is unavailable.

Expected:

```text
observe existing Listing
    allowed if otherwise authorised

withdraw Listing
    allowed if otherwise authorised

create/revise materially new Listing truth
    denied
```

**PASS**

---

# 37. Falsification — Hide Website Listing

Merchant wants temporary website hiding.

Expected:

```text
Exposure operation
```

not Listing withdrawal.

Commercial Listing authoring classification is irrelevant to that distinction.

**PASS**

---

# 38. Falsification — End Listing

Merchant chooses:

```text
End listing
```

Expected:

terminal withdrawal requires no independent Commercial Entitlement.

**PASS**

---

# 39. Falsification — RENT to SALE

Existing RENT Listing becomes obsolete.

Expected:

```text
withdraw RENT Listing
    no independent entitlement

create SALE Listing
    protected authoring entitlement required
```

**PASS**

---

# 40. Falsification — Map Provider Failure

Listing includes Property location but map provider is unavailable.

Expected:

Listing commercial authoring truth remains unaffected.

Map presentation may degrade independently.

**PASS**

---

# 41. Falsification — Category Change

Merchant changes:

```text
Student Properties
    → Featured Homes
```

Expected:

Category Assignment remains supporting/no-independent-entitlement activity.

No Listing authoring entitlement is consumed merely because category membership changes.

**PASS**

---

# 42. Rejected Alternative — BUSINESS-Only Listing Authoring

Rejected.

It conflicts with the accepted FREE/Establish purpose and earlier accepted basic catalogue/listing presentation value.

A merchant should not need an operating tier merely to present its legitimate Listing portfolio.

---

# 43. Rejected Alternative — All Listing Operations Plan-Independent

Rejected.

Creating and materially revising current Listing propositions is part of the commercial FREE service portfolio and therefore requires explicit catalogue admission rather than accidental unprotected access.

---

# 44. Rejected Alternative — Separate Sale and Rental Entitlements

Rejected.

`SALE` and `RENT` are structured Listing meanings, not separate commercial products.

---

# 45. Rejected Alternative — Charge for Withdrawal

Rejected.

Terminal withdrawal is residual/safety administration of existing state, not creation of new commercial value.

---

# 46. Rejected Alternative — Use Product/Offering Entitlement

Rejected.

Listing has independent semantic identity and cannot inherit Product or Offering commercial authority merely because all are presented publicly.

---

# 47. Hard Invariants

1. Listing authoring uses exact `listing/merchant-definition-authoring-access@1`.
2. Protected purpose is `MAINTAIN_MERCHANT_LISTING_DEFINITION`.
3. Authoring belongs to FREE + BUSINESS + GROWTH.
4. Merchant Listing observation requires no independent Commercial Entitlement.
5. Listing withdrawal requires no independent Commercial Entitlement.
6. Observation does not grant authoring.
7. Withdrawal does not grant authoring or reactivation.
8. Commercial grants do not create semantic applicability.
9. Commercial grants do not create Actor Authorisation.
10. Listing authoring does not grant subject mutation.
11. Listing authoring does not grant Exposure.
12. Listing authoring does not grant website delivery.
13. Listing authoring does not grant search/marketplace participation.
14. Listing authoring does not grant Enquiry.
15. Listing authoring does not grant Booking/Appointment.
16. Listing authoring does not grant Ordering.
17. Listing authoring does not grant Payment.
18. Listing authoring does not grant Quotation.
19. Listing authoring does not grant Invoicing.
20. Category support remains non-independently entitled.
21. Physical-location/map support remains non-independently entitled.
22. SALE/RENT do not produce separate entitlements.
23. Commercial loss does not delete or rewrite Listing history.
24. Exact entitlement identity remains Commercial-owned.
25. No wildcard binding is permitted.
26. No catalogue publication is authorised.
27. No implementation is activated.

---

# 48. Effect on `MS-PROT-056-V17-DQ-001`

Upon acceptance, the current Listing commercial-classification gap becomes resolved.

The Listing path becomes:

```text
MS-PROT-044 base
    Listing semantics

+
MS-PROT-044 v1.4
    Category + Transaction Mode

+
MS-PROT-044 v1.5
    lifecycle / revision / access contracts

+
MS-PROT-044 v1.6
    commercial classification
        ↓
Listing definition scope
commercially binding-ready
```

`MS-PROT-056-V17-DQ-001` remains OPEN for:

```text
final completeness audit

concrete CommercialEntitlementIdentity values

exact Commercial Access Bindings

complete FREE / BUSINESS / GROWTH manifests

explicit catalogue approval
```

---

# 49. Next Governed Step

After acceptance, GrandRue SHOULD perform the final DQ-001 commercial-classification completeness audit.

If no owner/supporting classification gaps remain, the next Commercial authority should be:

```text
MS-PROT-056 v1.10
Initial Standard Commercial Catalogue Manifest
```

That manifest would finally bind the accepted purposes into exact immutable catalogue generations.

This audit must not introduce additional capabilities merely to make the catalogue appear complete.

---

# 50. Implementation Activation

**NONE**

Acceptance does not authorise:

```text
Listing persistence

Listing API

merchant Listing screens

commercial entitlement implementation

catalogue publication

Standing Free changes

pricing

provider integration

deployment
```

---

# 51. Governance Recommendation

**Fundamental Vision Conformance:** `VISION-CONFORMING`  
**FREE/Establish conformity:** PASS  
**Listing semantic ownership:** PASS  
**Residual observation:** PASS  
**Residual withdrawal:** PASS  
**Transaction separation:** PASS  
**Exposure/Storefront separation:** PASS  
**Location/map boundary:** PASS  
**Actor-authority separation:** PASS  
**Commercial-loss/history review:** PASS  
**Cross-domain falsification:** PASS  
**Anti-wildcard review:** PASS  
**Ambiguity review:** PASS

```text
RECOMMENDATION: ACCEPT
```

**Manual approval:** GRANTED — 19 September 2026

---

# 52. Acceptance Statement

> **GrandRue shall classify merchant Listing creation and material current Listing revision through the exact `listing/merchant-definition-authoring-access@1` contract protected by `MAINTAIN_MERCHANT_LISTING_DEFINITION`, allocated to FREE, BUSINESS and GROWTH as part of the merchant's establishment/public-presentation proposition. Otherwise-authorised observation of existing Listing truth and terminal Listing withdrawal require no independent Commercial Entitlement. These classifications do not create semantic applicability, Actor Authorisation, underlying-subject mutation authority, Exposure, Storefront delivery, Enquiry, Booking, Appointment, Ordering, Payment, Quotation or Invoicing authority. SALE and RENT remain structured Listing meanings rather than separate entitlements, supporting Category and Physical Location/map functionality remain independently non-monetised, commercial loss does not delete or rewrite Listing history, and exact entitlement identities/bindings remain Commercial-owned pending the final standard catalogue manifest.**
