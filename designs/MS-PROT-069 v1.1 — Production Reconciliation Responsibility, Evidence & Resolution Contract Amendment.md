# MS-PROT-069 v1.1 — Production Reconciliation Responsibility, Evidence & Resolution Contract Amendment

**Document ID:** MS-PROT-069  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-069 v1.0 only within production reconciliation identity, initiation, evidence acquisition, historical affinity, automatic/manual resolution, completion, ageing/escalation and discrepancy handling  
**Preserves:** transport failure is not business failure; acceptance/execution/acknowledgement certainty remain distinct; `EXECUTION_UNCERTAIN` remains explicit; blind retry prohibited without independent safety; provider evidence is not business truth; current authority revalidation; owner-specific business mutation; no generic offline authority  
**Depends on:** MS-PROT-023; MS-PROT-024; MS-PROT-025; composite MS-PROT-026 through v1.1; composite MS-PROT-027; composite MS-PROT-048; composite MS-PROT-053; composite MS-PROT-055; composite MS-PROT-060; composite MS-PROT-061; MS-PROT-062; MS-PROT-064; composite MS-PROT-065 through v1.1; composite MS-PROT-068 through v1.1; MS-PROT-069 v1.0; MS-PROT-070; MS-PROT-072; composite MS-PROT-075; MS-PROT-079  
**Closes with MS-PROT-068 v1.1:** MS-PROT-079 Target 19 — Observability / reconciliation  
**Purpose:** Make reconciliation production-executable without introducing a universal ReconciliationCase, making operational tooling a business owner, confusing reconciliation with retry or projection rebuild, or permitting operators to force an outcome unsupported by authoritative evidence.

---

## 1. Governing Decision

MS-PROT-069 v1.0 survives:

> Transport failure is evidence about communication, not proof of business-operation failure.

Target 19 adds:

> **Reconciliation is an owner-qualified process for resolving an already-identified uncertainty or material discrepancy by obtaining and interpreting sufficient authoritative/provider evidence and, where necessary, invoking the authority that owns the resulting Main Street fact.**

Canonical:

```text
uncertain or discrepant condition
        ↓
ReconciliationContract
        ↓
evidence acquisition
        ↓
owner-qualified interpretation
        ↓
outcome established?
   │                 │
 YES                NO
   │                 │
owner operation      preserve uncertainty
where required       / escalate
```

---

## 2. Reconciliation Is Not Retry

Canonical distinction:

```text
Retry
    repeat a safely repeatable logical execution

Reconciliation
    determine what actually happened
    or what authoritative state should now recognise

Compensation
    new forward authoritative action
    after an established prior fact
```

These mechanisms MUST NOT be collapsed.

---

## 3. Reconciliation Is Not Projection Repair

Projection repair begins from established source truth and repairs a derived representation.

Reconciliation exists because a material outcome or discrepancy cannot yet safely be interpreted.

```text
known source → rebuild projection
    = repair

unknown/disputed effect → obtain evidence → interpret
    = reconciliation
```

---

## 4. ReconciliationContract

Every materially supported reconciliation class SHALL be governed by an owner-qualified **ReconciliationContract**.

Conceptually:

```text
ReconciliationContract
{
    contractIdentity
    semantic owner
    reconciliation purpose
    source uncertainty/discrepancy class
    exact subject/reference rule
    evidence sources
    historical affinity requirements
    current-state revalidation requirements
    permitted automatic resolution
    manual-intervention rule
    result interpretation rule
    retry / evidence-acquisition rule
    completion condition
    audit requirement
    data-minimisation requirement
}
```

It is not a generic workflow language.

---

## 5. No Universal ReconciliationCase

Target 19 does NOT introduce:

```text
ReconciliationCase
ReconciliationStatus
ReconciliationOrder
UniversalDiscrepancy
```

as new business Operational Objects.

Existing authoritative identities already identify the underlying question.

Examples:

```text
PaymentExecutionRequest
RefundExecutionRequest
ShipmentPreparationRequest
ReturnLabelPreparationRequest
DeliveryAttempt
provider-side data-disposition obligation
```

---

## 6. Logical Reconciliation Identity

