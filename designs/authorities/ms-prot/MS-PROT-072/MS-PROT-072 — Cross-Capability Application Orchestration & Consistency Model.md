# MS-PROT-072 — Cross-Capability Application Orchestration & Consistency Model

**Document ID:** MS-PROT-072  
**Version:** 1.0  
**Status:** **ACCEPTED after governed vertical-slice review, falsification and manual approval**  
**Depends on:** MS-PROT-024 v1.1, MS-PROT-026 v1.0, MS-PROT-040 v1.0 + v1.1, MS-PROT-056 v1.0–v1.5, MS-PROT-059 v1.0, MS-PROT-062 v1.0, MS-PROT-065 v1.0, MS-PROT-069 v1.0, MS-PROT-071 v1.0  
**Purpose:** Define how Main Street coordinates cross-capability application use cases, transaction boundaries, post-commit reactions, durable waiting, retries, duplicate delivery, partial committed progress and recovery without creating a universal merchant saga or transferring authority away from capability owners.

---

## 1. Governing decision

> **Application orchestration coordinates accepted capability operations and observes their outcomes; it does not own or duplicate Merchant Account, Merchant Configuration, Commercial Agreement, Trial, Publication, Trust, Provider or capability-owned operational state.**

Canonical shape:

```text
User / system intent
        ↓
Application Use Case
        ↓
resolve scope + principal
        ↓
invoke owning capability command
        ↓
authoritative commit
        ↓
observe committed result
        ↓
invoke next required capability
     OR publish/react asynchronously
        ↓
application outcome
```

Application code may own progression. It MUST NOT become the hidden owner of participating business truth.

---

## 2. No universal merchant lifecycle aggregate

Main Street SHALL NOT introduce a universal aggregate or status object that duplicates authoritative state such as:

```text
merchant status
onboarding status
configuration status
trial status
subscription status
publication status
```

when those facts are already owned independently.

A merchant-facing setup/progress view MAY derive a projection from authoritative facts. Such a projection is not mutation authority.

---

## 3. Three collaboration modes

Main Street shall select the smallest appropriate collaboration mechanism.

### 3.1 Synchronous application orchestration

Use when one application request must invoke one or more accepted operations and no durable waiting boundary is required.

The application service sequences calls and returns their authoritative result; participating capabilities retain ownership.

### 3.2 Independent post-commit reaction

Use when an upstream fact is already valid independently and downstream failure must not undo it.

```text
Authoritative fact commits
        ↓
durable publication intent
        ↓
independent consumer/reaction
```

Examples include trial establishment after first configuration activation and projection refresh after an authoritative configuration change.

### 3.3 Durable process coordination

Use only where progression must survive an execution boundary such as:

```text
restart
external callback
human wait
provider wait
scheduled time
multi-step recovery
```

A durable coordinator owns only progression context and waiting/recovery state. It MUST NOT become authoritative for participating business facts.

---

## 4. No universal saga

Main Street SHALL NOT create one persistent `MerchantSaga` or equivalent coordinator for the merchant lifetime.

Coordination MUST be use-case bounded.

Examples may include:

```text
MerchantAccountEstablishmentUseCase
ConfigurationApprovalUseCase
FirstConfigurationActivationReaction
PaidPlanAcceptanceProcess
PublicationRefreshReaction
```

Only use cases with genuine durable progression require a persistent coordinator.

---

## 5. Transaction boundary rule

> **Effects owned by different capabilities are atomic together only where an accepted invariant requires them to succeed or fail as one business fact. Cross-capability convenience is not sufficient justification for shared atomicity.**

Main Street therefore rejects one transaction spanning merchant establishment, onboarding, configuration activation, trial establishment, publication and notification.

Each authority protects its own invariant inside the smallest valid transaction boundary.

Where an accepted cross-capability invariant genuinely requires atomicity, the implementation MUST preserve that invariant using the accepted consistency boundary.

---

## 6. First merchant vertical-slice boundaries

