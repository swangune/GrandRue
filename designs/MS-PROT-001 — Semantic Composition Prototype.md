Yes. We now move into MS-PROT-001, Part 1: Primitive Registry.

The important thing is that we should not treat every noun or database object as a primitive. A primitive is a reusable semantic operation or fundamental relationship from which higher-level capabilities can be composed.

MS-PROT-001 — Semantic Composition Prototype

Status: Prototype
Purpose: Establish the smallest reusable semantic vocabulary capable of configuring materially different Main Street businesses.


---

1. Primitive taxonomy

I recommend five levels rather than one giant list:

SEMANTIC SYSTEM
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
     NATURE                 ACTION                CONTROL
        │                     │                     │
   Offering              Request                Policy
   Person                Create                 Permission
   Resource              Update                 Requirement
   Location              Cancel                 Constraint
   Capacity              Confirm
   Identity              Allocate
                         Schedule
                         Pay
                         Notify
                              │
                              ▼
                       ABSTRACT FRAMEWORKS
                              │
                              ▼
                         CAPABILITIES

This gives us an important distinction:

Primitive ≠ Capability ≠ Framework ≠ Policy.


---

2. Fundamental semantic primitives

These are the concepts that the system needs to be able to reason about.

OFFERING

Something a merchant makes available to customers.

Examples:

Standard Room
Oil Change
Milk
House for Sale
Structural Engineering Consultation

An Offering can subsequently be classified/composed as a product, service, hybrid, etc.


---

RESOURCE

Something whose availability or allocation can constrain an operation.

Examples:

Room
Mechanic
Service Bay
Delivery Capacity
Consultant
Vehicle

But not everything involved in a business is automatically a Resource.


---

CAPACITY

The quantity of something that can be fulfilled.

This is crucial for the motel model.

Standard rooms = 8
Executive rooms = 4

The customer doesn't need to know:

Room 101
Room 102
...

The booking system initially reasons about:

Standard capacity = 8

and allocation happens internally.


---

SCHEDULE

A temporal constraint describing when something can occur.

Examples:

Merchant opening hours
Consultant availability
Service appointment
Room expected checkout
Delivery window

This becomes particularly important for the hybrid/service/bookable problem we discussed.


---

LOCATION

A physical or operational location relevant to an activity.

Examples:

Merchant premises
Customer delivery address
Service location
Property location


---

IDENTITY

A representation of an identifiable actor/entity.

It remains separate from authentication and accounts.


---

PARTICIPANT

An entity participating in an operation.

Potential participants include:

Customer
Merchant
Staff
System
External Provider


---

3. Action primitives

Now the actual verbs.

REQUEST

Someone asks the system or merchant to perform something.

Customer → Request → Booking
Customer → Request → Enquiry
Customer → Request → Delivery

A request does not necessarily create a transaction.


---

OFFER

Make something available for consideration.

Merchant → Offer → Service
Merchant → Offer → Product
Merchant → Offer → Room Category

This is distinct from OFFERING, which is the domain object.


---

SELECT

A participant chooses among available options.

For example:

Customer
   ↓
Select
   ↓
Standard Room

This supports the important motel principle:

> Customer selects the category, not the physical room.




---

RESERVE

Temporarily or persistently hold capacity/resource according to policy.

This primitive needs very strict semantics because it is where concurrency enters.


---

ALLOCATE

Assign a specific capacity/resource.

Motel:

Standard category
      ↓
ALLOCATE
      ↓
Room 104

The customer never needs to know that allocation occurred.

This is a very important separation:

SELECT category
      ≠
ALLOCATE resource


---

SCHEDULE

Place an activity into a temporal slot.

Service
   ↓
Schedule
   ↓
Tuesday 14:00


---

CONFIRM

Turn a valid request into a confirmed business commitment according to the applicable capability/policy.


---

CANCEL

Terminate a pending or confirmed operation according to its rules.


---

COMPLETE

Record that the owning capability has reached its completion condition.

This must not be confused with a universal interaction state.


---

PAY

Initiate or execute a financial operation.

