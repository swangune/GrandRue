# IMP-00 — Implementation Conformance Baseline

**Programme:** MS-IMP-001 — Production Implementation Dependency Governance  
**Macro target:** IMP-00 — Current Implementation Baseline  
**Status:** COMPLETE  
**Recorded:** 28 August 2026  
**Baseline head before this evidence commit:** `140558e7fe232f01b9611d371e33c5fdbe2c9595`  
**Governing execution rules:** MS-IMPLEMENTATION-RULES-001 v1.3  
**Purpose:** Record the repository-state evidence required to determine which current implementation is conforming, partial, absent or conflicting with the complete accepted authority corpus, and to establish the first fine-grained implementation graph under MS-IMP-001.

---

## 1. Governing Constraint

This document is implementation evidence only.

It does not create semantic authority, capability ownership, business lifecycle state, transaction semantics, provider responsibility or implementation strategy beyond accepted MS-IMP-001 and current substantive authority.

Repository code and tests are evidence. Existing implementation counts as satisfying a macro/child prerequisite only where accepted authority + implementation + tests + conformance establish equivalent completion.

---

## 2. Fresh Build Baseline

The current accepted implementation-governance head was verified through GitHub Actions against PostgreSQL rather than inferred from the historical implementation-status note.

Fresh verified baseline:

```text
Production Java sources compiled:       516
Test Java sources compiled:             175
Unit tests:                             518
PostgreSQL integration tests:           167
Total tests:                            685
Failures:                               0
Errors:                                 0
Flyway migrations validated/applied:     28
PostgreSQL:                            18.6
jOOQ:                                 3.21.5
Build result:                       SUCCESS
```

The historical `docs/development/implementation-status.md` remains useful provenance but is no longer the current quantitative baseline.

---

## 3. Repository Shape

Current production implementation contains substantial foundations across:

```text
application
audit
background
booking
businesshours
commercial
credential
customer
fulfilment
infrastructure
inventory
media
merchantaccount
money
notification
observability
ordering
privacy
protection
prototype
resilience
runtime
scheduling
semantic
surface
workforce
```

The repository is therefore not greenfield.

IMP-00 MUST preserve conforming implementation and identify only the missing/conflicting work required by final accepted authority.

The current production package map does not expose complete top-level production modules for several required later responsibilities, notably ordinary authentication/session establishment, Merchant Profile/Location, Publication/Enquiry, Order Fulfilment/Shipment, Returns, production API transport and recovery execution.

Package absence alone is not semantic proof; the classifications below also use migration, class and contract-level evidence.

---

## 4. Persistence / Migration Evidence

The current migration chain contains 28 migrations. Recent migrations demonstrate real implementation investment in:

```text
V20  authoritative Booking persistence
V21  durable background work
V22  Payment authority
V23  semantic compatibility/evidence
V24  Ordering/Inventory quantity claim
V25  Ordering commitment
V26  Inventory movement
V27  Booking authoritative execution contracts
V28  Appointment authoritative execution contracts
```

Consequently, several later macro targets are materially implemented but remain partial relative to final post-design production contracts.

---

## 5. Confirmed Reusable Foundations

### Semantic / runtime

Current semantic and runtime code already includes registry/compiler/runtime/release foundations and must be reused where conforming.

### Engineering / persistence

Current CI compiles and tests against PostgreSQL; Flyway migration validation/application is exercised. Governance/conformance tests already exist.

### Background execution

Current background implementation includes `DurableWorkInstruction`, `WorkAttempt`, durable storage and execution-result concepts. These are reusable foundations for IMP-08C.

### Booking / Scheduling

Booking and Appointment have substantial authoritative persistence/execution implementation, including V20, V27 and V28.

### Ordering / Inventory

Current code/migrations include Inventory claims, Order commitment and Inventory movement foundations through V24–V26.

### Payment

Current Money/Payment implementation includes Payment Obligation, PaymentApplication and provider-payment evidence foundations.

### Notification

Current Notifications include Notification Intent/Dispatch/DeliveryAttempt foundations. `DeliveryAttempt` already separates attempt identity, Dispatch identity and a provider idempotency reference.

### Workforce / Media / Privacy / Observability / Credentials

Meaningful foundations exist and should be preserved, but final accepted post-Target-17/19/20 production contracts are not yet proven complete.

---

## 6. Confirmed Conflicts With Final Accepted Authority

### 6.1 Booking event acknowledgement

Current `BookingOutbox` acknowledges delivery at the event level using:

```text
acknowledgeDelivery(MerchantScope, eventIdentifier)
```

Final MS-PROT-026 v1.1 requires independent registered Event Reactions and per-reaction progression/acknowledgement.

Therefore:

```text
one global event acknowledgement
    ≠ accepted production reaction model
```

