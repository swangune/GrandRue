MS-PROT-004 — State & Transition Framework

Status: Proposed
Purpose: Define how Main Street represents business operations changing over time without imposing one universal workflow on every business.

This is a critical layer. If we get state wrong, the rest of the architecture becomes complicated.


---

1. First principle: there is no universal business state machine

We should not do this:

CREATED
→ PENDING
→ CONFIRMED
→ PROCESSING
→ COMPLETED

for everything.

A motel stay, a grocery order, a property enquiry and a mechanic appointment are fundamentally different.

Instead, Main Street provides a state framework from which each capability constructs its own valid lifecycle.

State Framework
      ↓
Capability lifecycle
      ↓
Merchant policy
      ↓
Actual operation


---

2. State is owned by the capability

This follows the same principle we established for events.

Booking owns Booking state
Order owns Order state
Stay owns Stay state
Enquiry owns Enquiry state
Appointment owns Appointment state

The generic infrastructure only provides the machinery for storing, transitioning, validating and observing state.

It does not dictate the business meaning.


---

3. What is a state?

A state is a persisted semantic condition of a domain operation.

For example:

Booking
    REQUESTED
    CONFIRMED
    CANCELLED

A temporary UI condition is not automatically a domain state.

This distinction solves the earlier "customer paused for a few minutes" problem.


---

4. Customer activity is not automatically a transaction

Suppose a customer starts booking a car wash.

They select:

Car wash
Tuesday
14:00

Then leave the page.

We should not necessarily create:

Booking
    status = PENDING

Instead, depending on merchant policy:

Customer interaction
        ↓
Cached activity

The activity can later be recovered.

If the slot remains available:

resume
 ↓
continue

If somebody else books it:

resume
 ↓
availability recalculated
 ↓
original slot unavailable
 ↓
alternative presented

This is a much cleaner model.


---

5. Therefore we need two distinct concepts

Interaction state

What the customer/system is currently doing.

Examples:

viewing
selecting
entering_information
reviewing

These are often transient.

Domain state

What the business has actually committed to.

Examples:

booking_confirmed
order_confirmed
appointment_confirmed

These are persistent business facts.

This distinction is fundamental.


---

6. The interaction layer

We already have the Persistent Capability-Owned Interaction Model.

We can now refine it:

Customer
   ↓
Interaction
   ├── Context
   ├── Activity
   ├── Selected data
   ├── Progress
   └── Expiry
          ↓
Capability command
          ↓
Domain state

An interaction can exist without creating a domain transaction.

That gives us the "fluid cache" behaviour you wanted.


---

7. Temporary allocation is different

Now consider a merchant requiring payment.

Customer:

Selects appointment
       ↓
Payment required
       ↓
Temporary allocation
       ↓
10-minute timer

Here we do create a domain-level temporary hold because capacity must be protected.

Conceptually:

Interaction
      ↓
Reservation/Hold
      ↓
Payment
      ↓
Commitment

If payment succeeds:

HOLD
 ↓
CONFIRMED

If it expires:

HOLD
 ↓
RELEASED

This is much better than making every abandoned interaction a "pending booking."


---

8. Three fundamentally different things

We should now explicitly distinguish:

INTERACTION
    "Customer is doing something."

HOLD
    "Main Street has temporarily protected something."

COMMITMENT
    "Merchant and customer have an accepted business obligation."

This is one of the most important decisions in the entire design.


---

9. State categories

We can therefore classify state into four categories.

1. Interaction state

Transient.

SELECTING
REVIEWING
ENTERING_DATA

Usually cached.


---

2. Hold state

Temporary protection of capacity/resource.

HELD
EXPIRED
RELEASED

Persistent because concurrency depends on it.


---

3. Commitment state

Actual business obligation.

CONFIRMED
ACTIVE
COMPLETED
CANCELLED

Persistent.


---

4. Resource state

Condition of a resource.

Motel:

UNAVAILABLE
AVAILABLE
ALLOCATED

But we should be cautious.

For the motel, available/unavailable is an operational condition, not necessarily a customer-facing state.


---

10. Generic transition contract

Every state transition should be defined as:

Transition
{
    from
    command
    guard
    to
    events
    side_effects
}

For example:

CONFIRM_BOOKING

from:
    REQUESTED

guard:
    offering_valid
    capacity_available
    required_information_present
    payment_requirement_satisfied

to:
    CONFIRMED

event:
    BookingConfirmed


---

11. Guards

A guard answers:

> Can this transition legally happen right now?



Examples:

capacity_available
merchant_open
payment_confirmed
required_information_present
customer_authorised
resource_available

A guard is deterministic.

AI does not decide whether a guard passes.


---

12. Invariants

Guards are transition-specific.

Invariants are universal constraints.

For example:

A confirmed booking cannot be confirmed again.

A resource cannot have two incompatible allocations.

An expired hold cannot be converted into a commitment.

These must hold regardless of merchant configuration.


---

13. Idempotency

Every command that can create or modify persistent business state must be safely repeatable.

Example:

ConfirmBooking(command_id = ABC)

If the request reaches the server twice:

ABC → processed
ABC → duplicate → return existing result

It must not create two bookings.

This is essential for payment, allocation and other race-prone operations.


---

14. Concurrency

The state framework must assume concurrent operations.

Example:

1 Standard room remaining

Customer A → booking request
Customer B → booking request

