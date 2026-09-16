# MS-PROT-060 v1.1 — Production Order Fulfilment, Shipment Execution & Tracking Contract Amendment

**Document ID:** MS-PROT-060  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Approved:** Manual approval on 27 August 2026 after governed authority trace, gap classification, fulfilment-quantity review, Order release/fulfilment concurrency review, Inventory consistency review, Shipment identity/fact review, provider execution/uncertainty review, redelivery/replacement-stock review, customer/projection review, falsification and ambiguity review under `DESIGN-RULES.md` v2.1  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-060 v1.0 within Order Fulfilment execution, Shipment execution/evidence, Inventory consequence, redelivery, customer tracking and production-read scope  
**Depends on:** MS-PROT-023; MS-PROT-024; MS-PROT-025; composite MS-PROT-027 through v1.5; composite MS-PROT-048 through v1.4; MS-PROT-049 v1.3; composite MS-PROT-055 through v1.1; composite MS-PROT-058 through v1.1; MS-PROT-059; MS-PROT-060 v1.0; MS-PROT-062; MS-PROT-063; MS-PROT-064; MS-PROT-065; MS-PROT-067; MS-PROT-069; MS-PROT-070; MS-PROT-072; composite MS-PROT-077 through v1.1; MS-PROT-079  
**Closes:** MS-PROT-079 Target 14 — Order Fulfilment / Shipment; Order-Fulfilment shortfall handoff from MS-PROT-058 v1.1; Fulfilment/Shipment CUSTOMER scope of `MS-PROT-027-V15-DQ-005`; Shipment-specific degraded-provider scope of `MS-PROT-048-V14-DQ-014`  
**Purpose:** Make Order Fulfilment and Shipment production-executable while preserving Order, Inventory, Payment, provider, customer and projection ownership boundaries.

---

## 1. Governing Decision

The authoritative separation remains:

```text
ORDER
    what was committed

ORDER FULFILMENT
    what committed quantity was supplied
    into its accepted fulfilment path

SHIPMENT
    physical movement attempt/evidence

INVENTORY
    stock position / claims / movements

PAYMENT
    monetary obligations / applications / refunds

PROVIDER FULFILMENT
    technical provider responsibility

PROJECTION / EXPOSURE
    audience-safe observation
```

Hard rule:

```text
Order truth
    ≠
Order Fulfilment truth
    ≠
Shipment truth
```

A convenient cross-capability `OrderStatus` remains prohibited.

---

## 2. Canonical Terminology

Because `Fulfilment` is also used by MS-PROT-048 for technical Provider Fulfilment, normative Target-14 prose SHALL use:

```text
Order Fulfilment
```

for satisfaction of Order commitments.

The existing Provider Fulfilment terminology remains unchanged.

---

## 3. Order Fulfilment Identity

An **Order Fulfilment** is a merchant-scoped durable Operational Object owned by the Order Fulfilment semantic boundary.

Conceptually:

```text
OrderFulfilmentIdentity
{
    MerchantScope
    fulfilmentIdentifier
}
```

Each Order Fulfilment belongs to:

```text
exactly 1 Order
```

Each Order may have:

```text
0..n Order Fulfilments
```

Attribute equality does not imply identity.

Two otherwise identical fulfilment occurrences may legitimately be distinct.

---

## 4. Order Fulfilment Satisfaction Portion

An Order Fulfilment contains:

```text
1..n Order Fulfilment Satisfaction Portions
```

Each satisfaction portion retains at least:

```text
satisfaction identity
Order Commitment Portion identity
satisfied quantity + unit semantics
actual supplied subject reference
accepted substitution relationship/provenance where applicable
fulfilment-method semantics
satisfaction timestamp
historical Order/configuration provenance
```

The satisfaction identity is durable within its Order Fulfilment.

---

## 5. Quantity Cardinality

One Order Commitment Portion may be satisfied through:

```text
0..n Order Fulfilment Satisfaction Portions
```

possibly across multiple Order Fulfilments.

For each Order Commitment Portion:

```text
sum(authoritative satisfied quantity)
<=
committed quantity
-
Ordering-owned released quantity
```

The current admissible quantity is therefore the current **remaining Order commitment** owned by composite MS-PROT-077.

---

## 6. Fulfilment Does Not Rewrite Order

Successful fulfilment does not mutate the original Order Commitment Portion.

Correct:

```text
Order Portion
    committed = 10

Fulfilment F1
    satisfied = 3

Fulfilment F2
    satisfied = 4

remaining commitment = 3
```

Rejected:

```text
UPDATE OrderPortion
SET quantity = 3
```

to represent fulfilment.

---

## 7. Actual Supplied Subject

Where:

```text
actual supplied subject
=
committed subject
```

the normal reference is retained.

Where an accepted merchant policy permits substitution:

```text
committed subject A
actual supplied subject B
```

Order Fulfilment MUST preserve both.

It MUST NOT rewrite the Order so that B appears to have been the originally committed subject.

---

## 8. Fulfilment Satisfaction Anchor

An Order Fulfilment Satisfaction Portion becomes authoritative only when the applicable registered fulfilment method's **Satisfaction Anchor** has occurred.

The initial semantic families are:

```text
COLLECTION_HANDOVER

OUTBOUND_MOVEMENT_HANDOVER
```

They are Main Street registered semantics, not merchant-authored executable code.

---

## 9. Collection Satisfaction

For collection:

```text
Satisfaction Anchor
=
physical handover of the supplied goods
to the customer or authorised collector
```

Collection creates:

```text
Order Fulfilment
```

and requires:

```text
Shipment = none
```

Merely marking an order:

```text
ready for collection
```

is not Order Fulfilment satisfaction.

It may become operational/projection information later.

---

## 10. Physical-Movement Satisfaction

For merchant delivery or external-carrier delivery:

```text
Satisfaction Anchor
=
physical handover of the supplied goods
into the accepted outbound movement
```

Examples include:

```text
merchant delivery leaves the dispatch point

goods handed to external carrier

goods otherwise enter the accepted
physical movement path
```

Terminal delivery to the customer occurs later under Shipment truth.

Therefore:

```text
Shipment later lost
    ≠ erase Order Fulfilment

Shipment later fails
    ≠ erase Order Fulfilment
```

This makes the existing MS-PROT-060 lost-shipment invariant executable.

---

## 11. Split Physical Dispatch

If quantities leave merchant control at materially different times:

```text
dispatch occurrence 1
dispatch occurrence 2
```

they MAY form separate Order Fulfilments.

Where one satisfaction occurrence is legitimately divided across multiple independently tracked movement attempts, one Order Fulfilment MAY reference multiple Shipments.

The satisfaction facts—not Shipment count—own fulfilled quantity.

---

## 12. `order-fulfilment.satisfy`

Canonical operation:

```text
order-fulfilment.satisfy
```

**Owner:** Order Fulfilment.

---

## 13. Principal Requirements

The operation requires:

```text
current Trusted Execution Context
+
applicable Actor Authorisation
```

according to MS-PROT-063/MS-PROT-062.

Customer ability to observe an Order does not authorise this merchant-side fulfilment operation.

---

## 14. Semantic Applicability

The exact active semantic/configuration context must establish that:

```text
Order Fulfilment applies
+
requested fulfilment method is supported
+
method semantics match the committed Order terms
```

Current merchant configuration MUST NOT silently reinterpret historical committed fulfilment terms.

---

## 15. Inputs

At minimum:

```text
MerchantScope
logical command identity
Order Fulfilment identity
Order identity

1..n satisfaction instructions:
    Order Commitment Portion identity
    quantity + unit
    actual supplied subject reference

applicable committed fulfilment-method reference
applicable historical destination/reference
where movement applies

Shipment preparation / movement references
where required

governing execution context
```

Client input is intent/evidence, not commitment authority.

---

## 16. Authoritative Reads

Immediately before commit the operation MUST establish:

```text
exact Order
current Order amendments/releases
already satisfied Order quantities
current remaining Order commitment
historically applicable fulfilment terms
applicable merchant fulfilment policy
principal authority

applicable Payment facts
    only where committed policy requires them

applicable Inventory Claims
    where stock protection exists

applicable prepared external Shipment evidence
    where the selected movement path requires it
```

---

## 17. Payment Prerequisite

There is no universal rule:

```text
paid
    → fulfil
```

or:

```text
not fully paid
    → cannot fulfil
```

Instead:

```text
historically applicable merchant policy
        ↓
required Payment predicate, if any
```

governs.

Thus accepted models may support:

```text
payment-before-fulfilment
invoice/pay-later
cash on delivery
```

without changing Order Fulfilment ownership.

Payment evaluates Payment facts.

Order Fulfilment consumes that result.

---

## 18. Stock-Protected Satisfaction

Where an Order Commitment Portion is protected by Inventory Claims, successful satisfaction MUST consume the corresponding Inventory quantity through:

```text
inventory.claim.fulfil
```

under composite MS-PROT-058.

Order Fulfilment MUST NOT change stock counters directly.

---

## 19. Atomic Satisfaction Boundary

Where Inventory consumption is required for the supplied quantity:

```text
Order Fulfilment Satisfaction
+
required Inventory Claim Resolution(s)
+
required Inventory Movement(s)
```

MUST form one local authoritative consistency decision.

Therefore Main Street MUST NOT commit:

```text
Fulfilment says 3 supplied

but

Inventory Claim still reserves those 3
```

or:

```text
Inventory consumed 3

but

no corresponding Order Fulfilment fact exists
```

