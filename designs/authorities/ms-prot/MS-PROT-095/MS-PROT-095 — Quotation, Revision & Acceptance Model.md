# MS-PROT-095 — Quotation, Revision & Acceptance Model

**Document ID:** MS-PROT-095  
**Version:** 1.0  
**Status:** **ACCEPTED**  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after Fundamental Vision Conformance, full design review, cross-domain falsification, ambiguity review and complete pre-approval presentation  
**Authority type:** Material Quotation semantic/design authority  
**Governed by:** `MS-DESIGN-RULES-001` v2.5; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Amends:** composite MS-PROT-043 within Quotation-intent Enquiry preparation/submission only; composite MS-PROT-055 within previously deferred Quotation commercial-term scope; composite MS-PROT-056 within Quotation reservation admission and owner-qualified commercial-access classification; composite MS-PROT-077 within accepted-Quotation commercial-source provenance for Order commitment  
**Preserves:** Enquiry ownership of customer requests; Offering/Product ownership of current propositions; Ordering ownership of purchase commitment; Inventory ownership of stock truth; Payment ownership of Payment Obligations and payment evidence; Appointment/Booking ownership of their commitments; CustomerContext ownership boundaries; Exposure ownership; Notification ownership; provider neutrality; merchant authority; business-type neutrality  
**Partially resolves:** `MS-PROT-056-V17-DQ-004` within the Quotation reserved portfolio only; `MS-PROT-056-V17-DQ-001` by supplying Quotation-owned access classifications only  
**Depends on:** composite MS-PROT-020; composite MS-PROT-021; composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-040; composite MS-PROT-043; composite MS-PROT-044; composite MS-PROT-049; composite MS-PROT-053; composite MS-PROT-055; composite MS-PROT-056; composite MS-PROT-057; composite MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; composite MS-PROT-069; composite MS-PROT-074; composite MS-PROT-075; composite MS-PROT-077; applicable Exposure, Customer Communication and Data Protection authorities  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`  
**Purpose:** Establish Quotation as the GrandRue semantic owner of merchant-issued quoted commercial offers, immutable issued revisions and recipient responses; integrate “Get a quotation” with existing Enquiry authority rather than creating duplicate request semantics; support goods, services and mixed merchant operations across business domains; and preserve strict boundaries with Ordering, Appointment, Inventory, Payment and future Invoicing.

---

# Authority Identity Preflight

Authority preflight for this proposal:

```text
branch
    development

HEAD
    c84fb1825d60a130031d6de101f9cb343ec41201

current accepted semantic/design composition
    MS-PROT-020..094 as indexed by
    AUTHORITY-INDEX.md
    with scope-aware amendment composition

MS-PROT-095
    no repository-resident authority found

candidate
    MS-PROT-095 v1.0

canonical authority directory if approved
    designs/authorities/ms-prot/MS-PROT-095/
```

This preflight does not reserve the identifier.

Immediately before any post-approval repository formalisation, the current `development` HEAD and authority identity MUST be revalidated under `MS-DESIGN-RULES-001`.

---

# 0. Fundamental Vision Conformance

## 0.1 Feature Admission

Quotation satisfies the **Representation Test**.

GrandRue already recognises `QUOTE_REQUIRED` as an Offering pricing form under composite MS-PROT-055.

Without authoritative Quotation semantics, GrandRue can represent that a price requires quotation but cannot represent the resulting merchant commercial offer, its revision history or the customer's response.

That leaves a material target-business operation outside GrandRue.

Quotation also satisfies the **Coordination Test**.

An accepted quoted price may need to participate later in:

```text
Ordering
Invoicing
Payment
Appointment / work progression
```

without forcing the merchant to re-enter or reinterpret the accepted commercial information.

Quotation additionally satisfies the **Administrative-Compression Test** because merchants who currently prepare quotations through disconnected documents, messages or spreadsheets can operate through the same GrandRue business context.

---

## 0.2 Business-to-Software Translation

The merchant interacts in business language:

```text
Get a quotation
Create quotation
Send quotation
Accept
Decline
Request a change
Withdraw quotation
```

The merchant MUST NOT configure:

```text
state machines
revision graphs
relationship definitions
object namespaces
idempotency rules
commercial-access contracts
cross-capability orchestration
```

GrandRue absorbs those software concerns.

---

## 0.3 Administrative Compression

Quotation SHALL reuse existing authoritative information where permitted.

The customer SHALL NOT be required to re-enter a known Offering, Product, merchant, customer context or other authoritative interaction context.

The merchant SHALL be able to use source Enquiry context when preparing a quotation without copying the customer request manually.

The design deliberately reuses Enquiry instead of introducing a second customer-request object.

---

## 0.4 Ordinary Staff

Ordinary merchant staff may require business authority to:

```text
prepare a quotation
issue a quotation
withdraw a quotation
record an externally received response
```

They MUST NOT need to understand GrandRue semantic architecture.

Staff complexity remains the complexity of the merchant's actual quotation work rather than GrandRue product administration.

---

## 0.5 Target-Market Proportionality

The initial model deliberately excludes:

```text
tender management
procurement RFQs
contract lifecycle management
complex estimating systems
bill-of-quantities engines
arbitrary pricing formulas
electronic-signature infrastructure
legal-contract adjudication
```

Those features are not required to provide the minimum sufficiently expressive Quotation semantics for GrandRue's target merchants.

---

## 0.6 Native Ownership Versus Integration

Native ownership is justified.

GrandRue requires authoritative knowledge of:

```text
what commercial offer was issued
which exact revision was issued
what quoted amounts were offered
which recipient the offer concerned
whether that exact revision was accepted, declined or withdrawn
```

These facts participate directly in GrandRue operations and cannot be delegated to a document-rendering or messaging provider without making that provider an accidental owner of GrandRue business truth.

PDF, email, SMS or another provider may deliver a quotation representation.

The provider SHALL NOT own Quotation semantics.

---

## 0.7 Cross-Capability Value

Quotation provides a stable commercial-source boundary for later operations without acquiring their ownership.

Canonical:

```text
Enquiry
    customer requirement
        ↓

Quotation
    merchant commercial offer
        ↓

accepted quotation
        ↓
explicit downstream operation

        ├── Ordering
        ├── future Invoicing
        └── other separately accepted owner
```

---

## 0.8 Complexity Burden

The proposal introduces internal complexity for:

```text
immutable issued revisions
historical quoted terms
response concurrency
retry safety
commercial-source provenance
customer relationship validation
```

That complexity is required for commercial correctness.

It remains internal to GrandRue.

---

## 0.9 Vision Result

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

The complexity is justified by historical commercial correctness, customer-response integrity and cross-capability coordination.

---

# 1. Governing Decision

GrandRue SHALL establish **Quotation** as the semantic owner of merchant-issued quoted commercial offers.

Canonical:

```text
CUSTOMER REQUEST
    Enquiry-owned
        ↓

MERCHANT PREPARATION
    Quotation-owned preparation
        ↓

ISSUED QUOTATION REVISION
    immutable quoted commercial offer
        ↓

RECIPIENT RESPONSE
    acceptance / decline
        ↓

EXPLICIT DOWNSTREAM HANDOFF
    separately owned
