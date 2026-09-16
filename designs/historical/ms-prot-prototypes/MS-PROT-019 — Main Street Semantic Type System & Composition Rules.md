MS-PROT-019 — Main Street Semantic Type System & Composition Rules

Status: Proposed
Purpose: Define the internal semantic language that Main Street uses to compose merchant capabilities.

This is a critical specification. We have established that AI should compose existing primitives rather than invent behaviour. For that to be reliable, Main Street needs something analogous to a programming language's type system, grammar, and compiler validation.

The objective is:

> AI may select and compose valid Main Street constructs, but it cannot invent new operational semantics during merchant configuration.




---

1. The semantic stack

We now formalise the hierarchy.

┌─────────────────────────────┐
│       MERCHANT CONFIG       │
├─────────────────────────────┤
│         FRAMEWORK           │
├─────────────────────────────┤
│         CAPABILITY          │
├─────────────────────────────┤
│      ABSTRACT PRIMITIVE     │
├─────────────────────────────┤
│          PRIMITIVE          │
└─────────────────────────────┘

Cross-cutting constructs operate across this stack:

POLICY
RULE
REQUIREMENT
RESOURCE
ACTOR
OPERATION
STATE
EVENT
INVARIANT

These are not simply another five levels in the hierarchy.


---

2. Primitive

A primitive is the smallest reusable semantic operation or concept that Main Street is willing to treat as fundamental.

Examples:

CREATE
READ
UPDATE
CANCEL
ALLOCATE
RELEASE
SCHEDULE
REQUEST
CONFIRM
COMPLETE

But we should be careful.

Not every CRUD operation necessarily deserves to be a domain primitive. Some may belong to the underlying platform rather than the business semantic language.

Therefore the registry must distinguish:

DOMAIN_PRIMITIVE
PLATFORM_PRIMITIVE


---

3. Primitive must be typed

A primitive is not just a word.

For example:

ALLOCATE

needs a type contract.

Conceptually:

ALLOCATE(
    Resource,
    AllocationRequirement,
    TimeInterval
)
→ Allocation

Therefore an AI agent cannot legally generate:

ALLOCATE(Customer, Payment)

because the argument types are invalid.

This is exactly how a programming language prevents nonsense operations.


---

4. Primitive contract

Every primitive should have:

Primitive
{
    id
    name
    version

    input_types
    output_type

    preconditions
    postconditions

    permitted_contexts

    emitted_events

    invariants

    dependencies
}

This becomes part of the semantic registry.


---

5. Abstract primitive

An abstract primitive is a reusable composition of existing primitives.

For example:

RESERVE_CAPACITY

could conceptually compose:

CHECK_CAPACITY
+
ALLOCATE
+
CREATE_COMMITMENT

The abstraction gives higher-level agents something useful to reason about without forcing them to manipulate low-level primitives directly.


---

6. Crucially, abstraction does not create new physics

An abstract primitive cannot secretly introduce behaviour.

It must have an expansion:

RESERVE_CAPACITY
       ↓
[existing primitives]

Therefore we can inspect it.

If an AI agent uses:

RESERVE_CAPACITY

the compiler knows exactly what it means.


---

7. Framework

A framework is a domain-level operational composition.

For example:

SERVICE_BOOKING

may contain:

REQUEST
OFFERING
SCHEDULE
RESOURCE
ALLOCATION
COMMITMENT
PAYMENT_CONDITION
CANCELLATION
FULFILMENT

A framework therefore establishes an operational vocabulary for a class of businesses.


---

8. Framework is not an industry

This is important.

We should not have:

CAR_WASH_FRAMEWORK
HAIRDRESSER_FRAMEWORK
MOTEL_FRAMEWORK
STRUCTURAL_ENGINEER_FRAMEWORK

unless the operational semantics genuinely require them.

Instead:

SERVICE_BOOKING

can support:

car wash
hairdresser
mechanic
consultant

with specialised capabilities and requirements.


---

9. Capability

A capability is something the merchant's operational model can actually do.

Examples:

BOOK_SERVICE
ACCEPT_PAYMENT
ALLOCATE_RESOURCE
COLLECT_DOCUMENT
SCHEDULE_APPOINTMENT
LIST_PROPERTY
MANAGE_INVENTORY

Capabilities are what ultimately become available to merchant/customer operations.


---

10. Capability composition

A capability may depend on other capabilities.

For example:

