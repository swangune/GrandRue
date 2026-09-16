# MS-PROT-055 v1.1 — Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment

**Document ID:** MS-PROT-055  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Approved:** Manual approval on 27 August 2026 after governed authority trace, gap classification, operation-contract review, provider/uncertainty review, refund review, customer-relationship review and falsification under `DESIGN-RULES.md` v2.1  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-055 v1.0 within Payment Obligation execution, due evaluation, obligation adjustment, provider execution/reconciliation, Refund, Payment customer relationship and production-read scope  
**Depends on:** MS-PROT-023; MS-PROT-024; MS-PROT-025; composite MS-PROT-027 through v1.5; composite MS-PROT-042 through v1.6; composite MS-PROT-048 through v1.4; MS-PROT-049 v1.3; MS-PROT-053; MS-PROT-055 v1.0; MS-PROT-059; MS-PROT-062; MS-PROT-063; MS-PROT-065; MS-PROT-067; MS-PROT-069; MS-PROT-070; MS-PROT-072; composite MS-PROT-077 through v1.1; MS-PROT-079  
**Closes:** MS-PROT-079 Target 13 — Payment; Payment scope of `MS-PROT-027-V15-DQ-005`; Payment-specific degraded-operation scope of `MS-PROT-048-V14-DQ-014`; MS-PROT-055 v1.0's deferred minimum Payment lifecycle/execution question  
**Purpose:** Make the accepted Money/Payment model production-executable across independently owned business commitments while preserving provider neutrality, historical obligation truth, payment/refund separation, uncertainty safety and capability ownership.

---

## 1. Governing Decision

Main Street SHALL retain:

```text
BUSINESS COMMITMENT
    ≠
PAYMENT OBLIGATION
    ≠
PAYMENT EXECUTION REQUEST
    ≠
PROVIDER PAYMENT EVIDENCE
    ≠
PAYMENT APPLICATION
    ≠
PROVIDER SETTLEMENT
    ≠
REFUND EXECUTION REQUEST
    ≠
REFUND
```

Payment owns:

```text
Payment Obligation
Payment Obligation Adjustment
payment-execution progression
Provider Payment Evidence interpretation
Payment Application
Refund execution progression
Refund
Payment-owned derived monetary results
```

Payment does not own:

```text
Order
Booking
Appointment
Merchant Commercial Agreement
Order Fulfilment
Return
merchant cancellation policy
provider financial truth
```

---

## 2. No Universal Payment Status

Target 13 resolves MS-PROT-055's deferred minimum lifecycle question by **rejecting a universal Payment lifecycle state machine**.

Main Street SHALL NOT establish:

```text
PENDING
→ PAID
→ REFUNDED
→ FAILED
```

as one authoritative Payment status.

Instead authoritative truth is composed from facts:

```text
Payment Obligation
+
Obligation Adjustments
+
Payment Applications
+
Refunds
+
provider evidence
+
execution-certainty evidence
```

Labels such as:

```text
UNPAID
PARTIALLY PAID
PAID
REFUNDED
PARTIALLY REFUNDED
```

MAY be derived projections where correctly defined.

---

## 3. Payment Obligation Identity

A Payment Obligation remains a durable merchant-scoped Payment-owned business fact.

It MUST preserve:

```text
obligation identity
MerchantScope
owner-qualified commercial-source reference
original MonetaryAmount
due-condition reference
historical commercial/policy provenance
semantic/configuration affinity where required
establishedAt
```

One business commitment MAY establish:

```text
0..n Payment Obligations
```

One Payment Obligation MUST reference exactly one authoritative commercial source.

---

## 4. Commercial Source Reference

A source reference MUST identify enough authoritative context to resolve the exact owning business fact.

Conceptually:

```text
CommercialSourceReference
{
    owner capability
    source object type
    source object identity
    source portion identity where required
    MerchantScope
}
```

A bare ambiguous `"commitment-1"` string is not sufficient semantic authority.

Exact Java representation remains implementation scope.

---

## 5. Payment Obligation Is Immutable Historical Truth

The original:

```text
obligation amount
currency
commercial source
due-condition meaning
establishment provenance
```

MUST NOT be destructively rewritten.

Later legitimate changes are represented by additional Payment-owned facts.

---

# Payment Obligation Adjustment

## 6. PaymentObligationAdjustment

A **PaymentObligationAdjustment** is an immutable Payment-owned fact recording an authorised monetary increase or decrease to an existing obligation.

Conceptually:

```text
PaymentObligationAdjustment
{
    adjustmentIdentity
    obligationIdentity
    direction
    MonetaryAmount
    source authority / causation
    provenance
    occurredAt
}
```

Direction is semantic:

```text
INCREASE
DECREASE
```

The amount remains non-negative.

