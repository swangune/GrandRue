# MS-PROT-055 v1.2 — Payment Commercial Access Classification Amendment

**Document ID:** MS-PROT-055  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Payment commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-055 through v1.1 within commercial-access classification only  
**Depends on:** Composite MS-PROT-055 through v1.1; composite MS-PROT-056 through v1.9; composite MS-PROT-062; composite MS-PROT-069; composite MS-PROT-077 through v1.2; applicable Booking/Appointment, Provider Fulfilment, Actor Authorisation, CustomerContext and Exposure authorities  
**Preserves:** Payment ownership of Payment Obligation, provider-payment interpretation, PaymentApplication and Refund; source-capability ownership of customer/business commitment; provider neutrality  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the missing Payment owner classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

---

# 0. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

GrandRue SHALL treat acquisition of **new customer monetary obligation** as BUSINESS operating value.

Once an authoritative Payment Obligation exists, commercial loss SHALL NOT prevent GrandRue from safely:

```text
observing it
collecting against it
reconciling external payment evidence
reducing it when source authority requires
refunding an already-applied payment
or otherwise resolving its historical financial consequence
```

Canonical:

```text
new customer monetary obligation
        ↓
BUSINESS commercial permission required

existing authoritative Payment Obligation
        ↓
payment / reconciliation / refund resolution
        ↓
no independent Payment entitlement
subject to all other authority
```

This prevents downgrade from becoming either:

```text
a way to create new payable business activity for free
```

or:

```text
a reason GrandRue can no longer finish
financial consequences already created legitimately
```

---

# 1. Governing Decision

Composite MS-PROT-055 SHALL define exactly one initially protected Payment commercial purpose:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

Its owner-qualified protected access contract SHALL be:

| Exact access contract | Protected purpose | Standard allocation | Target family |
|---|---|---|---|
| `payment/customer-obligation-establishment-access@1` | `ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION` | BUSINESS + GROWTH | `OPERATION_ACCESS` |

FREE SHALL NOT receive this purpose through the standard catalogue.

The following bounded Payment contracts SHALL require no independent Commercial Entitlement:

```text
payment/non-committing-preparation-access@1

payment/existing-obligation-observation-access@1

payment/existing-obligation-resolution-access@1

payment/provider-evidence-and-reconciliation-access@1

payment/refund-resolution-access@1
```

Missing classification is not an exemption.

---

# 2. Ownership Boundary

Payment continues to own:

```text
Payment Obligation

Payment Obligation Adjustment

Payment Execution Request

Provider Payment Evidence interpretation

PaymentApplication

Refund Execution Request

Provider Refund Evidence interpretation

Refund

Payment-owned due/outstanding derivation
```

Commercial owns:

```text
CommercialEntitlementIdentity

CommercialEntitlementDefinition

Commercial Access Binding

grant provenance

effective commercial permission

plan-revision grant sets

catalogue publication
```

Order, Booking, Appointment and other source capabilities retain ownership of the business commitment that caused a Payment consequence.

Provider infrastructure retains external execution responsibility.

Commercial permission SHALL NOT transfer any of those authorities.

---

# 3. Protected Payment Obligation Establishment

`payment/customer-obligation-establishment-access@1` SHALL govern creation of materially new customer monetary obligation under GrandRue Payment semantics.

It includes:

```text
payment.obligation.establish
```

where the operation establishes a new Payment Obligation.

Commercial permission does not establish:

```text
the source business commitment

the amount

currency

due condition

customer relationship

payment policy

actor authority

provider readiness
```

Those must already be valid under their applicable owners.

---

# 4. Atomic Source Commitment Does Not Remove the Payment Boundary

Where a source operation requires:

```text
business commitment
+
Payment Obligation
```

to commit atomically, both authorities remain distinct.

Example:

```text
new Order
    requires Ordering permission

and

its required Payment Obligation
    requires Payment permission
```

The standard BUSINESS and GROWTH catalogues may explicitly grant both.

Atomic persistence does not collapse:

```text
Order commercial access
        =
Payment commercial access
```

and does not transfer Payment ownership to Ordering.

---

# 5. New Obligation Increase

`payment.obligation.adjust` SHALL be commercial-effect-sensitive.

