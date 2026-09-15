# MS-PROT-055 — Money, Commercial Terms, Payment Obligations & Payment Evidence Model

**Document ID:** MS-PROT-055  
**Version:** 1.0  
**Status:** **ACCEPTED after cross-domain falsification and manual approval**  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-023, MS-PROT-025, MS-PROT-027, MS-PROT-035, MS-PROT-042 v1.2, MS-PROT-044 v1.0, MS-PROT-045 v1.1, MS-PROT-048 v1.1, MS-PROT-053 v1.0, MS-PROT-054 v1.0  
**Closes:** DDR-OD-003 — Money, price and commercial-value semantics  
**Purpose:** Define Main Street's reusable monetary value semantics and the authority boundaries among merchant commercial terms, committed commercial amounts, payment obligations, current amount due, provider payment evidence, settlement and refunds without creating one universal commercial transaction abstraction or allowing payment providers to redefine business truth.

---

# 1. Governing decision

Main Street shall distinguish:

```text
MONETARY AMOUNT
      ≠
PRICE / COMMERCIAL TERM
      ≠
COMMITTED COMMERCIAL AMOUNT
      ≠
PAYMENT OBLIGATION
      ≠
AMOUNT CURRENTLY DUE
      ≠
PAYMENT EXECUTION
      ≠
PROVIDER TRANSACTION EVIDENCE
      ≠
SETTLEMENT
      ≠
REFUND
```

The governing rule is:

> **Money is a value; price is a contextual commercial term; a committed amount is historical business truth; an amount due is an obligation-derived current result; and a provider payment result is external execution evidence. None of these shall be treated as interchangeable.**

This document deliberately avoids a universal `CommercialTransaction` object.

---

# 2. Why the distinction is necessary

A single merchant may simultaneously have:

```text
Offering.price                  £30
Listing.askingPrice             £350,000
Booking committed amount        £300
Deposit due now                 £60
Remaining balance               £240
Provider transaction            £60 captured
Provider fee                     £1.80
Merchant settlement             £58.20
Later refund                    £60
```

These values may use the same monetary primitive while owning materially different meaning.

If Main Street collapses them into one generic `price`, `amount` or `transaction` abstraction, it loses:

```text
semantic ownership
historical provenance
payment-policy meaning
provider independence
reconciliation correctness
refund history
customer-obligation truth
```

The reusable element is the monetary value, not one universal commercial owner.

---

# 3. MonetaryAmount is the reusable monetary DataConcept

MS-PROT-045 permits reusable DataConcepts while preserving field/capability ownership.

Main Street therefore recognises the conceptual DataConcept:

```text
MonetaryAmount
{
    currencyIdentity
    minorUnitAmount
}
```

For example:

```text
1250 GBP
```

may project to:

```text
£12.50
```

for an appropriate audience/locale.

The exact Java, SQL and transport representation remains implementation-deferred.

---

# 4. MonetaryAmount invariants

A `MonetaryAmount` shall be:

```text
exact
currency-qualified
non-floating-point
immutable as a value
non-negative in magnitude
```

Main Street shall not use binary floating-point semantics for authoritative monetary values.

The semantic meaning of a monetary movement shall not be encoded by a negative number.

Rejected:

```text
-£20
```

with the reader expected to infer whether this means:

```text
refund
discount
credit
correction
charge reversal
```

Accepted:

```text
MonetaryAmount = £20
semantic context = REFUND
```

or another explicitly owned semantic role.

Zero may be representable as a monetary magnitude. Individual fields/operations decide whether zero is valid for their context.

---

# 5. Currency identity is part of Money

Rejected:

```text
price = 25.00
```

because the value does not identify whether it means:

```text
GBP
EUR
USD
other supported currency
```

Accepted conceptually:

```text
price = 2500 GBP
```

Therefore:

```text
MonetaryAmount(1000 GBP)
    ≠
MonetaryAmount(1000 EUR)
```

A merchant-wide currency may later provide a convenient default or validation constraint, but it does not remove currency identity from the monetary value itself.

---

# 6. No implicit currency conversion

Main Street shall not silently combine or convert different currencies.

Rejected:

```text
100 GBP + 50 EUR
```

unless an explicitly registered conversion semantic exists.

Likewise a provider settlement currency must not silently rewrite the currency of the original customer obligation.

If Main Street later supports currency conversion, the conversion must preserve at least conceptually:

```text
source amount
source currency
target amount
target currency
rate / conversion provenance
effective time
authority
```

Actual foreign-exchange semantics are FUTURE_SCOPE.

---

# 7. Money is not Price

A monetary value such as:

```text
£75
```

is not inherently:

```text
Offering price
asking rent
asking sale price
deposit
amount due
refund
tax
discount
provider fee
balance
settlement
```

Meaning is supplied by the owning field, operation or business fact.

Therefore the same `MONETARY_AMOUNT` DataConcept may validly appear in:

```text
Offering.price
Listing.askingPrice
Listing.askingRent
PaymentObligation.amount
Refund.amount
ProviderPaymentEvidence.amount
```

without transferring ownership between those domains.

---

# 8. Commercial terms remain owned by the proposition that establishes them

Main Street rejects a universal authoritative:

```text
Price
    owns every commercial value
```

Instead:

```text
Offering
    owns applicable Offering commercial terms

Listing
    owns applicable Listing asking/market terms

future Quote
    owns quoted commercial terms

Order / Booking / Appointment or another commitment owner
    owns or references the commercial commitment it establishes

Payment
    owns payment obligations and payment evidence semantics
```

A reusable Money value does not create generic cross-capability mutation authority.

---

# 9. CommercialTerm is a semantic role/family, not a universal Operational Object

This document uses `CommercialTerm` to mean:

> **A capability-owned proposition describing a monetary condition under which a merchant makes something commercially available or advertises it.**

It is not a mandatory platform-wide Operational Object.

Examples:

```text
Offering fixed price
Offering rate
Offering "from" amount
Listing asking price
Listing asking rent
```

The owning capability determines the exact field identity, applicability, lifecycle and mutation operation.

---

# 10. Minimum Offering pricing forms

Main Street shall support a bounded initial vocabulary for the commercial form of an Offering:

```text
FIXED
RATE
FROM
QUOTE_REQUIRED
NO_CHARGE
REGISTERED_CALCULATION
```

This vocabulary constrains meaning; it does not require one database enum or Java type.

The owning Offering schema determines which forms are valid for a particular Offering type and which additional bounded fields are required.

---

# 11. FIXED

`FIXED` means a specific `MonetaryAmount` applies to the complete defined Offering unit under the applicable commercial terms.

Example:

```text
30-minute consultation
FIXED
75 GBP
```

A `FIXED` term may contribute to a later committed commercial amount when the applicable operation revalidates and establishes the customer's commitment.

It does not itself mean that payment is immediately due.

---

# 12. RATE

`RATE` means a `MonetaryAmount` applies per registered semantic basis.

Conceptually:

```text
100 GBP
per registered basis
```

Examples may include a registered basis such as:

```text
per night
per hour
per item
```

but the basis must be owned by accepted Main Street semantics.

A merchant may not create executable pricing meaning merely by entering arbitrary text such as:

```text
"per whatever my formula decides"
```

The rate basis and calculation contract must be registered and deterministic where it contributes to an authoritative commitment.

---

# 13. FROM

`FROM` is an indicative customer-facing commercial term.

Example:

```text
From £150
```

It means:

> a supported transaction may have a final amount at or above the displayed starting amount according to later accepted commercial semantics.

It does **not** mean:

```text
PaymentObligation = £150
```

and it does not by itself establish the final committed amount.

Hard rule:

> **An indicative `FROM` term shall not directly create an amount due.**

---

# 14. QUOTE_REQUIRED

`QUOTE_REQUIRED` means the Offering is commercial but the Offering itself does not currently contain an executable final payable amount.

Example:

```text
Roof repair
QUOTE_REQUIRED
```

Rejected representation:

```text
price = £0
```

because:

```text
unknown/not-yet-established price
    ≠
free service
```

A later authoritative quoted/agreed amount requires separately supported commercial semantics.

Detailed Quote/Estimate lifecycle, expiry, acceptance, revision and legal distinctions remain FUTURE_SCOPE until evidence requires them.

Hard rule:

> **`QUOTE_REQUIRED` shall not directly create an amount due.**

---

# 15. NO_CHARGE

`NO_CHARGE` explicitly means that the defined Offering has no commercial charge under the applicable term.

This is distinct from:

```text
missing price
unknown price
QUOTE_REQUIRED
pricing not configured
```

No payment obligation shall be manufactured merely because an Offering participates in a customer interaction.

---

# 16. REGISTERED_CALCULATION

`REGISTERED_CALCULATION` permits a later authoritative commercial amount to be calculated by a Main Street-registered deterministic pricing contract.

Possible future inputs may include supported semantics such as:

```text
quantity
number of nights
duration
registered tariff
registered bounded adjustment
```

Rejected:

```text
merchant-authored executable formula
merchant JavaScript
arbitrary expression language
AI-selected final price without accepted semantics
provider-owned hidden formula becoming Main Street truth
```

The calculation contract must already exist in accepted/registered Main Street semantics before a merchant may use it.

---

# 17. Asking price is not automatically executable price

A Listing may expose:

```text
asking price = £350,000
```

or:

```text
asking rent = £1,200 per month
```

without creating:

```text
PaymentObligation £350,000
```

or:

```text
monthly rent collection obligation
```

A Listing amount is a proposition/market term owned by Listing semantics.

A customer-visible monetary value shall not automatically become a payable obligation.

---

# 18. Current commercial term and committed commercial amount are different authorities

Suppose:

```text
Monday
Offering price = £25
```

and a customer establishes a commercial commitment under that term.

