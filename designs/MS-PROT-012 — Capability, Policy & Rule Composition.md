MS-PROT-012 — Capability, Policy & Rule Composition

Status: Proposed
Purpose: Define how Main Street combines its finite semantic primitives into capabilities, frameworks, policies and executable configuration while preventing contradictory or unsafe configurations.

This is the specification that turns your "alphabets → words → sentences" analogy into an actual engineering mechanism.

The central rule is:

> AI chooses and composes approved semantic constructs; the Main Street configuration engine determines whether that composition is valid and executable.



AI does not become the source of truth.


---

1. The composition hierarchy

We now formally establish:

PRIMITIVE
    ↓
ABSTRACT PRIMITIVE
    ↓
CAPABILITY
    ↓
FRAMEWORK
    ↓
POLICY
    ↓
MERCHANT CONFIGURATION
    ↓
RUNTIME

But these levels have different responsibilities.


---

2. Primitive

A primitive represents a fundamental semantic operation.

Examples:

READ
CREATE
CHECK
VALIDATE
REQUIRE
SELECT
CALCULATE
ALLOCATE
RELEASE
CONFIRM
CANCEL
COMPLETE
NOTIFY

A primitive should be:

deterministic;

versioned;

typed;

independently testable;

semantically stable.



---

3. Abstract primitive

An abstract primitive combines primitives into a reusable semantic operation.

For example:

BOOK

could represent:

CHECK
+
VALIDATE
+
ALLOCATE
+
CONFIRM

But the exact implementation depends on the framework using it.

This gives us abstraction without forcing every agent to reason at the lowest level.


---

4. Capability

A capability is something Main Street enables a merchant to do.

Examples:

BOOKING
ORDERING
DELIVERY
CONSULTATION
LISTING
PAYMENT
SCHEDULING
RESOURCE_ALLOCATION
INVENTORY

A capability may use one or more abstract primitives.


---

5. Framework

A framework defines a validated operational pattern.

Examples:

SERVICE_BOOKING
COMMERCE_ORDER
ACCOMMODATION_STAY
PROPERTY_VIEWING
RESOURCE_RENTAL
DELIVERY
CONSULTATION

A framework determines:

applicable states;

permitted transitions;

required capabilities;

data requirements;

allocation behaviour;

policy points;

events;

invariants.


This is where most complexity should live.


---

6. Policy

A policy is a configurable decision within an approved framework.

Examples:

PAYMENT_REQUIRED
CUSTOMER_SELECTS_TIME
CUSTOMER_SELECTS_RESOURCE
DELIVERY_ENABLED
AUTOMATIC_ALLOCATION
CUSTOMER_CANCELLATION_ALLOWED

A policy should not contain arbitrary code.

It should select from a controlled set of valid behaviours.


---

7. Rule

A rule connects semantic conditions to an approved outcome.

For example:

IF
    DELIVERY_ENABLED

THEN
    REQUIRE DeliveryAddress

But importantly:

merchants and AI do not author arbitrary rules.

The framework owns the rule.

AI selects the configuration that causes the rule to apply.


---

8. The distinction between policy and rule

This distinction prevents considerable confusion.

Policy

Merchant preference:

> "I require payment before confirming bookings."



Rule

System consequence:

> "When this policy is enabled, a booking cannot become committed until the required payment condition is satisfied."



Therefore:

Merchant
    changes POLICY

Framework
    applies RULE

The merchant never modifies the underlying rule.


---

9. Example: payment

Merchant selects:

PAYMENT_REQUIRED = TRUE

The framework may have:

RULE:
Commitment requires payment condition

The merchant is not writing:

if payment == true...

Main Street already knows what that policy means.


---

10. Configuration as a graph

A merchant configuration should be represented conceptually as a graph:

Merchant
   │
   ├── Capability
   │      │
   │      └── Framework
   │              │
   │              ├── Requirements
   │              ├── Policies
   │              ├── Rules
   │              ├── States
   │              └── Events
   │
   └── Offerings
          │
          └── Framework configuration

This graph is validated before activation.


