# MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment

**Document ID:** MS-PROT-026  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-026 v1.0 only within production event-contract registration, durable publication, reaction registration, per-reaction consumption, duplicate handling, ordering, semantic-release affinity and reaction failure handling  
**Preserves:** Domain Event fact semantics; semantic-owner authority; Domain Event/Command separation; Domain Event/Integration Event separation; purpose-limited payloads; no exactly-once assumption; no universal global ordering; no universal event-driven architecture  
**Depends on:** MS-PROT-023; MS-PROT-024 v1.1; MS-PROT-025 v1.1; MS-PROT-026 v1.0; composite MS-PROT-027; MS-PROT-031; composite MS-PROT-053 through v1.2; MS-PROT-059; MS-PROT-062; MS-PROT-063; MS-PROT-064; composite MS-PROT-065 through v1.1; MS-PROT-069; MS-PROT-072; MS-PROT-075 v1.1; MS-PROT-079  
**Closes with MS-PROT-065 v1.1:** MS-PROT-079 Target 18 — Events / background processes  
**Purpose:** Make Domain Event publication and independent post-commit reactions production-executable without turning one event acknowledgement into global consumer completion, allowing arbitrary listeners to manufacture authority, depending on exactly-once delivery or arrival order, or allowing event infrastructure to become a second business owner.

---

## 1. Governing Decision

The accepted MS-PROT-026 model remains authoritative:

```text
Command
    requests an authoritative operation

Domain Event
    states that an authoritative fact
    already occurred
```

Target 18 adds the production progression:

```text
authoritative business mutation
        +
durable Domain Event occurrence /
publication responsibility
        ↓
COMMIT
        ↓
Domain Event becomes available
        ↓
0..n registered Event Reactions
        ↓
each reaction progresses independently
        ↓
authorised owning operation /
process wake-up /
projection consequence
```

Hard invariant:

```text
one Domain Event
    ≠
one global delivery acknowledgement
```

## 2. Canonical Separation

Main Street SHALL distinguish:

```text
Domain Event Occurrence
    the authoritative historical fact

Event Publication Responsibility
    durable responsibility to make that occurrence
    available to legitimate consumers

Event Reaction Contract
    registered definition of one legitimate reaction

Event Reaction
    one logical reaction of one contract
    to one event occurrence

Reaction Delivery / Work Attempt
    infrastructure attempt to progress that reaction
```

These concepts MUST NOT be collapsed.

## 3. Domain Event Ownership

The semantic owner of the underlying fact remains the owner of the Domain Event definition.

Examples:

```text
Ordering
    owns Order-related Domain Events

Booking
    owns Booking-related Domain Events

Payment
    owns Payment-related Domain Events

Inventory
    owns Inventory-related Domain Events
```

A consumer MUST NOT redefine another owner’s event semantics.

## 4. Event Contract

Every durable production Domain Event type SHALL have a registered **Event Contract**.

Conceptually:

```text
EventContract
{
    eventContractIdentity
    semanticOwner
    factMeaning
    authorityScope
    subjectReferenceContract
    requiredProvenance
    payloadContract
    compatibility / evolution rules
}
```

This is a semantic contract.

It does not require one universal Java event class or one schema-registry product.

## 5. Event Contract Identity

A durable event occurrence MUST remain attributable to the exact Event Contract meaning under which it was produced.

Later code or semantic releases MUST NOT silently reinterpret an old persisted event using whatever the newest implementation happens to mean.

Sufficient affinity may include:

```text
eventContractIdentity
semantic release / contract version
schema/provenance identity where required
```

Exact encoding remains implementation scope.

## 6. Domain Event Occurrence

Conceptually, a durable Domain Event occurrence preserves sufficient information to determine:

```text
eventIdentity
Event Contract identity
semantic owner
authority scope
fact identity / meaning
subject reference
occurredAt
causation
correlation where applicable
purpose-limited fact payload/evidence
```

Not every field must exist in one generic envelope if the same facts remain determinable through another accepted representation.

## 7. Event Identity

`eventIdentity` identifies one authoritative event occurrence.

It MUST remain distinct from:

```text
business-object identity
Command identity
application-request identity
Event Reaction identity
Work Attempt identity
provider-effect identity
trace identity
```

Two different facts caused by one Command remain two different Domain Events.

## 8. Causation and Correlation

