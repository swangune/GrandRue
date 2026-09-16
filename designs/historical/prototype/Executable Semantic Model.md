> **Governance note (21 August 2026):** Retained as early design evidence. The accepted MS-PROT-020 through MS-PROT-023 contracts supersede this note where they differ, including capability-owned operation fulfilment and the current resolved operational model boundary.

Good. We now move into the actual detailed design.

1. Executable Semantic Model

The Executable Semantic Model (ESM) is the runtime-ready representation of a merchant's validated configuration.

It is not the merchant's raw configuration and it is not a database schema.

Its job is simple:

> Give the runtime everything it needs to execute a merchant's operations without interpreting the merchant's configuration from scratch on every request.



The pipeline is:

Merchant Configuration
        │
        ▼
Configuration Validator
        │
        ▼
Semantic Resolver
        │
        ▼
ESM Compiler
        │
        ▼
Executable Semantic Model
        │
        ▼
Runtime


---

2. ESM top-level structure

For Prototype 1, the model should have these components:

ExecutableMerchantModel
│
├── model_identity
├── version
├── resources
├── relationships
├── operations
├── states
├── events
├── constraints
├── capabilities
└── experience_contract

Notably, merchant content is not the same thing as the ESM.

For example:

"Chicken Burger"
"£8.50"
"Open Monday–Friday"

are merchant data/configuration.

The ESM describes what the system can do with that information.


---

3. Resource

A Resource represents a meaningful entity in the merchant's operational model.

Generic examples:

Customer
Product
Order
Appointment
StaffMember
Location
Document
Asset

We do not create:

RestaurantTable
SolicitorCase
MotelRoom

as Main Street primitives.

Those are merchant-defined resource types.

Conceptually:

ResourceDefinition
├── id
├── name
├── attributes
├── states
└── capabilities

Example:

ResourceDefinition
    id: resource.order
    name: Order

    attributes:
        customer
        items
        total
        fulfilment_method

The runtime doesn't care that another merchant calls its equivalent entity Booking.


---

4. Relationship

A relationship describes how resources relate.

RelationshipDefinition
├── id
├── source
├── target
├── cardinality
└── constraints

Examples:

Order → belongs_to → Customer

Order → contains → Product

Appointment → assigned_to → StaffMember

This is deliberately generic.

Relationships become extremely important because they prevent us from encoding domain-specific assumptions into the runtime.


---

5. State

A resource can have a lifecycle.

StateDefinition
├── id
├── resource
├── states
└── transitions

For example:

Order

pending
   ↓
confirmed
   ↓
in_progress
   ↓
completed

The important point is that state transitions are controlled by operations, not arbitrary database updates.

The runtime should never simply accept:

order.state = completed

from an untrusted client.


---

6. Operation

This is probably the most important runtime primitive.

An operation represents an action that can be performed against the semantic model.

OperationDefinition
├── id
├── target
├── input
├── authorization
├── constraints
├── state_effect
└── events

For example:

Operation
    id: order.confirm

Target:
    Order

Input:
    order_id

Preconditions:
    Order.state == pending

State effect:
    pending → confirmed

Events:
    OrderConfirmed

The implementation of the operation remains in the generic runtime.


---

7. Event

An event represents a fact that has already occurred.

EventDefinition
├── id
├── source_operation
├── payload
└── version

For example:

OrderConfirmed

The event doesn't say:

> Send this particular sentence to the customer.



It says:

> This semantic fact occurred.



Downstream systems decide what to do with it.


---

8. Constraint

We need a formal place for conditions that must hold.

ConstraintDefinition
├── id
├── subject
├── predicate
└── failure

Examples:

Order must contain at least one item.

Appointment requires an available resource.

Only authorised staff may perform operation X.

Resource cannot have conflicting active reservations.

The important boundary remains:

Constraint
    = declarative requirement

Runtime
    = mechanism that enforces it


---

9. Capability

Capabilities describe what a merchant has enabled.

This is particularly important for the evolving-business problem.

A merchant might begin with:

Capabilities:
    ordering
    payment

Later:

Capabilities:
    ordering
    payment
    reservations
    table_management

The runtime doesn't need to know why the merchant enabled them.

It simply receives a valid executable model containing those capabilities.


---

10. Experience contract

This is the bridge to the UI work.

The ESM shouldn't dictate pixel-level UI.

Instead, it exposes what experiences are available.

Conceptually:

ExperienceContract
├── customer
│   ├── resources
│   ├── operations
│   ├── information
│   └── navigation
│
└── merchant
    ├── resources
    ├── operations
    ├── management capabilities
    └── navigation

The customer renderer and merchant renderer then determine how those capabilities are presented.


---

11. The critical distinction

We now have three representations:

MERCHANT
                     │
                     ▼
            Configuration Model
                     │
              compile/validate
                     │
                     ▼
            Executable Model
                     │
              runtime execution
                     │
                     ▼
             Operational State

And separately:

Executable Model
       │
       ▼
Experience Contract
       │
 ┌─────┴─────┐
 ▼           ▼
Customer    Merchant
Web         Experience

This separation is essential.


---

12. What Prototype 1 actually needs

We should not implement every possible primitive now.

Prototype 1 needs the smallest complete execution path.

I recommend:

Resource
Relationship
Operation
State
Event
Constraint
Capability
Experience Contract

But only a small subset of each needs implementation.

For example, we can initially support:

Resource:
    typed entity + attributes

Relationship:
    many-to-one / one-to-many

State:
    finite state machine

Operation:
    command → validated state transition

Constraint:
    declarative predicates

Event:
    persisted domain event

Capability:
    enabled/disabled

Experience:
    resource list
    resource detail
    operation/form

That is enough to prove the architecture.


---

13. Now the operation execution path

This is the next thing we should lock down.

A request enters:

Customer/Merchant Client
        │
        ▼
API Boundary
        │
        ▼
Actor Resolution
        │
        ▼
Operation Resolution
        │
        ▼
Authorization
        │
        ▼
Input Validation
        │
        ▼
Constraint Evaluation
        │
        ▼
Transaction
        │
        ▼
State Transition
        │
        ▼
Persistence
        │
        ▼
Event Creation
        │
        ▼
Commit
        │
        ▼
Event Publication
        │
        ▼
Response

One crucial rule:

> The state transition and the event representing that transition must be committed consistently with the transaction.



We should not tell the customer:

> "Order confirmed"



before the underlying state is durably committed.


---

14. The runtime does not execute arbitrary merchant code

This is another boundary we should now lock.

Merchant configuration can declare:

resources
relationships
operations
states
constraints
capabilities
experience

But it cannot inject arbitrary backend code into the runtime.

If genuinely specialised computation is eventually required:

Operation
    ↓
Extension Contract
    ↓
Specialised implementation

That is a future extension mechanism.

Prototype 1 does not need it.


---

15. This is now detailed enough to move forward

We have crossed the line from architectural discussion into implementation design.

The next step should therefore be the exact operation execution contract:

request structure;

operation identity;

actor identity;

input;

authorization;

constraint evaluation;

transaction boundary;

state transition;

event creation;

idempotency;

failure handling;

response structure.


Once that is defined, we can design the persistence model around actual execution rather than guessing at database tables first.

Next: Operation Execution Contract.