Negative Money remains prohibited.

---

## 7. Effective Obligation Amount

For one obligation:

```text
effective obligation amount
=
original obligation amount
+
sum(INCREASE adjustments)
-
sum(DECREASE adjustments)
```

Invariant:

```text
effective obligation amount >= 0
```

Adjustments MUST use the same currency as their obligation.

---

## 8. Adjustment Does Not Rewrite History

Suppose:

```text
original obligation = £100
Order amendment reduces commercial commitment by £30
```

Correct:

```text
original obligation    £100
DECREASE adjustment     £30
effective obligation    £70
```

Rejected:

```text
UPDATE payment_obligation
SET amount = £70
```

as the semantic representation.

---

## 9. Adjustment Does Not Automatically Refund

Suppose:

```text
effective obligation before adjustment = £100
already applied payment                 = £100
later authorised DECREASE               = £30
```

Then:

```text
effective obligation = £70
applied payment      = £100
excess applied       = £30
```

The £30 discrepancy is explicit.

It does NOT automatically mean:

```text
Refund completed
customer credit exists
Order cancelled
```

A separately authorised Refund consequence is required.

---

# Due Conditions

## 10. Payment Due Condition Contract

A Payment Obligation's due condition MUST resolve through a registered deterministic **Payment Due Condition Contract**.

It MUST NOT be an arbitrary string interpreted by application code.

The contract establishes:

```text
owner
identity/version
required parameters
authoritative evidence sources
deterministic evaluator
historical affinity
```

A baseline immediate-due condition MAY be registered.

Capability-specific conditions require accepted registered semantics.

---

## 11. Due Evaluation

For one Payment Obligation, current due evaluation is:

```text
DUE
NOT_DUE
UNRESOLVED
```

### DUE

The registered due-condition predicate is currently satisfied.

### NOT_DUE

Sufficient authoritative evidence establishes that the obligation exists but is not presently due.

### UNRESOLVED

Required evidence cannot presently establish whether the obligation is due.

`UNRESOLVED` MUST NOT be treated as `DUE`.

---

## 12. Outstanding Amount and Current Amount Due Are Different

Define:

```text
applied amount
=
sum(PaymentApplications)
```

Then:

```text
outstanding obligation amount
=
max(0,
    effective obligation amount
    -
    applied amount)
```

Current amount due for that obligation is:

```text
if Due Evaluation = DUE
    → outstanding obligation amount

if Due Evaluation = NOT_DUE
    → no amount currently due

if Due Evaluation = UNRESOLVED
    → current amount due is UNRESOLVED
```

A scalar numeric field MUST NOT hide the third case.

---

## 13. Excess Applied Amount

Where:

```text
applied amount > effective obligation amount
```

derive:

```text
excess applied amount
=
applied amount
-
effective obligation amount
```

This is reconciliation evidence.

It is not automatically:

```text
credit
Refund
new obligation
```

Those require separately accepted semantics.

---

# Payment Obligation Operations

## 14. `payment.obligation.establish`

**Owner:** Payment.

**Principal:** Current accepted capability/application/system authority establishing the payment consequence.

**Inputs:**

```text
MerchantScope
logical command identity
obligation identity
owner-qualified commercial source
MonetaryAmount
registered due-condition reference
commercial/policy provenance
semantic/configuration provenance where required
```

**Authoritative reads:**

- source commercial authority;
- registered Payment semantics;
- applicable policy/provenance;
- MerchantScope.

**Mutation:** Exactly one immutable Payment Obligation.

**Idempotency:**

```text
same command + same intent
    → same obligation result

same command identity + changed intent
    → identity conflict
```

**Provider:** None.

Creating a Payment Obligation MUST NOT call an external payment provider.

---

## 15. Commitment/Obligation Atomicity

Where an accepted source capability declares Payment Obligation creation to be part of the validity of its business commitment:

```text
source commitment
+
required Payment Obligation(s)
```

MUST share the narrow local Main Street atomic consistency boundary.

MS-PROT-077 already explicitly permits this for Order commitments.

Where obligation creation is a separately valid subsequent consequence instead, it MAY be independently committed through accepted durable orchestration.

Cross-capability convenience alone does not justify shared atomicity.

---

## 16. No Provider Call Inside Commitment Transaction

External financial execution MUST NOT participate inside the local transaction establishing:

```text
Order
Booking
Appointment
Payment Obligation
```

Provider wait/retry/uncertainty is an external execution boundary.

---

## 17. `payment.obligation.adjust`

**Owner:** Payment.

**Requester:** An accepted capability/process whose authoritative business consequence changes an existing Payment Obligation.

**Inputs:**

```text
MerchantScope
logical command identity
adjustment identity
Payment Obligation identity
INCREASE | DECREASE
MonetaryAmount
source authority / causation
provenance
```