We should eventually split payment semantics much further, but the primitive vocabulary can initially use PAY while the Payment domain defines its precise lifecycle.


---

REFUND

Reverse or return funds according to financial rules.


---

FULFIL

Perform the actual business fulfilment.

Examples:

Grocery → prepare order
Mechanic → perform service
Motel → provide accommodation

The precise meaning remains capability-specific.


---

DELIVER

Move an ordered offering to a customer/location.


---

COLLECT

Customer receives something by collection rather than delivery.

Useful for:

Grocery
Spare parts
Retail
Equipment hire


---

NOTIFY

Communicate an event/status/action to an actor.

The Notification capability determines channel and delivery mechanics.


---

4. Control primitives

This is where Main Street's simplicity becomes powerful.

REQUIRE

A business can require information or an action.

Examples:

Grocery + Delivery
→ require delivery address

Mechanic
→ require vehicle registration

Consultation
→ require upfront payment

We do not create separate customer models for these.

We compose requirements.


---

ALLOW

Defines something permitted by configuration/policy.

allow:
    walk_in
    online_booking
    customer_cancellation


---

DENY

Explicitly prevents an operation.


---

ENABLE

Activates a capability/policy.


---

DISABLE

Deactivates it.

This is important because the merchant's configuration model fundamentally becomes:

Main Street understands business
             ↓
Available capabilities
             ↓
Merchant-controlled policies
             ↓
ENABLE / DISABLE


---

LIMIT

Restricts something.

Examples:

maximum bookings
maximum quantity
maximum guests
maximum delivery radius


---

CONSTRAIN

Expresses a domain constraint.

For example:

Booking
requires:
    capacity > 0

or:

Delivery
requires:
    valid address


---

5. Information primitives

We also need a controlled way of representing information requirements.

IDENTIFY

Establish who/what is involved.


---

COLLECT

Obtain information from an actor.

For example:

vehicle registration
delivery address
customer phone


---

VALIDATE

Determine whether information/action satisfies a rule.


---

VERIFY

Establish sufficient evidence for a domain assertion.

This remains distinct from ordinary validation.


---

6. We already see our first abstract frameworks

Now we can compose primitives.

Booking Framework

REQUEST
   ↓
SELECT
   ↓
CHECK AVAILABILITY
   ↓
RESERVE / ALLOCATE
   ↓
CONFIRM
   ↓
SCHEDULE

But not every booking uses every operation.

That is why this is a framework, not a primitive.


---

Service Appointment Framework

REQUEST
   ↓
SELECT OFFERING
   ↓
CHECK SCHEDULE
   ↓
SCHEDULE
   ↓
CONFIRM


---

Product Ordering Framework

SELECT
   ↓
ORDER
   ↓
CHECK CAPACITY/INVENTORY
   ↓
CONFIRM
   ↓
PAY
   ↓
FULFIL
   ↓
DELIVER / COLLECT


---

Enquiry Framework

REQUEST
   ↓
COLLECT INFORMATION
   ↓
NOTIFY MERCHANT
   ↓
RESPOND
   ↓
COMPLETE / CLOSE

Notice something important:

We haven't created "Motel", "Mechanic", "Grocery" or "Realtor" primitives.

That's exactly what we want.


---

7. The next architectural layer: Policies

Policies determine how the primitives behave for a particular merchant.

For example:

BOOKING
    │
    ├── payment_confirmation = required
    ├── temporary_allocation = 10 minutes
    ├── customer_selection = category
    ├── allocation = automatic
    └── availability_release = staff_action

Another merchant:

BOOKING
    │
    ├── payment_confirmation = not_required
    ├── temporary_allocation = none
    ├── customer_selection = offering
    └── availability = schedule_based

Same framework.

Different configuration.


---

8. This solves the problem you raised about "fluid" availability

We don't need a universal temporary state.

Instead:

Availability
      +
Policy
      +
Request
      ↓
Decision

For a car wash:

Customer A starts booking
        ↓
No payment requirement
        ↓
No reservation created
        ↓
Slot remains available
        ↓
