# MS-PROT-059 — Unified Interaction Channels, Authoritative Operations & Idempotency Model

**Document ID:** MS-PROT-059  
**Version:** 1.0  
**Status:** **ACCEPTED after proposal, review, graph-aware falsification and manual approval**  
**Depends on:** MS-PROT-023; MS-PROT-027; MS-PROT-043; MS-PROT-044; MS-PROT-049; MS-PROT-055; MS-PROT-058 and applicable capability authorities  
**Purpose:** Define how Main Street preserves one authoritative merchant operational model while supporting customer self-service, merchant-assisted, telephone, walk-in, POS, integration and AI-assisted interaction channels without duplicate business truth, duplicated configuration, double execution or channel-specific domain fragmentation.

---

# 1. Governing decision

Main Street shall maintain one authoritative semantic and operational model per merchant.

Customer self-service, merchant dashboard, POS, telephone-assisted, walk-in, integration and AI-assisted interactions are channels into and projections over that model. They are not parallel business systems.

Governing rule:

> **Enter each authoritative business fact once at its owning semantic authority; project it wherever legitimately required; accept intent through multiple channels; converge equivalent business intent on the same capability-owned operation; revalidate authoritative state before mutation; and prevent duplicate effects using the mechanism appropriate to the type of duplication.**

Conceptually:

```text
ONE AUTHORITATIVE BUSINESS TRUTH
            ↑
       many inputs
            ↓
      many projections
```

---

# 2. Channel is not business identity

The origin of an interaction shall not create a separate business-object type merely because the instruction arrived through a different channel.

Rejected examples:

```text
OnlineAppointment
PhoneAppointment
WalkInAppointment
AIAppointment

OnlineOrder
POSOrder
PhoneOrder
```

Preferred model:

```text
Appointment
    + origin/provenance

Order
    + origin/provenance
```

Channel/origin is contextual provenance where materially useful. It is not the defining semantic identity of the commitment.

---

# 3. Authoritative merchant information is owned once

Authoritative merchant information shall be authored at its owning semantic authority rather than duplicated per interface.

Examples include:

- Offering terms;
- Product identity;
- inventory configuration and stock-bearing relationships;
- business/opening hours;
- scheduling configuration;
- merchant policies;
- supported fulfilment modes;
- exposure configuration; and
- other registered semantic configuration.

Main Street shall reject parallel channel-specific authorities such as:

```text
website_service
manual_service
phone_service
walk_in_service

web_stock
shop_stock
```

where they refer to the same underlying business truth and operational scope.

---

# 4. Presentation variation does not duplicate business truth

The single-authority rule does not require every surface to use identical presentation text or layout.

Example:

```text
Authoritative Offering:
    identity = MOT Class 4
    price = £60
    duration = 45 minutes

Public presentation:
    "MOT Test"

Dashboard shorthand:
    "MOT"
```

Where presentation values genuinely have presentation meaning, they may be configured as projection/surface semantics.

They shall not become duplicate authorities for price, duration, stock, scheduling requirements or other business facts.

---

# 5. Multiple projections over one model

The same authoritative merchant model may legitimately produce different projections for different audiences and surfaces.

Conceptually:

```text
Authoritative Offering / Product / Inventory / Schedule
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
       Website    Dashboard    AI context
```

A projection is not a second copy of the authoritative object.

Changes to authoritative business facts shall become visible to projections according to their consistency and refresh contracts without requiring merchants to re-enter the information per channel.

---

# 6. Channel-convergent operations

Where multiple channels request the same business action, they shall converge on the same capability-owned application/runtime operation semantics.

Example:

```text
Customer website ─────┐
Phone-assisted entry ─┤
Walk-in merchant UI ──┤
Merchant dashboard ───┤──► CreateAppointment
AI-assisted merchant ─┘
```

The operation owner remains Scheduling/Appointment or the applicable capability.

The channel may affect:

- execution principal;
- provenance;
- available controls;
- applicable requirements;
- contextual information already known; and
- exposure/presentation.

It shall not create a parallel operation authority.

---

# 7. Channel availability is configuration around shared semantics

A merchant may legitimately allow an operation through some channels and not others.

Example:

```text
MOT Service
    phone-assisted booking = permitted
    walk-in booking = permitted
    public self-booking = unavailable
```

Main Street shall implement this through applicable surface, exposure, authority and policy semantics around the same Offering and operation.

It shall not create separate online/manual copies of the Offering.

---

# 8. Requirements remain semantic; forms remain contextual

Different channels do not require identical forms.

The applicable business operation owns its semantic Requirements.

The channel/runtime context determines which Requirements remain unresolved.

Conceptually:

```text
Operation Requirements
        +
Known Context
        ↓
Unresolved Requirements
        ↓
Channel-specific collection projection
```

Therefore a website may ask a customer for information that the merchant dashboard already knows through CustomerContext or other authoritative context.

Main Street shall use Context-Carry-Forward and resolved context to minimise repeated data entry rather than defining separate domain contracts for each form.

---

# 9. Channel authority may differ

Shared operation semantics do not imply identical permissions across channels or principals.

Example:

```text
Customer:
    create appointment within exposed availability

Authorised staff:
    create merchant-assisted appointment
    perform additional registered operations where permitted
```

Execution authority remains governed by principal, context, merchant scope, policy and capability invariants.

Channel is not itself authority.

---

# 10. Stale projections are never mutation authority

A website or dashboard may display state that becomes stale before the user commits an operation.

Therefore:

> **Displayed availability, stock, price or other projection state is never sufficient authority for mutation.**

Before authoritative mutation, Main Street shall validate/revalidate applicable state within the execution consistency boundary established by the owning capability and MS-PROT-023.

---

# 11. Concurrent booking invariant

Suppose one appointment capacity remains at 14:00.

Both customer website and merchant dashboard may observe it as available.

If both attempt to create a commitment concurrently:

```text
website CreateAppointment ─┐
                           ├── Scheduling authority
phone CreateAppointment ───┘
                                  ↓
                         authoritative revalidation
                                  ↓
                      only compatible commitments commit
```

Main Street shall not solve this by maintaining separate channel availability.

The authoritative Scheduling capability owns the invariant.

---

# 12. Concurrent inventory invariant

Where one stock unit remains, online Ordering and walk-in/POS Ordering shall compete against the same authoritative Inventory scope.

Conceptually:

```text
Online Order ───► Inventory claim
Walk-in Order ──► Inventory claim
                       ↓
               authoritative revalidation
```

Only compatible claims may commit.

Main Street shall not maintain `online stock` and `shop stock` for the same physical inventory scope merely to support different channels.

Distinct stock scopes are permitted only where they represent genuinely distinct operational inventory, such as different locations.

---

# 13. Duplicate safety is not one problem

Main Street shall distinguish at least four materially different duplicate scenarios:

```text
A. same logical technical request delivered repeatedly
B. competing operations that violate a business invariant
C. possible repeated human intent that remains semantically legal
D. uncertain customer identity / duplicate relationship context
```

These shall not be collapsed into one generic duplicate detector.

---

# 14. Technical retries require idempotency

A retry of the same logical operation must not create an additional business effect merely because transport, client or integration delivery occurred more than once.

Conceptually:

```text
Logical Operation X
        ↓
first delivery
        ↓
Execution Result R

Logical Operation X
        ↓
retry
        ↓
return/reconcile with R
```

not:

```text
X delivery 1 → effect A
X delivery 2 → effect B
```

The implementation may use an idempotency key, command identity, provider correlation reference or another suitable mechanism.

The governing semantic invariant is logical-operation identity and duplicate-effect prevention, not a mandated physical key type.

---

# 15. Idempotency identity is not operation identity

Two legitimate invocations of the same operation against the same target may both be valid.

Example:

```text
Fulfil one unit
Fulfil another unit
```

Therefore Main Street shall not infer technical duplication merely from:

```text
same operation type
+
same target
```

Idempotency identity must identify the same logical invocation/delivery attempt.

---

# 16. Business-invariant conflicts use authoritative revalidation

Where duplicate-like activity threatens a hard invariant, the capability owner shall enforce the invariant transactionally or through another accepted consistency mechanism.

Examples:

- allocating the same exclusive appointment capacity twice;
- overselling the same stock position;
- applying an incompatible lifecycle transition twice; or
- creating conflicting resource allocation.

This is not merely idempotency.

A second genuinely distinct request may still be rejected because authoritative state no longer permits it.

---

# 17. Possible duplicate human intent is advisory unless an invariant says otherwise

A customer may legitimately create similar commitments.

Example:

```text
MOT at 10:00
MOT at 15:00
```

Therefore Main Street shall not define universal rules such as:

```text
same customer + same service + same day = duplicate
```

Where contextual evidence suggests the merchant may be repeating an already completed action, Main Street may surface an advisory warning.

Example:

```text
Possible duplicate booking:
Sarah already has MOT tomorrow at 10:00.
```

Unless a registered capability invariant prohibits the second commitment, the warning shall not silently merge, cancel or rewrite business truth.

---

# 18. Customer identity ambiguity remains separate

Duplicate operation detection shall not become customer identity reconciliation.

Similar names, contact details or transaction patterns may support inference but do not establish identity truth.

