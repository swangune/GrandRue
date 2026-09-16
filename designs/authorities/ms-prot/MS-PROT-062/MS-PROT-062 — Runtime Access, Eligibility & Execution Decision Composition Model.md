# MS-PROT-062 — Runtime Access, Eligibility & Execution Decision Composition Model

**Document ID:** MS-PROT-062  
**Version:** 1.0  
**Status:** ACCEPTED  
**Depends on:** MS-PROT-022, MS-PROT-023, MS-PROT-025, MS-PROT-028, MS-PROT-032, MS-PROT-048, MS-PROT-056, MS-PROT-059  
**Purpose:** Define how independent runtime constraints compose when Main Street decides whether an operation may proceed to authoritative execution, without creating a universal policy engine or transferring semantic ownership away from the capabilities and platform authorities that own each predicate.

---

## 1. Problem

Main Street already defines several independent dimensions that affect whether an attempted operation can execute:

```text
semantic applicability
commercial entitlement
actor authorisation
trust requirements
operational eligibility
provider readiness
current authoritative state
```

These dimensions are intentionally owned by different authorities.

The unresolved backend question is:

> **How are those independent dimensions composed into one execution decision without collapsing them into one vague `enabled`, `available`, `allowed`, or universal permission concept?**

This document defines that composition boundary.

It does not redefine the meaning or ownership of any underlying predicate.

---

## 2. Governing principle

> **Runtime decision composition owns sequencing and diagnostic composition; it does not own the semantic predicates being evaluated.**

Conceptually:

```text
Command / requested operation
        ↓
resolve operation + context
        ↓
collect applicable independent constraints
        ↓
evaluate each through its owning authority
        ↓
preliminary execution decision
        ↓
authoritative consistency boundary
        ↓
revalidate concurrency-sensitive conditions
        ↓
capability-owned execution
```

A composed decision is therefore not itself a new business truth.

---

## 3. Canonical runtime decision dimensions

The following terms are normative.

### 3.1 Semantic Applicability

**Definition:** Whether the requested capability/operation belongs to the merchant's resolved semantic model in the current semantic context.

**Owner:** Configuration/compiler/resolved-model authority.

**Examples:**

```text
Appointment configured for this merchant?
Delivery method configured?
Inventory operation applicable to this merchant model?
```

Semantic applicability is not commercial entitlement.

### 3.2 Commercial Entitlement

**Definition:** Whether the merchant's current commercial state permits use of functionality that is commercially gated.

**Owner:** Subscription/entitlement authority.

Commercial entitlement does not create or remove semantic configuration.

### 3.3 Actor Authorisation

**Definition:** Whether the resolved execution principal may perform the requested operation on the requested subject/context.

**Owner:** Identity/access/authority semantics applicable to the operation.

Authentication alone is insufficient.

### 3.4 Trust Satisfaction

**Definition:** Whether the smallest applicable trust, verification or confidence requirement required for the operation/context is currently satisfied.

**Owner:** Trust/verification authority.

Trust requirements must not be broadened to merchant-wide restrictions unless the governing authority explicitly requires merchant-wide scope.

### 3.5 Operational Eligibility

**Definition:** Whether current capability-owned business facts, lifecycle conditions, policy predicates and invariants permit the requested operation now.

**Owner:** The capability that owns the authoritative business semantics.

Examples:

```text
appointment is cancellable under applicable merchant policy
stock quantity is sufficient for requested allocation
order state permits confirmation
```

Operational eligibility is not commercial entitlement or actor authorisation.

### 3.6 Provider Readiness

**Definition:** Whether a required internal/external provider can currently discharge the provider responsibility required by the operation.

**Owner:** Provider participation/readiness subsystem under the governing provider contract.

Provider readiness is evaluated only where a provider is actually required.

### 3.7 Authoritative Revalidation

**Definition:** Re-establishment, inside the consistency boundary of execution, of every concurrency-sensitive condition whose staleness could violate an invariant.

**Owner:** The authoritative execution boundary and participating capability owners.

A prior successful evaluation never substitutes for authoritative revalidation when the invariant can change concurrently.

---

## 4. Composition does not imply one universal engine

This document does not require a physical implementation named:

```text
EligibilityEngine
PermissionEngine
RuntimeDecisionEngine
UniversalAccessService
```

The composition may be implemented through application orchestration, capability contracts, policy evaluators, guards or other structures consistent with accepted backend architecture.

The invariant is semantic:

> **All applicable decision dimensions must be resolved without transferring ownership of those dimensions to the composition layer.**

---

## 5. Applicability of predicates

Not every operation requires every dimension.

