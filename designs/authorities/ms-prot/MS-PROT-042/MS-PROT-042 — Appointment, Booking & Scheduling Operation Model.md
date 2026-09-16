# MS-PROT-042 — Booking, Appointment & Customer Scheduling Commitment Model

**Document ID:** MS-PROT-042  
**Version:** 1.2
**Status:** **Accepted after material revision and revalidation**  
**Supersedes:** MS-PROT-042 v1.1
**Depends on:** MS-PROT-005, MS-PROT-006, MS-PROT-007, MS-PROT-020, MS-PROT-021, MS-PROT-022, MS-PROT-023, MS-PROT-024, MS-PROT-025, MS-PROT-026, MS-PROT-028, MS-PROT-039, MS-PROT-040, MS-PROT-041  
**Purpose:** Define the strict and non-interchangeable semantics of **Booking** and **Appointment**, their respective availability models, customer-selection flows, merchant-controlled appointment flows, confirmation, modification, cancellation, automatic capacity release, and calendar consequences.

**Revision 1.2:** Defines the durable result when a customer accepts a proposed time but authoritative Appointment confirmation subsequently fails. Acceptance remains historical truth, while commitment and request resolution remain contingent on successful confirmation.

---

# 1. Governing decision

Main Street shall treat **Booking** and **Appointment** as distinct business concepts.

> **A Booking is a reservation commitment securing a supported bookable subject, resource, capacity or entitlement for a defined scope.**

> **An Appointment is a customer-related commitment establishing an agreed time interval during which a supported service or interaction is expected to occur.**

They are not synonyms.

They shall not be combined into a generic `BookingAppointment`, nor shall one be treated as a universal subtype of the other.

The distinction is based on **what is being committed**, not:

- the customer-facing verb;
- whether automation is involved;
- whether time appears somewhere in the transaction;
- whether payment is required;
- whether a calendar is involved.

---

# 2. Canonical distinction

```text
BOOKING
    reserves something

APPOINTMENT
    schedules a service interaction
```

Examples:

```text
Hotel room category
Friday → Sunday
        ↓
BOOKING
```

```text
Solicitor consultation
Tuesday
14:00–14:30
        ↓
APPOINTMENT
```

A hotel may also offer:

```text
Spa treatment
Saturday
15:00–16:00
        ↓
APPOINTMENT
```

to the same customer who has:

```text
Executive room
Friday → Sunday
        ↓
BOOKING
```

Both may coexist without being collapsed.

---

# 3. Terminology is normative

Within Main Street specifications, domain models, APIs, runtime semantics and merchant-facing operational surfaces:

- `Booking` shall mean reservation semantics.
- `Appointment` shall mean agreed service-time semantics.
- `Availability` shall always be qualified by the context where ambiguity is possible.
- `Booking availability` and `Appointment availability` shall not be assumed to mean the same calculation.
- `Cancel Booking` and `Cancel Appointment` shall represent different capability-owned operations.
- `Modify Booking` and `Reschedule Appointment` shall not be used interchangeably.

The specification shall avoid phrases such as:

```text
booking/appointment
```

where the relevant semantic distinction is known.

A broader term such as:

```text
customer commitment
```

may be used when a rule genuinely applies to both.

---

# 4. Customer-facing terminology

Main Street should preserve the distinction in customer-facing language where practical.

Preferred examples:

```text
Reserve room
Reserve equipment
Confirm booking

Schedule appointment
Choose appointment time
Confirm appointment
```

Main Street should not internally interpret the natural-language phrase:

> "Book an appointment"

as evidence that the domain object must be `Booking`.

Business meaning remains authoritative.

---

# 5. Shared semantic primitives do not erase the distinction

Booking and Appointment may both compose primitives such as:

```text
Customer
Requirement
Policy
Capacity
Allocation
Payment
Time
Resource
Notification
```

That does not make them the same abstraction.

Main Street favours composition:

```text
Booking
    + Capacity
    + Allocation
    + DateRange

Appointment
    + Scheduling
    + TimeInterval
    + Staff/Resource where applicable
```

over universal inheritance.

---

# 6. Booking definition

A **Booking** is:

> **A customer-related reservation commitment by which Main Street secures a supported bookable subject, resource, capacity or entitlement for the customer within a defined reservation scope.**

The reservation scope may be:

```text
date range
time range
resource category
quantity
capacity amount
specific resource where applicable
entitlement period
```

A Booking does not inherently mean that a service encounter occurs at a specific appointment time.

---

# 7. Booking examples

Valid Booking semantics include cases such as:

```text
Hotel room/category reservation

Equipment reservation

Bookable facility reservation

Capacity reservation

Accommodation stay

Reserved attendance capacity

Other registered reservation-style commitments
```

The specific domain capability determines the booked subject.

---

# 8. Booking minimum semantics

Conceptually:

```text
Booking
{
    booking_identity
    merchant_scope
    customer_relationship
    booked_subject
    reservation_scope
    lifecycle_state
    configuration_provenance
}
```

Where applicable it may also relate to:

```text
allocation
resource
quantity
payment
policy
fulfilment
```

These are contextual, not universal.

---

# 9. Booking requires a customer relationship

Every Booking must have a direct relationship to:

```text
Merchant
    ↓
Customer / CustomerContext
    ↓
Booking
```

A persistent customer account is not required.

Therefore:

