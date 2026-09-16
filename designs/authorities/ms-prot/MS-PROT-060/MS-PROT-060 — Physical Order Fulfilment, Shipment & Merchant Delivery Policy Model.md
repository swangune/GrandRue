# MS-PROT-060 — Physical Order Fulfilment, Shipment & Merchant Delivery Policy Model

**Document ID:** MS-PROT-060  
**Version:** 1.0  
**Status:** ACCEPTED after proposal, review, graph falsification, recommendation and approval  
**Validation domain:** Online physical-product merchant operating without a public shopfront  
**Purpose:** Define the minimum generic semantics for satisfying physical-product Order commitments, dispatching goods, customer collection, shipment evidence, merchant-controlled delivery policy, registered and guest customer access, and customer-facing tracking without introducing business-category special cases or allowing Main Street to prescribe merchant operations.

---

# 1. Governing principles

> **Main Street streamlines merchant operations; it does not decide how merchants run their businesses.**

> **Merchant policy determines supported fulfilment choices and commercial terms. Main Street provides registered semantics, assists configuration, validates approved configuration, executes it deterministically, coordinates capability-owned consequences, records evidence and projects authorised views.**

> **Fulfilment records satisfaction of an Order commitment. Shipment records physical movement. They are not the same semantic object.**

> **Customer registration changes authentication, continuity and access convenience; it does not create a different Order or Fulfilment model.**

---

# 2. Architectural fit

This design follows Main Street's governing composite architecture and programming model:

```text
DOMAIN AUTHORITY
    capability-owned domain semantics

VARIABILITY
    declarative merchant configuration

SEMANTIC RESOLUTION
    deterministic compiler/resolution

RUNTIME
    imperative application orchestration

LIFECYCLES
    state/fact modelling only where justified

REACTIONS
    selective post-commit events

INFRASTRUCTURE
    ports/adapters for carriers/providers

AI
    probabilistic interpretation at boundary
    deterministic authority at core

EXTENSIBILITY
    composition over business-type inheritance
```

No `OnlineSeller`, `SocialMediaSeller`, `HomeSeller`, `ImporterBusiness` or similar business-type hierarchy is introduced.

---

# 3. Validation domain

The validating merchant may:

- purchase a finite quantity of physical merchandise;
- store stock at a home, office or other private operational location;
- advertise through social media;
- direct customers to a merchant website/landing page;
- expose multiple products for discovery;
- accept online Orders;
- optionally accept manual/phone/walk-in Orders through the same authoritative operations;
- permit guest ordering or customer accounts according to merchant policy;
- accept payment according to merchant policy;
- fulfil Orders by collection, merchant delivery or external carrier;
- expose Order and Shipment tracking through the merchant website.

This domain shall be represented by composition of existing capabilities plus the Fulfilment/Shipment semantics defined here.

---

# 4. Semantic separation

The following authority separation is normative:

```text
ORDER
    what the customer committed to obtain

FULFILMENT
    what authoritative portions of that commitment were actually satisfied

SHIPMENT
    physical movement/dispatch attempt associated with fulfilment

INVENTORY
    stock claims, stock movements, quantities and physical disposition

PAYMENT / MONEY
    monetary obligations, settlement and refund evidence

MERCHANT POLICY
    merchant-selected fulfilment methods and commercial rules

CUSTOMER CONTEXT
    merchant-scoped durable customer relationship

CUSTOMER ACCOUNT
    authenticated identity/access mechanism

EXPOSURE / PROJECTION
    what an authorised audience may observe
```

Reference authority shall not imply mutation authority.

---

# 5. Fulfilment

A **Fulfilment** is a capability-owned durable record representing satisfaction of one or more authoritative portions of a single Order commitment.

Conceptually:

```text
Order O1
    ├── Line L1 ×2
    └── Line L2 ×1

Fulfilment F1
    ├── satisfies L1 ×2
    └── satisfies L2 ×1
```

A Fulfilment may satisfy multiple lines or quantities within one Order.

One Order may have multiple Fulfilments.

A Fulfilment shall not be reduced to `Order.fulfilled = true`.

---

# 6. Fulfilment truth and derived status

Fulfilment truth shall primarily be represented by satisfaction facts against authoritative commitment portions.

Summary states such as:

```text
NOT_FULFILLED
PARTIALLY_FULFILLED
FULFILLED
```

should be derived where possible from the applicable commitment and recorded satisfaction facts rather than acting as the sole source of truth.