The runtime must determine which predicates are applicable to the requested operation/context.

Examples:

```text
local merchant-only note creation
    provider readiness may be NOT_APPLICABLE

public guest enquiry
    commercial entitlement may be NOT_APPLICABLE
    depending on accepted plan authority

carrier-backed return-label generation
    provider readiness IS_APPLICABLE
```

The system must not fabricate meaningless checks merely to satisfy a fixed checklist.

---

## 6. Canonical evaluation sequence

The default logical sequence is:

```text
1. resolve merchant / execution scope
2. resolve requested operation
3. establish semantic applicability
4. establish execution principal and actor authorisation
5. establish commercial entitlement where applicable
6. establish trust satisfaction where applicable
7. establish preliminary operational eligibility
8. establish provider readiness where applicable
9. enter authoritative consistency boundary
10. revalidate concurrency-sensitive conditions
11. execute through owning capability/application contract
12. commit authoritative effects
13. publish post-commit consequences according to existing event rules
```

This order is a canonical reasoning model, not a requirement that every implementation perform one network/database call per step.

Implementations may safely combine checks where ownership and failure meaning remain unambiguous.

---

## 7. Preliminary decision versus mutation authority

A successful preliminary decision means only:

> **No currently evaluated pre-execution constraint has rejected the request.**

It does not grant durable mutation authority.

Therefore:

```text
preliminary evaluation = PASS
```

must never be interpreted as:

```text
mutation is guaranteed to succeed later
```

Where authoritative conditions may change between evaluation and execution, they must be revalidated inside the execution consistency boundary.

This preserves MS-PROT-023 and MS-PROT-025.

---

## 8. Canonical rejection categories

A rejection result should preserve the dimension that failed.

The canonical diagnostic categories are:

```text
NOT_APPLICABLE
NOT_ENTITLED
NOT_AUTHORISED
TRUST_REQUIREMENT_UNSATISFIED
OPERATIONALLY_INELIGIBLE
PROVIDER_UNAVAILABLE
CONFLICT_ON_REVALIDATION
INVALID_INPUT
TARGET_NOT_FOUND_OR_NOT_ACCESSIBLE
```

These are runtime decision/result categories.

They are not domain lifecycle states.

A capability may expose more specific owned reasons beneath a category where doing so is safe and useful.

---

## 9. No vague universal `enabled` flag

The following model is prohibited as semantic authority:

```text
operation.enabled = true/false
```

when the value silently collapses multiple dimensions.

A surface may derive a simple presentation state such as:

```text
available
unavailable
upgrade required
verification required
```

but the underlying authoritative decision dimensions must remain distinguishable.

---

## 10. Commercial entitlement boundary

A merchant may have:

```text
semantic applicability = true
commercial entitlement = false
```

This means the business model remains configured, while the currently protected use is commercially unavailable.

Entitlement loss must not mutate:

```text
MerchantConfiguration
Resolved semantic meaning
existing business commitments
capability-owned historical truth
```

Existing-commitment residual management remains governed by MS-PROT-056.

---

## 11. Actor authorisation boundary

An entitled operation may still be unauthorised for the current principal.

Example:

```text
merchant plan allows refunds
staff principal lacks refund authority
```

Result:

```text
semantic applicability = true
commercial entitlement = true
actor authorisation = false
→ NOT_AUTHORISED
```

The runtime must not report this as `NOT_ENTITLED` merely because both outcomes deny execution.

---

## 12. Trust boundary

A trust requirement applies only where the governing trust authority makes it applicable.

Example:

```text
operation requires verified controlling-person identity
```

must not become:

```text
all merchant capabilities disabled
```

unless a merchant-wide trust invariant has separately been accepted.

Failure result:

```text
TRUST_REQUIREMENT_UNSATISFIED
```

must remain distinct from authorisation and entitlement.

---

## 13. Operational eligibility boundary

Operational eligibility belongs to the capability's current business semantics.

Examples:

```text
cancel appointment
    → current appointment state + merchant cancellation policy

allocate stock
    → current inventory authority + requested quantity

confirm order
    → order invariants + required participating state
```

The composition layer must not independently reinterpret these rules.

It invokes/evaluates the owning capability's published contract.

---

## 14. Provider readiness boundary

Provider readiness is relevant only when provider participation is required for the requested effect.

A provider outage may make one operation unavailable without changing unrelated merchant semantics.

Example:

```text
GenerateCarrierReturnLabel
    provider readiness = false
    → PROVIDER_UNAVAILABLE

RecordManualRefundEvidence
    may remain unaffected
```

Provider failure must not silently mutate merchant configuration or capability semantics.

---

## 15. Authoritative revalidation and concurrency

