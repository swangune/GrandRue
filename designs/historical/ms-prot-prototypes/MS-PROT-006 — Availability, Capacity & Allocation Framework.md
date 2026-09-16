MS-PROT-006 — Availability, Capacity & Allocation Framework

Status: Proposed
Purpose: Define one general mechanism for determining, protecting and consuming business capacity across different merchant types without creating niche-specific availability engines.

This is probably the most technically sensitive part of Main Street.

The design principle is:

> Availability is a decision. Capacity is a constraint. Allocation is the authoritative act that consumes available capacity.

Consuming available capacity does not always mean destroying or physically
depleting the underlying resource. For quantity inventory, allocation reserves
available-to-promise; fulfilment records the later inventory movement that
reduces stock-on-hand.




---

1. The three concepts

They must remain separate.

Capacity

How much the business can fulfil.

Standard rooms = 8

or:

Mechanic appointments = 1 at a time

or:

10 grocery units in stock

Availability

Whether a requested operation can currently be fulfilled.

Standard room tonight → available
Oil change Tuesday 14:00 → available

Allocation

The authoritative act of assigning/consuming capacity.

Standard category
       ↓
Room 104 allocated

or:

14:00 appointment
       ↓
Mechanic capacity consumed


---

2. Availability is never ownership

This becomes a hard invariant.

AVAILABILITY RESULT
        ≠
CAPACITY OWNERSHIP

When a customer sees:

> Available



Main Street has not promised that capacity to them.

Only successful allocation/commitment does that.

This single principle eliminates a large class of race-condition problems.


---

3. The availability decision

Conceptually:

AvailabilityRequest
{
    offering
    requested_time
    duration
    quantity
    location
    customer_context
}

The availability engine evaluates:

Offering
   +
Capacity
   +
Schedule
   +
Existing commitments
   +
Active holds
   +
Resource conditions
   +
Merchant policies
   +
Time constraints
        ↓
Availability Decision

Result:

AvailabilityDecision
{
    available
    quantity
    alternatives
    reason
}


---

4. Why "reason" matters

If unavailable, the system should know why.

For example:

UNAVAILABLE
reason = outside_merchant_hours

or:

UNAVAILABLE
reason = capacity_exhausted

or:

UNAVAILABLE
reason = resource_unavailable

or:

UNAVAILABLE
reason = offering_disabled

This is useful internally and allows the presentation layer to explain the result appropriately without exposing internal mechanics.


---

5. Capacity models

We should support several capacity types rather than assuming everything is a physical resource.

A. Count capacity

Standard rooms = 8

B. Resource capacity

Room 101
Room 102
Room 103

C. Concurrent capacity

One mechanic can perform one job at a time.

D. Quantity capacity

12 bottles of product.

E. Time capacity

Consultant has 6 available hours.

F. Composite capacity

Two technicians
+
one workshop bay

The availability engine can combine these.


---

6. Motel

Motel example:

Offering:
    Standard room

Capacity:
    8

Resources:
    Room 101–108

Customer asks:

Standard
20 Aug → 22 Aug

The system calculates whether at least one suitable room can be allocated for the entire requested interval.

If yes:

AVAILABLE

The customer doesn't need:

Room 104

The allocation engine handles that internally.


---

7. Mechanic

Mechanic:

Offering:
    Oil change

Duration:
    60 minutes

Capacity:
    Technician availability

Schedule:
    Merchant opening hours

Customer requests:

Tuesday 14:00

Availability checks whether the required capacity exists across:

14:00 → 15:00

Not merely at 14:00.

This is critical for services.


---

8. Consultant

Structural engineer:

Consultation
Duration = 90 minutes

Availability becomes:

Can required consultation capacity
be provided continuously for 90 minutes?

The same framework works.

No "engineering consultant availability engine."


---

9. Grocery

Grocery is different.

Offering:
    Milk

Capacity:
    inventory = 24

Customer requests:

quantity = 3

Availability:

24 >= 3

Therefore:

AVAILABLE

Allocation reserves 3 units, so available-to-promise becomes:

24 → 21

Stock-on-hand remains 24 until fulfilment. Fulfilment then records a separate
inventory movement and reduces stock-on-hand from 24 to 21 while closing the
reservation.

The same semantic mechanism works, although the capacity dimension is quantity rather than time.


---

10. Realtor

A property viewing:

Offering:
    Property viewing

Capacity:
    Viewing availability

Resource:
    Agent

Duration:
    30 minutes

Availability becomes:

Agent available
+
property available for viewing
+
merchant open
+
requested time available

Again:

AVAILABLE

Same framework.


---

11. Schedule

Schedule is a constraint on availability.

We should support:

Merchant schedule
Offering schedule
Resource schedule
Appointment schedule