This is a required IMP-08C correction. Existing event/outbox foundations remain reusable.

### 6.2 Notification provider idempotency identity

Current `NotificationDeliveryCoordinator` uses Dispatch identity as the provider idempotency reference.

Final MS-PROT-075 v1.1 explicitly requires:

```text
Dispatch identity
    ≠ provider-effect / idempotency identity
```

and requires attempt-scoped physical-effect identity.

Therefore the current coordinator path conflicts with accepted production authority and must be corrected under IMP-10.

The existing Notification Intent/Dispatch/DeliveryAttempt model is otherwise reusable evidence and must not be discarded wholesale.

---

## 7. Final-Authority Production Contract Gaps Confirmed During IMP-00

The current repository has no sufficient evidence of complete production implementations for the following final contracts/responsibilities:

```text
EventReactionContract
BackgroundWorkContract registration layer
ReconciliationContract
OperationalEvidenceContract
production ApiCommandContract / ApiQueryContract / callback contract infrastructure
ordinary Spring Web production controller surface
production SessionRecord / full ADR-014 authentication-session path
complete ProviderConnection production path
complete PaymentExecutionRequest / RefundExecutionRequest provider execution path
Order Fulfilment / Shipment business vertical
Returns capability production vertical
RecoveryCandidate / restore-promotion execution
```

Some underlying primitives already exist; absence here means the final accepted production contract is not yet proven complete.

---

## 8. Macro Classification

Classification vocabulary:

```text
CONFORMING_COMPLETE
    accepted authority + implementation + tests + conformance
    prove the macro target already satisfied

PARTIALLY_CONFORMING
    meaningful conforming foundations exist but required final
    target scope is incomplete

ABSENT
    no sufficient implementation of the macro responsibility exists

CONFLICTING
    current implementation contains a material path that contradicts
    final accepted authority, even if other reusable foundations exist
```

| Macro target | IMP-00 classification | Principal evidence / remaining gap |
|---|---|---|
| IMP-00 Current Implementation Baseline | **CONFORMING_COMPLETE** | This baseline closes the audit and identifies the first child graph. |
| IMP-01 Engineering & Conformance Foundation | **PARTIALLY_CONFORMING** | Reproducible CI, PostgreSQL integration, Flyway and governance tests exist; complete architecture/module-conformance and standardised test infrastructure require audit/completion. |
| IMP-02 Persistence & Semantic Release Foundation | **PARTIALLY_CONFORMING** | Strong PostgreSQL/Flyway/persistence/semantic compatibility foundations; exact final packaged semantic-definition materialisation/bootstrap is not yet proven complete. |
| IMP-03 Semantic Execution Spine | **PARTIALLY_CONFORMING** | Registry/compiler/runtime/release foundations are substantial; full final production execution spine and exact authority conformance require proof/completion. |
| IMP-04 Merchant Account, Scope, Identity & Trust | **PARTIALLY_CONFORMING** | MerchantAccount, MerchantScope, trusted-principal, credential and Audit foundations exist; complete ADR-014 WebAuthn/session/SessionRecord implementation is not established. |
| IMP-05 Merchant Definition, Configuration & Activation | **PARTIALLY_CONFORMING** | Configuration/activation foundations exist; final Merchant Profile/Location/Onboarding production path is incomplete. |
| IMP-06 Read, Exposure & Transport Spine | **PARTIALLY_CONFORMING** | Surface/protection/read foundations exist; final production API-contract/transport spine is incomplete. |
| IMP-07 Publication → Enquiry vertical slice | **ABSENT** | No complete production Merchant Profile → Publication → public observation → Enquiry → merchant observation path exists. |
| IMP-08A Merchant Assistance Foundation | **PARTIALLY_CONFORMING** | Media/privacy/AI-related foundations exist; final accepted assistance responsibility portfolio and integration are not proven complete. |
| IMP-08B Workforce Foundation | **PARTIALLY_CONFORMING** | Workforce/device-context foundations exist; final production identity/session integration remains dependent on IMP-04. |
| IMP-08C Durable Execution Foundation | **CONFLICTING** | DurableWorkInstruction/WorkAttempt foundations exist, but final reaction/background/reconciliation contracts are incomplete and BookingOutbox global acknowledgement conflicts with MS-PROT-026 v1.1. |
| IMP-09 Provider Foundation | **PARTIALLY_CONFORMING** | Fulfilment binding/routing/credential foundations exist; complete ProviderConnection/current Provider Readiness production contract is not established. |
| IMP-10 Notification Provider Vertical Slice | **CONFLICTING** | Strong Intent/Dispatch/DeliveryAttempt foundations; current Dispatch-as-provider-idempotency behaviour conflicts with MS-PROT-075 v1.1. |
| IMP-11 Scheduling Vertical Slice | **PARTIALLY_CONFORMING** | Substantial Booking/Scheduling/Appointment implementation and V20/V27/V28; final v1.6 production evidence/customer-surface contract not yet proven complete. |
| IMP-12 Ordering & Inventory Vertical Slice | **PARTIALLY_CONFORMING** | Strong Inventory Claim/Movement and Order Commitment foundations through V24–V26; final complete vertical conformance remains to prove. |
| IMP-13 Payment | **PARTIALLY_CONFORMING** | Payment Obligation/Application/provider evidence foundations exist; final execution/refund/provider/reconciliation path is incomplete. |
| IMP-14 Order Fulfilment & Shipment | **ABSENT** | Existing `fulfilment` package is Provider Fulfilment infrastructure; no sufficient Order Fulfilment/Shipment business vertical exists. |
| IMP-15 Returns | **ABSENT** | No sufficient production Returns capability vertical exists. |
| IMP-16 Cross-System Lifecycle & Historical Hardening | **PARTIALLY_CONFORMING** | Privacy/resilience/historical foundations exist; final DataLifecycle evaluation/owner-safe disposition and full cross-system hardening remain incomplete. |
| IMP-17 Complete Production API Portfolio | **ABSENT** | No sufficient final production API transport portfolio exists. |
| IMP-18 Production Operability | **PARTIALLY_CONFORMING** | Observability/resilience/background/infrastructure foundations exist; final operational-evidence contracts, production workers/adapters/alerts/reconciliation tooling are incomplete. |
| IMP-19 Backup, Restore & DR | **ABSENT** | Accepted TAS exists but production recovery execution is not implemented. |
| IMP-20 Controlled Live-Readiness | **ABSENT** | No controlled live-readiness evidence over completed IMP-00..IMP-19 exists. |

