# MS-PROT-096 — Invoice, Issuance & Payment Relationship Model

**Document ID:** MS-PROT-096  
**Version:** 1.0  
**Status:** **ACCEPTED**  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after Fundamental Vision Conformance, full design review, cross-domain falsification, ambiguity review, complete pre-approval presentation and explicit confirmation of CustomerContext linkage  
**Authority type:** Material Invoicing semantic/design authority  
**Governed by:** `MS-DESIGN-RULES-001` v2.5; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Resolves:** `MS-PROT-084-DQ-006 — Native Invoice Authority`  
**Amends:** composite MS-PROT-055 within Invoice commercial-source, Payment Obligation composition and customer-payment relationship scope; composite MS-PROT-084 within Invoice ownership and receivable non-duplication scope; composite MS-PROT-056 within native-Invoicing reservation admission and owner-qualified commercial-access classification  
**Preserves:** Payment ownership of Payment Obligation, PaymentApplication, payment execution/evidence, Amount Due and Refund; Financial Operations residual ownership; Order, Booking, Appointment and Quotation ownership; CustomerContext ownership boundaries; Exposure ownership; Notification ownership; provider neutrality; merchant authority; business-type neutrality  
**Partially resolves:** `MS-PROT-056-V17-DQ-004` within native Invoicing only; `MS-PROT-056-V17-DQ-001` by supplying Invoicing-owned commercial classifications only  
**Depends on:** composite MS-PROT-020; composite MS-PROT-021; composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-040; composite MS-PROT-043; composite MS-PROT-049; composite MS-PROT-053; composite MS-PROT-055; composite MS-PROT-056; composite MS-PROT-057; composite MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; composite MS-PROT-069; composite MS-PROT-074; composite MS-PROT-075; composite MS-PROT-077; composite MS-PROT-084; composite MS-PROT-092; MS-PROT-095 v1.0; applicable Booking, Appointment, Exposure, Data Protection and customer-access authorities  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`  
**Purpose:** Establish native customer Invoicing as the GrandRue semantic owner of merchant-issued billing records while preserving Payment as the sole owner of customer monetary obligations, payment execution and amount-due truth; support invoices across goods, services and mixed merchant operations; resolve the existing Invoice Authority deferral without creating accounting, tax, procurement or generic financial-ledger authority.

---

# Authority Identity Preflight

Authority preflight:

```text
branch
    development

HEAD
    c647545520f8597729cdf68ccda98ff6b9562a79

current accepted semantic/design composition
    MS-PROT-020..095
    scope-aware

MS-PROT-096
    no repository-resident authority found

candidate
    MS-PROT-096 v1.0

canonical authority directory if approved
    designs/authorities/ms-prot/MS-PROT-096/
```

This preflight does not reserve the identifier.

Immediately before any post-approval repository formalisation, GrandRue MUST revalidate the current `development` HEAD, authority composition and identifier availability.

---

# 0. Fundamental Vision Conformance

## 0.1 Feature Admission

Native Invoicing satisfies the **Representation Test**.

GrandRue already represents:

```text
commercial commitments
quoted commercial offers
Payment Obligations
Payments
Financial Operations
```

but currently cannot represent the merchant business act:

> **Issue this customer an invoice for this billed scope.**

Without native Invoicing, merchants must leave GrandRue to create and administer billing records elsewhere.

Invoicing also satisfies the **Coordination Test** because an Invoice must coordinate existing GrandRue truth without taking ownership from:

```text
Quotation
Order
Booking
Appointment
Payment
Financial Operations
```

Invoicing additionally satisfies the **Administrative-Compression Test** by removing avoidable re-entry of customer, commercial and payment information.

---

## 0.2 Business-to-Software Translation

Merchant-facing operations use ordinary business language:

```text
Create invoice
Issue invoice
Send invoice
View invoice
Withdraw invoice
Cancel amount billed
Pay invoice
```

Merchants MUST NOT configure:

```text
Payment Obligation aggregates
cross-capability transaction boundaries
customer relationship evaluators
commercial access contracts
idempotency keys
semantic object namespaces
financial reconciliation graphs
```

GrandRue absorbs those concerns.

---

## 0.3 Administrative Compression

GrandRue SHOULD carry forward authorised existing information such as:

```text
merchant identity
customer context
accepted Quotation
Order
commercial descriptions
amounts
Payment state
```

rather than requiring duplicate entry.

The merchant retains control over what is actually invoiced.

GrandRue does not autonomously decide that work should be billed.

---

## 0.4 Ordinary Staff

An ordinary authorised staff member may:

```text
prepare invoice
issue invoice
view invoice
send existing invoice
withdraw invoice
```

without understanding Invoicing internals.

Required complexity remains the complexity of billing customers rather than the complexity of GrandRue architecture.

---

## 0.5 Target-Market Proportionality

The initial authority deliberately excludes:

```text
general ledger
double-entry accounting
accounts-receivable ledger
statutory bookkeeping
tax calculation
VAT determination
tax-invoice compliance
credit-note authority
procurement
supplier invoice workflow
purchase orders
progress billing
construction valuations
retentions
complex recurring billing
subscription billing
multi-currency invoicing
legal debt adjudication
```

These are not prerequisites for useful native Invoicing.

---

## 0.6 Native Ownership Versus Integration

Native ownership is justified because GrandRue must authoritatively know:

```text
which Invoice was issued
to whom
when
under which merchant scope
what billed items it contained
what total it stated
which Payment Obligation it concerns
whether the Invoice was withdrawn
whether Invoice-created billing was cancelled
```

A PDF/email provider cannot own these business facts.

---

## 0.7 Vision Result

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

The additional internal complexity is required to keep billing history, Payment truth and customer access correct while presenting a simple merchant experience.

---

# 1. Governing Decision

GrandRue SHALL establish **Invoicing** as the semantic owner of merchant-issued customer Invoice records.

Canonical separation:

```text
COMMERCIAL SOURCE / BUSINESS CONTEXT
        ↓

INVOICE
    what the merchant billed
    and presented to the customer
        ↓

PAYMENT
    what monetary obligation exists
    what remains payable
    what has been paid
    what was refunded
```

Hard governing rule:

> **Invoice owns the bill. Payment owns the debt/payment obligation and payment truth. Financial Operations may consume those facts but MUST NOT duplicate them.**

An Invoice SHALL NOT own `AmountDue`.

---

# 2. Scope

This authority governs:

- Invoice identity;
- merchant scope;
- recipient context;
- Invoice preparation;
- Invoice Items;
- Invoice total;
- Invoice Reference;
- immutable issuance;
- billing-source provenance;
- Invoice-to-Payment relationship;
- withdrawal;
- Invoice-created billing cancellation;
- payment-deadline presentation;
- customer access;
- merchant customer-history relationship;
- Payment projection onto an Invoice;
- commercial access;
- provider/delivery boundaries;
- retry/idempotency;
- concurrency;
- historical affinity;
- cross-domain applicability.

---

# 3. Explicit Non-Goals

MS-PROT-096 v1.0 SHALL NOT establish:

```text
supplier/vendor inbound Invoice authority

procurement

purchase orders

accounts payable workflow

tax calculation

VAT/GST/sales-tax determination

statutory tax-invoice compliance

general ledger

double-entry accounting

revenue recognition

credit notes

customer account statements

receipts

estimates

debt collection

late-payment fees

interest calculations

legal enforceability of debt

invoice financing

factoring

recurring subscription billing

partial/progress invoicing semantics

invoice allocation across multiple prior obligations

multi-currency Invoice

