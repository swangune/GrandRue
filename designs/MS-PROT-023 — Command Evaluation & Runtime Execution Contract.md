# MS-PROT-023 — Command Evaluation & Runtime Execution Contract

**Version:** 1.1  
**Status:** Proposed → falsification reviewed → revised → validated → **Accepted**  
**Depends on:** MS-PROT-020, MS-PROT-021, MS-PROT-022

This document defines what happens when something attempts to change Main Street's operational state.

It builds directly on the prototype concepts already established:

```text
Actor
OperationRequest
Operation
ResourceInstance
OperationRuntime
OperationExecution
```

But this is an architectural contract, not an endorsement of the current Java classes as the final implementation.

---

## 1. Problem

We now have:

```text
Registered Semantics
        ↓
Merchant Configuration
        ↓
Resolved Operational Model
        ↓
        ?
        ↓
Operational State
```

The missing boundary is:

> **Given an actor's intent, current operational state, and the merchant's resolved model, how does Main Street determine whether an operation may execute and what authoritative result it produces?**

---

# 2. Proposed model

The first proposal is:

```text
Command
   │
   ▼
Command Evaluation
   │
   ├── resolve operation
   ├── establish actor/context
   ├── establish target
   ├── evaluate authority
   ├── evaluate requirements
   ├── evaluate policies
   ├── evaluate current state
   └── evaluate operation invariants
   │
   ▼
Execution Decision
   │
   ├── REJECT
   │
   └── AUTHORISE
          │
          ▼
       Execute
          │
          ▼
   State Transition
          +
   Execution Fact
          +
   Domain Events
```

The central rule is:

> **A command expresses requested intent. It is not authority to mutate state.**

---

# 3. Command

A Command represents an attempt to perform a registered Operation.

Conceptually:

```text
Command
{
    operation
    actor/context
    target
    input
}
```

For example:

```text
ConfirmOrder
CancelAppointment
AcceptQuote
MarkMealReady
CheckInGuest
CompleteJob
```

A Command does not contain executable behaviour.

It carries intent and required input.

---

# 4. Commands are not business-specific classes by necessity

We should not assume:

```text
GardenerCompleteJobCommand
RestaurantMarkMealReadyCommand
MotelCheckInGuestCommand
```

must each have bespoke execution infrastructure.

The semantic Operation identifies the intended behaviour.

Conceptually:

```text
Command
    operation = job.complete
    target = job-123
```

However, MS-PROT-023 does **not** require one universal physical `Command` Java class. Typed command representations remain possible where they provide compile-time or boundary safety.

The semantic contract is what must remain generic.

---

# 5. Actor is broader than staff

The existing prototype's `Actor` is useful but too easy to interpret as employee identity.

An operation may originate from:

```text
merchant
staff
customer
system
integration
scheduled process
AI-assisted process
```

Therefore the architectural concept is:

> **Execution Principal**

rather than specifically "staff member."

The current `Actor` may remain an implementation name during prototyping.

---

# 6. Authentication is not authorisation

MS-PROT-023 explicitly separates:

```text
Authentication
    Who/what is this?

Authorisation
    May this principal perform this operation?
```

A customer being logged in does not imply:

```text
customer may cancel any appointment
```

Likewise, a valid staff session does not imply:

```text
staff may issue refunds
```

Runtime evaluates authority against the requested operation and context.

---

# 7. Target resolution

Before mutation, runtime must establish the authoritative target.

Conceptually:

```text
Command target reference
        ↓
ResourceInstance
        ↓
verify:
    merchant ownership
    resource semantics
    existence
    current state
```

The command cannot supply an arbitrary mutable `ResourceInstance` and thereby establish authority over it.

The current prototype passes an object directly because it is an in-memory semantic experiment.

The production contract must not infer trust from object possession.

---

# 8. Evaluation contract

Before an operation may mutate authoritative state, runtime evaluates all applicable constraints.

Conceptually:

```text
evaluate(command, model, currentState)
```

must establish at least:

```text
1. operation exists in resolved model
2. target exists
3. target belongs to correct merchant scope
4. target is compatible with operation
5. principal has applicable authority
6. required input is satisfied
7. applicable policy permits execution
8. current state permits execution
9. relevant invariants hold
```