Customer B books
        ↓
Customer A returns
        ↓
System recalculates availability
        ↓
Original slot unavailable
        ↓
Alternative slot presented

For a merchant requiring payment:

Customer A requests slot
        ↓
Payment confirmation required
        ↓
10-minute temporary reservation
        ↓
Payment succeeds → confirm
        OR
Payment expires → release

The policy determines the behaviour.

We don't need:

CAR_WASH_SPECIAL_CASE
MOTEL_SPECIAL_CASE
SALON_SPECIAL_CASE


---

9. The key prototype rule

We should now impose a very strict test on every proposed primitive:

Primitive Acceptance Test

A primitive may enter the Main Street vocabulary only if:

1. It represents a meaningful domain concept/action.


2. It appears across more than one materially different business scenario or is fundamental infrastructure.


3. Its semantics can be defined independently of a specific UI.


4. Its semantics can be defined independently of a particular business niche.


5. It has clear inputs, outputs and invariants.


6. It cannot be adequately represented by an existing primitive.


7. Adding it reduces rather than increases special-case logic.



If it fails those tests:

do not add it.


---

10. We should not freeze this registry yet

This is still PROPOSED.

The next step is more important than adding more primitives:

Run the primitives against the four verticals.

We create a matrix:

Operation	Motel	Mechanic	Grocery	Realtor

Request	✓	✓	✓	✓
Select	✓	✓	✓	✓
Reserve	✓	✓	?	?
Allocate	✓	✓	✓	?
Schedule	✓	✓	?	✓
Confirm	✓	✓	✓	✓
Pay	✓	✓	✓	?
Fulfil	✓	✓	✓	?
Deliver	?	?	✓	?
Collect	?	?	✓	?
Enquire	?	✓	?	✓
Require	✓	✓	✓	✓
Notify	✓	✓	✓	✓


The ? cells are not assumptions.

We investigate them.

And if the matrix exposes something that our primitive vocabulary cannot represent, then—and only then—we consider adding or changing a primitive.

That is the next piece of the prototype.

After that, we can construct the first real Capability Registry from the validated primitive vocabulary.


Good. We now move from the proposed primitive vocabulary to the cross-vertical composition test. This is where we determine whether the vocabulary is actually good enough.

MS-PROT-001 — Part 2: Primitive Validation Matrix

Status: Proposed — validation in progress

Our test businesses are:

1. Motel


2. Mechanic


3. Grocery


4. Real-estate realtor



The objective is not to force every business through the same workflow. The objective is to determine whether the same semantic building blocks can represent their differences.


---

1. First correction: not everything is an action

Our previous registry was still mixing several categories.

We should formalise the hierarchy now:

SEMANTIC VOCABULARY
│
├── Entities / Things
│   ├── Offering
│   ├── Resource
│   ├── Capacity
│   ├── Schedule
│   ├── Location
│   └── Identity
│
├── Actions
│   ├── Request
│   ├── Select
│   ├── Reserve
│   ├── Allocate
│   ├── Schedule
│   ├── Confirm
│   ├── Cancel
│   ├── Complete
│   ├── Pay
│   ├── Refund
│   ├── Fulfil
│   ├── Deliver
│   ├── Collect
│   └── Notify
│
├── Information
│   ├── Identify
│   ├── Collect
│   ├── Validate
│   └── Verify
│
└── Policy / Control
    ├── Require
    ├── Allow
    ├── Deny
    ├── Enable
    ├── Disable
    ├── Limit
    └── Constraint

This is already better because the AI composition engine will know what it is manipulating.


---

2. Motel composition

Let's model the simple motel, not the five-star hotel we previously drifted toward.

Assume:

room categories;

finite room capacity;

customer chooses category;

Main Street automatically allocates a room;

payment may be required;

staff makes rooms available after cleaning;

customer does not choose a room number.


Semantic composition

Accommodation
│
├── Offering
│     ├── Standard
│     └── Executive
│
├── Capacity
│     ├── Standard: 8
│     └── Executive: 4
│
├── Resource
│     └── Physical rooms
│
├── Booking
│
├── Allocation
│
├── Availability
│
├── Payment
│
└── Customer

