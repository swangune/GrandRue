MS-PROT-007 — Interaction, Command & Event Framework

Status: Proposed
Purpose: Define how Main Street turns customer and merchant actions into controlled business operations, while keeping temporary interaction activity separate from committed business facts.

This layer connects the work we've already validated.


---

1. The fundamental pipeline

Every meaningful operation follows this general pattern:

ACTOR
  ↓
INTERACTION
  ↓
COMMAND
  ↓
VALIDATION
  ↓
CAPABILITY
  ↓
DOMAIN OPERATION
  ↓
STATE CHANGE
  ↓
EVENT
  ↓
PERSISTENCE / REACTION

For example:

Customer
  ↓
"Book a standard room"
  ↓
BookAccommodation
  ↓
validate requirements
  ↓
check availability
  ↓
allocate
  ↓
create commitment
  ↓
AccommodationBooked

The important point is that AI is not sitting inside this execution path making decisions.

AI helps construct and configure the system beforehand. Runtime execution is deterministic.


---

2. Interaction

An interaction represents an actor's ongoing engagement with Main Street.

It can contain:

Interaction
├── actor
├── merchant
├── context
├── current activity
├── collected information
├── selected offering
├── selected options
├── progress
├── correlation identifier
└── expiry/retention metadata

For example:

Customer
   ↓
Mechanic
   ↓
Oil change
   ↓
Tuesday
   ↓
14:00
   ↓
vehicle selected

At this point, this does not mean a booking exists.

It is merely interaction state.


---

3. Interaction is recoverable, not authoritative

This gives us the behaviour you wanted.

Customer leaves:

Oil change
Tuesday 14:00
Vehicle ABC123

Main Street can retain the interaction.

Customer returns:

Continue booking?

The system can reconstruct the previous context.

But before proceeding:

recalculate availability

If 14:00 is still available:

continue

If it isn't:

14:00 unavailable
→ show alternatives

The cached interaction never overrides reality.


---

4. Three levels of truth

We should explicitly establish this hierarchy.

Level 1 — Interaction truth

> What the customer was doing.



"I was looking at 14:00."

Level 2 — Current operational truth

> What can be done now.



14:00 is currently unavailable.

Level 3 — Committed business truth

> What has actually been accepted.



Appointment 8472 is confirmed for 15:00.

Higher levels always take precedence.

Committed truth
      ↑
Operational truth
      ↑
Interaction context

This prevents stale cached activity from creating incorrect bookings.


---

5. Commands

A command expresses an intent to perform an operation.

Examples:

BookAppointment
CancelBooking
CreateOrder
ConfirmOrder
AllocateRoom
ReleaseRoom
MarkRoomReady
RequestDelivery
ConfirmPayment
CancelAppointment

A command is not an event.


---

6. Command vs event

This distinction should be rigid.

Command

> "Please do this."



BookAppointment

Event

> "This happened."



AppointmentBooked

Therefore:

Customer
   ↓
BookAppointment
   ↓
system executes
   ↓
AppointmentBooked

An event must never be treated as a request to perform an action.


---

7. Commands are validated before execution

Every command passes through deterministic validation.

Command
  ↓
Schema validation
  ↓
Actor authorization
  ↓
Capability validation
  ↓
Requirement validation
  ↓
Policy validation
  ↓
Business invariant validation
  ↓
Execute

If any required condition fails:

CommandRejected

No partial operation.


---

8. Example: motel booking

Customer sends:

BookAccommodation
{
    category: STANDARD
    check_in: 20 Aug
    check_out: 22 Aug
}

Runtime validates:

✓ motel offers STANDARD
✓ dates valid
✓ customer requirements satisfied
✓ merchant accepts bookings
✓ category available

Then allocation occurs.

If allocation succeeds:

AccommodationBooked

If another customer won the last room milliseconds earlier:

AccommodationBookingRejected
reason = CAPACITY_CONFLICT

The system can then ask the presentation layer to offer alternatives.


---

9. The race-condition boundary is now crystal clear

This is important enough to make an explicit architectural rule:

> Availability may be evaluated before a command, but only the atomic domain operation can establish the final truth.



Therefore:

Availability
    ↓
Customer proceeds
    ↓
Command
    ↓
AUTHORITATIVE RECHECK
    ↓
Allocation
    ↓
Commitment

We do not trust an old availability result.


---

10. Idempotency

Commands that can mutate business state need an idempotency key.

For example:

BookAppointment
command_id = 8F72A

If the network causes the request to arrive twice:

8F72A → execute
8F72A → duplicate

The second request returns the existing result.

The handled command identity, its stable result and the local business changes
must commit atomically. A retry can then return the recorded result without
re-running domain effects.

It must not create:

Appointment #101
Appointment #102

from one customer action.


---

