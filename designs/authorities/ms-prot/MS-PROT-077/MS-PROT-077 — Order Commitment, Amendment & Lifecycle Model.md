# MS-PROT-077 — Order Commitment, Amendment & Lifecycle Model

**Document ID:** MS-PROT-077  
**Version:** 1.0  
**Status:** **ACCEPTED after proposal, graph review, falsification, cross-domain validation and manual approval**  
**Depends on:** MS-PROT-020 v1.5, MS-PROT-023 v1.1, MS-PROT-025 v1.1, MS-PROT-026, MS-PROT-027 v1.3, MS-PROT-040 v1.1, MS-PROT-043 v1.3, MS-PROT-044 v1.1, MS-PROT-054, MS-PROT-055, MS-PROT-058, MS-PROT-059, MS-PROT-060, MS-PROT-061, MS-PROT-062, MS-PROT-069, MS-PROT-072  
**Closes:** Promoted Order / Ordering semantic authority gap  
**Review evidence:** `docs/development/order-authority-design-review-falsification-2026-08-26.md`  
**Approved:** Manual approval on 26 August 2026 to formalise and implement the reviewed recommendation  
**Purpose:** Define Order as a capability-owned purchase/order commitment, establish Ordering ownership, historical commitment portions, commit/amend/release semantics and the boundaries with Inventory, Money/Payment, Order Fulfilment, Shipment, Customer context and projections without creating a universal Business Order, retail-specific hierarchy or cross-capability status aggregate.

---

## 1. Governing Decision

> **An Order is a merchant-scoped Operational Object representing an accepted purchase/order commitment to provide one or more committed subjects under committed terms. Ordering owns what was ordered and the continuing Order-side commitment truth; neighbouring capabilities retain their own independent facts.**

Canonical separation:

```text
OFFERING / PRODUCT
    current proposition / subject semantics

ORDERING
    accepted purchase/order commitment
    committed subjects / quantities / terms
    Order amendment / release truth

INVENTORY
    stock position / claims / movements / availability

MONEY / PAYMENT
    Payment Obligations / provider evidence / applications / refunds

ORDER FULFILMENT
    what authoritative Order commitment portions were satisfied

SHIPMENT
    physical movement evidence

CUSTOMER CONTEXT
    merchant-customer relationship / identity-access context

PROJECTION / EXPOSURE
    derived observation and presentation
```

Reference, orchestration, causation and physical database co-location do not transfer semantic ownership.

---

## 2. Problem and Scope

Accepted authorities already require an Order owner. MS-PROT-058 states that Order owns Order truth, committed quantities/terms, Order lifecycle and Order-specific policy, while MS-PROT-060 begins downstream with satisfaction of authoritative Order commitment portions. MS-PROT-055 requires committed commercial truth to survive later changes to current terms. MS-PROT-059 requires Online/POS/phone channels to converge on one Order authority.

This authority closes that upstream semantic boundary.

It governs:

- Order identity and merchant scope;
- purchase/order commitment establishment;
- durable Order commitment portions;
- committed quantity and commercial-term provenance;
- historical semantic/configuration affinity;
- Order-side amendment and release/cancellation truth;
- cross-capability Inventory/Payment/Fulfilment boundaries;
- atomicity where Inventory Claim is a commitment invariant;
- retry/idempotency/lost-acknowledgement behaviour;
- concurrency and conflict handling;
- guest/customer-context optionality;
- projection/Exposure consequences; and
- the narrow supersession of ADR-005's universal Business Order proposition.

---

## 3. Explicit Non-Goals

This authority does **not** define:

- Booking or Appointment semantics;
- Enquiry or Publication semantics;
- one universal customer-work object;
- Product/Offering/Variant semantics;
- Inventory stock truth or stock-movement semantics;
- payment-provider transaction semantics;
- Refund semantics;
- Order Fulfilment or Shipment truth;
- detailed return/reverse-fulfilment workflow;
- checkout page/UI structure;
- shopping-basket persistence as a mandatory domain object;
- one universal Order status machine;
- one payment-before-commit policy;
- one customer-account requirement;
- one database schema, Java aggregate shape or service topology; or
- a retail/business-category inheritance hierarchy.

