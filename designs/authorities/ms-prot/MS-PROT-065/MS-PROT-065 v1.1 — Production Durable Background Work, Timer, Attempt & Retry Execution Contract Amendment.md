# MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment

**Document ID:** MS-PROT-065  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-065 v1.0 only within production Background Work Contract registration, Durable Work Instruction identity, Work Attempt execution, downstream logical-intent affinity, timer/recurrence identity, acknowledgement recovery, retry exhaustion, supersession and generic projection/lifecycle/provider-reconciliation progression  
**Preserves:** owner/process authority over why work exists; Business Deadline/Timer separation; current authoritative revalidation; bounded scheduled principals; existing retry classifications; no exactly-once assumption; retry/reconciliation separation; no universal workflow engine; no provider/business ownership transfer  
**Depends on:** MS-PROT-023; MS-PROT-024 v1.1; MS-PROT-025 v1.1; composite MS-PROT-026 through v1.1; composite MS-PROT-027; composite MS-PROT-048; composite MS-PROT-053 through v1.2; MS-PROT-059; MS-PROT-062; MS-PROT-063; MS-PROT-064; MS-PROT-065 v1.0; MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-075 v1.1; MS-PROT-079  
**Closes with MS-PROT-026 v1.1:** MS-PROT-079 Target 18 — Events / background processes  
**Purpose:** Make future/scheduled/background responsibility production-executable while preserving the separation among business responsibility, Durable Work Instruction, scheduler representation, Work Attempt and downstream logical business/provider intent; ensure duplicate delivery, worker crash, overdue execution, recurrence and retry exhaustion cannot create duplicate or stale authoritative effects.

---

## 1. Governing Decision

MS-PROT-065 v1.0 survives:

> The owning capability or process defines why future work exists and what it means. Background infrastructure owns only durable scheduling, wake-up and execution attempts.

Target 18 adds production precision:

```text
owner-qualified responsibility
        ↓
BackgroundWorkContract
        ↓
DurableWorkInstruction
        ↓
0..n WorkAttempts
        ↓
owner/process operation or evaluation
        ↓
authoritative result
```

Hard separation:

```text
business responsibility
    ≠ DurableWorkInstruction

DurableWorkInstruction
    ≠ timer/scheduler entry

DurableWorkInstruction
    ≠ WorkAttempt

WorkAttempt
    ≠ downstream logical command

WorkAttempt
    ≠ provider effect
```

---

# BackgroundWorkContract

## 2. Registered BackgroundWorkContract

Every material durable background responsibility SHALL be governed by a registered **BackgroundWorkContract**.

Conceptually:

```text
BackgroundWorkContract
{
    contractIdentity
    semantic owner
    purpose
    authorityScope
    triggerForm
    target operation / process evaluation
    executionPrincipalContract
    dueCondition semantics
    overdue semantics
    current-revalidation requirements
    downstream logical-intent identity rule
    retry / uncertainty contract
    supersession / cancellation rule
    data-minimisation contract
    semantic / configuration affinity
}
```

It is not an arbitrary workflow definition language.

## 3. Owner

The owner answers:

> Why must this future responsibility exist?

Examples:

```text
Notification
    reminder evaluation

Payment
    provider reconciliation

Projection owner
    projection rebuild/update

Data Protection
    lifecycle reevaluation

Process Coordinator
    wait-until deadline
```

The scheduler does not own these meanings.

## 4. No Arbitrary Jobs

Main Street SHALL NOT accept:

```text
execute arbitrary class
execute arbitrary SQL
execute arbitrary script
AI-generated workflow
merchant-authored executable code
```

as production BackgroundWorkContracts.

Only registered platform/capability/process responsibilities are executable.

---

# Durable Work Instruction

## 5. Definition

A **DurableWorkInstruction** is:

> Durable evidence that one exact registered future responsibility must be evaluated or progressed after the originating execution boundary has ended.

Conceptually:

```text
DurableWorkInstruction
{
    workIdentity
    BackgroundWorkContract identity
    owner scope
    subject / process reference
    trigger / due condition
    correlation
    causation
    semantic/configuration provenance
    where required
    downstream logical-intent identity
    where already established
    createdAt
}
```