A coarse universal `FulfilmentStatus` shall not replace quantity/commitment-level evidence.

---

# 7. Historical affinity

A Fulfilment shall retain sufficient provenance to identify the authoritative Order commitment portion it satisfied even if the Order is later legitimately modified.

It shall not rely only on current Product identity or mutable current Order presentation.

Where substitution is permitted and approved, Fulfilment shall record the actual supplied subject and its relationship to the committed subject. It shall not falsify history by pretending the originally committed item was supplied.

---

# 8. Shipment

A **Shipment** is an optional durable physical-movement record associated with fulfilment where goods are dispatched toward a destination.

Shipment is not synonymous with Fulfilment.

```text
COLLECTION
    Fulfilment
    Shipment = none

ORDINARY DISPATCH
    Fulfilment
        ↓
    Shipment

SPLIT / RETRY MOVEMENT
    Fulfilment
        ↓
    Shipment S1
    Shipment S2
```

A Fulfilment may require zero, one or multiple Shipments.

A Shipment may, where future consolidation semantics explicitly permit it, transport fulfilment associated with more than one Order; Fulfilment itself remains traceable to one Order commitment.

---

# 9. Shipment is provider-neutral

Shipment semantics shall not be defined by Royal Mail, DHL, DPD or any other carrier vocabulary.

Carrier-specific status/events are provider evidence.

Provider evidence may inform Main Street Shipment truth and projections but shall not redefine platform semantics.

Carrier integrations shall be implemented behind ports/adapters.

---

# 10. Shipment lifecycle representation

Main Street shall not introduce a large carrier-inspired universal state machine such as:

```text
CREATED → PACKED → DISPATCHED → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED
```

unless later invariants prove it necessary.

The minimum authoritative representation should prefer constrained platform-owned facts such as dispatch evidence, destination, method, tracking/correlation evidence and terminal delivery outcome where known.

Customer-facing statuses may be projections derived from authoritative and provider evidence.

Out-of-order provider events shall not blindly regress authoritative Shipment truth.

---

# 11. Delivery is not initially a mandatory Operational Object

`Delivery` is initially a fulfilment method/outcome concept around Fulfilment and Shipment rather than a mandatory standalone Operational Object.

`Package` and carrier `TrackingEvent` likewise do not receive independent domain identity until independent authority, lifecycle or operational actions justify them.

Carrier tracking events may be retained as provider evidence without becoming Main Street Operational Objects.

---

# 12. Collection

Customer collection is a valid Fulfilment method and requires no Shipment.

A merchant may configure one or more supported collection locations where registered semantics permit it.

A collection location shall not be inferred to be the same as the merchant's stock or dispatch origin.

MerchantLocation role and Exposure semantics remain authoritative for whether a location may be used operationally and whether it may be shown to the customer.

---

# 13. Merchant self-delivery

Shipment semantics shall support merchant-operated physical delivery without requiring an external carrier, carrier account or tracking number.

Provider integration is optional infrastructure, not a prerequisite for physical fulfilment.

---

# 14. Merchant-owned fulfilment and delivery policy

The merchant owns supported operating choices including, where registered semantics exist:

- whether delivery is offered;
- whether collection is offered;
- delivery/collection methods;
- delivery areas;
- delivery charges;
- free-delivery thresholds;
- delivery timing promises or estimates;
- payment prerequisites;
- dispatch practices;
- applicable cancellation/substitution/fulfilment choices.

Main Street shall not choose these commercial or operational policies on the merchant's behalf.

---

# 15. Deterministic application is not policy authorship

Main Street may deterministically compute the consequence of an approved merchant-configured policy.

Example:

```text
Merchant policy:
    delivery = £5 below £50
    delivery = free at £50+

Order merchandise value:
    £62

Deterministic result:
    delivery charge = £0
```

This is execution of merchant policy, not Main Street deciding the delivery price.

Live carrier-derived pricing and provider quote expiry are FUTURE_SCOPE unless separately designed and accepted.

---

# 16. AI-assisted policy configuration

AI may help merchants express fulfilment policy through natural language.

Example:

> Charge £4.99 delivery, make it free above £50, and let customers collect for free.

The AI may infer a candidate configuration only from registered semantics.

```text
Merchant natural-language intent
        ↓
Specialist inference
        ↓
Candidate registered configuration
        ↓
MANDATORY MANUAL MERCHANT VALIDATION
        ↓
Deterministic semantic validation
        ↓
Authoritative merchant configuration
```