```

The central ownership rule is:

> **Enquiry owns the customer's request. Quotation owns the merchant's quoted commercial offer. Ordering owns a purchase/order commitment. Payment owns monetary obligations and payment evidence. These truths MUST NOT be collapsed.**

There SHALL be no independent `QuotationRequest` Operational Object.

---

# 2. Problem and Scope

GrandRue merchants may need to establish a price only after receiving information about:

```text
scope
quantity
measurements
delivery
location
custom requirements
materials
services
mixed goods and services
```

Examples include:

```text
carpenter
plumber
mechanic
building contractor
caterer
wholesale supplier
equipment supplier
consultant
custom manufacturer
```

The same business may also contain offerings that:

```text
can be ordered immediately

and

can be quoted separately
```

Quotation therefore cannot be modelled as a business-category-specific feature.

This authority governs:

- Quotation identity and merchant scope;
- merchant preparation;
- immutable issued revisions;
- quoted items and exact quoted commercial amounts;
- optional quotation validity;
- revision lineage;
- customer acceptance and decline;
- merchant withdrawal;
- requested-change handling;
- Enquiry integration;
- customer relationship/access requirements;
- historical commercial provenance;
- commercial-access classification;
- downstream commercial-source handoff;
- retry/idempotency;
- concurrency;
- failure semantics;
- AI boundaries;
- provider/delivery boundaries; and
- cross-domain applicability.

---

# 3. Explicit Non-Goals

MS-PROT-095 v1.0 SHALL NOT establish:

```text
Estimate semantics

supplier procurement / vendor RFQ

competitive tendering

bid comparison

purchase-order procurement

contract lifecycle management

statutory contract formation

legal enforceability adjudication

electronic-signature authority

tax-calculation authority

accounting authority

Invoice authority

arbitrary merchant-authored pricing formulas

open-ended cost-plus estimating

construction bill-of-quantities authority

manufacturing costing authority

partial acceptance of individual quotation items

alternative-option acceptance within one revision

multi-currency quotation

automatic Order creation

automatic Appointment creation

Inventory reservation

Payment Obligation creation

a generic workflow engine

a generic merchant form-builder DSL

a business-type-specific quotation hierarchy
```

`Estimate` remains future scope.

Native Invoice authority remains independently governed by `MS-PROT-084-DQ-006`.

---

# 4. Canonical Terminology

## 4.1 Quotation

A **Quotation** is:

> **A merchant-scoped Operational Object representing one merchant commercial-offer lineage addressed to one recipient context.**

Canonical Operational Object type identity:

```text
quotation / quotation
```

Quotation owns the lineage and its issued revisions.

A Quotation is not:

```text
an Enquiry
an Order
an Appointment
an Invoice
a Payment Obligation
a PDF
an email
a generic document
```

---

## 4.2 Issued Quotation Revision

An **Issued Quotation Revision** is:

> **An immutable, identity-bearing Quotation-owned commercial proposition containing the exact scope and quoted commercial amounts the merchant issued to the recipient at one point in time.**

An Issued Quotation Revision is the authoritative answer to:

> What exactly did the merchant quote?

An issued revision MUST NOT be edited in place.

---

## 4.3 Quotation Item

A **Quotation Item** is:

> **An identity-bearing component of one Issued Quotation Revision describing one quoted commercial scope and its exact quoted monetary contribution.**

A Quotation Item MAY describe:

```text
an Offering
a Product
a ProductVariant
a service
delivery
labour
a custom merchant-defined scope
another separately accepted quoteable subject
```

A source reference does not transfer ownership of that source object to Quotation.

---

## 4.4 Quotation Acceptance

A **Quotation Acceptance** is:

> **An immutable Quotation-owned fact that the authorised recipient, or an authorised merchant actor recording an externally received response, accepted one exact Issued Quotation Revision.**

Quotation Acceptance is not an Order.

Quotation Acceptance is not payment evidence.

---

## 4.5 Quotation Decline

A **Quotation Decline** is:

> **An immutable Quotation-owned fact that the authorised recipient, or an authorised merchant actor recording an externally received response, declined one exact Issued Quotation Revision.**

---

## 4.6 Quotation Withdrawal

A **Quotation Withdrawal** is:

> **An immutable Quotation-owned fact that an authorised merchant actor withdrew one exact outstanding Issued Quotation Revision before acceptance.**

---

## 4.7 Quotation Request

`Quotation Request` MAY be used in presentation language to describe customer intent.

It SHALL NOT identify a new Operational Object.

Canonical:

```text
customer:
    Get a quotation

authoritative result:
    Enquiry
    with Quotation-intent context
```

MS-PROT-043 remains the owner of the customer request.

---

## 4.8 Requested Quotation Entry

A **Requested Quotation Entry** is structured Enquiry submission data used only where a Quotation-intent Enquiry needs the customer to identify one or more things they want priced.

It is:

```text
Enquiry-owned submitted data

not an Operational Object

not a Quotation Item

not an Order Line

not an Enquiry SUBJECT relationship
```

It MAY carry:

```text
source semantic reference
requested quantity + unit where supplied
customer description/specification
other registered Enquiry requirement values
```

A Requested Quotation Entry represents what the customer requested.

A Quotation Item represents what the merchant subsequently offered.

The two MUST NOT be treated as identical facts.

---

# 5. Semantic Ownership

The ownership graph is:

```text
ENQUIRY
    customer request
    submitted requirements
    Requested Quotation Entries
    request provenance

OFFERING / PRODUCT / OTHER SOURCE OWNER
    current subject truth
    current source proposition

QUOTATION
    Quotation identity
    merchant preparation
    Issued Quotation Revision
    Quotation Item quoted scope
    quoted commercial terms
    revision lineage
    acceptance
    decline
    withdrawal

ORDERING
    purchase/order commitment

INVENTORY
    stock position
    stock claims
    movements

PAYMENT
    Payment Obligation
    provider payment evidence
    Refund

APPOINTMENT / BOOKING
    respective customer commitments

FUTURE INVOICING
    Invoice truth
```

Reference, projection, physical table co-location, orchestration or event consumption SHALL NOT transfer semantic ownership.

---

# 6. Quotation Identity and Scope

Every Quotation MUST belong to exactly one Merchant Scope.

Conceptually:

```text
QuotationIdentity
{
    merchantScope
    quotationIdentifier
}
```

Attribute similarity does not establish Quotation identity.

Two Quotations MAY legitimately contain:

```text
same merchant
same recipient
same items
same amounts
same originating Enquiry
```

and remain separate Quotations when they arise from separate merchant commercial intent.

A Quotation MUST have exactly one recipient context.

The recipient context MAY be represented through:

```text
one CustomerContext reference

or

one exact guest / transaction-specific recipient context
```

according to accepted customer-access authority.

Contact-value similarity MUST NOT establish recipient identity.

---

# 7. Source Enquiry

A Quotation MAY reference zero or one originating Enquiry in the initial model.

```text
0..1 originating Enquiry
        ↓
1 Quotation
```

An originating Enquiry provides provenance and customer-request context.

The Enquiry remains independently authoritative.

Creating a Quotation MUST NOT:

```text
convert the Enquiry into Quotation

delete the Enquiry

rewrite submitted customer requirements