A shopping basket, checkout session, payment-provider session, projected availability or Inventory Claim is not an Order merely because it may participate before commitment.

---

## 4. Canonical Terminology

### Order

An **Order** is:

> **A merchant-scoped Operational Object representing one accepted purchase/order commitment containing one or more durable Order Commitment Portions.**

An Order is not a universal synonym for customer work.

### Ordering

**Ordering** is the capability/semantic owner of Order commitment truth.

Ordering owns:

- Order identity and merchant scope;
- Order Commitment Portions;
- committed subject references;
- committed quantity semantics;
- Order-owned committed terms/provenance;
- Order-side amendment/release facts;
- Order-specific merchant policy where registered; and
- Order operations and their authoritative effects.

### Order Commitment Portion

An **Order Commitment Portion** is a durable identity-bearing portion of the accepted Order commitment sufficient to preserve what was committed and to act as the stable subject for later satisfaction, release/amendment, payment relationship or other governed consequence.

The term `Order Line` MAY be used in presentation or an implementation where it maps exactly to the accepted portion semantics. `Line` is not the canonical owner of the commitment.

---

## 5. Order Identity and Scope

Order identity is merchant-scoped operational-object identity.

Conceptually:

```text
OrderIdentity
{
    merchantScope
    orderIdentifier
}
```

Attribute similarity does not imply identity. Two Orders may legitimately contain identical customer context, subjects, quantities and terms.

The semantic object type follows MS-PROT-020 v1.5 capability-scoped identity:

```text
ordering / order
```

A bare `order` object name is not a global cross-capability namespace.

Every Order MUST contain at least one Order Commitment Portion.

Each Order Commitment Portion MUST have identity unique within its owning Order. A later amendment MUST NOT reuse a portion identity to mean a materially different original commitment.

---

## 6. Order Commitment Portion Minimum Semantics

Each durable Order Commitment Portion MUST retain enough authoritative/provenance information to determine, directly or by immutable accepted reference:

```text
portion identity
committed subject identity/reference
committed quantity + unit semantics
committed commercial terms where commercial terms apply
source proposition/revision provenance where required
semantic/configuration affinity
commitment timestamp/provenance
```

Where one purchase choice creates multiple independently satisfiable or releasable commitments, the model MUST preserve enough portion granularity to distinguish them.

A current Product/Offering record is not a substitute for historical commitment truth.

---

## 7. Historical Commercial Truth

MS-PROT-055 remains authoritative for Money and committed commercial distinctions.

Before `CommitOrder` establishes an Order, applicable current commercial terms MUST be authoritatively revalidated together with the other applicable operation requirements.

After commitment:

```text
current Offering/Product term changes
        ≠
existing Order term changes
```

Hard invariant:

> **Later mutation of Product, Offering, Listing, pricing configuration or other current proposition data MUST NOT retroactively reinterpret an Order Commitment Portion.**

If committed monetary terms apply, the Order retains the committed commercial truth or an immutable/provenance-bearing reference sufficient to reconstruct it exactly.

Displayed stale terms do not create a price hold unless a separately accepted hold/quote semantic does so.

---

## 8. Semantic Release and Configuration Affinity

An Order is a historical business commitment and MUST retain sufficient affinity to the exact semantic/configuration context under which its commitment was established.

The following remain governing:

- MS-PROT-040 for exact active Configuration Revision / RCP binding;
- MS-PROT-054 for semantic compatibility/migration and historical interpretation;
- ADR-011 for exact release materialisation where source-release resolution is required; and
- ADR-012 for executable support of historical contracts.

A newer application deployment, Semantic Registry Release or Merchant Configuration Revision MUST NOT silently reinterpret an existing Order.

---

## 9. Order Lifecycle Model — Facts, Not a Giant Status

Order existence is durable historical truth once successfully committed.

