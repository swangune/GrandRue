MS-PROT-010 — Offering & Operation Model

Status: Proposed
Purpose: Define the smallest common structure that can represent what a customer can obtain, request, schedule, purchase, reserve, or otherwise initiate with a merchant.

This is a critical convergence point. We should not create separate engines for products, services, bookings, consultations, rooms, property viewings, etc.

Instead, Main Street needs one common operational model with specialised compositions.


---

1. The fundamental distinction

We need three concepts:

OFFERING
    ↓
what the merchant makes available

OPERATION
    ↓
what the customer/merchant does with it

COMMITMENT
    ↓
what the business has actually accepted

For example:

Offering:
    Oil Change

Operation:
    Book Oil Change

Commitment:
    Appointment confirmed Tuesday 14:00

Or:

Offering:
    Standard Room

Operation:
    Book Standard Room

Commitment:
    Stay confirmed 20–22 August

Or:

Offering:
    Milk

Operation:
    Purchase Milk

Commitment:
    Order accepted

This gives us a common backbone.


---

2. An Offering is not necessarily a product

This is important.

An Offering may represent:

a physical product;

a service;

accommodation;

a consultation;

a property;

a rental;

a delivery service;

an appointment;

a package;

a combination of other offerings.


Therefore:

Offering

is the generic concept.

Not:

Product


---

3. Minimum Offering model

Conceptually:

Offering
{
    identity
    name
    description
    offering_type
    merchant
    availability_model
    requirements
    capacity_requirements
    pricing
    policies
    lifecycle
}

Not every field is necessarily populated.

The semantic configuration determines which structures apply.


---

4. Offering type

We should initially support a small controlled vocabulary.

For example:

PRODUCT
SERVICE
ACCOMMODATION
PROPERTY
RENTAL
PACKAGE

But these should primarily describe the nature of the offering.

They should not determine the complete runtime behaviour.


---

5. Behaviour comes from composition

Consider:

SERVICE

That tells us very little operationally.

The actual composition might be:

SERVICE
+
BOOKABLE
+
SCHEDULED
+
RESOURCE_CONSTRAINED
+
PAYMENT_REQUIRED

Another service:

SERVICE
+
REQUESTABLE
+
DISPATCHED
+
LOCATION_REQUIRED

Same broad offering type.

Completely different operational behaviour.


---

6. The Operation

An operation represents an attempt to perform a business action involving an offering.

Examples:

PURCHASE
BOOK
REQUEST
RESERVE
CANCEL
MODIFY
VIEW
ENQUIRE

But again, we should avoid creating dozens of unrelated operation types.

Instead, these should be semantic commands composed from primitives.


---

7. The operation lifecycle

Every consequential operation follows:

REQUESTED
    ↓
VALIDATING
    ↓
ELIGIBLE
    ↓
ALLOCATING / PROCESSING
    ↓
COMMITTED
    ↓
FULFILLED

With failure branches:

REJECTED
CANCELLED
EXPIRED
FAILED

The exact states applicable to an operation are determined by its framework.


---

8. Operation ≠ commitment

This distinction is essential.

Customer says:

> I want a car wash at 2pm.



That creates:

Operation

It does not yet create:

Appointment commitment

Only after the required validations and authoritative allocation succeed do we create the commitment.


---

9. Generic operation structure

Conceptually:

Operation
{
    operation_id
    actor
    merchant
    offering
    intent
    inputs
    context
    status
    interaction_id
    command_id
    commitment_id?
}

The operation references the semantic configuration rather than containing arbitrary executable logic.


---

10. Customer information belongs to the operation

This directly solves the problem you raised earlier.

Suppose:

Grocery

offers:

Home Delivery

Then:

Operation
    = Purchase
    + Delivery

The operation requires:

Customer Address

But:

Mechanic
    = Workshop Service

doesn't need an address.

Instead:

Vehicle Registration

is required.

And:

Mobile Mechanic

requires:

Vehicle Registration
+
Service Location

Therefore customer information is operation-specific.


---

11. Requirement resolution

At runtime:

Operation
   ↓
Offering
   ↓
Applicable requirements
   ↓
Merchant policies
   ↓
Customer context

Main Street determines what information is missing.

Example:

Book mobile brake repair

System determines:

✓ customer identity
✓ vehicle registration
✗ service location

Customer is asked only for:

Service location

Not a giant registration form.


---

12. This should be dynamic

The required customer data should not be a static merchant-wide form.

Instead:

Operation
    ↓
Requirement resolution
    ↓
Required data

Different operations from the same merchant can therefore request different information.


---

13. Example — grocery

Customer chooses:

Milk × 2
Bread × 1

For collection:

Requirements:
    customer identity
    collection details

For delivery:

Requirements:
    customer identity
    delivery address
    delivery instructions?