For example:

Merchant:
09:00–17:00

A mechanic appointment:

16:30–17:30

is unavailable if the merchant's policy does not permit it.


---

12. Buffers

Buffers are configuration, not hard-coded domain rules.

For example:

Service:
    duration = 60m
    preparation_buffer = 15m

Then:

14:00 appointment
consumes
14:00–15:15

For motel:

Expected checkout = 11:00
release buffer = 15m

But only where the merchant's configuration actually requires such a buffer.

Main Street must not assume:

> Every motel needs 15 minutes.




---

13. Capacity consumption

We need to distinguish:

CHECK

from:

CONSUME

Availability:

CHECK

does not change capacity.

Allocation:

CONSUME

does.

Here, CONSUME means that the capacity is no longer available to another
commitment. Its concrete effect depends on the capacity dimension:

- a time-window allocation occupies the relevant interval;
- a quantity allocation reserves available-to-promise;
- quantity fulfilment later records physical inventory depletion separately.

Therefore:

Availability query
    READ ONLY

while:

Allocation
    WRITE

This is architecturally clean.


---

14. The authoritative allocation transaction

At commitment:

BEGIN TRANSACTION

1. Re-evaluate relevant constraints
2. Verify capacity
3. Allocate capacity
4. Create/update commitment
5. Record the immutable domain event and its publication record
6. Record the handled command identity and stable result

COMMIT

If any step fails:

ROLLBACK

No partial commitment.

This is where race conditions are actually controlled.

The local business state, allocation, event publication record and handled
command result form the transactional core. Reactions outside that boundary,
such as notifications or remote integrations, run after commit and use
idempotent retry. Compensation is reserved for an already-committed external
effect that cannot be completed by retry; sequential best-effort local updates
are not permitted.


---

15. The critical race

Suppose:

capacity = 1

A and B both check:

available = true

Then:

A → allocation
B → allocation

The database/concurrency mechanism must ensure:

A → success
B → conflict

or vice versa.

Never:

A → success
B → success

The exact implementation—locking, serializable transaction, optimistic concurrency, atomic conditional update, etc.—belongs to the implementation design later.

But the domain requirement is already fixed:

> Allocation must be atomic with respect to the capacity it consumes.




---

16. We should not use a global availability lock

This would be an easy but poor design:

Any availability operation
        ↓
Lock Main Street

That destroys scalability.

Instead, concurrency must be scoped to the relevant capacity.

Conceptually:

Standard room capacity
        ↓
protect Standard room allocation

not:

protect entire merchant


---

17. Allocation strategies

We should define a small set of valid strategies.

Automatic

Capacity
   ↓
System selects resource

Motel:

Standard
   ↓
Room 104

Merchant

Customer requests category
   ↓
Merchant later assigns resource

Useful where automatic allocation isn't appropriate.

Customer-selected

Only where the merchant explicitly allows it.

Customer
   ↓
select specific resource

For our motel example, this is disabled.


---

18. Allocation should be invisible when possible

This is a core Main Street principle.

Customer:

Standard room

Internal:

Room 104

Merchant may see:

Standard
Room 104
Occupied

Customer does not need the underlying resource identity.

This lets Main Street simplify the customer experience without losing operational precision.


---

19. Resource lifecycle

A resource may have its own state.

Motel:

AVAILABLE
UNAVAILABLE
ALLOCATED

But the precise states are capability-defined.

A resource cannot be allocated if:

resource_state = unavailable

Staff explicitly changing a room:

UNAVAILABLE
      ↓
AVAILABLE

makes it eligible for future allocation.

Again:

Checkout does not automatically perform this transition.


---

20. Capacity vs resource state

This distinction prevents a subtle bug.

Suppose:

Standard capacity = 8

but one room is unavailable for maintenance.

We have:

Physical capacity = 8
Operational capacity = 7

Therefore:

Availability

must consider both:

declared capacity
+
currently usable capacity


---

21. Time-based capacity

For services, capacity is often a function:

Capacity(time interval)

rather than a static number.

For example:

09:00–12:00 → 2 technicians
12:00–13:00 → 0
13:00–17:00 → 2

Availability must evaluate the entire requested interval.

This means our model needs:

CapacityConstraint
    resource
    quantity
    interval


---

22. Composite capacity

Suppose a mechanic requires:

1 technician
+
1 workshop bay

Both must be available simultaneously.

Service request
      │
      ├── Technician capacity
      │
      └── Bay capacity

If either fails:

UNAVAILABLE

This is a powerful abstraction because it avoids creating a special "mechanic availability" engine.


---

23. Allocation groups

We therefore need the concept of an Allocation Set.

Conceptually:

Allocation
    ↓
