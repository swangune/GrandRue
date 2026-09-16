# MS-PROT-026 — Domain Event & Event Propagation Model

**Version:** 1.0  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020 → MS-PROT-025  
**Purpose:** Define how Main Street represents and propagates meaningful business facts without turning events into commands, shared mutable state, or a universal event-driven architecture.

---

## 1. Governing principle

> **A Domain Event is an immutable statement that an authoritative business fact has already occurred.**

Examples:

```text
OrderConfirmed
QuoteAccepted
AppointmentCancelled
JobCompleted
PaymentRecorded
```

An event reports history. It does not request future behaviour.

Therefore:

```text
Command      → "Confirm this order"
DomainEvent  → "This order was confirmed"
```

---

# 2. When an event exists

Not every state mutation requires a Domain Event.

Emit one when the fact has meaningful consequences outside the immediate execution boundary, such as:

```text
notification
coordination
integration
projection
audit correlation
another capability
```

An execution may therefore produce:

```text
0 events
1 event
multiple events
```

according to semantics.

Events must not be invented merely because something changed internally.

---

# 3. Event ownership

The semantic owner of the fact owns the event definition.

```text
Ordering   → OrderConfirmed
Scheduling → AppointmentCancelled
Quoting    → QuoteAccepted
```

Consumers do not define facts about another capability's state.

This prevents downstream modules from creating competing interpretations of authoritative history.

---

# 4. Events do not grant authority

A consumer receiving:

```text
OrderConfirmed
```

may react, but the event itself does not authorise arbitrary mutation.

If the reaction requires another authoritative business action:

```text
OrderConfirmed
      ↓
AllocateSomething
```

the receiving process must invoke the appropriate capability through its legitimate Command/Operation boundary.

Therefore:

> **Knowledge that something happened is not permission to perform another action.**

---

# 5. Internal events vs integration events

Not every internal Domain Event should become an external contract.

Main Street distinguishes:

### Domain Event

Internal expression of an authoritative business fact.

### Integration Event

Deliberately published contract intended for consumers outside the owning boundary or Main Street itself.

Conceptually:

```text
Domain Event
     ↓
explicit translation/publication
     ↓
Integration Event
```

They may sometimes contain similar information, but they are not automatically identical.

This protects internal semantic evolution from external coupling.

---

# 6. Event identity

A durable event must have stable identity sufficient for:

```text
deduplication
traceability
diagnostics
correlation
recovery
```

Conceptually:

```text
eventId
eventType
occurredAt
subject/reference
```

Additional metadata may include:

```text
merchant/scope
causation
correlation
actor/execution reference
schema version
```

Only semantically necessary metadata should be mandatory.

Exact envelope structure is deferred.

---

# 7. Causation and correlation

These concepts remain distinct.

```text
Causation:
What directly caused this fact?

Correlation:
Which wider interaction/process does it belong to?
```

Example:

```text
RequestBooking Command
        ↓ causation
BookingRequested
        │
        └──── correlation: booking-process-123
```

Later:

```text
PaymentSucceeded
AllocationCompleted
BookingConfirmed
```

may share correlation without sharing immediate causation.

---

# 8. Merchant and authority scope

Events must retain sufficient scope to prevent:

```text
Merchant A event
        ↓
Merchant B consumer/state
```

Where the underlying fact is merchant-scoped, event propagation must preserve that scope.

Platform-scoped facts remain possible.

Therefore event scope follows the authority scope of the underlying fact rather than assuming every event belongs to a merchant.

---

# 9. Commit and publication

MS-PROT-025 established:

> An event representing committed state must not become observable as committed before that state commits.

Therefore:

```text
authoritative mutation
       +
durable event publication intent
       ↓
COMMIT
       ↓
publication
```

If publication fails after commit:

```text
retry publication
```

rather than undoing valid business state merely because a consumer was temporarily unavailable.

The required guarantee is architectural; an outbox-equivalent implementation is likely but not mandated.

---

# 10. Delivery semantics

Main Street does **not** assume exactly-once delivery.

Consumers must tolerate:

```text
duplicates
retries
delays
out-of-order delivery
```

where relevant.

Duplicate-sensitive consumers must use an appropriate strategy such as:

```text
event identity
idempotency
deduplication
authoritative state inspection
```