automatic Invoice issuance from Order/Appointment/Quotation

arbitrary merchant executable formulas

generic document workflow
```

---

# 4. Outbound Invoice Versus Supplier Invoice

This authority governs **merchant-issued customer invoices**.

Example:

```text
building-material supplier
        ↓
issues Invoice to builder/customer
```

That merchant is using GrandRue Invoicing.

By contrast:

```text
GrandRue merchant
        ↓
receives Invoice from its own supplier
```

remains an inbound financial-document / Payable concern.

That flow remains governed by:

```text
MS-PROT-092
+
MS-PROT-084
```

and does not create a second Invoicing direction.

---

# 5. Canonical Terminology

## 5.1 Invoice

An **Invoice** is:

> **A merchant-scoped Operational Object representing one merchant-issued customer billing record.**

Canonical Operational Object type:

```text
invoicing / invoice
```

An Invoice may exist as mutable merchant preparation before issuance.

After issuance, its commercial content becomes immutable.

---

## 5.2 Invoice Item

An **Invoice Item** is:

> **An identity-bearing component of one Invoice describing one billed commercial scope and one exact billed monetary contribution.**

An Invoice Item MAY contain:

```text
description
quantity + unit
unit amount
line amount
optional source provenance
```

where those semantics are supported.

---

## 5.3 Invoice Reference

An **Invoice Reference** is:

> **A merchant-scoped human-facing immutable reference allocated to one Invoice when that Invoice is issued.**

Invoice Reference is not Invoice identity.

Canonical:

```text
InvoiceIdentity
    stable operational identity

InvoiceReference
    human-facing business reference
```

The reference MUST be unique within its Merchant Scope and MUST NOT be reused after allocation.

Its exact formatting is implementation scope.

This authority does not claim statutory sequential-number compliance.

---

## 5.4 Invoice Payment Binding

An **Invoice Payment Binding** is:

> **The immutable relationship between one issued Invoice and exactly one Payment Obligation.**

Every issued Invoice MUST have exactly one Invoice Payment Binding.

---

## 5.5 Invoice Withdrawal

An **Invoice Withdrawal** is:

> **An immutable Invoicing-owned fact that the merchant no longer presents one issued Invoice as the current customer billing representation.**

Withdrawal does not itself change the Payment Obligation.

---

## 5.6 Invoice Billing Cancellation

An **Invoice Billing Cancellation** is:

> **An immutable Invoicing-owned fact cancelling billing created by an Invoice whose issuance itself established its Payment Obligation.**

Billing Cancellation coordinates a Payment-owned reduction of that Invoice-created Payment Obligation.

It does not delete the historical Invoice.

---

# 6. Identity and Equality

Canonical Invoice identity:

```text
InvoiceIdentity
{
    merchantScope
    invoiceIdentifier
}
```

Attribute similarity does not imply identity.

Two Invoices may contain:

```text
same merchant
same recipient
same items
same amount
same source references
```

and remain different Invoices when they originate from distinct merchant billing intent.

Technical retry MUST NOT use content similarity to determine duplicate business identity.

---

# 7. Merchant Scope

Every Invoice belongs to exactly one Merchant Scope.

All source references, recipient context and Payment relationships MUST satisfy applicable Merchant Scope invariants.

Cross-merchant Invoice mutation is prohibited.

---

# 8. Recipient Context

Every Invoice MUST concern exactly one customer/recipient context.

The recipient MAY be represented through:

```text
CustomerContext

or

accepted exact transaction-specific
guest/customer access context
```

A CustomerAccount is not universally required.

Contact-value similarity MUST NOT establish Invoice relationship.

---

# 9. CustomerContext Relationship and Merchant Customer History

Where an Invoice recipient is represented by a `CustomerContext`, the Invoice SHALL retain an owner-qualified relationship to that exact CustomerContext.

Canonical:

```text
CustomerContext C1
        ↑
        │ relationship/reference
        │
Invoice I1
```

This relationship means:

> This Invoice concerns the customer relationship represented by CustomerContext C1 within this Merchant Scope.

It does not transfer Invoice ownership to CustomerContext.

GrandRue merchant customer-history projections MAY compose authorised Invoice information through this relationship together with other independently owned customer activity such as:

```text
Enquiries
Quotations
Orders
Bookings
Appointments
Customer Communication
```

The customer-history projection MUST NOT maintain a competing mutable copy of Invoice truth.

If an Invoice uses exact guest / transaction-specific customer context instead:

```text
no CustomerContext
    → no fake CustomerContext is manufactured merely for invoicing
```

A later legitimate CustomerContext reconciliation MAY associate the customer relationship under separately accepted CustomerContext authority without rewriting historical Invoice content or provenance.

---

# 10. Customer Relationship Requirement

Invoicing SHALL define:

```text
invoicing / related-customer-invoice
```

The requirement means:

> The current trusted customer context is authoritatively related to this exact Invoice in this exact Merchant Scope.

The relationship MAY be established from:

```text
Invoice → CustomerContext
```

or accepted narrow contextual access.

The following are insufficient independently:

```text
Invoice Reference
Invoice identifier
email match
telephone match
customer name
URL possession
CustomerAccount authentication alone
client-supplied CustomerContext ID
AI inference
```

---

# 11. Draft Preparation

Before issue, Invoice preparation is mutable merchant working state.

An authorised merchant actor MAY:

```text
add item
remove item
change description
change quantity
change amount
change recipient
change source provenance
set/remove supported payment deadline
discard the draft
```

A discarded never-issued draft does not constitute an issued financial/business record.

---

# 12. Invoice Items

At issue, an Invoice MUST contain:

```text
1..n Invoice Items
```

Each item MUST have identity unique within its Invoice.

Every issued item MUST preserve enough information to reconstruct its commercial meaning.

---

# 13. Monetary Rules

One Invoice uses exactly one currency.

Every Invoice Item line amount MUST use that currency.

Invoice total is:

```text
sum(exact Invoice Item line amounts)
```

Invoice total MUST be:

```text
> 0
```

Negative monetary values MUST NOT encode:

```text
discount
refund
credit
tax adjustment
```

MS-PROT-055 Money rules remain governing.

---

# 14. Quantity and Unit Pricing

Where an Invoice Item contains:

```text
quantity
+
unit amount
```

GrandRue MUST calculate the line amount deterministically using accepted quantity and Money semantics.

Merchant-authored arbitrary executable formulas remain prohibited.

An authorised merchant MAY instead enter one exact line amount where no registered quantity calculation is required.

---

# 15. Tax and Merchant-Authored Labels

An Invoice Item description is merchant-authored business content.

A merchant may enter wording such as:

```text
VAT
tax
delivery
labour
materials
```

but the wording itself creates no GrandRue tax authority.

Therefore:

```text
merchant label "VAT"
    ≠
GrandRue determined VAT liability

merchant-entered tax amount
    ≠
GrandRue validated tax calculation

Invoice
    ≠
statutory tax-invoice compliance
```

Tax determination remains separately governed.

---

# 16. Billing Basis References

An Invoice MAY retain zero or more owner-qualified **Billing Basis References** for provenance.

Examples include:

```text
accepted Quotation
Order
Booking
Appointment
Enquiry
Offering
```

A Billing Basis Reference answers:

> What business context led the merchant to issue this Invoice?

It does not transfer ownership.

It does not automatically prove:

```text
work completed
amount legally enforceable
amount not previously billed
source fully billed
source partially billed
```

Those meanings require their own accepted semantics.

---

# 17. No Universal Invoiced-Balance Ledger

MS-PROT-096 v1.0 SHALL NOT create:

```text
amount invoiced from Order
remaining uninvoiced amount
percentage invoiced
billing allocation ledger
progress-billing ledger
```

across arbitrary source capabilities.

This is deliberate.

The same upstream source may legitimately participate in several merchant billing decisions in future scenarios.

Until explicit allocation semantics exist, GrandRue MUST NOT infer:

```text
source referenced once
    → fully invoiced forever
