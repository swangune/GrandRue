# Main Street Implementation Checkpoint — V23 Verified / V24 Staged

**Date:** 24 August 2026  
**Branch:** `development`  
**Purpose:** Executable evidence checkpoint and automation stop record. This document does not create semantic or architectural authority.

## 1. Latest verified implementation tree

```text
8fdccc7424cec1ed7227193fa96d3aa2e1605cc9
```

Local verification supplied on 24 August 2026:

```text
mvn --batch-mode clean verify -Ppostgres-it

production compile: 335 source files, Java release 25 PASS
test compile:       115 source files, Java release 25 PASS
Surefire:            411 / 411 PASS
PostgreSQL:          18.6
Flyway:              V1–V23 PASS
Failsafe:            128 / 128 PASS
BUILD SUCCESS
finished:             2026-08-24T15:59:54+01:00
```

The run performed a real forward migration from schema V20 through:

```text
V21 audit append-only evidence
V22 durable background work
V23 credential security metadata
```

## 2. Nodes closed by this executable evidence

### `IMP-BOOKING-LEGACY-001` — COMPLETE

Verified:

- JDBC/H2 authoritative Booking persistence path removed;
- PostgreSQL + jOOQ + Spring transaction implementation is the authoritative Booking persistence path;
- H2 dependency removed from the build;
- `JooqBookingUnitOfWorkIT`: 8 / 8 PASS;
- duplicate command replay, allocation contention, transaction rollback, command-identity conflict, outbox recovery and authoritative fact integrity are executable on PostgreSQL.

### `IMP-AUDIT-001` — COMPLETE

Verified:

- append-oriented Audit evidence remains distinct from Domain Event/business authority;
- PLATFORM and MERCHANT execution scope remain explicit;
- audit identity is replay-idempotent and rejects conflicting evidence reuse;
- merchant query is tenant-isolated, half-open and bounded;
- concurrent duplicate delivery converges to one durable record;
- `JooqAuditStoreIT`: 5 / 5 PASS.

### `IMP-BGWORK-001` — COMPLETE

Verified:

- durable work is technical scheduling/progression evidence, not future mutation authority;
- due work claiming uses technical leases and PostgreSQL row locking;
- concurrent claimers do not receive the same work;
- expired claims may be recovered;
- `RETRY_SAFE` remains an attempt result and requires explicit rescheduling;
- terminal classifications require matching attributable attempt evidence;
- `NO_LONGER_APPLICABLE` is a valid terminal outcome rather than a retry failure;
- `JooqDurableWorkStoreIT`: 10 / 10 PASS.

### `IMP-CREDENTIAL-001` — COMPLETE

Verified:

- PostgreSQL stores non-secret credential binding/generation metadata only;
- raw credential values are not represented by the persistence contract;
- PLATFORM and MERCHANT credential scopes remain distinct;
- credential generation, binding identity and protected-material reference remain distinct;
- generation policy history is append-only and sequenced;
- rotation overlap may distinguish NEW_EXECUTION, EXISTING_OBLIGATION and VERIFICATION use;
- RETIRING cannot regain new-execution authority silently;
- EXPIRED, REVOKED and COMPROMISED are terminal no-use states;
- historical eligibility is evaluated at the requested execution instant;
- stale concurrent transitions cannot both commit;
- `JooqCredentialSecurityStoreIT`: 9 / 9 PASS.

## 3. Latest staged implementation tree

```text
c333e5930468c32487d6aa0bf53bbb3a318a03f3
```

Everything after the verified V23 tree remains **STAGED / AWAITING EXECUTABLE VERIFICATION**.

### `IMP-OBS-001` — STAGED

Authority: MS-PROT-068.

Staged:

- bounded diagnostic log observations;
- bounded metric observations without arbitrary payload maps;
- technical trace observations with business-correlation reference kept distinct from Trace identity;
- scope-qualified health observations with LIVENESS / READINESS / SERVICE_QUALITY dimensions;
- replaceable `OperationalTelemetrySink`;
- `BestEffortOperationalTelemetry` isolation so ordinary telemetry exporter failure does not fail business execution;
- unit falsifiers for telemetry failure independence, health qualification, trace/correlation separation and finite metric values.

No telemetry backend, SDK, alerting product or health-aggregate authority has been selected.