One logical reconciliation responsibility is identified by:

```text
ReconciliationContract identity
+
exact uncertain/discrepant subject
+
required historical execution affinity
```

Duplicate detection of the same unresolved condition MUST converge on the same logical reconciliation responsibility.

It MUST NOT manufacture independent business intentions.

---

## 7. Durable Progression

If reconciliation must survive restart, delay, provider outage, retry or manual escalation, it SHALL use MS-PROT-065 v1.1 durable background progression.

Canonical:

```text
logical reconciliation responsibility
        ↓
DurableWorkInstruction
        ↓
0..n WorkAttempts
        ↓
ReconciliationContract evaluation
```

The DurableWorkInstruction owns progression evidence only.

It does not own the business outcome.

---

## 8. Trigger Conditions

A ReconciliationContract may be activated by accepted conditions including:

```text
EXECUTION_UNCERTAIN
authenticated provider evidence inconsistent
with current interpreted Main Street state
missing expected provider evidence after an accepted effect
duplicate/conflicting provider evidence
post-commit consequence whose downstream result
cannot be safely determined
provider-side lifecycle/disposition uncertainty
manual discovery of a material provider discrepancy
```

An operational Alert by itself is insufficient business evidence.

It may cause the applicable reconciliation responsibility to be evaluated.

---

## 9. Detection Is Not Resolution

Canonical:

```text
telemetry detects anomaly
        ↓
reconciliation eligibility evaluated
        ↓
reconciliation begins if contract applies
```

Never:

```text
metric threshold exceeded
        ↓
business correction
```

---

## 10. Evidence Sources

A ReconciliationContract SHALL identify admissible evidence sources.

Examples may include:

```text
Main Street authoritative records
durable provider request/effect identity
authenticated provider callback
provider query result
provider transaction record
connection/binding provenance
historical request/response evidence
Audit evidence where evidentiary context is required
```

Logs, traces or metrics MAY help locate evidence but are not automatically authoritative inputs.

---

## 11. Provider Evidence Boundary

Provider evidence must pass the existing sequence:

```text
authenticate / validate source
        ↓
correlate exact provider effect
        ↓
resolve historical provider context
        ↓
registered provider interpretation
        ↓
owner validation
```

Provider-native status vocabulary does not become Main Street business vocabulary merely because reconciliation is occurring.

---

## 12. Historical Provider Affinity

Reconciliation SHALL preserve the exact historical execution path where material:

```text
provider
ProviderConnection
Fulfilment Binding revision
PlatformFulfilmentBindingSetRevision where applicable
credential generation/reference where evidentially required
provider correlation/idempotency identity
request identity
```

Current routing MUST NOT substitute for the path involved in the uncertain historical effect.

Canonical:

```text
effect attempted using Provider A
provider routing later moves to Provider B

reconcile old effect
    → Provider A context
```

not Provider B.

---

## 13. Existing Obligation vs New Activity

Loss of current readiness for new work does not automatically prevent bounded reconciliation of an already-accepted historical operation.

A ReconciliationContract may use residual provider access where separately authorised.

This does not restore general readiness for new activity.

---

## 14. Evidence Acquisition Is Normally Non-Mutating

Where a provider supports a non-mutating query to determine an existing effect, reconciliation SHOULD prefer that evidence path before another side-effecting execution.

A read/query failure may normally be retried under safe resilience rules.

A new external side effect is not evidence acquisition and requires its own owner/provider operation contract.

---

## 15. Execution Certainty Resolution

For reconciliation of an uncertain side effect, the existing certainty vocabulary survives.

Reconciliation may establish:

```text
KNOWN_EXECUTED
KNOWN_NOT_EXECUTED
EXECUTION_UNCERTAIN
```

as appropriate.

It SHALL NOT invent a universal `RECONCILED_SUCCESS` business status.

---

## 16. KNOWN_EXECUTED

Where sufficient evidence establishes that the historical effect occurred:

```text
EXECUTION_UNCERTAIN
        ↓
sufficient evidence
        ↓
KNOWN_EXECUTED
```

the owning capability determines whether a missing Main Street interpretation/fact must now be committed.