```

Technical duplicate prevention remains mandatory.

Distinct merchant billing intent remains merchant authority.

---

# 18. Issuance

An Invoice becomes an issued customer billing record only through:

```text
invoicing.issue
```

Successful issuance freezes:

```text
Invoice Items
descriptions
quantities
line amounts
currency
total
recipient snapshot/context
merchant-issued presentation snapshot
Billing Basis References
Invoice Reference
issuedAt
supported payment-deadline term
Invoice Payment Binding
```

These values MUST NOT subsequently be edited in place.

---

# 19. Historical Snapshot Boundary

Issued Invoice representation must remain reconstructible after changes to:

```text
Merchant Profile
customer contact information
Offering
Product
Quotation
Order
current price
merchant configuration
```

Invoicing SHALL retain only the minimum Invoice-specific snapshot information required for:

```text
historical interpretation
customer presentation
audit
business record continuity
```

It SHALL NOT copy an entire Merchant Profile or CustomerContext indiscriminately.

---

# 20. Invoice Reference Allocation

Invoice Reference MUST be allocated no later than successful issuance.

For distinct successfully issued Invoices:

```text
same Merchant Scope
    → distinct Invoice References
```

References MUST NOT be reused after:

```text
withdrawal
billing cancellation
account change
customer change
```

Gaps in generated references are not prohibited by this authority.

No statutory numbering claim is established.

---

# 21. Invoice Payment Binding Modes

Every issued Invoice MUST use exactly one of two initial binding modes:

```text
INVOICE_ESTABLISHES_OBLIGATION

EXISTING_OBLIGATION
```

No third implicit mode exists.

---

# 22. `INVOICE_ESTABLISHES_OBLIGATION`

This mode is used where Invoice issuance itself establishes the payable customer amount in GrandRue.

Canonical:

```text
authorised Invoice issuance
        +
Invoice total
        +
recipient relationship
        ↓

Invoice committed
        +
Payment Obligation committed
```

The Payment Obligation MUST:

```text
reference the Invoice as its
owner-qualified commercial source

use exactly the Invoice total

use exactly the Invoice currency

use a registered immediately-payable
Payment Due Condition
```

Invoice and Payment Obligation MUST commit atomically.

If Payment Obligation establishment cannot commit:

```text
Invoice issuance MUST NOT commit
```

---

# 23. Independent Commercial Permissions

`INVOICE_ESTABLISHES_OBLIGATION` requires both:

```text
ISSUE_CUSTOMER_INVOICE
```

and:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

where their respective commercial access rules apply.

Possession of one does not manufacture the other.

Canonical:

```text
Invoice entitlement
    ≠ Payment entitlement

Payment entitlement
    ≠ Invoice entitlement
```

The standard BUSINESS/GROWTH portfolio may grant both.

---

# 24. `EXISTING_OBLIGATION`

This mode is used where an exact Payment Obligation already exists.

Issuance MUST NOT create another Payment Obligation.

Before issue, Invoicing MUST revalidate:

```text
exact Payment Obligation
same Merchant Scope
recipient compatibility
effective obligation amount
currency
current Payment authority
```

The Invoice total MUST equal:

```text
current effective Payment Obligation amount
```

at issuance.

The currency MUST match exactly.

Existing PaymentApplications MAY already exist.

They do not change the issued Invoice total.

---

# 25. Existing-Obligation Recipient Safety

For `EXISTING_OBLIGATION`, the proposed Invoice recipient MUST satisfy the existing Payment customer's owner-qualified relationship requirements.

GrandRue MUST NOT attach another customer to an obligation merely because:

```text
names match
emails match
merchant selected them manually
```

without accepted relationship authority.

---

# 26. One Current Invoice Per Payment Obligation

At most one **non-withdrawn issued Invoice** may currently bind to one exact Payment Obligation.

Historical withdrawn Invoices MAY reference the same obligation.

Therefore:

```text
existing current Invoice I1
    → Payment Obligation P1

attempt issue I2
    → P1

while I1 not withdrawn
    = reject
```

To replace the customer billing representation:

```text
withdraw I1
        ↓
issue I2
        ↓
I2 → same P1
```

The Payment Obligation remains unchanged.

---

# 27. Invoice Payment Deadline

An Invoice MAY contain one optional **Invoice Payment Deadline**.

It consists of:

```text
local calendar date
+
IANA time-zone identity
```

It means:

> The merchant requests payment no later than the end of that local calendar date.

If absent:

```text
NO EXPLICIT INVOICE PAYMENT DEADLINE
```

The deadline is an Invoicing commercial/presentation term.

It does not replace Payment Due Condition authority.

---

# 28. Payability Versus Deadline

For `INVOICE_ESTABLISHES_OBLIGATION`, the initial Payment Obligation is immediately payable.

Therefore customers MAY pay before the optional Invoice Payment Deadline.

The Payment Deadline does not delay Payment execution.

Canonical:

```text
amount payable now
        ≠
payment deadline passed
```

This avoids inventing a separate early-payment exception.

---

# 29. Deadline-Passed Projection

Where a Payment Deadline exists:

```text
current local date
in stored deadline zone
>
deadline local date
```

means:

```text
payment deadline passed
```

If:

```text
payment deadline passed
+
Invoice-bounded outstanding amount > 0
```

a presentation MAY display:

```text
Overdue
```

as deterministic shorthand.

That label MUST NOT imply:

```text
legal default
statutory penalty
interest
collection authority
```

unless separately governed.

---

# 30. Invoice-Bounded Payment Projection

Invoice does not own `AmountDue`.

For customer presentation, derive:

```text
effective Payment Obligation amount

PaymentApplications

Payment adjustments

Payment due evaluation

Refund evidence
```

through Payment authority.

Invoice MAY derive an **Invoice-Bounded Outstanding Amount**:

```text
invoice remaining face amount
=
max(
    0,
    Invoice total
    -
    amount authoritatively applied
    to the bound Payment Obligation
)

Invoice-bounded outstanding
=
min(
    current Payment outstanding amount,
    invoice remaining face amount
)
```

This is a projection only.

It is not a second Payment Obligation.

---

# 31. Obligation Increase After Invoice Issue

A later Payment Obligation `INCREASE` MUST NOT silently rewrite the issued Invoice total.

If:

```text
Invoice total = £100

later effective obligation = £120
```

the Invoice remains:

```text
£100
```

The Invoice customer surface MUST NOT present the additional £20 as though it were part of the original Invoice.

A merchant requiring a current Invoice representing the increased amount must use an independently valid billing operation, such as withdrawing and replacing the Invoice where the governing source authority permits the increase.

---

# 32. Obligation Reduction After Invoice Issue

A valid Payment Obligation reduction MUST NOT rewrite the Invoice total.

Example:

```text
Invoice total = £100