**Reads:**

```text
original obligation
prior adjustments
source-authority consequence
```

**Mutation:** One immutable PaymentObligationAdjustment.

**Invariant:** Effective obligation cannot become negative.

**No effect on:**

```text
source business commitment
PaymentApplications
Refunds
provider evidence
```

Retries are idempotent by logical adjustment intent.

---

# Payment Execution

## 18. Payment Execution Request

A **Payment Execution Request** represents one logical Payment-owned request to discharge one or more currently payable Payment Obligations through the selected payment-execution fulfilment path.

It is durable execution/progression identity.

It is not:

```text
Payment Obligation
provider transaction
PaymentApplication
Payment status
```

---

## 19. Minimum Payment Execution Request

Conceptually:

```text
PaymentExecutionRequest
{
    requestIdentity
    MerchantScope

    targets [
        obligationIdentity
        intendedAmount
    ]

    total MonetaryAmount

    governing payment-execution binding
    ProviderConnection where required

    logical command / causation identity
    semantic/configuration provenance
    acceptedAt
}
```

Cardinality:

```text
1 PaymentExecutionRequest
    → 1..n obligation targets
```

All target amounts in one request MUST use the same currency.

---

## 20. Payment-Execution Fulfilment Role

Target 13 concretises the existing role family as:

```text
payment / payment-execution
```

Its applicable provider obligations include:

```text
initiate the exact accepted financial request

preserve Main Street correlation

use stable idempotency identity where provider supports it

return and/or permit retrieval of authenticated
provider transaction evidence

support reconciliation of uncertain execution
to the extent declared by the provider contract
```

The provider does not own Payment Obligation truth.

MS-PROT-048 already establishes this fulfilment-role architecture.

---

## 21. `payment.execute`

**Owner:** Payment.

Before accepting a new request it MUST establish:

```text
MerchantScope
trusted execution context
Actor Authorisation where required
Semantic Applicability
Commercial Entitlement where applicable
current effective obligation amount
Due Evaluation = DUE
current outstanding amount
requested amount permitted by applicable payment policy
current payment-execution Fulfilment Binding
Provider Readiness
```

Request amount MUST NOT exceed currently authorised payable scope.

Early payment of a `NOT_DUE` obligation requires a separately accepted early-payment rule.

---

## 22. Provider Readiness

For new provider-dependent payment execution:

```text
READY
    → execution may proceed

DEGRADED
    → proceed only when the registered
      Payment/provider contract explicitly permits it

NOT_READY
    → do not initiate provider side effect

UNKNOWN
    → do not initiate provider side effect
```

This resolves the Payment-specific degraded-operation scope left by MS-PROT-048 v1.4.

Callback/reconciliation of an already-attempted transaction remains independently permitted where its residual contract allows.

---

## 23. Durable Acceptance Before External Side Effect

Before Main Street performs the external financial side effect, it MUST durably establish enough information to reconstruct:

```text
PaymentExecutionRequest identity
exact intended amount
exact obligation target(s)
provider/binding provenance
correlation/idempotency identity
```

The originating browser/network connection is not required to survive.

This follows MS-PROT-069's durable-acceptance model.

---

## 24. Provider Call Is Post-Acceptance External Work

Canonical:

```text
PaymentExecutionRequest committed
        ↓
provider readiness revalidated where required
        ↓
provider financial request
        ↓
response / callback / query / uncertainty
```

Never:

```text
call provider
    ↓
provider may charge
    ↓
then try to invent a Main Street correlation identity
```

---

## 25. Payment Retry

One logical payment intent uses one stable PaymentExecutionRequest identity.

Transport retry MUST NOT create another logical request.

Where provider idempotency is available:

```text
same PaymentExecutionRequest
    → same provider idempotency identity
```

Where provider outcome is uncertain and retry safety cannot be proven:

```text
NO blind retry
    ↓
reconciliation required
```

A genuine new payment intention receives a new PaymentExecutionRequest identity.

---

# Provider Payment Evidence

## 26. ProviderPaymentEvidence Remains External Evidence

ProviderPaymentEvidence is an immutable observation of external provider execution.

Multiple observations MAY legitimately reference the same provider transaction:

```text
provider transaction P1
    ├── evidence E1
    ├── evidence E2
    └── evidence E3
```

because later authenticated evidence may provide materially new information.

Earlier evidence is not destructively rewritten.

---

## 27. Provider Result Vocabulary Remains Provider-Owned

Raw values such as:

```text
AUTHORIZED
CAPTURED
COMPLETED
SETTLED
FAILED
CANCELLED
```

MUST NOT directly determine PaymentApplication.

The registered provider-payment interpretation contract determines whether the evidence can substantiate discharge of a Payment Obligation.