The first merchant setup slice is coordinated as independently authoritative steps, including where applicable:

```text
T1 Merchant Account establishment
T2 Configuration Revision approval/state transition
T3 Configuration Revision activation
T4 Commercial initial-trial establishment
T5 Publication-owned mutation/projection refresh
T6 later Merchant Commercial Agreement transition
```

The existence of this sequence does not make the application layer owner of any T1–T6 fact.

---

## 7. Logical application-request identity

Every externally retriable application request capable of authoritative mutation MUST establish a stable logical application-request identity.

That identity represents one logical caller intention reaching one application use case.

It is distinct from:

```text
business-object identity
operation type
merchant identity
HTTP transport request identity
trace identity
```

Implementations may correlate these identities but MUST NOT conflate their semantics.

---

## 8. Layered idempotency

Application-level idempotency does not automatically provide downstream idempotency.

For example:

```text
CompleteInitialMerchantSetup request
    ↓
Configuration activation request
    ↓
Trial establishment request
    ↓
Projection refresh reaction
```

Each duplicate-sensitive authoritative or side-effecting boundary requires its own appropriate identity/reconciliation semantics.

Application request identity, configuration activation identity, commercial trial identity and provider payment identity are distinct even when causally related.

---

## 9. Correlation and causation

Cross-capability progression SHOULD preserve sufficient correlation and causation evidence.

```text
correlation
    = wider application/process context

causation
    = immediately preceding request or committed fact
```

Correlation and causation are provenance. They do not grant authority.

---

## 10. Setup completion is initially a projection

Main Street SHALL NOT introduce an authoritative universal `OnboardingCompleted` or `MerchantSetupCompleted` business fact merely to collapse independent authority states.

Merchant-facing setup progress should initially be derived from facts such as:

```text
Merchant Account established
Configuration activated
Trial established
applicable Publication state
current blockers
```

A future authoritative completion concept would require separate evidence that it represents an independent business fact rather than a convenience summary.

---

## 11. First configuration activation progression

Canonical progression:

```text
Merchant approves exact Configuration Revision
        ↓
Application invokes accepted Configuration operations
        ↓
Configuration validates/resolves exact revision
        ↓
Configuration activation transaction commits
        ↓
ConfigurationRevisionActivated
        ├───────────────┐
        ↓               ↓
Commercial reaction    applicable projection/publication reactions
        ↓               ↓
Initial trial          surface refresh/publication-owned effects
```

Configuration activation MUST remain valid if an independent downstream reaction is delayed or fails.

---

## 12. Partial committed progress

Cross-capability application outcomes MUST accurately represent committed progress.

A higher-level use case MUST NOT report total failure when an authoritative effect is known to have committed.

Conceptual application outcome categories include:

```text
COMPLETED
    requested application outcome reached

ACCEPTED_PENDING
    authoritative progress committed;
    recoverable required consequence remains pending

REJECTED
    requested authoritative transition did not commit

CONFLICT
    authoritative state changed incompatibly

EXECUTION_UNCERTAIN
    commit outcome cannot yet be determined

MANUAL_INTERVENTION_REQUIRED
    safe automatic progression cannot continue
```

Exact transport/error types remain implementation scope.

---

## 13. Configuration success versus trial establishment

Configuration activation succeeds when Configuration authority commits it.

A higher-level setup use case MAY wait synchronously for Commercial trial establishment when Commercial is immediately available, provided no shared/distributed transaction is created.

If trial establishment cannot complete immediately after configuration activation, the application outcome MUST preserve the committed configuration result and represent downstream commercial setup as pending rather than falsely reporting rollback or total failure.

---

## 14. Publication is conditional, not universal

Publication/public storefront availability is not a universal prerequisite for merchant setup.

Whether a public surface is applicable depends on the accepted merchant configuration and owning Exposure/Publication authorities.

Application orchestration MUST NOT impose public-site publication on merchants whose supported operating model does not require it.

---

## 15. Publication coordination

