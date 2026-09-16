# MS-PROT-065 — Durable Background Work, Timers, Retry & Scheduled Execution Model

**Document ID:** MS-PROT-065  
**Version:** 1.0  
**Status:** Accepted  
**Depends on:** MS-PROT-023, MS-PROT-024, MS-PROT-025, MS-PROT-026, MS-PROT-028, MS-PROT-031, MS-PROT-032, MS-PROT-048, MS-PROT-056, MS-PROT-062, MS-PROT-063, MS-PROT-064  
**Purpose:** Define the backend semantic contract for durable future work, timers, retries, scheduled/system execution, overdue work, and background delivery while preserving capability ownership and preventing scheduler infrastructure from becoming business authority.

---

## 1. Problem

Main Street must perform work after the originating request has ended or at a future time.

Examples include:

- trial-expiry reminders;
- appointment reminders;
- payment or provider reconciliation retries;
- outbox delivery attempts;
- deferred notifications;
- process deadlines;
- scheduled publication;
- delayed follow-up actions;
- retry after a known transient failure;
- periodic maintenance or reconciliation work;
- wake-up of a durable process waiting on time.

Without a strict semantic boundary, background infrastructure can become a hidden workflow engine, an implicit business-policy owner, or a source of stale future mutation authority.

---

## 2. Governing Principle

> **The owning capability or process defines why future work exists and what it means. Background infrastructure owns only durable scheduling, wake-up and delivery attempts.**

A scheduled instruction is not authority to mutate future business state.

When due work is attempted, current authoritative conditions MUST be re-evaluated according to the owning operation/process semantics.

---

## 3. Canonical Separation

Main Street SHALL distinguish:

```text
Business Deadline
    a semantic time condition owned by a capability/process

Durable Work Instruction
    durable instruction that future evaluation/attempt is required

Timer / Scheduler Entry
    infrastructure representation used to wake due work

Work Attempt
    one attributable attempt to perform/re-evaluate due work

Retry Policy
    semantic rules determining whether another attempt is valid

Scheduled/System Principal
    bounded execution principal used by background execution
```

These concepts MUST NOT be collapsed.

---

## 4. Business Deadline Is Not Timer

A deadline such as:

```text
payment must be received by 18:00
```

belongs to business/process semantics.

A timer such as:

```text
wake work at 18:00
```

is infrastructure.

Therefore:

```text
Business Deadline ≠ Timer
```

The scheduler may be late, unavailable or restarted without changing the semantic meaning of the business deadline.

---

## 5. Durable Work Instruction

A `DurableWorkInstruction` conceptually identifies one future responsibility to re-evaluate or attempt.

Where applicable it SHOULD preserve:

```text
workId
ownerContext
scope
dueAt or dueCondition
operation/process reference
correlation
causation
semantic/configuration provenance where required
retry classification
createdAt
```

The exact Java type and storage representation are implementation decisions.

The instruction MUST NOT embed arbitrary executable scripts or provider-specific business logic.

---

## 6. No Future Mutation Authority

The existence of a work instruction MUST NOT authorise later mutation by itself.

Canonical flow:

```text
Durable work becomes due
        ↓
background worker attempts execution
        ↓
Trusted Execution Context
        ↓
MS-PROT-062 runtime decision composition
        ↓
current authoritative revalidation
        ↓
capability/process-owned result
```

If current state makes the action invalid or unnecessary, the worker MUST NOT force execution merely because the work item exists.

---

## 7. Stale Scheduled Work

Scheduled work can become stale because business state changes before execution.

Example:

```text
09:00 schedule cancellation at 09:30 if unpaid
09:20 payment succeeds
09:30 timer fires
```

Required behaviour:

```text
timer fires
    ↓
re-evaluate payment/booking state
    ↓
condition no longer true
    ↓
NO_LONGER_APPLICABLE
```

The prior decision MUST NOT be treated as a reservation of future mutation authority.

---

## 8. Scheduled/System Principal

Background execution MUST use an attributable bounded principal established through MS-PROT-063.

Examples:

```text
ScheduledProcessPrincipal
SystemReconciliationPrincipal
OutboxDeliveryPrincipal
```

