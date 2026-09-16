> **Governance note (21 August 2026):** Retained as prototype evidence. The accepted MS-PROT-020 onward series governs current implementation and deliberately generalises beyond this validation slice.

Yes. We now stop expanding the architecture and define the Prototype 1 vertical slice precisely.

The prototype should prove the architecture, not try to prove every business domain.

Prototype 1: the minimum executable Main Street

We need a scenario that exercises:

merchant configuration;

customer website generation;

customer action;

merchant operation;

state transitions;

authorization;

events;

notification;

configuration evolution.


A generic resource booking scenario is ideal because it doesn't tie Main Street to hospitality, legal services, trades, or any other particular domain.

The semantic domain is simply:

Resource
Customer
Booking
Availability

The merchant can configure what the resource actually represents.


---

1. Initial merchant configuration

The merchant configures:

Capability:
    booking

Resource:
    bookable_resource

Operation:
    booking.create

State:
    requested
    confirmed
    cancelled

Relationships:

Booking
   ├── made_by → Customer
   └── reserves → BookableResource

That's enough.

The merchant's actual business terminology is content/configuration.

The runtime only sees the generic model.


---

2. Compilation

The configuration goes through:

Merchant Admin
      │
      ▼
Configuration API
      │
      ▼
Configuration Store
      │
      ▼
Compiler
      │
      ├── structural validation
      ├── semantic validation
      ├── reference resolution
      ├── dependency analysis
      ├── normalization
      └── ESM generation
      │
      ▼
ESM v1
      │
      ▼
Activation

At this point the merchant becomes operational.


---

3. Customer website

The customer browser requests:

GET /experience

The backend returns the customer experience contract.

Conceptually:

Customer Experience
│
├── resources
│     └── bookable_resource
│
├── operations
│     └── booking.create
│
└── navigation
      └── available resources

The Main Street customer renderer turns this into the merchant's website.

The important thing:

The website is not hard-coded for "bookings".

It receives the experience contract.


---

4. Customer chooses a resource

The customer selects a resource and starts the operation.

Customer
   │
   ▼
booking.create
   │
   ├── target = resource_definition:booking
   ├── resource_id to reserve
   ├── requested time
   └── other configured input

The browser sends the operation request.

POST /operations/booking.create

`booking.create` targets the Booking resource definition because no Booking
instance exists yet. The selected bookable resource remains an input to the
operation; it is not the operation target.


---

5. Runtime executes it

The previously defined execution pipeline takes over:

Request
  ↓
Merchant resolution
  ↓
ESM resolution
  ↓
Actor resolution
  ↓
Operation resolution
  ↓
Authorization
  ↓
Input validation
  ↓
Constraint evaluation
  ↓
Transaction
  ↓
State transition
  ↓
Persist
  ↓
Outbox event
  ↓
Commit

The resulting booking becomes:

requested

There is no persisted `none` state. Inside the transaction, the runtime
creates one stable Booking identity directly in `requested`, records the
corresponding event and commits both together. A retry with the same request
identity returns the same Booking rather than creating another one.


---

6. Customer receives the authoritative result

The operation response returns:

status: succeeded

booking:
    id: BKG-001
    state: requested

The browser updates its representation.

No guessing.

No client-side business logic deciding whether the booking exists.


---

7. Merchant sees it

The committed event is:

BookingCreated

The event publisher processes the outbox.

The merchant application has an active observation stream.

BookingCreated
      │
      ▼
Merchant SSE
      │
      ▼
Merchant application

The merchant sees the new booking.


---

8. Merchant confirms it

The merchant has the privilege:

booking.confirm

The merchant application invokes:

POST /operations/booking.confirm

The exact same runtime executes:

authorize
validate
check state
transaction
transition
persist
event
commit

The booking moves:

requested
     ↓
confirmed


---

9. Customer observes the change

The resulting event:

BookingConfirmed

is published.

The customer's browser receives the event.

BookingConfirmed
      ↓
Customer browser
      ↓
refresh/update booking representation

Now both sides converge on:

confirmed

