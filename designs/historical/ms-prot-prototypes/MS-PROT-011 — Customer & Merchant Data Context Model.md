MS-PROT-011 — Customer & Merchant Data Context Model

Status: Proposed
Purpose: Define how Main Street determines, collects, stores, reuses, exposes, and validates information required by different business operations without creating universal forms or unnecessary data collection.

This specification closes one of the most important gaps in the prototype.

The governing principle is:

> Data requirements belong to an operation and its semantic context, not to a business category or to the customer globally.




---

1. The problem we are solving

A customer may interact with several merchants:

Grocery
    → delivery address

Mechanic
    → vehicle registration

Mobile mechanic
    → vehicle registration + service location

Structural engineer
    → project information

Solicitor
    → matter information

Motel
    → guest/stay information

Realtor
    → viewing/property context

We must not create:

Customer Profile
    ├── every conceivable field
    ├── vehicle registration
    ├── property details
    ├── legal matter
    ├── engineering project
    └── delivery address

That would be exactly the complexity we are trying to eliminate.


---

2. Four different things must be separated

Main Street should distinguish:

DATA DEFINITION
    ↓
DATA REQUIREMENT
    ↓
DATA INSTANCE
    ↓
DATA ACCESS

Data Definition

What does a piece of information mean?

VehicleRegistration
DeliveryAddress
ProjectDescription
GuestName

Data Requirement

When is it necessary?

MobileVehicleService
    requires VehicleRegistration
    requires ServiceLocation

Data Instance

The actual value supplied by the customer.

VehicleRegistration = AB12 CDE

Data Access

Who may use it and for what purpose?

Mechanic → service fulfilment
Delivery provider → delivery fulfilment

These must never be conflated.


---

3. The Data Semantic Model

The core model should be:

DataConcept
    ↓
Requirement
    ↓
Context
    ↓
Value

For example:

VehicleRegistration
        ↓
required by
        ↓
VehicleServiceOperation
        ↓
provided by
        ↓
Customer


---

4. Data concepts are reusable

A VehicleRegistration concept should be defined once.

It can then be used by:

Mechanic
MOT service
Vehicle inspection
Vehicle collection
Mobile servicing
Parts compatibility

We don't create:

MechanicVehicleRegistration
MOTVehicleRegistration
MobileMechanicVehicleRegistration

unless their semantics genuinely differ.


---

5. Data concepts should have types

Examples:

STRING
NUMBER
BOOLEAN
DATE
TIME
DATETIME
ADDRESS
LOCATION
IDENTIFIER
ENUMERATION
DOCUMENT
MONEY
QUANTITY
REFERENCE

A vehicle registration is:

IDENTIFIER

A delivery address is:

ADDRESS

A service location could be:

LOCATION

This allows automatic validation.


---

6. Semantic type is more important than storage type

For example:

VehicleRegistration

may technically be stored as a string.

But Main Street should not treat it merely as:

STRING

It should understand:

VehicleRegistration
    semantic type = VEHICLE_IDENTIFIER
    storage type = STRING

This gives the system meaningful validation and reuse.


---

7. Data context

The same concept can have different contextual meanings.

For example:

Location

could mean:

DeliveryLocation
ServiceLocation
CollectionLocation
PropertyLocation
MerchantLocation

These should share the underlying location semantics while retaining contextual identity.


---

8. Contextual requirement

A requirement should therefore look conceptually like:

Requirement
{
    data_concept
    context
    required_when
    validation
    visibility
    retention
}

Example:

Requirement:
    VehicleRegistration

Context:
    MobileVehicleService

Required when:
    service_type = vehicle_service


---

9. Conditional requirements

This is essential.

A mechanic might have:

Workshop service

requiring:

VehicleRegistration

But:

Mobile service

requires:

VehicleRegistration
+
ServiceLocation

So requirements are evaluated from the operation configuration.

Conceptually:

IF
    mobile_service = true

THEN
    REQUIRE ServiceLocation

But merchants do not write this condition.

It comes from the approved semantic framework.


---

10. Requirement dependencies

Requirements can themselves activate other requirements.

For example:

Delivery
    ↓
DeliveryAddress

Then:

DeliveryAddress
    ↓
Country
Region
Postcode
Street

However, we should not necessarily expose all of these as separate concepts to the configuration engine.

The address framework can handle its internal structure.

This is another example of abstraction.


---

11. Composite data concepts

Some concepts are naturally composite.

ADDRESS
    ├── line
    ├── locality
    ├── postcode
    └── country

Similarly:

PERSON
    ├── name
    ├── contact
    └── identifiers

And:

VEHICLE
    ├── registration
    ├── make
    ├── model
    └── other attributes

The customer should see an appropriate form, while the semantic layer sees the structured object.


---

12. Data ownership

We should distinguish:

CUSTOMER_DATA
MERCHANT_DATA
OFFERING_DATA
RESOURCE_DATA
TRANSACTION_DATA
SYSTEM_DATA

Example:

Vehicle registration
→ customer-provided contextual data

Room number
→ merchant/resource data

Booking ID
→ system-generated transaction data

Delivery address
→ customer-provided operation data


---

13. Ownership does not necessarily mean exclusive control

A merchant may legitimately create operational records concerning a customer.

Therefore:

ownership
≠
access

We need separate concepts for:

source;

responsibility;

access;

authority;

retention.



---

14. Customer profile versus operational context

This is one of the most important decisions.

Main Street should maintain a minimal reusable customer profile, but operational data should remain contextual.

For example:

Customer Profile
    name
    contact details

Then:

Mechanic Operation
    vehicle registration

Grocery Delivery Operation
    delivery address

Realtor Operation
    viewing preferences

The latter should not automatically become permanent customer-profile attributes.


---

15. Reuse must be explicit

Suppose a customer gives a delivery address.

Main Street may be able to reuse it for a later order.

But reuse should happen because:

same semantic concept
+
appropriate customer authority
+
appropriate merchant/context

not simply because:

> "The system already has an address."




---

16. Cross-merchant data reuse

This needs particularly strong boundaries.

A customer's address supplied to:

Merchant A

should not automatically become available to:

Merchant B

merely because Main Street stores it.

Instead:

Customer data
     ↓
authorised contextual reuse

must be explicitly governed.

This becomes a privacy/security specification later.


---

17. Data provenance

Every contextual value should have provenance.

Conceptually:

Value
{
    concept
    value
    source
    context
    timestamp
    validation_status
}

For example:

VehicleRegistration
AB12 CDE

source:
customer

context:
vehicle service

validated:
true

validated_at:
...

This matters because data can become stale.


---

18. Validation state

A value should not simply be:

exists / doesn't exist

We need something closer to:

UNKNOWN
PROVIDED
VALIDATED
REJECTED
STALE
SUPERSEDED

For example:

VehicleRegistration
    PROVIDED

does not necessarily mean:

VehicleRegistration
    VALIDATED


---

19. Revalidation

Some information needs revalidation.

For example:

Delivery Address

may change.

A vehicle registration may remain stable for a while but could be associated with a different vehicle.

Therefore Main Street should support:

VALID
STALE
REVALIDATE

without forcing every field to be re-entered unnecessarily.


---

20. Data requirement resolution

At runtime:

Operation
    ↓
Semantic configuration
    ↓
Requirement resolver
    ↓
Customer context
    ↓
existing valid data?
       ↙        ↘
     YES         NO
      ↓           ↓
    reuse       request

This is one of the major mechanisms that makes Main Street feel intelligent.


---

21. Example — returning mechanic customer

Customer previously supplied:

VehicleRegistration = AB12 CDE

They request:

Brake inspection

Main Street determines:

VehicleRegistration required

Existing valid value:

YES

Therefore:

> Main Street does not ask again unnecessarily.




---

22. Example — different vehicle

Customer says:

> Book brake inspection for my other car.



The operation now has:

VehicleContext = Vehicle B

Main Street cannot blindly reuse:

Vehicle A registration

The requirement resolver asks for the appropriate identifier.

Again, semantics determine behaviour.


---

23. Example — grocery delivery

Customer previously supplied:

Home Address

Now chooses:

Deliver to work

The operation can request:

DeliveryAddress

The customer may select an existing address or provide another.

The operation remains contextual.


---

24. Example — mobile mechanic

Customer has:

VehicleRegistration

but no valid:

ServiceLocation

The system requests only:

ServiceLocation

It doesn't restart the booking.


---

25. Partial completion

This connects directly with our previous interaction model.

Suppose:

Operation
    vehicle registration ✓
    service type ✓
    time ✓
    location ✗

The operation remains recoverable.

Customer can return later.

The system says, conceptually:

> "You were arranging a mobile brake service. We still need the service location."



This is resumable context, not a temporary commitment.


---

26. Data and allocation remain separate

This distinction prevents a subtle architectural error.

Providing:

VehicleRegistration

does not allocate:

Mechanic

Providing:

DeliveryAddress

does not allocate:

Delivery capacity

Data satisfaction merely makes an operation eligible for further processing.


---

27. Data and payment remain separate

Similarly:

Payment successful

doesn't automatically mean:

all requirements satisfied

And:

all requirements satisfied

doesn't mean:

payment successful

They are independent conditions within the operation framework.


---

28. Data visibility

Every requirement should have a visibility policy.

For example:

Customer
Merchant
Staff
Delivery provider
System

A vehicle registration might be visible to:

Merchant
Authorised staff

but not necessarily to:

Other customers