The second requirement set is activated because:

DELIVERY

is part of the operation.


---

14. Example — mechanic

Customer selects:

Oil Change

Operation:

SERVICE_BOOKING

Requirements:

Vehicle registration

If mobile service is selected:

Vehicle registration
+
service location

If the merchant offers vehicle collection:

Vehicle registration
+
collection location

Again, no custom forms are required.


---

15. Operation composition

We can express an operation as:

Operation
    =
Intent
+
Offering
+
Requirements
+
Constraints
+
Policies
+
Capacity
+
Payment
+
Commitment

Not all components are mandatory.

For example:

Simple purchase

PURCHASE
+
PRODUCT
+
INVENTORY
+
PAYMENT

Appointment

BOOK
+
SERVICE
+
SCHEDULE
+
CAPACITY
+
PAYMENT

Motel

BOOK
+
ACCOMMODATION
+
INTERVAL
+
CAPACITY
+
ALLOCATION
+
PAYMENT


---

16. A powerful simplification

We can classify operations according to what they do to business state.

There are essentially three broad categories:

Inquiry

READ

Example:

> Is this property available?



Request

INTENT

Example:

> I want to book it.



Commitment

WRITE

Example:

> Booking accepted.



This gives us a very clean separation.


---

17. Inquiry operations

Examples:

VIEW_OFFERING
CHECK_AVAILABILITY
GET_PRICE
GET_DELIVERY_OPTIONS
GET_AVAILABLE_TIMES

These should normally be read-only.

They do not create commitments.


---

18. Request operations

Examples:

REQUEST_BOOKING
REQUEST_PURCHASE
REQUEST_DELIVERY
REQUEST_CONSULTATION
REQUEST_VIEWING

These initiate consequential processing.

They may eventually produce a commitment.


---

19. Commitment

The commitment is the authoritative business fact.

Examples:

Appointment
Order
Booking
Stay
Rental
Delivery
Property Viewing

Rather than creating completely unrelated commitment models, they should share a common conceptual structure.


---

20. Generic Commitment

Commitment
{
    commitment_id
    merchant
    customer
    offering
    obligations
    allocated_capacity
    schedule
    status
    created_at
}

Specialised frameworks add their domain-specific fields.


---

21. Commitment is the boundary of business promise

This is an important invariant:

> Main Street must not represent an uncommitted customer intention as a merchant obligation.



Therefore:

Customer selected 14:00

does not mean:

Merchant owes 14:00

Only:

AppointmentCommitted

creates that obligation.


---

22. Payment

Payment remains a separate concern.

An operation may have:

PAYMENT_REQUIRED

or:

PAYMENT_NOT_REQUIRED

But payment itself is another operation/state machine.

Therefore:

Booking
    ↕
Payment

rather than:

Booking = Payment

This is important because some businesses:

require upfront payment;

collect payment later;

require deposits;

accept payment externally;

don't require payment at all.



---

23. Payment confirmation and commitment

We should therefore support policies such as:

PAYMENT_BEFORE_COMMITMENT
COMMITMENT_BEFORE_PAYMENT
PAYMENT_OPTIONAL
PAYMENT_EXTERNAL

But these should be predefined semantic policies.

AI chooses the appropriate one based on merchant configuration.


---

24. The race-condition boundary remains intact

For payment-before-commitment:

Request
 ↓
Validate
 ↓
Availability
 ↓
Payment process
 ↓
AUTHORITATIVE AVAILABILITY RECHECK
 ↓
Allocation
 ↓
Commitment

For commitment-before-payment:

Request
 ↓
Validate
 ↓
AUTHORITATIVE ALLOCATION
 ↓
Commitment
 ↓
Payment

The exact transactional relationship between external payment infrastructure and Main Street will need a later integration specification.

But the domain model remains clean.


---

25. No arbitrary temporary allocation

Our previous decision remains.

If merchant does not require a hold:

Interaction
    ↓
no allocation

If merchant requires a hold:

Interaction
    ↓
configured hold mechanism
    ↓
temporary allocation

There is no universal Main Street "10-minute rule."


---

26. Operation interruption

Suppose the customer:

Selects car wash
↓
selects Friday
↓
selects 14:00
↓
leaves

We preserve:

Interaction

not:

Commitment

When they return:

Interaction
 ↓
reconstruct request
 ↓
revalidate
 ↓
continue

If 14:00 disappeared:

Interaction remains
+
new availability result

The system can offer:

14:30
15:00

without forcing the customer to rebuild the entire request.


---

27. Modification

Modification should not be treated as arbitrary mutation.

Instead:

Existing Commitment
       ↓
Modification Request
       ↓
Validate
       ↓
Recalculate requirements/capacity
       ↓
Apply atomic transition

For example:

Booking
14:00

changed to:

15:00

