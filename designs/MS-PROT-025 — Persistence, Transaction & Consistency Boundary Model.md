# MS-PROT-025 — Persistence, Transaction & Consistency Boundary Model

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020 → MS-PROT-024  
**Purpose:** Define what Main Street must persist, what must remain atomically consistent, what may be eventually consistent, and how persistence preserves semantic ownership without committing to a specific database or messaging technology.

---

## 1. Governing principle

> **Persistence follows semantic authority, while consistency boundaries follow the invariant being protected.**

Therefore:

```text
Semantic owner
    ↓
authoritative persisted state
```

but:

```text
transaction boundary
    ≠
capability boundary
```

A transaction may cross capability-owned state where one invariant genuinely requires it.

---

# 2. Persistence categories

Main Street distinguishes:

### Authoritative operational state

Current business truth controlled by Main Street.

Examples:

```text
Order state
Booking state
Allocation
Appointment
Resource state
```

### Domain facts/events

Meaningful facts that occurred.

Example:

```text
OrderConfirmed
AppointmentCancelled
PaymentRecorded
```

### Audit evidence

Evidence about execution, access, configuration change, rejected attempts, or administrative activity.

### Coordination state

Durable state required by long-running processes.

### Derived projections

Read-oriented representations such as:

```text
search
analytics
customer history
dashboard summaries
tracking views
```

These categories may share infrastructure but are not semantically interchangeable.

---

# 3. Ownership

A capability or platform subsystem owns the authoritative state corresponding to the semantics it governs.

For example:

```text
Ordering → Order
Inventory → Inventory/Allocation
Scheduling → Appointment
```

A shared physical database does not imply shared ownership.

Therefore:

> **Physical co-location never grants another capability unrestricted write authority.**

Capabilities may reference other owned data through published contracts.

---

# 4. Transaction boundaries

A transaction protects a specific invariant.

Example:

```text
ConfirmOrder
    +
required InventoryAllocation
```

If an accepted order is invalid without the allocation, both changes may require one authoritative consistency boundary.

But:

```text
OrderConfirmed
    +
SendEmail
```

normally does not.

Therefore:

> **Only state changes that must succeed or fail together to preserve an invariant belong in the same atomic consistency boundary.**

---

# 5. Cross-capability atomicity

Cross-capability atomic updates are permitted when required by a genuine invariant.

They must be:

```text
explicit
narrow
semantically justified
```

They must not become:

```text
one transaction around everything
```

Capability ownership remains intact even where a transaction spans multiple owned states.

---

# 6. External systems

Main Street cannot guarantee atomic transactions with independent external authorities such as:

```text
payment provider
courier
calendar provider
social platform
```

Therefore:

```text
local Main Street atomicity
    ≠
distributed atomicity
```

External consistency requires durable interaction and reconciliation.

Example:

```text
Main Street requests payment
        ↓
provider owns payment result
        ↓
provider fact received
        ↓
Main Street reconciles local state
```

---

# 7. Consistency categories

Main Street uses four architectural coordination categories.

### Atomic authoritative consistency

Required where Main Street-owned changes must appear together.

### Durable asynchronous consistency

Main Street commits authoritative state, then reliably propagates dependent work.

Example:

```text
OrderConfirmed → Notification
```

### External reconciliation

Used where another system owns part of the truth.

Example:

```text
Payment provider ↔ Booking
```

### Projection eventual consistency

Used for read models that may safely lag.

Example:

```text
analytics
search
customer history
dashboard summaries
```

These are architectural categories, not an exhaustive theory of consistency models.

---

# 8. Events and commit ordering

A Domain Event representing committed Main Street state must not become externally observable before the corresponding authoritative mutation commits.

Therefore:

```text
authoritative state change
        +
durable publication intent
        ↓
commit
        ↓
event publication/retry
```

The exact implementation is deferred, but this requires an **outbox-equivalent guarantee**.

---

# 9. Event delivery

Main Street does not assume exactly-once delivery.

Consumers must tolerate, where relevant:

```text
duplicate delivery
retry
late delivery
out-of-order delivery
```

Duplicate-sensitive effects require:

```text
idempotency
deduplication
authoritative state checks
or another semantically appropriate mechanism
```

No universal global event ordering is assumed.

---

# 10. Current state and history

Current state alone is not always sufficient.

For example:

```text
CONFIRMED → CANCELLED
```

may require proof that confirmation occurred before cancellation.

Main Street must retain whatever history is required for:

```text
audit
financial evidence
reconciliation
customer history
legal obligations
business evidence
```

However:

> **Required history does not imply Event Sourcing.**

Event Sourcing remains an undecided implementation architecture.

---

