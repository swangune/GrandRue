# MS-PROT-068 v1.1 — Production Operational Evidence, Health & Alerting Contract Amendment

**Document ID:** MS-PROT-068  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-068 v1.0 only within production operational-evidence registration, minimum diagnosability, health interpretation, asynchronous/background observability, alerting and reconciliation visibility  
**Preserves:** telemetry is not business truth; Log ≠ AuditRecord; Trace ≠ Business Process; HealthSignal ≠ Provider Readiness; Alert ≠ DomainEvent; ordinary telemetry failure does not normally fail business execution; observability remains replaceable  
**Depends on:** composite MS-PROT-026 through v1.1; composite MS-PROT-027; composite MS-PROT-048; composite MS-PROT-053; MS-PROT-062; MS-PROT-064; composite MS-PROT-065 through v1.1; MS-PROT-067; MS-PROT-068 v1.0; composite MS-PROT-069 through v1.1; MS-PROT-070; MS-PROT-072; composite MS-PROT-075  
**Closes with MS-PROT-069 v1.1:** MS-PROT-079 Target 19 — Observability / reconciliation  
**Purpose:** Make operational diagnosability production-constraining without converting telemetry, health, alerts or operational tooling into business authority.

---

## 1. Governing Decision

MS-PROT-068 v1.0 survives unchanged:

> Main Street observes operational reality without allowing the machinery that observes it to become the authority that defines business reality.

Target 19 adds:

```text
authoritative execution
        ↓
bounded operational evidence
        ↓
health / diagnostic interpretation
        ↓
operator or resilience awareness

NOT

telemetry
        ↓
business truth
```

Operational visibility must be sufficient to detect and investigate materially unsafe or stuck execution, but observability remains evidence rather than mutation authority.

---

## 2. Operational Evidence Contract

A materially operational responsibility SHALL have an explicit **Operational Evidence Contract** where inability to diagnose that responsibility could prevent safe production operation or recovery.

Conceptually:

```text
OperationalEvidenceContract
{
    contractIdentity
    owning responsibility
    observation scope
    evidence families
    safe correlation references
    health interpretation where applicable
    freshness / validity interpretation
    failure / backlog evidence
    alerting or escalation eligibility
    data-minimisation requirements
    retention classification
    audience / access restrictions
}
```

This is not a merchant capability, business aggregate or arbitrary telemetry DSL.

The exact Java registration representation is implementation scope.

---

## 3. Applicability

An Operational Evidence Contract is required where a responsibility materially involves one or more of:

```text
durable asynchronous progression
external provider interaction
execution uncertainty
manual operational recovery
material projection convergence
security-sensitive infrastructure
data-lifecycle convergence
release / deployment admission
persistent backlog whose age matters
```

This does not require one identical metric portfolio for every subsystem.

---

## 4. Evidence Families Remain Distinct

The accepted evidence families remain:

```text
Diagnostic Log
Operational Metric
Trace
Health Observation
```

An Alert remains a derived operational reaction.

No `ObservabilityRecord` aggregate is introduced.

---

## 5. Safe Correlation

Operational evidence MAY correlate to stable references including, where applicable:

```text
Merchant Scope
logical Command identity
Domain Event identity
Event Reaction identity
DurableWorkInstruction identity
WorkAttempt identity
PaymentExecutionRequest
RefundExecutionRequest
ShipmentPreparationRequest
ReturnLabelPreparationRequest
Notification DeliveryAttempt
ProviderConnection
provider-effect correlation identity
Projection contract/checkpoint
DataLifecycleContract scope
```

Correlation does not create authority.

Hard separation:

```text
TraceId
    ≠ Command identity
    ≠ Event identity
    ≠ Work identity
    ≠ provider-effect identity
```

---

## 6. Minimum Production Diagnosability

Where the applicable responsibility exists, production evidence SHALL be sufficient to answer the operationally material questions appropriate to that responsibility.

For asynchronous work these include:

```text
Is work outstanding?
How old is the oldest materially outstanding work?
Is progression occurring?
Are attempts failing repeatedly?
Has automatic progression exhausted?
Is reconciliation or manual intervention required?
```

For provider interaction these include:

```text
Which provider path is affected?
Is the issue provider-wide or connection-specific?
Was an effect never attempted, known completed,
known not completed, or uncertain?
Is reconciliation outstanding?
```

For materialised/maintained projections these include:

```text
Can the representation currently be served?
What authoritative source/checkpoint does it represent?
Is rebuild/update progression failing or lagging?
```

The contract need not expose these as literal metric names.

---

## 7. Event-Reaction Evidence

For durable Event Reactions governed by MS-PROT-026 v1.1, operational evidence SHOULD make materially stuck reaction progression detectable.

