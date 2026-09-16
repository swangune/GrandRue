# MS-PROT-042 v1.6 — Production Scheduling Evidence, Appointment Admission & Customer Surface Requirement Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.6  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Approved:** Manual approval on 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** Composite MS-PROT-042 v1.2–v1.5 only within Scheduling constraint-evidence evaluation, Appointment commitment admission and Booking/Appointment customer relationship-requirement scope  
**Depends on:** MS-PROT-023; MS-PROT-025; MS-PROT-027 v1.2–v1.5; MS-PROT-040; MS-PROT-041 v1.1; MS-PROT-042 v1.2–v1.5; MS-PROT-043; MS-PROT-048 v1.4; MS-PROT-049 v1.0–v1.3; MS-PROT-050 v1.3; MS-PROT-054; MS-PROT-056; MS-PROT-059; MS-PROT-062; MS-PROT-063; MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-079  
**Resolves:** Booking/Appointment scope of `MS-PROT-027-V15-DQ-005`; Scheduling-specific scope of `MS-PROT-048-V14-DQ-014`  
**Narrows:** `MS-PROT-027-V14-DQ-013` by determining backend degraded/unresolved Calendar/Scheduling meaning while leaving presentation wording/layout downstream  
**Programme effect:** Makes MS-PROT-079 Target 11 eligible for final Design-Closure review; this authority does not itself authorise production implementation.  
**Purpose:** Complete the production Scheduling boundary by distinguishing known scheduling rejection from unresolved required constraint evidence, define the admission rule used by `appointment.confirm`, preserve Calendar/provider/read-model separation, and establish the Booking- and Appointment-owned customer relationship requirements required by existing Surface and Exposure authority.

---

## 1. Governing Decision

Main Street SHALL distinguish:

```text
candidate interval known to satisfy
all applicable Scheduling constraints

        ≠

candidate interval known to violate
an applicable Scheduling constraint

        ≠

candidate interval whose required
Scheduling evidence cannot currently
be established
```

Scheduling therefore owns a deterministic **Appointment Scheduling Evaluation** with exactly three result classes:

```text
SCHEDULABLE

NOT_SCHEDULABLE

UNRESOLVED
```

These are derived evaluation results.

They are not:

```text
Appointment lifecycle states
Calendar lifecycle states
provider states
merchant configuration values
Exposure verdicts
persistent availability truth
```

`appointment.confirm` MUST establish an Appointment only after the current authoritative Scheduling evaluation is `SCHEDULABLE` and every invariant-required capacity claim can be established.

`UNRESOLVED` MUST NOT be treated as `SCHEDULABLE`.

In addition, Booking and Appointment SHALL provide their own owner-qualified relationship requirements for CUSTOMER Surface/Exposure evaluation rather than leaving Surface, Exposure, authentication or frontend code to infer the relationship.

---

## 2. Problem and Amendment Scope

MS-PROT-042 v1.5 already requires Scheduling revalidation before `appointment.confirm`.

MS-PROT-041 v1.1 already permits merchant-authorised external busy-time evidence to constrain Appointment availability.

MS-PROT-027 already prohibits Calendar and stale projections from becoming Scheduling authority.

MS-PROT-048 already separates Provider Readiness from Main Street business truth.

The remaining ambiguity is that the accepted corpus does not completely determine what Scheduling must conclude when an otherwise applicable required constraint cannot currently be evaluated.

For example:

```text
merchant uses external personal-calendar
busy time as a Scheduling constraint

        ↓

provider connection exists

        ↓

busy-time evidence is stale,
missing or unverifiable

        ↓

14:00 ?
```

The implementation MUST NOT choose between:

```text
assume free

assume busy

permit with warning

fail closed

reuse arbitrary stale data
```

from engineering convention.

This amendment determines that semantic consequence.

It also closes the capability-owned relationship predicate required before Booking/Appointment CUSTOMER contributions and CUSTOMER Exposure requirements may rely on a related-customer relationship.

---

## 3. Explicit Non-Goals

This amendment does not define:

```text
a universal Availability capability

a persisted Slot aggregate

a universal Schedule aggregate

a universal Calendar status

one global provider-data TTL

Google-specific semantics

provider-specific freshness durations

external calendar implementation technology

Calendar UI wording/layout

HTTP status codes

Production API representations

customer authentication technology

secure guest-link/token encoding

CustomerAccount lifecycle

new Booking lifecycle states

motel check-in/check-out

room housekeeping

overbooking policy

staff rota/payroll

generic workflow/rules DSL

business-category scheduling logic
```

It does not make every Booking subject to Scheduling merely because a Booking has dates or appears on a Calendar.

---

## 4. Canonical Terminology

### 4.1 Appointment Scheduling Evaluation

An **Appointment Scheduling Evaluation** is:

> A Scheduling-owned, merchant-scoped, side-effect-free evaluation of whether one candidate Appointment interval may currently proceed toward commitment under all applicable Scheduling constraints whose authoritative evidence is required for that decision.

