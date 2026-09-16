> **Governance note (21 August 2026):** This is the original prototype validation charter and is retained as research evidence. Its historical `MS-PROT-001` label predates the numbered design series; the authoritative numbered MS-PROT-001 is `MS-PROT-001 — Semantic Composition Prototype`. Accepted MS-PROT-020 onward governs current implementation where this note differs.

Good. We will now treat this as the prototype baseline.

Main Street Prototype Specification

Historical document label: MS-PROT-001 (prototype charter; not the numbered design-series identifier)
Version: 0.1
Status: Prototype Baseline
Relationship to MS-AVS-001: Experimental implementation; not architectural acceptance

1. Prototype objective

Demonstrate that Main Street can represent materially different local businesses through composition rather than business-specific templates, while keeping the merchant and customer experience simple.

The prototype must prove:

Business understanding
        ↓
Semantic composition
        ↓
Capability configuration
        ↓
Policy configuration
        ↓
Merchant approval
        ↓
       ┌───────────────┐
       ▼               ▼
Merchant Dashboard  Customer Website
       │               │
       └───────┬───────┘
               ▼
        Common Runtime


---

2. The semantic stack

We will prototype four semantic levels.

Level 1 — Business

What the merchant actually is.

Examples:

Motel
Grocery
Spare-parts dealer
Mechanic
Structural engineer
Solicitor
Realtor

Business identity is not the runtime architecture.

Level 2 — Framework

Broad operating patterns.

Initial candidates:

Trader
Service
Professional Service
Accommodation
Property

These are abstractions under validation, not rigid inheritance classes.

Level 3 — Capability

Something Main Street can enable for the merchant.

Examples:

Catalogue
Product
Service
Booking
Appointment
Inventory
Delivery
Collection
Enquiry
Consultation
Resource
Payment
Notifications

Level 4 — Primitive

Small reusable domain actions or concepts.

Examples:

Offer
Search
Enquire
Schedule
Reserve
Allocate
Order
Fulfil
Deliver
Collect
Pay
Refund
Cancel
Notify
Verify

The prototype will test whether this separation actually reduces complexity.


---

3. Configuration

A merchant configuration is the composition of existing semantic elements.

Conceptually:

Merchant Configuration
│
├── Frameworks
├── Capabilities
├── Policies
├── Data Requirements
└── Presentation Configuration

Example motel:

frameworks:
  - accommodation

capabilities:
  - catalogue
  - booking
  - resource
  - availability
  - payment
  - notifications

policies:
  booking:
    customer_selects: category
    automatic_resource_allocation: true

  payment:
    confirmation_required: true

  resource:
    availability_control: staff

The syntax is provisional. The semantics are what we are testing.


---

4. Merchant configuration principle

AI performs configuration inference, not content invention.

The distinction is important.

Website description

AI may generate a description from the merchant's answers.

Merchant approves it.

Configuration

AI selects from Main Street's existing semantic vocabulary.

AI
 ↓
Select existing capabilities
 ↓
Select existing policies
 ↓
Select existing data requirements
 ↓
Produce configuration

It cannot silently create:

NewCapabilityXYZ

If the business cannot be represented:

CONFIGURATION GAP

That becomes evidence for domain analysis.


---

5. Customer-data model

Customer information is also compositional.

We will not create:

GroceryCustomer
MechanicCustomer
EngineerCustomer
SolicitorCustomer

Instead, requirements are composed.

Examples:

CONTACT

VEHICLE

DELIVERY_ADDRESS

PROJECT

PROPERTY_INTEREST

A mechanic could therefore require:

CONTACT
+
VEHICLE

Grocery delivery:

CONTACT
+
DELIVERY_ADDRESS

Structural engineer:

CONTACT
+
PROJECT
+
SITE

The customer UI is then generated from the approved requirements.


---

6. Availability

We will not create one universal availability state machine.

Availability is calculated from the relevant capability's constraints.

Conceptually:

Availability
=
Capacity
+
Existing commitments
+
Schedule
+
Resource constraints
+
Merchant policies

Different capabilities use different subsets.

Motel

Category capacity
+
Existing bookings
+
Room allocation
+
Merchant availability policy

Mechanic

Opening hours
+
Service duration
+
Existing appointments
+
Staff/resource capacity

Grocery

Product stock
+
Order constraints
+
Fulfilment capacity
+
Delivery availability

This is a major prototype test.


---

7. Motel prototype

This is our first complete vertical slice.

The motel deliberately remains simple.

Suppose:

Standard: 10 rooms
Executive: 4 rooms

The customer does not choose Room 101.

They choose:

Standard
Executive

Main Street determines whether capacity exists.

If Standard capacity exists:

Customer requests Standard
        ↓
Capacity check
        ↓
Allocate physical room internally
        ↓
Booking

If Standard is unavailable:

Standard unavailable
        ↓
Executive available?
        ↓
Offer upgrade


---

8. Room availability