No macro target after IMP-00 is credited as `CONFORMING_COMPLETE` by package presence alone.

---

## 9. IMP-01 Fine-Grained Initial Graph

IMP-00 completion makes **IMP-01 — Engineering & Conformance Foundation** the next READY macro target.

Initial child graph:

```text
IMP-01.1 — Reproducible build / full-suite baseline
    classification: CONFORMING_COMPLETE
        │
        ├──► IMP-01.2 — PostgreSQL integration-test harness
        │       classification: CONFORMING_COMPLETE
        │
        ├──► IMP-01.3 — Flyway migration validation/application harness
        │       classification: CONFORMING_COMPLETE
        │
        ├──► IMP-01.4 — Architecture / module dependency conformance
        │       classification: PARTIALLY_CONFORMING
        │
        ├──► IMP-01.5 — Deterministic clock / time-test infrastructure
        │       classification: PARTIALLY_CONFORMING pending targeted audit
        │
        ├──► IMP-01.6 — Authority-to-test implementation traceability
        │       classification: PARTIALLY_CONFORMING
        │
        └──► IMP-01.7 — Test fixture / anti-flake conformance
                classification: PARTIALLY_CONFORMING pending targeted audit
```

The smallest high-leverage unresolved child is initially:

```text
IMP-01.4 — Architecture / module dependency conformance
```

because accepted module boundaries must be mechanically protected before significant new production code is added.

This ordering may be refined automatically under Implementation Rules if targeted repository inspection proves another IMP-01 child is smaller or already complete.

---

## 10. Macro Readiness After IMP-00

```text
COMPLETE
    IMP-00

READY
    IMP-01

BLOCKED_DEPENDENCY / PENDING
    IMP-02..IMP-20
```

No downstream target is unlocked merely because substantial prototype code exists.

Existing downstream code remains reusable evidence and may later satisfy child prerequisites once its exact authority/test/conformance proof is established.

---

## 11. Known Correction Queue Preserved for Later Eligible Targets

The following conflicts are recorded now but MUST NOT be repaired ahead of their macro eligibility:

```text
IMP-08C
    BookingOutbox event-global delivery acknowledgement
    → replace/compose with accepted per-EventReactionContract progression

IMP-10
    NotificationDeliveryCoordinator Dispatch-as-provider-idempotency
    → preserve Dispatch/Attempt distinction and use accepted attempt/effect identity
```

Recording a conflict does not make the downstream macro READY.

---

## 12. IMP-00 Completion Decision

IMP-00 completion criteria are satisfied:

```text
current repository known                         PASS
current tests/build known                       PASS
authority coverage mapped                       PASS
material conflicts identified                   PASS
already-satisfied prerequisites identified      PASS
initial fine-grained next graph derived         PASS
next READY macro determinable                   PASS
```

**IMP-00: COMPLETE**

**Next READY macro:** `IMP-01 — Engineering & Conformance Foundation`

The Implementation Rules may now continue automatically into the smallest READY IMP-01 child node unless a MANUAL_APPROVAL or DESIGN_ESCALATION condition is encountered.