Examples:

```text
Payment
    interpret provider evidence → PaymentApplication

Notification
    interpret provider evidence → DeliveryEvidence

Shipment
    interpret provider evidence → Shipment-owned fact where applicable
```

The reconciliation layer does not write those facts itself.

---

## 17. KNOWN_NOT_EXECUTED

Where sufficient evidence establishes that the prior effect did not occur:

```text
EXECUTION_UNCERTAIN
        ↓
sufficient evidence
        ↓
KNOWN_NOT_EXECUTED
```

the original uncertainty is resolved.

A later retry remains a new execution decision subject to the governing logical-intent/idempotency and current runtime requirements.

Reconciliation does not automatically perform that retry.

---

## 18. Remains Uncertain

If admissible evidence cannot establish an outcome:

```text
EXECUTION_UNCERTAIN
```

survives.

Time passing alone MUST NOT convert uncertainty into success or failure.

---

## 19. Conflicting Evidence

If trusted evidence conflicts:

```text
provider evidence A
    conflicts with
provider evidence B
    or
Main Street authoritative evidence
```

Main Street SHALL preserve the conflict.

It MUST NOT use:

```text
last callback wins
latest timestamp wins
provider dashboard wins
operator preference wins
```

as a universal rule.

The owning ReconciliationContract determines whether deterministic interpretation remains possible.

Otherwise manual intervention is required.

---

## 20. Automatic Reconciliation

Automatic reconciliation is permitted only where:

```text
admissible evidence is sufficient
+
registered interpretation is deterministic
+
the resulting owner operation is authorised
+
duplicate behaviour is safe
```

Automatic reconciliation is not permission for heuristic business judgement.

---

## 21. Manual Intervention

Manual reconciliation is used when deterministic automatic resolution is unsafe or unavailable.

The path SHALL be:

```text
authorised operator
        ↓
trusted execution context
        ↓
registered reconciliation/intervention operation
        ↓
evidence inspection
        ↓
owner operation
        ↓
audit evidence
```

No direct database edit is an ordinary reconciliation operation.

---

## 22. Operator Cannot Force Reality

Rejected:

```text
mark payment successful
mark shipment delivered
mark provider deletion complete
mark notification delivered
force reconciled
```

solely because an operator wishes to clear an exception.

The operation must express an accepted owner-specific fact or correction supported by the evidence/invariants appropriate to that owner.

---

## 23. Audit

Material manual reconciliation decisions SHALL produce Audit evidence under MS-PROT-064.

Provider/integration reconciliation remains an auditable action class.

Audit evidence records the decision/action and provenance.

It does not replace the resulting owner business fact.

Automatic reconciliation MAY also require durable audit evidence where its governing risk/evidentiary contract requires it.

---

## 24. Reconciliation Completion

One logical reconciliation responsibility is complete only when the ReconciliationContract's exact question has reached an accepted disposition.

Examples:

```text
outcome established as KNOWN_EXECUTED
and required owner interpretation committed

outcome established as KNOWN_NOT_EXECUTED

condition authoritatively determined no longer applicable

manual intervention completed through accepted owner operation
```

The mere completion of a WorkAttempt is insufficient.

The mere acknowledgement of an Alert is insufficient.

---

## 25. Escalation

A ReconciliationContract SHALL define when unresolved automatic reconciliation requires escalation where indefinite automated waiting would be operationally unsafe.

The trigger may depend on:

```text
age
attempt history
provider capability
risk
business significance
evidence conflict
owner policy
```

No universal numeric duration is imposed.

Escalation yields an operational/manual-intervention responsibility.

It does not manufacture a business outcome.

---

## 26. Ageing

Operational tooling may expose reconciliation age.

Age is diagnostic/progression evidence.

Canonical:

```text
uncertain for 24 hours
    ≠ failed
    ≠ succeeded
```

unless the owning business semantics separately define a real deadline consequence.

---

## 27. Duplicate Reconciliation Attempts

Multiple WorkAttempts for one logical reconciliation responsibility MUST NOT multiply business effects.

If attempt A causes an owner operation to commit and the worker loses acknowledgement, attempt B must recover the existing downstream logical outcome rather than create a second operation.

