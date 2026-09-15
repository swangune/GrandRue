# Deferred Decision Audit — 25 August 2026

> **Status:** Development evidence only — not semantic/design authority  
> **Branch audited:** `development`  
> **Audited head before this note:** `f0a8378fe2af13873f77f71fd869ebb936c45e4a`  
> **Canonical decision authority:** `designs/DEFERRED-DECISION-REGISTER.md`  
> **Authority navigation:** `designs/AUTHORITY-INDEX.md`

## Purpose

This note records the result of a repository review of the current deferred-decision state on `development`. It does not create, reopen, promote, close or redefine any design decision. If this note ever conflicts with the canonical Deferred Decision Register or an accepted authority, the canonical governance/authority documents govern.

## Summary finding

At the audited `development` head:

```text
active unresolved DDR decisions             0
active promoted design-review gaps          0
original DDR decisions resolved             9 / 9
explicit retained future-scope examples    54
current governed design blocker             none identified
```

The canonical `DEFERRED-DECISION-REGISTER.md` is version 3.1 and states that it is the sole current Deferred Decision Register. All original decisions `DDR-OD-001` through `DDR-OD-009` are marked **RESOLVED**. The promoted design-review queue contains no item awaiting governed review.

## Resolved original DDR decisions

| Decision | Status | Resolving authority / scope |
|---|---|---|
| DDR-OD-001 | RESOLVED | Composite MS-PROT-053 authority — Data Protection, Retention, Durable Evidence and Purpose-Bound Personal Media Use |
| DDR-OD-002 | RESOLVED | MS-PROT-054 — Semantic Release, Compatibility & Migration Model |
| DDR-OD-003 | RESOLVED | MS-PROT-055 — Money, Commercial Terms, Payment Obligations & Payment Evidence Model |
| DDR-OD-004 | RESOLVED | Composite MS-PROT-056 authority — subscription plans, trial, Merchant Commercial Agreement, entitlement binding and commercial-access boundary |
| DDR-OD-005 | RESOLVED | MS-PROT-050 v1.3 plus surviving earlier MS-PROT-050 authority — time/operating-hours resolution scope |
| DDR-OD-006 | RESOLVED | MS-PROT-042 v1.3 plus surviving Booking/Appointment authority — merchant-set policy boundary |
| DDR-OD-007 | RESOLVED | MS-PROT-043 v1.3 plus surviving CustomerContext/Enquiry authority |
| DDR-OD-008 | RESOLVED | MS-PROT-027 v1.2 plus surviving Projection/Exposure authority |
| DDR-OD-009 | RESOLVED | MS-PROT-044 v1.1 plus MS-PROT-058 — Product/Offering/Inventory/variant boundary and Inventory authority |

No original DDR row is open.

## Promoted design-review queue

Every currently listed promoted item is marked **RESOLVED**, including:

- Configuration Revision, Compilation & Activation;
- Initial Full-Experience Trial Establishment;
- Commercial Agreement & Entitlement Binding;
- Cross-Capability Application Orchestration & Consistency;
- Resource Fairness, Rate Limiting & Abuse Protection;
- Staff/team identity, delegated authority and lifecycle;
- Notification intent, recipient/channel selection and delivery evidence;
- Merchant/controller transfer, suspension and terminal account lifecycle;
- nursery/daycare/Montessori media-heavy domain stress test;
- production backup, restore, corruption recovery and disaster recovery.

The canonical register explicitly states that no promoted architecture/design gap is currently awaiting governed review.

## Retained future/deferred scope

The register retains narrower future scope beneath already-resolved parent decisions. These items are **not active design tasks** unless deliberately promoted through the governed lifecycle.

### MS-PROT-053 — Data Protection

1. jurisdiction-specific lawful-basis/legal-policy mapping;
2. exact guardian/representative evidence mechanisms where a future capability requires them;
3. subject-access/privacy administration UI;
4. future private parent/child or other restricted-media relationship semantics.

### MS-PROT-056 — Commercial / Entitlement

1. multi-location / medium-scale pricing details;
2. scale thresholds;
3. commercial usage/capacity metering;
4. negotiated large-merchant terms;
5. cross-Merchant-Account repeat-trial eligibility / abuse prevention;
6. exact payment grace / delinquency policy;
7. capability-specific classification of ambiguous residual operations.

### MS-PROT-060 — Physical Order Fulfilment

