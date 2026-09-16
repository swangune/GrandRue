# MS-PROT-073 — Platform Resource Fairness, Rate Limiting & Abuse Protection Model

**Document ID:** MS-PROT-073  
**Version:** 1.0  
**Status:** ACCEPTED  
**Depends on:** MS-PROT-056, MS-PROT-062, MS-PROT-064, MS-PROT-068, MS-PROT-069, MS-PROT-070, MS-PROT-071  
**Closes:** Promoted design-review item — Resource Fairness, Rate Limiting & Abuse Protection  
**Purpose:** Define how Main Street protects shared platform capacity and contains abusive or unfair consumption without allowing infrastructure protection to become business-semantic, commercial, identity, trust, Merchant Account or capability authority.

---

## 1. Governing Principle

> **Main Street MAY bound, defer or reject consumption of shared platform capacity to preserve fair and safe operation, but Platform Resource Protection MUST NOT create, remove or reinterpret Merchant Configuration, Commercial Entitlement, actor authority, trust state, Provider Readiness, business commitments, Merchant Account lifecycle or capability semantics.**

Resource Protection is an independent platform-operational authority.

It answers:

> **May this attempted work consume the applicable protected platform resources now?**

It does not answer:

```text
Is the operation semantically applicable?
Is the actor authorised?
Is the merchant commercially entitled?
Is a trust requirement satisfied?
Is the provider ready?
Is the business operation valid?
Did the business operation succeed?
```

A successful protection admission is therefore necessary only where applicable; it is never sufficient authority for business execution.

---

## 2. Semantic Ownership

The **Platform Resource Protection** authority exclusively owns:

```text
Protection Target definitions
Protection Policy
Protection Consumption State
Protection Admission Decisions
Temporary Protective Restrictions
Protection Evidence produced by this authority
```

It does NOT own:

```text
Merchant Configuration
Commercial plan or subscription identity
Commercial usage entitlement
Merchant Account existence/lifecycle
Authentication identity
Actor authorisation
Trust/verification state
ProviderConnection state
Provider Readiness
Orders, Bookings, Appointments or other commitments
Capability lifecycle state
Fraud/legal determination
Permanent account suspension/closure
```

Other authorities MAY consume protection decisions or evidence. They MUST NOT treat those decisions as a competing copy of their own truth.

---

## 3. Resource Protection Is Distinct from Commercial Usage Control

The following distinction is normative:

```text
Operational fairness / platform protection
    owner = Platform Resource Protection

Commercial usage allowance / purchased quota
    owner = Commercial authority
```

Example:

```text
20 AI requests/minute to protect shared inference capacity
    = operational protection

10,000 AI operations/month promised by a plan
    = commercial usage entitlement
```

Platform Resource Protection MUST NOT silently redefine a commercial allowance as though the merchant's entitlement ceased to exist.

Commercial plan names MUST NOT directly determine protection policy unless a separately accepted Commercial authority first establishes an explicit service guarantee or service-class entitlement that Resource Protection is authorised to consume.

---

## 4. Minimal Canonical Model

MS-PROT-073 authorises exactly six core semantic concepts for the initial model:

```text
ProtectionTarget
ProtectionSubject
ProtectionPolicy
ProtectionConsumptionState
ProtectionAdmissionDecision
TemporaryProtectiveRestriction
```

Additional concepts require demonstrated need and governed design review.

MS-PROT-073 does not authorise a universal FraudCase, Ban, RiskScore, TrafficTier, QuotaPackage or arbitrary priority language.

---

## 5. ProtectionTarget

A **ProtectionTarget** is a stable platform-owned identity for one bounded class of work whose shared-resource consumption may be independently protected.

Examples of possible targets include:

```text
Merchant Account establishment
AI configuration inference
provider calendar synchronisation
media rendition
notification dispatch
```

The examples do not define the required catalogue.

A ProtectionTarget MUST be registered explicitly. It MUST NOT be inferred merely from:

```text
URL path
HTTP verb
controller name
Java class or method
queue/topic name
server/process identity
```

Those are replaceable implementation details and cannot own resource-protection meaning.