Only then may mutation occur.

---

# 9. Evaluation must not mutate state

This becomes a hard invariant:

> **Evaluation is observational. Execution is mutational.**

Therefore:

```text
evaluate
    ↓
failure
```

must leave authoritative operational state unchanged.

This generalises the prototype invariant already tested:

```text
failed_execution_never_changes_instance_state
```

---

# 10. Requirements

MS-PROT-022 established that some Requirements are statically applicable while others depend on runtime context.

Therefore command evaluation receives:

```text
Resolved Requirement Semantics
        +
Command Context
        +
Supplied Input
```

and determines applicable Requirements.

Example:

```text
fulfilment = DELIVERY
        ↓
DeliveryAddress applicable
        ↓
address supplied?
```

But:

```text
fulfilment = COLLECTION
```

does not fabricate a meaningless delivery-address requirement.

---

# 11. Requirement applicability and satisfaction remain distinct

The evaluation pipeline therefore contains two steps:

```text
Determine applicable Requirements
                ↓
Determine whether each is satisfied
```

This matters because:

```text
Requirement applicable
```

is semantic truth, while:

```text
Requirement satisfied
```

depends on runtime data.

---

# 12. Policy evaluation

Policies modify permitted behaviour within registered semantic bounds.

Example:

```text
CancellationPolicy
    customer cancellation allowed until X
```

Runtime may evaluate the effective policy against:

```text
current time
appointment state
principal
merchant configuration
```

But the policy cannot introduce an unregistered Operation.

Therefore:

```text
Policy
    constrains Operation
```

not:

```text
Policy
    invents Operation
```

---

# 13. State evaluation

An Operation may only execute from semantically valid source state/context.

The prototype currently demonstrates:

```text
inactive
   ↓ activate
active
```

and rejects activation when the instance is already `active`.

That remains correct at the architectural level.

However, not every Main Street Operation necessarily needs to be modelled as a simple single-resource state transition.

That assumption must now be challenged.

---

# FALSIFICATION REVIEW

We now attempt to destroy the proposal rather than demonstrate that it works.

---

# 14. Falsification attempt — Does every command target one ResourceInstance?

Consider:

```text
Transfer stock
    from Store A
    to Store B
```

or:

```text
Move appointment
    from Instructor A
    to Instructor B
```

or:

```text
Allocate room
    booking + room
```

or:

```text
checkout
    order + inventory + payment
```

These may affect multiple authoritative entities.

If MS-PROT-023 requires:

```text
Command → exactly one ResourceInstance
```

the abstraction fails.

### Falsification successful.

The proposal must be revised.

---

# 15. Revision — execution scope

A Command has an **execution scope**, not necessarily exactly one mutable resource.

Conceptually:

```text
Command
    │
    ├── primary subject
    └── participating authoritative state
```

For a simple operation:

```text
activate product
    subject = product-001
```

For allocation:

```text
allocate room
    subject = booking-123
    participant = room-101
```

For stock movement:

```text
transfer stock
    subject = stock movement
    participants:
        source inventory
        destination inventory
```

This does **not** mean every Command can arbitrarily mutate unlimited resources.

The registered operation semantics determine its legitimate execution scope.

---

# 16. Falsification attempt — Can evaluation and execution safely be separate?

Suppose:

```text
T1: evaluate Room 101 → available
T2: another command allocates Room 101
T3: first command executes
```

The original proposal would allow double allocation.

Therefore:

```text
evaluate
    ↓
authorised
    ↓
later execute
```

cannot by itself guarantee correctness.

### Falsification successful.

---

# 17. Revision — authoritative revalidation

The decision that permits mutation must be made against authoritative state within the consistency boundary of execution.

Conceptually:

```text
preliminary evaluation
        ↓
execution boundary
        ↓
authoritative validation/revalidation
        ↓
mutation
```

Therefore:

> **A stale evaluation result is never sufficient authority for mutation.**

Implementation might eventually use:

```text
database transaction
optimistic concurrency
locking
compare-and-set
version check
```

MS-PROT-023 does not choose one yet.

