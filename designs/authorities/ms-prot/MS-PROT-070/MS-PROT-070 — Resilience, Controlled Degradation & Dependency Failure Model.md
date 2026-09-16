# MS-PROT-070 — Resilience, Controlled Degradation & Dependency Failure Model

**Document ID:** MS-PROT-070  
**Version:** 1.0  
**Status:** Accepted  
**Depends on:** MS-PROT-023, MS-PROT-027, MS-PROT-048, MS-PROT-053, MS-PROT-062, MS-PROT-064, MS-PROT-065, MS-PROT-067, MS-PROT-068, MS-PROT-069  
**Purpose:** Define how Main Street responds to dependency failure, saturation and partial outage while preserving security, privacy, authoritative invariants and existing commitments ahead of apparent availability.

---

## 1. Governing Principle

> **Main Street preserves security, privacy and authoritative business correctness before availability. Degradation may remove functionality, defer work or activate an already-authorised path; it MUST NOT manufacture business certainty or weaken invariants merely to keep a surface responsive.**

Prefer explicit bounded unavailability over silently degraded correctness.

---

## 2. Resilience Preservation Order

When guarantees conflict, Main Street SHALL preserve them in this order:

```text
1. Security / Privacy / Trust
2. Authoritative Business Invariants
3. Existing Commitments / Durable Evidence
4. Safe Availability
5. Performance
6. Convenience / Enhancement
```

A lower-ranked property MAY be sacrificed to preserve a higher-ranked property. The reverse MUST NOT occur merely for apparent availability.

---

## 3. Canonical Responsibility Graph

```text
Observability
    observes degradation

MS-PROT-069
    establishes execution certainty where communication failed

Semantic Owner
    defines required guarantee and valid alternatives

Resilience Policy
    chooses among already-valid technical execution modes

Infrastructure
    implements timeout/retry/circuit/isolation/shedding

Runtime
    re-evaluates current eligibility

Capability / Process
    owns final business consequence
```

Resilience is not a business-semantic owner.

---

## 4. Supported Resilience Mechanisms

Main Street MAY use conventional mechanisms including:

```text
Timeout
Retry
Circuit breaking
Bulkhead / isolation
Load shedding
Deferred execution
Accepted fallback
Read-only degradation
Recovery / backlog control
```

These mechanisms control execution conditions. They MUST NOT define business meaning.

---

## 5. Retry Safety

Retry MUST occur only where semantic retry safety is independently established by the owning operation/provider/process contract.

```text
safe to retry?
    ↓
YES
    ↓
resilience policy chooses when/how
```

A retry library, HTTP status or transport exception MUST NOT itself establish retry safety.

Bounded attempts, backoff, jitter and retry budgets are implementation techniques, not semantic authorities.

---

## 6. Timeout Semantics

Timeout limits how long Main Street waits for a dependency. It does not establish whether an operation executed.

```text
timeout
    ↓
MS-PROT-069 certainty classification
    ↓
known failure / known execution / execution uncertain
```

Side-effecting uncertainty remains subject to reconciliation. Timeout MUST NOT be converted directly into business failure.

---

## 7. Circuit Breaking

Circuit breaking MAY temporarily suppress calls to a failing dependency.

Circuit state MUST NOT become:

- ProviderConnection state;
- fulfilment-binding state;
- capability state;
- merchant configuration;
- business lifecycle state.

Previously uncertain operations remain uncertain even when a circuit opens.

---

## 8. Provider Fallback

Technical resilience MAY activate an already accepted fallback fulfilment path. It MUST NOT create one.

Automatic substitution from Provider A to Provider B is prohibited unless the governing provider/fulfilment semantics already authorise that fallback relationship.

Provider health alone MUST NOT create merchant consent to a different external provider.

---

## 9. Fallback Is Not Semantic Substitution

A fallback MAY:

- use another already-supported path to the same semantic result; or
- explicitly offer another already-supported business operation.

It MUST NOT silently reinterpret one operation as another.

Example:

```text
Booking unavailable
    → Enquiry may be offered if configured

Booking unavailable
    → Enquiry MUST NOT be created while reporting booking success
```

---

## 10. Authoritative Store Failure

When Main Street cannot establish current authoritative state, authoritative mutation MUST NOT proceed from stale cache, projection, local UI state or last-known values unless a separately accepted design explicitly grants that authority.

For example:

```text
cached stock = 1
DB unavailable
    → Product may remain browsable if projection contract permits
    → stock-dependent checkout MUST NOT rely on cached stock as write authority
```

Temporary write unavailability is preferable to violating authoritative invariants.

---

## 11. Read Degradation

Reads MAY remain available during dependency failure only under the owning projection/read model's freshness and staleness contract.

Stale tolerance is data-contract-specific, not page-wide or system-wide.

Examples:

```text
merchant description
published content
product imagery
```

may be safely stale in some contexts, while:

```text
live inventory
appointment availability
payment state
```

may not be.

---

## 12. Security and Privacy Override Availability

Cached or replicated content MUST NOT be served merely to preserve availability if current security, privacy, revocation or Exposure rules require it to be unavailable.

Security/privacy revocation outranks cache availability.

Resilience MUST NOT extend credential validity, authentication trust or secret use beyond governing security semantics merely to keep an operation working.

---

## 13. Dependency Criticality Is Contextual

A dependency is not globally `critical` or `optional`.

Criticality is determined by:

```text
dependency
+
operation/context
+
required guarantee
```

For example, notification delivery may be post-commit for one operation but completion-critical for another.

Resilience MUST defer to the owning semantic contract.

---

## 14. Partial Degradation

Failure in one responsibility SHOULD remain scoped where possible.

Examples:

```text
AI unavailable
    → deterministic/manual operations continue

Video processing degraded
    → existing READY media remains deliverable
    → new processing may remain pending

Provider credential unavailable
    → affected provider operation not ready
    → unrelated operations continue
```

A subsystem failure MUST NOT automatically become whole-platform failure.

---

## 15. Isolation and Bulkheads

Main Street MAY isolate capacity so that one failing or saturated responsibility does not consume unbounded resources required by unrelated higher-criticality responsibilities.

This authority does not mandate threads, processes, queues, containers or deployment topology.

The invariant is resource-failure containment.

---

## 16. Load Shedding

Under saturation, Main Street MAY reject, defer or reduce work to preserve higher-priority responsibilities.

Load shedding MUST NOT invent successful execution.

Operational importance MUST NOT be inferred solely from HTTP method or transport endpoint shape.

Conceptually, work may differ as `must preserve`, `deferable` or `optional`, but exact categories remain implementation/operational design.

Merchant quotas, abuse classification and commercial usage limits are outside MS-PROT-070.

---

## 17. Existing Commitments

Dependency degradation MUST NOT silently abandon existing business commitments.

Existing Orders, Appointments, Payments, Fulfilment obligations, retention duties and other durable commitments remain governed by their owning semantics.

Where fulfilment must wait, reconciliation/retry/background work MAY continue according to accepted authority.

---

## 18. Background and Event Resilience

If post-commit work cannot be delivered immediately, durability MUST follow its accepted event/background-work contract rather than queue availability alone.

A temporarily unavailable event consumer SHOULD NOT cause the producer's already-committed business fact to be undone unless a governing atomicity requirement explicitly requires otherwise.

---

## 19. Recovery and Backlog Drain

When a dependency recovers, Main Street MAY use bounded concurrency, gradual draining or equivalent techniques.

Recovery optimisation MUST preserve:

- idempotency;
- required ordering;
- MS-PROT-069 reconciliation;
- current runtime authority;
- authoritative revalidation.

Backlog speed MUST NOT outrank correctness.

---

## 20. Split-Brain Prohibition

Where an invariant requires one authoritative truth, Main Street MUST NOT permit independent partitions to accept conflicting authoritative mutations unless a separately accepted distributed-authority model explicitly supports it.

> **Temporary write unavailability is preferable to competing authoritative truths.**

This applies to resources such as Inventory, appointment slots and other contention-sensitive state.

Exact failover or regional topology remains downstream.

---

## 21. User-Facing Degradation

Raw infrastructure failures MUST NOT be exposed directly to merchants/customers.

Internal conditions SHALL be projected safely, for example:

```text
Online booking is temporarily unavailable.
Your calendar connection needs reconnecting.
Video processing is delayed.
```

rather than circuit-breaker states, stack traces, SQL failures or internal dependency topology.

---

## 22. Observability Interaction

MS-PROT-068 observations MAY inform resilience decisions but MUST NOT directly mutate business state.

```text
health/telemetry evidence
    ↓
resilience evaluation
    ↓
authorised runtime/capability path
```

False-positive alerts MUST NOT themselves produce business mutations.

---

## 23. Hard Invariants

1. Correctness, security and privacy MUST outrank availability.
2. Resilience infrastructure MUST NOT become a business-semantic owner.
3. Explicit bounded unavailability is preferable to silently degraded correctness.
4. Retry MUST require independently established semantic safety.
5. Timeout MUST bound waiting but MUST NOT determine execution outcome.
6. Circuit state MUST NOT alter ProviderConnection, fulfilment binding or capability semantics.
7. Provider fallback MUST require an already accepted fallback path.
8. Fallback MUST NOT silently substitute one business operation for another.
9. Degraded paths MAY use only semantics already supported for the merchant/context.
10. Cached/stale reads MUST obey the owning read model's freshness/staleness contract.
11. Stale data MUST NOT become write authority without separately accepted semantics.
12. Security/privacy revocation MUST outrank cached availability.
13. Dependency criticality MUST be operation/context-specific.
14. Isolation/bulkheads MAY protect unrelated higher-criticality work from saturation.
15. Load shedding MAY reject/defer work but MUST NOT invent successful execution.
16. Operation importance MUST NOT be inferred merely from HTTP method.
17. Recovery MUST preserve idempotency, required ordering, reconciliation and authoritative revalidation.
18. Existing commitments MUST NOT be silently abandoned because a dependency is unavailable.
19. Uncertain execution remains governed by MS-PROT-069.
20. Split-brain authoritative mutation is prohibited unless a separately accepted distributed-authority model exists.
21. User-facing degraded status MUST be a safe projection rather than raw infrastructure telemetry.
22. Resilience technology and deployment topology MUST remain replaceable implementation choices.

---

## 24. Explicit Non-Responsibilities

MS-PROT-070 does NOT select retry libraries, circuit-breaker libraries, load balancers, cache technology, queue technology, deployment regions, database failover topology, multi-region design, thread/process topology, autoscaling mechanism, rate-limit policy, abuse policy, merchant quotas or concrete Java resilience classes.

---

## 25. Falsification Summary

The model has been tested against authoritative-database loss, stale Inventory, unavailable booking authority, payment-provider outage and failover, AI/media degradation, notification dependency failure, audit and credential failure, timeout ambiguity, retry storms, circuit breakers, privacy/cache conflicts, bulkhead isolation, noisy merchants, load shedding, instance failure, regional failure, split brain, event-consumer outage and recovery surges.

No tested case requires resilience infrastructure to own business meaning or weaken authoritative invariants.

---

## 26. Acceptance Statement

Main Street now has a controlled resilience model that keeps as much of the platform useful as can be done safely, while refusing to trade away semantic correctness for superficial availability.

> **Degrade explicitly, isolate failure, retry only when safe, use only accepted fallbacks, and prefer temporary unavailability to false business truth.**