AI and specialist agents shall not invent new semantics.

---

# 17. Inventory boundary

Inventory remains authoritative for:

- stock claims/reservations;
- available quantity;
- physical stock movements;
- sellability/disposition;
- restoration/re-entry of physical goods.

Fulfilment shall not directly manipulate inventory counters.

Fulfilment may produce/request an authorised Inventory consequence through capability contracts.

Shipment failure, loss or delivery failure shall not itself restore stock.

Goods physically returned to merchant control require an Inventory movement/disposition decision under Inventory semantics.

---

# 18. Payment boundary

Payment/Money remains independently authoritative.

Main Street shall not universally require payment settlement before Fulfilment.

A merchant may legitimately support registered policies such as:

- payment before fulfilment;
- invoice/pay-later;
- cash on delivery;
- other supported payment timing.

Whether Fulfilment may proceed is therefore an applicable merchant-policy requirement, not a universal Fulfilment invariant.

Shipment failure shall not automatically create a refund.

---

# 19. Cancellation and partial fulfilment

Cancellation before physical fulfilment may release applicable Inventory claims through Inventory authority.

Cancellation after partial fulfilment shall not erase already satisfied commitment history.

The system must be able to represent:

```text
original commitment
    +
already fulfilled portions
    +
remaining commitment cancelled/modified
```

without pretending the entire Order was never fulfilled.

---

# 20. Shipment failure, loss and redelivery

A failed/lost Shipment does not automatically:

- cancel the Order;
- refund Payment;
- restore Inventory;
- create another Fulfilment quantity.

The merchant chooses the applicable supported next operation according to merchant policy.

Where the same committed quantity is resent because a transport attempt failed, multiple Shipment attempts shall not cause the Fulfilment quantity to be counted more than once.

---

# 21. Provider callback idempotency

Carrier/provider callbacks shall correlate to existing Shipment/process identity and be reconciled idempotently.

Repeated callbacks shall not create duplicate:

- Fulfilment satisfaction;
- Inventory movement;
- notifications;
- Payment effects;
- Shipment records.

This design inherits the unified interaction/idempotency rules of MS-PROT-059.

---

# 22. Customer delivery destination

A customer delivery destination is contextual Order/Fulfilment commitment data, not a MerchantLocation.

A mutable saved address on CustomerContext shall not silently reinterpret an existing Order.

Committed destination and applicable selected fulfilment terms require historical affinity sufficient to survive later changes to:

- customer saved addresses;
- merchant delivery configuration;
- delivery charges;
- delivery windows;
- merchant locations.

---

# 23. Customer accounts are merchant-configurable

Main Street shall support merchant-configurable customer-access policy within registered semantics.

Supported configurations may include:

```text
A. Customer accounts disabled
   Guest ordering enabled

B. Customer accounts enabled
   Guest ordering enabled
   Registration optional

C. Customer accounts enabled
   Guest ordering disabled
   Authentication required for applicable operations
```

The merchant chooses the supported policy.

Main Street may infer/propose the configuration during onboarding, but manual merchant validation is required before authoritative configuration is established.

---

# 24. CustomerAccount is not CustomerContext

Existing identity/customer semantics remain authoritative:

```text
CustomerContext
    = merchant-scoped durable customer business relationship

CustomerAccount
    = identity/access construct
```

An Order may require sufficient customer/business context without requiring a CustomerAccount.

Therefore:

```text
Order requires customer context
    ≠
Order requires registration/login
```

Fulfilment shall not introduce a separate customer identity system.

---

# 25. Guest ordering

Where merchant policy permits guest ordering, an unregistered Visitor may create an Order by supplying only the unresolved information required by the applicable Order/Fulfilment semantics, for example:

- name where required;
- contact channel;
- delivery destination where delivery applies;
- other required checkout information.

A CustomerAccount shall not be created merely because an Order exists.

Where the operation requires a durable merchant-customer relationship, the applicable merchant-scoped CustomerContext semantics shall be used.

---

# 26. Registered customer ordering

Where a customer authenticates through a merchant-enabled CustomerAccount:

```text
CustomerAccount
        ↓
authorised merchant-scoped CustomerContext
        ↓
Order / Booking / other authorised relationships
```

Authentication permits Main Street to project the customer's authorised merchant-scoped operational history without changing the underlying Order or Fulfilment semantics.

---

