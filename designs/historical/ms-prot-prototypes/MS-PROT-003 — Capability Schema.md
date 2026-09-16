Agreed. No implementation yet. We should complete the architectural and domain design down to the lowest meaningful level, then perform a consolidated validation pass, then freeze the design baseline before code.

The important adjustment is that we should not endlessly validate each abstraction in isolation. We will design downward, while continuously testing each layer against our four prototype businesses.

MS-DESIGN — Detailed Design Programme

Our design hierarchy should now be:

L0  Business-serving architecture
        ↓
L1  Semantic model
        ↓
L2  Primitive model
        ↓
L3  Abstract primitives / frameworks
        ↓
L4  Capability model
        ↓
L5  Capability contracts
        ↓
L6  Domain entities & value objects
        ↓
L7  Commands / queries
        ↓
L8  State & transition rules
        ↓
L9  Policies & configuration
        ↓
L10 Requirements & contextual data
        ↓
L11 Availability / capacity / allocation
        ↓
L12 Interaction & events
        ↓
L13 Cross-capability orchestration
        ↓
L14 Authorisation / tenancy / audit
        ↓
L15 Persistence & consistency
        ↓
L16 API / integration contracts
        ↓
L17 Operational configuration contract
        ↓
L18 Presentation contract
        ↓
L19 AI composition contracts
        ↓
L20 Validation & acceptance

Only after L20 do we design implementation.


---

1. We need one important architectural distinction

Our previous documents sometimes use the word framework broadly.

Let's formalise it.

Primitive
    ↓
Abstract Primitive
    ↓
Framework
    ↓
Capability
    ↓
Capability Configuration
    ↓
Business Composition

Primitive

Smallest reusable semantic operation.

REQUEST
SELECT
ALLOCATE
CONFIRM
PAY

Abstract Primitive

A reusable semantic pattern that combines primitives but is still domain-neutral.

Example:

TEMPORARY HOLD
=
RESERVE
+
EXPIRY
+
RELEASE

Framework

A reusable operational composition.

BOOKABLE SERVICE
=
OFFERING
+
SCHEDULE
+
AVAILABILITY
+
BOOKING

Capability

A business-operational capability exposed to a merchant runtime.

BOOKING
PAYMENT
DELIVERY
CATALOGUE

This distinction is important because otherwise our "primitive language" itself becomes bloated.


---

2. The lowest-level semantic model

Before defining more capabilities, we need to establish the semantic atoms from which everything else is built.

I propose the following fundamental entities.

ACTOR
OFFERING
RESOURCE
CAPACITY
COMMITMENT
ACTIVITY
LOCATION
TIME
IDENTITY
INTERACTION
REQUIREMENT
POLICY

These are not all capabilities.

They are concepts the runtime can reason about.


---

3. Actor

An actor is something capable of initiating or participating in an operation.

Actor
├── Customer
├── Staff
├── Merchant
├── System
└── External System

This connects directly to our Persistent Capability-Owned Interaction Model.

An actor does not automatically mean a Main Street user account.


---

4. Offering

An offering represents what the merchant makes available.

It could be:

Physical product
Service
Accommodation
Consultation
Property
Vehicle service

The offering should contain business-relevant semantics, not presentation.

For example:

Offering
├── identity
├── name
├── description
├── category
├── pricing
├── applicable capabilities
└── requirements

The actual catalogue implementation will be designed later.


---

5. Resource

A resource is something whose use can constrain fulfilment.

Examples:

Motel → Room
Mechanic → Service bay / technician
Consultant → Consultant availability
Delivery → Delivery capacity

But an offering isn't necessarily a resource.

This distinction is critical.

Standard Room
    = Offering

Room 104
    = Resource

The customer interacts with the offering.

The system may allocate the resource internally.


---

6. Capacity

Capacity represents how much fulfilment is possible.

This is especially important because not every business needs individual resource allocation.

For example:

Motel

Standard:
    capacity = 8

Executive:
    capacity = 4

The customer doesn't need eight separate resources presented to them.

Internally:

Capacity
   ↓
Allocation
   ↓
Resource

For another business:

Structural Engineer

Consultation capacity:
    4 appointments/day

There may not be individually allocated resources at all.


---

7. Commitment

We need this concept beneath Booking.

A commitment means Main Street has accepted an obligation to perform or provide something.

For example:

Confirmed motel stay
Confirmed mechanic appointment
Confirmed viewing
Confirmed delivery

This helps distinguish:

REQUEST

from:

COMMITMENT

A customer browsing or pausing an operation has not necessarily created a commitment.

This becomes extremely important for the caching/temporary allocation model.


---

8. Activity

An activity is something happening as part of a business process.

Examples:

Check-in
Repair
Consultation
Delivery
Viewing
Collection

Activity does not necessarily create a commitment.

This distinction helps us avoid turning every customer action into a transaction.


---

9. Time

Time must be treated as a first-class semantic input.

We need to distinguish:

Instant
Duration
Interval
Schedule
Deadline
Expiry
Buffer