11. Idempotency is not just for payments

It applies to all consequential operations:

booking
ordering
allocation
cancellation
refund initiation
resource release
payment confirmation
delivery request

This should be a framework-level primitive rather than something every capability engineer reinvents.


---

12. Events

Events represent facts.

Examples:

AppointmentBooked
BookingCancelled
RoomAllocated
RoomMarkedReady
OrderCreated
OrderConfirmed
PaymentReceived
DeliveryRequested
PropertyViewingBooked

Events should be immutable.

Once:

AppointmentBooked

has occurred, we don't edit it into:

AppointmentCancelled

We append:

AppointmentCancelled


---

13. Event sequence

This gives us an auditable history:

AppointmentCreated
       ↓
PaymentReceived
       ↓
AppointmentConfirmed
       ↓
AppointmentStarted
       ↓
AppointmentCompleted

Or:

AppointmentCreated
       ↓
AppointmentCancelled

The current state can be derived from the accepted domain transitions while the event history preserves what happened.


---

14. Events are not workflow instructions

This distinction prevents excessive coupling.

Suppose:

AppointmentBooked

occurs.

Several systems may react:

Notification
Analytics
Merchant dashboard
Customer history
Accounting integration

The booking capability doesn't need to know all of them.

It publishes the fact.

Subscribers decide whether the event is relevant.


---

15. Event consumers must also be idempotent

Suppose:

AppointmentBooked

is delivered twice.

The notification service must not send:

"Your appointment is confirmed."

twice unintentionally.

Therefore consumers need their own processing identity:

event_id
+
consumer_id

This becomes another framework primitive.

Event publication is at-least-once. A durable publication record is committed
with the domain change, and delivery is retried after commit. Each consumer
records successful processing by event_id + consumer_id so redelivery is safe.


---

16. Event ordering

We should not impose one global event ordering across Main Street.

That would unnecessarily constrain the architecture.

Instead, ordering matters within the relevant aggregate/domain context.

For example:

Booking A
    event 1
    event 2
    event 3

must preserve its logical sequence.

But:

Booking A
Booking B
Order C

do not need a single global sequence.

This is simpler and more scalable.


---

17. Aggregate boundary

We need one additional abstraction here: the business aggregate.

An aggregate is the boundary within which Main Street must preserve consistency.

Examples:

Booking
Order
Appointment
Stay
PropertyViewing

For capacity, the relevant allocation boundary may also involve:

Resource
CapacityPool

The exact aggregate boundaries are capability-specific.

We should not create one gigantic Merchant aggregate.


---

18. Why this matters for concurrency

Suppose a motel has:

Standard capacity = 8

Two bookings compete for the last unit.

The consistency boundary needs to protect the relevant capacity.

It does not need to lock:

entire motel

Likewise, two unrelated appointments at different times should not block one another unnecessarily.


---

19. Failed commands

A command can fail for different reasons.

We should distinguish at least:

INVALID

The request itself is malformed.

UNAUTHORISED

Actor cannot perform it.

POLICY_VIOLATION

Merchant configuration prohibits it.

REQUIREMENT_UNSATISFIED

Required information is missing.

CAPACITY_CONFLICT

Capacity disappeared before commitment.

STATE_CONFLICT

Operation is incompatible with current state.

TEMPORARY_FAILURE

Infrastructure failed and retry may be appropriate.

These should not all become one generic "something went wrong."


---

20. Retry behaviour

Main Street must distinguish:

RETRYABLE

from:

NON_RETRYABLE

For example:

network timeout
→ potentially retry

but:

room no longer available
→ do not blindly retry the same booking

Instead:

recalculate availability

This prevents automated retries from creating repeated conflicts.


---

21. Customer-facing recovery

The runtime produces a semantic result.

For example:

Booking failed
reason = CAPACITY_CONFLICT
alternatives =
    15:00
    16:00

The presentation layer decides how to communicate that.

It might show:

> That time is no longer available. These times are available instead.



Another merchant's UI could communicate it differently.

The backend remains identical.


---

22. Merchant actions use exactly the same framework

A motel employee:

MarkRoomReady

is just another command.

Staff
 ↓
MarkRoomReady
 ↓
authorization
 ↓
resource validation
 ↓
state transition
 ↓
RoomMarkedReady

This is preferable to creating a completely separate "staff workflow engine."


---

23. Example: room cleaning

Checkout:

StayCompleted

does not automatically generate:

RoomAvailable

Instead:

Room
    remains UNAVAILABLE

Staff later performs:

MarkRoomReady

Then:

RoomMarkedReady

and the room becomes eligible for availability calculations.

This directly implements the rule we established earlier.


---

24. Notifications are reactions, not domain logic

A successful booking might produce:

AppointmentBooked

Then:

Notification service
    ↓
customer confirmation