when both facts arise from the same Main Street operation.

---

## 20. Multiple Inventory Claims

The existing rule survives:

```text
1 Order Commitment Portion
    → 0..n Inventory Claims
```

A bundle or composite subject may therefore require multiple Inventory consequences for one Order Fulfilment Satisfaction Portion.

Order Fulfilment does not assume:

```text
1 satisfaction = 1 claim
```

---

## 21. Fulfilment and Order Release Concurrency

The following must be impossible:

```text
remaining quantity = 1

Transaction A
    fulfil 1

Transaction B
    release 1

both commit
```

`order-fulfilment.satisfy` and Ordering release/amendment SHALL preserve one authoritative serialization/conflict boundary over the same remaining Order commitment.

Technology remains implementation scope:

```text
locking
optimistic versioning
CAS
equivalent
```

The semantic result does not.

---

## 22. Inventory Shortfall

Suppose:

```text
Order quantity protected = 1

Inventory claim remaining = 1

later physical correction:
stock-on-hand = 0
```

Then normal:

```text
order-fulfilment.satisfy
```

cannot satisfy the stock-protected quantity because `inventory.claim.fulfil` cannot validly commit.

Result:

```text
INVENTORY_FULFILMENT_CONFLICT
```

No Order Fulfilment Satisfaction Portion commits.

No Inventory fact is fabricated.

---

## 23. Already-Occurred Physical Action

If physical handover occurred outside the successful Main Street operation before such a conflict was discovered:

```text
physical-world fact exists
+
Main Street Inventory cannot reconcile normal fulfilment
```

Main Street MUST NOT:

```text
invent positive stock
drive stock negative
silently release claim
erase physical evidence
blindly retry the normal command
```

The case enters bounded operational reconciliation/intervention under MS-PROT-064 and Target 19.

Target 14 establishes the invariant; Target 19 supplies the generic operator/reconciliation machinery.

---

## 24. Shipment Identity

A **Shipment** is a merchant-scoped durable physical-movement attempt associated with exactly one Order Fulfilment in the initial production model.

Conceptually:

```text
ShipmentIdentity
{
    MerchantScope
    shipmentIdentifier
}
```

Initial cardinality:

```text
Order Fulfilment
    → 0..n Shipments

Shipment
    → exactly 1 Order Fulfilment
```

Multi-Order Shipment consolidation remains deferred.

---

## 25. Shipment Is Not Fulfilment Quantity

A Shipment may reference one or more exact Order Fulfilment Satisfaction Portions.

Those references identify what the movement concerns.

They do not create or increase fulfilled quantity.

Therefore:

```text
same Fulfilment
+
Shipment S1
+
Shipment S2
```

does not double Order Fulfilment.

---

## 26. Shipment Minimum Truth

A Shipment retains at least:

```text
Shipment identity
MerchantScope
Order Fulfilment identity
relevant satisfaction-scope references
committed/selected movement method
destination provenance
dispatch-origin provenance where required internally
historical provider/binding provenance where applicable
dispatch fact where established
provider/tracking correlation where applicable
terminal outcome fact where established
createdAt
```

Internal origin/provider data is not automatically customer-exposable.

---

## 27. No Universal Shipment Status Machine

Rejected:

```text
CREATED
→ PACKED
→ DISPATCHED
→ IN_TRANSIT
→ OUT_FOR_DELIVERY
→ DELIVERED
```

as universal Shipment authority.

Initial authoritative Shipment meaning is composed from facts.

---

## 28. Shipment Dispatch Fact

A Shipment may contain at most one authoritative initial dispatch fact.

It establishes:

```text
goods entered this exact physical movement attempt
```

and retains:

```text
dispatch timestamp
dispatch provenance
method
applicable provider correlation
```

Shipment existence without dispatch does **not** mean goods moved.

---

## 29. Shipment Terminal Outcome

Initial platform-owned terminal outcome vocabulary is deliberately small:

```text
DELIVERED

NOT_DELIVERED
```

A provider-specific reason such as:

```text
lost
refused
address problem
damaged
```

may be preserved through registered evidence/reason provenance without creating carrier-specific lifecycle states.

`NOT_DELIVERED` is recorded only where authoritative evidence establishes a terminal outcome for that Shipment attempt.

A temporary provider retry state is not `NOT_DELIVERED`.

---

## 30. Shipment Outcome Correction

A later authoritative correction MUST preserve the previous outcome evidence.

It MUST NOT destructively overwrite history.

Conceptually:

```text
ShipmentOutcomeFact O1
    NOT_DELIVERED

ShipmentOutcomeCorrection O2
    supersedes O1
    DELIVERED
```

The physical representation remains implementation scope.

A delayed/out-of-order provider callback therefore cannot blindly regress current authoritative Shipment truth.

---

## 31. External Provider Is Optional