---

11. Configuration is declarative

A conceptual configuration could look like:

SERVICE_BOOKING

capabilities:
  - scheduling
  - payment

policies:
  payment:
    required: true

requirements:
  vehicle:
    registration: required

This is declaration, not executable code.

The runtime knows how to execute SERVICE_BOOKING.


---

12. AI's output

AI should produce an intermediate semantic configuration.

For example:

{
  framework: SERVICE_BOOKING,
  capabilities: [
    SCHEDULING,
    PAYMENT
  ],
  policies: [
    PAYMENT_REQUIRED
  ],
  requirements: [
    VEHICLE_REGISTRATION
  ]
}

The AI is effectively saying:

> "I believe these approved constructs describe this merchant."



It is not saying:

> "Here is the software Main Street should execute."




---

13. Configuration compiler

We therefore need a deterministic component:

AI inference
     ↓
Candidate Configuration
     ↓
Configuration Compiler
     ↓
Schema validation
     ↓
Type validation
     ↓
Dependency validation
     ↓
Conflict detection
     ↓
Invariant checking
     ↓
Compiled Configuration

The compiler is authoritative.


---

14. Why this matters

AI can make mistakes.

For example:

AI:
PAYMENT_REQUIRED
+
PAYMENT_NOT_REQUIRED

The configuration compiler rejects the composition.

AI cannot override the compiler.


---

15. Capability dependencies

Capabilities can have declared dependencies.

For example:

DELIVERY
    requires
        DELIVERY_METHOD

Or:

BOOKING
    requires
        scheduling model

Or a particular booking framework may require:

BOOKING
    + CAPACITY

The exact dependency belongs to the framework.


---

16. Dependency resolution

Configuration compilation performs:

requested capability
       ↓
dependency graph
       ↓
required supporting constructs
       ↓
verify availability

If a dependency is missing, Main Street has two choices:

Safe inference

Add an existing dependency automatically.

Ambiguity

Ask the merchant.

The system must never silently invent a business policy.


---

17. Example

Merchant enables:

DELIVERY

Framework determines:

Delivery requires delivery method.

If merchant already has:

HOME_DELIVERY

configuration is valid.

If no delivery method exists:

Configuration incomplete.

Main Street asks the merchant the smallest relevant question.


---

18. Conflict detection

We need explicit conflict classes.

Direct conflict

PAYMENT_REQUIRED
PAYMENT_NOT_REQUIRED

Structural conflict

AUTOMATIC_ALLOCATION

when there is no allocatable capacity.

Semantic conflict

CUSTOMER_SELECTS_RESOURCE

when the resource is intentionally hidden and assigned automatically.

Lifecycle conflict

ALLOW_CANCELLATION_AFTER_FULFILMENT

if that policy isn't supported by the framework.


---

19. Policy precedence

We need a deterministic hierarchy.

I recommend:

System invariants
      ↓
Framework rules
      ↓
Merchant policies
      ↓
Offering policies
      ↓
Operation context

But lower levels cannot override higher-level safety invariants.

For example:

System invariant:
never allocate unavailable capacity

cannot be overridden by:

Merchant:
I want to allow overbooking

unless overbooking is itself an explicitly supported framework capability.


---

20. Do not use arbitrary rule precedence

Avoid:

Rule 17 beats Rule 8
unless Rule 21 is active...

That becomes impossible to reason about.

Instead, every rule belongs to a known semantic layer.


---

21. Invariants

An invariant is a condition that must always hold.

Examples:

No commitment without valid offering.
No allocation without allocatable capacity.
No operation may bypass authorization.
No invalid configuration may activate.
No UI instruction may alter operational semantics.

These are stronger than merchant policies.


---

22. Invariants are not configurable

This is critical.

Merchant can configure:

payment required

Merchant cannot configure:

ignore authorization

AI cannot configure it either.

The runtime's safety invariants are outside merchant configuration.


---

23. Composition validation

We should use several validation passes.

Pass 1 — Syntax

Does the configuration conform to the schema?

Pass 2 — Type