```text
Booking requires CustomerRelationship

Booking does not require CustomerAccount
```

---

# 10. Booking availability

Booking availability answers:

> **Can the requested bookable subject, capacity or entitlement be reserved for the requested scope under current authoritative conditions?**

Conceptually:

```text
Requested subject
        +
Requested reservation scope
        +
Current capacity
        +
Existing allocations/bookings
        +
Applicable holds
        +
Resource constraints
        +
Applicable policies
        +
Applicable requirements
        ↓
Booking availability
```

This calculation is capability-specific.

---

# 11. Booking availability is not appointment-calendar availability

Example:

```text
Executive room
Friday → Sunday
```

may require evaluation of:

```text
room-category capacity
existing stays
allocation
checkout rules
date overlap
buffers
```

It does not require creation of:

```text
Friday 14:00 appointment
```

merely because check-in may occur at a particular time.

Hard invariant:

> **Booking availability shall not be forced through the Appointment Calendar model unless a separately registered Appointment genuinely exists.**

---

# 12. Automatic Booking is a normal operating mode

Where Main Street can determine authoritatively that the requested bookable subject is available and all applicable requirements are satisfied, a Booking may be created automatically.

Canonical flow:

```text
Customer chooses bookable subject
        ↓
Customer chooses reservation scope
        ↓
Main Street calculates Booking availability
        ↓
Customer sees available option
        ↓
Customer confirms selection
        ↓
Authoritative revalidation
        ↓
Applicable requirements satisfied
        ↓
Applicable capacity/allocation committed
        ↓
BOOKING CONFIRMED
```

No merchant intervention is required simply because a reservation is being created.

---

# 13. Automatic does not change the semantic type

Automation does not distinguish Booking from Appointment.

A Booking may be automatic.

An Appointment may also be established automatically after the customer selects a Main Street-calculated available time.

Therefore:

```text
AUTOMATIC
≠
BOOKING
```

and:

```text
MERCHANT-CONFIRMED
≠
APPOINTMENT
```

Automation is an execution mode.

Booking and Appointment are semantic types.

---

# 14. Booking must be authoritatively revalidated

A customer-visible available Booking option is not yet owned by the customer.

Therefore:

```text
BookingAvailable
    ≠
BookingConfirmed
```

Before commitment Main Street shall revalidate:

```text
current capacity
current allocations
current holds
current policies
current requirements
current relevant state
```

Only successful commitment establishes the Booking.

---

# 15. Booking concurrency

If two customers attempt to reserve the final available capacity concurrently:

```text
Customer A
Customer B
        ↓
same final capacity
```

Main Street shall preserve the applicable capacity invariant.

Only commitments that can validly coexist may succeed.

A stale availability result cannot authorise over-allocation.

---

# 16. Booking and Hold

Where temporary exclusivity is required before final Booking commitment, explicit `Hold` semantics defined by MS-PROT-006 shall be used.

```text
Booking option
    ≠
Hold
```

A displayed available room or resource is not automatically held.

---

# 17. Booking confirmation

A Booking becomes confirmed when the registered Booking commitment operation has successfully established the required reservation semantics.

Confirmation may depend on:

```text
availability
capacity
allocation
customer information
policy acceptance
payment
```

where registered.

No universal rule states that payment is always required.

---

# 18. Payment and Booking are distinct

Depending on registered policy:

```text
Booking may confirm before payment

Booking may require payment before confirmation

Booking may require deposit

Booking may use external payment later
```

Therefore:

```text
PAYMENT_SUCCESS
≠ universally BOOKING_CONFIRMED
```

The relationship must be explicitly defined by supported semantics.

---

# 19. Booking modification

A Booking may support capability-owned modification operations such as:

```text
change reservation dates
change quantity
change resource category
change reservation scope
```

A Booking modification is not called Appointment rescheduling unless a separate Appointment is actually involved.

---

# 20. Booking modification must preserve invariants

Suppose:

```text
Booking B100

Friday → Sunday
```

is changed to:

```text
Saturday → Monday
```

Main Street must authoritatively validate the new reservation scope before committing the modification.

The existing reservation does not automatically authorise the new scope.

---

# 21. Booking cancellation

Booking cancellation means:

> **The authorised termination of an existing reservation commitment while retaining the historical fact that the Booking existed.**

Therefore:

```text
CancelBooking
≠
DeleteBooking
```

---

# 22. Booking cancellation automatically releases its claim

When a Booking is authoritatively cancelled, every future reservation/capacity claim owned solely by that Booking shall be released automatically.

Canonical behaviour:

```text
Booking B100 cancelled
        ↓
reservation claim released
        ↓
allocation/capacity state updated
        ↓
Booking availability recalculated
        ↓
released capacity becomes available
to prospective customers
if current constraints permit
```

No merchant action such as:

```text
Make available
Reopen capacity
Release booking slot
```

shall be required.

---

# 23. Booking availability after cancellation is recalculated

Cancellation does not blindly set:

```text
available = true
```

Main Street recalculates current truth.

For example, released accommodation capacity may still be unavailable because:

```text
another allocation exists
another valid hold exists
resource removed from service
capacity configuration changed
```

Therefore:

> **Cancellation releases the cancelled Booking's claim; Main Street derives the resulting Booking availability from all remaining authoritative constraints.**

---

# 24. Appointment definition

An **Appointment** is:

> **A customer-related business commitment establishing an agreed time interval during which a supported service, consultation, visit or interaction is expected to occur.**

The defining semantic characteristic is the **agreed service interval**.

---

# 25. Appointment examples

Examples include:

```text
Solicitor consultation

Haircut

Mechanic service appointment

Dental appointment

Home service visit

Professional consultation

Spa treatment
```

The service may involve resources, staff, capacity or payment.

Those are additional semantics.

They do not redefine the Appointment.

---

# 26. Appointment minimum semantics

Conceptually:

```text
Appointment
{
    appointment_identity
    merchant_scope
    customer_relationship
    offering_or_service_context
    scheduled_interval
    lifecycle_state
    configuration_provenance
}
```

Where applicable it may also relate to:

```text
staff
resource
location
capacity
payment
remote meeting
notes
```

---

# 27. Appointment requires direct customer relationship

Every Appointment shall have:

```text
Merchant
    ↓
Customer / CustomerContext
    ↓
Appointment
```

A merchant calendar entry such as:

```text
"Dentist appointment"
```

with:

```text
intent = UNAVAILABLE
```

is therefore not an Appointment in Main Street semantics because it has no customer relationship.

---

# 28. Appointment does not require customer account

Valid appointment creation may originate from:

```text
guest website customer
telephone customer
walk-in customer arranging future service
merchant-entered customer
```

The required construct is merchant-scoped customer context, not universal account registration.

---

# 29. Appointment availability

Appointment availability answers:

> **Which appointment start times or intervals can currently support the relevant service under the active scheduling configuration and authoritative operational constraints?**

Conceptually:

```text
Working hours
        +
Recurring breaks
        +
Service duration
        +
Buffers
        +
Merchant UNAVAILABLE intents
        +
Existing Appointments
        +
Applicable staff/resource state
        +
Capacity
        +
Applicable policies
        ↓
Appointment availability
```

---

# 30. Customers do not calculate appointment validity

For customer-selectable appointments, customers shall not:

```text
enter arbitrary appointment times
drag calendar intervals
choose invalid times
choose merchant blocked periods
choose outside working hours
reason about staff/resource constraints
```

Main Street performs that work.

Hard invariant:

> **The customer may select only from Appointment availability calculated and presented by Main Street.**

---

# 31. Customer-facing Appointment availability must be streamlined

The customer should see:

```text
Choose a time

09:00
10:30
13:00
14:30
16:00
```

The customer should not need to see:

```text
09:30 unavailable because Staff A is busy
12:30 blocked by recurring break
14:00 unavailable because merchant has dentist appointment
17:30 outside working hours
```

Those constraints are internal to Main Street's calculation.

The customer sees **valid choices**, not the scheduling engine's reasoning.

---

# 32. Merchant-facing and customer-facing calendar information differ

The merchant may need explanations such as:

```text
Outside working hours

Regular break

Dentist appointment

Required resource unavailable

Existing customer appointment
```

The customer generally needs only the resulting availability.

Thus:

```text
Merchant experience
    → explain constraints

Customer experience
    → expose valid appointment choices
```

This preserves complexity compression.

---

# 33. Customer-selected Appointment flow

For:

```text
CUSTOMER_SELECTS_AVAILABLE_TIME
```

the flow shall be:

```text
Main Street calculates
Appointment availability
        ↓
Customer sees only available times
        ↓
Customer selects one
        ↓
Customer submits
        ↓
Main Street performs authoritative
revalidation
        ↓
Appointment committed
        ↓
APPOINTMENT CONFIRMED
```

No ordinary merchant approval is required.

---

# 34. Customer-selected Appointment shall not require artificial intermediate objects

The normal self-scheduling path does not require Main Street to create:

```text
SchedulingRequest
TimeProposal
MerchantApproval
```

merely for architectural symmetry.

The shortest valid path is preferred:

```text
Availability
    ↓
Selection
    ↓
Revalidation
    ↓
Appointment
```

---

# 35. Customer selection is not yet an Appointment

A customer selecting:

```text
14:00
```

does not create the Appointment until authoritative commitment succeeds.

Therefore:

```text
CustomerSelection
≠
Appointment
```

If revalidation fails because the slot was concurrently consumed:

```text
no Appointment is created
```

and Main Street presents updated availability.

---

# 36. Appointment availability is advisory

Consistent with MS-PROT-006:

```text
AVAILABLE APPOINTMENT TIME
    ≠
RESERVED
    ≠
HELD
    ≠
CONFIRMED APPOINTMENT
```

Displaying 14:00 does not grant the customer ownership of 14:00.

---

# 37. Appointment final revalidation

Immediately before commitment Main Street shall evaluate authoritative current state.

At minimum, as applicable:

```text
active configuration
working hours
recurring breaks
merchant unavailable intents
current Appointments
service duration
buffers
resource state
staff state
capacity
policy
requirements
```

Stale availability shall never authorise commitment.

---

# 38. Appointment concurrency

If two customers select the same final available appointment opportunity:

```text
Customer A → 14:00
Customer B → 14:00
```

and only one can validly occupy it:

```text
authoritative consistency boundary
        ↓
one succeeds
one fails
```

The unsuccessful customer receives recalculated alternatives.

---

# 39. Merchant-controlled Appointment scheduling

Some services require the merchant to determine or confirm the final appointment time.

For:

```text
MERCHANT_PROPOSES_OR_CONFIRMS
```