On Tuesday the merchant changes:

```text
Offering price
£25 → £30
```

The existing commitment shall not silently become £30.

Therefore:

```text
CURRENT COMMERCIAL TERM
        ≠
COMMITTED COMMERCIAL TERMS
```

Hard invariant:

> **Later mutation of a current Offering/Listing commercial term shall not retroactively reinterpret an already-established business commitment.**

This aligns with MS-PROT-054's prohibition on silent reinterpretation of historical commitments.

---

# 19. Commercial commitment boundary

Before an authoritative customer commitment is established, Main Street shall revalidate the applicable commercial terms together with the other capability-owned requirements for that operation.

Canonical pattern:

```text
current applicable commercial terms
        +
request context
        +
applicable registered calculation
        ↓
authoritative revalidation
        ↓
commercial commitment established
        ↓
immutable committed amount/terms
and provenance retained
```

This document does not require one universal `CommittedCommercialTerms` Java object.

The invariant is that the committed commercial truth and enough source provenance survive later changes to current terms.

---

# 20. Pre-commit display does not imply price lock

A customer may view:

```text
Haircut £25
```

while the merchant later changes the current term to £30 before commitment.

Unless Main Street has explicitly established a registered price-hold semantic:

```text
displayed price
        ≠
committed price
```

The committing operation shall revalidate the current applicable terms.

If the terms changed materially, the customer must not be silently committed under an unseen different amount.

Price-hold semantics remain FUTURE_SCOPE until an accepted capability requires them.

---

# 21. Commercial commitment is not Payment obligation

A customer may commit to a commercial amount without all of that amount being immediately payable.

Example:

```text
Service committed amount = £100
```

with policy:

```text
£20 deposit now
£80 balance later
```

Therefore:

```text
COMMERCIAL COMMITMENT
        ↓ may establish
0..n PAYMENT OBLIGATIONS
```

Rejected:

```text
price = amount due now
```

as a universal rule.

---

# 22. PaymentObligation

A `PaymentObligation` represents:

> **A Main Street business fact that a defined monetary amount is required to be discharged in relation to an identified merchant business activity under accepted payment/commercial semantics.**

Conceptually:

```text
PaymentObligation
{
    obligationIdentity
    merchantScope
    commercialSubjectReference
    obligationAmount
    sourceCommercialCommitment
    dueCondition
    provenance
}
```

This shape is conceptual and does not mandate one Java aggregate.

A durable obligation must retain sufficient identity and provenance for reconciliation and historical interpretation.

PaymentObligation is Main Street business truth.

It is not a Stripe, PayPal, Klarna or other provider transaction object.

---

# 23. Deposit is an obligation arrangement, not a price

For:

```text
Service price / committed amount = £100
Deposit due now                = £20
Balance later                  = £80
```

Main Street shall preserve:

```text
Committed commercial amount
    £100

Payment obligation A
    £20
    due according to registered policy

Payment obligation B
    £80
    due according to registered policy
```

The deposit does not redefine the service price to £20.

Whether a deposit is refundable, forfeitable, transferable or affected by cancellation belongs to Booking/Appointment/payment-policy semantics and is outside this document's closure boundary.

---

# 24. AmountDue is contextual derived truth

Main Street shall not treat one mutable scalar named `amountDue` as the universal source of truth.

Conceptually:

```text
applicable PaymentObligations
        -
accepted PaymentApplications
        ±
accepted obligation adjustments
        ↓
Current Amount Due
```

`AmountDue` therefore answers:

> **What monetary amount remains payable in this context under current authoritative obligations and accepted payment facts?**

It is a contextual derived result/projection, not a replacement for the obligations and evidence from which it is calculated.

---

# 25. Payment execution follows obligation

The provider-neutral flow is:

```text
Commercial commitment
        ↓
Payment obligation
        ↓
payment-execution request
        ↓
selected fulfilment provider
        ↓
provider transaction
        ↓
authenticated provider evidence
        ↓
Main Street interpretation / reconciliation
        ↓
PaymentApplication / payment fact
        ↓
AmountDue recalculated
```

A payment provider executes collection or other payment-side effects.

It does not create the merchant's Offering price or the customer's underlying commercial commitment merely by reporting a transaction amount.

---

# 26. ProviderPaymentEvidence

Provider payment evidence represents the authenticated/correlated external facts returned by a payment fulfiller.

It may include, where justified:

```text
provider identity
provider transaction reference
related Main Street correlation
amount
currency
provider payment method category
provider transaction status/evidence
timestamp
receipt/evidence reference
```

Sensitive payment credentials shall not be stored merely because provider evidence exists.

MS-PROT-053 remains authoritative for data minimisation, protection and retention.

---

# 27. Provider evidence does not directly rewrite commercial truth

Suppose Main Street owns:

```text
PaymentObligation = £100 GBP
```

and provider evidence reports:

```text
captured = £90 GBP
```

Rejected:

```text
PaymentObligation becomes £90
```