Are semantic objects compatible?

Pass 3 — Dependencies

Are all required constructs available?

Pass 4 — Conflicts

Are mutually exclusive policies active?

Pass 5 — Invariants

Does the configuration violate fundamental system rules?

Pass 6 — Operational simulation

Can representative operations execute successfully?


---

24. Operational simulation

This is especially important.

A configuration should not merely be syntactically valid.

Suppose:

SERVICE_BOOKING

is configured.

We should simulate:

create booking request
→ collect requirements
→ check availability
→ allocate
→ confirm
→ cancel
→ release capacity

If the framework cannot complete the lifecycle, configuration is rejected.


---

25. The configuration test

Conceptually:

Configuration
      ↓
Generate representative scenarios
      ↓
Execute against deterministic simulator
      ↓
Expected invariants
      ↓
PASS / FAIL

This gives us a powerful safety net before deployment.


---

26. Policy activation

A merchant changing:

PAYMENT_REQUIRED

should not immediately mutate live operational state without validation.

Instead:

Merchant change
      ↓
Draft configuration
      ↓
Compile
      ↓
Validate
      ↓
Preview consequences
      ↓
Merchant confirms
      ↓
Publish

This also supports rollback.


---

27. Configuration versions

Every published configuration receives a version:

Merchant Configuration v12

A later change creates:

v13

We never silently mutate historical configuration.


---

28. Why versioning matters

Suppose a customer booked under:

Configuration v12

and the merchant later changes:

cancellation policy

to produce:

v13

The existing commitment must retain the appropriate historical policy context.

Otherwise historical transactions become ambiguous.


---

29. Configuration snapshots

Therefore consequential operations should reference the applicable configuration version.

Conceptually:

Commitment
    configuration_version = 12

This does not mean we duplicate the entire configuration inside the commitment.

It means the system can reconstruct the applicable semantic rules.


---

30. Draft versus published configuration

We should formally distinguish:

DRAFT
VALIDATED
PUBLISHED
RETIRED

A draft cannot drive production operations.

A published configuration can.

A retired configuration remains available for historical interpretation.


---

31. AI configuration lifecycle

The AI pipeline becomes:

Business answers
       ↓
Business semantic inference
       ↓
Candidate configuration
       ↓
Compiler
       ↓
Validation
       ↓
Merchant review
       ↓
Publish

This is substantially safer than:

AI → production


---

32. Different AI agents can share the same language

This is where your specialist-agent idea becomes powerful.

For example:

Business Agent
       ↓
MS-SCL

Operational Agent
       ↓
MS-SCL

Policy Agent
       ↓
MS-SCL

UI Agent
       ↓
UI semantic language

Every operational agent speaks the same controlled semantic language.


---

33. UI remains a separate language

We should therefore eventually have:

MS-SCL
Main Street Semantic Configuration Language

for operational behaviour.

And something conceptually like:

MS-UIL
Main Street UI Language

for presentation.

The UI agent consumes:

MS-SCL

as one of its inputs.

But it cannot change its meaning.


---

34. This gives us two compilers

Eventually:

Merchant Understanding
                           │
                           ▼
                    Semantic Model
                           │
                ┌──────────┴──────────┐
                ▼                     ▼
             MS-SCL                 MS-UIL
                │                     │
                ▼                     ▼
       Operational Runtime      Presentation Runtime

This is a very strong architectural separation.


---

35. Merchant dashboard

The merchant dashboard is also generated from configuration semantics.

For example, if:

PAYMENT_REQUIRED

is applicable, the dashboard exposes:

Payment
    Require payment before confirmation

If payment isn't relevant to a merchant, the control doesn't appear.

Thus:

Merchant configuration
        ↓
Relevant dashboard controls

The merchant never sees irrelevant capabilities.


---

36. Customer UI

Likewise:

Requirement:
VehicleRegistration

causes the UI layer to determine an appropriate way of collecting that information.

The operational layer doesn't care whether the UI uses:

a text field;

vehicle lookup;

a saved vehicle selector;

a conversational interaction.



---