the customer does not gain direct calendar selection authority.

The flow may be:

```text
Customer requests service/appointment
        ↓
Merchant reviews Main Street Calendar
        ↓
Main Street identifies valid scheduling surface
        ↓
Merchant chooses valid time
        ↓
Main Street validates
        ↓
time offered to customer where acceptance required
        ↓
Appointment committed
```

---

# 40. Scheduling Request

A durable `SchedulingRequest` is justified only where the process requires waiting for merchant action.

It represents:

> A customer's request to arrange an Appointment where an agreed Appointment interval does not yet exist.

Conceptually:

```text
SchedulingRequest
{
    merchant_scope
    customer_context
    service_context
    relevant preferences
}
```

It is not an Appointment.

---

# 41. Scheduling Request does not reserve appointment time

Even if a customer says:

```text
Tuesday afternoon preferred
```

the request does not consume a slot.

If capacity protection is required while a merchant/customer decision remains outstanding, an explicit Hold is required.

---

# 42. Time Proposal

A `TimeProposal` exists where the merchant offers a particular valid interval to the customer before the Appointment becomes committed.

Example:

```text
Merchant proposes:
Tuesday 15:00–15:30
```

A proposal is not an Appointment.

---

# 43. Merchant may propose only valid time

The merchant's control does not permit bypassing scheduling configuration.

Before Main Street permits proposal of:

```text
Tuesday 15:00
```

it shall evaluate the applicable Appointment constraints.

The merchant-facing calendar may make invalid periods non-interactive.

---

# 44. Proposal does not automatically reserve appointment capacity

By default:

```text
TimeProposal
≠
Hold
```

If 15:00 must be protected while the customer decides, Main Street shall use explicit Hold semantics.

This prevents hidden capacity reservation.

---

# 45. Customer acceptance of a proposed Appointment

Where customer acceptance is required:

```text
TimeProposal
        ↓
Customer accepts
        ↓
authoritative revalidation
        ↓
Appointment committed
```

Acceptance itself does not override current scheduling reality.

## 45.1 Acceptance is durable customer-decision evidence

Once Main Street has authoritatively recorded customer acceptance, the `TimeProposal` remains `ACCEPTED` as historical evidence of that customer decision.

It shall not revert to `PROPOSED` merely because subsequent authoritative revalidation or commitment fails.

```text
Customer accepts proposal
        ↓
TimeProposal = ACCEPTED
        ↓
authoritative confirmation attempt
```

Acceptance still does not create an Appointment or allocate capacity by itself.

## 45.2 Failed confirmation after acceptance

If authoritative confirmation fails after acceptance—for example because the interval has become unavailable—Main Street shall:

```text
retain TimeProposal = ACCEPTED
retain SchedulingRequest as open/requested
record the failed confirmation attempt and precise reason
create no Appointment commitment
create no hidden allocation
permit a new proposal or another valid scheduling decision
```

The failed attempt is operational evidence distinct from both customer acceptance and Appointment state.

## 45.3 Successful confirmation resolves the request

Only successful authoritative commitment resolves the waiting scheduling request:

```text
accepted proposal
        ↓
revalidation succeeds
        ↓
Appointment committed
        ↓
SchedulingRequest = RESOLVED
        ↓
request, proposal and Appointment remain explicitly correlated
```

This preserves the distinction between what the customer decided and what Main Street successfully committed.

---

# 46. Appointment confirmation

An Appointment becomes confirmed when the registered operation has successfully established its agreed service-time commitment.

For customer-selected scheduling:

```text
selection
    ↓
revalidation
    ↓
confirmation
```

For merchant-proposed scheduling:

```text
proposal
    ↓
applicable acceptance
    ↓
revalidation
    ↓
confirmation
```

---

# 47. Appointment confirmation is distinct from notification

An Appointment may be confirmed even if:

```text
email delivery fails
SMS delivery fails
push notification fails
```

Notification is a downstream reaction.

It does not define Appointment existence.

---

# 48. Appointment confirmation is distinct from payment

Depending on registered policy:

```text
Appointment may confirm without payment

Appointment may require payment first

Appointment may require deposit

Appointment may permit later payment
```

No universal payment rule is introduced.

---

# 49. Appointment and Main Street Calendar

A confirmed Appointment that occupies scheduled time shall have a correlated Main Street Calendar representation as defined by MS-PROT-041.

Conceptually:

```text
Appointment A123
        ↓
Calendar representation
```

The calendar record remains a representation of the Appointment.

It is not the Appointment itself.

---

# 50. Appointment rescheduling

`RescheduleAppointment` means:

> **Change the agreed scheduled interval of an existing Appointment while preserving its business identity, unless explicitly registered semantics require replacement.**

Example:

```text
Appointment A123

10:00–11:00
        ↓
rescheduled
        ↓
15:00–16:00
```

Ordinarily:

```text
identity remains A123
```

---

# 51. Appointment rescheduling requires full validation

The target interval must be evaluated against current authoritative scheduling conditions.

The validity of the old interval provides no authority for the new one.

---

# 52. Rescheduling automatically releases the old appointment interval

When rescheduling succeeds:

```text
old interval claim
        ↓
released automatically

new interval
        ↓
committed automatically
```

The merchant does not manually reopen the old interval.

---

# 53. Old appointment time automatically re-enters availability when appropriate

Suppose:

```text
Appointment A123
10:00–11:00
```