rewrite original Enquiry provenance
```

A merchant MAY create a Quotation with no originating Enquiry.

Examples:

```text
telephone enquiry
walk-in customer
existing customer conversation
site visit
email received outside GrandRue
merchant-initiated quotation
```

Channel origin is provenance, not a new semantic type.

---

# 8. “Get a quotation” and Enquiry

A customer-facing **Get a quotation** interaction SHALL use the accepted Enquiry submission authority.

Canonical:

```text
known merchant context
+
known Offering/Product context where present
+
Quotation-intent Enquiry requirements
+
minimum unresolved customer input
        ↓
Submit Enquiry
```

The customer SHALL NOT be required to restate context GrandRue already knows.

A subject-specific interaction SHALL continue to use the accepted Enquiry `SUBJECT` relationship where one exact subject initiated the interaction.

Example:

```text
Offering: Bespoke Wardrobe
        ↓
Get a quotation
        ↓
Enquiry
SUBJECT → Bespoke Wardrobe Offering
```

---

# 9. Multi-Item Supplier Request

The existing unresolved question of universal multi-`SUBJECT` Enquiry cardinality SHALL NOT be silently resolved by Quotation.

For a customer requesting prices for multiple supplier items, Quotation-intent Enquiry submission MAY contain:

```text
1..n Requested Quotation Entries
```

Example:

```text
Requested Quotation Entry 1
    ProductVariant → Cement 25 kg
    requested quantity = 100 bags

Requested Quotation Entry 2
    ProductVariant → Concrete Block 100 mm
    requested quantity = 500 each

Requested Quotation Entry 3
    customer description = delivery to supplied address
```

These entries are immutable Enquiry submission evidence.

They SHALL NOT manufacture multiple Enquiry `SUBJECT` relationships.

They SHALL NOT create Quotation Items.

The merchant remains responsible for deciding what is actually quoted.

This amendment therefore does **not** resolve the general MS-PROT-043 multi-subject Enquiry question.

---

# 10. Requested Quotation Entry Integrity

Each Requested Quotation Entry MUST contain at least one of:

```text
a valid source semantic reference

or

customer-supplied description/specification
```

A client-supplied source reference is an untrusted locator.

At Enquiry submission the owning source MUST be revalidated according to applicable:

```text
Merchant Scope
source existence
source type
public interaction participation
Exposure
Enquiry applicability
```

Later source mutation MUST NOT rewrite what the customer requested.

The Enquiry retains sufficient submission provenance under composite MS-PROT-043.

---

# 11. Quotation Preparation

Merchant preparation precedes issuance.

Preparation data is Quotation-owned working state.

It is not yet:

```text
an issued offer
a customer commitment
historical quoted commercial truth
```

Preparation MAY be edited by an authorised merchant actor until issuance.

AI MAY assist preparation under Section 27.

Preparation MAY use:

```text
originating Enquiry context
current Offering/Product projections
customer context
merchant-entered custom scope
accepted Money primitives
```

Preparing a Quotation MUST NOT mutate those sources.

---

# 12. Issued Revision Immutability

Issuance creates one immutable Issued Quotation Revision.

After successful issue:

```text
issued description
quoted items
quoted quantities
quoted monetary amounts
currency
recipient snapshot/context
issuance timestamp
validity boundary
merchant commercial notes
source provenance
```

MUST remain historically reconstructible.

Later changes to:

```text
Offering
Product
ProductVariant
current catalogue price
customer contact details
merchant configuration
```

MUST NOT rewrite the issued revision.

A correction to issued commercial content requires another revision.

---

# 13. Quotation Revision Identity and Cardinality

Every Issued Quotation Revision MUST have identity unique within its owning Quotation.

Conceptually:

```text
IssuedQuotationRevisionIdentity
{
    quotationIdentity
    revisionIdentifier
}
```

Revision number MAY be projected for humans.

Revision number alone is not globally authoritative identity.

One Quotation MAY have:

```text
0..n Issued Quotation Revisions
```

Each Issued Quotation Revision MUST contain:

```text
1..n Quotation Items
```

Each Quotation Item MUST have identity unique within its exact Issued Quotation Revision.

---

# 14. Quotation Item Commercial Semantics

Every Quotation Item MUST preserve:

```text
quoted description/scope
exact MonetaryAmount contribution
source reference where present
source provenance where required
quantity + unit where quantity applies
unit amount where unit pricing is represented
```

All monetary values contributing to one Issued Quotation Revision MUST use one currency.

No implicit currency conversion is permitted.

Where:

```text
quantity
+
unit amount
```

are supplied, the resulting line amount MUST be calculated deterministically under accepted quantity/Money semantics.

Negative monetary values MUST NOT be used to manufacture discount, refund or other semantic meaning.

MS-PROT-095 does not introduce independent tax or discount authority.

A merchant-authored textual label does not make GrandRue authoritative for statutory tax correctness.

---

# 15. Exact Quoted Amount

The initial Quotation model represents an exact merchant-issued commercial amount.

It SHALL NOT issue an authoritative revision whose final commercial amount remains:

```text
unknown
open-ended
a range
FROM-only
AI-estimated
dependent on arbitrary merchant code
```

Those cases require later accepted semantics.

An Offering MAY be `QUOTE_REQUIRED`.

The resulting Issued Quotation Revision provides the separately owned exact quoted commercial terms anticipated by MS-PROT-055.

---

# 16. `QUOTE_REQUIRED` and Quotation Are Distinct

`QUOTE_REQUIRED` remains an Offering pricing form.

It means:

```text
the Offering does not itself carry
an executable final payable amount
```

It does not mean:

```text
a Quotation exists
```

Likewise, Quotation participation does not require `QUOTE_REQUIRED`.

An Offering MAY support:

```text
FIXED price
+
Ordering
+
Quotation
```

Example:

A supplier sells one bag of cement at a fixed retail price but permits bulk customers to request a quotation.

Therefore:

```text
Offering pricing form
    ≠
supported customer operation
```

---

# 17. Revision Lineage

An Issued Quotation Revision MAY reference zero or one immediately preceding revision within the same Quotation.

The resulting lineage MUST be linear.

Branching revision graphs are outside the initial model.

If a new revision is issued while the preceding revision remains outstanding:

```text
new revision issuance
+
prior-revision supersession
```

MUST commit within one authoritative Quotation consistency boundary.

The previously outstanding revision then becomes non-acceptable.

If the preceding revision was already:

```text
declined
withdrawn
expired
```

a new revision MAY follow it without rewriting that historical outcome.

If a revision was accepted, the Quotation lineage is closed to further issue.

A materially new commercial offer after acceptance requires a new Quotation or a separately governed downstream amendment.

---

# 18. Quotation Validity

An Issued Quotation Revision MAY contain:

```text
validUntil
```

`validUntil` is an absolute time boundary.

If `validUntil` is absent:

```text
automatic expiry = NOT APPLICABLE
```

If `validUntil` is present:

```text
acceptanceTime < validUntil
    → validity predicate satisfied

acceptanceTime >= validUntil
    → revision expired for new acceptance
