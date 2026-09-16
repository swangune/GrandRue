MS-PROT-016 — Concrete Prototype Domain Model

Status: Proposed
Purpose: Prove that the semantic model can represent materially different Main Street businesses without creating bespoke application logic for each niche.

This is our first serious prototype validation gate.

The test is not whether we can describe businesses. The test is whether their operations can be expressed using the same underlying semantic machinery.


---

1. The fundamental model

Every merchant is represented through the same structure:

MERCHANT
   │
   ├── BUSINESS PROFILE
   │
   ├── BUSINESS DOMAIN
   │
   ├── FRAMEWORKS
   │
   ├── CAPABILITIES
   │
   ├── RESOURCES
   │
   ├── OFFERINGS
   │
   ├── REQUIREMENTS
   │
   ├── POLICIES
   │
   └── OPERATING CONFIGURATION

The important point is:

> The merchant's niche determines which semantic constructs are appropriate; it does not determine a separate software architecture.




---

2. The prototype businesses

We should use a deliberately difficult sample.

Merchant	Primary model

Grocery store	Commerce
Spare-parts dealer	Commerce
Car wash	Service booking
Structural engineer	Professional consultation/service
Solicitor	Professional consultation/service
Real-estate realtor	Property listing + enquiry + viewing
Motel	Accommodation
Mobile mechanic	Mobile service
Hairdresser	Appointment service


If the model handles these, it has survived a meaningful test.


---

3. First distinction — Business Domain

We should not immediately classify businesses by industry.

Instead, we identify their operational domain.

For example:

GROCERY STORE
    → COMMERCE

SPARE-PARTS DEALER
    → COMMERCE

CAR WASH
    → SERVICE

STRUCTURAL ENGINEER
    → PROFESSIONAL_SERVICE

SOLICITOR
    → PROFESSIONAL_SERVICE

REALTOR
    → PROPERTY_SERVICE

MOTEL
    → ACCOMMODATION

This gives us broad semantic families.


---

4. But domain alone is insufficient

A grocery store and spare-parts dealer both fall under commerce.

Yet:

GROCERY

may require:

quantity
weight
perishable goods
substitution

while:

SPARE_PARTS

may require:

vehicle compatibility
part number
manufacturer
technical specification

Therefore:

> Domain provides the semantic neighbourhood, not the complete operational model.




---

5. Framework selection

The next layer is the operational framework.

Grocery

COMMERCE_ORDER
INVENTORY
FULFILMENT
PAYMENT

Spare parts

COMMERCE_ORDER
INVENTORY
PRODUCT_COMPATIBILITY
PAYMENT
FULFILMENT

Notice something important:

The spare-parts dealer did not require a new commerce architecture.

It required an additional semantic capability.


---

6. Grocery example

A grocery merchant might have:

Framework:
    COMMERCE_ORDER

Capabilities:
    PRODUCT_CATALOG
    INVENTORY
    ORDERING
    PAYMENT
    COLLECTION
    DELIVERY

Possible policies:

DELIVERY_ENABLED
COLLECTION_ENABLED
SUBSTITUTION_ALLOWED
CUSTOMER_CANCELLATION_ALLOWED
PAYMENT_REQUIRED


---

7. Spare-parts dealer

Framework:
    COMMERCE_ORDER

Capabilities:
    PRODUCT_CATALOG
    INVENTORY
    ORDERING
    PAYMENT
    COLLECTION
    PRODUCT_COMPATIBILITY

Additional data concepts:

PART_NUMBER
MANUFACTURER
VEHICLE_MAKE
VEHICLE_MODEL
VEHICLE_YEAR

The underlying order lifecycle remains the same.


---

8. This is precisely the abstraction we wanted

COMMERCE
                    │
             ┌──────┴──────┐
             ↓             ↓
         GROCERY       SPARE PARTS
             │             │
       special data    compatibility
       + policies      semantics

The shared semantics are reused.

The differences are represented explicitly.


---

9. Car wash

Now consider:

CAR_WASH

Primary framework:

SERVICE_BOOKING

Capabilities might include:

SERVICE_CATALOG
SCHEDULING
RESOURCE_ALLOCATION
CUSTOMER_BOOKING
PAYMENT

Resources:

WASH_BAY
STAFF_MEMBER
SERVICE_SLOT


---

10. Car-wash booking lifecycle

A simplified state model:

REQUESTED
    │
    ├── payment-required path
    │
    ▼
CONFIRMED
    │
    ▼
IN_PROGRESS
    │
    ▼
COMPLETED

Alternative:

REQUESTED
    │
    ▼
CONFIRMED

when payment is not required.

