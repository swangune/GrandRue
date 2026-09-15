# IMP-05-R4B Payment Impact Progress — 2026-09-08

**Programme node:** `IMP-05-R4B — Concrete business-effect and commitment assessments`  
**State:** `IN_PROGRESS`  
**Branch:** `development`

## Scope

This bounded R4B increment adds a Payment-owned configuration-impact assessment for whether **new payment activity** can use Main Street Payment under the candidate configuration.

It does not model, create, mutate, reinterpret, execute, reconcile, refund, or otherwise act on existing payment obligations or provider execution state.

## Accepted authority

The slice is derived only from accepted authority:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, especially §§20–28 on impact analysis, existing-commitment protection, capability deactivation, and residual management of outstanding obligations;
- `designs/MS-PROT-055 — Money, Commercial Terms, Payment Obligations & Payment Evidence Model.md`, especially §§18–26 on committed commercial truth, Payment Obligation ownership, and provider-payment boundaries;
- `designs/MS-PROT-055 v1.1 — Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment.md`, especially §§1–5 and §§14–25 on Payment ownership, immutable obligation truth, provider execution, reconciliation, payment application, and refund boundaries;
- `designs/IMPLEMENTATION-RULES.md`, `designs/MS-IMP-001.md`, and `AGENTS.md` for test-first and programme-state discipline.

## Implemented artefacts

Production:

- `src/main/java/mainstreet/money/PaymentAvailabilityImpactAssessment.java`

Tests:

- `src/test/java/mainstreet/money/PaymentAvailabilityImpactAssessmentTest.java`

The production class implements the existing `ConfigurationImpactAssessment` port. It determines Payment membership from the **compiled capability dependency closure** of each pinned semantic release rather than from direct selected identifiers.

## Business-facing semantics

When Payment becomes available to the candidate operational model:

> New payment activity can use Main Street Payment where applicable payment semantics and authority permit it.

When Payment ceases to be available to the candidate operational model:

> New payment activity will no longer be initiated through Main Street Payment.

Both changes are classified `CONSEQUENTIAL` because they alter the operational path available for applicable new activity.

Unchanged Payment membership contributes no business-facing effect and no impact finding.

## Semantic ownership preserved

This increment deliberately preserves the following distinctions:

```text
Payment capability membership
        ≠
Payment Obligation
        ≠
Payment Execution Request / provider execution
        ≠
ProviderPaymentEvidence
        ≠
PaymentApplication
        ≠
Refund execution / Refund
        ≠
source Order / Booking / Appointment commitment
```

The assessment reports only whether Main Street Payment is available for **new applicable activity** after configuration activation.

## Negative boundaries proved

The slice does **not** infer or perform any of the following:

- enabling Payment does not create a `PaymentObligation`;
- enabling Payment does not invoke a provider or infer provider readiness;
- enabling Payment does not manufacture provider evidence or a `PaymentApplication`;
- enabling Payment does not infer amount due, current payment state, settlement, reconciliation, refund eligibility, or refund execution;
- disabling Payment does not cancel, delete, adjust, abandon, or reinterpret an existing `PaymentObligation`;
- disabling Payment does not erase provider evidence, payment applications, reconciliation truth, refunds, or already-attempted execution state;
- disabling Payment does not rewrite the source Order, Booking, Appointment, or other source commitment;
- unchanged Payment membership produces no invented impact;
- unrelated capability changes do not activate Payment semantics;
- a stale selected-identifier comparison cannot override release-specific dependency closure;
- an unavailable historical semantic release fails closed rather than being treated as an inactive base.

These boundaries follow MS-PROT-040's non-retroactivity rule and MS-PROT-055's Payment ownership model.

## Focused test evidence

`PaymentAvailabilityImpactAssessmentTest` proves six cases:

1. initial Payment enablement reports the bounded new-activity effect;
2. Payment deactivation reports only the bounded new-activity effect and does not claim abandonment of existing Payment truth;
3. unchanged enabled membership reports no effect;
4. unrelated capabilities report no Payment effect;
5. historical and candidate pinned semantic releases are independently compiled so dependency-closure changes are detected even when direct selections are identical;
6. a missing historical semantic release fails closed.

## Test-first evidence

### RED

Commit:

- `2d9789c5398e3e2a3e7ebde866a2a572861dcc26` — `test(imp-05): specify payment impact semantics`

GitHub Actions:

- Maven Tests `#1718`
- run `34180462382`
- command: `mvn --batch-mode clean verify -Ppostgres-it`
- result: **FAILED as intended**
- failure mode: test compilation failed only because `PaymentAvailabilityImpactAssessment` did not yet exist (`cannot find symbol`), establishing a clean RED proof for the missing production contract.

### GREEN

Commit:

- `b78918312c86627017a9b5408cbac7163a4f64f1` — `feat(imp-05): add payment impact assessment`

GitHub Actions:

- Maven Tests `#1719`
- run `34180562230`
- command: `mvn --batch-mode clean verify -Ppostgres-it`
- Java 25 / PostgreSQL integration gate
- result: **BUILD SUCCESS**
- tests: **783**
- failures: **0**
- errors: **0**
- skipped: **0**

## Programme consequence

This evidence strengthens `IMP-05-R4B`, but does **not** complete it.

`IMP-05-R4B` remains `IN_PROGRESS` because broader cross-domain impact/commitment families, owner-model completion, richer preview, and residual what-if modelling remain outstanding. No downstream programme node is unlocked by this bounded Payment increment alone.
