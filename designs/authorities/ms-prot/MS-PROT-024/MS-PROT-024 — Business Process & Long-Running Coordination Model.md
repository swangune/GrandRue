# MS-PROT-024 — Business Process & Long-Running Coordination Model

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020, MS-PROT-021, MS-PROT-022, MS-PROT-023  
**Purpose:** Define how Main Street coordinates business activity that spans multiple commands, actors, capabilities, external systems or long periods without turning `Command`, `Operation` or capability composition into a universal workflow engine.

## 1. Core problem

A `Command` is a bounded attempt to perform an `Operation`, but many business activities span multiple commands:

```text
Gardener:
Enquiry → Inspection → Quote → Acceptance → Schedule → Work → Completion

Motel:
Request → Payment → Allocation → Confirmation → Check-in → Stay → Check-out

Restaurant:
Order → Acceptance → Payment → Preparation → Ready → Fulfilment → Completion
```

These may involve hours or months, human decisions, external systems, retries and several capabilities. One Command cannot safely represent the whole process.

## 2. Governing principle

> **Authoritative business state remains capability-owned. Long-running coordination owns only the progression needed to observe facts, wait and issue valid commands.**

```text
Capability-owned state
        ▲
        │ Commands / Events
        │
Process Coordinator
        │
        ├── observes facts
        ├── waits for time/events/people
        ├── retains coordination context
        └── issues authorised Commands
```

A coordinator never becomes the authoritative state machine for all participating capabilities.

## 3. No universal process state

A business may simultaneously have:

```text
Quote = ACCEPTED
Job = SCHEDULED
Invoice = UNPAID
Payment = PENDING
```

Main Street does not collapse these into a synthetic universal status.

A coordinator may store coordination states such as `waiting_for_payment`, `waiting_for_customer`, `retry_due` or `manual_intervention_required`, but these are not replacements for capability-owned business truth.

## 4. Coordination is optional

Not every lifecycle requires a persistent coordinator.

```text
OrderConfirmed
    ├── Notification reacts
    ├── Analytics reacts
    └── CustomerHistory reacts
```

These may be independent event reactions.

Persistent coordination exists only when progression must be remembered across execution boundaries, e.g. waiting for payment, customer acceptance, time, an external callback or restart recovery.

## 5. Collaboration modes

### Coordinated process

```text
observe fact → retain context → wait/decide → issue Command → observe result
```

### Independent reaction

```text
DomainEvent → independent consumer
```

These mechanisms must not be collapsed.

## 6. Process semantics and merchant configuration

Process semantics remain platform/capability-owned.

Merchant configuration may select or parameterise registered behaviour such as payment timing, approval requirement, allocation mode or cancellation policy. It may not author arbitrary workflow graphs, conditions or scripts.

No universal merchant workflow DSL is accepted.

## 7. Coordinator authority

A coordinator cannot mutate participating capability state directly.

```text
Coordinator
    ↓ issues Command
Owning capability evaluates authority/invariants
    ↓
Authoritative mutation
```

> **Coordination owns progression, not participating capability state.**

Causation does not grant downstream authority. Each issued Command requires its own legitimate execution principal and authority.

## 8. Stale decisions and consistency

Stored coordination context cannot become mutation authority.

A previous availability result, for example, must be revalidated before a concurrency-sensitive allocation/commitment.

```text
Process context
      ↓
Command
      ↓
Authoritative runtime revalidation
      ↓
Mutation
```

Long-running coordination does not weaken MS-PROT-023 consistency rules.

## 9. Delivery, duplicates and ordering

Process correctness cannot depend on exactly-once delivery.

Where relevant it must tolerate:

```text
duplicate events
duplicate commands
retries
late delivery
out-of-order delivery
```

through appropriate idempotency, deduplication, causal/version information and authoritative state checks.

Arrival order alone is not necessarily domain order.

## 10. Compensation and failure

Compensation is a new forward business action, not rollback of history.

```text
PaymentCaptured → later failure → RefundPayment
```

The refund does not erase the original payment fact.

Not every failure requires compensation. Possible semantic outcomes include retry, business rejection, terminal failure, compensation, manual intervention or non-blocking failure. Retry is permitted only where duplicate/retry semantics make it safe.

## 11. Waiting, time and people

A process may wait for:

```text
time
customer
merchant
external provider
another DomainEvent
```

Timeout meaning remains process-owned. A payment deadline may cancel a pending booking; a solicitor response deadline may merely alert staff.