Internally:

Room
├── category
├── current allocation
└── merchant-controlled availability

The critical rule established in our design:

> A room does not become available automatically. A staff member marks it available after it has been prepared.



Therefore:

AVAILABLE
    │
    ▼
ALLOCATED / OCCUPIED
    │
    ▼
UNAVAILABLE
    │
    │ staff confirms preparation
    ▼
AVAILABLE

We are deliberately avoiding:

checkout
 ↓
automatic cleaning
 ↓
automatic inspection
 ↓
automatic availability

That would be unnecessary complexity for the target merchant.


---

9. Customer-facing availability

The customer does not need to understand room operational state.

They ask:

> "Can I book a Standard room?"



Main Street evaluates the internal model and responds appropriately.

The customer experience is:

Standard
Available → Book
Unavailable → Suggest alternative

The internal complexity remains invisible.

This is exactly what business-serving technology should mean.


---

10. Payment policy

The merchant controls whether payment is required for confirmation.

Payment required

Request
   ↓
Temporary allocation
   ↓
10-minute payment window
   ↓
Payment
   ↓
Confirmed

If payment fails/expires, the allocation is released.

The customer's unfinished activity can remain recoverable without forcing them to restart from zero.

Payment not required

Request
   ↓
Confirm according to capability rules

No artificial temporary state is introduced.


---

11. Merchant dashboard

The dashboard is configuration-driven.

A motel might see:

Dashboard

Today
────────────────
Bookings       7
Occupied       6
Available      4

Bookings
Rooms
Customers
Payments
Website
Announcements

A mechanic might see:

Dashboard

Today
────────────────
Appointments   8
Pending        2

Appointments
Services
Customers
Vehicles
Payments
Website

The mechanic doesn't see:

Rooms
Inventory
Accommodation

unless those capabilities actually exist.


---

12. Customer website

The website is another projection of the same configuration.

It should not be independently hard-coded.

Approved Configuration
          │
     ┌────┴────┐
     ▼         ▼
 Dashboard   Website

This is critical.

The same business understanding controls both experiences.


---

13. Prototype verticals

After the motel:

Mechanic

Service
    ↓
Appointment
    ↓
Vehicle information
    ↓
Schedule
    ↓
Optional payment

Grocery

Products
    ↓
Catalogue
    ↓
Inventory
    ↓
Order
    ↓
Delivery / collection
    ↓
Address
    ↓
Payment

Structural engineer

Service
    ↓
Consultation
    ↓
Project/site information
    ↓
Appointment
    ↓
Payment

Realtor

Property
    ↓
Listing
    ↓
Enquiry
    ↓
Viewing
    ↓
Customer

The realtor is particularly useful because it challenges our assumptions about "commerce", "inventory", "booking", and "service".


---

14. Prototype acceptance tests

We should not judge the prototype by how attractive the screens look.

The important tests are architectural.

Test 1 — Composition

Can five materially different businesses use the same semantic machinery?

Test 2 — Capability isolation

Does each merchant receive only relevant functionality?

Test 3 — Data requirements

Can different customer-information requirements be composed without business-specific customer models?

Test 4 — Availability

Can materially different availability problems be represented without a universal state machine?

Test 5 — Configuration

Can AI configure existing capabilities without inventing domain concepts?

Test 6 — UX simplicity

Does the merchant see a business-specific dashboard rather than a generic Main Street control panel?

Test 7 — Website consistency

Does the customer website derive from the same approved configuration?

Test 8 — Domain integrity

Can we change one capability without corrupting another capability's domain?


---

15. What we build first

We should not start with all five businesses.

The implementation order should be:

PHASE 1
Semantic vocabulary
        ↓
Configuration model
        ↓
Motel configuration
        ↓
Motel runtime
        ↓
Merchant dashboard
        ↓
Customer website
        ↓
Complete booking journey

Then:

PHASE 2
Mechanic composition
        ↓
Identify architectural gaps
        ↓
Correct model

Then:

PHASE 3
Grocery
        ↓
Identify architectural gaps

Then:

PHASE 4
Structural engineer
        ↓
Realtor

Only after this should we begin turning the prototype discoveries into formal validated domain specifications.


---

One important discipline

From this point onward, when the prototype encounters something we haven't designed, we do not immediately add another abstraction.

We classify the discovery:

Existing primitive sufficient?
        │
       YES
        ↓
     Compose it

        NO
        ↓
Existing capability sufficient?
        │
       YES
        ↓
     Configure it

        NO
        ↓
Existing framework sufficient?
        │
       YES
        ↓
Extend capability

        NO
        ↓
     Semantic gap

Only a demonstrated semantic gap earns a new abstraction.

That is how we keep Main Street internally sophisticated but externally simple.

Next artefact: MS-PROT-002 — Semantic Vocabulary & Composition Specification. This is the first concrete design document we should produce before drawing the prototype screens, because every subsequent screen and workflow will depend on this vocabulary.