Collection requires no Shipment.

Merchant self-delivery requires no external carrier.

Therefore:

```text
Order Fulfilment
    ≠ requires ProviderConnection
```

Provider Readiness participates only when the selected fulfilment method requires an external provider obligation.

---

## 32. Shipment Preparation Request

A **Shipment Preparation Request** is a durable progression identity for one logical external-provider request required to prepare a future physical Shipment.

It exists before provider side effects.

It is not:

```text
Shipment
Order Fulfilment
Order commitment
provider transaction
```

---

## 33. Minimum Shipment Preparation Request

Conceptually:

```text
ShipmentPreparationRequest
{
    requestIdentity
    MerchantScope

    Order identity
    intended Order Commitment Portion scope
    quantity/scope references

    committed destination provenance
    movement-method reference

    governing Fulfilment Binding
    ProviderConnection where required

    stable provider-correlation identity
    logical command identity
    acceptedAt
}
```

One external provider request may prepare at most one initial Shipment attempt unless a separately accepted provider contract explicitly establishes another cardinality.

---

## 34. Provider Fulfilment Role

Target 14 concretises the capability responsibility:

```text
shipment / shipment-preparation
```

for provider-dependent Shipment preparation.

Its obligations include:

```text
prepare the exact intended movement attempt

preserve Main Street correlation/idempotency

return or permit retrieval of authenticated
provider evidence

preserve tracking/provider reference
where supplied

support uncertainty reconciliation
where the provider contract permits
```

This is **Provider Fulfilment**, not Order Fulfilment.

---

## 35. `shipment.prepare`

For a provider-dependent movement, the operation MUST establish before external side effect:

```text
exact Shipment Preparation Request
+
selected historical/current binding as applicable
+
provider/connection provenance
+
stable correlation/idempotency identity
```

Then—and only then—may the external provider call occur.

No provider call participates in the local database transaction.

---

## 36. Provider Readiness

For a **new** provider-dependent Shipment Preparation Request:

```text
READY
    → may attempt

DEGRADED
    → may attempt only where the registered
      shipment/provider contract explicitly
      permits the exact obligation safely

NOT_READY
    → do not initiate

UNKNOWN
    → do not initiate
```

This closes the Shipment-specific scope of `MS-PROT-048-V14-DQ-014`.

Provider Readiness still does not predict execution success. Existing MS-PROT-048 already requires readiness to be revalidated at provider-dependent execution and distinguishes callbacks/reconciliation from new activity.

---

## 37. Provider Timeout

Suppose:

```text
Shipment Preparation Request committed
provider call sent
network timeout
```

Main Street cannot infer:

```text
provider did nothing
```

The request enters:

```text
EXECUTION_UNCERTAIN
```

under MS-PROT-069.

No blind duplicate provider request is permitted unless retry safety is independently established.

---

## 38. Provider Switch

If preparation used:

```text
Provider A
Connection C1
Binding B1
```

and the merchant later changes provider:

```text
future movement
    → may use new binding

existing uncertain/prepared movement
    → retains A / C1 / B1 affinity
```

Main Street MUST NOT silently reinterpret the old Shipment preparation through the new provider.

---

## 39. ProviderShipmentEvidence

Provider Shipment Evidence is immutable external evidence concerning:

```text
Shipment Preparation Request
or
existing Shipment
```

It may include:

```text
provider reference
tracking reference
provider result/event category
provider timestamp
evidence timestamp
correlation
```

Raw provider payload is not Shipment truth.

---

## 40. `shipment.provider-evidence.record`

Before evidence is accepted:

```text
provider/source authentication
+
integrity validation
+
expected provider/connection context
+
MerchantScope
+
exact correlation
```

MUST succeed.

Successful recording creates provider evidence only.

It does not itself:

```text
create Order Fulfilment
create Shipment
mark dispatch
mark delivered
refund Payment
change Inventory
```

---

## 41. Provider Interpretation

Provider evidence passes through:

```text
provider evidence
    ↓
registered provider interpretation
    ↓
Shipment-owned validation
    ↓
Shipment-owned fact
```

Never:

```text
provider JSON
    ↓
Shipment database status
```

This directly consumes MS-PROT-048's existing callback/evidence boundary.

---

## 42. Movement-Based Fulfilment

Where the Satisfaction Anchor is:

```text
OUTBOUND_MOVEMENT_HANDOVER
```

successful `order-fulfilment.satisfy` SHALL establish, within its local consistency boundary:

```text
Order Fulfilment Satisfaction
+
required Inventory consumption
+
1..n Shipment records as applicable
+
their dispatch facts
```

No external provider call occurs inside that transaction.

Any required external provider preparation must already have sufficient correlated evidence.

---

## 43. Collection

For:

```text
COLLECTION_HANDOVER
```

success establishes:

```text
Order Fulfilment Satisfaction
+
required Inventory consumption
```

with:

```text
Shipment count = 0
```

Shipment is not fabricated merely for uniformity.

---

## 44. New Carrier Delivery Attempt vs Provider Internal Retry

A carrier retry within the same provider-side physical movement MAY remain evidence concerning the same Shipment.

Main Street MUST NOT manufacture a new Shipment for every provider tracking event.

A **new Shipment** is created only when Main Street/merchant initiates a materially new physical movement attempt.

---

## 45. `shipment.redispatch`

Canonical operation:

```text
shipment.redispatch
```

It creates a new physical movement attempt associated with an already-existing Order Fulfilment Satisfaction scope.

It does:

```text
new Shipment
```

but does **not** create:

```text
new Order Fulfilment quantity
```

---

## 46. Same Physical Goods

Where the new movement continues the same physical goods and those goods have not re-entered merchant Inventory:

```text
additional Inventory consumption = none
```

Example:

```text
carrier movement rerouted / reinitiated
without goods returning to merchant stock
```

The new Shipment references the existing fulfilled scope.

---

## 47. Replacement Goods

Where the merchant dispatches additional physical goods from merchant stock because the first Shipment failed/lost:

```text
new Order Fulfilment quantity = 0

but

new physical stock consumption > 0
```

Therefore the replacement movement MUST obtain an Inventory-owned stock-protection/use path.

Canonical:

```text
replacement Shipment intent
        ↓
new Inventory Claim where stock protection is required
    use reference = replacement Shipment
        ↓
Shipment dispatch
+
inventory.claim.fulfil
```

within the required local consistency boundary.

The replacement claim is not a new Order commitment.

---

## 48. Why Replacement Stock Matters

Rejected:

```text
Order already fulfilled
    ↓
send replacement
    ↓
do not change Inventory
```

because a second physical item left merchant stock.

Also rejected:

```text
send replacement
    ↓
increase Order Fulfilment quantity again
```

because the same customer commitment was not doubled.

The accepted result is:

```text
Fulfilment quantity unchanged
Shipment attempts increase
Inventory consumption reflects actual additional goods
```

---

## 49. Returned-to-Sender Goods

If goods return into merchant control, their sellability/re-entry/disposition remains Inventory/Target-15 scope.

Target 14 MUST NOT automatically increment sellable stock merely because carrier evidence says:

```text
returned to sender
```

---

## 50. `shipment.outcome.record`

Canonical operation:

```text
shipment.outcome.record
```

**Owner:** Shipment authority within the MS-PROT-060 boundary.

Inputs include:

```text
MerchantScope
logical command identity
Shipment identity
DELIVERED | NOT_DELIVERED
authoritative evidence/provenance
occurredAt
optional registered reason
```

Evidence may originate from:

```text
authorised merchant operation
or
accepted interpreted provider evidence
```

---

## 51. Outcome Preconditions

The operation MUST establish:

```text
Shipment exists
Shipment dispatched
evidence applies to exact Shipment
evidence is authoritative enough for
the requested terminal outcome
current outcome/revision basis
```

Provider status text alone is insufficient.

---

## 52. Terminal Outcome Effects

`DELIVERED` does not:

```text
create additional Order Fulfilment
create Payment
```

`NOT_DELIVERED` does not:

```text
release Order
Refund Payment
restore Inventory
create new Order Fulfilment
create replacement Shipment
```

Those consequences remain separately authorised.

This preserves existing MS-PROT-060 authority.

---

## 53. Order Fulfilment Retry

For:

```text
order-fulfilment.satisfy
```

the same logical command plus same intent returns the prior committed result.

It MUST NOT duplicate:

```text
Order Fulfilment
Satisfaction Portions
Inventory claim resolutions
Inventory movements
Shipment
dispatch facts
events
```

Same command identity with materially different intent is a conflict.

---

## 54. Shipment Preparation Retry

The same logical Shipment Preparation Request uses one stable request identity and provider idempotency identity where supported.

Provider uncertainty blocks unsafe new attempts.

---

## 55. Provider Callback Duplication

Duplicate or replayed provider callbacks MUST NOT multiply:

```text
ProviderShipmentEvidence
Shipment
dispatch facts
terminal outcomes
Order Fulfilment quantity
Inventory movement
Notification consequences
```

---

## 56. Outcome Concurrency

Conflicting terminal outcome attempts against the same Shipment must serialise or conflict.

A later correction must be explicit and preserve prior evidence.

---

## 57. Fulfilment Terms

Existing Orders retain enough historical affinity to determine:

```text
selected fulfilment method
committed destination
applicable charges
applicable payment prerequisite
applicable substitution policy
other commitment-affecting fulfilment terms
```

A later merchant configuration change does not silently rewrite an existing Order's fulfilment meaning.