Correct interpretation is a registered payment consequence such as:

```text
partial payment
mismatch
reconciliation required
```

according to accepted Payment semantics.

Likewise provider evidence for £110 must not silently increase the underlying customer obligation to £110.

Hard rule:

> **Provider transaction amount is evidence of provider execution, not authority to redefine the source commercial commitment or payment obligation.**

---

# 28. Provider state is not Main Street payment semantic state

Provider vocabularies such as:

```text
CAPTURED
COMPLETED
SUCCESS
PENDING
FAILED
REFUNDED
```

are provider-owned evidence/state.

Under MS-PROT-048 they must pass through a registered provider interpretation boundary before Main Street derives capability-owned consequences.

Rejected:

```text
provider.status string
        ↓ direct copy
MainStreetPayment.status
```

Accepted:

```text
provider evidence
        ↓
registered provider interpretation
        ↓
Main Street payment fact / decision
        ↓
capability-owned consequence
```

---

# 29. Partial payment and many-to-one execution must be structurally possible

Main Street shall not assume:

```text
one PaymentObligation
    =
one provider transaction
```

Valid cases include:

```text
£100 obligation
    ↓
£20 deposit payment
+
£80 balance payment
```

or multiple attempts before successful discharge.

Conceptually Main Street may require a relationship/fact such as:

```text
PaymentApplication
{
    obligationReference
    paymentEvidenceReference
    appliedAmount
}
```

The exact representation is implementation/domain-detail work.

The invariant is:

> **Provider transactions and PaymentObligations are distinct and may not have one-to-one cardinality.**

---

# 30. Payment application cannot exceed semantic authority silently

A provider may report an amount greater than the currently expected obligation.

Main Street shall not silently reinterpret the obligation to make the arithmetic fit.

Possible future accepted outcomes may include:

```text
overpayment credit
refund
reconciliation required
merchant action
```

but this document does not choose a universal policy.

Until such semantics exist, the discrepancy must remain explicit rather than being normalised away.

---

# 31. Provider settlement is not customer payment

Suppose:

```text
Customer obligation   £100
Customer payment      £100
Provider fee            £3
Merchant settlement    £97
```

Main Street must preserve those as different facts.

Rejected:

```text
customer paid £97
```

merely because the merchant received a £97 settlement.

Hard rule:

> **A provider fee or settlement deduction does not modify the customer's commercial obligation unless an accepted merchant commercial policy explicitly makes that charge a customer obligation.**

---

# 32. Settlement currency does not rewrite transaction currency

A provider may settle a merchant in a currency different from the customer's payment currency.

This does not rewrite:

```text
original commercial term
committed commercial amount
PaymentObligation currency
customer payment currency
```

Any conversion/settlement evidence must remain explicitly identified as provider-side financial evidence.

Main Street shall not infer an exchange rate by comparing final settlement with the customer obligation unless an accepted reconciliation/FX contract explicitly permits it.

---

# 33. Refund is a subsequent monetary/payment fact

Suppose:

```text
Committed amount = £100
Paid             = £100
Later refunded   = £100
```

Historical truth remains:

```text
original committed amount = £100
original payment          = £100
refund                    = £100
```

Rejected:

```text
original price = £0
```

A refund does not rewrite the historical price or payment.

Likewise:

```text
REFUND
    ≠
CANCELLATION
```

A payment refund does not by itself establish whether a Booking, Appointment, Order, allocation or other business commitment is cancelled or otherwise changed.

Those consequences belong to the owning capability/payment-policy contract and remain downstream, particularly DDR-OD-006 where Booking/Appointment policy is concerned.

---

# 34. Adjustments require explicit semantic provenance

Main Street does not yet define a universal tax, discount, coupon, gratuity or fee engine.

However the following invariant is accepted now:

> **Any adjustment that contributes to an authoritative committed amount must have explicit registered semantic provenance; Main Street shall not hide unexplained arithmetic inside a final total.**

Conceptually:

```text
base commercial amount
        +
registered charges
        -
registered discounts / credits
        +
applicable tax amounts
        ↓
committed amount
```

The exact adjustment vocabulary and applicability rules remain owned by future accepted capability/domain contracts.

Main Street shall not universally assume that displayed prices are tax-inclusive or tax-exclusive.

---

# 35. Rounding must be explicit where required

Simple integer-quantity arithmetic may preserve the currency quantum exactly.

Example:

```text
£2.20 × 3 = £6.60
```

Other calculations may produce values below the supported currency quantum, including:

```text
percentage adjustments
fractional quantities
tax allocation
currency conversion
```

Therefore:

> **Whenever an accepted commercial calculation can produce a value below the supported currency quantum, the owning pricing/tax/adjustment contract shall define the rounding rule.**

Rejected:

```text
whatever the programming-language default does
```

or:

```text
provider rounding silently becomes Main Street semantics
```

The actual rounding-policy catalogue remains downstream until required by an accepted calculation.