An `INCREASE` that authoritatively establishes additional payable amount SHALL require:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

when it creates materially new customer monetary obligation.

Example:

```text
existing effective obligation = £100

accepted source amendment
adds another £20

Payment adjustment:
INCREASE £20
```

The new £20 payable scope is commercially protected.

The source amendment must independently possess whatever commercial permission governs its own new business commitment.

---

# 6. Obligation Reduction

A `DECREASE` adjustment that only reduces an existing Payment Obligation SHALL require:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

through:

```text
payment/existing-obligation-resolution-access@1
```

Example:

```text
Order commitment legitimately reduced
        ↓
Payment obligation reduced
£100 → £70
```

Commercial downgrade SHALL NOT force GrandRue to continue claiming that £100 remains owed merely because Payment administration is no longer commercially available.

Source authority remains mandatory.

---

# 7. No Generic Free Upward Correction

This amendment does not invent a generic no-entitlement upward “correction” route.

Where a future accepted source authority must distinguish:

```text
new payable obligation
```

from:

```text
correction of historically misstated
Payment truth
```

that exact correction semantics must be accepted before it acquires a different commercial classification.

Until then:

```text
Payment Obligation INCREASE
that adds payable scope
```

uses the protected purpose.

This avoids an unbounded free route for increasing customer debt.

---

# 8. Non-Committing Preparation

GrandRue SHALL define:

```text
payment/non-committing-preparation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

It may include bounded preparation such as:

```text
identify current obligations

derive current authorised amount due

validate candidate payment amount

resolve the applicable current
payment-execution path

present provider-independent
payment preparation
```

Preparation SHALL NOT:

```text
establish a Payment Obligation

increase an obligation

create a PaymentExecutionRequest

call a provider

create PaymentApplication

create Refund
```

A preparation result is not durable payment authority.

---

# 9. Existing Payment Observation

GrandRue SHALL define:

```text
payment/existing-obligation-observation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

It covers otherwise-authorised observation of existing Payment facts, including where applicable:

```text
original obligation

effective obligation

Due Evaluation

outstanding amount

current amount due

PaymentApplications

Refunds

authorised execution/reconciliation state

authorised retained operation results
```

This classification grants no:

```text
customer relationship

Actor Authorisation

Exposure

provider diagnostic access

cross-Merchant access
```

A downgrade SHALL NOT make historical or still-unresolved customer payment obligations disappear.

---

# 10. Existing Obligation Discharge

GrandRue SHALL define:

```text
payment/existing-obligation-resolution-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for:

```text
payment.execute
```

when it is used solely to discharge one or more already-authoritative Payment Obligations.

Reason:

```text
Payment Obligation already exists
        ↓
customer already owes
authoritative amount
        ↓
payment execution resolves
that existing obligation
```

It does not establish new customer monetary obligation.

---

# 11. Payment Execution After Downgrade

Loss of BUSINESS/GROWTH entitlement SHALL NOT by itself prohibit payment of an obligation that was validly established earlier.

Example:

```text
Booking established while entitled

£100 obligation validly established

merchant later downgrades

£80 balance remains due
```

Where all other current Payment requirements are satisfied:

```text
customer may still discharge
the existing £80 obligation
```

through the accepted Payment execution path.

This is bounded residual resolution.

It is not ongoing permission to create another payable commitment.

---

# 12. Existing Obligation Bounds the Residual Path

The residual execution path SHALL NOT permit arbitrary provider charges.

`payment.execute` must continue to establish:

```text
exact existing Payment Obligation

Due Evaluation = DUE

current outstanding amount

requested amount within
authorised payable scope

current fulfilment binding

provider readiness

trusted execution context

applicable actor authority
```

The execution request SHALL NOT exceed current authorised payable scope.

Therefore:

```text
existing Payment Obligation = £40
```

cannot become:

```text
provider charge = £100
```

through residual commercial access.

---

# 13. No Payment Obligation, No Residual Payment Execution

The existence of a payment provider connection does not establish residual permission.

Neither does:

```text
merchant once had BUSINESS

customer wants to pay

provider checkout is available

merchant knows an amount