Observability may identify:

```text
pending reactions
oldest pending age
attempt/failure classifications
reaction completion lag
manual/reconciliation escalation
```

It MUST NOT acknowledge or complete the Event Reaction.

---

## 8. Background-Work Evidence

For MS-PROT-065 v1.1 responsibilities, observability may report:

```text
outstanding DurableWorkInstructions
oldest-work age
WorkAttempt volume
retry-safe failures
reconciliation-required work
manual-intervention-required work
terminal failures
no-longer-applicable resolution
```

Observability does not decide retry, exhaustion or disposition.

---

## 9. Provider Health

Provider-wide health remains operational evidence.

It MUST remain distinct from:

```text
ProviderConnection
Provider Readiness
provider execution outcome
provider business evidence
```

A provider-wide outage signal may contribute to readiness resolution but cannot itself rewrite ProviderConnection or business state.

---

## 10. Provider Readiness Visibility

Where Provider Readiness materially blocks execution, operations must be able to distinguish enough evidence to diagnose, as applicable:

```text
READY
DEGRADED
NOT_READY
UNKNOWN
```

without treating the observability representation as the readiness authority.

The authoritative runtime resolver remains the source of the readiness verdict.

---

## 11. Execution-Uncertainty Visibility

Operational evidence SHALL make materially unresolved execution uncertainty discoverable.

It must be possible to distinguish, where applicable:

```text
known pending
known executed
known not executed
execution uncertain
reconciliation outstanding
manual intervention required
```

These remain uncertainty/progression classifications rather than capability business states.

---

## 12. Reconciliation Visibility

For a durable reconciliation responsibility under MS-PROT-069 v1.1, observability MAY expose:

```text
responsibility age
last evidence acquisition time
last WorkAttempt
automatic-reconciliation progress
resolution classification
manual escalation condition
```

Observability SHALL NOT provide a `mark reconciled` mutation shortcut.

---

## 13. Projection Repair Is Not Business Reconciliation

A Projection Contract may expose:

```text
source/checkpoint lag
rebuild failure
serviceability degradation
```

Repair or rebuild from known authoritative source state is projection recovery.

It MUST NOT be represented as business reconciliation merely because operational tooling detects the problem.

Canonical distinction:

```text
Projection repair
    known source truth → rebuild derived representation

Reconciliation
    uncertainty/discrepancy → establish authoritative interpretation
```

---

## 14. Data-Lifecycle Convergence

Operational evidence may identify:

```text
lifecycle review overdue
owner disposition pending
provider disposition outstanding
projection/search convergence incomplete
```

The evidence does not establish that deletion, anonymisation, provider disposition or lifecycle completion occurred.

Composite MS-PROT-053 remains the authority.

---

## 15. Health Interpretation

Health remains subject- and dimension-qualified.

No universal `MainStreetHealthy` state is accepted.

A health interpretation must identify enough scope to distinguish failures such as:

```text
platform service
provider family
provider connection
background responsibility
projection
surface
dependency
```

Exact persisted enums and endpoint formats remain implementation choices.

---

## 16. UNKNOWN Must Remain Representable

Where sufficient evidence cannot be established, operational interpretation SHALL preserve uncertainty.

The system MUST NOT transform:

```text
telemetry missing
```

into:

```text
healthy
ready
successful
```

merely to produce a convenient dashboard state.

---

## 17. Evidence Freshness

An Operational Evidence Contract SHALL define how evidence validity is interpreted where stale evidence could materially mislead operations.

No universal TTL is accepted.

Different evidence may use:

```text
observation age
source revision
provider timestamp
checkpoint
current query
bounded validity window
```

as appropriate.

---

## 18. Alert

An Alert is:

> A derived operational signal requesting attention because configured operational evidence satisfies an alerting condition.

Canonical:

```text
observations
    ↓
alerting rule
    ↓
Alert
```

An Alert is not:

```text
DomainEvent
Business Fact
Provider Readiness
Reconciliation result
AuditRecord
mutation authority
```

---

## 19. Alert Consequences

An Alert MAY:

```text
notify operators
open operational attention
trigger a registered diagnostic check
cause a registered BackgroundWork responsibility
request authorised intervention
```

It MUST NOT directly:

```text
refund payment
cancel order
restock inventory
change configuration
mark provider outcome
complete reconciliation
```

Any consequential operation must pass through the owning authority.

---

## 20. Alert Deduplication and Suppression

Alert aggregation, deduplication, routing, suppression and paging policy are operational implementation choices unless a future semantic requirement makes one materially authoritative.

Alert acknowledgement means only that operational attention was acknowledged.

It does not mean the underlying business or reconciliation condition is resolved.