Conceptually:

```text
MerchantScope
+
scheduled-operation / Offering context
+
candidate interval
+
active release/configuration context
+
current applicable Scheduling constraints
+
current required constraint evidence
        ↓
Appointment Scheduling Evaluation
```

The evaluation has no durable business identity or independent lifecycle.

### 4.2 Applicable Scheduling Constraint

An **Applicable Scheduling Constraint** is an accepted Scheduling constraint whose registered applicability predicate is satisfied for the current:

```text
MerchantScope
scheduled operation
candidate interval
resource/context
active configuration
```

Constraint applicability MUST derive from accepted semantics and active configuration.

It MUST NOT be inferred from:

```text
merchant category
Calendar presentation
provider presence alone
frontend form
AI suggestion
```

### 4.3 Scheduling Constraint Evidence

**Scheduling Constraint Evidence** is the current authoritative or accepted provider-derived evidence required to evaluate one Applicable Scheduling Constraint.

Examples include owner-qualified evidence from:

```text
Business Hours
ScheduleIntent
existing Appointments
Resource/Allocation authority
accepted capacity facts
registered scheduling policy
accepted external busy-time integration
```

Possession of evidence does not transfer semantic ownership to Scheduling.

### 4.4 Evidence Sufficiency

**Evidence Sufficiency** answers:

> Is the evidence required for this particular Scheduling constraint sufficiently trustworthy, current and complete under its accepted owning/integration contract to evaluate the constraint now?

Evidence Sufficiency MUST be deterministic or explicitly delegated to an accepted owner/provider contract.

Main Street SHALL NOT introduce one global freshness duration.

### 4.5 Scheduling Evaluation Outcome

The three result classes are:

```text
SCHEDULABLE
NOT_SCHEDULABLE
UNRESOLVED
```

They classify one evaluation only.

They MUST NOT be persisted as independent authoritative slot truth.

---

## 5. Semantic Ownership

Ownership remains:

```text
SCHEDULING
    owns Appointment Scheduling Evaluation
    and composition of applicable scheduling constraints

APPOINTMENT
    owns Appointment commitment truth

BOOKING
    owns Booking reservation commitment truth

BUSINESS HOURS
    owns Business Hours truth

SCHEDULE INTENT
    remains Scheduling-owned operational intent

RESOURCE / ALLOCATION
    owns capacity and Allocation truth

CALENDAR
    owns calendar-shaped projection/serviceability

EXTERNAL PROVIDER
    owns its external event/evidence

PROVIDER INTEGRATION
    owns interpretation/correlation contract boundaries

SURFACE
    owns contribution composition

EXPOSURE
    owns EXPOSE/WITHHOLD evaluation

CUSTOMER CONTEXT
    owns its accepted merchant-scoped customer-context semantics
```

Reference, orchestration or shared transaction participation MUST NOT transfer those ownership boundaries.

---

## 6. Scheduling Constraint Applicability

Scheduling SHALL derive the Applicable Scheduling Constraint set from the exact active semantic/configuration context.

For a given candidate interval, applicable constraints MAY include accepted instances of:

```text
Business Hours / operating windows
recurring breaks
duration
buffers
ScheduleIntent
existing Appointment commitments
required Resource/capacity constraints
merchant Scheduling policy
merchant-enabled external busy-time constraints
```

A constraint that is not applicable MUST NOT block Scheduling merely because the platform supports that constraint type.

---

## 7. Deterministic Evaluation Algebra

For each Applicable Scheduling Constraint, Scheduling SHALL determine one of:

```text
SATISFIED

UNSATISFIED

UNRESOLVED
```

where:

```text
SATISFIED
    sufficient evidence exists
    and the constraint permits the interval

UNSATISFIED
    sufficient evidence exists
    and the constraint rejects the interval

UNRESOLVED
    the constraint is applicable
    but evidence sufficient to decide it
    cannot currently be established
```

The aggregate Appointment Scheduling Evaluation is:

```text
if ANY applicable constraint = UNSATISFIED
    → NOT_SCHEDULABLE

else if ANY applicable constraint = UNRESOLVED
    → UNRESOLVED

else
    → SCHEDULABLE
```

This ordering is normative.

A known authoritative rejection remains a valid rejection even if another independent constraint is unresolved.

---

## 8. `SCHEDULABLE`

`SCHEDULABLE` means:

> Every Applicable Scheduling Constraint required for the evaluation has sufficient current evidence and evaluates as satisfied.

It does not mean:

```text
Appointment already exists
capacity has been reserved
customer has authority
payment succeeded
provider sync succeeded
the displayed result cannot become stale
```

Therefore:

```text
SCHEDULABLE
    ≠ reservation
    ≠ guarantee of later commit
```

---

## 9. `NOT_SCHEDULABLE`

`NOT_SCHEDULABLE` means:

> At least one Applicable Scheduling Constraint has sufficient authoritative evidence establishing that the candidate interval is not currently permitted.

