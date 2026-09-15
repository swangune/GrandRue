# MS-PROT-032 — Backend Module, Bounded Context & Dependency Architecture

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020 → MS-PROT-031  
**Purpose:** Define how the Spring Boot modular monolith is divided internally so that capabilities remain independently owned, application surfaces remain isolated, and the codebase does not decay into a tightly coupled monolith.

---

## 1. Governing principle

> **Backend modules are organised around semantic ownership and business capability, not technical layers alone. Dependencies point inward through explicit contracts.**

The backend should look conceptually like:

```text
mainstreet
│
├── platform
├── semantic
├── configuration
├── runtime
├── capabilities
│   ├── ordering
│   ├── scheduling
│   ├── inventory
│   ├── quotation
│   ├── notification
│   └── ...
│
├── merchant
├── customer
├── publicsite
└── infrastructure
```

This is conceptual organisation, not yet the final Java package tree.

---

# 2. Bounded context vs capability

These must not be treated as synonyms.

A **Capability** is:

> a coherent business ability Main Street supports.

A **Bounded Context/Module** is:

> an implementation and semantic ownership boundary.

Sometimes:

```text
1 Capability
    =
1 Module
```

but not always.

Several closely related capabilities may legitimately belong to one bounded context if they share one semantic model and invariants.

Conversely, one broad capability may eventually require internal modules.

Therefore:

> **Module boundaries follow semantic cohesion, not a mechanical one-capability-one-package rule.**

---

# 3. Core backend layers

Within a bounded context, use a small consistent structure:

```text
capability
│
├── domain
├── application
├── ports
└── infrastructure
```

### Domain

Owns:

```text
entities/value concepts
operations
invariants
domain services where necessary
domain events
```

### Application

Owns:

```text
use cases
command handling
query coordination
transaction boundaries
cross-capability orchestration
```

### Ports

Defines required contracts for:

```text
persistence
external services
other capability interfaces
event publication
```

### Infrastructure

Implements those contracts using:

```text
Spring
database
HTTP clients
messaging
external providers
```

Not every tiny module needs every folder. Structure should follow actual complexity.

---

# 4. Shared semantic kernel

The existing generic concepts:

```text
Resource
State
Transition
Operation
Privilege
```

belong to a deliberately small shared semantic kernel.

Future generic concepts may include:

```text
Command
Execution
Requirement
Policy
Allocation
AvailabilityDecision
```

only where cross-domain evidence proves they are truly generic.

The shared kernel must remain difficult to grow.

> **A concept does not enter the shared kernel merely because two modules currently use something similar.**

This prevents the shared model from becoming a universal dumping ground.

---

# 5. Platform modules

Some concerns are not merchant business capabilities.

Examples:

```text
identity/access
tenant scope
configuration compiler
audit
platform administration
website delivery infrastructure
```

These belong to platform modules.

They must not be forced into artificial business capability models.

For example:

```text
tenant isolation
```

is not a restaurant or scheduling capability.

---

# 6. Delivery modules

MS-PROT-029 established three application surfaces:

```text
merchant
customer
public
```

The backend should therefore expose separate delivery modules conceptually:

```text
delivery
├── merchant
├── customer
└── public
```

They may contain:

```text
controllers
request/response mapping
transport authentication integration
surface-specific validation
```

They must not contain authoritative business rules.

---

# 7. Dependency direction

The core rule is:

```text
Delivery
   ↓
Application
   ↓
Domain / Capability contracts
```

Infrastructure points inward by implementing ports:

```text
Infrastructure
      ↓ implements
Ports
```

Invalid dependency:

```text
Ordering domain
      ↓
RestaurantController
```

Invalid dependency:

```text
Scheduling domain
      ↓
Hibernate repository implementation
```

The domain should not know how it is delivered or persisted.

---

# 8. Capability-to-capability dependencies

Direct dependencies are permitted only through **published capability contracts**.

Example:

```text
Ordering Application
        ↓
Inventory Allocation Port
        ↓
Inventory Capability
```

Not:

```text
Ordering
   ↓
InventoryRepository
   ↓
inventory tables
```

This preserves semantic ownership.

---

# 9. No arbitrary cross-module domain-object access

A common modular-monolith failure is:

```text
module A imports module B entity
module C mutates it
module D reads its repository
```

until ownership disappears.

Main Street therefore requires:

> **A capability's internal domain objects are private by default.**

Cross-module communication uses:

```text
commands
queries
published interfaces
domain/integration events
explicit application coordination
```

as appropriate.

---

# 10. Cross-capability transactions

MS-PROT-025 allows narrow cross-capability atomicity where one invariant requires it.

Therefore the application layer may coordinate:

```text
Ordering
+
Inventory
```