moves to:

```text
15:00–16:00
```

Main Street shall recalculate both affected regions.

If 10:00–11:00 remains valid under:

```text
working hours
breaks
merchant unavailability
other appointments
resource/capacity
buffers
policies
```

it automatically becomes customer-visible Appointment availability again.

No merchant operation such as:

```text
Make 10:00 available
```

shall exist.

---

# 54. New appointment interval automatically leaves availability

After successful rescheduling:

```text
15:00–16:00
```

is occupied by the Appointment and therefore ceases to be presented as customer-selectable availability where the capacity is exclusive.

Again, no manual merchant update is necessary.

---

# 55. Appointment cancellation

Appointment cancellation means:

> **The authorised termination of an existing Appointment commitment while preserving the historical fact that the Appointment existed.**

Therefore:

```text
CancelAppointment
≠
DeleteAppointment
```

---

# 56. Appointment cancellation automatically releases the scheduled claim

Once cancellation commits:

```text
Appointment A123
        ↓
CANCELLED
        ↓
its future scheduling claim released
        ↓
affected Appointment availability recalculated
```

There shall be no separate:

```text
Release appointment slot
Make slot available
Reopen calendar
```

merchant operation.

---

# 57. Cancelled appointment time automatically becomes available when valid

Suppose:

```text
Appointment A123
Tuesday
14:00–15:00
```

is cancelled.

If no other applicable constraint prevents the interval from being used:

```text
14:00–15:00
        ↓
automatically returned to
customer-facing Appointment availability
```

This is mandatory behaviour.

---

# 58. Cancellation does not blindly make a time available

If the merchant has meanwhile created:

```text
UNAVAILABLE
13:30–15:30
title = "Dentist appointment"
```

then cancelling Appointment A123 shall not make:

```text
14:00
```

customer-visible.

The Appointment claim has been released, but the merchant unavailability remains.

Therefore:

```text
Appointment cancellation
        ↓
release Appointment claim
        ↓
recalculate
        ↓
UNAVAILABLE
```

---

# 59. Availability is derived, never merchant-maintained

Main Street shall not treat appointment slots as merchant-maintained Boolean state.

Rejected:

```text
slot.available = true
slot.available = false
```

as the authoritative availability model.

Instead:

```text
AppointmentAvailability =
    SchedulingConfiguration
    + CalendarScheduleIntent
    + ExistingAppointments
    + ResourceState
    + Capacity
    + Buffers
    + ApplicablePolicies
```

Similarly:

```text
BookingAvailability =
    BookableSubject
    + ReservationScope
    + ExistingBookings
    + Allocations
    + Holds
    + Capacity
    + ApplicablePolicies
```

Hard invariant:

> **Availability is a derived consequence of authoritative state, not a manually maintained merchant property.**

---

# 60. Automatic availability recomputation triggers

Main Street shall recompute affected availability when authoritative inputs change.

For Appointment availability, examples include:

```text
Appointment created
Appointment cancelled
Appointment rescheduled
merchant UNAVAILABLE created
merchant UNAVAILABLE moved
merchant UNAVAILABLE removed
working hours changed
recurring break changed
resource availability changed
capacity changed
```

For Booking availability:

```text
Booking created
Booking cancelled
Booking modified
Hold created
Hold expired
allocation released
capacity changed
bookable resource removed/restored
```

---

# 61. Merchant-created UNAVAILABLE remains distinct

A merchant may create:

```text
intent = UNAVAILABLE
title = "Dentist appointment"
14:00–15:30
```

This is not:

```text
Appointment
Booking
```

It has no customer relationship.

It blocks otherwise-valid Appointment availability.

---

# 62. Working hours and recurring breaks remain configuration

These are not:

```text
Appointments
Bookings
Calendar Events
```

for semantic purposes.

They define validity constraints for Appointment scheduling.

The merchant-facing calendar projects them as invalid scheduling regions with explanatory reasons.

---

# 63. Invalid and occupied remain distinct

Example:

```text
18:00
outside working hours
```

is:

```text
INVALID
```

Example:

```text
14:00
existing Appointment A123
```

is:

```text
OCCUPIED
```

Example:

```text
14:00
"Dentist appointment"
intent = UNAVAILABLE
```

is:

```text
UNAVAILABLE
```

These distinctions shall not be flattened.

---

# 64. Merchant cancellation versus calendar deletion

The merchant shall not cancel an Appointment merely by deleting its visual calendar representation.

Correct:

```text
Cancel Appointment
        ↓
Main Street cancellation semantics
        ↓
Appointment state changed
        ↓
capacity released
        ↓
availability recalculated
        ↓
calendar representation updated
```

The same principle applies to Booking interfaces where calendar representation exists.

---

# 65. Calendar failure does not change business commitment semantics

If an Appointment is successfully cancelled but Google Calendar update fails:

```text
Appointment = CANCELLED
```

remains authoritative.

Main Street must:

```text
release applicable scheduling claim
prevent incorrect availability calculations
reconcile calendar representation
```

Google Calendar failure must not resurrect the Appointment.

---

# 66. Missing calendar representation must not block automatic capacity release

Suppose an Appointment exists in Main Street but its calendar representation has become inconsistent.

If that Appointment is validly cancelled, Main Street shall still release the authoritative Appointment claim.

Calendar reconciliation remains a separate infrastructure concern.

---

# 67. Appointment completion