## 6. Stable Work Identity

`workIdentity` identifies one logical future responsibility.

It remains distinct from:

```text
business-object identity
Command identity
Event identity
Event Reaction identity
WorkAttempt identity
provider request identity
trace identity
```

## 7. Durable Acceptance

A work responsibility is durably accepted only when enough work evidence has committed such that future execution/recovery does not depend on:

```text
original HTTP connection
original process memory
original thread
original worker
```

## 8. No Future Mutation Authority

The existence of the DurableWorkInstruction does not itself authorise the future business mutation.

Canonical:

```text
work becomes due
        ↓
WorkAttempt
        ↓
trusted bounded execution principal
        ↓
current authoritative revalidation
        ↓
owner operation/process evaluation
```

---

# Work Attempt

## 9. Definition

A **WorkAttempt** is:

> One attributable infrastructure attempt to progress one DurableWorkInstruction.

Conceptually:

```text
WorkAttempt
{
    attemptIdentity
    workIdentity
    attemptedAt
    execution principal/context
    execution/progression reference
    result classification
    failure/evidence reference where applicable
}
```

## 10. Attempt Identity

Multiple WorkAttempts may legitimately belong to one DurableWorkInstruction:

```text
W1
    ├── A1
    ├── A2
    └── A3
```

That does not mean:

```text
three business intentions
```

## 11. Attempt Evidence Before Consequential Execution

Where a WorkAttempt may initiate a duplicate-sensitive authoritative or external effect, sufficient durable progression identity/evidence MUST exist before that consequential execution begins.

The system must be able to distinguish at least:

```text
not attempted
attempt accepted/started
known completed
known safely not completed
outcome uncertain
```

where required for safe recovery.

Exact persistence mechanics remain implementation detail.

---

# Downstream Logical Intent

## 12. WorkAttempt Is Not Command Identity

Suppose:

```text
W1
    reminder evaluation

A1
    invokes Notification logical command N1
```

If the worker crashes after `N1` commits but before acknowledging W1:

```text
A2
```

MUST NOT automatically create:

```text
N2
```

as a second independent communication.

## 13. Downstream Logical-Intent Affinity

A BackgroundWorkContract that invokes a duplicate-sensitive downstream operation MUST define how the same logical downstream intent is reconstructed or recovered across WorkAttempts.

Conceptually:

```text
work W1
    → downstream intent N1

retry WorkAttempt
    → resolve/retry N1
```

not:

```text
retry WorkAttempt
    → invent new N2
```

unless a separately authorised new business effect is legitimately required.

## 14. Layered Idempotency

Main Street SHALL preserve:

```text
work identity
    ≠ downstream command identity
    ≠ provider effect identity
```

Each boundary keeps its own accepted duplicate-safety semantics.

---

# Due Time / Timers

## 15. Business Deadline vs Timer

MS-PROT-065 v1.0 survives:

```text
Business Deadline
    semantic authority

Timer / Scheduler Entry
    infrastructure wake-up mechanism
```

## 16. Due Time Is Not Advance Authority

A scheduler MUST NOT execute semantically time-bound work before its accepted due condition permits it.

Technical early wake-up requires:

```text
re-evaluate due condition
    ↓
not due
    ↓
no authoritative consequence
```

## 17. Timer Delivery Is Not Condition Satisfaction

```text
timer fired
```

does not mean:

```text
business deadline condition satisfied
```

The owner/process evaluates current authoritative time/state.

## 18. Overdue Work

The existing owner-defined modes survive:

```text
EXECUTE_WHEN_OVERDUE
RE_EVALUATE_CURRENT_STATE
EXPIRE_WITHOUT_EXECUTION
ESCALATE
```

or semantically equivalent registered behaviour.

Infrastructure MUST NOT invent which mode applies.

## 19. Scheduler Outage

If due work was not executed because scheduling infrastructure was unavailable:

```text
work remains durably outstanding
```

until owner semantics resolve it.

Passing the target time alone MUST NOT silently delete it.

---

# Recurrence