```

Expiry is a derived consequence of the immutable validity boundary and authoritative time.

GrandRue SHALL NOT require an authoritative mutable `EXPIRED` status merely for presentation.

---

# 19. Lifecycle Facts, Not a Giant Status

Quotation SHALL NOT establish one universal authoritative enum such as:

```text
DRAFT
SENT
VIEWED
ACCEPTED
DECLINED
EXPIRED
CANCELLED
CONVERTED
PAID
```

because several of those terms belong to different owners or are projections.

Authoritative Quotation truth is composed from facts including:

```text
Quotation existence
+
Issued Quotation Revisions
+
revision lineage
+
Quotation Acceptance
+
Quotation Decline
+
Quotation Withdrawal
+
validUntil where supplied
```

Presentation MAY derive labels such as:

```text
Draft
Issued
Accepted
Declined
Expired
Withdrawn
Superseded
```

where the derivation is deterministic.

`Paid` is never a Quotation-owned lifecycle state.

---

# 20. Acceptance

`AcceptQuotationRevision` SHALL operate on one exact Issued Quotation Revision.

Acceptance MUST require:

```text
exact Merchant Scope
exact Quotation identity
exact revision identity
authorised recipient context
or authorised merchant-recorded external response
current outstanding revision
no prior Acceptance
no prior Decline
no prior Withdrawal
not superseded
validity predicate satisfied
current conflict/version revalidation
```

Successful acceptance establishes exactly one Quotation Acceptance for that revision.

Acceptance MUST preserve:

```text
exact revision identity
response provenance
responding principal/context
acceptance timestamp
accepted quoted total
```

The accepted commercial content is the immutable content of the referenced revision.

It SHALL NOT be copied into a second mutable Quotation truth.

---

# 21. Customer Acceptance Versus Merchant-Recorded Acceptance

GrandRue MUST distinguish:

```text
CUSTOMER-AUTHORISED ACCEPTANCE

from

MERCHANT-RECORDED EXTERNAL ACCEPTANCE
```

A customer-authorised acceptance proves only that the currently trusted recipient context executed the GrandRue acceptance operation.

A merchant-recorded external acceptance records the authorised merchant actor's assertion that acceptance was received outside the direct GrandRue customer operation.

Examples:

```text
telephone
in person
external email
```

Merchant-recorded acceptance MUST preserve that provenance.

GrandRue MUST NOT misrepresent merchant-recorded acceptance as cryptographically authenticated customer action.

---

# 22. Decline

`DeclineQuotationRevision` SHALL operate on one exact currently outstanding Issued Quotation Revision.

Successful decline establishes one immutable Quotation Decline.

After decline, that revision cannot be accepted.

The merchant MAY later issue another revision within the same Quotation subject to current issuance authority.

---

# 23. Withdrawal

`WithdrawQuotationRevision` SHALL be a merchant-authorised operation.

It applies only to an outstanding revision.

Successful withdrawal establishes one immutable Quotation Withdrawal.

A withdrawn revision cannot subsequently be accepted or declined as though still outstanding.

Withdrawal SHALL NOT delete the revision.

---

# 24. Requesting Changes

**Request changes** SHALL NOT mutate an Issued Quotation Revision.

Customer intent such as:

> Please quote for 700 units instead of 500.

is a customer request.

It SHALL be represented through:

```text
Enquiry
or
accepted Customer Communication semantics
```

referencing the exact Quotation / Issued Quotation Revision context.

The merchant may then:

```text
clarify
leave the existing revision unchanged
withdraw the existing revision
prepare another revision
```

Only the Quotation owner may issue the resulting commercial offer.

---

# 25. Customer Relationship and Surface Eligibility

Quotation SHALL define the owner-qualified customer relationship requirement:

```text
quotation / related-customer-quotation
```

It establishes that the current trusted customer access context is related to the exact Quotation in the exact Merchant Scope.

The requirement MAY be satisfied through an accepted:

```text
Quotation → CustomerContext relationship
```

combined with trusted access to that CustomerContext,

or:

```text
transaction-specific contextual access
bound to the exact Quotation
```

under existing customer-access authority.

The following SHALL NOT independently satisfy the requirement:

```text
Quotation identifier possession
email-address similarity
telephone similarity
name similarity
CustomerContext identifier supplied by client
CustomerAccount authentication alone
URL possession alone
```

A CustomerAccount is not universally required.

The physical secure-link, token or credential representation remains implementation/security architecture rather than Quotation semantics.

---

# 26. Offering and Merchant Configuration

Merchant Configuration MAY establish Quotation participation only through registered GrandRue semantics.

Configuration MAY express:

```text
Quotation applicable to merchant

Quotation supported for Offering A

Quotation not supported for Offering B

Quotation-intent Enquiry requirements
```

Business category SHALL NOT determine runtime behaviour.

Rejected:

```text
if businessType == CARPENTER
    enableQuotation()
```

Accepted:

```text
Merchant Offering O1
    supports Quotation
```

A merchant-level Quotation interaction MAY also exist without one exact Offering where accepted configuration establishes that interaction.

---

# 27. AI Boundary

AI MAY assist:

```text
interpreting merchant quotation intent
summarising originating Enquiry
suggesting draft scope wording
suggesting draft structure
extracting candidate quantities
preparing candidate Quotation Items
highlighting missing information
```

AI MUST NOT independently:

```text
issue a Quotation Revision
set an authoritative quoted amount
accept a quotation
record customer acceptance
withdraw a quotation
change an issued revision
invent executable pricing semantics
create an Order
create an Invoice
reserve stock
```

Candidate AI output remains non-authoritative until the applicable deterministic and authorised Quotation operation executes.

---

# 28. Commercial Access Classification

Quotation fulfils the existing BUSINESS reservation established by MS-PROT-056 v1.7.

The initially protected commercial purpose SHALL be:

```text
ISSUE_QUOTATION_COMMERCIAL_OFFER
```

The protected owner-qualified access contract SHALL be:

```text
quotation/commercial-offer-issuance-access@1
```

Standard allocation:

```text
BUSINESS
GROWTH
```

FREE SHALL NOT receive this protected purpose through the standard catalogue.

The following bounded contracts SHALL require no independent Commercial Entitlement:

```text
quotation/new-offer-preparation-access@1

quotation/existing-offer-observation-access@1

quotation/existing-offer-response-access@1

quotation/existing-offer-resolution-access@1
```

The final Commercial Entitlement identity and complete catalogue binding remain owned by composite MS-PROT-056 and `MS-PROT-056-V17-DQ-001`.

---

# 29. Commercial Meaning of Preparation

`quotation/new-offer-preparation-access@1` MAY support:

```text
create merchant working preparation
inspect source Enquiry
add candidate quoted items
calculate deterministic candidate totals
preview prospective quotation
```

It SHALL NOT:

```text
issue a commercial offer
create an accepted quote
create retained commercial permission
create an Order
create a Payment Obligation
reserve Inventory
```

A prepared draft does not grandfather a right to issue later.

`IssueQuotationRevision` MUST revalidate current protected commercial permission.

---

# 30. Existing Quotation After Entitlement Loss

Loss of protected Quotation commercial permission SHALL NOT:

```text
delete existing Quotations
delete issued revisions
hide required historical quotation records
delete acceptance/decline/withdrawal evidence
rewrite quoted terms
```

Existing quotation observation and resolution remain available to independently authorised actors through their no-independent-entitlement contracts.

A customer MAY still respond to a legitimately issued outstanding quotation after the merchant loses the protected issuance entitlement.

Reason:

```text
the merchant already issued the commercial offer
        ↓