A free `providerResultCategory` therefore remains evidence only.

---

## 28. `payment.provider-evidence.record`

Incoming evidence MUST pass:

```text
provider/source authentication
+
message integrity validation
+
expected provider/connection context
+
MerchantScope resolution
+
PaymentExecutionRequest correlation
```

before entering Payment authority.

Successful recording stores immutable ProviderPaymentEvidence.

Recording provider evidence does NOT itself create PaymentApplication.

A duplicate delivery of the same provider observation MUST NOT create a second evidence fact.

A later materially distinct provider observation may create new evidence.

---

## 29. Evidence Must Survive Reconciliation Failure

ProviderPaymentEvidence is a valid external fact independently of whether Main Street can immediately apply it.

Therefore:

```text
record provider evidence
```

MUST NOT be rolled back merely because:

```text
obligation changed
amount mismatch exists
due relationship changed
automatic allocation is ambiguous
```

The evidence remains available for reconciliation.

This is why provider-evidence recording and PaymentApplication establishment are distinct authoritative steps.

---

# Payment Reconciliation

## 30. `payment.reconcile`

**Owner:** Payment.

The operation reads:

```text
PaymentExecutionRequest
ProviderPaymentEvidence
current obligation(s)
Payment Obligation Adjustments
existing PaymentApplications
provider interpretation contract
binding/provider provenance
```

It may establish zero or more PaymentApplications.

---

## 31. PaymentApplication Preconditions

A PaymentApplication may commit only when:

```text
provider evidence is authenticated/correlated

provider interpretation establishes
discharge-eligible payment evidence

currency matches

applied amount > 0

applied amount does not exceed
the provider evidence amount remaining unapplied

applied amount does not exceed
the currently applicable obligation amount
available for application
```

A raw provider `"SUCCESS"` or `"CAPTURED"` string alone is insufficient.

---

## 32. PaymentApplication Cardinality

Accepted cardinality is:

```text
1 PaymentObligation
    → 0..n PaymentApplications

1 ProviderPaymentEvidence item
    → 0..n PaymentApplications
```

Therefore Payment Obligations and provider transactions remain many-to-many through PaymentApplication.

This preserves MS-PROT-055 v1.0.

---

## 33. Automatic Reconciliation — Exact Match

For a multi-target PaymentExecutionRequest where provider evidence confirms the exact requested aggregate amount:

```text
provider confirmed amount
=
request total
```

Main Street MAY establish the request's pre-recorded target allocations atomically, subject to current authoritative obligation checks.

---

## 34. Partial Provider Payment

For a request containing exactly one target:

```text
request £100
provider evidence establishes £40
```

Main Street MAY establish:

```text
PaymentApplication £40
```

where the payment/provider contract permits partial discharge.

The source obligation remains £100.

---

## 35. Multi-Target Underpayment

For:

```text
Target A £40
Target B £60
requested total £100

provider evidence £70
```

Main Street MUST NOT invent:

```text
A gets £40
B gets £30
```

unless the accepted PaymentExecutionRequest carries a registered deterministic allocation rule establishing that result.

Absent such authority:

```text
provider evidence retained
automatic allocation unresolved
reconciliation required
```

---

## 36. Overpayment

For:

```text
requested / outstanding = £100
provider evidence        = £110
```

Main Street MUST NOT:

```text
increase Payment Obligation to £110
```

It may apply only the authorised amount.

The residual provider evidence remains explicit and unresolved.

Target 13 does not create a generic credit ledger.

---

## 37. Concurrent Payment Applications

Payment reconciliation MUST prevent:

```text
two concurrent evidence items
    ↓
collectively applying more than
the current applicable obligation amount
```

and:

```text
one evidence item
    ↓
being applied beyond its evidenced amount
```

Exact locking/CAS technology remains implementation scope.

---

# Refunds

## 38. Refund Is Payment-Owned Historical Truth

A **Refund** is:

> A durable Payment-owned fact establishing that a specific monetary amount previously received through an accepted payment path has subsequently been returned through an authorised refund execution.

A Refund does not rewrite:

```text
Payment Obligation
PaymentApplication
original provider payment evidence
commercial commitment
```

---

## 39. Refund Is Not Obligation Reversal

After:

```text
Obligation £100
PaymentApplication £100
Refund £100
```

the historical truth remains all three facts.

The Refund does not automatically make:

```text
Current Amount Due = £100
```

again.

A new/reinstated obligation requires separate authorised Payment Obligation semantics.

---

## 40. Refund Source

Initial production Refund MUST trace to an exact PaymentApplication and its underlying provider payment evidence.

Conceptually:

```text
Refund
{
    refundIdentity
    MerchantScope
    PaymentApplication reference
    source provider-payment evidence reference
    MonetaryAmount
    ProviderRefundEvidence reference
    business causation/provenance
    refundedAt
}
```