Where active configuration implies an applicable public or merchant-facing surface:

```text
ConfigurationRevisionActivated
        ↓
projection/publication consumer
        ↓
resolve current authoritative configuration
        ↓
commit publication-owned state where required
```

Publication failure MUST NOT roll back Configuration activation or Trial establishment.

Merchant-facing projections may expose publication as pending/unavailable with the actual reason.

---

## 16. Durable post-commit consequence delivery

Where a downstream consequence is required by accepted semantics after an authoritative commit, publication intent MUST be durable with the originating commit or otherwise provide an equivalent guarantee that survives process failure.

This applies particularly to first-configuration activation consequences required to establish the initial trial.

No specific outbox, queue, broker or framework is mandated.

---

## 17. Duplicate delivery

Main Street does not assume exactly-once delivery.

Consumers MUST tolerate duplicate delivery according to their semantics.

Examples:

```text
ConfigurationRevisionActivated delivered repeatedly
    → Commercial resolves the same initial trial

projection refresh delivered repeatedly
    → produces the same authoritative projection result
```

Notification and provider consumers independently apply their own duplicate-sensitive contracts.

---

## 18. Ordering and stale context

Arrival order MUST NOT silently become business order.

Where ordering matters, consumers MUST use the smallest accepted ordering/version scope or query the owning authority before mutation.

A delayed consumer carrying Configuration Revision C1 MUST NOT overwrite effects derived from currently active C2 merely because the C1 message arrived later.

Stored orchestration context is not mutation authority.

---

## 19. Application layer MUST NOT write participating storage directly

A cross-capability orchestrator SHALL NOT mutate capability-owned persistence directly in order to bypass accepted Command/Operation boundaries.

Rejected:

```text
application coordinator
    → direct Merchant Account table write
    → direct Configuration activation write
    → direct Trial insert
    → direct Publication write
```

Accepted:

```text
Application orchestrator
        ↓ accepted command/operation
Owning capability
        ↓
authoritative mutation
```

---

## 20. Collaboration selection rule

Use direct invocation when the caller requires an immediate authoritative result and no independent post-commit or durable-wait semantics require decoupling.

Use post-commit reaction when the upstream fact remains valid independently and downstream failure must not undo it.

Use durable process coordination when progression must survive waiting, restart, callback or recovery boundaries.

Main Street SHALL NOT adopt either universal synchronous orchestration or universal event-driven orchestration.

---

## 21. Paid-plan acceptance validation flow

A paid-plan flow may require durable coordination where payment/provider evidence crosses execution boundaries:

```text
Merchant accepts exact plan revision
        ↓
Commercial application process
        ↓
Money establishes Payment Obligation
        ↓
provider interaction / wait / reconciliation
        ↓
trusted Payment Evidence
        ↓
Commercial command
        ↓
Merchant Commercial Agreement transition
        ↓
entitlement becomes derivable
```

Money/Payment retain their authoritative facts. Commercial retains agreement authority. The coordinator owns only progression.

---

## 22. Compensation

Compensation is a new forward authoritative action, not rollback of history.

If a prior authoritative action committed and later progression fails, the coordinator may request an accepted compensating operation such as refund or commercial remediation where governing authority permits it.

The coordinator MUST NOT delete or rewrite committed history to simulate rollback.

---

## 23. Retry safety

Application orchestration may retry only where the same logical intent is established and duplicate behaviour is independently safe.

Generic `catch → retry everything` behaviour is prohibited for side-effecting operations.

Provider interactions, payment effects and execution-uncertain outcomes require their own accepted correlation/reconciliation semantics.

---

## 24. Recovery after restart

A durable coordinator recovering after restart MUST reconcile authoritative capability state before issuing new commands where stored context may be stale.

Coordinator state is progression context, not business truth.

---

## 25. Manual intervention

A coordinator may require manual intervention when deterministic automatic progression is unsafe or exhausted.

Manual intervention MUST still invoke accepted attributable capability operations and MUST NOT become a generic invariant bypass.