Both may observe:

available = true

But only one can successfully commit the allocation.

Therefore:

Availability check
        ↓
Transactional decision
        ↓
Atomic allocation

must be treated as one protected operation at the commitment boundary.


---

15. Availability is therefore not a state

This deserves emphasis.

Do not model:

Availability
    = state

Instead:

Availability
    = decision

based on:

capacity
+
existing commitments
+
holds
+
resource conditions
+
schedule
+
merchant policy
+
time

Then:

AvailabilityDecision
{
    available
    reason
    alternatives
}

This is much more powerful.


---

16. Motel example

Suppose:

Standard rooms = 8
Occupied = 7
Cleaning = 1

The customer asks:

> Can I book a Standard room tonight?



Availability evaluates:

Total capacity = 8
Committed = 7
Unavailable = 1
Effective capacity = 7

Result:

AVAILABLE = false

The system might then evaluate:

Executive = available

and return an upgrade option.

The customer never needs to know:

Room 101 = dirty
Room 102 = occupied
...


---

17. Staff availability action

When a motel employee cleans a room, they perform:

MARK_RESOURCE_AVAILABLE

The system validates:

resource exists
resource belongs to merchant
resource is eligible for release

Then:

Room
UNAVAILABLE
   ↓
AVAILABLE

There is no automatic assumption that cleaning occurred merely because checkout happened.

This preserves the rule we agreed:

> A room does not become available until staff explicitly marks it available.




---

18. Service example

For a mechanic:

09:00–10:00
Oil change

After the appointment finishes, we don't necessarily create a "service available" state.

Instead the availability engine calculates:

merchant hours
+
existing appointments
+
service duration
+
resource constraints

and determines whether another appointment can start.

If the merchant is open until 17:00:

10:00 → next available

unless policy/resource constraints say otherwise.

This is the common mechanism we wanted.


---

19. Motel and service therefore share the same framework

AVAILABILITY
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
      ACCOMMODATION              SERVICE
          │                         │
      capacity                  schedule
      resources                 duration
      room state                resources
      reservations              appointments

The domain-specific inputs differ.

The decision mechanism remains common.


---

20. State transition classes

We should support five transition types.

Normal

REQUESTED → CONFIRMED

Cancellation

CONFIRMED → CANCELLED

Completion

CONFIRMED → COMPLETED

Expiration

HELD → EXPIRED

Administrative correction

For authorised staff/system correction.

INCORRECT_STATE → CORRECTED_STATE

Administrative transitions need stronger audit controls.


---

21. No arbitrary state transitions

A capability declares its legal transition graph.

For example:

Booking

REQUESTED
   │
   ├── cancel → CANCELLED
   │
   └── confirm → CONFIRMED
                    │
                    ├── cancel → CANCELLED
                    │
                    └── complete → COMPLETED

This means the runtime can reject:

COMPLETED → CONFIRMED

without needing AI or merchant configuration to decide.


---

22. State framework schema

Our conceptual schema becomes:

StateMachine
{
    id
    version

    states[]

    initial_state

    transitions[]
}

Each transition:

Transition
{
    id

    from_state

    command

    guards[]

    to_state

    emitted_events[]

    side_effects[]

    idempotency_policy
}

This is enough for the design stage.

We should not add a general-purpose rules engine.


---

23. The AI's role

AI can select an existing state framework:

Merchant understanding
       ↓
Capability = Booking
       ↓
Payment required = YES
       ↓
Select validated booking state model

AI cannot invent:

BOOKING_WAITING_FOR_MAGIC_PAYMENT

The state vocabulary is controlled.


---

24. This gives us our first major architectural safety boundary

AI
                     │
             inference/composition
                     │
                     ▼
             Semantic Configuration
                     │
             deterministic validation
                     │
                     ▼
              State Framework
                     │
                     ▼
                 Runtime

AI proposes.

The system validates.

The runtime executes.


---

25. MS-PROT-004 decisions

I would now mark these as accepted design decisions, subject to final validation:

Decision	Status

No universal state machine	Accepted
Capability owns domain state	Accepted
Interaction ≠ domain transaction	Accepted
Customer activity may remain cached	Accepted
Hold is distinct from interaction	Accepted
Hold is distinct from commitment	Accepted
Availability is a decision, not a state	Accepted
Allocation occurs at transactional boundary	Accepted
Commands must be idempotent	Accepted
State transitions require deterministic guards	Accepted
AI cannot invent states/transitions	Accepted
State machine framework is reusable	Accepted



---

26. What comes next

We have now reached the point where Requirements and Policies need to be designed properly.

This is particularly important because your central Main Street proposition depends on:

Merchant business understanding
        ↓
Relevant capabilities
        ↓
Relevant requirements
        ↓
Relevant policies
        ↓
Simple merchant controls

So the next design should be:

MS-PROT-005 — Requirement & Policy Framework

It will answer, at the lowest design level:

How requirements are declared.

How conditional requirements work.

How customer information is requested only when relevant.

How merchant policies are represented.

Which policies are configurable.

Which policies are immutable invariants.

How policies affect state transitions.

How enabling/disabling one policy affects dependent requirements.

How payment requirements work.

How temporary allocation is derived.

How merchant-facing controls are inferred.

How configuration changes are validated.


That layer is the bridge between Main Street understanding a business and Main Street actually configuring its operational behaviour.