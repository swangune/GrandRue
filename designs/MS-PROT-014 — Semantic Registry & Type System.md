MS-PROT-014 — Semantic Registry & Type System

Status: Proposed
Purpose: Define the canonical internal vocabulary of Main Street and the type system that prevents incompatible semantic constructs from being composed.

This specification is where we make the "pixels → image" and "alphabet → language" analogy concrete.

The most important decision is:

> Main Street should have a finite, strongly typed semantic vocabulary. AI can compose vocabulary items, but cannot create new semantic types at runtime.




---

1. The Semantic Registry

Main Street requires a canonical registry:

SEMANTIC REGISTRY
                           │
       ┌───────────────────┼───────────────────┐
       │                   │                   │
   Behaviour            Business             Data
       │                 Structure             │
       │                   │                   │
   Primitive             Offering          Data Concept
   Capability            Operation         Data Type
   Framework             Commitment
   Policy
   Rule
   State
   Event
   Invariant

The registry is the source of semantic truth.


---

2. Registry entries are typed

Every object in the registry has a declared type.

For example:

SERVICE_BOOKING
type = FRAMEWORK

PAYMENT_REQUIRED
type = POLICY

VEHICLE_REGISTRATION
type = DATA_CONCEPT

ALLOCATE
type = PRIMITIVE

The type determines what the object is allowed to do.


---

3. The registry must not become an ontology monster

We should resist creating hundreds of abstract philosophical types.

The registry needs only the concepts required to describe Main Street's operational reality.

The initial type system should therefore be deliberately small.


---

4. Core semantic types

I recommend these as the initial canonical types:

PRIMITIVE
ABSTRACTION
CAPABILITY
FRAMEWORK
POLICY
RULE
REQUIREMENT
DATA_CONCEPT
STATE
TRANSITION
EVENT
INVARIANT
RESOURCE_TYPE

We can add types later only when a demonstrated architectural need exists.


---

5. Primitive

A primitive is the smallest executable semantic operation recognised by Main Street.

Examples:

CREATE
READ
VALIDATE
CHECK
ALLOCATE
RELEASE
CONFIRM
CANCEL
MODIFY
CALCULATE
NOTIFY

A primitive should have very little semantic surface area.


---

6. Primitive structure

Conceptually:

Primitive
{
    id
    name
    version
    input_type
    output_type
    effects
    invariants
}

For example:

ALLOCATE

input:
    CapacityRequest

output:
    Allocation

effects:
    creates allocation

invariants:
    capacity must be available


---

7. Abstraction

An abstraction combines primitives into a reusable semantic operation.

Example:

BOOK

could conceptually compose:

CHECK
VALIDATE
ALLOCATE
CONFIRM

The abstraction hides implementation detail.

This is exactly what your "grouping" idea requires.


---

8. Abstraction structure

Abstraction
{
    id
    name
    version
    inputs
    outputs
    composed_primitives
    invariants
}

An abstraction is still not merchant configuration.

It is an internal reusable building block.


---

9. Capability

A capability describes something Main Street can enable for a merchant.

Examples:

BOOKING
ORDERING
DELIVERY
SCHEDULING
PAYMENT
CONSULTATION
INVENTORY
RESOURCE_ALLOCATION
LISTING

Capabilities should be business-meaningful.


---

10. Capability structure

Capability
{
    id
    name
    version
    abstractions
    dependencies
    supported_frameworks
}

The capability doesn't itself define every runtime state.

It points toward the appropriate frameworks.


---

11. Framework

A framework is the largest reusable operational building block.

Examples:

SERVICE_BOOKING
COMMERCE_ORDER
ACCOMMODATION_STAY
PROPERTY_VIEWING
RESOURCE_RENTAL
DELIVERY
CONSULTATION

A framework defines the operational contract.


---

12. Framework structure

Conceptually:

Framework
{
    id
    name
    version

    capabilities
    states
    transitions
    requirements
    policies
    rules
    events
    invariants
    resources
}