Payment obligation later reduced to £70
```

Invoice remains historical evidence that £100 was billed.

Current payable/outstanding projection may reflect £70.

This preserves both truths.

---

# 33. Partial Payment

Partial Payment is Payment-owned.

Example:

```text
Invoice total        £100
PaymentApplication    £40
```

Invoice remains £100.

Customer-facing representation MAY show:

```text
Invoice total    £100
Paid              £40
Remaining         £60
```

as a derived Payment projection.

No Invoice mutation occurs.

---

# 34. Full Payment

Full Payment does not mutate or close Invoice authority.

A UI may derive a label such as:

```text
Paid
```

from Payment truth.

There SHALL NOT be a mutable authoritative:

```text
Invoice.status = PAID
```

owned by Invoicing.

---

# 35. Refund

Refund remains Payment-owned historical truth.

A Refund does not:

```text
delete Invoice
rewrite Invoice total
automatically reopen Invoice amount due
cancel Invoice
```

Invoice/customer projections MAY display applicable authorised Refund information.

---

# 36. Lifecycle Facts, Not a Giant Status

Invoicing SHALL NOT use one authoritative lifecycle enum such as:

```text
DRAFT
SENT
VIEWED
OVERDUE
PARTIALLY_PAID
PAID
REFUNDED
CANCELLED
```

Authoritative truth is composed from:

```text
Invoice preparation
+
Invoice Issue
+
Invoice Withdrawal
+
optional Invoice Billing Cancellation
+
Payment-owned facts
+
optional Payment Deadline
```

Presentation status is derived.

---

# 37. Invoice Withdrawal

`invoicing.withdraw` establishes an Invoice Withdrawal.

Withdrawal means:

```text
this issued Invoice
is no longer the current
customer billing representation
```

Withdrawal MUST NOT:

```text
delete Invoice
alter Invoice total
adjust Payment Obligation
refund money
cancel Order
cancel Appointment
cancel Booking
```

The underlying Payment Obligation may continue to exist.

---

# 38. Replacement Invoice

An issued Invoice cannot be edited.

Where a merchant needs a corrected customer billing representation without changing the underlying obligation:

```text
withdraw old Invoice
        ↓
prepare new Invoice
        ↓
issue new Invoice
        ↓
bind to same existing Payment Obligation
```

The new Invoice receives:

```text
new Invoice identity
new Invoice Reference
```

Historical Invoice remains retained.

---

# 39. Invoice Billing Cancellation

An Invoice MAY cancel billing only where:

```text
Invoice Payment Binding mode
=
INVOICE_ESTABLISHES_OBLIGATION
```

This is because that Invoice is the commercial source that created the Payment Obligation.

For:

```text
EXISTING_OBLIGATION
```

Invoicing MUST NOT cancel another source owner's obligation.

That source owner must independently authorise any obligation change.

---

# 40. Billing-Cancellation Effect

Successful Invoice Billing Cancellation establishes:

```text
Invoice Billing Cancellation
```

and, where the effective Payment Obligation amount remains greater than zero:

```text
PaymentObligationAdjustment
    DECREASE
    by the exact amount required
    to reduce effective obligation to zero
```

The Invoice Cancellation fact and required Payment decrease MUST share one atomic local consistency boundary.

If the Invoice was not already withdrawn, billing cancellation also makes it non-current for customer presentation.

---

# 41. Payment Already Applied Before Billing Cancellation

Suppose:

```text
Invoice total           £100
Payment applied         £100
merchant cancels billing
```

Billing cancellation may reduce the effective Payment Obligation to zero.

Historical truth becomes:

```text
Invoice issued          £100
Payment applied         £100
Billing cancelled
Obligation reduced       £0 effective
Excess applied          £100
```

This does **not** automatically create Refund.

Refund remains separately governed.

GrandRue MUST surface the resulting financial resolution requirement rather than silently losing the discrepancy.

---

# 42. Pending or Uncertain Payment Execution

Invoice Billing Cancellation MUST NOT proceed blindly while an unresolved external Payment Execution Request may still charge the customer.

Where Payment reports:

```text
pending provider side effect
or
EXECUTION_UNCERTAIN
```

against the exact bound obligation:

```text
billing cancellation
    → reject / defer authoritative cancellation
```

until Payment reconciliation establishes sufficient truth.

This prevents:

```text
cancel bill locally
while provider may still charge
```

from being treated as safe completion.

---

# 43. Commercial Access Classification

Invoicing fulfils the existing BUSINESS native-Invoicing reservation.

The initially protected purpose SHALL be:

```text
ISSUE_CUSTOMER_INVOICE
```

Protected access contract:

```text
invoicing/invoice-issuance-access@1
```

Standard allocation:

```text
BUSINESS
GROWTH
```

FREE SHALL NOT receive the protected purpose through the standard catalogue.

The following bounded contracts require no independent Invoicing Commercial Entitlement:

```text
invoicing/new-invoice-preparation-access@1

invoicing/existing-invoice-observation-access@1

invoicing/existing-invoice-resolution-access@1
```

The final Commercial Entitlement identity remains Commercial-owned under `MS-PROT-056-V17-DQ-001`.

---

# 44. Preparation Commercial Boundary

`invoicing/new-invoice-preparation-access@1` may:

```text
prepare candidate Invoice
copy authorised source context
calculate candidate line totals
preview Invoice
identify Payment binding candidate
```

It MUST NOT:

```text
issue Invoice
create Payment Obligation
increase Payment Obligation
create provider payment request
```

A draft does not preserve expired entitlement.

Issue revalidates current commercial permission.

---

# 45. Entitlement Loss

Loss of new-Invoice issuance entitlement SHALL NOT:

```text
delete issued Invoices
hide legitimate existing Invoice history
delete Invoice Items
delete Payment Binding
delete payment history
delete withdrawal/cancellation evidence
```

An independently authorised merchant/customer may continue to observe an existing Invoice.

Existing Payment Obligations remain payable/resolvable under Payment residual-access semantics.

The merchant may still:

```text
withdraw existing Invoice
cancel Invoice-created billing
where otherwise valid
```

without a new Invoicing entitlement.

The merchant may not issue a replacement/new Invoice without current protected issuance permission.

---

# 46. Payment After Invoicing Downgrade

If:

```text
Invoice legitimately issued
Payment Obligation legitimately exists
merchant later downgrades
```

the customer MAY continue paying the exact existing obligation under composite MS-PROT-055 residual Payment authority.

This is not free new Invoicing.

It is resolution of an existing monetary obligation.

---

# 47. Financial Operations Boundary

Financial Operations remains a residual owner.

Where an Invoice is bound to a Payment Obligation:

```text
Payment owns customer monetary obligation
```

Therefore Financial Operations MUST NOT additionally create:

```text
Finance-Native Receivable
```

for the same economic exposure merely because an Invoice exists.

Canonical:

```text
Invoice
    customer billing record

Payment Obligation
    customer monetary obligation

Financial Operations
    may consume/reference
    those source-owned facts
```

This directly resolves the ambiguity retained by `MS-PROT-084-DQ-006`.

---

# 48. Finance-Native Receivable Boundary

A Finance-Native Receivable remains valid only where no more specific accepted source owns the receivable meaning.

After MS-PROT-096:

```text
Invoice-bound customer Payment Obligation
    → Payment-owned

not
    → duplicate Finance-Native Receivable
```

MS-PROT-084's residual-owner rule remains unchanged.

---

# 49. Quotation Boundary

An accepted Quotation MAY be retained as Invoice billing provenance.

Canonical:

```text
Accepted Quotation
        ↓
merchant chooses to invoice
        ↓
