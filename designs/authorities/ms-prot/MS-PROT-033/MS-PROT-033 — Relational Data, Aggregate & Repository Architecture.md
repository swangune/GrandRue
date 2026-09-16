# MS-PROT-033 — Relational Data, Aggregate & Repository Architecture

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-025, MS-PROT-031, MS-PROT-032  
**Purpose:** Define how authoritative business state, aggregates, repositories, cross-capability references, transactions, projections, and merchant isolation map onto relational persistence without allowing the database model to redefine domain ownership.

---

## 1. Governing principle

> **The relational model persists domain authority; it does not become the domain model.**

Therefore:

```text
Domain / Capability ownership
          ↓
Repository contract
          ↓
Relational persistence
```

not:

```text
Database tables
      ↓
generated domain semantics
```

Tables, foreign keys, and ORM mappings must implement accepted invariants rather than determine them.

---

## 2. Relational database baseline

Main Street should initially use a **relational persistence model** for authoritative operational state.

This fits the system's dominant needs:

```text
transactions
merchant isolation
relationships
constraints
orders/bookings/jobs
scheduling
allocation
configuration
auditability
```

The exact database product remains deferred, although later technology selection should strongly evaluate PostgreSQL against these requirements.

---

## 3. Aggregate

An Aggregate is:

> **A consistency and mutation boundary around state that must preserve its own invariants together.**

It is not:

```text
every table
every capability
every object graph
```

Examples may include:

```text
Order
Appointment
Quote
Job
```

depending on their final domain models.

An aggregate should be no larger than required to preserve its invariants.

---

## 4. Aggregate is not automatically transaction boundary

Often:

```text
one aggregate mutation
    → one transaction
```

But MS-PROT-025 already established legitimate cross-capability consistency boundaries.

Therefore:

```text
Aggregate boundary
    ≠
universal transaction boundary
```

A use case may coordinate two independently owned aggregates in one local transaction where one invariant requires it.

---

## 5. Repository ownership

Each capability owns repositories for its authoritative aggregates/state.

Example:

```text
Ordering
    → OrderRepository

Scheduling
    → AppointmentRepository

Inventory
    → AllocationRepository
```

A repository represents a capability's persistence contract.

Another capability must not bypass that ownership by importing the repository implementation or directly modifying its tables.

---

## 6. Repository contracts are domain-oriented

Reject repository APIs that merely mirror SQL tables:

```text
saveRow()
updateColumn()
findAllRecords()
```

Prefer domain-oriented concepts:

```text
findOrder(scope, orderId)
save(order)
findAppointment(scope, appointmentId)
```

Exact method signatures are deferred.

The repository should preserve domain concepts rather than exposing persistence mechanics.

---

## 7. Merchant scope

Merchant-owned aggregate identity is conceptually:

```text
MerchantScope
    +
AggregateIdentifier
```

Therefore queries and uniqueness constraints must preserve merchant ownership where appropriate.

Example:

```text
merchant A / order-123
merchant B / order-123
```

may coexist.

Even if identifiers later become globally unique UUIDs, tenant scope remains an authority boundary.

---

## 8. Shared schema

Initial persistence remains:

> **Shared relational database and shared schema, with explicit ownership scope on merchant-owned records.**

This is operationally appropriate for many small merchants.

However, isolation cannot depend solely on application convention.

Persistence technology should support defence in depth where possible.

---

## 9. Table ownership

A capability owns its persistence tables conceptually.

For example:

```text
ordering.*
scheduling.*
inventory.*
```

This does not require separate physical SQL schemas initially.

The important rule is:

> **Only the owning module writes its authoritative tables except through explicitly designed consistency mechanisms.**

Direct SQL access from unrelated modules is prohibited.

---

## 10. Cross-capability references

A domain object may need to reference another capability's object.

Example:

```text
Order
    references customer relationship

Booking
    references appointment/resource

Allocation
    references business commitment
```

Prefer storing stable identifiers rather than embedding foreign capability objects directly.