RESCHEDULING
    requires:
        SCHEDULING
        RESOURCE_ALLOCATION

Therefore:

RESCHEDULING

cannot be enabled if its required semantic dependencies are absent.

This prevents invalid configurations.


---

11. Policy

A policy determines how an applicable capability behaves under merchant-defined conditions.

Examples:

PAYMENT_REQUIRED
CUSTOMER_CANCELLATION_ALLOWED
CUSTOMER_RESCHEDULING_ALLOWED
CUSTOMER_SELECTS_STAFF

A policy must not invent a capability.

For example:

PAYMENT_REQUIRED

cannot somehow create payment infrastructure.

It modifies the behaviour of an existing payment-capable framework.


---

12. Rule

A rule is a logical constraint.

For example:

IF payment_required
THEN confirmation_requires_payment

Another:

IF resource_capacity = 0
THEN allocation_must_fail

Rules enforce semantic correctness.


---

13. Policy versus rule

These must remain distinct.

Policy

Merchant/business choice:

> Customers must pay before confirmation.



Rule

System consequence:

> A booking requiring payment cannot become confirmed until payment is satisfied.



So:

POLICY
    ↓
activates/configures
    ↓
RULE

The merchant chooses the policy.

The platform owns the rule.


---

14. Requirement

A requirement states information or conditions needed to perform an operation.

Examples:

Vehicle information
Customer identity
Property address
Project description
Consultation type

A requirement can be:

REQUIRED
OPTIONAL
CONDITIONAL


---

15. Conditional requirements

This is especially important for Main Street.

For example:

IF service = VEHICLE_DETAIL
THEN vehicle_size is required

Or:

IF site_visit = enabled
THEN service_location is required

The AI is not inventing this rule.

It is selecting an existing rule template and supplying valid parameters.


---

16. Resource

A resource is an entity whose capacity can constrain an operation.

Examples:

WASH_BAY
STAFF_MEMBER
ROOM
CONSULTANT
VIEWING_SLOT

A resource can expose:

capacity
availability
constraints
allocation rules


---

17. Resource type versus resource instance

This distinction is important.

WASH_BAY

is a resource type.

Wash Bay 2

is a resource instance.

Likewise:

STAFF_MEMBER

versus:

John

Main Street's semantic model should support both.


---

18. Offering

An offering is what the merchant makes available to customers.

Examples:

Full Wash
Premium Detail
Structural Inspection
Legal Consultation
Property Viewing

An offering references capabilities and requirements.

For example:

FULL_WASH
    requires:
        WASH_BAY
        STAFF_MEMBER


---

19. Actor

An actor is an entity capable of initiating or participating in operations.

Core actors:

CUSTOMER
MERCHANT
MERCHANT_USER
MAIN_STREET
EXTERNAL_PROVIDER

Later, specialised actors may exist.


---

20. Operation

An operation is an executable business action exposed by the semantic model.

Examples:

REQUEST_BOOKING
CONFIRM_BOOKING
CANCEL_BOOKING
RESCHEDULE_BOOKING
MAKE_PAYMENT
START_SERVICE
COMPLETE_SERVICE

An operation has:

actor
inputs
preconditions
effects
outputs
events
authorisation requirements


---

21. State

A state describes the current condition of a particular entity.

Examples:

Booking:
    REQUESTED
    CONFIRMED
    CANCELLED
    COMPLETED

State belongs to the entity whose lifecycle it describes.

There is no universal Main Street state.


---

22. Event

An event is an immutable statement that something happened.

Examples:

BookingRequested
BookingConfirmed
BookingCancelled
PaymentConfirmed
AllocationCreated
AllocationReleased
ServiceCompleted

Events can trigger downstream processing but cannot rewrite historical reality.


---

23. Invariant

An invariant is a condition that must remain true regardless of merchant configuration.

Examples:

capacity cannot be exceeded
unauthorised actors cannot perform protected operations
illegal state transitions cannot occur
duplicate events cannot create duplicate effects

These are the strongest constraints in the system.


---

24. The semantic type system

We can now define the broad type relationships.

Primitive
   ↓ composes into
Abstract Primitive
   ↓ composes into
Capability
   ↓ composes into
Framework
   ↓ parameterised by
Merchant Configuration

And:

Framework
 ├── Operations
 ├── States
 ├── Resources
 ├── Requirements
 ├── Events
 ├── Policies
 ├── Rules
 └── Invariants