### `IMP-RESILIENCE-001` — STAGED

Authority: MS-PROT-069 + MS-PROT-070.

Staged:

- acceptance certainty, execution certainty and acknowledgement certainty as independent dimensions;
- logical command certainty distinct from transport attempts;
- explicit owning-contract `RetrySafety` evidence;
- retry gate preserving:
  - KNOWN_EXECUTED → no retry;
  - EXECUTION_PENDING → wait;
  - KNOWN_NOT_EXECUTED → retry only when independently SAFE;
  - EXECUTION_UNCERTAIN → reconciliation unless retry safety is independently SAFE;
- no retry library, timeout policy, circuit-breaker library or provider fallback router selected.

### `IMP-ORCH-001` — STAGED

Authority: MS-PROT-072.

Staged:

- stable logical `ApplicationRequestIdentity` distinct from trace/transport/business-object identity;
- accepted application outcome taxonomy:
  `COMPLETED`, `ACCEPTED_PENDING`, `REJECTED`, `CONFLICT`, `EXECUTION_UNCERTAIN`, `MANUAL_INTERVENTION_REQUIRED`;
- non-authoritative committed-progress references to capability-owned facts;
- enforcement that REJECTED/CONFLICT cannot erase known committed progress;
- `ACCEPTED_PENDING` requires both committed progress and an explicit pending responsibility;
- no universal Merchant Saga, setup aggregate or generic persistent workflow table introduced.

Existing post-commit/outbox and durable-background-work infrastructure remains the mechanism for concrete use-case coordination where accepted semantics require it.

### `IMP-MONEY-001` — STAGED

Authority: MS-PROT-055.

Migration:

```text
V24__money__create_payment_authority.sql
```

Staged provider-neutral facts:

```text
PaymentObligation
ProviderPaymentEvidence
PaymentApplication
```

Staged persistence:

```text
JooqPaymentAuthorityStore
```

The implementation preserves:

```text
Payment Obligation
    ≠ Provider Payment Evidence
    ≠ Payment Application
    ≠ current Amount Due
```

Current Amount Due is derived from immutable obligation magnitude minus accepted applications; provider evidence never rewrites the obligation.

PostgreSQL falsifiers staged for:

- durable obligation/evidence persistence;
- partial payment;
- provider overpayment evidence not increasing the obligation;
- application not exceeding provider evidence;
- exact currency matching;
- idempotent identity with conflicting-intent rejection;
- Merchant Scope isolation;
- concurrent evidence applications not over-discharging one obligation;
- concurrent reuse of one evidence item not over-applying provider amount.

No Stripe, PayPal, Klarna or other provider semantics are embedded.

## 4. Automation stop — semantic architecture decision required

Automation stops before connecting Provider Payment Evidence to paid Commercial Agreement activation.

Current accepted/implemented separation is intentional:

```text
Merchant acceptance provenance
    ≠ Provider Payment Evidence

Payment Obligation
    ≠ Provider transaction/evidence

Merchant Commercial Agreement
    ≠ Payment Evidence
```

`MerchantCommercialAgreementTransition` currently contains:

```text
logical request identity
expected current Agreement identity
candidate MerchantCommercialAgreement
```

and `MerchantCommercialAgreement` explicitly states that it is not payment evidence.

The next concrete paid-plan orchestration therefore requires an authoritative decision on at least:

1. which interpreted payment-evidence result is sufficient to progress a paid Commercial Agreement;
2. whether first paid Agreement establishment requires confirmed payment, durable payment acceptance, or another accepted commercial/payment condition;
3. how payment pending / execution uncertain / reconciliation-required evidence maps to application progression without becoming Commercial Agreement state;
4. whether plan changes use the same payment prerequisite as first paid activation;
5. the exact trusted provider-interpretation boundary that converts provider vocabulary into Main Street payment facts suitable for Commercial use.

MS-PROT-055 explicitly prohibits copying provider status directly into Main Street payment semantics. Guessing this bridge would change authoritative Commercial-transition preconditions and therefore requires governed semantic/architectural approval rather than an implementation-only fix.

## 5. Next executable verification

Before any staged node may be marked COMPLETE, run the full PostgreSQL profile on the latest staged tree:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Expected forward migration from a V23 database includes V24.
