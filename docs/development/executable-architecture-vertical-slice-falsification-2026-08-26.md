# Main Street Executable Architecture Vertical-Slice Falsification — 26 August 2026

**Status:** FALSIFICATION COMPLETE — **PARTIAL PASS; ORDER-BEARING SLICE BLOCKED BY UNAPPROVED ORDER AUTHORITY**  
**Authority class:** Design-review / implementation-readiness evidence only  
**Governed by:** `designs/DESIGN-RULES.md`, `designs/DOCUMENT-GOVERNANCE.md`  
**Branch:** `development`  
**Purpose:** Re-run the end-to-end executable-architecture falsification after formalisation of ADR-011, ADR-012 and MS-PROT-027 v1.3, and determine whether representative merchant operations can cross semantic definition, configuration, activation, execution, persistence, projection, restart and deployment boundaries without implementation-time invention of material semantics.

This document creates no new business semantics and does not override accepted authority.

---

# 1. Acceptance Test

The governing implementation-readiness question is:

> **Can a competent engineer implement the tested scenario from accepted authority without inventing a material domain, ownership, consistency, historical-affinity, failure or architectural rule?**

Classification:

```text
implementation detail
    → PASS; implementation may choose within accepted constraints

design underspecification
    → FAIL; return to governed design

authority ambiguity
    → FAIL; return to governed design

semantic contradiction
    → FAIL; return to governed design
```

---

# 2. Architecture Path Under Test

```text
Accepted semantic definitions
        ↓
Published Semantic Registry Release
        ↓
SemanticReleaseAssembly / exact release evidence
        ↓
Merchant Configuration Revision
        ↓
Deterministic compilation
        ↓
Resolved Configuration Package
        ↓
Atomic activation
        ↓
Runtime RCP binding
        ↓
Executable-support resolution
        ↓
Capability-owned execution
        ↓
Authoritative persistence / provider boundary
        ↓
Projection + freshness/serviceability contract where triggered
        ↓
Exposure / API / Surface
        ↓
restart / deployment / recovery / historical execution
```

---

# 3. EAC Closure Re-Test

## 3.1 Release publication / restart

Scenario:

```text
Merchant A active C18 @ R8
Merchant B active C27 @ R9
process restarts
```

Required result:

- exact retained RCPs may resume without mandatory recompilation where accepted runtime authority permits;
- any source-release-resolution path for R8 resolves immutable published R8 evidence rather than relabelled current definitions;
- R9 remains independently resolvable;
- partial/corrupt release evidence cannot be treated as valid R8/R9;
- recovery and ordinary bootstrap preserve the same release meaning.

Authority:

```text
MS-PROT-054
ADR-010
ADR-011
MS-PROT-040 v1.1
MS-TAS-RECOVERY-001
```

**PASS — no material implementation-time semantic decision remains.**

---

## 3.2 Historical executable support / deployment

Scenario:

```text
historical commitment requires execution contract under R8
new deployment primarily serves R9
```

Required result:

- invocation binds exact semantic/RCP context;
- runtime proves executable support for the affected execution contract plus required participant/effect contracts;
- unsupported process does not fall through to newest handler;
- one implementation may serve multiple releases only when contract support is conformant;
- deployment does not knowingly orphan the last safe still-required path;
- security/privacy/integrity authority may prohibit unsafe old execution without rewriting historical business truth.

Authority:

```text
MS-PROT-023
MS-PROT-040
MS-PROT-054
MS-PROT-069
MS-PROT-070
MS-PROT-072
ADR-012
```

**PASS — physical manifest/routing mechanism remains a valid implementation choice.**

---

## 3.3 Projection freshness / serviceability

Scenario:

```text
public/merchant projection is asynchronous or cached
source temporarily unavailable or projection known stale
```

Required result:

- A–G applicability triggers determine whether a dedicated Projection Contract is required;
- exactly one Projection Owner owns the contract;
- required freshness/provenance evidence is explicit;
- stale serving occurs only under that contract;
- absence/corruption of required evidence is not treated as `current`;
- privacy/security/Exposure revocation outranks stale-serving tolerance;
- command path revalidates authoritative truth;
- one failed projection contribution does not erase an independently required residual Surface responsibility;
- rebuild claims depend on retained source/provenance evidence.

Authority:

```text
MS-PROT-027 v1.1 + v1.2 + v1.3
MS-PROT-049
MS-PROT-053
MS-PROT-059
MS-PROT-070
```

**PASS — exact cache/index/TTL technology remains implementation detail.**

---

# 4. Physical Retailer Stress Test

Representative merchant:

```text
merchant sells finite-stock physical products
public storefront
optional guest/customer-account ordering
payment provider
collection or shipment
staff may operate through merchant dashboard/POS
```

Desired path:

```text
Offering/Product
    ↓
Inventory availability projection
    ↓
customer intent
    ↓
authoritative Inventory revalidation + claim
    ↓
Order commitment
    ↓
Payment Obligation / provider execution
    ↓
Order Fulfilment
    ↓
Shipment / collection
    ↓
Return/refund where applicable
```

## 4.1 Last-unit concurrency

Accepted Inventory authority requires the stock claim and owning Order/commercial commitment to preserve the atomic oversell-prevention invariant.

However, no accepted current authority owns:

- exact Order commitment identity;
- Order commitment-portion establishment;
- Order amendment/release semantics; or
- the Order side of that atomic contract.

**FAIL — DESIGN UNDERSPECIFICATION: Order / Ordering semantic authority.**

## 4.2 Duplicate HTTP / lost acknowledgement

MS-PROT-059 and MS-PROT-069 govern logical duplicate delivery and acknowledgement uncertainty, but the exact authoritative effect whose duplicate must be prevented is the missing Order commitment operation.

**FAIL only at the Order operation owner; generic idempotency architecture is governed.**

## 4.3 Payment timeout after commitment

MS-PROT-055/069 govern Payment Obligation, provider evidence and uncertainty, but accepted authority does not yet define the upstream Order commitment/release operation against which the payment obligation is related.

**FAIL at Order boundary; Money/provider uncertainty itself is governed.**

## 4.4 Projection lag

Inventory/order/customer projections can be governed by MS-PROT-027 v1.3 once their exact projection contracts exist.

**PASS at projection architecture; blocked only by missing Order source semantics.**

## 4.5 Subscription downgrade

MS-PROT-056 residual-access rules prevent commercial entitlement loss from silently erasing existing commitments.

**PASS at commercial-access architecture; exact Order residual obligation still requires the Order authority.**

## 4.6 Staff authority loss

MS-PROT-063/074/062 govern authentication/device/actor/access decisions independently from Order state.

**PASS at access architecture; Order operation ownership remains the only semantic blocker.**

### Physical retailer verdict

**BLOCKED.** The newly reviewed Order authority is material and cannot be filled from ADR-005 or e-commerce convention.

---

# 5. Appointment-Based Online Consultant Stress Test

Representative merchant:

```text
consultation Offering
Appointment capability
optional Payment
possible external calendar provider
merchant/customer projections
```

## 5.1 Two users choose final slot

Scheduling/Appointment authority owns authoritative capacity/revalidation and appointment commitment.

**PASS.**

## 5.2 External calendar unavailable

Main Street calendar remains authoritative where applicable; provider readiness/degradation stays contextual and provider-neutral.

**PASS.**

## 5.3 Stale calendar projection

MS-PROT-027 v1.3 supplies the Projection Contract boundary; command revalidates Scheduling authority.

**PASS.**

## 5.4 Merchant edits business hours while request is open

Commit operation revalidates current authoritative scheduling/time semantics; stale form/view is not mutation authority.

**PASS.**

## 5.5 Deploy newer application with old Appointment outstanding

Exact semantic/RCP context plus ADR-012 executable support prevents silent newest-handler reinterpretation.

**PASS.**

### Consultant verdict

**PASS — no new material architecture gap discovered.**

---

# 6. Information Publisher Stress Test

Representative merchant:

```text
Merchant Profile
Publication / Opportunity
public storefront/content
optional Enquiry
no commerce commitment
```

Required absence test:

```text
Order            NOT REQUIRED
Inventory        NOT REQUIRED
Payment          NOT REQUIRED
Booking          NOT REQUIRED
Appointment      NOT REQUIRED
```

Public/cached projections use MS-PROT-027 v1.3 only where an explicit trigger applies.

Release/restart/deployment semantics remain governed by ADR-011/012 if the merchant's RCP/release context requires them.

**PASS — architecture remains capability-composed rather than commerce-gated.**

---

# 7. Hybrid Salon / Retailer Stress Test

