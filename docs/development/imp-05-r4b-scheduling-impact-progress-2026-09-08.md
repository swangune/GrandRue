# IMP-05-R4B Scheduling Impact Progress — 2026-09-08

**Programme node:** `IMP-05-R4B — Concrete business-effect and commitment assessments`  
**State:** `IN_PROGRESS`  
**Branch:** `development`

## Scope

This bounded R4B increment adds a Scheduling-owned configuration-impact assessment for whether **new appointment scheduling activity** can use Main Street Scheduling under the candidate configuration.

It does not evaluate current Appointment schedulability, calculate live availability, infer business-open state, or create, mutate, cancel, abandon, or reinterpret existing Appointment, Booking, or Time Proposal truth.

## Accepted authority

The slice is derived only from accepted authority:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, especially §§20–28 on impact analysis, existing-commitment protection, capability deactivation, and residual management of outstanding obligations;
- `designs/MS-PROT-042 — Scheduling, Appointment & Time-Commitment Model.md` and its accepted amendments through v1.8, which keep Appointment Scheduling Evaluation a Scheduling-owned current/runtime determination and preserve provider, Workforce-availability, and schedulable-Resource ownership boundaries;
- `designs/MS-PROT-050 — Merchant Operating-Time & Business-Hours Model.md` and its accepted amendments, which distinguish Public Business Hours, Scheduling Operating Constraints, staff/resource availability, one-off schedule unavailability, and Enquiry;
- `designs/IMPLEMENTATION-RULES.md`, `designs/MS-IMP-001.md`, and `AGENTS.md` for test-first and programme-state discipline.

## Implemented artefacts

Production:

- `src/main/java/mainstreet/scheduling/SchedulingAvailabilityImpactAssessment.java`

Tests:

- `src/test/java/mainstreet/scheduling/SchedulingAvailabilityImpactAssessmentTest.java`

The production class implements the existing `ConfigurationImpactAssessment` port. It determines Scheduling membership from the **compiled capability dependency closure** of each pinned semantic release rather than from direct selected identifiers.

## Business-facing semantics

When Scheduling becomes available to the candidate operational model:

> New appointment scheduling activity can use Main Street Scheduling where applicable scheduling semantics and authority permit it.

When Scheduling ceases to be available to the candidate operational model:

> New appointment scheduling activity will no longer be initiated through Main Street Scheduling.

Both changes are classified `CONSEQUENTIAL` because they alter the operational path available for applicable new activity.

Unchanged Scheduling membership contributes no business-facing effect and no impact finding.

## Semantic ownership preserved

This increment deliberately preserves the following distinctions:

```text
Scheduling capability membership
        ≠
Appointment Scheduling Evaluation
        ≠
current Appointment availability / live slots
        ≠
Public Business Hours / Scheduling Operating Constraints
        ≠
Workforce availability
        ≠
schedulable Resource availability / capacity
        ≠
provider readiness
        ≠
Enquiry availability
        ≠
Appointment / Booking / Time Proposal commitment truth
```

The assessment reports only whether Main Street Scheduling is available for **new applicable appointment scheduling activity** after configuration activation.

## Negative boundaries proved

The slice does **not** infer or perform any of the following:

- enabling Scheduling does not emit `SCHEDULABLE`, `NOT_SCHEDULABLE`, or `UNRESOLVED`;
- enabling Scheduling does not infer current Appointment availability or live appointment slots;
- enabling Scheduling does not infer that the business is open or closed;
- enabling Scheduling does not calculate effective Scheduling Operating Constraints or runtime scheduling intervals;
- enabling Scheduling does not infer Workforce or staff availability;
- enabling Scheduling does not infer schedulable Resource identity, linkage, availability, or capacity;
- enabling Scheduling does not invoke a provider or infer provider readiness;
- enabling Scheduling does not infer breaks, one-off unavailability, existing conflicts, or other current availability evidence;
- enabling or disabling Scheduling does not infer Enquiry availability;
- enabling Scheduling does not create an Appointment, Booking, or Time Proposal;
- disabling Scheduling does not cancel, delete, abandon, or reinterpret an existing Appointment, Booking, Time Proposal, or other time-commitment truth;
- unchanged Scheduling membership produces no invented impact;
- unrelated capability changes do not activate Scheduling semantics;
- a stale selected-identifier comparison cannot override release-specific dependency closure;
- an unavailable historical semantic release fails closed rather than being treated as an inactive base.

These boundaries follow MS-PROT-040's non-retroactivity rule and the Scheduling ownership/runtime distinctions established by MS-PROT-042 and MS-PROT-050.

## Focused test evidence

`SchedulingAvailabilityImpactAssessmentTest` proves six cases:

1. initial Scheduling enablement reports the bounded new appointment-scheduling activity effect;
2. Scheduling deactivation reports only the bounded new-activity effect and does not claim cancellation or abandonment of existing Scheduling or commitment truth;
3. unchanged enabled membership reports no effect;
4. unrelated capabilities report no Scheduling effect;
5. historical and candidate pinned semantic releases are independently compiled so dependency-closure changes are detected even when direct selections are identical;
6. a missing historical semantic release fails closed.

## Test-first evidence

### RED

Commit:

- `c4bcbcd29b9f0a3ae853327577db974ba6f59391` — `test(imp-05): specify scheduling impact semantics`

GitHub Actions:

- Maven Tests `#1724`
- run `34184206816`
- command: `mvn --batch-mode clean verify -Ppostgres-it`
- result: **FAILED as intended**
- failure mode: test compilation failed only because `SchedulingAvailabilityImpactAssessment` did not yet exist (`cannot find symbol`), establishing a clean RED proof for the missing production contract.

### GREEN

Commit:

- `98b0173ec57a2a33dee2a44a48738117409b9a0b` — `feat(imp-05): add scheduling impact assessment`

GitHub Actions:

- Maven Tests `#1725`
- run `34184283311`
- command: `mvn --batch-mode clean verify -Ppostgres-it`
- Java 25 / PostgreSQL integration gate
- result: **BUILD SUCCESS**

## Programme consequence

This evidence strengthens `IMP-05-R4B`, but does **not** complete it.

`IMP-05-R4B` remains `IN_PROGRESS` because concrete production policy-owner interpretations, remaining owner-backed commitment conflicts, catalogue/resource-owned effects, and any other uncovered semantic-owner effects still require production coverage. `IMP-05-R4C` therefore remains blocked on R4B, and no downstream programme node is unlocked by this bounded Scheduling increment alone.