Conceptually:

```text
Order {
    customerRelationshipId
}
```

not:

```text
Order {
    CustomerRelationship entire mutable object graph
}
```

This preserves ownership.

---

## 11. Foreign keys

Relational foreign keys are permitted where they strengthen integrity.

But:

> **A foreign key does not establish semantic ownership or permission to mutate the referenced data.**

They should also be used carefully across module boundaries because excessively interconnected schemas can make future extraction difficult.

Semantic dependency comes first; database constraint follows.

---

## 12. Cross-capability transaction coordination

Where one invariant requires atomic changes across owned states:

```text
Application coordinator
      ↓
Capability A
Capability B
      ↓
single local transaction
```

may be valid.

The coordinator uses capability contracts.

It must not perform:

```sql
UPDATE ordering...
UPDATE inventory...
```

directly merely because both tables are available.

---

## 12.1 Transactional fulfilment of executable effects

The Executable Semantic Model describes the bounded effects an Operation may
produce. It does not select repositories, issue generic writes or determine a
universal transaction shape.

A capability-owned application handler resolves authoritative state and uses
owned repositories or ports inside the consistency boundary required by the
operation's invariant. Final state, authority and concurrency validation occurs
inside that boundary rather than relying on an earlier applicability result.

For a local invariant spanning capabilities:

```text
Narrow application coordinator
      ↓
Capability-owned contract A
Capability-owned contract B
      ↓
One local transaction where required
```

The coordinator may coordinate the transaction but may not bypass either
capability's mutation contract. The committed effect and event result must be
checked against the captured executable Operation before it is accepted as a
successful execution result.

---

## 13. Persistence model vs domain model

One-to-one mapping between Java domain objects and tables is not required.

For example:

```text
Domain Aggregate
      ↓
multiple relational tables
```

may be appropriate.

Likewise a read projection may denormalise several domain concepts into one read table.

Therefore:

```text
Entity ≠ Table
Aggregate ≠ Table
Projection ≠ Aggregate
```

This must remain explicit.

---

## 14. ORM boundary

If an ORM is later selected, ORM annotations must not determine domain architecture.

Avoid allowing persistence concerns to force:

```text
public setters everywhere
bidirectional object graphs
lazy-loaded domain semantics
database-generated business behaviour
```

Core domain semantics should remain understandable independently of ORM behaviour.

This means persistence entities and domain objects may be separate where necessary.

---

## 15. Aggregate loading

Do not automatically load massive object graphs.

Repositories should load enough authoritative state to evaluate the intended aggregate operation.

A restaurant Order should not automatically load:

```text
entire customer history
all merchant inventory
all staff
all payments ever made
```

merely because relational links exist.

Bounded loading preserves performance and ownership.

---

## 16. Historical snapshots

Some business records require historical values even when referenced master data changes.

Example:

```text
Customer changes address today.

Order from last month must retain:
    delivery address used then.
```

Therefore transactional aggregates may persist contextual snapshots such as:

```text
delivery details
quoted price
tax amount
customer-facing name
```

where historical truth requires them.

This is not improper duplication.

It is business evidence.

---

## 17. Mutable shared data

Where data represents current shared truth, avoid uncontrolled duplication.

Example:

```text
current customer contact
current resource configuration
```

should have an authoritative owner.

Other modules either:

```text
reference it
query it
consume events/projections
```

or deliberately retain historical snapshots.

The distinction must be explicit.

---

## 18. Concurrency enforcement

Persistence must protect vulnerable invariants.

Examples:

```text
exclusive appointment slot
room allocation
inventory quantity
one active configuration
unique staff number within merchant
```

No single concurrency mechanism is mandated.

Database-supported constraints should be preferred where they directly encode an invariant reliably.

Examples may later include:

```text
unique constraints
transaction isolation
locking
version checks
exclusion constraints
conditional updates
```

---

## 19. Scheduling intervals

Scheduling creates relational complexity.

A simplistic unique key:

```text
(resource_id, start_time)
```

does not prevent overlapping intervals such as:

```text
10:00–11:00
10:30–11:30
```

Therefore scheduling persistence must eventually enforce interval conflicts according to Scheduling semantics, not assume exact timestamp equality is sufficient.

The mechanism is deferred.

---

## 20. Quantity allocation

Inventory/capacity persistence must not model all capacity as:

```text
available BOOLEAN
```

Quantity-based resources require authoritative accounting of:

```text
capacity
claims/allocations
release
consumption where applicable
```

The relational design must reflect the accepted MS-PROT-006 distinction rather than flattening capacity to binary status.

---

## 21. Read projections

Read-oriented tables/models may be deliberately denormalised.

Examples:

```text
merchant dashboard
customer order tracking
student history
public catalogue
search projection
```

These models may duplicate values from multiple capabilities.

They remain non-authoritative unless explicitly designated otherwise.

Projection tables may therefore be optimised for read use cases rather than aggregate design.

---

## 22. Queries must not mutate

A read repository/projection query must not produce hidden business side effects.

Reject patterns where:

```text
loadOrder()
```

silently:

```text
updates status
allocates capacity
writes audit business state
```

Queries observe.

Commands mutate.

Technical telemetry is separate.

---

## 23. Domain events and persistence

When an authoritative transaction produces Domain Events:

```text
business state
+
durable publication intent
```

must commit consistently as established by MS-PROT-025/026.

The event publication mechanism must not require the domain aggregate itself to know messaging infrastructure.

---

## 24. Audit persistence

Audit records may reference:

```text
merchant scope
actor
command/execution
target
time
result
```

where required.

Audit tables are not substitutes for capability-owned state or Domain Events.

Their retention and privacy policies are separate concerns.

---

## 25. Soft deletion

Do not make `deleted = true` a universal pattern.

Different concepts may require:

```text
cancel
archive
deactivate
anonymise
delete
retain
```

according to semantics.

A StaffRelationship becoming inactive is different from deleting historical Order evidence.

Deletion behaviour belongs to each semantic lifecycle plus future retention/privacy policy.

---

# 26. Falsification review

### Failed assumption: one aggregate per capability

Scheduling may own multiple independent appointments/resources; Inventory may own many allocations.

Mechanical one-aggregate-per-capability creates oversized consistency boundaries.

**Rejected.**

### Failed assumption: one table per aggregate

Normalised persistence may require several tables; projection models may do the opposite.

**Rejected.**

### Failed assumption: every cross-module reference should be a foreign-key object graph

This couples modules and encourages foreign writes.

**Rejected.**

### Failed assumption: never use foreign keys across modules

Some integrity constraints benefit from them and a physical monolith can use them responsibly.

Absolute prohibition is unnecessary.

**Rejected.**

### Failed assumption: ORM entity should be domain entity

ORM requirements can distort semantic design.

**Rejected as a universal rule.**

### Failed assumption: repositories should be globally shared

This destroys capability persistence ownership.

**Rejected.**

### Failed assumption: UUIDs solve tenant isolation

Global uniqueness does not establish authority.

**Rejected.**

### Failed assumption: read tables should always be normalised

Customer/public/dashboard projections benefit from deliberate denormalisation.

**Rejected.**

### Failed assumption: historical duplication is always bad

Historical transaction truth may require snapshots.

**Rejected.**

### Failed assumption: database constraints are implementation detail only

Some invariants are safest when reinforced at the authoritative persistence boundary.

**Rejected.**

---

# 27. Cross-domain validation

### Gardener

`Quote` can persist agreed price/details as historical evidence even if service catalogue changes later. Job and appointment remain separately owned aggregates where appropriate.

**PASS**

### Driving instructor

Lesson purchase and lesson appointment need not form one giant aggregate. Scheduling persistence must protect instructor interval conflicts.

**PASS**

### Solicitor

Appointment can remain a scheduling aggregate while client/matter information is referenced through owned contracts rather than embedded universally.