---

## 21. Ordinary Telemetry Failure

Existing MS-PROT-068 semantics survive.

Ordinary telemetry export failure normally yields:

```text
reduced operational visibility
```

not:

```text
business rollback
```

Required Audit evidence remains separately governed by MS-PROT-064.

Durable business/reconciliation responsibilities must survive observability failure.

---

## 22. Sensitive Data

Telemetry SHALL prefer bounded identifiers, categories and references.

It MUST NOT indiscriminately contain:

```text
credentials
session secrets
provider secrets
full personal-data payloads
full payment payloads
full AI prompts/responses
full business aggregates
```

MS-PROT-053 and MS-PROT-067 remain controlling authorities.

---

## 23. Merchant and Customer Projection

Raw diagnostic evidence is not directly exposed to merchant/customer surfaces.

Any operational condition shown externally is a safe projection.

Examples:

```text
"Calendar connection needs attention"

not

"OAuth refresh_token invalid_grant ..."
```

Exact API and presentation contracts remain Target 20 / presentation scope.

---

## 24. Initial Production Evidence Portfolio

Target 19 requires operational diagnosability for the following existing responsibility classes where they are active:

```text
semantic materialisation / serving admission
authentication/session infrastructure
provider readiness
Domain Event reaction progression
Durable Background Work
provider-dependent financial execution
Shipment preparation
return-label preparation
Notification delivery
Projection update/rebuild where materialised
Data Lifecycle review/disposition convergence
execution uncertainty / reconciliation
```

This is a responsibility portfolio, not a mandated metric-name catalogue.

---

## 25. AI

AI may assist diagnosis or summarise already-authorised operational evidence.

AI MUST NOT:

```text
invent health truth
declare reconciliation complete
decide provider execution occurred
retry uncertain side effects
override owner operations
```

Any future AI operational assistant requires bounded read/action contracts.

---

## 26. Falsification

The amendment is falsified against the following cases.

Telemetry backend unavailable while an Order commits: Order remains committed.

Provider-health dashboard says DOWN while one exact existing callback is valid: callback evidence is processed under provider/capability authority rather than rejected from dashboard state alone.

Health evidence is stale: result may be UNKNOWN; stale evidence does not become readiness authority.

Background queue contains one old reconciliation item: alerting may occur, but business state remains unchanged.

Projection checkpoint stops moving: observability detects lag; rebuild uses Projection authority rather than changing source business truth.

False-positive alert: no business mutation occurs.

Operator acknowledges alert: underlying uncertainty remains unresolved until reconciliation authority resolves it.

Telemetry contains a PaymentExecutionRequest reference: the reference aids diagnosis but does not prove payment.

Event reaction retries across several traces: tracing remains technical correlation, not reaction completion authority.

Data-lifecycle work is overdue: alerting does not delete data.

All cases preserve accepted semantic ownership.

---

## 27. Alternatives and Trade-offs

Rejected: make observability a central platform state authority. This would collapse evidence and business truth.

Rejected: require every subsystem to emit an identical universal metric set. This would create meaningless telemetry and coupling.

Rejected: let alerts directly trigger business mutations. False-positive operational evidence would gain business authority.

Rejected: use raw logs as audit or reconciliation evidence. Their retention, integrity and meaning are insufficient.

Accepted: owner-qualified evidence contracts with bounded mandatory diagnosability while technology, metric names and dashboards remain replaceable.

---

## 28. Deferred / Future Scope

Deferred:

```text
OpenTelemetry or other SDK selection
Prometheus/Grafana/Datadog/etc.
log backend
trace backend
alerting/paging product
exact metric names
exact histogram buckets
sampling strategy
SLO/SLI catalogue
numeric alert thresholds
dashboard layout
log format
exact health endpoints
telemetry persistence
retention durations
anomaly detection
AI operational assistant
operator UI
merchant/customer status APIs
```

Target 20 owns external API representation where applicable.

---

## 29. Conformance / Acceptance Criteria

A conforming production design must allow another engineer to determine:

```text
what responsibility is being observed
what evidence is operational only
which stable identities may safely correlate it
what health scope is represented
how stale/unknown evidence is treated
how stuck asynchronous work becomes detectable
how reconciliation backlog becomes visible
why an alert cannot mutate business state
why telemetry loss cannot erase authoritative truth
```

No implementation may require telemetry to become the source of a business fact.

---

## 30. Acceptance Statement

Main Street has production operational visibility when materially important execution can be diagnosed without making observability authoritative.

> **Observe enough to operate and recover the system safely; correlate without stealing identity; represent unknown evidence honestly; alert without mutating; and preserve every owning authority beneath the operational view.**