Invoice
```

Quotation Acceptance does not automatically create the Invoice.

Invoice issuance remains separately authorised.

The Invoice MAY copy relevant accepted commercial information into preparation, but issued Invoice Items become Invoice-owned billing truth.

---

# 50. Ordering Boundary

An Order MAY provide billing provenance.

Invoice issuance MUST NOT:

```text
change Order
fulfil Order
release Order
change ordered quantity
```

Ordering continues to own purchase commitment.

Invoice does not create Order.

---

# 51. Appointment and Booking Boundary

Appointment or Booking MAY provide billing context.

Invoice issuance does not mean:

```text
Appointment occurred
Booking was used
service was completed
```

unless their owners independently establish those facts.

Invoicing MUST NOT infer fulfilment from mere existence of the Appointment/Booking.

---

# 52. Payment Boundary

Payment owns:

```text
Payment Obligation
Payment Obligation Adjustment
Due Evaluation
Payment Execution Request
ProviderPaymentEvidence
PaymentApplication
current outstanding amount
Amount Due
Refund
```

Invoice owns none of them.

Invoice may reference/project Payment facts only through accepted Payment contracts.

---

# 53. No “Mark Invoice Paid” Shortcut

Invoicing SHALL NOT provide an authoritative operation equivalent to:

```text
invoice.markPaid()
```

that bypasses Payment authority.

A UI action labelled:

```text
Record payment
```

must execute an accepted Payment-owned operation/evidence path.

Invoice presentation may reflect the resulting Payment fact afterwards.

---

# 54. Provider and Delivery Boundary

Invoice issuance is local GrandRue business truth.

Email, SMS, PDF delivery or another provider occurs after issue.

Canonical:

```text
Invoice issue commits
        ↓
optional InvoiceIssued event
        ↓
Notification / delivery
```

Delivery failure MUST NOT:

```text
delete Invoice
rollback Invoice
delete Payment Obligation
```

A merchant MAY retry delivery of the same issued Invoice without creating another Invoice.

---

# 55. Document/PDF Boundary

A:

```text
PDF
web page
email
printout
```

is a representation of Invoice truth.

It is not the Invoice.

Changing:

```text
layout
font
delivery channel
PDF renderer
```

does not create a new Invoice.

Changing billed commercial content after issuance requires a new Invoice.

---

# 56. Customer Invoice Surface

A customer-facing Invoice surface MAY display authorised:

```text
Invoice Reference
issue date
merchant billing identity
recipient billing identity
Invoice Items
Invoice total
Payment Deadline
Payment applied
Invoice-bounded outstanding amount
payment action
withdrawal/cancellation information
```

according to current Exposure and customer-access authority.

Internal staff notes and unrelated financial data MUST NOT become customer-visible merely because the Invoice exists.

---

# 57. Merchant Customer-History Projection

Where the Invoice has an exact `Invoice → CustomerContext` relationship, merchant-facing customer history MAY project authorised Invoice information from Invoicing.

Canonical:

```text
CustomerContext
        ↓ merchant history projection

Quotation Q-...
Invoice INV-...
Order ...
Appointment ...
Conversation ...
```

The projection MAY derive useful customer-facing merchant labels such as current Invoice payment summary from the independently authoritative Payment relationship.

The projection MUST NOT:

```text
own Invoice state
own Payment state
copy Invoice into CustomerContext
copy Payment Obligation into CustomerContext
create customer identity from contact-value similarity
```

This rule supports coherent merchant customer records without creating a CRM-style universal customer aggregate.

---

# 58. AI Boundary

AI MAY assist:

```text
draft description
summarise accepted Quotation
suggest Invoice Item wording
extract candidate item data
prepare candidate Invoice
identify likely missing information
```

AI MUST NOT independently:

```text
issue Invoice
create Payment Obligation
increase amount owed
withdraw Invoice
cancel billing
record Payment
record Refund
determine tax
declare statutory compliance
```

Merchant or separately authorised deterministic authority remains required.

---

# 59. Data Protection

Invoice historical snapshots MUST be purpose-bounded.

GrandRue SHALL preserve only recipient/merchant information necessary for:

```text
billing interpretation
customer access
business record continuity
audit
accepted retention purpose
```

Issued Invoice retention/disposition remains governed by MS-PROT-053 and applicable accepted legal/JRA authority.

MS-PROT-096 does not invent a universal statutory retention period.

---

# 60. Operation Contract — `invoicing.prepare`

**Owner:** Invoicing  
**Principal:** authorised merchant actor  
**Commercial access:** no independent entitlement  
**Inputs:** Merchant Scope, recipient, items, optional Billing Basis References, candidate binding mode, optional deadline  
**Reads:** authorised customer/source projections, Money semantics, current Payment context where needed  
**Mutates:** Invoice preparation only  
**Success:** draft preparation updated  
**Business rejection:** unsupported/invalid candidate structure  
**Authorisation rejection:** actor not authorised  
**Provider:** none  
**Events:** none required  

Preparation establishes no customer monetary obligation.

---

# 61. Operation Contract — `invoicing.issue`

**Owner:** Invoicing  
**Principal:** authorised merchant actor  
**Required commercial purpose:** `ISSUE_CUSTOMER_INVOICE`  
**Additional commercial requirement:** `ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION` when binding mode is `INVOICE_ESTABLISHES_OBLIGATION`

Required inputs include:

```text
Merchant Scope
logical command identity
Invoice identity
final Invoice preparation
recipient context
binding mode
```

Before mutation, Invoicing MUST establish:

```text
Semantic Applicability
Actor Authorisation
Invoice commercial permission
Payment commercial permission where new obligation is created
recipient relationship
item validity
one currency
total > 0
Invoice Reference uniqueness
Payment Binding validity
current concurrency/version conditions
```

Where recipient context is a `CustomerContext`, successful issue MUST preserve the exact Invoice → CustomerContext relationship atomically with the Invoice Issue.

---

# 62. `invoicing.issue` — New Obligation Atomicity

For:

```text
INVOICE_ESTABLISHES_OBLIGATION
```

successful execution atomically establishes:

```text
Invoice Issue
+
Invoice Reference
+
Invoice Payment Binding
+
Invoice → CustomerContext relationship where applicable
+
one Payment Obligation
```

Failure to establish any required element means the Invoice is not issued.

External provider calls remain outside the transaction.

---

# 63. `invoicing.issue` — Existing Obligation

For:

```text
EXISTING_OBLIGATION
```

successful execution establishes:

```text
Invoice Issue
+
Invoice Reference
+
Invoice Payment Binding
+
Invoice → CustomerContext relationship where applicable
```

and creates no Payment Obligation.

Before commit:

```text
Invoice total
=
current effective obligation amount
```

and currency must match.

A conflicting current Invoice for the same obligation rejects issuance.

---

# 64. Operation Contract — `invoicing.withdraw`

**Owner:** Invoicing  
**Principal:** authorised merchant actor  
**Commercial access:** existing-Invoice resolution; no independent entitlement  
**Input:** exact issued Invoice  
**Mutation:** exactly one Invoice Withdrawal  
**Payment mutation:** NONE  
**Success:** Invoice stops being current billing representation  
**Business rejection:** already withdrawn or billing-cancelled where no new effect exists  
**Retry:** reconcile to existing Withdrawal  
**Provider:** none required  

---

# 65. Operation Contract — `invoicing.cancel-billing`

**Owner:** Invoicing coordinating Payment  
**Principal:** authorised merchant actor  
**Commercial access:** existing-Invoice resolution; no independent Invoicing entitlement  
**Permitted only when:** Invoice binding mode is `INVOICE_ESTABLISHES_OBLIGATION`

Reads:

```text
Invoice
Invoice Payment Binding
bound Payment Obligation
Payment adjustments
PaymentApplications
pending/uncertain Payment execution
existing Billing Cancellation
```

Mutation:

```text
one Invoice Billing Cancellation