The framework supports both.

The merchant selects the applicable policy.


---

11. Race condition handling

Suppose:

Bay 2
10:00
capacity = 1

Customer A requests it.

Customer B simultaneously requests it.

Both operations enter the resource-allocation subsystem.

The invariant is:

successful allocations ≤ available capacity

The runtime, not the UI and not the AI, enforces this.

One transaction wins.

The other receives the next valid availability.

This remains true regardless of whether payment is required.

That preserves our previous decision to remove the artificial 10-minute Main Street allocation window.


---

12. Structural engineer

Now we deliberately move away from commerce.

STRUCTURAL_ENGINEER

Framework:

PROFESSIONAL_CONSULTATION

Capabilities:

SERVICE_CATALOG
CONSULTATION
SCHEDULING
DOCUMENT_COLLECTION
PAYMENT
CUSTOMER_COMMUNICATION

Possible requirements:

PROJECT_DESCRIPTION
SITE_LOCATION
PROPERTY_TYPE
EXISTING_DOCUMENTS


---

13. Structural engineer policies

Potential merchant configuration:

CONSULTATION_PAYMENT = BEFORE_CONFIRMATION

CUSTOMER_BOOKING = REQUIRED

DOCUMENT_UPLOAD = OPTIONAL

SITE_VISIT = ENABLED

CUSTOMER_CANCELLATION = ALLOWED

Again, the merchant does not configure the semantic machinery.

They simply answer meaningful questions.


---

14. Solicitor

The solicitor can use the same:

PROFESSIONAL_CONSULTATION

framework.

But with different requirements:

CLIENT_IDENTITY
MATTER_TYPE
MATTER_DESCRIPTION
RELEVANT_DOCUMENTS

And potentially:

CONFLICT_CHECK

as a specialised capability.

This is a good example of specialisation without duplication.


---

15. Structural engineering versus legal consultation

PROFESSIONAL_CONSULTATION
                       │
              ┌────────┴────────┐
              ↓                 ↓
       STRUCTURAL             LEGAL
       ENGINEERING           CONSULTATION
              │                 │
         site/project        client/matter
         information         information
              │                 │
         site visit          conflict check

Shared operational infrastructure:

scheduling
payment
documents
customer communication

Domain-specific semantics:

engineering information
legal information

This is exactly the level of abstraction we want.


---

16. Real-estate realtor

This is particularly useful because it tests whether Main Street can support a business that is neither conventional commerce nor conventional booking.

Primary framework:

PROPERTY_SERVICE

Capabilities:

PROPERTY_LISTING
PROPERTY_SEARCH
ENQUIRY
VIEWING_SCHEDULING
LEAD_MANAGEMENT
DOCUMENT_COLLECTION
CUSTOMER_COMMUNICATION

Resources:

PROPERTY
VIEWING_SLOT
AGENT


---

17. Realtor operation

A customer might:

SEARCH PROPERTY
      ↓
VIEW PROPERTY
      ↓
REQUEST VIEWING
      ↓
SELECT AVAILABLE SLOT
      ↓
CONFIRM VIEWING

This is different from purchasing a product.

There may be no:

ORDER

at all.

Yet the same:

scheduling
resource allocation
customer identity
communication

infrastructure can be reused.


---

18. Property viewing concurrency

This is another useful test.

Suppose:

Property X
Saturday 14:00
Viewing capacity = 1

Two customers request the viewing.

The resource-allocation primitive ensures only one confirmed allocation.

The second customer is offered another valid slot.

Again:

UI ≠ concurrency control

The backend owns the invariant.


---

19. Motel

Now we test accommodation.

Framework:

ACCOMMODATION_STAY

Capabilities:

ROOM_INVENTORY
AVAILABILITY
BOOKING
PAYMENT
CHECK_IN
CHECK_OUT

Resources:

ROOM


---

20. Motel state model

A room could have operational states such as:

AVAILABLE
HELD
OCCUPIED
OUT_OF_SERVICE

A reservation has a different lifecycle:

REQUESTED
CONFIRMED
CHECKED_IN
CHECKED_OUT
CANCELLED

This demonstrates why resource state and commitment state must remain separate concepts.


---

21. Why that distinction matters

A room can be:

OCCUPIED

while a reservation is:

CHECKED_IN

They are related but not the same entity.

Similarly:

WASH_BAY = AVAILABLE

and:

BOOKING = CONFIRMED

are different state domains.

This prevents us from creating one universal status field that tries to represent everything.


---

22. Mobile mechanic

Framework:

MOBILE_SERVICE

Capabilities:

SERVICE_BOOKING
SCHEDULING
MOBILE_LOCATION
RESOURCE_ALLOCATION
PAYMENT