But the booking capability shouldn't contain:

send_email()
send_sms()
send_push()

That would tightly couple the domain to presentation/infrastructure.

The booking state, allocation, handled command result, domain event and event
publication record form one local transactional core. Notification delivery
starts only after that transaction commits. Retry is the normal recovery path;
compensation is used only when an already-committed external effect cannot be
completed or reversed through retry. A notification failure must never roll
back an authoritative booking or leak a partial local commitment.


---

25. The complete operational loop

We can now express Main Street's runtime architecture as:

┌──────────────┐
│    ACTOR     │
└──────┬───────┘
       ↓
┌──────────────┐
│ INTERACTION  │
└──────┬───────┘
       ↓
┌──────────────┐
│   COMMAND    │
└──────┬───────┘
       ↓
┌──────────────┐
│  VALIDATION  │
└──────┬───────┘
       ↓
┌──────────────┐
│  CAPABILITY  │
└──────┬───────┘
       ↓
┌──────────────┐
│  AUTHORITATIVE│
│   OPERATION   │
└──────┬────────┘
       ↓
┌──────────────┐
│ STATE CHANGE │
└──────┬───────┘
       ↓
┌──────────────┐
│    EVENT     │
└──────┬───────┘
       ↓
 ┌─────┼──────────┬───────────┐
 ↓     ↓          ↓           ↓
UI  Notification Audit    Integration

This is the core operational spine.


---

26. Where cached activity fits

It does not enter the domain event stream merely because someone clicked something.

For example:

Customer selects 14:00

may remain:

Interaction Activity

No:

AppointmentCreated

until the customer actually submits the consequential command.

This keeps our event stream meaningful.


---

27. Interaction activity vs business events

We should therefore have two separate records.

Interaction record

Customer selected 14:00
Customer entered vehicle registration
Customer viewed alternative

Domain event

AppointmentBooked
AppointmentCancelled
AppointmentCompleted

This distinction prevents event streams becoming polluted with UI telemetry.


---

28. AI remains outside the runtime authority path

This is another important architectural boundary.

AI can help with:

business understanding
configuration inference
semantic classification
presentation inference

But it must not be responsible for:

capacity allocation
state transition validity
payment confirmation
concurrency control

Those are deterministic system responsibilities.


---

29. The emerging Main Street architecture

We now have a very clear separation:

AI / INFERENCE
                       │
                       ▼
             CONFIGURATION SYSTEM
                       │
                       ▼
                CAPABILITY MODEL
                       │
        ┌──────────────┼───────────────┐
        ▼              ▼               ▼
   Requirements      Policies       State Models
        │              │               │
        └──────────────┼───────────────┘
                       ▼
                OPERATIONAL RUNTIME
                       │
        ┌──────────────┼───────────────┐
        ▼              ▼               ▼
   Interaction      Commands       Availability
        │              │               │
        └──────────────┼───────────────┘
                       ▼
                  Allocation
                       │
                       ▼
                 Domain State
                       │
                       ▼
                    Events

Then independently:

Operational semantics
        ↓
Presentation inference
        ↓
Customer UI / Merchant UI

That separation is exactly what we wanted.


---

30. MS-PROT-007 decisions

Decision	Status

Interaction is distinct from transaction	Accepted
Interaction can be cached/resumed	Accepted
Cached activity does not consume capacity	Accepted
Commands express intent	Accepted
Events represent immutable facts	Accepted
Commands and events are distinct	Accepted
Consequential commands are idempotent	Accepted
Event consumers are idempotent	Accepted
Handled command identity, stable result and local effects commit atomically	Accepted
Domain events use a transactional publication record and at-least-once delivery	Accepted
Final allocation is authoritative	Accepted
Availability results are not commitments	Accepted
No global event ordering	Accepted
Consistency is scoped to relevant domain boundaries	Accepted
Failed operations have semantic failure classes	Accepted
Retryability is explicit	Accepted
Merchant and customer actions use the same command framework	Accepted
Notifications react to events	Accepted
External reactions use retry first and compensation only where necessary	Accepted
AI does not control runtime business invariants	Accepted
Interaction telemetry is separate from domain events	Accepted



---

Next: MS-PROT-008 — Merchant Operational Configuration

This is the next piece I would design before touching UI.

We need to define exactly how the merchant's inferred business model becomes an executable configuration:

Merchant understanding
        ↓
Business semantic model
        ↓
Capabilities
        ↓
Policies
        ↓
Requirements
        ↓
State models
        ↓
Capacity/allocation rules
        ↓
Operational configuration

The critical question will be:

> What is the smallest, formal configuration language Main Street needs to represent all of this without turning it into a giant programming language?



That is where we should be particularly disciplined. We have enough primitives now to design that layer without introducing unnecessary complexity.