The customer operation becomes:

REQUEST
   ↓
SELECT category
   ↓
CHECK CAPACITY
   ↓
ALLOCATE room internally
   ↓
PAY / CONFIRM according to policy
   ↓
CHECK-IN
   ↓
STAY
   ↓
CHECK-OUT
   ↓
ROOM UNAVAILABLE
   ↓
STAFF CLEANING
   ↓
STAFF MARKS AVAILABLE

The critical insight is:

Availability is not a room-state UI concept anymore.

It is a capacity decision.

The merchant may see:

Standard
8 total
5 available
3 occupied/unavailable

The customer simply gets:

Standard
Available / Not available

or an alternative category.

That is substantially simpler.


---

3. Mechanic composition

Now change the domain completely.

Assume:

services;

appointments;

vehicle registration;

optional payment;

no resource management required initially.


Service
│
├── Offering
│     ├── Oil change
│     ├── Brake service
│     └── Diagnostics
│
├── Schedule
├── Booking
├── Customer
├── Vehicle information
├── Payment
└── Notification

Customer:

REQUEST
   ↓
SELECT SERVICE
   ↓
COLLECT vehicle registration
   ↓
CHECK SCHEDULE
   ↓
SCHEDULE
   ↓
CONFIRM

Notice what disappeared:

Capacity
Room allocation
Physical room state
Accommodation

We haven't created a Mechanic architecture.

We composed capabilities.


---

4. Grocery composition

Now:

Retail
│
├── Catalogue
├── Offering
├── Inventory
├── Ordering
├── Payment
├── Fulfilment
├── Delivery
├── Collection
└── Customer

Customer:

SELECT PRODUCTS
      ↓
CREATE ORDER
      ↓
CHOOSE:
   ├── DELIVERY
   └── COLLECTION
      │
      ├── Delivery → REQUIRE ADDRESS
      │
      └── Collection → REQUIRE COLLECTION DETAILS
      ↓
PAY
      ↓
FULFIL
      ↓
DELIVER / COLLECT

Again, the important thing is that customer requirements are conditional.

We do not create:

GroceryCustomer

We create:

Requirement:
Delivery → Address


---

5. Realtor composition

This is a very useful test because it doesn't look like the other three.

Assume:

property catalogue;

enquiries;

property viewings;

scheduling;

customer contact;

optional qualification information.


Property
│
├── Catalogue
├── Offering
├── Location
├── Enquiry
├── Booking
├── Schedule
├── Customer
└── Notification

Customer:

SELECT PROPERTY
       ↓
REQUEST VIEWING
       ↓
COLLECT CONTACT DETAILS
       ↓
CHECK SCHEDULE
       ↓
SCHEDULE VIEWING
       ↓
CONFIRM
       ↓
NOTIFY

No:

Payment
Inventory
Room allocation

required for the basic scenario.

The same semantic machinery survives.


---

6. What the test tells us

We now have a much more interesting result.

There are actually three different kinds of composition.

A. Capability composition

Booking + Payment + Resource

B. Data/requirement composition

Booking
+
Vehicle Registration

or:

Delivery
+
Address

C. Policy composition

Payment required
Payment not required
Customer selects category
Customer selects specific offering
Automatic allocation
Merchant allocation

This means we don't need to create hundreds of capabilities.

The system can remain relatively small.


---

7. The important discovery: "customer information" should be a requirement system

This resolves one of the complexities you raised earlier.

Instead of:

Motel Customer
Mechanic Customer
Grocery Customer
Realtor Customer

we have:

CUSTOMER
   │
   └── Contextual Requirements
          │
          ├── Name
          ├── Phone
          ├── Email
          ├── Address
          ├── Vehicle Registration
          ├── Delivery Address
          └── Other validated requirement

But the system does not ask for everything.

The active capability determines requirements.

Delivery enabled?
     │
     ├── YES → Address becomes relevant
     └── NO  → Address not required