---

25. Composition rule #1

Only registered constructs may be composed.

AI cannot produce:

NEW_UNKNOWN_CAPABILITY

during normal merchant configuration.

It must select:

existing registry entry

or fail inference.


---

26. Composition rule #2

Types must match.

If:

ALLOCATE

expects:

Resource
AllocationRequirement
TimeInterval

then the AI cannot provide:

Customer
Payment
Description

just because they appear semantically related.


---

27. Composition rule #3

Dependencies must resolve.

If:

RESCHEDULING

requires:

SCHEDULING
RESOURCE_ALLOCATION

then:

RESCHEDULING = enabled

without those dependencies is invalid.


---

28. Composition rule #4

Policies cannot violate invariants.

A merchant may configure:

CUSTOMER_CANCELLATION_ALLOWED

but cannot configure:

CAPACITY_OVERBOOKING_ALLOWED

if overbooking is prohibited by the platform invariant.


---

29. Composition rule #5

Frameworks cannot arbitrarily modify primitives.

A framework may use:

ALLOCATE

but cannot redefine ALLOCATE to mean something else.

This is equivalent to preventing a function from silently changing its type contract.


---

30. Composition rule #6

Abstractions must be reducible.

Every abstract primitive must have a valid expansion into lower-level registered constructs.

Therefore:

ABSTRACT_PRIMITIVE
        ↓
deterministic semantic expansion

must succeed.


---

31. Composition rule #7

Configuration is declarative.

The merchant configuration should describe:

> What the merchant has enabled and how applicable policies are configured.



It should not contain arbitrary executable instructions.

This is one of the strongest safeguards against AI-generated behaviour becoming unpredictable.


---

32. Composition rule #8

AI is not the runtime.

AI can infer:

"These existing capabilities appear applicable."

But the runtime determines:

whether the resulting configuration is valid

and executes the established semantics.

Therefore:

AI ≠ business rules engine
AI ≠ transaction engine
AI ≠ authorization engine
AI ≠ concurrency controller


---

33. This is the most important architectural separation

AI
 │
 │ inference
 ▼
Semantic Candidate
 │
 │ compiler validation
 ▼
Approved Configuration
 │
 │ execution
 ▼
Deterministic Runtime

AI proposes.

The compiler validates.

The runtime executes.


---

34. AI agent specialisation

Now your earlier idea about specialist agents becomes much more practical.

We could have:

Business Understanding Agent
       ↓
Capability Inference Agent
       ↓
Configuration Agent
       ↓
Policy Agent
       ↓
Validation Agent
       ↓
UI Semantic Agent

But none of these agents should have unrestricted authority.


---

35. Specialist agent contract

For example, the Capability Inference Agent gets:

merchant profile
onboarding answers
approved description
semantic registry

and produces:

candidate capability set

It does not produce arbitrary backend code.


---

36. Validation agent

The validation agent checks:

types
dependencies
policies
state transitions
resource requirements
invariants
configuration consistency

If invalid:

REJECT

not:

> "AI, please try to make it work somehow."



That distinction matters.


---

37. Unknown semantics

Suppose onboarding describes something Main Street does not support.

The AI should not invent a primitive.

It should produce:

UNRESOLVED_SEMANTIC_REQUIREMENT

Then Main Street can:

ask another onboarding question

or:

route to human/product-domain review

or:

reject unsupported operation

This is much safer.


---

38. Semantic registry

We therefore need a central registry.

Conceptually:

SEMANTIC REGISTRY
│
├── Primitives
├── Abstract Primitives
├── Capabilities
├── Frameworks
├── Policies
├── Rules
├── Requirements
├── Resource Types
├── Operations
├── States
├── Events
└── Invariants

This becomes one of the foundational components of Main Street.


---

39. Registry versioning

The registry itself must be versioned.

For example:

Semantic Registry v1.0
Semantic Registry v1.1
Semantic Registry v2.0

A merchant configuration references the registry version under which it was compiled.

This protects historical behaviour.


---

40. Why this is powerful

We have effectively created a domain-specific language without asking merchants to learn a DSL.

The DSL is internal.

The merchant sees:

simple business questions
simple controls
simple approval

The AI sees:

semantic constructs

The runtime sees:

validated deterministic configuration


---

41. Scratch analogy — now formalised

Your analogy is actually useful if we refine it.

Pixels

Primitives

Shapes