Representative merchant:

```text
Appointment
    scheduled services

Order
    retail product purchases

Inventory
    retail stock and possibly service-consumed materials

CustomerContext / Enquiry
    merchant-customer relationship
```

The architecture correctly avoids one generic `CustomerWork` or `BusinessOrder` owner.

Appointment path is governed.

Retail Order path reaches the same missing Order / Ordering authority as the physical-retailer test.

**PARTIAL PASS — composition is sound; Order-bearing path remains blocked.**

---

# 8. Restart + Deployment Combined Falsification

Scenario:

```text
R8 published immutable
Merchant A C18 @ R8
historical Appointment A100 @ R8

R9 published immutable
Merchant B C27 @ R9

new application deployment
process restart
R8 provider dependency degraded
one R8 projection stale
```

Required outcomes:

1. R8 and R9 identities remain exact after restart. — **PASS ADR-011**
2. C18/C27 retain exact RCP/release affinity. — **PASS MS-PROT-040/022**
3. A100 does not execute through incompatible newest handler. — **PASS ADR-012**
4. provider degradation does not rewrite semantic configuration. — **PASS MS-PROT-048/049/070**
5. stale projection follows owner contract and cannot authorise mutation. — **PASS MS-PROT-027 v1.3**
6. security/privacy revocation outranks cached serving. — **PASS MS-PROT-027 v1.3/MS-PROT-053/070**
7. unaffected merchant/new activity may remain serviceable where independently safe. — **PASS ADR-012/MS-PROT-070**
8. recovery uses the same R8 semantic meaning as ordinary bootstrap. — **PASS ADR-011/MS-TAS-RECOVERY-001**

**PASS.**

The previously identified executable-architecture EAC seam is closed for this scenario.

---

# 9. New Omissions Discovered During This Re-Run

No new EAC-class release/bootstrap/executable-support/projection gap was discovered.

One already-promoted semantic gap remains material:

```text
Order / Ordering commitment authority
```

No additional material architecture gap was discovered in the information-publisher or Appointment consultant slices.

Analytics, PRD traceability, TAS consolidation and repository hygiene remain non-blocking product/governance work and are not required to make the tested non-Order execution architecture semantically coherent.

---

# 10. Implementation-Readiness Matrix

| Scope | Result | Reason |
|---|---|---|
| Semantic release publication/bootstrap | READY | ADR-011 accepted |
| Historical executable compatibility | READY | ADR-012 accepted |
| Projection serviceability/rebuild boundary | READY | MS-PROT-027 v1.3 accepted |
| Configuration → RCP → activation | READY | MS-PROT-022/040 |
| Appointment consultant vertical slice | READY FOUNDATION | no material gap discovered |
| Information publisher vertical slice | READY FOUNDATION | no commerce dependency required |
| Physical Order retailer slice | **BLOCKED** | Order / Ordering authority awaiting post-review approval/formalisation |
| Hybrid retail portion | **BLOCKED** | same Order gap |
| Analytics implementation | INTENTIONALLY DEFERRED | product/semantic design not required for current executable slice |

`READY FOUNDATION` means material authority boundaries are sufficient; it does not mean every endpoint, UI, provider adapter or concrete implementation choice is designed.

---

# 11. Governance Verdict

The EAC closure programme has achieved its intended result:

```text
EAC-001    CLOSED
EAC-002    CLOSED
EAC-003    CLOSED
```

The architecture should **not** reopen those topics merely because concrete implementation technology remains undecided.

The full commerce-inclusive implementation gate remains blocked by exactly one material semantic decision:

```text
Order / Ordering commitment authority
```

That gap has completed proposal/review/falsification in:

`docs/development/order-authority-design-review-falsification-2026-08-26.md`

but cannot be formalised until explicit post-review manual approval is obtained according to `DESIGN-RULES.md`.

Independent implementation scopes whose accepted authority is complete are not semantically blocked by the Order gap, but implementation planning should avoid accidentally creating Order semantics in shared infrastructure while the gap remains open.

---

# 12. Acceptance Statement

> **The executable architecture from semantic release through restart, compatible runtime execution and safe projection serving now survives the representative non-Order vertical slices without material implementation-time invention. The physical-retail/hybrid Order path fails at one precise upstream semantic boundary: Order commitment ownership and lifecycle. No broader EAC redesign is justified by this re-run.**