This proves the two-way operational relationship we have been discussing.


---

10. Notification

The same event enters the notification subsystem.

BookingConfirmed
       │
       ▼
Notification subsystem
       │
       ├── determine recipient
       ├── determine channel
       ├── obtain appropriate content
       └── deliver

Crucially, the notification subsystem does not need to know that the business is a booking business.

It processes:

event
→ message intent
→ recipient
→ channel
→ representation

The merchant's content/configuration supplies the domain-specific wording.


---

11. Now we test evolution

This is where the prototype becomes genuinely valuable.

The merchant changes configuration.

Initially:

Capability:
    booking

Resource:
    bookable_resource

Then they add:

Capability:
    resource_selection

and perhaps another operation:

booking.reschedule

The process is:

Configuration v1
      │
      ▼
Merchant change
      │
      ▼
Configuration v2
      │
      ▼
Compile
      │
      ▼
Validate
      │
      ▼
ESM v2
      │
      ▼
Activate
      │
      ▼
Experience v2

Existing bookings remain intact.

New customer sessions see the new capability.

Existing operational records aren't rewritten.

That proves evolution without migration of the whole application.


---

12. Prototype architecture

We can now define the actual first implementation boundary:

┌──────────────────────────────────────────────┐
│                  MAIN STREET                 │
│                                              │
│  Merchant Admin                              │
│       │                                      │
│       ▼                                      │
│  Configuration API                           │
│       │                                      │
│       ▼                                      │
│  Configuration Compiler                      │
│       │                                      │
│       ▼                                      │
│  Executable Semantic Model                   │
│       │                                      │
│       ▼                                      │
│  ┌────────────────────────────────────────┐  │
│  │             Generic Runtime            │  │
│  │                                        │  │
│  │ authorization                          │  │
│  │ validation                             │  │
│  │ constraints                            │  │
│  │ state transitions                      │  │
│  │ persistence                            │  │
│  │ events                                 │  │
│  └────────────────────────────────────────┘  │
│       │                       │              │
│       ▼                       ▼              │
│ Customer API             Merchant API       │
│       │                       │              │
└───────┼───────────────────────┼──────────────┘
        │                       │
        ▼                       ▼
 Customer Browser        Merchant Applications

For Prototype 1, these can all be implemented as one modular monolith.

That is exactly consistent with the structural decision we already made.


---

13. What we are NOT building yet

Do not let Prototype 1 expand into the entire Main Street vision.

We are deliberately excluding:

Multiple merchant locations
Complex inventory
Advanced scheduling
Payments
Accounting
Advanced analytics
External integrations
AI agents
Sophisticated workflow orchestration
Enterprise administration

Not because the architecture can't eventually support them.

Because we have no reason to implement them before validating the kernel.


---

14. The prototype acceptance test

The prototype is successful only if we can demonstrate this complete sequence:

1. Create merchant
       ↓
2. Define configuration
       ↓
3. Compile configuration
       ↓
4. Activate model
       ↓
5. Customer opens generated website
       ↓
6. Customer performs operation
       ↓
7. Runtime validates and executes
       ↓
8. State persists
       ↓
9. Event is recorded
       ↓
10. Merchant receives live update
       ↓
11. Merchant performs operation
       ↓
12. Customer receives updated state
       ↓
13. Notification is generated
       ↓
14. Merchant changes configuration
       ↓
15. New model is compiled
       ↓
16. New experience becomes available
       ↓
17. Existing operational data remains valid

That is our first real milestone.


---

15. What we should design next

At this point I would not continue creating architectural layers.

We should now switch to implementation specifications.

The immediate next task is:

Define the concrete Prototype 1 domain-neutral configuration schema.

Not the entire Main Street configuration language.

Just enough to express:

Merchant
Resource
Relationship
Operation
State
Transition
Constraint
Capability
Experience

Then we can implement the compiler against that schema.

After that:

database schema → runtime interfaces → API → customer renderer → merchant interface → vertical slice.

That is the shortest credible path from what we've designed to a running Main Street prototype.
