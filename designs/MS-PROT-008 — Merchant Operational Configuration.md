MS-PROT-008 — Merchant Operational Configuration Framework

Status: Proposed
Purpose: Define how Main Street converts its understanding of a merchant into a small, validated, executable configuration—without requiring the merchant to understand the underlying system.

This is where your "Scratch/pixels/alphabets" idea becomes concrete.

The key decision is:

> Main Street should not generate arbitrary operational logic. It should compose a finite vocabulary of validated semantic primitives and abstractions.




---

1. Configuration is a composition, not generated software

We should explicitly reject this model:

AI
 ↓
generate custom business logic
 ↓
execute generated logic

Instead:

Business understanding
        ↓
AI inference
        ↓
select existing primitives
        ↓
compose existing abstractions
        ↓
validate
        ↓
merchant approval where required
        ↓
executable configuration

AI is therefore closer to a compiler front-end/configuration planner than a programmer.


---

2. The configuration hierarchy

We need several levels of abstraction.

Primitive
   ↓
Semantic Primitive
   ↓
Capability
   ↓
Framework
   ↓
Merchant Configuration

These levels are important because not everything should be expressed directly in primitives.


---

3. Primitive

A primitive is the smallest meaningful operation the runtime understands.

Examples:

CHECK
REQUIRE
VALIDATE
SELECT
CALCULATE
ALLOCATE
RELEASE
CONFIRM
CANCEL
COMPLETE
COLLECT
PAY
NOTIFY

These should be extremely stable.


---

4. Semantic primitives

Some operations require several primitive operations.

For example:

BOOK

might conceptually compose:

CHECK_REQUIREMENTS
CHECK_AVAILABILITY
ALLOCATE
CREATE_COMMITMENT

Therefore:

BOOK

is an abstract/semantic primitive, not necessarily a runtime atomic operation.

This is exactly the abstraction you were proposing.


---

5. Capability

A capability is a meaningful business ability.

Examples:

BOOKING
ORDERING
DELIVERY
PAYMENT
SCHEDULING
INVENTORY
ACCOMMODATION
CONSULTATION
PROPERTY_VIEWING
RESOURCE_ALLOCATION

A capability is composed from semantic primitives.

For example:

BOOKING
    ↓
CHECK
ALLOCATE
CONFIRM
CANCEL


---

6. Framework

A framework is a validated composition for a recurring business pattern.

For example:

SERVICE_BOOKING

could provide:

Service
Schedule
Duration
Capacity
Booking
Cancellation
Payment

Another:

ACCOMMODATION_BOOKING

could provide:

Accommodation category
Capacity
Stay interval
Resource allocation
Booking
Check-in
Check-out
Room release

The framework prevents every merchant from reinventing these structures.


---

7. Merchant configuration

Finally:

Merchant
   ↓
selected frameworks
   ↓
selected capabilities
   ↓
policies
   ↓
requirements
   ↓
operational configuration

For example:

Motel
    ACCOMMODATION_BOOKING
    PAYMENT

with:

allocation = automatic
customer_selection = category
room_release = staff


---

8. This solves the niche problem

Remember the earlier question:

> Grocery and spare-parts dealer are both traders, but their information requirements differ.



We should therefore avoid:

TRADER

being treated as a complete operational model.

Instead:

TRADER
   ↓
common commercial primitives

Then:

GROCERY
   ↓
INVENTORY
   ↓
QUANTITY
   ↓
DELIVERY

while:

SPARE_PARTS
   ↓
INVENTORY
   ↓
PRODUCT_COMPATIBILITY
   ↓
VEHICLE/MACHINE INFORMATION

They share the commercial foundation but compose different capabilities.


---

9. Consultation has the same structure

Structural engineer:

CONSULTATION
+ technical project context

Solicitor:

CONSULTATION
+ legal matter context

Both can use:

CONSULTATION
    ↓
appointment
    ↓
duration
    ↓
schedule
    ↓
customer information
    ↓
payment policy

but each adds different semantic requirements.

The abstraction is therefore compositional, not hierarchical in the traditional rigid sense.


---

10. Configuration should be declarative

This is extremely important.

AI should produce something conceptually like:

capabilities:
  - consultation
  - scheduling
  - payment

policies:
  payment:
    required_for_confirmation: true

requirements:
  consultation:
    - customer_identity
    - contact_information

Not:

if customer clicks button X,
run function Y,
then inspect database Z...

The latter is programming.

The former is configuration.


---

11. Configuration schema

At the highest level:

MerchantConfiguration
{
    capabilities
    policies
    requirements
    resources
    offerings
    schedules
    state_models
    allocation_models
}

But these are not arbitrary dictionaries.

Every section is validated against Main Street's schema.


---

12. Configuration references primitives

For example:

capability:
    BOOKING

implementation:
    framework = SERVICE_BOOKING

The AI isn't generating the implementation.

It is selecting:

SERVICE_BOOKING

from the approved framework catalogue.


---

13. Framework parameters

A framework needs parameters.

For example:

SERVICE_BOOKING
{
    duration_source
    capacity_source
    scheduling_required
    payment_policy
    cancellation_policy
}

A mechanic might configure:

duration_source = offering
capacity_source = technician
scheduling_required = true

A consultant:

duration_source = consultation_type
capacity_source = consultant

Same framework.

Different composition.


---

14. Composition must be typed

This is essential if we want to avoid AI mistakes.

A capability should declare what it accepts.

For example:

ALLOCATE
requires:
    AllocatableCapacity

returns:
    Allocation

Then the configuration validator can reject:

ALLOCATE
    input = CustomerAddress

because the types don't match.

This is where the programming-language analogy becomes genuinely useful.


---

15. Main Street's internal semantic type system

We should therefore introduce a small semantic type system.

Examples:

Customer
Merchant
Offering
Resource
Capacity
Schedule
Requirement
Policy
Interaction
Command
Commitment
Allocation
Payment
Location
Vehicle
Property

These aren't database tables yet.

They are semantic types.


---

16. Relationships between types

For example:

Merchant
  └── offers → Offering

Offering
  └── requires → Capability

Capability
  └── consumes → Capacity

Commitment
  └── allocates → Capacity

Customer
  └── creates → Interaction

Interaction
  └── produces → Command

This creates a semantic graph.


---

17. Why the graph matters

AI can infer:

"This business repairs vehicles."

and map that to:

VEHICLE_SERVICE

The graph tells it what that implies.

For example:

VEHICLE_SERVICE
   ↓
SERVICE
   ↓
potentially BOOKING
   ↓
potentially VEHICLE_REQUIREMENT

AI is selecting relationships from an existing ontology rather than inventing them.


---

18. But we should not build an enormous ontology

This is an important warning.

We should not attempt to model every business concept in the world.

That would recreate the complexity we're trying to eliminate.

Instead:

> Build the smallest semantic vocabulary capable of expressing Main Street's supported operational capabilities.



When a new niche appears, we ask:

Can existing primitives compose this?

If yes:

no new primitive.

If not:

Is a new semantic abstraction genuinely necessary?

Only then do we expand the language.


---

19. Frameworks are the main complexity-control mechanism

Instead of AI repeatedly composing:

CHECK
VALIDATE
ALLOCATE
CONFIRM
...

we can have:

SERVICE_BOOKING

and:

ACCOMMODATION

and:

COMMERCE_ORDER

These frameworks are internally complex but externally simple.

That is exactly what we want.


---

20. Merchant sees neither primitives nor frameworks

This is another hard boundary.

Merchant sees:

Online booking
Payment required
Customer cancellation
Delivery

They do not see:

ALLOCATE
STATE_MACHINE
CAPACITY_POOL
EVENT_HANDLER

And they certainly don't see:

SERVICE_BOOKING_FRAMEWORK_V3

The complexity remains internal.


---

21. AI sees semantic configuration

The AI agent can operate at the semantic level:

"This mechanic offers mobile vehicle servicing."

Infer:

SERVICE
BOOKING
MOBILE_SERVICE
VEHICLE_REQUIREMENT
LOCATION_REQUIREMENT

Then the configuration validator determines whether that composition is valid.


---

22. Multiple specialist agents

This now makes your earlier specialist-agent idea much more viable.

Instead of one huge AI agent:

AI

we can have:

Business Understanding Agent
        ↓
Capability Configuration Agent
        ↓
Policy Configuration Agent
        ↓
Requirement Configuration Agent
        ↓
Operational Validation Agent

And later:

Presentation Inference Agent

The agents operate over the same controlled semantic language.

They cannot arbitrarily modify the runtime.


---

23. Agent boundaries

For example:

Business Understanding Agent

Determines:

What kind of business is this?
What does it offer?

Capability Agent

Determines:

Which capabilities are applicable?

Policy Agent

Determines:

Which policy values fit the merchant?

Requirement Agent

Determines:

What information is required in each operation?

Validation Agent

Determines:

Is the resulting composition internally coherent?

None of them invents a new primitive.


---

24. The compiler analogy is now strong

We can think of the system as:

Merchant answers
      ↓
Business semantic representation
      ↓
Intermediate configuration
      ↓
Semantic validation
      ↓
Compiled operational configuration
      ↓
Runtime

Similar to a compiler:

Source
 ↓
AST
 ↓
Type checking
 ↓
Intermediate representation
 ↓
Compilation
 ↓
Execution

But Main Street is not literally a programming language.

The programming-language concepts are useful because they give us:

types;

schemas;

composition;

validation;

dependency resolution;

versioning;

deterministic execution.



---

25. We should therefore name this layer

I recommend:

Main Street Semantic Configuration Language — MS-SCL

It is an internal declarative language, not something merchants program with.

Its job is to express:

business capability
+
requirements
+
policies
+
resources
+
capacity
+
state
+
allocation

in a machine-validatable form.


---

26. What MS-SCL must NOT contain

To prevent runaway complexity, it should not initially contain:

loops
arbitrary functions
arbitrary scripts
AI-generated code
database queries
network requests
general conditionals

It should contain controlled constructs such as:

USE
REQUIRE
ENABLE
DISABLE
ALLOW
DENY
MAP
COMPOSE
DEPEND
CONSTRAIN
ALLOCATE
TRANSITION

Even conditional behaviour should initially be represented through predefined policy/requirement dependency mechanisms.


---

27. Example — mechanic

Conceptually:

BUSINESS
    VEHICLE_SERVICE

CAPABILITIES
    SERVICE
    BOOKING
    SCHEDULING
    PAYMENT

REQUIREMENTS
    VEHICLE_IDENTIFIER
    CUSTOMER_CONTACT

POLICIES
    PAYMENT_REQUIRED = true
    CUSTOMER_SELECTS_TIME = true
    MOBILE_SERVICE = false

The actual runtime behaviour is compiled from the corresponding frameworks.


---

28. Example — motel

BUSINESS
    ACCOMMODATION

CAPABILITIES
    ACCOMMODATION
    BOOKING
    ALLOCATION
    PAYMENT

RESOURCES
    ROOM

OFFERINGS
    STANDARD
    EXECUTIVE

POLICIES
    CUSTOMER_SELECTS = CATEGORY
    ALLOCATION = AUTOMATIC
    ROOM_RELEASE = STAFF
    PAYMENT_REQUIRED = true
    HOLD = false

Again, no custom code.


---

29. The crucial validation step

After composition:

Candidate Configuration
        ↓
Schema validation
        ↓
Type validation
        ↓
Dependency validation
        ↓
Conflict validation
        ↓
Capability validation
        ↓
Invariant validation
        ↓
Operational simulation
        ↓
VALID

Only then is it eligible for activation.


---

30. We should add static configuration analysis

Before a configuration reaches runtime, Main Street should detect things such as:

Delivery enabled
BUT
no delivery method

or:

Booking enabled
BUT
no schedulable capacity

or:

Automatic allocation enabled
BUT
no allocatable resource

or:

Vehicle requirement activated
BUT
no operation uses vehicles

These are configuration errors, not runtime errors.

We should catch them early.


---

31. This also protects merchant simplicity

The merchant might simply answer:

> Do you offer mobile servicing?



Yes.

Main Street can infer:

MOBILE_SERVICE = enabled

which activates:

service_location requirement

The merchant doesn't have to discover:

> "Go to customer data requirements → add service-location dependency → attach it to mobile-service operation."



That is precisely the complexity we are hiding.


---

32. Merchant configuration becomes a constrained surface

The dashboard effectively becomes:

MERCHANT
                    │
          ┌─────────┴─────────┐
          ▼                   ▼
     Relevant controls    Operational data
          │
          ▼
      Policy changes
          │
          ▼
    Configuration compiler

The merchant changes intent, not implementation.


---

33. AI configuration is also constrained

AI does:

INFER
    ↓
COMPOSE
    ↓
VALIDATE

It does not:

INVENT

This should become a fundamental Main Street invariant:

> AI may select, compose and parameterise approved semantic constructs; it may not introduce executable semantics outside the approved language.




---

34. MS-PROT-008 decisions

Decision	Status

Configuration is declarative	Accepted
AI composes rather than generates runtime logic	Accepted
Primitive → semantic primitive → capability → framework → configuration	Accepted
Semantic types are required	Accepted
Frameworks abstract recurring compositions	Accepted
Merchant does not see internal primitives/frameworks	Accepted
AI cannot invent primitives	Accepted
Configuration must be statically validated	Accepted
Configuration must be versioned	Accepted
Configuration must pass invariant validation before activation	Accepted
Internal semantic configuration language is appropriate	Accepted in principle
MS-SCL should remain declarative	Accepted
MS-SCL should initially exclude arbitrary programming	Accepted
Merchant controls intent/policy, not implementation	Accepted



---

One important caution

We should not start designing MS-SCL syntax yet.

That would be premature.

We first need to finish the semantic model it expresses.

We now have:

MS-PROT-004  State & Transition
MS-PROT-005  Requirements & Policy
MS-PROT-006  Availability & Capacity
MS-PROT-007  Interaction, Command & Event
MS-PROT-008  Operational Configuration

The next logical layer is:

MS-PROT-009 — Business Semantic Model & Capability Taxonomy

This is where we solve the question you raised earlier:

> How do grocery, spare parts, mechanics, structural engineers, solicitors, motels and realtors share semantics without pretending they are the same business?



We need to establish the semantic lattice:

BUSINESS
                       │
             ┌─────────┴─────────┐
             │                   │
         COMMERCIAL           SERVICE
             │                   │
       ┌─────┴─────┐       ┌─────┴──────┐
       ▼           ▼       ▼            ▼
    GROCERY     PARTS   MECHANIC    CONSULTANT
       │           │       │            │
       └──────┬────┘       └─────┬──────┘
              │                  │
           shared             shared
          primitives          primitives

But rather than forcing businesses into a rigid hierarchy, we should design composable semantic dimensions.

That will become the foundation from which the AI can infer the right configuration for virtually any supported merchant without giving merchants a bewildering collection of features.