inside one local transaction.

But ownership remains:

```text
Ordering owns Order
Inventory owns Allocation
```

The coordinator does not gain unrestricted access to both internal repositories.

---

# 11. Application orchestration

Where several capabilities must participate synchronously:

```text
Application Coordinator
     ↓
Capability A contract
Capability B contract
Capability C contract
```

The coordinator owns the use-case sequencing required by the invariant.

It must not become:

```text
GlobalBusinessWorkflowService
```

containing every merchant process.

Long-running coordination remains governed by MS-PROT-024.

---

## 11.1 Executable operation ownership

The generic runtime may resolve a merchant's executable Operation, preserve its
model provenance and apply generic guards. It must not become a shared engine
with unrestricted authority to mutate capability state.

Authoritative fulfilment belongs to a capability-owned application handler:

```text
Generic runtime contract
      ↓
Capability application handler
      ↓
Capability domain and ports
      ↓
Owned authoritative state
```

An Operation spanning one local invariant across capabilities uses a narrow
application coordinator. The coordinator invokes deliberately published
capability contracts and owns sequencing only; repositories and internal
mutation APIs remain private to their owning modules.

The captured Executable Semantic Model is a conformance boundary. A handler may
fulfil its declared effects but may not extend the Operation with undeclared
authoritative effects or committed event identities.

---

# 12. Queries

Cross-capability read composition may be less restrictive than writes.

For example:

```text
Merchant Dashboard Query
       ↓
Orders projection
Schedule projection
Payment projection
```

may combine several read models.

This does not transfer domain ownership.

Therefore:

> **Read composition may cross module boundaries freely through published read contracts/projections; authoritative writes remain tightly owned.**

---

# 13. Shared database does not erase modules

Even if every module initially uses one database:

```text
Ordering tables
Scheduling tables
Inventory tables
```

remain logically owned.

A module must not directly mutate another module's tables because the SQL connection permits it.

Foreign keys may exist where justified, but they must not become a substitute for semantic ownership.

---

# 14. Internal events

Modules may publish domain events for independent reactions.

Example:

```text
OrderConfirmed
     ↓
Notification
Analytics
Projection
```

A subscriber must not reach back into the publisher's internal persistence to infer what the event meant.

The published contract must contain or expose sufficient semantics.

---

# 15. Public contracts

A module's public boundary should be intentionally small.

Conceptually:

```text
Scheduling
├── Commands/API
│   ├── createAppointment
│   ├── rescheduleAppointment
│   └── cancelAppointment
│
├── Queries
│   └── schedule views
│
└── Events
    ├── AppointmentScheduled
    └── AppointmentCancelled
```

Internal helpers, entities, repositories and implementation services remain private.

---

# 16. Spring usage

Spring may wire modules together.

It must not become the semantic coupling mechanism.

Avoid architectural dependence on:

```text
@Autowired random service from any package
```

simply because Spring can inject it.

Dependencies must be semantically justified and visible in module contracts.

---

# 17. Package visibility

Java package/module boundaries should eventually help enforce architecture.

Prefer:

```text
public
```

only for intended module contracts.

Use package-private/internal types where possible.

Future tooling may enforce forbidden dependencies, but MS-PROT-032 does not yet mandate Java Platform Module System or a specific architecture-testing library.

---

# 18. Generic infrastructure

Infrastructure may provide shared technical utilities such as:

```text
database configuration
HTTP client setup
event transport
logging
serialization
security adapters
```

But avoid shared business helpers such as:

```text
BusinessUtils
CommonService
GenericWorkflowManager
```

These usually signal missing semantic ownership.

---

# 19. Module extraction

A module boundary should be strong enough that future extraction into a service is possible if justified.

That means avoiding reliance on:

```text
shared mutable objects
direct internal table writes
implicit Spring bean access
global transaction assumptions everywhere
```

However:

> **Extractability is a design quality, not a requirement to design every module as a remote service today.**

The modular monolith remains the accepted deployment architecture.

---

# 20. Falsification review

### Failed assumption: one capability must equal one module

A scheduling capability may include availability, appointment lifecycle and schedule rules that are semantically inseparable.

Mechanical one-to-one decomposition would fragment invariants.

**Rejected.**

### Failed assumption: technical layers should be top-level

A structure such as:

```text
controllers/
services/
repositories/
entities/
```

across the entire system scatters one capability across the whole codebase and weakens ownership.

**Rejected.**

### Failed assumption: modules may share domain entities for convenience

This creates ambiguous ownership and cross-module mutation.

**Rejected.**

### Failed assumption: no cross-capability synchronous calls

MS-PROT-023/025 already provide cases where an invariant spans capabilities.

Absolute prohibition would break order + allocation consistency.

**Rejected.**