Abstract primitives

Components

Capabilities

Scenes

Frameworks

Finished image

Merchant configuration

But there is also a type system determining which pieces can legally connect.

That is what prevents chaos.


---

42. Merchant configuration is therefore a semantic program

Not executable code.

But structurally:

Merchant Configuration
=
valid composition of registered semantic constructs

This is a very useful way to think about Main Street internally.


---

43. Example

A car wash configuration might conceptually be:

SERVICE_BOOKING
{
    capabilities:
    [
        SERVICE_CATALOG,
        SCHEDULING,
        RESOURCE_ALLOCATION,
        PAYMENT
    ]

    policies:
    [
        PAYMENT_BEFORE_CONFIRMATION,
        CUSTOMER_CANCELLATION_ALLOWED
    ]

    resources:
    [
        WASH_BAY,
        STAFF_MEMBER
    ]
}

This is not application code.

It is a declarative semantic program.


---

44. Another merchant

A structural engineer:

PROFESSIONAL_CONSULTATION
{
    capabilities:
    [
        CONSULTATION,
        SCHEDULING,
        DOCUMENT_COLLECTION,
        PAYMENT
    ]

    requirements:
    [
        PROJECT_DESCRIPTION,
        SITE_LOCATION
    ]

    policies:
    [
        PAYMENT_BEFORE_CONFIRMATION
    ]
}

Same language.

Different valid composition.


---

45. This gives us the central Main Street architectural principle

> Business diversity should be represented as data and semantic composition wherever possible, not as divergent application code.



That should become a governing principle of the architecture.


---

46. What this does NOT mean

We should not pursue the extreme interpretation:

> "Everything must be configurable."



That would create an enormous meta-system.

Some things should remain hard platform invariants:

identity
authorization
transaction integrity
concurrency safety
auditability
configuration versioning
event idempotency
security

These should not be merchant-configurable.


---

47. Three authority layers

We can now distinguish:

PLATFORM INVARIANTS
        ↓
DOMAIN FRAMEWORK SEMANTICS
        ↓
MERCHANT POLICIES

The lower layer cannot violate the upper layer.

For example:

Merchant Policy
    "allow cancellation"

cannot override:

Platform Invariant
    "unauthorised users cannot modify bookings"


---

48. Proposed authority hierarchy

LEVEL 0 — PLATFORM INVARIANTS
        ↓
LEVEL 1 — SEMANTIC PRIMITIVES
        ↓
LEVEL 2 — ABSTRACT PRIMITIVES
        ↓
LEVEL 3 — FRAMEWORKS
        ↓
LEVEL 4 — CAPABILITIES
        ↓
LEVEL 5 — MERCHANT POLICIES
        ↓
LEVEL 6 — MERCHANT CONFIGURATION

Some relationships are lateral rather than strictly hierarchical, but this is the correct conceptual authority direction.


---

49. MS-PROT-019 acceptance

Requirement	Result

Registered semantic vocabulary	PASS
Primitive typing	PASS
Abstract composition	PASS
Framework composition	PASS
Capability dependency model	PASS
Policy/rule separation	PASS
Resource typing	PASS
Operation contracts	PASS
State/event separation	PASS
Platform invariants	PASS
AI cannot invent runtime semantics	PASS
Invalid compositions rejectable	PASS
Configuration is declarative	PASS
Specialist AI agents possible	PASS
Semantic registry defined	PASS



---

50. One major design decision is now ready

We have enough structure to define the actual canonical semantic object model.

That is different from merely listing concepts.

We now need to specify exactly what a:

Primitive
Capability
Framework
Policy
Resource
Offering
Operation
State Machine
Event
Configuration

looks like, including its mandatory fields, identifiers, references, dependencies, versioning, ownership, inheritance/composition and validation metadata.

In other words, we are ready to design the Main Street Semantic Schema.

Next: MS-PROT-020 — Canonical Semantic Schema

We will construct the actual lowest-level structures, beginning with something like:

SemanticDefinition
PrimitiveDefinition
AbstractPrimitiveDefinition
CapabilityDefinition
FrameworkDefinition
PolicyDefinition
ResourceTypeDefinition
OfferingDefinition
OperationDefinition
StateMachineDefinition
EventDefinition
InvariantDefinition
MerchantConfiguration

and establish the relationships between them.

This is the point where the prototype starts becoming a real engineering blueprint, while still remaining implementation-independent.