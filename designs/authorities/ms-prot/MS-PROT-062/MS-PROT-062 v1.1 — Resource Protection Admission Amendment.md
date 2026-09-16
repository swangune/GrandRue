# MS-PROT-062 v1.1 — Resource Protection Admission Amendment

**Document ID:** MS-PROT-062  
**Version:** 1.1  
**Status:** ACCEPTED  
**Amends:** MS-PROT-062 v1.0 — Runtime Access, Eligibility & Execution Decision Composition Model  
**Depends on:** MS-PROT-062 v1.0, MS-PROT-073  
**Purpose:** Add Resource Protection Admission as an independent runtime decision dimension without transferring Platform Resource Protection ownership into runtime composition.

---

## 1. Scope

This amendment is intentionally narrow.

It does not redefine the existing MS-PROT-062 meanings of Semantic Applicability, Commercial Entitlement, Actor Authorisation, Trust Satisfaction, Operational Eligibility, Provider Readiness or Authoritative Revalidation.

It adds one independent dimension required by the accepted MS-PROT-073 Platform Resource Protection authority.

Unrelated MS-PROT-062 v1.0 rules remain unchanged.

---

## 2. New Canonical Runtime Dimension — Resource Protection Admission

### Definition

**Resource Protection Admission** means:

> Whether the attempted execution is currently admitted to consume the applicable protected platform resources under the authoritative Platform Resource Protection policy and state.

### Owner

```text
Platform Resource Protection
```

as defined by MS-PROT-073.

Runtime composition consumes the decision. It does not own, duplicate or reinterpret the underlying protection policy or consumption state.

---

## 3. Hard Distinction

The following inequality is normative:

```text
Resource Protection Admission
    ≠
Semantic Applicability
    ≠
Commercial Entitlement
    ≠
Actor Authorisation
    ≠
Trust Satisfaction
    ≠
Operational Eligibility
    ≠
Provider Readiness
    ≠
Authoritative Revalidation
```

Therefore:

```text
Protection Admission = ADMIT
```

means only that the attempted work may consume the applicable protected resources at that point in the evaluation.

It MUST NOT be interpreted as permission to execute if another required runtime dimension rejects the operation.

Likewise, Resource Protection rejection or deferral MUST NOT be reported internally as though semantic applicability, authorisation, entitlement, trust, operational eligibility or Provider Readiness failed.

---

## 4. Applicability

Resource Protection Admission is evaluated only where one or more accepted ProtectionPolicies apply to the requested work/context.

For work with no applicable ProtectionTarget/ProtectionPolicy:

```text
Resource Protection Admission = NOT_APPLICABLE
```

No meaningless protection check is required merely because MS-PROT-062 contains the dimension.

---

## 5. Logical Composition Sequence

The MS-PROT-062 canonical reasoning sequence is amended conceptually to include Resource Protection Admission:

```text
1. resolve merchant / execution scope
2. resolve requested operation
3. establish semantic applicability
4. establish execution principal and actor authorisation
5. establish commercial entitlement where applicable
6. establish trust satisfaction where applicable
7. establish Resource Protection Admission where applicable
8. establish preliminary operational eligibility
9. establish Provider Readiness where applicable
10. enter authoritative consistency boundary
11. revalidate concurrency-sensitive conditions
12. execute through owning capability/application contract
13. commit authoritative effects
14. publish post-commit consequences according to accepted event rules
```

This remains a logical reasoning model, not a requirement for one physical call per step.

### 5.1 Early Technical Protection

An implementation MAY perform a cheap Resource Protection check earlier at ingress when this prevents expensive work or protects the platform from obvious floods.

Such early rejection is only a protective optimisation.

It MUST NOT:

```text
grant later authority
skip required semantic/runtime checks when admitted
change the owning meaning of the protection decision
```

---

## 6. Decision Preservation

MS-PROT-073 defines canonical protection decisions:

```text
ADMIT
DEFER
REJECT
```

Runtime composition MUST preserve their meaning.

### ADMIT