Requirements:

VEHICLE_INFORMATION
SERVICE_LOCATION
SERVICE_TYPE

This demonstrates that a service can involve both:

customer-selected time
+
customer-provided location

without needing a completely different platform.


---

23. Hairdresser

Framework:

SERVICE_BOOKING

Capabilities:

SERVICE_CATALOG
SCHEDULING
STAFF_ALLOCATION
PAYMENT

Resource:

STAFF_MEMBER

Possible policy:

CUSTOMER_SELECTS_STAFF = OPTIONAL

The same booking framework can therefore allocate:

wash bay
staff member
consultation slot
room
viewing slot

because these are all instances of the broader semantic concept:

ALLOCATABLE_RESOURCE


---

24. The emerging common infrastructure

Across all these businesses we repeatedly see:

IDENTITY
OFFERING
REQUEST
SCHEDULING
RESOURCE
ALLOCATION
COMMITMENT
PAYMENT
DOCUMENT
LOCATION
COMMUNICATION
FULFILMENT

These should become candidates for our core semantic primitives and abstractions.


---

25. But we must not make them all capabilities

This is where semantic discipline matters.

For example:

ALLOCATE

is likely a primitive/abstraction.

RESOURCE_ALLOCATION

is a capability.

SERVICE_BOOKING

is a framework.

PAYMENT_REQUIRED

is a policy.

They operate at different semantic levels.


---

26. Proposed hierarchy

PRIMITIVE
    ↓
ABSTRACTION
    ↓
CAPABILITY
    ↓
FRAMEWORK
    ↓
MERCHANT CONFIGURATION

Cross-cutting constructs:

POLICY
RULE
REQUIREMENT
RESOURCE
STATE
EVENT
INVARIANT

These interact with the hierarchy rather than simply sitting above or below it.


---

27. The merchant configuration example

Suppose the structural engineer says:

> "Customers must pay before I confirm a consultation."



Main Street might derive:

Framework:
    PROFESSIONAL_CONSULTATION

Capability:
    CONSULTATION

Policy:
    PAYMENT_POLICY = BEFORE_CONFIRMATION

The framework already knows what that policy means operationally.

The merchant does not need to configure:

payment state
transaction state
confirmation transition
failure transition
rollback

Those are framework semantics.


---

28. This validates the "merchant simplicity" principle

Merchant sees:

> Require payment before confirming appointments?



Yes

Internally:

PAYMENT_POLICY
      ↓
framework rule
      ↓
confirmation precondition
      ↓
payment event
      ↓
state transition

The complexity remains inside Main Street.


---

29. Customer experience

The customer-facing website is generated from a different semantic layer, as we previously agreed.

Operational semantics tell the UI system:

customer must provide:
    SERVICE_LOCATION

customer may:
    select appointment

confirmation requires:
    payment

The UI system decides how that is presented.

Therefore two structural engineers can have different websites while sharing identical operational semantics.


---

30. Merchant dashboard

The same separation applies.

Operational configuration says:

resource
booking
payment
customer
service

The UI configuration layer determines:

which dashboard controls appear
where they appear
how they are grouped
what terminology is used

So:

Operational Semantic Layer
            ↓
UI Semantic Layer
            ↓
Customer UI / Merchant UI


---

31. No unnecessary feature exposure

This validates another important decision.

A motel should not receive a merchant dashboard containing:

vehicle compatibility
product SKU management
engineering project requirements
legal conflict checking

because those capabilities are not part of its configuration graph.

Likewise, a grocery merchant should not receive:

room inventory
check-in/check-out

The dashboard is therefore compiled from applicable capabilities.


---

32. The configuration surface becomes dynamic

Conceptually:

Merchant semantic configuration
             ↓
Applicable capabilities
             ↓
Merchant configuration controls

So the merchant dashboard isn't a giant application with features hidden behind hundreds of flags.

It is generated from the merchant's semantic capability set.


---

33. This is a major architectural consequence

We can now state:

> Main Street does not build one universal merchant dashboard. It builds a dashboard runtime capable of rendering the configuration surface implied by a merchant's semantic model.



Likewise:

> Main Street does not build one universal customer website. It builds a customer-experience runtime capable of rendering the customer-facing behaviour implied by the merchant's semantic model.




---

34. Prototype domain matrix

Business	Framework	Distinctive semantics