## 20. Recurring Responsibility

Recurring responsibility belongs to the owning contract.

Physical implementation may use:

```text
next-occurrence work
periodic scan
scheduler recurrence
provider callback
reconciliation cursor
```

No single approach is mandated.

## 21. Occurrence Identity

Where recurrence represents discrete logical occurrences, each intended occurrence MUST remain distinguishable.

Conceptually:

```text
recurrence R

occurrence O1
occurrence O2
occurrence O3
```

Duplicate scheduler delivery for O1 MUST NOT manufacture O1a/O1b as new intended occurrences.

## 22. Recurrence Misses

If several occurrences pass during an outage, the owning BackgroundWorkContract determines whether to:

```text
process each occurrence
coalesce to current evaluation
skip obsolete occurrences
escalate
```

The scheduler does not invent catch-up semantics.

---

# Retry

## 23. Existing Result Classifications Survive

MS-PROT-065 v1.0 remains authoritative for:

```text
SUCCESS
RETRY_SAFE
RECONCILIATION_REQUIRED
TERMINAL_FAILURE
MANUAL_INTERVENTION_REQUIRED
NO_LONGER_APPLICABLE
```

These are technical/process progression classifications, not universal business states.

## 24. RETRY_SAFE

`RETRY_SAFE` permits another WorkAttempt only where the owner contract proves repetition is safe.

Technical transient failure alone does not prove business retry safety.

## 25. RECONCILIATION_REQUIRED

Where an earlier attempt may have produced a consequential effect but proof is insufficient:

```text
RECONCILIATION_REQUIRED
```

applies.

No blind repetition.

MS-PROT-069 remains uncertainty authority.

## 26. Lost Worker Acknowledgement

Canonical case:

```text
WorkAttempt
    ↓
downstream authoritative operation commits
    ↓
worker crashes before work completion acknowledgement
```

Recovery must:

```text
resolve downstream logical outcome
```

rather than assume no effect occurred.

## 27. Known Pre-Effect Failure

Where Main Street can prove no downstream consequential effect occurred:

```text
new WorkAttempt may proceed
```

subject to current authoritative revalidation.

---

# Retry Exhaustion / Poison Work

## 28. Retry Budget Is Not Business Meaning

Implementation may use a bounded technical retry budget.

But:

```text
retryCount exceeded
```

does not itself mean:

```text
business responsibility disappears
```

## 29. Exhaustion Resolution

When automatic retry policy is exhausted:

```text
re-evaluate owner contract
        ↓
TERMINAL_FAILURE
or
MANUAL_INTERVENTION_REQUIRED
or
RECONCILIATION_REQUIRED
or
NO_LONGER_APPLICABLE
```

as appropriate.

## 30. No Silent Poison Deletion

Rejected:

```text
failed too many times
    ↓
drop from queue
```

without an authoritative disposition.

## 31. Manual Intervention

`MANUAL_INTERVENTION_REQUIRED` routes to an accepted intervention path under MS-PROT-064.

It does not authorise:

```text
manual database edits
generic force-success
invariant bypass
```

---

# Current-State Revalidation

## 32. Stale Work

Every new consequential execution must revalidate all owner-defined mutable predicates.

Examples:

```text
current business state
current applicability
current entitlement where required
current provider readiness
current recipient eligibility
current lifecycle authority
```

as applicable.

## 33. Captured vs Current Semantics

The BackgroundWorkContract must state whether execution depends on:

```text
CAPTURED COMMITMENT SEMANTICS
```

or:

```text
CURRENT ELIGIBILITY SEMANTICS
```

or a defined composition of both.

Background infrastructure does not decide.

## 34. Historical Provenance

Where captured semantics apply, the DurableWorkInstruction MUST preserve enough affinity to the:

```text
Configuration Revision
semantic release
provider/binding identity
policy revision
commercial commitment
```

required by the owner.

---

# Supersession / Cancellation

## 35. Work Cancellation vs Business Cancellation

Canonical:

```text
cancel reminder Work Instruction
    ≠
cancel Appointment
```

Infrastructure cancellation affects progression responsibility only.