A background worker MUST NOT execute with implicit unrestricted `SYSTEM` authority.

At minimum, the execution context MUST preserve the scope and the operation/process responsibility the principal is permitted to invoke.

---

## 9. Background Infrastructure Does Not Own Business Meaning

Background infrastructure MAY own:

- durable work storage;
- due-time indexing;
- work claiming/locking;
- delivery attempts;
- worker liveness;
- technical retry scheduling once semantic retry permission exists;
- operational metrics about work processing.

It MUST NOT own:

- cancellation policy;
- payment policy;
- trial-reminder semantics;
- appointment eligibility;
- publication authority;
- provider reconciliation meaning;
- entitlement semantics;
- capability business invariants.

---

## 10. Work Attempt

Each processing attempt SHOULD have attributable identity sufficient for diagnostics, retry analysis and audit where required.

Conceptually:

```text
WorkAttempt
    attemptId
    workId
    attemptedAt
    principal/context
    resultClassification
    failure/evidence reference where applicable
```

Multiple attempts MAY refer to one logical work instruction.

Multiple physical attempts MUST NOT be interpreted as multiple independent business intents unless the owning semantics explicitly define them as such.

---

## 11. Delivery Semantics

Main Street MUST NOT assume exactly-once background execution.

Workers and queues may produce:

```text
duplicate delivery
retry
late delivery
worker crash after effect but before acknowledgement
out-of-order attempts
```

Business correctness MUST therefore rely on operation-specific idempotency, deduplication, authoritative state checks or reconciliation as appropriate.

The scheduler is not responsible for guaranteeing exactly-once business effects.

---

## 12. Retry Classification

Background retry MUST be semantically classified.

The canonical result categories are:

```text
RETRY_SAFE
RECONCILIATION_REQUIRED
TERMINAL_FAILURE
MANUAL_INTERVENTION_REQUIRED
NO_LONGER_APPLICABLE
SUCCESS
```

These are background/process execution classifications, not universal business lifecycle states.

The owning capability/process determines which classification applies.

---

## 13. RETRY_SAFE

`RETRY_SAFE` means another attempt may be made according to the owning retry policy without creating unacceptable duplicate business effects.

Possible examples:

- transient internal delivery failure;
- safe idempotent provider request with stable idempotency key;
- temporary infrastructure unavailability where the business operation has not become invalid.

The background subsystem MAY calculate the next technical attempt time only within the constraints supplied by the owning contract.

---

## 14. RECONCILIATION_REQUIRED

`RECONCILIATION_REQUIRED` means the prior outcome is uncertain and blind re-execution may duplicate or corrupt business effects.

Examples include:

```text
external provider timed out after request transmission
callback lost after provider may have acted
connection reset after payment/courier request
```

Required behaviour:

```text
uncertain outcome
    ↓
reconcile provider/business evidence
    ↓
determine authoritative consequence
```

The worker MUST NOT convert uncertainty into either success, failure or automatic duplicate retry without governing semantics.

---

## 15. TERMINAL_FAILURE

`TERMINAL_FAILURE` means the work cannot or should not be retried under current semantics.

Examples may include:

- permanently invalid request;
- retired provider binding with no permitted fallback;
- semantic precondition that can no longer become true;
- operation removed/superseded from the relevant captured contract where no migration exists.

Terminal failure MAY require audit, projection or intervention according to the owning process.

---

## 16. MANUAL_INTERVENTION_REQUIRED

`MANUAL_INTERVENTION_REQUIRED` means automation cannot safely determine the next authoritative action.

The work subsystem MUST surface the condition to a governed intervention path under MS-PROT-064.

It MUST NOT resolve the condition by arbitrary database edits, universal override or generic force execution.

---

## 17. NO_LONGER_APPLICABLE

`NO_LONGER_APPLICABLE` means the scheduled/retry responsibility no longer requires execution because authoritative state or configuration has changed.

Examples:

- subscription reminder due but merchant already upgraded;
- cancellation timer due but payment was received;
- notification due but relevant commitment was cancelled;
- provider retry due but binding was intentionally replaced and old work was superseded.

This result MUST NOT be treated as failure.

---

## 18. Missed and Overdue Work

