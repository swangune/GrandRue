# MS-PROT-081 — Workforce Scheduling, Timekeeping, Break & Leave Model

**Document ID:** MS-PROT-081  
**Version:** 1.0  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Scheduling, Timekeeping, Leave & Compensation authority package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic / application-architecture authority  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Purpose:** Establish workforce scheduling, timekeeping, scheduled-break, leave/holiday and personal workforce participation semantics while preserving independent workforce-access, Appointment Scheduling and Workforce Compensation/Payroll ownership.

---

# 1. Governing Decision

Main Street SHALL support a generic **Workforce Scheduling, Timekeeping and Leave** capability for merchants whose people perform work according to planned shifts, accepted work offers, recorded attendance or approved leave.

The capability SHALL support materially different workforce arrangements without inferring legal employment status.

Canonical flow:

```text
Merchant Membership
        +
Workforce Time Terms Revision
        ↓
Workforce Scheduling
        ↓
Scheduled Work Commitment
        ↓
Timekeeping / Attendance
        ↓
Approved Worked-Time Evidence
        ─────────────┐
                     │
Approved Leave Evidence
        ─────────────┤
                     ↓
          Workforce Compensation
                     ↓
          Payroll / Non-Payroll
```

Workforce Scheduling SHALL NOT become Appointment Scheduling, Payroll, employment-law authority, HR authority or generic Calendar authority.

---

# 2. Product Boundary

Main Street SHALL enable supported merchants to:

```text
schedule staff
offer shifts for acceptance
allow permitted staff to accept/reject work
publish personal work schedules
record clock-in/out
record break start/end
track attendance
correct missed/incorrect clock events
approve worked time
allow staff to request/book leave
approve/reject leave
prevent ordinary schedule/leave conflicts
provide own-schedule/time/leave self-service
supply approved evidence to Workforce Compensation
```

The capability SHALL NOT determine statutory pay calculations.

---

# 3. Semantic Ownership

Workforce Scheduling/Timekeeping/Leave owns:

```text
WorkforceTimeTermsRevision
ShiftOffer
ShiftOfferDecision
ScheduledWorkCommitment
ScheduledBreak
TimeCaptureEvent
TimeCorrectionEvidence
Attendance/Worked-Time derivation
ApprovedWorkedTimeEvidence
LeaveRequest
LeaveDecision
ApprovedLeaveEvidence
worker-specific schedule projection
worker-specific time projection
worker-specific leave projection
```

It does NOT own:

```text
Identity
Merchant Membership
Merchant Controller
Role Assignment
Compensation Relationship
Compensation Terms
Payroll calculation
Jurisdiction Pay Treatment
Appointment
Booking
customer-facing Resource availability
professional licences
bank/payment truth
```

---

# 4. Workforce Subject

A workforce schedule SHALL refer to one exact Merchant Membership.

Therefore:

```text
Merchant Membership
    enables merchant-scoped workforce participation

Merchant Membership
    ≠ employee
    ≠ contractor
    ≠ Payroll Participant
    ≠ Compensation Relationship
```

A person who requires scheduling/self-service MAY hold a Merchant Membership without receiving merchant-operational privileges.

A project contractor who does not require workforce scheduling MAY have a Compensation Relationship without a Merchant Membership.

---

# 5. Workforce Time Terms Revision

A **Workforce Time Terms Revision** is an effective-dated authoritative Main Street record describing the work-time rules applicable to one Merchant Membership.

It MAY establish, where relevant:

```text
normal/expected working pattern
guaranteed or non-guaranteed hours
whether merchant may directly assign shifts
whether offered shifts require acceptance
break scheduling rules
leave-request rules
applicable scheduling constraints
other registered workforce-time terms
```

It MUST NOT itself establish:

```text
employment-law classification
PAYROLL
NON_PAYROLL
hourly/salary compensation
holiday-pay amount
tax treatment
```

Those remain separately governed.

---

# 6. Agreement Affinity

A Main Street-generated or externally evidenced agreement MAY establish both:

```text
Compensation Terms Revision
+
Workforce Time Terms Revision
```

where the accepted terms contain both compensation and working-time provisions.

The agreement document itself SHALL NOT become runtime schedule authority.

Conceptually:

```text
accepted agreement
       ↓
orchestration
   ┌──────┴──────┐
   ↓             ↓
Compensation    Workforce
Terms           Time Terms
```

Material changes SHALL create new effective-dated revisions rather than rewriting historical terms.

---

# 7. Work Allocation Modes

Main Street SHALL support at least two generic work-allocation mechanisms.

## 7.1 Direct Assignment

Where current Workforce Time Terms permit direct assignment:

```text
authorised merchant actor
        ↓
creates shift
        ↓
ScheduledWorkCommitment
```

Worker acceptance is not required merely by Main Street.

## 7.2 Offer / Acceptance

Where current Workforce Time Terms require worker acceptance:

```text
merchant creates ShiftOffer
        ↓
worker observes own offer
        ↓
ACCEPT
    → ScheduledWorkCommitment

REJECT
    → no ScheduledWorkCommitment
```

The distinction MUST NOT be inferred from Payroll treatment.

Therefore:

```text
PAYROLL
    ≠ direct assignment

NON_PAYROLL
    ≠ acceptance required
```

A Payroll relationship may use shift acceptance.

A Non-Payroll relationship may permit binding direct assignment where legally/contractually valid.

---

# 8. Shift Offer Identity and Retry

Every Shift Offer MUST have stable logical identity.

Duplicate delivery or repeated acceptance of the same offer MUST NOT create multiple Scheduled Work Commitments.

Acceptance MUST refer to the exact offered revision.

---

# 9. Scheduled Work Commitment

A **Scheduled Work Commitment** is the authoritative merchant-scoped fact that one Merchant Membership is scheduled to perform work over an explicit interval under one exact shift revision.

It MUST identify or reference:

```text
Merchant Scope
Merchant Membership
shift identity
shift revision
scheduled start
scheduled end
Scheduled Breaks
applicable Workforce Time Terms Revision
source mechanism:
    direct assignment
    or accepted offer
provenance
```

A Scheduled Work Commitment is not proof that work occurred.

---

# 10. Material Shift Change

A material shift change MUST create a new shift revision.

Material changes include at least:

```text
start time
end time
work date
material location/assignment where governed
scheduled-break structure
other acceptance-material terms
```

Where worker acceptance was required for the previous revision, a material amendment MUST NOT silently reuse that acceptance unless the applicable Workforce Time Terms explicitly make that class of change non-material.

---

# 11. Shift Cancellation

Cancellation SHALL be explicit.

Cancelling a shift:

```text
ends future expectation
```

but MUST NOT erase:

```text
original commitment
acceptance evidence
prior attendance evidence
prior communication evidence
```

---

# 12. Scheduled Break Is Part of the Shift

A **Scheduled Break** SHALL be part of the applicable Scheduled Work Commitment revision.

Conceptually:

```text
Shift
08:00 ───────────────────────── 17:00
                 │
                 └── Scheduled Break
                     12:00–12:30
```

Therefore:

```text
Scheduled Break
    is scheduling truth
```

not an independent Payroll object.

A Scheduled Break MAY carry:

```text
planned start/end
or permitted break window
scheduled duration
```

as required by the applicable Workforce Time Terms.

---

# 13. Scheduled Break Does Not Prove Break Taken

Hard distinction:

```text
Scheduled Break
    ≠
Actual Break
```

The existence of a 30-minute scheduled break MUST NOT by itself establish that the person stopped work for 30 minutes.

Actual break evidence derives from Timekeeping or another accepted attendance mechanism.

---

# 14. Time Capture Events

Main Street SHALL support immutable attributable Time Capture Events including:

```text
CLOCK_IN
BREAK_START
BREAK_END
CLOCK_OUT
```

where applicable.

Every event MUST retain:

```text
Merchant Scope
Merchant Membership
event type
trusted/effective timestamp
recordedAt
capture mechanism/source
applicable shift reference where established
principal/provenance
```

Client device time MAY be retained as evidence but MUST NOT automatically become authoritative event time.

---

# 15. Basic Time-Capture Sequence

For an ordinary shift:

```text
NOT CLOCKED IN
      ↓ CLOCK_IN
WORKING
      ↓ BREAK_START
ON BREAK
      ↓ BREAK_END
WORKING
      ↓ CLOCK_OUT
COMPLETED ATTENDANCE
```