Examples include:

```text
outside applicable operating window
current ScheduleIntent blocks interval
current Appointment conflict
current authoritative capacity cannot support interval
registered Scheduling policy rejects interval
current external busy-time evidence proves conflict
```

This is a Scheduling business decision.

It is not a provider/technical failure.

---

## 10. `UNRESOLVED`

`UNRESOLVED` means:

> No currently established Applicable Scheduling Constraint has already proved the interval invalid, but one or more Applicable Scheduling Constraints cannot presently be evaluated from sufficient trustworthy evidence.

Examples include:

```text
required external busy-time evidence missing

required external busy-time evidence stale
under its governing evidence contract

required provider evidence unverifiable

required Resource/capacity source unavailable

required constraint evaluator unavailable
```

`UNRESOLVED` MUST NOT be interpreted as free capacity.

No new Appointment may be committed while final Scheduling revalidation remains `UNRESOLVED`.

---

## 11. External Busy-Time Constraint Boundary

Connecting an external calendar does not automatically make that calendar a required Scheduling dependency.

Canonical:

```text
external calendar connected
        ≠
external busy-time constraint applicable
```

The external source participates only when accepted merchant configuration/integration semantics make its busy-time evidence an Applicable Scheduling Constraint.

Where no such constraint applies:

```text
external provider outage
        ↓
does not block Appointment Scheduling
```

Where such a constraint does apply:

```text
sufficient evidence
    → evaluate constraint

insufficient/unverifiable evidence
    → UNRESOLVED
```

---

## 12. Provider Readiness and Evidence Sufficiency

Provider Readiness and Scheduling Constraint Evidence Sufficiency remain distinct.

Therefore:

```text
Provider Readiness = READY
        ≠
required busy-time evidence necessarily sufficient
```

and:

```text
Provider Readiness = DEGRADED
        ≠
automatic Scheduling rejection
```

and:

```text
provider currently unavailable
        ≠
automatic erasure of previously valid evidence
```

Scheduling SHALL rely on the accepted evidence-sufficiency contract applicable to the required provider-derived evidence.

If sufficiently current evidence can still be established under that contract, the constraint MAY still be evaluated.

If the required evidence cannot be established, the constraint result is `UNRESOLVED`.

No provider status may by itself fabricate:

```text
SCHEDULABLE
NOT_SCHEDULABLE
```

without the required Scheduling evidence.

---

## 13. No Universal Stale-Evidence Rule

Main Street SHALL NOT define:

```text
external calendar data < N minutes old
    → universally safe
```

within generic Scheduling semantics.

Provider/integration-specific evidence freshness remains governed by the accepted provider/integration contract.

Until such a contract can establish Evidence Sufficiency:

```text
unknown freshness
        ↓
UNRESOLVED
```

not:

```text
assume current
```

---

## 14. Business Hours Boundary

Business Hours may constrain Appointment Scheduling only where their accepted scope and Scheduling applicability require them.

Scheduling MUST preserve Business Hours scope, including:

```text
merchant scope
location scope
dated override
cross-midnight resolution
timezone
```

according to MS-PROT-050.

Public Business Hours MUST NOT be treated as a universal restriction on every internal or merchant-assisted operation merely because they are publicly displayed.

---

## 15. ScheduleIntent Boundary

A current applicable ScheduleIntent such as merchant unavailability is authoritative Scheduling constraint evidence.

Therefore:

```text
ScheduleIntent blocks interval
        ↓
NOT_SCHEDULABLE
```

A descriptive title on a Calendar/provider event is not equivalent to ScheduleIntent.

---

## 16. Resource and Allocation Boundary

Resource/capacity authority may provide constraint evidence to Scheduling.

Scheduling MAY determine that currently established capacity makes an interval schedulable.

That read does not reserve capacity.

Where Appointment commitment requires an Allocation Claim:

```text
Scheduling final evaluation
        ↓
required capacity claim
        +
Appointment commitment
```

MUST preserve the accepted atomic invariant.

A concurrent claim may therefore cause commitment to fail even after an earlier `SCHEDULABLE` read.

---

## 17. Initial Scheduling Availability Read Architecture

Initial production Scheduling availability SHALL use:

```text
current owner queries
+
current accepted provider evidence where required
+
request-scoped deterministic Scheduling evaluation
```

by default.

Main Street SHALL NOT initially require:

```text
persistent free-slot table
Redis slot cache
async Availability projection
generic Availability service/database
```

A separate persisted/cached/asynchronous Scheduling availability representation may be introduced only when MS-PROT-027 v1.3 applicability triggers justify it and its required Projection Contract has been accepted.

---

## 18. Calendar Projection Relationship

`calendar / merchant-calendar` remains the governing Calendar Projection Contract.

Calendar MAY represent:

```text
Appointments
Booking timing where legitimately relevant
ScheduleIntent
Business Hours
resource/capacity representation
accepted external busy constraints
```