Scheduler/worker outage may cause work to become overdue.

The system MUST NOT silently discard overdue work solely because its target time passed.

The owning contract MUST define the applicable overdue semantics, such as:

```text
EXECUTE_WHEN_OVERDUE
RE_EVALUATE_CURRENT_STATE
EXPIRE_WITHOUT_EXECUTION
ESCALATE
```

These are semantic handling modes; exact naming may vary in implementation.

The scheduler MUST NOT invent overdue meaning.

---

## 19. Reminder Semantics

Reminder work is an obligation to evaluate whether communication remains due when processing occurs.

Example:

```text
TrialReminderDue
    ↓
re-evaluate subscription state
    ↓
merchant already subscribed
    ↓
NO_LONGER_APPLICABLE
```

A scheduled reminder MUST NOT guarantee message delivery regardless of current business state.

Notification delivery remains separately governed by the Notification/Provider contracts.

---

## 20. Process Timers

A persistent process MAY require time-based wake-up.

Example:

```text
waiting_for_payment until deadline D
```

The process owns:

- why the deadline exists;
- what should be evaluated when it expires;
- which operation(s) may follow;
- whether the process should remain waiting, fail, compensate, notify or escalate.

The timer subsystem owns only durable wake-up/delivery.

---

## 21. Configuration Changes Before Execution

A work instruction MAY have been created under an earlier configuration or semantic release.

The owning authority MUST determine whether execution is governed by:

### 21.1 Captured Commitment Semantics

Use the captured configuration/semantic provenance because the future work belongs to an already-established commitment.

### 21.2 Current-State Eligibility

Re-evaluate the current active configuration because no enduring commitment was established.

The background subsystem MUST NOT choose between these modes itself.

Where captured semantics are required, the work instruction MUST preserve enough provenance to identify the governing configuration/semantic release.

---

## 22. Semantic Release and Migration

Long-lived work may survive software or semantic releases.

Implementation MUST preserve compatibility with outstanding work or migrate/supersede it according to MS-PROT-054.

A deployment MUST NOT silently reinterpret old durable work using incompatible new semantics.

Where a work instruction becomes invalid after controlled migration, the migration MUST record the new disposition explicitly.

---

## 23. Provider-Backed Retry

Provider interactions remain governed by MS-PROT-048.

Background work MAY retry provider fulfilment only where the provider/capability contract classifies the retry as safe.

Where outcome is uncertain, the system MUST enter reconciliation instead of blind retry.

Where provider-side idempotency exists, Main Street SHOULD preserve and reuse stable idempotency/correlation identities across safe retries.

Provider-specific HTTP/network errors MUST NOT themselves define business retry semantics.

---

## 24. Outbox and Post-Commit Delivery

Durable post-commit publication may create background work for:

```text
domain/integration event publication
notification delivery
projection update
provider side effect
```

The outbox-equivalent mechanism owns reliable delivery intent and attempts.

It MUST NOT become owner of the domain fact that produced the publication.

A business transaction MUST NOT be rolled back merely because an independent post-commit worker is temporarily unavailable.

---

## 25. Recurring Work

Recurring responsibilities MAY be implemented through:

```text
periodic scan
next-occurrence durable work
provider callback
reconciliation cursor
scheduled process wake-up
```

No single physical scheduling strategy is semantically required.

The owning platform/capability contract defines the recurrence obligation; implementation chooses the efficient scheduling technique.

---

## 26. Periodic Scans

A periodic scan is an implementation mechanism, not a business model.

Example:

```text
scan provider connections requiring reconciliation
```

MAY satisfy a reconciliation obligation.

But specifications MUST NOT encode merchant business semantics as arbitrary cron expressions where the domain concept is actually a deadline, recurrence policy or process condition.

---

## 27. Work Cancellation and Supersession

Durable work MAY become superseded or cancelled.

Cancellation of the infrastructure work item MUST be distinct from cancellation of the underlying business object.

Example:

```text
cancel reminder work
    ≠
cancel appointment
```

Work supersession SHOULD preserve traceability to the replacing work instruction or authoritative state change where material.

---

## 28. Idempotency

Each duplicate-sensitive background operation MUST define how repeated attempts are recognised.