---

# 11. Ordering

No global ordering across all Main Street events is required.

For example, there is no useful ordering relationship between:

```text
Merchant A: OrderConfirmed

and

Merchant B: AppointmentCancelled
```

Where ordering matters, it must be defined at the smallest meaningful semantic scope.

Possible scopes include:

```text
resource instance
business interaction
process
capability-owned stream
```

Arrival order alone must never silently become business order.

---

# 12. Event payloads

An event should contain enough information to express the fact and allow intended consumers to identify its subject.

It should not become:

```text
complete database snapshot
arbitrary object graph
shared mutable entity
```

Consumers needing current authoritative state should query the appropriate capability rather than assuming an old event payload remains current forever.

---

# 13. Privacy and disclosure

Events can propagate data beyond their original execution boundary.

Therefore:

> **An event carries only information justified by its intended semantic purpose and audience.**

Example:

A notification subsystem may need:

```text
appointment reference
notification destination reference
customer-facing status
```

but not necessarily:

```text
internal merchant notes
unrelated customer history
staff-only state
```

Internal events must not automatically become customer-visible or externally published.

This directly supports Main Street's configurable visibility model.

---

# 14. Internal vs customer-visible state

A business may have:

```text
inspection_required
quoted
accepted
scheduled
in_progress
completed
```

while customers may see only:

```text
Request received
Appointment scheduled
Work completed
```

Events do not dictate presentation.

Instead:

```text
Authoritative Domain Event
          ↓
consumer / projection policy
          ↓
merchant-facing representation
or
customer-facing representation
or
notification
or
no external representation
```

Therefore:

> **Domain semantics, visibility, and communication are separate concerns.**

This allows restaurants to expose detailed order progress while a solicitor keeps most operational states internal.

---

# 15. Events and notifications

A Domain Event is not itself a notification.

```text
AppointmentConfirmed
        ↓
Notification capability
        ↓
Email / SMS / chat / push / ICS
```

The same event might generate:

```text
customer email
merchant alert
no notification
```

depending on resolved merchant/business configuration.

This prevents notification requirements from contaminating domain state machines.

---

# 16. Events and customer tracking

Customer tracking is a projection of permitted business facts.

For example:

```text
OrderAccepted
PreparingStarted
OrderReady
OrderCompleted
        ↓
Customer Order Tracking
```

But:

```text
FraudReviewStarted
InternalEscalationRaised
StaffAssignmentChanged
```

may remain internal.

Customer accounts are not required for this architecture.

Tracking may be exposed through an appropriate secure interaction/reference mechanism.

Authentication and access design are deferred.

---

# 17. Events and long-running processes

A coordinator may consume events to progress a process:

```text
PaymentSucceeded
        ↓
Booking Coordinator
        ↓
RequestAllocation
```

But the coordinator must:

- correlate the event correctly;
- tolerate duplicates;
- revalidate authoritative conditions where necessary;
- use authorised Commands for subsequent mutations.

Events therefore connect MS-PROT-024 coordination with capability-owned state without transferring ownership.

---

# 18. Schema evolution

Persisted or externally consumed events may outlive the code version that produced them.

Therefore event contracts must support controlled evolution.

Breaking an existing event interpretation silently is unacceptable.

Possible later mechanisms include:

```text
schema version
additive evolution
translation
consumer migration
new event type
```

No specific versioning mechanism is selected yet.

---

# 19. Event failure

Failure of one consumer must not automatically invalidate the authoritative fact.

Example:

```text
OrderConfirmed ✓
Email consumer fails ✗
```

The order remains confirmed.

The failed consequence may:

```text
retry
fail independently
require intervention
```

according to its semantics.

If a consequence must succeed atomically with the original action, it was incorrectly modelled as an independent asynchronous consumer.

---

# 20. Events are not a universal integration mechanism

Not every collaboration must use events.

Main Street may use:

```text
direct capability invocation
query
transactional coordination
Command
Domain Event
Integration Event
external callback
```

depending on semantics.

Therefore:

> **Main Street is not adopting "everything must be event-driven" as an architectural rule.**

---

# 21. Falsification findings