---

# 36. Public monetary formatting is projection

The semantic amount:

```text
1250 GBP
```

may be projected as:

```text
£12.50
GBP 12.50
12.50 GBP
```

according to audience/locale/presentation policy.

Therefore:

```text
currency identity
    → semantic value

currency symbol / display formatting
    → projection
```

Where an authoritative structured monetary value exists, Main Street shall not recover business truth by reparsing a formatted display string.

---

# 37. Commercial term mutation is capability-owned

Changing an Offering price uses an Offering-owned operation/field mutation.

Conceptually:

```text
ChangeOfferingPrice
        ↓
MUTATE_DATA
        ↓
Offering.price
```

Changing a Listing asking price uses Listing-owned semantics:

```text
ChangeListingAskingPrice
        ↓
MUTATE_DATA
        ↓
Listing.askingPrice
```

Rejected:

```text
SetPrice(any object)
```

as a universal mutation operation.

The common Money DataConcept does not grant cross-capability write authority.

---

# 38. Commercial commitment provenance

An established commercial commitment must retain enough immutable evidence to answer:

> **Why was this customer expected to pay this amount?**

Depending on the owning capability this may include:

```text
Offering / Listing / commercial subject identity
commercial-term field/revision/value
quantity or registered basis
registered calculation identity/version
accepted adjustments
merchant configuration revision
semantic registry release affinity
time of commitment
```

The exact storage shape is implementation-deferred.

Later mutation of current merchant terms shall not destroy historical interpretability.

---

# 39. Relationship to semantic/configuration migration

MS-PROT-054 remains authoritative for semantic/schema/configuration migration.

An already-established commercial commitment shall not silently adopt:

```text
new price
new pricing basis
new default
new calculation meaning
new currency
```

merely because the merchant configuration or Semantic Registry Release changes later.

Existing-commitment migration remains FUTURE_SCOPE unless a separately accepted design explicitly permits and governs it.

---

# 40. Relationship to Booking and Appointment

MS-PROT-042 remains authoritative for Booking and Appointment commitment semantics.

This document establishes only the shared monetary boundary:

```text
Booking / Appointment commercial commitment
        may establish
PaymentObligation
```

according to registered policy.

It does not decide:

```text
minimum payment-before-confirmation policy
refundability of deposits
forfeiture
cancellation fee policy
rescheduling price difference policy
payment failure consequences
payment/release sequencing
```

Those belong to DDR-OD-006 or later capability-specific payment policy.

---

# 41. Relationship to Product / Inventory

This document establishes reusable Money and commercial-term semantics but does not decide:

```text
Product identity
Offering versus Product ownership
variant pricing
inventory relationship
bundle semantics
SKU semantics
stock valuation
```

Those remain DDR-OD-009.

A future Product/Inventory specification may reuse:

```text
MonetaryAmount
FIXED / RATE / REGISTERED_CALCULATION
committed commercial amount
PaymentObligation
```

without redefining them.

---

# 42. Relationship to payment providers

Under MS-PROT-048:

```text
Payment capability/business semantics
        ↓
payment-execution fulfilment role
        ↓
supported provider
```

Provider selection changes execution plumbing, not commercial meaning.

A merchant may switch provider for future payment execution without changing:

```text
Offering price semantics
committed commercial amount
existing PaymentObligations
```

Historical provider provenance shall remain sufficient for old payment evidence.

---

# 43. Relationship to API contracts

MS-PROT-035 already requires monetary API contracts to carry conceptually:

```text
amount
currency
```

and rejects floating-point monetary semantics.

This document supplies the domain meaning beneath that transport rule.

API DTOs must preserve the distinction among:

```text
current commercial term
committed amount
amount due
provider payment evidence
settlement/refund
```

where those concepts are exposed.

A transport field named simply:

```text
amount
```

shall not be used where its semantic role would be ambiguous.

---

# 44. Relationship to data protection

MS-PROT-053 remains authoritative for data protection, minimisation, retention and durable evidence.

Main Street may retain payment evidence necessary for legitimate business/audit/reconciliation purposes while still minimising provider payloads and sensitive credentials.

At minimum:

```text
provider identity
transaction correlation
amount/currency
relevant result evidence
timestamp
business relationship
```

may be justified where required by accepted purpose.

This does not authorise storage of unnecessary:

```text
full card number
CVV
provider secrets
raw provider payload forever
unrelated payer information
```

---

# 45. This model is not an accounting ledger

MS-PROT-055 does not establish:

```text
double-entry accounting
general ledger
bank reconciliation
invoice accounting
revenue recognition
VAT/tax-return preparation
profit-and-loss accounting
provider payout accounting
chargeback accounting
merchant bookkeeping
```

Those systems may later consume the accepted Money, commitment and payment-evidence facts.

They are not prerequisites for Main Street's basic commercial operation.

---

# 46. Quote semantics remain bounded future scope

`QUOTE_REQUIRED` permits Main Street to represent merchants whose price cannot be established at initial Offering publication.