An Appointment may transition to completion according to its capability-owned semantics.

Clock passage alone shall not universally mean:

```text
COMPLETED
```

because some businesses may distinguish:

```text
completed
no-show
cancelled
in-progress
```

Only required states shall exist.

---

# 68. Booking completion/fulfilment is separate

A Booking's lifecycle need not resemble an Appointment lifecycle.

For example, accommodation may distinguish:

```text
confirmed
checked-in
checked-out
cancelled
```

according to its owning capability.

These states shall not be imported into Appointment merely because both are customer commitments.

---

# 69. No universal shared lifecycle

Rejected:

```text
PENDING
CONFIRMED
IN_PROGRESS
COMPLETED
CANCELLED
```

as a universal state machine for both Booking and Appointment.

Each owning semantic model defines only the states it actually requires.

Shared words may exist where their meanings genuinely coincide.

---

# 70. Booking and Appointment may relate explicitly

A Booking may legitimately relate to an Appointment.

Example:

```text
Hotel Booking B100
    │
    └── Spa Appointment A200
```

or future supported cases where a reserved resource also has scheduled service interactions.

Such relationships must be registered explicitly.

They shall not be inferred merely because the same customer is involved.

---

# 71. Booking does not implicitly create Appointment

A room Booking:

```text
Friday → Sunday
```

shall not automatically manufacture:

```text
Appointment Friday 15:00
```

for check-in unless Main Street explicitly models a separate Appointment semantic for that operation.

---

# 72. Appointment does not implicitly create Booking

A consultation Appointment:

```text
Tuesday 14:00
```

shall not automatically create a Booking object merely because scheduled capacity is consumed.

The Appointment itself owns the relevant service-time commitment.

Any resource/capacity claims are composed through explicit semantics.

---

# 73. Natural-language inference boundary

Inference may interpret merchant language such as:

```text
"Customers book rooms online."
```

as evidence for Booking.

It may interpret:

```text
"Customers choose a time for consultation."
```

as evidence for Appointment.

But inference shall not equate every occurrence of the word:

```text
book
booking
reserve
appointment
```

directly to a semantic type without considering registered meaning.

---

# 74. Domain events remain distinct

Where materially useful, committed outcomes may produce events such as:

```text
BookingConfirmed
BookingModified
BookingCancelled

AppointmentConfirmed
AppointmentRescheduled
AppointmentCancelled
```

The names shall preserve semantic distinction.

Avoid generic:

```text
ReservationChanged
```

unless an actual higher-level abstraction has justified use independently.

---

# 75. Booking cancellation event

If:

```text
BookingCancelled
```

is produced, it follows successful authoritative cancellation.

The event does not itself release capacity.

The capacity release is part of the committed business operation.

Events communicate the resulting fact.

---

# 76. Appointment cancellation event

Likewise:

```text
AppointmentCancelled
```

is emitted only after the Appointment cancellation has successfully committed.

Availability recomputation follows authoritative state change, not eventual receipt of an event required to make the cancellation true.

---

# 77. Customer simplicity invariant

The customer experience shall not expose platform complexity that Main Street can resolve deterministically.

For customer-selectable Appointment scheduling:

```text
Customer sees available times
Customer selects
Customer confirms
```

For automatic Booking:

```text
Customer sees available bookable option
Customer selects reservation scope
Customer confirms
```

Main Street performs:

```text
constraint evaluation
capacity checks
resource checks
conflict detection
authoritative revalidation
commitment
```

behind the interface.

---

# 78. Merchant simplicity invariant

The merchant manages:

```text
configuration
bookable subjects/resources
customer commitments
merchant temporary unavailability
```

The merchant does **not** manually maintain derived availability.

Thus:

```text
MERCHANT
    defines operational truth/intent

MAIN STREET
    derives availability
```

---

# 79. Falsification — automatic hotel Booking

Merchant has:

```text
Executive rooms
capacity = 5
```

Four are reserved for Friday night.

Customer requests one Executive room Friday → Saturday.

Main Street calculates:

```text
remaining capacity = 1
```

Customer confirms.

Authoritative revalidation succeeds.

Correct:

```text
BOOKING CONFIRMED
```

No merchant approval is required.

**PASS**

---

# 80. Falsification — last Booking capacity race

Two customers see the final Executive-room availability.

Both attempt confirmation.

Only one remaining unit exists.

Correct:

```text
authoritative capacity boundary
        ↓
one Booking succeeds
other fails
```

No overbooking unless an explicit overbooking policy exists.

**PASS**

---

# 81. Falsification — consultant self-scheduling

Consultant:

```text
09:00–17:00
break 12:00–13:00
30-minute consultation
```

Main Street computes:

```text
09:00
09:30
10:00
...
```

subject to current constraints.

Customer sees only valid times.

Customer chooses 14:30.

Correct result:

```text
APPOINTMENT
```

not:

```text
BOOKING
```

**PASS**

---

# 82. Falsification — customer attempts arbitrary Appointment time

Customer wants 18:30.

Merchant closes at 17:00.

Correct customer experience:

```text
18:30 is never presented
```

No need for:

```text
customer selects 18:30
        ↓
error message
```

in the ordinary self-scheduling flow.

**PASS**

---

# 83. Falsification — merchant-controlled solicitor

Customer requests consultation.

Merchant chooses from the Main Street-valid scheduling surface.

