# MS-PROT-068 — Operational Observability, Health & Diagnostic Evidence Model

**Document ID:** MS-PROT-068  
**Version:** 1.0  
**Status:** Accepted  
**Depends on:** MS-PROT-027, MS-PROT-053, MS-PROT-057, MS-PROT-062, MS-PROT-064, MS-PROT-065, MS-PROT-067  
**Purpose:** Define how Main Street observes execution behaviour, operational health and diagnostic evidence without allowing telemetry, health signals or alerts to become business truth or mutation authority.

---

## 1. Governing Principle

> **Main Street observes operational reality without allowing the machinery that observes it to become the authority that defines business reality.**

Observability supports diagnosis, operations, capacity understanding and incident response. It does not own business semantics, business state, retry policy, entitlement, provider binding, configuration or capability execution.

---

## 2. Canonical Separation

```text
Business State
    owns authoritative business facts

AuditRecord
    owns attributable durable evidence where required

DomainEvent
    communicates accepted business occurrence

Operational Observability
    describes execution behaviour and system condition
```

Therefore:

```text
Log          ≠ AuditRecord
Metric       ≠ Business State
Trace        ≠ Business Process
HealthSignal ≠ Capability Applicability
Alert        ≠ Domain Event
Alert        ≠ Mutation Authority
```

---

## 3. Primary Observability Evidence

Main Street recognises four primary operational evidence forms:

```text
Log
    discrete diagnostic record

Metric
    quantitative operational measurement

Trace
    correlated technical execution path

HealthSignal
    observation of operational condition
```

`Alert` is a derived operational reaction to observations/rules and is not a fifth source of business truth.

No universal Observability aggregate or business capability is required by this authority.

---

## 4. Logs

Logs MAY record bounded diagnostic information required to understand execution and failure.

Logs MUST NOT become authoritative business records or audit evidence merely because they are durable.

Where identifiers are sufficient, telemetry SHOULD reference safe identifiers and classifications rather than duplicate complete business payloads.

---

## 5. Metrics

Metrics measure operational quantities such as latency, throughput, queue depth, failure rates, resource pressure and dependency behaviour.

Metrics MUST NOT be treated as authoritative business state or used to reconstruct business truth where the owning domain record exists.

Metrics MUST avoid uncontrolled high-cardinality dimensions. Business identifiers MAY be used only where operationally justified, deliberately bounded and governed by access/retention requirements.

---

## 6. Traces and Correlation

A trace represents a technical execution path, not a business process.

```text
TraceId ≠ BusinessProcessId
TraceId ≠ durable business correlation identity
```

One request MAY execute multiple business operations. One long-running business process MAY span many traces.

Asynchronous execution, retries and scheduled work MAY begin new traces while preserving safe correlation metadata to originating operations, events, work items or provider interactions where appropriate.

Correlation metadata does not itself establish business authority.

---

## 7. Health Is Scope-Qualified

Main Street MUST NOT rely on one universal binary `MainStreetHealthy` truth.

Health observations MUST identify their subject/scope sufficiently to represent partial degradation, for example:

```text
public storefront       READY
merchant dashboard      DEGRADED
AI assistance           UNAVAILABLE
payment integration     READY
media processing        DEGRADED
```

Exact health-state enums are implementation choices. The normative requirement is that liveness, readiness and degraded service quality can be distinguished where operationally material.

---

## 8. Health Evidence Is Not Execution Authority

A health observation is evidence about operational condition. It MUST NOT directly rewrite capability applicability, merchant configuration, entitlement, business state or provider binding.

Where health evidence should influence execution:

```text
Observability evidence
        ↓
authorised resilience/runtime mechanism
        ↓
current-state validation
        ↓
governing runtime/capability decision
```

Observability itself does not own the consequence.

---

## 9. Provider Health vs Connection Readiness

Provider-wide operational health and one merchant's provider-connection readiness are distinct.

```text
Provider Health
    evidence about external provider/service condition

ProviderConnection Readiness
    connection/account-specific operational truth
```

A merchant OAuth failure MUST NOT by itself mark the external provider globally unavailable. Provider-wide failure evidence MUST NOT silently rewrite every merchant connection.

---

## 10. AI Observability

AI telemetry MAY observe bounded operational facts such as specialist invocation, latency, provider failure, fallback use, proposal outcome classification and resource usage where appropriate.

Full merchant prompts, merchant content and model responses MUST NOT automatically become ordinary logs/traces merely for convenience.

Where AI proposal/approval evidence requires durable retention, the owning configuration/audit authority governs that evidence rather than generic observability.

AI provider failure MUST NOT imply whole-platform failure where deterministic alternatives remain available.

---

## 11. Background Work and Retry

Observability MAY report background-work attempts, age, backlog, failure classifications and throughput.

It MUST NOT own retry count, retry timing, abandonment, compensation or business consequence. Those remain with MS-PROT-065 and the owning capability/process.

A retry MAY have a new trace while retaining safe correlation to the durable work identity.

---

## 12. Sensitive Data and Credential Protection

MS-PROT-067 constraints apply to all telemetry.

Raw credentials, passwords, API keys, OAuth tokens, session secrets, webhook secrets and equivalent protected material MUST NOT enter ordinary logs, traces, metrics, alerts, error telemetry or AI diagnostic context.

Telemetry instrumentation/export MUST support sanitisation/redaction before protected data reaches ordinary observability sinks.

Diagnostic evidence SHOULD prefer bounded identifiers and classifications over serialized domain objects or unnecessary personal data.