For example:

Motel checkout:
    expected = 11:00

availability buffer:
    15 minutes

potential next availability:
    11:15

But only if all other availability conditions are satisfied.


---

10. Requirement

Requirement answers:

> What must be known or satisfied before this operation can proceed?



Examples:

Delivery → address
Mechanic → vehicle registration
Paid consultation → payment
Property viewing → contact information

Requirement composition is therefore central to our architecture.


---

11. Policy

Policy answers:

> How has this merchant chosen to operate within the capability's permitted behaviour?



Examples:

Payment required
Payment optional

Customer cancellation allowed
Customer cancellation prohibited

Automatic allocation
Merchant allocation

Delivery enabled
Delivery disabled

The merchant should only see relevant policies.

This is where our onboarding inference becomes operationally important.


---

12. The merchant configuration model

The merchant should never see:

Primitive
Framework
Capability dependency graph
Event schema
State machine
Concurrency model

Instead:

Main Street understands business
             ↓
Relevant capabilities
             ↓
Relevant policies
             ↓
Simple merchant controls

For example:

Online booking
    ON

Require payment to confirm booking
    ON

Allow customer cancellation
    OFF

Behind the scenes, that could configure dozens of lower-level rules.

This is exactly the complexity compression Main Street is supposed to provide.


---

13. We now need to design the "configuration compiler"

This is one of the most important missing pieces.

The system effectively needs:

Business Understanding
        ↓
Semantic Composition
        ↓
Configuration
        ↓
Validation
        ↓
Runtime configuration

I would call the conceptual component:

Configuration Compiler

Not necessarily a literal compiler in implementation, but architecturally it behaves like one.

Its job is:

High-level business semantics
        ↓
Validated Main Street configuration

It does not execute business operations.


---

14. Configuration compiler rules

The compiler should operate against four registries:

Primitive Registry
Capability Registry
Framework Registry
Policy Registry

And produce:

Merchant Operational Configuration

The AI proposes the composition.

The deterministic compiler validates it.

Therefore:

AI = inference/composition

Compiler = deterministic validation/materialisation

Runtime = execution

This is a very strong separation.


---

15. AI must never be the final authority

The pipeline becomes:

Merchant answers
      ↓
AI inference
      ↓
Candidate configuration
      ↓
Schema validation
      ↓
Capability validation
      ↓
Dependency validation
      ↓
Policy validation
      ↓
Invariant validation
      ↓
Configuration accepted

If validation fails:

DO NOT PUBLISH

This is especially important because the merchant will trust Main Street to understand their business.


---

16. Merchant approval remains at the correct boundary

We already established:

AI generates the business description.

Merchant:

Review
Approve

For operational configuration:

Merchant answers
      ↓
AI inference
      ↓
Configuration
      ↓
Relevant merchant controls
      ↓
Merchant approval

For website/customer-facing terms:

Operational configuration
      ↓
Terms/content inference
      ↓
Merchant review
      ↓
Approval
      ↓
Publish

For internal mechanics:

Configuration
      ↓
Runtime

No unnecessary merchant involvement.


---

17. The next detailed-design layer: state

We now need to go significantly deeper.

Every capability must define:

States
Transitions
Commands
Events
Guards
Side effects
Terminal conditions
Failure behaviour
Concurrency rules
Idempotency rules

For example, Booking:

REQUESTED
    │
    ├── reject
    │
    ▼
PENDING
    │
    ├── payment required
    │
    ▼
CONFIRMED
    │
    ▼
COMPLETED

But the exact model depends on policy.

A no-payment booking should not be forced through a payment state.

That suggests that state machines themselves may be configuration-composed, but only from validated transition patterns.

That is a major design problem we should solve next.


---

18. We should therefore design the State Machine Framework next

Not individual motel states.

Not individual mechanic states.

Instead:

MS-PROT-004 — State & Transition Framework

It will define:

State
Transition
Command
Guard
Event
Terminal state
Failure
Compensation
Idempotency
Concurrency

Then Booking, Stay, Repair, Viewing, etc. can define their own state models using that framework.

This is the correct next level.


---

19. Our prototype progression is now fixed

✓ Semantic model
✓ Primitive concept
✓ Capability concept
✓ Capability schema
→ State & Transition Framework
→ Requirement Framework
→ Policy Framework
→ Availability Framework
→ Capacity & Allocation Framework
→ Interaction implementation contract
→ Cross-capability orchestration
→ Configuration Compiler
→ Operational Configuration Schema
→ AI Composition Contract
→ Validation Architecture
→ Presentation Contract
→ Final architecture validation
→ Design freeze
→ Only then implementation

And throughout this process we will continuously test against:

Motel
Mechanic
Grocery
Realtor

plus edge cases.

Next is therefore MS-PROT-004 — State & Transition Framework. This is where we can finally resolve the deeper questions around pending activities, confirmation, completion, cancellation, temporary allocation, cached customer activity, and race conditions without inventing business-specific state machines.