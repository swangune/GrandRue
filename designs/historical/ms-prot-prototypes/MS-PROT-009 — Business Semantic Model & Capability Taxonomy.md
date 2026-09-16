MS-PROT-009 — Business Semantic Model & Capability Taxonomy

Status: Proposed
Purpose: Define how Main Street represents fundamentally different businesses using a common semantic foundation without forcing every business into the same operational model.

This is the layer that makes the previous work scalable.

The central decision is:

> A merchant is not primarily classified by a single business category. It is represented by the combination of the business activities, resources, offerings, constraints, requirements and policies that actually constitute its operation.




---

1. Reject the single-category model

We should not build:

Merchant
 └── BusinessType
      ├── Grocery
      ├── Mechanic
      ├── Solicitor
      ├── Motel
      └── Realtor

That looks attractive initially but becomes brittle very quickly.

For example, a mechanic may offer:

workshop servicing;

mobile servicing;

parts sales;

vehicle collection;

consultation.


Calling the merchant simply:

MECHANIC

doesn't tell the operational engine enough.


---

2. Use a semantic composition model

Instead:

Merchant
   │
   ├── Activities
   ├── Offerings
   ├── Resources
   ├── Capabilities
   ├── Requirements
   ├── Policies
   ├── Schedules
   └── Constraints

The business category becomes context, rather than the complete definition.


---

3. The semantic dimensions

I recommend seven primary dimensions.

BUSINESS SEMANTIC MODEL

1. Activity
2. Offering
3. Actor
4. Resource
5. Capacity
6. Requirement
7. Policy

Schedule and location are important contextual dimensions attached to these objects rather than being treated as an entirely separate business identity.


---

4. Activity

An activity describes something the merchant does.

Examples:

SELL
SERVICE
REPAIR
CONSULT
RENT
ACCOMMODATE
DELIVER
INSTALL
INSPECT
VIEW
ASSESS

These are deliberately broad.

A mechanic:

REPAIR
SERVICE

A structural engineer:

CONSULT
INSPECT
ASSESS

A realtor:

LIST
MARKET
VIEW
NEGOTIATE

A motel:

ACCOMMODATE


---

5. Activity is not capability

This distinction matters.

Activity:

> What the merchant does.



Capability:

> What Main Street must enable the merchant/customer to do operationally.



For example:

Activity:
SERVICE

might require:

Capability:
BOOKING
SCHEDULING
PAYMENT
RESOURCE_ALLOCATION

Another service business might only require:

SERVICE
PAYMENT

Therefore activities inform configuration; they do not dictate it.


---

6. Offering

An Offering is what the customer can actually obtain.

Examples:

Oil Change
Brake Inspection
Structural Consultation
Standard Room
2-Bedroom Property
Milk
Brake Pad Set
Property Viewing

This is where the business's commercial catalogue lives.


---

7. Offerings have semantic characteristics

An offering can declare characteristics such as:

Offering
 ├── type
 ├── activity
 ├── price
 ├── duration
 ├── quantity
 ├── schedule
 ├── location
 ├── requirements
 ├── capacity requirements
 └── policies

Not every offering uses every field.

This is critical.


---

8. Optional semantics, not giant forms

A grocery product doesn't need:

duration
technician
appointment
vehicle registration

A mechanic's service doesn't need:

inventory quantity

unless it actually uses inventory.

So Main Street should have:

semantic applicability

rather than:

every merchant gets every field


---

9. Actor

We need a generic actor concept.

ACTOR
 ├── Customer
 ├── Merchant
 ├── Staff
 └── External participant

An actor can perform actions permitted by the relevant capability and policy.

This means we don't create separate operational engines for "customer actions" and "merchant actions" unless their authorization or context differs.


---

10. Resource

A resource is something operationally necessary to fulfil an offering.

Examples:

Mechanic
Workshop Bay
Room
Vehicle
Consultant
Property
Delivery Vehicle
Equipment

Resources may be:

allocatable
schedulable
quantity-bearing
stateful

or combinations of these.


---

11. Capacity

Capacity answers:

> How much can this resource/business fulfil under the current constraints?



Examples:

8 rooms
2 mechanics
1 workshop bay
20 units
6 consultation hours

Capacity is deliberately separate from resources because not all capacity is a one-to-one physical object.


---

12. Requirement

A requirement answers:

> What information or condition must exist before an operation can proceed?



This directly solves the problem you identified earlier.

Grocery delivery

Delivery Address

Mechanic service

Vehicle Registration

Mobile mechanic

Service Location
Vehicle Registration

Property viewing

Customer Contact
Property

Consultation

Consultation Subject
Customer Contact

The requirement is attached to the operation/offering where it is relevant, not globally to the merchant.


---

13. Policy

A policy answers:

> Under what rules is an operation permitted?



Examples:

PAYMENT_REQUIRED
CUSTOMER_CANCELLATION_ALLOWED
CUSTOMER_SELECTS_TIME
AUTOMATIC_ALLOCATION
DELIVERY_AVAILABLE
CUSTOMER_SELECTS_RESOURCE

Policies are merchant-controlled within the boundaries Main Street supports.


---

14. Schedule

Schedule is a constraint over operations and resources.

For example:

Merchant:
09:00–17:00

Consultant:
10:00–16:00

Offering:
11:00–15:00

Appointment:
14:00–15:30

Availability emerges from their intersection.

This is much cleaner than embedding schedule logic into every business niche.


---

15. Location

Location similarly acts as a constraint/context.

For example:

Grocery
    ↓
customer delivery location

versus:

Mechanic
    ↓
vehicle service location

versus:

Motel
    ↓
merchant premises

The same primitive:

LOCATION

is reused differently.


---

16. The semantic lattice

Rather than a rigid hierarchy, we should have a graph.

For example:

MERCHANT
                             │
              ┌──────────────┼──────────────┐
              ▼              ▼              ▼
           ACTIVITY       OFFERING       RESOURCE
              │              │              │
              ▼              ▼              ▼
         CAPABILITY      REQUIREMENT     CAPACITY
              │              │              │
              └──────────────┼──────────────┘
                             ▼
                           POLICY
                             │
                             ▼
                          OPERATION

These objects can reference one another.

This is more expressive than:

Merchant → BusinessType → Features


---

17. Business niche becomes a semantic fingerprint

Now we can represent a merchant as something like:

Merchant Semantic Fingerprint
{
    activities: [...]
    offering_patterns: [...]
    resource_patterns: [...]
    capability_patterns: [...]
    requirement_patterns: [...]
    policy_patterns: [...]
}

A grocery shop and spare-parts dealer can both contain:

SELL
INVENTORY
ORDER
PAYMENT

but their additional semantic dimensions differ.


---

18. Grocery example

GROCERY

Activities
    SELL
    DELIVER

Offerings
    PRODUCTS

Resources
    INVENTORY

Capabilities
    ORDERING
    PAYMENT
    DELIVERY

Requirements
    delivery → CUSTOMER_ADDRESS

Policies
    delivery_enabled
    payment_required


---

19. Spare-parts dealer

SPARE_PARTS

Activities
    SELL

Offerings
    VEHICLE_PART

Resources
    INVENTORY

Capabilities
    ORDERING
    PAYMENT

Requirements
    product compatibility information
    potentially vehicle information

Policies
    payment_required

The shared semantics are reused.

The differences are compositional.


---

20. Mechanic

MECHANIC

Activities
    SERVICE
    REPAIR

Offerings
    VEHICLE_SERVICE

Resources
    TECHNICIAN
    WORKSHOP_BAY

Capabilities
    BOOKING
    SCHEDULING
    PAYMENT
    RESOURCE_ALLOCATION

Requirements
    VEHICLE_REGISTRATION

Policies
    payment_required
    customer_selects_time

If mobile service is enabled:

Requirements
    VEHICLE_REGISTRATION
    SERVICE_LOCATION

No new fundamental primitive is necessary.


---

21. Structural engineer

STRUCTURAL_ENGINEER

Activities
    CONSULT
    INSPECT
    ASSESS