37. Configuration surface becomes adaptive

This is one of the biggest benefits of the entire architecture.

Merchant A:

Payment
Booking
Delivery

sees those controls.

Merchant B:

Accommodation
Booking
Payment

sees those controls.

Merchant C:

Consultation
Scheduling
Payment

sees those controls.

No universal dashboard full of switches.


---

38. But configuration controls must themselves be semantic

The UI should not contain hard-coded business logic such as:

if mechanic:
    show vehicle registration

Instead:

configuration declares:
VEHICLE_REGISTRATION is applicable

The UI renderer responds to that semantic declaration.


---

39. The complete configuration chain

We can now draw the prototype architecture as:

MERCHANT
                   │
                   ▼
             Onboarding
                   │
                   ▼
          Business Understanding
                   │
                   ▼
          Semantic Business Model
                   │
                   ▼
        Capability / Framework Selection
                   │
                   ▼
             Policy Selection
                   │
                   ▼
          Configuration Compiler
                   │
        ┌──────────┴──────────┐
        ▼                     ▼
   Operational             UI Semantic
   Configuration           Configuration
        │                     │
        ▼                     ▼
   Runtime Engine        UI Runtime

That is now a coherent architecture.


---

40. The strongest architectural invariant

I recommend we formally adopt:

> No AI-generated configuration becomes executable solely because an AI agent generated it. Every configuration must be represented using approved semantic constructs and pass deterministic schema, type, dependency, conflict, invariant and operational validation before publication.



This should eventually appear in MS-AVS, not merely the prototype documents.


---

41. Another important invariant

> Policies express supported business choices; rules express system consequences; primitives express system semantics. These three layers must not be conflated.



This prevents the configuration language from becoming a disguised programming language.


---

42. MS-PROT-012 decisions

Decision	Status

Primitive/abstract primitive/capability/framework hierarchy	Accepted
Policies represent merchant-configurable choices	Accepted
Rules represent framework-defined consequences	Accepted
AI cannot author arbitrary runtime rules	Accepted
Configuration is declarative	Accepted
Configuration compiler is authoritative	Accepted
Dependency resolution required	Accepted
Conflict detection required	Accepted
System invariants cannot be overridden	Accepted
Deterministic precedence required	Accepted
Configuration requires multiple validation passes	Accepted
Operational simulation required before publication	Accepted in principle
Draft/published/retired states	Accepted
Configuration versioning	Accepted
Historical configuration must remain interpretable	Accepted
Specialist AI agents share semantic contracts	Accepted
Operational semantics and UI semantics remain separate	Accepted
Merchant dashboard generated from applicable configuration	Accepted
Customer UI consumes operational semantics but does not redefine them	Accepted



---

Where we are now

The core semantic architecture is becoming stable:

MS-PROT-004  State & Transition
MS-PROT-005  Requirements & Policy
MS-PROT-006  Resource, Capacity & Allocation
MS-PROT-007  Interaction, Command & Event
MS-PROT-008  Operational Configuration
MS-PROT-009  Business Semantic Model
MS-PROT-010  Offering & Operation
MS-PROT-011  Data Context
MS-PROT-012  Capability, Policy & Rule Composition

The next specification should move into a different problem.

MS-PROT-013 — Configuration Compilation, Validation & Publication

We have just defined what configuration means. Now we need to design, at the lowest level, how Main Street takes an AI-inferred configuration and safely turns it into an immutable, executable merchant configuration.

That specification should define the actual pipeline:

INFER
 ↓
NORMALISE
 ↓
RESOLVE
 ↓
TYPE-CHECK
 ↓
DEPENDENCY-CHECK
 ↓
CONFLICT-CHECK
 ↓
INVARIANT-CHECK
 ↓
SIMULATE
 ↓
GENERATE CONFIGURATION ARTIFACT
 ↓
MERCHANT APPROVAL
 ↓
PUBLISH
 ↓
VERSION
 ↓
RUNTIME

After that, we can stop refining the abstract semantic model and begin producing the actual detailed prototype drawings and schemas you wanted.