Target 18 downstream-intent affinity applies.

---

## 28. Concurrent Evidence

Concurrent callbacks, scheduled queries and operator actions may all concern one reconciliation responsibility.

They must converge through stable logical identity and owner concurrency/idempotency controls.

Arrival order is not authority.

---

## 29. Payment and Refund

Initial production reconciliation SHALL support the existing Payment-owned execution identities:

```text
PaymentExecutionRequest
RefundExecutionRequest
```

The contract preserves exact PaymentApplication/provider-path affinity.

A reconciliation layer may acquire provider evidence and invoke Payment interpretation.

It does not create PaymentApplication or Refund directly.

---

## 30. Shipment

An uncertain `ShipmentPreparationRequest` is reconciled against its exact historical provider effect.

Confirmation of provider preparation does not create extra Order Fulfilment quantity.

Shipment authority interprets the evidence.

---

## 31. Returns

An uncertain `ReturnLabelPreparationRequest` is reconciled only within the applicable historical Returns/provider context.

Reconciliation does not approve a return, issue a Refund or restock Inventory.

---

## 32. Notification

An uncertain Notification `DeliveryAttempt` is reconciled against the exact attempt/provider-effect identity.

A provider status may establish Notification Delivery Evidence after registered interpretation.

It does not establish business acknowledgement.

No second DeliveryAttempt is created merely because the first result was initially uncertain.

---

## 33. Data Protection Provider Disposition

Where an external provider must disposition data and Main Street lacks sufficient evidence of completion, the provider-side obligation remains unresolved.

Reconciliation may acquire provider evidence.

It MUST NOT claim provider deletion merely because Main Street deleted its own local copy.

---

## 34. ProviderConnection Validation

Periodic/background ProviderConnection validation is ordinarily current-state operational validation.

It is not historical side-effect reconciliation unless an exact uncertain historical effect is being resolved.

This prevents every health check from becoming a reconciliation workflow.

---

## 35. Projection Convergence

Projection lag or failed rebuild is normally Projection recovery, not reconciliation.

If a projection conflict exposes uncertainty in its authoritative source, that source uncertainty must be resolved by the owning authority first.

Projection machinery cannot reconcile business truth.

---

## 36. Event Reactions and Background Work

Where an Event Reaction or DurableWorkInstruction is uncertain only because worker acknowledgement was lost:

```text
resolve downstream logical outcome
```

before retrying a duplicate-sensitive consequence.

This is operational reconciliation of progression, not creation of a new business intent.

---

## 37. Observability

MS-PROT-068 v1.1 may report reconciliation backlog, age, evidence-acquisition failure and escalation.

Telemetry never establishes reconciliation completion.

Observability loss does not erase the durable reconciliation responsibility.

---

## 38. Safe Merchant / Customer Status

A merchant or customer may receive a safe projection such as:

```text
payment confirmation pending
delivery update pending
```

where applicable.

The projection MUST NOT disclose unsupported provider conclusions or internal operational details.

Exact API representation is Target 20 scope.

---

## 39. AI

AI may:

```text
summarise evidence
group diagnostics
suggest an authorised next investigation step
```

AI MUST NOT:

```text
decide an uncertain financial effect happened
choose last-write-wins provider truth
force reconciliation complete
retry an uncertain side effect
invent owner facts
```

---

## 40. No Global ACID Reconciliation Transaction

Reconciliation may involve evidence acquisition followed by one or more separately owned operations.

No universal transaction spans:

```text
provider
Payment
Shipment
Notification
Audit
background infrastructure
```

Each authority retains its consistency boundary.

---

## 41. Failure Classification

Reconciliation execution SHALL distinguish, as applicable:

```text
evidence unavailable
evidence invalid/untrusted
evidence insufficient
evidence conflicting
known executed
known not executed
still uncertain
owner operation rejected
authoritative conflict
manual intervention required
technical failure
```

These may be represented using existing classifications where already accepted.

A new universal business status is not required.

---

## 42. Falsification

### Provider charged; response lost

Payment execution times out.

Provider query confirms the exact historical charge.