If automation cannot safely decide, the process may require manual intervention. Manual intervention still uses legitimate capability Operations and is not a generic invariant bypass.

## 12. Identity, correlation and durability

A persistent coordinator needs stable identity and unambiguous correlation.

```text
correlation = which process/business context?
causation   = what prior action/fact led here?
```

If coordination spans restart, external callback or long delay, required coordination state must be durable. Potential information includes process identity, correlation, waiting conditions, deadlines, retry state and duplicate-handling state.

No persistence technology is selected here.

## 13. External systems

External providers may remain authoritative for their own facts, such as payment success, courier acceptance or external calendar state.

Main Street consumes authenticated/validated provider facts and reconciles local state when necessary. Uncertain external outcomes must not be silently converted into success or failure.

## 14. Process completion

Process completion means the coordinator has fulfilled its coordination responsibility. It does not imply all associated business objects are terminal.

```text
Booking confirmation process = COMPLETE
Booking = still ACTIVE
```

Event Sourcing is not required.

## 15. Capability boundaries

A process may span capabilities but depends on published capability contracts rather than implementation internals or persistence schemas. This prevents coordinators from becoming cross-module god objects.

## 16. Falsification findings

The following assumptions were tested and rejected:

| Failed assumption | Why it fails |
|---|---|
| Every process has one universal state | Independent capability states create combinatorial state explosion |
| Every cross-capability action needs a coordinator | Independent reactions do not |
| Merchants may author workflow graphs | Configuration becomes programming |
| Coordinator may directly mutate capability state | Bypasses ownership, authority and invariants |
| Initiating authority propagates downstream | Creates authority leakage |
| Stored availability can authorise later allocation | Availability becomes stale |
| Exactly-once delivery can be assumed | Retries/duplicates occur |
| Arrival order equals business order | Messages may reorder |
| Compensation restores previous reality | Business history cannot be erased |
| Every failure needs retry/compensation | Many failures are non-blocking or terminal |
| Generic retry is always safe | Duplicate-sensitive effects can repeat harmfully |
| Every lifecycle needs persistent coordination | Many need only capability state |
| One workflow DSL can model all businesses | Recreates universal-workflow complexity |
| Every timeout is failure | Meaning is process-specific |
| Manual intervention may bypass invariants | Destroys semantic integrity |

## 17. Cross-domain validation

- **Gardener:** waiting for quote acceptance may require coordination; Quote/Job remain authoritative. **PASS**
- **Driving instructor:** purchase, lesson entitlement and scheduling remain separate; cancellation consequences are semantic, not generic compensation. **PASS**
- **Solicitor:** simple appointment confirmation may need no coordinator; document-response flows may. **PASS**
- **Restaurant:** ordinary order lifecycle can remain capability state; external courier interaction may require coordination. **PASS**
- **Grocery:** inventory commitment may require immediate consistency, delivery progression long-running coordination, notifications independent reaction. **PASS**
- **Motel:** payment callbacks may require coordination; allocation still requires authoritative revalidation. **PASS**
- **Mechanic:** quote acceptance/scheduling may wait for customer action while service state remains capability-owned. **PASS**
- **Realtor:** enquiry may be one Command; viewing approval may require coordination. **PASS**

## 18. Accepted invariants

1. `Command` is not a long-running process.
2. Authoritative business state remains capability-owned.
3. No universal process status exists.
4. Persistent coordination is introduced only where progression must survive execution boundaries.
5. Independent reactions do not require central orchestration.
6. Merchants cannot author executable process graphs.
7. Coordinators act through authorised Commands/Operations.
8. Causation does not grant authority.
9. Stale decisions cannot authorise later authoritative mutation.
10. Exactly-once delivery is not assumed.
11. Compensation is forward business action, not rollback.
12. Retry and timeout semantics are process-specific.
13. Persistent process instances require stable identity/correlation and recoverable state.
14. External systems may remain authoritative for their own facts.
15. Manual intervention does not automatically bypass invariants.
16. Process completion and business-object completion are distinct.
17. Process coordination uses published capability contracts, not internals.
18. Event Sourcing and a universal workflow DSL are not required.

## 19. Deferred decisions

Workflow engine, scheduler, broker, database, event store, retry library, saga framework, outbox implementation, process persistence format and event envelope remain downstream decisions.

## Governance verdict

**ACCEPTED.** The original central-process/single-state proposal failed falsification; the reduced capability-owned coordination model survived cross-domain validation.