Ordering SHALL NOT use one universal authoritative status such as:

```text
PENDING → PAID → PROCESSING → SHIPPED → DELIVERED → REFUNDED
```

because those labels collapse independently owned facts.

The authoritative model is composed from facts such as:

```text
original Order commitment
+
Order-owned amendments/releases
+
Order Fulfilment-owned satisfaction
+
Money-owned Payment facts
+
Shipment-owned movement facts
```

Convenient merchant/customer labels MAY be derived projections.

An Order does not cease historically to exist because every remaining commitment has been released, satisfied, returned or refunded.

---

## 10. Remaining Order Commitment

Ordering owns the original commitment and Order-side release/amendment evidence. Order Fulfilment owns actual satisfaction.

The remaining actionable commitment is therefore derived from the composition of these independently authoritative facts, conceptually:

```text
original committed quantity
    - Order-owned released quantity
    - Order Fulfilment-owned satisfied quantity
    = remaining commitment
```

The exact calculation MUST respect the applicable quantity/unit semantics and amendment provenance.

Ordering MUST NOT mutate the original committed quantity downward merely because fulfilment occurred.

Order Fulfilment MUST NOT rewrite what was originally ordered.

---

## 11. CommitOrder Operation Contract

`CommitOrder` is the authoritative Ordering operation that establishes a new Order purchase commitment.

Before mutation, the runtime/application boundary MUST establish or revalidate, by applicable accepted authorities:

```text
exact merchant scope
execution principal / Actor Authorisation
semantic applicability
Commercial Entitlement where required
Resource Protection Admission where applicable
current operation/RCP/release affinity
required purchaser/customer context where applicable
current orderable subject/proposition semantics
current commercial terms
required Inventory state/claim compatibility
other registered operation requirements
```

The command/request is intent, not mutation authority.

### Success

Successful `CommitOrder` establishes:

```text
one Order identity
+
one or more immutable original Order Commitment Portions
+
required Inventory Claims atomically where the accepted commitment invariant requires them
+
required local capability-owned consequences whose atomicity is independently accepted
+
a committed Order fact/event where the registered operation declares it
```

### Business rejection

If authoritative revalidation shows that required terms, subject availability, quantity, Inventory capacity, policy or another business invariant is unsatisfied, the Order MUST NOT commit.

### Authorisation / entitlement / protection rejection

These remain distinct runtime decisions under MS-PROT-062 and related authorities and MUST NOT be represented as an Order business lifecycle state.

### Provider/technical failure

External provider failure MUST NOT be used to fabricate an Order rejection or rollback when Order commitment is already known to have committed. MS-PROT-069 governs execution uncertainty where the local commit outcome is unknown.

---

## 12. Inventory Boundary and Atomicity

MS-PROT-058 remains the owner of stock truth and Inventory Claims.

Inventory participation is **optional**. An orderable subject MAY require no finite Inventory semantics.

Where the applicable Order commitment requires stock protection:

```text
current authoritative Inventory revalidation
+
required Inventory Claim(s)
+
Order commitment
```

MUST share the narrow accepted atomic consistency boundary required to prevent incompatible commitments.

Example:

```text
on hand = 1
Customer A CommitOrder
Customer B CommitOrder
```

At most the combination permitted by authoritative Inventory capacity may commit.

Rejected:

```text
commit Order
    ↓
commit succeeds
    ↓ later/asynchronously
attempt required stock claim
```

when the claim is a prerequisite for a valid commitment.

Ordering MAY request/participate in an Inventory-owned claim through an accepted cross-capability contract. Physical co-transaction does not transfer Inventory ownership to Ordering.

---

## 13. Inventory Claim Does Not Define the Order

An Inventory Claim records stock reservation/allocation truth under Inventory authority. It is not the Order commitment.

One Order Commitment Portion MAY relate to zero, one or multiple Inventory Claims according to registered subject semantics. Examples include a non-stock order, a simple stocked item, or a bundle whose components are independently stocked.

Therefore the platform MUST NOT assume universally:

```text
1 Order Portion = 1 Inventory Claim
```

or:

```text
Order quantity = one Inventory counter field
```

The relationship must be explicit where it affects correctness.

---

## 14. Money / Payment Boundary

Order commitment and Payment Obligation are independent authorities.

Canonical direction:

```text
Order commitment
    ↓ may establish
0..n Money-owned Payment Obligations
    ↓
provider payment execution
    ↓
provider evidence / Payment Application
```

A merchant may support registered policies including payment before fulfilment, pay later, invoice, cash on delivery, deposits or no payment where applicable.

The external payment provider MUST NOT create or redefine Order commitment merely by reporting a transaction.

Where creation of a Money-owned Payment Obligation is an accepted local atomic consequence of Order commitment, both may participate in the same local Main Street transaction while preserving separate semantic ownership.

External provider execution MUST remain outside that local database transaction.

Payment failure, refund or settlement MUST NOT erase or rewrite historical Order commitment truth.

---

## 15. Order Fulfilment and Shipment Boundary

MS-PROT-060 remains authoritative.

```text
ORDER
    what was committed

ORDER FULFILMENT
    what commitment portions were actually satisfied

SHIPMENT
    physical movement / dispatch evidence
```

One Order may have multiple Order Fulfilments; one commitment portion may be partially satisfied where quantity semantics permit it.

A failed Shipment does not automatically release the Order, refund Payment or restore Inventory.

Ordering MUST NOT use fulfilment/shipment provider states as its own lifecycle authority.

---

## 16. Order Amendment

A material change to an existing Order MUST preserve historical commitment truth.

Ordering SHALL NOT destructively overwrite an original Order Commitment Portion when the change would alter the meaning of an already established commitment.

A supported amendment therefore establishes explicit authoritative amendment evidence, conceptually:

```text
OrderAmendment
{
    amendmentIdentity
    orderIdentity
    affectedPortionIdentity/identities
    authorised requested change
    released/replaced commitment scope
    replacement commitment scope where applicable
    governing semantic/configuration context
    provenance
    committedAt
}
```

This conceptual shape does not require one Java class/table.

Where a change creates materially new committed subject/quantity/terms, the new commitment truth MUST receive distinct provenance rather than pretending it was part of the original commitment.

---

## 17. Release / Cancel Order Semantics

`Cancel Order` is presentation shorthand only if it maps deterministically to an accepted Ordering release operation.

Ordering may release only the remaining Order commitment that the applicable policy/authority permits to be released.

A release MUST NOT erase:

- original Order existence;
- original commitment portions;
- already satisfied Order Fulfilment;
- Payment history;
- Shipment history; or
- required durable evidence.

A release MAY request/coordinate separately owned consequences such as:

```text
release applicable Inventory Claim
request applicable Refund
cancel future Fulfilment work
send Notification
```

but those consequences remain owned by their capabilities.

Hard rule:

> **Order release is not universal undo.**

---

## 18. Amendment / Release Preconditions

An Order amendment or release operation MUST revalidate current authoritative state at execution time.

At minimum, where applicable it must determine:

- current Order commitment and prior amendment/release facts;
- already satisfied Order Fulfilment portions;
- current Inventory Claim consequences;
- applicable merchant policy;
- current principal authority;
- applicable commercial consequences; and
- concurrency/version conditions required to prevent lost update.

An operation MUST reject a request that attempts to release or replace a commitment scope that is no longer remaining/releasable under the governing authorities.

---

## 19. Concurrency for Amendment / Release

Concurrent operations targeting the same remaining Order commitment MUST NOT both commit incompatible interpretations.

The implementation MUST provide an authoritative serialization/conflict mechanism sufficient to protect the Order invariant, such as optimistic versioning, locking, compare-and-set or another semantically equivalent mechanism.

Technology remains implementation scope.

A stale customer/merchant projection is not mutation authority.

---

## 20. Idempotency and Lost Acknowledgement

`CommitOrder`, amendment and release operations are reachable through retriable transports and multi-channel interaction.