It establishes the invariant.

---

# 18. Falsification attempt — Is one state transition enough?

Consider checkout:

```text
Order:
    pending → confirmed

Inventory:
    available → allocated

Payment:
    pending → authorised
```

If the command contract insists every successful operation produces exactly:

```text
one ResourceInstance
one source state
one target state
```

the model fails.

### Falsification successful.

The current prototype's `OperationExecution` is therefore a valid **minimal single-resource execution fact**, but not a sufficient universal architecture.

---

# 19. Revision — Execution Result

The architectural result becomes:

```text
ExecutionResult
{
    command identity/context
    operation
    principal
    effects
    produced facts/events
}
```

An effect may conceptually describe:

```text
resource X:
    state A → state B
```

but an execution may contain more than one effect.

This allows simple commands to remain simple while supporting legitimate atomic multi-entity operations.

---

# 20. Falsification attempt — Must every operation change state?

Consider:

```text
record enquiry
add note
send message
capture measurement
record payment reference
```

Some operations may create information rather than transition an existing resource through a lifecycle.

If `Operation == StateTransition`, Main Street would be forced to manufacture artificial states.

Example:

```text
note:
    nonexistent → written
```

This is semantic distortion.

### Falsification successful.

---

# 21. Revision — Transition is one possible effect

Therefore:

> **An Operation represents permitted business action; a State Transition is one possible semantic effect of an Operation, not the universal definition of Operation.**

This is a significant finding.

The current prototype:

```java
Operation {
    Transition transition;
}
```

is therefore suitable for testing the transition subset of the semantic kernel but must **not yet be promoted to the universal Main Street Operation model**.

We should not change the code now because we are deliberately focusing on design.

But this finding must govern later implementation.

---

# 22. Falsification attempt — Does every operation require a human principal?

Consider:

```text
payment provider webhook
appointment reminder
automatic hold expiry
scheduled publication
inventory release after cancellation
```

There may be no human Actor.

If the architecture requires a human identity, it fails.

### Already prevented

Execution Principal includes:

```text
SYSTEM
INTEGRATION
```

etc.

But we need an additional invariant:

> Every authoritative mutation must have an attributable execution principal/origin, even where that principal is the Main Street system or an authenticated integration.

This preserves auditability.

---

# 23. Falsification attempt — Can system authority bypass normal semantics?

Suppose:

```text
principal = SYSTEM
```

automatically means unrestricted authority.

Then any background process could mutate arbitrary state.

The model fails its own authorisation boundary.

### Revision

`SYSTEM` is an identity/origin category, not omnipotence.

System operations still require registered authority.

---

# 24. Falsification attempt — Customer owns command target?

Suppose customer A discovers:

```text
/order/1234
```

belonging to customer B.

Both belong to the same merchant.

Merchant-scope validation alone would pass.

Therefore:

```text
target belongs to merchant
```

is insufficient.

### Falsification successful.

Evaluation must support contextual relationship authority such as:

```text
customer is party to order
staff belongs to merchant
agent assigned to case
resource belongs to merchant
```

without assuming simple role-based access control is enough.

---

# 25. Revised authority model

Authority conceptually depends on:

```text
Principal
    +
Operation
    +
Merchant Scope
    +
Target/Execution Scope
    +
Relevant Relationships
    +
Effective Policy
```

Therefore:

```text
hasPrivilege("order.cancel")
```

may be necessary but is not universally sufficient.

The existing prototype's privilege model remains useful for proving basic operation authority, but production authorisation must support contextual constraints.

---

# 26. Falsification attempt — What if a command is submitted twice?

Examples:

```text
customer double-clicks Pay
payment provider retries webhook
mobile network retries request
staff taps Complete twice
```

If each valid request independently mutates state, duplicate effects may occur.

The simple state transition prototype sometimes protects itself because the second transition becomes invalid.

But this does not protect:

```text
charge payment
send message
create booking
create order
```

### Falsification successful.

---

# 27. Revision — command identity and duplicate handling

Authoritative mutation infrastructure must support distinguishing logically repeated submission where duplicate execution would be unsafe.

We should **not** require universal idempotence for every Operation.