---

## 6. ProtectionSubject

A **ProtectionSubject** identifies the operational scope whose consumption is measured or restricted for a particular policy.

Legitimate conceptual scopes include:

```text
PLATFORM
MERCHANT
TRUSTED_PRINCIPAL
PROVIDER
PROVIDER_CONNECTION
INTEGRATION
ANONYMOUS_CLIENT_CONTEXT
```

This list is not a closed implementation enum unless separately accepted.

One attempted operation MAY participate in multiple applicable protection scopes simultaneously.

Example:

```text
provider calendar synchronisation
    ↓
PLATFORM scope
MERCHANT scope
PROVIDER scope
PROVIDER_CONNECTION scope
```

Where all applicable scopes are mandatory, admission succeeds only when every such scope admits the attempted consumption.

### 6.1 AnonymousClientContext

`ANONYMOUS_CLIENT_CONTEXT` is a bounded technical correlation context used for protection before trusted identity exists.

It MUST NOT become:

```text
CustomerAccount
CustomerContext
Merchant Account
civil identity
trusted principal
```

IP address, cookies, device/network fingerprints, user-agent and similar signals MAY contribute to protection correlation where lawfully and safely designed, but they do not become authoritative identity merely because they participate in abuse protection.

---

## 7. ProtectionPolicy

A **ProtectionPolicy** is a versioned platform-operational rule defining how consumption of a ProtectionTarget is admitted for one or more applicable ProtectionSubject scopes.

A policy MUST determine, where relevant:

```text
protection target
applicable subject scope(s)
measurement basis
bounded budget/capacity rule
admission behaviour
time/concurrency semantics where applicable
protection-state failure behaviour
policy version/effective applicability
```

The exact representation is implementation scope.

A ProtectionPolicy MUST NOT contain arbitrary merchant-authored executable logic.

Policy changes do not create Merchant Configuration revisions.

---

## 8. Measurement Basis

Every protection policy MUST explicitly define what it measures.

Possible measurement bases include:

```text
transport attempts
logical application requests
completed expensive operations
concurrent executions
bytes processed
provider calls
other explicitly registered consumption units
```

The implementation MUST NOT guess the measurement basis.

In particular:

```text
transport-attempt accounting
    ≠
business-operation idempotency
```

A duplicate transport attempt may legitimately consume network/CPU protection budget while remaining the same idempotent business intent.

Conversely, a policy defined over logical operations MUST NOT double-consume that logical quota merely because an acknowledgement was lost.

---

## 9. ProtectionConsumptionState

**ProtectionConsumptionState** is the authoritative operational accounting required to evaluate a ProtectionPolicy correctly at its applicable scope.

It is platform-operational state, not Merchant Configuration or capability business state.

Where correctness depends on bounded consumption, the system MUST preserve the policy invariant under concurrency.

Example:

```text
remaining admissible unit = 1
requests A and B race
```

If the governing policy permits only one further unit, the system MUST NOT independently admit both.

Therefore:

> **Where policy correctness depends on bounded consumption, admission and consumption accounting MUST form one atomic consistency boundary at the smallest applicable scope requiring it.**

This does not prescribe Redis, SQL locking, API gateways, distributed counters or any other storage/algorithm choice.

---

## 10. ProtectionAdmissionDecision

The canonical decisions are:

```text
ADMIT
DEFER
REJECT
```

### 10.1 ADMIT

`ADMIT` means only:

> Resource Protection currently permits the attempted work to consume the applicable protected resources.

It does not establish authorisation, entitlement, trust, semantic applicability, Provider Readiness, operational eligibility or business success.

### 10.2 DEFER

`DEFER` means:

> Protection permits execution later rather than now, and the owning operation/process independently supports deferred execution.

Resource Protection MUST NOT invent asynchronous semantics.

If the governing operation/process does not already permit deferred execution, Resource Protection cannot convert an immediate command into hidden queued work merely to preserve capacity.

### 10.3 REJECT

`REJECT` means:

> This attempt may not consume the protected resource under the current applicable ProtectionPolicy and ProtectionConsumptionState.