1. reverse fulfilment / returns workflow beyond the accepted boundary;
2. live carrier-rate quoting;
3. package-level logistics;
4. warehouse management;
5. route optimisation;
6. advanced shipment consolidation;
7. customs/import management.

### MS-PROT-061 — Returns / Courier Labels

1. customer self-service return processing;
2. automated return approval;
3. customer-triggered return-label generation.

### MS-PROT-066 — Media

1. exact photographic codecs/quality factors/responsive dimensions;
2. exact long-form `INFORMATION_VIDEO` duration/upload-size limits;
3. adaptive-streaming/CDN/transcoder implementation;
4. any future explicitly authorised media-editing capability.

### MS-PROT-073 — Resource Protection

1. commercial/contractual differentiated service guarantees remain outside Platform Resource Protection;
2. cross-account commercial trial eligibility remains outside Platform Resource Protection;
3. permanent account/security consequences remain owned by their applicable authorities.

### MS-PROT-074 — Workforce

1. exact personal authentication technology and PIN policy;
2. default merchant Role templates;
3. nested Groups / explicit DENY precedence;
4. delegated operational-device enrolment by non-controllers;
5. durable offline staff operational-data architecture;
6. staff scheduling / rota / payroll / HR semantics;
7. cross-merchant workforce administration.

### MS-PROT-075 — Notifications

1. specific email/SMS/push providers and provider-status mappings;
2. exact retry/backoff policy;
3. template/rendering technology;
4. exact notification preference UI/defaults;
5. marketing-consent law/policy details;
6. exact reminder timings;
7. batching/digest and quiet-hours policy;
8. notification analytics/open-click tracking policy.

### MS-PROT-076 — Merchant Account Lifecycle

1. future co-controller / quorum-governance model if demonstrated by merchant scale;
2. exact exceptional Merchant Control Recovery evidence and provider/mechanism;
3. detailed Merchant Account Suspension reason/policy catalogue;
4. exact catalogue of post-closure residual/remediation operations;
5. future terminal-account reopening/migration semantics if ever required.

### MS-TAS-RECOVERY-001 — Production Recovery

1. concrete cloud provider and managed PostgreSQL product;
2. concrete object-storage / secret/KMS / backup products;
3. infrastructure-as-code and monitoring/alerting product selection;
4. exact runbook automation and operator tooling;
5. future customer-facing SLA/SLO adoption or revised recovery objectives;
6. any future active-active / distributed-authority regional architecture.

Total explicit retained examples in the current register: **54**.

## Important distinction: deferred decisions vs implementation frontier

The repository still has significant `PARTIAL` and `NOT YET IMPLEMENTED` areas. These are not automatically deferred semantic/design decisions.

Examples include:

- CUSTOMER-context surface eligibility;
- PUBLIC Exposure filtering;
- projection-availability filtering;
- concrete Provider Readiness and residual-obligation adapters;
- HTTP/API delivery surfaces;
- merchant dashboard and storefront web applications;
- complete onboarding/inference flows;
- concrete provider integrations;
- complete Order Fulfilment / returns implementation;
- production cloud/IaC/DR implementation.

These remain implementation frontiers while accepted authority is sufficient. They become governed deferred/promoted decisions only if implementation or review discovers a consequential ambiguity not already answered by accepted authority.

## Cross-check findings

The current implementation-status and design-implementation conformance records are consistent with the DDR on the following previously consequential questions:

- Appointment Operational Object ownership is **RESOLVED AND ALIGNED** (`appointment / appointment`); GitHub issue #18 is closed.
- Merchant fulfilment-binding authority is **RESOLVED** by MS-PROT-048 v1.2, with applicability/obligation-subset refinement resolved by v1.3; GitHub issue #19 is closed.
- MS-PROT-049 contextual merchant surface eligibility and provider-readiness interaction availability are implementation foundations, not open DDR questions.

No contradictory current `OPEN_DESIGN` or promoted-review blocker was identified in the active implementation/conformance navigation reviewed for this audit.

## Governance consequence

Current work may proceed under accepted authority without a deferred-decision stop.

If a new consequential ambiguity is discovered:

```text
implementation/design evidence
        ↓
confirm accepted authority does not already answer it
        ↓
deliberately promote through DEFERRED-DECISION-REGISTER.md
        ↓
formal governed design review
        ↓
accepted authority
        ↓
implementation
```

A future-scope item must not be treated as active merely because it is listed, and an implementation gap must not be promoted merely because it is incomplete.
