MS-PROT-018 — State, Transition & Transaction Model

Status: Proposed
Scope: Car-wash prototype
Design rule: No code implementation yet. We are specifying the lowest-level domain behaviour first.

This specification continues the state and transaction investigation from MS-PROT-016. The proposed MS-PROT-017 car-wash walkthrough was intentionally retired before publication because it added no necessary cross-domain evidence; this document has no normative dependency on it.


---

1. First correction: do not create one universal state machine

We have several different things moving through different lifecycles.

They must not share one giant status field.

Main Street will model at least these independently:

Customer Activity
Booking Request
Resource Allocation
Payment Condition
Booking Commitment
Service Execution
Resource

Their relationships are explicit.


---

2. Customer Activity

An activity represents what the customer has been doing, whether or not a business commitment exists.

ACTIVE
   │
   ├── updated
   ├── resumed
   ├── converted
   ├── abandoned
   └── expired

States

ACTIVE
CONVERTED
ABANDONED
EXPIRED

ACTIVE does not reserve scarce capacity.

This is fundamental.


---

3. Activity example

Customer selects:

Full Wash
Saturday
14:00
Vehicle: BMW

Main Street records:

Activity
    offering = Full Wash
    preferred_slot = Saturday 14:00
    vehicle = BMW

But it does not automatically consume the 14:00 wash-bay capacity.

Another customer can legitimately obtain that capacity.


---

4. Activity conversion

An activity becomes CONVERTED when it results in a business transaction/commitment.

ACTIVE
   │
   │ successful commitment
   ▼
CONVERTED

The activity is then historical context, not the operational authority.

The booking/commitment becomes authoritative.


---

5. Booking Request

A request represents the customer's attempt to obtain a service.

REQUESTED
   │
   ├── rejected
   ├── cancelled
   ├── superseded
   └── accepted

We should distinguish:

REQUESTED
ACCEPTED
REJECTED
CANCELLED
SUPERSEDED

However, depending on the framework, ACCEPTED may immediately result in a commitment.


---

6. Booking Commitment

A commitment is stronger than a request.

It means Main Street has recognised that the business obligation exists.

PENDING
   │
   ▼
CONFIRMED
   │
   ├── CANCELLED
   ├── COMPLETED
   └── ...

Potential states:

PENDING
CONFIRMED
CANCELLED
COMPLETED

The exact meaning of PENDING must be tightly constrained.

It cannot mean "we probably have the slot."


---

7. Resource Allocation

This is separate again.

A resource allocation means:

> This capacity has been committed to this operation.



ALLOCATED
   │
   ├── RELEASED
   └── CONSUMED

We should avoid unnecessary intermediate allocation states.

The system should not expose a fake state such as:

TEMPORARILY_ALLOCATED

unless a specific framework genuinely requires that semantic distinction.

Our car-wash model does not.


---

8. Payment Condition

Payment is not a booking state.

It is a condition associated with a transaction.

Possible semantic states:

NOT_REQUIRED
REQUIRED
PENDING
SATISFIED
FAILED
CANCELLED
REFUNDED

The payment provider remains authoritative for payment processing.

Main Street consumes payment events and applies its own business rules.


---

9. Service execution

Once the customer actually arrives:

SCHEDULED
   │
   ▼
IN_PROGRESS
   │
   ▼
COMPLETED

Potential exceptional state:

CANCELLED

This is distinct from booking cancellation.

A booking can be cancelled before service execution.

A service can also encounter operational failure after execution has begun.


---

10. Resource lifecycle

A wash bay has its own lifecycle.

AVAILABLE
   │
   ▼
ALLOCATED
   │
   ▼
AVAILABLE

But it may also become:

OUT_OF_SERVICE

Therefore:

AVAILABLE
ALLOCATED
OUT_OF_SERVICE

are resource states.

They should not be confused with booking states.


---

11. The complete relationship

Conceptually:

CUSTOMER
   │
   ▼
ACTIVITY
   │
   ▼
REQUEST
   │
   ├───────────────┐
   ▼               ▼
PAYMENT          ALLOCATION
   │               │
   └───────┬───────┘
           ▼
      COMMITMENT
           │
           ▼
     SERVICE EXECUTION
           │
           ▼
       COMPLETION

But this diagram is causal, not necessarily sequential.