Again, the semantic layer defines the requirement; the security layer governs access.


---

29. Don't put security rules inside every primitive

We should avoid:

VehicleRegistration
    merchant_can_read = true
    staff_can_read = true
    ...

for every conceivable data concept.

Instead:

Data concept
    ↓
context
    ↓
access policy

This keeps the semantic language manageable.


---

30. Merchant data

The same model applies to merchant information.

For example:

Merchant
    opening hours
    service area
    payment policies
    offerings
    resources
    staff

These are merchant-context data.

They become inputs into customer operations.


---

31. Resource data

A motel:

Room 101
    category = standard
    capacity = 2
    status = available

A mechanic:

Workshop Bay 2
    type = service_bay
    capacity = 1

A consultant:

Engineer A
    consultation capacity
    schedule

These are not customer data.

But they participate in the same operational model.


---

32. Data should therefore be modelled by semantic ownership/context

A useful conceptual structure is:

DATA
 │
 ├── Customer Context
 ├── Merchant Context
 ├── Offering Context
 ├── Resource Context
 ├── Operation Context
 └── Commitment Context

This is much better than one universal customer/merchant database model driving everything.


---

33. Data lifecycle

Every contextual value should have a lifecycle:

NOT_REQUIRED
    ↓
REQUIRED
    ↓
REQUESTED
    ↓
PROVIDED
    ↓
VALIDATED
    ↓
USED
    ↓
STALE / SUPERSEDED
    ↓
RETAINED / DELETED

Not every value follows every transition.

The framework controls the applicable lifecycle.


---

34. Data minimisation becomes structural

Because requirements are operation-specific:

Customer
    ↓
only required data
    ↓
for this operation

rather than:

Customer
    ↓
complete universal profile
    ↓
every merchant gets everything

This is a major architectural advantage.


---

35. AI's role

AI does not invent data requirements.

It infers which approved requirements apply.

For example:

Business understanding:
"Mobile vehicle servicing."

AI may select:

SERVICE
MOBILE_SERVICE
VEHICLE_REQUIREMENT
SERVICE_LOCATION_REQUIREMENT

The framework then determines the exact operational behaviour.


---

36. UI generation remains separate

This connects directly to the architectural decision we already made.

The operational semantic configuration says:

Required:
VehicleRegistration
ServiceLocation

It does not say:

put vehicle registration textbox at top
use a dropdown
show location modal

That belongs to the UI semantic configuration layer.

Therefore:

Operational Semantics
        ↓
UI Semantics
        ↓
Merchant/Customer Interface

The UI can vary dramatically between two businesses while consuming the same operational capabilities.


---

37. This is a hard architectural boundary

We should formally establish:

> Operational configuration must never contain presentation instructions.



And:

> Presentation configuration must consume operational semantics rather than redefine them.



This is one of the most important boundaries in the entire Main Street architecture.


---

38. Example: two motels

Both may have:

ACCOMMODATION_STAY
ROOM
BOOKING
PAYMENT

Operationally similar.

But Motel A's UI may be:

Rooms → dates → guests → payment

while Motel B's may be:

Dates → room comparison → packages → guests → payment

The operational engine remains the same.

The UI semantic layer determines presentation.


---

39. MS-PROT-011 decisions

Decision	Status

Data concepts are reusable semantic objects	Accepted
Requirements are contextual	Accepted
Requirements belong to operations/offering contexts	Accepted
Data definition ≠ requirement ≠ value ≠ access	Accepted
Semantic data types required	Accepted
Composite data concepts supported	Accepted
Customer profile remains minimal	Accepted
Operational data remains contextual	Accepted
Cross-merchant reuse is not automatic	Accepted
Data provenance required	Accepted
Validation state required	Accepted
Revalidation supported	Accepted
Partial data completion supported	Accepted
Data does not imply allocation	Accepted
Data does not imply payment	Accepted
Visibility/access separated from semantic definition	Accepted
Operational configuration does not contain UI instructions	Accepted
UI configuration consumes operational semantics	Accepted
AI selects approved requirements rather than inventing them	Accepted



---

The next specification

We now have enough to tackle one of the most fundamental pieces of the runtime:

MS-PROT-012 — Capability, Policy & Rule Composition

This will formalise the actual "Scratch/pixels/alphabets" mechanism you have been describing.

We need to answer precisely:

Primitive
   ↓
Abstract primitive
   ↓
Capability
   ↓
Framework
   ↓
Policy
   ↓
Rule
   ↓
Merchant configuration

and, critically:

> How can these be composed without creating an unmanageable rule engine or allowing AI to produce contradictory configurations?



That specification should also establish precedence, dependencies, conflicts, invariants, versioning and deterministic evaluation.

Once MS-PROT-012 is complete, we will have most of the semantic machinery required for the prototype.