requires another availability decision.


---

28. Cancellation

Similarly:

Cancel

is a command.

It produces:

CommitmentCancelled

which may trigger:

capacity release
payment action
notification
merchant workflow

The cancellation rules come from the merchant's policy configuration.


---

29. Operation framework examples

We can now define a relatively small catalogue.

Commerce

COMMERCE_ORDER

Service appointment

SERVICE_BOOKING

Consultation

CONSULTATION

Accommodation

ACCOMMODATION_STAY

Property viewing

PROPERTY_VIEWING

Rental

RESOURCE_RENTAL

Delivery

DELIVERY

These are frameworks, not hard-coded industries.


---

30. Composite operations

This is where the system becomes powerful.

A grocery order might become:

COMMERCE_ORDER
+
DELIVERY
+
PAYMENT

A mobile mechanic:

SERVICE_BOOKING
+
MOBILE_SERVICE
+
PAYMENT

A motel:

ACCOMMODATION_STAY
+
PAYMENT

A realtor:

PROPERTY_VIEWING
+
SCHEDULING

The framework composition determines behaviour.


---

31. Avoid framework explosion

We should not create:

MOBILE_MECHANIC_BOOKING
GROCERY_HOME_DELIVERY
SOLICITOR_PAID_CONSULTATION
STRUCTURAL_ENGINEER_CONSULTATION
MOTEL_ROOM_BOOKING
...

Those are merchant compositions.

The framework catalogue should remain small.


---

32. The operation graph

An operation can be represented internally as:

Customer
   ↓
Operation
   │
   ├── Offering
   ├── Requirements
   ├── Policies
   ├── Schedule
   ├── Capacity
   ├── Payment
   └── Location
          ↓
      Commitment

This is probably the most useful conceptual drawing so far.


---

33. Main Street's fundamental unit

We can now answer:

> What is the smallest useful representation of something a customer asks a merchant to do?



It is:

INTENT
+
OFFERING
+
CONTEXT

Everything else is resolved from configuration.

For example:

"Book an oil change tomorrow at 14:00."

becomes:

Intent:
    BOOK

Offering:
    Oil Change

Context:
    date = tomorrow
    time = 14:00

Main Street then derives:

requirements
capacity
schedule
payment
allocation
commitment


---

34. This is the "alphabet" principle

Your original analogy now has a concrete form.

PRIMITIVES
    ↓
SEMANTIC PATTERNS
    ↓
FRAMEWORKS
    ↓
OPERATIONS
    ↓
MERCHANT CONFIGURATION

Like:

letters
 ↓
words
 ↓
sentences

Main Street doesn't invent a new alphabet for every merchant.


---

35. Validation rule

An operation is valid only if:

Offering exists
AND
Intent is supported
AND
Requirements satisfied
AND
Policies permit it
AND
Required capacity exists
AND
Schedule permits it
AND
Allocation succeeds
AND
Payment conditions satisfied

The exact sequence depends on the operation framework, but these are semantic constraints—not AI decisions at runtime.


---

36. MS-PROT-010 decisions

Decision	Status

Offering is a first-class concept	Accepted
Offering ≠ product	Accepted
Operation represents an attempted business action	Accepted
Operation ≠ commitment	Accepted
Commitment represents merchant obligation	Accepted
Customer requirements are operation-specific	Accepted
Operations are compositional	Accepted
Inquiry/request/commitment are distinct	Accepted
Payment remains a separate concern	Accepted
Temporary allocation remains optional	Accepted
Interrupted interactions remain recoverable	Accepted
Resumed interactions must revalidate reality	Accepted
Modification requires fresh validation	Accepted
Cancellation is a domain command	Accepted
Small operational framework catalogue preferred	Accepted
Merchant-specific frameworks should be avoided	Accepted
Intent + Offering + Context forms the core customer request	Accepted



---

The prototype is now ready for the next major layer

We have established:

MS-PROT-004  State & Transition
MS-PROT-005  Requirements & Policies
MS-PROT-006  Capacity & Allocation
MS-PROT-007  Interaction & Events
MS-PROT-008  Configuration
MS-PROT-009  Semantic Business Model
MS-PROT-010  Offering & Operations

The next specification should not be another abstract conceptual layer.

MS-PROT-011 — Customer & Merchant Data Context Model

This is where we finally formalise the issue you raised earlier:

> A grocery delivery needs an address; a mechanic needs a vehicle registration; a mobile mechanic needs both; a consultation may need a case/project description; a realtor needs property/viewing context.



We need to determine:

What data exists?
        ↓
Who owns it?
        ↓
When is it required?
        ↓
Who can see it?
        ↓
How long is it retained?
        ↓
Can it be reused?
        ↓
When must it be revalidated?

That model is necessary before we can safely design the detailed customer and merchant operational flows.