Example:

```text
Boiler repair
QUOTE_REQUIRED
```

A later agreed amount such as:

```text
£1,250
```

must enter authority through a separately accepted commercial proposal/commitment semantic.

This document intentionally does not invent:

```text
Quote lifecycle
Estimate lifecycle
quote expiry
quote acceptance
quote revision
quote negotiation
legal quote-versus-estimate interpretation
```

Reopen when a concrete capability requires them.

---

# 47. Tax, discount, fee and credit engines remain downstream

This document accepts only the cross-cutting provenance and rounding constraints.

It does not define a universal:

```text
TaxEngine
DiscountEngine
CouponEngine
PromotionEngine
TipEngine
ServiceChargeEngine
CreditLedger
```

A future semantic may introduce one of these only with evidence that the abstraction belongs across affected merchant domains.

---

# 48. Foreign exchange remains downstream

Main Street accepts:

```text
currency-qualified Money
no implicit FX
```

but does not yet accept:

```text
merchant FX pricing
customer multi-currency checkout
provider settlement FX
exchange-rate authority
currency-conversion fee semantics
FX rounding
```

These remain FUTURE_SCOPE.

---

# 49. Falsification — information publisher

Merchant uses:

```text
Publication
Enquiry
```

with no commercial interaction.

No Money, commercial term or PaymentObligation is required.

**PASS**

The model does not force commerce into informational merchants.

---

# 50. Falsification — free consultant

Offering:

```text
Initial consultation
NO_CHARGE
```

Main Street creates no payment obligation merely because an Appointment may exist.

**PASS**

---

# 51. Falsification — fixed-price consultant

Offering:

```text
60-minute consultation
FIXED £75
```

Customer establishes commitment at £75.

Merchant later changes current Offering price to £90.

Existing commitment remains £75 with source provenance.

**PASS**

---

# 52. Falsification — tradesperson

Offering:

```text
Roof repair
QUOTE_REQUIRED
```

No fake £0 price is introduced and no premature PaymentObligation is created.

**PASS**

---

# 53. Falsification — salon deposit

```text
Service committed amount = £100
Deposit obligation       = £20
Balance obligation       = £80
```

The £20 deposit does not redefine the service price.

Deposit refund/cancellation consequences remain capability policy.

**PASS**

---

# 54. Falsification — grocery quantity

```text
Milk
FIXED £2.20 per registered product unit
quantity = 3
```

A registered deterministic quantity calculation may establish:

```text
£6.60 committed amount
```

without inventing merchant-authored executable formulas.

Inventory remains a separate DDR-OD-009 concern.

**PASS**

---

# 55. Falsification — motel rate

Offering:

```text
Room category
RATE £100 per registered night basis
```

A future accepted Booking pricing calculation may establish:

```text
3 nights → £300
```

without requiring business-category branches or arbitrary merchant formulas.

**PASS**

---

# 56. Falsification — realtor

Listing:

```text
asking price = £350,000
```

The amount remains a Listing-owned proposition.

No PaymentObligation arises merely because the customer views or enquires about the Listing.

**PASS**

---

# 57. Falsification — provider fee

```text
Customer obligation = £100
Provider captures    = £100
Provider fee         = £3
Merchant settlement = £97
```

The customer obligation is fully satisfied by the £100 customer payment.

The provider fee and settlement remain separate provider-side financial facts.

**PASS**

---

# 58. Falsification — partial provider payment

```text
PaymentObligation = £100
Provider evidence = £40 paid
```

Main Street does not mutate the source obligation to £40.

The evidence may satisfy £40 of the obligation through accepted PaymentApplication/reconciliation semantics.

Remaining amount due is derived from authoritative facts.

**PASS**

---

# 59. Falsification — overpayment mismatch

```text
PaymentObligation = £100
Provider evidence = £110
```

Main Street does not silently increase the obligation to £110.

The discrepancy remains explicit until an accepted overpayment/refund/credit semantic handles it.

**PASS**

---

# 60. Falsification — refund

```text
Committed = £100
Paid      = £100
Refunded  = £100
```

The original commitment and payment remain historical truth.

Refund is a later monetary/payment fact.

Booking/Order cancellation is not inferred merely from the refund.

**PASS**

---

# 61. Falsification — hybrid merchant

Salon merchant simultaneously has:

```text
Haircut Offering      £30
Shampoo Offering      £12
Appointment commitment
Product Order
one or more payment obligations
```

The same monetary primitive supports all relevant values without creating:

```text
HybridMoney
HybridTransaction
category-specific runtime
```

**PASS**

---

# 62. Falsification — changed pre-commit price

Customer sees:

```text
Offering £25
```

Before authoritative commitment the current term becomes £30.

Without a registered price hold, the system revalidates the term and shall not silently charge £30 under the customer's earlier £25 understanding.

**PASS**

---

# 63. Rejected models

The following are rejected:

1. Floating-point authoritative monetary values.
2. Monetary values without currency identity.
3. Implicit cross-currency arithmetic.
4. Implicit foreign-exchange conversion.
5. Negative Money values used to encode refund/discount/credit meaning.
6. One universal `Price` object owning all commercial values.
7. One universal `CommercialTransaction` abstraction owning price, commitment, payment, refund and settlement.
8. Any public monetary value automatically becoming amount due.
9. `FROM` automatically creating PaymentObligation.
10. `QUOTE_REQUIRED` represented as zero price.
11. Merchant-authored executable pricing formulas.
12. AI deciding authoritative price outside registered semantics.
13. Provider formula/state redefining Main Street commercial semantics.
14. Current Offering price mutation rewriting historical commitments.
15. Displayed price automatically implying price hold.
16. Price universally equal to amount due now.
17. Deposit redefining the Offering price.
18. PaymentObligation equated with provider transaction.
19. One PaymentObligation universally requiring exactly one provider transaction.
20. Provider evidence directly rewriting a PaymentObligation amount.
21. Provider state string copied directly into Main Street business state.
22. Merchant settlement amount treated as customer payment amount.
23. Provider fee silently changing customer obligation.
24. Refund rewriting original price/payment history.
25. Refund automatically meaning Booking/Appointment/Order cancellation.
26. Hidden unexplained arithmetic inside committed totals.
27. Implicit/default programming-language rounding as semantic policy.
28. Currency display formatting as authoritative Money representation.
29. Generic cross-capability `SetPrice(anything)` mutation authority.
30. Treating this specification as an accounting-ledger design.

---

# 64. Accepted invariants

1. Money, commercial term, committed amount, PaymentObligation, AmountDue, payment evidence, settlement and refund are distinct concepts.
2. `MonetaryAmount` is an exact currency-qualified reusable DataConcept.
3. Authoritative Money does not use floating-point semantics.
4. Monetary magnitude is non-negative; semantic direction/meaning belongs to context.
5. Currency identity is intrinsic to Money.
6. Cross-currency arithmetic/conversion is never implicit.
7. Money does not own the meaning of price, deposit, refund, fee or balance.
8. Commercial terms remain owned by the capability/proposition that defines them.
9. `CommercialTerm` is not a mandatory universal Operational Object.
10. Initial Offering pricing forms are `FIXED`, `RATE`, `FROM`, `QUOTE_REQUIRED`, `NO_CHARGE`, `REGISTERED_CALCULATION`.
11. `FROM` is indicative and cannot directly establish amount due.
12. `QUOTE_REQUIRED` cannot directly establish amount due and is not represented as zero price.
13. `NO_CHARGE` is distinct from missing/unknown price.
14. `REGISTERED_CALCULATION` requires a Main Street-registered deterministic contract.
15. Merchant-authored executable pricing is prohibited.
16. Listing asking amounts do not automatically create payable obligations.
17. Current commercial terms and committed commercial terms are distinct authorities.
18. Later current-price mutation cannot rewrite an existing commitment.
19. Commercial terms are revalidated at the authoritative commitment boundary.
20. Pre-commit display does not establish a price hold unless accepted semantics explicitly do so.
21. Commercial commitment and PaymentObligation are distinct.
22. One commitment may establish zero, one or multiple PaymentObligations according to accepted semantics.
23. Deposit is a payment-obligation arrangement, not the service/product price itself.
24. PaymentObligation is Main Street business truth and provider-independent.
25. AmountDue is a contextual derived result from obligations and accepted payment/adjustment facts.
26. Payment execution occurs after/against an established payment obligation where payment is required.
27. ProviderPaymentEvidence is external execution evidence, not source commercial authority.
28. Provider evidence cannot silently rewrite commercial commitment or obligation amount.
29. Provider states require registered interpretation before Main Street business consequences.
30. PaymentObligation and provider transaction cardinality is not universally one-to-one.
31. Partial payment must be structurally supportable.
32. Payment mismatches/overpayments remain explicit until accepted semantics resolve them.
33. Provider settlement and customer payment are distinct.
34. Provider fees do not change customer obligation unless explicit accepted policy makes them customer charges.
35. Refund is a subsequent monetary/payment fact and does not rewrite historical commitment/payment.
36. Refund is not automatically cancellation.
37. Authoritative adjustments require explicit semantic provenance.
38. Rounding rules are explicit whenever an accepted calculation can exceed currency quantum precision.
39. Monetary display formatting is projection, not authority.
40. Commercial-term mutation remains capability-owned and field-targeted.
41. Historical commercial commitments retain sufficient source provenance.
42. Semantic/configuration migration cannot silently reinterpret established commercial commitments.
43. Booking/Appointment payment-policy consequences remain owned by DDR-OD-006/later capability policy.
44. Product/Offering/Inventory/variant semantics remain owned by DDR-OD-009.
45. Payment provider selection changes fulfilment plumbing, not commercial meaning.
46. MS-PROT-053 governs retention/minimisation of payment evidence.
47. MS-PROT-055 does not establish a general accounting ledger.