For each duplicate-sensitive operation:

1. the same logical invocation MUST have stable identity;
2. retry after a committed-but-unacknowledged result MUST reconcile to the existing committed result;
3. the same logical invocation MUST NOT multiply Order, Inventory, Payment or event effects; and
4. a genuinely separate customer intent MAY create another otherwise identical Order.

Command identity is not Order identity.

Same merchant + customer + subjects + quantities + terms does **not** prove technical duplication.

MS-PROT-059 and MS-PROT-069 remain governing for interaction convergence and uncertain acknowledgement.

---

## 21. Customer Context and Guest Ordering

CustomerAccount is not required universally.

Where the merchant permits guest ordering, the Order may preserve only the purchaser/contact/destination context required by the applicable Order/Fulfilment/communication semantics without manufacturing a CustomerAccount.

Where an existing CustomerContext is authoritatively associated, the Order may relate to it under MS-PROT-043.

Mutable contact-value similarity MUST NOT silently attach an Order to an unrelated CustomerContext.

A saved/current customer address MUST NOT overwrite the delivery/contact commitment captured for an existing Order.

---

## 22. Merchant Policy and Configuration

Ordering is optional by merchant capability composition.

Merchant configuration MAY establish supported Ordering policy only from registered semantics, including bounded choices such as supported ordering channels or applicable cancellation/amendment rules where those semantics exist.

The merchant chooses merchant-owned policy; Main Street validates/compiles and executes it deterministically.

Ordering MUST NOT be activated merely because the merchant has Publication, Appointment, Booking, Enquiry, Payment or Inventory capability.

Information publishers and appointment-only consultants remain valid merchant compositions with no Ordering capability.

---

## 23. Runtime Decision Separation

An Ordering operation MAY require all applicable runtime dimensions, but they remain independent:

```text
Semantic Applicability
Commercial Entitlement
Actor Authorisation
Resource Protection Admission
Operational Eligibility
Provider Readiness where applicable
Surface Exposure
```

One `enabled`, `available`, `allowed` or `status` field MUST NOT collapse those dimensions.

Loss of entitlement for new Ordering activity MUST NOT erase existing Orders. Purpose-bound residual management of existing commitments remains governed by the applicable Commercial/Account/capability authorities.

---

## 24. Cross-Capability Communication

Ordering may request or participate in capability-owned operations through explicit contracts.

Material edges are classified as follows:

```text
Ordering → Inventory
    command/transaction participation for claim/release where required

Ordering → Money
    establish/request Payment Obligation under accepted commercial semantics

Order Fulfilment → Ordering
    reference/query committed portions; no rewrite of Order truth

Ordering → Notification
    post-commit communication intent where required

Projections → Ordering
    query/read authoritative Order facts; no mutation authority
```

Application orchestration may coordinate progression under MS-PROT-072 but MUST NOT write Ordering, Inventory or Money persistence directly.

---

## 25. Events

Where registered semantic operations declare Domain Events, those events represent facts already committed.

A minimum Order-commit event may conceptually identify:

```text
fact = Order committed
Order identity
operation / causation identity
occurredAt
semantic/configuration provenance where required by the event contract
```

Publication intent MUST obey MS-PROT-025/MS-PROT-026 commit ordering.

Duplicate event delivery MUST NOT create duplicate downstream business effect.

An event does not become a second Order authority.

---

## 26. Projection and Exposure

Merchant/customer Order progress is a projection over the applicable authoritative facts, potentially including Ordering, Money, Order Fulfilment, Shipment and Inventory-derived information.

Projection labels such as:

```text
Awaiting payment
Processing
Partially fulfilled
Shipped
Completed
Cancelled
Refunded
```

MAY be useful presentation results but MUST NOT become one cross-capability Order lifecycle authority.

Where an Order projection meets an MS-PROT-027 v1.3 Projection Contract applicability trigger, the declared Projection Owner governs freshness/serviceability evidence.

Stale projection data MUST NOT authorise CommitOrder, amendment or release.