This is where most operational semantics converge.


---

13. Policy

A policy is a permitted configuration choice.

Examples:

PAYMENT_REQUIRED
CUSTOMER_SELECTS_TIME
CUSTOMER_SELECTS_RESOURCE
DELIVERY_ENABLED
CUSTOMER_CANCELLATION_ALLOWED

Policies must have explicit allowed values.

For example:

PAYMENT_POLICY

allowed:
    REQUIRED
    OPTIONAL
    NOT_REQUIRED

rather than arbitrary Boolean values everywhere.


---

14. Why enums are preferable to unrestricted Booleans

Consider:

payment_required = true

This loses semantic precision.

What happens if payment is:

required before request;

required before commitment;

required after fulfilment;

optional;

external?


Instead:

PAYMENT_POLICY =
    BEFORE_COMMITMENT
    AFTER_COMMITMENT
    OPTIONAL
    EXTERNAL
    NONE

This gives the compiler meaningful possibilities.


---

15. Rule

A rule defines a deterministic consequence.

Example:

IF
    PAYMENT_POLICY = BEFORE_COMMITMENT

THEN
    commitment requires payment condition

The rule is owned by the framework.

Merchant configuration activates it.


---

16. Rule structure

Rule
{
    id
    name
    version
    scope
    conditions
    effects
    priority?
    invariants
}

We should avoid arbitrary scripting inside rules.


---

17. Requirement

A requirement specifies something needed for an operation or transition.

Example:

VEHICLE_REGISTRATION

may be required for:

VehicleService

A requirement references a data concept.


---

18. Requirement structure

Requirement
{
    id
    data_concept
    required_when
    validation
    visibility
    reuse_policy
}

The required_when condition should reference approved semantic predicates rather than arbitrary code.


---

19. Data Concept

A data concept represents meaningful information.

Examples:

VEHICLE_REGISTRATION
DELIVERY_ADDRESS
SERVICE_LOCATION
GUEST_IDENTITY
PROPERTY_ID
PROJECT_DESCRIPTION


---

20. Data concept structure

DataConcept
{
    id
    name
    semantic_type
    validation
    sensitivity_class
    composite_schema?
}

Again, semantic type is different from database storage type.


---

21. State

State describes a valid condition of an entity.

For example:

Booking:
    REQUESTED
    CONFIRMED
    CANCELLED
    COMPLETED

A framework owns its state machine.


---

22. Transition

A transition defines an allowed movement:

REQUESTED
    ↓
CONFIRMED

or:

CONFIRMED
    ↓
CANCELLED

A transition is not simply:



It is an authorised state transition with preconditions and effects.


---

23. Transition structure

Transition
{
    id
    from_state
    to_state
    command
    preconditions
    effects
    events
}

This gives us a controlled state machine.


---

24. Event

An event represents a fact that has occurred.

Examples:

BookingRequested
BookingConfirmed
PaymentReceived
AllocationReleased
BookingCancelled

Events are facts, not instructions.

This distinction is critical.


---

25. Command versus event

We should retain:

COMMAND
    "Cancel booking"

EVENT
    "Booking was cancelled"

A command asks the system to do something.

An event records what happened.


---

26. Invariant

An invariant must always hold.

Examples:

capacity_used <= capacity_available

published configuration must be valid

commitment must reference an existing offering

operation cannot transition through an undefined transition

Invariants sit above configurable policies.


---

27. Resource Type

A resource type describes something that can participate in capacity/allocation.

Examples:

ROOM
WORKSHOP_BAY
STAFF_MEMBER
VEHICLE
CONSULTATION_SLOT
PROPERTY_VIEWING_SLOT

The framework determines how a resource type behaves.


---

28. Semantic relationships

The registry isn't merely a collection.

It is a graph.

For example:

SERVICE_BOOKING
      │
      ├── requires → SCHEDULING
      │
      ├── uses → BOOK
      │
      ├── has → REQUIREMENT
      │             └── VEHICLE_REGISTRATION
      │
      ├── supports → PAYMENT_POLICY
      │
      ├── uses → RESOURCE_ALLOCATION
      │
      └── produces → BookingConfirmed