Continue evaluation of other applicable runtime dimensions.

### DEFER

Do not execute immediately. Deferred progression is valid only where the owning operation/process independently supports deferred execution.

Runtime composition MUST NOT invent queued/asynchronous semantics merely because Resource Protection returned `DEFER`.

### REJECT

Do not permit the attempted work to consume the protected resource under the current protection state.

The requested capability/business state remains unchanged unless another separately authorised operation changes it.

---

## 7. Diagnostic Separation

Runtime/API/application results SHOULD preserve the Resource Protection dimension sufficiently to distinguish protection outcomes from existing MS-PROT-062 rejection categories.

The exact public/internal error type is downstream implementation design.

At minimum, implementation MUST NOT collapse:

```text
resource protection rejection
```

into:

```text
NOT_AUTHORISED
NOT_ENTITLED
TRUST_REQUIREMENT_UNSATISFIED
OPERATIONALLY_INELIGIBLE
PROVIDER_UNAVAILABLE
```

where those statements are not true.

---

## 8. Authoritative Revalidation Interaction

Resource Protection may itself have concurrency-sensitive consumption state.

Where the accepted ProtectionPolicy requires atomic admission/consumption accounting, that invariant is governed by MS-PROT-073.

MS-PROT-062 does not require all protection accounting to occur inside a capability's business transaction.

However, a preliminary `ADMIT` MUST NOT be treated as durable reserved capacity unless the applicable ProtectionPolicy explicitly establishes such reservation/lease semantics.

If protection admission can become stale before the protected expensive work begins, implementation must obey the owning ProtectionPolicy's revalidation/consumption contract.

---

## 9. Existing Commitment Interaction

Where MS-PROT-073 preserves bounded capacity for work required to resolve an existing authoritative commitment, MS-PROT-062 consumes the resulting Resource Protection Admission decision.

Runtime composition does not decide whether an operation qualifies as commitment-resolution work.

That classification remains owned by the applicable capability/process authority.

---

## 10. Provider Interaction

`Provider Readiness` and `Resource Protection Admission` remain independent.

Examples:

```text
Provider Ready = YES
Resource Protection Admission = REJECT
```

is valid when provider-call capacity is being protected.

Likewise:

```text
Resource Protection Admission = ADMIT
Provider Ready = NO
```

still prevents execution where the provider is required.

No mapping between these dimensions is implied.

---

## 11. Commercial Interaction

Commercial Entitlement and Resource Protection Admission remain independent.

A merchant may be commercially entitled but temporarily not admitted to consume shared capacity under an applicable protection rule.

This MUST NOT be represented as entitlement revocation.

Conversely, Resource Protection admission cannot create commercial permission that does not exist.

---

## 12. Hard Invariants

1. Resource Protection Admission is an independent MS-PROT-062 runtime dimension.
2. Platform Resource Protection owns the underlying policy, state and decision semantics.
3. Runtime composition MUST NOT convert `ADMIT` into business authority.
4. Runtime composition MUST preserve protection rejection/deferral as distinct from unrelated rejection dimensions.
5. Resource Protection Admission is NOT_APPLICABLE where no accepted protection policy applies.
6. Early ingress protection MAY reject work for efficiency but cannot bypass later required runtime checks when work is admitted.
7. `DEFER` is valid only where deferred execution is independently supported by the owning operation/process.
8. Preliminary protection admission is not automatically a durable capacity reservation.
9. Provider Readiness and Resource Protection Admission remain distinct.
10. Commercial Entitlement and Resource Protection Admission remain distinct.
11. Existing-commitment classification remains owned by the capability/process authority, not runtime composition or Resource Protection.
12. Unrelated MS-PROT-062 v1.0 semantics remain unchanged.

---

## 13. Acceptance Statement

MS-PROT-062 now composes Platform Resource Protection without turning runtime composition into the owner of fairness, rate-limit or abuse-protection semantics.

> **Admission may constrain execution; it never substitutes for semantic applicability, authorisation, entitlement, trust, operational validity or provider readiness.**