One payment may have multiple partial Refunds.

---

## 41. Refundable Payment Safety Boundary

For one PaymentApplication:

```text
cumulative Refund amount
<=
applied payment amount
```

Target 13 does not decide whether merchant policy **should** permit a Refund.

That decision originates from:

```text
Order policy
Booking/Appointment policy
Return policy
Commercial remediation
another accepted business authority
```

Payment enforces only Payment-owned execution and monetary safety once the refund consequence is authorised.

---

## 42. Refund-Execution Fulfilment Role

Target 13 establishes:

```text
payment / refund-execution
```

as a distinct fulfilment responsibility.

Its obligations include:

```text
initiate exact authorised Refund amount
against the compatible provider-side payment

preserve correlation/idempotency

return/query authenticated refund evidence

support uncertainty reconciliation
as declared by provider contract
```

Payment collection and Refund execution MAY use the same provider infrastructure, but they are not assumed to be the same technical obligation.

---

## 43. Historical Provider Affinity

A later merchant provider change MUST NOT silently route a Refund for an old payment through the new provider.

Canonical:

```text
Payment made using Provider A / Binding B1

merchant later selects Provider B

Refund of original payment
    → uses provider/binding context
      compatible with the original provider transaction
```

unless a separately accepted provider contract explicitly supports another valid route.

This preserves historical provider provenance required by MS-PROT-048.

---

## 44. RefundExecutionRequest

A **RefundExecutionRequest** is durable Payment-owned progression identity for one authorised external refund side effect.

It retains:

```text
request identity
MerchantScope
PaymentApplication
source provider evidence
Refund amount
business causation
historical provider/binding provenance
logical command identity
acceptedAt
```

It is not the Refund itself.

---

## 45. `payment.refund.request`

Before accepting the request, Payment MUST establish:

```text
authoritative business refund consequence/permission
PaymentApplication exists
same MerchantScope
requested Refund amount > 0
requested currency matches
cumulative committed Refunds will not exceed
the eligible PaymentApplication amount
historically compatible provider path exists
Provider Readiness for new refund execution
```

Success durably establishes one RefundExecutionRequest before provider side effect begins.

---

## 46. Refund Provider Uncertainty

If:

```text
refund request sent
+
provider response lost
```

Main Street MUST NOT assume:

```text
Refund failed
```

and MUST NOT issue another potentially duplicate Refund unless provider retry safety is independently established.

The request becomes subject to MS-PROT-069 reconciliation.

---

## 47. ProviderRefundEvidence

Provider refund evidence is distinct from ProviderPaymentEvidence.

It preserves:

```text
provider identity
provider refund/transaction reference
original provider transaction correlation
RefundExecutionRequest correlation
MonetaryAmount
provider result evidence
observedAt
receipt/reference where justified
```

Negative Money MUST NOT be used to distinguish Refund evidence from Payment evidence.

---

## 48. `payment.refund-evidence.record`

Authenticated/correlated Refund provider evidence is durably recorded before attempting to establish the Main Street Refund fact.

Recording the evidence does not automatically establish Refund.

Provider result vocabulary must pass the registered refund interpretation contract.

---

## 49. `payment.refund.reconcile`

A Refund commits only when provider evidence establishes that the authorised refund side effect occurred according to the registered provider contract.

The operation atomically protects:

```text
Refund identity
+
cumulative refund quantity
+
source PaymentApplication relationship
+
provider refund evidence relationship
```

Duplicate callbacks/retries MUST NOT multiply Refund.

---

## 50. Refund Failure Does Not Mutate Source Commitment

Known refund rejection or technical failure leaves:

```text
Order
Booking
Appointment
PaymentApplication
```

unchanged.

If the underlying business commitment was already cancelled/released, that business fact remains cancelled/released.

The refund failure becomes a Payment/provider resolution concern.

This preserves the accepted Booking/Appointment policy boundary.

---

# Provider and Uncertainty Boundaries

## 51. Callback Processing

Provider callbacks concerning previously initiated payment/refund execution do not require ordinary new-operation readiness.

They require:

```text
authenticated provider source
correct connection/provider context
exact correlation
existing PaymentExecutionRequest
or RefundExecutionRequest
registered provider interpretation
```

before Payment-owned mutation.

This directly composes with MS-PROT-048 v1.4.

---

## 52. Provider Switch

Changing current payment provider affects future execution routing.

It does not rewrite:

```text
existing Payment Obligations
existing PaymentExecutionRequests
ProviderPaymentEvidence
PaymentApplications
Refunds
historical provider provenance
```

Existing uncertain operations continue to reconcile against their historical provider context.

---

## 53. Settlement Boundary

Provider Settlement remains provider-side financial evidence.