# 27. Customer order and shipment tracking

Customer tracking shall be a projection over authoritative Order, Fulfilment and Shipment data.

It shall not create a second tracking authority.

For authenticated customers:

```text
CustomerAccount
        ↓
CustomerContext
        ↓
authorised Orders
        ↓
Order / Fulfilment / Shipment projection
```

The merchant website may expose customer-safe views such as:

- Order history;
- current Order progress;
- fulfilment progress;
- dispatch evidence;
- carrier/tracking reference where permitted;
- customer-safe delivery outcome.

Main Street remains infrastructure behind the merchant-facing customer experience.

---

# 28. Guest transaction tracking

Guest customers may track a specific transaction where registered contextual-access semantics permit it.

Conceptually:

```text
Order confirmation communication
        ↓
secure transaction-specific access
        ↓
Order O100
        ↓
customer-safe Order/Fulfilment/Shipment projection
```

Transaction-specific access shall authorise only the applicable scope and shall not imply a global CustomerAccount.

A guest shall not gain access to unrelated Orders merely because contact data appears similar.

---

# 29. Guest-to-account transition

If a guest later creates a CustomerAccount, historical Orders shall not be attached solely because an email address, telephone number, name or address matches.

Existing CustomerContext reconciliation and identity-authority rules remain governing.

Strong authoritative correlation may establish the appropriate relationship where supported.

Weak similarity may propose reconciliation but shall not silently merge merchant-customer history.

---

# 30. Onboarding configuration

For an online physical-product merchant, onboarding may infer the need to ask concise structured questions such as:

```text
How should customers buy from you?

○ Anyone can order without an account
○ Customers may create an account, but it is optional
○ Customers must have an account to order
```

and applicable fulfilment questions such as:

```text
How can customers receive physical orders?

□ Delivery
□ Collection
```

Subsequent questions are determined from the resolved configuration graph.

AI inference may streamline the sequence but shall not gatekeep merchant choices or invent semantic relationships.

---

# 31. Projection and exposure

Internal fulfilment data and customer-facing tracking data are not identical.

Internal data may include:

- private dispatch origin;
- carrier account information;
- internal notes;
- stock location;
- provider evidence.

Customer projection may expose only authorised information such as:

- Order progress;
- dispatch status;
- tracking reference;
- selected fulfilment method;
- estimated delivery information;
- collection instructions.

Exposure/Projection semantics determine observation. Fulfilment does not own presentation labels or audience policy.

---

# 32. Notification boundary

Notification failure shall not reverse or alter authoritative Fulfilment or Shipment state.

Notifications react to committed facts and are independently responsible for communication delivery.

A dispatch notification, tracking email or collection-ready message is therefore a projection/reaction to authoritative business truth, not the truth itself.

---

# 33. Returns and reverse fulfilment

Customer returns/reverse fulfilment are recognised as a distinct downstream bounded design.

They shall not be collapsed into outbound Shipment failure.

```text
RETURN / REVERSE FULFILMENT
    → FUTURE_SCOPE / separate design authority
```

The present model shall not prohibit later reverse-fulfilment composition.

---

# 34. Explicitly rejected designs

The following are rejected:

```text
OnlineSeller business subclass
SocialMediaSeller business subclass
Order.fulfilled boolean as fulfilment authority
Shipment == Fulfilment
Delivery as mandatory standalone entity
Package as mandatory entity
TrackingEvent as mandatory Operational Object
Packing as universal lifecycle state
Carrier lifecycle as Main Street lifecycle
Carrier status directly mutating unrelated authorities
Shipment failure automatically cancelling Order
Shipment failure automatically refunding Payment
Shipment failure automatically restoring Inventory
Payment settlement universally required before fulfilment
CustomerAccount universally required to order
Guest Order creating a CustomerAccount automatically
Customer tracking creating a second Order/Shipment authority
Email/phone equality automatically merging customer histories
Main Street choosing merchant delivery prices
AI inventing unsupported fulfilment semantics
```

---

# 35. Cross-domain validation result

The online physical-product seller validates the existing composition model:

```text
Merchant
    + Product
    + ProductVariant where justified
    + Offering
    + Inventory
    + Ordering
    + Payment/Money
    + MerchantLocation
    + Exposure
    + CustomerContext
    + optional CustomerAccount
    + Projection
    + Notification
    + Fulfilment
    + optional Shipment
```

Social media is an acquisition/referral channel into the merchant's own projected storefront. It does not require duplicate Product, Offering, Order or Inventory authority.