MS-PROT-026 v1.0 survives:

```text
causation
    = what immediately caused this event

correlation
    = the wider process / interaction context
```

Neither is mutation authority.

---

# Publication

## 9. Commit Boundary

Where an authoritative Main Street mutation creates a required durable Domain Event:

```text
authoritative mutation
+
durable event occurrence /
publication responsibility
        ↓
same required local consistency boundary
        ↓
COMMIT
```

The event MUST NOT become observable as a committed fact before the authoritative source mutation commits.

This preserves MS-PROT-025.

## 10. Publication Failure After Commit

Once the authoritative source mutation commits:

```text
source fact = committed
```

even if event publication infrastructure later fails.

Required behaviour:

```text
recover publication responsibility
```

not:

```text
rollback source fact
```

unless an accepted invariant explicitly required the downstream consequence to be atomic, in which case it should not have been modelled as independent post-commit event consumption.

## 11. Event Publication Is Not Reaction Completion

Main Street MUST NOT model:

```text
event acknowledged
```

as meaning every interested consumer completed.

Instead:

```text
event occurrence
    exists once

reaction R1
    has independent progression

reaction R2
    has independent progression

reaction R3
    has independent progression
```

## 12. Publication Responsibility

Infrastructure MAY retain technical evidence that an event occurrence has been made available for reaction discovery/delivery.

That evidence is not a business lifecycle state.

Exact mechanics such as:

```text
outbox row
broker publication record
database polling cursor
queue record
```

remain implementation detail.

---

# Event Reactions

## 13. EventReactionContract

A legitimate production reaction SHALL be defined by a registered **EventReactionContract**.

Conceptually:

```text
EventReactionContract
{
    reactionContractIdentity
    source Event Contract
    reactionOwner
    purpose
    authorityScope
    executionMode
    target responsibility
    executionPrincipalContract
    duplicateIdentityRule
    orderingRequirement
    supersession / coalescing rule
    required event data
    failure / retry contract
}
```

## 14. Registration Is Required

The existence of code that can technically subscribe to an event does not create semantic authority.

Rejected:

```text
new listener class
    ↓
automatically legitimate consumer
```

Required:

```text
accepted EventReactionContract
    ↓
implementation may realise that reaction
```

## 15. Reaction Owner

The reaction owner answers:

> Why does this consequence legitimately exist?

Examples:

```text
Notification
    may own creation/progression of Notification responsibility

Projection owner
    may own projection refresh responsibility

Data Protection lifecycle
    may own lifecycle review responsibility

Process Coordinator
    may own wake-up of its existing process
```

The Domain Event owner does not automatically own every downstream reaction.

## 16. Event Reaction Identity

One logical Event Reaction SHALL be identifiable from:

```text
source Event occurrence
+
EventReactionContract
```

or an equivalent stable identity.

Conceptually:

```text
Event E1

Reaction R1:
    E1 + notification-reaction

Reaction R2:
    E1 + projection-reaction

Reaction R3:
    E1 + lifecycle-review-reaction
```

Each progresses independently.

## 17. Per-Reaction Acknowledgement

Completion acknowledgement MUST be scoped to the Event Reaction, not globally to the event occurrence.

Therefore:

```text
Notification reaction SUCCESS
    does not mark
Projection reaction SUCCESS

Projection reaction RETRY_SAFE
    does not invalidate
Notification reaction SUCCESS
```

## 18. Multiple Consumers

A single Domain Event MAY legitimately have:

```text
0 reactions
1 reaction
many reactions
```

according to accepted contracts.

The event producer does not require knowledge of all physical consumer implementations.

## 19. Independent Reaction

An independent reaction is appropriate where:

```text
source fact remains valid
even if reaction fails
```

Examples may include:

```text
notification intent creation
projection refresh
analytics update
lifecycle review trigger
```

The reaction MUST NOT rewrite the source fact.

## 20. Process Wake-Up

A Domain Event MAY wake a durable process under MS-PROT-024/MS-PROT-072.

Canonical:

```text
Domain Event
    ↓
correlated Event Reaction
    ↓
existing process wake-up
    ↓
process re-evaluates current authority
```

The event does not permit arbitrary process creation unless the governing process contract says that the event legitimately establishes that process responsibility.

## 21. Authoritative Downstream Mutation

Where a reaction requires authoritative mutation:

```text
Event Reaction
    ↓
legitimate trusted execution principal
    ↓
owning Command / Operation
    ↓
current authoritative revalidation
    ↓
mutation
```

Never:

```text
event listener
    ↓
direct write into another capability’s storage
```

---

# Duplicate Delivery

## 22. Exactly-Once Is Not Assumed

Reaction infrastructure MUST tolerate:

```text
duplicate event delivery
duplicate reaction wake-up
worker restart
late delivery
delivery after acknowledgement loss
```

Business safety is provided by Event Reaction identity plus downstream idempotency/revalidation as appropriate.

## 23. Reaction Deduplication

Duplicate physical deliveries of:

```text
Event E1
to Reaction Contract R1
```

MUST converge on one logical Event Reaction:

```text
E1/R1
```

They MUST NOT manufacture multiple logical business consequences.

## 24. Downstream Idempotency Remains Separate

Reaction deduplication does not automatically provide idempotency for the target authoritative operation.

Example:

```text
Event Reaction identity
    ≠
Notification command identity
```

The reaction contract must define how the downstream logical intent identity is derived or preserved.

---

# Ordering

## 25. No Global Ordering

MS-PROT-026 v1.0 survives unchanged.

There is no global ordering requirement across unrelated:

```text
merchants
capabilities
objects
processes
```

## 26. Reaction Ordering Requirement

Every reaction whose correctness depends on ordering MUST identify the smallest meaningful ordering authority.

Possible semantic sources include:

```text
source-object revision
source stream/sequence
process causality
explicit predecessor fact
```

The exact vocabulary is owner-specific.

## 27. Arrival Order Is Not Authority

A consumer MUST NOT assume:

```text
message arrived later
    →
business fact occurred later
```

where delivery can reorder.

If a delayed event represents older source state:

```text
query current authority
or
apply accepted revision/sequence rules
```

before mutation.

## 28. Concurrent Reactions

Two different reactions to the same event may progress concurrently unless accepted semantics require otherwise.

The event infrastructure MUST NOT create unnecessary global serialisation.

---

# Supersession and Coalescing

## 29. Reaction Supersession

A reaction contract may define that older pending reaction work becomes unnecessary because a newer authoritative fact supersedes it.

Example:

```text
refresh projection for C1

then C2 becomes current

pending C1 refresh
    may become NO_LONGER_APPLICABLE
```

where the Projection Contract permits current-state rebuild semantics.

## 30. Coalescing

Reaction work MAY be coalesced only when the owning contract proves that intermediate reaction execution is not itself historically material.

Valid candidate:

```text
rebuild projection to current source authority
```

Not automatically valid:

```text
preserve every financial event
```

Infrastructure does not decide this.

---

# Event Evolution

## 31. Schema / Contract Evolution

Persisted Domain Events may outlive the software release that produced them.

Breaking interpretation MUST be handled through accepted compatibility mechanisms such as:

```text
additive evolution
version-affined interpretation
translation
new Event Contract
consumer migration
```

No specific schema-registry technology is mandated.

## 32. Historical Affinity

When an Event Reaction processes an old durable event, it MUST have enough contract affinity to interpret that event safely.

Current “latest” event semantics MUST NOT silently overwrite historical meaning.

---

# Integration Events

## 33. Domain Event vs Integration Event

MS-PROT-026 v1.0 survives:

```text
Domain Event
    internal authoritative fact

Integration Event
    deliberately published external contract
```

External publication requires explicit translation/publication authority.

## 34. Integration Reaction

Publishing an Integration Event may itself be a registered Event Reaction.

Canonical:

```text
Domain Event
    ↓
registered integration-publication reaction
    ↓
purpose-limited Integration Event
```

It is not automatic for every Domain Event.

---

# Data Protection

## 35. Event Payload Minimisation

Event occurrences and reaction work MUST contain only the data needed to:

```text
express the fact
identify the subject
support registered reactions
```

They MUST NOT become:

```text
full aggregate snapshots
permanent PII archives
credential stores
provider-secret stores
```

## 36. Reaction Data Requirement

An EventReactionContract SHALL identify the event data it requires.

A producer MUST NOT add unrelated sensitive data merely because some hypothetical future consumer might find it useful.

## 37. Current Sensitive Data

Where a reaction needs current personal/restricted data rather than historical event data:

```text
reaction
    ↓
query current authoritative owner
    ↓
apply current use/Exposure/security authority
```