Calendar MUST NOT become the authoritative source used to bypass Scheduling evaluation.

Canonical direction:

```text
authoritative sources
        ↓
Scheduling evaluation

authoritative sources
        ↓
Calendar projection
```

Rejected:

```text
Calendar looks free
        ↓
therefore appointment.confirm allowed
```

---

## 19. Calendar Degradation

Where required evidence is unavailable:

```text
Calendar
    may truthfully represent
    known commitments / known blocked time

Calendar
    MUST NOT claim current schedulability
    for an interval whose Scheduling result
    is UNRESOLVED
```

Exact presentation wording, icons, labels and UI behaviour remain downstream presentation scope.

This amendment determines backend meaning only.

---

## 20. `appointment.confirm` Operation Contract Amendment

`appointment.confirm` remains an Appointment-owned operation.

### Owner

```text
Appointment
```

### Actor/principal requirement

Current trusted execution context and applicable Actor Authorisation MUST be established under MS-PROT-062/MS-PROT-063 and applicable customer/merchant authority.

### Semantic Applicability

Appointment semantics and the selected scheduled operation MUST be applicable under the exact active RCP/release.

### Commercial Entitlement

Any applicable Commercial Entitlement is independently evaluated and MUST NOT substitute for Scheduling validity.

### Required inputs

At minimum the existing MS-PROT-042 v1.5 semantics remain:

```text
trusted MerchantScope
logical command identity
Appointment identity
CustomerContext identity
scheduled-operation / Offering context
candidate interval
governing configuration/release context
```

### Authoritative facts read

Where applicable:

```text
current Scheduling configuration
current Business Hours
current ScheduleIntent
current Appointment commitments
current Resource/Allocation capacity
current merchant Scheduling policy
required provider-derived constraint evidence
current customer/context authority
```

### Required Scheduling result

Immediately before commitment:

```text
Appointment Scheduling Evaluation
    MUST = SCHEDULABLE
```

### Authoritative facts mutated

Successful execution MAY establish only the already accepted effects:

```text
Appointment commitment
required Allocation Claim
Appointment → CustomerContext relationship
handled command/result
committed Appointment fact/event
```

### Atomicity

Where Allocation is an Appointment commitment invariant, Allocation establishment and Appointment commitment MUST share the accepted local consistency boundary.

### Success

Exactly one intended Appointment is established.

### Known Scheduling rejection

```text
NOT_SCHEDULABLE
    → business rejection
    → no Appointment
    → no invariant-required new Allocation Claim
```

### Unresolved Scheduling evidence

```text
UNRESOLVED
    → execution cannot presently establish
      Scheduling eligibility
    → no Appointment
    → no invariant-required new Allocation Claim
```

This outcome MUST remain distinguishable from `NOT_SCHEDULABLE` because caller/retry behaviour differs.

### Technical/provider failure after Appointment commit

A downstream provider/calendar projection failure MUST NOT roll back or reinterpret the known committed Appointment.

---

## 21. Concurrency

Suppose:

```text
14:00 capacity = 1

Customer A reads SCHEDULABLE
Customer B reads SCHEDULABLE
```

Both reads may be truthful at their observation times.

At final commitment:

```text
A ─┐
   ├─► authoritative Scheduling/capacity boundary
B ─┘
```

only the combination allowed by current authoritative constraints may commit.

No cached/request-scoped availability decision may provide a concurrency entitlement.

---

## 22. Booking Negative Boundary

This Scheduling admission contract governs Appointment Scheduling.

It MUST NOT imply:

```text
Booking has dates
        ↓
Booking requires Scheduling
```

A room-category reservation, equipment reservation or other Booking may continue to use:

```text
Booking
+
Resource / Capacity / Allocation
```

without Appointment Scheduling where accepted Booking semantics are sufficient.

Likewise:

```text
Booking appears in Calendar
```

does not transfer Booking authority to Calendar or Scheduling.

A Booking becomes subject to a Scheduling constraint only where separately accepted Booking semantics explicitly establish that dependency.

---

## 23. Outbound External Calendar Projection

After an Appointment commits:

```text
Appointment authoritative commit
        ↓
optional outbound calendar projection
```

The provider projection is post-commit integration work unless a separately accepted contract establishes otherwise.

Failure, retry or uncertainty in that external projection:

```text
MUST NOT
    delete Appointment
    unconfirm Appointment
    rewrite scheduled interval
    manufacture cancellation
```

Existing MS-PROT-041/MS-PROT-069 integration/reconciliation rules remain governing.

---

## 24. Late External Conflict Evidence

Suppose:

```text
Appointment committed legitimately

later external evidence reports
an overlapping external event
```

The later provider evidence does not retroactively make the original Main Street Appointment nonexistent.

It MAY establish reconciliation/merchant-attention evidence under later accepted operational contracts.

It MUST NOT autonomously:

```text
cancel Appointment
reschedule Appointment
alter CustomerContext
rewrite historical Scheduling evaluation
```