Multiple breaks MAY be supported where applicable.

Invalid event sequences MUST NOT silently manufacture valid attendance.

---

# 16. Clocking Does Not Create a Shift

Hard invariant:

```text
CLOCK_IN
    ≠ ScheduledWorkCommitment
```

If a worker clocks without an applicable scheduled commitment, the active merchant policy MAY:

```text
reject the event
or
record unmatched attendance evidence for review
```

but Main Street MUST NOT silently fabricate a scheduled shift.

Exact unscheduled-work policy remains merchant/jurisdiction qualified.

---

# 17. Attendance and Worked Time

Main Street SHALL derive attendance/worked-time evidence from accepted Time Capture Events and applicable corrections.

Conceptually:

```text
CLOCK_IN
    ↓
work interval
    ↓
BREAK_START
    ↓
break interval
    ↓
BREAK_END
    ↓
work interval
    ↓
CLOCK_OUT
```

Worked Time excludes actual break intervals.

---

# 18. Actual Break Time

Actual Break Time SHALL derive from accepted break-start/end evidence.

Therefore:

```text
Actual Break Time
    ≠ Scheduled Break Duration
```

Example:

```text
Scheduled break: 30 min
Actual break:    45 min
```

Main Street MUST retain the 45-minute actual break evidence.

---

# 19. Excess Break Time

Where actual break duration exceeds the applicable scheduled break allowance:

```text
Actual Break Time
-
Scheduled Break Allowance
=
Excess Break Time
```

subject to the exact Workforce Time Terms applicable to that shift.

Hard invariant:

> **Excess Break Time MUST NOT be represented as Worked Time.**

---

# 20. Excess Break and Time-Based Pay

Where Compensation Terms calculate compensation from Worked Time:

> **Excess Break Time MUST NOT contribute to the time-based Compensation Amount.**

Example:

```text
Shift span               9h00
Actual break             0h45
Actual Worked Time       8h15
```

If compensation is based on Approved Worked Time, the additional 15 minutes beyond a scheduled 30-minute break is not payable worked time.

---

# 21. Scheduled Break Pay Treatment Is Separate

Main Street MUST preserve:

```text
Scheduled Break
        ≠
Actual Break
        ≠
Worked Time
        ≠
Paid Break Entitlement
```

A scheduled break MAY be:

```text
paid
unpaid
or subject to another jurisdiction/terms-qualified treatment
```

Compensation authority owns the monetary consequence.

Example:

```text
Actual Worked Time             8h15
Paid scheduled-break entitlement 0h30
Excess break                     0h15
```

A time-based compensation calculation MAY therefore recognise:

```text
8h15 worked
+
0h30 paid-break entitlement
```

while excluding the excess 15 minutes.

Paid Break Time does not become Worked Time merely because compensation is due.

---

# 22. Salaried Staff

Excess Break Time MUST NOT automatically reduce fixed/salaried compensation.

For salaried arrangements:

```text
Excess Break
    → attendance variance
```

Any monetary consequence MUST derive from applicable Compensation Terms and jurisdiction rules.

Workforce Timekeeping MUST NOT invent salary deductions.

---

# 23. Shorter-Than-Scheduled Break

If a person takes less break than scheduled:

```text
actual break evidence
```

rather than scheduled duration determines actual Worked Time.

However, Main Street MAY separately identify:

```text
break-compliance variance
```

where relevant.

Working through part/all of a required break MUST NOT itself authorise unlawful or non-compliant scheduling.

Compliance treatment remains jurisdiction-qualified.

---

# 24. Worked-Time Approval

Main Street SHALL distinguish:

```text
Recorded Worked-Time Evidence
        ≠
Approved Worked-Time Evidence
```

An authorised merchant actor MAY approve, reject or correct the relevant period according to merchant policy and applicable authority.

Worker self-recording MUST NOT automatically equal merchant approval unless an accepted policy explicitly permits self-attestation.

---

# 25. Time Corrections

Missing or incorrect clock events MUST be correctable without rewriting historical capture evidence.

Example:

```text
original:
08:00 CLOCK_IN
12:00 BREAK_START
[missing BREAK_END]
17:00 CLOCK_OUT

correction:
BREAK_END should be 12:30
```

Main Street MUST preserve:

```text
original events
+
correction evidence
+
who proposed correction
+
who approved it where required
+
correction time
+
resulting effective worked-time derivation
```

The original evidence MUST NOT be destructively edited.

---

# 26. Time Approval and Compensation

Approved Worked-Time Evidence MAY be consumed by Workforce Compensation.

Canonical boundary:

```text
Timekeeping
    owns:
        what time was worked/approved

Workforce Compensation
    owns:
        monetary consequence
```

Therefore:

```text
Approved Worked Time
        ×
Compensation Terms
        ↓
Compensation Amount
```

where applicable.

---

# 27. Leave / Holiday

Main Street SHALL support generic **Leave / Time Off** semantics.

Merchant-facing terminology MAY use:

```text
Holiday
Annual Leave
Time Off
```

as appropriate.

Generic authority MUST NOT assume every jurisdiction uses identical legal categories.

---

# 28. Leave Semantic Separation

Main Street MUST distinguish:

```text
Leave entitlement/rule evidence
        ≠
Leave Request
        ≠
Leave Approval
        ≠
Approved Leave Interval
        ≠
Leave Taken
        ≠
Leave Pay
```

Leave does not own statutory/payroll calculation.

---

# 29. Leave Request

An eligible staff member MAY submit a Leave Request through Personal Workforce Self-Service.

The request SHALL identify:

```text
Merchant Scope
Merchant Membership
requested interval/date(s)
applicable leave category where required
request provenance
```

Submitting a request does not create approved leave.

---

# 30. Leave Decision

An actor with applicable merchant authority MAY:

```text
APPROVE
REJECT
```

a pending Leave Request.

The worker MAY withdraw a pending request where applicable.

Approved leave MUST preserve exact decision provenance.

---

# 31. Schedule Conflict With Approved Leave

Approved Leave SHALL become authoritative workforce-availability evidence.

Ordinary Workforce Scheduling MUST NOT create a conflicting new Scheduled Work Commitment for the same worker unless an explicitly accepted override mechanism permits that result.

Unknown or unresolved leave status MUST NOT be silently treated as approved leave.

---

# 32. Existing Shift Versus New Leave Approval

Main Street MUST NOT permit an Approved Leave decision to silently coexist with an unresolved conflicting Scheduled Work Commitment.

Where requested leave overlaps an existing work commitment, leave approval MUST:

```text
resolve the conflicting work commitment according to applicable policy/terms
or
remain uncommitted/rejected until the conflict is resolved
```

The exact permissible cancellation/amendment consequence remains governed by Workforce Time Terms and applicable jurisdiction policy.

No hidden shift deletion is permitted.

---

# 33. Leave Pay Is Separate

Approved Leave does not itself calculate pay.

Canonical boundary:

```text
Approved Leave Evidence
        +
Compensation Relationship
        +
Compensation Terms
        +
jurisdiction-qualified rules
        ↓
Workforce Compensation
        ↓
leave-pay consequence
```

---

# 34. Payroll Does Not Determine Leave Eligibility

Hard distinction:

```text
PAYROLL
    ≠ automatically leave-eligible

NON_PAYROLL
    ≠ automatically leave-ineligible
```

Leave eligibility derives from the applicable relationship facts, Workforce Time Terms and jurisdiction.

Payroll is an execution route, not leave-entitlement authority.

---

# 35. Paid and Unpaid Leave

Approved Leave MAY produce:

```text
paid compensation
no additional compensation because fixed salary already covers it
unpaid absence
other jurisdiction-qualified consequence
```

Workforce Leave supplies the authoritative absence evidence.

Workforce Compensation determines the monetary consequence.

---

# 36. Personal Workforce Schedule Projection

Main Street SHALL provide a purpose-bound personal projection for a staff member's own workforce information.

It MAY include:

```text
own upcoming shifts
own shift offers
own accepted/rejected shift history
own scheduled breaks
own clocking status
own time history
own time corrections
own leave requests
own approved leave
own leave balance/entitlement projection where supported
own compensation/pay documents where separately permitted
```

It MUST NOT include merchant-wide workforce information merely because the person is a staff member.