Possible mechanisms include:

- stable command identity;
- stable business idempotency key;
- workId + operation identity;
- provider idempotency key;
- authoritative state inspection;
- deduplication record.

No universal mechanism is mandated.

The mechanism follows operation semantics.

---

## 29. Concurrency

Background execution is subject to the same concurrency rules as foreground execution.

A worker MUST revalidate concurrency-sensitive predicates inside the accepted consistency boundary.

A work instruction created from an earlier availability, entitlement or state decision MUST NOT bypass current authoritative concurrency checks.

---

## 30. Work Claiming

Infrastructure MAY use locks, leases, compare-and-set, database claims or queue ownership to reduce concurrent duplicate processing.

Such claiming is a technical coordination mechanism.

It MUST NOT be treated as business authority and MUST NOT replace operation-level concurrency protection where the domain invariant requires it.

---

## 31. Failure of Worker Infrastructure

Worker or scheduler unavailability MUST be observable.

The backend SHOULD distinguish operational conditions such as:

```text
work due but unclaimed
work claimed but worker lost
retry delayed
scheduler unavailable
queue unavailable
```

These operational states MUST NOT be copied into capability lifecycle states unless the capability explicitly models a corresponding business consequence.

---

## 32. Audit

Background attempts MAY require audit evidence under MS-PROT-064.

High-risk scheduled administrative or reconciliation actions SHOULD preserve attributable audit evidence.

Routine technical delivery attempts MAY rely on operational telemetry where durable audit evidence is not semantically required.

`WorkAttempt` telemetry and `AuditRecord` remain distinct concepts.

---

## 33. Observability

The work subsystem SHOULD expose sufficient observability to diagnose:

- backlog age;
- overdue work;
- retry volume;
- terminal failures;
- reconciliation-required work;
- manual-intervention-required work;
- worker availability;
- processing latency.

Metrics/logs/traces remain operational telemetry and do not become business authority.

---

## 34. Data Minimisation

Durable work payloads MUST contain only information needed to identify and execute/re-evaluate the responsibility.

Workers SHOULD load current authoritative data through owning contracts rather than embedding complete mutable aggregates in work payloads.

Secrets, session tokens and provider private credentials MUST NOT be copied into durable work instructions.

Use secure references to provider connections/credentials where required.

---

## 35. Merchant Scope

Merchant-scoped work MUST preserve trusted Merchant Scope or enough immutable information to resolve it safely at execution time.

Client-supplied merchant identifiers from the original request MUST NOT become trusted merely because they were persisted into work payloads.

Cross-merchant batch work requires an explicitly platform-scoped operation.

---

## 36. Platform-Scoped Work

Not all background work is merchant-scoped.

Examples include:

- semantic release maintenance;
- platform security cleanup;
- global provider catalogue maintenance;
- governance/conformance housekeeping.

Such work MUST execute under explicit platform scope rather than a fake merchant scope.

---

## 37. AI and Background Work

AI MAY help a merchant configure a registered future action where accepted semantics allow it.

AI MUST NOT create arbitrary executable jobs, scripts or workflow graphs.

The path remains:

```text
merchant intent
    ↓
AI inference
    ↓
registered policy/configuration candidate
    ↓
required approval/validation
    ↓
accepted capability/process semantics
    ↓
durable work created by deterministic system
```

AI does not become the worker's semantic authority.

---

## 38. Administrative Background Work

Scheduled administrative activity MUST comply with MS-PROT-064.

A periodic admin/security job MUST have a registered platform operation and bounded Scheduled/System Principal.

Background administration MUST NOT write arbitrary domain tables directly.

---

## 39. Internal Contract

The background work boundary SHOULD expose a narrow application-facing contract conceptually equivalent to:

```text
BackgroundWorkPort
    schedule(work specification)
    supersede/cancel(work identity) where permitted
    acknowledge/record attempt outcome
```

The exact interface is implementation detail.

Capabilities/processes supply semantic work intent; infrastructure persists and delivers it.

---

## 40. No Universal Workflow DSL

This model MUST NOT be interpreted as approval for merchants or developers to encode all business processes as generic job graphs.