Grocery	Commerce	inventory, quantity, substitution
Spare parts	Commerce	compatibility, technical identifiers
Car wash	Service Booking	bays, service slots
Structural engineer	Professional Consultation	projects, site information
Solicitor	Professional Consultation	matters, client information, conflict checks
Realtor	Property Service	properties, enquiries, viewings
Motel	Accommodation	rooms, stays, check-in/out
Mobile mechanic	Mobile Service	vehicle + location
Hairdresser	Service Booking	staff/resource allocation


Result: the model survives the first cross-niche test.


---

35. But we have discovered an important requirement

The framework cannot be determined solely from the industry label.

The onboarding engine should infer:

BUSINESS DOMAIN
+
OPERATIONAL PATTERN
+
RESOURCE MODEL
+
CUSTOMER INTERACTION MODEL
+
TRANSACTION MODEL

For example, two realtors might differ substantially.

One may only:

list properties
collect enquiries

Another may:

list
schedule viewings
collect deposits
manage appointments

The semantic configuration reflects the actual business.


---

36. Therefore onboarding questions should target semantics

Not:

> "What industry are you in?"



alone.

Instead:

> "What do customers come to you to do?"



> "Do customers purchase something, book a service, request a consultation, enquire about a property, or something else?"



> "Do you allocate appointments, physical resources, staff, rooms, or none?"



> "Do customers pay before the service is confirmed?"



These questions reveal operational semantics.


---

37. AI inference becomes much easier

Instead of asking an AI agent to design an application:

"Build a website for a structural engineer."

we give it structured evidence:

business_domain = PROFESSIONAL_SERVICE

customer_action = BOOK_CONSULTATION

resource = CONSULTANT

resource_allocation = TIME_SLOT

payment = BEFORE_CONFIRMATION

documents = OPTIONAL

service_location = CUSTOMER_SITE

The candidate graph becomes much more deterministic.


---

38. The prototype should now use a real merchant configuration

Our next prototype should not merely draw boxes.

We should construct one complete configuration.

I recommend:

> Car wash as Prototype Merchant A.



Why?

Because it exercises almost everything we have discussed:

service
offering
customer
resource
capacity
scheduling
allocation
payment
confirmation
cancellation
concurrency
merchant policy
customer operation
merchant operation

Then use:

> Structural engineer as Prototype Merchant B



to prove that the model generalises outside appointment-based consumer services.

And:

> Realtor as Prototype Merchant C



to test a non-transactional enquiry/viewing model.


---

39. Prototype acceptance criterion

We should not move to implementation merely because the three prototypes "look reasonable."

They must demonstrate:

1. Same primitives can be reused.
2. Same abstractions can be reused.
3. Frameworks can specialise appropriately.
4. Merchant-specific differences are configuration.
5. No business-specific runtime code is required.
6. Customer and merchant operations remain separate from UI.
7. Policies can be enabled/disabled safely.
8. Invalid combinations are rejected.
9. Resource allocation remains race-safe.
10. Configuration can be versioned and published.

If any of these fail, we return to the semantic model.


---

40. MS-PROT-016 validation result

Question	Result

Can commerce support multiple merchant niches?	Yes
Can service booking support different resources?	Yes
Can professional consultation support different professions?	Yes
Can property services coexist with booking semantics?	Yes
Can accommodation use the same resource allocation concepts?	Yes
Can merchant-specific data requirements be represented?	Yes
Can merchant-specific policies be represented?	Yes
Can dashboard capabilities be derived from configuration?	Yes
Can customer UI remain independent?	Yes
Does every niche require bespoke backend logic?	No
Is the semantic model sufficiently concrete for a first prototype?	Yes



---

Retired next step: proposed MS-PROT-017 — Car Wash End-to-End Semantic Prototype

The following experiment was planned but intentionally not published as MS-PROT-017. Cross-domain validation in this document and the later accepted series already established that the kernel's semantic machinery was not tied to one business domain. A car-wash-specific walkthrough would therefore have repeated evidence without deciding a new architectural boundary.

The number remains unused to preserve historical traceability; MS-PROT-018 is not normatively dependent on a missing document.

The retired experiment would have instantiated one hypothetical car wash and traced the complete system from:

We will take one hypothetical car wash and trace the complete system from:

Onboarding answers
       ↓
Business description
       ↓
AI semantic inference
       ↓
Primitive composition
       ↓
Framework selection
       ↓
Capabilities
       ↓
Policies
       ↓
Resources
       ↓
State machines
       ↓
Customer operations
       ↓
Merchant operations
       ↓
Concurrency/resource allocation
       ↓
Payment
       ↓
Events
       ↓
Configuration artifact
       ↓
Merchant approval
       ↓
Customer-facing semantic contract
       ↓
Merchant-dashboard semantic contract
       ↓
Publication

This graph is retained as research history, not an outstanding implementation dependency.