where applicable.

---

# Notification

## 38. Domain Event Is Not Notification

The accepted separation survives.

Example:

```text
AppointmentConfirmed
    ↓
registered Notification reaction
    ↓
NotificationIntent
    ↓
NotificationDispatch
    ↓
DeliveryAttempt
```

The Domain Event is not itself the customer message.

---

# Projection

## 39. Projection Reaction

A Projection Contract remains authority for:

```text
projection meaning
source authority
freshness
serviceability
rebuild semantics
```

An Event Reaction only supplies durable execution progression where asynchronous update/rebuild is appropriate.

## 40. Projection Stale-Write Protection

A delayed projection Event Reaction MUST NOT overwrite a newer projection state merely because its message arrived later.

It must use:

```text
current owner source
or
accepted source revision/checkpoint semantics
```

according to the Projection Contract.

---

# Data Lifecycle

## 41. Lifecycle Review Reaction

MS-PROT-053 v1.2 may establish that an event is a material lifecycle-review trigger.

Canonical:

```text
business Event
    ↓
registered lifecycle-review reaction
    ↓
data-lifecycle.evaluate
```

The Event Reaction MUST NOT encode an irreversible future disposition as though the event itself authorised deletion.

---

# Provider / Reconciliation

## 42. Provider Evidence Event

Provider-native callbacks/evidence are not automatically Domain Events.

The owning provider/capability boundary first authenticates, correlates and interprets provider evidence.

Only an accepted Main Street fact may then produce an applicable Domain Event.

## 43. Reconciliation Reaction

A Domain Event may establish or wake a reconciliation responsibility where the owning contract requires it.

The reconciliation operation remains owned by the affected capability/provider process.

---

# Failure

## 44. Reaction Result Classification

A reaction may produce background execution classifications including:

```text
SUCCESS
RETRY_SAFE
RECONCILIATION_REQUIRED
NO_LONGER_APPLICABLE
TERMINAL_FAILURE
MANUAL_INTERVENTION_REQUIRED
```

where consistent with MS-PROT-065.

These are not source-business lifecycle states.

## 45. Consumer Failure

Failure of Reaction R1:

```text
does not invalidate Event E1
does not erase source business fact
does not automatically fail Reaction R2
```

## 46. Poison / Exhausted Reaction

Repeated failure MUST NOT result in silent event/reaction deletion.

When automatic progress cannot continue safely:

```text
re-evaluate reaction contract
        ↓
TERMINAL_FAILURE
or
MANUAL_INTERVENTION_REQUIRED
or
RECONCILIATION_REQUIRED
```

as applicable.

---

# Security / Principal

## 47. Event Receipt Does Not Carry Actor Authority

The actor who caused the original fact does not automatically authorise all downstream work.

A reaction requiring mutation uses its own legitimate scheduled/system/process principal under MS-PROT-063.

## 48. Merchant Scope

Merchant-scoped Event Reactions MUST preserve trusted Merchant Scope.

Event identity alone does not establish tenant scope.

Platform-scoped events/reactions remain possible without fake Merchant Scope.

---

# Audit and Observability

## 49. Reaction Evidence

Material Event Reactions may produce Audit evidence under MS-PROT-064 where required.

Routine delivery/retry telemetry may remain operational observability.

```text
Event Reaction
    ≠ AuditRecord
    ≠ log entry
```

## 50. Observability

Target 18 requires enough technical evidence to diagnose:

```text
unpublished durable events
pending reactions
reaction age
duplicate delivery
retries
terminal failures
manual-intervention requirements
ordering conflicts
```

Exact metrics/dashboard design belongs to Target 19.

---

# Atomicity

## 51. No Global Event Transaction

There is no distributed atomic transaction spanning:

```text
source capability
+
all Event Reactions
+
external providers
+
projections
```

The atomic source boundary ends at the durable event/publication responsibility required by MS-PROT-025.

---

# Falsification

## 52. One Event, Notification + Projection

```text
E1
    → Notification succeeds
    → Projection fails
```

Required:

```text
Notification reaction complete
Projection reaction remains pending/retriable
E1 remains true
```

**PASS**

## 53. Notification Acknowledges First

Required:

```text
global event is NOT marked consumed
for every other reaction
```

**PASS**

## 54. Duplicate Event Delivery

Required:

```text
same E1/R1 logical reaction
```