CustomerContext reconciliation remains governed by MS-PROT-043.

Main Street shall not silently merge customers merely to prevent an apparent duplicate booking/order.

---

# 19. Manual stock changes are operations, not raw field overwrites

Merchant inventory correction and customer inventory consumption are different operations against the same Inventory authority.

Rejected:

```text
merchant UI writes stock = 4
online Order writes stock = 4
```

Preferred semantic model:

```text
Inventory position
    + authorised adjustment
    + claims
    + fulfilment/movement consequences
        ↓
validated resulting stock truth
```

This preserves the Inventory authority established by MS-PROT-058.

---

# 20. Material commercial state must be revalidated

Where an Offering price, terms, availability or other material commercial fact changes after a projection was rendered but before commitment, the committing operation shall use the applicable authoritative commercial semantics.

Main Street shall not silently treat stale projected terms as current authority unless an accepted hold/quote/historical-price semantic explicitly preserves those terms.

---

# 21. Atomic multi-capability invariants remain transactional where required

Channel convergence does not remove transactional requirements.

Where an operation requires multiple authoritative effects to succeed atomically, they shall execute within the appropriate consistency boundary.

Example:

```text
Order commitment
    + required Inventory claim
        ↓
atomic invariant where required
```

Post-commit reactions such as notifications and analytics may remain event-driven where appropriate.

This preserves Main Street's composite architecture: transactional core where invariants require atomicity; selective event-driven reactions after commit.

---

# 22. Offline authoritative mutation is not implied

The unified-channel model does not imply that every channel can mutate authoritative state while disconnected.

For concurrency-sensitive operations such as booking or stock allocation, Main Street shall not represent an offline action as successfully authoritative unless a separately accepted offline consistency/reservation architecture exists.

Cached read projections may be supported where safe.

Authoritative offline mutation is future design scope unless explicitly accepted.

---

# 23. Integration/provider correlation

External providers and integrations may retry requests or callbacks.

Provider communication shall correlate external delivery with existing Main Street operation/process identity where applicable.

A provider callback shall not automatically become a fresh business commitment merely because it arrived as a new message.

Conceptually:

```text
Main Street operation/process
        ↓
provider request
        ↓
provider correlation reference
        ↓
callback/retry
        ↓
reconcile with existing operation/process
```

Provider-boundary idempotency and reconciliation remain subject to capability/provider contracts.

---

# 24. AI is another interaction channel, not another business system

The merchant AI concierge and specialist agents may infer merchant intent and propose registered operations.

Example:

```text
"Book Sarah for an MOT tomorrow at 2pm."
        ↓
AI inference
        ↓
CreateAppointment candidate
        ↓
manual approval where required
        ↓
same application operation
```

AI shall not create:

```text
AIBooking
AIOrder
AIInventory
```

as parallel domain authorities.

AI memory also shall not be relied upon as the authoritative duplicate-prevention mechanism.

Final execution remains subject to deterministic operation authority, idempotency and capability invariants.

---

# 25. Provenance

Where operationally useful, Main Street may preserve interaction origin as provenance.

Potential registered origins may include concepts equivalent to:

```text
CUSTOMER_SELF_SERVICE
MERCHANT_DASHBOARD
MERCHANT_PHONE_ASSISTED
MERCHANT_WALK_IN
POS
AI_ASSISTED_MERCHANT
INTEGRATION
SYSTEM
```

This list is illustrative rather than a frozen universal enum.

The semantic requirement is attributable origin/context where materially useful.

Origin does not alter the fundamental business identity of the resulting object.

---

# 26. Graph model

The accepted graph is:

```text
                      AUTHORITATIVE MODEL
             Offering / Product / Inventory /
             Scheduling / Policies / Customers
                          │
          ┌───────────────┴────────────────┐
          │                                │
          ▼                                ▼
      PROJECTIONS                    OPERATIONS
          │                                ▲
    ┌─────┼──────┐                  ┌──────┼────────┐
    ▼     ▼      ▼                  ▼      ▼        ▼
 Website Dash   AI               Website  Phone   Walk-in
                                      │      │        │
                                      └──────┼────────┘
                                             ▼
                                      shared operation
                                             │
                                     idempotency
                                     authority
                                     requirements
                                     revalidation
                                             │
                                             ▼
                                      capability owner
                                             │
                                             ▼
                                      authoritative state
```

---

# 27. Failure taxonomy

Main Street shall distinguish failures rather than returning one generic duplicate error.

Conceptually:

```text
RETRY_OF_COMPLETED_LOGICAL_OPERATION
    → reconcile/return prior result

AUTHORITATIVE_CONFLICT
    → reject/retry according to capability semantics

POSSIBLE_HUMAN_DUPLICATE
    → advisory warning where useful

IDENTITY_AMBIGUITY
    → preserve separation / use reconciliation workflow

STALE_PROJECTION
    → re-evaluate against current authoritative state

UNSUPPORTED_OFFLINE_MUTATION
    → do not represent as committed
```

Physical error names remain implementation scope.

---

# 28. Falsification record

The model was tested against:

- simultaneous online and phone booking;
- same web request retry after timeout;
- intentionally repeated customer bookings;
- customer booking online then phoning because confirmation is uncertain;
- two customers with similar identity evidence;
- simultaneous online and walk-in inventory consumption;
- merchant stock correction during ordering;
- Offering changes while customer holds a stale page;
- offline merchant operation;
- provider callbacks and retries;
- repeated AI instruction;
- channel-specific operation availability;
- channel-specific unresolved information;
- channel-specific permissions;
- walk-in transactions without unnecessary customer identity;
- presentation-specific labels;
- intentionally repeated operations against one target; and
- partial failure across atomic participants.

The model rejected:

```text
parallel online/manual business models
channel-specific copies of Offering/Product/Inventory
channel-specific Appointment/Order domain types
projection state as mutation authority
one universal duplicate detector
same operation+target as universal idempotency identity
same customer+service+date as universal duplicate rule
AI memory as duplicate authority
customer identity merging as duplicate prevention
raw inventory quantity overwrite
implicit authoritative offline mutation
provider callback = new business commitment
```

---

# 29. Validation matrix

| Constraint | Result |
|---|---|
| One authoritative merchant operational model | PASS |
| Merchant business facts authored at owning authority once | PASS |
| Multiple projections without duplicate truth | PASS |
| Presentation variation remains possible | PASS |
| Multiple channels converge on capability operations | PASS |
| Channel provenance does not fragment domain types | PASS |
| Channel-specific authority remains possible | PASS |
| Context-specific requirement collection remains possible | PASS |
| Online/manual booking race protected by authoritative revalidation | PASS |
| Online/walk-in inventory race protected by Inventory authority | PASS |
| Technical retries handled separately through idempotency | PASS |
| Legitimate repeated operations remain possible | PASS |
| Weak duplicate evidence remains advisory | PASS |
| Customer identity reconciliation remains separate | PASS |
| Stale projections cannot authorise mutation | PASS |
| Atomic cross-capability invariants preserved | PASS |
| Provider retries/callbacks can correlate to existing operations | PASS |
| AI remains inference/input channel rather than authority | PASS |
| Unsupported offline mutation not falsely represented as committed | PASS |
| Composite architecture/programming model preserved | PASS |

---

# 30. Prohibited designs

The following are rejected unless a future separately accepted design demonstrates materially different semantics:

- separate online and manual business models;
- duplicated Offering definitions per channel;
- duplicated Inventory truth per interface for the same stock scope;
- `OnlineAppointment`, `PhoneAppointment`, `WalkInAppointment` or equivalent channel-defined domain subclasses;
- treating displayed availability/stock as commit authority;
- universal duplicate detection based on superficial field equality;
- using customer identity merge to solve repeated-operation problems;
- using operation type and target alone as idempotency identity;
- raw destructive stock overwrites as the inventory operation model;
- AI-specific copies of capability operations;
- AI memory as authoritative idempotency/duplicate protection;
- assuming provider callbacks are always new business commands; and
- representing disconnected concurrency-sensitive mutations as successfully committed without accepted offline consistency semantics.

---

# 31. Accepted result

The accepted Main Street interaction model is:

```text
ENTER BUSINESS TRUTH ONCE
        ↓
PROJECT IT WHERE LEGITIMATELY REQUIRED
        +
ACCEPT INTENT FROM MANY CHANNELS
        ↓
CONVERGE ON ONE CAPABILITY-OWNED OPERATION
        ↓
ESTABLISH PRINCIPAL + CONTEXT
        ↓
RESOLVE REQUIREMENTS
        ↓
APPLY IDEMPOTENCY WHERE SAME LOGICAL REQUEST
        ↓
AUTHORITATIVELY REVALIDATE INVARIANTS
        ↓
EXECUTE ONCE WITHIN REQUIRED CONSISTENCY BOUNDARY
        ↓
RECORD RESULT + PROVENANCE
        ↓
REFRESH/REBUILD PROJECTIONS
```

> **Main Street shall obtain multi-channel flexibility through shared semantics, shared operations and multiple projections—not through duplicated online/manual systems. Duplicate safety shall be semantic: idempotency for repeated delivery, transactional invariants for conflicting commitments, advisory assistance for weak human duplication, and separate governed reconciliation for identity ambiguity.**