Exposure/customer-context authority determines who may observe Order information.

---

## 27. AI Boundary

AI may:

- interpret customer/merchant natural-language ordering intent;
- map intent to registered orderable subjects/options;
- explain derived Order progress;
- propose registered merchant Ordering configuration; and
- highlight potential conflicts for deterministic validation.

AI MUST NOT:

- invent committed subjects, quantities or prices;
- manufacture Inventory availability;
- choose materially changed committed terms without required authority;
- treat provider payment output as Order truth;
- directly mutate an Order outside an accepted Ordering operation; or
- create a universal Business Order for every customer interaction.

AI inference remains upstream of deterministic authority.

---

## 28. Failure Taxonomy

Ordering operation outcomes MUST preserve distinctions where caller/recovery behaviour differs:

```text
VALIDATION_REJECTION
    malformed/missing semantic input

AUTHORISATION_REJECTION
    principal not permitted

ENTITLEMENT_REJECTION
    commercial access unavailable for requested new activity

RESOURCE_PROTECTION_REJECTION/DEFER
    governed by resource-protection authority

BUSINESS_REJECTION
    current terms/policy/subject requirements not satisfied

AUTHORITATIVE_CONFLICT
    concurrent/stale state no longer permits requested commitment/change

EXECUTION_UNCERTAIN
    mutation outcome cannot yet be established safely

PROVIDER/TECHNICAL_FAILURE
    infrastructure/provider failed without redefining known committed truth
```

Physical exception/transport names remain implementation scope.

---

## 29. Business-Type Neutrality

This model is validated by composition, not business inheritance.

### Physical retailer

```text
Offering/Product
+ Ordering
+ Inventory where applicable
+ Money/Payment where applicable
+ Order Fulfilment
+ Shipment/collection where applicable
```

### Made-to-order / non-stock seller

Ordering may commit without fake infinite Inventory.

### Appointment consultant

Appointment + optional Payment Obligation may be sufficient. No Order is manufactured.

### Information publisher

Publication/Opportunity/Enquiry may operate without Ordering, Inventory or Payment.

### Hybrid salon/retailer

Appointment owns scheduled service commitments; Ordering owns product purchase commitments. A unified dashboard/customer history may project both without merging their semantic authority.

---

## 30. Falsification Evidence Incorporated

The approved review rejected:

1. one universal Business Order for all customer work;
2. one giant `OrderStatus` containing Payment/Fulfilment/Shipment/Refund truth;
3. payment-provider success as Order creation authority;
4. committing stock-constrained Order before its required Inventory Claim;
5. destructive editing of committed Order portions;
6. `cancelOrder()` automatically refunding, restocking and erasing history;
7. mandatory Inventory for every Order;
8. mandatory CustomerAccount for every Order; and
9. channel-specific OnlineOrder/POSOrder/PhoneOrder semantic owners.

These rejected models MUST NOT be reintroduced as implementation shortcuts.

---

## 31. Trade-Off

The accepted model rejects the implementation convenience of one universal customer-work aggregate.

Cost:

- merchant dashboards/customer history/analytics may need cross-capability projections;
- purchase, reservation and scheduled-service commitments remain distinct models; and
- some operations require explicit orchestration across capability-owned facts.

Benefit:

- no fake Orders for non-purchase activity;
- Inventory, Money and Fulfilment ownership remains coherent;
- hybrid merchants compose naturally;
- historical commitments remain precise; and
- Main Street remains generic rather than retail-shaped.

The universal model may be reconsidered only if future evidence proves the commitment types have materially identical authoritative invariants and mutation semantics, not because a unified UI is convenient.

---

## 32. Deferred / Future Scope

The following remain future scope until deliberately promoted:

- shopping-basket/cart durable lifecycle if independent authority is demonstrated;
- Quote/Estimate lifecycle beyond MS-PROT-055's current boundary;
- advanced Order amendment policy catalogues;
- exchange/substitution workflows beyond existing accepted boundaries;
- reverse fulfilment beyond MS-PROT-061/MS-PROT-060 current scope;
- complex package/logistics/warehouse semantics;
- split-order/split-merchant marketplace semantics;
- cross-merchant Order aggregation;
- advanced tax/legal invoice semantics; and
- offline authoritative Ordering.