---

## 26. Authority does not propagate through causation

An initiating actor's authority does not automatically propagate to later asynchronous/background steps.

Each downstream authoritative command requires a legitimate execution principal/context appropriate to that operation.

Knowledge that a merchant caused an earlier event is not permission for arbitrary later mutation.

---

## 27. Observability and orchestration state

Logs, traces, metrics and coordinator progress are not authoritative evidence that Merchant Configuration is active, a Trial exists or a Commercial Agreement is effective.

Those facts MUST be queried from their owning authorities.

Operational tooling may project authoritative facts and coordinator progress for diagnostics.

---

## 28. Failure taxonomy

Cross-capability orchestration SHALL distinguish at least:

```text
REJECTED
CONFLICT
COMPLETED
ACCEPTED_PENDING
EXECUTION_UNCERTAIN
MANUAL_INTERVENTION_REQUIRED
```

Capability-specific domain errors remain capability-owned.

Transport failure alone MUST NOT be treated as proof that an authoritative effect did not commit.

---

## 29. Cross-domain and failure falsification

The model was tested against:

- trial consumer crash after Configuration commit;
- duplicate setup submission;
- Configuration activation with delayed Commercial availability;
- projection/publication failure;
- out-of-order configuration events;
- paid-plan acceptance waiting for provider callback;
- duplicate payment callback;
- stale coordinator recovery;
- delayed consumer after a newer configuration activation;
- background work following merchant-originated causation;
- information-only merchant with no universal provider/payment/publication step.

The model preserves authoritative ownership and recoverability in each case.

---

## 30. Explicitly rejected alternatives

Rejected:

```text
one giant merchant transaction
one universal merchant saga
application-layer direct mutation of participating repositories
universal setup status as business authority
exactly-once delivery assumption
arrival order as authority
initiating authority propagated to all downstream work
generic retry of side-effecting operations
rollback of committed history as compensation
```

Accepted:

```text
explicit application orchestration
+
narrow atomic authority boundaries
+
selective post-commit reactions
+
durable coordination only where progression must wait
```

---

## 31. Implementation boundary

This authority does not select:

```text
Spring orchestration pattern
Kafka / RabbitMQ / other broker
workflow engine
saga library
outbox technology
database transaction API
process persistence schema
event envelope format
retry library
scheduler implementation
```

Implementations must satisfy the accepted semantics without allowing those technologies to redefine authority.

---

## 32. Accepted invariants

1. Application orchestration owns progression, not participating business truth.
2. No universal merchant lifecycle aggregate or saga is accepted.
3. Synchronous orchestration, post-commit reaction and durable process coordination remain distinct modes.
4. Cross-capability atomicity requires an accepted invariant, not convenience.
5. Logical application-request identity is distinct from business and downstream operation identities.
6. Idempotency is layered by authoritative/side-effecting boundary.
7. Correlation and causation do not grant authority.
8. Setup completion is initially a projection over authoritative facts.
9. Application outcomes must preserve partial committed progress truthfully.
10. Configuration activation remains committed if independent downstream consequences fail.
11. Publication is configuration-dependent, not universally required.
12. Required post-commit consequences require durable delivery/recovery semantics.
13. Exactly-once and global event ordering are not assumed.
14. Delayed/stale consumers must revalidate current authority before mutation.
15. Application orchestrators may not mutate participating persistence directly.
16. Durable coordinators recover against authoritative state.
17. Compensation is forward action, not history rollback.
18. Retry safety must be independently established.
19. Manual intervention uses accepted operations and does not bypass invariants.
20. Initiating authority does not automatically propagate downstream.
21. Observability and coordinator state are not business authority.
22. Infrastructure/framework choices remain downstream implementation decisions.

## Governance verdict

**ACCEPTED.** The vertical-slice composition gap is resolved by explicit application orchestration over capability-owned authorities, with selective post-commit reaction and durable coordination only where progression genuinely crosses waiting/recovery boundaries.