Target 19 remains responsible for generic observability/reconciliation completion.

---

## 25. Rescheduling

A supported Appointment reschedule is a new Scheduling decision over the proposed replacement interval.

Before authoritative reschedule:

```text
current applicable Scheduling constraints
        ↓
current Appointment Scheduling Evaluation
        ↓
must be SCHEDULABLE
        ↓
release/replace required Allocation
        +
authoritative Appointment mutation
```

The operation MUST preserve historical evidence sufficient to determine the previously committed interval and governing semantics.

`UNRESOLVED` MUST NOT permit reschedule commitment.

---

## 26. Cancellation

Appointment cancellation does not require a new Scheduling Evaluation merely to establish that the existing Appointment is cancelled.

Cancellation remains governed by:

```text
Appointment authority
merchant policy
customer/actor authority
applicable capacity-release consequences
commercial consequences
```

Released capacity may subsequently affect future Scheduling evaluations.

---

## 27. Canonical Booking Customer Requirement

This amendment establishes the canonical owner-qualified requirement identity:

```text
booking / related-customer-booking
```

It means:

> The current trusted customer context is authoritatively related to the exact Booking being considered within the exact MerchantScope.

Booking owns the Booking → CustomerContext association established as part of its commitment semantics.

MS-PROT-043 remains owner of CustomerContext semantics.

MS-PROT-049 remains owner of Customer Surface Eligibility composition.

MS-PROT-027 remains owner of Exposure resolution.

---

## 28. Canonical Appointment Customer Requirement

This amendment establishes the canonical owner-qualified requirement identity:

```text
appointment / related-customer-appointment
```

It means:

> The current trusted customer context is authoritatively related to the exact Appointment being considered within the exact MerchantScope.

Appointment owns the Appointment → CustomerContext association established as part of its commitment semantics.

No Booking relationship is required merely because the object is an Appointment.

---

## 29. Customer Requirement Satisfaction

For either requirement, satisfaction requires all applicable conditions below.

### Merchant scope

The target Booking/Appointment MUST belong to the current trusted MerchantScope.

### Target relationship

The target commitment MUST carry the applicable authoritative CustomerContext relationship established by its owning capability.

### Trusted customer context

The current customer access context MUST establish either:

```text
A.
trusted access to the exact CustomerContext
related to the target commitment

or

B.
an accepted transaction-specific contextual access
authoritatively scoped to the exact target commitment
and MerchantScope
```

### Current validity

Any session/contextual-access evidence required by the trusted context MUST currently remain valid under its security authority.

If these predicates cannot be established:

```text
requirement = UNSATISFIED
or UNRESOLVED
according to the owning context authority
```

and fail-closed CUSTOMER handling applies under MS-PROT-049/MS-PROT-027.

---

## 30. Things That Do Not Satisfy Customer Relationship

None of the following independently satisfies either requirement:

```text
CustomerAccount authenticated

CustomerContext merely exists

client knows Booking ID

client knows Appointment ID

email values match

phone values match

names match

merchant category

frontend route

previous rendered page

provider account

AI confidence

Exposure result
```

Possession of an identifier is a locator, not customer relationship authority.

---

## 31. Surface Eligibility and Exposure Use

The owner-qualified requirement identities established here MAY be referenced by:

```text
Booking/Appointment CUSTOMER
Surface Eligibility Requirements

and

applicable Booking/Appointment CUSTOMER
Exposure Element Contract requirements
```

The underlying relationship predicate remains capability-owned.

The decisions remain distinct:

```text
Customer Surface Eligibility
        ↓
Projection Serviceability
        ↓
candidate element
        ↓
Exposure
        ↓
customer-visible element
```

Satisfying the relationship requirement does not imply that every Booking/Appointment field is exposable.

---

## 32. Exposure Contract Classification

Target 11 establishes the relationship evaluators required by `MS-PROT-027-V15-DQ-005` for Booking and Appointment.

It does not declare every internal Booking/Appointment field customer-visible.

Any audience-facing Booking/Appointment element MUST either:

```text
resolve an already accepted
owner-qualified Exposure Element Contract

or

receive an explicit owner-qualified
Exposure Element Contract
before production exposure
```

Internal Allocation identifiers, internal notes, release identifiers, provider evidence and other non-customer-safe data MUST NOT inherit Exposure merely because the related commitment is customer-eligible.

---

## 33. CUSTOMER Visibility Is Not Mutation Authority

A customer may legitimately observe an Appointment or Booking while lacking authority for a requested mutation.

Therefore:

```text
related customer requirement satisfied
        ≠
cancel authorised

related customer requirement satisfied
        ≠
reschedule authorised

related customer requirement satisfied
        ≠
refund authorised
```

Every mutation re-enters current:

```text
trusted principal/context
Actor Authorisation
merchant policy
Operational Eligibility
Commercial Entitlement/residual authority where applicable
capability-owned invariants
```

---

## 34. Guest and Transaction-Specific Access