Instead:

> Operations with externally retryable or duplicate-sensitive execution must define an appropriate duplicate-execution strategy.

Possible mechanisms later include:

```text
command identifier
idempotency key
external transaction reference
natural uniqueness constraint
state-based rejection
```

The architecture does not select the mechanism yet.

---

# 28. Falsification attempt — External side effects inside atomic execution

Consider:

```text
confirm order
    ↓
charge external payment provider
    ↓
update database
```

A database transaction cannot atomically control the external payment provider.

If MS-PROT-023 promises:

```text
all operation effects are atomic
```

the promise is impossible.

### Falsification successful.

---

# 29. Revision — authoritative state atomicity versus external effects

We distinguish:

```text
authoritative Main Street state changes
```

from:

```text
external side effects
```

The runtime must preserve the invariants of its authoritative state boundary.

External effects may require later patterns such as:

```text
outbox
workflow
saga/process manager
provider idempotency
reconciliation
```

No mechanism is selected here.

Therefore MS-PROT-023 does **not** claim distributed atomicity.

---

# 30. Falsification attempt — What if external action succeeds but Main Street crashes?

Example:

```text
payment provider:
    payment succeeds

Main Street:
    crashes before recording confirmation
```

The system may temporarily disagree with the provider.

This proves again that:

```text
Command → atomic everything
```

cannot be our architecture.

The later integration/reconciliation design must handle externally authoritative facts.

**Proposal survives after the previous revision.**

---

# 31. Falsification attempt — Can notification failure invalidate business execution?

Suppose:

```text
appointment confirmed
email notification fails
```

If notification is part of the command's atomic success criteria, appointment confirmation may incorrectly fail.

But if the business invariant says notification is legally required before confirmation, perhaps it matters.

Therefore universal:

```text
notifications always asynchronous
```

also fails.

### Finding

Effects need semantic ownership and consistency classification.

Usually:

```text
Appointment confirmation
    authoritative business mutation

Notification
    reaction to resulting fact
```

But the architecture must permit a capability to define a genuine synchronous prerequisite when required.

This reinforces our earlier orchestration rule:

> Consistency requirement determines coordination mechanism; the generic runtime must not assume all capability collaboration is synchronous or asynchronous.

---

# 32. Falsification attempt — What if configuration changes during a command?

Model A permits cancellation.

Model B disables it.

Command begins under A while B activates.

Which applies?

MS-PROT-022 established:

> a command executes against one coherent resolved operational model.

Therefore runtime must bind execution to a coherent model context.

However, that alone doesn't settle long-running external workflows.

A command taking milliseconds is straightforward.

A process lasting hours cannot simply retain an obsolete model indefinitely.

### Finding

MS-PROT-023 covers **command execution**, not long-running business processes.

Long-running process semantics require a separate model.

Proposal survives because we constrain scope rather than force Command to represent workflow.

---

# 33. Falsification attempt — Can Command represent a quotation lifecycle?

Consider gardener:

```text
enquiry
inspection
quotation
customer acceptance
scheduling
work
completion
```

Trying to represent this entire sequence as one Command would make execution long-lived and stateful.

That breaks the command abstraction.

Instead:

```text
RequestInspection
RecordInspection
IssueQuote
AcceptQuote
ScheduleJob
StartJob
CompleteJob
```

are separate operations around persistent business state.

Therefore:

> **Command is a bounded attempt to perform an operation, not a business process.**

Survives.

---

# 34. Falsification attempt — Solicitor operation without customer account

A client receives an ICS appointment by email and never has an account.

Can runtime still execute:

```text
ConfirmAppointment
```

Yes.

The principal performing confirmation may be staff.

The client is a party/contact, not necessarily an authenticated Actor.

This proves:

```text
business participant
        ≠
execution principal
```

The model must preserve this distinction.

Survives.

---

# 35. Falsification attempt — Anonymous customer creates enquiry

Gardener website:

```text
name
email
message
address
submit
```

No authenticated customer exists.

Who is the principal?

If every command requires authenticated identity, the design fails.

### Revision

Execution origin can include:

```text
ANONYMOUS_INTERACTION
```