Allocation Set
    ├── Resource A
    ├── Resource B
    └── Capacity constraint

Example:

Oil change
    requires:
        technician
        workshop bay

Allocation succeeds only when the complete set can be committed.


---

24. Alternative availability

If requested capacity isn't available, the availability engine should be able to produce alternatives.

Examples:

Requested:
Standard room
20 Aug

Alternatives:
Executive room
20 Aug

or:

Requested:
14:00

Alternatives:
15:00
16:00

or:

Requested:
Delivery tomorrow

Alternative:
Collection tomorrow

But alternatives are recommendations, not automatic substitutions unless the merchant policy permits it.


---

25. Important distinction: availability vs recommendation

Availability Engine
    ↓
What can be fulfilled?

Recommendation Engine
    ↓
What else might satisfy the customer?

Do not mix them.

Availability should remain deterministic.


---

26. The availability contract

Our conceptual contract becomes:

AvailabilityRequest
{
    offering
    interval?
    quantity?
    location?
    customer_context?
}

Result:

AvailabilityResult
{
    status
    available_quantity
    constraints[]
    alternatives[]
}

Where:

status =
    AVAILABLE
    UNAVAILABLE
    PARTIALLY_AVAILABLE

Whether PARTIALLY_AVAILABLE is needed for every capability should be capability-defined.


---

27. Allocation contract

Conceptually:

AllocationRequest
{
    commitment
    offering
    requested_capacity
    interval?
    strategy
}

Result:

AllocationResult
{
    success
    allocation_id
    allocated_resources[]
    conflict?
}

The allocation operation is authoritative.


---

28. Holds

We have already decided that holds are optional.

Therefore:

Hold capability = disabled

means:

Customer activity
    ↓
No capacity consumption

If enabled:

Customer activity
    ↓
Hold
    ↓
capacity temporarily protected

But Main Street does not impose a universal expiry duration.

Expiry comes from the configured mechanism/associated infrastructure.

This keeps the domain clean.


---

29. Payment interaction

We can now formally express the two models.

Fluid model

Interaction
   ↓
Availability check
   ↓
Payment
   ↓
Atomic allocation + commitment

Hold model

Interaction
   ↓
Atomic hold
   ↓
Payment
   ↓
Atomic commitment

The second is optional.


---

30. Motel final model

Our agreed motel now looks extremely simple:

MOTEL

Offerings
    Standard
    Executive

Capacity
    Standard = 8
    Executive = 4

Resources
    physical rooms

Booking
    customer selects category

Allocation
    automatic

Availability
    category-level

Room release
    staff action

Payment
    merchant policy

Hold
    optional, not implied

Customer sees:

Standard
Available

or:

Standard unavailable

Executive available

They never need room numbers.


---

31. Main Street's operational philosophy is now becoming explicit

We can state it as a design law:

> Expose the least amount of operational complexity necessary to complete the customer's intended action, while retaining sufficient internal state to operate the merchant's business correctly.



This is probably one of the strongest principles we have established.


---

32. MS-PROT-006 decisions

Decision	Status

Availability is a decision	Accepted
Capacity is a constraint	Accepted
Allocation is authoritative	Accepted
Availability does not consume capacity	Accepted
Allocation must be concurrency-safe	Accepted
Quantity allocation reserves available-to-promise rather than depleting stock-on-hand	Accepted
Quantity fulfilment records inventory movement separately	Accepted
Availability checks are advisory	Accepted
Final commitment revalidates availability	Accepted
Capacity can be count/time/resource/quantity/composite	Accepted
Resource state can constrain capacity	Accepted
Automatic allocation is a policy	Accepted
Customer resource selection is optional	Accepted
Customer need not see internal resources	Accepted
Buffers are configurable	Accepted
Universal motel/service buffers are rejected	Accepted
Holds are optional	Accepted
Universal 10-minute hold is rejected	Accepted
Payment does not imply hold	Accepted
Recommendation is separate from availability	Accepted
Local commitment, allocation, event publication and command result are one transactional core	Accepted
External reactions use retry first and compensation only where necessary	Accepted



---

33. Next layer

We have now dealt with the hardest part of the resource decision itself.

The next design is:

MS-PROT-007 — Interaction, Command & Event Framework

This will connect:

Customer action
       ↓
Interaction
       ↓
Command
       ↓
Capability
       ↓
Validation
       ↓
Availability / allocation
       ↓
Domain state
       ↓
Event
       ↓
Persistent interaction history

It will also formalise:

customer activity;

merchant activity;

cached activity;

commands;

domain events;

event ordering;

idempotency;

retries;

failed operations;

interaction correlation;

auditability;

notifications;

recovery after interruption.


This is where our "customer can pause and resume without starting from ground zero" principle gets its formal architecture.