For example, payment and allocation may need to participate in one carefully defined transactional protocol.


---

12. The critical transaction boundary

This is one of the most important decisions in Main Street.

We must never implement:

check availability
↓
wait
↓
allocate

as independent business operations where another transaction can intervene.

Instead, the runtime needs an atomic semantic operation equivalent to:

> Commit this requested business operation if all required conditions remain valid.



Internally that may involve database transactions, locks, serialisation, optimistic concurrency, or another mechanism.

We will specify the invariant now and choose implementation later.


---

13. Core invariant

For every resource R:

CommittedAllocation(R, interval)
    ≤
EffectiveCapacity(R, interval)

Always.

Not just normally.

Not eventually.

Always.

This invariant is non-configurable.


---

14. Capacity is multidimensional

This becomes important later.

A service might require:

1 wash bay
+
1 staff member

Therefore the allocation succeeds only if all required resources can be committed.

Example:

Bay 2 available
Staff A unavailable

The booking cannot claim success merely because the bay is available.


---

15. Composite allocation

We therefore define:

AllocationRequirement
    =
    set of resource requirements

For example:

FULL_WASH
    requires:
        WASH_BAY × 1
        STAFF_MEMBER × 1

The allocation operation must succeed or fail as a coherent unit.

We cannot safely allocate the bay and then discover that no staff exists.


---

16. Failed composite allocation

Suppose:

Bay 1 = available
Bay 2 = available

Staff A = unavailable
Staff B = unavailable

A request requiring both bay + staff must fail.

The system must not leave:

Bay 1 = mysteriously occupied

because the rest of the operation failed.

This is an important atomicity requirement.


---

17. Payment ordering

There are two competing concerns:

Payment

An external system may authorise/capture money.

Allocation

Main Street controls scarce business capacity.

We must not create a distributed transaction fantasy where Main Street assumes it can atomically roll back an external payment provider.

Therefore the eventual implementation must use an explicit transaction protocol.

The domain specification should not pretend these are one physical transaction.


---

18. Payment policy semantics

For:

PAYMENT_BEFORE_CONFIRMATION

the invariant is:

CONFIRMED
    →
PaymentCondition = SATISFIED

But this does not mean:

payment succeeded
    →
booking automatically confirmed

because resource availability and other business conditions still apply.


---

19. Payment succeeds while capacity disappears

This is an important failure case.

Customer pays.

Before the commitment can be completed, the requested resource is no longer available.

Main Street cannot simply say:

> "Payment succeeded, therefore booking succeeded."



Instead:

Payment = SATISFIED
Booking = NOT_CONFIRMED

The system must enter a defined compensation/recovery path.

Depending on the payment operation, that could involve:

refund
void
payment reversal
customer credit
merchant intervention

The exact mechanism is payment-provider-specific.

The semantic framework only defines the required business outcome.


---

20. This gives us a crucial invariant

> Payment satisfaction does not override resource-allocation invariants.



Likewise:

> Resource availability does not override payment prerequisites.



Both conditions must be satisfied where the merchant policy requires both.


---

21. No artificial ten-minute state

This confirms our earlier decision.

Main Street does not define:

TEMPORARY_ALLOCATION_10_MINUTES

as a universal state.

Instead:

customer activity

can persist independently.

Payment infrastructure manages its payment-session mechanics.

Resource allocation occurs when the business operation reaches its defined commitment boundary.


---

22. What if a customer pauses?

Customer:

selects Full Wash
selects 14:00
leaves

Activity:

ACTIVE

No scarce capacity is necessarily consumed.

Another customer can obtain 14:00.

When the first customer returns:

activity → ACTIVE

Main Street recalculates current availability.

If 14:00 is gone:

preferred slot = unavailable

but the rest of the activity remains.

The system offers alternatives.


---

23. This is better than "cart reservation"

We should avoid treating every customer interaction as a shopping cart.

The activity is essentially:

> resumable customer intent.



That is more general and works across:

booking
consultation
property viewing
accommodation
commerce


---

24. Activity persistence

An activity should contain enough information to reconstruct the customer's meaningful progress.

For example:

Activity
────────────────────
merchant
customer/session
offering
selected options
customer inputs
preferred time
location
vehicle information
last meaningful step
configuration version
created_at
updated_at