where the registered operation explicitly permits it.

The request still receives traceable technical context, but anonymous interaction is not transformed into a fake customer account.

---

# 36. Falsification attempt — AI performs operation

Suppose an AI agent notices an appointment conflict and attempts rescheduling.

Can:

```text
AI
```

become semantic authority?

No.

The operation must execute under explicitly delegated system/merchant authority and applicable policies.

AI can propose or initiate within its granted authority envelope.

It cannot bypass the command contract.

Survives.

---

# 37. Falsification attempt — Read operations

What about:

```text
view schedule
track order
view purchase history
```

These are operational interactions but do not mutate state.

Should every read produce an `ExecutionResult` and state effects?

No.

That would overload the mutation command model.

### Scope correction

MS-PROT-023 governs **authoritative operation execution that may produce state/effects**.

Query/read semantics need their own access/projection contract.

This preserves:

```text
Command
    ≠
Query
```

A future read-model design should handle customer tracking and dashboards.

---

# 38. Falsification attempt — Operation with no persistent mutation but authoritative fact

Consider:

```text
send_message
```

It might create an outgoing message record, so persistence exists.

But there may be operations whose principal outcome is an emitted integration request.

Can they fit?

Yes, provided the authoritative effect is represented appropriately.

The contract does not require lifecycle state transition after Revision 21.

Survives.

---

# 39. Falsification attempt — Two simultaneous valid bookings

This is one of the most important tests.

```text
Customer A → book 10:00
Customer B → book 10:00
```

Both evaluate against:

```text
10:00 available
```

If runtime simply performs application-level checks followed by writes, double booking occurs.

MS-PROT-023 therefore requires:

> **Invariants vulnerable to concurrent execution must be enforced at an authoritative consistency boundary, not solely by pre-execution application checks.**

This may later require:

```text
unique constraint
optimistic concurrency
serialisation
locking
capacity ledger
```

depending on the invariant.

The proposal survives only with this explicit constraint.

---

# 40. Falsification attempt — Quantity greater than one

Restaurant has:

```text
10 portions available
```

Five concurrent orders each request three.

Simple:

```text
available/unavailable
```

cannot represent the invariant.

But this does not falsify the Command contract.

It falsifies any assumption that all resources use binary states.

Command evaluation can operate against quantity/capacity semantics owned elsewhere.

Survives.

---

# 41. Falsification attempt — Privilege revoked mid-command

Staff is authorised at evaluation.

Merchant revokes privilege immediately afterwards.

If mutation occurs later based on stale authorisation, revoked authority may still execute.

The earlier revalidation rule covers operational state but must explicitly include **security-sensitive authority state** where relevant.

Revision:

> Final execution authorisation must be evaluated against authoritative security context appropriate to the operation's consistency requirements.

Survives.

---

# 42. Falsification attempt — Audit evidence fabricated by caller

Suppose Command supplies:

```text
actor = merchant
timestamp = yesterday
```

and runtime trusts it.

Auditability collapses.

Therefore authoritative execution metadata such as:

```text
execution time
authenticated principal
merchant context
internal command identity
```

must be established by trusted platform boundaries, not accepted blindly from merchant/customer input.

Survives after constraint.

---

# 43. Falsification attempt — Events emitted before commit

Runtime:

```text
emit OrderConfirmed
        ↓
database transaction fails
```

Notification sees an order confirmation that never happened.

Therefore:

> Domain facts/events representing committed authoritative change must not become externally observable as committed facts before the underlying authoritative mutation succeeds.

Implementation mechanism is deferred.

This will matter when we design persistence/events.

---

# 44. Falsification attempt — Partial multi-resource mutation

Suppose:

```text
booking updated
room allocation fails
```

If both are required for the same Main Street invariant, partial success is invalid.

Therefore registered operation semantics must establish the required consistency boundary.

The runtime cannot blindly perform a list of effects sequentially.

Survives with the authoritative consistency requirement established earlier.

---

# Revised execution model

After attempted falsification, the original simple model:

```text
Command
   ↓
validate
   ↓
transition one ResourceInstance
   ↓
ExecutionFact
```

does **not survive**.