### Failed assumption: all cross-module interaction should use events

Immediate consistency operations cannot always wait asynchronously.

**Rejected.**

### Failed assumption: all cross-module interaction should use direct calls

Independent notifications, analytics and projections do not require synchronous coupling.

**Rejected.**

### Failed assumption: shared database means repositories may be shared

This destroys modular boundaries.

**Rejected.**

### Failed assumption: every module must be independently deployable now

This would reintroduce microservice complexity.

**Rejected.**

---

# 21. Cross-domain validation

### Restaurant

```text
Ordering
Inventory
Payment
Notification
```

remain separate ownership boundaries while order acceptance may coordinate Ordering + Inventory.

**PASS**

### Gardener

```text
Enquiry
Quotation
Scheduling
Service Job
Portfolio
```

can compose without `GardenerService` owning everything.

**PASS**

### Driving instructor

Scheduling, purchases and customer access remain distinct but can be composed in merchant/customer application layers.

**PASS**

### Solicitor

Scheduling and notification can operate without customer-account module dependency.

**PASS**

### Motel

Booking, allocation and resource state can remain independently owned while local application coordination preserves booking invariants.

**PASS**

### Grocery

Ordering and inventory are separate modules; quantity allocation remains owned by Inventory.

**PASS**

No vertical requires a niche-wide backend module.

---

# 22. Accepted backend shape

Conceptually:

```text
mainstreet
│
├── semantic
│   └── minimal shared semantic kernel
│
├── platform
│   ├── identity
│   ├── tenancy
│   ├── configuration
│   ├── audit
│   └── ...
│
├── capabilities
│   ├── ordering
│   ├── scheduling
│   ├── inventory
│   ├── quotation
│   ├── notification
│   └── ...
│
├── application
│   └── narrow cross-capability coordination
│
├── delivery
│   ├── merchant
│   ├── customer
│   └── public
│
└── infrastructure
```

Actual packages should evolve from validated capability boundaries rather than copying this tree mechanically.

---

# 23. Accepted invariants

1. Backend modules follow semantic/business ownership rather than global technical layers.
2. Capability and module are related but not necessarily one-to-one.
3. The shared semantic kernel remains deliberately small.
4. Platform concerns and merchant business capabilities remain distinguishable.
5. Delivery modules contain transport concerns, not business rules.
6. Dependencies point from delivery → application → domain.
7. Infrastructure implements inward-facing ports.
8. Capability internals are private by default.
9. Cross-capability interaction occurs through published contracts.
10. A module may not mutate another module's persistence directly.
11. Narrow synchronous cross-capability coordination is permitted where an invariant requires it.
12. Independent reactions favour events rather than synchronous coupling.
13. Long-running coordination does not belong in generic application services.
14. Read projections may compose across capability boundaries.
15. Shared database does not imply shared semantic ownership.
16. Spring dependency injection cannot justify otherwise invalid dependencies.
17. Public module contracts should be intentionally small.
18. Generic infrastructure must not become a home for business semantics.
19. Modules should remain extractable in principle without pretending they are already services.
20. Modular-monolith deployment remains the default.
21. Executable Semantic Model effects constrain capability-owned fulfilment; they are not instructions for a generic mutation engine.
22. Authoritative operation handlers belong to the capability application boundary owning the affected semantics.
23. Cross-capability operation fulfilment uses narrow coordinators and published contracts.
24. Runtime conformance must reject undeclared authoritative effects or committed event identities.

---

# 24. Deferred decisions

MS-PROT-032 does not yet decide:

```text
exact Java packages
Spring Modulith adoption
JPMS modules
architecture-test framework
repository interfaces
ORM strategy
database schemas
module naming conventions
event transport implementation
dependency-injection conventions
```

These implementation choices must satisfy the accepted module rules.

---

# 25. Governance verdict

```text
MS-PROT-032
Backend Module, Bounded Context & Dependency Architecture

PROPOSE                     ✓
FALSIFICATION REVIEW        ✓
FAILED ASSUMPTIONS REMOVED  ✓
CROSS-DOMAIN VALIDATION     ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street's Spring Boot backend remains a modular monolith organised around semantic ownership and business capability. Each module owns its domain model and authoritative writes, exposes a deliberately small contract, and interacts with other modules through explicit commands, queries, application coordination or events according to consistency requirements. Shared infrastructure and a shared database do not weaken semantic ownership, and technical framework convenience must never become a substitute for module boundaries.**

The next unresolved design is now the **data/persistence implementation architecture inside those modules**—specifically how aggregates, repositories, relational data, cross-capability references, transactions, projections and tenant isolation should map onto the database.

The logical next document is **MS-PROT-033 — Relational Data, Aggregate & Repository Architecture**.