This graph is what the compiler traverses.


---

29. Allowed relationships

We should explicitly restrict what can reference what.

For example:

Framework
    → Capability
    → Policy
    → Rule
    → Requirement
    → State
    → Transition
    → Event
    → ResourceType
    → Invariant

But:

Policy
    ✕ directly creates arbitrary Framework

and:

DataConcept
    ✕ executes Primitive

These constraints prevent semantic contamination.


---

30. Type compatibility

The compiler should be able to answer:

> Can A legally compose with B?



For example:

SERVICE_BOOKING
+
SCHEDULING

Yes.

SERVICE_BOOKING
+
PROPERTY_VIEWING

Not automatically.

The latter may be possible only through a defined composite framework.


---

31. No implicit semantic coercion

We should avoid behaviour such as:

"If something looks similar, just make it work."

Similarity is useful for AI inference.

It must not determine runtime semantics.

The registry determines legal composition.


---

32. Versioning

Every registry construct needs a version.

For example:

SERVICE_BOOKING@1.2

A framework may evolve.

But existing merchant configurations should continue referencing the version against which they were validated.


---

33. Compatibility

We need compatibility metadata:

compatible_with
supersedes
deprecated_by
requires
conflicts_with

Example:

PAYMENT_POLICY@2
supersedes PAYMENT_POLICY@1


---

34. Deprecation

We should never simply delete a semantic construct.

Instead:

ACTIVE
    ↓
DEPRECATED
    ↓
RETIRED

Existing configurations remain interpretable.

New configurations cannot necessarily select retired constructs.


---

35. Registry ownership

The semantic registry should be controlled by Main Street engineering.

Not:

merchant

Not:

AI agent

Not:

customer

The registry is platform infrastructure.


---

36. AI access to the registry

AI agents receive a controlled semantic interface.

They can:

SEARCH
RETRIEVE
COMPARE
SELECT
COMPOSE

They cannot:

CREATE_RUNTIME_TYPE
MODIFY_INVARIANT
ALTER_FRAMEWORK
EXECUTE_ARBITRARY_RULE


---

37. This solves the "AI hallucination" problem structurally

Suppose AI encounters an unusual business.

It cannot invent:

SUPER_BOOKING_XYZ

and push it into production.

It must choose from the registry.

If no appropriate framework exists:

UNSUPPORTED_SEMANTIC_REQUIREMENT

The system flags it for engineering expansion.


---

38. Important distinction: new merchant configuration vs new platform capability

This is fundamental.

If a merchant says:

> "I provide consultation."



AI should compose existing constructs.

If Main Street encounters a genuinely new operational pattern that cannot be represented:

new semantic requirement

that is an engineering problem, not an AI configuration problem.

Engineering adds a new registry construct.

Then the compiler, simulator and validation suite are updated.


---

39. Registry growth therefore remains controlled

Instead of:

every merchant
→ new code

we get:

many merchants
→ existing constructs

genuinely new business behaviour
→ new framework/primitive

This is exactly how Main Street can scale across niches without becoming an unmaintainable collection of bespoke applications.


---

40. The semantic language

We can now formally define the idea you have been developing as:

> Main Street Semantic Configuration Language (MS-SCL)



MS-SCL is not intended to be a general-purpose programming language.

It is a typed declarative language for expressing supported business behaviour.

Its vocabulary comes from the semantic registry.


---

41. Conceptual MS-SCL

Something like:

framework SERVICE_BOOKING

capabilities [
    SCHEDULING,
    RESOURCE_ALLOCATION,
    PAYMENT
]

requirements [
    VEHICLE_REGISTRATION,
    SERVICE_LOCATION
]

policies {
    payment = BEFORE_COMMITMENT
    customer_selects_time = true
}

This is illustrative, not yet the final syntax.