not two independent business intents.

**PASS**

## 55. Event Delivered Out of Order

```text
C2 event arrives
then delayed C1 event
```

Required:

```text
consumer uses revision/current authority
C1 cannot overwrite C2
```

**PASS**

## 56. Consumer Added in Later Release

A later consumer does not retroactively gain authority to reinterpret every retained historical event unless accepted migration/replay semantics explicitly permit it.

**PASS**

## 57. Provider Callback

Raw provider webhook:

```text
≠ Domain Event automatically
```

Provider/capability interpretation comes first.

**PASS**

## 58. Lifecycle Review

```text
OrderClosed Event
```

may trigger:

```text
data-lifecycle.evaluate
```

but not:

```text
DELETE customer immediately
```

without current lifecycle authority.

**PASS**

## 59. Event Publication Worker Crashes

Source transaction remains committed.

Publication responsibility remains recoverable.

**PASS**

---

# Rejected Alternatives

## 60. Rejected

Target 18 rejects:

```text
one global delivered bit per Domain Event
one Event acknowledgement means all consumers completed
arbitrary listener class = semantic consumer authority
event receipt = mutation authority
event actor authority propagates downstream
exactly-once requirement
arrival order = business order
global event sequence across Main Street
Domain Event = Notification
Domain Event = Integration Event
provider callback = Domain Event automatically
event payload = complete aggregate snapshot
event bus = business owner
consumer failure = rollback source fact
all collaboration must use events
```

---

# Deferred / Implementation Scope

## 61. Deferred

Target 18 does not select:

```text
Kafka
RabbitMQ
Redis Streams
database polling
broker topology
topic naming
partition strategy
outbox table shape
consumer framework
serialization format
schema-registry technology
lease/lock implementation
poll frequency
batch size
event retention period
exact Java envelope
exact SQL indexes
```

These must satisfy this authority rather than define it.

---

# Conformance

## 62. Conformance Criteria

A conforming production design/implementation MUST prove:

```text
[ ] every durable event has exact registered event semantics
[ ] old events remain safely interpretable after release evolution
[ ] source mutation and required durable publication responsibility
    share the required local atomic boundary
[ ] event publication and consumer completion are separate
[ ] every legitimate reaction has a registered EventReactionContract
[ ] arbitrary listeners do not manufacture authority
[ ] one event may support multiple independent reactions
[ ] acknowledgement is per logical reaction
[ ] duplicate physical delivery converges on one logical reaction
[ ] reaction identity is distinct from downstream command identity
[ ] exactly-once delivery is not assumed
[ ] no global ordering is assumed
[ ] ordering-sensitive reactions define the smallest meaningful
    ordering authority
[ ] delayed stale reactions cannot overwrite newer authoritative results
[ ] coalescing/supersession occurs only where owner semantics permit it
[ ] event data remains purpose-limited
[ ] actor causation does not propagate authority downstream
[ ] consumer failure does not erase source business truth
[ ] exhausted reactions do not disappear silently
[ ] Domain Event, Integration Event, Notification and Audit remain distinct
```

---

# 63. Closure Effect

With MS-PROT-065 v1.1:

```text
Domain Event semantic ownership
    → composite MS-PROT-026

source/publish atomicity
    → MS-PROT-025

Event Contract / publication / reaction
    → MS-PROT-026 v1.1

durable physical reaction progression
    → MS-PROT-065 v1.1

downstream authoritative mutation
    → target owning capability

process wake-up
    → MS-PROT-024 / MS-PROT-072

uncertainty
    → MS-PROT-069

projection semantics
    → MS-PROT-027

data lifecycle semantics
    → MS-PROT-053

observability/reconciliation tooling
    → Target 19
```

No material event-publication/reaction semantic decision remains for implementation to invent.

---

# 64. Governing Principle

> **A Domain Event records one authoritative fact; publication makes that fact durably available; each legitimate downstream consequence is a separately registered reaction with its own identity, authority, ordering and completion semantics. Event infrastructure never becomes the business owner, one consumer never acknowledges work on behalf of every other consumer, and duplicate or reordered physical delivery never manufactures new business intent.**

---

# 65. Acceptance Statement

MS-PROT-026 v1.1 makes production event publication and independent consumption deterministic without adopting a universal event-driven architecture, exactly-once delivery or global ordering.

**Status: ACCEPTED**