Target 13 establishes no general payout/accounting reconciliation architecture.

Therefore:

```text
customer payment £100
provider fee       £3
settlement        £97
```

still means the PaymentApplication may represent £100.

Settlement does not reduce customer payment to £97.

MS-PROT-055 v1.0 already governs this.

---

# Customer Relationship and Reads

## 54. Payment Customer Requirement

Target 13 establishes:

```text
payment / related-customer-payment-obligation
```

It means:

> The current trusted customer access context is authoritatively related to the exact commercial source whose Payment Obligation is being considered within the exact MerchantScope.

Payment does **not** create a competing customer relationship.

---

## 55. Relationship Evaluation

Canonical:

```text
Payment Obligation
    ↓
owner-qualified commercial-source reference
    ↓
source capability's accepted
related-customer requirement
    ↓
current trusted customer context
```

Examples:

```text
Order obligation
    → ordering / related-customer-order

Booking obligation
    → booking / related-customer-booking

Appointment obligation
    → appointment / related-customer-appointment
```

Payment reuses the owning source relationship rather than duplicating CustomerContext linkage.

---

## 56. Things That Do Not Establish Payment Customer Relationship

None independently qualifies:

```text
CustomerAccount authentication
Payment Obligation ID
provider transaction ID
email match
payer name
cardholder name
provider customer ID
receipt possession
AI inference
```

Provider payer identity is not Main Street customer-relationship authority.

---

## 57. Guest Payment Access

A valid transaction-specific customer context MAY satisfy Payment customer access if it is authoritatively scoped to:

```text
exact MerchantScope
+
exact source business commitment
or exact Payment Obligation
```

under accepted security authority.

Exact token/credential representation remains `ADR-014-DQ-011`.

---

## 58. Initial Payment Read Architecture

Initial production Payment reads SHALL use:

```text
Payment owner query
+
request-scoped due/effective/outstanding derivation
+
bounded source-capability composition where required
```

No initial:

```text
Payment status cache
Redis payment read model
persistent AmountDue projection
generic financial dashboard database
```

is required.

No current MS-PROT-027 trigger demonstrates the need.

---

## 59. Amount-Due Projection Safety

A read representation MAY derive:

```text
original obligation
effective obligation
applied amount
outstanding amount
Due Evaluation
current amount due
Refund evidence
```

where audience and Exposure permit.

A stale read MUST NOT authorise:

```text
payment execution
Refund
obligation adjustment
```

All mutations re-enter current Payment authority.

---

## 60. CUSTOMER Exposure

Satisfying:

```text
payment / related-customer-payment-obligation
```

does NOT expose every Payment field.

Provider diagnostics, internal correlation identities, connection IDs, merchant/provider settlement evidence and internal reconciliation details require independent Exposure authority before CUSTOMER observation.

This resolves the Payment relationship-evaluator scope without pre-designing Target-20 API payloads.

---

# Security, Data Protection and AI

## 61. Sensitive Payment Data

Target 13 does not authorise Main Street storage of:

```text
full card number
CVV
provider secret
raw payment credential
unnecessary provider payload
```

Provider-hosted/tokenised mechanisms remain implementation/provider concerns under MS-PROT-067 and MS-PROT-053.

PaymentExecutionRequest stores correlation/provenance, not raw payment credentials.

---

## 62. AI Boundary

AI MAY:

```text
explain Payment Obligation
explain amount due
help merchant understand a mismatch
interpret an authorised refund request
```

AI MUST NOT:

```text
declare provider payment successful
create PaymentApplication from confidence
invent due-condition satisfaction
invent obligation adjustment
invent refund entitlement
repeat an uncertain provider charge
invent customer relationship
```

---

# Failure and Concurrency

## 63. Failure Taxonomy

Target-13 operations MUST preserve at least:

```text
VALIDATION_REJECTION

AUTHORISATION_REJECTION

ENTITLEMENT_REJECTION

PAYMENT_NOT_DUE

PAYMENT_DUE_UNRESOLVED

OBLIGATION_AMOUNT_CONFLICT

PROVIDER_NOT_READY

PROVIDER_BUSINESS_REJECTION

PAYMENT_EVIDENCE_MISMATCH

PAYMENT_RECONCILIATION_REQUIRED

REFUND_NOT_AUTHORISED

REFUND_EXCEEDS_ELIGIBLE_AMOUNT

AUTHORITATIVE_CONFLICT

PROVIDER / TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

Transport/exception names remain implementation scope.

---

## 64. Concurrency Invariants

The authoritative Payment boundary MUST prevent:

```text
PaymentApplications exceeding provider evidence

PaymentApplications over-discharging
the currently applicable obligation amount

Payment Obligation adjustments
making effective obligation negative

Refunds exceeding eligible applied payment amount