We should not implement this syntax yet.

We still have to finish the semantic specification.


---

42. Why a language is useful

It gives every specialist agent a common contract.

Business Agent
      ↓
MS-SCL

Operations Agent
      ↓
MS-SCL

Policy Agent
      ↓
MS-SCL

And:

Compiler
    ↓
valid MS-SCL

This is much safer than passing natural-language instructions between agents.


---

43. But don't expose MS-SCL to merchants

This is critical.

The merchant sees:

> "Do you require payment before confirming an appointment?"



Not:

payment.policy = BEFORE_COMMITMENT

MS-SCL is an internal language.


---

44. Three layers now become explicit

MERCHANT
                   │
                   ▼
            SIMPLE CONTROLS
                   │
                   ▼
             CONFIGURATION
                   │
                   ▼
                 MS-SCL
                   │
                   ▼
             SEMANTIC REGISTRY
                   │
                   ▼
                RUNTIME

And for AI:

Business Understanding
        ↓
AI Inference
        ↓
MS-SCL Candidate
        ↓
Compiler


---

45. UI language remains independent

The operational language:

MS-SCL

describes:

> What the business can do and how it operates.



The UI semantic language will describe:

> How those capabilities should be presented.



Therefore:

MS-SCL
    ≠
UI-SCL

They interact through a defined contract.


---

46. The registry is the "DNA" of Main Street

The analogy is now quite useful:

Primitive
    = atom

Abstraction
    = molecule

Capability
    = component

Framework
    = organ/system

Merchant configuration
    = organism configuration

Runtime
    = living system

Or using your preferred analogy:

Pixel
 ↓
shape
 ↓
component
 ↓
image

The important thing is that the system does not need to understand every possible image beforehand.

It needs a sufficiently expressive, controlled vocabulary of components.


---

47. Registry design principle

I recommend this invariant:

> Every semantic construct must have one canonical definition. Synonymous concepts must resolve to the same canonical identifier rather than becoming separate registry entries.



This prevents semantic duplication.


---

48. Another important invariant

> A registry construct must never encode merchant-specific assumptions unless those assumptions are explicitly part of its semantic definition.



For example:

BOOKING

must not secretly mean:

> booking with Stripe, 30-minute slots, upfront payment and customer-selected resources.



Those are configuration/framework concerns.


---

49. MS-PROT-014 decisions

Decision	Status

Main Street requires a canonical Semantic Registry	Accepted
Registry constructs are strongly typed	Accepted
Primitive/Abstraction/Capability/Framework distinction	Accepted
Policies are controlled configuration choices	Accepted
Rules are deterministic framework consequences	Accepted
Requirements reference Data Concepts	Accepted
States and Transitions are first-class	Accepted
Commands and Events remain distinct	Accepted
Invariants are first-class and non-overridable	Accepted
Resource Types are first-class	Accepted
Registry is a graph of typed relationships	Accepted
Legal relationships are explicitly constrained	Accepted
Implicit semantic coercion prohibited	Accepted
Registry constructs are versioned	Accepted
Deprecation/retirement supported	Accepted
AI can select/compose but cannot modify registry semantics	Accepted
New semantic capability requires engineering intervention	Accepted
MS-SCL becomes the internal operational semantic language	Accepted in principle
Merchants never interact directly with MS-SCL	Accepted
UI semantics remain separate from operational semantics	Accepted



---

Next: MS-PROT-015 — Semantic Graph & Composition Algebra

We now have the vocabulary and types. The next question is more fundamental:

How exactly are these things mathematically/structurally composed?

We need to define:

A + B
A requires B
A contains B
A extends B
A conflicts with B
A activates B
A produces B
A consumes B
A refines B

and establish the rules for composition, inheritance, abstraction and specialisation.

This is where we determine whether the "Scratch" architecture remains elegant—or turns into a giant dependency graph that becomes impossible to maintain.

I would make MS-PROT-015 the last major abstract specification before we start drawing the actual prototype architecture and concrete schemas.