---

# 37. Merchant Workforce Management Projection

Managers/authorised merchant actors MAY receive separately authorised merchant-operational projections including:

```text
team schedule
shift creation
shift amendment/cancellation
time approval
leave approval
attendance exceptions
```

This remains merchant operational information.

---

# 38. Personal Clocking

A merchant MAY permit staff to record:

```text
CLOCK_IN
BREAK_START
BREAK_END
CLOCK_OUT
```

from an authenticated Personal Workforce Self-Service Context.

A merchant MAY instead require another accepted capture mechanism.

The generic authority MUST NOT mandate:

```text
GPS
QR
NFC
shared terminal
personal phone
biometric mechanism
```

as universal.

---

# 39. Presence Evidence

Where a merchant/jurisdiction legitimately requires additional presence evidence, Main Street MAY consume an accepted configured mechanism.

Possession of:

```text
GPS
QR scan
NFC tap
authorised phone
```

MUST NOT independently prove that work occurred.

Such evidence contributes only according to its accepted contract.

---

# 40. Scheduling Notification Boundary

Workforce Scheduling MAY create Notification Intents for events such as:

```text
new shift offer
shift amended
shift cancelled
leave approved/rejected
time correction requires action
```

Notification content destined for a staff personal device MUST contain only information permitted by the Personal Workforce Self-Service boundary.

Notifications MUST NOT use a shift notification as a path to expose unrelated merchant operational data.

MS-PROT-075 remains the cross-cutting notification authority.

---

# 41. Historical Evidence

Workforce Scheduling/Timekeeping/Leave MUST preserve history for:

```text
shift offers
accept/reject decisions
shift revisions
work commitments
cancellations
Scheduled Breaks
Time Capture Events
corrections
worked-time approvals
leave requests
leave decisions
approved leave
```

Termination of Merchant Membership MUST NOT erase this history.

---

# 42. Membership End

After Merchant Membership becomes ENDED:

```text
new workforce scheduling participation
new clocking
new leave requests
```

MUST NOT be accepted under that ended membership.

Historical schedules/time/leave evidence remains attributable.

A later rejoin creates a new Merchant Membership and new workforce-time relationship.

---

# 43. Concurrency

Main Street MUST protect at least:

```text
accept/reject same Shift Offer
material shift amendment versus worker acceptance
leave approval versus conflicting shift creation
time approval versus time correction
membership termination versus new workforce action
```

from stale concurrent commits.

Exact persistence/locking implementation is downstream.

---

# 44. Idempotency

Logical retries of:

```text
shift acceptance
shift rejection
leave request
leave decision
time correction submission
time approval
```

MUST NOT create repeated semantic effect.

---

# 45. Failure Semantics

Main Street SHALL distinguish:

```text
VALIDATION REJECTION
    malformed/invalid time or schedule input

AUTHORISATION REJECTION
    actor lacks authority

MEMBERSHIP INELIGIBLE
    membership inactive/ended

SHIFT CONFLICT
    conflicting work/leave commitment

ACCEPTANCE REQUIRED
    offered work not yet accepted

TIME EVIDENCE INCOMPLETE
    cannot establish reliable worked time

TIME APPROVAL PENDING
    evidence exists but merchant approval missing

LEAVE DECISION PENDING
    leave requested but not approved

OPERATIONAL FAILURE
    dependency unavailable
```

One generic `FAILED` state MUST NOT erase these distinctions.

---

# 46. Explicit Non-Goals

MS-PROT-081 SHALL NOT define:

```text
universal employment contract
performance reviews
recruitment
disciplinary procedures
general HR records
universal statutory working-time law
universal overtime law
Payroll calculation
tax
pension
customer Appointment semantics
customer Booking semantics
project-management truth
professional licensing
native GPS tracking architecture
biometric attendance
shift marketplace
advanced shift swaps
AI-generated staffing optimisation
```

---

# 47. Cross-Authority Invariants

This accepted package establishes:

```text
Merchant Membership
    ≠ Compensation Relationship

Workforce Time Terms
    ≠ Compensation Terms

Workforce Scheduling
    ≠ Appointment Scheduling

Shift Offer
    ≠ Scheduled Work Commitment

Scheduled Work Commitment
    ≠ Attendance

Scheduled Break
    ≠ Actual Break

Actual Break
    ≠ Worked Time

Paid Break
    ≠ Worked Time

Excess Break
    ≠ Worked Time

Worked Time
    ≠ Compensation Amount

Approved Leave
    ≠ Leave Pay

Payroll
    ≠ Leave Eligibility

Personal Workforce Self-Service
    ≠ Staff Operational Context

Merchant Membership
    ≠ customer-facing schedulable Resource

Calendar presentation
    ≠ workforce scheduling authority
```

---

# 48. Staff Experience

A staff member's personal Main Street surface SHOULD conceptually provide:

```text
My Schedule
My Time
My Leave
My Pay & Documents
```

## My Schedule

```text
upcoming committed shifts
offered shifts
scheduled breaks
shift changes
```

## My Time

```text
clock in
start break
end break
clock out
timecard
time corrections
```

## My Leave

```text
request/book holiday
view pending requests
view approved/rejected requests
view supported entitlement/balance projection
```

## My Pay & Documents

As permitted by Workforce Compensation:

```text
own payslips
own payment statements
own permitted compensation history/documents
```

These are purpose-bound self-service projections, not a full merchant dashboard.

---

# 49. Merchant Experience

An authorised merchant/manager workforce surface SHOULD support:

```text
team rota/schedule
create shifts
offer or assign shifts
amend/cancel shifts
attendance exceptions
time approval
leave requests
leave approval/rejection
workforce conflicts
```

Exact UX remains downstream.

---

# 50. Falsification

## 50.1 Salaried Employee With Mandatory Shifts

```text
salary
directly assigned Monday–Friday shifts
clocking required
30-minute break
paid annual leave
```

PASS.

Clocking records attendance.

Excess breaks produce attendance variance.

Salary is not automatically reduced.

Approved paid leave flows into compensation according to jurisdiction/terms.

## 50.2 Hourly Payroll Worker With Mandatory Shifts

```text
£15/hour
direct assignment
8h scheduled
30m break
45m actual break
```

PASS.

```text
7h15 worked
```

plus any applicable paid-break entitlement is the compensation basis.

Excess 15 minutes is not time-based pay.

## 50.3 Zero-Guaranteed-Hours Payroll Worker

```text
PAYROLL treatment
no guaranteed hours
merchant offers shifts
worker may accept/reject
```

PASS.

Payroll does not force direct-assignment scheduling.

## 50.4 Self-Employed / Non-Payroll Partner

```text
NON_PAYROLL
shift offered
accept/reject permitted
paid from Approved Worked Time
```

PASS.

Scheduling treatment and pay treatment remain independent.

## 50.5 Contractor With Binding Work Commitment

```text
NON_PAYROLL
contract permits directly assigned agreed shifts
```

PASS.

Non-Payroll does not universally imply voluntary shift acceptance.

## 50.6 Project Contractor Without Staff Schedule

```text
Compensation Relationship
project milestone payments
no Merchant Membership
no shift schedule
```

PASS.

Workforce Scheduling is not mandatory for all Payees.

## 50.7 Scheduled Break Overrun

```text
Scheduled: 30m
Actual:    45m
```

PASS.

Actual break = 45m.

Excess = 15m.

Excess is not Worked Time and does not contribute to hourly/time-based pay.

## 50.8 Paid Break

```text
30m scheduled paid break
45m actual break
```

PASS.

Applicable 30m Paid Break Entitlement may be compensated.

Extra 15m is not Worked Time and is not paid through the scheduled paid-break entitlement.

## 50.9 Worker Skips Required Break

PASS with separation.

Actual work evidence is preserved.

Potential legal/compliance breach is not hidden.

Scheduling/payroll cannot use the fact that work occurred to declare the missed break legally acceptable.

## 50.10 Holiday Over Existing Shift

PASS only with explicit conflict resolution.

Leave approval cannot silently produce contradictory:

```text
approved holiday
+
active work commitment
```

for the same interval.

## 50.11 Holiday Over Existing Customer Appointment

PASS.

Approved workforce leave does not silently cancel customer Appointment truth.

Conflict/remediation is surfaced.