A private home/office fulfilment origin may remain operationally usable while withheld from public exposure.

The domain therefore requires no business-category special case.

---

# 36. Canonical operational graph

```text
                     MERCHANT POLICY
         fulfilment methods / charges / access policy
                           │
                           ▼
                        ORDER
                 customer commitment
                           │
                           ▼
                      FULFILMENT
             satisfaction of commitment portions
                           │
                ┌──────────┴──────────┐
                ▼                     ▼
            COLLECTION          PHYSICAL MOVEMENT
                                      │
                                      ▼
                                  SHIPMENT
                            transport attempt/evidence
                                      │
                           provider evidence optional

Inventory     → owns stock truth
Payment/Money → owns monetary truth
CustomerContext → owns merchant-scoped customer relationship
CustomerAccount → owns authenticated access
Projection/Exposure → owns audience-safe observation
Notification → owns communication delivery
```

---

# 37. Hard invariants

1. Main Street shall not prescribe merchant fulfilment or delivery policy where merchant choice is legitimate.
2. AI may infer only registered semantics and requires manual merchant validation before authoritative policy configuration.
3. Fulfilment shall not be reduced to a coarse Order status.
4. Shipment shall not be required for collection.
5. Shipment shall remain provider-neutral.
6. Carrier/provider evidence shall not become cross-capability mutation authority.
7. Fulfilment shall preserve what was actually supplied and the commitment portion satisfied.
8. Shipment retries shall not double-count Fulfilment quantities.
9. Shipment failure shall not automatically cancel, refund or restore stock.
10. Inventory effects occur through Inventory authority.
11. Payment effects occur through Payment/Money authority.
12. Committed destination and selected fulfilment terms shall not be silently reinterpreted from mutable current configuration.
13. CustomerAccount shall not be required merely because a durable CustomerContext or Order exists.
14. Guest ordering shall be supported where merchant policy enables it.
15. Customer tracking is a projection over authoritative Order/Fulfilment/Shipment truth.
16. Guest tracking shall use appropriately scoped contextual access rather than creating implicit accounts.
17. Contact-data similarity alone shall not establish authenticated identity or merge customer histories.
18. Main Street may deterministically evaluate merchant-approved delivery policy but shall not author the merchant's commercial policy.
19. Returns/reverse fulfilment remain a distinct downstream semantic concern.
20. No business-category-specific fulfilment hierarchy shall be introduced.

---

# 38. Implementation consequences

This design does not require immediate implementation of every future fulfilment feature.

Implementation should preserve these boundaries while introducing the smallest accepted semantic surface necessary for the first physical-product flow.

Likely implementation work includes, subject to implementation design and tests:

- Fulfilment identity and commitment-portion references;
- Shipment identity where physical dispatch occurs;
- provider-neutral dispatch/tracking evidence;
- merchant-configured fulfilment options;
- Order/Fulfilment historical affinity;
- capability contracts with Inventory, Payment/Money, Notification and Projection;
- authenticated customer tracking projections;
- transaction-scoped guest tracking;
- idempotent provider reconciliation.

Implementation shall not create unsupported semantics merely for convenience.

---

# 39. Deferred scope

The following remain explicitly deferred unless separately proposed, falsified and approved:

```text
RETURN / REVERSE FULFILMENT
LIVE CARRIER RATE QUOTING
PACKAGE-LEVEL LOGISTICS
WAREHOUSE MANAGEMENT
ROUTE OPTIMISATION
CARRIER-SPECIFIC WORKFLOW SEMANTICS
ADVANCED SHIPMENT CONSOLIDATION
DELIVERY SLOT OPTIMISATION
CUSTOMS / IMPORT MANAGEMENT
```

Their deferral shall not be interpreted as architectural prohibition.

---

# 40. Acceptance statement

MS-PROT-060 accepts Physical Order Fulfilment and Shipment as composable Main Street semantics required by the online physical-product validation domain.

The design preserves the established composite architecture and programming paradigm, capability ownership, merchant operational autonomy, declarative configuration, deterministic authority, composition over inheritance, ports/adapters, projection boundaries, customer identity separation and unified-channel/idempotency model.

The canonical rule is:

> **The merchant decides how to fulfil and what commercial policy to offer; Main Street helps configure those choices, validates them, executes the approved semantics consistently, records what actually happened, and exposes the resulting truth safely to merchants and customers.**