# 11. DomainEvent is not AuditRecord

A DomainEvent represents meaningful business fact.

Audit may also need:

```text
failed attempts
actor identity
configuration changes
administrative access
security-relevant actions
before/after evidence
```

Therefore:

```text
DomainEvent ≠ AuditRecord
```

They may reference the same execution but remain separate concerns.

---

# 12. Merchant configuration persistence

The authoritative source remains:

```text
MerchantConfiguration
```

The:

```text
ResolvedOperationalModel
```

is derived.

It may be persisted or cached for efficiency, but cannot replace the configuration as the authoritative source.

Configuration changes must produce coherent revisions rather than mutating an active model piecemeal.

Runtime executes against one coherent active model context.

---

# 13. Runtime state vs resolved model

The resolved model contains relatively stable effective semantics such as:

```text
active capabilities
policy selections
operation applicability
resource definitions
schedule rules
```

It must not contain dynamic truth such as:

```text
current allocation
current payment status
current order state
current availability
```

Therefore:

```text
ResolvedOperationalModel
    ≠
runtime database
```

---

# 14. Availability and persistence

Availability is a contextual decision, not a universally stored Boolean.

A cached or persisted availability result cannot authorise later Allocation.

Example:

```text
09:00 available
09:01 another allocation occurs
09:02 old result reused
```

must not permit double allocation.

Therefore:

> **Concurrency-sensitive mutations must re-establish validity against authoritative state.**

---

# 15. Concurrency

Where an invariant can be violated by simultaneous execution, persistence must enforce it authoritatively.

Examples:

```text
two customers booking one slot
two bookings allocating one room
orders exceeding stock
two jobs allocating same technician interval
```

MS-PROT-025 requires concurrency protection but does not prescribe one universal mechanism.

Possible later mechanisms include:

```text
unique constraint
optimistic concurrency
locking
serialisation
capacity ledger
compare-and-set
```

Mechanism follows invariant.

---

# 16. Long-running coordination persistence

Where a process must survive restart, its required coordination state must be durable.

Potential information includes:

```text
process identity
correlation
waiting condition
deadline
retry state
deduplication state
```

This state does not replace capability-owned business truth.

Where coordinator state and local business mutation must move atomically, they may share a consistency boundary.

Where that is impossible or unnecessary, recovery must rely on durable facts, idempotency, and authoritative reinspection.

---

# 17. Caches and projections

Caches, search indexes, dashboards, and customer tracking projections may improve performance or usability.

They are not authoritative merely because they are easier to read.

Thus:

```text
Search says "1 room available"
```

does not authorise:

```text
AllocateRoom
```

unless the search/index is explicitly designed as the authoritative source for that invariant.

Default rule:

> **Derived reads may guide interaction; authoritative writes consult authoritative state.**

---

# 18. Shared data

Using the same data does not mean every capability owns it.

Example:

```text
CustomerContact
```

may be used by:

```text
Order
Booking
Enquiry
Quote
```

We distinguish:

```text
authoritative current data
```

from:

```text
historical/contextual snapshot
```

An order may legitimately retain the delivery/contact details used at purchase time even if the customer's current details later change.

Ownership is therefore semantic, not merely a database-normalisation question.

---

# 19. Scope and tenancy

Persisted state must carry the correct semantic ownership scope.

Possible scopes include:

```text
merchant-scoped
platform-scoped
other explicitly defined scope
```

Not every object must belong to a merchant.

But merchant-scoped state must never accidentally participate in another merchant's operation.

Therefore merchant isolation is an authoritative persistence invariant.

---

# 20. Time

Where both matter, Main Street must distinguish:

```text
recorded_at
```

from:

```text
effective_at
```

Example:

```text
work happened yesterday
but was recorded today
```

Platform-observed execution/audit time should not be replaced blindly by client-supplied timestamps.

---

# 21. Retention and deletion

No universal hard-delete rule is accepted.

We must distinguish:

```text
business cancellation
operational deletion
archival
retention expiry
privacy erasure/anonymisation
```

The detailed retention/privacy lifecycle belongs to a later specification.

---

# 22. Local development independence

Persistence implementation must support ordinary local development and testing from the checked-out Main Street project.

Local testing may use:

```text
local database
containerised database
embedded/test persistence
fixtures
```

but must not require GitHub fetch/pull to obtain Main Street-owned schemas, definitions, or test configuration.

GitHub remains source control, not runtime infrastructure.

---

# 23. Falsification findings

The following assumptions were tested and **discarded**:

| Failed assumption | Why it fails |
|---|---|
| Every command and all consequences belong in one transaction | Notifications/external effects do not |
| One capability equals one transaction boundary | Some invariants span capabilities |
| Cross-capability atomicity should never occur | Order + required allocation can require it |
| Local DB transaction can include external payment | External authority cannot participate |
| Every capability stores every data item it uses | Produces duplicated competing authority |
| Current state is enough | Historical evidence can matter |
| DomainEvent can double as audit log | Audit includes denied/admin/security actions |
| Events can be published before state commit | Consumers could observe nonexistent state |
| Exactly-once delivery can be assumed | Retries/duplicates occur |
| Event arrival order equals business order | Delivery can reorder |
| One global ordered event stream is required | Most semantics need only local ordering |
| ResolvedOperationalModel may replace MerchantConfiguration | Derived state would replace source |
| Cache/search may authorise writes | Projections can be stale |
| Shared database grants cross-module write access | Destroys semantic ownership |
| Every object is merchant-scoped | Platform-owned state exists |
| All deletion should be physical deletion | Audit, legal, privacy requirements differ |
| One concurrency strategy works universally | Different invariants need different mechanisms |

The surviving model excludes these assumptions.

---

# 24. Cross-domain validation

### Gardener
Quote, job, schedule and customer contact can be authoritative state; notifications and portfolio projections may lag. No special persistence model required. **PASS**

### Driving instructor
Lesson purchases and appointments persist independently; one instructor slot cannot be double-booked. Student dashboard may be a projection. **PASS**

### Solicitor
Appointment is authoritative; email/ICS delivery may be asynchronous. Customer portal is not required. **PASS**

### Restaurant
Order confirmation and hard inventory allocation may share a transaction; notification and tracking projections may be asynchronous. **PASS**

### Grocery
Concurrent quantity allocation must prevent overcommitment despite stale availability reads. **PASS**

### Motel
Two customers may both see availability, but authoritative allocation permits only valid final assignment. External payment may require reconciliation. **PASS**

### Mechanic
Technician/time allocation requires authoritative scheduling consistency; vehicle/customer data can remain contextual. **PASS**

### Realtor
Simple enquiry needs durable creation only; viewing scheduling additionally requires capacity consistency. No artificial transaction complexity is imposed on enquiry. **PASS**

---

# 25. Accepted invariants

1. Persistence authority follows semantic ownership.
2. Physical storage location does not establish semantic ownership.
3. Transaction boundaries follow invariants.
4. Narrow cross-capability atomicity is permitted where necessary.
5. Main Street does not assume distributed atomicity with external systems.
6. External consistency requires durable reconciliation.
7. Business transaction and database transaction are distinct.
8. Current state and required history are distinct concerns.
9. DomainEvent and AuditRecord are distinct.
10. Committed events must correspond to committed authoritative state.
11. Event delivery may duplicate or reorder.
12. No universal global event ordering is required.
13. MerchantConfiguration remains authoritative over its derived operational model.
14. Active resolved models remain coherent.
15. Dynamic operational state does not belong in the resolved model.
16. Stale Availability cannot authorise Allocation.
17. Concurrency-sensitive invariants require authoritative enforcement.
18. Durable long-running coordination must survive restart.
19. Coordination state does not replace business state.
20. Caches/projections are non-authoritative unless explicitly designed otherwise.
21. Shared data requires explicit semantic ownership.
22. Merchant isolation is an authoritative persistence invariant where merchant scope applies.
23. Not every persisted object is merchant-scoped.
24. Recorded time and business-effective time remain distinct where required.
25. No universal persistence, locking, deletion, or event-sourcing strategy is imposed.
26. Ordinary local persistence/testing must not depend on GitHub.

---

# 26. Deferred decisions

MS-PROT-025 does **not** select:

```text
PostgreSQL
JPA/Hibernate
JDBC
Redis
Kafka
RabbitMQ
event store
CQRS
Event Sourcing
outbox library
locking strategy
tenant schema strategy
workflow engine
```

Those technologies must satisfy the accepted persistence and consistency contracts rather than define them.

---

# 27. Governance verdict

```text
MS-PROT-025
Persistence, Transaction & Consistency Boundary Model

PROPOSE                   ✓
FALSIFICATION REVIEW      ✓
FAILED ASSUMPTIONS REMOVED ✓
REVISED                   ✓
CROSS-DOMAIN VALIDATION   ✓
ACCEPT                    ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street persists authoritative state according to semantic ownership and chooses consistency boundaries according to the invariant being protected. Main Street uses atomic local consistency where necessary, durable asynchronous propagation where sufficient, explicit reconciliation where external systems own part of the truth, and eventually consistent projections where lag cannot violate a business invariant.**

The next document should be **MS-PROT-026 — Domain Event & Event Propagation Model**, using the same concise propose → falsify → validate → accept discipline.