A rejection does not mutate the requested business object merely because protection denied execution.

---

## 11. Rate Limiting Is Admission Control, Not Punishment

Rate-limit exhaustion or high consumption does not itself establish malicious intent.

Normative distinction:

```text
rate limit exceeded
    ≠
abuse proven

protection rejection
    ≠
Merchant Account suspension
```

Legitimate flash traffic, software defects, retry loops, provider duplication and malicious traffic can create superficially similar volume.

Resource Protection MAY protect the platform immediately while preserving that distinction.

---

## 12. Protection Evidence

Resource Protection MAY produce evidence from patterns such as:

```text
repeated rejected authentication attempts
high-volume account-creation attempts
request amplification
repeated provider duplication
resource-exhaustion patterns
other registered protection observations
```

Protection evidence remains evidence.

A heuristic, anomaly score or signal MUST NOT itself become authoritative:

```text
fraud determination
civil identity
Merchant Account suspension
identity revocation
commercial cancellation
business legitimacy decision
```

Material downstream consequences require the authority that owns those consequences.

---

## 13. TemporaryProtectiveRestriction

A **TemporaryProtectiveRestriction** is a Platform Resource Protection-owned, bounded restriction on one ProtectionSubject and one or more ProtectionTargets.

Conceptually it carries sufficient information to determine:

```text
restriction identity
subject
protected target(s)
effective start
bounded expiry or mandatory reevaluation point
reason class/provenance
```

A TemporaryProtectiveRestriction is NOT:

```text
Merchant Account suspension
identity revocation
commercial cancellation
permanent ban
fraud/legal determination
```

### 13.1 Boundedness

A Resource Protection-owned restriction MUST be bounded in time or bounded by an explicit mandatory reevaluation point.

Resource Protection MUST NOT own indefinite punitive account restrictions.

Where evidence appears to justify permanent or broader consequences, the case must be escalated to the authority that owns that consequence, normally through accepted audit/intervention/security/account-governance paths.

---

## 14. Merchant Account Establishment

MS-PROT-071 remains authoritative for ordinary Merchant Account self-establishment authorisation.

Resource Protection is an additional independent admission dimension.

Therefore:

```text
authenticated human principal        PASS
authorised for account establishment PASS
resource-protection admission        REJECT
                                      ↓
account establishment does not execute now
```

A protection rejection MUST NOT revoke `ESTABLISH_OWN_MERCHANT_ACCOUNT` authority.

Resource Protection MAY constrain unsafe account-creation velocity or pressure. It MUST NOT invent controller-to-account cardinality rules.

---

## 15. Cross-Account Trial Abuse Boundary

Resource Protection MAY contain high-volume Merchant Account creation or related operational abuse.

It MUST NOT decide that two Merchant Accounts represent the same commercial customer merely to deny a trial.

Cross-Merchant-Account repeat-trial eligibility remains a separately governed Commercial/abuse decision under the MS-PROT-056 future scope.

Operational protection evidence may later inform that authority, but it does not own the commercial conclusion.

---

## 16. Existing Commitments

Resource fairness MUST respect Main Street's accepted preservation of existing authoritative commitments.

> **Platform Resource Protection MUST be able to preserve bounded capacity for work necessary to inspect, reconcile or discharge existing authoritative commitments where the owning semantic authority requires continued progression.**

Resource Protection does not determine whether an operation is commitment-resolution work.

The owning capability/process supplies that classification.

Examples may include valid cancellation, reconciliation, refund, fulfilment or other commitment-resolution paths where the applicable authority requires continuation.

This does not grant unlimited capacity or permanent entitlement to create new work.

---

## 17. Protection Treatment / Operational Importance

Platform work need not receive identical capacity treatment.

However, Resource Protection MUST NOT use arbitrary developer priority numbers, HTTP methods, URL paths or subscription plan names as semantic importance.

Protection treatment must be based on an already-established execution guarantee or platform-operational classification.

MS-PROT-070's preservation order remains governing when degradation/saturation forces trade-offs:

```text
1. Security / Privacy / Trust
2. Authoritative Business Invariants
3. Existing Commitments / Durable Evidence
4. Safe Availability
5. Performance
6. Convenience / Enhancement
```

Resource Protection may operationalise accepted protection treatment within those constraints; it does not rewrite the ordering.

---

## 18. Provider and AI Capacity

Resource Protection MAY constrain shared AI or provider-call capacity.

Applicable scopes may include:

```text
PLATFORM
MERCHANT
PROVIDER
PROVIDER_CONNECTION
INTEGRATION
```

Provider quota exhaustion or protection rejection MUST remain distinguishable from:

```text
merchant not entitled
merchant not authorised
operation not applicable
provider generally not configured
business operation invalid
```

Provider Readiness and Resource Protection Admission remain separate runtime dimensions.

---

## 19. Concurrent / Leased Capacity

Some protection policies govern attempts or units consumed immediately. Others govern capacity held across an execution interval, such as concurrent expensive work.

Where admitted capacity remains occupied across execution, its accounting MUST eventually be released, expired or reconciled when the admitted work completes, fails, times out or loses its worker.

The exact lease/heartbeat/storage mechanism is implementation scope.

The invariant is that crashed or abandoned technical execution cannot consume bounded shared capacity indefinitely without an accepted reason.

---

## 20. Time Semantics

Time-based ProtectionPolicy MUST define its interval/window semantics and boundary behaviour sufficiently for deterministic enforcement.

Protection time is platform-operational time unless a separately accepted authority explicitly requires another time basis.

Merchant opening hours or local business time MUST NOT implicitly redefine protection windows.

---

## 21. Protection-State Failure

There is no universal fail-open or fail-closed rule.

For every ProtectionTarget whose admission depends on protection state, the accepted ProtectionPolicy MUST define safe behaviour when authoritative protection state cannot be established.

The answer is target/context-specific.

Security-sensitive protection may require conservative rejection; low-risk work may permit a bounded accepted fallback where explicitly governed.

Infrastructure MUST NOT choose fail-open/fail-closed behaviour merely for convenience or availability.

---

## 22. Interaction with MS-PROT-062

`Resource Protection Admission` is an independent runtime decision dimension consumed by MS-PROT-062.

Normative distinction:

```text
ADMITTED
    ≠
semantically applicable
    ≠
authorised
    ≠
entitled
    ≠
trust-satisfied
    ≠
operationally eligible
    ≠
provider-ready
```

Resource Protection MAY be evaluated cheaply at ingress to reject obviously excessive work before expensive downstream checks, but this optimisation cannot turn admission into authority or bypass later required runtime predicates.

MS-PROT-062 v1.1 defines the corresponding runtime-composition amendment.

---

## 23. Interaction with MS-PROT-070

MS-PROT-070 remains authoritative for dependency failure, saturation response, controlled degradation and recovery.

MS-PROT-073 owns operational fairness, consumption admission and bounded protection restrictions.

Conceptually:

```text
MS-PROT-070
    detects/handles degraded execution conditions
        ↓
accepted protection path may be applied
        ↓
MS-PROT-073
    decides resource admission under its policy
```

Neither authority may invent semantic business success or silently rewrite capability truth.

---

## 24. Interaction with Audit and Administrative Intervention

Protection telemetry and material protection evidence MAY be consumed by MS-PROT-064-compliant audit/intervention paths.

Material actions such as broader security restriction, Merchant Account suspension, closure or other lasting consequence require separately authorised operations owned by the applicable authority.

A rate limiter, heuristic or protection store MUST NOT directly mutate those unrelated authorities.

---

## 25. Observability

MS-PROT-068 MAY observe:

```text
admission/rejection rates
budget pressure
restriction counts
protection failures
suspicious consumption patterns
```

Observability is evidence only.

An alert MUST NOT itself create a TemporaryProtectiveRestriction unless a separately accepted Resource Protection operation evaluates the evidence and establishes that restriction under an accepted ProtectionPolicy.

---

## 26. Failure and Decision Categories

Resource Protection MUST preserve distinctions sufficient to diagnose at least:

```text
ADMITTED
DEFERRED
RATE_LIMITED
TEMPORARILY_RESTRICTED
CAPACITY_SATURATED
PROTECTION_STATE_UNAVAILABLE
```

These are protection/runtime diagnostic outcomes, not business lifecycle states.

The exact implementation result types are downstream.

---

## 27. Falsification Summary

### 27.1 Runaway merchant integration

A faulty merchant integration can be constrained at Merchant/Integration/ProviderConnection scopes without disabling unrelated merchants or changing the merchant's capabilities, plan or ProviderConnection truth.

**PASS.**

### 27.2 Abusive Merchant Account creation

An authenticated and authorised principal can be independently denied protection admission for excessive establishment attempts. A bounded TemporaryProtectiveRestriction can contain the target-specific pressure without revoking authentication/authorisation or inventing Merchant Account cardinality.

**PASS.**

### 27.3 Scarce shared AI/provider capacity

Platform, merchant, provider and connection scopes can compose. Optional/deferable work may be constrained while bounded capacity is preserved for higher-guarantee or commitment-resolution work. Subscription plan names do not become direct protection policy.

**PASS.**

No falsified scenario requires Resource Protection to own business meaning, commercial state, identity truth or permanent account consequence.

---

## 28. Hard Invariants

1. Resource Protection MAY deny or defer execution; it MUST NOT grant semantic/business authority.
2. ProtectionTarget MUST be explicit and platform-owned; transport/infrastructure names MUST NOT silently define semantic protection targets.
3. ProtectionSubject MUST preserve scope without redefining identity semantics.
4. One attempted work item MAY participate in multiple applicable protection scopes.
5. Operational fairness limits MUST remain distinct from Commercial usage entitlements.
6. Commercial plan names MUST NOT directly select protection policy without separately accepted service-guarantee semantics.
7. Rate-limit exhaustion MUST NOT itself establish abuse/fraud or Merchant Account suspension.
8. Protection evidence MUST remain evidence unless consumed by the authority that owns a downstream consequence.
9. TemporaryProtectiveRestriction MUST be bounded in time or by explicit mandatory reevaluation.
10. Permanent/broader account, security or commercial consequences require their own owning authority.
11. Existing-commitment progression MAY receive bounded protected capacity only where the owning capability/process establishes the continuation requirement.
12. DEFER is permitted only where the owning operation/process independently supports deferred execution.
13. Protection policies MUST define their measurement basis.
14. Transport-attempt accounting MUST remain distinct from business idempotency.
15. Concurrency-sensitive budgets MUST use atomic admission/consumption semantics at the smallest required scope.
16. Time-based policy MUST define deterministic boundary semantics.
17. Protection-state failure behaviour MUST be ProtectionTarget/context-specific; no universal fail-open/fail-closed convention is authorised.
18. Anonymous protection correlation MUST NOT become CustomerAccount, Merchant Account or trusted identity.
19. Provider/resource protection failure MUST remain distinguishable from entitlement, authorisation, semantic applicability, operational eligibility and Provider Readiness.
20. Resource Protection Policy changes MUST NOT create Merchant Configuration revisions merely because platform operational limits changed.
21. Cross-account repeat-trial eligibility remains outside this authority.
22. Resource Protection technology and algorithm choices remain replaceable implementation details.

---

## 29. Explicit Non-Responsibilities

MS-PROT-073 does NOT define:

```text
numerical rate thresholds
rate-limiting algorithms
Token Bucket / Leaky Bucket / fixed/sliding window selection
Redis/database/gateway technology
commercial usage quotas
paid service-level guarantees
cross-account trial eligibility
fraud/legal determinations
permanent bans
Merchant Account suspension/closure
controller cardinality
device/network fingerprinting implementation
production deployment topology
provider commercial contract limits
```

These require downstream implementation design or separately accepted semantic authority as appropriate.

---

## 30. Acceptance Statement

Main Street now has a bounded platform-operational authority for fair shared-resource consumption and abuse containment without turning rate limiting into business or account governance.

> **Protect shared capacity, isolate unfair consumption, preserve independent authority, and never confuse admission control with business permission.**