## 36. Supersession

A work item may be superseded where an accepted owner rule establishes a newer work responsibility or state that replaces it.

Material supersession SHOULD preserve traceability.

## 37. NO_LONGER_APPLICABLE

Where current state proves the work responsibility no longer requires action:

```text
NO_LONGER_APPLICABLE
```

is a successful semantic resolution of that background responsibility.

It is not failure.

---

# Event Reactions

## 38. Event → Durable Work

An EventReactionContract under MS-PROT-026 v1.1 MAY create or identify a DurableWorkInstruction when reaction progression must survive:

```text
restart
delay
retry
provider wait
manual intervention
```

Not every Event Reaction requires durable work.

## 39. Event Reaction Identity vs Work Identity

```text
EventReaction
    identifies logical consequence

DurableWorkInstruction
    identifies durable future progression
```

One may map directly to the other where appropriate, but they remain semantically distinguishable.

---

# Projection Work

## 40. Projection Update / Rebuild

Background work may progress an accepted Projection Contract update/rebuild responsibility.

The Projection Contract owns:

```text
source authority
projection meaning
checkpoint/revision semantics
freshness/serviceability
```

Background infrastructure owns only progression.

## 41. Projection Coalescing

For a projection whose semantics are:

```text
rebuild to current authoritative source
```

multiple pending update triggers MAY be coalesced where the Projection Contract permits.

For historically meaningful incremental processing, coalescing is not automatically safe.

## 42. Projection Ordering

A late projection WorkAttempt MUST NOT overwrite a projection based on newer source authority.

Current source/revision/checkpoint semantics must govern.

---

# Data Lifecycle Work

## 43. Lifecycle Review

MS-PROT-053 v1.2 establishes material lifecycle-review triggers.

Background work may durably represent:

```text
reevaluate lifecycle scope X
at/after condition D
```

It MUST NOT generally represent:

```text
DELETE X at D
```

as future destructive authority.

## 44. Lifecycle Revalidation

When the lifecycle work executes:

```text
data-lifecycle.evaluate
```

re-establishes current retention/use authority.

Any eventual disposition performs its own required final revalidation.

---

# Provider Reconciliation

## 45. Provider Reconciliation Work

Background work may progress owner-qualified provider reconciliation such as:

```text
Payment execution uncertainty
Shipment preparation uncertainty
return-label uncertainty
Notification provider uncertainty
provider-side privacy disposition
```

## 46. Historical Provider Affinity

Provider reconciliation work MUST preserve the exact historical:

```text
provider
ProviderConnection
binding revision
provider correlation/idempotency identity
```

required by the uncertain operation.

It MUST NOT resolve current provider routing and accidentally reconcile old Provider-A work against Provider B.

## 47. Reconciliation Work Does Not Own Provider Truth

Background infrastructure only causes the appropriate reconciliation operation to run.

The owning capability/provider boundary determines the resulting authoritative interpretation.

---

# Notification / Reminders

## 48. Reminder Work

A reminder DurableWorkInstruction means:

```text
reevaluate whether communication remains due
```

not:

```text
message must definitely be transmitted
```

Current Notification/recipient/provider authority remains required.

## 49. Notification Delivery Retry

Notification DeliveryAttempt/provider-effect retry follows MS-PROT-075 v1.1.

Generic background retry MUST NOT bypass Notification’s attempt-scoped idempotency and uncertainty rules.

---

# Process Timers

## 50. Durable Process Wake-Up

For a persistent process:

```text
process owner
    establishes wait/deadline

Background Work
    preserves future wake-up

Process
    re-evaluates on wake-up
```

The timer does not own process transition meaning.

---

# Concurrency

## 51. Worker Claiming

Implementation may use:

```text
lease
lock
queue ownership
compare-and-set
database claim
```

to reduce concurrent duplicate processing.

These are technical mechanisms only.

## 52. Claim Does Not Replace Domain Concurrency

A worker holding a lease does not gain:

```text
Inventory authority
Booking authority
Payment authority
provider side-effect certainty
```

Target operations still enforce their own concurrency/invariants.

## 53. Multiple Workers