---

## 58. Saved Customer Address

A later change to a CustomerContext address does not alter an existing Order/Shipment destination.

Current saved address:

```text
≠ committed delivery destination
```

MS-PROT-060 v1.0 already establishes this boundary.

---

## 59. No New Fulfilment Customer Relationship

Target 14 SHALL NOT introduce:

```text
order-fulfilment / related-customer
```

or:

```text
shipment / related-customer
```

because that would duplicate existing Order relationship truth.

Every Order Fulfilment belongs to exactly one Order.

Every initial Shipment belongs to exactly one Order Fulfilment.

Therefore Customer relationship evaluation delegates to:

```text
ordering / related-customer-order
```

from MS-PROT-077 v1.1.

---

## 60. Guest Tracking

The same rule applies to guest contextual access.

If exact trusted contextual access satisfies:

```text
ordering / related-customer-order
```

for Order O1, it may satisfy the relationship prerequisite for authorised Fulfilment/Shipment observations belonging to O1.

It does not expose:

```text
another Order
another Shipment
another merchant
```

and does not create CustomerAccount.

---

## 61. Customer Exposure Contract Portfolio

Target 14 registers CUSTOMER-observable candidate element families for:

```text
order-fulfilment / satisfied-quantity
order-fulfilment / fulfilled-at
order-fulfilment / fulfilment-method

shipment / dispatched-at
shipment / tracking-reference
shipment / terminal-outcome
```

Each contract requires, at minimum:

```text
ordering / related-customer-order
```

plus applicable:

```text
Projection Serviceability
data-protection/security restrictions
exact element availability
```

before Exposure may return `EXPOSE`.

---

## 62. Internal Elements

The following do not automatically receive CUSTOMER Exposure:

```text
private dispatch origin
Inventory scope
Inventory Claim identifiers
ProviderConnection identity
carrier account identifiers
provider raw payload
internal notes
audit evidence
semantic/configuration internal identifiers
```

Relationship satisfaction does not expose them.

---

## 63. No PUBLIC Tracking Authority

Target 14 registers no anonymous PUBLIC Shipment tracking merely because a tracking identifier exists.

Public access requires separately accepted contextual/security semantics.

Guest contextual access remains narrow CUSTOMER-context authority rather than unrestricted PUBLIC Exposure.

---

## 64. Initial Read Architecture

Initial production reads use:

```text
Ordering owner query
+
Order Fulfilment owner query
+
Shipment owner query
+
stored accepted provider evidence where required
+
request-scoped composition
```

No initial requirement exists for:

```text
Redis tracking cache
persistent OrderProgress projection
generic ShipmentStatus table
async customer-tracking projection
```

A future materialised/cached tracking view must first satisfy MS-PROT-027's Projection Contract triggers.

---

## 65. Stale Read Is Never Mutation Authority

A customer or merchant view showing:

```text
2 remaining
```

or:

```text
Shipment in progress
```

cannot authorise a mutation.

Fulfilment, redelivery and outcome operations always re-enter current authoritative state.

---

## 66. Existing Shipment Survival

Later provider degradation/disconnection does not erase:

```text
Order Fulfilment
Shipment
dispatch evidence
provider evidence already recorded
```

Existing callback/reconciliation may continue under the applicable historical/residual provider contract even when new Shipment preparation is unavailable.

---

## 67. Order Fulfilment Event

Successful `order-fulfilment.satisfy` SHALL establish a post-commit Domain Event:

```text
order-fulfilment.satisfied
```

The already-true fact is:

> The identified satisfaction quantities were authoritatively recorded against the identified Order commitment.

Minimum references:

```text
MerchantScope
Order identity
Order Fulfilment identity
satisfaction identities
command/causation identity
occurredAt
```

Consumer failure cannot roll back fulfilment.

---

## 68. Shipment Dispatch Event

Committed dispatch establishes:

```text
shipment.dispatched
```

The event means only that the identified goods entered the identified physical movement attempt.

It does not mean:

```text
customer received goods
Shipment delivered
payment completed
notification delivered
```

---

## 69. Shipment Terminal Outcome Event

Successful terminal outcome mutation establishes:

```text
shipment.terminal-outcome-recorded
```

with:

```text
Shipment identity
outcome identity
DELIVERED | NOT_DELIVERED
causation/evidence reference
occurredAt
```

A consumer failure cannot change the already-committed Shipment outcome.

Target 18 later closes generic event/background-delivery machinery.

---

## 70. Notification Boundary

Notifications may react to:

```text
order-fulfilment.satisfied
shipment.dispatched
shipment.terminal-outcome-recorded
```

but Notification failure does not change the underlying facts.

A tracking email is not Shipment truth.

---

## 71. AI Boundary

AI MAY:

```text
interpret merchant fulfilment intent
prepare a candidate command
explain Shipment evidence
summarise customer-safe tracking
```