recipient response resolves that existing offer
        ≠
merchant acquiring a new Quotation service
```

The merchant SHALL NOT issue a new revision without current protected Quotation permission.

Any downstream creation of:

```text
Order
Invoice
Appointment
Payment Obligation
```

must independently satisfy that owner's current authority and commercial requirements.

---

# 31. Public “Get a quotation” Exposure After Entitlement Loss

A public **Get a quotation** interaction represents intent to begin a new Quotation journey.

GrandRue SHALL NOT continue presenting that specific customer action when the merchant cannot currently satisfy the protected Quotation issuance path.

Canonical:

```text
Quotation configured
+
Offering supports Quotation
+
current Quotation issuance commercial permission
+
applicable Exposure
+
other independent predicates
        ↓
Get a quotation may participate
```

Loss of Quotation entitlement SHALL NOT require removal of:

```text
Offering
Product
general Enquiry
merchant website
```

if those remain independently applicable.

---

# 32. Ordering Boundary

Quotation Acceptance does not create an Order.

Canonical:

```text
Accepted Issued Quotation Revision
        ≠
Order
```

A separately authorised Ordering operation MAY consume an accepted Issued Quotation Revision as commercial-source provenance.

When it does:

```text
accepted quoted commercial terms
        ↓
candidate Order commercial source
```

The Order operation MUST independently revalidate all applicable Ordering predicates, including:

```text
Ordering Semantic Applicability
Ordering Commercial Entitlement
Actor Authorisation
current orderable-subject semantics
quantity compatibility
Inventory requirements
merchant policy
other accepted commitment requirements
```

Where the Order is explicitly established from an accepted Quotation Revision, current Offering price mutation MUST NOT silently replace the accepted quoted amount.

The accepted Quotation Revision is the quoted commercial source for the mapped Order commitment.

Ordering remains the owner of the resulting committed Order terms.

---

# 33. Quotation-to-Order Mapping

Where an Order is established from an accepted Quotation Revision, every resulting Order commitment portion deriving commercial terms from the quotation MUST retain enough provenance to identify:

```text
source Quotation
source Issued Quotation Revision
source Quotation Item or exact mapped quoted scope
```

The mapping MUST NOT silently:

```text
add unquoted commercial scope

increase quoted quantity

replace quoted subject

replace quoted amount
```

If the desired Order differs materially from the accepted quotation, another separately authorised commercial operation is required.

MS-PROT-095 does not redefine Order amendment.

---

# 34. Inventory Boundary

Quotation MUST NOT establish:

```text
Inventory Claim
stock reservation
stock allocation
stock movement
```

Canonical:

```text
Quotation issued
    ≠ stock reserved

Quotation accepted
    ≠ stock reserved
```

If a later Order requires stock protection, Inventory and Ordering own that invariant.

This prevents speculative quotation activity from consuming real inventory capacity.

---

# 35. Appointment and Booking Boundary

Quotation MAY originate from or lead to a business interaction involving Appointment or Booking.

Examples:

```text
Enquiry
    ↓
site-assessment Appointment
    ↓
Quotation
```

or:

```text
Quotation accepted
    ↓
merchant later establishes Appointment
```

Quotation SHALL NOT establish Appointment or Booking commitment.

Appointment and Booking retain their own:

```text
time
capacity
resource
customer commitment
rescheduling
cancellation
```

authority.

---

# 36. Payment Boundary

Quotation SHALL NOT establish:

```text
Payment Obligation
Payment Application
provider payment execution
provider payment evidence
Refund
```

Canonical:

```text
quoted amount
    ≠ amount due

accepted quotation
    ≠ payment received
```

A later accepted Payment-owning operation may derive an obligation from a separately governed commercial commitment.

Quotation does not acquire Payment ownership.

---

# 37. Invoicing Boundary

MS-PROT-095 establishes no Invoice authority.

Future Invoicing MAY consume:

```text
accepted Quotation provenance
Order provenance
other accepted commercial sources
```

but its semantics remain blocked by the independent `MS-PROT-084-DQ-006` Invoice Authority decision until governed separately.

Quotation Acceptance SHALL NOT automatically issue an Invoice.

---

# 38. Notification and Delivery Boundary

Successful issue commits the Issued Quotation Revision before optional external delivery.

Notification, email, SMS or another delivery mechanism MAY react post-commit.

Canonical:

```text
IssueQuotationRevision commits
        ↓
QuotationRevisionIssued fact/event
        ↓