It is discarded as the universal architecture.

The surviving model is:

```text
             COMMAND
                │
                ▼
        Establish trusted context
                │
        ┌───────┴────────┐
        ▼                ▼
   Principal         Merchant scope
        │                │
        └───────┬────────┘
                ▼
        Resolve registered
           Operation
                │
                ▼
        Resolve execution scope
                │
                ▼
       Determine applicable
     Requirements + Policies
                │
                ▼
         Evaluate authority
        and runtime guards
                │
                ▼
     ENTER CONSISTENCY BOUNDARY
                │
                ▼
       Revalidate authoritative
       state/authority/invariants
                │
         ┌──────┴──────┐
         │             │
       reject        execute
         │             │
         ▼             ▼
     no mutation   authoritative
                     effects
                         │
                         ▼
                  committed result
                         │
                  ┌──────┴──────┐
                  ▼             ▼
            Execution       Domain
             Evidence        Facts
```

---

# VALIDATION

Only now do we demonstrate that the surviving model actually serves Main Street.

### Gardener

```text
IssueQuote
AcceptQuote
ScheduleJob
CompleteJob
```

works without requiring a customer account or gardener-specific runtime.

**PASS**

### Driving instructor

```text
BookLesson
RescheduleLesson
CancelLesson
CompleteLesson
```

supports authenticated student actions and merchant actions with different contextual authority.

**PASS**

### Solicitor

Staff can:

```text
ScheduleConsultation
ConfirmConsultation
CancelConsultation
```

while client remains merely a business participant receiving email/ICS.

**PASS**

### Restaurant

```text
PlaceOrder
AcceptOrder
MarkPreparing
MarkReady
CompleteOrder
```

supports customer-visible projections without exposing all internal states.

**PASS**

### Grocery

`PlaceOrder` can evaluate contextual Requirements:

```text
DELIVERY → address required
COLLECTION → address not required
```

and concurrent inventory invariants remain an authoritative consistency concern.

**PASS**

### Motel

`AllocateRoom` can legitimately involve booking plus room rather than pretending only one ResourceInstance participates.

**PASS**

### Mechanic

```text
RequestWorkshopService
RequestMobileService
```

can produce different applicable Requirements from registered semantics without separate runtime engines.

**PASS**

### Realtor

`SubmitEnquiry` can be anonymous and require no scheduling.

`RequestViewing` can participate in Scheduling and contextual authority.

**PASS**

---

# 45. Consequences for the existing prototype

This falsification exercise has produced an important result.

The current prototype has proved something useful:

```text
Actor
   +
Privilege
   +
Operation
   +
ResourceInstance
   +
Transition
        ↓
safe generic single-resource
state-transition execution
```

The 101 passing tests remain valuable evidence for that subset.

But we must **not extrapolate beyond what those tests prove**.

Specifically, these current assumptions are now considered prototype restrictions rather than universal architecture:

```java
Operation {
    Transition transition;
}
```

and:

```java
execute(
    Actor,
    Operation,
    ResourceInstance
)
```

and:

```java
OperationExecution {
    sourceState;
    targetState;
}
```

They prove the **single-resource transition case**.

They do not yet prove Main Street's complete Operation model.

That distinction is precisely why design is being completed before we continue implementation.

---

## 45.1 Capability-owned effect fulfilment

The Executable Semantic Model constrains what a registered Operation may
authoritatively produce. Its effects are declarative execution contracts, not
imperative bytecode for a generic mutation engine.

```text
Scoped Command
      ↓
Resolve one coherent model and Operation
      ↓
Apply runtime guards
      ↓
Capability-owned application handler
      ↓
Resolve authoritative targets and input
      ↓
Enter the required consistency boundary
      ↓
Revalidate authority, state and invariants
      ↓
Apply effects through capability-owned contracts
      ↓
Verify committed effects/facts remain within the ESM contract
```

The capability-owned handler determines how declared semantics are fulfilled
against its authoritative model. It must not invent effects or committed event
identities absent from the captured executable Operation.

Where one invariant spans several capabilities, a narrow application
coordinator may invoke their published contracts inside one local transaction.
The coordinator does not acquire direct write access to capability internals.