---

## 13. Data Minimisation and Retention

Operational telemetry has its own bounded diagnostic retention requirements and MUST compose with MS-PROT-053.

```text
Business record
    business/evidentiary retention

AuditRecord
    audit retention

Log / Trace / Metric
    operational diagnostic retention
```

Telemetry MUST NOT automatically inherit business-record retention merely because it references a business operation.

Diagnostic identifiers and personal data, where genuinely necessary, MUST be minimised, access-controlled and retention-bounded.

---

## 14. Observability Failure Independence

Ordinary observability infrastructure MUST NOT normally be a synchronous prerequisite for successful business execution.

```text
business operation succeeds
        │
        └── telemetry export failure
                ↓
        operational visibility reduced
```

Loss of telemetry may reduce diagnostic knowledge but MUST NOT erase authoritative business truth.

Where MS-PROT-064 requires durable audit evidence before a sensitive operation may safely complete, that is an Audit requirement and is not weakened by this rule.

---

## 15. Health Checks

Routine health observation MUST be bounded and non-destructive.

Health checks MUST NOT create Orders, reserve Inventory, make real payments, create customer accounts or otherwise mutate business state merely to determine health.

Deep synthetic operational tests MAY exist separately under explicitly controlled execution semantics.

---

## 16. Alerts

Alerts are derived operational signals created from observations, thresholds, patterns or rules.

An alert MAY request operator attention or feed an authorised resilience mechanism. It MUST NOT itself mutate business state.

Individual business failures need not map one-to-one to operator alerts; aggregation, deduplication, suppression and routing are implementation/operational policies.

False-positive alerts MUST NOT have direct business mutation authority.

---

## 17. Safe Merchant and Customer Status Projection

Raw telemetry MUST NOT be exposed directly to merchant or customer surfaces.

```text
internal telemetry
      ↓
interpreted operational condition
      ↓
safe projection
      ↓
merchant/customer surface
```

A merchant may see `Your calendar connection needs reconnecting` rather than internal OAuth exceptions, stack traces or credential details.

Customer-facing degradation messages MUST similarly avoid infrastructure topology, provider secrets and unnecessary internal diagnostics.

---

## 18. Operational Health Evidence Examples

Depending on subsystem, useful evidence MAY include:

```text
latency
error/failure rate
request throughput
queue depth
oldest-work age
retry volume
processing throughput
worker availability
dependency reachability
resource saturation
provider response classification
```

No listed signal becomes universally mandatory merely by appearing here. Each subsystem should expose evidence proportionate to its operational responsibility.

---

## 19. Hard Invariants

1. Log MUST NOT become AuditRecord merely because it is durable.
2. Metric MUST NOT become authoritative business state.
3. Trace MUST NOT become business-process identity.
4. HealthSignal MUST NOT become capability applicability or merchant configuration.
5. Alert MUST NOT become DomainEvent or direct mutation authority.
6. TraceId MUST NOT be treated as BusinessProcessId or durable business correlation identity.
7. Provider Health MUST remain distinct from ProviderConnection readiness.
8. Telemetry loss MUST NOT erase business truth.
9. Ordinary observability failure MUST NOT normally fail business execution.
10. Required Audit evidence remains governed separately by MS-PROT-064.
11. Routine health observation MUST NOT mutate business state.
12. Health MUST be scope-qualified sufficiently to represent partial degradation.
13. AI failure MUST NOT imply whole-platform failure where deterministic alternatives remain available.
14. Raw credentials MUST NOT enter ordinary telemetry.
15. Business payloads and personal information MUST NOT be indiscriminately duplicated into telemetry.
16. Diagnostic identifiers MUST be minimised, access-controlled, retention-bounded and cardinality-aware.
17. Metrics MUST avoid uncontrolled high-cardinality dimensions.
18. Asynchronous execution MAY start new traces while preserving safe correlation.
19. Observability MUST NOT own retry, compensation, provider binding, entitlement or business-state transitions.
20. Merchant/customer operational status MUST be a safe projection rather than raw telemetry exposure.
21. Observability technology MUST remain replaceable.

---

## 20. Explicit Non-Responsibilities

MS-PROT-068 does NOT select telemetry SDK, metrics backend, log store, trace backend, dashboard product, alerting provider, paging system, sampling algorithm, retention duration, exact health endpoint format, concrete Java instrumentation classes, or infrastructure topology.

Technology choices such as OpenTelemetry, Prometheus, Grafana, Datadog, New Relic, ELK, CloudWatch, Jaeger, Tempo, Sentry or PagerDuty remain downstream implementation choices.

---

## 21. Falsification Summary

The model has been tested against provider-wide payment outage, merchant-specific OAuth expiry, database latency, partial surface failure, AI-provider outage, video-processing backlog, repeated background-work failure, asynchronous trace propagation, retry traces, multi-operation requests, credential leakage through exceptions, personal data in logs, AI content in traces, high-cardinality metrics, observability-backend outage, metric loss, false-positive alerts, alert storms, destructive health checks, provider-wide versus merchant-specific failure, operator diagnosis and safe merchant/customer status projection.

No tested scenario requires observability to own business semantics or mutation authority.

---

## 22. Acceptance Statement

Main Street now has a canonical operational-observability model in which logs, metrics, traces and health observations provide diagnostic evidence without becoming business truth.

> **Observe execution; qualify health by scope; minimise telemetry data; preserve correlation without confusing it with business identity; and route operational consequences through the authority that owns them.**