duplicate provider callbacks
multiplying PaymentApplication or Refund

same logical provider request
multiplying financial side effects
```

Exact SQL locks, versions or CAS are implementation details.

---

# Cross-Capability Consequences

## 65. Payment Does Not Confirm Other Commitments Automatically

A PaymentApplication MAY be evidence consumed by an accepted application process.

It does not itself:

```text
confirm Booking
confirm Appointment
commit Order
activate Commercial Agreement
fulfil Order
```

The owning capability/process performs its own authoritative transition.

---

## 66. Commitment Cancellation Does Not Automatically Refund

Likewise:

```text
Order released
Booking cancelled
Appointment cancelled
Return accepted
```

does not itself establish Refund.

The source policy/operation may authorise or require a Payment refund consequence.

Payment then executes that consequence through the contracts above.

---

# Events and Background Work

## 67. Source Facts

Target 13 does not require one event for every Payment mutation.

The following committed facts MAY serve as source events where an accepted process requires them:

```text
Payment Obligation established
PaymentApplication recorded
Refund recorded
```

An event, where registered, describes an already committed Payment fact.

Provider execution requests themselves use durable external-work/process mechanics rather than pretending provider work completed merely because a Domain Event was emitted.

---

## 68. Background Work

Provider calls, callback waits and reconciliation may require durable background/process work under MS-PROT-065/MS-PROT-072.

Target 13 defines **why and what** must survive.

Target 18 later closes the generic machinery.

A worker MUST re-enter authoritative Payment/provenance state before issuing another financial side effect.

---

# Falsification

## 69. Falsification Record

| Scenario | Required result |
|---|---|
| £20 deposit immediate + £80 balance future | £20 currently due; £80 remains outstanding but not due |
| Due-condition evidence unavailable | `UNRESOLVED`; ordinary payment execution not authorised |
| Raw provider `CAPTURED` but no registered interpretation | no PaymentApplication |
| £100 obligation, £40 confirmed provider payment | £40 application; £60 outstanding |
| £100 obligation, provider reports £110 | apply at most authorised amount; £10 explicit mismatch |
| Provider evidence says FAILED | evidence may be retained; no PaymentApplication |
| Same payment callback twice | one evidence/application effect |
| Payment request timeout after provider may charge | `EXECUTION_UNCERTAIN`; no blind second charge |
| Payment provider changes after old charge | old transaction reconciles against original provider context |
| Order £100 paid £100 then commitment reduced to £70 | obligation adjustment records £30 reduction; £30 excess applied; no automatic Refund |
| Refund £30 then refund retry | one £30 Refund |
| Two partial refunds £30 + £20 against £100 application | valid; £50 still refundable if business authority permits |
| Refund requests £110 against £100 application | reject |
| Refund succeeds | original PaymentApplication remains historical truth |
| Booking cancelled but refund provider fails | Booking remains cancelled; Refund unresolved/failed |
| Logged-in customer knows another customer's obligation ID | no Payment customer eligibility |
| Guest with exact Order contextual access | may access associated Payment obligation under source relationship |
| Provider settlement £97 from £100 payment | customer payment remains £100 |
| Multi-target payment returns partial aggregate with no allocation rule | evidence retained; automatic application unresolved |
| Provider unavailable for new payment but sends valid old callback | callback can still reconcile |
| Merchant uses no Payment capability | no Payment objects manufactured |

No case requires a universal Payment status, provider-owned business truth or CommercialTransaction aggregate.

---

## 70. Rejected Alternatives

Rejected:

```text
one Payment.status

provider.status copied into Payment status

dueCondition as arbitrary application string

all existing obligations counted as currently due

call provider before durable correlation exists

provider callback directly updates amountDue

provider timeout = payment failed

blind retry after uncertain charge

PaymentApplication mutates PaymentObligation

Refund reverses/deletes PaymentApplication

Refund automatically reopens amount due

Order cancellation automatically means refund

new payment provider handles old-provider refund silently

payer email/card name creates Customer relationship

settlement amount = customer payment

persistent payment-status projection by default
```

---

## 71. Deferred Scope

Remain downstream:

```text
exact PayPal/Klarna/Stripe/etc. provider selection

provider SDK/library

hosted-checkout/redirect UI

exact API DTOs/routes — Target 20

payment retry presentation UX — Target 20

specific customer receipt presentation

chargebacks/disputes

credit ledger

generic overpayment-credit model

FX

tax engines

discount/coupon/promotion engines

invoice/accounting/general ledger

bank/provider payout reconciliation

revenue recognition

return-specific refund policy — Target 15

generic provider/background implementation — Targets 18–19

exact PostgreSQL layout/indexes/locking