**PASS**

### Restaurant

Order and Inventory remain separate owned state while order acceptance may atomically create required stock allocation.

**PASS**

### Grocery

Quantity allocation requires concurrency-safe relational accounting rather than Boolean stock status.

**PASS**

### Motel

Room allocation requires interval/resource consistency; booking and room need not become one aggregate.

**PASS**

### Mechanic

Job may reference technician and vehicle/customer context without taking ownership of those complete mutable models.

**PASS**

### Realtor

Enquiry persistence remains simple, while Viewing scheduling adds interval constraints without contaminating every enquiry record.

**PASS**

---

# 28. Accepted invariants

1. Relational persistence implements domain semantics; it does not define them.
2. Aggregate is a consistency/mutation boundary, not a table or capability synonym.
3. Aggregates should be as small as their invariants permit.
4. Transaction boundaries and aggregate boundaries may differ.
5. Repositories are owned by the capability owning authoritative state.
6. Repository contracts are domain-oriented.
7. Merchant scope remains part of persistence authority.
8. Initial architecture uses shared-schema relational persistence.
9. Shared schema does not grant shared write ownership.
10. Cross-capability references favour stable identifiers/contracts over mutable object graphs.
11. Foreign keys may reinforce integrity but do not establish semantic ownership.
12. Cross-capability atomic operations use explicit application coordination.
13. Domain model and relational model need not map one-to-one.
14. ORM design cannot dictate domain semantics.
15. Aggregate loading must remain bounded.
16. Historical snapshots are valid where business truth requires them.
17. Current shared data has explicit authority.
18. Concurrency-sensitive invariants must be enforced authoritatively.
19. Scheduling interval conflicts cannot be reduced universally to timestamp uniqueness.
20. Quantity capacity cannot be reduced universally to Boolean availability.
21. Read projections may be denormalised.
22. Read models remain non-authoritative by default.
23. Queries do not perform hidden authoritative mutation.
24. Event publication remains transactionally coupled to authoritative state through a durable intent mechanism.
25. Audit, Domain Events and authoritative state remain separate concerns.
26. No universal soft-delete strategy is accepted.
27. Executable effects constrain authoritative outcomes but do not act as generic persistence instructions.
28. Capability-owned handlers perform final validation and mutation through owned repositories or ports.
29. Cross-capability local atomicity preserves capability contracts even when one coordinator controls the transaction.
30. A committed execution result must conform to the captured executable Operation's declared effects and event identities.

---

# 29. Deferred decisions

MS-PROT-033 does **not** yet select:

```text
PostgreSQL
JPA/Hibernate
Spring Data
JDBC
jOOQ
Flyway/Liquibase
physical SQL schemas
table naming
UUID/ULID identifier format
isolation levels
locking mechanism
projection database
row-level security
outbox schema
```

These technology decisions should now be evaluated against a substantially clearer persistence contract.

---

# 30. Governance verdict

```text
MS-PROT-033
Relational Data, Aggregate & Repository Architecture

PROPOSE                     ✓
FALSIFICATION REVIEW        ✓
FAILED ASSUMPTIONS REMOVED  ✓
CROSS-DOMAIN VALIDATION     ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street will persist authoritative business state relationally through capability-owned repositories and bounded aggregates. Aggregate, transaction and table boundaries remain distinct; cross-capability consistency is coordinated explicitly; merchant ownership is preserved throughout persistence; historical snapshots and denormalised projections are permitted where their semantics justify them; and database/ORM convenience may never redefine domain ownership or invariants.**

The next logical step is now sufficiently constrained to make a concrete infrastructure choice rather than another abstract model:

> **MS-PROT-034 — Database, Persistence Framework & Migration Technology Selection**

That document should falsify **PostgreSQL vs plausible alternatives**, and **JPA/Hibernate vs JDBC/jOOQ-style persistence**, specifically against tenant isolation, interval scheduling, quantity allocation, modular ownership, transaction control, local development, and maintainability.