Merchant proposes 15:00.

Customer accepts where applicable.

Main Street revalidates.

Correct result:

```text
APPOINTMENT
```

No Booking is created.

**PASS**

---

# 84. Falsification — merchant's dentist event

Merchant creates:

```text
UNAVAILABLE
"Dentist appointment"
14:00–15:30
```

Correct:

```text
merchant schedule intent
```

not:

```text
Appointment
Booking
```

Customer no longer sees affected Appointment availability.

**PASS**

---

# 85. Falsification — Appointment cancellation

Appointment A100 occupies:

```text
10:00–11:00
```

Merchant cancels it.

No other constraint affects the interval.

Required:

```text
Appointment cancelled
        ↓
claim released automatically
        ↓
10:00–11:00 recalculated
        ↓
10:00 available to customers
```

No manual merchant reopening.

**PASS**

---

# 86. Falsification — Appointment cancellation with another constraint

Appointment A100 occupies:

```text
14:00–15:00
```

Merchant has also created:

```text
UNAVAILABLE
13:30–15:30
```

Appointment is cancelled.

Correct:

```text
Appointment claim released
```

but:

```text
14:00 remains unavailable
```

because merchant unavailability remains authoritative.

**PASS**

---

# 87. Falsification — Appointment rescheduling

Appointment moves:

```text
10:00 → 15:00
```

Correct:

```text
10:00 claim released
15:00 claim acquired
both regions recalculated
```

If 10:00 has no remaining conflict, it automatically returns to customer availability.

**PASS**

---

# 88. Falsification — Booking cancellation

Hotel Booking B100 consumes one room-category capacity unit.

Merchant cancels B100.

Correct:

```text
capacity released
Booking availability recalculated
released room capacity automatically
offered again if currently valid
```

No manual inventory reopening caused solely by cancellation.

**PASS**

---

# 89. Falsification — terminology collision

Customer UI says:

```text
Book consultation
```

Underlying operation establishes a specific service interval.

Correct semantic object:

```text
Appointment
```

The UI verb does not redefine the domain.

However, Main Street should prefer terminology that avoids this ambiguity where possible.

**PASS**

---

# 90. Falsification — hybrid hotel

Customer has:

```text
Booking B100
Executive room
Friday → Sunday
```

and:

```text
Appointment A200
Spa treatment
Saturday 14:00
```

Both coexist with separate lifecycles and availability calculations.

**PASS**

---

# 91. Falsification — calendar-only customer entry

Google Calendar contains:

```text
"Sarah consultation"
14:00
```

No Main Street Appointment/customer relationship exists.

Correct:

```text
integration inconsistency / orphan
```

not:

```text
Appointment
```

**PASS**

---

# 92. Rejected models

The following are explicitly rejected.

### 92.1 Booking and Appointment are interchangeable terms

Rejected.

### 92.2 Every Appointment is a Booking

Rejected.

### 92.3 Every Booking is an Appointment

Rejected.

### 92.4 Appointment extends Booking

Rejected as a universal semantic relationship.

### 92.5 Automatic commitment implies Booking

Rejected.

### 92.6 Merchant-confirmed commitment implies Appointment

Rejected.

### 92.7 Presence of time makes something an Appointment

Rejected.

### 92.8 Customer-facing word “book” determines Booking semantics

Rejected.

### 92.9 Customer may choose arbitrary Appointment times

Rejected for customer-selectable scheduling.

### 92.10 Merchant manually controls available Appointment slots

Rejected.

### 92.11 Cancellation requires merchant to reopen availability

Rejected.

### 92.12 Rescheduling requires merchant to reopen old availability

Rejected.

### 92.13 Availability is a persisted Boolean slot property

Rejected.

### 92.14 Calendar event is an Appointment

Rejected.

### 92.15 Calendar occupancy is automatically a Booking

Rejected.

### 92.16 Deleting a calendar representation equals cancellation

Rejected.

---

# 93. Accepted invariants