External or long-running effects remain governed by MS-PROT-024 through
MS-PROT-026. They are not made transactionally atomic merely because they are
declared by one Operation.

---

# 46. Accepted invariants

MS-PROT-023 establishes:

1. A Command expresses intent; it does not grant mutation authority.
2. Commands execute registered Operations.
3. Command execution is bounded; it is not a long-running business process.
4. Authentication and authorisation are separate.
5. Business participants and execution principals are separate concepts.
6. Anonymous interaction may initiate explicitly permitted operations without fake customer accounts.
7. System and integration principals are not automatically omnipotent.
8. Authority may depend on target relationships and context, not merely privileges.
9. Merchant scope must be established from trusted context.
10. Evaluation alone cannot mutate authoritative state.
11. Requirement applicability and satisfaction remain separate.
12. Policies constrain registered behaviour; they cannot invent behaviour.
13. An Operation need not universally be a single state transition.
14. An Operation may legitimately affect multiple authoritative entities.
15. Registered semantics determine legitimate execution scope.
16. Stale preliminary evaluation cannot authorise mutation.
17. Concurrent invariants must be enforced at an authoritative consistency boundary.
18. Final execution must revalidate the state relevant to the invariant.
19. Security-sensitive authority may also require authoritative revalidation.
20. A failed operation must not leave prohibited partial authoritative mutation.
21. Main Street does not claim distributed atomicity over external systems.
22. External side effects require separate reliability/reconciliation semantics.
23. Duplicate-sensitive operations require an appropriate duplicate-execution strategy.
24. Committed domain facts must correspond to committed authoritative state.
25. An execution may produce multiple effects.
26. Execution evidence must use trusted execution metadata.
27. Commands and Queries remain distinct.
28. A command executes against one coherent resolved operational model.
29. Long-running workflows are outside the Command execution abstraction.
30. The current Java runtime proves only the single-resource transition subset.
31. Executable effects are declarative constraints, not generic mutation bytecode.
32. Capability-owned application handlers fulfil authoritative effects through owned contracts.
33. A handler may not produce authoritative effects or committed event identities absent from the captured executable Operation.
34. Cross-capability atomic fulfilment uses a narrow application coordinator rather than direct cross-module persistence access.
35. Final authority, state and invariant validation occurs inside the consistency boundary that protects the mutation.

---

# Governance verdict

The original proposal **failed falsification** in several places.

In particular, these assumptions were broken:

```text
one command → one resource
one operation → one transition
evaluation can safely precede execution
one execution → one state change
privilege alone → sufficient authority
all effects → atomically controllable
```

Those assumptions have therefore been **discarded**, not rationalised.

The revised proposal was then attacked using multi-resource allocation, concurrent booking, duplicate requests, anonymous interactions, customer ownership, external payment failures, configuration replacement, privilege revocation, partial mutation and non-transition operations.

The revised core survived those attacks.

```text
MS-PROT-023
Command Evaluation & Runtime Execution Contract

PROPOSE                  ✓
FALSIFY ORIGINAL         ✓ — several failures found
DISCARD FAILED ASSUMPTIONS ✓
REVISE                   ✓
FALSIFY REVISION         ✓
VALIDATE                 ✓
ACCEPT                   ✓
```

## **Status: ACCEPTED**

The most consequential architectural discovery is:

> **Main Street's `Operation` cannot be defined universally as a state transition on one ResourceInstance. State transition is one important operation effect, but the general execution model must accommodate bounded multi-entity effects, creation/recording operations and concurrency-sensitive invariants without abandoning generic semantics.**

That finding should be carried forward before we touch the current Java prototype again.

## Next design document

The falsification work exposed the next missing abstraction naturally:

```text
Command
   ↓
bounded execution
   ↓
???
   ↓
multi-step business progress over time
```

Gardener quotation-to-completion, payment processing, booking lifecycles, cancellations/refunds, delivery and other processes can span multiple Commands, external systems and substantial time.

We should therefore address that explicitly rather than stretching `Command` until it becomes a workflow engine.

**Next: MS-PROT-024 — Business Process & Long-Running Coordination Model.**