Notification / delivery attempt
```

Delivery-provider failure SHALL NOT cause GrandRue to pretend the quotation was never issued.

GrandRue MAY report delivery failure separately and permit governed retry.

Likewise, successful email delivery is not proof that the recipient accepted or even read the quotation.

---

# 39. Document and PDF Boundary

A rendered:

```text
web page
PDF
email
printout
```

is a representation of an Issued Quotation Revision.

It is not the authoritative Quotation.

Regenerating a representation MUST reproduce the same issued commercial meaning.

Changing layout, typography or delivery format MUST NOT create a new quotation revision.

Changing commercial content requires a new issued revision.

---

# 40. Operation Contract — PrepareQuotation

**Operation:** `quotation.prepare`  
**Owner:** Quotation  
**Principal:** authorised merchant actor  
**Commercial Entitlement:** no independent entitlement under `quotation/new-offer-preparation-access@1`  
**Semantic Applicability:** Quotation must belong to the merchant's resolved semantics  
**Inputs:** merchant scope; optional recipient context; optional originating Enquiry; candidate items; candidate terms  
**Reads:** applicable Enquiry/source projections; CustomerContext where authorised; registered semantics  
**Mutates:** Quotation-owned preparation state only  
**Atomicity:** preparation mutation and its version/concurrency evidence  
**Success:** preparation saved  
**Business rejection:** structurally invalid or semantically unsupported preparation  
**Authorisation rejection:** actor lacks preparation authority  
**Technical failure:** no authoritative issue occurs  
**Events:** no issued-quotation event required  
**Projection effect:** merchant draft representation may update

Preparation does not create a commercial offer.

---

# 41. Operation Contract — IssueQuotationRevision

**Operation:** `quotation.issue`  
**Owner:** Quotation  
**Principal:** authorised merchant actor  
**Commercial Entitlement:** `ISSUE_QUOTATION_COMMERCIAL_OFFER`  
**Inputs:** exact Quotation; final preparation; optional predecessor revision  
**Reads:** current Quotation lineage; current actor authority; current commercial permission; referenced semantic identities; Money/quantity semantics  
**Mutates:** creates exactly one immutable Issued Quotation Revision; supersedes preceding outstanding revision where one exists  
**Atomicity:** new revision creation and required prior outstanding-revision supersession MUST be atomic  
**Success:** one issued revision identity returned  
**Business rejection:** invalid quotation content; no items; currency mismatch; invalid validity boundary; accepted lineage; unsupported quoted semantics  
**Entitlement rejection:** current protected permission absent  
**Authorisation rejection:** actor not authorised  
**Conflict:** lineage changed since preparation/revalidation  
**Technical failure:** no partial issued revision  
**Committed event:** `QuotationRevisionIssued` where the event portfolio is registered  
**Post-commit reactions:** Notification/delivery MAY react  
**Projection effect:** merchant/customer representations may expose the new revision according to current Exposure authority

Retry of the same logical issuance MUST reconcile to the same issued revision.

---

# 42. Operation Contract — AcceptQuotationRevision

**Operation:** `quotation.accept`  
**Owner:** Quotation  
**Principal:** authorised related customer context or authorised merchant actor recording external acceptance  
**Commercial Entitlement:** no independent entitlement under `quotation/existing-offer-response-access@1`  
**Inputs:** exact Quotation; exact Issued Quotation Revision; response provenance  
**Reads:** current revision lineage; existing response facts; validity boundary; current recipient relationship/access  
**Mutates:** exactly one Quotation Acceptance  
**Atomicity:** acceptance and outstanding-revision conflict protection  
**Success:** acceptance fact established  
**Business rejection:** expired, declined, withdrawn, superseded or already incompatibly resolved revision  
**Authorisation rejection:** recipient relationship or merchant authority not established  
**Conflict:** concurrent revision/withdrawal/response won first  
**Technical failure:** committed result must be recoverable by idempotent retry  
**Committed event:** `QuotationRevisionAccepted` where registered  
**Post-commit reactions:** downstream Attention/Notification MAY react; no Order/Invoice creation is implied  
**Projection effect:** accepted presentation may be derived

---

# 43. Operation Contract — DeclineQuotationRevision

**Operation:** `quotation.decline`  
**Owner:** Quotation  
**Principal:** authorised related customer context or authorised merchant actor recording external decline  
**Commercial Entitlement:** no independent entitlement  
**Inputs:** exact Quotation and revision; response provenance  
**Mutates:** exactly one Quotation Decline  
**Success:** decline recorded  
**Business rejection:** revision already accepted, withdrawn, superseded or already declined  
**Conflict:** incompatible concurrent resolution  
**Technical failure:** retry reconciles to committed result where already committed  
**Post-commit effect:** no automatic downstream mutation

---

# 44. Operation Contract — WithdrawQuotationRevision

**Operation:** `quotation.withdraw`  
**Owner:** Quotation  
**Principal:** authorised merchant actor  
**Commercial Entitlement:** no independent entitlement under existing-offer resolution  
**Inputs:** exact outstanding revision  
**Mutates:** one Quotation Withdrawal  
**Success:** revision becomes non-acceptable  
**Business rejection:** already accepted, declined, withdrawn or superseded  
**Conflict:** concurrent recipient acceptance/decline or revision issuance won first  
**Technical failure:** retry must reconcile where withdrawal already committed

---

# 45. Retry and Duplicate Semantics

All mutation operations are reachable through retriable transports and repeated UI actions.

For each duplicate-sensitive command:

1. the same logical invocation MUST have stable command identity;
2. retry after committed-but-unacknowledged success MUST reconcile to the committed result;
3. repeated transport MUST NOT multiply authoritative effects;
4. genuinely separate merchant intent MAY produce another otherwise identical Quotation or revision.

Canonical:

```text
same merchant
same recipient
same amount
same content
    ≠
proof of duplicate intent
```

Technical retry and repeated human intent remain distinct.

---

# 46. Concurrency

The Quotation consistency boundary MUST prevent incompatible concurrent interpretations of one outstanding revision.

At minimum, these races require authoritative serialization/conflict detection:

```text
accept
vs
merchant issues replacement revision

accept
vs
withdraw

decline
vs
accept

two replacement issues

two incompatible recipient responses
```

For one exact revision, at most one incompatible terminal response/outcome may commit.

Example:

```text
Customer accepts revision R1
Merchant simultaneously issues R2

exactly one operation may win
against the same outstanding-R1 predicate
```

If acceptance wins first:

```text
R1 accepted
new revision rejected
```

If revision issuance wins first:

```text
R1 superseded
R2 issued
R1 acceptance rejected
```

Implementation technology remains downstream.

---

# 47. Failure and Rejection Semantics

Quotation operations MUST distinguish at least:

```text
SEMANTICALLY_INAPPLICABLE

COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

ACTOR_NOT_AUTHORISED

RECIPIENT_RELATIONSHIP_UNSATISFIED

INVALID_INPUT

QUOTATION_NOT_FOUND_OR_NOT_ACCESSIBLE

REVISION_NOT_CURRENT

REVISION_EXPIRED

REVISION_SUPERSEDED

REVISION_WITHDRAWN

REVISION_DECLINED

REVISION_ALREADY_ACCEPTED

CONFLICT

TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

A failure to send email MUST NOT be represented as:

```text
QUOTATION_NOT_ISSUED
```

when issuance already committed.

An expired quotation MUST NOT be represented as an authorisation failure.

A commercial-entitlement denial MUST NOT be represented as an invalid quoted price.

---

# 48. Historical Affinity

Issued Quotation Revisions are historical commercial truth.

They MUST retain sufficient semantic/configuration provenance to remain interpretable after:

```text
Merchant Configuration revision
semantic release change
Offering mutation
Product mutation
application deployment
commercial catalogue change
```

Newer configuration MUST NOT silently reinterpret an issued revision.

Historical executable support remains subject to the applicable accepted semantic-release/runtime compatibility authorities.

---

# 49. Projection and Exposure

Authoritative Quotation truth remains distinct from representation.

Canonical:

```text
Quotation authoritative facts
        ↓
Quotation projection
        ↓
Exposure
        ↓
merchant/customer surface
```

A UI button, PDF state or cached read model MUST NOT become mutation authority.

`Viewed` MAY be delivery/presentation evidence where separately governed.

It SHALL NOT become required Quotation lifecycle truth merely because a user interface wants a “Viewed” label.

---

# 50. Business-Type Neutrality

The same Quotation authority MUST support materially different merchant domains without industry branches.

### Supplier

```text
cement × 100
blocks × 500
delivery
```

### Carpenter

```text
bespoke wardrobe
materials
installation
```

### Mechanic

```text
parts
labour
diagnostic/work scope
```

### Caterer

```text
event service
guest quantity
delivery/staffing scope
```

### Consultant

```text
defined professional service
custom scope
```

Differences belong in:

```text
Offering
Enquiry requirements
quoted scope
quantity/unit semantics
merchant configuration
```

not:

```text
if supplier
if carpenter
if mechanic
```

---

# 51. Falsification

The proposal has been challenged against the following cases.