exact secure guest credential representation
```

---

## 72. Conformance Criteria

A conforming implementation MUST demonstrate:

```text
[ ] Payment Obligation remains independent of provider transaction

[ ] original Payment Obligation is immutable

[ ] obligation adjustments preserve history

[ ] effective obligation cannot become negative

[ ] due condition uses registered deterministic authority

[ ] DUE / NOT_DUE / UNRESOLVED remain distinguishable

[ ] future obligation does not inflate current amount due

[ ] unresolved due condition cannot authorise payment

[ ] PaymentExecutionRequest is durable before provider side effect

[ ] request retains exact historical provider/binding provenance

[ ] same logical provider request cannot multiply charge

[ ] provider timeout preserves uncertainty

[ ] provider raw status cannot directly create PaymentApplication

[ ] provider evidence survives failed automatic reconciliation

[ ] PaymentApplication respects obligation and evidence bounds

[ ] one provider evidence item may support multiple applications

[ ] one obligation may have multiple applications

[ ] ambiguous multi-obligation underpayment is not arbitrarily allocated

[ ] overpayment does not rewrite obligation

[ ] Refund is immutable later payment truth

[ ] Refund does not delete/reverse PaymentApplication

[ ] Refund does not automatically reopen amount due

[ ] cumulative Refund cannot exceed eligible applied amount

[ ] old-provider Refund/reconciliation preserves historical affinity

[ ] duplicate callback cannot duplicate Refund/application

[ ] settlement does not redefine customer payment

[ ] Payment customer eligibility derives from authoritative source relationship

[ ] provider payer identity does not create customer relationship

[ ] initial Payment reads are request-scoped

[ ] stale read never authorises Payment mutation

[ ] no universal Payment status is introduced

[ ] no business-category Payment branch is introduced
```

---

## 73. Target-13 Closure Effect

This accepted amendment closes Target 13 as follows:

```text
MonetaryAmount / commercial terms
    → existing MS-PROT-055 v1.0

Payment Obligation
    → composite MS-PROT-055 through v1.1

due evaluation
    → MS-PROT-055 v1.1

obligation adjustment
    → MS-PROT-055 v1.1

provider payment execution
    → MS-PROT-055 v1.1 + composite MS-PROT-048

provider evidence
    → composite MS-PROT-055 / MS-PROT-048

PaymentApplication
    → composite MS-PROT-055 through v1.1

Refund execution/fact
    → MS-PROT-055 v1.1

execution uncertainty
    → MS-PROT-069 consumed concretely by Payment

cross-capability progression
    → MS-PROT-072

Payment customer relationship
    → payment / related-customer-payment-obligation

initial Payment reads
    → request-scoped owner queries
```

No Target-13 material business rule remains for implementation to invent within the declared scope.

Under MS-PROT-079:

```text
Target 13 — Payment
    DESIGN-CLOSED

Target 14 — Order Fulfilment / Shipment
    CURRENT ACTIVE TARGET
```

---

## 74. Governing Principle

> **Main Street shall treat a Payment Obligation as durable provider-independent business truth and financial execution as a separately correlated external process. What is owed, what is currently due, what a provider attempted, what provider evidence establishes, what amount Main Street validly applies, what a provider settles to the merchant and what is later refunded remain distinct facts. Payment execution begins only from a durable exact request, provider evidence is recorded before reconciliation, uncertain side effects are reconciled rather than blindly repeated, and Refund creates new historical payment truth rather than rewriting the original obligation, payment or source commitment.**

---

## 75. Governance Assessment

```text
AUTHORITY TRACE:
    COMPLETE

GAP CLASSIFICATION:
    COMPLETE

DESIGN / PROPOSE:
    COMPLETE

OWNERSHIP REVIEW:
    PASS

DUE-CONDITION REVIEW:
    PASS

OBLIGATION-ADJUSTMENT REVIEW:
    PASS

PROVIDER EXECUTION REVIEW:
    PASS

UNCERTAINTY / RETRY REVIEW:
    PASS

PAYMENT APPLICATION REVIEW:
    PASS

REFUND REVIEW:
    PASS

CUSTOMER RELATIONSHIP REVIEW:
    PASS

PROJECTION / EXPOSURE REVIEW:
    PASS

BUSINESS-TYPE NEUTRALITY:
    PASS

FALSIFICATION:
    PASS

AMBIGUITY REVIEW:
    PASS within Target-13 scope

RECOMMENDATION:
    ACCEPT

MANUAL APPROVAL:
    GRANTED — 27 August 2026

STATUS:
    ACCEPTED
```

---

## 76. Acceptance Statement

**MS-PROT-055 v1.1 is ACCEPTED.**

It closes **MS-PROT-079 Target 13 — Payment** and preserves all unrelated MS-PROT-055 v1.0 authority and deferred scope except where this amendment is explicitly more precise.