AI MUST NOT:

```text
invent fulfilled quantity
declare goods dispatched from confidence
declare provider evidence delivered
manufacture customer relationship
release Inventory Claim
invent replacement-stock consumption
override merchant policy
```

---

## 72. Existing Commitments

Loss of commercial access to create **new** Orders MUST NOT by itself erase existing Order/Fulfilment/Shipment truth.

Where accepted residual authority requires an outstanding Order commitment to remain resolvable, the bounded fulfilment/shipment operations required to resolve that commitment remain subject to that residual authority.

This does not grant:

```text
new Order creation
new unrelated commerce activity
new provider path
```

and does not bypass Actor Authorisation or provider readiness.

---

## 73. Required Failure Distinctions

Target-14 operations preserve at least:

```text
VALIDATION_REJECTION

AUTHORISATION_REJECTION

ENTITLEMENT_REJECTION

NO_REMAINING_ORDER_COMMITMENT

FULFILMENT_POLICY_REJECTION

PAYMENT_PREREQUISITE_UNSATISFIED

PAYMENT_PREREQUISITE_UNRESOLVED

INVENTORY_FULFILMENT_CONFLICT

SHIPMENT_PREPARATION_REQUIRED

PROVIDER_NOT_READY

PROVIDER_BUSINESS_REJECTION

SHIPMENT_OUTCOME_CONFLICT

AUTHORITATIVE_CONFLICT

PROVIDER / TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

Exact Java exception/HTTP representation remains downstream.

---

## 74. Target-14 Falsification Record

| Scenario | Required result |
|---|---|
| Order quantity 10, fulfil 3 | Satisfaction 3; remaining 7 |
| Fulfil 3 then release 7 | both histories survive correctly |
| Concurrent fulfil 1 / release 1 against final quantity | at most one succeeds |
| Fulfil stocked quantity 3 | Fulfilment + claim resolution + stock movement atomic |
| Bundle satisfaction needs 3 Inventory claims | all required claim effects governed without 1:1 assumption |
| Inventory corrected below claim before fulfilment | normal fulfilment rejects; no negative stock |
| Physical handover occurred outside failed transaction | operational reconciliation; no fabricated stock |
| Collection | Fulfilment, no Shipment |
| Merchant self-delivery | Shipment permitted with no external provider |
| External carrier unavailable before new preparation | no new provider side effect |
| Carrier readiness DEGRADED but exact provider contract permits safe operation | may proceed |
| Carrier readiness UNKNOWN | fail closed for new preparation |
| Carrier request timeout after possible provider acceptance | uncertain; no blind duplicate |
| Carrier callback arrives while connection not ready for new work | authenticated existing callback may reconcile |
| Duplicate carrier callback | no duplicate business effect |
| Carrier sends `OUT_FOR_DELIVERY` | provider evidence/projection, not new lifecycle state |
| Carrier says temporary delivery failed | not automatically terminal `NOT_DELIVERED` |
| Terminal loss | `NOT_DELIVERED`; no auto refund/cancel/restock |
| Later corrected authoritative evidence says delivered | explicit outcome correction, old evidence survives |
| Same parcel carrier retry | may remain same Shipment |
| Merchant starts materially new redelivery | new Shipment |
| Replacement item taken from merchant stock | extra Inventory consumption, no extra Fulfilment quantity |
| Same already-consumed goods continue movement | no duplicate Inventory consumption |
| Two Shipments transport same fulfilled quantity | no double-count Fulfilment |
| Customer changes saved address | existing Shipment destination unchanged |
| Merchant changes carrier | historical Shipment retains old provider/binding affinity |
| Cash on delivery | fulfilment may proceed if committed policy permits |
| Payment-before-dispatch policy unsatisfied | fulfilment rejected by applicable policy |
| Logged-in unrelated customer knows Shipment ID | no tracking eligibility |
| Guest exact Order context | may see permitted Fulfilment/Shipment elements |
| Tracking reference alone | no customer relationship |
| Provider status says delivered | cannot directly mutate Shipment |
| Shipment lost | cannot automatically create Refund or Inventory restoration |

The design survives without a universal logistics aggregate, generic `OrderStatus`, mandatory carrier, mandatory Package object or business-category branch.

---

## 75. Rejected Alternatives

Rejected:

```text
Order.fulfilled boolean

OrderStatus = SHIPPED / DELIVERED as authority

Shipment == Order Fulfilment

carrier lifecycle = Main Street lifecycle

provider callback directly mutates Shipment

Shipment delivery automatically creates Fulfilment quantity

Shipment failure automatically releases Order

Shipment failure automatically refunds Payment

Shipment failure automatically restores Inventory

replacement dispatch does not consume stock

replacement dispatch increases Order Fulfilment quantity again