+

Invoice Withdrawal where not already withdrawn

+

Payment DECREASE adjustment where required
to reduce effective obligation to zero
```

Required Invoice/Payment consequences commit atomically.

Refund is not part of this operation.

---

# 66. Retry and Duplicate Semantics

Every mutation operation MUST distinguish:

```text
transport retry
```

from:

```text
new merchant intent
```

For one logical command:

```text
same command
same intent
    → same result
```

A committed issue whose acknowledgement is lost MUST reconcile to the same:

```text
Invoice
Invoice Reference
Payment Binding
Payment Obligation where created
CustomerContext relationship where created
```

It MUST NOT create another Invoice.

---

# 67. Invoice Reference Concurrency

Concurrent issuance within one Merchant Scope MUST NOT allocate the same Invoice Reference to two Invoices.

The implementation MUST provide sufficient authoritative serialization/uniqueness protection.

Technology choice remains implementation scope.

---

# 68. Existing-Obligation Concurrency

Concurrent attempts to issue two current Invoices for one Payment Obligation MUST NOT both commit.

At most one may become the current non-withdrawn Invoice.

---

# 69. Billing Cancellation Versus Payment

The critical race is:

```text
customer starts Payment
        ↕
merchant cancels billing
```

If cancellation commits before Payment execution is durably accepted:

```text
new payment must fail current obligation revalidation
```

If a Payment Execution Request is already accepted or its provider outcome is uncertain:

```text
billing cancellation MUST NOT
pretend no external payment can occur
```

The applicable Payment uncertainty/reconciliation contract governs.

---

# 70. Failure and Rejection Semantics

Invoicing MUST distinguish at least:

```text
SEMANTICALLY_INAPPLICABLE

COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

PAYMENT_PERMISSION_DENIED

ACTOR_NOT_AUTHORISED

RECIPIENT_RELATIONSHIP_UNSATISFIED

INVALID_INVOICE

INVALID_INVOICE_ITEM

CURRENCY_CONFLICT

INVOICE_TOTAL_INVALID

INVOICE_NOT_FOUND_OR_NOT_ACCESSIBLE

INVOICE_ALREADY_ISSUED

INVOICE_ALREADY_WITHDRAWN

INVOICE_ALREADY_BILLING_CANCELLED

INVOICE_REFERENCE_CONFLICT

PAYMENT_OBLIGATION_NOT_FOUND_OR_NOT_ACCESSIBLE

PAYMENT_OBLIGATION_AMOUNT_CONFLICT

PAYMENT_OBLIGATION_CURRENCY_CONFLICT

CURRENT_INVOICE_ALREADY_EXISTS

INVOICE_DOES_NOT_OWN_PAYMENT_OBLIGATION

PAYMENT_EXECUTION_UNRESOLVED

CONFLICT

TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

A failed email MUST NOT be reported as Invoice issuance failure when issue already committed.

---

# 71. Channel Convergence

The same authoritative Invoice operation applies whether the merchant prepared the Invoice from:

```text
mobile app
desktop app
web dashboard
telephone conversation
walk-in interaction
AI-assisted preparation
accepted Quotation
Order context
```

Channel provenance does not create separate Invoice semantics.

---

# 72. Supplier Scenario

A building-material supplier receives an Order or customer request.

The supplier creates:

```text
Cement × 100
Blocks × 500
Delivery
```

as one Invoice.

If no Payment Obligation exists:

```text
INVOICE_ESTABLISHES_OBLIGATION
```

may establish it.

If an exact obligation already exists:

```text
EXISTING_OBLIGATION
```

may represent it.

No supplier-specific Invoice implementation exists.

---

# 73. Tradesperson Scenario

A carpenter's customer accepts a quotation.

The carpenter later chooses:

```text
Create invoice
```

GrandRue may carry the accepted Quotation into Invoice preparation.

The Invoice does not mutate the Quotation.

On issue:

```text
Invoice
+
Payment Obligation
```

may be established.

The same Invoicing semantics used by the supplier apply.

---

# 74. Appointment-Service Scenario

A consultant completes a session.

The merchant creates an Invoice.

Invoicing does not infer session completion merely because the Appointment exists.

The merchant may retain the Appointment as Billing Basis provenance.

The Invoice owns billing.

Appointment owns Appointment truth.

---

# 75. Standalone Invoice Scenario

A merchant performs work arranged outside GrandRue.

The merchant may create an Invoice with:

```text
no Quotation
no Order
no Appointment
```

and merchant-defined Invoice Items.

GrandRue SHALL NOT force creation of fake upstream objects merely to permit invoicing.

This is necessary for phone, walk-in and existing-customer business.

---

# 76. Customer-Record Scenario

A merchant has:

```text
CustomerContext C1
    Sarah Jones
```

The merchant issues:

```text
Quotation Q1
Invoice I1
Order O1
```

where each applicable capability owns its own truth and relates to C1 under its accepted relationship semantics.

Merchant customer history MAY present:

```text
Sarah Jones

Quotation Q1
Invoice I1
Order O1
```

without making `CustomerContext` the mutable owner of those objects.

If a one-off guest receives Invoice I2 and no legitimate durable customer relationship exists:

```text
Invoice I2
    remains exact guest-context bound

CustomerContext
    is NOT manufactured
```

This preserves customer-history usefulness without creating a universal CRM aggregate.

---

# 77. Falsification

The proposal has been challenged against the following cases.

| Challenge | Required outcome |
|---|---|
| Low-software-capacity tradesperson wants to bill £180 | Create item → issue Invoice; no accounting concepts exposed |
| Supplier invoices goods + delivery | Same Invoice semantics; no supplier branch |
| Carpenter invoices accepted Quotation | Quotation is provenance; Invoice separately issued |
| Consultant invoices without Quotation | Valid standalone Invoice |
| Existing CustomerContext receives Invoice | Exact Invoice → CustomerContext relationship preserved; merchant history may project Invoice |
| One-off guest receives Invoice | No fake CustomerContext created |
| CustomerContext later reconciled legitimately | Relationship may be associated under customer authority without rewriting Invoice history |
| No Payment Obligation exists | Invoice issue may atomically establish one |
| Payment Obligation already exists | Invoice binds it; no duplicate obligation |
| Existing obligation £100 but Invoice candidate £120 | Reject |
| Existing obligation GBP but Invoice EUR | Reject |
| Two Invoices race for same existing obligation | At most one current Invoice commits |
| Merchant retries Invoice issue after lost response | Same Invoice/reference/obligation returned |
| Merchant intentionally creates two unrelated £100 Invoices | Separate merchant intent permitted |
| Product price changes after Invoice issue | Invoice remains unchanged |
| Customer address changes | Historical Invoice remains reconstructible |
| Merchant changes Invoice item after issue | Prohibited; issue new Invoice |
| Invoice withdrawn | Payment Obligation remains |
| Withdrawn Invoice is replaced | New Invoice may bind same obligation |
| Invoice-created billing cancelled | Payment obligation reduced to zero through Payment |
| Existing-obligation Invoice attempts billing cancellation | Reject; Invoicing does not own source obligation |
| Invoice paid £40 of £100 | Invoice remains £100; £60 derived balance |
| Invoice fully paid | Paid is derived; no `Invoice.status=PAID` mutation |
| Invoice paid then billing cancelled | historical payment remains; excess applied requires separate resolution |
| Payment provider may still charge | billing cancellation cannot falsely complete |
| Payment refunded | Refund does not rewrite Invoice |
| Merchant downgrades after Invoice issue | Invoice remains observable and payable/resolvable |
| Merchant after downgrade tries new Invoice | protected issuance denied |
| Email provider unavailable | Invoice/Payment Obligation remain committed |
| Merchant calls line “VAT 20%” | wording is merchant content; no tax authority created |
| Merchant receives vendor Invoice | not native customer Invoicing; Financial Evidence/Payable boundary remains |
| Financial Operations sees customer Invoice | does not create duplicate Finance-Native Receivable |
| AI drafts Invoice | candidate only; merchant must issue |
| Customer guesses another Invoice Reference | no customer access |
| Invoice Billing Basis is Order | Invoice does not fulfil/amend Order |
| Invoice Billing Basis is Appointment | Invoice does not prove occurrence |
| Existing Payment obligation later increases | old Invoice does not silently increase |
| Existing Payment obligation later decreases | old Invoice remains historical; derived balance reflects Payment |
| Same Order cited in two separately authorised Invoices | no false “fully invoiced” inference absent future allocation semantics |