Concurrent physical processing of the same work identity must converge safely.

Work infrastructure MUST NOT rely solely on “this normally runs on one worker”.

---

# Scope and Principal

## 54. Scheduled/System Principal

Each BackgroundWorkContract requires a bounded trusted execution principal/context under MS-PROT-063.

Rejected:

```text
unrestricted SYSTEM superuser
```

## 55. Merchant Scope

Merchant-scoped work preserves trusted Merchant Scope.

Persisting a client-provided merchant identifier does not make it trusted.

## 56. Platform-Scoped Work

Platform responsibilities use explicit platform scope.

They MUST NOT manufacture a fake merchant merely to fit worker infrastructure.

---

# Data Minimisation / Security

## 57. Work Payload

Durable work stores only the data needed to:

```text
identify responsibility
resolve owner context
re-establish authoritative state
preserve required historical affinity
```

Prefer references over full mutable aggregate snapshots.

## 58. Secrets

Durable Work Instructions and WorkAttempts MUST NOT store raw:

```text
passwords
session tokens
provider API secrets
OAuth refresh tokens
private credentials
```

Use accepted secure references.

---

# Recovery

## 59. Restart Recovery

After process/node restart, durable work remains discoverable from committed state.

Recovery MUST NOT depend on memory of the worker that previously owned it.

## 60. Worker Lost Mid-Attempt

Worker loss creates no automatic conclusion that:

```text
target effect failed
```

Outcome must be established from durable authoritative/progression evidence.

---

# Observability

## 61. Operational Evidence

Background infrastructure should make it possible to diagnose:

```text
due work
overdue work
unclaimed work
claimed work
attempt age
retry volume
reconciliation-required work
manual-intervention work
terminal failure
scheduler/worker health
```

Exact telemetry/dashboard authority belongs to Target 19.

## 62. Telemetry Is Not Work Truth

Logs/metrics/traces MUST NOT be the sole evidence that durable work:

```text
exists
completed
was reconciled
```

where semantic recovery requires durable evidence.

---

# Atomicity

## 63. Work Creation

Where an authoritative mutation requires a durable post-commit consequence, its durable reaction/work responsibility must be established with the guarantee required by MS-PROT-025/MS-PROT-072.

## 64. No Distributed Work Transaction

There is no universal transaction spanning:

```text
work store
target capability
external provider
projection
notification
```

Each owner protects its accepted local invariant.

---

# Falsification

## 65. Reminder Executes After Customer Already Cancelled

Expected:

```text
current-state revalidation
    ↓
NO_LONGER_APPLICABLE
```

**PASS**

## 66. Notification Committed, Worker Crashes

```text
W1
    → A1
    → Notification command N1 commits
    → worker dies
```

Retry:

```text
A2
    resolves/reuses N1
```

not new N2 by default.

**PASS**

## 67. Provider Timed Out

Expected:

```text
RECONCILIATION_REQUIRED
```

not blind retry.

**PASS**

## 68. Retry Budget Exhausted

Expected:

```text
explicit owner-governed disposition
```

not silent deletion.

**PASS**

## 69. Timer Fires Early

Expected:

```text
due condition false
no effect
```

**PASS**

## 70. Worker Runs Late

Expected:

```text
owner overdue semantics
```

not scheduler invention.

**PASS**

## 71. Duplicate Scheduler Delivery

Expected:

```text
same work / occurrence identity
```

not multiple logical business responsibilities.

**PASS**

## 72. Recurring Job Misses Three Occurrences

Required behaviour comes from BackgroundWorkContract:

```text
catch up individually
or
coalesce
or
skip
or
escalate
```

Infrastructure cannot choose semantically.

**PASS**

## 73. Projection C1 Runs After C2

Expected:

```text
C1 cannot overwrite newer C2 authority
```

**PASS**

## 74. Lifecycle Review Due

Expected:

```text
reevaluate lifecycle
```

not unconditional stored delete command.

**PASS**

## 75. Old Provider Reconciliation After Provider Switch

Expected:

```text
historical Provider A affinity
```

not current Provider B.

**PASS**