Reconciliation records/adopts authenticated provider evidence and Payment authority interprets it.

No second charge occurs.

### Provider did not charge

Provider evidence establishes no effect.

Execution becomes known-not-executed.

Any later payment attempt re-enters current Payment/provider admission.

### Provider cannot answer

The uncertainty remains.

Automatic querying may continue according to the contract.

After applicable escalation criteria, manual intervention is requested.

The system does not invent failure.

### Provider changed after timeout

Original effect used Provider A.

Current routing uses Provider B.

Reconciliation remains affined to Provider A.

### Notification worker crashes after send

DeliveryAttempt already exists and provider effect may have occurred.

Recovery reconciles that exact attempt.

No new Dispatch or physical effect is manufactured merely because WorkAttempt acknowledgement was lost.

### Duplicate callbacks

Multiple identical callbacks correlate to the same historical effect.

They do not multiply Main Street facts.

### Conflicting callbacks

Two valid provider messages disagree.

The conflict is preserved and interpreted by the owner contract.

Last arrival does not automatically win.

### False alert

An operational alert incorrectly suggests provider trouble.

No reconciliation result or business mutation occurs merely from the alert.

### Operator tries to clear queue

Operator cannot set `reconciled=true`.

An accepted intervention must resolve the underlying exact question through owner authority.

### Projection lag

A projection is stale while source authority is known.

Projection rebuild occurs.

No business reconciliation object is created.

### Provider-side data deletion

Local deletion succeeded but provider deletion is uncertain.

Provider disposition obligation remains unresolved until sufficient evidence establishes its own outcome.

### Telemetry outage

Telemetry exporter fails during an uncertain provider operation.

The durable request/reconciliation responsibility remains recoverable because telemetry is not its authority.

### Current provider disconnected

New provider activity is NOT_READY.

Bounded historical reconciliation may still proceed where residual historical access is explicitly authorised.

All cases preserve business and provider ownership.

---

## 43. Alternatives and Trade-offs

Rejected: universal Reconciliation Service owning canonical state. It would become a second business owner.

Rejected: a universal `ReconciliationCase` aggregate. Existing owner/request/work identities already define the exact question and lifecycle.

Rejected: use Audit as reconciliation state. Audit preserves evidence, not business truth or progression authority.

Rejected: allow operational dashboards to directly edit authoritative tables. This bypasses owner invariants.

Rejected: treat reconciliation as retry. It would duplicate uncertain external effects.

Rejected: provider-dashboard-last-write-wins. Provider evidence still requires authentication, correlation and owner interpretation.

Accepted: owner-qualified ReconciliationContracts using existing uncertain-operation identities, durable background progression where required, authenticated evidence, owner operations and audited manual intervention.

---

## 44. Deferred / Future Scope

Deferred:

```text
provider-specific reconciliation APIs
provider-specific status mapping
exact reconciliation persistence tables
whether separate reconciliation indexes are required
exact scheduler cadence
backoff timing
numeric escalation thresholds
bulk reconciliation scans
operator dashboard
operator API
merchant/customer pending-status API
reconciliation reporting
incident-management integration
automated anomaly detection
AI operational assistance
exact audit presentation
SQL/indexing/partitioning
```

These choices must preserve this authority.

---

## 45. Conformance / Acceptance Criteria

Another engineer must be able to determine for every supported reconciliation class:

```text
what exact uncertainty/discrepancy is being resolved
who owns that question
what stable identity defines one logical responsibility
which evidence sources are admissible
which historical provider path applies
whether automatic resolution is allowed
what owner operation may commit a result
what happens if evidence remains insufficient
when manual intervention is required
what must be audited
how duplicate/concurrent reconciliation behaves
when reconciliation is actually complete
```

If implementation requires inventing one of these answers, the design is incomplete.

---

## 46. Acceptance Statement

Main Street reconciliation resolves uncertainty without replacing the authority that owns the underlying truth.

> **Detect operational discrepancies without treating detection as truth; reconcile the exact historical effect using admissible evidence; preserve uncertainty when proof is insufficient; commit consequences only through the owning authority; and escalate without inventing reality.**