historical Order exists without
an authoritative Payment Obligation
```

Residual Payment execution requires an exact authoritative existing Payment Obligation.

Without it:

```text
payment/existing-obligation-resolution-access@1
```

does not apply.

---

# 14. Provider Evidence Recording

GrandRue SHALL classify:

```text
payment.provider-evidence.record
```

under:

```text
payment/provider-evidence-and-reconciliation-access@1
```

with no independent Commercial Entitlement.

Provider evidence may arrive after:

```text
subscription change

merchant logout

request timeout

provider delay

downgrade
```

GrandRue MUST still preserve authenticated external financial evidence.

Commercial loss SHALL NOT cause GrandRue to discard or ignore a provider fact concerning an already-attempted transaction.

---

# 15. Payment Reconciliation

```text
payment.reconcile
```

SHALL require no independent Commercial Entitlement.

Reconciliation determines whether retained provider evidence can legitimately establish PaymentApplication against existing Payment Obligations.

It does not create a new customer business commitment.

Therefore:

```text
provider evidence exists
        ↓
commercial state later changes
        ↓
reconciliation still proceeds
```

subject to all existing Payment authority.

This directly implements the residual `reconcile` principle in MS-PROT-056.

---

# 16. PaymentApplication Is Not a New Paid Activity

A PaymentApplication records authoritative discharge of an existing obligation from accepted payment evidence.

It does not itself create:

```text
new Order

new Booking

new Appointment

new Payment Obligation

new provider charge
```

Therefore committing a valid PaymentApplication through reconciliation requires no independent Commercial Entitlement.

---

# 17. Provider Call Recovery and Uncertainty

An already-accepted PaymentExecutionRequest retains its execution/reconciliation authority after commercial state changes.

If:

```text
PaymentExecutionRequest committed

provider may have charged

acknowledgement lost

merchant entitlement then ends
```

GrandRue SHALL:

```text
reconcile the original request
```

rather than:

```text
deny reconciliation because plan changed

or

blindly create another charge
```

Commercial loss cannot make financial uncertainty unresolvable.

---

# 18. Refund Resolution

GrandRue SHALL define:

```text
payment/refund-resolution-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for the accepted refund execution chain:

```text
payment.refund.request

payment.refund-evidence.record

payment.refund.reconcile
```

when the Refund is bounded to an exact existing eligible PaymentApplication and independently authorised business refund consequence.

---

# 19. Refund Is Residual Financial Resolution

A Refund does not create new customer purchase activity.

It resolves money previously received.

Therefore a merchant SHALL NOT be forced to retain BUSINESS/GROWTH merely to refund an amount that the applicable business authority validly requires or permits returning.

The existing bounds remain mandatory:

```text
exact PaymentApplication

same Merchant Scope

authoritative refund business consequence

requested amount > 0

currency compatibility

cumulative Refund
<= eligible applied payment

historically compatible provider path

provider readiness
```

No-entitlement classification grants none of those facts.

---

# 20. Refund Cannot Become a Free Money-Transfer Service

The residual Refund path SHALL NOT permit:

```text
arbitrary outbound payment

refund without prior PaymentApplication

refund exceeding received amount

refund to unrelated transaction

new provider-side payout unrelated
to an accepted payment
```

The exact prior PaymentApplication and business causation remain mandatory.

This keeps residual Refund access finite and source-bound.

---

# 21. Provider Refund Evidence and Reconciliation

Provider refund callbacks/evidence and:

```text
payment.refund.reconcile
```

remain available regardless of later subscription changes.

A provider effect already attempted must remain reconcilable.

Commercial loss SHALL NOT cause GrandRue to:

```text
forget uncertain refund execution

duplicate a refund

misreport provider uncertainty

discard authenticated refund evidence
```

---

# 22. Provider Readiness Remains Independent

No Payment commercial entitlement, protected or residual, establishes Provider Readiness.

For new or residual external execution:

```text
provider readiness
```

must independently satisfy composite MS-PROT-048.

A valid commercial path does not permit:

```text
use unavailable provider

ignore revoked connection

reuse incompatible historical provider
```

Likewise provider readiness does not manufacture a Payment entitlement or residual Payment authority.

---

# 23. Provider Connection Is Not Payment Commercial Authority

Possession of:

```text
provider credentials

ProviderConnection

provider merchant account

hosted checkout availability
```

does not satisfy:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

and does not establish an existing Payment Obligation.

Provider infrastructure is execution infrastructure.

Commercial permission remains GrandRue-owned.

The exact commercial classification of generic provider-connection establishment/administration remains with its owning authority where catalogue assembly requires that classification.

---

# 24. Source Capability Independence

A Payment entitlement SHALL NOT create source business semantics.

Example:

```text
merchant possesses Payment entitlement
```

does not mean the merchant may:

```text
create Order

create Booking

create Appointment

invent payable commercial source
```

The applicable source capability must independently authorise the business commitment.

Conversely, an Ordering entitlement does not by itself establish:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

where a new Payment Obligation is actually required.

The standard BUSINESS grant set may include both.

---

# 25. Payment Without Provider Execution

A Payment Obligation is Payment truth even when no immediate provider side effect occurs.

Therefore:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

is not equivalent to:

```text
provider charged successfully
```

The customer may owe an amount before it is paid.

This preserves:

```text
Payment Obligation
    ≠
Payment execution
```

---

# 26. Payment Without Ordering

Payment commercial access SHALL NOT be defined as an Ordering subfeature.

Accepted Payment sources may include:

```text
Booking

Appointment

Order

another separately accepted
commercial-source authority
```

The Payment binding remains independent even though BUSINESS currently includes the applicable operating portfolios together.

---

# 27. Configuration Independence

Commercial loss SHALL NOT delete:

```text
Payment configuration

provider-binding configuration

Payment Obligations

PaymentApplications

Refunds

historical provider affinity
```

merely because protected new Payment activity is no longer available.

Canonical:

```text
merchant operating configuration
        ≠
current commercial permission
```

A later valid entitlement may restore new-obligation establishment without reconstructing Payment history.

---

# 28. Customer Observation

The no-entitlement observation classification SHALL NOT make Payment data public.

Customer observation still requires the accepted:

```text
payment / related-customer-payment-obligation
```

relationship plus current security, authorisation and Exposure requirements.

Merchant observation likewise retains its applicable actor and data-access authority.

Commercial classification does not widen audience.

---

# 29. Retry and Idempotency

Recovery of an already-committed:

```text
Payment Obligation

PaymentExecutionRequest

PaymentApplication

RefundExecutionRequest

Refund
```

shall not be treated as a new protected commercial effect.

A retry that would establish a new Payment Obligation or protected payable increase SHALL satisfy current:

```text
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

Idempotency does not preserve expired commercial permission for an uncommitted protected effect.

---

# 30. Failure Semantics

Commercial outcomes SHALL remain distinguishable from Payment outcomes.

At minimum:

```text
COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

PAYMENT_NOT_DUE

PAYMENT_DUE_UNRESOLVED

OBLIGATION_AMOUNT_CONFLICT

PROVIDER_NOT_READY

PROVIDER_BUSINESS_REJECTION

PAYMENT_EVIDENCE_MISMATCH

PAYMENT_RECONCILIATION_REQUIRED

REFUND_NOT_AUTHORISED

REFUND_EXCEEDS_ELIGIBLE_AMOUNT

AUTHORISATION_REJECTION

AUTHORITATIVE_CONFLICT

TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

Commercial denial SHALL NOT be reported as:

```text
payment declined

provider unavailable

customer owes nothing

refund rejected
```

unless those independent conditions actually exist.

Missing binding information fails closed.

---

# 31. Standard Allocation

The future canonical entitlement binding for:

```text
payment/customer-obligation-establishment-access@1
+
ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION
```

SHALL be granted explicitly by:

```text
BUSINESS

GROWTH
```

and SHALL NOT be granted by:

```text
FREE
```

through the standard catalogue.

This amendment does not mint the final `CommercialEntitlementIdentity`.

That remains Commercial-owned work under:

```text
MS-PROT-056-V17-DQ-001
```

---

# 32. Catalogue Consequences

The complete standard manifest SHALL record:

```text
protected:
    payment/customer-obligation-establishment-access@1
    +
    ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION

no independent entitlement:
    payment/non-committing-preparation-access@1

    payment/existing-obligation-observation-access@1

    payment/existing-obligation-resolution-access@1

    payment/provider-evidence-and-reconciliation-access@1

    payment/refund-resolution-access@1
```