Notably:

resource allocation

does not belong here unless an actual allocation exists.


---

25. Configuration versioning

This is another requirement we should now formalise.

Suppose the merchant changes:

Full Wash duration
45 min → 60 min

Existing bookings must not silently reinterpret themselves under the new configuration.

Therefore a commitment must reference the applicable configuration/version.

Conceptually:

Booking
    configuration_version = V12

The merchant's new configuration becomes:

V13

Future operations use V13.

Existing commitments remain governed by their established semantics unless an explicit migration is performed.


---

26. This protects historical correctness

Without versioning, changing:

price
duration
resource requirements
cancellation policy
payment policy

could retroactively alter existing bookings.

That would be unacceptable.


---

27. Policy change example

Merchant changes:

PAYMENT_REQUIRED

to:

PAYMENT_NOT_REQUIRED

New bookings use the new configuration.

Existing confirmed bookings do not suddenly become unpaid bookings.

Again:

configuration version

is part of operational correctness.


---

28. State transition contract

Every transition should be represented conceptually as:

TRANSITION
{
    actor
    operation
    current_state
    preconditions
    effects
    resulting_state
    emitted_events
}

This is approaching the lowest level we need for the domain specification.


---

29. Example: confirm booking

Operation:
    CONFIRM_BOOKING

Preconditions:
    request is valid
    required payment condition satisfied
    required resources allocatable
    merchant policy permits confirmation

Effects:
    create/commit allocation
    create commitment
    mark request accepted/fulfilled
    emit BookingConfirmed

Result:
    commitment = CONFIRMED

If any mandatory precondition fails:

NO CONFIRMATION


---

30. Example: cancel booking

Operation:
    CANCEL_BOOKING

Preconditions:
    actor authorised
    booking is cancellable
    policy permits cancellation

Effects:
    booking → CANCELLED
    allocation → RELEASED
    emit BookingCancelled
    emit AllocationReleased

If payment has already occurred:

payment compensation

is initiated according to the payment policy.


---

31. Example: start service

Operation:
    START_SERVICE

Preconditions:
    booking = CONFIRMED
    scheduled conditions satisfied
    actor authorised

Effects:
    service → IN_PROGRESS
    emit ServiceStarted


---

32. Example: complete service

Operation:
    COMPLETE_SERVICE

Preconditions:
    service = IN_PROGRESS
    actor authorised

Effects:
    service → COMPLETED
    allocation → CONSUMED/RELEASED according to resource semantics
    emit ServiceCompleted


---

33. Illegal transitions

The model must explicitly reject:

CANCELLED → CONFIRMED
COMPLETED → IN_PROGRESS
COMPLETED → REQUESTED
ALLOCATED → ALLOCATED

unless a framework explicitly defines a legal compensating operation.

We should never rely on developers remembering which transitions "probably make sense."

The state machine itself defines legality.


---

34. Idempotency

This is essential for real systems.

Suppose a payment provider sends:

PaymentConfirmed

twice.

Main Street must not:

confirm booking twice
allocate two wash bays

The operation must be idempotent.

Likewise:

CancelBooking

received twice must not produce two independent cancellations.


---

35. Event identity

Every externally delivered event should have a stable identity.

Conceptually:

event_id
event_type
source
occurred_at
subject_id
payload

Main Street records whether the event has already been processed.

This becomes especially important for payment integration.


---

36. Events are facts, not commands

We should distinguish:

PaymentConfirmed

from:

ConfirmBooking

The first is a fact:

> Payment provider says payment was confirmed.



The second is an operation:

> Main Street is being asked to confirm a booking.



This distinction will prevent significant architectural confusion later.


---

37. Command → state → event

Our general operational pattern becomes:

COMMAND
   ↓
validate
   ↓
state transition
   ↓
persist result
   ↓
EVENT

External events follow a related pattern:

EXTERNAL EVENT
   ↓
validate identity/context
   ↓
apply domain consequence
   ↓
persist
   ↓
emit Main Street events if required


---

38. Merchant and customer commands

Examples:

Customer:
    REQUEST_BOOKING
    CANCEL_BOOKING
    RESCHEDULE_BOOKING
    MAKE_PAYMENT

Merchant:
    CHANGE_AVAILABILITY
    CANCEL_BOOKING
    START_SERVICE
    COMPLETE_SERVICE