## 76. Work Lease Held

Expected:

```text
lease provides technical coordination
not business authority
```

**PASS**

---

# Rejected Alternatives

## 77. Rejected

Target 18 rejects:

```text
cron expression = business deadline
timer fired = condition satisfied
work instruction = future mutation authority
WorkAttempt = new business intent
worker retry = new provider effect automatically
retry count exceeded = delete work
queue lease = domain lock
unrestricted SYSTEM principal
scheduler owns overdue semantics
all recurring work must use cron
all background work must use one queue
all event reactions require durable coordinator
current provider replaces historical provider affinity
stale work bypasses current authoritative revalidation
AI may generate executable jobs
```

---

# Deferred / Implementation Scope

## 78. Deferred

Target 18 does not select:

```text
RabbitMQ
Kafka
Redis
PostgreSQL work queue
Quartz
Spring Scheduler
Temporal
Camunda
workflow engine
saga library
lease duration
lock algorithm
poll interval
worker count
backoff algorithm
retry counts
jitter
queue partitioning
batch size
dead-letter physical representation
work table schema
index strategy
exact Java workers
deployment topology
```

These remain implementation architecture/details unless a later concrete requirement proves a semantic dependency.

---

# Conformance

## 79. Conformance Criteria

A conforming production implementation MUST prove:

```text
[ ] every material durable responsibility has an owner-qualified
    BackgroundWorkContract
[ ] DurableWorkInstruction does not become business authority
[ ] work identity, WorkAttempt identity and downstream logical
    intent identity remain distinct
[ ] duplicate WorkAttempts cannot multiply one downstream intent
[ ] consequential attempts preserve enough progression evidence
    for crash recovery
[ ] current owner predicates are revalidated before new effects
[ ] captured-vs-current semantic affinity is explicit
[ ] timer wake-up does not equal business-condition satisfaction
[ ] overdue behaviour is owner-defined
[ ] recurring logical occurrences cannot multiply through
    scheduler duplication
[ ] retry safety is owner/provider-specific
[ ] uncertain side effects enter reconciliation
[ ] retry exhaustion cannot silently discard responsibility
[ ] manual intervention uses accepted operations
[ ] superseding work does not cancel the underlying business fact
[ ] projection work preserves projection authority
[ ] lifecycle work re-evaluates rather than storing stale
    irreversible disposition authority
[ ] provider reconciliation preserves historical provider affinity
[ ] worker claims do not replace domain concurrency protection
[ ] merchant/platform scope remains explicit
[ ] raw credentials do not enter durable work payloads
[ ] worker/process restart does not lose accepted responsibilities
[ ] exact queue/scheduler/worker technology remains implementation
    detail
```

---

# 80. Closure Effect

With MS-PROT-026 v1.1:

```text
business process meaning
    → MS-PROT-024 / owning capability

Domain Event meaning/reaction
    → composite MS-PROT-026 through v1.1

transaction/outbox-equivalent guarantee
    → MS-PROT-025

background responsibility/execution
    → composite MS-PROT-065 through v1.1

network/provider uncertainty
    → MS-PROT-069

cross-capability progression
    → MS-PROT-072

projection semantics
    → composite MS-PROT-027

data lifecycle semantics
    → composite MS-PROT-053

Notification provider attempts
    → MS-PROT-075 v1.1

intervention
    → MS-PROT-064

observability/reconciliation tooling
    → Target 19
```

No Target-18 material semantic rule remains for implementation to invent.

---

# 81. Governing Principle

> **Durable background work records one future responsibility to re-evaluate or progress; it is not future business authority. Physical WorkAttempts may repeat, but they must not manufacture new business or provider intent. Timers only wake evaluation, retry occurs only where owner semantics prove it safe, uncertain effects reconcile rather than repeat, overdue and recurring behaviour remain owner-defined, and no worker, scheduler, queue, lease or AI system becomes a hidden business-policy owner.**

---

# 82. Acceptance Statement

MS-PROT-065 v1.1 makes durable future work production-constraining without selecting a broker, queue, scheduler, worker framework or workflow engine.

**Status: ACCEPTED**