| Challenge | Required result |
|---|---|
| Low-software-capacity plumber receives “Get a quotation” request | Existing Enquiry receives the request; merchant can prepare one simple quoted amount without configuring software abstractions |
| Supplier customer requests 100 cement bags + 500 blocks | One Enquiry may contain multiple Requested Quotation Entries without inventing multi-SUBJECT Enquiry semantics |
| Mechanic quotes parts and labour | Same Quotation model supports goods + services |
| Carpenter quotes custom work absent from catalogue | Custom Quotation Item permitted; Product creation not forced |
| Merchant receives request by telephone | Merchant may create Quotation with no originating Enquiry |
| Offering fixed price is £10 but bulk quotation required | Offering can support Ordering and Quotation simultaneously; `FIXED` does not prohibit Quotation |
| Offering is `QUOTE_REQUIRED` | No payable amount exists until separately issued Quotation |
| Product price changes after issue | Issued quotation amount remains unchanged |
| Product is renamed after issue | Historical issued wording remains reconstructible |
| Product becomes non-orderable after acceptance | Quotation remains historical truth; downstream Ordering independently rejects if Order requirements fail |
| Customer asks to change quantity | Existing revision is not edited; request is Enquiry/communication; merchant may issue another revision |
| Customer accepts superseded revision | Rejected |
| Customer accepts withdrawn revision | Rejected |
| Customer accepts at `validUntil` | Rejected because acceptance requires `acceptanceTime < validUntil` |
| Customer accepts before `validUntil` | Acceptance may commit if all other predicates succeed |
| Merchant issues R2 while customer accepts R1 concurrently | Serialization permits only one interpretation of R1 as outstanding |
| `quotation.issue` commits but HTTP response is lost | Retry returns/reconciles to same issued revision |
| Acceptance commits but acknowledgement is lost | Retry returns/reconciles to same acceptance |
| Same customer intentionally requests two separate quotations | Separate intent may produce separate Quotations |
| Quotation issued then merchant downgrades | Existing revision survives and may be observed/responded to; new revision issue denied without protected permission |
| Email provider unavailable after issue | Issued revision remains authoritative; delivery failure handled independently |
| Customer accepts quotation | No Inventory Claim created |
| Customer accepts quotation | No Order created |
| Customer accepts quotation | No Payment Obligation created |
| Customer accepts quotation | No Invoice created |
| Customer has only quote identifier | Customer relationship requirement not satisfied solely by identifier possession |
| AI drafts price | Draft remains candidate only; AI cannot issue authoritative quoted amount |
| Information-only merchant has no Quotation | No Quotation machinery forced into that merchant's configuration |
| Million merchants use Quotation | Reusable capability semantics remain stable; no merchant-specific semantic types are created |

The cross-domain claim therefore survives materially different supplier, trade, mixed goods/services and professional-service scenarios without business-type branching.

---

# 52. Alternatives and Trade-Offs

## Alternative A — Use Enquiry Alone

Rejected.

Enquiry correctly owns the customer request but does not own:

```text
merchant-issued quoted commercial terms
revision history
quotation validity
acceptance
withdrawal
```

Adding those semantics to Enquiry would turn Enquiry into a commercial-offer owner and violate its existing boundary.

---

## Alternative B — Represent a quotation as an Order in draft state

Rejected.

An Order is an accepted purchase/order commitment.

A quotation may be:

```text
issued
declined
expired
withdrawn
```

without an Order ever existing.

Making Quotation a draft Order would corrupt Ordering semantics.

---

## Alternative C — Treat a quotation as a PDF/document

Rejected.

A PDF is representation.

The business truth must survive:

```text
PDF regeneration
email-provider change
website rendering change
print delivery
```

Document representation therefore cannot own Quotation semantics.

---

## Alternative D — Introduce `QuotationRequest`

Rejected.

MS-PROT-043 already owns the customer-side request.

A second request object would create:

```text
duplicate customer request truth
duplicate requirements
duplicate provenance
extra reconciliation
extra merchant administration
```

The correct composition is Enquiry → Quotation.

---

## Alternative E — Mutable issued Quotation

Rejected.

Editing an already issued commercial offer would make it impossible to determine reliably what the recipient received or accepted.

Immutable issued revisions are required.

---

## Alternative F — Automatic Order on Acceptance

Rejected.

Some merchants use accepted quotations before:

```text
scheduling
internal confirmation
deposit handling
invoice issue
manual work commencement
```

Automatic Order creation would impose one business workflow and violate capability ownership.

---

## Selected Approach

```text
Enquiry-owned request
+
Quotation-owned merchant offer
+
immutable issued revisions
+
explicit recipient response
+
explicit downstream handoff
```

This introduces more internal provenance/versioning than a mutable quotation row.

That cost is accepted because it preserves commercial truth, retry safety and cross-capability ownership while keeping merchant interaction simple.

---

# 53. Mandatory Ambiguity Review

The mandatory ambiguity review under MS-DESIGN-RULES-001 §77 produces the following result.

1. **Materially different normative interpretations:** PASS — core predicates and ownership are explicit.
2. **One owner per authoritative fact:** PASS.
3. **Overloaded terminology qualified:** PASS — Quotation, Enquiry, Order, Invoice and Payment are separated.
4. **Enabled/available/active/valid/status terminology:** PASS — no generic flag controls execution.
5. **Determinable conditions:** PASS — validity and response predicates are explicit.
6. **Pronouns/references:** PASS — normative ownership uses named concepts.
7. **Cardinalities:** PASS for initial Quotation scope.
8. **Identity/equality:** PASS.
9. **Transaction/concurrency guarantees:** PASS at the semantic boundary.
10. **Retries/duplicates:** PASS.
11. **Business rejection versus technical failure:** PASS.
12. **Configuration/runtime/projection separation:** PASS.
13. **Applicability/entitlement/authorisation/eligibility/readiness/exposure:** PASS.
14. **Provider facts versus business truth:** PASS.
15. **Merchant policy versus platform policy:** PASS.
16. **AI inference versus execution:** PASS.
17. **Examples subordinate to rules:** PASS.
18. **Negative ownership boundaries:** PASS.
19. **Cross-document references:** PASS subject to normal authority-index resolution.
20. **Amendment scope:** PASS.
21. **Open questions:** PASS for the governed initial semantic scope; future capabilities are explicitly excluded rather than left as normative gaps.
22. **Testable invariants:** PASS.
23. **Implementation without inventing business rules:** PASS for semantic scope; downstream implementation architecture remains separately governed.
24. **Fundamental Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`.
25. **Merchant-visible complexity:** PASS — quotation business actions only.
26. **Ordinary-staff training burden:** PASS — no architectural concepts exposed.

No A–G ambiguity is retained within the initial Quotation semantic scope.

---

# 54. Core Invariants

The following are hard invariants.

1. A customer Quotation request is an Enquiry, not a Quotation.
2. There is no separate `QuotationRequest` Operational Object.
3. Quotation owns merchant-issued quoted commercial terms.
4. A Quotation belongs to exactly one Merchant Scope.
5. A Quotation has exactly one recipient context.
6. A Quotation MAY exist without an originating Enquiry.
7. An Issued Quotation Revision is immutable.
8. Every issued revision contains at least one Quotation Item.
9. One issued revision uses one currency.
10. Later source-price mutation does not rewrite issued quoted terms.
11. A new revision is required to change issued commercial content.
12. At most one incompatible terminal response/outcome may commit for one exact revision.
13. An accepted revision cannot later be withdrawn, declined or superseded.
14. A withdrawn revision cannot later be accepted.
15. A declined revision cannot later be accepted.
16. A superseded revision cannot later be accepted.
17. An expired revision cannot newly be accepted.
18. `Request changes` does not mutate an issued revision.
19. Quotation Acceptance does not create an Order.
20. Quotation Acceptance does not reserve Inventory.
21. Quotation Acceptance does not create a Payment Obligation.
22. Quotation Acceptance does not create an Appointment or Booking.
23. Quotation Acceptance does not create an Invoice.
24. Provider delivery failure does not rewrite committed Quotation truth.
25. A rendered document does not own Quotation semantics.
26. Business category does not determine Quotation runtime behaviour.
27. AI cannot issue or accept an authoritative Quotation.
28. Commercial entitlement does not determine Merchant Configuration.
29. Existing issued quotation history survives later commercial-entitlement loss.
30. New issue/revision requires current `ISSUE_QUOTATION_COMMERCIAL_OFFER` permission.
31. Existing recipient response does not independently require that issuance entitlement.
32. A downstream owner must independently authorise and establish its own business truth.
33. Technical retry must not multiply Quotation effects.
34. Concurrent incompatible operations against one outstanding revision cannot both commit.

---

# 55. Deferred and Future Scope

The following remain explicitly outside MS-PROT-095 v1.0:

```text
Estimate