every carrier retry = new Shipment

collection creates fake Shipment

all Fulfilment requires provider

current merchant delivery policy rewrites old Order terms

new fulfilment-specific customer identity

tracking reference possession = access authority

persistent tracking projection by default
```

---

## 76. Deferred Scope

Still deferred:

```text
Returns / reverse fulfilment — Target 15

customer self-service return processing

return-to-stock/disposition completion — Target 15

live carrier-rate quoting

package-level logistics

warehouse management

route optimisation

advanced multi-Order Shipment consolidation

delivery-slot optimisation

customs/import management

carrier-specific workflow vocabularies

exact carrier provider/SDK

exact provider retry/backoff

exact Shipment preparation adapter shapes

exact SQL/jOOQ representation

exact REST/API representations — Target 20

tracking UI wording/layout — Target 20

generic background-work machinery — Target 18

generic reconciliation/operator tooling — Target 19

exact data-retention periods — Target 17
```

None permits later implementation to collapse Order Fulfilment and Shipment authority.

---

## 77. Conformance Criteria

A conforming Target-14 implementation MUST prove:

```text
[ ] Order Fulfilment has merchant-scoped durable identity

[ ] each Order Fulfilment belongs to exactly one Order

[ ] satisfaction is quantity-bearing and portion-specific

[ ] original Order commitment is not destructively rewritten

[ ] collection handover creates no Shipment

[ ] outbound-movement satisfaction uses an explicit dispatch anchor

[ ] fulfilled quantity cannot exceed current remaining commitment

[ ] release and fulfilment cannot both consume same remaining quantity

[ ] required Inventory claim fulfilment is atomic with
    Order Fulfilment satisfaction

[ ] non-stock Order fulfilment creates no fake Inventory

[ ] Inventory shortfall cannot drive stock negative

[ ] Shipment has independent identity/facts

[ ] Shipment does not own Order Fulfilment quantity

[ ] Shipment existence does not imply dispatch

[ ] no universal carrier-inspired Shipment state machine exists

[ ] external provider side effect has durable correlation before call

[ ] UNKNOWN provider readiness cannot initiate new provider-dependent work

[ ] provider timeout cannot be treated as definite failure

[ ] callback evidence is authenticated/correlated/interpreted

[ ] raw carrier status cannot directly mutate Shipment

[ ] terminal Shipment failure cannot cancel/refund/restock automatically

[ ] redelivery cannot double-count Order Fulfilment

[ ] replacement stock consumption remains real Inventory truth

[ ] same-goods movement retry does not duplicate Inventory consumption

[ ] historical destination and provider affinity survive later configuration changes

[ ] Fulfilment/Shipment customer tracking reuses
    ordering / related-customer-order

[ ] tracking identifiers/contact similarity do not manufacture access

[ ] initial tracking reads remain request-scoped

[ ] stale tracking data never becomes mutation authority

[ ] provider, Inventory, Payment, Ordering and Shipment ownership remain distinct
```

---

## 78. Target-14 Closure Effect

This amendment closes Target 14 as follows:

```text
Order commitment / remaining commitment
    → composite MS-PROT-077

Inventory claim and physical stock movement
    → composite MS-PROT-058

Payment prerequisite facts
    → composite MS-PROT-055

Provider readiness / binding
    → composite MS-PROT-048

Order Fulfilment identity / satisfaction
    → composite MS-PROT-060 through v1.1

Shipment identity / movement facts
    → composite MS-PROT-060 through v1.1

external Shipment preparation
    → MS-PROT-060 v1.1 + MS-PROT-048/MS-PROT-069

redelivery / replacement-stock boundary
    → MS-PROT-060 v1.1 + MS-PROT-058

customer relationship
    → ordering / related-customer-order

initial tracking reads
    → request-scoped owner composition
```

No Target-14 material business rule remains for implementation to invent.

Under MS-PROT-079:

```text
Target 14 — Order Fulfilment / Shipment
    DESIGN-CLOSED

Target 15 — Returns
    CURRENT ACTIVE TARGET
```

---

## 79. Governing Principle

> **Main Street shall record Order Fulfilment as immutable quantity-bearing evidence that exact Order commitment portions entered their accepted satisfaction path, while Shipment independently records each physical movement attempt. Fulfilment never rewrites the Order; Shipment never creates fulfilled quantity. Stock-protected fulfilment consumes Inventory through the Inventory authority within the required local consistency boundary. External logistics begins only from durable, correlated provider preparation and uncertain side effects are reconciled rather than blindly repeated. Shipment failure does not automatically cancel, refund or restore stock, and a replacement movement consumes any additional physical stock without falsely satisfying the Order twice. Customer tracking derives from exact Order relationship authority and request-scoped Order/Fulfilment/Shipment truth rather than identifiers, provider status or a second tracking authority.**