## 50.12 Missed Clock-Out

PASS.

Correction evidence is added.

Original capture history remains immutable.

## 50.13 Personal Phone

Staff:

```text
views own rota
accepts shift
clocks in
books holiday
views own payslip
```

PASS under Personal Workforce Self-Service Context.

The same phone cannot thereby:

```text
view customers
manage staff
approve someone else's leave
see merchant reports
```

without separately satisfying Staff Operational Context.

## 50.14 Manager Attempts Approval From Personal Self-Service Context

REJECTED.

Approving another person's time/leave is merchant operational work.

Operational device/authority requirements remain applicable.

## 50.15 Staff Leaves Business

PASS.

Ended Membership blocks future self-service/work scheduling while retaining all historical shift/time/leave/pay evidence.

---

# 51. Rejected Alternatives

The following are rejected:

```text
one universal Scheduling aggregate

expanding Appointment Scheduling into staff rota

Payroll owning clock-in/out

Payroll owning holiday approval

Schedule automatically creating pay

scheduled hours automatically equal worked hours

scheduled break automatically equal actual break

deducting scheduled break regardless of actual evidence

paying excess break for time-based compensation

automatic salary deduction for break overrun

Payroll status deciding shift acceptance rights

Payroll status deciding leave eligibility

contractor globally meaning accept/reject shifts

employee globally meaning mandatory shifts

personal phone becoming merchant-operational device merely because staff uses rota

merchant-wide staff data in personal self-service

generated contract/PDF becoming live scheduling state

leave approval silently cancelling customer commitments
```

---

# 52. Trade-Off Decisions

## 52.1 Universal Scheduler vs Separate Scheduling Authorities

**Rejected:** one generic scheduler for customers, staff and every resource.

**Chosen:** Workforce Scheduling and Appointment Scheduling remain distinct but exchange owner-qualified constraint evidence.

**Cost:** additional cross-capability contract.

**Benefit:** avoids semantic gravity and preserves commitment ownership.

## 52.2 Payroll-Owned Time vs Independent Timekeeping

**Rejected:** Payroll owns timesheets/clocking.

**Chosen:** Timekeeping owns actual-work evidence; Compensation consumes it.

**Cost:** explicit handoff.

**Benefit:** salaried, hourly, Payroll and Non-Payroll arrangements all work without corrupting attendance semantics.

## 52.3 Operational Phone Access vs Purpose-Bound Personal Self-Service

**Rejected:** allow staff merchant-dashboard access from any authenticated private phone.

**Chosen:** Personal Workforce Self-Service Context.

**Cost:** one additional trusted-context class.

**Benefit:** useful staff mobile experience without weakening operational-device security.

## 52.4 Leave as Payroll State vs Independent Leave Evidence

**Rejected:** holiday exists only inside Payroll.

**Chosen:** Leave owns absence/approval; Compensation owns monetary consequence.

**Benefit:** supports Payroll, salary, hourly and jurisdiction-specific leave arrangements correctly.

---

# 53. Ambiguity Review

## 53.1 Staff

`Staff` remains merchant-facing terminology over Merchant Membership/workforce participation.

It is not a universal legal employment category.

## 53.2 Schedule

Unqualified `Schedule` MUST NOT be used where ownership is material.

Use:

```text
Workforce Scheduling
Appointment Scheduling
Scheduled Work Commitment
```

as applicable.

## 53.3 Hours

The following MUST NOT be treated as synonyms:

```text
scheduled hours
clocked span
actual break time
Worked Time
Approved Worked Time
compensable paid-break time
paid leave time
```

## 53.4 Holiday

Merchant-facing `Holiday` may map to an applicable supported Leave category.

The generic architecture uses Leave/Time Off and does not assume identical jurisdiction semantics.

## 53.5 Paid Time

Paid time MAY arise from:

```text
Worked Time
Paid Break Entitlement
Paid Leave
other Compensation Terms
```

Therefore paid time is not synonymous with Worked Time.

---

# 54. Deferred Questions

The following are deliberately deferred:

```text
MS-PROT-081-DQ-001
Exact Java/persistence representation.

MS-PROT-081-DQ-002
Exact shift-swap / shift-cover workflow.

MS-PROT-081-DQ-003
Exact overtime authority and jurisdiction rules.

MS-PROT-081-DQ-004
Exact minimum/maximum working-time compliance rules by jurisdiction.

MS-PROT-081-DQ-005
Exact break-compliance rules by jurisdiction.

MS-PROT-081-DQ-006
Exact leave category/entitlement formulas by jurisdiction.

MS-PROT-081-DQ-007
Exact holiday-pay calculation rules — owned with MS-PROT-080 jurisdiction implementation.

MS-PROT-081-DQ-008
Exact GPS/QR/NFC/shared-terminal attendance mechanisms.

MS-PROT-081-DQ-009
Exact anti-time-theft/fraud evidence policy.

MS-PROT-081-DQ-010
Exact time-rounding rules.

MS-PROT-081-DQ-011
Exact manager time-approval policy and optional self-attestation rules.

MS-PROT-081-DQ-012
Exact unscheduled-work handling.

MS-PROT-081-DQ-013
Exact staff personal-surface API/UI.

MS-PROT-081-DQ-014
Exact leave-balance projection representation.

MS-PROT-081-DQ-015
Exact notification contracts for shift/leave/time actions.

MS-PROT-081-DQ-016
Exact linkage representation between Merchant Membership and customer-facing Resource.

MS-PROT-081-DQ-017
Exact remediation workflow when leave/workforce changes conflict with committed Appointments.

MS-PROT-081-DQ-018
Exact legal/commercial treatment of shift cancellation after acceptance.

MS-PROT-081-DQ-019
Exact native-mobile/offline workforce support.

MS-PROT-081-DQ-020
Commercial entitlement/tier packaging.
```

Implementation MUST NOT invent these where the distinction becomes material.

---

# 55. Implementation Readiness

Approval of this package establishes semantic authority only.

It does NOT immediately authorise production implementation.

Before implementation:

```text
accepted authority package
        ↓
implementation-programme impact review
        ↓
determine sequencing
        ↓
jurisdiction requirements where applicable
        ↓
security/privacy review
        ↓
API/Projection/Exposure contract review
        ↓
tests-first implementation
```

Existing implementation priorities remain unchanged unless explicitly reprioritised.

---

# 56. Final Decision

> **Main Street shall support workforce scheduling as an independent merchant capability rather than treating staff rota as Appointment Scheduling or Payroll. A staff member's applicable Workforce Time Terms determine whether work may be directly assigned or must first be offered and accepted.**

> **A Scheduled Work Commitment contains the planned working interval and its Scheduled Breaks. Timekeeping independently records actual clock-in, break-start, break-end and clock-out evidence. Actual break time is not Worked Time, and break time exceeding the applicable scheduled allowance is Excess Break Time. Excess Break Time shall not contribute to time-based compensation. Paid scheduled breaks, where applicable, remain separate compensation entitlements and do not become Worked Time.**

> **Leave/Holiday is independently requested, approved and scheduled. Approved Leave constrains workforce availability but does not itself calculate holiday pay. Workforce Compensation consumes approved leave evidence and determines the applicable paid, unpaid or salary-continuation consequence under the Compensation Terms and supported jurisdiction rules.**

> **Staff shall be able to use a bounded Personal Workforce Self-Service surface on their own devices to view their schedule, accept/reject offered shifts, clock in/out and record breaks, review their own time, request leave and access permitted own pay documents. This personal surface shall not grant general merchant-operational access or authority over other staff.**

> **Where a Merchant Membership is explicitly linked to a customer-facing schedulable Resource, Appointment Scheduling may consume authoritative workforce schedule and approved-leave evidence as Scheduling Constraint Evidence. Workforce Scheduling shall not own Appointment commitment truth, and later workforce changes shall not silently cancel existing customer commitments.**

> **Workforce Scheduling determines when work is expected. Timekeeping determines what work actually occurred. Leave determines approved absence. Workforce Compensation determines the monetary consequence. Payroll executes the applicable employer-payroll treatment. These authorities remain separate and compose through explicit evidence.**

---

## Recommendation / Approval Record

**RECOMMENDATION: ACCEPT**

**Manual approval of this exact authority package:** GRANTED on 5 September 2026.

### End of accepted MS-PROT-081 v1.0