A CustomerAccount is not universally required.

An accepted transaction-specific customer access context MAY satisfy:

```text
booking / related-customer-booking
```

or:

```text
appointment / related-customer-appointment
```

only for the exact commitment and MerchantScope authorised by that context.

This amendment does not define token/signature/credential representation.

That remains owned by the applicable authentication/contextual-access architecture.

A Booking-specific access proof MUST NOT expose an unrelated Appointment, another Booking or another merchant's data.

---

## 35. CustomerContext Reconciliation

CustomerContext reconciliation MUST NOT silently expand these requirements.

If:

```text
C2 RECONCILED_TO C1
```

that fact alone does not establish that customer access through C1 may observe every historical Booking/Appointment associated with C2.

Any such access broadening requires accepted relationship/access authority.

MS-PROT-049 v1.3 remains governing.

---

## 36. Commercial Entitlement and Existing Commitments

Customer relationship truth remains independent of whether new Booking or Appointment activity is commercially enabled.

Loss of Commercial Entitlement:

```text
MUST NOT
    delete historical Booking
    delete historical Appointment
    rewrite CustomerContext relationship
```

Whether an existing commitment retains a customer-facing residual contribution is governed by applicable residual capability/commercial/account authority and MS-PROT-049.

This amendment does not create a new universal customer residual-access guarantee.

---

## 37. Historical Affinity

An Appointment remains historically governed by the semantic/configuration context under which it was committed.

Later changes to:

```text
Scheduling policy
Business Hours
external provider
external calendar connection
merchant configuration
semantic release
```

MUST NOT reinterpret the historical meaning of the Appointment.

A later reschedule or other accepted mutation evaluates current applicable rules for the new mutation while preserving required historical provenance.

Booking historical-affinity rules remain unchanged.

---

## 38. Failure Taxonomy

Target-11 behaviour SHALL preserve at least:

```text
VALIDATION_REJECTION
    malformed/unsupported semantic input

AUTHORISATION_REJECTION
    current principal/context lacks authority

ENTITLEMENT_REJECTION
    applicable commercial access unavailable

NOT_SCHEDULABLE
    sufficient Scheduling evidence proves
    candidate interval invalid

SCHEDULING_EVIDENCE_UNRESOLVED
    required Scheduling evidence cannot
    currently establish schedulability

AUTHORITATIVE_CONFLICT
    concurrent authoritative state changed
    before commitment

PROVIDER / TECHNICAL FAILURE
    infrastructure/provider operation failed

EXECUTION_UNCERTAIN
    mutation outcome cannot safely be determined
```

Physical exception names and HTTP representations remain downstream.

---

## 39. Retry and Idempotency

Appointment Scheduling Evaluation is side-effect-free and MAY be repeated.

A later repeated evaluation MAY legitimately return a different result because authoritative evidence changed.

This is not an idempotency defect.

For `appointment.confirm`, existing MS-PROT-042/MS-PROT-059 rules survive:

```text
same logical command + same intent
    → reconcile to one committed result

same command identity + changed intent
    → reject identity conflict

genuinely new intent
    → may create distinct Appointment
```

A retry after `SCHEDULING_EVIDENCE_UNRESOLVED` may re-evaluate current evidence because no Appointment commitment was established by the unresolved attempt.

---

## 40. Execution Uncertainty

Failure to obtain required provider evidence before Appointment mutation begins is:

```text
Scheduling evidence unresolved
```

not:

```text
Appointment execution uncertain
```

Conversely, if an authoritative mutation has begun and its commit outcome cannot be established, MS-PROT-069 governs execution uncertainty.

The two cases MUST NOT be collapsed because retry safety differs.

---

## 41. Events and Background Work

Appointment Scheduling Evaluation creates no Domain Event merely because availability was checked.

Successful `appointment.confirm` continues to establish its accepted committed Appointment fact/event.

External Calendar projection, refresh and reconciliation MAY use durable background work under MS-PROT-065 and later Target-18 completion.

Background work MUST NOT convert stale availability evidence into commitment authority.

---

## 42. Data Protection and Security

Scheduling evaluation SHALL consume only evidence required for the applicable Scheduling constraint.

External busy-time import normally requires occupancy/busy evidence rather than unnecessary personal event content.

Main Street SHALL NOT require customer-facing exposure of:

```text
external event title
external attendee data
personal calendar description
internal provider identifiers
```

merely to determine busy time.

Customer relationship evaluation likewise SHALL use trusted bounded context rather than contact-value inference.

Detailed retention/erasure completion remains Target 17.

---

## 43. AI Boundary

AI MAY:

```text
help merchant configure supported Scheduling policy

interpret natural-language scheduling intent

explain why an interval is unavailable

explain that required evidence is currently unresolved
```

AI MUST NOT:

```text
convert UNRESOLVED to SCHEDULABLE

invent provider freshness

invent Customer relationship

manufacture capacity

override ScheduleIntent

bypass appointment.confirm

create Booking/Appointment Exposure requirements
outside registered semantics
```