MS-PROT-024 remains authoritative:

- capability state remains capability-owned;
- process coordination is introduced only where progression must survive execution boundaries;
- merchants do not author executable workflow graphs.

Background work is infrastructure supporting those semantics, not a universal workflow language.

---

## 41. Canonical Backend Graph

```text
Capability / Process / Platform responsibility
                │
                ▼
       Future work becomes required
                │
                ▼
       Durable Work Instruction
                │
                ▼
      Background Work subsystem
                │
        due / retry condition
                ▼
      Scheduled/System Principal
                │
                ▼
      Trusted Execution Context
                │
                ▼
           MS-PROT-062
                │
       ┌────────┴────────┐
       │                 │
    REJECT            PROCEED
       │                 │
       ▼                 ▼
 classify outcome   authoritative
 / reschedule /     revalidation
 reconcile /             │
 intervene                ▼
                   Capability-owned
                      execution
```

---

## 42. Explicit Non-Responsibilities

MS-PROT-065 does NOT define:

- individual capability deadlines/policies;
- specific reminder cadence;
- specific retry backoff values;
- provider retry semantics beyond the required classification boundary;
- specific scheduler technology;
- queue technology;
- distributed locking technology;
- worker deployment topology;
- workflow engine selection;
- cron syntax;
- Java class/interface representation;
- operational SLOs.

Those belong to owning capability/process authorities or downstream implementation decisions.

---

## 43. Deferred Technology Choices

The following remain implementation choices unless later semantic evidence requires otherwise:

```text
Spring Scheduler
Quartz
database-backed work queue
RabbitMQ
Kafka
SQS
Temporal
cron
distributed scheduler
locking/lease mechanism
worker topology
polling interval
backoff library
```

Technology MUST satisfy the accepted semantic contract rather than define it.

---

## 44. Hard Invariants

1. Scheduled work MUST NOT become future mutation authority.
2. Business Deadline and Timer MUST remain distinct.
3. Background infrastructure MUST NOT own business semantics.
4. Scheduled/System principals MUST remain bounded and attributable.
5. Exactly-once background business execution MUST NOT be assumed.
6. Duplicate delivery MUST be tolerated according to operation semantics.
7. Stale scheduled work MUST re-evaluate authoritative state.
8. Retry semantics MUST be owned by the relevant capability/process/provider contract.
9. Uncertain provider outcome MUST NOT be blindly retried.
10. Missed/overdue work MUST have explicit semantic handling.
11. Configuration change MUST NOT automatically cancel or preserve future work; the semantic owner decides.
12. Long-lived work MUST preserve compatible semantic/configuration provenance where required.
13. Background workers MUST NOT write capability-owned persistence directly as a normal execution path.
14. Infrastructure work claiming MUST NOT replace domain concurrency protection.
15. Work cancellation MUST remain distinct from cancellation of the underlying business object.
16. Durable work payloads MUST obey data minimisation and MUST NOT contain secrets.
17. Merchant-scoped work MUST preserve trusted scope.
18. Platform-scoped work MUST NOT be represented through a fake merchant.
19. AI MUST NOT author arbitrary executable jobs/workflows.
20. Missing safe automation semantics MUST escalate to reconciliation, manual intervention or design rather than generic force execution.

---

## 45. Falsification Summary

The model has been tested against:

- stale scheduled cancellation after payment;
- worker crash and duplicate delivery;
- trial reminder after merchant subscription;
- recurring reconciliation;
- scheduled execution under bounded SYSTEM authority;
- merchant configuration change before work execution;
- uncertain external provider timeout;
- business deadline versus scheduler timer;
- overdue work after worker outage;
- large numbers of future timers;
- long-lived work across semantic releases;
- provider idempotency and reconciliation;
- background concurrency;
- AI-configured future actions.

No tested scenario requires the scheduler to own business meaning or bypass capability/runtime authority.

---

## 46. Acceptance Statement

MS-PROT-065 establishes durable background execution as a supporting infrastructure responsibility within Main Street's composite architecture.

The canonical rule is:

> **The semantic owner decides what future work means; the background subsystem remembers when to attempt it and delivers that attempt back through the normal authoritative execution path.**
