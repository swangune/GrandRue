Next is MS-PROT-002 — Capability Registry.

We should stop expanding primitives for the moment. The next job is to define the small set of reusable capabilities that sit above the primitives and can be composed into a merchant's operational model.

MS-PROT-002 — Capability Registry

Status: Proposed
Purpose: Define reusable business capabilities without encoding business niches.

1. The hierarchy

We now have:

PRIMITIVES
    ↓
ABSTRACT FRAMEWORKS
    ↓
CAPABILITIES
    ↓
MERCHANT CONFIGURATION
    ↓
OPERATIONAL RUNTIME
    ↓
PRESENTATION CONTRACT
    ↓
CUSTOMER / MERCHANT UI

AI operates primarily at the composition/configuration layer.

It does not invent domain behaviour.


---

2. What is a capability?

A capability is something Main Street can enable for a merchant because that merchant's business needs it.

Examples:

Catalogue
Booking
Ordering
Scheduling
Payment
Delivery
Collection
Enquiry
Service
Accommodation
Inventory
Resource
Availability
Customer Communication

But we need to be careful.

A capability is not merely a database table and not merely a UI feature.

It represents a coherent piece of business behaviour.


---

3. Capability categories

I propose grouping capabilities into six families.

MAIN STREET CAPABILITIES
                              │
       ┌──────────┬───────────┼───────────┬───────────┐
       ▼          ▼           ▼           ▼           ▼
   Discovery   Commerce   Engagement   Fulfilment  Operations
       │          │           │           │           │
       └──────────────────────┬───────────────────────┘
                              ▼
                       Cross-cutting

Discovery

Catalogue
Offering
Location
Discovery
Website presence

Commerce

Ordering
Booking
Payment
Quotation
Transaction

Engagement

Enquiry
Customer interaction
Communication
Notification

Fulfilment

Service
Delivery
Collection
Accommodation
Appointment

Operations

Inventory
Resource
Availability
Scheduling
Allocation
Staff operations

Cross-cutting

Identity
Verification
Audit
Interaction
Policy
Requirements

Not every item necessarily becomes a standalone bounded context. This is a capability classification, not an architectural boundary decision.


---

4. Capability contract

Every capability should conform to the same conceptual contract.

CAPABILITY
│
├── Identity
│
├── Purpose
│
├── Inputs
│
├── Outputs
│
├── Requirements
│
├── Policies
│
├── State
│
├── Events
│
├── Commands
│
├── Invariants
│
├── Dependencies
│
└── Configuration Schema

This is important because it gives our specialist AI agents something precise to work with.


---

5. Example: Booking capability

BOOKING
│
├── Purpose
│   └── Create and manage bookable commitments
│
├── Requires
│   ├── Offering
│   └── Availability
│
├── Optional
│   ├── Payment
│   ├── Resource
│   ├── Schedule
│   └── Customer identity
│
├── Policies
│   ├── payment_confirmation
│   ├── cancellation
│   ├── temporary_allocation
│   └── customer_selection
│
├── Commands
│   ├── RequestBooking
│   ├── ConfirmBooking
│   ├── CancelBooking
│   └── ModifyBooking
│
└── Events
    ├── BookingRequested
    ├── BookingConfirmed
    ├── BookingCancelled
    └── BookingModified

Notice that Booking does not say:

> "A booking is for a room."



It remains domain-neutral.


---

6. Motel composition

The motel might therefore receive:

Accommodation
Catalogue
Booking
Availability
Allocation
Payment
Interaction
Notification

with configuration:

Booking
    customer_selection = category
    allocation = automatic
    payment = merchant_policy

And:

Availability
    resource_type = room
    release = staff_action

This produces the simple motel behaviour we agreed on.

No housekeeping management system.

No cleaner mobile application.

No elaborate hotel operations.


---

7. Mechanic composition

The mechanic might receive:

Catalogue
Service
Booking
Scheduling
Customer
Payment
Interaction
Notification

with:

Booking
    schedule = required
    payment = optional

Requirements
    vehicle_registration = required

If the mechanic later enables mobile service:

Service
    location_mode = merchant_or_customer

and then:

Customer location

becomes relevant.

Again, no new "Mobile Mechanic" capability.


---

8. Grocery composition

Catalogue
Ordering
Inventory
Payment
Fulfilment
Delivery
Collection
Interaction
Notification

Policy:

Delivery
    enabled = true

Collection
    enabled = true

Requirement:

Delivery
    delivery_address = required

If the merchant disables delivery:

Delivery = disabled

and the address requirement disappears from the applicable customer workflow.

This is exactly what we want:

configuration changes the operational model; the merchant doesn't configure a giant system.


---

9. Realtor composition

A realtor could receive:

Catalogue
Enquiry
Scheduling
Appointment
Interaction
Notification

Potentially:

Quotation
Payment

if their particular business needs those capabilities.

Property information becomes part of the offering/property domain rather than requiring a special "realtor platform."


---

10. The most important distinction

We need to distinguish:

Capability

What Main Street can do.

Booking
Delivery
Payment
Enquiry

Policy

How this merchant chooses to use it.

Payment required before confirmation
Delivery enabled
Customer cancellation disabled

Requirement

What information/action is necessary in a particular operation.

Delivery → address
Vehicle service → registration
Paid consultation → payment

Framework

A reusable composition of capabilities.

For example:

SERVICE_BOOKING_FRAMEWORK
    Service
    Booking
    Schedule
    Customer
    Notification

Niche

The business context inferred by Main Street.

Mechanic
Motel
Grocery
Realtor

The niche should not become the architecture.


---

11. This gives us the AI architecture

This is where your original "Scratch" analogy becomes useful.

The AI does something like:

Merchant answers structured questions
              ↓
Business understanding
              ↓
Semantic classification
              ↓
Capability selection
              ↓
Framework composition
              ↓
Policy selection
              ↓
Requirement selection
              ↓
Configuration validation
              ↓
Approved operational configuration

The AI is effectively assembling legal semantic blocks.

It isn't programming Main Street.


---

12. Example

Merchant answers:

> "We are a small motel with 12 rooms. Guests can book online or walk in. They choose standard or executive rooms. We require payment before online booking is confirmed. Guests cannot choose individual room numbers. Rooms become available again only after staff have cleaned them."



AI should infer something equivalent to:

USE Accommodation
USE Catalogue
USE Booking
USE Availability
USE Allocation
USE Payment
USE Interaction
USE Notification

CONFIGURE Booking:
    selection = category
    allocation = automatic
    online_confirmation = payment_required

CONFIGURE Availability:
    resource = room
    release = staff_action

CONFIGURE Access:
    online_booking = enabled
    walk_in = enabled

It should not invent:

HousekeepingManager
CleaningShift
RoomInspectionWorkflow
CleanerMobileApp

unless those are actually selected capabilities.

That is how we prevent over-engineering.


---

13. Capability Registry acceptance criteria

Before we declare the registry validated, it must demonstrate:

[ ] No capability is tied unnecessarily to one business niche.

[ ] Capabilities have clear ownership.

[ ] Capabilities have explicit dependencies.

[ ] Capabilities have explicit invariants.

[ ] Policies are separate from capabilities.

[ ] Requirements are separate from policies.

[ ] AI cannot invent capabilities.

[ ] A capability can be enabled/disabled without corrupting unrelated capabilities.

[ ] Motel can be represented without hotel-industry overengineering.

[ ] Mechanic can be represented.

[ ] Grocery can be represented.

[ ] Realtor can be represented.

[ ] A new business can compose existing capabilities without modifying the core.



---

14. The next thing we should design

There is one layer missing before we can safely build the AI composer:

MS-PROT-003 — Capability Schema

We need to define the actual machine-readable structure of a capability.

For example, conceptually:

Capability
{
    id
    version
    purpose
    requires[]
    optional[]
    commands[]
    events[]
    policies[]
    requirements[]
    invariants[]
    dependencies[]
    configuration_schema
}

Then we can define Booking, Scheduling, Availability, Payment, Ordering, etc. using the same contract.

Once that exists, we can finally prototype the Semantic Composer that turns merchant understanding into a validated configuration.

That is the next major milestone.