These deferrals MUST NOT weaken the accepted commitment/atomicity/history invariants above.

---

## 33. ADR-005 Supersession Effect

`docs/foundation/adr/business-order-domain-model.md` remains historical architecture evidence.

MS-PROT-077 **supersedes ADR-005 only within the scope of ADR-005 claims that:**

- all customer work should be represented through one Business Order abstraction;
- Business Order is the primary universal operational object for Unified POS; or
- services, appointments/jobs and other materially different commitments should be forced into that common authority merely for internal consistency.

ADR-005 statements that independently preserve useful boundaries—such as Payment and Inventory being separate concerns—remain historical reasoning but do not override later accepted MS-PROT authority.

Current semantic authority is:

```text
Booking      → MS-PROT-042 reservation commitment
Appointment  → MS-PROT-042 scheduled-service commitment
Order        → MS-PROT-077 purchase/order commitment
Enquiry      → MS-PROT-043 request/information context
Publication  → MS-PROT-046 information dissemination
```

A unified POS/dashboard is a surface/application composition problem, not evidence for one universal Business Order domain owner.

---

## 34. Implementation Constraints

Initial implementation MUST follow `designs/IMPLEMENTATION-RULES.md` and SHOULD begin with the smallest authoritative slice:

```text
ordering.commit
    → typed command
    → capability-owned Order creation
    → immutable commitment portions
    → exact release affinity
    → stable logical command identity/idempotency
    → merchant isolation
    → required Inventory Claim atomicity where represented in the slice
    → committed Domain Event/outbox evidence
```

Implementation MUST NOT introduce checkout UI, provider payment execution, shipment or a universal commerce engine merely to implement Order commitment.

Amend/release implementation may proceed in a later implementation target while remaining governed by this accepted authority; no new semantic approval is required unless implementation reveals a material unanswered question.

---

## 35. Conformance / Acceptance Criteria

An implementation conforms to this authority only if applicable tests demonstrate:

1. Order identity is merchant-scoped.
2. A successful Order contains at least one durable commitment portion.
3. Commitment portions retain exact committed subject/quantity/term provenance required by the slice.
4. Later mutable proposition data cannot rewrite the persisted Order commitment.
5. Runtime execution retains exact governing release affinity.
6. Duplicate delivery of the same logical `CommitOrder` does not multiply effect.
7. Reuse of one command identity for materially different intent is rejected.
8. Two genuinely separate identical-looking Orders remain valid where business invariants permit them.
9. Required Inventory Claim failure prevents Order commit atomically.
10. Non-stock Orders do not require fake Inventory.
11. Merchant scopes cannot observe/mutate each other's Orders/claims.
12. Payment-provider behaviour is not required to establish the local Order commitment unless a separately accepted policy/process requires a provider wait before commitment.
13. Order Fulfilment/Shipment/Refund truth is not stored as one authoritative Order status.
14. Failed mutation leaves no partial authoritative Order/event/required claim effect.
15. Commit timestamp used for authoritative evidence is platform-observed at the mutation boundary.
16. Projection/read representations do not authorise mutation.
17. No BusinessOrder/OnlineOrder/POSOrder/PhoneOrder semantic subtype is introduced for the same commitment.

---

## 36. Acceptance Statement

The promoted Order / Ordering authority gap is closed.

> **Main Street models a purchase/order commitment as a capability-owned Order composed of durable historical commitment portions. Ordering owns what was committed; Inventory owns stock and claims; Money owns obligations/payment evidence; Order Fulfilment owns satisfaction; Shipment owns movement; projections unify observation without unifying authority.**

This preserves Main Street's capability-based, configuration-driven modular-monolith architecture and removes the final commerce-inclusive semantic authority hole identified by the executable-architecture vertical-slice review.