Any pre-execution fact that can become stale and violate an invariant must be revalidated inside the appropriate consistency boundary.

Examples:

```text
slot availability
room availability
stock quantity
versioned order state
exclusive allocation
capacity
```

Canonical pattern:

```text
pre-check
   ↓
possible user/application delay
   ↓
execution consistency boundary
   ↓
revalidate authoritative state
   ↓
execute or reject with CONFLICT_ON_REVALIDATION
```

A stale `PASS` is never mutation authority.

---

## 16. Cross-capability operations

Where one accepted invariant spans several capabilities, a narrow application coordinator may compose the applicable decisions.

The coordinator may sequence calls such as:

```text
Ordering contract
Inventory contract
Payment evidence contract
```

but does not acquire ownership of Order, Inventory or Payment truth.

Where atomicity is required, MS-PROT-025 governs the consistency boundary.

---

## 17. Existing commitments under entitlement change

For an existing commitment, runtime may legitimately distinguish:

```text
create new commitment
    commercially denied

manage/cancel/complete existing commitment
    residual operation permitted
```

The exact residual operation set belongs to the applicable accepted authority.

The composition layer evaluates those explicit semantics; it does not infer them from plan names.

---

## 18. Surface exposure is downstream

Whether an operation is shown in a merchant/customer/public interface is not identical to whether it can execute.

A surface may hide an operation known to be unavailable, but:

```text
hidden
```

does not become mutation authority.

Conversely, a stale visible control does not guarantee execution.

Final execution always uses runtime evaluation and authoritative revalidation.

---

## 19. Error disclosure and security

Internal rejection reason and externally disclosed reason may differ.

Example:

```text
internal:
TARGET_NOT_FOUND_OR_NOT_ACCESSIBLE

external:
Not found
```

where disclosing whether a target exists would leak information.

Diagnostic precision must not defeat security or privacy policy.

---

## 20. Idempotency and duplicate attempts

A duplicate command may pass the same applicability/entitlement/authorisation checks as the original command.

That does not permit duplicate authoritative effect.

MS-PROT-059 idempotency and duplicate-action rules remain independently applicable.

Therefore runtime decision composition must preserve operation/correlation/idempotency context required by the owning execution boundary.

---

## 21. AI boundary

AI may help a user formulate or select an operation.

AI does not determine runtime authority.

The following is prohibited:

```text
AI confidence high
    → bypass entitlement/authorisation/invariants
```

All authoritative operations initiated through AI-assisted surfaces pass through the same runtime composition and execution contracts as any other channel.

---

## 22. Provider callbacks and system principals

A provider callback or system process has an execution principal/origin but does not receive universal authority.

It remains subject to every applicable dimension, including:

```text
semantic applicability
integration authority
trust/authentication of provider evidence
operational eligibility
idempotency
revalidation
```

where relevant.

---

## 23. Query versus command behaviour

This document primarily governs attempted authoritative operations.

Read/query surfaces may use related dimensions for exposure and read authorisation, but a query result must not be treated as future mutation authority.

Example:

```text
query says stock available
```

is not sufficient authority for later allocation.

---

## 24. Caching

Some decision inputs may be cached where the owning authority permits it.

Cached results must not outlive the correctness requirements of the operation.

In particular, caching is prohibited as a substitute for authoritative revalidation of concurrency-sensitive predicates.

---

## 25. Failure semantics

A failed pre-execution decision must not mutate authoritative business state.

A failure during authoritative revalidation must also leave the protected invariant intact.

Independent diagnostics/audit evidence may still be recorded according to accepted audit/data-protection authority.

---

## 26. Auditability

Runtime decision evidence should be sufficient to explain, where required and safe:

```text
what operation was attempted
which merchant/scope applied
which principal/origin acted
which decision dimension rejected execution
which authoritative execution committed
correlation/idempotency identity where applicable
```

This does not require storing every successful intermediate predicate forever.

Retention follows the accepted data-protection/evidence authority.

---

## 27. Falsification — configured but not entitled

Scenario:

```text
Appointment semantics configured
merchant commercial state lacks required entitlement
```

Expected:

```text
applicability = PASS
entitlement = FAIL
→ NOT_ENTITLED
```

Merchant configuration remains unchanged.

**PASS.**

---

## 28. Falsification — entitled but unauthorised staff

Scenario:

```text
refund capability applicable
plan entitlement present
staff lacks refund privilege
```

Expected:

```text
→ NOT_AUTHORISED
```

No entitlement or payment semantics change.

**PASS.**

---

## 29. Falsification — provider outage

Scenario:

```text
carrier-label operation applicable
merchant authorised
free-return policy applicable
carrier integration unavailable
```

Expected:

```text
→ PROVIDER_UNAVAILABLE
```

The merchant's return policy and Order remain unchanged.

**PASS.**

---

## 30. Falsification — stale availability

Scenario:

```text
T1 preliminary availability = true
T2 competing execution consumes capacity
T3 original execution attempts commit
```

Expected:

```text
authoritative revalidation fails
→ CONFLICT_ON_REVALIDATION
```

No double allocation occurs.

**PASS.**

---

## 31. Falsification — operation with no provider

Scenario:

```text
merchant adds an internal note
```

Provider readiness is not applicable.

The system must not fail because no provider binding exists.

**PASS.**

---

## 32. Falsification — existing commitment after downgrade

Scenario:

```text
merchant downgrades
new appointment creation no longer entitled
existing appointment must still be completed/cancelled according to residual-management authority
```

Expected:

```text
CreateAppointment → NOT_ENTITLED
CompleteExistingAppointment → evaluate applicable residual entitlement and operational rules
```

No configuration reconstruction occurs.

**PASS.**

---

## 33. Falsification — channel convergence

The same operation originates from:

```text
website
merchant dashboard
phone-assisted merchant entry
AI assistant
```

Expected:

All channels converge on the same runtime decision and capability-owned execution semantics.

A channel cannot bypass checks merely because it is internal or AI-assisted.

**PASS.**

---

## 34. Explicitly rejected designs

The following are rejected:

```text
one universal enabled flag
one universal permission engine owning all predicates
commercial plan rewriting semantic configuration
provider health rewriting capability semantics
surface visibility being treated as execution authority
authentication being treated as authorisation
system principal being omnipotent
AI confidence bypassing deterministic runtime checks
preliminary evaluation authorising later stale mutation
exactly one fixed checklist for every operation
all provider failures becoming merchant-wide outages
cross-capability coordinator owning participating business state
```

---

## 35. Internal contract requirements

Any implementation boundary that composes runtime decisions must make clear:

```text
requested operation identity
merchant/execution scope
principal/origin
subject/context references
applicable decision dimensions
owning contract for each evaluated predicate
preliminary result
revalidation requirements
idempotency/correlation context
final execution result or rejection category
```

It must not expose another module's internal repositories merely to perform evaluation.

---

## 36. Implementation freedom

This authority does not mandate:

```text
one Java class
one service
one framework
one policy engine
one rules engine
one middleware filter chain
```

Implementation must preserve the semantic boundaries and may choose the smallest structure consistent with the accepted backend module architecture and implementation rules.

---

## 37. Accepted invariants

1. Runtime decision composition owns sequencing/diagnostics, not underlying semantic predicates.
2. Semantic applicability, commercial entitlement, actor authorisation, trust satisfaction, operational eligibility, provider readiness and authoritative revalidation are distinct concepts.
3. Not every decision dimension applies to every operation.
4. A successful preliminary evaluation is not durable mutation authority.
5. Concurrency-sensitive predicates must be revalidated within the execution consistency boundary.
6. Entitlement loss does not rewrite merchant configuration.
7. Authentication does not imply authorisation.
8. Provider unavailability does not redefine capability semantics.
9. Operational eligibility remains capability-owned.
10. Surface visibility/exposure does not grant execution authority.
11. AI-assisted requests use the same deterministic runtime authority path.
12. System/integration principals are not omnipotent.
13. Rejection categories are diagnostics, not domain lifecycle states.
14. Cross-capability coordinators preserve participating capability ownership.
15. Idempotency remains independently enforced for duplicate attempts.
16. Internal diagnostic precision may be reduced at external surfaces for security/privacy.
17. A universal `enabled` flag is not accepted as authoritative runtime semantics.
18. The composition model does not mandate one physical universal engine.

---

## 38. Backend completion consequence

With MS-PROT-062 accepted, the backend has an explicit composition contract connecting:

```text
Resolved configuration semantics
        +
Subscription entitlement
        +
Identity/access authority
        +
Trust requirements
        +
Capability-owned operational rules
        +
Provider readiness
        +
Authoritative consistency/revalidation
        ↓
Runtime execution decision
```

This closes the identified ambiguity around the phrase "can this operation execute now?" without introducing a new semantic owner.

---

## 39. Governance verdict

```text
MS-PROT-062
Runtime Access, Eligibility & Execution Decision Composition Model

PROPOSE                    ✓
REVIEW                     ✓
FALSIFICATION              ✓
RECOMMENDATION             ✓
MANUAL APPROVAL            ✓
FORMALISED                 ✓
```

**Status: ACCEPTED**