Scheduling admission remains deterministic.

---

## 44. Business-Type Neutrality

This amendment MUST work through capability composition.

### Consultant

```text
Appointment
+
Scheduling
+
optional external busy-time integration
```

No `ConsultantScheduling` model is required.

### Salon

```text
Appointment
+
Scheduling
+
staff/resource capacity where applicable
```

No `SalonSchedule` authority is required.

### Gardener

A merchant may support:

```text
Publication + Enquiry
```

with no Scheduling at all, or independently activate Appointment/Scheduling.

### Motel

A room-category reservation may remain:

```text
Booking
+
Resource/Allocation
```

without Appointment Scheduling.

The merchant category never decides the architecture.

---

## 45. Falsification Record

The proposal was tested against the following cases.

| Scenario | Required result |
|---|---|
| Required external calendar constraint; fresh sufficient evidence says free; all local constraints satisfied | `SCHEDULABLE` |
| Required external busy evidence stale/unverifiable | `UNRESOLVED`; no Appointment commitment |
| External provider connected but busy-time import not applicable | provider outage does not block Scheduling |
| Local Appointment already occupies interval while provider evidence unavailable | `NOT_SCHEDULABLE` because known local rejection is sufficient |
| Provider degraded but exact required evidence remains sufficient | provider label alone does not block; evaluate evidence |
| Provider reports READY but busy evidence itself is insufficient/stale | `UNRESOLVED` |
| Public page previously showed free; another actor takes capacity before commit | final revalidation/claim prevents incompatible Appointment |
| Provider fails after Appointment committed | Appointment survives |
| Late provider event conflicts with committed Appointment | no automatic cancellation/reschedule; reconciliation only |
| Room-category Booking plus external calendar connection | no synthetic Scheduling dependency |
| ScheduleIntent blocks interval | `NOT_SCHEDULABLE` |
| Business Hours prove interval invalid while another evidence source is unresolved | `NOT_SCHEDULABLE` |
| Cross-midnight/DST Business Hours case | resolved by exact Business Hours authority, not UI arithmetic |
| Same appointment command retried after lost acknowledgement | no duplicate Appointment |
| Reschedule | current Scheduling revalidation + correct capacity replacement + history preservation |
| Authenticated CustomerAccount with no Booking relationship | no Booking CUSTOMER eligibility |
| Forged Appointment identifier | no Appointment CUSTOMER eligibility |
| Guest transaction-specific Booking access for exact B1 | may satisfy B1 requirement only |
| CustomerContext reconciliation only | no automatic historical access expansion |
| Salon/consultant/motel comparison | no business-category runtime branching |

No case requires shared mutable ownership or a universal Availability/Calendar aggregate.

---

## 46. Rejected Alternatives

The following models are rejected:

```text
UNKNOWN external evidence = FREE

UNKNOWN external evidence = BUSY
as fabricated business truth

one universal AvailabilityStatus entity

persisted slots as mutation authority

Calendar projection as Scheduling authority

Provider Readiness = Appointment availability

Google connection = mandatory Scheduling dependency

every timed Booking = Appointment/Scheduling

CustomerAccount authentication = customer relationship

resource-ID possession = customer relationship

one generic RELATED_CUSTOMER boolean

Surface-owned relationship graph

frontend eligibility rules

AI-determined slot authority
```

---

## 47. Trade-Offs and Consequences

The fail-closed `UNRESOLVED` result may temporarily prevent new Appointment commitments when a merchant has explicitly made unavailable evidence a required Scheduling constraint.

That is an accepted correctness consequence.

The alternative would permit Main Street to create commitments while knowingly unable to evaluate a merchant-selected blocking constraint.

The architecture avoids that dependency entirely for merchants who do not configure the external constraint.

Therefore the model preserves both:

```text
merchant freedom
+
commitment correctness
```

without making external calendars mandatory platform infrastructure.

The three-result Scheduling evaluation adds one explicit semantic distinction but avoids a substantially more complex generic Availability engine.

---

## 48. Deferred / Future Scope

The following remain downstream unless deliberately promoted:

```text
provider-specific busy-evidence freshness durations

exact provider refresh/poll mechanisms

Calendar UI degraded-state wording/icons

Production API representation of
NOT_SCHEDULABLE versus UNRESOLVED

exact secure guest-access token representation

native/mobile scheduling delivery

generic cached availability projection

waitlists

overbooking

group/multi-resource scheduling expansion

automatic conflict-resolution policy after
late external evidence

bidirectional external-calendar mutation

customer notification wording

observability dashboards/reconciliation UI
```

These deferrals do not permit implementation to treat unresolved required Scheduling evidence as schedulable.

---

## 49. Amendment and Surviving Authority Effect

This amendment does not supersede MS-PROT-042 v1.2–v1.5 wholesale.

The following remain unchanged:

```text
Booking and Appointment are distinct commitments

booking.confirm establishes Booking only

appointment.confirm establishes Appointment only

Scheduling owns Appointment availability constraints

Calendar remains projection/integration

Booking booked subject remains distinct from Allocation

merchant policy remains merchant-controlled
within registered semantics

historical policy affinity survives

Booking residual obligation remains
IN_FORCE / RELEASED within v1.4 scope

channel convergence/idempotency survives
```

This amendment adds only:

```text
deterministic Scheduling evidence outcome semantics

UNRESOLVED commitment-admission rule

provider/evidence distinction for Scheduling

initial request-scoped availability classification

Booking/Appointment owner-qualified
CUSTOMER relationship requirements
```

---

## 50. Conformance / Acceptance Criteria

A conforming implementation MUST demonstrate, where applicable:

```text
[ ] Scheduling evaluation is owner-qualified and merchant-scoped

[ ] Scheduling distinguishes SCHEDULABLE,
    NOT_SCHEDULABLE and UNRESOLVED

[ ] one known rejecting constraint produces
    NOT_SCHEDULABLE even if another constraint is unresolved

[ ] SCHEDULABLE requires sufficient evidence
    for every applicable constraint

[ ] UNRESOLVED never authorises appointment.confirm

[ ] external provider connection alone does not
    make external busy time applicable

[ ] provider readiness does not replace evidence sufficiency

[ ] stale/missing provider evidence is not silently
    treated as current

[ ] no global freshness TTL is invented by Scheduling

[ ] availability reads create no capacity reservation

[ ] final capacity conflict prevents incompatible Appointment

[ ] required Allocation and Appointment preserve atomicity

[ ] Calendar does not become Scheduling authority

[ ] provider failure after commit does not
    invalidate Appointment

[ ] late external conflict evidence does not
    automatically rewrite Appointment

[ ] Booking with time scope does not automatically
    enter Scheduling authority

[ ] booking / related-customer-booking is evaluated
    from trusted Booking/customer-context evidence

[ ] appointment / related-customer-appointment is
    evaluated from trusted Appointment/customer-context evidence

[ ] CustomerAccount authentication alone cannot
    satisfy either relationship

[ ] client-supplied commitment identifiers
    do not establish relationship

[ ] guest contextual access remains bounded to its
    accepted MerchantScope/commitment

[ ] Customer Surface Eligibility remains separate
    from Projection Serviceability and Exposure

[ ] Exposure remains separate from execution authority

[ ] existing commitment history survives
    configuration/provider evolution

[ ] retry after unresolved evidence can re-evaluate
    because no commitment was created

[ ] retry after committed-but-unacknowledged
    appointment does not duplicate commitment

[ ] no business-category-specific scheduling authority exists
```

---

## 51. Target-11 Closure Effect

With this amendment approved and formalised, Target-11 closure review SHALL treat the following as resolved:

```text
Scheduling missing-constraint-evidence semantics

Appointment admission under unresolved evidence

external busy-time degraded-operation boundary

initial Scheduling availability read classification

Booking/Appointment customer relationship
requirement evaluators
```

Remaining choices identified in this amendment are either:

```text
later numbered-target responsibilities

presentation/API decisions

provider-specific implementation contracts

or behaviour-preserving implementation details
```

They do not require implementation to invent Target-11 business semantics.

Subject to successful Authority Index/DDR/Lexicon review and corpus conformance, MS-PROT-079 Target 11 may then be marked:

```text
DESIGN-CLOSED
```

and Target 12 — Ordering / Inventory may become the current active design target.

---

## 52. Governing Principle

> **Main Street shall commit an Appointment only when Scheduling can positively establish, from sufficient current evidence, that every applicable Scheduling constraint permits the candidate interval and every invariant-required capacity claim can be established. A known constraint violation is NOT_SCHEDULABLE; missing or unverifiable required constraint evidence is UNRESOLVED and cannot authorise commitment. Calendar and external providers supply projection/evidence rather than Scheduling authority. Booking remains independent unless its own accepted semantics explicitly require Scheduling. Customer-specific Booking and Appointment surfaces rely on owner-qualified trusted relationship requirements rather than authentication, identifiers, presentation or inference.**

---

## Governance Assessment

```text
DESIGN / PROPOSE:
    COMPLETE

AUTHORITY TRACE:
    COMPLETE

IMPLEMENTATION-EVIDENCE REVIEW:
    COMPLETE

OWNERSHIP REVIEW:
    PASS

CROSS-CAPABILITY REVIEW:
    PASS

FALSIFICATION:
    PASS

BUSINESS-TYPE NEUTRALITY:
    PASS

PROVIDER BOUNDARY:
    PASS

PROJECTION / EXPOSURE BOUNDARY:
    PASS

AMBIGUITY REVIEW:
    PASS within Target-11 scope

RECOMMENDATION:
    ACCEPT

MANUAL APPROVAL:
    GRANTED on 27 August 2026

STATUS:
    ACCEPTED
```