Offerings
    CONSULTATION
    INSPECTION

Resources
    ENGINEER

Capabilities
    CONSULTATION
    SCHEDULING
    PAYMENT

Requirements
    project_information
    customer_contact

Policies
    payment_required


---

22. Solicitor

SOLICITOR

Activities
    CONSULT
    ADVISE
    REPRESENT

Offerings
    LEGAL_CONSULTATION

Resources
    SOLICITOR

Capabilities
    CONSULTATION
    SCHEDULING
    PAYMENT

Requirements
    matter_information
    customer_contact

Policies
    payment_required

Notice that the structural engineer and solicitor share a large amount of operational infrastructure.

But Main Street doesn't need to pretend they are the same business.


---

23. Motel

MOTEL

Activities
    ACCOMMODATE

Offerings
    ROOM_CATEGORY

Resources
    ROOMS

Capabilities
    ACCOMMODATION
    BOOKING
    PAYMENT
    RESOURCE_ALLOCATION

Requirements
    guest_information

Policies
    automatic_allocation
    customer_selects_category
    payment_required

The room's operational lifecycle remains governed by the resource/state framework we already established.


---

24. Realtor

REALTOR

Activities
    LIST
    MARKET
    VIEW
    NEGOTIATE

Offerings
    PROPERTY
    PROPERTY_VIEWING

Resources
    PROPERTY
    AGENT

Capabilities
    LISTING
    VIEWING
    SCHEDULING
    CONSULTATION
    PAYMENT

Requirements
    customer_contact
    viewing_information

Again, no bespoke "realtor engine."


---

25. The important discovery

The real reusable unit is not the business niche.

It is the operational pattern.

For example:

SERVICE

can appear in:

mechanic
engineer
solicitor
cleaner
beautician
electrician

And:

BOOKING

can appear in:

mechanic
motel
consultant
realtor

And:

PAYMENT

can appear almost everywhere.

Therefore the architecture should optimise around patterns, not industries.


---

26. Business profiles remain useful

This does not mean abandoning business niches.

A business profile can provide the AI with strong priors.

For example:

Business profile:
MOTEL

suggests:

ACCOMMODATION
ROOM
STAY
BOOKING

But the final operational model is still derived from the merchant's actual answers.

Thus:

Niche = inference shortcut
Semantic model = operational truth

That distinction is important.


---

27. This fits the onboarding strategy

The onboarding sequence becomes:

Merchant answers simple questions
            ↓
Business understanding
            ↓
Initial semantic fingerprint
            ↓
Capability inference
            ↓
Requirement inference
            ↓
Policy inference
            ↓
Configuration validation
            ↓
Merchant review

The merchant isn't configuring the system.

They are confirming Main Street's understanding of their business.


---

28. Merchant approval remains essential

The system may infer:

PAYMENT_REQUIRED = true

but the merchant decides whether that is actually correct.

Likewise:

DELIVERY_ENABLED = true

must not silently become operational simply because AI inferred it.

The process remains:

INFER
 ↓
PRESENT
 ↓
MERCHANT APPROVES
 ↓
ACTIVATE


---

29. Configuration inheritance

We can also use layered defaults.

Global semantic defaults
        ↓
Framework defaults
        ↓
Business-profile defaults
        ↓
Merchant configuration
        ↓
Offering-specific configuration

For example:

SERVICE_BOOKING
    default → requires scheduling

Merchant
    scheduling = enabled

Offering
    "Emergency call-out"
    scheduling = immediate/dispatch

The more specific configuration overrides the more general one.

This avoids duplicating configuration.


---

30. But inheritance must be controlled

We should not create arbitrary inheritance chains.

A dangerous model would be:

A inherits B
B inherits C
C overrides D
D depends on E

That becomes impossible to reason about.

Instead:

> Configuration precedence must be finite, explicit and deterministic.



For example:

Global
→ Framework
→ Merchant
→ Offering

and no arbitrary user-defined inheritance.


---

31. Semantic compatibility

Every capability should declare compatibility constraints.

For example:

AUTOMATIC_ALLOCATION
requires:
    allocatable_capacity

Therefore this configuration:

CONSULTATION
AUTOMATIC_ALLOCATION

is valid only if the consultation has an allocatable resource.

Similarly:

DELIVERY

requires a valid delivery mechanism/configuration.


---

32. Capability dependency graph

Capabilities can declare dependencies.

Example:

BOOKING
   ├── requires SCHEDULING
   └── requires ALLOCATION

But not every booking needs physical resource allocation in exactly the same form.

Therefore dependencies must be expressed at the framework level, not as simplistic global rules.

This is another reason frameworks are important.


---

33. The taxonomy should therefore have three layers

I recommend formally separating:

Layer A — Semantic vocabulary

SELL
SERVICE
BOOK
PAY
RESOURCE
CUSTOMER
LOCATION
TIME

Layer B — Operational patterns

COMMERCE_ORDER
SERVICE_BOOKING
ACCOMMODATION_STAY
CONSULTATION
PROPERTY_VIEWING
DELIVERY

Layer C — Business composition

Merchant
    =
SERVICE
+
BOOKING
+
VEHICLE_REQUIREMENT
+
MOBILE_SERVICE
+
PAYMENT

This is much more manageable than creating thousands of business types.


---

34. Main Street can therefore grow horizontally

Suppose tomorrow we add:

Dog groomer

We don't need:

DOG_GROOMER_ENGINE

We might compose:

SERVICE
BOOKING
SCHEDULING
PAYMENT
PET_REQUIREMENT

If those semantics already exist, the new niche costs very little.

That is exactly the scalability property we want.


---

35. New primitives should be expensive

This should become a governance rule:

> Adding a new primitive requires evidence that existing primitives and abstractions cannot express the required business behaviour cleanly.



Adding a framework is much cheaper.

Adding a business profile is cheaper still.

Therefore:

New merchant niche
        ↓
try existing composition
        ↓
new business profile if useful
        ↓
new framework if recurring pattern emerges
        ↓
new semantic primitive only if unavoidable

This gives us controlled semantic growth.


---

36. MS-PROT-009 decisions

Decision	Status

Business niche is not the operational model	Accepted
Merchant represented through semantic composition	Accepted
Activity is distinct from capability	Accepted
Offering is a first-class semantic object	Accepted
Resource and capacity remain separate	Accepted
Requirements are operation/offering-specific	Accepted
Policies are explicit	Accepted
Schedule/location act as operational constraints	Accepted
Semantic graph preferred over rigid business hierarchy	Accepted
Business profile provides inference priors, not operational truth	Accepted
Operational patterns are more reusable than niches	Accepted
Frameworks abstract recurring patterns	Accepted
Merchant configuration is layered	Accepted in principle
Configuration precedence must be deterministic	Accepted
New primitives require strong justification	Accepted
AI selects/composes existing semantics	Accepted
Merchant approves inferred configuration	Accepted



---

37. The prototype is now approaching the right shape

We have effectively separated Main Street into two very different concerns:

BUSINESS UNDERSTANDING
                   │
                   ▼
        SEMANTIC CONFIGURATION
                   │
          ┌────────┴────────┐
          ▼                 ▼
     Operational        Presentation
     configuration       configuration
          │                 │
          ▼                 ▼
       RUNTIME          CUSTOMER UI
          │
          ▼
    REAL BUSINESS

And the operational configuration is built from:

Vocabulary
    ↓
Patterns
    ↓
Frameworks
    ↓
Merchant composition

rather than AI-generated code.


---

Next: MS-PROT-010 — Offering & Operation Model

The next level should get much more concrete.

We need to define exactly what an Offering is and how an Offering becomes an Operation.

That is where we can finally unify:

grocery purchase
spare-part purchase
car wash
mechanic appointment
consultation
motel booking
property viewing
home delivery
mobile service

under a small number of operational forms.

The key question will be:

> What is the minimum structure required to describe anything a customer can actually ask a merchant to do?



Once that is nailed down, we can begin building the detailed prototype specifications rather than continuing to expand the conceptual architecture.