It SHALL also preserve independently required provider, source-capability, security and Exposure classifications.

No Payment binding is a wildcard over those neighbouring authorities.

---

# 33. Review and Falsification

| Challenge | Required outcome |
|---|---|
| FREE merchant has no Payment use | No Payment Obligation is manufactured |
| BUSINESS Order creates a required payable obligation | Protected Payment purpose required in addition to applicable source authority |
| Merchant creates an otherwise-valid Booking with no payable amount | No Payment Obligation requirement is manufactured |
| Existing £100 obligation survives downgrade | Obligation remains authoritative and observable where authorised |
| £100 obligation is reduced to £70 after valid Order reduction | DECREASE remains possible without new Payment entitlement |
| Merchant tries to increase £70 to £100 after downgrade | Protected Payment permission required unless future accepted correction semantics state otherwise |
| £80 remained due when merchant downgraded | Customer may still discharge exact existing obligation |
| Provider returns old callback after downgrade | Evidence remains recordable and reconcilable |
| Payment request became uncertain before downgrade | Reconciliation continues; no blind second charge |
| Refund becomes valid after downgrade | Bounded refund remains possible against prior PaymentApplication |
| Merchant attempts £150 refund against £100 application | Existing Payment safety rejects it |
| Provider connection exists but no Payment Obligation | No residual collection authority |
| Merchant once had BUSINESS | Historical plan possession alone grants nothing |
| Provider is READY | Provider readiness does not create Payment commercial authority |
| BUSINESS merchant has Payment entitlement but no Order/Booking authority | Payment entitlement cannot invent a source commitment |
| Customer pays £100 and provider settles £97 | Customer PaymentApplication remains governed by Payment semantics, not settlement |
| Existing payment obligation is already fully discharged | Residual execution cannot charge beyond current authorised payable scope |

No falsifier requires a universal Payment status, provider-owned commercial authority or continued subscription to resolve historical payment effects.

---

# 34. Low-Software-Capacity Merchant Falsifier

A small merchant should experience:

```text
BUSINESS:
    take a new customer order
    collect payment normally
```

After downgrade:

```text
no new paid operating commitments

but:

customers can still pay
amounts they already legitimately owe

GrandRue still reconciles
payments already attempted

merchant can still refund
legitimate prior payments
```

The merchant does not need to understand:

```text
PaymentExecutionRequest

ProviderPaymentEvidence

PaymentApplication

Commercial Access Binding

residual commercial access
```

GrandRue absorbs those distinctions.

---

# 35. Alternatives Rejected

## Protect `payment.execute` unconditionally

Rejected because a merchant could lose the ability to collect an already-authoritative customer debt merely because the GrandRue subscription changed.

## Make all Payment activity free once Payment exists

Rejected because it would permit creation of new payable customer activity without the BUSINESS boundary.

## Provider connection grants Payment access

Rejected because external provider state does not own GrandRue commercial permission.

## Source-capability entitlement automatically grants Payment

Rejected because Payment remains independently owned and not every source commitment has Payment semantics.

## Separate payment and refund paid entitlements

Rejected because Refund is bounded resolution of money already received rather than new customer operating capacity.

## Block reconciliation after downgrade

Rejected because it would turn subscription state into financial uncertainty and could cause duplicate or lost provider effects.

## One generic `PAYMENTS_ENABLED` flag

Rejected because it collapses Payment configuration, commercial permission, provider readiness, existing obligations and reconciliation.

---

# 36. Amendment Effect

This amendment closes the **Payment owner-classification blocker** discovered during completion of `MS-PROT-056-V17-DQ-001`.

It does not:

```text
mint final CommercialEntitlementIdentity values

classify generic provider-connection
commercial access

classify Order Fulfilment / Shipment

classify Financial Operations

publish the standard catalogue

resolve pricing

activate implementation
```

`MS-PROT-056-V17-DQ-001` remains OPEN until the remaining owner/support classifications and complete Commercial manifest are accepted.

# Recommendation

**RECOMMENDATION: ACCEPT**