partial quotation-item acceptance

customer-selected alternatives/options

formal legal signature

jurisdiction-specific quotation/contract law

multi-currency quotation

automatic tax determination

merchant-authored executable formulas

procurement/vendor quotations

competitive tendering

supplier comparison

purchase orders

automatic quotation-to-Order conversion

automatic quotation-to-Invoice conversion

advanced estimating engines
```

Future need for any of these requires normal Feature Admission and governed design.

They SHALL NOT be inferred during implementation from conventional quotation software.

---

# 56. Implementation and Programme Impact

This authority, if approved, establishes semantics only.

It does not activate implementation.

Implementation planning MUST:

```text
map Quotation into MS-IMP-001
review IMPLEMENTATION-RULES impact
establish exact design-to-code traceability
write tests before production behaviour
respect dependency-driven implementation order
```

At minimum downstream implementation evidence must prove:

```text
issued-revision immutability
issue idempotency
acceptance idempotency
revision/acceptance concurrency
withdrawal/acceptance concurrency
historical price preservation
customer relationship isolation
commercial-entitlement enforcement
Enquiry request integration
no automatic cross-capability mutation
provider-delivery failure isolation
```

Customer self-service acceptance also requires executable trusted customer contextual-access support under the applicable accepted security/access architecture.

The physical token/session mechanism is not determined by MS-PROT-095.

---

# 57. Commercial Catalogue Consequence

If approved, MS-PROT-095 supplies the Quotation owner classifications required for later complete Commercial catalogue resolution:

```text
protected:
quotation/commercial-offer-issuance-access@1
    →
ISSUE_QUOTATION_COMMERCIAL_OFFER
    →
BUSINESS + GROWTH

no independent Commercial Entitlement:
quotation/new-offer-preparation-access@1
quotation/existing-offer-observation-access@1
quotation/existing-offer-response-access@1
quotation/existing-offer-resolution-access@1
```

MS-PROT-095 does not mint the final `CommercialEntitlementIdentity`.

`MS-PROT-056-V17-DQ-001` therefore remains OPEN.

`MS-PROT-056-V17-DQ-004` is partially resolved only for the Quotation reservation.

---

# 58. Lexicon Consequence

The current Canonical Semantic Lexicon does not yet define Quotation.

If MS-PROT-095 is explicitly approved and formalised, the same governance completion cycle MUST add canonical entries for at least:

```text
Quotation
Issued Quotation Revision
Quotation Item
Quotation Acceptance
Quotation Decline
Quotation Withdrawal
```

The Lexicon update SHALL summarise MS-PROT-095.

It SHALL NOT become the semantic owner.

---

# 59. Conformance Conditions

An implementation conforms to MS-PROT-095 only if all applicable conditions below hold.

```text
[ ] customer Get-a-quotation request is Enquiry-owned
[ ] no duplicate QuotationRequest Operational Object exists
[ ] Quotation uses capability-scoped identity
[ ] merchant scope is mandatory
[ ] recipient context is exact
[ ] guest/customer access does not trust identifier possession
[ ] merchant can create Quotation without originating Enquiry
[ ] multi-item supplier request does not invent multi-SUBJECT Enquiry semantics
[ ] issued revisions are immutable
[ ] issued revision contains 1..n items
[ ] one currency governs one revision
[ ] issued quoted amounts survive current-source mutation
[ ] validity predicate is exact
[ ] revision lineage is linear
[ ] outstanding-revision supersession is atomic
[ ] acceptance targets exact revision
[ ] response provenance distinguishes customer action from merchant-recorded external response
[ ] request-changes path does not mutate quotation
[ ] retry does not duplicate effects
[ ] concurrency prevents incompatible outcomes
[ ] Quotation does not reserve Inventory
[ ] Quotation does not create Payment truth
[ ] Quotation does not create Order truth
[ ] Quotation does not create Appointment/Booking truth
[ ] Quotation does not create Invoice truth
[ ] accepted Quotation may serve only as explicit commercial-source provenance
[ ] downstream owners independently revalidate their own requirements
[ ] protected issue permission is BUSINESS + GROWTH
[ ] existing quotation response/resolution survives entitlement loss
[ ] public Get-a-quotation exposure does not advertise unavailable protected issuance
[ ] provider delivery cannot redefine Quotation truth
[ ] AI remains assistive
[ ] business type does not branch runtime semantics
[ ] required implementation tests exist before production code is accepted
```

---

# 60. Amendment Effect

If explicitly approved and conformingly formalised, MS-PROT-095 v1.0 shall:

1. establish Quotation as a first-class GrandRue semantic owner;
2. activate the previously deferred Quotation semantic scope anticipated by MS-PROT-055;
3. preserve Enquiry as the owner of customer quotation requests;
4. add bounded Quotation-intent Enquiry request-entry semantics without resolving general multi-subject Enquiry cardinality;
5. establish immutable issued Quotation revisions and explicit response facts;
6. establish Quotation commercial-access classifications under the existing BUSINESS reservation;
7. permit accepted Quotation revisions to serve as explicit commercial-source provenance for separately authorised Ordering;
8. preserve all neighbouring capability ownership;
9. introduce no Invoice authority;
10. authorise no implementation merely by acceptance of this semantic authority.

After formalisation, applicable:

```text
AUTHORITY-INDEX
DEFERRED-DECISION-REGISTER
CANONICAL-SEMANTIC-LEXICON
IMPLEMENTATION-RULES impact review
DESIGN-CORPUS-CONFORMANCE
```

work must complete before the governance cycle is considered complete.

---

# 61. Recommendation

```text
RECOMMENDATION: ACCEPT
```

Evidence supporting the recommendation:

- Quotation passes Feature Admission.
- Vision Conformance is `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`.
- existing Enquiry authority is reused rather than duplicated;
- commercial-term ownership aligns with MS-PROT-055;
- Offering participation remains business-type neutral;
- Ordering, Inventory, Payment, Appointment and future Invoicing retain independent ownership;
- supplier, tradesperson, mixed goods/services and professional-service scenarios survive falsification;
- retry and concurrency semantics prevent duplicate or contradictory commercial truth;
- merchant-visible complexity remains ordinary quotation work;
- credible simpler alternatives either fail to represent quotation meaning or violate existing semantic ownership.

This recommendation is a design-review conclusion only.

```text
RECOMMENDATION: ACCEPT
        ≠
MANUAL APPROVAL: GRANTED
        ≠
STATUS: ACCEPTED
```

Explicit manual approval was granted on 19 September 2026. Repository formalisation remains governed by the post-approval revalidation and governance-completion rules in `MS-DESIGN-RULES-001`.