---

# 65. Deferred decisions

The following remain deliberately outside this specification:

```text
exact Java Money type
exact database Money representation
exact JSON/API serialisation syntax
currency catalogue implementation
merchant default/base currency rules
foreign-exchange execution and rate authority
multi-currency checkout
price-hold lifecycle
Quote / Estimate lifecycle
quote acceptance/expiry/revision
full Order pricing model
Product/Offering/Inventory/variant model (DDR-OD-009)
Booking/Appointment payment/cancellation/refund policy (DDR-OD-006)
minimum Payment lifecycle state vocabulary
payment retry UX
provider-specific payment adapters
tax calculation engines
tax inclusion/exclusion policy catalogue
discount/coupon/promotion engines
gratuity/service-charge semantics
credit/overpayment model
chargeback/dispute semantics
invoice semantics
accounting/general ledger
bank/provider payout reconciliation
revenue recognition
exact rounding-policy catalogue
```

A downstream design may refine these only while preserving the accepted authority separations in this document.

---

# 66. Continuous-improvement checkpoint

The design was challenged against the temptation to create one universal commercial abstraction.

A simpler but weaker model would be:

```text
CommercialTransaction
    owns price
    owns amount due
    owns payment
    owns refund
    owns settlement
```

This was rejected because the evidence shows that these facts have different semantic owners, lifecycle and authority sources.

The stronger minimum architecture is:

```text
Reusable MonetaryAmount
        ↓ used by
Capability-owned commercial terms
        ↓
Capability-owned business commitments
        ↓ may establish
Payment-owned obligations
        ↓ fulfilled through
Provider-neutral payment execution
        ↓ evidenced by
Provider transaction evidence
        ↓ reconciled into
Main Street payment facts
```

Three material improvements are preserved:

1. `FROM` and `QUOTE_REQUIRED` are explicitly non-payable terms, allowing variable-price merchants without fake zero prices or arbitrary executable rules.
2. provider settlement is separated from customer payment before provider integration hardens, preventing future reconciliation/accounting corruption.
3. current commercial terms are separated from committed historical truth, preserving MS-PROT-054 compatibility/migration invariants.

No additional P0 design cluster was discovered during falsification.

---

# 67. Governance review

## PROPOSE

Define a reusable exact Money value while keeping commercial term, commitment, payment obligation, amount due and provider evidence semantically separate.

**PASS**

## REVIEW / FALSIFICATION

The model was challenged against:

```text
information publisher
free consultant
fixed-price consultant
tradesperson requiring quote
salon deposit
grocery quantity calculation
motel nightly rate
realtor asking price
provider fee/settlement
partial payment
overpayment mismatch
refund
hybrid merchant
pre-commit price change
```

No valid case requires one universal `CommercialTransaction`, one universal `Price`, provider-owned commercial truth or category-specific money semantics.

**PASS**

## VALIDATE

The model preserves:

```text
capability-owned authority
merchant-controlled bounded pricing values
historical commitment integrity
provider neutrality
payment reconciliation correctness
cross-domain reuse
no business-category runtime branches
```

while leaving detailed Booking/Appointment and Product/Inventory policies to their existing deferred-decision clusters.

**PASS**

## MANUAL APPROVAL

The material authority boundary was presented for manual approval and approved on **22 August 2026**.

**PASS**

## ACCEPT

**MS-PROT-055 v1.0 is ACCEPTED.**

It closes **DDR-OD-003 — Money, price and commercial-value semantics**.

---

# 68. Canonical decision

> **Main Street shall represent monetary value through an exact currency-qualified `MonetaryAmount` DataConcept while preserving semantic ownership in the field, capability or business fact that uses it. Money is not Price; current price is not historical committed price; committed commercial amount is not PaymentObligation; PaymentObligation is not provider transaction evidence; customer payment is not provider settlement; and refund does not rewrite historical price or automatically cancel the owning business commitment. Offering commercial terms use a bounded initial vocabulary of `FIXED`, `RATE`, `FROM`, `QUOTE_REQUIRED`, `NO_CHARGE` and `REGISTERED_CALCULATION`; indicative or quote-required terms cannot directly establish amount due, and calculated authoritative prices require registered deterministic Main Street semantics rather than merchant-authored executable formulas. Authoritative commitments revalidate current commercial terms and retain sufficient provenance so later price/configuration/semantic changes cannot reinterpret existing obligations. Payment obligations remain provider-independent Main Street business truth; provider transactions are authenticated external evidence applied/reconciled against those obligations, with partial payments and mismatches represented explicitly. Provider fees and settlement do not redefine customer payment or obligation. Adjustments and rounding require explicit semantic provenance where applicable. Detailed Booking/Appointment payment policy, Product/Inventory/variant semantics, quote lifecycle, tax/discount engines, foreign exchange, chargebacks and accounting remain downstream.**