| Failed assumption | Why it fails |
|---|---|
| Every state change emits an event | Creates meaningless event noise |
| Event = Command | Confuses historical fact with requested action |
| Receiving an event grants authority | Bypasses authorisation |
| Domain Event = Integration Event | Couples external consumers to internals |
| Publish before commit | Consumers may observe nonexistent facts |
| Exactly-once delivery can be assumed | Retries and duplicates occur |
| Arrival order equals business order | Delivery may reorder |
| One global event ordering is required | Unrelated facts have no meaningful order |
| Events should contain complete entities | Creates coupling, leakage and stale snapshots |
| All events may be customer-visible | Exposes internal operational state |
| Domain Event = Notification | Couples business semantics to communication channels |
| Domain Event = AuditRecord | Their purposes differ |
| Consumer failure invalidates original fact | Independent consequences may fail independently |
| Everything should communicate through events | Adds unnecessary asynchronous complexity |
| Event schemas never change | Persisted/external contracts outlive code |

All are rejected.

---

# 22. Cross-domain validation

**Gardener:** `QuoteAccepted` may schedule follow-up while internal inspection events remain merchant-only. Customer account unnecessary. **PASS**

**Driving instructor:** `LessonScheduled` can update student history/calendar projection and trigger notification. **PASS**

**Solicitor:** `AppointmentConfirmed` can produce email + ICS without exposing internal matter progression. **PASS**

**Restaurant:** preparation events can feed customer order tracking and notifications. **PASS**

**Grocery:** `OrderConfirmed` can trigger downstream processing without stale events authorising inventory allocation. **PASS**

**Motel:** payment/allocation events can coordinate booking while authoritative allocation remains capability-owned. **PASS**

**Mechanic:** internal diagnostic/work states can remain merchant-only while selected milestones notify customers. **PASS**

**Realtor:** viewing events can drive reminders without requiring every enquiry transition to become customer-visible. **PASS**

---

# 23. Accepted invariants

1. A Domain Event states an authoritative fact that already occurred.
2. Events and Commands are semantically distinct.
3. Not every state mutation requires an event.
4. The semantic owner of a fact owns its event definition.
5. Event receipt does not grant mutation authority.
6. Domain Events and Integration Events are distinct contracts.
7. Durable events require stable identity.
8. Correlation and causation are distinct.
9. Event propagation preserves the authority scope of the underlying fact.
10. Events representing committed state cannot be published as committed before state commit.
11. Publication must recover from post-commit delivery failure.
12. Exactly-once delivery is not assumed.
13. Duplicate-sensitive consumers require protection.
14. Global event ordering is not required.
15. Ordering scope must be no broader than semantics require.
16. Event payloads must remain purpose-limited.
17. Events must not leak data merely because downstream consumers exist.
18. Domain state, customer visibility and notification policy are separate.
19. A Domain Event is not a notification.
20. Customer tracking is a controlled projection, not authoritative state.
21. Coordinators may observe events but mutate state through authorised Commands.
22. Event contracts require controlled evolution.
23. Independent consumer failure does not automatically invalidate committed business facts.
24. Domain Events and AuditRecords remain distinct.
25. Main Street does not require all collaboration to be event-driven.

---

# 24. Deferred decisions

MS-PROT-026 does **not** choose:

```text
Kafka / RabbitMQ / other broker
event store
serialization format
event envelope implementation
schema registry
outbox technology
partitioning strategy
topic structure
consumer framework
retention period
customer tracking authentication mechanism
```

These must implement the accepted semantics rather than define them.

---

# 25. Governance verdict

```text
MS-PROT-026
Domain Event & Event Propagation Model

PROPOSE                    ✓
FALSIFICATION REVIEW       ✓
FAILED ASSUMPTIONS REMOVED ✓
CROSS-DOMAIN VALIDATION    ✓
ACCEPT                     ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street uses Domain Events as immutable records of meaningful authoritative facts, published only after those facts are durably established. Events propagate knowledge, not authority. Their delivery may duplicate, delay or reorder; their visibility is controlled independently from domain state; and events are introduced only where asynchronous propagation provides genuine architectural value.**

The next dependency is **MS-PROT-027 — Read Model, Projection & Customer/Merchant Visibility Model**. It should formalise the distinction we have now repeatedly encountered between **authoritative operational state**, **merchant-facing operational views**, **customer tracking**, **customer history**, and **public website presentation**.