1. Booking and Appointment are distinct semantic constructs.
2. The terms shall be used distinctively throughout Main Street.
3. Booking represents reservation semantics.
4. Appointment represents an agreed service-time commitment.
5. Neither universally inherits from the other.
6. Both may reuse common semantic primitives through composition.
7. Both require a merchant-scoped customer relationship/context.
8. Neither requires a persistent customer account.
9. A Booking may be created automatically.
10. An Appointment may be created automatically after a valid customer time selection.
11. Automation does not determine semantic type.
12. Booking availability and Appointment availability are distinct calculations.
13. Booking availability is not forced through Appointment Calendar semantics.
14. Appointment availability derives from scheduling configuration and current operational state.
15. Customers may select only Main Street-calculated available Appointment times.
16. Customers do not need to understand why unavailable times are absent.
17. Merchant-facing calendar may expose scheduling reasons.
18. Displayed availability is not a commitment.
19. Final commitment always requires authoritative revalidation.
20. Booking concurrency must preserve reservation/capacity invariants.
21. Appointment concurrency must preserve scheduling/capacity invariants.
22. A SchedulingRequest is only required where merchant-controlled appointment coordination genuinely waits.
23. A SchedulingRequest does not reserve time.
24. A TimeProposal is not an Appointment.
25. A TimeProposal does not reserve time unless explicit Hold semantics apply.
26. Merchant-controlled Appointment scheduling cannot bypass Main Street constraints.
27. Booking confirmation remains distinct from payment.
28. Appointment confirmation remains distinct from payment.
29. Confirmation remains distinct from notification.
30. Booking modification validates the new reservation scope.
31. Appointment rescheduling validates the new service interval.
32. Ordinary Appointment rescheduling preserves Appointment identity.
33. Booking cancellation is not deletion.
34. Appointment cancellation is not deletion.
35. Booking cancellation automatically releases its reservation/capacity claim.
36. Appointment cancellation automatically releases its scheduling claim.
37. Appointment rescheduling automatically releases the old interval.
38. Main Street automatically recalculates affected availability after commitment changes.
39. Released availability automatically becomes customer-visible when current constraints permit.
40. Merchants do not manually reopen capacity solely because a Booking or Appointment was cancelled.
41. Merchants do not manually reopen an old Appointment interval after successful rescheduling.
42. Availability is derived rather than manually maintained.
43. Merchant `UNAVAILABLE` intent is neither Booking nor Appointment.
44. Working hours and recurring breaks are neither Booking nor Appointment.
45. Calendar records represent business scheduling information but do not define commitment semantics.
46. Google Calendar cannot manufacture Booking or Appointment semantics.
47. Booking and Appointment may coexist for the same merchant and customer.
48. Relationships between Booking and Appointment must be explicit where required.
49. Domain events preserve the semantic distinction.
50. Customer experience shall expose valid choices rather than Main Street's internal scheduling complexity.
51. Recorded proposal acceptance remains durable customer-decision evidence even if later confirmation fails.
52. Failed confirmation after acceptance leaves the SchedulingRequest open and creates no Appointment or allocation.
53. Failed confirmation records a precise attempt result rather than rewriting acceptance history.
54. A waiting SchedulingRequest becomes resolved only after authoritative Appointment commitment succeeds.
55. Successful commitment retains explicit correlation between request, accepted proposal and Appointment.

---

# 94. Deferred decisions

The following remain intentionally unresolved:

```text
exact Booking lifecycle states

exact Appointment lifecycle states

specific Booking capability catalogue

motel/allocation detail model

specific resource-booking representations

Booking payment-policy catalogue

Appointment payment-policy catalogue

proposal expiry defaults

explicit Hold policies for proposed Appointments

recurring Appointment semantics

group Appointment semantics

multi-customer Appointment semantics

waiting-list semantics

overbooking policy

late cancellation semantics

no-show semantics

refund consequences

staff substitution

resource reassignment

customer acceptance requirements for merchant rescheduling

booking modification rules by capability

appointment arrival/check-in semantics
```

Any future design must preserve the distinction established by this specification.

---

# 95. Governance review

## PROPOSE

The previous model was rewritten to establish a hard semantic boundary between Booking and Appointment and to simplify customer-facing scheduling. Revision 1.2 additionally separates durable customer acceptance from the success or failure of the resulting authoritative confirmation attempt.

**PASS**

## REVIEW / FALSIFICATION

The revised model was challenged against:

```text
automatic hotel reservation

final-capacity concurrency

customer self-scheduling

merchant-controlled scheduling

arbitrary customer time selection

merchant personal unavailability

Appointment cancellation

Appointment rescheduling

Booking cancellation

hybrid Booking + Appointment merchants

calendar-only customer records

payment differences

availability restoration
```

No case requires Booking and Appointment to collapse into one abstraction.

**PASS**

## VALIDATE

The model supports materially different Main Street merchants:

```text
online consultant
solicitor
salon
mechanic
hotel/motel
resource-rental operator
hybrid hospitality merchant
```

without business-category runtime branches.

**PASS**

## ACCEPT

The revised specification survives falsification, corrects the ambiguity present in MS-PROT-042 v1.0 and defines the accepted-but-uncommitted outcome left open by v1.1.

**ACCEPTED**

---

# 96. Canonical decision

> **Main Street uses Booking and Appointment as distinct semantic terms. A Booking reserves a supported bookable subject, resource, capacity or entitlement for a customer over an applicable reservation scope. A Booking may be committed automatically when Main Street authoritatively determines that the requested reservation is available and all applicable requirements are satisfied. An Appointment establishes an agreed time interval for a customer-related service or interaction. For customer-selectable Appointment scheduling, Main Street calculates current Appointment availability and presents only valid times; the customer selects from those times and Main Street authoritatively revalidates before commitment. Merchant-controlled Appointment scheduling similarly remains bounded by Main Street-calculated scheduling validity. Cancellation of either a Booking or Appointment automatically releases the claim owned by that commitment and causes affected availability to be recalculated. Successful Appointment rescheduling automatically releases the previous interval and commits the new one. Any released capacity or time becomes available to prospective customers automatically when all current constraints permit it. Availability is therefore derived by Main Street and is never manually reopened or maintained by the merchant.**

> **Where a customer accepts a proposed Appointment time, acceptance remains durable historical truth. If authoritative confirmation then fails, the proposal remains accepted, the scheduling request remains open, the precise failure is recorded, and no Appointment or allocation is created. The request resolves only when an Appointment is successfully committed and correlated to the request and accepted proposal.**

**MS-PROT-042 v1.2 is ACCEPTED.**

**MS-PROT-043 — Customer, Enquiry & Merchant-Customer Relationship Model** now defines the merchant-scoped customer relationship used by these Booking and Appointment contracts without making customer accounts mandatory.