Cross-domain generality survives supplier, trade, appointment-based and standalone billing scenarios without industry-specific code semantics.

---

# 78. Alternatives and Trade-Offs

## Alternative A — Use Payment Obligation as the Invoice

Rejected.

Payment Obligation answers:

> What monetary amount must be discharged?

It does not own:

```text
Invoice Reference
Invoice Items
issued billing representation
merchant/customer snapshots
Invoice withdrawal
billing document history
```

Collapsing them would overload Payment.

---

## Alternative B — Use Finance-Native Receivable as the Invoice

Rejected.

Financial Operations is deliberately residual.

Customer Payment Obligations already have a more specific owner.

Using Receivable would create duplicate financial ownership.

---

## Alternative C — Treat Invoice as a PDF

Rejected.

PDF is representation.

Changing renderer/provider must not erase business Invoice truth.

---

## Alternative D — Treat Order as Invoice

Rejected.

Order means:

```text
accepted purchase/order commitment
```

Invoice means:

```text
merchant-issued billing record
```

Either may exist without the other.

---

## Alternative E — Invoice owns Amount Due and Paid status

Rejected.

That duplicates Payment truth and creates reconciliation corruption.

Payment remains authoritative.

---

## Alternative F — Every Invoice always creates another Payment Obligation

Rejected.

Some valid business flows already possess the exact Payment Obligation.

Creating another would double the customer's payable amount.

The two-mode Payment Binding avoids this.

---

## Alternative G — Full accounting/tax invoicing now

Rejected.

It would violate Target-Market Proportionality and reopen statutory accounting/tax domains immediately before GrandRue's operationalisation phase.

---

# 79. Selected Trade-Off

Selected design:

```text
Invoice
    owns billing record

Invoice Payment Binding
    links exactly one Payment Obligation

Payment
    owns monetary obligation and discharge

Financial Operations
    consumes rather than duplicates
```

The principal accepted limitation is:

> **Initial Invoicing does not attempt universal partial/progress billing allocation across source capabilities.**

This sacrifices automatic “remaining uninvoiced” calculations in exchange for:

```text
clear ownership
small operational model
no accounting ledger
no cross-domain billing-allocation engine
faster operational implementation
```

That trade-off should be revisited only when demonstrated merchant requirements justify the additional semantics.

---

# 80. Mandatory Ambiguity Review

The mandatory ambiguity review produces:

1. normative interpretations — **PASS**
2. unique authoritative owner — **PASS**
3. overloaded terminology — **PASS**
4. status/availability terminology — **PASS**
5. determinable predicates — **PASS**
6. references/pronouns — **PASS**
7. cardinalities — **PASS**
8. identity/equality — **PASS**
9. transaction/concurrency guarantees — **PASS**
10. retries/duplicates — **PASS**
11. business versus technical failure — **PASS**
12. configuration/runtime/projection separation — **PASS**
13. applicability/entitlement/authorisation/eligibility/readiness/exposure — **PASS**
14. provider facts versus business truth — **PASS**
15. merchant policy versus platform policy — **PASS**
16. AI inference versus execution — **PASS**
17. examples subordinate to rules — **PASS**
18. negative ownership boundaries — **PASS**
19. cross-authority navigation — **PASS**
20. amendment/closure scope — **PASS**
21. open material questions — **PASS for initial scope**
22. testable invariants — **PASS**
23. implementability without invented business rule — **PASS**
24. Fundamental Vision Conformance — **VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**
25. merchant-visible complexity — **PASS**
26. ordinary-staff training burden — **PASS**

No A–G ambiguity remains within the initial governed Invoicing scope.

---

# 81. Core Invariants

1. Invoice and Payment Obligation are different authoritative concepts.
2. Invoicing owns Invoice truth.
3. Payment owns Payment Obligation and payment truth.
4. Financial Operations MUST NOT duplicate an Invoice-bound customer Payment Obligation as a Finance-Native Receivable.
5. Every Invoice belongs to exactly one Merchant Scope.
6. Every Invoice has exactly one recipient context.
7. Where recipient context is a CustomerContext, Invoice retains an owner-qualified relationship to that exact CustomerContext.
8. CustomerContext does not own or duplicate Invoice truth.
9. Merchant customer-history projection may compose authorised Invoice information through the CustomerContext relationship.
10. One-off guest Invoice does not require manufacture of a CustomerContext.
11. Every issued Invoice has exactly one Invoice Payment Binding.
12. Initial binding mode is exactly `INVOICE_ESTABLISHES_OBLIGATION` or `EXISTING_OBLIGATION`.
13. Every issued Invoice contains at least one Invoice Item.
14. One Invoice uses one currency.
15. Invoice total is greater than zero.
16. Issued Invoice content is immutable.
17. Invoice Reference is immutable, merchant-scoped, unique and never reused.
18. Current source data changes cannot rewrite an issued Invoice.
19. `INVOICE_ESTABLISHES_OBLIGATION` atomically creates the Invoice and exact Payment Obligation.
20. New Invoice-created obligation requires independent Invoice and Payment commercial permission.
21. `EXISTING_OBLIGATION` creates no Payment Obligation.
22. Existing-obligation Invoice total must equal effective Payment Obligation amount at issuance.
23. Existing-obligation currency must match.
24. At most one non-withdrawn Invoice is current for one Payment Obligation.
25. Withdrawal does not change Payment truth.
26. Billing cancellation may change Payment only when Invoice created that obligation.
27. Billing cancellation does not automatically refund payment.
28. Invoice does not own Amount Due.
29. Invoice does not own paid/unpaid state.
30. Partial payment does not mutate Invoice total.
31. Refund does not mutate Invoice total.
32. Payment-obligation increase does not rewrite Invoice.
33. Payment-obligation decrease does not rewrite Invoice.
34. Invoice does not create Order.
35. Invoice does not create Appointment/Booking.
36. Invoice does not fulfil work.
37. Invoice does not create Quotation acceptance.
38. Invoice does not create tax authority.
39. Invoice does not create accounting authority.
40. Inbound supplier Invoice does not become customer Invoicing authority.
41. PDF/email/printout is not authoritative Invoice truth.
42. Delivery-provider failure does not rollback Invoice.
43. AI cannot issue authoritative Invoice.
44. Technical retry cannot multiply Invoice or Payment consequences.
45. Business category does not determine runtime Invoicing behaviour.
46. Existing Invoice and Payment resolution survive commercial downgrade.
47. New Invoice issuance does not survive loss of its protected commercial permission.

---

# 82. Deferred/Future Scope

Explicit future scope:

```text
credit notes
statutory/tax Invoice profiles
VAT/GST calculation
late fees
interest
invoice financing
supplier/vendor Invoice workflow
procurement
purchase orders
statements
receipts
partial/progress billing allocations
retainage/retention
recurring billing
subscriptions
cross-Invoice credit
multi-currency
customer disputes
debt collection
legal debt enforcement
```

Implementation MUST NOT invent these from conventional accounting software behaviour.

---

# 83. Implementation Consequences

This authority establishes semantics only.

Implementation remains separately governed.

Minimum required implementation evidence includes:

```text
Invoice issue immutability

Invoice Reference uniqueness

Invoice issue idempotency

atomic Invoice + Payment Obligation creation

existing-obligation amount/currency matching

one-current-Invoice-per-obligation invariant

recipient isolation

Invoice → CustomerContext relationship preservation

merchant customer-history projection without duplicated authority

guest Invoice without forced CustomerContext

partial Payment projection

Payment adjustment projection

withdrawal without Payment mutation

Invoice-created billing cancellation

billing cancellation / payment-execution race

provider delivery failure isolation

entitlement downgrade behaviour

Financial Operations no-duplicate Receivable

Quotation-to-Invoice provenance

business-type-neutral execution
```

Tests MUST precede production implementation according to `IMPLEMENTATION-RULES.md`.

---

# 84. IMPLEMENTATION-RULES Impact

```text
IMPLEMENTATION-RULES AMENDMENT REQUIRED:
    NONE
```

Current implementation governance already covers:

```text
tests first
dependency-driven slices
atomic invariants
idempotency
failure recovery
design-to-code traceability
manual design escalation
```

MS-PROT-096 introduces semantic work, not a new implementation-governance rule.

---

# 85. Commercial Catalogue Consequence

MS-PROT-096 contributes:

```text
protected:
    invoicing/invoice-issuance-access@1
    +
    ISSUE_CUSTOMER_INVOICE
    →
    BUSINESS + GROWTH

no independent Invoicing entitlement:
    invoicing/new-invoice-preparation-access@1
    invoicing/existing-invoice-observation-access@1
    invoicing/existing-invoice-resolution-access@1
```

Where Invoice issuance creates a new Payment Obligation, the independent protected Payment purpose also remains required:

```text
payment/customer-obligation-establishment-access@1
+
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

MS-PROT-096 mints no final `CommercialEntitlementIdentity`.

`MS-PROT-056-V17-DQ-001` remains OPEN.

---

# 86. MS-PROT-056-V17-DQ-004 Consequence

The native-Invoicing reservation becomes admitted.

Therefore DQ-004 becomes partially resolved for:

```text
Quotation
+
native Invoicing
```

Other reserved portfolios, including future taxation services, remain open.

No reservation outside this exact scope is activated.

---

# 87. MS-PROT-084-DQ-006 Resolution

```text
MS-PROT-084-DQ-006
    Native Invoice Authority
```

is resolved by:

```text
MS-PROT-096 v1.0
```

The resolution means:

```text
Invoicing owns outbound merchant-issued customer Invoice truth

Payment owns customer monetary obligation

Financial Operations remains residual and MUST NOT
duplicate Invoice-bound Payment truth

Document Evidence may process inbound supplier Invoice
documents without acquiring outbound Invoicing authority
```

No other MS-PROT-084 deferred decision changes.

---

# 88. Lexicon Consequence

The Canonical Semantic Lexicon must add at least:

```text
Invoice
Invoice Item
Invoice Reference
Invoice Payment Binding
Invoice Withdrawal
Invoice Billing Cancellation
```

The Lexicon summarises this authority.

It does not own Invoice semantics.

---

# 89. Conformance Conditions

An implementation conforms only when:

```text
[ ] Invoice owner is Invoicing
[ ] Payment Obligation owner remains Payment
[ ] Amount Due remains Payment-derived
[ ] Financial Operations creates no duplicate Receivable
[ ] exactly one Merchant Scope per Invoice
[ ] exactly one recipient per Invoice
[ ] CustomerContext relationship retained where recipient uses CustomerContext
[ ] customer-history projection does not duplicate Invoice/Payment ownership
[ ] one-off guest Invoice does not force CustomerContext creation
[ ] 1..n items per issued Invoice
[ ] one currency per Invoice
[ ] total > 0
[ ] issued content immutable
[ ] Invoice Reference unique/non-reused
[ ] exactly one Payment Binding
[ ] binding mode is one of the two accepted modes
[ ] new-obligation mode commits Invoice + Payment atomically
[ ] Invoice and Payment permissions are independently checked
[ ] existing-obligation mode creates no obligation
[ ] existing obligation amount/currency match is enforced
[ ] customer relationship is revalidated
[ ] one current Invoice per obligation is enforced
[ ] withdrawal never changes Payment
[ ] billing cancellation cannot cancel another owner's obligation
[ ] billing cancellation handles pending payment uncertainty safely
[ ] partial/full payment is projection only
[ ] Refund does not rewrite Invoice
[ ] obligation adjustments do not rewrite Invoice
[ ] customer payment surface cannot exceed Invoice-bounded scope
[ ] tax labels create no tax authority
[ ] inbound vendor Invoice remains outside native Invoicing
[ ] delivery failure cannot rollback Invoice
[ ] AI cannot issue Invoice
[ ] retry cannot duplicate effects
[ ] commercial downgrade preserves existing Invoice resolution
[ ] new issue remains commercially protected
[ ] business type does not branch semantic behaviour
```

---

# 90. Amendment Effect

MS-PROT-096 v1.0:

1. establishes native GrandRue Invoicing;
2. resolves `MS-PROT-084-DQ-006`;
3. establishes Invoice as distinct from Payment Obligation;
4. establishes the two-mode Invoice Payment Binding;
5. establishes atomic Invoice-created Payment Obligation semantics;
6. establishes existing-obligation Invoice semantics without duplicate debt;
7. establishes immutable issue and Invoice Reference semantics;
8. establishes withdrawal and bounded billing-cancellation semantics;
9. establishes exact Invoice → CustomerContext linkage where a durable customer relationship exists;
10. permits merchant customer-history projection without transferring Invoice ownership;
11. establishes Invoice customer access;
12. establishes Invoice commercial-access classification;
13. preserves Financial Operations as a residual owner;
14. preserves tax/accounting/procurement as separate future scope;
15. activates no implementation merely by design acceptance.

Applicable post-approval governance consequences must update:

```text
AUTHORITY-INDEX
DEFERRED-DECISION-REGISTER
CANONICAL-SEMANTIC-LEXICON
```

and perform:

```text
IMPLEMENTATION-RULES impact review
DESIGN-CORPUS-CONFORMANCE
```

before governance completion.

---

# 91. Recommendation

```text
RECOMMENDATION: ACCEPT
```

The recommendation is based on:

- Feature Admission passing Representation, Coordination and Administrative-Compression tests;
- `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`;
- preservation of Payment ownership;
- resolution of the existing Invoice Authority deferral;
- no duplicate Financial Operations Receivable;
- explicit CustomerContext relationship and merchant customer-history projection without authority duplication;
- one model working for suppliers, tradespeople, appointment businesses and standalone billing;
- deliberate avoidance of ERP/accounting expansion;
- explicit correction, retry, concurrency and downgrade behaviour;
- merchant-facing simplicity despite internal correctness requirements.

Explicit manual approval was granted on 19 September 2026. Repository formalisation remains governed by the post-approval revalidation and governance-completion rules in `MS-DESIGN-RULES-001`.