Different actors can invoke different commands.

The domain validates them independently.


---

39. Rescheduling

Rescheduling must not be treated as:

cancel old booking
+
create unrelated new booking

from the domain's perspective.

It is a semantic operation:

RESCHEDULE_BOOKING

because the system needs to protect against accidentally releasing the old allocation before securing the new one.


---

40. Safe rescheduling

Conceptually:

existing allocation
       │
       ▼
validate new allocation
       │
       ├── failure → old booking unchanged
       │
       ▼
commit new allocation
       │
       ▼
release old allocation
       │
       ▼
booking updated

This prevents the customer from losing their original booking merely because their desired alternative was unavailable.


---

41. This is another composite transaction

Rescheduling demonstrates why our primitive model must support atomic composition.

We have:

ALLOCATE_NEW
+
RELEASE_OLD
+
UPDATE_COMMITMENT

as one semantic operation.

This is a perfect example of an abstract primitive/framework operation built from lower-level primitives.


---

42. The hierarchy is becoming clearer

Primitive

ALLOCATE
RELEASE
CREATE
UPDATE
TRANSITION

Abstract primitive

RESERVE_CAPACITY
BOOK_RESOURCE
RESCHEDULE

Framework operation

RESCHEDULE_SERVICE_BOOKING

Merchant capability

RESCHEDULING

This validates your "Scratch" analogy while preventing us from exposing the raw blocks to merchants.


---

43. The merchant sees one switch

For example:

> Allow customers to reschedule bookings?



ON

Internally, Main Street may activate:

RESCHEDULING capability
    ↓
framework operations
    ↓
resource reallocation
    ↓
policy evaluation
    ↓
customer operation
    ↓
merchant operation

The merchant never sees that complexity.


---

44. Validation result

MS-PROT-018 now establishes the essential low-level rules:

✓ Activities are not commitments
✓ Availability is not allocation
✓ Allocation is not payment
✓ Payment is not confirmation
✓ Confirmation is not service execution
✓ Resource state is independent
✓ State transitions are explicit
✓ Illegal transitions are rejected
✓ Composite allocation is atomic
✓ Capacity invariant is universal
✓ External events are idempotent
✓ Configuration is versioned
✓ Rescheduling is atomic
✓ Merchant configuration remains simple


---

45. New invariant set

We should formally carry these into the master validation standard.

INV-001 — Capacity

Committed allocation cannot exceed effective capacity.

INV-002 — Payment

A payment-required commitment cannot become confirmed without satisfied payment conditions.

INV-003 — Authorization

Every state-changing operation requires an authorised actor.

INV-004 — State legality

Only explicitly defined state transitions are permitted.

INV-005 — Idempotency

Repeated processing of the same operation/event cannot create duplicate business effects.

INV-006 — Version integrity

Existing commitments remain governed by the configuration under which they were established.

INV-007 — Composite atomicity

A multi-resource allocation either succeeds coherently or produces no partial committed allocation.

INV-008 — Activity independence

Uncommitted customer activity does not consume scarce capacity.

These are platform invariants, not merchant policies.


---

46. One remaining major question

We have now gone very deep into transactional behaviour.

The next thing we must settle before proceeding is the semantic type system itself.

We need to define precisely what Main Street means by:

Primitive
Abstract Primitive
Framework
Capability
Policy
Rule
Requirement
Resource
Offering
Actor
Operation
State
Event
Invariant
Configuration

and, critically:

> Which of these can contain/combine which others?



That is the equivalent of defining the grammar and type system of our internal language.

Without it, the AI agents could still compose semantically invalid configurations.


---

Next: MS-PROT-019 — Main Street Semantic Type System & Composition Rules

This is the next logical level.

We will design the "programming language" behind Main Street—not executable code, but its domain grammar:

PRIMITIVE
    ↓
ABSTRACT PRIMITIVE
    ↓
CAPABILITY
    ↓
FRAMEWORK
    ↓
POLICY / RULE
    ↓
MERCHANT CONFIGURATION

including typing rules, composition rules, dependency rules, inheritance/abstraction rules, validation rules, forbidden compositions, and how AI agents are constrained to select and compose only existing constructs.

That is the piece that turns your "pixels → image" analogy into an actual engineering architecture.