Vehicle service?
     │
     ├── YES → Vehicle information relevant
     └── NO  → Not required

This is exactly the kind of semantic composition Main Street needs.


---

8. We need one more abstraction: Requirement

I would therefore promote Requirement to a first-class semantic concept.

Not necessarily a standalone bounded context yet.

Conceptually:

CAPABILITY
     │
     └── Requirements
            │
            ├── Customer information
            ├── Merchant information
            ├── Offering information
            ├── Resource information
            └── Transaction requirements

For example:

DELIVERY
requires:
    delivery_address

VEHICLE_SERVICE
requires:
    vehicle_registration

PAID_BOOKING
requires:
    payment_confirmation

This is much cleaner than hard-coded forms.


---

9. Another important distinction: requirement vs policy

These must not be confused.

Requirement

Something necessary to perform an operation.

Delivery → address required

Policy

The merchant's chosen rule about how the operation behaves.

Booking → payment required before confirmation

Therefore:

Requirement
"What information/action is necessary?"

Policy
"How does this merchant choose to operate?"

This distinction will be extremely useful later.


---

10. Our emerging configuration language

We can now express a business almost like a small declarative program:

BUSINESS
    USE Accommodation
    USE Booking
    USE Allocation
    USE Payment

    OFFER RoomCategory
        Standard
        Executive

    POLICY Booking
        customer_selects = category
        allocation = automatic
        payment_confirmation = required
        availability_release = staff

    REQUIRE Booking
        customer = contact_information

Mechanic:

BUSINESS
    USE Service
    USE Booking
    USE Schedule
    USE Payment

    REQUIRE Booking
        customer = contact_information
        vehicle = registration

    POLICY Booking
        payment_confirmation = optional

Grocery:

BUSINESS
    USE Retail
    USE Catalogue
    USE Ordering
    USE Fulfilment
    USE Delivery
    USE Collection
    USE Payment

    REQUIRE Delivery
        customer = delivery_address

This is starting to look much closer to the "programming language/Scratch" idea you proposed.

And I think this is the right direction.


---

11. But we should NOT let AI write arbitrary syntax

This is critical.

AI should produce a structured intermediate representation, not executable arbitrary instructions.

Conceptually:

Business Understanding
        ↓
AI Composer
        ↓
Semantic Configuration
        ↓
Schema Validator
        ↓
Domain Validator
        ↓
Approved Configuration
        ↓
Runtime

The AI can compose:

USE Booking
USE Payment
REQUIRE VehicleRegistration

but it cannot invent:

USE MagicMechanicEngine

unless MagicMechanicEngine exists in the validated registry.


---

12. Prototype decision

At this point I would provisionally accept the following prototype concepts:

Concept	Status

Primitive vocabulary	Proposed
Capability composition	Proposed → strong candidate
Framework/abstract capability	Proposed → strong candidate
Policy composition	Proposed → strong candidate
Requirement composition	Proposed → strong candidate
Business-specific templates	Rejected direction
AI-generated domain primitives	Rejected
AI as semantic composer	Proposed → strong candidate
AI as domain authority	Rejected
UI generated directly from business description	Rejected
Operational config → presentation config	Proposed → strong candidate


We still haven't earned Validated status.


---

13. What comes next

Now we need to build the thing that will make or break this architecture:

The Capability Registry

For each capability we define:

Capability
├── Purpose
├── Required primitives
├── Optional primitives
├── Inputs
├── Outputs
├── Requirements
├── Policies
├── Domain invariants
├── Events
├── State/lifecycle
├── Dependencies
└── Configuration schema

Then we test whether:

Motel
Mechanic
Grocery
Realtor

can each be generated by composing those capabilities.

Only after that should we touch UI configuration.

That gives us a clean prototype sequence:

Primitive Registry
       ↓
Capability Registry
       ↓
Framework Registry
       ↓
Policy Registry
       ↓
Requirement Registry
       ↓
Business Composition
       ↓
Operational Configuration
       ↓
Validation
       ↓
Presentation Contract
       ↓
UI Prototype

This is now the path I would follow without further architectural pivoting.